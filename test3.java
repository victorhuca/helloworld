package com.hsbc.selenium.crmanagement.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hsbc.selenium.crmanagement.controller.crmanagementController;
import com.hsbc.selenium.crmanagement.model.CRAutomationWebInstance;
import com.hsbc.selenium.crmanagement.model.CRTicketManager;
import com.hsbc.selenium.crmanagement.model.PAMTemplate;
import com.hsbc.selenium.crmanagement.model.Task;

public class Utilities {
	private static boolean debugEnabled;
	private static int surveyQuestionNumber = 19;
	public static final int SHEET_NUMBER_IMPLEMENTATION_PLAN =0;
	public static final int SHEET_NUMBER_CR_TICKET=1;
	public static final int SHEET_NUMBER_BREAK_GLASS_TICKET=2;
	public static final int SHEET_NUMBER_CIS=3;
	public static final int SHEET_NUMBER_PAM_TEMPLATES=4;
	public static final int SHEET_NUMBER_SURVEY=5;
	public static final int SHEET_NUMBER_APPROVERS=6;
	public static final int SHEET_NUMBER_OWNING_GROUPS=7;
	public static final int SHEET_NUMBER_IMPLEMENTATION_GROUPS=8;
	public static final int SHEET_NUMBER_EMPLOYEE_NAMES=9;
	public static final int SHEET_NUMBER_SUPPORTING_REGIONS=10;
	public static final int SHEET_NUMBER_ASSIGNED_GROUPS=11;
	private static Logger logger = Logger.getLogger(Utilities.class);
	
	
	
	public static void setDebugEnabled(boolean enabled){
		debugEnabled = enabled;
	}
	
	public static Task[] getTasksFromItemsList(ArrayList<Map<String, String>> itemsList){
		logger.debug("Entering Utilities.getTasksFromItemsList...");
		Task[] tasks = null;
		// System.out.println("Entering getTasksFromItemsList");
		if(itemsList != null && itemsList.size()!=0){
			// System.out.println("itemsList size: " + itemsList.size());
			tasks = new Task[itemsList.size()];
			for(int i = 0; i < itemsList.size(); i++){
			   Map<String, String> items = itemsList.get(i);
			   tasks[i] = new Task();
			   tasks[i].setTaskNumber(items.get("TASK_NUMBER"));
			   tasks[i].setTaskTitle(items.get("TASK_TITLE"));
			   tasks[i].setStartDate(items.get("START_TIME"));
			   tasks[i].setEndDate(items.get("END_TIME"));
			   // tasks[i].setTaskDescription(items.get("TASK_DESCRIPTION") + "\n\n" + items.get("TASK_CIS"));
			   tasks[i].setTaskDescription(items.get("TASK_DESCRIPTION"));
			   tasks[i].setGroupAssigned(items.get("ASSIGNED_GROUP"));
			}
		}
		logger.debug("Exiting Utilities.getTasksFromItemsList...");
		return tasks;
	}
	
	public static String[] getAssignedGroupsFromTasks(Task[] tasks){
		logger.debug("Entering Utilities.getAssignedGroupsFromTasks...");
		String[] assignedGroups = null;
		if (tasks != null && tasks.length > 0){
			assignedGroups = new String[tasks.length];
			for(int i = 0; i < tasks.length; i++){
				assignedGroups[i]=tasks[i].getGroupAssigned();
				logger.debug("assigned group " + i + ": " + assignedGroups[i]);
			}
		}
		return assignedGroups; 
	}
	
   public static boolean appendCRAutomationWebInstanceMsg(int instanceNumber, String msg) {
	   boolean result = true;
	   CRAutomationWebInstance instance = CRAutomationWebInstanceFactory.getCRAutomationWebInstance(instanceNumber);
	   if(instance == null) {
		   logger.error("Got null of CRAutomationWebInstance from the instance number: " + instanceNumber);
		   result = false;
	   } else {
		   String existingMsg = instance.getMsg();
		   if(existingMsg == null) {
			   existingMsg = "";
		   } 
		   String newMsg = existingMsg + "<span>" + msg + "</span> <br />";
		   instance.setMsg(newMsg);
	   }
	       
	   return result;
   }
   
   public static boolean isExcelRequired(CRTicketManager manager) {
	   return true;
   }
   
   public static boolean isINExpiredForCreatingBGCR(String actualStartDate) throws ParseException {
	boolean result = true;
	Date now = new Date();
	String pattern = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	Date actualDate = simpleDateFormat.parse(actualStartDate);
	long difference = now.getTime() -  actualDate.getTime();
	double dayDifference = difference/(1000.0 * 60 * 60 * 24);
	logger.debug("Day difference: " + dayDifference);
	// if(dayDifference < 4 && dayDifference > 0) result = false;
	if(dayDifference < 4) result = false;
	return result;
   }
   
   public static boolean appendCRAutomationWebInstanceErrMsg(int instanceNumber, String msg) {
	   boolean result = true;
	   CRAutomationWebInstance instance = CRAutomationWebInstanceFactory.getCRAutomationWebInstance(instanceNumber);
	   if(instance == null) {
		   logger.error("Got null of CRAutomationWebInstance from the instance number: " + instanceNumber);
		   result = false;
	   } else {
		   String existingMsg = instance.getMsg();
		   if(existingMsg == null) {
			   existingMsg = "";
		   } 
		   String newMsg = existingMsg + "<span style='color:red'>" + msg + "</span> <br />";
		   instance.setMsg(newMsg);
	   }
	       
	   return result;
   } 
   
   public static boolean removedCRAutomationWebInstanceMsg(int instanceNumber) {
	   boolean result = true;
	   CRAutomationWebInstance instance = CRAutomationWebInstanceFactory.getCRAutomationWebInstance(instanceNumber);
	   if(instance == null) {
		   logger.error("Got null of CRAutomationWebInstance from the instance number: " + instanceNumber);
		   result = false;
	   } else {
		   instance.setMsg("");
	   }
	   
	   return result;
   } 
   
   public static void setCRAutomationWebInstanceCompleted(int instanceNumber) {
	   CRAutomationWebInstance instance = CRAutomationWebInstanceFactory.getCRAutomationWebInstance(instanceNumber);
	   if(instance == null) {
		   logger.error("Got null of CRAutomationWebInstance from the instance number: " + instanceNumber);
	   } else {
		   instance.setTaskCompleted(true);
	   }	   
   }
   
   public static int getITServiceAcceptanceOptionValue(String acceptanceStr){
	   int result = 1;
	   String[] itServiceAcceptanceOptions = {"ttttt", "It is not a change to a Production", "It does not affect an IT Technology Service", 
			                               "The change is for a scheduled Disaster Recovery", "This is an Emergency change",
			                               "This is a Functional change or Planned Patching", "Yes"};
	   for(int i = 0; i < 7; i++){
		   if(acceptanceStr.contains(itServiceAcceptanceOptions[i])){
			   result = i;
			   break;
		   }
	   }
	   return result;
   }
	
	public static int[] getSurveySelectionsFromStrings(String[] surveryStrs){
		String[][] surveySubStrings = {
				                       {"across multiple legal entities in different regions","across multiple legal entities the same region","within one legal entity","no impact to users or consumers"}, 
				                       {"Yes", "No"}, 
				                       {"Yes", "No"},
				                       {"Yes, I have discussed with the IT Service Owner", "No, I have reviewed the maintenance windows"},
				                       {"severely impair revenue", "impair some revenue", "impair some business functionality", "no impact to revenue"},
				                       {"an Emergency change","a Normal Change"},
				                       {"3 or more software components","2 software components","No multi-platform coordination","one software component","not involve any software components"},
				                       {"3 or more hardware","2 hardware components","No multi-platform coordination","1 hardware component","not involve any hardware"},
				                       {"Lengthy back out","Involved back out","Moderate back out","Rapid back out"},
				                       {"Entire population","More than half of population","Less than half of population","Pilot or no segment of population"},
				                       {"Production System environment, application or infrastructures","Non Production System environment, application or infrastructure", "Production and Non Production System environments, applications or infrastructures"},
				                       {"not been tested/executed in a production or non-production environment","executed previously in a production environment","tested in non-production but not executed in a production environment"},
				                       {"Successful at last attempt","Failed at last attempt","Not attempted previously"},
				                       {"taken steps to mitigate risks with manual implementation","unable to mitigate risks with manual implementation","Not applicable"},
				                       {"Yes, all artefacts from upstream controls have been reviewed","No, there are outstanding risks from upstream controls"},
				                       {"Yes, I have reviewed that all pre-requisites and dependencies","No, there are pre-requisites and/or dependencies"},
				                       {"taken measures to mitigate risks","unable to mitigate risks", "Not applicable"},
				                       {"Yes, I have reviewed to ensure that adequately skilled resource(s) are arranged", "No, but I have taken mitigation measures in place", "No, I am unable to secure adequately skilled resource"},
				                       {"Yes, I have planned Post Implementation Verifications","No, I am unable to plan for an effective Post Implementation Verifications"}
				                       
		};

		int[] surveySelections = new int[surveyQuestionNumber];
		for(int i = 0; i < surveyQuestionNumber; i++) {
			// System.out.println("Survey String: " + i + ": " + surveryStrs[i]);
			for(int j = 0; j < surveySubStrings[i].length; j++){
				 
				if(surveryStrs[i].contains(surveySubStrings[i][j])){
					System.out.println(surveySubStrings[i][j] + "; ");
					surveySelections[i] = j;			
				//	System.out.println("question " + i + ": " + j);
					break;
				}
			}
			// System.out.print("\n");
		}
		return surveySelections;
	}
	
	public static double convertDurationStrToHours(String durationStr){
		double duration = 0;
		if(durationStr != null && !durationStr.trim().equals("")){
			String[] parts = durationStr.split ( ":" );
			if ( parts.length == 3 ) {
			    double hours = Double.parseDouble  ( parts[ 0 ] );
			    double minutes = Double.parseDouble ( parts[ 1 ] );
			    double seconds = Double.parseDouble ( parts[ 2 ] );
			    duration = hours + minutes/60 + seconds/3600;
			} else if ( parts.length == 2 ) {
			    double hours = Double.parseDouble  ( parts[ 0 ] );
			    double minutes = Double.parseDouble ( parts[ 1 ] );
			    duration = hours + minutes/60;
			} else {
				logger.error("ERROR - Unexpected duration input. The duration String is: " + durationStr );
			}
		}
		
		duration = Math.round(duration * 100)/100.00;
		return duration;
	}
	
   public static long getSecondsOfDifferenceBetweenTwoDates(Date date1, Date date2){
	    TimeUnit timeUnit = TimeUnit.SECONDS;

	    long diffInMilli = date2.getTime() - date1.getTime();
	    long s = timeUnit.convert(diffInMilli, TimeUnit.MILLISECONDS);	   
	    return s;
   }
	
    public static int convertStringIntegerToInt(String intStr) {
    	int result = -1;
    	if(intStr != null && !intStr.trim().equals("")) {
    		Integer integer = Integer.parseInt(intStr.trim());
    		if(integer != null) {
    			result = integer.intValue();
    		}
    	}
    	return result;
    }
    
    public static String stripTimeStampFromFileName(String fileName) {
    	String result = "";
    	if(fileName == null || fileName.trim().equals("")) {
    		result = "";
    	} else {
    		int index = fileName.indexOf("-");
    		result = fileName.substring(index+1);
    	}
    	return result;
    }
    
	public static Date convertDateTimeStringToDate(String dateTimeStr) throws ParseException{
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = simpleDateFormat.parse(dateTimeStr);
		return date;
	}
	
