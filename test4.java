package com.hsbc.selenium.crmanagement.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp; 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;     
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hsbc.selenium.crmanagement.CRAutomationWeb;
import com.hsbc.selenium.crmanagement.model.CRAutomationWebInstance;
import com.hsbc.selenium.crmanagement.model.CRTicketManager;
import com.hsbc.selenium.crmanagement.model.FileModel;
import com.hsbc.selenium.crmanagement.model.Task;
import com.hsbc.selenium.crmanagement.model.UserCredential;
import com.hsbc.selenium.crmanagement.utilities.CRAutomationWebInstanceFactory;
import com.hsbc.selenium.crmanagement.utilities.SeleniumImplementation;
import com.hsbc.selenium.crmanagement.utilities.Utilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;

@Controller
@MultipartConfig(maxFileSize = 10*1024*1024,maxRequestSize = 20*1024*1024,fileSizeThreshold = 5*1024*1024)

public class crmanagementController {
	
	private static Logger logger = Logger.getLogger(crmanagementController.class);
	private static Map<String,UserCredential> userCredentials;
	public static final int TICKET_TYPE_STANDARD_CR = 1;
	public static final int TICKET_TYPE_BREAKGLASS_CR = 2;
	private void addUserCredential(String fileName, UserCredential cred) {
		if(userCredentials == null) {
			userCredentials = new HashMap<String, UserCredential>();
		}
		if(cred != null) {
			logger.debug("credential to be added is not null");
			logger.debug("User ID in the credential: " + cred.getUserID());
			// logger.debug("password in the credential: " + cred.getPassword());
		} else {
			logger.debug("credential to be added is null!");			
		}
		logger.debug("fileName as key: " + fileName);
		userCredentials.put(fileName, cred);
	}
	
	private UserCredential getUserCredential(String fileName) {
		UserCredential cred = null;
		if(userCredentials != null) {
			cred = userCredentials.get(fileName);
			if(cred != null) {
				logger.debug("Got the user credential that is not null for file name: " + fileName);
			}
		}
		return cred;
	}
	
	private void setUserCredential(String fileName, UserCredential cred) {
		userCredentials.replace(fileName, cred);
	}
	
	private void removeUserCredential(String fileName) {
		if(userCredentials != null) {
			userCredentials.remove(fileName);
		}
	}
	
		  @RequestMapping(value = "/", method = RequestMethod.GET)	  
		  public String goToCrOptionsPage(@RequestParam(value = "fn", required = false) String fileName, 
				                          @RequestParam(value = "fn1", required = false) String key, 
				                          @RequestParam(value = "rm", required = false) String rmFileName, 
				                          @RequestParam(value = "rm1", required = false) String rmKey,
				                          ModelMap model) {
			  // model.addAttribute("essage", "Hello Spring MVC Framework!");
	    		if(!CRAutomationWeb.getWerePropertiesSet()) {
	    			CRAutomationWeb.setupProperties();
	    		} 
			  
			  ArrayList<String> multiTasksList = new ArrayList<String>();
			  ArrayList<String> bgMultiTasksList = new ArrayList<String>();
			  
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR_TASK);
			  // multiTasksList.add(SeleniumImplementation.STANDARD_CR_COPY_CR_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_EDIT_CR_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_PLAN_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_CIs_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_PAM_TEMPLATES_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_PROJECT_TASK);
//			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_APPROVER_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_CHANGE_SURVEY_TASK);
//			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_REMOVE_PLAN_TASK);
//			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_SUBMIT_FOR_APPROVAL_TASK);
			  
			  bgMultiTasksList.add(SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET_TASK);
			  bgMultiTasksList.add(SeleniumImplementation.BREAK_GLASS_CR_ADD_CIS_TASK);
			  bgMultiTasksList.add(SeleniumImplementation.BREAK_GLASS_CR_ADD_PAM_TEMPLATES_TASK);
			  bgMultiTasksList.add(SeleniumImplementation.BREAK_GLASS_CR_ADD_PROJECT_TASK);
			  //bgMultiTasksList.add(SeleniumImplementation.BREAK_GLASS_CR_SUBMIT_FOR_APPROVAL_TASK);
			  
