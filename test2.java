package com.hsbc.selenium.crmanagement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.hsbc.selenium.crmanagement.controller.crmanagementController;
import com.hsbc.selenium.crmanagement.model.AddCIs;

import com.hsbc.selenium.crmanagement.model.Task;
import com.hsbc.selenium.crmanagement.utilities.SeleniumImplementation;
import com.hsbc.selenium.crmanagement.utilities.Utilities;

public class CRAutomationWeb {
	private static String SDM_URL ="https://gsdprodlogin.us.hsbc/CAisd/pdmweb.exe";
	private static String excelFileRootPath;
	private static String nodeURL;
	private static boolean isVirtual = false;
	private static boolean debugEnabled = false;
	private static boolean werePropertiesSet;
	private static String templateFilePath;
	private static int servertimeZone;
	private String excelFileName="cr1.xlsx";
	private String userID = "";
	private String password = "";
	private WebDriver driver;
	private String CR_Number;
	private String IN_Number;
	private static final String propertyFilePathLinux = "/opt/seleniumGrid/crmanagement.properties";
	private static final String propertyFilePathWin = "C:/Temp/seleniumGrid/crmanagement.properties";
	private static Logger logger = Logger.getLogger(CRAutomationWeb.class);
	
	public static void setupProperties() {
		logger.info("Set up Properties...");
		String OS = System.getProperty("os.name").toLowerCase();
        logger.info("The OS of the server is " + OS);
        String propertyFilePath = propertyFilePathLinux;
        
        if (OS.indexOf("win") >= 0) {
            // System.out.println("This is Windows");
            propertyFilePath = propertyFilePathWin;
        } 
        
        logger.info("crmanagement property File Path: " + propertyFilePath);
        
        Map<String, String> crmanagementProperties = Utilities.getCrManagementProperties(propertyFilePath);
        if(crmanagementProperties != null) {
        	String nodeURLStr = crmanagementProperties.get("NodeURL");
        	String virtualWindowStr = crmanagementProperties.get("VirtualWindow");
        	String SDM_URLStr = crmanagementProperties.get("SDM_URL");
        	String debugStr = crmanagementProperties.get("Debug");
           	String excelFileRootPathStr = crmanagementProperties.get("ExcelFileRootPath");
           	String templateFilePathStr = crmanagementProperties.get("TemplateFilePath");
           	String serverTimezoneStr = crmanagementProperties.get("ServerTimezone");
           	
           	logger.info("NodeURL: " + nodeURLStr);
           	logger.info("virtualWindowStr: " + virtualWindowStr);
           	logger.info("SDM_URLStr: " + SDM_URLStr);
           	logger.info("debugStr: " + debugStr);
           	logger.info("excelFileRootPath: " + excelFileRootPathStr);
           	logger.info("templateFilePath: " + templateFilePathStr);
           	logger.info("serverTimezoneStr: " + serverTimezoneStr);
           	
           	if(nodeURLStr != null && !nodeURLStr.trim().equals("")) {
           		nodeURL = nodeURLStr;
           	}

           	if(SDM_URLStr != null && !SDM_URLStr.trim().equals("")) {
           		SDM_URL = SDM_URLStr;
           	}

           	if(excelFileRootPathStr != null && !excelFileRootPathStr.trim().equals("")) {
           		excelFileRootPath = excelFileRootPathStr;
           	}
           	
           	if(templateFilePathStr != null && !templateFilePathStr.trim().equals("")) {
           		templateFilePath = templateFilePathStr;
           	}
           	
           	if(serverTimezoneStr != null && !serverTimezoneStr.trim().equals("")) {
           		servertimeZone = Integer.parseInt(serverTimezoneStr);
           	}
           	
           	
    	    if(debugStr != null && debugStr.trim().equalsIgnoreCase("Yes")){
    	    	debugEnabled = true;
    	    	Utilities.setDebugEnabled(true);
    	    } else {
    	    	debugEnabled = false;
    	    	Utilities.setDebugEnabled(false);
    	    }
 
    	    if(virtualWindowStr != null  && virtualWindowStr.trim().equalsIgnoreCase("Yes")){
    	    	isVirtual = true;
    	    } else {
    	    	isVirtual = false;
    	    }
    	    werePropertiesSet = true;
        } else {
        	logger.error("Not able to get the setup properties from the properties file!!! Please check!");
        }
	}
	
