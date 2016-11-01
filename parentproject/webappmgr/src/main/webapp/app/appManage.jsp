<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>应用管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/app/js/appManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加应用',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	initAppProvince('add','privinceA','');
  	    	generateCode();//生成应用编码
  	    	$("#addApp").dialog('open');
  	    	
  	    }
  	} ,{
  	    text:'批量上架',
  	    iconCls:'icon-redo',
  	    handler:function(){
  	    		updateAppStatus('1');
  	    	}
  	} ,{
  	    text:'批量下架',
  	    iconCls:'icon-undo',
  	    handler:function(){
  	    		updateAppStatus('2');
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
		    		<td width="7%" class="td_font">应用名称：</td>
		    		<td width="15%">
		    			<input id="appNameC" class="input_border"  type="text" name="appNameC"  />  
		    		</td>
		    		<td width="7%" class="td_font">应用编码：</td>
		    		<td width="15%">
		    			<input type="text" class="input_border"  name="appcodeC" id="appcodeC" >
		    		</td>
		    		<td width="7%" class="td_font">彩种：</td>
		    		<td width="15%">
						<select class="easyui-combobox " id="lotterytypeC" name="lotterytypeC"  
			          	  data-options="editable:false" style="width:150px;" >
			          	  <option value="" >全部</option>
			          	  <option value="1" >体彩</option>
			          	  <option value="2" >福彩</option>
						</select>
		    		</td>
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div  data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="应用列表" >
			</table>
 	</div>  
  
  
    <!-- 添加商品弹框 -->
  <div id="addApp" class="easyui-dialog"  title="添加应用" style="width:500px;height:400px;padding:10px;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddapp();
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
	            <label for="codeA">应用编码:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <div style="float:left;margin-left: 30px;">
		            <input name="appCode" id="codehidden" type="hidden" >
		            <input class="easyui-textbox" type="text" id="codeA"  style="width:200px"  readonly="readonly" 
		               ></input>
		        </div>
	        </div>
	        <div class="ftitle">
	            <label for="nameA">应用名称:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appNameA" name="appName" data-options="required:true"
	             validType="checkAname['#appNameA','idA','privinceA']" missingMessage="应用名称不可以为空" ></input>
	        </div>
	         <div class="ftitle">
				            <label for="lotteryTypeA">彩种分类:</label>
				             <div style="float:left;margin-left:30px;">
				             	 <select class="easyui-combobox " id="lotteryTypeA" name="lotteryType"  data-options="editable:false" style="width:200px;" >
						          	  <option value="1" checked>体彩</option>
						          	  <option value="2">福彩</option>
								</select>
				             </div>
				           
			 </div>
	         <div class="ftitle">
	            <label for="nameA">应用默认单价(元):</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appMoneyA" name="appMoney" data-options="required:true"
	             validType="money" missingMessage="应用单价不可以为空" ></input>
	        </div>
	         <div class="ftitle">
	            <label for="subject">应用地域:</label>
	             <div style="float:left;margin-left:30px;">
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
	            <label for="priceA">应用开发商:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appDeveloperA" name="appDeveloper" data-options="required:true"
	            missingMessage="应用开发商不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="appStatus">应用状态:</label>
	            <div style="float:left;margin-left: 5%;">
		            <input class="easyui-validatebox" type="radio" name="appStatus"  value="1" >上架</input>
		            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appStatus" value="2">下架</input>
		            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appStatus" value="0" checked>待上架</input>
	        		<input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appStatus" value="3" >更新</input>
	        	</div>
	        </div>
	      </form>
    </div>
     <!-- 修改应用弹框 -->
     <div id="updateApp" class="easyui-dialog"  title="修改应用信息" style="width:500px;height:400px;padding:10px;top:1px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateapp();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateApp').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeA">应用编码:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <div style="float:left;margin-left: 30px;">
		            <input name="appCode" id="codehidden" type="hidden" >
		            <input class="easyui-textbox" type="text" id="codeU"  style="width:200px"  readonly="readonly" 
		               ></input>
		        </div>
	        </div>
	        <div class="ftitle">
	            <label for="nameA">应用名称:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appNameU" name="appName" data-options="required:true"
	             validType="checkAname['#appNameU','idU','privinceU']" missingMessage="应用名称不可以为空" ></input>
	        </div>
	         <div class="ftitle">
	            <label for="lotteryTypeU">彩种分类:</label>
	             <div style="float:left;margin-left:30px;">
	             	 <select class="easyui-combobox " id="lotteryTypeU" name="lotteryType"  data-options="editable:false" style="width:200px;" >
			          	  <option value="1" >体彩</option>
			          	  <option value="2">福彩</option>
					</select>
	             </div>
	           
	        </div>
	         <div class="ftitle">
	            <label for="nameA">应用默认单价(元):</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appMoneyU" name="appMoney" data-options="required:true"
	             validType="money" missingMessage="应用单价不可以为空" ></input>
	        </div>
	         <div class="ftitle">
	            <label for="subject">应用地域:</label>
	             <div style="float:left;margin-left:30px;">
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
	            <label for="priceA">应用开发商:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appDeveloperU" name="appDeveloper" data-options="required:true"
	              missingMessage="应用开发商不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="appStatus">应用状态:</label>
	            <div style="float:left;margin-left: 5%;">
		            <input class="easyui-validatebox" type="radio" name="appStatus"  value="1" >上架</input>
		            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appStatus" value="2">下架</input>
		            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appStatus" value="0" checked>待上架</input>
	        		<input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appStatus" value="3" >更新</input>
	        	</div>
	        </div>
	      </form>
    </div>
</body>
	
	
</html>