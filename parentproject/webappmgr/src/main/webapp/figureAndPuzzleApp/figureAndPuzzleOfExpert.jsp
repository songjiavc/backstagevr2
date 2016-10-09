<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>专家的图谜字谜管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <jsp:include page="../common/top.jsp" flush="true" /> 
 	<link rel="shortcut icon" href="<%=request.getContextPath() %>/images/favicon.ico">
    <script src="<%=request.getContextPath() %>/figureAndPuzzleApp/js/figureAndPuzzleOfExpert.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加字谜类型',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	$("#addPuzzleType").dialog('open');
  	    	
  	    }
  	}  ];
  	  
  	
		
	</script>
		<style type="text/css">
			.ztree li button.switch {visibility:hidden; width:1px;}
			.ztree li button.switch.roots_docu {visibility:visible; width:16px;}
			.ztree li button.switch.center_docu {visibility:visible; width:16px;}
			.ztree li button.switch.bottom_docu {visibility:visible; width:16px;}
			
			 .ftitle{
	  			width:100%;
	  			float : left;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑',
	  		}
	  		.ftitle label{
	  			float : left;
	  			margin-left: 30px;
	  			width:100px;
	  		}
	  		.ftitle .commonInput{
	  			float : left;
	  			width: 200px;
	  		
	  			border-radius : 5px;
	  		}
	  		
	  		.ftitle .numberInput{
	  			float : left;
	  			width: 200px;
	  			margin-left: 30px;
	  			border-radius : 5px;
	  		}
	  		
	  		.td_font{
	  			font-weight:bold;
	  		}
	  		
	  		.input_border{
	  			width:150px;
	  			border-radius : 5px;
	  		}
	  		
	  		#main-layout{     min-width:1050px;     min-height:240px;     overflow:hidden; }
		</style>
		
	 
</head>
<body class="easyui-layout">
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
         		当前登录专家：<span id="loginuser">admin</span>
         </span>
		<img alt="" src="<%=request.getContextPath() %>/images/clogo.png" style="float:left;">
        <span style="padding-left:10px; font-size: 36px; float:left;font-family:隶书;">图谜字谜发布平台</span>
    </div>
    <div region="south" split="false" style="height: 30px; background: #D2E0F2; ">
        <div class="footer"><center>佰艺霖专家图谜字谜发布管理平台@2016</center></div>
    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden;">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<div title="欢迎使用" style=" " id="home">
				<!-- 模糊查询 -->
			<div   data-options="region:'north'" style="height:10%;border:1px solid white; background-color:white;">
	    	<table style="border: none;">
		    	<tr>
		    		<td width="7%" class="td_font">字谜类型名称：</td>
		    		<td width="15%">
		    			<input id="typeNameC" class="input_border"  type="text" name="typeNameC"  />  
		    		</td>
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
		    	</table>	
			</div>
			
		    <div  data-options="region:'center'" data-options="border:false" style="height:89%;">
		    	 <table id="datagrid" class="easyui-datagrid"  title="字谜类型列表" >
					</table>
		 	</div>  
 	
			</div>
		</div>
    </div>
	
 	<!-- message中存放的是登录信息 -->
	<input type="hidden" name="message" id="message" value="${message}">
  
  
    <!-- 添加字谜类型 -->
  <div id="addPuzzleType" class="easyui-dialog"  title="添加字谜类型" style="width:500px;height:400px;padding:10px;top:10px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddPuzzleType();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                       		addDialogCancel();
                    }
                }]
            ">
		<form id="ff" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeA">字谜类型名称:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <input class="easyui-validatebox commonInput" type="text" id="typeNameA" name="typeName"  
	                data-options="required:true" validType="checkTypeName['#typeNameA','idA']"></input>
	        </div>
	        <div class="ftitle">
	            <label for="nameA">字谜行数:</label>
	            <input class="easyui-numberbox numberInput" type="text" id="typeColA" name="typeCol" data-options="required:true"
	              missingMessage="字谜行数不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="passwordA">字谜每行最多字数:</label>
	            <input class="easyui-numberbox numberInput" type="text" id="typeColWordsNumA" name="typeColWordsNum" data-options="required:true"
	             missingMessage="字谜每行最多字数不可以为空" ></input>
	        </div>
	        
	      </form>
    </div>
     <!-- 修改字谜类型-->
     <div id="updatePuzzleType" class="easyui-dialog"  title="修改字谜类型" style="width:500px;height:400px;padding:10px;top:10px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdatePuzzleType();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updatePuzzleType').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	       <div class="ftitle">
	            <label for="typeNameU">字谜类型名称:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <input class="easyui-validatebox commonInput" type="text" id="typeNameU" name="typeName"  
	                data-options="required:true" validType="checkTypeName['#typeNameU','idU']"></input>
	        </div>
	        <div class="ftitle">
	            <label for="typeColU">字谜行数:</label>
	            <input class="easyui-numberbox numberInput" type="text" id="typeColU" name="typeCol" data-options="required:true"
	              missingMessage="字谜行数不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="typeWordsNumU">字谜每行最多字数:</label>
	            <input class="easyui-numberbox numberInput" type="text" id="typeColWordsNumU" name="typeColWordsNum" data-options="required:true"
	             missingMessage="字谜每行最多字数不可以为空" ></input>
	        </div>
	      </form>
    </div>
    
    <!--修改密码窗口-->
    <div id="w" class="easyui-window" title="修改密码" collapsible="false" minimizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <form id="updatePasswordForm" action="<%=request.getContextPath() %>/fmpApp/updateExpertPassword.action" method="post">
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
</body>



	
	
</html>