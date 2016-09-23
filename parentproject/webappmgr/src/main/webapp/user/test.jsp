<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
  <head>
    
    <title>后台数据管理平台</title>
	 <jsp:include page="../common/top.jsp" flush="true" /> 
	  <script type="text/javascript">
	 
        //设置登录窗口
        function openPwd() {
            $('#w').window({
                title: '修改密码',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 160,
                resizable:false
            });
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }
    </script>

<script type="text/javascript" src="<%=request.getContextPath() %>/user/js/test.js"></script>  
</head>
<body class="easyui-layout" style="overflow-y: hidden"  scroll="no">
<!-- message中存放的是登录信息 -->
<input type="hidden" name="message" id="message" value="${message}">

<noscript>
<div style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
    <img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
</div></noscript>
    <div region="north" split="false" border="false" style="overflow: hidden; height: 50px;
        background: url('../images/1.jpg')  repeat-x center 50%;
        color: #fff; font-family: Verdana, 微软雅黑,黑体">
        <span style="float:right; padding-right:20px;" class="head"> 
       	 <a href="#" id="editpass">修改密码</a> <a href="#" id="loginOut" onclick="logout()">安全退出</a>
        </span>
         <span style="float:right; padding-right:20px;" class="head"> 
         		当前登录用户：<span id="loginuser">admin</span>
         </span>

        <span style="padding-left:10px; font-size: 36px; float:left;font-family:隶书;">后台数据管理平台</span>
    </div>
    <div region="south" split="false" style="height: 30px; background: #D2E0F2; ">
        <div class="footer"><center>后台数据管理平台@2016</center></div>
    </div>
    <div region="west" hide="true" split="false" title="导航菜单" style="width:180px;" id="west">
<div id='wnav' class="easyui-accordion" fit="true" border="false">
		<!--  导航内容 -->
				
			</div>

    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<div title="欢迎使用" style="padding:20px;overflow:hidden;" id="home">
				
			<h1>欢迎进入佰艺霖企业管理平台</h1>

			</div>
		</div>
    </div>
    
    
    <!--修改密码窗口-->
    <div id="w" class="easyui-window" title="修改密码" collapsible="false" minimizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <form id="updatePasswordForm" action="<%=request.getContextPath() %>/account/updatePassword.action" method="post">
                <table cellpadding=3>
                    <tr>
                        <td>新密码：</td>
                        <td><input class="easyui-validatebox textbox" type="password" id="password" name="password" data-options="required:true" validType="length[6,15]"   ></input></td>
                    </tr>
                    <tr>
                        <td>确认密码：</td>
                        <td><input class="easyui-validatebox textbox" type="password" id="confirmPassword" name="confirmPassword" data-options="required:true"  validType="equalTo['#password']"   ></input></td>
                    </tr>
                </table>
                </form>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消</a>
            </div>
        </div>
    </div>

	<div id="mm" class="easyui-menu" style="width:150px;">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>


</body>
</html>