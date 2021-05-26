package com.hsbc.selenium.crmanagement.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; 
 
import com.hsbc.selenium.crmanagement.model.AjaxResponseBody;
import com.hsbc.selenium.crmanagement.model.CRAutomationWebInstance;
import com.hsbc.selenium.crmanagement.utilities.CRAutomationWebInstanceFactory;
import com.hsbc.selenium.crmanagement.utilities.Utilities;

@Controller
public class CRAjaxController {
	private static Logger logger = Logger.getLogger(CRAjaxController.class);

	@ResponseBody
    @RequestMapping(value = "/ajaxMessages", method = RequestMethod.GET)
	public AjaxResponseBody getMessagesViaAjax(@RequestParam("instanceNumber") String instanceNoStr) {
		AjaxResponseBody body = new AjaxResponseBody();
	    // body.setMsg("<span>Hello World!!!! </span> <br /> <span>Hello World??? </span>");
	    logger.debug("Instance number from the page:" + instanceNoStr);
	    int instanceNumber = Utilities.convertStringIntegerToInt(instanceNoStr);
	    // logger.debug("instance number:" + instanceNumber);
	    if(instanceNumber < 0) {
	    	logger.error("The instance number from the page is not valid. End the task...");
	    	Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "The instance number from the page is not valid. End the task...");
	    	body.setTaskEnded(true);
	    	return body;
	    }
	    
		CRAutomationWebInstance instance = CRAutomationWebInstanceFactory.getCRAutomationWebInstance(instanceNumber);
	    logger.debug("got the instance back from the instance number: " + instanceNumber);
		if(instance != null) {
			body.setMsg(instance.getMsg());
			instance.setMsg("");
		    logger.debug("the instance is not null...");
		    long instanceStartTime = instance.getStartTime();
		    long difference = System.currentTimeMillis() - instanceStartTime;
		    logger.debug(difference/1000 + " seconds have passed since instanced was initiated.");
	  	    
		    boolean taskCompleted = instance.isTaskCompleted();
		    if(taskCompleted) {
		    	logger.debug("Task has been completed or stopped..."); 
		    	body.setTaskEnded(true);
		    	CRAutomationWebInstanceFactory.removeCRAutomationWebInstance(instance);
		    } else if (!instance.isDriverSetup()) {
		       if(difference/1000 > 120) {
			    	logger.debug("It took too long to set up web driver. stop waiting and display the error message..."); 
			    	instance.setTaskCompleted(true);
			    	Utilities.appendCRAutomationWebInstanceErrMsg(instanceNumber, "Waiting too long for web driver setup. Please contact administrator to check...");
		       }
	  	    }
		} else {
			body.setMsg("<span style='color:red'>No sessoin information was found. You may have been idle for too long. Please have a fresh start ... </span>");
			body.setTaskEnded(true);
		    logger.debug("the instance is null...");
		}
	    return body; 
	} 

		// AjaxResponseBody result = new AjaxResponseBody();
		//logic
		//return result;
  
	//}

}
