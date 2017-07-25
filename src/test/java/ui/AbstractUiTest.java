package ui;

import com.codeborne.selenide.Configuration;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.servlet.ServletException;
import java.io.File;

public abstract class AbstractUiTest {

    private static Tomcat tomcat;
    private static final int TOMCAT_PORT = 9999;
    private static String contextPath = "/morcheca";
    private static String baseUrl = "http://localhost:" + TOMCAT_PORT + contextPath + "/";


    @BeforeClass
    public static void beforeClass() throws LifecycleException, ServletException {
        tomcat = new Tomcat();
        tomcat.setBaseDir(".");
        tomcat.setPort(TOMCAT_PORT);

        Context context = tomcat.addWebapp(contextPath, new File("src/main/webapp").getAbsolutePath());
        context.setAltDDName(new File("src/test/resources/ui_tests/web-test.xml").getAbsolutePath());

        tomcat.init();
        tomcat.start();

        System.setProperty("webdriver.chrome.driver",
                            new File("src/test/resources/ui_tests/chromedriver.exe").getAbsolutePath());
        Configuration.browser = "Chrome";
        Configuration.baseUrl = baseUrl;
    }

    @AfterClass
    public static void afterClass() throws LifecycleException {
        tomcat.stop();
    }

}