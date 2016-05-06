<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
    
    <title>代理管理</title>
	  
	<jsp:include page="../common/top.jsp" flush="true" /> 
    <script type="text/javascript" src="<%=request.getContextPath() %>/user/js/agentManager.js"></script>  
    <script type="text/javascript">
	   	//后台传入前台初始化参数
	  	var initParam = {
    		userId : '<%=request.getAttribute("userId")%>',
    		flag : '<%=request.getAttribute("flag")%>'
    	};
    	var toolbar = [{
	  	    text:'添加',
	  	    iconCls:'icon-add',
	  	    handler:function(){
	  	    	 //初始化上级权限下拉框值
	  	    	initProvince('add','','');//默认选中全部，则全部下是没有市数据的
	  	    	$('.panel-title.panel-with-icon').html('添加代理');
	  	    	$('#addOrUpdateAgentForm').form('clear');
	  	    	$("#addOrUpdateAgent").dialog('open');
	  	    	initAddFormParentId(initParam);
	  	    }
	  	},{
	  	    text:'删除',
	  	    iconCls:'icon-remove',
	  	    handler:function(){
	  	    	deleteAgentByIds();
	  	    }
	  	}];
	</script>
	 <style type="text/css" scoped="scoped">
	  		.ftitle{
	  			width:100%;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑',
	  		}
	  		.ftitle label{
	  			margin-left: 30px;
	  		}
	  		
	  		.ftitle .textbox{
	  			margin-right: 50px;
	  			float : right;
	  			width: 200px;
	  		}
	  		
	  	</style>
</head>
<body class="easyui-layout">
	<div  data-options="region:'north'" style="height:100px;">
		<form id="searchForm" method="post" >
	        <table style="border: none;height: 70px;margin:10 10 10 100">
		    	<tr style="margin:10;">
		    		<td colspan="1" class="td_font">代理帐号：</td>
		    		<td colspan="2">
		    			<input id="searchFormNumber" class="input_border"  type="text" name="searchFormNumber"  />  
		    		</td>
		    		<td colspan="1" class="td_font">代理姓名：</td>
		    		<td colspan="2">
		    			<input id="searchFormName" class="input_border"  type="text" name="searchFormName"  />  
		    		</td>
		    		<td colspan="1" class="td_font">代理电话：</td>
		    		<td colspan="2">
		    			<input id="searchFormTelephone" class="input_border"  type="text" name="searchFormTelephone"  />  
		    		</td>
	    		</tr>
	    		<tr>
		    		<td colspan="1"  class="td_font">代理位置：</td>
		    		<td colspan="5">
		    			 <label for="searchFormProvince">省:</label>
		    			 <select class="easyui-combobox " id="searchFormProvince" name="searchFormProvince" style="width:150px;" >
						 </select>
						 <label for="searchFormCity">市:</label>
								<select class="easyui-combobox " id="searchFormCity" name="searchFormCity"  style="width:150px;" >
						</select>
		    		</td>
	        		<td colspan="1" class="td_font">所属专员：</td>
		    		<td colspan="1">
		    			<input id="searchFormParentId" name="searchFormParentId" class="easyui-combobox textbox"  />
		    		</td>
		    		<td class="td_font" colspan="2">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;width:80;" class="easyui-linkbutton" data-options="iconCls:'icon-search'"  type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;width:80;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	    	</form>
	</div>
	<div data-options="region:'center'" style="height:100px;">
		<table id="agentDataGrid" class="easyui-datagrid" title="代理列表"  data-options="toolbar:toolbar" >
		</table>
	</div>
	    <!-- 添加代理 -->
  	<div id="addOrUpdateAgent" class="easyui-dialog" title="代理编辑" style="width:480px;height:530px;padding:20px;"
            data-options="
           	modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddAgent();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addOrUpdateAgent').dialog('close');
                    }
                }]
            ">
		<form id="addOrUpdateAgentForm" method="post" >
	        <input type="hidden" name="id" />
	        <div class="ftitle">
	            <label for="addFormAgentCode">登录帐号:</label>
	            <input class="easyui-validatebox textbox" type="text" id="addFormAgentCode" name="addFormAgentCode"  data-options="required:true,validType:['englishOrNum','length[0,20]',]"" 
	              missingMessage="代理编码不可以为空" invalidMessage="代理编码长度不可以超过20个字符"></input>
	        </div>
	         <div class="ftitle">
	            <label for="addFormProvince">所属专员:</label>
	            <input id="addFormParentId" name="addFormParentId" class="easyui-combobox textbox"  />
	        </div>
	         <div class="ftitle">
	            <label for="addFormName" >代理姓名:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormName"  data-options="required:true,validType:['CHS','length[0,20]']"  missingMessage="站主真实姓名"></input>
	        </div>
	        <div class="ftitle">
	            <label for="addFormProvince">省:</label>
	            <input id="addFormProvince" name="addFormProvince" class="easyui-combobox textbox"  />
	        </div>
	        <div class="ftitle">
	        	<label for="addFormCity">市:</label>
	        	<input id="addFormCity" name="addFormCity" class="easyui-combobox textbox"   />
	        </div>
	        <div class="ftitle">
	        	<label for="addFormRegion">区:</label>
	        	<input id="addFormRegion" name="addFormRegion" class="easyui-combobox textbox"   />
	        </div>
	        <div class="ftitle">
	            <label for="addFormAddress" >详细地址:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormAddress"  data-options="multiline:true,required:true,validType:['length[0,40]']"  missingMessage="代理详细地址"></input>
	        </div>
	        <div class="ftitle">
	        	<label for="addFormTelephone" >代理手机号:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormTelephone"  data-options="required:true,validType:['mobile']" missingMessage="站主手机号"></input>
	        </div>
	        <div class="ftitle">
	            <label  for="password">密码:</label>
	            <input class="easyui-validatebox textbox" type="password" id="password" name="password" data-options="required:true"
	            validType="length[6,15]"   ></input>
	        </div>
	        <div class="ftitle">
	            <label for="confirmPassword" >确认密码:</label>
	            <input class="easyui-validatebox textbox" type="password" name ="confirmPassword"  data-options="required:true" validType="equalTo['#password']" invalidMessage="两次输入密码不匹配"></input>
	        </div>
          </form>
     </div> www
     <!-- 添加权限弹框 -->
  	 <div id="setAgentScope" class="easyui-dialog" data-options="modal:true" title="设定代理范围" style="width:480px;height:450px;padding:20px;" ></div>
</body>
</html>