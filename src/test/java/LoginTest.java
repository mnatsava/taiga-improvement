
import api.CustomEmailApiClient;
import api.EmailVerificationHelper;
import base.TestBase;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

import static org.testng.Assert.assertTrue;

/**
 * @author Sargis Sargsyan on 6/7/21
 * @project internal-training-exam
 */
public class LoginTest extends TestBase {
    @Test
    public void loginViaApi() {
        String createdEmail = EmailVerificationHelper.getCreatedEmail();
        LoginPage taigaLoginPage = new LoginPage();
        System.out.println("email " + createdEmail);

        login(createdEmail, "Tumo123!");
        HomePage homePage = (HomePage) new HomePage();
        homePage.clickProjectsIcon();
        assertTrue(homePage.getCurrentUrl().contains("projects"),
                "URL is not correct");
    }

    @Test
    public void verifyEmail() throws InterruptedException {
        String createdEmail = EmailVerificationHelper.getCreatedEmail();
        LoginPage taigaLoginPage = new LoginPage();
        System.out.println("email " + createdEmail);

        login(createdEmail, "Tumo123!");
        HomePage homePage = (HomePage) new HomePage();
        Thread.sleep(5000);

        EmailVerificationHelper.verifyEmail();
        homePage.clickProjectsIcon();
        assertTrue(homePage.getCurrentUrl().contains("projects"),
                "URL is not correct");
    }

}
