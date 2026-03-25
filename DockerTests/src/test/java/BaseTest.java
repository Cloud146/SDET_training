import io.qameta.allure.Attachment;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
                .addArguments("--remote-allow-origins=**")
                .addArguments("--disable-gpu")
                .addArguments("--disable-infobars")
                .addArguments("--start-maximized");

        String hubUrlDocker = "http://host.docker.internal:4444/wd/hub";
        String hubUrlLocal = "http://localhost:4444/wd/hub";
        String remoteUrl = System.getenv("SELENIUM_URL");

        System.out.println("SELENIUM SETUP: Connecting to URL: " + remoteUrl);

//        driver = new ChromeDriver(options);

        try {
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } catch (Exception e){
            throw new RuntimeException("Не удалось подключиться к Selenium Grid", e);
        }

        driver.manage().window().setSize(new Dimension(1920, 1080));
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
