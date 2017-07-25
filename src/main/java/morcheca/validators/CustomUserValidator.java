package morcheca.validators;

import morcheca.entities.User;
import morcheca.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;


@Component("CustomUserValidator")
public class CustomUserValidator implements Validator {

    @Autowired
    UserService userService;


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        validatePasswordConfirmation(user, errors);
        if (errors.hasFieldErrors("name") && errors.hasFieldErrors("email")) return;
        validateUniqueness(user, errors);

    }

    public void validatePasswordConfirmation(User user, Errors errors) {
        String password = user.getPassword();
        String passwordConfirm = user.getPasswordConfirm();
        if (password != null && passwordConfirm != null && !passwordConfirm.equals(password)) {
            errors.rejectValue("passwordConfirm", "user.passwordConfirm.notEquals");
        }
    }

    public void validateUniqueness(User user, Errors errors) {

        Long id = user.getId();
        String name = user.getName();
        String email = user.getEmail();

        if (name == null) name = "";
        if (email == null) email = "";

        List<User> users = userService.getByNameOrEmail(name, email);
        if (users.isEmpty()) return;

        for (User persistedUser : users) {
            if (!persistedUser.getId().equals(id) && name.equals(persistedUser.getName())) {
                errors.rejectValue("name", "user.name.notUnique");
            }
            if (!persistedUser.getId().equals(id) && email.equals(persistedUser.getEmail())) {
                errors.rejectValue("email", "user.email.notUnique");
            }
        }
    }
}
