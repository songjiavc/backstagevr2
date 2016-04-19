<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>市中心通行证应用广告管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/stationAppAdManage/js/stationAppAdManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	   /*  text:'批量删除订单',
  	    iconCls:'icon-remove',
  	    handler:function(){
  	    	deleteOrdersList();
  	    	} */
  	}];
  	  
  	
		
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
	  			font-family:'微软雅黑';
	  		}
	  		
	  		.ftable{
	  			width:100%;
	  			float : left;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑';
	  		}
	  		
	  		.ftitle label{
	  			float : left;
	  			margin-left: 30px;
	  			width:75px;
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
<body  class="easyui-layout">
	<!-- 模糊查询 -->
	<div   data-options="region:'north'" style="height:90px;border:1px solid #95b8e7;background-color:white;">
	    	<table style="border: none;height: 80px;">
		    	<tr>
		    		<td width="7%" class="td_font">广告名称：</td>
		    		<td width="15%">
		    			<input id="orderNameC" class="input_border"  type="text" name="orderNameC"  />  
		    		</td>
		    		<!-- <td width="7%" class="td_font">商品编码：</td>
		    		<td width="15%">
		    			<input type="text" class="input_border"  name="goodscodeC" id="goodscodeC" >
		    		</td> -->
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div  data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="通行证应用广告列表" >
			</table>
 	</div>  
  
  
  
     <!-- 修改订单弹框 -->
    <!--  <div id="updateOrders" class="easyui-dialog" title="修改订单信息" style="width:500px;height:450px;padding:10px;top:40px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateOrders('0');
                    }
                },{
                    text:'保存并提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateOrders('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateOrders').dialog('close');
                        clearGoodsArray();
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeU">订单编码:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <input class="easyui-validatebox commonInput" readonly="readonly" type="text" id="codeU" name="code" data-options="required:true"
	              ></input>
	        </div>
	        <div class="ftitle">
	            <label for="creatorU">订单创建人:</label>读取创建订单的代理人姓名
	            <input class="easyui-validatebox commonInput" type="text" id="creatorU" name="creator" readonly="readonly"
	               ></input>
	        </div>
	        <div class="ftitle">
	            <label for="nameU">购买的应用:</label>
	            <input type="hidden" name="appId" id="appIdU"/>
	            <input type="hidden" name="appprovince" id="appprovinceU"/>
	            <input type="hidden" name="appCity" id="appCityU"/>
	            <input class="easyui-validatebox commonInput" type="text" id="appNameU" name="appName" readonly="readonly"
	             ></input>
	        </div>
	        <div class="ftitle">
	            <label for="stationA">通行证编码:</label>
	             <div style="float:left;margin-left: 30px;">
	             <input type="hidden" name="station" id="stationIdU"/>
		            <input class="easyui-textbox" type="text" id="stationU" name="stationCode" style="width:200px"  readonly="readonly"/> 	
				</div>
	        </div>
	        <div class="ftitle">
	            <label for="stationA">使用年限:</label>
	             <div style="float:left;margin-left: 30px;">
		            <select class="easyui-combobox " id="userYearU" name="userYearId"  
			          	 		 data-options="editable:false" style="width:200px;" >
			          	 		
					</select>
				</div>
	        </div>
	        <div class="ftitle">
	            <label for="priceA">成交价格(元):</label>
	            <div style="float:left;margin-left: 30px;">
	           	 	<input class="easyui-textbox" readonly="readonly" type="text" name="price" id="priceU" style="width:200px"/> 
	           	</div>
	        </div>
	      </form>
    </div> -->
    <div id="uploadShowUimgPreview" title="图片预览" class="easyui-dialog" style="width:500px; height:400px;"> </div>
    
     <div id="detailOrders" class="easyui-dialog" title="订单详情" style="width:500px;height:450px;padding:10px;top:40px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'审批通过',
                    iconCls:'icon-ok',
                    handler:function(){
                        approveStationAdsInDialog('2');
                    }
                },{
                    text:'审批驳回',
                    iconCls:'icon-ok',
                    handler:function(){
                        approveStationAdsInDialog('3');
                    }
                },{
                    text:'不通过',
                    iconCls:'icon-ok',
                    handler:function(){
                       approveStationAdsInDialog('4');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#detailOrders').dialog('close');
                    }
                }]
            ">
		<form id="ffDetail" method="get" novalidate>
	    	 <div class="ftitle">
		            <label for="codeA">广告名称:</label>
		            <input type="hidden" name="id" id="idU"/>
		            <input type="hidden" name="imgOrWord" id="imgOrWordU"/>
		            <input class="easyui-validatebox commonInput" type="text" id="appAdNameU" name="appAdName" style="width:200px"  
		             data-options="editable:false" readonly="readonly">
		        </div>
		         <div class="ftitle">
		            <label for="priceA">有效开始时间:</label>
		            <div style="float:left;margin-left:30px;">
				            <input class="easyui-datebox commonInput" type="text" id="startTimeU" name="startTime" data-options="editable:false" readonly="readonly"
				           ></input>
			          </div>
		        </div>
		        <div class="ftitle">
		            <label for="priceA">有效结束时间:</label>
		            <div style="float:left;margin-left:30px;">
			            <input class="easyui-datebox commonInput" type="text" id="endTimeU" name="endTime" data-options="editable:false" readonly="readonly"
			             ></input>
			         </div>
		        </div>
		        <!-- tabs -->
					<div class="ftitle">
						<label for="priceA">广告图片:</label>
						 	 <input type="hidden" id="appImgUrlU" name="appImgUrl"/>
					        	<div id="uploadShowU">
		                    		<ul>
		                    			
		                    		</ul>
		                    	</div>
		                    	
					</div>
		        
		      </form>
    </div>
    
     <!-- 普通用户订单详情弹框 -->
    <div id="detailPTOrders" class="easyui-dialog" title="订单详情" style="width:500px;height:450px;padding:10px;top:40px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#detailPTOrders').dialog('close');
                        clearGoodsArray();
                    }
                }]
            ">
		<form id="ffPTDetail" method="get" novalidate>
	          <div class="ftitle">
		            <label for="codeA">广告名称:</label>
		            <input type="hidden" name="id" id="idU"/>
		            <input type="hidden" name="imgOrWord" id="imgOrWordU"/>
		            <input class="easyui-validatebox commonInput" type="text" id="appAdNameU" name="appAdName" style="width:200px"  
		             data-options="editable:false" readonly="readonly">
		        </div>
		         <div class="ftitle">
		            <label for="priceA">有效开始时间:</label>
		            <div style="float:left;margin-left:30px;">
				            <input class="easyui-datebox commonInput" type="text" id="startTimeU" name="startTime" data-options="editable:false" readonly="readonly"
				           ></input>
			          </div>
		        </div>
		        <div class="ftitle">
		            <label for="priceA">有效结束时间:</label>
		            <div style="float:left;margin-left:30px;">
			            <input class="easyui-datebox commonInput" type="text" id="endTimeU" name="endTime" data-options="editable:false" readonly="readonly"
			             ></input>
			         </div>
		        </div>
		        <!-- tabs -->
					<div class="ftitle">
						<label for="priceA">广告图片:</label>
						 	 <input type="hidden" id="appImgUrlU" name="appImgUrl"/>
					        	<div id="uploadShowU">
		                    		<ul>
		                    			
		                    		</ul>
		                    	</div>
		                    	
					</div>
	      </form>
    </div>
    
</body>
	
	
</html>