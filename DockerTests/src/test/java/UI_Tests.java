import RegistrationPage.RegistrationPage;
import Utils.ConfigProvider;
import Utils.DbUtils;
import com.github.javafaker.Faker;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UI_Tests extends BaseTest {

    private WebDriver driver;
    private RegistrationPage registrationPage;

    @BeforeMethod(enabled = false)
    public void cleanUp() {
        DbUtils.executeSqlFile("clean_db.sql");
    }

    @Test(description = "Тест UI")
    public void UI_test() {
        Faker faker = new Faker();
        driver = getDriver();
        driver.get(ConfigProvider.getHostUrl());
        registrationPage = new RegistrationPage(driver);
        registrationPage.fillForm(faker.name().username(), faker.internet().password());
        Assert.assertEquals(registrationPage.getResultMessage(), "Пользователь успешно зарегистрирован!");
        saveScreenshot("Success Screenshot");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            saveScreenshot("Failure Screenshot");
        }

        if (driver != null) {
            driver.quit();
        }
    }
}
