<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String staticserver=application.getAttribute("staticserver")==null?request.getContextPath():(String)application.getAttribute("staticserver");  
%>
<%String requestContext=request.getContextPath(); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>

    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
        <%
	 		String flag = request.getAttribute("flag")==null?"":(String)request.getAttribute("flag");
		%>
      <form action=" <%=staticserver %>/index/login.htm""  method="post" name="mcform" id="mcform">
			
					<table class="tab" >
					       <tr>
							<td width="10%" class="tdtit"><%=flag %></td>
							<td width="20%"  class="tdcont">
							</td>
						</tr>
						
						<tr>
							<td width="10%" class="tdtit">用户名：</td>
							<td width="20%"  class="tdcont">
								<input type="text" name="username" id="username" value=""  style="width: 225px"/>
							</td>
						</tr>
						<tr>
							<td width="10%" class="tdtit">密码：</td>
							<td width="20%"  class="tdcont">
								<input type="text" name="password" id="password" value=""  style="width: 225px"/>
							</td>
						</tr>
						
                      	<tr>
							<td colspan="6" style="text-align: center;" >
								<input type="submit" id="btn_add" value="确定" class="rb1" /> &nbsp;&nbsp;&nbsp;&nbsp;
          					</td>
						</tr>
					</table>
			
	   </form>
  </body>
</html>
