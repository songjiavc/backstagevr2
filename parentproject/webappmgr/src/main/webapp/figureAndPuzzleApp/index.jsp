<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
  <head>
    
    <title>图谜字谜</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	
	<jsp:include page="../common/top.jsp" flush="true" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/indexStyle.css">
  <link rel="shortcut icon" href="<%=request.getContextPath() %>/images/favicon.ico">
  </head>
  
    <script >
		$(document).ready(function()
				{
					var alertmsg = getQueryString('alertmsg');
					if(null != alertmsg)
						{
							var msg = "";
							switch(alertmsg)
							{
								case '0':msg="当前登录名不存在，请确认后登录!";break;
								case '1':msg="登录密码不可以为空!";break;
								case '2':msg="登录密码不正确，请确认后登录!";break;
								case '3':msg="安全退出!";break;
								//default:msg="安全退出!";
							}
							$("#err").html(msg);
						}
					
					$(document).keydown(function(event){ 
							if(event.keyCode==13){ 
								$("#form1").submit();
							} 
						}); 
						
				});
		
		function submitform()
		{
			$("#form1").submit();
		}
		
</script>

<style>
	.submitBut{
		background-image: url("images/bt1.gif");
		color:white;
		cursor:pointer; 
	}
	
	
</style>

</head>
<body  style=" background-image: url('<%=request.getContextPath() %>/images/1.jpg'); background-repeat: repeat;width:100%;height:100%; margin:0; padding:0;">
	<div  id="top" style="height:50px;">
		<div style="margin-top:10px;margin-left:20px;">佰艺霖图谜字谜专家发布平台</div>
		
	</div>
    <section class="container">
	    <div class="login">
	      <h1>专家登录</h1>
	      <form method="post" id="form1" name="login" action="<%=request.getContextPath() %>/fmpApp/expertLogin.action">
	        <p><input type="text" name="code" id="username" placeholder="登录名"></p>
	        <p><input type="password" id="password" name="password"  placeholder="密码"></p>
	       
	      </form>
	      <div id="err" align="center"  id="error" style="color: red;float:right;font-weight:bold; font-size: 12px; height: 16px; "></div>
	        <p class="submit"><button type="button" onclick="submitform()" name="submit">登录</button></p>
	    </div>
    </section>
    <div style="text-align:center;" id="footer">
		<div style="margin-top: 20px;">Copyright © 2015 - 2016  by 佰艺霖</div>
	</div>
 
  </body>
</html>