	public static int[] getSurveySelectionsFromSpreadsheet(String fileName) throws IOException{
		String[] strs = getSurveyStringsFromSpreadsheet(fileName);
		int[] selections = getSurveySelectionsFromStrings(strs);
		return selections;
	}
	public static String[] getSurveyStringsFromSpreadsheet(String fileName) throws IOException{
		logger.debug("Entering Utilities.getSurveySelectionsFromSpreadsheet...");
	    Sheet sheet = Utilities.readSheetFromExcel(fileName, Utilities.SHEET_NUMBER_SURVEY);
		//every sheet has rows, iterate over them
		Iterator<Row> rowIterator = sheet.iterator();
		String[] surveyStrs = new String[surveyQuestionNumber];
		int line = 0;	
		while (rowIterator.hasNext()) 
        {
			String name = "";
			String shortCode = "";
			line++;
			//Get the row object
			Row row = rowIterator.next();
			
			//Every row has columns, get the column iterator and iterate over them
			Iterator<Cell> cellIterator = row.cellIterator();
            // System.out.println("Row " + line + ":"); 
            int cellNo = 0;
            boolean isTaskNumberEmpty = false;
            boolean isTaskTitleEmpty=false;
            while (cellIterator.hasNext()) 
            {
            	//Get the Cell object
            	Object cellValue = null;
            	Cell cell = cellIterator.next();
            	CellType cellType = cell.getCellType();
            	if (cellType == CellType.STRING) {
            		cellValue = cell.getStringCellValue();
            	} else if (cellType == CellType.NUMERIC) {
            		if (DateUtil.isCellDateFormatted(cell)) {
            			String pattern;
            			//if they are the lines for duration, only time is obtained
            			if(line==2 || line==4 || line==5){
            				pattern = "HH:mm:ss";
            			} else {
            			    pattern = "yyyy-MM-dd HH:mm:ss";
            			}
            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
            		} else {
            			cellValue = cell.getNumericCellValue();
            		}
            	} else if (cellType == CellType.BOOLEAN) {
            		cellValue = cell.getBooleanCellValue();
            	} else if (cellType == CellType.FORMULA) {
            		    CellType cellType1 = cell.getCachedFormulaResultType();
            		    if (cellType1 == CellType.STRING) {
		            		cellValue = cell.getStringCellValue();
		            	} else if (cellType1 == CellType.NUMERIC) {
		            		if (DateUtil.isCellDateFormatted(cell)) {
		            			String pattern = "yyyy-MM-dd HH:mm:ss";
		            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
		            		} else {
		            			cellValue = cell.getNumericCellValue();
		            		}
		            	} else if (cellType1 == CellType.BOOLEAN) {
		            		cellValue = cell.getBooleanCellValue();
		            	} 
            		    
            	} else if (cellType == CellType.BLANK) {
            		cellValue = "";
            	}
            	
                //System.out.println(line + ":");
            	// System.out.println(cellNo + ": " + cellValue);
            	if(cellNo == 1){        		
            		if(line == 1 ) {
            			surveyStrs[0] = cellValue + "";
            		}else if(line == 3 ) {
            			surveyStrs[1] = cellValue + "";
            		}else if(line == 5 ) {
            			surveyStrs[2] = cellValue + "";
            		} else if(line == 7 ) {
            			surveyStrs[3] = cellValue + "";
            		} else if(line == 9 ) {
            			surveyStrs[4] = cellValue + "";
            		} else if (line == 11) {
            			surveyStrs[5] = cellValue + "";
            		} else if (line == 13) {
            			surveyStrs[6] = cellValue + "";
            		} else if (line == 15) {
            			surveyStrs[7] = cellValue + "";
            		} else if (line == 17) {
            			surveyStrs[8] = cellValue + "";
            		} else if (line == 19) {
            			surveyStrs[9] = cellValue + "";
            		} else if (line == 21) {
            			surveyStrs[10] = cellValue + "";
            		} else if (line == 23) {
            			surveyStrs[11] = cellValue + "";
            		} else if (line == 25) {
            			surveyStrs[12] = cellValue + "";
            		} else if (line == 27) {
            			surveyStrs[13] = cellValue + "";
            		} else if (line == 29) {
            			surveyStrs[14] = cellValue + "";
            		} else if (line == 31) {
            			surveyStrs[15] = cellValue + "";
            		} else if (line == 33) {
            			surveyStrs[16] = cellValue + "";
            		} else if (line == 35) {
            			surveyStrs[17] = cellValue + "";
            		} else if (line == 37) {
            			surveyStrs[18] = cellValue + "";
            		} 
            	}           	
            	cellNo++;
            } //end of cell iterator
            
        } //end of rows iterator
			 for(int i = 0; i < surveyQuestionNumber; i++){
				 logger.debug("Answers of Survey " + i + ": " + surveyStrs[i]);
			 }
			 logger.debug("Exiting Utilities.getSurveySelectionsFromSpreadsheet...");	
		return surveyStrs;
	}
		
	public static Task[] getTasksFromExcel(String filePath, int serverTimeDifference) throws FileNotFoundException, IOException, ParseException{
		logger.debug("Entering Utilities.getTasksFromExcel...");
		Task[] tasks = null;
		

			ArrayList<Map<String, String>> itemsList = new ArrayList<Map<String, String>>();
			
			    Sheet sheet = Utilities.readSheetFromExcel(filePath, Utilities.SHEET_NUMBER_IMPLEMENTATION_PLAN);
				//every sheet has rows, iterate over them
				Iterator<Row> rowIterator = sheet.iterator();
				int line = 0;
				while (rowIterator.hasNext()) 
		        {
					String name = "";
					String shortCode = "";
					line++;
					//Get the row object
					Row row = rowIterator.next();
					
					//Every row has columns, get the column iterator and iterate over them
					Iterator<Cell> cellIterator = row.cellIterator();
		            // System.out.println("Row " + line + ":"); 
		            int cellNo = 0;
		            HashMap<String, String> items = new HashMap<String, String>();
		            boolean isTaskNumberEmpty = false;
		            boolean isTaskTitleEmpty=false;
		            while (cellIterator.hasNext()) 
		            {
		            	//Get the Cell object
		            	Object cellValue = null;
		            	Cell cell = cellIterator.next();
		            	CellType cellType = cell.getCellType();
		            	if (cellType == CellType.STRING) {
		            		cellValue = cell.getStringCellValue();
		            	} else if (cellType == CellType.NUMERIC) {
		            		if (DateUtil.isCellDateFormatted(cell)) {
		            			String pattern = "yyyy-MM-dd HH:mm:ss";
		            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
		            		} else {
		            			cellValue = cell.getNumericCellValue();
		            		}
		            	} else if (cellType == CellType.BOOLEAN) {
		            		cellValue = cell.getBooleanCellValue();
		            	} else if (cellType == CellType.FORMULA) {
		            		    CellType cellType1 = cell.getCachedFormulaResultType();
		            		    if (cellType1 == CellType.STRING) {
				            		cellValue = cell.getStringCellValue();
				            	} else if (cellType1 == CellType.NUMERIC) {
				            		if (DateUtil.isCellDateFormatted(cell)) {
				            			String pattern = "yyyy-MM-dd HH:mm:ss";
				            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
				            		} else {
				            			cellValue = cell.getNumericCellValue();
				            		}
				            	} else if (cellType1 == CellType.BOOLEAN) {
				            		cellValue = cell.getBooleanCellValue();
				            	} 
		            		    
		            	} else if (cellType == CellType.BLANK) {
		            		cellValue = "";
		            	}
		            	
		            	if(cellValue == null || cellValue.equals("null"))  cellValue = "";
		            	
		            	// System.out.println("Line " + line + ":");
		            	// System.out.println("CellNo: " + cellNo + ": " + cellValue);
		            	if(cellNo == 0){
		            		if(cellValue == null || cellValue.toString().trim().equals("") || cellValue.toString().trim().equals("null")){
		            			isTaskNumberEmpty = true;
		            		} else {
		            		  // items.put("TASK_NUMBER", "" + ((Double)cellValue).intValue());
		            			items.put("TASK_NUMBER", line  + "");
		            		}
		            	}else if(cellNo == 1){
		            		String start_Date_Time = cellValue + "";
		            		String serversideStartTime = Utilities.getServerSideTimeFromLocalTime(start_Date_Time, serverTimeDifference);
		            		items.put("START_TIME", serversideStartTime);
		            	}else if(cellNo == 2){
		            		String end_Date_Time = cellValue + "";
		            		String serversideEndTime = Utilities.getServerSideTimeFromLocalTime(end_Date_Time, serverTimeDifference);
		            		items.put("END_TIME", serversideEndTime);
		            	}else if(cellNo == 3){
		            		if(cellValue == null || cellValue.toString().trim().equals("") || cellValue.toString().trim().equals("null")){
		            			isTaskTitleEmpty = true;
		            		} else {
		            		   items.put("TASK_TITLE", "" + cellValue);
		            		}
		            	} else if(cellNo == 4){
		            		items.put("TASK_DESCRIPTION", "" + cellValue);
		            	} else if(cellNo == 5){
		            		items.put("ASSIGNED_GROUP", "" + cellValue);
		            	}
		            	cellNo++;
		            	
		            } //end of cell iterator
		            
		            String taskNumber = items.get("TASK_NUMBER");
		            String taskTitle = items.get("START_TIME");
		            if(taskNumber != null && !taskNumber.trim().equals("") && !taskNumber.trim().equals("null")
		            		&& taskNumber != null && !taskNumber.trim().equals("") && !taskNumber.trim().equals("null")	){
			            itemsList.add(items);
			            	logger.debug("row " + line + ":");
			            	logger.debug("Task Number: " + items.get("TASK_NUMBER"));
			            	logger.debug("Task START TIME: " + items.get("START_TIME"));
			            	logger.debug("Task END TIME: " + items.get("END_TIME"));
			            	logger.debug("Task TITLE: " + items.get("TASK_TITLE"));
			            	logger.debug("Task Description: " + items.get("TASK_DESCRIPTION"));
			            	// System.out.println("Task CIs: " + items.get("TASK_CIS"));
			            	logger.debug("Task ASSIGNED GROUP: " + items.get("ASSIGNED_GROUP"));
		            } else
		            {
		            	logger.debug("Row " + line  + ": Task Number and Task title in the line are all empty. It's regarded to be the end of tasks");
		            	break;
		            } 
		        } //end of rows iterator
				
				
			//} //end of sheets for loop
			tasks = getTasksFromItemsList(itemsList);
			//close file input stream
			// fis.close();
			logger.debug("Exiting Utilities.getTasksFromExcel...");
		return tasks;
	}
	
	public static ArrayList<String> getExistingGroupsFromFile(String fileForExistingGroups) throws FileNotFoundException, IOException{
				logger.debug("Entering Utilities.getExistingGroupsFromFile...");
				RandomAccessFile file = new RandomAccessFile(fileForExistingGroups, "r");
				String str;
				ArrayList<String> list = new ArrayList<String>();       
				while ((str = file.readLine()) != null) {
					list.add(str); 
				}
				file.close();
				logger.debug("Exiting Utilities.getExistingGroupsFromFile...");
				return list;
	}
	
	public static String getFileNameFromFilePath(String filePath) {
		if(filePath != null) {
			if(filePath.contains("\\")) {
				return filePath.substring(filePath.lastIndexOf("\\") + 1);
			}  else if(filePath.contains("/")) {
				return filePath.substring(filePath.lastIndexOf("/") + 1);
			} else {
				return filePath;
			}
		} else {
			
			return " ";
		}
	}
	
