package com.hsbc.selenium.crmanagement.utilities;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hsbc.selenium.crmanagement.controller.crmanagementController;
import com.hsbc.selenium.crmanagement.model.AddCIs;
import com.hsbc.selenium.crmanagement.model.PAMTemplate;
import com.hsbc.selenium.crmanagement.model.Task;

import org.openqa.selenium.JavascriptExecutor;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.openqa.selenium.Alert;
import java.util.*;
import java.lang.Exception;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;

import org.apache.commons.io.FileUtils;



public class SeleniumImplementation {
	  private static WebDriver driver;
	  private static Scanner scan;
	  private static int manageNumber;
	  private static int actionNumber;
	  private static boolean debugEnabled;
	  private static boolean isVirtual;
	  private static ArrayList<String> addedCIs;
	  private static ArrayList<String> failedCIs;
	  private static ArrayList<PAMTemplate> failedPAMTemplates;
	  
	  public static final String STANDARD_CR_CREATE_NEW_CR_TASK = "create a new CR ticket";
	  public static final String STANDARD_CR_COPY_CR_TASK="copy a CR ticket";
	  public static final String STANDARD_CR_EDIT_CR_TASK="edit a CR ticket";
	  public static final String STANDARD_CR_ADD_PLAN_TASK = "add an implementation plan";
	  public static final String STANDARD_CR_ADD_CIs_TASK="add CIs";
	  public static final String STANDARD_CR_ADD_PAM_TEMPLATES_TASK="add PAM Templates";
	  public static final String STANDARD_CR_ADD_PROJECT_TASK="add a project";
	  public static final String STANDARD_CR_ADD_APPROVER_TASK="add approvers";
	  public static final String STANDARD_CR_ADD_DEPLOYMENT_TOOL_TASK="add Deployment Tool";
	  public static final String STANDARD_CR_CHANGE_SURVEY_TASK="complete Change Survey";
	  public static final String STANDARD_CR_REMOVE_PLAN_TASK="delete the implementation plan from the CR ticket";
	  public static final String STANDARD_CR_SUBMIT_FOR_APPROVAL_TASK="submit CR for approval";
	  public static final String STANDARD_CR_SAVE_CLOSE_PLAN_TO_REFLESH_TASK="submit CR for approval";
	  public static final String STANDARD_CR_SAVE_CR_TO_SPREADSHEET_TASK="save CR ticket Information and Implementation tasks to a spreadsheet";
	  public static final String STANDARD_CR_DO_MULTIPLE_TASKS_TASK="do multiple tasks together";
	  
	  public static final String BREAK_GLASS_CR_CREATE_TICKET_TASK = "create a new Break Glass CR ticket";
	  public static final String BREAK_GLASS_CR_ADD_CIS_TASK = "add CIs";
	  public static final String BREAK_GLASS_CR_ADD_PAM_TEMPLATES_TASK = "add PAM Templates";
	  public static final String BREAK_GLASS_CR_ADD_PROJECT_TASK = "add a project";
	  public static final String BREAK_GLASS_CR_SUBMIT_FOR_APPROVAL_TASK="Submit a Break Glass CR ticket for approval";     
	  public static final String BREAK_GLASS_CR_DO_MULTIPLE_TASKS_TASK="do multiple tasks together";
	  
	  public static final int STANDARD_CR_CREATE_NEW_CR = 1;	  
	  public static final int STANDARD_CR_COPY_CR=2;
	  public static final int STANDARD_CR_EDIT_CR=3;
	  public static final int STANDARD_CR_ADD_PLAN = 4;
	  public static final int STANDARD_CR_ADD_CIs=5;
	  public static final int STANDARD_CR_ADD_PAM_TEMPLATES=6;
	  public static final int STANDARD_CR_ADD_PROJECT=7;
	  public static final int STANDARD_CR_ADD_APPROVER=8;
	  public static final int STANDARD_CR_ADD_DEPLOYMENT_TOOL=9;
	  public static final int STANDARD_CR_CHANGE_SURVEY=10;
	  public static final int STANDARD_CR_REMOVE_PLAN=11;
	  public static final int STANDARD_CR_SUBMIT_FOR_APPROVAL=12;
	  public static final int STANDARD_CR_SAVE_TASKS_TO_SPREADSHEET = 13;
	  public static final int STANDARD_CR_MULTIPLE_TASKS = 14;
	  public static final int STANDARD_CR_SAVE_CLOSE_PLAN_TO_REFLESH = 15;
	  //  public static final int STANDARD_CR_CREATE_NEW_CR_AND_SUBMIT = 14;
      
	  public static final int BREAK_GLASS_CR_CREATE_TICKET_AND_SUBMIT = 21;
	  public static final int BREAK_GLASS_CR_CREATE_TICKET = 22;
	  public static final int BREAK_GLASS_CR_ADD_CIS = 23;
	  public static final int BREAK_GLASS_CR_ADD_PAM_TEMPLATES = 24;
	  public static final int BREAK_GLASS_CR_ADD_PROJECT = 25;
	  public static final int BREAK_GLASS_CR_SUBMIT_FOR_APPROVAL=26;
	  public static final int BREAK_GLASS_CR_MULTIPLE_TASKS = 27;

	  public static final int MANAGE_TYPE_STANDARD_CR = 1;
	  public static final int MANAGE_TYPE_BREAK_GLASS_CR=2;
	  
	  public static final int SHEET_NUMBER_IMPLEMENTATION_PLAN = 0;
	  public static final int SHEET_NUMBER_STANDARD_CR_TICKET = 1;
	  public static final int SHEET_NUMBER_BREAK_GLASS_CR = 2;
	  public static final int SHEET_NUMBER_CIS = 3;
	  public static final int SHEET_NUMBER_PAM_TEMPLATES = 4;
	  public static final int SHEET_NUMBER_SURVEY = 5;
	  public static final int SHEET_NUMBER_APPROVERS = 6;
	  
	  public static final int BREAK_GLASS_MULTIPLE_TASKS_CREATE_TICKET = 1;
	  public static final int BREAK_GLASS_MULTIPLE_TASKS_ADD_CIS = 2;
	  public static final int BREAK_GLASS_MULTIPLE_TASKS_ADD_PAM_TEMPLATE = 3;
	  public static final int BREAK_GLASS_MULTIPLE_TASKS_ADD_PROJECT= 4;
	  public static final int BREAK_GLASS_MULTIPLE_TASKS_SUBMIT_TICKET = 5;
	  
	 
	  // public static final int STANDARD_CR_ADD_PROJECT=9;	  
	  public static final int EXIT_NUMBER = 0;

