<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>预测信息管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/forecast/js/forecastManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加预测信息',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	initforecastProvince('add','appAreaProvinceA','');
  	    	$("#addForecast").dialog('open');
  	    	
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
		    		<td width="7%" class="td_font">彩种：</td>
		    		<td width="15%">
						<select class="easyui-combobox " id="lotterytypeC" name="lotterytypeC"  
			          	  data-options="editable:false" style="width:150px;" >
			          	  <option value="" >全部</option>
			          	  <option value="1" >体彩</option>
			          	  <option value="2" >福彩</option>
						</select>
		    		</td>
		    		<td width="7%" class="td_font">省：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox " id="privinceC" name="privinceC"  
			          	  data-options="editable:false" style="width:150px;" >
						</select>
		    		</td>
		    		<td width="7%" class="td_font">市：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox " id="cityC" name="cityC"  
			          	  data-options="editable:false" style="width:150px;" >
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
    	 <table id="datagrid" class="easyui-datagrid"  title="预测信息列表" >
			</table>
 	</div>  
  
  
    <!-- 添加预测信息弹框 -->
  <div id="addForecast" class="easyui-dialog" data-options="modal:true" title="添加预测信息" style="width:600px;height:500px;padding:10px;top:1px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddForecast();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addForecast').dialog('close');
                        $('#ff').form('clear');//清空表单内容
                        $('#lotteryTypeA').combobox('setValue','1');
                        
                    }
                }]
            ">
		<form id="ff" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeA">预测信息名称:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <div style="float:left;margin-left: 30px;">
		            <input class="easyui-textbox " type="text" id="forecastNameA" name="forecastName"  style="width:200px"  
		               data-options="required:true" validType="length[0,20]" invalidMessage="名称字数最多可输入20个字" missingMessage="预测信息名称不可以为空"/>
		        </div>
	        </div>
	       <div class="ftitle">
	            <label for="lotteryTypeA">彩种分类:</label>
	             <div style="float : left;margin-left:30px;">
	             	 <select class="easyui-combobox " id="lotteryTypeA" name="lotteryType"  
		          	  data-options="editable:false" style="width:200px;" >
		          	  <option value="1" >体彩</option>
		          	  <option value="2" >福彩</option>
					</select>
	             </div>
	           
	        </div>
	         <div class="ftitle">
	         	 <label for="forecastContentA">预测信息内容:</label>
	         	 <!-- ※textarea两个标签间有空格时焦点会不落在首字符上※ -->
	         	 <textarea id="forecastContentA" name="forecastContent" class="easyui-validatebox" data-options="required:true" missingMessage="预测信息内容不可以为空"
	         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left:30px;"></textarea>
	         </div>
	         <div class="ftitle">
	            <label for="subject">地域:</label>
	             <div style="float:left;margin-left:30px;">
	           		<!-- <label for="privinceA">省:</label> -->
		            <select class="easyui-combobox " id="appAreaProvinceA" name="appAreaProvince"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="appAreaCityA" name="appAreaCity"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
	            </div>
	        </div>
	        <div class="ftitle">
	            <label for="priceA">有效开始时间:</label>
	            <div style="float:left;margin-left:30px;">
			            <input class="easyui-datebox commonInput" type="text" id="startTimeA" name="startTime" data-options="required:true,editable:false" 
			           />
		          </div>
	        </div>
	        <div class="ftitle">
	            <label for="priceA">有效结束时间:</label>
	            <div style="float:left;margin-left:30px;">
		            <input class="easyui-datebox commonInput" type="text" id="endTimeA" validType="md['#startTimeA']" name="endTime" data-options="required:true,editable:false" 
		             />
		         </div>
	        </div>
	      </form>
    </div>
     <!-- 修改应用弹框 -->
     <div id="updateForecast" class="easyui-dialog" data-options="modal:true" title="修改预测信息" style="width:600px;height:500px;padding:10px;top:1px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateForecast();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateForecast').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeA">预测信息名称:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <div style="float:left;margin-left: 30px;">
		            <input class="easyui-textbox " type="text" id="forecastNameU" name="forecastName"  style="width:200px"  
		               readonly="readonly"/>
		        </div>
	        </div>
	       <div class="ftitle">
	            <label for="lotteryTypeA">彩种分类:</label>
	             <div style="float : left;margin-left:30px;">
	             	 <select class="easyui-combobox " id="lotteryTypeU" name="lotteryType"  
		          	  data-options="editable:false" style="width:200px;" >
		          	  <option value="1" checked>体彩</option>
		          	  <option value="2" >福彩</option>
					</select>
	             </div>
	           
	        </div>
	         <div class="ftitle">
	         	 <label for="forecastContentA">预测信息内容:</label>
	         	 <!-- ※textarea两个标签间有空格时焦点会不落在首字符上※ -->
	         	 <textarea id="forecastContentU" name="forecastContent" class="easyui-validatebox" data-options="required:true" missingMessage="预测信息内容不可以为空"
	         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left:30px;"></textarea>
	         </div>
	         <div class="ftitle">
	            <label for="subject">地域:</label>
	             <div style="float:left;margin-left:30px;">
	           		<!-- <label for="privinceA">省:</label> -->
		            <select class="easyui-combobox " id="appAreaProvinceU" name="appAreaProvince"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="appAreaCityU" name="appAreaCity"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
	            </div>
	        </div>
	        <div class="ftitle">
	            <label for="priceA">有效开始时间:</label>
	            <div style="float:left;margin-left:30px;">
			            <input class="easyui-datebox commonInput" type="text" id="startTimeU" name="startTime" data-options="required:true,editable:false" 
			           />
		          </div>
	        </div>
	        <div class="ftitle">
	            <label for="priceA">有效结束时间:</label>
	            <div style="float:left;margin-left:30px;">
		            <input class="easyui-datebox commonInput" type="text" id="endTimeU" validType="md['#startTimeU']" name="endTime" data-options="required:true,editable:false" 
		             />
		         </div>
	        </div>
	     </form>
    </div>
</body>
	
	
</html>