package morcheca.validators;

import morcheca.entities.User;
import morcheca.services.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.Errors;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;


@RunWith(Enclosed.class)
@ContextConfiguration(locations = {"classpath:application-context.xml", "classpath:security-context.xml"})
public class CustomUserValidatorTest {

    @Mock
    UserService service;

    @Mock
    User user;

    @Mock
    Errors errors;

    @InjectMocks
    @Resource
    CustomUserValidator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @RunWith(BlockJUnit4ClassRunner.class)
    public static class ValidateTests extends CustomUserValidatorTest{

        CustomUserValidator validator = spy(CustomUserValidator.class);

        @Override
        @Before
        public void setUp() {
            super.setUp();
            doNothing().when(validator).validatePasswordConfirmation(user, errors);
            doNothing().when(validator).validateUniqueness(user, errors);
        }

        @Test
        public void validate_ShouldCallInnerMethods() {
            validator.validate(user, errors);

            verify(validator, times(1)).validatePasswordConfirmation(user, errors);
            verify(validator, times(1)).validateUniqueness(user, errors);
        }

        @Test
        public void validate_NameHasErrors_ShouldCallValidateUniqueness() {
            when(errors.hasFieldErrors("name")).thenReturn(true);

            validator.validate(user, errors);

            verify(validator, times(1)).validatePasswordConfirmation(user, errors);
            verify(validator, times(1)).validateUniqueness(user, errors);

        }

        @Test
        public void validate_EmailHasErrors_ShouldNotCallValidateUniqueness() {
            when(errors.hasFieldErrors("email")).thenReturn(true);

            validator.validate(user, errors);

            verify(validator, times(1)).validatePasswordConfirmation(user, errors);
            verify(validator, times(1)).validateUniqueness(user, errors);
        }

        @Test
        public void validate_NameAndEmailHasErrors_ShouldNotCallValidateUniqueness() {
            when(errors.hasFieldErrors("name")).thenReturn(true);
            when(errors.hasFieldErrors("email")).thenReturn(true);

            validator.validate(user, errors);

            verify(validator, times(1)).validatePasswordConfirmation(user, errors);
            verify(validator, never()).validateUniqueness(user, errors);
        }
    }


    @RunWith(BlockJUnit4ClassRunner.class)
    public static class ValidatePasswordConfirmationTests extends CustomUserValidatorTest{

        @Test
        public void validatePasswordConfirmation_PassAndConfirmAreEquals_ShouldNotCauseErrors() {
            when(user.getPassword()).thenReturn("12345678");
            when(user.getPasswordConfirm()).thenReturn("12345678");

            validator.validatePasswordConfirmation(user, errors);
            verifyZeroInteractions(errors);
        }

        @Test
        public void validatePasswordConfirmation_PassAndConfirmAreNotEquals_ShouldCauseErrors() {
            when(user.getPassword()).thenReturn("12345678");
            when(user.getPasswordConfirm()).thenReturn("qwertyuiop");

            validator.validatePasswordConfirmation(user, errors);
            verify(errors, times(1)).rejectValue("passwordConfirm", "user.passwordConfirm.notEquals");
        }

        @Test
        public void validatePasswordConfirmation_NullPassword_ShouldCauseError() {
            when(user.getPassword()).thenReturn(null);
            when(user.getPasswordConfirm()).thenReturn("12345678");

            validator.validatePasswordConfirmation(user, errors);
            verifyZeroInteractions(errors);
        }

        @Test
        public void validatePasswordConfirmation_NullConfirmation_ShouldCauseError() {
            when(user.getPassword()).thenReturn("12345678");
            when(user.getPasswordConfirm()).thenReturn(null);

            validator.validatePasswordConfirmation(user, errors);
            verifyZeroInteractions(errors);
        }

    }


    @RunWith(BlockJUnit4ClassRunner.class)
    public static class ValidateUniquenessTests extends CustomUserValidatorTest{

        @Mock
        User persistedUser;

        @Before
        public void setUp() {
            super.setUp();
        }

        @Test
        public void validateUniqueness_NameAndEmailAreUnique_ShouldNotCauseErrors() {
            when(user.getName()).thenReturn("TestUser");
            when(user.getEmail()).thenReturn("testUser@example.com");
            when(service.getByNameOrEmail(anyString(), anyString())).thenReturn(new ArrayList<>());

            validator.validateUniqueness(user, errors);

            verifyZeroInteractions(errors);
        }

        @Test
        public void validateUniqueness_NewUser_NameIsNotUnique_ShouldCauseError() {
            when(user.getId()).thenReturn(null);
            when(persistedUser.getId()).thenReturn(2L);

            when(user.getName()).thenReturn("TestUser");
            when(persistedUser.getName()).thenReturn("TestUser");

            when(service.getByNameOrEmail(anyString(), anyString())).thenReturn(Collections.singletonList(persistedUser));

            validator.validateUniqueness(user, errors);

            verify(errors, times(1)).rejectValue("name", "user.name.notUnique");
        }

        @Test
        public void validateUniqueness_newUser_EmailIsNotUnique_ShouldCauseError() {
            when(user.getId()).thenReturn(null);
            when(persistedUser.getId()).thenReturn(2L);

            when(user.getEmail()).thenReturn("testUser@example.com");
            when(persistedUser.getEmail()).thenReturn("testUser@example.com");

            when(service.getByNameOrEmail(anyString(), anyString())).thenReturn(Collections.singletonList(persistedUser));

            validator.validateUniqueness(user, errors);

            verify(errors, times(1)).rejectValue("email", "user.email.notUnique");
        }

        @Test
        public void validateUniqueness_EditUser_NameIsNotUnique_ShouldNotCauseError() {
            when(user.getId()).thenReturn(2L);
            when(persistedUser.getId()).thenReturn(2L);

            when(user.getName()).thenReturn("TestUser");
            when(persistedUser.getName()).thenReturn("TestUser");

            when(service.getByNameOrEmail(anyString(), anyString())).thenReturn(Collections.singletonList(persistedUser));

            validator.validateUniqueness(user, errors);

            verifyZeroInteractions(errors);
        }

        @Test
        public void validateUniqueness_editUser_EmailIsNotUnique_ShouldNotCauseError() {
            when(user.getId()).thenReturn(2L);
            when(persistedUser.getId()).thenReturn(2L);

            when(user.getEmail()).thenReturn("testUser@example.com");
            when(persistedUser.getEmail()).thenReturn("testUser@example.com");

            when(service.getByNameOrEmail(anyString(), anyString())).thenReturn(Collections.singletonList(persistedUser));

            validator.validateUniqueness(user, errors);

            verifyZeroInteractions(errors);
        }

    }


}