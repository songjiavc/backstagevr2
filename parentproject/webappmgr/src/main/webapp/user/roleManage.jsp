<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>角色管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/user/js/roleManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	 //初始化上级权限下拉框值
  	    	getParentRole('add','','');
  	    	$("#addRole").dialog('open');
  	    	
  	    }
  	},{
  	    text:'删除',
  	    iconCls:'icon-remove',
  	    handler:function(){
  	    	deleteRoleList();
  	    	}
  	}];
  	  
  	
		
	</script>
		<style type="text/css">
			.ztree li button.switch {visibility:hidden; width:1px;}
			.ztree li button.switch.roots_docu {visibility:visible; width:16px;}
			.ztree li button.switch.center_docu {visibility:visible; width:16px;}
			.ztree li button.switch.bottom_docu {visibility:visible; width:16px;}
			
			 .ftitle{
	  			width:100%;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑',
	  		}
	  		.ftitle label{
	  			margin-left: 30px;
	  		}
	  		.ftitle .commonInput{
	  			margin-right: 50px;
	  			float : right;
	  			width: 200px;
	  			border-radius : 5px;
	  		}
		</style>
		
	 
</head>
<body>

   	 <table id="datagrid"  title="角色列表" class="easyui-datagrid">
		</table>
  
  
  <!-- 权限设置弹框 -->
  <div id="w" class="easyui-dialog" title="权限设置" style="width:400px;height:300px;padding:10px"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'确认',
                    iconCls:'icon-ok',
                    handler:function(){
                        roleManage();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-ok',
                    handler:function(){
                        $('#w').dialog('close');
                    }
                }]
            ">
        <div class="zTreeDemoBackground left" style="float:left;width: 200px;">
        	<input type="hidden" id="ztreeId"/>
			<ul id="treeDemo" class="ztree"></ul>
		</div>
    </div>
    
    <!-- 添加角色弹框 -->
  <div id="addRole" class="easyui-dialog"  title="添加角色" style="width:400px;height:300px;padding:10px"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddrole();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-ok',
                    handler:function(){
                        $('#addRole').dialog('close');
                        $('#parentRoleAname').val('');
                    }
                }]
            ">
		<form id="ff" method="get" novalidate>
	        <div class="ftitle">
	            <label for="name">角色编码:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <input class="easyui-validatebox commonInput" type="text" id="codeA" name="code" data-options="required:true"
	             validType="checkCodes['#codeA','idA']" missingMessage="角色编码不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="email">角色名称:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="nameA" name="name" data-options="required:true"
	             validType="checkAname['#nameA','idA']" missingMessage="角色名称不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="subject">角色上级:</label>
	            <div style="margin-right: 15%;float:right;">
	            	<input type="hidden" id="parentRoleAname" name="parentRolename"/>
		            <select class="easyui-combobox" id="parentRoleA" name="parentRole"  
		          	  data-options="editable:false" style="width:200px;" >
					</select>
	            </div>
	        </div>
	        <!--  <div class="ftitle">
	            <label for="isVisible">是否启用:</label>
	            <div style="float:right;margin-right: 40%;">
		            <input class="easyui-validatebox" type="radio" name="status"  value="1" checked>是</input>
		            <input class="easyui-validatebox" style="margin-left:10px;" type="radio" name="status" value="0">否</input>
		        </div>
	        </div> -->
	      </form>
    </div>
     <!-- 修改角色弹框 -->
     <div id="updateRole" class="easyui-dialog"  title="修改角色" style="width:400px;height:300px;padding:10px"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdaterole();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-ok',
                    handler:function(){
                        $('#updateRole').dialog('close');
                        $('#parentRoleUname').val('');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	         <div class="ftitle">
	            <label for="name">角色编码:</label>
	             <input type="hidden" name="id" id="idU"/>
	            <input class="easyui-validatebox commonInput" type="text" id="codeU" name="code" data-options="required:true"
	             validType="checkCodes['#codeU','idU']" missingMessage="角色编码不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="email">角色名称:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="nameU" name="name" data-options="required:true"
	             validType="checkAname['#nameU','idU']" missingMessage="角色名称不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="subject">角色上级:</label>
	            <div style="margin-right: 15%;float:right;">
	         	   <input type="hidden" id="parentRoleUname" name="parentRolename"/>
		            <select class="easyui-combobox" id="parentRoleU" name="parentRole"  
		          	  data-options="editable:false" style="width:200px;" >
					</select>
	            </div>
	        </div>
	         <!-- <div class="ftitle">
	            <label for="isVisible">是否启用:</label>
	            <div style="float:right;margin-right: 40%;">
		             <input class="easyui-validatebox" type="radio" name="status"  value="1" checked>是</input>
		            <input class="easyui-validatebox" style="margin-left:10px;" type="radio" name="status" value="0">否</input>
		        </div>
	        </div> -->
	      </form>
    </div>
</body>
	
 <%-- <script src="<%=request.getContextPath() %>/js/ztree/jquery-1.4.4.min.js" type="text/javascript"></script> --%>
    <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.core-3.5.js" type="text/javascript"></script>	
    <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>	
	
</html>