	  private static Logger logger = Logger.getLogger(SeleniumImplementation.class);
	  
	  
	  public static void waitForWindow(int timeout) {
		    try {
		      Thread.sleep(timeout);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
	  }
	  
	  // 
	  public static void waitForElementByName(WebDriver driver, String elementName){
		  WebDriverWait wait = new WebDriverWait(driver, 30);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(elementName)));
	  }
	  
	  public static void waitForElementByXPath(WebDriver driver,String xPath){
		  WebDriverWait wait = new WebDriverWait(driver, 30);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
	  }
	  
	     
	  public static void waitForElementById(WebDriver driver,String elementId){
		  WebDriverWait wait = new WebDriverWait(driver, 30);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementId)));
	  }

	  public static void waitForElementByCssSelector(WebDriver driver,String elementCssSelector){
		  WebDriverWait wait = new WebDriverWait(driver, 30);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(elementCssSelector)));
	  }	   
	  
	  public static void waitForElementByLinkText(WebDriver driver,String elementLinkText){
		  WebDriverWait wait = new WebDriverWait(driver, 30);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(elementLinkText)));
	  }
	  
	  public static void waitForElementByPartialLinkText(WebDriver driver, String partialLinkText){
		  WebDriverWait wait = new WebDriverWait(driver, 30);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(partialLinkText)));
	  }
	  
		  public static boolean tryToSelectTextFromSelectElement(WebDriver driver, Select select, String text){
			  boolean result = false;
	    	  driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		      try {
			         select.selectByVisibleText(text);
			         result = true;
		        }
		      
		       catch (NoSuchElementException ignored) {
		    	  result = false;
		        }
		       catch (StaleElementReferenceException ignored) {
		          result = false;
		        }
	    	  driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		      return result;
		      
		  }		
		
	  public static boolean tryToFindElementById(WebDriver driver, String id){
		  boolean result = false;
    	  driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	      try {
		          result = driver.findElement(By.id(id)).isDisplayed();
	        }
	      
	      catch (NoSuchElementException ignored) {
	    	  result = false;
	        }
	      catch (StaleElementReferenceException ignored) {
	          result = false;
	        }
    	  driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	      return result;
	      
	  }
	  public static boolean tryToFindElementByXpath(WebDriver driver, String xpath){
		  boolean result = false;
    	  driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	      try {
		          result = driver.findElement(By.xpath(xpath)).isDisplayed();
	        }
	      
	      catch (      NoSuchElementException ignored) {
	    	  result = false;
	        }
	      catch (      StaleElementReferenceException ignored) {
	          result = false;
	        }
    	  driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	      return result;
	      
	  }	  
	  public static boolean tryToFindElementByPartialLinkText(WebDriver driver, String partialLinkText){
		  boolean result = false;
    	  driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
	      try {
		          result = driver.findElement(By.partialLinkText(partialLinkText)).isDisplayed();
	        }
	      
	      catch (NoSuchElementException ignored) {
	    	  result = false;
	        }
	      catch (StaleElementReferenceException ignored) {
	          result = false;
	        }
    	  driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	      return result;
	      
	  }	  
	  public static void waitForKeyPressed(String messages){
	        try {
	        	logger.debug(messages);
	        	logger.debug("Press any key to continue...");
	            System.in.read();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	  }
	  
	  public static boolean validateStartAndEndDateWithCurrentDate(String startDateStr, String endDateStr) throws ParseException {
		  boolean result = true;
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date startDate = simpleDateFormat.parse(startDateStr);
			Date endDate = simpleDateFormat.parse(endDateStr);
			Date now = new Date();
			if(startDate.before(now) || endDate.before(now)){
				result = false;
			}
		  return result;
	  }
	  
	  public static int validateDate(String scheduledStartDateStr, String scheduledEndDateStr, String startDateStr, String endDateStr) throws ParseException{
			logger.debug("Entering SeleniumTest.validateDate...");
		    int validationCode = 0;
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date startDate = simpleDateFormat.parse(startDateStr);
			Date endDate = simpleDateFormat.parse(endDateStr);
			Date scheduledStartDate = simpleDateFormat.parse(scheduledStartDateStr);
			Date scheduledEndDate = simpleDateFormat.parse(scheduledEndDateStr);
			if(startDate.before(scheduledStartDate) || endDate.before(scheduledStartDate)) {
				validationCode = Task.DATE_BEFORE_START_DATE;
			} else if(startDate.after(scheduledEndDate) || endDate.after(scheduledEndDate)){
				validationCode = Task.DATE_AFTER_END_DATE;
			} else if(endDate.before(startDate)){
				validationCode = Task.END_DATE_BEFORE_START_DATE;
			} else {
				validationCode = Task.START_DATE_END_DATE_ALL_GOOD;
			}
			logger.debug("Exiting SeleniumTest.validateDate...");	  
		  return validationCode;
	  }
	  

	  

	  public static String getLastWindowHandle(Set<String> allWindowHandles){
		// logger.debug("Entering SeleniumTest.getLastWindowHandle...");
		  if(allWindowHandles == null) return "";
		  String lastWindowHandle = "";
	        for(String handle : allWindowHandles)
	        {
	        	logger.debug("Window handle: - > " + handle);
	        	lastWindowHandle = handle;
	        }
		//	logger.debug("Exiting SeleniumTest.getLastWindowHandle...");
	        return lastWindowHandle;
	  }
	  
	  public static void addTask(WebDriver driver, String taskTitle, String taskDescription){
			logger.debug("Entering SeleniumTest.addTask...");
	        driver.findElement(By.id("taskTitle")).click();
	        driver.findElement(By.id("taskTitle")).sendKeys(taskTitle);
	        driver.findElement(By.id("taskDescription")).click();
	        driver.findElement(By.id("taskDescription")).sendKeys(taskDescription);		  
	        driver.findElement(By.id("addupdateTask")).click();
			logger.debug("Exiting SeleniumTest.addTask..."); 
	  }
	  
	  public static void addTask(WebDriver driver,Task task, int instanceNumber){
		  logger.debug("Entering SeleniumTest.addTask...");
          Set<String> allWindowHandles = driver.getWindowHandles();
          String originalLastWindowHandle = getLastWindowHandle(allWindowHandles);
		    SeleniumImplementation.waitForElementById(driver, "taskTitle");
	        driver.findElement(By.id("taskTitle")).click();
	        driver.findElement(By.id("taskTitle")).sendKeys(task.getTaskTitle().trim());
		    SeleniumImplementation.waitForElementById(driver,"taskDescription");
	        driver.findElement(By.id("taskDescription")).click();
	        driver.findElement(By.id("taskDescription")).sendKeys(task.getTaskDescription().trim());
	        if(task!=null && !task.getStartDate().trim().isEmpty()){
			    SeleniumImplementation.waitForElementById(driver,"taskStartDate");
	            driver.findElement(By.id("taskStartDate")).click();
	            driver.findElement(By.id("taskStartDate")).clear();
	            driver.findElement(By.id("taskStartDate")).click();
	            driver.findElement(By.id("taskStartDate")).sendKeys(task.getStartDate().trim());
	            logger.debug("Task Start Date: " + task.getStartDate().trim());
	        }
       
	        if(task!=null && !task.getEndDate().trim().isEmpty()){
			    SeleniumImplementation.waitForElementById(driver,"taskEndDate");
	            driver.findElement(By.id("taskEndDate")).click();
	            driver.findElement(By.id("taskEndDate")).clear();
	            driver.findElement(By.id("taskEndDate")).click();
	            driver.findElement(By.id("taskEndDate")).sendKeys(task.getEndDate().trim());
	            logger.debug("Task End Date: " + task.getEndDate().trim());
	        }
	        
	        if(task!=null && !task.getGroupAssigned().trim().isEmpty()){
	        	logger.debug("set group assigned!");
	        	SeleniumImplementation.waitForElementByLinkText(driver, "Assignment Group *");
	        	driver.findElement(By.linkText("Assignment Group *")).click();
	            // waitForWindow(runningSpeed * speedUnit);
	        	waitForWindow(2000);
	            allWindowHandles = driver.getWindowHandles();
	            String lastWindowHandle = getLastWindowHandle(allWindowHandles);
	            driver.switchTo().window(lastWindowHandle);	 
	            SeleniumImplementation.waitForElementById(driver, "grpName");
	            driver.findElement(By.id("grpName")).click();
	            driver.findElement(By.id("grpName")).sendKeys(task.getGroupAssigned().trim());
	            SeleniumImplementation.waitForElementById(driver, "search");
	            driver.findElement(By.id("search")).click();
	            driver.findElement(By.linkText(task.getGroupAssigned().trim())).click();
	            logger.debug("Group Assigned: " + task.getGroupAssigned().trim());
	        }	        
            // waitForWindow(runningSpeed * speedUnit);
	        waitForWindow(2000);
            driver.switchTo().window(originalLastWindowHandle);	
            SeleniumImplementation.waitForElementById(driver, "addupdateTask");
	        driver.findElement(By.id("addupdateTask")).click();
	        logger.debug("Clicked the button to add a task");
			logger.debug("Exiting SeleniumTest.addTask...");
    }
	  
	

	  public static boolean validateDatesOfTasks(Task[] tasks, String scheduledStartDate, String scheduledEndDate, int instanceNumber) throws ParseException{
		logger.debug("Entering SeleniumTest.validateDatesOfTasks...");
		boolean result = true;
		for(int i = 0; i < tasks.length; i++){
			logger.debug("Dates of task " + tasks[i].getTaskNumber() + ":  start date " + tasks[i].getStartDate() + "; end date " + tasks[i].getEndDate());
			int validation = SeleniumImplementation.validateDate(scheduledStartDate, scheduledEndDate, tasks[i].getStartDate(), tasks[i].getEndDate());
			if(validation == Task.DATE_BEFORE_START_DATE){
				logger.error("Start Date or End Date of a task can not be earlier than the scheduled start date");
				logger.error(tasks[i].getTaskNumber() + ", " + tasks[i].getTaskTitle() + ", " + tasks[i].getStartDate() + ", " + tasks[i].getEndDate());
	     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Start Date or End Date of a task can not be earlier than the scheduled start date: " + tasks[i].getTaskNumber() + ", " + tasks[i].getTaskTitle() + ", " + tasks[i].getStartDate() + ", " + tasks[i].getEndDate());
			    result = false;
			}else if(validation == Task.DATE_AFTER_END_DATE){
				logger.error("Start Date or End Date of a task can not later than the scheduled end date");
				logger.error(tasks[i].getTaskNumber() + ", " + tasks[i].getTaskTitle() + ", " + tasks[i].getStartDate() + ", " + tasks[i].getEndDate());
	     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Start Date or End Date of a task can not later than the scheduled end date: " + tasks[i].getTaskNumber() + ", " + tasks[i].getTaskTitle() + ", " + tasks[i].getStartDate() + ", " + tasks[i].getEndDate());
			    result = false;
			}else if(validation == Task.END_DATE_BEFORE_START_DATE){
				logger.error("End Date of a task can not be earlier than start date");
				logger.error(tasks[i].getTaskNumber() + ", " + tasks[i].getTaskTitle() + ", " + tasks[i].getStartDate() + ", " + tasks[i].getEndDate());
	     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "End Date of a task can not be earlier than start date: " + tasks[i].getTaskNumber() + ", " + tasks[i].getTaskTitle() + ", " + tasks[i].getStartDate() + ", " + tasks[i].getEndDate());
			    result = false;
			}else{
				logger.debug("Dates of task " + tasks[i].getTaskNumber() + " are valid.");
			}
		}
		if(result) logger.debug("The dates in the tasks are valid.");
		logger.debug("Exiting SeleniumTest.validateDatesOfTasks...");
		return result;
	}
	
	public static boolean validatePropertiesAfterMenu(Map<String, String> properties, int[] multipleTasks, int[] bg_multipleTasks, String fileName ){		
		boolean result = true;
		String CR_Number = properties.get("CR_NUMBER");
		String IN_Number = properties.get("IN_NUMBER");
		String excelFileNme =  properties.get("EXCEL_FILE_NAME");
		String userID =  properties.get("USER_ID");
		String password =  properties.get("PASSWORD");
		String chromeWebDriver =  properties.get("CHROME_WEB_DRIVER");
		String SDM_URL =  properties.get("SDM_URL");
		String Debug =  properties.get("Debug");
		String virtualWindow =  properties.get("VirtualWindow");

	    if(excelFileNme == null || excelFileNme.trim().equals("")){
	    	logger.error("excelFileName in "  + fileName + " can not be empty." );
	    	result = false;
	    } 

	    if(chromeWebDriver == null || chromeWebDriver.trim().equals("")){
	    	logger.error("chromeWebDriver in "  + fileName + " can not be empty." );
	    	result = false;	    	
	    } 

	    if(SDM_URL == null || SDM_URL.trim().equals("")){
	    	logger.error("SDM_URL in "  + fileName + " can not be empty." );
	    	result = false;	    	
	    } 
	    
	    if(userID == null || userID.trim().equals("")){
	    	logger.error("userID can not be empty. Please provide it in the command or in the properties file " + fileName);
	    	result = false;
	    } 
	    
	    if(password == null || password.trim().equals("")){
	    	logger.error("password can not be empty. Please provide it in the command or in the properties file " + fileName );
	    	result = false;
	    } 		
		
		if(manageNumber == SeleniumImplementation.MANAGE_TYPE_STANDARD_CR && actionNumber != SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR && actionNumber != SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS){
			if(CR_Number == null || CR_Number.trim().equals("")) {
				logger.error("CR_Number can not be empty in " + fileName + " for the task selected");
				result = false;
			} 
	
		}
		
		
		if(manageNumber == SeleniumImplementation.MANAGE_TYPE_STANDARD_CR && actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS) {
			if(multipleTasks == null || multipleTasks.length == 0) {
				logger.error("The property MultiTasks can not be empty in " + fileName + " for the task selected");
				result = false;
				
			} else {
				if(!SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR)){
					if(CR_Number == null || CR_Number.trim().equals("")) {
						logger.error("CR_Number can not be empty in " + fileName + " for the task selected");
						result = false;
					} 				
				}
			}
		}
		
		if(manageNumber == SeleniumImplementation.MANAGE_TYPE_BREAK_GLASS_CR && (actionNumber == SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET || actionNumber == SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET_AND_SUBMIT)) {
			if(IN_Number == null || IN_Number.trim().equals("")) {
				logger.error("IN_Number can not be empty in " + fileName + " for the task selected");
				result = false;
			} 			
		}
		
		if(manageNumber == SeleniumImplementation.MANAGE_TYPE_BREAK_GLASS_CR && actionNumber != SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET 
				                     && actionNumber != SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET_AND_SUBMIT && actionNumber != SeleniumImplementation.BREAK_GLASS_CR_MULTIPLE_TASKS) {
			if(CR_Number == null || CR_Number.trim().equals("")) {
				logger.error("CR_Number can not be empty in " + fileName + " for the task selected");
				result = false;
			} 							
		}
		
		if(manageNumber == SeleniumImplementation.MANAGE_TYPE_BREAK_GLASS_CR && actionNumber == SeleniumImplementation.BREAK_GLASS_CR_MULTIPLE_TASKS){
			if(bg_multipleTasks == null || multipleTasks.length == 0) {
				logger.error("The property BG_MultiTasks can not be empty in " + fileName + " for the task selected");
				result = false;
				
			} else {
				if(!SeleniumImplementation.doMultiTasksContainATask(bg_multipleTasks, SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_CREATE_TICKET)){
					if(CR_Number == null || CR_Number.trim().equals("")) {
						logger.error("CR_Number can not be empty in " + fileName + " for the task selected");
						result = false;
					} 				
				} else {
					if(IN_Number == null || IN_Number.trim().equals("")) {
						logger.error("IN_Number can not be empty in " + fileName + " for the task selected");
						result = false;
					} 						
				}
			}
		}
		
		return result;
	}
	
	public static boolean validateTaskDatesWithCurrentTime(Task[] tasks, int instanceNumber) throws ParseException{
		boolean result = true;
		if(tasks == null || tasks.length == 0){
			logger.error("No tasks to validate the dates ...");
			Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "No tasks to validate the dates ...");
			result = false;
		} else {
			for(int i = 0; i < tasks.length; i++){
				String startDate = tasks[i].getStartDate();
				String endDate = tasks[i].getEndDate();
				if(!SeleniumImplementation.validateStartAndEndDateWithCurrentDate(startDate, endDate)){
					logger.error("Error: The start date or end date can not be earlier than current date time. \n "
							+ "       Task " + (i + 1) + ": Start Date " + startDate + "  End Date: " + endDate);
					Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Error: The start date or end date can not be earlier than current date time. \n "
							+ "       Task " + (i + 1) + ": Start Date " + startDate + "  End Date: " + endDate);
					result = false;
				}
			}
		}	
		return result;
	}
	
	public static boolean validateCRDatesWithCurrentTime(Map<String, String> items, int instanceNumber) throws ParseException {
		boolean result = true;
		if(items == null && items.size() == 0) {
			result = false;
			logger.error("No CR information was passed for CR dates' validation...");
			Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "No CR information was passed for CR dates' validation...");
		} else {
			Date now = new Date();
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String scheduledStartDate = items.get("SCHEDULED_START_DATE");
			String verificationDate = items.get("VERIFICATION_START_DATE");
			String actualStartDate = items.get("ACTUAL_START_DATE");
			String actualEndDate = items.get("ACTUAL_END_DATE");
			if(scheduledStartDate != null && !scheduledStartDate.trim().equals("")){
				logger.info("scheduledStartDate: '" + scheduledStartDate + "'");
				// Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "scheduledStartDate: '" + scheduledStartDate + "'");
				Date date = simpleDateFormat.parse(scheduledStartDate);
				if(date.before(now)){
					logger.error("Scheduled Start Date can not be earlier that current time...");
					Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Scheduled Start Date can not be earlier that current time...");
					result = false;
				}
				
			}
			if(verificationDate != null && !verificationDate.trim().equals("")){
				logger.info("verificationDate: '" + verificationDate + "'");
				Date date = simpleDateFormat.parse(verificationDate);
				if(date.before(now)){
					logger.error("Verification Date can not be earlier that current time...");
					Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Verification Date can not be earlier that current time...");
					result = false;
				}
				
			}
			if(actualStartDate != null && !actualStartDate.trim().equals("")){
				logger.info("actualStartDate: '" + actualStartDate + "'");
				Date date = simpleDateFormat.parse(actualStartDate);
				if(date.before(now)){
					logger.error("Actual Start Date can not be earlier that current time...");
					Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Actual Start Date can not be earlier that current time...");
					result = false;
				}
				
			}
			
			if(actualEndDate != null && !actualEndDate.trim().equals("")){
				logger.info("Actual End Date: '" + actualEndDate + "'");
				Date date = simpleDateFormat.parse(actualEndDate);
				if(date.before(now)){
					logger.error("Actual End Date can not be earlier that current time...");
					Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Actual End Date can not be earlier that current time...");
					result = false;
				}
				
			}			
		}
		return result;
		
	}
	
	public static boolean validateDatesOnMultipleTasks(String CR_Number, String filePath, int[] multipleTasks, String scheduleStartDateFromPage, String scheduledEndDateFromPage, int instanceNumber, int serverTimeDifference) throws IOException, ParseException {
		boolean result = false;
		if(isDateValidationNecessaryInMultiTasks(multipleTasks)){
			Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(filePath, serverTimeDifference);

			if(doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR) || doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_COPY_CR) ) {
				result = Utilities.validateDatesInSpreadSheetForCopyingCR(items, instanceNumber);
				if(result && doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_PLAN)){
					Task[] tasks = Utilities.getTasksFromExcel(filePath, serverTimeDifference);
			    	String scheduledStartDate = items.get("SCHEDULED_START_DATE");
			    	String scheduledDuration = items.get("SCHEDULED_DURATION");
			    	String scheduledEndDate = Utilities.getEndDateFromDurationHours(scheduledStartDate, scheduledDuration);
			    	result = SeleniumImplementation.validateDatesOfTasks(tasks, scheduledStartDate, scheduledEndDate, instanceNumber);
				}
			} else if(doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_EDIT_CR)){
				result = Utilities.validateDatesInSpreadSheetForEditingCR(items, scheduleStartDateFromPage, scheduledEndDateFromPage, instanceNumber);
				if(result && doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_PLAN)){
					Task[] tasks = Utilities.getTasksFromExcel(filePath, serverTimeDifference);
			    	result = SeleniumImplementation.validateDatesOfTasks(tasks, scheduleStartDateFromPage, scheduledEndDateFromPage, instanceNumber);
				}
				
			} else if(doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_PLAN)){
				Task[] tasks = Utilities.getTasksFromExcel(filePath, serverTimeDifference);
		    	result = SeleniumImplementation.validateDatesOfTasks(tasks, scheduleStartDateFromPage, scheduledEndDateFromPage, instanceNumber);		
			}
		}
		return result;
	}
	
	public static boolean isDateValidationNecessaryInMultiTasks(int[] multipleTasks){
		boolean result = false;
		if(multipleTasks != null && multipleTasks.length > 0){
			for(int i = 0; i < multipleTasks.length; i++){
				int task = multipleTasks[i];
				if( task == SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR || task == SeleniumImplementation.STANDARD_CR_COPY_CR
					|| task == SeleniumImplementation.STANDARD_CR_EDIT_CR || task == SeleniumImplementation.STANDARD_CR_ADD_PLAN){
					
					result = true;
					break;
				}
			}
		}
		return result;
	}
	
	public static boolean isReadingImplementationPlanRequired(int[] multipleTasks){
		boolean result = false;
        if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PLAN){
          	result = true;
          }
  		
          if(actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS ){
          	if(SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_PLAN)){
          		result = true;
          	}
          }		return result;
	}

	public static boolean isReadingImplementationPlanRequired(int actionNumber, int[] multipleTasks){
		boolean result = false;
        if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PLAN){
          	result = true;
          }
  		
          if(actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS ){
          	if(SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_PLAN)){
          		result = true;
          	}
          }		return result;
	}	
	
	public static boolean isReadingCRRequired(int[] multipleTasks){
		boolean result = false;
        if(actionNumber == SeleniumImplementation.STANDARD_CR_EDIT_CR || actionNumber == SeleniumImplementation.STANDARD_CR_COPY_CR 
      		  || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PROJECT || actionNumber == SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR
      		  || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL){
        	result = true;
        }
		
        if(actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS ){
        	if(SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_EDIT_CR)
        		|| 	SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_COPY_CR)
        		|| 	SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_PROJECT)
        		|| 	SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR)
        		|| 	SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL)){
        		result = true;
        	}
        }

        	
        	return result;
	}

	public static boolean isReadingCRRequired(int actionNumber, int[] multipleTasks){
		boolean result = false;
        if(actionNumber == SeleniumImplementation.STANDARD_CR_EDIT_CR || actionNumber == SeleniumImplementation.STANDARD_CR_COPY_CR 
      		  || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PROJECT || actionNumber == SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR
      		  || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL){
        	result = true;
        }
		
        if(actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS ){
        	if(SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_EDIT_CR)
        		|| 	SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_COPY_CR)
        		|| 	SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_PROJECT)
        		|| 	SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR)
        		|| 	SeleniumImplementation.doMultiTasksContainATask(multipleTasks, SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL)){
        		result = true;
        	}
        }

        	
        	return result;
	}	
	
	public static boolean doMultiTasksContainATask(int[] multipleTasks, int task){
		boolean result = false;
		if(multipleTasks != null && multipleTasks.length > 0){
			for(int i = 0; i < multipleTasks.length; i++){
				
				if(multipleTasks[i] == task) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

    //return     
    public static boolean checkGroupsInTasks(String rootPath, Task[] tasks){
		logger.debug("Entering SeleniumTest.checkGroupsInTasks...");
       boolean result = true;
 		
 		String[] groups = Utilities.getAssignedGroupsFromTasks(tasks);
 		for(int i=0; i<groups.length; i++){
 			// System.out.println("assigned group " + (i+1) + ": " + groups[i]);
 		}
 		ArrayList<String> groupsNotInFile = new ArrayList<String>();
 		String FileForExistingGroups = rootPath + "ExistingGroups.txt";
 		ArrayList<String> list = null;
 		try{
 		  list = Utilities.getExistingGroupsFromFile(FileForExistingGroups);
 		}catch(FileNotFoundException e){
 			logger.error("The file for the existing groups " + FileForExistingGroups + " can not be found. Please check.");
 			e.printStackTrace();
			    result = false;
			    return result;
 		}catch(IOException e){
 			logger.error("Exception happened when reading the file " + FileForExistingGroups + ". Please check.");
 			e.printStackTrace();
			    result = false;
			    return result;
 		}
 		
 		for(int i = 0; i < groups.length; i++){
 			if(!Utilities.isStringInList(groups[i], list)){
 				boolean isItNew = true;
 				if(!groupsNotInFile.isEmpty()){
 					for(int j = 0; j < groupsNotInFile.size(); j++){
 						if(groupsNotInFile.get(j).trim().equals(groups[i].trim())){
 							isItNew = false;
 							break;
 						}
 					}
 				}
 				if(isItNew){
 				 groupsNotInFile.add(groups[i]);
 				}
 			}
 		}
 		
 		if(!groupsNotInFile.isEmpty()){
 			logger.error("The following assigned group(s) are(is) not in the file ExistingGroups.txt. Please make sure they are correct groups and add them to ExistingGroups.txt ");          
 		    for(int i =0; i < groupsNotInFile.size(); i++){
 			  logger.error(groupsNotInFile.get(i));
 		    }
		    result = false;
 		}
		logger.debug("Exiting SeleniumTest.checkGroupsInTasks..."); 		
        return result;
    }
    public static void loginToSDM(WebDriver driver, String SDM_URL, String userID, String password, int instanceNumber){
		logger.debug("Entering SeleniumTest.loginToSDM...");
		logger.debug("SDM_URL: " + SDM_URL);
		logger.debug("userId: " + userID);
        driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        logger.info("Navigating to Service Desk page...");
        driver.get(SDM_URL);
        logger.debug("waiting for USERNAME element...");
        SeleniumImplementation.waitForElementById(driver,"USERNAME");
        logger.debug("Input element for user name is available");
        logger.debug("User Name: " + userID);
        logger.info("Enter the user name");
        driver.findElement(By.name("USERNAME")).sendKeys(userID);
        SeleniumImplementation.waitForElementById(driver,"PIN");
        logger.debug("Input element for password is available");
        logger.info("Enter the password");
        driver.findElement(By.id("PIN")).sendKeys(password);
        SeleniumImplementation.waitForElementByCssSelector(driver,"span");
        logger.debug("Login button is available");
        driver.findElement(By.cssSelector("span")).click();              
        logger.info("Click the login button");
        waitForWindow(4000);
		logger.debug("Exiting SeleniumTest.loginToSDM...");
    }
    
    public static boolean checkLoginSuccess(WebDriver driver) {
    	boolean result = false;
        driver.switchTo().frame(0);
    	result = SeleniumImplementation.tryToFindElementById(driver, "ticket_type");
    	return result;
    }
    
    public static void editCRTicketAndSubmit(WebDriver driver, String fileName, String CR_Number, int instanceNumber, int serverTimeDifference) throws IOException, ParseException {
    	  logger.info("Editing the ticket " + CR_Number + "...");
    	  Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(fileName, serverTimeDifference);
  	      //click "Edit" button to edit the CR ticket
		  SeleniumImplementation.waitForElementById(driver,"imgBtn0");
    	  driver.findElement(By.id("imgBtn0")).click();
    	  waitForWindow(4000);
    	  SeleniumImplementation.editCR(driver, CR_Number, items, false, instanceNumber);   	
    	  completeAllOtherCRTasksAndSubmit(driver, fileName, CR_Number, instanceNumber, serverTimeDifference);
    }
    
    public static void completeAllOtherCRTasksAndSubmit(WebDriver driver, String fileName, String CR_Number, int instanceNumber, int serverTimeDifference) throws IOException, ParseException {
    	Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(fileName, serverTimeDifference);
    	ArrayList<String> list = Utilities.readCIsFromSpreadsheet(fileName);
    	if(list != null && list.size() > 0) {
        	logger.info("Adding CIs to the ticket " + CR_Number + "...");
   			addCIs(driver, list, instanceNumber, null);    	
    	}
    	logger.info("Adding PAM Templates to the ticket " + CR_Number + "...");
  	    SeleniumImplementation.addPAMTemplatesFromSpreadsheet(driver, fileName, instanceNumber, null);
  	    String projectStr = Utilities.getProjectIDFromMap(items);
  	    if(projectStr != null && !projectStr.trim().equals("")) {
	    	logger.info("Adding the ticket " + CR_Number + " a project with proect ID " + projectStr);  	    
	  	    SeleniumImplementation.addProject(driver, projectStr, instanceNumber, null);
  	    }
    	ArrayList<String> approvers = Utilities.getApproversFromExcel(fileName);
    	if(approvers != null && approvers.size() > 0) {
	    	logger.info("Adding approvers to the ticket " + CR_Number + "...");
	    	SeleniumImplementation.addApprovers(driver, approvers, instanceNumber);
    	}
    	logger.info("Completing the change survey for the ticket " + CR_Number + "...");
    	SeleniumImplementation.completeChangeSurvey(driver, fileName);
    	logger.info("Submitting the ticket " + CR_Number + " for approval...");
    	SeleniumImplementation.submitStandardCRForApproval(driver);
    }
    
    public static void completeChangeSurvey(WebDriver driver,String fileName) throws IOException{
      logger.debug("Entering SeleniumTest.completeChangeSurvey...");            
       Set<String>  allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        String mainWindowHandle = lastWindowHandle;
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3);
        
		SeleniumImplementation.waitForElementById(driver,"imgBtn1");
    	driver.findElement(By.id("imgBtn1")).click();
    	 logger.info("Click Change Survey Button...");
    	waitForWindow(2000);
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3);
        int[] selections = Utilities.getSurveySelectionsFromSpreadsheet(fileName);
        SeleniumImplementation.waitForElementByName(driver,"qsvy_1");
        driver.findElements(By.name("qsvy_1")).get(selections[0]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_2");
        driver.findElements(By.name("qsvy_2")).get(selections[1]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_3");
        driver.findElements(By.name("qsvy_3")).get(selections[2]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_4");
        driver.findElements(By.name("qsvy_4")).get(selections[3]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_5");
        driver.findElements(By.name("qsvy_5")).get(selections[4]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_6");
        driver.findElements(By.name("qsvy_6")).get(selections[5]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_7");
        driver.findElements(By.name("qsvy_7")).get(selections[6]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_8");
        driver.findElements(By.name("qsvy_8")).get(selections[7]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_9");
        driver.findElements(By.name("qsvy_9")).get(selections[8]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_10");
        driver.findElements(By.name("qsvy_10")).get(selections[9]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_11");
        driver.findElements(By.name("qsvy_11")).get(selections[10]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_12");
        driver.findElements(By.name("qsvy_12")).get(selections[11]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_13");
        driver.findElements(By.name("qsvy_13")).get(selections[12]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_14");
        driver.findElements(By.name("qsvy_14")).get(selections[13]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_15");
        driver.findElements(By.name("qsvy_15")).get(selections[14]).click();
        SeleniumImplementation.waitForElementByName(driver,"qsvy_16");
        driver.findElements(By.name("qsvy_16")).get(selections[15]).click();
        driver.findElements(By.name("qsvy_17")).get(selections[16]).click();
        driver.findElements(By.name("qsvy_18")).get(selections[17]).click();
        driver.findElements(By.name("qsvy_19")).get(selections[18]).click();
        
		SeleniumImplementation.waitForElementById(driver,"imgBtn0");
    	driver.findElement(By.id("imgBtn0")).click(); 
    	waitForWindow(3000);
        driver.switchTo().window(mainWindowHandle);
        driver.switchTo().frame(3);
        SeleniumImplementation.waitForElementById(driver,"alertmsgText");
    	String savedSuccessText = driver.findElement(By.id("alertmsgText")).getText();
    	if(savedSuccessText != null && savedSuccessText.contains("Save Successful")){
    		logger.info("Change Survey was saved successfully!");
    	} else {
    		logger.info("Change Survey was not saved successfully!");
    	}   
        logger.debug("Entering SeleniumTest.completeChangeSurvey...");            
    }
    
    public static void openINTicket(WebDriver driver, String IN_Number, int instanceNumber){
		logger.debug("Entering SeleniumImplementation.openINTicket...");
        //driver.findElement(By.id("ticket_type")).click();
        // 11 | select | id=ticket_type | label=Change Order | 
		logger.debug("IN Number is " + IN_Number);
        driver.switchTo().frame(0);
        waitForWindow(2000);
        // 10 | click | id=ticket_type |  |
        SeleniumImplementation.waitForElementById(driver,"ticket_type");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Logging into Service Desk Manager was successful...");        
      {
          WebElement dropdown = driver.findElement(By.id("ticket_type"));
          dropdown.findElement(By.xpath("//option[. = 'Incident']")).click();
        }
        waitForWindow(2000); 
         SeleniumImplementation.waitForElementByCssSelector(driver,"option:nth-child(3)");
         driver.findElement(By.cssSelector("option:nth-child(3)")).click();
        logger.debug("Select Insident from the dropdown...");
        waitForWindow(2000);
        SeleniumImplementation.waitForElementByName(driver,"searchKey");
        driver.findElement(By.name("searchKey")).click();
        logger.debug("Enter IN Number...");
        driver.findElement(By.name("searchKey")).sendKeys(IN_Number);
        SeleniumImplementation.waitForElementByCssSelector(driver,"#imgBtn0 > span");
        driver.findElement(By.cssSelector("#imgBtn0 > span")).click();
        logger.debug("Click Go button to load the page of IN details ...");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Loading the detailed page of " + IN_Number);
        
        waitForWindow(2000);
        Set<String>  allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        logger.debug("Swtich to last window after loaing the detaile page of IN: " + lastWindowHandle);
        //Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Swtich to last window after loaing the detaile page of IN: " + lastWindowHandle);
		logger.debug("Exiting SeleniumTest.openINTicket...");    	
    }
    
    public static String createBreakGlassTicketInAINPage(WebDriver driver, String IN_Number, String fileName, int instanceNumber, int serverTimeDifference) throws IOException, ParseException{
    	SeleniumImplementation.openINTicket(driver, IN_Number, instanceNumber);
    	driver.switchTo().frame(3);
    	String actualStartDateIN = driver.findElement(By.id("df_8_0")).getText();
    	if(Utilities.isINExpiredForCreatingBGCR(actualStartDateIN)) {
			  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, IN_Number + " was created more than 4 days ago. Please create an new IN for the Break Glass CR Ticket...");
			  Utilities.setCRAutomationWebInstanceCompleted(instanceNumber);
			  SeleniumImplementation.waitForWindow(5000);
              return null;
              
    	}
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_2");
        driver.findElement(By.id("tabHyprlnk1_2")).click();            
        SeleniumImplementation.waitForElementById(driver,"imgBtn7");
        driver.findElement(By.id("imgBtn7")).click();
        SeleniumImplementation.waitForWindow(2000);
        
        
        //get Map items for Break Glass 
        Map<String, String> bgItems = Utilities.readBreakGlassTicketFromSpreadsheet(fileName);
        String lastWinowHandle = SeleniumImplementation.createBreakGlassTicket(driver, bgItems, instanceNumber, serverTimeDifference);
        waitForWindow(2000);  
        return lastWinowHandle;
    }
    
    
    public static void createBreakGlassTicket(WebDriver driver, String fileName, int instanceNumber, int serverTimeDifference) throws IOException, ParseException{
    	Map<String, String> items = Utilities.readBreakGlassTicketFromSpreadsheet(fileName);
    	createBreakGlassTicket(driver, items, instanceNumber, serverTimeDifference);
    }
    
  /*  public static void CreateStandardCRTicketAndSubmit(WebDriver driver, String fileName, int instanceNumber) throws IOException {
    	Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(fileName);
    	String CR_Number = createStandardCRTicket(driver, items, instanceNumber);
    	SeleniumImplementation.completeAllOtherCRTasksAndSubmit(driver, fileName, CR_Number);
    } */
    
    public static String createStandardCRTicket(WebDriver driver, String fileName, int instanceNumber, int serverDifferentHours) throws IOException, ParseException{
    	Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(fileName, serverDifferentHours);
    	String CR_Number = createStandardCRTicket(driver, items, instanceNumber);  
        return CR_Number;
    }
    
    public static String createStandardCRTicket(WebDriver driver, Map<String, String> items, int instanceNumber){
        Set<String>  allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        waitForWindow(8000); 
        driver.switchTo().frame(3);
    	// driver.switchTo().frame("menubar");	
        // driver.findElement(By.xpath("//frame[@name='product']/html/body"));
        driver.findElement(By.id("tbarDiv0"));
        logger.debug("Found element tbarDiv0");
        driver.findElement(By.id("tab_2001"));
        logger.debug("Found element tab_2001");
        driver.switchTo().frame("tab_2001");
       driver.findElement(By.id("menubar"));
       logger.debug("Found element menubar");
       driver.switchTo().frame("menubar");
       //driver.findElement(By.id("toolbar_0")).click();
       
      
   	    SeleniumImplementation.waitForElementById(driver,"toolbar_0");
   	 logger.debug("found the link toolbar_0");
    	driver.findElement(By.id("toolbar_0")).click();
    	logger.debug("Click the link");
    	SeleniumImplementation.waitForWindow(2000);
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle); 
        driver.switchTo().frame(3);
        
        String pageTitle = driver.getTitle();
        logger.debug("The title of new CR: " + pageTitle);
        while(pageTitle.indexOf("Create New Change Order") < 0) {
        	logger.debug("Still loading the page for the new ticket. Will wait for 2 more seconds...");
        	waitForWindow(2000);
        	pageTitle = driver.getTitle();
        	logger.debug("The title of new CR: " + pageTitle);       	
        }
        //Create New Change Order CR3376180 - CA Service Desk Manager
        String newCRNumber = pageTitle.substring("Create New Change Order ".length(), "Create New Change Order ".length() + 9);
        logger.info("Editing the new ticket " + newCRNumber + " with the values from the spreadsheet");
        String theCategory = items.get("CATEGORY").trim();
        //check element of Category
   	    SeleniumImplementation.waitForElementById(driver,"df_0_2");
   	    logger.debug("found element for category");
    	driver.findElement(By.id("df_0_2")).click();
    	driver.findElement(By.id("df_0_2")).clear();
    	SeleniumImplementation.waitForWindow(1000);
    	driver.findElement(By.id("df_0_2")).sendKeys(theCategory);
    	logger.debug("Set Category to: '" + theCategory + "'");
        
        SeleniumImplementation.editCR(driver, newCRNumber, items, true, instanceNumber);
   	//((JavascriptExecutor)driver).executeScript("window.parent.role_main.cai_main.setActKeyMenuState(0)");   	
        //((JavascriptExecutor)driver).executeScript("window.parent.cai_main.setActKeyMenuState(0) ");
       // SeleniumTest.editCR(CR_Number, items, false);
        return newCRNumber;
   }
    
    public static String createBreakGlassTicket(WebDriver driver, Map<String, String> items, int instanceNumber, int serverTimeDifference) throws ParseException {
        logger.debug("Entering SeleniumTest.createBreakGlassTicket...");            
    	SeleniumImplementation.waitForWindow(4000);
    	String requesterStr = items.get("REQUESTER");
    	String affectedUserStr = items.get("AFFECTED_END_USER");
    	String categoryStr = items.get("CATEGORY");
    	String owningGroupStr = items.get("OWNING_GROUP");
    	String supportingRegionStr = items.get("SUPPORTING_REGION");
    	String implementerStr = items.get("IMPLEMENTER");
    	String implementationTeamStr = items.get("IMPLEMENTING_TEAM");
    	String contactNameStr = items.get("CONTACT_NAME");
    	String contactInfoStr = items.get("CONTACT_INFORMATION");
    	String isProductionChangeStr = items.get("PRODUCTION_CHANGE");
    	String isEnduringChangeStr = items.get("ENDURING_CHANGE");
    	String projectIdStr = items.get("PROJECT_ID");
    	String verificationStartDateStr = items.get("VERIFICATION_START_DATE");
    	String serverVerificationStartDateStr = Utilities.getServerSideTimeFromLocalTime(verificationStartDateStr, serverTimeDifference);
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Creating a new Break Glass CR ticket ...");    	

    	Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        logger.debug("Swtich to last window after creating a new break glass CR ticket: " + lastWindowHandle);
        // Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Swtich to last window after creating a new break glass CR ticket: " + lastWindowHandle);
        
        driver.switchTo().frame(3);    	
        String pageTitle = driver.getTitle();
        logger.debug("The title of new CR: " + pageTitle);
        while(pageTitle.indexOf("Create New Change Order") < 0) {
        	logger.debug("Still loading the page for the new ticket. Will wait for 2 more seconds...");
        	waitForWindow(2000);
        	pageTitle = driver.getTitle();
        	logger.debug("The title of new CR: " + pageTitle);       	
        }
        //Create New Change Order CR3376180 - CA Service Desk Manager
        String newCRNumber = pageTitle.substring("Create New Change Order ".length(), "Create New Change Order ".length() + 9);
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "A new Break Glass CR ticket " +  newCRNumber + " was created. Editing the values of the ticket with those in the spreadsheet...");    	       
        //set reqester
        if(requesterStr != null && !requesterStr.trim().equals("")){
	        SeleniumImplementation.waitForElementById(driver,"df_0_0");
	        driver.findElement(By.id("df_0_0")).click();
	        driver.findElement(By.id("df_0_0")).clear(); 
	        driver.findElement(By.id("df_0_0")).sendKeys(requesterStr.trim());
	    	SeleniumImplementation.waitForWindow(1000);
        }
    	//set affected end user
        if(affectedUserStr != null && !affectedUserStr.trim().equals("")){
	        SeleniumImplementation.waitForElementById(driver,"df_0_1");
	    	driver.findElement(By.id("df_0_1")).click();
	    	SeleniumImplementation.waitForWindow(1000);
	    	driver.findElement(By.id("df_0_1")).clear();
	    	SeleniumImplementation.waitForWindow(2000);
	    	driver.findElement(By.id("df_0_1")).sendKeys(affectedUserStr.trim());
	    	SeleniumImplementation.waitForWindow(2000);
        }
    	//set Category
        if(categoryStr != null && !categoryStr.trim().equals("")){
	    	SeleniumImplementation.waitForElementById(driver,"df_0_2");
	    	driver.findElement(By.id("df_0_2")).click();
	    	// SeleniumTest.waitForWindow(1000);
	    	driver.findElement(By.id("df_0_2")).clear();
	    	SeleniumImplementation.waitForWindow(1000);
	    	driver.findElement(By.id("df_0_2")).sendKeys(categoryStr.trim());
	    	SeleniumImplementation.waitForWindow(2000);
        }
    	
    	//set Production Change
        if(isProductionChangeStr != null && !isProductionChangeStr.trim().equals("")){        
	    	SeleniumImplementation.waitForElementById(driver,"df_1_4");
	        Select productionChange = new Select(driver.findElement(By.id("df_1_4")));
	        productionChange.selectByVisibleText(isProductionChangeStr.trim().toUpperCase());
        }
       
    	//set Enduring change activities
        if(isEnduringChangeStr != null && !isEnduringChangeStr.trim().equals("")){
	    	SeleniumImplementation.waitForElementById(driver,"df_1_5");
	        Select enduringChange = new Select(driver.findElement(By.id("df_1_5")));
	        enduringChange.selectByVisibleText(isEnduringChangeStr.trim().toUpperCase()); 
        }
        //set owning group
        if(owningGroupStr != null && !owningGroupStr.trim().equals("")){
    	SeleniumImplementation.waitForElementById(driver,"df_2_1");
	        driver.findElement(By.id("df_2_1")).click();
	        driver.findElement(By.id("df_2_1")).clear();
	        driver.findElement(By.id("df_2_1")).sendKeys(owningGroupStr.trim());
        }
        //set support region
        if(supportingRegionStr != null && !supportingRegionStr.trim().equals("")){
    	SeleniumImplementation.waitForElementById(driver,"df_2_2");
	        driver.findElement(By.id("df_2_2")).click();
	        driver.findElement(By.id("df_2_2")).clear();
	        driver.findElement(By.id("df_2_2")).sendKeys(supportingRegionStr.trim());
        }
        
        //set implementer
        if(implementerStr != null && !implementerStr.trim().equals("")){
    	SeleniumImplementation.waitForElementById(driver,"df_2_3");
	        driver.findElement(By.id("df_2_3")).click();
	        driver.findElement(By.id("df_2_3")).clear();
	        driver.findElement(By.id("df_2_3")).sendKeys(implementerStr.trim());
        }
        
        //set implementation group
        if(implementationTeamStr != null && !implementationTeamStr.trim().equals("")){
	    	SeleniumImplementation.waitForElementById(driver,"df_2_4");
	        driver.findElement(By.id("df_2_4")).click();
	        driver.findElement(By.id("df_2_4")).clear();
	         waitForWindow(2000);
        driver.findElement(By.id("df_2_4")).sendKeys(implementationTeamStr.trim());
        }
        
        //set contact name
        if(contactNameStr != null && !contactNameStr.trim().equals("")){
	    	SeleniumImplementation.waitForElementById(driver,"df_5_0"); 		
	        driver.findElement(By.id("df_5_0")).click();
	        driver.findElement(By.id("df_5_0")).clear();
	        driver.findElement(By.id("df_5_0")).sendKeys(contactNameStr.trim());
        }
        
        //set contact information
        if(contactInfoStr != null && !contactInfoStr.trim().equals("")){
	    	SeleniumImplementation.waitForElementById(driver,"df_6_0"); 		
	        driver.findElement(By.id("df_6_0")).click();
	        driver.findElement(By.id("df_6_0")).clear();
	        driver.findElement(By.id("df_6_0")).sendKeys(contactInfoStr.trim());
        }
        //set Verification Start Date
        if(verificationStartDateStr != null && !verificationStartDateStr.trim().equals("")){
	    	SeleniumImplementation.waitForElementById(driver,"df_9_0");
	    	driver.findElement(By.id("df_9_0")).click();
	    	driver.findElement(By.id("df_9_0")).clear();
	    	driver.findElement(By.id("df_9_0")).sendKeys(serverVerificationStartDateStr.trim());  
        }
        
       	SeleniumImplementation.waitForElementById(driver,"imgBtn0");
    	// Click Save Button
        driver.findElement(By.id("imgBtn0")).click();
    	waitForWindow(4000);     
    	if(SeleniumImplementation.checkIfSaveSuccessByAltermsgTxt(driver)){
	        logger.info( "The new created Break Glass CR Ticket " + newCRNumber + " was saved successfully!");
 			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The new created Break Glass CR Ticket " + newCRNumber + " was saved successfully!");
   	} else {
    		logger.info("The new created Break Glass CR Ticket " + newCRNumber + " was saved successfully! Please check the spreadsheet if there are values that are not valid...");
 			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The new created Break Glass CR Ticket " + newCRNumber + " was saved successfully! Please check the spreadsheet if there are values that are not valid...");
   		
   	}   	
        logger.debug("Exiting SeleniumTest.createBreakGlassTicket...");   
        return lastWindowHandle;
    }
    
    public static boolean openCRTicket(WebDriver driver, String CR_Number, int instanceNumber){
		logger.debug("Entering SeleniumTest.openCRTicket...");
        //driver.findElement(By.id("ticket_type")).click();
        // 11 | select | id=ticket_type | label=Change Order | 
        // 10 | click | id=ticket_type |  |
        try {
            driver.switchTo().frame(0);
            waitForWindow(2000);
            SeleniumImplementation.waitForElementById(driver,"ticket_type");
	    	logger.debug("Logging into Service Desk Manager was successful...");
	    	Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Logging into Service Desk Manager was successful...");
	        logger.info("Loading the page of Change Order Details for " + CR_Number); 
			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Loading the page of Change Order Details for " + CR_Number);
        } catch(Exception e) {
        	e.printStackTrace();
        	logger.error("Logging into GSD was not successful. UserID or Password might be wrong. Please try again...");
        	Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Logging into Service Desk Manager was not successful. Your userID or password provided might not be correct. Please try again...");
        	// Utilities.setCRAutomationWebInstanceCompleted(instanceNumber);
        	return false;
        } 

      {
          WebElement dropdown = driver.findElement(By.id("ticket_type"));
          dropdown.findElement(By.xpath("//option[. = 'Change Order']")).click();
        }
        // 12 | click | css=option:nth-child(1) |  
        waitForWindow(2000); 
        SeleniumImplementation.waitForElementByCssSelector(driver,"option:nth-child(1)");
        driver.findElement(By.cssSelector("option:nth-child(1)")).click();
        logger.debug("Select Change Order from the dropdown...");
        waitForWindow(2000);
        SeleniumImplementation.waitForElementByName(driver,"searchKey");
        driver.findElement(By.name("searchKey")).click();
        logger.debug("Enter CR Number...");
        driver.findElement(By.name("searchKey")).sendKeys(CR_Number);
        SeleniumImplementation.waitForElementByCssSelector(driver,"#imgBtn0 > span");
        driver.findElement(By.cssSelector("#imgBtn0 > span")).click();
        logger.debug("Click Go button to load the page of CR details ...");
        waitForWindow(2000);
        Set<String>  allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
		logger.debug("Exiting SeleniumTest.openCRTicket...");
		return true;
    }
    
    public static boolean checkIfElementLoaded(WebDriver driver, String elementId, String elementName, int maxCheckTimes, boolean areMessagesDisabled){
		logger.debug("Entering SeleniumTest.checkIfElementLoaded...");
   	    boolean result = true;
        int count = 0;
        while(!(driver.findElements(By.id(elementId)).size() > 0)){
        	if(!areMessagesDisabled){
        	  logger.debug("Element for " + elementName + " has not been loaded yet...");
        	}
        	waitForWindow(2000);
        	count++;
        	if(count == maxCheckTimes - 1) {
        		if(!areMessagesDisabled){
        			logger.debug("Element for " + elementName + " has not been loaded yet. Please check performance of your computer. You may need to restart you laptop and run it again");
        		}
        		result = false;
        		break;
        	}
        }    	
		logger.debug("Exiting SeleniumTest.checkIfElementLoaded...");
    	return result;
    }
    
    public static void closeAll(WebDriver driver){
    	try{
	    	if(driver != null) {
	    		//driver.close();
	    		 driver.quit();
	    	}
    	}catch(Exception e){
    		logger.error("Exception happened when closing the Web driver ... ");
    		e.printStackTrace();
    	}
    }
    
    public static String getProjectIDFromPage(WebDriver driver, String CR_Number){
    	String projectId = "";
        logger.debug("Entering SeleniumTest.getProjectIDFromPage...");            
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
       // driver.switchTo().window(lastWindowHandle);
        String mainWindowHandle = lastWindowHandle;
        // driver.switchTo().frame(3);
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        waitForWindow(2000);
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_7");
        driver.findElement(By.id("tabHyprlnk1_7")).click();
        waitForWindow(5000);
        SeleniumImplementation.waitForElementById(driver,"nbtab_1_7");
        int size = driver.findElements(By.xpath("//*[@id='nbtab_1_7']/table/tbody/tr[2]/td")).size();
        logger.debug("Size of td tags for Project ID: " + size);
        if(size == 3){
        	projectId = driver.findElement(By.xpath("//*[@id='nbtab_1_7']/table/tbody/tr[2]/td[1]")).getText();
        }      
        	logger.info("Project ID from page: " + projectId);
        	logger.debug("Entering SeleniumTest.getProjectIDFromPage...");          
    	return projectId;
    }
    public static Object[][] getCRTicketInfoFromPage(WebDriver driver, String CR_Number, int serverTimeDifference) throws IOException, ParseException{
		logger.debug("Entering SeleniumTest.getCRTicketInfoFromPage...");
		String categroyStr = driver.findElement(By.id("df_0_2")).getText();
		String suportingRegion = driver.findElement(By.id("df_2_2")).getText();
		String owningGroup = driver.findElement(By.id("df_2_1")).getText();
		String implementingGroup = driver.findElement(By.id("df_2_4")).getText();
		String implementer = driver.findElement(By.id("df_2_3")).getText();
		String crSummary = driver.findElement(By.id("df_3_0")).getText();
		String crDescription = driver.findElement(By.id("df_4_0")).getText();
		String contactInfo = driver.findElement(By.id("df_6_0")).getText();
		
		String sched_start_date = driver.findElement(By.id("df_7_0")).getText();
		String sched_duration = driver.findElement(By.id("df_7_1")).getText();
		String verification_start_date = driver.findElement(By.id("df_9_0")).getText();
		String verification_duration = driver.findElement(By.id("df_9_1")).getText();
		String backout_duration = driver.findElement(By.id("df_9_3")).getText();
		String businessJustification = driver.findElement(By.id("df_12_0")).getText();
		String requirePAR = driver.findElement(By.id("df_13_0_z_par_acc")).getText();
		String outsideOfWindow = driver.findElement(By.id("df_14_0_z_imp_hr")).getText();
		String itServiceAcceptance = driver.findElement(By.id("df_15_0_z_itsa_res")).getText();
			logger.debug("Category: " + categroyStr);
			logger.debug("suportingRegion: " + suportingRegion);
			logger.debug("owningGroup: " + owningGroup);
			logger.debug("implementingGroup: " + implementingGroup);
			logger.debug("implementer: " + implementer);		
			logger.debug("crSummary: " + crSummary);		
			logger.debug("crDescription: " + crDescription);		
			logger.debug("contactInfo: " + contactInfo);		
			logger.debug("sched_start_date: " + sched_start_date);		
			logger.debug("sched_duration: " + sched_duration);		
			logger.debug("verification_start_date: " + verification_start_date);
			// verification_duration = "23:30:00";
			logger.debug("verification_duration: " + verification_duration);		
			logger.debug("backout_duration: " + backout_duration);		
			logger.debug("businessJustification: " + businessJustification);		
			logger.debug("requirePAR: " + requirePAR);		
			logger.debug("outsideOfWindow: " + outsideOfWindow);		
			logger.debug("itServiceAcceptance: " + itServiceAcceptance);
		
		String[] data1 = { "Scheduled Start Date",
				          "Schedule Duration",
				          "Verification Start Date",
				          "Verification Duration",
				          "Backout Duration",
				          "Need By Date",
				          "Actual Implementation Start Date",
				          "Actual Implementation End Date",
				          "Change Order Summary",
				          "Change Order Description",
				          "Contact Information and Instructions",
				          "Business justification for the change",
				          "Requester",
				          "Affected End User",
				          "Owning Group",
				          "Supporting Region",
				          "Implementer",
				          "Implementing Team",
				          "Require PAR",
				          "Will the change be executed during online hours and outside a scheduled maintenance window ?",
				          "Have you completed IT Service Acceptance to support this Change Record implementation ?",
				          "Project ID",
				          "Category"
		};
		
		Object[][] data = new Object[23][2];
		for(int i = 0; i < 23; i++){
			data[i][0] = data1[i];
		}
		
		
		// logger.debug("verification_duration: " + verification_duration + " converted time: " + DateUtil.convertTime(verification_duration));
		logger.debug("backout_duration: " + backout_duration + "converted time: " + DateUtil.convertTime(backout_duration));
		String projectId = SeleniumImplementation.getProjectIDFromPage(driver, CR_Number);
		String local_sched_start_date = Utilities.getLocalTimeFromServerSideTime(sched_start_date, serverTimeDifference);
		Date scheduledStartDate = Utilities.convertDateTimeStringToDate(local_sched_start_date);
		data[0][1] = scheduledStartDate;
		data[1][1] = Utilities.convertDurationStrToHours(sched_duration);
		logger.debug("scheduled duration hours: " + data[1][1]);
		String local_verification_start_date = Utilities.getLocalTimeFromServerSideTime(verification_start_date, serverTimeDifference);
		data[2][1] = Utilities.convertDateTimeStringToDate(local_verification_start_date);
		// data[3][1] = DateUtil.convertTime(verification_duration);
		double verification_duration_hours = Utilities.convertDurationStrToHours(verification_duration);
		logger.debug("Verification duration hours: " + data[3][1]);
		if(verification_duration_hours < 24) {
			data[3][1] = DateUtil.convertTime(verification_duration);
		} else {
			data[3][1] = verification_duration_hours;			
		}
		data[4][1] = DateUtil.convertTime(backout_duration);
		data[5][1] = "  ";
		data[6][1] = "  ";
		data[7][1] = "  ";
		data[8][1] = crSummary;
		data[9][1] = crDescription;
		data[10][1] = contactInfo;
		data[11][1] = businessJustification;
		data[12][1] = "  ";
		data[13][1] = "  ";
		data[14][1] = owningGroup;
		data[15][1] = suportingRegion;
		data[16][1] = implementer;
		data[17][1] = implementingGroup;
		data[18][1] = requirePAR;
		data[19][1] = outsideOfWindow;
		data[20][1] = itServiceAcceptance;
		data[21][1] = projectId;
		data[22][1] = categroyStr;
	/*	String fileName = "c:\\temp\\" + CR_Number + "-Info.xlsx";
		String sheetName = "CR Ticket Information";
		Utilities.writeToSpreadsheet(data, sheetName, fileName);
		*/
		logger.debug("Exiting SeleniumTest.getCRTicketInfoFromPage..."); 
		return data;
    }
    
    public static String[][] getPAMTemplatesFromPage(WebDriver driver, String CR_Number) throws IOException{
    	ArrayList<String[]> list = new ArrayList<String[]>();
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        String mainWindowHandle = lastWindowHandle;
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3); 
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk3");
        driver.findElement(By.id("accrdnHyprlnk3")).click();
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk3_4");
        driver.findElement(By.id("tabHyprlnk3_4")).click();
        waitForWindow(2000); 
        SeleniumImplementation.waitForElementById(driver,"tbl202"); 
        String entriesStr = driver.findElement(By.xpath("//*[@id='tbl202']/tbody/tr[1]/th")).getText();
        int entryNumber = 0;
        if(entriesStr != null && entriesStr.contains("Entries")){
        	SeleniumImplementation.waitForElementByPartialLinkText(driver,"Search PAR");
        	driver.findElement(By.partialLinkText("Search PAR")).click();
        	SeleniumImplementation.waitForWindow(2000);
            allWindowHandles = driver.getWindowHandles();
            lastWindowHandle = getLastWindowHandle(allWindowHandles);
            driver.switchTo().window(lastWindowHandle);
            driver.switchTo().frame("cai_main"); 
            int maxPage = 1;
            int currentPage = 1;
        	
            SeleniumImplementation.waitForElementById(driver,"dataGrid");
            int size = driver.findElements(By.xpath("//*[@id='dataGrid']/tbody/tr")).size();
            logger.debug("Size of PAM Templates in the page: " + size);
            if(size > 1) {
            	for(int i = 1; i < size; i++){
            		String[] template = new String[3];
            		template[0] = driver.findElement(By.xpath("//*[@id='dataGrid']/tbody/tr[" + (i+1) + "]/td[4]")).getText();
            		template[1] = driver.findElement(By.xpath("//*[@id='dataGrid']/tbody/tr[" + (i+1) + "]/td[2]")).getText();
            		template[2] = driver.findElement(By.xpath("//*[@id='dataGrid']/tbody/tr[" + (i+1) + "]/td[5]")).getText();
            		logger.debug(i + ": PAM Location: " + template[1] + " CI: " + template[0] + " Group: " + template[2]);
            		list.add(template);
            	}
            }
 
            if(SeleniumImplementation.tryToFindElementById(driver,"dataGrid_toppager_center")){
            	String maxPageStr = driver.findElement(By.xpath("//*[@id='dataGrid_toppager_center']/table/tbody/tr/td[4]/span")).getText();
            	maxPage = Integer.parseInt(maxPageStr);
            }
            logger.debug("Max Page: '" + maxPage + "'");        
        	logger.debug("Current Page: '" + currentPage + "'");

            
            while(currentPage < maxPage){
    	        currentPage++;
            	if(SeleniumImplementation.tryToFindElementById(driver,"next_t_dataGrid_toppager")){
    	        	logger.debug("The button for the next page of PAM Templates exists. Click the button");
    	        	driver.findElement(By.xpath("//*[@id='next_t_dataGrid_toppager']/a")).click();
    	        	SeleniumImplementation.waitForWindow(2000);
    	            SeleniumImplementation.waitForElementById(driver,"dataGrid");
    	            size = driver.findElements(By.xpath("//*[@id='dataGrid']/tbody/tr")).size();
                	logger.debug("currentPage: " + currentPage);
    	            logger.debug("Size of PAM Templates in the page: " + size);
    	            if(size > 1) {
    	            	for(int i = 1; i < size; i++){
    	            		String[] template = new String[3];
    	            		template[0] = driver.findElement(By.xpath("//*[@id='dataGrid']/tbody/tr[" + (i+1) + "]/td[4]")).getText();
    	            		template[1] = driver.findElement(By.xpath("//*[@id='dataGrid']/tbody/tr[" + (i+1) + "]/td[2]")).getText();
    	            		template[2] = driver.findElement(By.xpath("//*[@id='dataGrid']/tbody/tr[" + (i+1) + "]/td[5]")).getText();
    	            		logger.debug(i + ": PAM Location: " + template[0] + " CI: " + template[1] + " Group: " + template[2]);
    	            		list.add(template);
    	            	}
    	            }
    	        }            
            }
        }

        String[][] templates = null;
        int listSize = list.size();
        if(listSize > 0){
        	templates = new String[listSize][3];
        	for(int i = 0; i < listSize; i++){
        		templates[i] = list.get(i);
        	}
        }  
        return templates;
    }
    
    
    public static String[][] getCIsFromPage(WebDriver driver, String CR_Number) throws IOException{
    	ArrayList<String> list = new ArrayList<String>();
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        String mainWindowHandle = lastWindowHandle;
        driver.switchTo().window(lastWindowHandle);
        logger.debug("Switch to frame 3");
        driver.switchTo().frame(3); 
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_2");
        driver.findElement(By.id("tabHyprlnk1_2")).click();
        waitForWindow(2000); 
        logger.debug("Switch to frame chgnr_iframe");
        driver.switchTo().frame("chgnr_iframe");   
        SeleniumImplementation.waitForElementById(driver,"dataGrid");
        int maxPage = 1;
        if(SeleniumImplementation.tryToFindElementById(driver,"dataGrid_toppager_center")){
        	String maxPageStr = driver.findElement(By.xpath("//*[@id='dataGrid_toppager_center']/table/tbody/tr/td[4]/span")).getText();
        	maxPage = Integer.parseInt(maxPageStr);
        }
        int currentPage = 1;
    	logger.debug("Max Page: '" + maxPage + "'");        
    	logger.debug("Current Page: '" + currentPage + "'");        
    	int size = driver.findElements(By.xpath("//*[@id='dataGrid']/tbody/tr")).size();
    	logger.debug("number of CIs in the page: " + (size - 1));
       // if(debugEnabled)  System.out.println("size of tr: " + size);
        if(size > 1){
	        for(int i = 1; i < size; i++){
	        	String ci = driver.findElement(By.xpath("//*[@id='dataGrid']/tbody/tr[" +(i+1) + "]/td/a")).getText();
	        	logger.debug("CI: '" + ci + "'");
	        	list.add(ci);
	        	
	        }
        } else {
        	logger.info("There is no CI created in the ticket");
        }
        while(currentPage < maxPage){
	        currentPage++;
        	if(SeleniumImplementation.tryToFindElementById(driver,"next_t_dataGrid_toppager")){
	        	logger.debug("There is next page of CIs. Clicking the next page button ...");
	        	driver.findElement(By.xpath("//*[@id='next_t_dataGrid_toppager']/a")).click();
	        	SeleniumImplementation.waitForWindow(2000);
	        	SeleniumImplementation.waitForElementById(driver,"dataGrid");
	            size = driver.findElements(By.xpath("//*[@id='dataGrid']/tbody/tr")).size();
	        	logger.debug("currentPage: " + currentPage);
	            logger.debug("number of CIs in the page: " + (size - 1));
	            if(size > 1){
		            for(int i = 1; i < size; i++){
		            	String ci = driver.findElement(By.xpath("//*[@id='dataGrid']/tbody/tr[" +(i+1) + "]/td/a")).getText();
		            	logger.debug("CI: '" + ci + "'");
		            	list.add(ci);
		            }	
	            }
	        } 

        }
        String[][] cis = null;
        int listSize = list.size();
        if(listSize > 0){
        	cis = new String[listSize][1];
        	for(int i = 0; i < listSize; i++){
        		cis[i][0] = list.get(i);
        	}
        }
        return cis;
    }
    
    public static Object[][] getImplementationTasksFromPage(WebDriver driver, String CR_Number, int serverTimeDifference) throws IOException, ParseException{
		logger.debug("Entering SeleniumTest.getImplementationTasksFromPage...");
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        String mainWindowHandle = lastWindowHandle;
       // driver.switchTo().frame(3);       
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_4");
        driver.findElement(By.id("tabHyprlnk1_4")).click();
        logger.debug("Click the attachment...");
        waitForWindow(5000); 
        driver.switchTo().frame("attmnt_iframe");       
        if(!SeleniumImplementation.tryToFindElementByPartialLinkText(driver,"Implementation Plan")){
        	logger.info("There is no implementaion plan in the ticket!");
            logger.debug("Exiting SeleniumTest.getImplementationTasksFromPage...");
        	return null;
        }
        SeleniumImplementation.waitForElementByPartialLinkText(driver,"Implementation Plan");
        driver.findElement(By.partialLinkText("Implementation Plan")).click();
        waitForWindow(2000);
        allWindowHandles = driver.getWindowHandles(); 
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle); 
	    JavascriptExecutor js = (JavascriptExecutor) driver;  
        // js.executeScript("toggle_visibility('tbl1','lnk1','hid1')");
      //  SeleniumTest.waitForElementById(driver,"tbl1");
       // System.out.println("tbl1 was found!!!");    
      //  String taskDescription = driver.findElement(By.xpath("//*[@id='tbl1']/td/textarea[2]")).getText();
       // System.out.println("Task Descrition: " + taskDescription);
       int size = driver.findElements(By.xpath("//*[contains(@id,'tbl')]")).size();
       logger.debug("tbl size: " + size);
       Object[][] tasks = new Object[size][6];
       Date firstDate = new Date();
       String firstStartDateStr = "";
       for(int i = 1; i < size + 1; i++){
    	   String jsStr = "toggle_visibility('tbl" + i + "','lnk" + i + "','hid" + i + "')";
    	   logger.debug("Javascript String: " + jsStr);
           js.executeScript(jsStr);   
           SeleniumImplementation.waitForElementById(driver,"tbl" + i);
           logger.debug("tbl" + i + " was found!!!");
           tasks[i-1][0] = "" + i;
           String taskStartDateStr = driver.findElement(By.xpath("//*[@id='taskListTable']/tr[" + (1 + 2 * (i - 1)) + "]/td[5]")).getText();
           String taskEndDateStr = driver.findElement(By.xpath("//*[@id='taskListTable']/tr[" + (1 + 2 * (i - 1)) + "]/td[6]")).getText();
          // tasks[i-1][1] = driver.findElement(By.xpath("//*[@id='taskListTable']/tr[" + (1 + 2 * (i - 1)) + "]/td[5]")).getText();
           // tasks[i-1][2] = driver.findElement(By.xpath("//*[@id='taskListTable']/tr[" + (1 + 2 * (i - 1)) + "]/td[6]")).getText();
           Date date1 = Utilities.convertDateTimeStringToDate(taskStartDateStr);
           Date date2 = Utilities.convertDateTimeStringToDate(taskEndDateStr);
           tasks[i-1][1] = Utilities.convertDateTimeStringToDate(taskStartDateStr);
           double diffHours1 = 0;
           double diffHours2 = 0;
           if(i == 1) {
        	   firstDate = date1;
        	   firstStartDateStr = Utilities.getLocalTimeFromServerSideTime(taskStartDateStr, serverTimeDifference);
        	   tasks[i-1][1] = Utilities.convertDateTimeStringToDate(firstStartDateStr);
        	   // tasks[i-1][1] = Utilities.convertDateTimeStringToDate(taskStartDateStr);
        	   diffHours2 = Utilities.getSecondsOfDifferenceBetweenTwoDates(firstDate, date2)/3600.0000;
        	   tasks[i-1][2] = "=B1+"+ diffHours2 + "/24";  
           } else {
        	   diffHours1 = Utilities.getSecondsOfDifferenceBetweenTwoDates(firstDate, date1)/3600.0000;
        	   diffHours2 = Utilities.getSecondsOfDifferenceBetweenTwoDates(firstDate, date2)/3600.0000;
        	   tasks[i-1][1] = "=B1+"+ diffHours1 + "/24";  
        	   tasks[i-1][2] = "=B1+"+ diffHours2 + "/24";  
        	   
           }
           // tasks[i-1][2] = Utilities.convertDateTimeStringToDate(taskEndDateStr);
          // tasks[i-1][3] = "  ";
           
           tasks[i-1][3] = driver.findElement(By.xpath("//*[@id='taskListTable']/tr[" + (1 + 2 * (i - 1)) + "]/td[3]")).getText();
           tasks[i-1][4] = driver.findElement(By.xpath("//*[@id='tbl" + i + "']/td/textarea[2]")).getText();
           // tasks[i-1][6] = "  ";
           tasks[i-1][5] = driver.findElement(By.xpath("//*[@id='taskListTable']/tr[" + (1 + 2 * (i - 1)) + "]/td[4]")).getText();
           
       }
       
       SeleniumImplementation.waitForElementByPartialLinkText(driver,"Close Window");
       driver.findElement(By.partialLinkText("Close Window")).click();
       SeleniumImplementation.waitForWindow(2000);
       driver.switchTo().window(mainWindowHandle); 
       // Utilities.writeToSpreadsheet(tasks, "Implementation Plan", "c:\\temp\\" + CR_Number + "-Info.xlsx");
	   logger.info("Got information of " + size + " tasks from the pages");	
       logger.debug("Exiting SeleniumTest.getImplementationTasksFromPage...");
		return tasks;
    }
    
    public static boolean openPlanToEdit(WebDriver driver, String CR_Number, int instanceNumber) throws Exception {  
        logger.debug("Entering SeleniumImplementation.openPlanToEdit ..." );
    	SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_3");
        driver.findElement(By.id("tabHyprlnk1_3")).click();
        logger.debug("Click the attachment...");
        waitForWindow(5000); 
        // driver.switchTo().frame(1);
        driver.switchTo().frame("attmnt_iframe");
        boolean isPlanExisting = SeleniumImplementation.tryToFindElementByPartialLinkText(driver,"Implementation Plan");
        if(!isPlanExisting){
        	logger.debug("No implementation plan was found. Skip opening plan and return");
            logger.debug("Entering SeleniumImplementation.openPlanToEdit ..." );        	
        	return isPlanExisting;
        }
        // SeleniumTest.waitForElementByPartialLinkText(driver,"Implementation Plan");
        logger.debug("Activate context menu for editing the implementation plan");
    	Actions action = new Actions(driver);
    	// WebElement element = driver.findElement(By.id("df_26_0"));
    	WebElement element = driver.findElement(By.partialLinkText("Implementation Plan"));
    	action.moveToElement(element).perform();
    	logger.debug("Select Edit from the context menu");
        // driver.findElement(By.id("ctx_1_2")).click();
        SeleniumImplementation.waitForElementByLinkText(driver,"Edit");
        driver.findElement(By.linkText("Edit")).click();
        logger.info("Loading the editable page of the implementation plan...");
 		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Loading the editable page of the implementation plan...");
        // waitForWindow(runningSpeed * speedUnit);
        waitForWindow(2000);
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        logger.debug("Exiting SeleniumImplementation.openPlanToEdit ..." );
       return isPlanExisting;
    }
    
    public static void removePlan(WebDriver driver, String CR_Number, int instanceNumber) throws Exception {
		logger.debug("Entering SeleniumTest.removePlan...");
            Set<String> allWindowHandles = driver.getWindowHandles();
            String lastWindowHandle = getLastWindowHandle(allWindowHandles);
            String mainWindowHandle = lastWindowHandle;
            if(!openPlanToEdit(driver, CR_Number, instanceNumber)){
            	logger.info("There is no implementation plan to delete...");
     			Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "There is no implementation plan to delete...");
            	logger.debug("Exiting SeleniumTest.removePlan...");
            	return;
            }

            SeleniumImplementation.waitForElementById(driver,"delete");
            logger.info("Cick the Delete button to delete the implementation plan...");
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Cick the Delete button to delete the implementation plan...");
            driver.findElement(By.id("delete")).click();
            waitForWindow(2000);
            logger.info("Confirm to delete the implementation plan");
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Confirm to delete the implementation plan");
              Alert alert = driver.switchTo().alert();
              alert.sendKeys("DELETE");
              waitForWindow(2000);
              alert.accept();
              waitForWindow(1000);
             // driver.close();
               driver.switchTo().window(mainWindowHandle); 
              driver.switchTo().frame(3);                  
              logger.info("Refreshing the page...");
              SeleniumImplementation.refreshPage(driver);
              logger.info("The Implemation Plan was deleted from the ticket " + CR_Number);
   			  Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The Implemation Plan was deleted from the ticket " + CR_Number);

		logger.debug("Exiting SeleniumTest.removePlan...");
   }
    
    public static void saveCRTicketToSpreadsheet(WebDriver driver, Object[][] crTicketInfo, Object[][] tasks, String[][] cis, String[][] templates, String fileName) throws IOException{
    	Object[][] data = new Object[4][2];
    	data[0][0] = crTicketInfo;
    	data[0][1] = SeleniumImplementation.SHEET_NUMBER_STANDARD_CR_TICKET;
    	data[1][0] = tasks;
    	data[1][1] = SeleniumImplementation.SHEET_NUMBER_IMPLEMENTATION_PLAN;
    	data[2][0] = cis;
    	data[2][1] = SeleniumImplementation.SHEET_NUMBER_CIS;
    	data[3][0] = templates;
    	data[3][1] = SeleniumImplementation.SHEET_NUMBER_PAM_TEMPLATES;
    	//Utilities.writeDataToSheets(data, fileName);  
    	Utilities.updateDataToSheets(data, fileName);
    }
    
    public static void refreshPage(WebDriver driver) throws Exception {
		logger.debug("Entering SeleniumTest.refreshPage...");
		logger.debug("Start the context menu for refreshing the page");
        ((JavascriptExecutor)driver).executeScript("window.parent.cai_main.setActKeyMenuState(1) ");
        waitForWindow(1000);
        Set<String> allWindowHandles  = driver.getWindowHandles(); 
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        logger.debug("Click Refresh in the context menu to refresh the page");
        SeleniumImplementation.waitForElementByLinkText(driver,"Refresh");
    	driver.findElement(By.linkText("Refresh")).click(); 
    	waitForWindow(2000);    	
		logger.debug("Exiting SeleniumTest.refreshPage...");
    }
    
    public static void copyCR(WebDriver driver, Map<String, String> items, int instanceNumber) throws Exception{
		logger.debug("Entering SeleniumTest.copyCR...");
    	//activate the context menu
    	logger.info("Start the context menu for copying the CR");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Start the context menu for copying the CR...");
        ((JavascriptExecutor)driver).executeScript("window.parent.cai_main.setActKeyMenuState(0) ");
        waitForWindow(2000);
        //Select copy   
        logger.info("Click Copy in the context menu to copy the CR");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Click Copy in the context menu to copy the CR...");
        SeleniumImplementation.waitForElementByLinkText(driver,"Copy");
    	driver.findElement(By.linkText("Copy")).click(); 
        //waitForWindow(runningSpeed * speedUnit * 2);
    	waitForWindow(2000);
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3); 

        String pageTitle = driver.getTitle();
        logger.debug("The title of new CR: " + pageTitle);
        while(pageTitle.indexOf("Update Change Order") < 0) {
        	logger.debug("Still loading the page for the new copied ticket. Will wait for 2 more seconds...");
        	waitForWindow(2000);
        	pageTitle = driver.getTitle();
        	logger.debug("The title of new CR: " + pageTitle);       	
        }
        
        String copiedCRNumber = pageTitle.substring(0, pageTitle.indexOf("Update Change Order")).trim();
        logger.info("Editing the new ticket " + copiedCRNumber + " with the values from the spreadsheet");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Editing the new ticket " + copiedCRNumber + " with the values from the spreadsheet");
        SeleniumImplementation.editCR(driver, copiedCRNumber, items, true, instanceNumber);
		logger.debug("Exiting SeleniumTest.copyCR...");
    }
 
    public static void addDeploymentTool(WebDriver driver, String packageId, String deploymentTool, int instanceNumber){
        logger.debug("Entering SeleniumTest.addDeploymentTool...");
        boolean isPackageIDorDeploymentToolNullOrEmpty = false;
        if(packageId == null || packageId.trim().equals("")){
        	logger.error("Package ID can not be null or empty...");
        	Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Package ID can not be null or empty. Please check the spreadsheet ...");
        	isPackageIDorDeploymentToolNullOrEmpty = true;
        }
 
        if(packageId == null || packageId.trim().equals("")){
        	logger.error("Deployment Tool can not be null or empty...");
           	Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Deployment Tool can not be null or empty. Please check the spreadsheet ...");
        	isPackageIDorDeploymentToolNullOrEmpty = true;
        }        
        
        if(isPackageIDorDeploymentToolNullOrEmpty){
        	logger.error("Package ID and Deployment Tool were not set ...");
           	Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Package ID and Deployment Tool were not set ...");
        	return;
        }
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        String mainWindowHandle = lastWindowHandle;
        driver.switchTo().frame("cai_main");
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        waitForWindow(1000);
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_8");
        driver.findElement(By.id("tabHyprlnk1_8")).click();
        waitForWindow(2000);
        
        //driver.switchTo().frame(0);   	
        logger.debug("Check if Add Projects button is available...");
        // SeleniumTest.waitForElementById(driver,"imgBtn12");
        // SeleniumTest.waitForElementByPartialLinkText(driver,"Add(");
        logger.debug("Add($) button is avaible. Click the Add($) button...");
        //driver.findElement(By.id("imgBtn13")).click();  
        JavascriptExecutor js = (JavascriptExecutor) driver;  
        js.executeScript("ImgBtnExecute(15)"); 
        waitForWindow(2000); 
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame("cai_main");
        logger.debug("Check if Package ID input is available...");
        SeleniumImplementation.waitForElementByName(driver,"SET.pc_id");
        logger.debug("Package ID input is available. Set its value...");
        driver.findElement(By.name("SET.pc_id")).click();
        driver.findElement(By.name("SET.pc_id")).clear();
        driver.findElement(By.name("SET.pc_id")).sendKeys(packageId);
        waitForWindow(1000); 
        logger.debug("Check if Deployment Tool input is available...");
        SeleniumImplementation.waitForElementByName(driver,"KEY.pc_instance");
        logger.debug("Deployment Tool input is available. Set its value...");
        driver.findElement(By.name("KEY.pc_instance")).click();
        driver.findElement(By.name("KEY.pc_instance")).clear();
        driver.findElement(By.name("KEY.pc_instance")).sendKeys(deploymentTool);
        waitForWindow(5000); 
        if(SeleniumImplementation.tryToFindElementByPartialLinkText(driver,"Save")){
        	driver.findElement(By.partialLinkText("Save")).click();
            driver.switchTo().window(mainWindowHandle);
            driver.switchTo().frame("cai_main");
            SeleniumImplementation.waitForElementById(driver,"alertmsgText");
            String saveResult = driver.findElement(By.id("alertmsgText")).getText();
            if(saveResult.contains("Save Successful")) {
                logger.info("Deployment Tool " + deploymentTool + " was added to the ticket successfully");
               	Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Deployment Tool " + deploymentTool + " was added to the ticket successfully");
            }
        } else {
        	logger.error("Save Button is not available on the page. Please check...");
        }
    	   
       logger.debug("Exiting SeleniumTest.addDeploymentTool...");        
    }    

    public static void addProject(WebDriver driver, String projectStr, int instanceNumber, String injectedLastWindowHandle){
        logger.debug("Entering SeleniumTest.addProject...");            
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        if(injectedLastWindowHandle != null && !injectedLastWindowHandle.trim().equals("")) {
        	lastWindowHandle = injectedLastWindowHandle;
        }
       // driver.switchTo().window(lastWindowHandle);
        String mainWindowHandle = lastWindowHandle;
        // driver.switchTo().frame(3);
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        waitForWindow(1000);
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_8");
        driver.findElement(By.id("tabHyprlnk1_8")).click();
        waitForWindow(2000);
        
        //driver.switchTo().frame(0);   	
        logger.debug("Check if Add Projects button is available...");
        // SeleniumTest.waitForElementById(driver,"imgBtn12");
        SeleniumImplementation.waitForElementByPartialLinkText(driver,"Add Pro");
        logger.debug("Add Projects button is avaible. Click \"Add Projects\" button...");
        //driver.findElement(By.id("imgBtn12")).click();    
        waitForWindow(4000);
	    JavascriptExecutor js = (JavascriptExecutor) driver;  
        //js.executeScript("ImgBtnExecute(12)");
        String linkName = driver.findElement(By.partialLinkText("Add Pro")).getAttribute("name");
        String jsExecuteNumber;
		if(linkName.contains("imgBtn")){
			jsExecuteNumber = linkName.substring(6);
			// System.out.println("imgBtn: " + str);
		} else {
			jsExecuteNumber = "12";
		}
		js.executeScript("ImgBtnExecute(" + jsExecuteNumber + ")");
        //waitForWindow(runningSpeed * speedUnit * 2);
        waitForWindow(2000);
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3);
        logger.debug("Check if Proect ID input is available...");
        SeleniumImplementation.waitForElementById(driver,"sf_0_0");
        logger.debug("Proect ID input is available...");
        driver.findElement(By.id("sf_0_0")).click();
        driver.findElement(By.id("sf_0_0")).clear();
        driver.findElement(By.id("sf_0_0")).sendKeys(projectStr);
        waitForWindow(2000); 

        logger.debug("Check if search button is available...");
        SeleniumImplementation.waitForElementById(driver,"imgBtn0");
        logger.debug("Search button is available. Click the button...");
        driver.findElement(By.id("imgBtn0")).click();
        // System.out.println("Waiting for the Select element ... ");
        SeleniumImplementation.waitForElementById(driver,"lhs");
        logger.debug("Select the project ID in the project list");
        Select fruits = new Select(driver.findElement(By.id("lhs")));
		fruits.selectByVisibleText(projectStr);
        SeleniumImplementation.waitForElementById(driver,"imgBtn3");
        driver.findElement(By.id("imgBtn3")).click();
        SeleniumImplementation.waitForElementById(driver,"imgBtn1");
        logger.debug("OK button is available");
       driver.findElement(By.id("imgBtn1")).click();
       waitForWindow(2000);
       driver.switchTo().window(mainWindowHandle);
       driver.switchTo().frame(3);
       SeleniumImplementation.waitForElementById(driver,"alertmsgText");
       String saveResult = driver.findElement(By.id("alertmsgText")).getText();
       if(saveResult.contains("Save Successful")) {
           logger.info("Project " + projectStr + " was added to the ticket successfully");
			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The project " + projectStr + " was added to the ticket successfully");
   	   
       }
    	   
       logger.debug("Exiting SeleniumTest.addProject...");        
    }
    public static void addApprovers(WebDriver driver, ArrayList<String> approvers, int instanceNumber){
        logger.debug("Entering SeleniumTest.addApprovers..."); 
 		if(approvers == null || approvers.size() == 0) {
			logger.info("No approver was defined and added...");
			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "No approver was defined and added because no approver was passed to the method ...");
			logger.debug("Exiting SeleniumTest.addApprovers..."); 
			return;
		}
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        String mainWindowHandle = lastWindowHandle;		
        for(int i = 0; i < approvers.size(); i++){
        	logger.info("Adding approver: " + approvers.get(i));
			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding approver: " + approvers.get(i));
    		addApprover(driver, approvers.get(i), instanceNumber, mainWindowHandle);
    		//driver.switchTo().window(mainWindowHandle);
    	}
        
        // driver.switchTo().frame(3);
        logger.debug("Exiting SeleniumTest.addApprovers...");             
    }
    
    public static boolean checkIfSaveSuccessByAltermsgTxt(WebDriver driver) {
    	boolean result = false;
	    SeleniumImplementation.waitForElementById(driver,"alertmsgText");
    	String saveResultText = driver.findElement(By.id("alertmsgText")).getText();
    	if(saveResultText != null && saveResultText.contains("Save Successful")){
    		result = true;
    	}
    	return result;
    }
    
    public static boolean checkIfSavedSuccessfullyByXpath(WebDriver driver, String xpath){
    	boolean result = false;
	    SeleniumImplementation.waitForElementByXPath(driver,xpath);;
    	String saveResultText = driver.findElement(By.xpath(xpath)).getText();
    	if(saveResultText != null && saveResultText.contains("Saved Successfully")){
    		result = true;
    	}
    	return result;
    	
    }
    
    public static void addApprover(WebDriver driver,String approver, int instanceNumber, String lastWindowHandleFromOutside){
        logger.debug("Entering SeleniumTest.addApprover...");            
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        // String mainWindowHandle = lastWindowHandle;
        String mainWindowHandle = lastWindowHandleFromOutside;
        driver.switchTo().window(mainWindowHandle);
        driver.switchTo().frame(3);
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        waitForWindow(1000);
        // click 1.5 Approvers
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_5");
        driver.findElement(By.id("tabHyprlnk1_5")).click();
        logger.debug("Clicked the link for Approvers...");
        waitForWindow(2000);
        // driver.switchTo().frame(0);    
        logger.debug("Check if \"Add Approvers\" button is available...");
        // SeleniumTest.waitForElementById(driver,"imgBtn6");
        SeleniumImplementation.waitForElementByPartialLinkText(driver,"Add Approver");
        logger.debug("\"Add Approvers\" button is avaible. Click \"Add Approvers\" button...");
        // driver.findElement(By.id("imgBtn6")).click();
        driver.findElement(By.partialLinkText("Add Approver")).click();
        logger.debug("Clicked the 'Add Approver' button...");
        waitForWindow(4000);
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);        
        driver.switchTo().frame(3);
        //check approver input element
        SeleniumImplementation.waitForElementById(driver,"df_0_0");
        driver.findElement(By.id("df_0_0")).click();
        driver.findElement(By.id("df_0_0")).clear();
        driver.findElement(By.id("df_0_0")).sendKeys(approver);
        waitForWindow(2000);
        SeleniumImplementation.waitForElementById(driver,"imgBtn0");
        //click the save button to save approver
        driver.findElement(By.id("imgBtn0")).click();
        waitForWindow(6000);
       // allWindowHandles = driver.getWindowHandles();
        // lastWindowHandle = getLastWindowHandle(allWindowHandles);
        // driver.switchTo().window(lastWindowHandle);
        driver.switchTo().window(mainWindowHandle);
       /* if(!lastWindowHandle.equals(mainWindowHandle)){
	         driver.close();
	         allWindowHandles = driver.getWindowHandles();
	         lastWindowHandle = getLastWindowHandle(allWindowHandles);
	         driver.switchTo().window(mainWindowHandle);
        } */
        driver.switchTo().frame("cai_main");
        if(SeleniumImplementation.checkIfSaveSuccessByAltermsgTxt(driver)){
          logger.info("The approver " + approver + " was added to the ticket");
			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The approver " + approver + " was added to the ticket");
        } else {
          logger.info("The message of successfully adding the approver was not found!");
			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The message of successfully adding the approver was not found!");
        }
        logger.debug("Exiting SeleniumTest.addApprover...");            
   	 
    }
    
    public static boolean addCI(WebDriver driver,String CIHost, int instanceNumber, String injectedLastWindowHandle){
        logger.debug("Entering SeleniumTest.addCI..."); 
        boolean result = true;
        String ciHostTrimed = CIHost.trim();
        if(ciHostTrimed.contains(" ")) ciHostTrimed = ciHostTrimed.replace(" ", "");
        logger.debug("Add CI \"" + ciHostTrimed +"\"");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding CI '" + ciHostTrimed +"'");        
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        String mainWindowHandle = lastWindowHandle;
        if(injectedLastWindowHandle != null && !injectedLastWindowHandle.trim().equals("")) {
        	lastWindowHandle = injectedLastWindowHandle;
        	mainWindowHandle = lastWindowHandle;
        }
        driver.switchTo().window(lastWindowHandle);
        logger.debug("Switched to the last window handle before adding CI: " + lastWindowHandle);
		// Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Switched to the last window handle before adding CI: " + lastWindowHandle);
        //driver.switchTo().frame(3);
        waitForWindow(1000);
        driver.switchTo().frame("cai_main");
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        logger.debug("Clicked the link for 1. Additional Information...");
        waitForWindow(1000);
        // click 3.4 Previlege Template
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_2");
        driver.findElement(By.id("tabHyprlnk1_2")).click();
        logger.debug("Clicked the link for 1.2 Config items...");
        waitForWindow(5000);
        // driver.switchTo().frame(0);  
        driver.switchTo().frame("chgnr_iframe");
        logger.debug("Check if Update CIs button is available...");
        if(!SeleniumImplementation.tryToFindElementByPartialLinkText(driver,"Update CIs")){
        	logger.error("'Update CIS' button is not available...");
			Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "'Update CIS' button is not available...");
        	result = false;
        	return result;
        }
        driver.findElement(By.partialLinkText("Update CIs")).click();
        //SeleniumTest.waitForElementById(driver,"imgBtn0");
        logger.debug("Click \"Update CIs\" button...");
        //driver.findElement(By.id("imgBtn0")).click();
        //waitForWindow(runningSpeed * speedUnit * 2);
        waitForWindow(4000);
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3);
        SeleniumImplementation.waitForElementByName(driver,"QBE.IN.name");
        driver.findElement(By.name("QBE.IN.name")).click();
        driver.findElement(By.name("QBE.IN.name")).clear();
        driver.findElement(By.name("QBE.IN.name")).sendKeys(ciHostTrimed);
        waitForWindow(2000);
        //SeleniumTest.waitForElementById(driver,"imgBtn0");
        SeleniumImplementation.waitForElementByPartialLinkText(driver,"Search");
        logger.debug("Search button is avaible. Click \"Search\" button...");
        // driver.findElement(By.id("imgBtn0")).click();
        driver.findElement(By.partialLinkText("Search")).click();
        waitForWindow(1000);
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3);
        logger.debug("Waiting for the Select element ... ");
        SeleniumImplementation.waitForElementById(driver,"lhs");
        logger.debug("Select element is available...");
        Select fruits = new Select(driver.findElement(By.id("lhs")));
        if(SeleniumImplementation.tryToSelectTextFromSelectElement(driver,fruits, ciHostTrimed.toUpperCase())){
            Select addedCIsSelect = new Select(driver.findElement(By.id("rhs")));
            if(SeleniumImplementation.tryToSelectTextFromSelectElement(driver,addedCIsSelect, ciHostTrimed.toUpperCase())){
            	logger.info("CI '" + ciHostTrimed.toUpperCase() + "' was already added to the ticket. It can not be added again...");
    			Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "CI '" + ciHostTrimed.toUpperCase() + "' was already added to the ticket. It can not be added again...");
		        SeleniumImplementation.waitForElementById(driver,"imgBtn2");
		        logger.debug("'Cancel' button is available. Click 'Cancel' to close the page...");
		        driver.findElement(By.id("imgBtn2")).click();
	    	    result = false;
	    	    return result;      	
            }
        	
		// fruits.selectByVisibleText(ciHostTrimed.toUpperCase());
	         SeleniumImplementation.waitForElementById(driver,"imgBtn3");
        	// SeleniumTest.waitForElementById(driver,">");
	         driver.findElement(By.id("imgBtn3")).click();
        	// driver.findElement(By.id(">")).click();
	          SeleniumImplementation.waitForElementById(driver,"imgBtn1");
        	// SeleniumTest.waitForElementByPartialLinkText(driver,"OK");
	        logger.debug("OK button is available");
	         driver.findElement(By.id("imgBtn1")).click();
	       // driver.findElement(By.partialLinkText("OK"));
	        // System.out.println("New CI was saved successfully");
	        SeleniumImplementation.waitForWindow(2000);
	        driver.switchTo().window(mainWindowHandle);
	        driver.switchTo().frame(3);
	        SeleniumImplementation.waitForElementById(driver,"imgBtn1");
	    	String newCRSavedText = driver.findElement(By.id("alertmsgText")).getText();
	    	if(newCRSavedText != null && newCRSavedText.contains("Save Successful")){
	    		//System.out.println("New CI " +  ciHostTrimed + " was saved successfully!");
    			// Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "New CI " +  ciHostTrimed + " was saved successfully!");
	    	} else {
	    		
	    		// System.out.println("New CI " + ciHostTrimed + " was not saved successfully!");
	    		result = false;
	    	} 
	     } else {
	    	    logger.info("CI to be added was not found in search..." );
		        SeleniumImplementation.waitForElementById(driver,"imgBtn2");
		        logger.info("'Cancel' button is available. Click 'Cancel' to close the page...");
		        driver.findElement(By.id("imgBtn2")).click();
	    	    result = false;
	     }
        logger.debug("Exiting SeleniumTest.addCI...");
        return result;
    }
    
    public static AddCIs addCIs(WebDriver driver, ArrayList<String> CIs, int instanceNumber, String injectedLastWindowHandle){
    	AddCIs cis = new AddCIs();
    	if(CIs == null || CIs.size() == 0) return cis;
    	ArrayList<String> addedCIs = new ArrayList<String>();
    	ArrayList<String> failedCIs = new ArrayList<String>();

            Set<String> allWindowHandles = driver.getWindowHandles();
            String lastWindowHandle = getLastWindowHandle(allWindowHandles);
            if(injectedLastWindowHandle != null && !injectedLastWindowHandle.trim().equals("")) {
            	lastWindowHandle = injectedLastWindowHandle;
            }

    	for(int i = 0; i < CIs.size(); i++){
    		// System.out.println("CI " + i + ":'" + CIs.get(i) + "'");
    		String ci = CIs.get(i).trim();
    		if(ci.equals("")){
    			continue;
    		}
    		if(!addCI(driver, ci, instanceNumber, lastWindowHandle)){
    			failedCIs.add(ci);
    			logger.info("CI " + i + ":'" + ci + "' was not added...");
     			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "CI " + i + ":'" + ci + "' was not added...");
    	
    		}else {
    			addedCIs.add(ci);
    			logger.info("CI " + i + ":'" + ci + "' was added...");
    			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "CI " + i + ":'" + ci + "' was added...");
    		}
    	}
    	cis.setAddedCIs(addedCIs);
    	cis.setFailedCIs(failedCIs);
    	return cis;
    }
    
    public static AddCIs addCIs(WebDriver driver, String fileName, int instanceNumber, String injectedLastWindowHandle) throws FileNotFoundException, IOException{
        logger.debug("Entering SeleniumTest.addCIs...");            
    	ArrayList<String> list = Utilities.readCIsFromSpreadsheet(fileName);
    	AddCIs cis = addCIs(driver, list, instanceNumber, injectedLastWindowHandle);
        logger.debug("Exiting SeleniumTest.addCIs...");  
        return cis;
    }
    
    public static void addPAMTemplatesFromSpreadsheet(WebDriver driver, String fileName, int instanceNumber, String injectedLastWindowHandle) throws IOException{
        logger.debug("Entering SeleniumTest.addPAMTemplatesFromSpreadsheet...");            
		ArrayList<PAMTemplate> templateList = Utilities.readPAMTemplatesFromSpreadsheet(fileName);
		if(templateList == null || templateList.size() == 0) {
			logger.info("No PAM Templates were defined and added...");
 			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "No PAM Templates were defined and added...");
			logger.debug("Exiting SeleniumTest.addPAMTemplatesFromSpreadsheet..."); 
			return;
		}
		ArrayList<String> grouplist = Utilities.getAllGroupsInPamTemplateList(templateList);
		ArrayList<String> locationlist = Utilities.getAllPamLocationsInPamTemplateList(templateList);
        failedPAMTemplates = new ArrayList<PAMTemplate>();

        Set<String> allWindowHandles = driver.getWindowHandles();
        // System.out.println("Get window handles before clicking relationships");
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        if(injectedLastWindowHandle != null && !injectedLastWindowHandle.trim().equals("")) {
        	lastWindowHandle = injectedLastWindowHandle;
        }
        String mainWindowHandle = lastWindowHandle;
        String originalWindowHandle = lastWindowHandle;
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3);
        logger.debug("Last handle before clicking relationship is " + originalWindowHandle);
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk3");
        driver.findElement(By.id("accrdnHyprlnk3")).click();
        waitForWindow(1000);
        // click 3.4 Previlege Template
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk3_4");
        driver.findElement(By.id("tabHyprlnk3_4")).click();
        waitForWindow(2000); 
		for(int i = 0; i < grouplist.size(); i++){
			for(int j=0; j < locationlist.size(); j++){
				// System.out.println("group: " + grouplist.get(i) + "  location: " + locationlist.get(j));
				ArrayList<PAMTemplate> tempList =  Utilities.getPAMTemplateListWithSameGroupAndPAMLocation(grouplist.get(i), locationlist.get(j), templateList);
				if(tempList.size() > 0) {

						logger.debug("Adding PAM Template with group " + grouplist.get(i) + ", pamLocation " + locationlist.get(j) + " and CIs: ");
						for(int k = 0; k < tempList.size(); k++){
							logger.debug("'" + tempList.get(k).getCi() + "'");
						}

					
					if(SeleniumImplementation.addPARTemplates(driver, grouplist.get(i), locationlist.get(j), tempList, instanceNumber)){
						// System.out.println("get window handles after adding PAM Templates...");
						allWindowHandles = driver.getWindowHandles();
						lastWindowHandle = getLastWindowHandle(allWindowHandles);
						driver.switchTo().window(lastWindowHandle);
						SeleniumImplementation.waitForElementByXPath(driver,"//*[@id='createChgTmplForm']/fieldset/table/tbody/tr[2]/td");
						String result = driver.findElement(By.xpath("//*[@id='createChgTmplForm']/fieldset/table/tbody/tr[2]/td")).getText();
						if(result.contains("Created Successfully")){
							logger.info("The PAM Templates were created and saved successfully");
				 			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The PAM Templates were created and saved successfully");
						}
						SeleniumImplementation.waitForElementByPartialLinkText(driver,"(Close Window");
						driver.findElement(By.partialLinkText("(Close Window")).click();
					} 
					// driver.close();
					// System.out.println("PAM Templates saved successfully page was close.");
					// allWindowHandles = driver.getWindowHandles();
					// lastWindowHandle = getLastWindowHandle(allWindowHandles); 					
					driver.switchTo().window(originalWindowHandle);
					driver.switchTo().frame(3);
					
				}
			}
		}   	
        logger.debug("Exiting SeleniumTest.addPAMTemplatesFromSpreadsheet...");            
    }
    
    public static boolean addPARTemplates(WebDriver driver, String group, String pamLocation, ArrayList<PAMTemplate> tempList, int instanceNumber){
        logger.debug("Entering SeleniumTest.addPARTemplates...");  
        boolean result = false;

    	SeleniumImplementation.waitForElementByPartialLinkText(driver,"Create Template");
    	driver.findElement(By.partialLinkText("Create Template")).click();
    	 logger.debug("found the element Create Template button...");
        waitForWindow(1000);
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        //String mainWindowHandle = lastWindowHandle;
        driver.switchTo().window(lastWindowHandle);
        // driver.switchTo().frame(3);
        logger.debug("Waiting for the Select element for TPAM location ... ");
        SeleniumImplementation.waitForElementById(driver,"tpamLoc");
        logger.debug(" TPAM location Select element is available...");
        Select tpamloc = new Select(driver.findElement(By.id("tpamLoc")));
        tpamloc.selectByVisibleText(pamLocation);

        SeleniumImplementation.waitForElementById(driver,"grpSearch");
        driver.findElement(By.id("grpSearch")).click();
        driver.findElement(By.id("grpSearch")).clear();
        driver.findElement(By.id("grpSearch")).sendKeys(group);
        
        SeleniumImplementation.waitForElementById(driver,"grpSearchFilter");
        driver.findElement(By.id("grpSearchFilter")).click();
        waitForWindow(1000);
        
        SeleniumImplementation.waitForElementById(driver,"grplistbox");
        Select groups = new Select(driver.findElement(By.id("grplistbox")));
        groups.selectByVisibleText(group);
        
        SeleniumImplementation.waitForElementById(driver,"grplefttoright");
        driver.findElement(By.id("grplefttoright")).click();         
        SeleniumImplementation.waitForElementById(driver,"cilistbox");
        logger.debug(" CIs Select element is available...");
        Select cis = new Select(driver.findElement(By.id("cilistbox")));
        // System.out.println("Searching the following CIs from the CI list:");
        ArrayList<PAMTemplate> addedTemplates = new ArrayList<PAMTemplate>();
        for(int i = 0; i < tempList.size(); i++){
        	// System.out.println("CI " + i + ": " + tempList.get(i).getCi());
			String ci = tempList.get(i).getCi().trim().toUpperCase();
        	if(!SeleniumImplementation.tryToSelectTextFromSelectElement(driver,cis, ci)){
        		logger.debug("CI '" + tempList.get(i).getCi() + "' is not in the CI list, and so the following template will not be added to the ticket:");
        		logger.debug(tempList.get(i).getCi() + "  " + tempList.get(i).getPamLocation() + "  " + tempList.get(i).getGroup());
	 			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "CI '" + tempList.get(i).getCi() + "' is not in the CI list. So the PAM Template '" + tempList.get(i).getCi() + "'   '" + tempList.get(i).getPamLocation() + "'    '" + tempList.get(i).getGroup() + "' was not added to the ticket");
        		failedPAMTemplates.add(tempList.get(i));
        	} else {
        		addedTemplates.add(tempList.get(i));
        		logger.info("new added PAM Template: '" + tempList.get(i).getCi() + "'   '" + tempList.get(i).getPamLocation() + "'    '" + tempList.get(i).getGroup() + "'");
	 			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "new added PAM Template: '" + tempList.get(i).getCi() + "'   '" + tempList.get(i).getPamLocation() + "'    '" + tempList.get(i).getGroup() + "'");
        	}
        	// cis.selectByVisibleText(tempList.get(i).getCi().trim().toUpperCase());
			waitForWindow(1000);
        }
        if(addedTemplates.size() > 0){
        // waitForWindow(3000);
                  
	        SeleniumImplementation.waitForElementById(driver,"cilefttoright");
	        logger.debug(" Button for moving CIs to right is available...");
	        driver.findElement(By.id("cilefttoright")).click();
	        SeleniumImplementation.waitForElementById(driver,"chgTempDesc");
	        driver.findElement(By.id("chgTempDesc")).click();  
	        driver.findElement(By.id("chgTempDesc")).sendKeys("No Description");
	        waitForWindow(4000);
	        SeleniumImplementation.waitForElementById(driver,"Submit");
	        driver.findElement(By.id("Submit")).click(); 
	        result = true;
        } else {
			SeleniumImplementation.waitForElementByPartialLinkText(driver,"(Close Window");
			driver.findElement(By.partialLinkText("(Close Window")).click();        	
        }
        logger.debug("Exiting SeleniumTest.addPARTemplates...");    
        return result;
    }
    
    public static void saveAndCloseImplementationPlanToRefresh(WebDriver driver, String CR_Number, int instanceNumber) throws Exception {
        logger.debug("Entering SeleniumTest.saveAndCloseImplementationPlanToRefresh...");               
        SeleniumImplementation.openPlanToEdit(driver, CR_Number, instanceNumber);
        SeleniumImplementation.waitForElementById(driver,"saveandclose");
        logger.debug("Cick the Save and Close button to refresh the implementation plan...");
        driver.findElement(By.id("saveandclose")).click();
        waitForWindow(2000);
        logger.debug("Exiting SeleniumTest.saveAndCloseImplementationPlanToRefresh...");               
    }
    
    public static void setWebDriver(WebDriver webDriver){
    	driver = webDriver;
    }
    
    public static boolean addImplementationPlan(WebDriver driver, Task[] tasks, String CR_Number, int instanceNumber) throws Exception {
        boolean result = true;
    	logger.debug("Entering SeleniumTest.addImplementationPlan...");               
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);        
        String mainWindowHandle = lastWindowHandle;
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk1");
        driver.findElement(By.id("accrdnHyprlnk1")).click();
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk1_4");
        driver.findElement(By.id("tabHyprlnk1_4")).click();
        logger.debug("Click the attachment...");
        waitForWindow(5000); 
        // driver.switchTo().frame(1); 
        driver.switchTo().frame("attmnt_iframe"); 
        logger.debug("Check if there is an existing implemenation plan...");
       if(SeleniumImplementation.tryToFindElementByPartialLinkText(driver,"Implementation Plan")){
        	logger.info("An implemenation plan exists already. You can not create additional implementation plan.");
 			Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "An implemenation plan exists already. You can not create additional implementation plan.");
        	logger.info("Please remove the existing implemenation plan before you can add a new one...");
 			Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Please remove the existing implemenation plan before you can add a new one...");
        	return false;
        } 
        logger.debug("Check if Add Plan button is available...");
        
        SeleniumImplementation.waitForElementByName(driver,"imgBtn0");
        //SeleniumTest.waitForElementByPartialLinkText(driver,"Plan(");
        logger.info("Click the \"Add Plan\" button...");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Click the \"Add Plan\" button...");
        driver.findElement(By.name("imgBtn0")).click();
        //driver.findElement(By.partialLinkText("Plan("));
        //waitForWindow(runningSpeed * speedUnit * 2);
        waitForWindow(4000);
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        logger.info("Adding tasks to the implementation plan...");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding tasks to the implementation plan...");
		//Task[] tasks = SeleniumTest.getTasksFromFile("c:\\temp\\Tasks.txt");
		// Task[] tasks = Utilities.getTasksFromExcel(fileName);
		if(tasks != null && tasks.length != 0){
			for(int i =0; i < tasks.length; i++){
				logger.info("Adding Task " + (i + 1) + " ...");
			    if(tasks[i] != null) {
			    	SeleniumImplementation.addTask(driver,tasks[i], instanceNumber);
					Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Added task " + (i + 1 ) + " " + tasks[i].getTaskTitle());
			    	// System.out.println("Add Task " + i + " " + tasks[i].getTaskTitle());
			    } else {
			    	// System.out.println("Task " + i + " is null!");
			    }
			    waitForWindow(2000);
			}
		}
		// waitForWindow(2000);
		logger.info("Click the Save Plan button...");
		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Click the Save Plan button...");
		SeleniumImplementation.waitForElementById(driver,"savePlan");
        driver.findElement(By.id("savePlan")).click();
        String planSavedTextXpath = "//*[@id='savePlanForm']/ul/li";
        SeleniumImplementation.waitForElementByXPath(driver,planSavedTextXpath);
        if(SeleniumImplementation.checkIfSavedSuccessfullyByXpath(driver, planSavedTextXpath)){
        	logger.info("The implementation plan has been saved successfully!");
    		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The implementation plan has been saved successfully!");
        }
        logger.debug("Click 'Save and Close' button to close the implementation plan window");
		SeleniumImplementation.waitForElementById(driver,"saveandclose");
        driver.findElement(By.id("saveandclose")).click();        
