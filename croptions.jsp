<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
		<title>CR Management Tool</title>
    </head>
    <body>
        <h2>CR tickets management tool</h2>
        <br>
        <h4 id ="ExistingExcelMsg" style="color:green; display: none;" "> Using the uploaded ${uploadedFileName} file for the next task...</h4>
        <h3>Please enter the following information for CR tickets' management</h3>
        <br>
        <form:form method="POST" name="myform" action="/crmanagement/select" modelAttribute="crManager"  enctype = "multipart/form-data" onsubmit="return validateForm()">
             <table>
                <tr>
                    <td><form:label path="ticketType">Select CR ticket type:</form:label></td>
                    <td> 
        			<!-- <form:radiobutton path="ticketType" value="1" checked="checked" onclick="setSelectValue('crOptions',1);" />Standard CR Ticket <br>
                     <form:radiobutton path="ticketType" value="2"  onclick="setSelectValue('crOptions',2);" />Break Glass CR Ticket  
                 	</td> -->
                 	  <form:select  path="ticketType" id="ticketType" onchange="changeCrOptions()">
						    <form:option value="0">-- Select Ticket Type --</form:option> 
						    <form:option value="1">Standard CR Ticket</form:option> 
						    <form:option value="2">Break Glass CR Ticket</form:option> 
					   </form:select>
					 </td> <td id="ticketTypeErr"></td>
                </tr> 
                <tr>
        			
                 	
                </tr>
                <tr> </tr>

				<tr>
                    <td><form:label path="cr_number">CR Ticket Number:</form:label></td>
                    <td><form:input id="cr_number" path="cr_number"/></td> <td id = "cr_numberErr"></td>
				
				</tr>
				<tr/>
				<tr/>
				<tr>
                    <td><form:label path="in_number">IN Ticket Number:</form:label></td>
                    <td><form:input id="in_number" path="in_number"/></td>	<td id = "in_numberErr"></td>			
				</tr>
				<tr/>
				<tr id = "userIDLine">
                    <td><form:label path="userID" id = "userIDLabel">Your user ID to login to GSD:</form:label></td>
                    <td><form:input id = "userID" path="userID"/></td> <td id = "userIdErr"> </td>				
				<tr/>
				<tr/>
				<tr id = "passwordLine">
                    <td><form:label path="password" id = "passwordLabel">Your password to login to GSD:</form:label></td>
                    <td><form:input id="password" path="password" type = "password"/></td> <td id = "passwordErr"></td>
                </tr>
                <tr> </tr>

	
                <tr>
           
                  <td><form:label path="actionNumber">Select task to manage a CR Ticket:</form:label></td>
                  <td>
	                  <form:select  path="actionNumber" id="crOptions" onchange="hideOrShowMultiTasks()">
						    <form:option value="0">-- Select Task --</form:option>
					   </form:select>
				   </td> <td id = "crOptionsErr"></td>
                </tr>
                <tr>  
				</tr>
				<tr/>
				<tr id = "bgCRDescriptionTr" style="display: none;">
                    <td><form:label path="bgCRDescription" id = "bgCRDescriptionLabel">Describe activities in the BG CR Ticket:</form:label></td>
                    <td><form:input id = "bgCRDescription" path="bgCRDescription" /> </td> <td id = "bgCRDescriptionErr"> </td>				
				</tr>				

				<tr/>
							
				<tr/>
		            				
	                <tr id="multiTaskTr" style="display: none;">
							<td> <ul><form:checkboxes element="li" items="${multiTasksList}" path="multipleTasks" /> </ul>
							</td>             
					</tr>
 
 	                <tr id="bgMultiTaskTr" style="display: none;">
							<td> <ul><form:checkboxes element="li" items="${bgMultiTasksList}" path="bgMultipleTasks" /> </ul>
							</td>             
					</tr>
 				<tr id="FileUploadLine"  >
                    <td><label>Please select a file to upload:</label></td>
                    <td><input id="fileId"  type = "file" name = "file" onchange="file_selected = true;" /></td> <td id = "fileErr"></td>
                </tr>
 				
 
 		             <tr id="FileUploadLine1" style="display: none;">
			             <td> Please select a file to upload:</td> 
			            
			             <td>  <input id = "fileId" type = "file" name = "file" onchange="file_selected = true;" /> </td> <td id="fileErr"> </td>
		            </tr>

		            <tr>
		              <td><input type = "hidden" id = "ExistingExcelInput" name = "ExistingExcelName" value = ""></td>
		            </tr>			
		            <tr>
		              <td><input type = "hidden" id = "ExistingInfo" name = "ExistingInfo" value = ""></td>
		            </tr>			
				<tr />
				<tr><td><input type = "hidden" id = "timezoneInput" name = "timezoneInput" value = ""></td>
				     
                </tr>
				     
                </tr>				     	
                <tr> 
                    <td><input type="submit" value="Submit"/></td>
                </tr>
                
            </table>
        </form:form>
        
         <br> <br>
         <a href='/crmanagement/download?template=template.xlsx' >Download the Excel template file if you don't have it</a>
    </body>