	public static boolean isStringInList(String str, ArrayList<String> list){
		logger.debug("Entering Utilities.isStringInList...");
		boolean result = false;
		if(list !=null && !list.isEmpty()){
		    for(int i=0; i < list.size(); i++){
		    	String strInList=(String)list.get(i);
                if(str.trim().equals(strInList.trim())){
                	result = true;
                	break;
                }
		    }
		}
		logger.debug("Exiting Utilities.isStringInList...");
		return result;
	}	
	
    public static Task getTaskFromString(String taskStr){
		logger.debug("Entering Utilities.getTaskFromString...");
		String[] taskAttrs = (taskStr + ",end").split(",");
		Task task = null;
		if(taskAttrs.length != Task.TASK_ATTRIBUTE_NUMBER + 1) {
			logger.error("Task String does have not correct number of attributes! its size is " + (taskAttrs.length - 1));
		} else {
			// task.setTaskNumber(taskAttrs[0]);
			task = new Task();
			task.setTaskTitle(taskAttrs[0]);
			task.setStartDate(taskAttrs[1]);
			task.setEndDate(taskAttrs[2]);
			task.setGroupAssigned(taskAttrs[3]);
			task.setTaskDescription(taskAttrs[4]);
			task.setAttachment(taskAttrs[5]);
			for(int i = 0; i < taskAttrs.length; i++){
				// System.out.println("Attr " + i + ": " + taskAttrs[i]);
			}
		}
		logger.debug("Exiting Utilities.getTaskFromString...");
		return task;
    }
    
	  public static Task[] getTasksFromFile(String filePath){
		  Task[] tasks = null;
		  try {
				RandomAccessFile file = new RandomAccessFile(filePath, "r");
				String str;
				ArrayList<String> list = new ArrayList<String>();       
				while ((str = file.readLine()) != null) {
					// System.out.println(str);
					list.add(str); 
				}
				file.close();
				if(!list.isEmpty()){
					tasks = new Task[list.size()];
				    for(int i=0; i < list.size(); i++){
				    	str=(String)list.get(i);
				    	tasks[i] = Utilities.getTaskFromString(str);
				    	if(tasks[i] == null){
				    		logger.error("Something is not right to convert from String to Task. String:" + str);
				    	}
				    }
				}
			    
			} catch (IOException e) {
				e.printStackTrace();
			}
		  return tasks;
	  } 
	  
	  public static Sheet readSheetFromExcel(String filePath, int sheetNumber) throws FileNotFoundException, IOException {
			FileInputStream fis = new FileInputStream(filePath);
 			//Create Workbook instance for xlsx/xls file input stream
			Workbook workbook = null;
			if(filePath.toLowerCase().endsWith("xlsx")){
				workbook = new XSSFWorkbook(fis);
			}else if(filePath.toLowerCase().endsWith("xls")){
				workbook = new HSSFWorkbook(fis);
			}		  
			int numberOfSheets = workbook.getNumberOfSheets();
			logger.debug("Total sheet number of file " + filePath + ": " + numberOfSheets);
			Sheet sheet = null;
			if(sheetNumber >= numberOfSheets){
				logger.error("the sheet number exceeds the maximum sheet number. Please check if required sheet exists in the file");
			} else {
				sheet = workbook.getSheetAt(sheetNumber);
			}
			fis.close();
			return sheet;
	  }
	  
		public static ArrayList<String>  getApproversFromExcel(String fileName) throws FileNotFoundException, IOException{
			  ArrayList<String> list = readListFromSpreadsheet(fileName, Utilities.SHEET_NUMBER_APPROVERS); 
			  return list;
		}

	  public static ArrayList<String> readCIsFromSpreadsheet(String filePath) throws FileNotFoundException, IOException{
		  // ArrayList<String> list = readListFromSpreadsheet(filePath, 2);  
		  ArrayList<String> list = readListFromSpreadsheet(filePath, Utilities.SHEET_NUMBER_CIS); 
		  return list;
	  }	  
	  
	  public static ArrayList<String> readOwningGroupsFromSpreadsheet(String filePath) throws FileNotFoundException, IOException{
		 // ArrayList<String> list = readListFromSpreadsheet(filePath, 4); 
		  ArrayList<String> list = readListFromSpreadsheet(filePath, Utilities.SHEET_NUMBER_OWNING_GROUPS);
		  return list;
	  }	  

	  public static ArrayList<String> readValidGroupsFromSpreadsheet(String filePath) throws FileNotFoundException, IOException{
		  logger.debug("Entering Utilities.readValidGroupsFromSpreadsheet...");
		  // ArrayList<String> list = readListFromSpreadsheet(filePath, 5);
		  ArrayList<String> list = readListFromSpreadsheet(filePath, Utilities.SHEET_NUMBER_IMPLEMENTATION_GROUPS); 
		  logger.debug("Exiting Utilities.readValidGroupsFromSpreadsheet...");
		  return list;
	  }

	  public static ArrayList<String> readValidNamesFromSpreadsheet(String filePath) throws FileNotFoundException, IOException{
		  logger.debug("Entering Utilities.readValidNamesFromSpreadsheet...");
		  // ArrayList<String> list = readListFromSpreadsheet(filePath, 6); 
		  ArrayList<String> list = readListFromSpreadsheet(filePath, Utilities.SHEET_NUMBER_EMPLOYEE_NAMES); 
		  logger.debug("Exiting Utilities.readValidNamesFromSpreadsheet...");
		  return list;
	  }
	  
	  public static ArrayList<String> readSupportingRegionFromSpreadsheet(String filePath) throws FileNotFoundException, IOException{
		  logger.debug("Entering Utilities.readSupportingRegionFromSpreadsheet...");
		  // ArrayList<String> list = readListFromSpreadsheet(filePath, 7);
		  ArrayList<String> list = readListFromSpreadsheet(filePath, Utilities.SHEET_NUMBER_SUPPORTING_REGIONS);
		  logger.debug("Exiting Utilities.readSupportingRegionFromSpreadsheet...");
		  return list;
	  }
	  
	  public static ArrayList<String> readListFromSpreadsheet(String filePath, int sheetNumber) throws FileNotFoundException, IOException{
		logger.debug("Entering Utilities.readListFromSpreadsheet...");
		  ArrayList<String> list = new ArrayList<String>();
		  Sheet sheet = readSheetFromExcel(filePath, sheetNumber);
			Iterator<Row> rowIterator = sheet.iterator();
			int line = 0;	
			while (rowIterator.hasNext()) 
	        {
				String name = "";
				String shortCode = "";
				//Get the row object
				Row row = rowIterator.next();			
				//Every row has columns, get the column iterator and iterate over them
				Iterator<Cell> cellIterator = row.cellIterator();
	            // System.out.println("Row " + line + ":"); 
	            int cellNo = 0;
	            boolean isTaskNumberEmpty = false;
	            boolean isTaskTitleEmpty=false;
	            while (cellIterator.hasNext()) 
	            {
	            	//Get the Cell object
	            	Object cellValue = "";
	            	Cell cell = cellIterator.next();
	            	CellType cellType = cell.getCellType();
	            	if (cellType == CellType.STRING) {
	            		cellValue = cell.getStringCellValue();
	            	} else if (cellType == CellType.NUMERIC) {
	            			cellValue = cell.getNumericCellValue();
	            	} else if (cellType == CellType.BOOLEAN) {
	            		cellValue = cell.getBooleanCellValue();
	            	} else if (cellType == CellType.BLANK) {
	            		cellValue = "";
	            	}
	            	// System.out.println(cellNo + ": " + cellValue);
	            	if(cellNo == 0){
	            		// System.out.println(line + ": " + cellValue);
	            		list.add(line, cellValue+"");
	            		break;
	            	}
	            	cellNo++;
	            	
	            } //end of cell iterator
				line++;
	        } //end of rows iterator
		  logger.debug("Exiting Utilities.readListFromSpreadsheet...");
		  return list;
	  }
		  
	 public static Object readCellValue(Cell cell){
		 Object cellValue = readCellValue(cell, false, 0);
		 return (cellValue + "").trim();
	 }
     public static Object readCellValue(Cell cell, boolean isItForCRInfo, int line){
     	Object cellValue = null;
     	CellType cellType = cell.getCellType();
     	if (cellType == CellType.STRING) {
     		cellValue = cell.getStringCellValue();
     	} else if (cellType == CellType.NUMERIC) {
     		if (DateUtil.isCellDateFormatted(cell)) {
     			String pattern = "yyyy-MM-dd HH:mm:ss";
     			//if they are the lines for duration, only time is obtained
     			if(isItForCRInfo) {
	     			if(line==2 || line==4 || line==5){
	     				pattern = "HH:mm:ss";
	     			} 
     			}
     			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
     			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
     		} else {
     			cellValue = cell.getNumericCellValue();
     		}
     	} else if (cellType == CellType.BOOLEAN) {
     		cellValue = cell.getBooleanCellValue();
     	} else if (cellType == CellType.FORMULA) {
     		    CellType cellType1 = cell.getCachedFormulaResultType();
     		    if (cellType1 == CellType.STRING) {
	            		cellValue = cell.getStringCellValue();
	            	} else if (cellType1 == CellType.NUMERIC) {
	            		if (DateUtil.isCellDateFormatted(cell)) {
	            			String pattern = "yyyy-MM-dd HH:mm:ss";
	            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
	            		} else {
	            			cellValue = cell.getNumericCellValue();
	            		}
	            	} else if (cellType1 == CellType.BOOLEAN) {
	            		cellValue = cell.getBooleanCellValue();
	            	} 
     		    
     	} else if (cellType == CellType.BLANK) {
     		cellValue = "";
     	}
    	return cellValue;
     }
	  
