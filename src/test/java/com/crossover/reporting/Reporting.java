package com.crossover.reporting;

import java.io.File;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IClass;
import org.testng.IInvokedMethodListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.ScreenCapture;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.utils.FileUtil;
import com.crossover.e2e.GMailTest;
import com.mongodb.diagnostics.logging.Logger;

public class Reporting extends TestListenerAdapter{
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest logger;
	public GMailTest obj;
	public String timestamp = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());

	public void onStart(ITestContext testcontext) {
		String repname = "testreport-"+timestamp+".html";
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"/extent-reports/"+repname);
		htmlReporter.loadXMLConfig(System.getProperty("user.dir")+"/extent-config.xml");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", "localhost");
		extent.setSystemInfo("environment", "QA");
		extent.setSystemInfo("user", "summi");
		htmlReporter.config().setDocumentTitle("BankingProjectReport");
		htmlReporter.config().setReportName("Functional test");
		htmlReporter.config().setTheme(Theme.DARK);
		
	}
	public void onTestSuccess(ITestResult tr) {
		System.out.println("OnSuccess");
		logger = extent.createTest(tr.getName());
		logger.log(Status.PASS, MarkupHelper.createLabel(tr.getName(),ExtentColor.GREEN));	
		System.out.println("Test '" + tr.getName() + "' PASSED");
	}


	public void onTestFailure(ITestResult tr) {
		System.out.println("OnFailure");
		logger = extent.createTest(tr.getName()); // Create new entry in the report
		logger.log(Status.FAIL, MarkupHelper.createLabel(tr.getName(),ExtentColor.RED));
		TakesScreenshot ts = (TakesScreenshot)obj.driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String screenshotpath = System.getProperty("user.dir")+"/Screenshots/"+tr.getName()+"_"+timestamp +".png";
		File dest = new File(screenshotpath);
			try{
				FileUtils.copyFile(source, dest);
				System.out.println("Taking screenshot");
				logger.fail(tr.getName());
			}
			catch (Exception e) {
				// TODO: handle exception
				System.out.println("screenshort exception "+e.getMessage());
			}	
		System.out.println("Test '" + tr.getName() + "' FAILED");
	}


	public void onTestSkipped(ITestResult tr) {
		logger = extent.createTest(tr.getName()); 
		logger.log(Status.SKIP, MarkupHelper.createLabel(tr.getName(), ExtentColor.ORANGE));

	}
	

	  public void onFinish(ITestContext testContext) {
		 extent.flush();
	  }
}
	