	public static boolean getWerePropertiesSet() {
		return werePropertiesSet;
	}
	

	public boolean setupWebDriver() {
		boolean result = true;
		if(!werePropertiesSet) {
			setupProperties();
		} 
		logger.info("Set up Capabilities...");
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setBrowserName("chrome");
		cap.setPlatform(Platform.WINDOWS);
		// String Node = "http://localhost:4450/wd/hub";
		// String Node = "http://130.178.100.124:4450/wd/hub";
		String Node = nodeURL;
         ChromeOptions options = new ChromeOptions();
         options.addArguments("--headless");  
         
         if(isVirtual){
	       cap.setCapability(ChromeOptions.CAPABILITY, options);
         }
 		logger.info("Initiate remote driver...");
 		logger.info("nodeURL:" + Node);
		WebDriver driver = null;
		try{
			driver = new RemoteWebDriver(new URL(Node), cap);
			logger.info("Setting implicitly wait timeout of remote driver ...");
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		} catch(Exception e) {
			logger.error("Exception happened when getting a new Remote WebDriver...", e);
			result = false;
			if(driver != null) {
				driver.quit();
				driver = null;
			}
		}
		// driver.manage().window().fullscreen();
		this.driver = driver;
		return result;
	}
		
	public boolean loginToSDM(String userID, String password, int instanceNumber)  {
		logger.debug("Entering CRAutomationWeb.loginToSDM");
		boolean result = true;
		logger.info("Logging into Service Desk Manager...");
        // SeleniumTest.setWebDriver(driver);
        //SeleniumImplementation.loginToSDM(driver, SDM_URL, this.getUserID(), this.getPassword()); 
		SeleniumImplementation.loginToSDM(driver, SDM_URL, userID, password, instanceNumber); 
        logger.debug("Exiting CRAutomationWeb.loginToSDM");
        return result;
	}	
	
	
	public boolean precheckCRTask(String excelFilePath, int actionNumber, int[] multiTasks, Map<String, String> items, Task[] tasks, int instanceNumber) {
		boolean result = true;
		//String excelFilePath = excelFileRootPath + excelFileName;


        if(actionNumber == SeleniumImplementation.STANDARD_CR_COPY_CR || actionNumber == SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR
                || actionNumber == SeleniumImplementation.STANDARD_CR_EDIT_CR ){
        	   try{
        		
        		  // items = Utilities.readCRInfoFromSpreadsheet(planFileName);
        		  if(!SeleniumImplementation.validateCRDatesWithCurrentTime(items, instanceNumber)){
        			return false;
        		}
        	   } catch(Exception e){
        		   e.printStackTrace();
        		   logger.error("Exception happened: " + e.getMessage());
	       			// SeleniumTest.closeAll(driver);
         		   Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Exception happened when doing precheck before doing the task. Please contact administrator ... ");
        		   return false;
        		   
        	   }
        }
        
        if(actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS){
        	if(SeleniumImplementation.doMultiTasksContainATask(multiTasks, SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR)
        	 || SeleniumImplementation.doMultiTasksContainATask(multiTasks, SeleniumImplementation.STANDARD_CR_COPY_CR)
        	 || SeleniumImplementation.doMultiTasksContainATask(multiTasks, SeleniumImplementation.STANDARD_CR_EDIT_CR)){
         	   try{
           		
         		 // items = Utilities.readCRInfoFromSpreadsheet(planFileName);
         		  if(!SeleniumImplementation.validateCRDatesWithCurrentTime(items, instanceNumber)){
         		      // SeleniumTest.closeAll(driver);
         			  return false;
         		}
         	   } catch(Exception e){
         		   e.printStackTrace();
         		   logger.error("Exception happened: " + e.getMessage());
         		   Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Exception happened when doing precheck before doing the task. Please contact administrator ... ");
 	       			// SeleniumTest.closeAll(driver);
 	       			return false;
         		   
         	   }       		
        	}
        	
        }
        
        if(SeleniumImplementation.isReadingImplementationPlanRequired(actionNumber, multiTasks)){
        	try{
        		if(!SeleniumImplementation.validateTaskDatesWithCurrentTime(tasks, instanceNumber)){
         			return false;
        		}
        	} catch(Exception e){
      		   e.printStackTrace();
      		   logger.error("Exception happened: " + e.getMessage());
     		   Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Exception happened when doing precheck before doing the task. Please contact administrator ... ");
	       	   return false;
      		   
      	   }  
        }        
 		
		return result;
	}
	