	 public static  Map<String, String> readCRInfoFromSpreadsheet(String filePath, int serverDifferentHours) throws FileNotFoundException, IOException, ParseException{
		 Map<String, String> crMap = readCRInfoFromSpreadsheet(filePath, null);
		 String scheduledStartDate = crMap.get("SCHEDULED_START_DATE");
		 logger.debug("Got Scheduled start date from the spreadsheet: " + scheduledStartDate);
		 if(scheduledStartDate != null && !scheduledStartDate.trim().equals("") && !scheduledStartDate.toLowerCase().equals("null")) {
			 String serverScheuledStartDate = Utilities.getServerSideTimeFromLocalTime(scheduledStartDate, serverDifferentHours);
			 crMap.replace("SCHEDULED_START_DATE", serverScheuledStartDate);
			 logger.debug("Replace SCHEDULED_START_DATE with adjusted value: " + serverScheuledStartDate);
		 }

		 String verificationStartDate = crMap.get("VERIFICATION_START_DATE");
		 logger.debug("Got verificationStartDate from the spreadsheet: " + verificationStartDate);
		 if(verificationStartDate != null && !verificationStartDate.trim().equals("") && !verificationStartDate.toLowerCase().equals("null")) {
			 String serverVerificationStartDate = Utilities.getServerSideTimeFromLocalTime(verificationStartDate, serverDifferentHours);
			 crMap.replace("VERIFICATION_START_DATE", serverVerificationStartDate);
		 }
		 
		 String actualStartDate = crMap.get("ACTUAL_START_DATE");
		 if(actualStartDate != null && !actualStartDate.trim().equals("") && !actualStartDate.toLowerCase().equals("null")) {
			 String serverActualStartDate = Utilities.getServerSideTimeFromLocalTime(actualStartDate, serverDifferentHours);
			 crMap.replace("ACTUAL_START_DATE", serverActualStartDate);
		 }		 

		 String actualEndDate = crMap.get("ACTUAL_END_DATE");
		 if(actualEndDate != null && !actualEndDate.trim().equals("") && !actualEndDate.toLowerCase().equals("null")) {
			 String serverActualEndtDate = Utilities.getServerSideTimeFromLocalTime(actualEndDate, serverDifferentHours);
			 crMap.replace("ACTUAL_END_DATE", serverActualEndtDate);
		 }		 
		 
		 return crMap;
	 }
     public static Map<String, String> readCRInfoFromSpreadsheet(String filePath, String extra) throws FileNotFoundException, IOException, ParseException{
			logger.debug("Entering Utilities.readCRInfoFromSpreadsheet...");
			//Create the input stream from the xlsx/xls file
			 HashMap<String, String> items = new HashMap<String, String>();
			//Create Workbook instance for xlsx/xls file input stream
				
				//Get the sheet for CR Info from the spreadsheet file
				// Sheet sheet = readSheetFromExcel(filePath, 1);
			    Sheet sheet = readSheetFromExcel(filePath, Utilities.SHEET_NUMBER_CR_TICKET);
				//every sheet has rows, iterate over them
				Iterator<Row> rowIterator = sheet.iterator();
				int line = 0;	
				while (rowIterator.hasNext()) 
		        {
					String name = "";
					String shortCode = "";
					line++;
					//Get the row object
					Row row = rowIterator.next();
					
					//Every row has columns, get the column iterator and iterate over them
					Iterator<Cell> cellIterator = row.cellIterator();
		            // System.out.println("Row " + line + ":"); 
		            int cellNo = 0;
		            boolean isTaskNumberEmpty = false;
		            boolean isTaskTitleEmpty=false;
		            while (cellIterator.hasNext()) 
		            {
		            	//Get the Cell object
		            	Object cellValue = null;
		            	Cell cell = cellIterator.next();
		            	CellType cellType = cell.getCellType();
		            	if (cellType == CellType.STRING) {
		            		cellValue = cell.getStringCellValue();
		            	} else if (cellType == CellType.NUMERIC) {
		            		if (DateUtil.isCellDateFormatted(cell)) {
		            			String pattern;
		            			//if they are the lines for duration, only time is obtained
		            			if(line==2 || line==4 || line==5){
		            				pattern = "HH:mm:ss";
		            			} else {
		            			    pattern = "yyyy-MM-dd HH:mm:ss";
		            			}
		            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
		            		} else {
		            			cellValue = cell.getNumericCellValue();
		            			if (line == 24) cellValue = (int)cell.getNumericCellValue();
		            			
		            		}
		            	} else if (cellType == CellType.BOOLEAN) {
		            		cellValue = cell.getBooleanCellValue();
		            	} else if (cellType == CellType.FORMULA) {
		            		    CellType cellType1 = cell.getCachedFormulaResultType();
		            		    if (cellType1 == CellType.STRING) {
				            		cellValue = cell.getStringCellValue();
				            	} else if (cellType1 == CellType.NUMERIC) {
				            		if (DateUtil.isCellDateFormatted(cell)) {
				            			String pattern = "yyyy-MM-dd HH:mm:ss";
				            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
				            		} 
				            	} else if (cellType1 == CellType.BOOLEAN) {
				            		cellValue = cell.getBooleanCellValue();
				            	} 
		            		    
		            	} else if (cellType == CellType.BLANK) {
		            		cellValue = "";
		            	}
		            	
		            	// System.out.println(cellNo + ": " + cellValue);
		            	if(cellNo == 1){
		            		if(line == 1 ) {
		            			items.put("SCHEDULED_START_DATE", "" + cellValue);
		            		}else if(line == 2 ) {
		            			items.put("SCHEDULED_DURATION", "" + cellValue);
		            		}else if(line == 3 ) {
		            			items.put("VERIFICATION_START_DATE", "" + cellValue);
		            		} else if(line == 4 ) {
		            			items.put("VERIFICATION_DURATION", "" + cellValue);
		            		} else if(line == 5 ) {
		            			items.put("BACKOUT_DURATION", "" + cellValue);
		            		} else if (line == 6) {
		            			items.put("NEED_BY_DATE", "" + cellValue);
		            		} else if (line == 7) {
		            			items.put("ACTUAL_START_DATE", "" + cellValue);
		            		} else if (line == 8) {
		            			items.put("ACTUAL_END_DATE", "" + cellValue);
		            		} else if (line == 9) {
		            			items.put("CHANGE_ORDER_SUMMARY", "" + cellValue);
		            		} else if (line == 10) {
		            			items.put("CHANGE_ORDER_DESCRIPTION", "" + cellValue);
		            		} else if (line == 11) {
		            			items.put("CONTACT_INFORMATION", "" + cellValue);
		            		} else if (line == 12) {
		            			items.put("BUSINESS_JUSTIFICATION", "" + cellValue);
		            		} else if (line == 13) {
		            			items.put("REQUESTER", "" + cellValue);
		            		} else if (line == 14) {
		            			items.put("AFFECTED_END_USER", "" + cellValue);
		            		} else if (line == 15) {
		            			items.put("OWNING_GROUP", "" + cellValue);
		            		} else if (line == 16) {
		            			items.put("SUPPORTING_REGION", "" + cellValue);
		            		} else if (line == 17) {
		            			items.put("IMPLEMENTER", "" + cellValue);
		            			logger.debug("IMPLEMENTER from spreadsheet:'" + cellValue + "'");
		            		} else if (line == 18) {
		            			items.put("IMPLEMENTING_TEAM", "" + cellValue);
		            			logger.debug("IMPLEMENTING_TEAM from spreadsheet:'" + cellValue + "'");
		            		} else if (line == 19) {
		            			items.put("REQUIRE_PAR", "" + cellValue);
		            		} else if (line == 20) {
		            			items.put("OUTSIDE_MAINTENANCE_WINDOW", "" + cellValue);
		            		} else if (line == 21) {
		            			items.put("IT_SERVICE_ACCEPTANCE", "" + cellValue);
		            			logger.debug("PROJECT_ID from spreadsheet:'" + cellValue + "'");
		            		} else if (line == 22) {
		            			items.put("PROJECT_ID", "" + cellValue);
		            			logger.debug("PROJECT_ID from spreadsheet:'" + cellValue + "'");
		 	            	} else if (line == 23){
		            			items.put("CATEGORY", "" + cellValue);
		            			logger.debug("CATEGORY from spreadsheet:'" + cellValue + "'");
		            		} else if (line == 24){
		            			items.put("PACKAGE_ID", "" + cellValue);
		            			logger.debug("PACKAGE_ID from spreadsheet:'" + cellValue + "'");		            			
		            		} else if (line == 25){
		            			items.put("DEPLOYMENT_TOOL", "" + cellValue);
		            			logger.debug("DEPLOYMENT_TOOL from spreadsheet:'" + cellValue + "'");		            					            			
		            		}
		            		
		                	String isOutsideMaintenanceWindow = items.get("OUTSIDE_MAINTENANCE_WINDOW");
		                	String itServiceAcceptance = items.get("IT_SERVICE_ACCEPTANCE");		            	}
		            	cellNo++;
		            } //end of cell iterator
		        } //end of rows iterator
			//close file input stream
			  logger.debug("Information of CR that was from the spreadsheet:");
			  logger.debug("SCHEDULED START DATE: " + items.get("SCHEDULED_START_DATE"));
			  logger.debug("SCHEDULED DURATION: " + items.get("SCHEDULED_DURATION"));
			  logger.debug("VERIFICATION START DATE: " + items.get("VERIFICATION_START_DATE"));
			  logger.debug("VERIFICATION DURATION: " + items.get("VERIFICATION_DURATION"));
			  logger.debug("BACKOUT DURATION: " + items.get("BACKOUT_DURATION"));
			  logger.debug("NEED_BY DATE: " + items.get("NEED_BY_DATE"));
			  logger.debug("ACTUAL START DATE: " + items.get("ACTUAL_START_DATE"));
			  logger.debug("ACTUAL END DATE: " + items.get("ACTUAL_END_DATE"));
			  logger.debug("CHANGE ORDER SUMMARY: " + items.get("CHANGE_ORDER_SUMMARY"));
			  logger.debug("CHANGE ORDER DESCRIPTION: " + items.get("CHANGE_ORDER_DESCRIPTION"));
			  logger.debug("CONTACT INFORMATION: " + items.get("CONTACT_INFORMATION"));
			  logger.debug("BUSINESS JUSTIFICATION: " + items.get("BUSINESS_JUSTIFICATION"));
			  logger.debug("REQUESTER: " + items.get("REQUESTER"));
			  logger.debug("AFFECTED END USER: " + items.get("AFFECTED_END_USER"));
			  logger.debug("OWNING GROUP: " + items.get("OWNING_GROUP"));
			  logger.debug("SUPPORTING REGION: " + items.get("SUPPORTING_REGION"));
			  logger.debug("IMPLEMENTER: " + items.get("IMPLEMENTER"));
			  logger.debug("IMPLEMENTING TEAM: " + items.get("IMPLEMENTING_TEAM"));
			  logger.debug("Require PAR: " + items.get("REQUIRE_PAR"));
			  logger.debug("OUTSIDE MAINTENANCE WINDOW: " + items.get("OUTSIDE_MAINTENANCE_WINDOW"));
			  logger.debug("IT SERVICE ACCEPTANCE: " + items.get("IT_SERVICE_ACCEPTANCE"));
			  logger.debug("Project ID: " + items.get("PROJECT_ID"));
			  logger.debug("Exiting Utilities.readCRInfoFromSpreadsheet...");

		  return items;
	  }
	  
	  public static String getProjectIDFromMap(Map<String, String> items){
		  String projectId = items.get("PROJECT_ID");
		  if(projectId == null) projectId = "";
		  return projectId;
	  }
	  
