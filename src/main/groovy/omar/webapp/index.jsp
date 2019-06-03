<%@ page import="omar.cucumber.backend.test.CucumberTest"%>
<html>
<head>
</head>
<body>
<%!
  public void callBackend() {
    CucumberTest backend = new CucumberTest();
    backend.startTest();
  }
%>
<%
  String requestMethod = request.getMethod();
  if(requestMethod.equals("POST")){
    callBackend();
  }
%>
<form name="backendTestForm" action="#" method="post">
  <input type="submit" id="backendTestBtn" value="Run Test" style="background-color: green; height: 50px; width: 100px; color: white; font-size: 16px">
</form>
<h2>Please wait until test is complete before clicking the links below or you will see old data.</h2>
<p><a href="./reports/html/">Test Report</a></br>
<a href="./reports/json/backend.json">Test Json</a></p>
</body>
</html>