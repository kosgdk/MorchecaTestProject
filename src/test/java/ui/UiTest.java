package ui;

import com.codeborne.selenide.SelenideElement;
import morcheca.controllers.MainController;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"/ui_tests/application-context-uiTest.xml", "classpath:dispatcher-servlet.xml", "classpath:security-context.xml"})
public class UiTest extends AbstractUiTest {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MessageSource messageSource;


    @Before
    public void setUp(){
        open("");
    }

    @After
    public void tearDown(){
        clearBrowserCache();
    }

    @Rule
    public ErrorCollector collector = new ErrorCollector();
    

    @Test
    public void anonymousUserShouldNotHaveAccessToSomePages() {
        logger.debug("anonymousUserShouldNotHaveAccessToSomePages");

        String[] disallowedUrls = {"home", "edit?id=1", "delete?id=1"};

        for (String disallowedUrl : disallowedUrls) {
            open(disallowedUrl);
            $("legend").shouldHave(text("Test project"));
        }
    }

    @Test
    public void successLoginTest() {
        logger.debug("successLoginTest");

        assertEquals("Test project", title());
        $("#nameLoginField").setValue("admin");
        $("#passwordLoginField").setValue("adminpass")
                .submit();

        $("legend").shouldHave(text("Welcome, admin"));
    }

    @Test
    public void failLoginTest() {
        logger.debug("failLoginTest");

        $("#nameLoginField").setValue("admin");
        $("#passwordLoginField").setValue("12345678")
                .submit();

        $(byText("Wrong username or password")).shouldBe(visible);
    }

    @Test
    public void successRegisterTest() {
        logger.debug("successRegisterTest");

        String name = "testUser2";
        $("#registerTabLink").click();
        $("#nameRegisterField").setValue(name);
        $("#emailRegisterField").setValue("testUser2@example.com");
        $("#passwordRegisterField").setValue("password2");
        $("#passwordConfirmRegisterField").setValue("password2")
                .submit();

        $("legend").shouldHave(text("Welcome, " + name));
    }

    @Test
    public void failRegisterShouldActivateRegisterTabOnLoginPage() {
        logger.debug("failRegisterShouldActivateRegisterTabOnLoginPage");

        $("#registerForm").submit();
        $("#nameRegisterField").shouldBe(visible);
        $("#nameLoginField").shouldNotBe(visible);
    }

    @Test
    public void loginPageTabsTest() {
        SelenideElement nameLoginField = $("#nameLoginField");
        SelenideElement nameRegisterField = $("#nameRegisterField");

        nameLoginField.shouldBe(visible).shouldBe(focused);
        nameRegisterField.shouldNotBe(visible);

        $("#registerTabLink").click();
        nameRegisterField.shouldBe(visible).shouldBe(focused);
        nameLoginField.shouldNotBe(visible);

        $("#loginTabLink").click();
        nameLoginField.shouldBe(visible).shouldBe(focused);
        nameRegisterField.shouldNotBe(visible);
    }

    @Test
    public void registerInvalidNameErrorMessagesTest() {
        logger.debug("registerInvalidNameErrorMessagesTest");

        String shortName = "aa";
        String longName = StringUtils.leftPad("", 21, "a");
        String nameWithSpace = "test name";

        Locale locale = Locale.getDefault();
        String emptyNameMessage = messageSource.getMessage("user.name.isEmpty", null, null, locale);
        String nameLengthMessage = messageSource.getMessage("user.name.length", new Integer[] {null, 20, 3}, null, locale);
        String invalidNameMessage = messageSource.getMessage("user.name.invalid", null, null, locale);

        SelenideElement nameRegisterField = $("#nameRegisterField");

        $("#registerTabLink").click();

        nameRegisterField.submit();
        collector.checkThat("Empty name", emptyNameMessage, is($(byText(emptyNameMessage)).text()));

        nameRegisterField.setValue(shortName).submit();
        collector.checkThat("Short name", nameLengthMessage, is($(byText(nameLengthMessage)).text()));

        nameRegisterField.setValue(longName).submit();
        // Will not produce validation error, but will trim text according to "maxlength" parameter of input
        collector.checkThat("Long name", false, is($(byText(nameLengthMessage)).exists()));
        collector.checkThat("Long name", StringUtils.leftPad("", 20, "a"), is(nameRegisterField.getValue()));

        nameRegisterField.setValue(nameWithSpace).submit();
        collector.checkThat("Name with space", invalidNameMessage, is($(byText(invalidNameMessage)).text()));
    }

}
