import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;

public class BaseTest {

    private WebDriver driver;
    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions()
                .addArguments("--headless=new")
                .addArguments("--no-sandbox")
                .addArguments("--disable-dev-shm-usage")
                .addArguments("--remote-allow-origins=**")
                .addArguments("--disable-gpu")
                .addArguments("--disable-extensions")
                .addArguments("--disable-infobars")
                .addArguments("--window-size=1920,1080");

        String remoteUrl = System.getenv("SELENIUM_URL");

        System.out.println("SELENIUM SETUP: Connecting to URL: " + remoteUrl);

//        driver = new ChromeDriver(options);

        try {
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } catch (Exception e){
            throw new RuntimeException("Не удалось подключиться к Selenium Grid", e);
        }

    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Attachment(value = "{name}", type = "image/png")
    public byte[] saveScreenshot(String name) {
        if (driver == null) {
            return new byte[0];
        }

        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