	public boolean completeCRTask(String excelFilePath, int actionNumber, int[] multiTasks, Map<String, String> items, Task[] tasks, int[] bg_multiTasks, int instanceNumber, String IN_Number, String bgCRActivities, int serverTimeDifference) throws Exception{
		boolean result = true;
        String messageToPrint = "";
 		//if it's to add a new plan, get the tasks from the spreadsheet and check the groups for the tasks
 		if(actionNumber == SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR){
 			Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Creating new CR ticket...");
 			SeleniumImplementation.createStandardCRTicket(driver, items, instanceNumber);
            SeleniumImplementation.closeAll(driver); 
            return result;
 		}
 		
        if(actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS){
        	if(Utilities.isNewTicketCreatedInMultipleTasks(multiTasks)){
	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Working on the multiple tasks you selected...");
	        	SeleniumImplementation.completeMultipleTasks(driver, multiTasks, excelFilePath, CR_Number, instanceNumber, serverTimeDifference);
	            SeleniumImplementation.printFailedPAMTemplates();
	        	SeleniumImplementation.closeAll(driver);
       	 	    return result;    
        	}
        }       
        
 		if (actionNumber == SeleniumImplementation.STANDARD_CR_COPY_CR){
 			try{
 				if(!Utilities.validateDatesInSpreadSheetForCopyingCR(items, instanceNumber)){
 					logger.error("The validation of dates failed. Please check the dates in the spreadsheet.");
 		     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "The validation of dates failed. Please check the dates in the spreadsheet...");
					SeleniumImplementation.closeAll(driver);
 	 				return false;
 				}
 			}catch(Exception e){
 				logger.error("Got exception while copying CR ticket: " + e.getMessage());
 				e.printStackTrace();
	     		   Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Got exception while copying CR ticket. Please contact the administrator...");
 				SeleniumImplementation.closeAll(driver);
 				return false;
 			}
 		} 
 		
 		int waitTime = 12000;
 		if(actionNumber == SeleniumImplementation.STANDARD_CR_REMOVE_PLAN) waitTime = 14000;
        SeleniumImplementation.waitForWindow(waitTime);
      
        if(actionNumber == SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET){
        	SeleniumImplementation.createBreakGlassTicketInAINPage(driver, IN_Number, excelFilePath, instanceNumber, serverTimeDifference);
            SeleniumImplementation.closeAll(driver);
            return result;
        }
        
        if(actionNumber == SeleniumImplementation.BREAK_GLASS_CR_MULTIPLE_TASKS && Utilities.isNewBGTicketCreatedInMultipleTasks(bg_multiTasks)){
        	SeleniumImplementation.completeBreakGlassMultipleTasks(driver, bg_multiTasks, excelFilePath, CR_Number, IN_Number, instanceNumber, serverTimeDifference);
            SeleniumImplementation.closeAll(driver);
            return result;
        }       
        
        if(!SeleniumImplementation.openCRTicket(driver, CR_Number, instanceNumber)) {
        	 Utilities.setCRAutomationWebInstanceCompleted(instanceNumber);
        	 SeleniumImplementation.closeAll(driver);
        	 return true;
        }
        driver.switchTo().frame(3);
        SeleniumImplementation.waitForElementById(driver, "df_7_0");
        SeleniumImplementation.waitForElementById(driver, "df_7_2");
        String scheduledStatDate = driver.findElement(By.id("df_7_0")).getText();
        logger.info("Scheduled Start Date: " + scheduledStatDate);
        String scheduledEndDate = driver.findElement(By.id("df_7_2")).getText();
        logger.info("Scheduled End Date: " + scheduledEndDate);
        Object[][] crInfoToSave = null;
        
