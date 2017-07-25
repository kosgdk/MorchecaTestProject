package morcheca.controllers;

import morcheca.entities.User;
import morcheca.services.interfaces.UserService;
import morcheca.validators.CustomUserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static morcheca.security.UserNameCachingAuthenticationFailureHandler.LAST_USERNAME_KEY;
import static morcheca.security.UserNameCachingAuthenticationFailureHandler.LOGIN_FAIL_KEY;


@Controller
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    Validator validator;

    @Autowired
    CustomUserValidator customUserValidator;

    @Autowired
    UserService userService;

    @Autowired
    private MessageSource messageSource;

    private final String NEW_USER = "newUser";
    private final String EDIT_USER = "editUser";
    private final String MESSAGE = "message";


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.addValidators(customUserValidator);
    }


    @RequestMapping(value = "error")
    public String errorHandler(Model model, Locale locale) {
        String message = messageSource.getMessage("standardErrorMessage", null, null, locale);
        model.addAttribute(MESSAGE, message);
        return "error";
    }



    // Login page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String pageLogin(Model model, Locale locale, HttpServletRequest request) {
        logger.debug("inside pageLogin()");

        if (isLoggedIn()) return "redirect:/home";

        boolean loginFail = Collections.list(request.getSession().getAttributeNames()).contains(LOGIN_FAIL_KEY);
        if (loginFail) {
            String message = messageSource.getMessage("login.fail", null, null, locale);
            String name = (String) request.getSession().getAttribute(LAST_USERNAME_KEY);
            request.getSession().removeAttribute(LOGIN_FAIL_KEY);
            model.addAttribute(MESSAGE, message)
                    .addAttribute("name", name);
        }

        if (!model.containsAttribute(NEW_USER)) {
            model.addAttribute(NEW_USER, new User());
        }

        return "login";
    }


    // Home page
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String pageHome(Model model) {
        logger.debug("inside pageHome()");

        model.addAttribute("users", userService.getAll())
                .addAttribute("loggedInUser", loggedInUser());

        if (!model.containsAttribute(NEW_USER)) {
            model.addAttribute(NEW_USER, new User());
        }

        return "home";
    }


    // Create user
    @RequestMapping(value = "/create_user", method = RequestMethod.POST)
    public String createUser(@Valid @ModelAttribute User user, Errors errors,
                             @RequestParam(name = "register", required = false) boolean register,
                             RedirectAttributes redirectAttributes) {
        logger.debug("inside createUser()");

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute(NEW_USER, user);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + NEW_USER, errors);
            redirectAttributes.addFlashAttribute("registerFail", "true");
            if (register) {
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("creationInProcess", true);
                return "redirect:/home";
            }
        }

        userService.save(user);

        if (register) {
            Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        return "redirect:/home";
    }


    // Delete user
    @RequestMapping(value = "/delete", method = RequestMethod.GET, params = {"id"})
    public String removeUser(@RequestParam("id") Long id,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        logger.debug("inside pageEditUser()");

        if (id == 0L) {
            String errorMessage = messageSource.getMessage("deleteAdmin", new Object[]{userService.getById(id).getName()}, null, locale);
            redirectAttributes.addFlashAttribute(MESSAGE, errorMessage);
            return "redirect:/home";
        }

        if (id.equals(loggedInUser().getId())) {
            SecurityContextHolder.clearContext();
        }

        userService.deleteById(id);
        return "redirect:/home";
    }


    // Edit user page
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String pageEditUser(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes) {
        logger.debug("inside pageEditUser()");

        redirectAttributes.addFlashAttribute(EDIT_USER, userService.getById(id));

        return "redirect:/home";
    }


    // Update user
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute User user, Errors errors,
                             RedirectAttributes redirectAttributes) {
        logger.debug("inside updateUser()");

        boolean isPasswordChanged = true;

        if (user.getPassword() == null) {
            isPasswordChanged = false;
            String dummyPassword = "12345678";
            user.setPassword(dummyPassword);
            user.setPasswordConfirm(dummyPassword);
        }

        validator.validate(user, errors);
        customUserValidator.validate(user, errors);

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute(EDIT_USER, user)
                    .addFlashAttribute("org.springframework.validation.BindingResult." + EDIT_USER, errors)
                    .addFlashAttribute("creationInProcess", true);
            return "redirect:/home";
        }

        User loggedInUser = loggedInUser();

        if (!isPasswordChanged) {
            user.setPassword(userService.getById(user.getId()).getPassword());
            System.out.println("user = " + user);
            userService.update(user, false);
        } else {
            System.out.println("user = " + user);
            userService.update(user, true);
        }

        if (loggedInUser.getId().equals(user.getId())) {
            Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        return "redirect:/home";
    }


    private boolean isLoggedIn() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Set<String> roles = AuthorityUtils.authorityListToSet(authorities);
        return roles.contains("ROLE_USER");
    }

    private User loggedInUser() {
        return userService.getByName(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}