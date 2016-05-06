<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
    
    <title>测试页面</title>
	  
	<jsp:include page="../common/top.jsp" flush="true" /> 
    <script type="text/javascript" src="<%=request.getContextPath() %>/user/js/useraccount.js"></script>  
    <script type="text/javascript">
	    var toolbar = [{
	  	    text:'添加',
	  	    iconCls:'icon-add',
	  	    handler:function(){
	  	    	initProvince('add','privinceA','');//默认选中全部，则全部下是没有市数据的
	  	    	$("#addAccount").dialog('open');
	  	    }
	  	},{
	  	    text:'删除',
	  	    iconCls:'icon-remove',
	  	    handler:function(){
	  	    	deleteAccountByIds();
	  	    }
	  	}];
	</script>
	 <style type="text/css" scoped="scoped">
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
	  		.ftitle .textbox{
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
<body>
	
	<table id="accountDataGrid" class="easyui-datagrid" title="用户列表" 
			data-options="toolbar:toolbar" ></table>
	    <!-- 添加权限弹框 -->
  	<div id="addAccount" class="easyui-dialog"  title="添加用户" style="width:500px;height:400px;padding:10px;top:40px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddAccount();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addAccount').dialog('close');
                    }
                }]
            ">
		<form id="addAccountForm" method="post" >
	        <div class="ftitle">
	            <label for="code">用户编码:</label>
	            <input class="easyui-validatebox textbox" type="text" name="code"  data-options="required:true,validType:['englishOrNum','length[0,20]']""
	              missingMessage="用户编码不可以为空" invalidMessage="用户编码长度不可以超过20个字符"></input>
	        </div>
	        <div class="ftitle">
	            <label for="name" >用户姓名:</label>
	            <input class="easyui-validatebox textbox" type="text" name="name"  data-options="required:true,validType:['CHS','length[0,20]']"  missingMessage="用户真实姓名"></input>
	        </div>
	        <div class="ftitle">
	        	<label for="telephone" >用户电话:</label>
	            <input class="easyui-validatebox textbox" type="text" name="telephone"  data-options="required:true,validType:['mobile']" missingMessage="站主手机号"></input>
	        </div>
	        <div class="ftitle">
	            <label  for="password">密码:</label>
	            <input class="easyui-validatebox textbox" type="password" id="password" name="password" data-options="required:true"
	            validType="length[6,20]"   missingMessage="密码不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="confirmPassword" >确认密码:</label>
	            <input class="easyui-validatebox textbox" type="password" name ="confirmPassword"  data-options="required:true" validType="equalTo['#password']" invalidMessage="两次输入密码不匹配"></input>
	        </div>
	        <div class="ftitle">
	            <label for="lotteryTypeA" >用户类型:</label>
		    			<select class="easyui-combobox" id="lotteryTypeA" name="lotteryType" style="width:200px;">
							<option value="">全部</option>
							<option value="1" >体彩</option>
							<option value="2">福彩</option>
						</select>
	        </div>
	         <div class="ftitle">
	            <label for="subject">用户地域:</label>
	            <div style="">
	           		<!-- <label for="privinceA">省:</label> -->
		            <select class="easyui-combobox " id="privinceA" name="province"  
		          	  data-options="editable:false" style="width:100px;" >
					</select>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="cityA" name="city"  
		          	  data-options="editable:false" style="width:100px;" >
					</select>
	            </div>
	        </div>
	       	 <div class="ftitle">
	            <label for="status">是否启用:</label>
	            <div style="float:right;margin-right: 40%;">
		            <input class="easyui-validatebox" type="radio" name="status"  value="1" checked>是</input>
		            <input class="easyui-validatebox" style="margin-left:10px;" type="radio" name="status" value="0">否</input>
	        	</div>
	        </div>
          </form>
     </div>  
     <!-- 修改权限弹框 -->
    <div id="updateAccount" class="easyui-dialog"  title="修改用户" style="width:500px;height:400px;padding:10px;"
            data-options=" 
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateAccount();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateAccount').dialog('close');
                    }
                }]
            ">
		<form id="updateAccountForm" method="post" novalidate>
	       	<input type="hidden" name="id" />
	      	<div class="ftitle">
	            <label for="code" >用户帐号:</label>
	          	<input class="easyui-validatebox textbox" type="text" name="code"  readonly="readonly" data-options="required:true" 
	             validType="checkCodes['#codeA','idA']" missingMessage="权限编码不可以为空" invalidMessage="权限编码长度不可以超过20个字符"></input>
	        </div>
	        <div class="ftitle">
	            <label for="name" >用户姓名:</label>
	            <input class="easyui-validatebox textbox" type="text" name="name"  data-options="required:true"  missingMessage="用户真实姓名"></input>
	        </div>
	        <div class="ftitle">
	        	<label for="telephone" >用户电话:</label>
	            <input class="easyui-validatebox textbox" type="text" name="telephone" missingMessage="用户电话"></input>
	        </div>
	        <div class="ftitle">
	            <label  for="password">密码:</label>
	            <input class="easyui-validatebox textbox" type="password" name="password" data-options="required:true"
	            validType="length[1,20]"   missingMessage="密码不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="confirmPassword" >确认密码:</label>
	            <input class="easyui-validatebox textbox" type="password" name ="confirmPassword"  data-options="required:true"></input>
	        </div>
	        <div class="ftitle">
	            <label for="lotteryTypeU" >用户类型:</label>
		    			<select class="easyui-combobox" id="lotteryTypeU" name="lotteryType" style="width:200px;">
							<option value="0">全部</option>
							<option value="1" >体彩</option>
							<option value="2">福彩</option>
						</select>
	        </div>
	          <div class="ftitle">
	            <label for="subject">用户地域:</label>
	            <div style="">
	           		<!-- <label for="privinceA">省:</label> -->
		            <select class="easyui-combobox " id="privinceU" name="province"  
		          	  data-options="editable:false" style="width:100px;" >
					</select>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="cityU" name="city"  
		          	  data-options="editable:false" style="width:100px;" >
					</select>
	            </div>
	        </div>
          	<div class="ftitle">
	            <label for="status">是否启用:</label>
	            <div style="float:right;margin-right: 40%;">
		            <input class="easyui-validatebox" type="radio" name="status"  value="1" checked>是</input>
		            <input class="easyui-validatebox" style="margin-left:10px;" type="radio" name="status" value="0">否</input>
	        	</div>
	        </div>
    		</form>
       </div>
       
       
    <div id="selectRoleDiv" class="easyui-dialog"   title="角色选择" style="width:800px;height:600px;padding:0px;border:0;" data-options="
   				 modal:true,
                iconCls: 'icon-search',
                 onBeforeClose:function(){
        			  selectRoleBeforeClose();
   		 		},
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitSelRoles();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#selectRoleDiv').dialog('close');
                    }
                }]"
               >
    	<div class="easyui-layout" style="overflow-y: hidden;height:100%;padding:0;" >
    	 	<div region="north" style="height:70%;">
    	 		<input type="hidden" id = "userId" ></input>
    	 		<table id="selectRoleGrid" class="easyui-datagrid" style="width:98%;height:98%;border:0;" title="待选列表" ></table>
    	 	</div>
    	 	<div region="center" style="height:30%;padding:0;">
    	 		<table id="selectedRoleGrid" class="easyui-datagrid" style="width:98%;height:98%;border:0;" title="已选列表" ></table>
    	 	</div>
    	</div>
 	</div>  
</body>
</html>