</html>

<script>
var crTaskOptions = {};
var file_selected = false;
crTaskOptions['0'] = ['-- Select Task --'];
crTaskOptions['1'] = ['-- Select Task --','create a new CR ticket', 'copy a CR ticket', 
	                    'edit a CR ticket', 'add an implementation plan', 'Add CIs',
	                    'Add PAM Templates', 'Add a Project', 'Add approvers', 
	                    'Add a Deployment Tool', 'complete Change Survey', 'delete the implementation plan from the CR ticket',
	                    'submit CR ticket for approval', 'Save a CR ticket to a spreadsheet', 'do multiple tasks at a time'];
crTaskOptions['2'] = ['-- Select Task --','create a new BG CR ticket', 'Add CIs', 'Add PAM Template', 'Add a project',
	                  'Submit the BG CR ticket for approval', 'do multiple tasks at a time']; 

var excelFileName = '${uploadedFileName}';
var existingInfo = '${ExistingInfo}';
var usingExistingFile = false;
var usingExistingInfo = false;

if(excelFileName != null && excelFileName.trim() != ""){
	 usingExistingExcelFile();
} else if(existingInfo != null && existingInfo.trim() != ""){
	usingExistingInfoWithoutExcelFile();
} else  {
	 usingNewExcelFile();
}

getTimezoneFromBrowser();

function changeCrOptions() {
  var crTypeList = document.getElementById("ticketType");
  var crTaskOptionList = document.getElementById("crOptions");
  var selCrType = crTypeList.options[crTypeList.selectedIndex].value;
  while (crTaskOptionList.options.length) {
	  crTaskOptionList.remove(0);
  }
  var crTasks = crTaskOptions[selCrType];
  if (crTasks) {
    var i;
    for (i = 0; i < crTasks.length; i++) {
      var task = new Option(crTasks[i], i);
      crTaskOptionList.options.add(task);
    }
  }
} 

function hideOrShowMultiTasks(){
	  var crTaskOptionList = document.getElementById("crOptions");
	  var selTaskOption = crTaskOptionList.options[crTaskOptionList.selectedIndex].value;
	  if(selTaskOption == '14'){
		  document.getElementById("multiTaskTr").style.display = "block";
		 // getElementByXPath("//*[@id='multiTaskTr']/td/ul/li[2]").hide();
	  } else {
		  document.getElementById("multiTaskTr").style.display = "none";		  
	  }
	  if(selTaskOption == '11' || selTaskOption == '12'){
		  hideFileUploadLine();
	  } else {
		  showFileUploadLine();
	  }
	  var ticketType =  document.getElementById("ticketType").value;
	  if(ticketType == '2' && selTaskOption == '6'){
		  document.getElementById("bgMultiTaskTr").style.display = "block";
	  }else {
		  document.getElementById("bgMultiTaskTr").style.display = "none";		  
	  }

	  if(ticketType == '2' && selTaskOption == '5'){
		  document.getElementById("bgCRDescriptionTr").style.display = "";
		  document.getElementById("FileUploadLine").style.display = "none";	
	  }else {
		  document.getElementById("bgCRDescriptionTr").style.display = "none";		  
	  }	  

	  if(usingExistingFile){
		  document.getElementById("FileUploadLine").style.display = "none";
	  }


	  
}

function showFileUploadLine(){
	  document.getElementById("FileUploadLine").style.display = "";		
}

function getElementByXPath(xpath) {
    var result = document.evaluate(xpath, document, null, XPathResult.ANY_TYPE, null).singleNodeValue;
    return result;
}


function usingExistingExcelFile(){
	  document.getElementById("ExistingExcelMsg").style.display = "block";
	  document.getElementById("FileUploadLine").style.display = "none";
	  document.getElementById("ExistingExcelInput").value = '${ExistingExelFileName}';
	  document.getElementById("userIDLine").style.display = "none";
	  document.getElementById("passwordLine").style.display = "none";
	  usingExistingFile = true;
	 		  	
}

