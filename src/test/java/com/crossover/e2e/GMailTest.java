package com.crossover.e2e;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class GMailTest {
    public WebDriver driver;
    private Properties properties = new Properties();
    String emailSubject ;
    String emailBody ;
    @BeforeMethod
	public void setUp() throws Exception {
        
        properties.load(new FileReader(new File("src/test/resources/test.properties")));
        emailSubject = properties.getProperty("email.subject");
        emailBody = properties.getProperty("email.body"); 
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
        WebElement sub  = driver.findElement(By.cssSelector(".aoD.az6 input"));
        sub.click();
        sub.sendKeys(emailSubject);
        WebElement body = driver.findElement(By.cssSelector(".Ar.Au div"));
        body.click();
        body.sendKeys(emailBody);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@aria-label='More options']")).click();  /// More Options
        driver.findElement(By.xpath("//div[@class='J-N-Jz'][contains(text(),'Label')]")).click();  /// click on Label
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@title='Social']")).click();   //Click on Social
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click();
        Thread.sleep(1000);
        
        ////////////////////// Mark email as starred ////
      
        driver.findElement(By.xpath("//div[@aria-label='Social']")).click();
        Thread.sleep(2000);
       // driver.findElement(By.xpath("//span[@id=':7a']")).click();  // Mark email starred  
        //Storing mails in a list with subject as test properties
        List<WebElement> emailthread = driver.findElements(By.xpath("//*[@class='bog']"));
        List<WebElement> star= driver.findElements(By.xpath("//span[@title='Not starred']"));
        
        System.out.println("number of email is"+ emailthread.size());
        for(int i=0;i<emailthread.size();i++){
                if(emailthread.get(i).getText().equals(emailSubject)){
                    System.out.println("Yes we have got mail from " + emailSubject);
                    JavascriptExecutor executor = (JavascriptExecutor)driver;
                    executor.executeScript("arguments[0].click();", star.get(i));
                    emailthread.get(i).click();
                    break;
                }
                else{
                	
                    System.out.println("No mail from " + emailSubject);
                    
                }
       }
    }
   
}
        
        
        //////////////Open received email ////////////////////////

        
    
    /// Mark Email Star


