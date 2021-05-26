<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
 
<!DOCTYPE html> 
<html>
<head>
<meta charset="ISO-8859-1"> 
<title>CR Management Tool</title>
 <!--  script src="<c:url value='/resources/js/jquery.min.js' />"></script -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>

<!--  Instance Number: ${Instance_Number} <br /> -->

<div id="returnedMsgDiv" ">
	<h3>Messages returned to you from the system:</h3>
	    - <b><span id=returnedMsg></span></b><br/>
</div>

<script>
$(document).ready(function(){
  /*  $.get("/crmanagement/ajaxMessages", function(data, status){
    	$('#returnedMsg').text(data.msg);
    }); */

    var varInverval = setInterval(function() 
    	    {

	                var count = 0;
                 	$.get("/crmanagement/ajaxMessages?instanceNumber=${Instance_Number}", function(data, status){
                 		 const div = document.createElement('div');

                 		  div.className = 'row';
                 		  var isTaskCompleted = data.taskEnded;
                 		  var displayStr = data.msg;
                 		  if(isTaskCompleted){
                     		  displayStr = displayStr + " <br /> </br> <span style='font-weight:bold'> End of the task </span> <br /> <br />" 
                     		                + " <span style='color:blue'> <a href='/crmanagement/?fn=${ExcelFileName}&fn1=${ExistingInfo}'>Continue another task with the provided information previously</a> </span>"
                     		                + " <br /> <br /> <span style='color:blue'><a href='/crmanagement/?rm=${ExcelFileName}&rm1=${ExistingInfo}' >Continue another task with a fresh start</a>  </span>"
                     		                + " <br /> <br /> <span style='color:blue'><a href='/crmanagement/remove?fn=${ExcelFileName}&fn1=${ExistingInfo}' >Remove your information from memory</a>  </span>";
                     		 clearInterval(varInverval);
                     	  }

                 		  div.innerHTML = displayStr;
                 		  document.getElementById('returnedMsgDiv').appendChild(div);
                 		  
		    	});
    	    }, 5000);
   
});
</script>
</body>
</html>