	  public static Map<String, String> readBreakGlassTicketFromSpreadsheet(String filePath) throws FileNotFoundException, IOException{
			logger.debug("Entering Utilities.readBreakGlassTicketFromSpreadsheet...");
			//Create the input stream from the xlsx/xls file
			 HashMap<String, String> items = new HashMap<String, String>();
			//Create Workbook instance for xlsx/xls file input stream
				
				//Get the sheet for CR Info from the spreadsheet file
				// Sheet sheet = readSheetFromExcel(filePath, 1);
			    Sheet sheet = readSheetFromExcel(filePath, Utilities.SHEET_NUMBER_BREAK_GLASS_TICKET);
				//every sheet has rows, iterate over them
				Iterator<Row> rowIterator = sheet.iterator();
				int line = 0;	
				while (rowIterator.hasNext()) 
		        {
					String name = "";
					String shortCode = "";
					line++;
					//Get the row object
					Row row = rowIterator.next();
					
					//Every row has columns, get the column iterator and iterate over them
					Iterator<Cell> cellIterator = row.cellIterator();
		            // System.out.println("Row " + line + ":"); 
		            int cellNo = 0;
		            boolean isTaskNumberEmpty = false;
		            boolean isTaskTitleEmpty=false;
		            while (cellIterator.hasNext()) 
		            {
		            	//Get the Cell object
		            	Object cellValue = null;
		            	Cell cell = cellIterator.next();
		            	CellType cellType = cell.getCellType();
		            	if (cellType == CellType.STRING) {
		            		cellValue = cell.getStringCellValue();
		            	} else if (cellType == CellType.NUMERIC) {
		            		if (DateUtil.isCellDateFormatted(cell)) {
		            			String pattern;
		            			//if they are the lines for duration, only time is obtained
		            			    pattern = "yyyy-MM-dd HH:mm:ss";
		            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
		            		} else {
		            			cellValue = cell.getNumericCellValue();
		            		}
		            	} else if (cellType == CellType.BOOLEAN) {
		            		cellValue = cell.getBooleanCellValue();
		            	} else if (cellType == CellType.FORMULA) {
		            		    CellType cellType1 = cell.getCachedFormulaResultType();
		            		    if (cellType1 == CellType.STRING) {
				            		cellValue = cell.getStringCellValue();
				            	} else if (cellType1 == CellType.NUMERIC) {
				            		if (DateUtil.isCellDateFormatted(cell)) {
				            			String pattern = "yyyy-MM-dd HH:mm:ss";
				            			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				            			cellValue = simpleDateFormat.format(cell.getDateCellValue());				            			
				            		} else {
				            			cellValue = cell.getNumericCellValue();
				            		}
				            	} else if (cellType1 == CellType.BOOLEAN) {
				            		cellValue = cell.getBooleanCellValue();
				            	} 
		            		    
		            	} else if (cellType == CellType.BLANK) {
		            		cellValue = "";
		            	}
		            	
		            	// System.out.println(cellNo + ": " + cellValue);
		            	if(cellNo == 1){
		            		if (line == 1) {
		            			items.put("REQUESTER", "" + cellValue);
		            		} else if (line == 2) {
		            			items.put("AFFECTED_END_USER", "" + cellValue);
		            		} else if (line == 3) {
		            			items.put("CATEGORY", "" + cellValue);
		            		} 	else if (line == 4) {
		            			items.put("OWNING_GROUP", "" + cellValue);
		            		} else if (line == 5) {
		            			items.put("SUPPORTING_REGION", "" + cellValue);
		            		} else if (line == 6) {
		            			items.put("IMPLEMENTER", "" + cellValue);
		            		} else if (line == 7) {
		            			items.put("IMPLEMENTING_TEAM", "" + cellValue);
		            		} else if (line == 8) {
		            			items.put("CONTACT_NAME", "" + cellValue);
		            		}else if (line == 9) {
		            			items.put("CONTACT_INFORMATION", "" + cellValue);
		            		} else if (line == 10) {
		            			items.put("PRODUCTION_CHANGE", "" + cellValue);
		            		} else if (line == 11) {
		            			items.put("ENDURING_CHANGE", "" + cellValue);
		            		} else if (line == 12) {
		            			items.put("PROJECT_ID", "" + cellValue);
		            		} else if (line == 13) {
		            			items.put("VERIFICATION_START_DATE", "" + cellValue);
		            		}
		            	}
		            	cellNo++;
		            } //end of cell iterator
		        } //end of rows iterator
			//close file input stream
			  logger.debug("REQUESTER: " + items.get("REQUESTER"));
			  logger.debug("AFFECTED END USER: " + items.get("AFFECTED_END_USER"));
			  logger.debug("CATEGORY: " + items.get("CATEGORY"));
			  logger.debug("OWNING GROUP: " + items.get("OWNING_GROUP"));
			  logger.debug("SUPPORTING REGION: " + items.get("SUPPORTING_REGION"));
			  logger.debug("IMPLEMENTER: " + items.get("IMPLEMENTER"));
			  logger.debug("IMPLEMENTING TEAM: " + items.get("IMPLEMENTING_TEAM"));
			  logger.debug("CONTACT_NAME: " + items.get("CONTACT_NAME"));
			  logger.debug("CONTACT_INFORMATION: " + items.get("CONTACT_INFORMATION"));
			  logger.debug("PRODUCTION CHANGE: " + items.get("PRODUCTION_CHANGE"));
			  logger.debug("ENDURING CHANGE: " + items.get("ENDURING_CHANGE"));
			  logger.debug("PROJECT_ID: " + items.get("PROJECT_ID"));
			  logger.debug("VERIFICATION_START_DATE: " + items.get("VERIFICATION_START_DATE"));			  
			  logger.debug("Exiting Utilities.readBreakGlassTicketFromSpreadsheet...");
		  

		  return items;
	  }	  
	  
	  
	  public static ArrayList<PAMTemplate> getPAMTemplateListWithSameGroupAndPAMLocation(String group, String pamLocation, ArrayList<PAMTemplate> list){
		  ArrayList<PAMTemplate> newList = new ArrayList<PAMTemplate>();
		  if(list.size() > 0){
			  for(int i = 0; i < list.size(); i++){
				  if(group.trim().equals(list.get(i).getGroup().trim()) && pamLocation.trim().equals(list.get(i).getPamLocation().trim())){
					  newList.add(list.get(i));
				  }
			  }
		  }
		  return newList;
	  }
	  
	  public static ArrayList<String> getAllGroupsInPamTemplateList(ArrayList<PAMTemplate> list){
		  ArrayList<String> groupList = new ArrayList<String>();
		  if(list.size() > 0){
			  for(int i = 0; i < list.size(); i++){
				  String group = list.get(i).getGroup().trim();
				  if(groupList.size() == 0){ 
					  groupList.add(group);
				  }else{
					  boolean isNewGroup = true;
					  for(int j = 0; j < groupList.size(); j++){
						  if(group.equals(groupList.get(j))){
							  isNewGroup = false;
							  break;
						  }
					  }
					  if(isNewGroup) groupList.add(group);
				  }
			  }
		  }
		  return groupList;
	  }
	  
	  public static ArrayList<String> getAllPamLocationsInPamTemplateList(ArrayList<PAMTemplate> list){
		  ArrayList<String> locationList = new ArrayList<String>();
		  if(list.size() > 0){
			  for(int i = 0; i < list.size(); i++){
				  String location = list.get(i).getPamLocation().trim();
				  if(locationList.size() == 0){
					  locationList.add(location);
				  }else{
					  boolean isNewLocation = true;
					  for(int j = 0; j < locationList.size(); j++){
						  if(location.equals(locationList.get(j))){
							  isNewLocation = false;
							  break;
						  }
					  }
					  if(isNewLocation) locationList.add(location);
				  }
			  }
		  }
		  return locationList;
	  }	  
	  public static ArrayList<PAMTemplate> readPAMTemplatesFromSpreadsheet(String filePath) throws FileNotFoundException, IOException{
			logger.debug("Entering Utilities.readPAMTemplatesFromSpreadsheet...");
			//Create the input stream from the xlsx/xls file
			ArrayList<PAMTemplate> list = new ArrayList<PAMTemplate>();
			//Create Workbook instance for xlsx/xls file input stream
				
				//Get the sheet for CR Info from the spreadsheet file
				// Sheet sheet = readSheetFromExcel(filePath, 3);
			    Sheet sheet = readSheetFromExcel(filePath, Utilities.SHEET_NUMBER_PAM_TEMPLATES);
				//every sheet has rows, iterate over them
				Iterator<Row> rowIterator = sheet.iterator();
				int line = 0;	
				while (rowIterator.hasNext()) 
		        {
					String name = "";
					String shortCode = "";
					line++;
					//Get the row object
					Row row = rowIterator.next();
					
					//Every row has columns, get the column iterator and iterate over them
					Iterator<Cell> cellIterator = row.cellIterator();
					PAMTemplate template = new PAMTemplate();
		            // System.out.println("Row " + line + ":"); 
		            int cellNo = 0;
		            boolean isEndOfList = false;
		            //boolean isTaskTitleEmpty=false;
		            while (cellIterator.hasNext()) 
		            {
		            	//Get the Cell object
		            	Object cellValue = null;
		            	Cell cell = cellIterator.next();
		            	cellValue = Utilities.readCellValue(cell);

		            	
		            	// System.out.println(cellNo + ": " + cellValue);
		            	if(cellNo == 0){
		            		if(cellValue == null || (cellValue+"").trim().equals("") || (cellValue+"").trim().equals("null")){
		            			isEndOfList = true;
		            			break;
		            		} else {		            		
		            		    template.setCi((cellValue + "").trim());
		            		}

		            	} else if (cellNo == 1) {

		            		template.setPamLocation((cellValue + "").trim());
		            	} else if (cellNo == 2) {
		            		template.setGroup((cellValue + "").trim());
		            	}
		            	cellNo++;
		            } //end of cell iterator
		            if(isEndOfList) break;
		            list.add(template);
		        } //end of rows iterator
			//close file input stream
			  if(list.size() > 0){
				  logger.debug("PAM Templates from spreadsheet:");
				  logger.debug("template size: " + list.size());
				  for(int i = 0; i < list.size(); i++){
					  PAMTemplate template = list.get(i);
					  logger.debug(template.getCi() + "; " + template.getPamLocation() + "; " + template.getGroup());
				  }
			  }
			logger.debug("Exiting Utilities.readPAMTemplatesFromSpreadsheet...");
		  return list;
	  }	 
	  
