package com.example.punchscript.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SeleniumUtil {

    private static final String DRIVER_PATH = "src/main/resources/chromedriver.exe";

    private static final String URL = "https://pass.sdu.edu.cn/cas/login?service=https%3A%2F%2Fscenter.sdu.edu.cn%2Ftp_fp%2Findex.jsp";

    private WebDriver driver;

    @PostConstruct
    public void init(){
        System.setProperty("webdriver.chrome.driver",DRIVER_PATH);
    }

    public String getCookie(String userName, String password) {
        driver = new ChromeDriver();
        driver.get(URL);
        auto(userName, password);
        Cookie jsessionid = driver.manage().getCookieNamed("JSESSIONID");
        driver.close();
        return jsessionid.getName()+"="+jsessionid.getValue();
    }

    private void auto(String userName, String password) {
        WebElement accountElement = driver.findElement(By.id("un"));
        accountElement.sendKeys(userName);
        WebElement passwordElement = driver.findElement(By.id("pd"));
        passwordElement.sendKeys(password);
        WebElement autoLoginCheckbox = driver.findElement(By.className("login_box_checkbox"));
        autoLoginCheckbox.click();
        WebElement loginButton = driver.findElement(By.className("login_box_landing_btn"));
        loginButton.click();
    }
}