			  model.addAttribute("crManager", new CRTicketManager());
			  if(fileName == null || fileName.trim().equals("")) {
				  logger.debug("No excel file name passed. Need to show file upload button...");
				  if(key == null || key.trim().equals("")) {
					  logger.debug("No excel file name and no key were passed. Start from scratch...");					  
				  } else {
					  logger.debug("Use existing information, but need to upload excel file...");
					  
					  if(this.getUserCredential(key) == null) {
						  logger.debug("User's input information has been removed from memory. Need to start from scratch...");
						  model.addAttribute("RemoveMsg", "Your information in memory has been removed. Please start from scratch...");
						  return "removeMemoryInfo";
					  }
					  model.addAttribute("ExistingInfo", key);					  
				  }
			  } else {
				  logger.debug("Excel file name was passed: " + fileName);
				  if(this.getUserCredential(fileName) == null) {
					  logger.debug("User's input information has been removed from memory. Need to start from scratch...");
					  model.addAttribute("RemoveMsg", "Your information in memory has been removed. Please start from scratch...");
					  return "removeMemoryInfo";
				  }				  
				  String uploadedFileName = Utilities.stripTimeStampFromFileName(fileName);
				  model.addAttribute("uploadedFileName", uploadedFileName);
				  model.addAttribute("ExistingExelFileName", fileName);
			  }
			  model.addAttribute("multiTasksList", multiTasksList);
			  model.addAttribute("bgMultiTasksList", bgMultiTasksList);
			  if(rmFileName == null || rmFileName.trim().equals("")) {
				  if(rmKey == null || rmKey.trim().equals("")) {
				  } else {
					  logger.debug("removing credentail from memory for " + rmKey);
					  this.removeUserCredential(rmKey);
				  }
			  } else {
				  logger.debug("removing credentail from memory for " + rmFileName);
				  this.removeUserCredential(rmFileName);
			  }
			  logger.info("go to crOPtions ...");
			  return "crOptions";
		  }

		  @RequestMapping(value = "/remove", method = RequestMethod.GET)	  
		  public String removeLoginFromMemory(@RequestParam(value = "fn", required = false) String fileName, @RequestParam(value = "fn1", required = false) String key, ModelMap model) {
			  // model.addAttribute("essage", "Hello Spring MVC Framework!");
			  if(fileName == null || fileName.trim().equals("")) {
				  logger.debug("No excel file name passed...");
				  if(key == null || key.trim().equals("")) {
					  logger.debug("No excel file name and no key were passed...");	
					  model.addAttribute("RemoveMsg", "Information passed was not correct. No authentication information was removed...");
				  } else {
					  logger.debug("Use existing information, but need to upload excel file...");
					  this.removeUserCredential(key);
					  model.addAttribute("RemoveMsg", "Your information has been removed from the memory...");					  
				  }
			  } else {
				  logger.debug("Excel file name was passed: " + fileName + " for removing the information in memory");
				  this.removeUserCredential(fileName);
				  model.addAttribute("RemoveMsg", "Your information has been removed from the memory...");					  
			  }
			  return "removeMemoryInfo";
		  }		  
		  
		    @RequestMapping(value = "/download", method = RequestMethod.GET)
		    public void doDownload(@RequestParam(value = "fn", required = false) String fileName,
						    		@RequestParam(value = "template", required = false) String templateFileName,
						    		HttpServletRequest request,
						            HttpServletResponse response, ModelMap model) {
		    	// String dataDirectory = "C:\\Temp\\excel\\";
		    	String dataDirectory = CRAutomationWeb.getExcelFileRootPath();
		    	if(fileName != null && !fileName.trim().equals("")) {
			    	logger.debug("fileName from the request: " + fileName);
			        Path file = Paths.get(dataDirectory, fileName);
			        if (Files.exists(file)) 
			        {
			            logger.debug("The file to be downloaded " + fileName + " exists in " + dataDirectory);
			        	response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			            response.addHeader("Content-Disposition", "attachment; filename="+fileName);
			            try
			            {
			                Files.copy(file, response.getOutputStream());
			                response.getOutputStream().flush();
			                logger.debug("The file " + fileName + " was downloaded successfully...");
			            }
			            
			            catch (IOException ex) {
			                ex.printStackTrace();
			            }
			        }
		    	} else if(templateFileName != null && !templateFileName.trim().equals("")) {
		    		CRAutomationWeb automation = new CRAutomationWeb();
		    		if(!CRAutomationWeb.getWerePropertiesSet()) {
		    			CRAutomationWeb.setupProperties();
		    			// System.out.println("NodeURL: " + automation.getNodeURL());
		    			// System.out.println("ExcelFileRootPath: " + automation.getExcelFileRootPath());
		    		} 
		    		
			    	String templateFilePath = CRAutomationWeb.getTemplateFilePath();
			    	logger.debug("Got template file path: " + templateFilePath);
			        Path file = Paths.get(templateFilePath);
			        if (Files.exists(file)) 
			        {
			            logger.debug("The template file to be downloaded " + templateFilePath + " exists");
			        	response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			            response.addHeader("Content-Disposition", "attachment; filename="+templateFileName);
			            try
			            {
			                Files.copy(file, response.getOutputStream());
			                response.getOutputStream().flush();
			                logger.debug("The file " + templateFileName + " was downloaded successfully...");
			            }
			            
			            catch (IOException ex) {
			                ex.printStackTrace();
			            }
			        }		    		
		    	}
		    }
	      
		  @RequestMapping(value = "/crOptions", method = RequestMethod.GET)	  
		  public String displayCROptions(ModelMap model) {
			  ArrayList<String> multiTasksList = new ArrayList<String>();
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR_TASK);
			  // multiTasksList.add(SeleniumImplementation.STANDARD_CR_COPY_CR_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_EDIT_CR_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_PLAN_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_CIs_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_PAM_TEMPLATES_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_PROJECT_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_APPROVER_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_ADD_DEPLOYMENT_TOOL_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_CHANGE_SURVEY_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_REMOVE_PLAN_TASK);
			  multiTasksList.add(SeleniumImplementation.STANDARD_CR_SUBMIT_FOR_APPROVAL_TASK);
			  model.addAttribute("crManager", new CRTicketManager());
			  model.addAttribute("multiTasksList", multiTasksList);
			  logger.info("go to crOPtions ...");
	          return "crOptions";	      
	      }	
		  
		  @RequestMapping(value = "/doTask", method = RequestMethod.POST)	  
		  public String completeCRTask(ModelMap model) throws IOException {
			  return "crmanagement";
		  }
		  
		  
		  
		  @RequestMapping(value = "/select", method = RequestMethod.POST)	  
		  public String selectCROptions(@RequestParam("file") MultipartFile multiFile,
				  @RequestParam("ExistingExcelName") String existingExcelName,
				  @RequestParam("ExistingInfo") String existingInfo,
				  @RequestParam("timezoneInput") String timezoneInput,
				  ModelMap model, CRTicketManager manager) throws IOException {
					logger.debug("Timezone number from browser: " + timezoneInput);
			        String originalFileName = multiFile.getOriginalFilename();
					String fileName = Utilities.getFileNameFromFilePath(originalFileName);
					logger.debug("uploaded file name: " + fileName + "   original File Name: " + originalFileName);
					if(!CRAutomationWeb.getWerePropertiesSet()) {
						logger.info("CR management properties were not set up yet. Set it up...");
						logger.info("Set up CR management properties"); 
						CRAutomationWeb.setupProperties();
					}
                    int serverTimeZone = CRAutomationWeb.getServertimeZone();
                    int timezoneDifference = Integer.parseInt(timezoneInput) - serverTimeZone;
                    logger.debug("timezoneDifference: " + timezoneDifference);
				    String excelRoot = CRAutomationWeb.getExcelFileRootPath();
				    String newExcelName = new Date().getTime()+ "-" + fileName;
					  int actionNumber = manager.getActionNumber();
					  String CR_Number = manager.getCr_number();
					  String IN_Number = manager.getIn_number();
					  String[] multiTasks = manager.getMultipleTasks();
					  String[] bgMultiTasks = manager.getBgMultipleTasks();
					  String userID = manager.getUserID();
					  String password = manager.getPassword();	
					  int ticketType = manager.getTicketType();
					  if(ticketType == 2) {
						  if(actionNumber == 1) {
							  manager.setActionNumber(SeleniumImplementation.BREAK_GLASS_CR_CREATE_TICKET);
						  } else if(actionNumber == 2) {
							  manager.setActionNumber(SeleniumImplementation.BREAK_GLASS_CR_ADD_CIS);
						  } else if(actionNumber == 3) {
							  manager.setActionNumber(SeleniumImplementation.BREAK_GLASS_CR_ADD_PAM_TEMPLATES);
						  } else if(actionNumber == 4) {
							  manager.setActionNumber(SeleniumImplementation.BREAK_GLASS_CR_ADD_PROJECT);
						  } else if(actionNumber == 5) {
							  manager.setActionNumber(SeleniumImplementation.BREAK_GLASS_CR_SUBMIT_FOR_APPROVAL);
						  } else if(actionNumber == 6) {
							  manager.setActionNumber(SeleniumImplementation.BREAK_GLASS_CR_MULTIPLE_TASKS);
						  }
					  }
					if(existingExcelName != null && !existingExcelName.trim().equals("")) {
						logger.debug("Existing Excel File is used. Existing Excel File name: " + existingExcelName);
						newExcelName = existingExcelName;
						UserCredential cred = this.getUserCredential(newExcelName);
						if( cred != null) {
							userID = cred.getUserID();
							password = cred.getPassword();
						} else {
							
							//Todo  add code to resolve the issue that user credential is not in the map.
							logger.error("The credential obtained from method getUserCredential() is null. Set credential with hard-coded user ID and password...");
							
						}
						manager.setUserID(userID);
						manager.setPassword(password);
					} else {
						logger.debug("No Existing Excel File is used ...");
						if(existingInfo != null && !existingInfo.trim().equals("")) {
							
							logger.debug("existingInfo exists. Get the credential from the map");
							UserCredential cred = this.getUserCredential(existingInfo);
							if( cred != null) {
								userID = cred.getUserID();
								password = cred.getPassword();
								logger.debug("got credentail from the map with the key " + existingInfo + " and the user ID in the credential is  " + userID);
								manager.setUserID(userID);
								manager.setPassword(password);
							}
						} else {
							logger.debug("There are no existing Excel File and no existing info. Use userid" + userID + " and password  that were from the page to create credential and add the credential to the map...");
							UserCredential cred = new UserCredential();
							cred.setUserID(userID);
							cred.setPassword(password);
							cred.setStartTime(System.currentTimeMillis());
							if(this.getUserCredential(newExcelName) == null) {
								this.addUserCredential(newExcelName, cred);
							} else {
								this.setUserCredential(newExcelName, cred);
							}
						}
					}
					
					String destination = excelRoot  + newExcelName;
				    // System.out.println("Destination: " + destination);
				    logger.debug("Excel File Path: " + destination);
									    
				    if(existingExcelName == null || existingExcelName.trim().equals("")) {
				    		if(originalFileName != null && !originalFileName.trim().equals("")) {
				    			File rootDir = new File(excelRoot);
				    			if(!rootDir.exists()) {
				    				logger.debug("Excel Root Directory does not exist. Creating Excel Root Directory: " + excelRoot);
				    				rootDir.mkdir();
				    			}
							    File fileToSave = new File(destination);
							    multiFile.transferTo(fileToSave);	    
							    logger.info("File was saved to " + destination);
								model.addAttribute("ExcelFileName", newExcelName);
								logger.debug("File Name added to model attribute:" + newExcelName);
								if(existingInfo != null && !existingInfo.trim().equals("")) {
									UserCredential cred = this.getUserCredential(existingInfo);
									this.removeUserCredential(existingInfo);
									this.addUserCredential(newExcelName, cred);
									logger.debug("No existing Excel file uploaded. But there is existingInfo. The user credential with the key " + existingInfo 
											+ " was removed from the user credential map, and was added back with the key of " +  newExcelName);						
								}
				    		} else {
								model.addAttribute("ExistingInfo", newExcelName);
								logger.debug("ExistingInfo was added to model attribute:" + newExcelName);
				    		}
					} else {
						model.addAttribute("ExcelFileName", existingExcelName);						
					}
					
				    manager.setExcelFilePath(destination);
			  
			  logger.info("Ticket type: " + manager.getTicketType());
			  logger.info("Action Number: " + manager.getActionNumber());
			  logger.info("CR Number: " + CR_Number);
			  logger.info("IN Number: " + IN_Number);
			  logger.info("XML file destination: " + manager.getExcelFilePath());
			  logger.info("User ID: " + manager.getUserID());
			  // logger.info("password: " + manager.getPassword());
			  String excelFilePath = manager.getExcelFilePath();
			  logger.info("XML file destination: " + manager.getExcelFilePath());
			  int[]  intMultiTasks = null;
			  int[]  intBgMultiTasks = null;
			  if(multiTasks != null && multiTasks.length > 0) {
				  intMultiTasks = new int[multiTasks.length];
				  for(int i = 0; i < multiTasks.length; i++) {
					  logger.debug("Task " + i + ": " + multiTasks[i]);
					  intMultiTasks[i]= Utilities.convertStandardCRTaskStrToTaskInt(multiTasks[i]);				  
				  }
				 // return "crmanagement";
			  } else {
				  logger.debug("no multiple Tasks were passed from the page...");
				  // return "crmanagement";
	     		  // completeTaskForException(instanceNumber, "Exception happened when reading CR infromation from the spreadsheet, please contact administrators ... ");
			  }  
			  
			  if(bgMultiTasks != null && bgMultiTasks.length > 0) {
				  intBgMultiTasks = new int[bgMultiTasks.length];
				  for(int i = 0; i < bgMultiTasks.length; i++) {
					  intBgMultiTasks[i] = Utilities.convertBreakGlassCRTaskStrToTaskInt(bgMultiTasks[i]);
				  }
			  }
			  //if(true) return  "crmanagement";
			  
			 // if(bgMultiTasks )
			  
			  CRAutomationWebInstance  webInstance = CRAutomationWebInstanceFactory.getCRAutomationWebInstance();
			  CRAutomationWeb automation = webInstance.getAutomationWeb();
			  int instanceNumber = webInstance.getInstanceNumber();
			  model.addAttribute("Instance_Number", instanceNumber);
			 	Map<String, String> items1 = null;		
		 		Task[] tasks1 = null;
		 		// String excelFilePath = automation.getExcelFileRootPath() + automation.getExcelFileName();
		 		//String excelFilePath = manager.getExcelFilePath();
		 	  if(ticketType == 1) {
			 		if(SeleniumImplementation.isReadingCRRequired(actionNumber, intMultiTasks)){
			 			try{
			 				logger.info("Read CR information from the Excel file: " + excelFilePath);
							Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Reading CR information from the Excel file ... ");
			 				items1 = Utilities.readCRInfoFromSpreadsheet(excelFilePath, timezoneDifference);
			 			} catch(Exception e){
			     		   e.printStackTrace();
			     		  logger.error("Exception happened when reading CR infromation from the spreadsheet: ", e);
			     		  completeTaskForException(instanceNumber, "Exception happened when reading CR infromation from the spreadsheet, please contact administrators ... ", e);
						  return "crmanagement";		
			 			}
			 		}
			 
			 		if(SeleniumImplementation.isReadingImplementationPlanRequired(actionNumber, intMultiTasks)){
			 			try{
							Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Reading implementation tasks from the Excel file ... ");
			 				tasks1 = Utilities.getTasksFromExcel(excelFilePath, timezoneDifference);
			 			} catch(Exception e){
			     		   e.printStackTrace();
			     		   logger.error("Exception happened when reading implementation plan tasks from the spreadsheet: ", e);
				     	   completeTaskForException(instanceNumber, "Exception happened when reading implementation plan tasks from the spreadsheet. Please contact administrators ... ", e);
				       	   return "crmanagement";				
			 			}
			 		}
			 		
			 	   if(!automation.precheckCRTask(excelFilePath, actionNumber, intMultiTasks, items1, tasks1, instanceNumber)) {
			 		  logger.error("Precheck failed...");
			 		  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "");
					  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Precheck of the dates in the spreadsheet failed. Please check the spreadsheet according to the error messages ... ");
					  Utilities.setCRAutomationWebInstanceCompleted(instanceNumber);
			 		  return "crmanagement";
			 	   } 
		 	  }

			  //System.out.println("Excel File name: " + manager.getExcelFile().getName());
			  CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				    // Simulate a long-running Job   
				 // CRAutomationWeb automation = new CRAutomationWeb();
				  automation.setCR_Number(CR_Number);
				  Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Setting up Remote Web Driver now... ");
				  if(automation.setupWebDriver()) {
						  Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Remote Web Driver was already set up ... ");
						  webInstance.setDriverSetup(true);
				  } else {

						  logger.error("Failed to get WebDriver set up. Try one more time...");
						  if(automation.setupWebDriver()) {
							  Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Remote Web Driver was already set up ... ");
							  webInstance.setDriverSetup(true);
						  } else {
							  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Not able to set up remote web driver. Please try one more time. If issue persists, please contact administrators ... ");
							  Utilities.setCRAutomationWebInstanceCompleted(instanceNumber);
							  return;
						  }
				    }
				  
			 		Map<String, String> items = null;		
			 		Task[] tasks = null;
					String[] multiTasks1 = manager.getMultipleTasks();
					int[] intMultiTasks1 = Utilities.convertStandCRTaskStrsToTaskInts(multiTasks1);
			 		// String excelFilePath = automation.getExcelFileRootPath() + automation.getExcelFileName();
			 		//String excelFilePath1 = manager.getExcelFilePath();
					String[] bgMultiTasks1 = manager.getBgMultipleTasks();
					  int[]  intBgMultiTasks1 = null;
					  if(bgMultiTasks1 != null && bgMultiTasks1.length > 0) {
						  intBgMultiTasks1 = new int[bgMultiTasks1.length];
						  for(int i = 0; i < bgMultiTasks1.length; i++) {
							  intBgMultiTasks1[i] = Utilities.convertBreakGlassCRTaskStrToTaskInt(bgMultiTasks1[i]);
						  }
					  }
					  
			 		if(SeleniumImplementation.isReadingCRRequired(actionNumber, intMultiTasks1)){
			 			try{
			 				logger.info("Read CR information from the Excel file: " + excelFilePath);
							Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Reading CR information from the Excel file ... ");
			 				items = Utilities.readCRInfoFromSpreadsheet(excelFilePath, timezoneDifference);
			 			} catch(Exception e){
			     		   e.printStackTrace();
			     		  logger.error("Exception happened when reading CR infromation from the spreadsheet: ", e);
			     		  completeTaskForException(instanceNumber, "Exception happened when reading CR infromation from the spreadsheet, please contact administrators ... ", e);
						  return;		
			 			}
			 		}
			 
			 		if(SeleniumImplementation.isReadingImplementationPlanRequired(actionNumber, intMultiTasks1)){
			 			try{
							Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Reading implementation tasks from the Excel file ... ");
			 				tasks = Utilities.getTasksFromExcel(excelFilePath, timezoneDifference);
			 			} catch(Exception e){
			     		   e.printStackTrace();
			     		   logger.error("Exception happened when reading implementation plan tasks from the spreadsheet", e);
				     	   completeTaskForException(instanceNumber, "Exception happened when reading implementation plan tasks from the spreadsheet. Please contact administrators ... ", e);
				       	   return;				
			 			}
			 		} 
			 		

			 	   logger.info("Logging into Service Desk Manager ...");
				   Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Logging into Service Desk Manager ... ");
				   String userID1 = manager.getUserID();
				   String password1 = manager.getPassword();
			 	   automation.loginToSDM(userID1, password1, instanceNumber);
				   try {
					   automation.completeCRTask(excelFilePath, manager.getActionNumber(), intMultiTasks1, items, tasks, intBgMultiTasks1, instanceNumber, manager.getIn_number(), manager.getBgCRDescription(), timezoneDifference);
					   Utilities.setCRAutomationWebInstanceCompleted(instanceNumber);
				   }catch(Exception e) { 
					   // logger.error("Exception happened when completing the CR Task...", e);
 					   WebDriver driver = automation.getDriver();
 					   String screenShotFile = "";
					   try {
				        screenShotFile = SeleniumImplementation.takeSnapShot(driver, excelRoot) ;  
				        logger.debug("The screen shot file name is: " + screenShotFile);
					   }catch(Exception e1) {
						   logger.debug("Caught Exception when taking Screen Shot: " + e1.getMessage());
						   e1.printStackTrace();
					   }
					   logger.error("Exception happened when completing the CR Task. Error Message:" + e.getMessage());
				       completeTaskForException(instanceNumber, "Exception happened when completing the CR Task. Please check the screenshot and values in the spreadsheets ... ", e);
				       if(screenShotFile != null && !screenShotFile.trim().equals("")) {
				    	   Utilities.appendCRAutomationWebInstanceMsg(instanceNumber,"<a href='/crmanagement/download?fn=" + screenShotFile + "' >Download the new generated screenshot for the errors above</a>");  
				       } 
 					   
 					   
				   }

				});	
 			  logger.info("go to page of crmanagement.jsp");
			  return "crmanagement";
			  
	   }	
		  
		  @RequestMapping(value = "/uploadForm", method = RequestMethod.GET)	  
		  public String fileUploadPage(ModelMap model) {
		     // FileModel file = new FileModel();
			 // model.addAttribute("fileUpload", file);
		      return "uploadExcel";	
		  }	
		  
		  @RequestMapping(value = "/upload", method = RequestMethod.POST)	  
		  public String handleUploadedFile(@RequestParam("file") MultipartFile multiFile, @RequestParam("userID") String userID, ModelMap model) throws IOException {
				if(multiFile != null) {
					String originalFileName = multiFile.getOriginalFilename();
					String fileName = Utilities.getFileNameFromFilePath(originalFileName);
					System.out.println("upload file name: " + fileName + "   original File Name: " + originalFileName);
				    logger.debug("User ID: " + userID);
					if(!CRAutomationWeb.getWerePropertiesSet()) {
						logger.info("CR management properties were not set up yet. Set it up...");
						logger.info("Set up CR management properties"); 
						CRAutomationWeb.setupProperties();
					}
				    String excelRoot = CRAutomationWeb.getExcelFileRootPath();
					String destination = excelRoot  + new Date().getTime()+ "-" + fileName;
				    System.out.println("Destination: " + destination);
				    // logger.info("Destination: " + destination);
				    File fileToSave = new File(destination);
				    multiFile.transferTo(fileToSave);
				    logger.info("File was saved to " + destination);
				    model.addAttribute("file_destination", destination);
				    model.addAttribute("uploadedFileName", multiFile.getOriginalFilename());
				} else {
					logger.error("multipart File is empty!!!"); 
					model.addAttribute("ErrorMsg", "multipart File is empty!!!");
					return "errorMsgs";
				}
				
			    return "reviewBeforeDoingTask";
		  }
		  
		  private void completeTaskForException(int instanceNumber, String msg, Exception e) {
			  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, msg);
			  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Messages from the exception: " + e.getMessage());
			  Utilities.setCRAutomationWebInstanceCompleted(instanceNumber);
		  }
}