	  public static boolean validateDatesInSpreadSheetForCopyingCR(Map<String, String> items, int instanceNumber) throws ParseException{
		logger.debug("Entering Utilities.validateDatesInSpreadSheetForCopyingCR...");
		  boolean result = true;
		  if(items == null){
			  logger.error("Null was passed to the method of date validation. Please check.");
	     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Null was passed to the method of date validation. Please check with the administrator...");
			  return false;
		  }
		  String scheduledStartDateStr = items.get("SCHEDULED_START_DATE");
		  String scheduledDurationNumberStr = items.get("SCHEDULED_DURATION");
		  String needByDateStr = items.get("NEED_BY_DATE");
		  String verificationStartDateStr = items.get("VERIFICATION_START_DATE");
		  String verificationDurationStr = items.get("VERIFICATION_DURATION");
		  String backoutDuration = items.get("BACKOUT_DURATION");
		  String actualStartDateStr = items.get("ACTUAL_START_DATE");
		  String actualEndDateStr = items.get("ACTUAL_END_DATE");
		  if(scheduledStartDateStr != null){
			  scheduledStartDateStr = scheduledStartDateStr.trim();
		  }else{
			  scheduledStartDateStr = "";
		  }
		  
		  if(scheduledDurationNumberStr != null){
			  scheduledDurationNumberStr = scheduledDurationNumberStr.trim();
		  }else{
			  scheduledDurationNumberStr = "";
		  }
		  if(needByDateStr != null){
			  needByDateStr = needByDateStr.trim();
		  }else{
			  needByDateStr = "";
		  }
		  if(verificationStartDateStr != null){
			  verificationStartDateStr = verificationStartDateStr.trim();
		  }else{
			  verificationStartDateStr = "";
		  }
		  if(verificationDurationStr != null){
			  verificationDurationStr = verificationDurationStr.trim();
		  }else{
			  verificationDurationStr = "";
		  }
		  if(backoutDuration != null){
			  backoutDuration = backoutDuration.trim();
		  }else{
			  backoutDuration = "";
		  }
		  if(actualStartDateStr != null){
			  actualStartDateStr = actualStartDateStr.trim();
		  }else{
			  actualStartDateStr = "";
		  }
		  if(actualEndDateStr != null){
			  actualEndDateStr = actualEndDateStr.trim();
		  }else{
			  actualEndDateStr = "";
		  }
		  
		  String scheduledEndDateStr;
		  if(scheduledStartDateStr.equals("") || scheduledDurationNumberStr.equals("")) {
			  logger.error("Scheduled Start Date or Scheduled Duration can not be empty for copying a CR ticket. Please correct it!");
			  result = false;
		  } else {
			  // String scheduledDurationTimeStr = Utilities.convertHoursDecimalToTimeFormat(scheduledDurationNumberStr);
			  scheduledEndDateStr = Utilities.getEndDateFromDurationHours(scheduledStartDateStr, scheduledDurationNumberStr);
			  logger.debug("Scheduled End Date got from the scheduled start date and duration: " + scheduledEndDateStr);
			  if(!Utilities.isDateBetweenTwoDates(scheduledStartDateStr, scheduledEndDateStr, verificationStartDateStr)) {
				  logger.error("Verifiction Start Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
		     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Verifiction Start Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
				  result = false;
			  }
			  
			  if(!actualStartDateStr.trim().equals("")){
				  if(!Utilities.isDateBetweenTwoDates(scheduledStartDateStr, scheduledEndDateStr, actualStartDateStr)) {
					  logger.error("Actual Start Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
			     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Actual Start Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
					  result = false;
				  }			  
			  }

			  if(!actualEndDateStr.trim().equals("")){
				  if(!Utilities.isDateBetweenTwoDates(scheduledStartDateStr, scheduledEndDateStr, actualEndDateStr.trim())) {
					  logger.error("Actual End Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
			     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Actual End Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
					  result = false;
				  }			  
			  }
		  } 
		  
		  if(verificationDurationStr != null && verificationDurationStr.trim().equals("")){
			  logger.error("Verification Duration can not be empty!");
	     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Verification Duration can not be empty! Please check the spreadsheet!");
			  result = false;
		  }
		  logger.debug("Exiting Utilities.validateDatesInSpreadSheetForCopyingCR...");
		  return result;
	  }
	  
    
	  
	  public static boolean isDurationZero(String durationStr){
			logger.debug("Entering Utilities.isDurationZero...");
			Time duration = Time.valueOf(durationStr);
		    // System.out.println("got the duration...");
			
		    int hours = duration.getHours();
            int minutes = duration.getMinutes();
			int seconds = duration.getSeconds();
			logger.debug("Exiting Utilities.isDurationZero...");
		  return ((hours + minutes + seconds) ==0);
	  }
	  
	  public static boolean isDateBeforeStartDate(String startDateStr, String dateStr) throws ParseException {
			logger.debug("Entering Utilities.isDateBeforeStartDate...");
		    boolean result = false;
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date startDate = simpleDateFormat.parse(startDateStr);
			Date date = simpleDateFormat.parse(dateStr);
			result = date.before(startDate);
				logger.debug("Start Date: " + startDateStr + "  Date: " + dateStr);
				logger.debug("Is the date before the start date: " + result);
				logger.debug("Exiting Utilities.isDateBeforeStartDate...");
	        return result;
	  }
	  
	  public static boolean validateDatesInSpreadSheetForEditingCR(Map<String, String> items, String scheduledStartDateStrFromCR, String scheduledEndDateStrFromCR, int instanceNumber) throws ParseException{
		  logger.debug("Entering Utilities.validateDatesInSpreadSheetForEditingCR...");
		  boolean result = true;
		  logger.info("Validating dates in spreadsheet for editing CR ticket...");
     	  Utilities.appendCRAutomationWebInstanceMsg(instanceNumber, "Validating dates in spreadsheet for editing CR ticket...");
		  String scheduledStartDateStr = items.get("SCHEDULED_START_DATE").trim();
		  String scheduledDurationNumberStr = items.get("SCHEDULED_DURATION").trim();
		  String needByDateStr = items.get("NEED_BY_DATE").trim();
		  String verificationStartDateStr = items.get("VERIFICATION_START_DATE").trim();
		  String verificationDurationStr = items.get("VERIFICATION_DURATION").trim();
		  String backoutDuration = items.get("BACKOUT_DURATION").trim();
		  String actualStartDateStr = items.get("ACTUAL_START_DATE").trim();
		  String actualEndDateStr = items.get("ACTUAL_END_DATE").trim();
		  String scheduledEndDateStr;
		  String verificationEndDateStr;
		  if(scheduledStartDateStr.equals("")){ 
			  scheduledStartDateStr = scheduledStartDateStrFromCR;
		  } 
		  if(scheduledDurationNumberStr.equals("")){
			  scheduledEndDateStr = scheduledEndDateStrFromCR;
		  }else {
			 // String scheduledDurationTimeStr = Utilities.convertHoursDecimalToTimeFormat(scheduledDurationNumberStr);
			  scheduledEndDateStr = Utilities.getEndDateFromDurationHours(scheduledStartDateStr, scheduledDurationNumberStr);
		  }
		  logger.debug("Scheduled Start Date to be used for validation: " + scheduledStartDateStr);
		  logger.debug("Scheduled End Date to be used for validation: " + scheduledEndDateStr);
		  
		  if(!verificationDurationStr.equals("") ) {
			      if(isDurationZero(verificationDurationStr)) {
			    	  logger.error("Verifiction duration can not be 0. Please check the spreadsheet!");
			     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Verifiction duration can not be 0. Please check the spreadsheet!");
			    	  result = false;	
			      }
		  }
			  
		  if(!actualStartDateStr.equals("")){
				  if(!Utilities.isDateBetweenTwoDates(scheduledStartDateStr, scheduledEndDateStr, actualStartDateStr)) {
					  logger.error("Actual Start Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
			     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Actual Start Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
					  result = false;
				  }			  
		 }

		 if(!actualEndDateStr.equals("")){
			 if(!Utilities.isDateBetweenTwoDates(scheduledStartDateStr, scheduledEndDateStr, actualEndDateStr.trim())) {
				logger.error("Actual End Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
		     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Actual End Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
					  result = false;
			 }			  
		 }
			  
		  
		  if(!verificationStartDateStr.equals("")){
			  
			  if(Utilities.isDateBeforeStartDate(scheduledStartDateStr, verificationStartDateStr)){
				  logger.error("Validation Start Date can not be before scheduled start date...");
		     	  Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Actual End Date has to be between scheduled start date and scheduled end date. Please check the spreadsheet!");
				  result = false;
			  }
			  
		  }
				logger.debug("Are the dates in spreadsheet all valid: " + result);
				logger.debug("Exiting Utilities.validateDatesInSpreadSheetForEditingCR...");
		  return result;
	  }	  
	  
	  public boolean isStringInList(ArrayList<String> list, String str){
			logger.debug("Entering Utilities.isStringInList...");
		  boolean result = false;
		  if(list != null && list.size() > 0 && str != null) {
			  for(int i = 0; i < list.size(); i++){
				  if(str.trim().equals(list.get(i))){
					  result = true;
				  }
			  }
		  } 
			logger.debug("Exiting Utilities.isStringInList...");
		  return result;
	  }
	  
	  public static boolean isDateBetweenTwoDates(Date startDate, Date endDate, Date date) {
		 // logger.debug("Entering Utilities.isDateBetweenTwoDates...");
		  boolean result = false;
		  if(!date.before(startDate) && !date.after(endDate)){
			  result = true;
		  }
		  //	logger.debug("Exiting Utilities.isDateBetweenTwoDates...");        
		  return result;
	  }
	  
	  public static boolean isDateBetweenTwoDates(String startDateStr, String endDateStr, String dateStr) throws ParseException {
			logger.debug("Entering Utilities.isDateBetweenTwoDates...");
		    boolean result = false;
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date startDate = simpleDateFormat.parse(startDateStr);
			Date endDate = simpleDateFormat.parse(endDateStr);
			Date date = simpleDateFormat.parse(dateStr);
			result = isDateBetweenTwoDates(startDate, endDate, date);  
			if(debugEnabled){
				logger.debug("Exiting Utilities.isDateBetweenTwoDates...");
			}
		    return result;
	  }
	  
	  public static String getEndDate(String startDateStr, String durationStr) throws ParseException{
			logger.debug("Entering Utilities.getEndDate...");
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date startDate = simpleDateFormat.parse(startDateStr);
		    Date endDate = getEndDate(startDate, durationStr);
		    String formattedEndDate = simpleDateFormat.format(endDate);
				logger.debug("Start date: " + startDate);
				logger.debug("Duration: " + durationStr);
				logger.debug("End date: " + formattedEndDate);
				logger.debug("Exiting Utilities.getEndDate...");
			return formattedEndDate;
            
	  }
	  
	  public static String getEndDateFromDurationHours(String startDateStr, String decimalOfHoursStr) throws ParseException{
			// logger.debug("Entering Utilities.getEndDate...");
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date startDate = simpleDateFormat.parse(startDateStr);
		    Date endDate = getEndDateFromDurationHours(startDate, decimalOfHoursStr);
		    String formattedEndDate = simpleDateFormat.format(endDate);
			// logger.debug("Exiting Utilities.getEndDate...");
			return formattedEndDate;
		  
	  }
	  
	  public static String getServerSideTimeFromLocalTime(String localTime, int serverDifferentHours) throws ParseException {
		  return getEndDateFromDurationHours(localTime, serverDifferentHours + "");
	  }
	  
	  public static String getLocalTimeFromServerSideTime(String hkTime, int serverDifferentHours) throws ParseException {
		  return getEndDateFromDurationHours(hkTime, ((-1) * serverDifferentHours)+"");
	  }	  
	  
	  public static int[] convertStandCRTaskStrsToTaskInts(String[] tasks) {
		  int[] intTasks = null;
		  if(tasks != null && tasks.length > 0) {
			  intTasks = new int[tasks.length];
			  for(int i = 0; i < tasks.length; i++) {
				  String task = tasks[i];
				  intTasks[i] = convertStandardCRTaskStrToTaskInt(task);
			  }
		  } 
		  return intTasks;
	  }
	  
	  public static int[] convertBGCRTaskStrsToTaskInts(String[] tasks) {
		  int[] intTasks = null;
		  if(tasks != null && tasks.length > 0) {
			  intTasks = new int[tasks.length];
			  for(int i = 0; i < tasks.length; i++) {
				  String task = tasks[i];
				  intTasks[i] = convertBreakGlassCRTaskStrToTaskInt(task);
			  }
		  } 
		  return intTasks;
	  }	  
	  
	  public static int convertStandardCRTaskStrToTaskInt(String taskStr) {
		  int result = 0;
		  if(taskStr != null) {
			  if(taskStr.contains("create a new CR ticket")) {
				  result = 1;
			  } else if(taskStr.contains("copy a CR ticket")) {
				  result = 2;
			  }else if(taskStr.contains("edit a CR ticket")) {
				  result = 3;
			  } else if(taskStr.contains("add an implementation plan")) {
				  result = 4;
			  } else if(taskStr.contains("add CIs")) {
				  result = 5;
			  } else if(taskStr.contains("add PAM Templates")) {
				  result = 6;
				  
			  } else if(taskStr.contains("add a project")) {
				  result = 7;
				  
			  } else if(taskStr.contains("add approvers")) {
				  result = 8;
				  
			  } else if(taskStr.contains("add Deployment Tool")) {
				  result = 9;
				  
			  } else if(taskStr.contains("complete Change Survey")) {
				  result = 10;
				  
			  } else if(taskStr.contains("delete the implementation plan from the CR ticket")) {
				  result = 11;
				  
			  } else if(taskStr.contains("submit CR for approval")) {
				  result = 12;
			  } 
			  return result;
		  }
		  
		  return result;
	  }
	  
	  public static int convertBreakGlassCRTaskStrToTaskInt(String taskStr) {
		  int result = 0;
		  if(taskStr != null) {
			  if(taskStr.contains("create a new Break Glass CR ticket")) {
				  result = SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_CREATE_TICKET;
			  } else if(taskStr.contains("add CIs")) {
				  result = SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_CIS;
			  }else if(taskStr.contains("add PAM Templates")) {
				  result = SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_PAM_TEMPLATE;
			  } else if(taskStr.contains("add a project")) {
				  result = SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_ADD_PROJECT;
			  } else if(taskStr.contains("Submit a Break Glass CR ticket for approval")) {
				  result = SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_SUBMIT_TICKET;
			  } 
		  }
		  
		  return result;
	  }	  
	  
	  public static int[] convertMultiTasksStrToArry(String multiTasksStr) {
		  if(multiTasksStr == null) return null;
		  String[] multiTasks = multiTasksStr.split(",");
		  int[] intMultiTasks = null;
		  if(multiTasks != null && multiTasks.length > 0){
			  intMultiTasks = new int[multiTasks.length];
			  for(int i = 0; i < multiTasks.length; i++){
				  multiTasks[i] = multiTasks[i].trim();
				  intMultiTasks[i] = Integer.parseInt(multiTasks[i]);
				  // System.out.println("'" + multiTasks[i] + "'");
			  }
		  }
		  return intMultiTasks;
	  }
	  
	  public static boolean isNewTicketCreatedInMultipleTasks(int[] multiTasks){
		  boolean result = false;
		  if(multiTasks != null && multiTasks.length > 0) {
			  for(int i = 0; i < multiTasks.length; i++){
				  if(multiTasks[i] == SeleniumImplementation.STANDARD_CR_CREATE_NEW_CR){
					  result = true;
				  }
			  }
		  }
		  return result;
	  }
	  
	  public static boolean isNewBGTicketCreatedInMultipleTasks(int[] bg_multiTasks){
		  boolean result = false;
		  if(bg_multiTasks != null && bg_multiTasks.length > 0) {
			  for(int i = 0; i < bg_multiTasks.length; i++){
				  if(bg_multiTasks[i] == SeleniumImplementation.BREAK_GLASS_MULTIPLE_TASKS_CREATE_TICKET){
					  result = true;
				  }
			  }
		  }
		  return result;
	  }
	  
	  public static void copyFile(String src, String dest) throws IOException{
		  File srcFile = new File(src);
		  File destFile = new File(dest);
		  copyFile(srcFile, destFile); 
		  logger.debug("The template file " + src + " was copied to " + dest);
	  }
	  
	  public static void copyFile(File src, File dest) throws IOException {
		    InputStream inputStream = null;
		    OutputStream outputStream = null;
		    try {
		        inputStream = new FileInputStream(src);
		        outputStream = new FileOutputStream(dest);

		        // the size of the buffer doesn't have to be exactly 1024 bytes, try playing around with this number and see what effect it will have on the performance
		        byte[] buffer = new byte[1024];
		        int length = 0;
		        while ((length = inputStream.read(buffer)) > 0) {
		        	outputStream.write(buffer, 0, length);
		        }
		        // logger.debug("The file " + src + " was copied to " + dest);
		    } finally {
		    	inputStream.close();
		    	outputStream.close();
		    }
		}
	  
	  public static String convertHoursDecimalToTimeFormat(String decimalOfHours){
		  double finalBuildTime = Double.parseDouble(decimalOfHours);
		  int hours = (int) finalBuildTime;
		  int minutes = (int) (finalBuildTime * 60) % 60;
		  int seconds = (int) (finalBuildTime * (60*60)) % 60;
		  return String.format("%s:%s:%s", hours, minutes, seconds);
	  }
	  
	  public static Date getEndDateFromDurationHours(Date startDate, String decimalOfHoursStr) {
		  double decimalOfHours = Double.parseDouble(decimalOfHoursStr);  
		  int hours = (int) decimalOfHours;
		  int minutes = (int) (decimalOfHours * 60) % 60;
		  int seconds = (int) (decimalOfHours * (60*60)) % 60;
		  Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.HOUR, hours);
			calendar.add(Calendar.MINUTE, minutes);
			calendar.add(Calendar.SECOND, seconds);
		    Date date1 = calendar.getTime();
		    return date1;
	  }
	  
	  public static Map<String, String> getCrManagementProperties(String propertiesFilePath){
	    	 FileReader reader = null;
	 	     Map<String, String> propertiesAndValues = new HashMap<String, String>();
	    	 try {
	      	   reader=new FileReader(propertiesFilePath); 
	      	   Properties p=new Properties();  
	      	   p.load(reader);  
	    	   String SDM_URL  = p.getProperty("SDM_URL");
	    	   String nodeURL  = p.getProperty("NodeURL");
	    	   String debugStr = p.getProperty("Debug");
	    	   String virtualWindow = p.getProperty("VirtualWindow");
	    	   String excelFileRoot = p.getProperty("ExcelFileRootPath");
	    	   String templateFilePath = p.getProperty("TemplateFilePath");
	    	   String serverTimeZone = p.getProperty("ServerTimezone");
	    	    propertiesAndValues.put("NodeURL", nodeURL);
	    	    propertiesAndValues.put("SDM_URL", SDM_URL);
	    	    propertiesAndValues.put("Debug", debugStr);
	    	    propertiesAndValues.put("VirtualWindow", virtualWindow);
	    	    propertiesAndValues.put("ExcelFileRootPath", excelFileRoot);
	    	    propertiesAndValues.put("TemplateFilePath", templateFilePath);
	    	    propertiesAndValues.put("ServerTimezone", serverTimeZone);
	    	    logger.debug("ServerTimezone from the file: " + serverTimeZone);
	    	    
	    	   
	    	 } catch(FileNotFoundException e) {
	        	 logger.error("Properties file " + propertiesFilePath + " does not exist. Please check.");
	        	 e.printStackTrace();
	        	 return null;
	         } catch(IOException e){
	        	 logger.error("Problems happened when reading proerties from the file " + propertiesFilePath);
	        	 e.printStackTrace();
	        	 return null;
	         }
	    	 
	    	 return propertiesAndValues;
		  
	  }
	  
	  public static Date getEndDate(Date startDate, String durationStr) throws ParseException{
			//logger.debug("Entering Utilities.getEndDate...");
		    // System.out.println("Getting End Date...");
		    // System.out.println("durationStr: " + durationStr);
			Time duration = Time.valueOf(durationStr);
		    // System.out.println("got the duration...");
			Calendar calendar = Calendar.getInstance();
			int hours = duration.getHours();
			int minutes = duration.getMinutes();
			int seconds = duration.getSeconds();
			
			logger.debug("hours: " + hours + " minutes: " + minutes + "  seconds: " + seconds);
			calendar.setTime(startDate);
			calendar.add(Calendar.HOUR, hours);
			calendar.add(Calendar.MINUTE, minutes);
			calendar.add(Calendar.SECOND, seconds);
			// System.out.println("Hours: " + hours + " minutes: " + minutes + " seconds: " + seconds);
		    Date date1 = calendar.getTime();
		    // System.out.println("End date: " + date1);
			// logger.debug("Exiting Utilities.getEndDate...");
		    return date1;
		    
	  }
	  
	  public static void writeDataToSheets(Object[][] dataOfSheets, String fileName) throws IOException{
		if(dataOfSheets != null && dataOfSheets.length > 0){
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet;
	        CreationHelper createHelper = workbook.getCreationHelper();  
            CellStyle cellStyle = workbook.createCellStyle();  
            cellStyle.setDataFormat(  
                createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));  
            for(int i =0; i < dataOfSheets.length; i++){
				String sheetName = (String)dataOfSheets[i][1];
				Object[][] data =(Object[][]) dataOfSheets[i][0];
				sheet = workbook.createSheet(sheetName);
		        int rowCount = 0;	         
		        for (Object[] oneRowData : data) {
		            Row row = sheet.createRow(rowCount);		             
		            int columnCount = 0;
		             
		            for (Object field : oneRowData) {
		            	if(debugEnabled) System.out.print(" " + field);
		                Cell cell = row.createCell(columnCount);
		                if (field instanceof String) {
		                    cell.setCellValue((String) field);
		                } else if (field instanceof Integer) {
		                    cell.setCellValue((Integer) field);
		                } else if (field instanceof Date){
		                	cell.setCellValue((Date) field);
		                	cell.setCellStyle(cellStyle); 
		                	logger.debug("Collumn: " + columnCount + "  Date:" + field);
		                }
		                columnCount++;
		            }
		            logger.debug("\n");
		            rowCount++;
		             
		        }
		    }
	        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
	            workbook.write(outputStream);
	            outputStream.close();
	        }
	        workbook.close();
			
		} else {
			logger.error("No data to save. The file " + fileName + " was not created or updated");
		}
	  }
	  

	  
	  public static void writeToSpreadsheet(Object[][] data, String sheetName, String fileName) throws IOException {
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet(sheetName);
	        int rowCount = 0;	         
	        for (Object[] oneRowData : data) {
	            Row row = sheet.createRow(rowCount);
	             
	            int columnCount = 0;
	             
	            for (Object field : oneRowData) {
	                Cell cell = row.createCell(columnCount);
	                if (field instanceof String) {
	                    cell.setCellValue((String) field);
	                } else if (field instanceof Integer) {
	                    cell.setCellValue((Integer) field);
	                }
	                columnCount++;
	            } 
	            
	            rowCount++;
	             
	        }
	         	         
	        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
	            workbook.write(outputStream);
	        }
	        workbook.close();
	        
	  }
	  
	  
    public static void updateDataInASheet(Object[][] data, XSSFSheet sheet){
        int rowCount = 0;	         
        for (Object[] oneRowData : data) {
            Row row = sheet.createRow(rowCount);
             
            int columnCount = 0;
             
            for (Object field : oneRowData) {
                Cell cell = row.createCell(columnCount);
                if (field instanceof String) {
                	if(!field.equals("")){
                		cell.setCellValue((String) field);
                	}
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
                columnCount++;
            }
            
            rowCount++;
             
        }
    }

	  public static void updateDataToSheets(Object[][] dataOfSheets, String filePath) throws IOException{
		 // logger.debug("Creating file input stream for " + );
          FileInputStream inputStream = new FileInputStream(new File(filePath));
          Workbook workbook = WorkbookFactory.create(inputStream);
          FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
          workbook.setForceFormulaRecalculation(true);
          CreationHelper createHelper = workbook.getCreationHelper(); 
	        CellStyle strCellStyle = workbook.createCellStyle();
	        strCellStyle.setWrapText(true);
            CellStyle dateCellStyle = workbook.createCellStyle();  
            dateCellStyle.setDataFormat(  
                createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));  
		  if(dataOfSheets != null && dataOfSheets.length > 0){
	        Sheet sheet = null;
			for(int i =0; i < dataOfSheets.length; i++){
				int sheetNumber = (int)dataOfSheets[i][1];
				logger.debug("sheetNumber: " + sheetNumber);
				Object[][] data =(Object[][]) dataOfSheets[i][0];
				if(data != null && data.length > 0){
				sheet = workbook.getSheetAt(sheetNumber);
		        int rowCount = 0;	         
		        for (Object[] oneRowData : data) {
		            Row row = sheet.createRow(rowCount);
		             
		            int columnCount = 0;
		             
		            for (Object field : oneRowData) {
		                Cell cell = row.createCell(columnCount);
		                logger.debug("Row: " + rowCount + " Column: " + columnCount + " cell value: " + field);
		                if (field instanceof String) {
		                	if(!field.equals("")){
		                		if(((String) field).contains("=B1")){
		                			//cell.setCellType(CellType.FORMULA);
		                			cell.setCellFormula(((String) field).substring(1)+"\n");
		                			cell.setCellStyle(dateCellStyle);
		                			//evaluator.evaluateFormulaCell(cell);
		                		} else {
		                		cell.setCellValue((String) field);
		                		cell.setCellStyle(strCellStyle);
		                		}
		                	}
		                } else if (field instanceof Integer) {
		                    cell.setCellValue((Integer) field);
		                	CreationHelper creationHelper = workbook.getCreationHelper();
		                	CellStyle timeStyle = workbook.createCellStyle();          
		                	timeStyle.setDataFormat(creationHelper.createDataFormat().getFormat("h:mm:ss"));
		                	cell.setCellStyle(timeStyle);
		                	
		                } else if (field instanceof Double) {
		                    cell.setCellValue((Double) field);
		                    if(sheetNumber == SeleniumImplementation.SHEET_NUMBER_STANDARD_CR_TICKET && (rowCount == 3 || rowCount == 4) ){
		                    	if((Double)field < 24) {
				                	CreationHelper creationHelper = workbook.getCreationHelper();
				                	CellStyle timeStyle = workbook.createCellStyle();          
				                	timeStyle.setDataFormat(creationHelper.createDataFormat().getFormat("h:mm:ss"));
				                	cell.setCellStyle(timeStyle);
		                    	}
		                    }
		                    
		                } else if (field instanceof Date){
		                	cell.setCellValue((Date) field);
		                	cell.setCellStyle(dateCellStyle); 
		                	// logger.debug("Collumn: " + columnCount + "  Date:" + field);
		                }
		                columnCount++;
		            }
		            
		            rowCount++;
		        }
		             
		        }
		    }
			// evaluator.evaluateAll();
			inputStream.close();
	        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
	            workbook.write(outputStream);
	            outputStream.close();
	        }
	           workbook.close();
			
		} else {
			logger.error("No data to save. The file " + filePath + " was updated");
		}
	  }   

