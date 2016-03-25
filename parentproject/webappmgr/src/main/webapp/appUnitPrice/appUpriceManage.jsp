<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>应用区域单价管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/appUnitPrice/js/appUpriceManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加应用区域单价',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	initAppList('appIdA','','',false);//初始化上级应用数据下拉列表
  	    	$("#addAppUnitPrice").dialog('open');
  	    	
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
		    		<td width="7%" class="td_font">应用：</td>
		    		<td width="15%">
			    		<select class="easyui-combobox " id="appC" name="appC"  
					          	  data-options="editable:false" style="width:200px;" >
						</select>
		    		</td>
		    		<td width="7%" class="td_font">单价区域：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox " id="cityC" name="cityC"  
			          	  data-options="editable:false" style="width:100px;" >
						</select>
		    		</td>
		    		
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div id="main-layout" data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="应用区域单价列表" >
			</table>
 	</div>  
  
  
    <!-- 添加应用区域单价弹框 -->
  <div id="addAppUnitPrice" class="easyui-dialog" title="添加应用区域单价" style="width:500px;height:300px;padding:10px;top:40px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddappUnitPrice();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addAppUnitPrice').dialog('close');
                        $('#ff').form('clear');//清空表单内容
                    }
                }]
            ">
		<form id="ff" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeA">应用:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <div style="float:left;margin-left: 5%;">
		            <select class="easyui-combobox " id="appIdA" name="appId"  
				          	  data-options="editable:false" style="width:200px;" >
					</select>
				</div>
	        </div>
	        
	        <div class="ftitle">
	            <label for="subject">单价应用区域:</label>
	             <div style="float:left;margin-left:30px;">
	           		<!-- <label for="privinceA">省:</label> -->
	           		 <input class="easyui-textbox" type="text" id="privinceA" readonly="readonly"/>
	            		<input type="hidden" id="provinceAhidden" name="province"/>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="cityA" name="city"  
		          	  data-options="editable:false" style="width:100px;" >
					</select>
	            </div>
	        </div>
	        
	         <div class="ftitle">
	            <label for="nameA">区域单价(元):</label>
	            <input class="easyui-validatebox commonInput" type="text" id="unitPriceA" name="unitPrice" data-options="required:true"
	             validType="money" missingMessage="区域单价不可以为空" ></input>
	        </div>
	         
	      </form>
    </div>
     <!-- 修改应用区域单价弹框 -->
     <div id="updateAppUnitPrice" class="easyui-dialog" title="修改应用区域单价信息" style="width:500px;height:300px;padding:10px;top:40px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateappUnitPrice();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateAppUnitPrice').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	         <div class="ftitle">
	            <label for="codeA">应用:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <div style="float:left;margin-left: 5%;">
		            <!-- <select class="easyui-combobox " id="appIdU" name="appId"  
				          	  data-options="editable:false" style="width:200px;" >
					</select> -->
					<input class="easyui-textbox" type="text" id="appIdU" readonly="readonly"/>
	            	<input type="hidden" id="appIdUhidden" name="appId"/>
				</div>
	        </div>
	        
	        <div class="ftitle">
	            <label for="subject">单价应用区域:</label>
	             <div style="float:left;margin-left:30px;">
		           <input class="easyui-textbox" type="text" id="privinceU" readonly="readonly"/>
	            	<input type="hidden" id="provinceUhidden" name="province"/>
	            	
					<select class="easyui-combobox " id="cityU" name="city"  
		          	  data-options="editable:false" style="width:100px;" >
					</select>
	            </div>
	        </div>
	        
	         <div class="ftitle">
	            <label for="nameA">区域单价(元):</label>
	            <input class="easyui-validatebox commonInput" type="text" id="unitPriceU" name="unitPrice" data-options="required:true"
	             validType="money" missingMessage="区域单价不可以为空" ></input>
	        </div>
	      </form>
    </div>
</body>
	
	
</html>