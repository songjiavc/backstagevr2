<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
  <head>
    
    <title>后台数据管理平台</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	
	<jsp:include page="common/top.jsp" flush="true" />
	<link rel="stylesheet" href="css/indexStyle.css">

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
								default:msg="登录信息错误，请确认后登录!";
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
<body  style=" background-image: url('images/1.jpg'); background-repeat: repeat;width:100%;height:100%; margin:0; padding:0;">
	
	<div  id="top">
		<div style="margin-top:10px;margin-left:20px;">后台数据管理平台</div>
		
	</div>
    <section class="container">
	    <div class="login">
	      <h1>用户登录</h1>
	      <form method="post" id="form1" name="login" action="<%=request.getContextPath() %>/menu/getNewPage.action">
	        <p><input type="text" name="code" id="username" placeholder="用户名"></p>
	        <p><input type="password" id="password" name="password"  placeholder="密码"></p>
	       
	      </form>
	      <div id="err" align="center"  id="error" style="color: red;float:right;font-weight:bold; font-size: 12px; height: 16px; "></div>
	        <p class="submit"><button type="button" onclick="submitform()" name="submit">登录</button></p>
	    </div>
    </section>
    <div style="text-align:center;" id="footer">
		<div style="margin-top: 20px;">Copyright © 2015 - 2016  by our company</div>
	</div>
 
  </body>
</html>