	  public static boolean isAValidDate(String dateStr) {
		  DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		       sdf.setLenient(false);
		        try {
		            Date date = sdf.parse(dateStr);
		            System.out.println("Date: " + date.toString());
		        	// SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		        	// Date actualDate = simpleDateFormat.parse(actualStartDate);      
		        } catch (ParseException e) {
		        	System.out.println("Exception: " + e.getMessage());
		        	e.printStackTrace();
		            return false;
		        }
		        return true;
	  }	  
	  
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//String dateStr = "2020-06-05 2:00:00PM";
		// System.out.println("Is the date '" + dateStr + "' a valid date string? " + Utilities.isAValidDate(dateStr));
		//String endDate = Utilities.getEndDate("2020-06-05 5:00:00PM", "05:00:00");
		//System.out.println("Date: " + endDate);
		// setDebugEnabled(true);
		 String fileName = "c:\\temp\\selenium\\cr1.xlsx";
		 double hours = Utilities.convertDurationStrToHours("47:30:50");
		 logger.debug("hours: " + hours);
	/*	 Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(fileName, 15);
		  logger.debug("SCHEDULED START DATE: " + items.get("SCHEDULED_START_DATE"));
		  logger.debug("SCHEDULED DURATION: " + items.get("SCHEDULED_DURATION"));
		  logger.debug("VERIFICATION START DATE: " + items.get("VERIFICATION_START_DATE"));
		  logger.debug("VERIFICATION DURATION: " + items.get("VERIFICATION_DURATION"));
		  logger.debug("BACKOUT DURATION: " + items.get("BACKOUT_DURATION"));
		  logger.debug("NEED_BY DATE: " + items.get("NEED_BY_DATE"));
		  logger.debug("ACTUAL START DATE: " + items.get("ACTUAL_START_DATE"));
		  logger.debug("ACTUAL END DATE: " + items.get("ACTUAL_END_DATE"));
		  logger.debug("CHANGE ORDER SUMMARY: " + items.get("CHANGE_ORDER_SUMMARY"));
		  logger.debug("CHANGE ORDER DESCRIPTION: " + items.get("CHANGE_ORDER_DESCRIPTION"));
		  logger.debug("CONTACT INFORMATION: " + items.get("CONTACT_INFORMATION"));
		  logger.debug("BUSINESS JUSTIFICATION: " + items.get("BUSINESS_JUSTIFICATION"));
		  logger.debug("REQUESTER: " + items.get("REQUESTER"));
		  logger.debug("AFFECTED END USER: " + items.get("AFFECTED_END_USER"));
		  logger.debug("OWNING GROUP: " + items.get("OWNING_GROUP"));
		  logger.debug("SUPPORTING REGION: " + items.get("SUPPORTING_REGION"));
		  logger.debug("IMPLEMENTER: " + items.get("IMPLEMENTER"));
		  logger.debug("IMPLEMENTING TEAM: " + items.get("IMPLEMENTING_TEAM"));
		  logger.debug("Require PAR: " + items.get("REQUIRE_PAR"));
		  logger.debug("OUTSIDE MAINTENANCE WINDOW: " + items.get("OUTSIDE_MAINTENANCE_WINDOW"));
		  logger.debug("IT SERVICE ACCEPTANCE: " + items.get("IT_SERVICE_ACCEPTANCE"));
		  logger.debug("Project ID: " + items.get("PROJECT_ID"));	*/	 
		 
