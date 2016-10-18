<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../common/top.jsp" flush="true" />

<title>error!</title>


<style type="text/css">
	A:link {
		COLOR: #555555; TEXT-DECORATION: none
	}
	A:visited {
		COLOR: #555555; TEXT-DECORATION: none
	}
	A:active {
		COLOR: #555555; TEXT-DECORATION: none
	}
	A:hover {
		COLOR: #6f9822; TEXT-DECORATION: none
	}
	.text {
		FONT-SIZE: 12px; COLOR: #555555; FONT-FAMILY: ""; TEXT-DECORATION: none
	}
	.STYLE1 {font-size: 13px}
	.STYLE2 {font-size: 12px}
	.STYLE3 {font-size: 11px}
	td {
	
		font-size: 14px;
		font-family: "宋体";
	}
</style>

<script language="javascript">
	window.onload = function(){
		var d = new Date();
		var vYear = d.getFullYear();
		var vMon = d.getMonth() + 1;
		var vDay = d.getDate();
		var h = d.getHours(); 
		var m = d.getMinutes(); 
		var se = d.getSeconds(); 
		s=vYear+(vMon<10 ? "0" + vMon : vMon)+(vDay<10 ? "0"+ vDay : vDay)+(h<10 ? "0"+ h : h)+(m<10 ? "0" + m : m)+(se<10 ? "0" +se : se);
		
		
		document.getElementById('tim').innerHTML=s;
		}


  </script>

</head>
<body>
    <table align="center" border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
			<tbody><tr>
				<td align="center" valign="center">
					<table align="center" border="0" cellpadding="0" cellspacing="0" width="500">
						<tbody><tr>
							<td height="17" width="17">
								<img src="<%=request.getContextPath() %>/images/co_01.gif" height="17" width="17">
							</td>
							<td background="<%=request.getContextPath() %>/images/bg01.gif" width="316"></td>
							<td height="17" width="17">
								<img src="<%=request.getContextPath() %>/images/co_02.gif" height="17" width="17">
							</td>
						</tr>
						<tr>
							<td background="<%=request.getContextPath() %>/images/bg02.gif"></td>
							<td>
								<table class="text" align="center" border="0" cellpadding="10" cellspacing="0" width="100%">
									<tbody><tr>
										<td>
											<table border="0" cellpadding="0" cellspacing="0" width="100%">
												<tbody><tr>
													<td width="20">
													</td>
													<td>
														<img src="<%=request.getContextPath() %>/images/404error.gif" height="66" width="400">
													</td>
												</tr>
											</tbody></table>
										</td>
									</tr>
									<tr>
										<td>
											<table border="0" cellpadding="0" cellspacing="0" width="100%">
												<tbody><tr>
													<td background="<%=request.getContextPath() %>/images/dot_01.htm" height="1"></td>
												</tr>
											
											</tbody></table>
											<br>
											<table class="text" border="0" cellpadding="0" cellspacing="0" width="100%">
												<tbody><tr>
													<td width="20">
													</td>
													<td>
														<p>
															<strong><font color="#ba1c1c">系统错误</font>
															</strong>
															<br>
															<br>
															错误ID：【<span id="tim"></span>】.请检查您的操作是否正确。
															<br>
															<%-- 异常信息： ${exceptionMessage} --%>
															<br>
															请尝试以下操作重新登录系统：
														</p>
														<br>

														
														·单击
														<a href="<%=request.getContextPath() %>/menu/el.action"><font color="#BA1C1C">跳转到登录页...</font>
														</a><br><br><br>
														<div>
															
														</div>
													</td>
												</tr>
											</tbody></table>
										</td>
									</tr>
								</tbody></table>
							</td>
							<td background="<%=request.getContextPath() %>/images/bg03.gif"></td>
						</tr>
						<tr>
							<td height="17" width="17">
								<img src="<%=request.getContextPath() %>/images/co_03.gif" height="17" width="17">
							</td>
							<td background="<%=request.getContextPath() %>/images/bg04.gif" height="17"></td>
							<td height="17" width="17">
								<img src="<%=request.getContextPath() %>/images/co_04.gif" height="17" width="17">
							</td>
						</tr>
					</tbody></table>
				
				</td>
			</tr>

		</tbody></table>
</body>
</html>