/*		allWindowHandles = driver.getWindowHandles();
		lastWindowHandle = getLastWindowHandle(allWindowHandles);
		driver.switchTo().window(lastWindowHandle);
		 SeleniumTest.waitForElementByPartialLinkText(driver,"(Close Window");
		driver.findElement(By.partialLinkText("(Close Window")).click();
    */  // driver.close();
        waitForWindow(1000);
		driver.switchTo().window(mainWindowHandle);        
        driver.switchTo().frame(3);
        logger.info("Refreshing the page...");
    	SeleniumImplementation.refreshPage(driver); 
    	logger.debug("Exiting SeleniumTest.addImplementationPlan..."); 
    	return result;
   }
    
    public static void editCIs(WebDriver driver){
        SeleniumImplementation.waitForElementById(driver,"accrdnHyprlnk3");
        driver.findElement(By.id("accrdnHyprlnk3")).click();
        waitForWindow(1000);
        // click 3.4 Previlege Template
        SeleniumImplementation.waitForElementById(driver,"tabHyprlnk3_4");
        driver.findElement(By.id("tabHyprlnk3_4")).click();
        waitForWindow(2000);
        SeleniumImplementation.waitForElementById(driver,"nbtab_3_4");
        logger.debug("found the element nbtab_3_4");
        SeleniumImplementation.waitForElementById(driver,"tbl502");
        logger.debug("found the element tbl502");
    	SeleniumImplementation.waitForElementByPartialLinkText(driver,"Search PAR");
    	driver.findElement(By.partialLinkText("Search PAR")).click();
        logger.debug("found the element Search PAR Template");
        waitForWindow(1000);
     }
    
    public static void addProectCIsPAMTemplatesAndSubmitTogether(WebDriver driver, String projectStr, String fileName, int instanceNumber) throws IOException, Exception {	
    	addProject(driver, projectStr, instanceNumber, null);
    	waitForWindow(4000);
    	addCIs(driver, fileName, instanceNumber, null);
    	waitForWindow(2000);
    	addPAMTemplatesFromSpreadsheet(driver, fileName, instanceNumber, null); 
    	waitForWindow(6000);
        logger.debug("Refreshing the page...");
    	SeleniumImplementation.refreshPage(driver);
    }   
    
    public static void submitBreakGlassForApproval(WebDriver driver, int instanceNumber, String bgCRActivities){
	    SeleniumImplementation.waitForElementById(driver,"imgBtn0");
    	driver.findElement(By.id("imgBtn0")).click();
    	waitForWindow(2000);
    	logger.info("Editing to add activity descriptions to the Break Glass CR Ticket...");
	    Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Editing to add activity descriptions to the Break Glass CR Ticket...");
	    SeleniumImplementation.waitForElementById(driver,"df_4_0");
	    String changeDescription = driver.findElement(By.id("df_4_0")).getText() + " " + bgCRActivities;
	    WebElement element = driver.findElement(By.id("df_4_0"));
	    element.clear();
	    element.sendKeys(changeDescription);
    	waitForWindow(1000);
	    SeleniumImplementation.waitForElementById(driver,"imgBtn0");
    	driver.findElement(By.id("imgBtn0")).click();
    	waitForWindow(2000);	    	    
		SeleniumImplementation.waitForElementById(driver,"imgBtn3");
    	driver.findElement(By.id("imgBtn3")).click();     	
    	waitForWindow(1000);
    	driver.switchTo().alert().accept();
    	logger.info("The Break Glass CR Ticket was submitted for approval...");
	    Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The Break Glass CR Ticket was submitted for approval...");
    }
    
    public static void submitStandardCRForApproval(WebDriver driver){
        logger.debug("Entering SeleniumTest.submitStandardCRForApproval...");            
        Set<String> allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        driver.switchTo().frame(3);
		SeleniumImplementation.waitForElementById(driver,"imgBtn4");
    	driver.findElement(By.id("imgBtn4")).click();
    	waitForWindow(2000);
        allWindowHandles = driver.getWindowHandles();
        lastWindowHandle = getLastWindowHandle(allWindowHandles);
        driver.switchTo().window(lastWindowHandle);
        // driver.switchTo().frame(3);
		SeleniumImplementation.waitForElementById(driver,"text1");
		String successText = driver.findElement(By.id("text1")).getText();
		if(successText.contains("Successful")){
			logger.info(" CR ticket was submitted for approval successfully!");
		}
		// System.out.println("Success text: " + successText);
        logger.debug("Exiting SeleniumTest.submitStandardCRForApproval...");            
   }    
    
    public static void editCR(WebDriver driver, String CR_Number, Map<String, String> items, boolean isCopyingTicket, int instanceNumber){
		logger.debug("Entering SeleniumTest.editCR...");
    	String crSummary = items.get("CHANGE_ORDER_SUMMARY");
    	String crDescription = items.get("CHANGE_ORDER_DESCRIPTION");
    	String crContact = items.get("CONTACT_INFORMATION");
    	String crJustification = items.get("BUSINESS_JUSTIFICATION");
    	String needByDate = items.get("NEED_BY_DATE");
    	String scheduledStartDate = items.get("SCHEDULED_START_DATE");
    	String scheduledDuration = items.get("SCHEDULED_DURATION");
    	String verificationStartDate = items.get("VERIFICATION_START_DATE");
    	String verificationDuration = items.get("VERIFICATION_DURATION");
    	String backoutDuration = items.get("BACKOUT_DURATION");
    	String actualStartDate = items.get("ACTUAL_START_DATE");
    	String actualEndDate = items.get("ACTUAL_END_DATE");
    	String requester = items.get("REQUESTER");
    	String affectedEndUser = items.get("AFFECTED_END_USER");
    	String owningGroup = items.get("OWNING_GROUP");
    	String supportingRegion = items.get("SUPPORTING_REGION");
    	String implementer = items.get("IMPLEMENTER");
    	String implementingTeam = items.get("IMPLEMENTING_TEAM");
    	String requirePAR = items.get("REQUIRE_PAR");
    	String isOutsideMaintenanceWindow = items.get("OUTSIDE_MAINTENANCE_WINDOW");
    	String itServiceAcceptance = items.get("IT_SERVICE_ACCEPTANCE");
    	String projectIdStr = items.get("PROJECT_ID");


    	if(requester != null && !requester.trim().equals("")){
    		logger.debug("Modifying CR requester to: " + requester);
        	SeleniumImplementation.waitForElementById(driver,"df_0_0");
	        driver.findElement(By.id("df_0_0")).click();
	        driver.findElement(By.id("df_0_0")).clear();
	        driver.findElement(By.id("df_0_0")).sendKeys(requester.trim());
    	}
    	
    	if(affectedEndUser != null && !affectedEndUser.trim().equals("")){
    		logger.debug("Modifying CR affected End User to: " + affectedEndUser);
        	SeleniumImplementation.waitForElementById(driver,"df_0_1");
	        driver.findElement(By.id("df_0_1")).click();
	        waitForWindow(1000);
	        driver.findElement(By.id("df_0_1")).clear();
	        waitForWindow(2000);
	        driver.findElement(By.id("df_0_1")).sendKeys(affectedEndUser.trim());
	         waitForWindow(2000);
   	}   	
    	if(owningGroup != null && !owningGroup.trim().equals("")){
    		logger.debug("Modifying CR Owning Group to: " + owningGroup);
        	SeleniumImplementation.waitForElementById(driver,"df_2_1");
	        driver.findElement(By.id("df_2_1")).click();
	        driver.findElement(By.id("df_2_1")).clear();
	        driver.findElement(By.id("df_2_1")).sendKeys(owningGroup.trim());
    	}
    	
    	if(supportingRegion != null && !supportingRegion.trim().equals("")){
    		logger.debug("Modifying CR Supporting Region to: " + supportingRegion);
        	SeleniumImplementation.waitForElementById(driver,"df_2_2");
	        driver.findElement(By.id("df_2_2")).click();
	        driver.findElement(By.id("df_2_2")).clear();
	        driver.findElement(By.id("df_2_2")).sendKeys(supportingRegion.trim());
    	}
    	
    	if(implementer != null && !implementer.trim().equals("")){
    		logger.debug("Modifying CR implementer to: " + implementer);
        	SeleniumImplementation.waitForElementById(driver,"df_2_3");
	        driver.findElement(By.id("df_2_3")).click();
	        driver.findElement(By.id("df_2_3")).clear();
	        driver.findElement(By.id("df_2_3")).sendKeys(implementer.trim());
    	}

    	if(implementingTeam != null && !implementingTeam.trim().equals("")){
    		logger.debug("Modifying CR Implementing Team to: " + implementingTeam);
        	SeleniumImplementation.waitForElementById(driver,"df_2_4");
	        driver.findElement(By.id("df_2_4")).click();
	        driver.findElement(By.id("df_2_4")).clear();
	         waitForWindow(2000);
	        driver.findElement(By.id("df_2_4")).sendKeys(implementingTeam.trim());
   	}   	
    	
    	
    	if(needByDate != null && !needByDate.trim().equals("")){
    		logger.debug("Modifying CR Need By Date to: " + needByDate);
        	SeleniumImplementation.waitForElementById(driver,"df_1_0");
	        driver.findElement(By.id("df_1_0")).click();
	        driver.findElement(By.id("df_1_0")).clear(); 
	        driver.findElement(By.id("df_1_0")).sendKeys(needByDate.trim());
    	}    	

    	if(crSummary != null && !crSummary.trim().equals("")){
    		logger.debug("Modifying CR Summary to: " + crSummary);
        	SeleniumImplementation.waitForElementById(driver,"df_3_0");
	        driver.findElement(By.id("df_3_0")).click();
	        driver.findElement(By.id("df_3_0")).clear();
	        driver.findElement(By.id("df_3_0")).sendKeys(crSummary);
    	}
        
    	if(crDescription != null && !crDescription.trim().equals("")){
    		logger.debug("Modifying CR description to: \n" + crDescription);
        	SeleniumImplementation.waitForElementById(driver,"df_4_0");
	        driver.findElement(By.id("df_4_0")).click();
	        driver.findElement(By.id("df_4_0")).clear();
	        driver.findElement(By.id("df_4_0")).sendKeys(crDescription);
    	}

    	if(crContact != null && !crContact.trim().equals("")){
    		logger.debug("Modifying CR contact information to: " + crContact);
        	SeleniumImplementation.waitForElementById(driver,"df_6_0"); 		
	        driver.findElement(By.id("df_6_0")).click();
	        driver.findElement(By.id("df_6_0")).clear();
	        driver.findElement(By.id("df_6_0")).sendKeys(crContact);
    	}
    	
    	if(scheduledStartDate != null && !scheduledStartDate.trim().equals("")){
    		logger.debug("Modifying CR scheduled start date to: " + scheduledStartDate);
        	SeleniumImplementation.waitForElementById(driver,"df_7_0");
	        driver.findElement(By.id("df_7_0")).click();
	        driver.findElement(By.id("df_7_0")).clear(); 
	        driver.findElement(By.id("df_7_0")).sendKeys(scheduledStartDate.trim());
    	}
    	
    	if(scheduledDuration != null && !scheduledDuration.trim().equals("")){
    		logger.debug("Modifying CR scheduled duration to: " + scheduledDuration);
    		String scheduledDurationTimeStr = Utilities.convertHoursDecimalToTimeFormat(scheduledDuration);
        	SeleniumImplementation.waitForElementById(driver,"df_7_1");
	        driver.findElement(By.id("df_7_1")).click();
	        driver.findElement(By.id("df_7_1")).clear(); 
	        driver.findElement(By.id("df_7_1")).sendKeys(scheduledDurationTimeStr.trim());
    	}
    	
   	
    	if(verificationStartDate != null && !verificationStartDate.trim().equals("")){
    		logger.debug("Modifying CR verification start date to: " + verificationStartDate);
        	SeleniumImplementation.waitForElementById(driver,"df_9_0");
	        driver.findElement(By.id("df_9_0")).click();
	        driver.findElement(By.id("df_9_0")).clear();
	        driver.findElement(By.id("df_9_0")).sendKeys(verificationStartDate.trim());
    	}

    	if(verificationDuration != null && !verificationDuration.trim().equals("")){
    		logger.debug("Modifying CR verification duration to: " + verificationDuration);
        	SeleniumImplementation.waitForElementById(driver,"df_9_1");
	    	driver.findElement(By.id("df_9_1")).click();
	        driver.findElement(By.id("df_9_1")).clear(); 
	        if(verificationDuration.indexOf(":") < 0) {
	        	verificationDuration = Utilities.convertHoursDecimalToTimeFormat(verificationDuration);
	        	logger.debug("verification duration is a number. convert it to time format: " + verificationDuration);
	        }
	        driver.findElement(By.id("df_9_1")).sendKeys(verificationDuration.trim());
    	}
    	
    	if(backoutDuration != null && !backoutDuration.trim().equals("")){
    		logger.debug("Modifying CR backout duration to: " + backoutDuration);
        	SeleniumImplementation.waitForElementById(driver,"df_9_3");
	        driver.findElement(By.id("df_9_3")).click();
	        driver.findElement(By.id("df_9_3")).clear();
	        // 44 | click | id=df_12_0 |  | 
	        driver.findElement(By.id("df_9_3")).sendKeys(backoutDuration.trim());
    	}
    	
    	if(crJustification != null && !crJustification.trim().equals("")){
    		logger.debug("Modifying CR business justification to: " + crJustification);
           	SeleniumImplementation.waitForElementById(driver,"df_12_0");
	        driver.findElement(By.id("df_12_0")).click();
	        driver.findElement(By.id("df_12_0")).clear(); 
	        driver.findElement(By.id("df_12_0")).sendKeys(crJustification.trim());
    	}

    	if(actualStartDate != null && !actualStartDate.trim().equals("")){
    		logger.debug("Modifying CR Actual Implementation Start Date to: " + actualStartDate.trim());
           	SeleniumImplementation.waitForElementById(driver,"df_8_0");
	        driver.findElement(By.id("df_8_0")).click();
	        driver.findElement(By.id("df_8_0")).clear(); 
	        driver.findElement(By.id("df_8_0")).sendKeys(actualStartDate);
    	}
    	
    	if(actualEndDate != null && !actualEndDate.trim().equals("")){
    		logger.debug("Modifying CR Actual Implementation End Date to: " + actualEndDate);
           	SeleniumImplementation.waitForElementById(driver,"df_8_1");
	        driver.findElement(By.id("df_8_1")).click();
	        driver.findElement(By.id("df_8_1")).clear(); 
	        driver.findElement(By.id("df_8_1")).sendKeys(actualEndDate.trim());
    	} 
    	
    	if(requirePAR != null && !requirePAR.trim().equals("")){
    		logger.debug("Modifying requring PAR to: " + requirePAR);
    		// wait for select dropdown for requiring PAR
        	SeleniumImplementation.waitForElementById(driver,"df_13_0");
            Select requireParSelect = new Select(driver.findElement(By.id("df_13_0")));
            if(requirePAR.trim().equalsIgnoreCase("Yes")){
            	requireParSelect.selectByVisibleText("YES");
            } else {
            	requireParSelect.selectByVisibleText("NO");
            }
    	}

    	if(isOutsideMaintenanceWindow != null && !isOutsideMaintenanceWindow.trim().equals("")){
    		logger.debug("Modifying Outside of Maintenance Window to: " + isOutsideMaintenanceWindow);
    		// wait for select dropdown for requiring PAR
        	SeleniumImplementation.waitForElementById(driver,"df_14_0");
            Select requireParSelect = new Select(driver.findElement(By.id("df_14_0")));
            if(isOutsideMaintenanceWindow.trim().equalsIgnoreCase("Yes")){
            	requireParSelect.selectByVisibleText("YES");
            } else {
            	requireParSelect.selectByVisibleText("NO");
            }
    	}
    	
    	if(itServiceAcceptance != null && !itServiceAcceptance.trim().equals("")){
    		logger.debug("Modifying IT Service Acceptance to: " + itServiceAcceptance);
    		// wait for select dropdown for requiring PAR
        	SeleniumImplementation.waitForElementById(driver,"df_15_0");
            Select requireParSelect = new Select(driver.findElement(By.id("df_15_0")));
            requireParSelect.selectByValue("" + Utilities.getITServiceAcceptanceOptionValue(itServiceAcceptance));

    	}
       	SeleniumImplementation.waitForElementById(driver,"imgBtn0");
    	// Click Save Button
        driver.findElement(By.id("imgBtn0")).click();
    	waitForWindow(4000);     
    	String startStr = "The edited ticket ";
    	if(isCopyingTicket) startStr = "The new ticket ";    	
        // if(SeleniumTest.checkIfElementLoaded("alertmsgText", "CR saved", 2, false)){
    	    // SeleniumTest.waitForElementById(driver,"alertmsgText");
        	// String newCRSavedText = driver.findElement(By.id("alertmsgText")).getText();
        	//if(newCRSavedText != null && newCRSavedText.contains("Save Successful")){
        	if(SeleniumImplementation.checkIfSaveSuccessByAltermsgTxt(driver)){
    	        logger.info(startStr + CR_Number + " was saved successfully!");
     			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, startStr + CR_Number + " was saved successfully!");
       	} else {
        		logger.info(startStr + CR_Number + " was not saved successfully! Please correct the items with error messages, and save the ticket again.");
     			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, startStr + CR_Number + " was not saved successfully! Please correct the items with error messages, and save the ticket again.");
       		
       	}
       // } else {
        //	System.out.println(startStr + "was not saved");
       //  }
		logger.debug("Exiting SeleniumTest.editCR...");
   }
    public static void completeMultipleTasks(WebDriver driver, int[] multiTasks, String fileName, String CR_Number, int instanceNumber, int serverTimeDifference) throws Exception{
    	if(multiTasks == null || multiTasks.length ==0) {
    		logger.error("No multiple tasks were defined. Please check selenium.properties to see if the property MultiTasks was defined correctly.");
	     	Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "No multiple tasks were defined. Please check selenium.properties to see if the property MultiTasks was defined correctly.");
    	} else {
    		String crNumber = CR_Number;
    		
      	    Map<String, String> items = null;
      	  if(SeleniumImplementation.isReadingCRRequired(14, multiTasks)){
      		items = Utilities.readCRInfoFromSpreadsheet(fileName, serverTimeDifference);
      	  }
    		for(int i = 0; i < multiTasks.length; i++){
    	        String taskString;
    	        switch (multiTasks[i]) {
    	            case SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR:
    	            	        logger.info("Creating a new CR ticket...");
    	          	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Creating a new CR ticket...");
    	          	     		crNumber = SeleniumImplementation.createStandardCRTicket(driver, fileName, instanceNumber, serverTimeDifference);
    	                        break;
    	            case SeleniumImplementation.STANDARD_CR_COPY_CR: 
          	          		 logger.info("Copying a new CR ticket...");
 	          	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Copying a new CR ticket...");
    	                     SeleniumImplementation.copyCR(driver, items, instanceNumber);
    	                     Set<String> allWindowHandles = driver.getWindowHandles();
    	                     String lastWindowHandle = getLastWindowHandle(allWindowHandles);
    	                 	 SeleniumImplementation.saveAndCloseImplementationPlanToRefresh(driver, CR_Number, instanceNumber); 
    	                     driver.switchTo().window(lastWindowHandle);    	                 	 
    	                     break;
    	            case SeleniumImplementation.STANDARD_CR_EDIT_CR:  
	    	        	    SeleniumImplementation.waitForElementById(driver,"imgBtn0");
	    		        	driver.findElement(By.id("imgBtn0")).click();
	    		        	waitForWindow(2000);
	    		        	logger.info("Editing the ticket " + CR_Number + " ...");
		          	     	Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Editing the ticket " + CR_Number + " ...");
	   	            		SeleniumImplementation.editCR(driver, CR_Number, items, false, instanceNumber);
	                    	break;
	    	        case SeleniumImplementation.STANDARD_CR_ADD_PLAN: 
    	            	     Task[] tasks = Utilities.getTasksFromExcel(fileName, serverTimeDifference);
    	            	     logger.info("Adding an implementation plan ...");
 		          	     	 Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding an implementation plan ...");
    	            	     SeleniumImplementation.addImplementationPlan(driver, tasks, CR_Number, instanceNumber);
                    		 break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_CIs:  
	            	     	 logger.info("Adding CIs to the ticket ...");
	 	          	     	 Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding CIs to the ticket ...");
    	            	     AddCIs cis = SeleniumImplementation.addCIs(driver, fileName, instanceNumber, null);
    	            	     SeleniumImplementation.printAddedCIs(cis.getAddedCIs());
    	            	     SeleniumImplementation.printFailedCIs(cis.getFailedCIs());
                    		 break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_PAM_TEMPLATES:
    	            	     logger.info("Adding PAM Templates to the ticket ...");
  	          	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding PAM Templates to the ticket ...");
   	            	     SeleniumImplementation.addPAMTemplatesFromSpreadsheet(driver, fileName, instanceNumber, null);
                    		 break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_PROJECT:
    	            		String projectStr = items.get("PROJECT_ID");
    	            		logger.info("Adding a project to the ticket ...");
 	          	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding a project to the ticket ...");
    	            	    SeleniumImplementation.addProject(driver, projectStr, instanceNumber, null);
                    		break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_APPROVER:   	            	
    	            	ArrayList<String> approvers = Utilities.getApproversFromExcel(fileName);
    	            	logger.info("Adding approvers to the ticket ...");
	          	     	Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding approvers to the ticket ...");
   	            	    SeleniumImplementation.addApprovers(driver, approvers, instanceNumber);
                    		break;
    	            case SeleniumImplementation.STANDARD_CR_CHANGE_SURVEY:
    	            	    logger.info("Completing the change survey ...");
 	          	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Completing the survey ...");
    	            	    SeleniumImplementation.completeChangeSurvey(driver, fileName);
              	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Completed the survey ...");
              	     	                    		break;
    	            case SeleniumImplementation.STANDARD_CR_REMOVE_PLAN:
    	            	    logger.info("Removing the implementation plan from the ticket ...");
 	          	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Removing the implementation plan from the ticket ...");
    	            	    SeleniumImplementation.removePlan(driver, crNumber, instanceNumber);
                    		break;
    	            case SeleniumImplementation.STANDARD_CR_SUBMIT_FOR_APPROVAL:
	          	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Submitting the CR ticket for approval ...");
    	            	    SeleniumImplementation.submitStandardCRForApproval(driver);
                    		break;
    	            default:break;
    	        }
    		}
      		
    	}
    }   
    
    public static String getLastWindowHandle(WebDriver driver) {
        Set<String>  allWindowHandles = driver.getWindowHandles();
        String lastWindowHandle = getLastWindowHandle(allWindowHandles);   
        return lastWindowHandle;
    }
    
    public static void completeBreakGlassMultipleTasks(WebDriver driver, int[] bg_multiTasks, String fileName, String CR_Number, String IN_Number, int instanceNumber, int serverTimeDifference) throws Exception{
    	logger.debug("Entering SeleniumTest.completeBreakGlassMultipleTasks");
    	if(bg_multiTasks == null || bg_multiTasks.length ==0) {
    		logger.error("No multiple tasks were defined. Please check selenium.properties to see if the property BG_MultiTasks was defined correctly.");
    	} else {
    		String crNumber = CR_Number;
      	    // Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(fileName);
    		Map<String, String> items = Utilities.readBreakGlassTicketFromSpreadsheet(fileName);
      	  logger.debug("length of bg_multiTasks is " + bg_multiTasks.length);
      	    String lastWindowHandleOfBGCR = null;
    		for(int i = 0; i < bg_multiTasks.length; i++){

    	        switch (bg_multiTasks[i]) {
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_CREATE_TICKET:
    	            	          logger.info("Creating a new Break Glass CR ticket...");
    	            	          lastWindowHandleOfBGCR = SeleniumImplementation.createBreakGlassTicketInAINPage(driver, IN_Number, fileName, instanceNumber, serverTimeDifference);
    	                          break;
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_CIS:  
	            	     	 logger.info("Adding CIs to the Break Glass ticket ...");
		          	     	 Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding CIs to the Break Glass ticket ...");
	            	     	 if(lastWindowHandleOfBGCR == null || lastWindowHandleOfBGCR.trim().equals("")) {
	            	     		lastWindowHandleOfBGCR = getLastWindowHandle(driver);
	            	     	 }
    	            	     SeleniumImplementation.addCIs(driver, fileName, instanceNumber, lastWindowHandleOfBGCR);
                    		 break;
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_PAM_TEMPLATE:
    	            	     logger.info("Adding PAM Templates to the Break Glass ticket ...");
		          	     	 Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding PAM Templates to the Break Glass ticket ...");
	            	     	 if(lastWindowHandleOfBGCR == null || lastWindowHandleOfBGCR.trim().equals("")) {
	            	     		lastWindowHandleOfBGCR = getLastWindowHandle(driver);
	            	     	 }
   	            	     SeleniumImplementation.addPAMTemplatesFromSpreadsheet(driver, fileName, instanceNumber, lastWindowHandleOfBGCR);
                    		 break;
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_PROJECT:
    	            		String projectStr = items.get("PROJECT_ID");
    	            		logger.info("Adding a project to the Break Glass ticket ...");
		          	     	 Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding a project to the Break Glass ticket ...");
    	            	    SeleniumImplementation.addProject(driver, projectStr, instanceNumber, lastWindowHandleOfBGCR);
                    		break;

    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_SUBMIT_TICKET:
    	            	    // SeleniumImplementation.submitBreakGlassForApproval(driver, instanceNumber);
                    		break; 
    	            default:break;
    	        } 
    		} 
      		
    	}
       	logger.debug("Exiting SeleniumTest.completeBreakGlassMultipleTasks");
 }    
    
    public static void printTheTaskSelected(String CR_Number, int[] multiTasks, int[] bg_multiTasks){
        if (actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PLAN){
        	logger.info("Adding an implemention plan to the ticket " + CR_Number + "...");
        } else if (actionNumber == SeleniumImplementation.STANDARD_CR_REMOVE_PLAN){
        	logger.info("Removing the implemention plan from the ticket " + CR_Number + "...");
        } else if (actionNumber == SeleniumImplementation.STANDARD_CR_EDIT_CR) {
        	logger.info("Editing the ticket " + CR_Number);
        } else if (actionNumber == SeleniumImplementation.STANDARD_CR_COPY_CR){
        	logger.info("Copying a new CR from " + CR_Number + "...");
        } else if (actionNumber == SeleniumImplementation.STANDARD_CR_ADD_CIs || actionNumber == SeleniumImplementation.BREAK_GLASS_CR_ADD_CIS){
        	logger.info("Adding CIs to the ticket " + CR_Number + "...");
        } else if (actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PAM_TEMPLATES || actionNumber == SeleniumImplementation.BREAK_GLASS_CR_ADD_PAM_TEMPLATES){
        	logger.info("Adding PAM Templates to the ticket " + CR_Number + "...");
        } else if (actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PROJECT || actionNumber == SeleniumImplementation.BREAK_GLASS_CR_ADD_PROJECT){
        	logger.info("Adding a project to the ticket " + CR_Number + "...");
        } else if (actionNumber == SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET_AND_SUBMIT){
        	logger.info("Creating a new Break Glass CR Ticket and submitting it ...");
        } else if (actionNumber == SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET){
        	logger.info("Creating a new Break Glass CR Ticket...");
        } else if (actionNumber == SeleniumImplementation.STANDARD_CR_ADD_APPROVER){
        	logger.info("Adding approvers to the CR Ticket " + CR_Number + "...");
        } else if (actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS){
        	printMultipleTasks(multiTasks);
        } else if(actionNumber == SeleniumImplementation.BREAK_GLASS_CR_MULTIPLE_TASKS){
        	SeleniumImplementation.printBreakGlassMultipleTasks(bg_multiTasks);
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR){
        	logger.info("Creating a new CR from the spreadsheet...");
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL){
        	logger.info("Adding Deployment Tool configuration to the ticket " + CR_Number + "...");        	
        }
    }
    
    public static void printProperties(Map<String, String> properties, int instanceNumber){
    	if(properties == null || properties.size() == 0){
    		logger.error("No properties were provided to print. Please check... ");
			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "No properties were provided to print. Please check... ");
 	} else {   	    
    		String CR_Number = properties.get("CR_NUMBER");
    		String IN_Number = properties.get("IN_NUMBER");
    		String excelFileName = properties.get("EXCEL_FILE_NAME");
    		String chromeWebDriver = properties.get("CHROME_WEB_DRIVER");
    		String SDM_URL = properties.get("SDM_URL");
    		String UserId = properties.get("USER_ID");
    		String Password = properties.get("PASSWORD");
    		String VirtualWindow = properties.get("VirtualWindow");
    		String Debug = properties.get("Debug");
    		String MultiTasks = properties.get("MultiTasks");
    		String BG_MultiTasks = properties.get("BG_MultiTasks");
    		
    		logger.info("CR_Number:" + CR_Number);
    		logger.info("IN_Number:" + IN_Number);
    		logger.info("excelFileName:" + excelFileName);
    		logger.info("chromeWebDriver:" + chromeWebDriver);
    		logger.info("SDM_URL:" + SDM_URL);
    		logger.info("UserId:" + UserId);
    		if(Password == null || Password.trim().equals("")){
    		   logger.info("Password: ");
    		} else {
    			logger.info("Password:xxxxxx");
    		}
    		logger.info("VirtualWindow:" + VirtualWindow);
    		logger.info("Debug:" + Debug);
    		logger.info("MultiTasks:" + MultiTasks);
    		logger.info("BG_MultiTasks:" + BG_MultiTasks);
    	}
    }
    
    public static void printAddedCIs(ArrayList<String> addedCIs){
    	logger.debug("Entering SeleniumTest.printAddedCIs...");
    	if(addedCIs != null && addedCIs.size() > 0){
    		logger.info("");
    		logger.info("##############################################");    		
    		logger.info("The following CIs have been added to the CR ticket:");
    		for(int i = 0; i < addedCIs.size(); i++){
    			logger.info(addedCIs.get(i));
    		}
    	} else {
    		logger.info("No CIs was added to the CR ticket...");
    	}
    	logger.debug("Exiting SeleniumTest.printAddedCIs...");
   }
    
    public static void printFailedCIs(ArrayList<String> failedCIs){
    	logger.debug("Entering SeleniumTest.printFailedCIs...");
       	if(failedCIs != null && failedCIs.size() > 0){
    		logger.info("");
    		logger.info("##############################################");    		
    		logger.info("It was not successful to add the following CIs to the CR ticket:");
    		for(int i = 0; i < failedCIs.size(); i++){
    			logger.info(failedCIs.get(i));
    		}
    		logger.info("Please check if they are valid CIs or if they are in the CR ticket already");
    	}
       	logger.debug("Exiting SeleniumTest.printFailedCIs...");
    }
    
    public static void printFailedPAMTemplates(){
    	logger.debug("Entering SeleniumTest.printFailedPAMTemplates...");
       	if(failedPAMTemplates != null && failedPAMTemplates.size() > 0){
    		logger.info("");
    		logger.info("##############################################");    		
    		logger.info("It was not successful to add the following PAM Templates to the CR ticket:");
    		for(int i = 0; i < failedPAMTemplates.size(); i++){
    			PAMTemplate template = failedPAMTemplates.get(i);
    			logger.info(template.getCi() + "  " + template.getPamLocation() + "  " + template.getGroup());
    		}
    		logger.info("Please check if they are the valid PAM Templates");
    	}
       	logger.debug("Exiting SeleniumTest.printFailedPAMTemplates...");
    }  
    
    public static void printMultipleTasks(int[] multiTasks){
    	if(multiTasks == null || multiTasks.length ==0) {
    		logger.info("No multiple tasks were defined. Please check selenium.properties to see if the property MultiTasks was defined correctly.");
    	} else {
    		logger.info("You are planning to do the following tasks:");
    		for(int i = 0; i < multiTasks.length; i++){
    	        String taskString;
    	        switch (multiTasks[i]) {
    	            case SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR:  taskString = SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR_TASK;
    	                     break;
    	            case SeleniumImplementation.STANDARD_CR_COPY_CR:  taskString = SeleniumImplementation.STANDARD_CR_COPY_CR_TASK;
    	                     break;
    	            case SeleniumImplementation.STANDARD_CR_EDIT_CR:  taskString = SeleniumImplementation.STANDARD_CR_EDIT_CR_TASK;
                    		 break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_PLAN:  taskString = SeleniumImplementation.STANDARD_CR_ADD_PLAN_TASK;
                    		 break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_CIs:  taskString = SeleniumImplementation.STANDARD_CR_ADD_CIs_TASK;
                    		 break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_PAM_TEMPLATES:  taskString = SeleniumImplementation.STANDARD_CR_ADD_PAM_TEMPLATES_TASK;
                    		 break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_PROJECT:  taskString = SeleniumImplementation.STANDARD_CR_ADD_PROJECT_TASK;
                    		break;
    	            case SeleniumImplementation.STANDARD_CR_ADD_APPROVER:  taskString = SeleniumImplementation.STANDARD_CR_ADD_APPROVER_TASK;
                    		break;
    	            case SeleniumImplementation.STANDARD_CR_CHANGE_SURVEY:  taskString = SeleniumImplementation.STANDARD_CR_CHANGE_SURVEY_TASK;
                    		break;
    	            case SeleniumImplementation.STANDARD_CR_REMOVE_PLAN:  taskString = SeleniumImplementation.STANDARD_CR_REMOVE_PLAN_TASK;
                    		break;
    	            case SeleniumImplementation.STANDARD_CR_SUBMIT_FOR_APPROVAL:  taskString = SeleniumImplementation.STANDARD_CR_SUBMIT_FOR_APPROVAL_TASK;
                    		break;
    	            default: taskString = "Invalid Task";
    	                    break;
    	        }
    	        logger.info(taskString);
    		}
    	}
    }
    
    
    public static void printBreakGlassMultipleTasks(int[] bg_multiTasks){
    	if(bg_multiTasks == null || bg_multiTasks.length ==0) {
    		logger.error("No multiple tasks for Break Glass were defined. Please check selenium.properties to see if the property BG_MultiTasks was defined correctly.");
    	} else {
    		logger.info("You are planning to do the following tasks for the Break Glass CR ticket:");
    		for(int i = 0; i < bg_multiTasks.length; i++){
    	        String taskString;
    	        switch (bg_multiTasks[i]) {
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_CREATE_TICKET:  taskString = SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET_TASK;
    	                     break;
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_CIS:  taskString = SeleniumImplementation.BREAK_GLASS_CR_ADD_CIS_TASK;
                    		 break;
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_PAM_TEMPLATE:  taskString = SeleniumImplementation.BREAK_GLASS_CR_ADD_PAM_TEMPLATES_TASK;
                    		 break;
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_PROJECT:  taskString = SeleniumImplementation.BREAK_GLASS_CR_ADD_PROJECT_TASK;
                    		break;
    	            case SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_SUBMIT_TICKET:  taskString = SeleniumImplementation.BREAK_GLASS_CR_SUBMIT_FOR_APPROVAL_TASK;
                    		break;
    	            default: taskString = "Invalid Task";
    	                    break;
    	        }
    	        logger.info(taskString);
    		}
    	}
    }   
    
    

    public static void menu(){
   	 scan = new Scanner(System.in);
     logger.info("Please enter the number: ");
     logger.info(" 1 : to manage standard CR ticket");
     logger.info(" 2 : to manage Break Glass CR ticket");
     logger.info(" 0 : to exit the program");
     boolean flag = false;
     while(!flag){
     // This method reads the number provided using keyboard
    	 manageNumber = scan.nextInt();
    	 if(manageNumber == SeleniumImplementation.MANAGE_TYPE_STANDARD_CR || manageNumber == SeleniumImplementation.MANAGE_TYPE_BREAK_GLASS_CR 
        		  || manageNumber == SeleniumImplementation.EXIT_NUMBER) {
   	// if(actionNumber == SeleniumTest.STANDARD_CR_ADD_PLAN || actionNumber == SeleniumTest.STANDARD_CR_REMOVE_PLAN || actionNumber == SeleniumTest.STANDARD_CR_EXIT) {

    		 flag = true;
        	 // logger.info("Get the number: " + actionNumber);
         } else {
        	// logger.info("Please enter number 1, 2, 3 or 0");
        	 logger.info("Please enter number 1, 2, or 0");
         }	 
     }
     if(manageNumber == SeleniumImplementation.EXIT_NUMBER){
    	 return;
     } else  if(manageNumber == SeleniumImplementation.MANAGE_TYPE_STANDARD_CR){
         logger.info("Please enter the number for managing the standard CR ticket: ");
         logger.info(" 1: to create new CR ticket");
         logger.info(" 2 : to copy a CR ticket");
         logger.info(" 3 : to edit a CR ticket");
         logger.info(" 4 : to add an implementation plan to the CR ticket");
         logger.info(" 5 : to add CIs");
         logger.info(" 6 : to add PAM Templates");
         logger.info(" 7 : to add a project");
         logger.info(" 8 : to add approvers");
         logger.info(" 9 : to add Deployment Tool");
         logger.info(" 10 : to complete Change Survey");
         logger.info(" 11 : to delete the implementation plan from the CR ticket");
         logger.info(" 12: to submit CR for approval");
         logger.info(" 13: to save CR ticket Information and Implementation tasks to a spreadsheet");
         logger.info(" 14: to do multiple tasks at a time");
         // System.out.println(" 14: to reflesh implementation plan");
         logger.info(" 0 : to exit the program");
     }  if(manageNumber == SeleniumImplementation.MANAGE_TYPE_BREAK_GLASS_CR){
         logger.info("Please enter the number for managing the Break Glass CR ticket: ");
         logger.info(" 1 : to Create a Break Glass CR ticket");
         // logger.info(" 3 : to edit a Break Glass CR ticket");
         logger.info(" 2 : to add CIs");
         logger.info(" 3 : to add PAM Templates");
         logger.info(" 4 : to Add a project");
         logger.info(" 5 : to Submit Break Glass CR ticket");
         logger.info(" 6 : to do multiple tasks for a Break Glass CR ticket");
         logger.info(" 0 : to exit the program");
    } 
     
     flag = false;
     while(!flag){
     // This method reads the number provided using keyboard
    	 actionNumber = scan.nextInt();
    	 if(manageNumber == SeleniumImplementation.MANAGE_TYPE_STANDARD_CR){
    		// System.out.println("management number is for standard CR");
	          if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PLAN || actionNumber == SeleniumImplementation.STANDARD_CR_REMOVE_PLAN 
	        		  || actionNumber == SeleniumImplementation.STANDARD_CR_EDIT_CR || actionNumber == SeleniumImplementation.STANDARD_CR_COPY_CR 
	        		  || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_CIs || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PAM_TEMPLATES
	        		  || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PROJECT || actionNumber == SeleniumImplementation.STANDARD_CR_CHANGE_SURVEY
	        		  || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_APPROVER  || actionNumber == SeleniumImplementation.STANDARD_CR_SUBMIT_FOR_APPROVAL
	        		  || actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS  || actionNumber == SeleniumImplementation.STANDARD_CR_SAVE_TASKS_TO_SPREADSHEET
	        		  || actionNumber == SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR
	        		  || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL  || actionNumber == SeleniumImplementation.EXIT_NUMBER ) {
	    	// if(actionNumber == SeleniumTest.STANDARD_CR_ADD_PLAN || actionNumber == SeleniumTest.STANDARD_CR_REMOVE_PLAN || actionNumber == SeleniumTest.STANDARD_CR_EXIT) {
	
	    	    flag = true;
	        	 // System.out.println("Get the number: " + actionNumber);
	         } else {
	        	// System.out.println("Please enter number 1, 2, 3 or 0");
	        	 logger.info("Please enter number 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 or 0");
	         }
    	 } else if(manageNumber == SeleniumImplementation.MANAGE_TYPE_BREAK_GLASS_CR){
    		 logger.info("management Number is for Break Glass");
    		 if(actionNumber == 1 || actionNumber == 2 || actionNumber == 3 
    			|| actionNumber == 4 || actionNumber == 5 || actionNumber == 6 
    			|| actionNumber == 0){
    			 flag = true;
    			 if(actionNumber == 1) {
    				 actionNumber = SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET;
    			 } else if(actionNumber == 2) {
    				 actionNumber = SeleniumImplementation.BREAK_GLASS_CR_ADD_CIS;
    			 } else if(actionNumber == 3) {
    				 actionNumber = SeleniumImplementation.BREAK_GLASS_CR_ADD_PAM_TEMPLATES;
    			 } else if(actionNumber == 4) {
    				 actionNumber = SeleniumImplementation.BREAK_GLASS_CR_ADD_PROJECT;
    			 } else if(actionNumber == 5) {
    				 actionNumber = SeleniumImplementation.BREAK_GLASS_CR_SUBMIT_FOR_APPROVAL;
    			 } else if(actionNumber == 6){
    				 actionNumber = SeleniumImplementation.BREAK_GLASS_CR_MULTIPLE_TASKS;
    			 }
    		 } else {
	        	// System.out.println("Please enter number 1, 2, 3 or 0");
	        	 logger.info("Please enter number 1, 2, 3, 4, 5, 6, or 0");
	         }
    		 logger.info("Action Number: " + actionNumber);
    	 }
      }    	
    }
    

    
    public static boolean prevalidation(String fileName, int instanceNumber, int serverTimeDifference) throws ParseException{
    	boolean result = true;
 		Task[] tasks = null;
 		Map<String, String> items = null; 
 		if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PLAN){
	 		   try{
	 			   logger.debug("Get Tasks from Excel");	
	 			   tasks = Utilities.getTasksFromExcel(fileName, serverTimeDifference);
	 			  logger.debug("Got the tasks from the spreadshhet");
	 		   } catch(FileNotFoundException e){
	 			    logger.error("The plan spreadsheet " + fileName + " is not found. Please check.");
		     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "The plan spreadsheet " + fileName + " is not found. Please check.");
	 			    e.printStackTrace();
	 			    return false;

	 		   } catch(IOException e){
	 			   
	 			   logger.error("Exception happened when reading the plan spreadsheet " + fileName + ". Please check.");
		     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Exception happened when reading the plan spreadsheet " + fileName + ". Please check.");
	 			   e.printStackTrace();
	 			   return false;
	 			   
	 		   } 
	 		
		} else if (actionNumber == SeleniumImplementation.STANDARD_CR_COPY_CR){
			try{
				logger.info("Get information from spreadsheet for new CR");
				items = Utilities.readCRInfoFromSpreadsheet(fileName, serverTimeDifference);
				if(!Utilities.validateDatesInSpreadSheetForCopyingCR(items, instanceNumber)){
					logger.error("The validation of dates failed for copying CR. Please check the dates in the spreadsheet.");
		     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "The validation of dates failed for copying CR. Please check the dates in the spreadsheet.");
					result = false;
				}
			}catch(Exception e){
				logger.error("The validation of dates failed for copying CR because of exception.: " + e.getMessage());
	     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "The validation of dates failed for copying CR because of exception. Please check with the administrator...");
				return false;
			}
		} else if (actionNumber == SeleniumImplementation.STANDARD_CR_EDIT_CR || actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PROJECT ){
			try{
				logger.info("Get information from spreadsheet for Editing the CR ticket");
	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Rechieving information from the spreadsheet for Editing the CR ticket...");
				items = Utilities.readCRInfoFromSpreadsheet(fileName, serverTimeDifference);			
			}catch(Exception e){
				logger.error("Reading CR information from the spreadsheet failed because of exception: " + e.getMessage());
	     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Reading CR information from the spreadsheet failed because of exception. Please check with the administrator...");
				return false;
			}
		}     
 		return result;
    }
    
	public static String takeSnapShot(WebDriver webdriver,String fileDir) throws Exception{

        //Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

        //Call getScreenshotAs method to create image file

                File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
			    String fileName = new Date().getTime()+ "-screenshot.png";
                
                File DestFile=new File(fileDir + fileName); 

                //Copy file at destination
               FileUtils.copyFile(SrcFile, DestFile);
               logger.debug("The screenshot object was saved to: " + fileDir + fileName);                
               return fileName;

    }

}