		//System.out.println("localTime: " + Utilities.getLocalTimeFromServerSideTime("2021-04-25  12:00:00", -2));
		// System.out.println("hkTime: " + Utilities.getServerSideTimeFromLocalTime("2021-04-25  12:00:00", -2));
	/**	    String fileName = "c:\\temp\\selenium\\cr1 - new change survey.xlsx";
		// String fileName = "c:\\temp\\selenium\\NL OTP WALKMAN CR3537199.xlsx";
		 // System.out.println("Duration: " + Utilities.convertDurationStrToHours("45:18:30"));
		// String fileName = "c:\\temp\\selenium\\cr1.xlsx";
		int[] answers = getSurveySelectionsFromSpreadsheet(fileName);
		for(int i = 0; i < answers.length; i++) {
			System.out.println("Answer " + i + ": " + answers[i]);
		} **/
		/* Task[] tasks = Utilities.getTasksFromExcel(fileName);
		for(int i = 0; i < tasks.length; i++){
			System.out.println("task "  + i + ": " + tasks[i].getStartDate() + " " + tasks[i].getEndDate() + " " + tasks[i].getTaskTitle());
		} */
		   
		  // String intStr = "0 ";
		 //  int result = Utilities.convertStringIntegerToInt(intStr);
		 //  System.out.println("int: '" + result + "'" );
		//System.out.println("result: " + Utilities.isINExpiredForCreatingBGCR("2021-01-25 11:06:41"));
	
		   
	/*	Map<String, String> map = Utilities.getCrManagementProperties("C:\\Temp\\seleniumGrid\\crmanagement.properties");
		if(map != null) {
			System.out.println("NodeURL: " + map.get("NodeURL"));
			System.out.println("SDM_URL: " + map.get("SDM_URL"));
			System.out.println("VirtualWindow: " + map.get("VirtualWindow"));			
		} */
		 // System.out.println(Utilities.convertDateTimeStringToDate("2020-08-10 14:25:00") + "");
		// String destFileName = "c:\\temp\\ttt.xlsx";
		// Utilities.copyFile(fileName, destFileName);
    /*    String[][] bookData = {
                {"Head First Java", "Kathy Serria", "79"},
                {"Effective Java", "Joshua Bloch", "36"},
                {"Clean Code", "Robert martin", "42"},
                {"Thinking in Java", "Bruce Eckel", "35"},
        }; */
       //  String sheetName = "implementation plan";
       // String newFileName = "c:\\temp\\CR3230033-Info.xlsx";
		// Utilities.writeToSpreadsheet(bookData, sheetName, newFileName);
		
		// System.out.println("time: " + Utilities.convertHoursDecimalToTimeFormat("7.52"));
       /* String[] acceptance = {"No - It does not affect an IT Technology Service",
        		               "No - It is not a change to a Production or Contingency environment",
        		               "No - The change is for a scheduled Disaster Recovery test only",
        		               "No - This is a Functional change or Planned Patching Change (not impacting Non-Functional Requirements (NFRs))",
        		               "No - This is an Emergency change (ITSA NFRs will be completed retrospectively)",
        		               "Yes"};
        for(int i = 0; i < 6; i++){
        	System.out.println("option value: " + Utilities.getITServiceAcceptanceOptionValue(acceptance[i]) );
        } */
		
		// ArrayList list = Utilities.getApproversFromExcel(fileName);
		// System.out.println("Approver number: " + list.size());
		/*for(int i =0; i < 10; i++){
			Sheet sheet = Utilities.readSheetFromExcel(fileName, i);
			System.out.println("sheet " + i + ", name: " + sheet.getSheetName());
		}*/
		//Utilities.getSurveySelectionsFromStrings(new String[16]);
		// Utilities.getSurveySelectionsFromSpreadsheet(fileName);
	//	ArrayList<PAMTemplate> templateList = Utilities.readPAMTemplatesFromSpreadsheet(fileName);
		/* Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(fileName);
		if(Utilities.validateDatesInSpreadSheetForCopyingCR(items)){
			System.out.println("The dates in the spreadsheet are valid!");
		} else {
			System.out.println("The dates in the spreadsheet are not valid!");
		} 
		*/
		
		//ArrayList<String> list = Utilities.readCIsFromSpreadsheet(fileName);
		// ArrayList<String> grouplist = Utilities.getAllGroupsInPamTemplateList(templateList);
		// ArrayList<String> locationlist = Utilities.getAllPamLocationsInPamTemplateList(templateList);
		// ArrayList<String> locationlist = Utilities.readOwningGroupsFromSpreadsheet(fileName);
	/*	for(int i = 0; i < grouplist.size(); i++){
			for(int j=0; j < locationlist.size(); j++){
				System.out.println("group: " + grouplist.get(i) + "  location: " + locationlist.get(j));
				ArrayList<PAMTemplate> tempList =  Utilities.getPAMTemplateListWithSameGroupAndPAMLocation(grouplist.get(i), locationlist.get(j), templateList);
				if(tempList.size() > 0) {
					System.out.println("Template with group " + grouplist.get(i) + " and pamLocation " + locationlist.get(j));
					for(int k = 0; k < tempList.size(); k++){
						System.out.println("'" + tempList.get(k).getCi() + "'");
					}
				}
			}
		} */
		/**  ArrayList<String> list = Utilities.readValidGroupsFromSpreadsheet(fileName);
		// ArrayList<String> list = Utilities.readValidNamesFromSpreadsheet(fileName);
		// ArrayList<String> list = Utilities.readSupportingRegionFromSpreadsheet(fileName);
		**/
		/** for(int i = 0; i < list.size(); i++){
			System.out.println("'" + list.get(i).trim() + "'");
		} **/
  	 /*  Map<String, String> items = Utilities.readCRInfoFromSpreadsheet(fileName);
  	  boolean flag = Utilities.validateDatesInSpreadSheetForCopyingCR(items);
  	  System.out.println("date check result: " + flag); */
  	 // Utilities.validateDatesInSpreadSheetForEditingCR(items, "2020-07-15  9:00:00", "2020-07-17  9:00:00");
		//Map<String, String> items = Utilities.readBreakGlassTicketFromSpreadsheet(fileName);
		
		// String fileName = "c:\\temp\\selenium\\plan1.xlsx";
	/*Task[] tasks = Utilities.getTasksFromExcel(fileName);
		String[] groups = Utilities.getAssignedGroupsFromTasks(tasks);
		for(int i=0; i<groups.length; i++){
			 System.out.println("assigned group " + (i+1) + ": " + groups[i]);
		}
		ArrayList<String> groupsNotInFile = new ArrayList<String>();
		String FileForExistingGroups = "c:\\temp\\selenium\\ExistingGroups.txt";
		ArrayList<String> list = Utilities.getExistingGroupsFromFile(FileForExistingGroups);
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
			System.out.println("The following assigned group(s) are(is) not in the file ExistingGroups.txt. Please make sure they are correct groups and add them to ExistingGroups.txt ");          
		    for(int i =0; i < groupsNotInFile.size(); i++){
			  System.out.println(groupsNotInFile.get(i));
		    }
		}else{
			System.out.println("All assigned groups are in the file as existing groups.");
		}  */
		
	/*	 if(tasks != null && tasks.length > 0){
			for(int i = 0; i < tasks.length; i++){
				System.out.println("Task " + i + ":");
				System.out.println("Task Title: " + tasks[i].getTaskTitle());
				System.out.println(tasks[i].getStartDate());
				System.out.println(tasks[i].getEndDate());
				System.out.println("Task description: " + tasks[i].getTaskDescription());
				System.out.println("Task group: " + tasks[i].getGroupAssigned());
			}
		} */

	}

}
