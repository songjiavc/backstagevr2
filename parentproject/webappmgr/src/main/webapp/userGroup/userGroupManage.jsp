<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>通行证组管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/userGroup/js/userGroupManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加通行证组',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	clearstationList();
  	    	$("#searchFormStyleA").combobox('setValue','');
  	    	initProvince('searchFormProvinceA');
  	    	initStationList('','stationDataGridA');
  	    	$("#addUgroup").dialog('open');
  	    	
  	    }
  	} ];
  	  
  	
		
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
	<!-- 模糊查询 -->
	<div   data-options="region:'north'" style="height:90px;border:1px solid #95b8e7; background-color:white;">
	    	<table style="border: none;height: 80px;">
		    	<tr>
		    		<td width="7%" class="td_font">通行证组名称：</td>
		    		<td width="15%">
		    			<input id="userGroupNameC" class="input_border"  type="text" name="userGroupNameC"  />  
		    		</td>
		    		<td width="7%" class="td_font">通行证组编码：</td>
		    		<td width="15%">
		    			<input type="text" class="input_border"  name="userGroupCodeC" id="userGroupCodeC" >
		    		</td>
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div id="main-layout" data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="通行证组列表" >
			</table>
 	</div>  
  
  
    <!-- 添加通行证组弹框 -->
  <div id="addUgroup" class="easyui-dialog" title="添加通行证组" style="width:800px;height:600px;padding:0px;border:0;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddUgroup();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addUgroup').dialog('close');
                        $('#ff').form('clear');//清空表单内容
                    }
                }]
            ">
		
			<div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:45%;" title="通行证基本内容" hide="false">
	    	 		<form id="ff" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeA">通行证组编码:</label>
				            <input type="hidden" name="id" id="idA"/>
				            <input class="easyui-validatebox commonInput" type="text" id="userGroupCodeA" name="userGroupCode" style="width:200px"  
				             data-options="required:true"  validType="checkUgcode['#userGroupCodeA','idA']" missingMessage="通行证组编码不可以为空" ></input>
				        </div>
				        <div class="ftitle">
				            <label for="nameA">通行证组名称:</label>
				            <input class="easyui-validatebox commonInput" type="text" id="userGroupNameA" name="userGroupName" data-options="required:true"
				             validType="checkUgname['#userGroupNameA','idA']" missingMessage="通行证组名称不可以为空" ></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">通行证组描述:</label>
				            <textarea id="userGroupDescriptionA" name="userGroupDescription" class="easyui-validatebox" 
				         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
				        </div>
				       </form>
	    	 	</div>
	    	 	<div region="center" style="height:55%;padding:0;" title="选择通行证">
	    	 		<table id="stationDataGridA" class="easyui-datagrid" style="width:100%;height:95%;"  title="通行证列表" toolbar="#tbA"></table>
	    	 	</div>
    		</div>
			
    		
	     
    </div>
     <!-- 修改应用弹框 -->
     <div id="updateUgroup" class="easyui-dialog" title="修改应用信息" style="width:800px;height:600px;padding:0px;border:0;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateUgroup();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateUgroup').dialog('close');
                    }
                }]
            ">
	      <div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:45%;" title="通行证基本内容" hide="false">
	    	 		<form id="ffUpdate" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeA">通行证组编码:</label>
				            <input type="hidden" name="id" id="idU"/>
				            <input class="easyui-validatebox commonInput" type="text" id="userGroupCodeU" name="userGroupCode" style="width:200px"  
				             data-options="required:true"  validType="checkUgcode['#userGroupCodeU','idU']" missingMessage="通行证组编码不可以为空" ></input>
				        </div>
				        <div class="ftitle">
				            <label for="nameA">通行证组名称:</label>
				            <input class="easyui-validatebox commonInput" type="text" id="userGroupNameU" name="userGroupName" data-options="required:true"
				             validType="checkUgname['#userGroupNameU','idU']" missingMessage="通行证组名称不可以为空" ></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">通行证组描述:</label>
				            <textarea id="userGroupDescriptionU" name="userGroupDescription" class="easyui-validatebox" 
				         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
				        </div>
				       </form>
	    	 	</div>
	    	 	<div region="center" style="height:55%;padding:0;" title="选择通行证">
	    	 		<table id="stationDataGridU" class="easyui-datagrid" style="width:100%;height:95%;"  title="通行证列表" toolbar="#tbU"></table>
	    	 	</div>
    		</div>
    </div>
    
     <div id="tbA" style="padding:3px">
    	<span>省:</span>
	    	<select class="easyui-combobox " id="searchFormProvinceA" name="searchFormProvince" style="width:150px;" >
	    	</select>
    	<span>市:</span>
	    	<select class="easyui-combobox " id="searchFormCityA" name="searchFormProvince" style="width:150px;" >
	    	</select>
    	<span>站点类型:</span>
	    	<select class="easyui-combobox" id="searchFormStyleA" name="searchFormStyle" style="width:100px;">
								<option value="">全部</option>
								<option value="1" >体彩</option>
								<option value="2">福彩</option>
						</select>
    	<a href="#" class="icon-search" plain="true" onclick="dosearch(0)">查询</a>
    </div>
     <div id="tbU" style="padding:3px">
    	<span>省:</span>
	    	<select class="easyui-combobox " id="searchFormProvinceU" name="searchFormProvince" style="width:150px;" >
	    	</select>
    	<span>市:</span>
	    	<select class="easyui-combobox " id="searchFormCityU" name="searchFormProvince" style="width:150px;" >
	    	</select>
    	<span>站点类型:</span>
    			<select class="easyui-combobox" id="searchFormStyleU" name="searchFormStyle" style="width:100px;">
							<option value="">全部</option>
							<option value="1" >体彩</option>
							<option value="2">福彩</option>
				</select>
    	<a href="#" class="icon-search" plain="true" onclick="dosearch(1)">查询</a>
    </div>
</body>
	
	
</html>