        if(actionNumber == SeleniumImplementation.STANDARD_CR_SAVE_TASKS_TO_SPREADSHEET){
        	crInfoToSave = SeleniumImplementation.getCRTicketInfoFromPage(driver, CR_Number, serverTimeDifference);
        }

        if(actionNumber == SeleniumImplementation.STANDARD_CR_COPY_CR){
            	logger.info("Creating a new ticket by copying from " + CR_Number);
        		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Creating a new ticket by copying from " + CR_Number);
            	SeleniumImplementation.copyCR(driver, items, instanceNumber);
            	logger.info("The new created ticket " + CR_Number + " was edited with values from spreadsheets and saved successfully...");
        		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Creating a new ticket by copying from " + CR_Number);
            	
            	SeleniumImplementation.saveAndCloseImplementationPlanToRefresh(driver, CR_Number, instanceNumber);
            SeleniumImplementation.closeAll(driver);
        	return result;
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_MULTIPLE_TASKS){
        	if(!Utilities.isNewTicketCreatedInMultipleTasks(multiTasks)){
	        	SeleniumImplementation.completeMultipleTasks(driver, multiTasks, excelFilePath, CR_Number, instanceNumber, serverTimeDifference);
	        	SeleniumImplementation.closeAll(driver);;
       	 	    return false;    
        	}
        } else  if(actionNumber == SeleniumImplementation.STANDARD_CR_EDIT_CR) {
        	if(!Utilities.validateDatesInSpreadSheetForEditingCR(items, scheduledStatDate, scheduledEndDate, instanceNumber)){
        		logger.error("The validation of CR dates failed. Please check the dates in the spreadsheet.");
	     		   Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "The validation of CR dates failed. Please check the dates in the spreadsheet.");
        	} else {
        		logger.info("The dates in the CR spreadsheet are valid.");
	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The dates in the CR spreadsheet are valid.");
	      	    //click "Edit" button to edit the CR ticket
        		SeleniumImplementation.waitForElementById(driver, "imgBtn0");
	        	driver.findElement(By.id("imgBtn0")).click();
	        	SeleniumImplementation.waitForWindow(2000);
	        	logger.info("Editing the ticket " + CR_Number);
	     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Editing the ticket " + CR_Number + " ...");
	        	SeleniumImplementation.editCR(driver, CR_Number, items, false, instanceNumber);
        	}
        	messageToPrint = "";
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_APPROVER) {
        	ArrayList approvers = Utilities.getApproversFromExcel(excelFilePath);
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding approvers from the spreadsheet to the ticket " + CR_Number + " ...");
        	SeleniumImplementation.addApprovers(driver, approvers, instanceNumber);
        	SeleniumImplementation.waitForWindow(4000);
        	messageToPrint = "";       	
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_CHANGE_SURVEY) {
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Completing survey for the ticket " + CR_Number + " ...");
        	SeleniumImplementation.completeChangeSurvey(driver, excelFilePath);
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Completed survey for the ticket " + CR_Number + " ...");
     		messageToPrint = "";
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL){
        	String packageId = items.get("PACKAGE_ID");
        	String deploymentTool = items.get("DEPLOYMENT_TOOL");
        	logger.info("PACKAGE_ID: " + packageId);
        	logger.info("DEPLOYMENT_TOOL: " + deploymentTool);
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding Deployment Tool for the ticket " + CR_Number + " ...");
        	SeleniumImplementation.addDeploymentTool(driver, packageId, deploymentTool, instanceNumber);
        	messageToPrint = "";
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_CIs || actionNumber == SeleniumImplementation.BREAK_GLASS_CR_ADD_CIS){
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding CIs to the ticket " + CR_Number + " ...");
        	AddCIs cis = SeleniumImplementation.addCIs(driver, excelFilePath, instanceNumber, null);
        	SeleniumImplementation.printAddedCIs(cis.getAddedCIs());
        	SeleniumImplementation.printFailedCIs(cis.getFailedCIs());
        	messageToPrint = "";      	
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PAM_TEMPLATES || actionNumber == SeleniumImplementation.BREAK_GLASS_CR_ADD_PAM_TEMPLATES){
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding PAM templates to the ticket " + CR_Number + " ...");
        	SeleniumImplementation.addPAMTemplatesFromSpreadsheet(driver, excelFilePath, instanceNumber, null);
        	SeleniumImplementation.waitForWindow(6000);
        	logger.info("Refreshing the page...");
        	SeleniumImplementation.refreshPage(driver);
        	messageToPrint = "";
         } else if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PROJECT){
        	String projectStr = items.get("PROJECT_ID");
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding a project to the ticket " + CR_Number + " ...");
        	SeleniumImplementation.addProject(driver, projectStr, instanceNumber, null);
        	messageToPrint = "";
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_SUBMIT_FOR_APPROVAL){
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Submitting the ticket " + CR_Number + " for approval...");
        	SeleniumImplementation.submitStandardCRForApproval(driver);
        	messageToPrint = "";
        } else if(actionNumber == SeleniumImplementation.BREAK_GLASS_CR_ADD_PROJECT){
            Map<String, String> bgItems = Utilities.readBreakGlassTicketFromSpreadsheet(excelFilePath);
            String projectStr = bgItems.get("PROJECT_ID");
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Adding a project to the ticket " + CR_Number + " ...");
            SeleniumImplementation.addProject(driver, projectStr, instanceNumber, null);
            SeleniumImplementation.waitForWindow(2000);
            logger.info("Refreshing the page...");
            SeleniumImplementation.refreshPage(driver);
            messageToPrint = "";
        } else if(actionNumber == SeleniumImplementation.BREAK_GLASS_CR_SUBMIT_FOR_APPROVAL){
     		// Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Submitting the Break Glass ticket " + CR_Number + " for approval ...");
        	SeleniumImplementation.submitBreakGlassForApproval(driver, instanceNumber, bgCRActivities);
        	messageToPrint = "";
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_ADD_PLAN){
	        if(!SeleniumImplementation.validateDatesOfTasks(tasks, scheduledStatDate, scheduledEndDate, instanceNumber)){
	        	logger.error("Did not pass the dates' validation of tasks in the implementation plan.");
	        	logger.error("Please check error messages above and fix the problematic dates in spreadsheet.");	        	
	     		Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Did not pass the dates' validation of tasks in the implementation plan. Please check the error messages and fix the problematic dates in spreadsheet...");
	        	SeleniumImplementation.closeAll(driver);
	        	return false;
	        } else {
	        	if(SeleniumImplementation.addImplementationPlan(driver, tasks, CR_Number, instanceNumber)){
	        		messageToPrint = "Implemation Plan was created and saved to " + CR_Number;
	         		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Implemation Plan was created and saved to " + CR_Number);
	        	} else {
	        		messageToPrint = "No implementation plan was added to " + CR_Number;
	         		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "No implementation plan was added to " + CR_Number);
     	}
	        }
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_REMOVE_PLAN){
        	SeleniumImplementation.waitForWindow(1000);
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Removing the implementation plan from the ticket " + CR_Number);
        	SeleniumImplementation.removePlan(driver, CR_Number, instanceNumber);
        	messageToPrint = " ";
        } else if(actionNumber == SeleniumImplementation.BREAK_GLASS_CR_MULTIPLE_TASKS && !Utilities.isNewBGTicketCreatedInMultipleTasks(bg_multiTasks)){
        		SeleniumImplementation.completeBreakGlassMultipleTasks(driver, bg_multiTasks, excelFilePath, CR_Number, IN_Number, instanceNumber, serverTimeDifference);
        		messageToPrint = "All the tasks defined in BG_MultiTasks of selenium.properties have been completed...";
        } else if(actionNumber == SeleniumImplementation.STANDARD_CR_SAVE_TASKS_TO_SPREADSHEET){
        	logger.info("Getting information of tasks in the implementation plan from the page ...");
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Getting information of tasks in the implementation plan from the page ...");
        	Object[][] tasksToSave = SeleniumImplementation.getImplementationTasksFromPage(driver, CR_Number, serverTimeDifference);
        	//SeleniumTest.saveCRTicketToSpreadsheet(CR_Number);
        	String spreadsheetFileName = CR_Number + ".xlsx";
        	String spreadsheetFullPath = excelFileRootPath  + spreadsheetFileName;
        	logger.info("Getting CIs from the pages...");
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Getting CIs from the pages...");
        	String[][] cis =  SeleniumImplementation.getCIsFromPage(driver, CR_Number);
        	logger.info("  ");        	
        	logger.info("#########################################");
        	logger.info("#########################################");        	
        	logger.info("Getting PAM Templates from the pages...");
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Getting PAM Templates from the pages...");
        	String[][] templates = SeleniumImplementation.getPAMTemplatesFromPage(driver, CR_Number);
           	//Utilities.copyFile(templateFilePath, spreadsheetFileName);
        	 String uploadedTemplateFilePath = excelFilePath;
        	logger.debug("Copy uploaded template file " + uploadedTemplateFilePath + " to the file " + spreadsheetFullPath);
        	Utilities.copyFile(uploadedTemplateFilePath, spreadsheetFullPath);
           	logger.info("  ");        	
           	logger.info("#########################################");
           	logger.info("#########################################");        	
           	logger.info("Saving the CR information of " +  CR_Number + " to the file " + spreadsheetFullPath + " ...");
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Saving the CR information of " +  CR_Number + " to the file " + spreadsheetFileName + " ...");
           	SeleniumImplementation.saveCRTicketToSpreadsheet(driver, crInfoToSave, tasksToSave, cis, templates, spreadsheetFullPath);
           	messageToPrint = "The ticket information of " + CR_Number + " was saved successfully to the file " + spreadsheetFileName;
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "The ticket information of " + CR_Number + " was saved successfully to the file " + spreadsheetFileName);
     		Utilities.appendCRAutomationWebInstanceMsg(instanceNumber,"<a href='/crmanagement/download?fn=" + spreadsheetFileName + "' >Download the new generated Excel file for the CR ticket " + CR_Number + " </a>");
      	}
        SeleniumImplementation.waitForWindow(4000);
        logger.info("Closing the web driver...");
        SeleniumImplementation.closeAll(driver);
		return result;
	}
	
	public static void main(String[] args) throws Exception{ 
		CRAutomationWeb automation = new CRAutomationWeb();
		if(!automation.getWerePropertiesSet()) {
			automation.setupProperties();
			System.out.println("NodeURL: " + automation.getNodeURL());
			System.out.println("ExcelFileRootPath: " + automation.getExcelFileRootPath());
		} 		

     }

	public static String getSDM_URL() {
		return SDM_URL;
	}

	public static void setSDM_URL(String sDM_URL) {
		SDM_URL = sDM_URL;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public static boolean isVirtual() {
		return isVirtual;
	}

	public static void setVirtual(boolean virtual) {
		isVirtual = virtual;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static String getExcelFileRootPath() {
		return excelFileRootPath;
	}

	public static void setExcelFileRootPath(String excelFileRoot) {
		excelFileRootPath = excelFileRoot;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public String getCR_Number() {
		return CR_Number;
	}

	public void setCR_Number(String cR_Number) {
		CR_Number = cR_Number;
	}

	public String getIN_Number() {
		return IN_Number;
	}

	public void setIN_Number(String iN_Number) {
		IN_Number = iN_Number;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public String getPassword() {
		return password;
	}

	public static String getNodeURL() {
		return nodeURL;
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	public static void setWerePropertiesSet(boolean werePropertiesSet) {
		CRAutomationWeb.werePropertiesSet = werePropertiesSet;
	}

	public static String getTemplateFilePath() {
		return templateFilePath;
	}

	public static void setTemplateFilePath(String templateFilePath) {
		CRAutomationWeb.templateFilePath = templateFilePath;
	}

	public static int getServertimeZone() {
		return servertimeZone;
	}
	
}