function usingExistingInfoWithoutExcelFile(){
	  // document.getElementById("ExistingExcelMsg").style.display = "block";
	  // document.getElementById("FileUploadLine").style.display = "none";
	  document.getElementById("ExistingInfo").value = '${ExistingInfo}';
	  document.getElementById("userIDLine").style.display = "none";
	  document.getElementById("passwordLine").style.display = "none";
	  usingExistingInfo = true;	
}

function hideFileUploadLine(){
	  document.getElementById("FileUploadLine").style.display = "none";	
}


function getTimezoneFromBrowser() {
    timezone = (new Date()).getTimezoneOffset()/60;
    document.getElementById("timezoneInput").value = timezone;
    return timezone;
}




function usingNewExcelFile(){
	  document.getElementById("ExistingExcelMsg").style.display = "none";
	 // document.getElementById("FileUploadLine").style.display = "flex";
	  // document.getElementById("fileId").required = true;		  	
}


function validateForm() {
	  var userId =  document.getElementById("userID").value;
	  var userIdErr =  document.getElementById("userIdErr");
	  var cr_number =  document.getElementById("cr_number").value;
	  var cr_numberErr =  document.getElementById("cr_numberErr");
	  var ticketType =  document.getElementById("ticketType").value;
	  var ticketTypeErr =  document.getElementById("ticketTypeErr");
	  var in_number =  document.getElementById("in_number").value;
	  var in_numberErr =  document.getElementById("in_numberErr");
	  var password =  document.getElementById("password").value;
	  var passwordErr =  document.getElementById("passwordErr");
	  var crOptions =  document.getElementById("crOptions").value;
	  var crOptionsErr =  document.getElementById("crOptionsErr");
	  var fileErr =  document.getElementById("fileErr");
	  var bgTicketActivities =  document.getElementById("bgCRDescription").value;
	  var bgTicketActivitiesErr =  document.getElementById("bgCRDescriptionErr");
	  
	  var multipleTasks1 = document.getElementById("multipleTasks1");
	  var multipleTasks2 = document.getElementById("multipleTasks2");
	  var multipleTasks3 = document.getElementById("multipleTasks3");
	  var multipleTasks4 = document.getElementById("multipleTasks4");
	  var multipleTasks5 = document.getElementById("multipleTasks5");
	  var multipleTasks6 = document.getElementById("multipleTasks6");
	  var multipleTasks7 = document.getElementById("multipleTasks7");
	  var multipleTasks8 = document.getElementById("multipleTasks8");
	  var multipleTasks9 = document.getElementById("multipleTasks9");
	  // var multipleTasks10 = document.getElementById("multipleTasks10");
	 // var multipleTasks11 = document.getElementById("multipleTasks11");
	 // var multipleTasks12 = document.getElementById("multipleTasks12");
	  var crTaskOptionList = document.getElementById("crOptions");
	  var selTaskOption = crTaskOptionList.options[crTaskOptionList.selectedIndex].value;
	  var bgMultipleTasks1 = document.getElementById("bgMultipleTasks1");
	  var bgMultipleTasks2 = document.getElementById("bgMultipleTasks2");
	  var bgMultipleTasks3 = document.getElementById("bgMultipleTasks3");
	  var bgMultipleTasks4 = document.getElementById("bgMultipleTasks4");
	  
	  var result = true;
	  userIdErr.innerText = "";
	  passwordErr.innerText = "";
	  ticketTypeErr.innerText = "";
	  cr_numberErr.innerText = "";
	  in_numberErr.innerText = "";
	  bgTicketActivitiesErr.innerText = "";
	  crOptionsErr.innerText = "";
	  fileErr.innerText = "";
 
	  if(!usingExistingFile && !usingExistingInfo){
		  if (userId.trim() == "") {
		    //alert("User ID must be filled out");
		    userIdErr.innerText = "User ID can not be empty!";
		    userIdErr.style.color = "red";
		    result = false;
		  } 
		  
		  if (password.trim() == "") {
			    //alert("User ID must be filled out");
			    passwordErr.innerText = "Password can not be empty!";
			    passwordErr.style.color = "red";
			    result = false;
		  }	
	  }

	  if (ticketType == "0") {
		    //alert("User ID must be filled out");
		    ticketTypeErr.innerText = "Please select the ticket type!";
		    ticketTypeErr.style.color = "red";
		    result = false;
	  }	

	  if(ticketType == "1"){
		  if(crOptions != "0" && crOptions != "1" && crOptions != "14" ) {
			  if(cr_number.trim() == ""){
				  cr_numberErr.innerText = "CR Number can not be empty!";
				  cr_numberErr.style.color = "red";
				    result = false;
			  } else {
				  cr_numberErr.innerText = "";
			  }
		  } 
	  }

	  if(ticketType == "1" && crOptions == "14"){
		  if(multipleTasks1.checked){
			  cr_numberErr.innerText = "";
		  } else if(multipleTasks2.checked || multipleTasks3.checked || multipleTasks4.checked
			 || multipleTasks5.checked || multipleTasks6.checked || multipleTasks7.checked
			 || multipleTasks8.checked ){
			  if(cr_number.trim() == ""){
				  cr_numberErr.innerText = "CR Number can not be empty!";
				  cr_numberErr.style.color = "red";
				    result = false;
			  } 
		  } 

		if(!multipleTasks1.checked && !multipleTasks2.checked && !multipleTasks3.checked && !multipleTasks4.checked
				  && !multipleTasks5.checked && !multipleTasks6.checked && !multipleTasks7.checked
				  && !multipleTasks8.checked )  {
				    crOptionsErr.innerText = "At lease one of the multiple tasks has to be checked!";
				    crOptionsErr.style.color = "red";
				    result = false;		      
			  }
	  }  

	  if(ticketType == "2" && crOptions == "1"){
		  if(in_number.trim() == ""){
			  in_numberErr.innerText = "IN Number can not be empty!";
			  in_numberErr.style.color = "red";
			    result = false;
		  } 		  
	  } 

	  if(ticketType == "2" && crOptions == "5"){
		  if(bgTicketActivities.trim() == ""){
			  bgTicketActivitiesErr.innerText = "BG CR Ticket activities can not be empty!";
			  bgTicketActivitiesErr.style.color = "red";
			  result = false;
		  } 	  
	  }	  
	/*  if(ticketType == "2"){
		    ticketTypeErr.innerText = "The fuctions for Break Glass CRs are under construction. Please check it later!";
		    ticketTypeErr.style.color = "red";
		    result = false;
	  }	else {
		  ticketTypeErr.innerText = "";
	  }	*/

	  if(ticketType == "2" && crOptions != "1" && crOptions != "6"){
		  if(cr_number.trim() == ""){
			  cr_numberErr.innerText = "CR Number can not be empty!";
			  cr_numberErr.style.color = "red";
			    result = false;
		  } 		  
	  }
	    

	  if (crOptions == "0") {
		    //alert("User ID must be filled out");
		    crOptionsErr.innerText = "Please select Task!";
		    crOptionsErr.style.color = "red";
		    result = false;
	  }	

		 if(!usingExistingFile && ticketType == "2" && crOptions != "5" && crOptions != "6" ){
			  if(!file_selected){
				  fileErr.innerText = "Please select a Excel to upload!";
				  fileErr.style.color = "red";
				  result = false;
			 }
		 }


		 if(ticketType == "2" && crOptions == "6"){
				if(!bgMultipleTasks1.checked && !bgMultipleTasks2.checked && !bgMultipleTasks3.checked && !bgMultipleTasks4.checked){
				    crOptionsErr.innerText = "At lease one of the multiple tasks has to be checked!";
				    crOptionsErr.style.color = "red";
				    result = false;
						
				}

				if(bgMultipleTasks1.checked){
					  cr_numberErr.innerText = "";
					  if(in_number.trim() == ""){
						  in_numberErr.innerText = "IN Number can not be empty!";
						  in_numberErr.style.color = "red";
						    result = false;
					  } 
				 } else if(multipleTasks2.checked || multipleTasks3.checked || multipleTasks4.checked){
					  if(cr_number.trim() == ""){
						  cr_numberErr.innerText = "CR Number can not be empty!";
						  cr_numberErr.style.color = "red";
						    result = false;
					  } 
				  } 				
			 }	 
		 

	  if(!usingExistingFile){
			if(ticketType == "1" && selTaskOption != '11' && selTaskOption != '12'  && selTaskOption != '14'){
				  if(!file_selected){
					  fileErr.innerText = "Please select a Excel to upload!";
					  fileErr.style.color = "red";
					  result = false;
				 }
			} else if(selTaskOption == '14'){
			  if(!multipleTasks2.checked && !multipleTasks3.checked && !multipleTasks4.checked
					  && !multipleTasks5.checked && !multipleTasks6.checked && !multipleTasks7.checked
					  && !multipleTasks8.checked && !multipleTasks1.checked){
				      fileErr.innerText = "";
			  } else {
				  if(!file_selected){
					  fileErr.innerText = "Please select a Excel to upload!";
					  fileErr.style.color = "red";
					  result = false;
				 } 
		     }
	     } 


	  }
	    return result;
		  
}

</script>
