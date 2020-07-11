package com.crossover.e2e;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


public class GMailTest {
    private WebDriver driver;
    private Properties properties = new Properties();
    String emailSubject = properties.getProperty("email.subject");
    String emailBody = properties.getProperty("email.body"); 
    @BeforeMethod
	public void setUp() throws Exception {
        
        properties.load(new FileReader(new File("src/test/resources/test.properties")));
        //Dont Change below line. Set this value in test.properties file incase you need to change it..
        System.setProperty("webdriver.chrome.driver",properties.getProperty("webdriver.chrome.driver") );
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        driver =  new ChromeDriver(options); 
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    @AfterMethod
	public void tearDown() throws Exception {
       // driver.quit();
    }

    /*
     * Please focus on completing the task
     * 
     */
    @Test
    public void testSendEmail() throws Exception {
        driver.get("https://mail.google.com/");
        String title = driver.getTitle();
        System.out.println("Title is "+title);
        
        WebElement userElement = driver.findElement(By.id("identifierId"));
        userElement.sendKeys(properties.getProperty("username"));
        Thread.sleep(1000);
        driver.findElement(By.id("identifierNext")).click();

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(properties.getProperty("password"));
        driver.findElement(By.id("passwordNext")).click();
        Thread.sleep(2000);
        WebElement composeElement = driver.findElement(By.xpath("//div[@class='T-I T-I-KE L3']"));
        composeElement.click();

        driver.findElement(By.name("to")).clear();
        WebElement to = driver.findElement(By.name("to"));
        to.sendKeys(String.format("%s@gmail.com", properties.getProperty("username")));
        Thread.sleep(1000);
        to.sendKeys(Keys.ENTER);
        WebElement sub  = driver.findElement(By.xpath(".//textarea[contains(@aria-label, 'subject')]"));
        sub.click();
        sub.sendKeys(emailSubject);
        driver.findElement(By.className("Am Al editable LW-avf tS-tW")).sendKeys(emailBody);
        driver.findElement(By.id(":an")).click();
        driver.findElement(By.xpath("//div[@class='J-N-Jz'][contains(text(),'Label')]")).click();
      
        driver.findElement(By.id(":d0")).click();   //Social
        
        driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click();
        
        
    }
}
