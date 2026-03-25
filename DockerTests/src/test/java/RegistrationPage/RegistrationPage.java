package RegistrationPage;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage {

    private WebDriver driver;

    public RegistrationPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(css = "input#username")
    public WebElement loginField;

    @FindBy(css = "input#password")
    public WebElement passwordField;

    @FindBy(xpath = "//button")
    public WebElement sendButton;

    @FindBy(css = "div#result")
    public WebElement resultMessage;

    @Step("Ввод логина")
    public RegistrationPage enterLogin(String login){
        loginField.sendKeys(login);
        return this;
    }

    @Step("Ввод пароля")
    public RegistrationPage enterPassword(String password){
        passwordField.sendKeys(password);
        return this;
    }

    @Step("Нажатие кнопки отправить")
    public RegistrationPage clickSendButton(){
        sendButton.click();
        return this;
    }

    @Step("Заполнение формы")
    public RegistrationPage fillForm(String login, String password){
        return enterLogin(login).enterPassword(password).clickSendButton();
    }

    @Step("Получение сообщения результата")
    public String getResultMessage(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(resultMessage));
        wait.until(driver -> !resultMessage.getText().trim().isEmpty());
        return resultMessage.getText();
    }
}
