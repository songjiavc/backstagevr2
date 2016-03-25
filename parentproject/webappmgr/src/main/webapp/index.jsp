<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Boolean useMaskCode = (Boolean)request.getAttribute("useMaskCode");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>后台数据管理平台</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	
	<jsp:include page="common/top.jsp" flush="true" />

  </head>
  
    <script language="javascript">
		function setFocus(){
			document.login.username.focus();
		}
		function submit(){//点击登录按钮提交
			submitForm();
		}
		function resetForm(){
			document.login.reset();
			document.login.username.focus();
		}
		function changeimage(obj,path){
		    obj.src=path;
		}
		function submitLoginForm(){//回车提交
		 if(event.keyCode==13)
			 submitForm();
		}
		//提交表单方法
		function submitForm()
		{
			
		}
		
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
					
				});
		
</script>

<style>
	.submitBut{
		background-image: url("images/bt1.gif");
		color:white;
		cursor:pointer; 
	}
</style>

</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
	
<center>
  <form method="post" name="login" style="margin:0px" action="<%=request.getContextPath() %>/menu/getNewPage.action"><!-- <%=request.getContextPath() %>/account/Login.action -->
    <table id="__01" width="1000" height="600" border="0" cellpadding="0" cellspacing="0" background="images/bg_01.gif">
      <tr>
        <td colspan="3" height="100">&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td width="578"><div align="center">
        	<div align="right">
        		<span style="float:left;font-size:30px;font-family:隶书">后台数据管理平台</span>
        	</div>
        
            <TABLE align="right" cellSpacing=0 cellPadding=0 width="40%" border="0">
              <TBODY>
                <!-- <TR>
                  <TD class="px12-1" align="right" width="28%" height="25">
                  <div align="center"><font color="#0066CC">类&nbsp;&nbsp;型：</font></div></TD>
                  <TD width="57%">
                  
                  </TD>
                </TR> -->
                <TR>
                  <TD width="28%" height=25 align=right class=px12-1><div align="center">
                  	<font color="#0066CC" >登录名：</font></div>
                  	</TD>
                  <TD><INPUT name="code" id="username" style="width:130px"></TD>
                </TR>
                <TR>
                  <TD class=px12-1 align=right width="28%"
                        height=25><div align="center"><font color="#0066CC">密&nbsp;&nbsp;码：</font></div></TD>
                  <TD width="57%"><INPUT type=password id="password" name="password" style="width:130px"></TD>
                </TR>
                
               <!--  <TR>
                  <TD class=px12-1 align=right width="28%"
                        height=25><div align="center"><font color="#0066CC">验证码：</font></div></TD>
                  <TD width="57%"><INPUT type=text  class=box6 name="code" style="width:63px" onkeydown="submitLoginForm();">
&nbsp;<img id="licenceImg" src="/zyProcurement/authImage" border="0" name="" width="60" height="20" align="absmiddle" alt='验证码,看不清楚? 请点击刷新验证码' onClick="this.src='/zyProcurement/authImage'" style="cursor : pointer;"></TD>
                </TR> -->
        
                <TR>
                  <TD height=40 colspan="2" align=center><br>
                	<button onclick="submit()" class="submitBut">登录</button>
                 
                  </TD>
                </TR>
                
              </TBODY>
            </TABLE>
          </div>
          	<div id="err" align="center"  id="error" style="color: red;float:right;font-weight:bold; font-size: 12px; height: 16px; margin-top: 10px;">
          	
          	</div>
          </td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td colspan="3">&nbsp;</td>
      </tr>
    </table>
  </form>
</center>
</body>
  </body>
</html>
