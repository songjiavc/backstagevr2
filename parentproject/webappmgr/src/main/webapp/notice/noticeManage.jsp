<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>应用公告管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
   
   <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    
      <script src="<%=request.getContextPath() %>/notice/js/noticeManage.js" type="text/javascript"></script>
      <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.core-3.5.js" type="text/javascript"></script>	
    <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加应用公告',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	
  	    	addAppNotice();
  	    	
  	    	
  	    	
  	    }
  	}/*  ,
  	{
	    text:'批量发布应用公告',
	    iconCls:'icon-add',
	    handler:function(){
	    	publishNoticeList('');
  	    	
	    }
	} */ ];
  	  
  	
		
	</script>
		<style type="text/css">
			.ztree li button.switch {visibility:hidden; width:1px;}
			.ztree li button.switch.roots_docu {visibility:visible; width:16px;}
			.ztree li button.switch.center_docu {visibility:visible; width:16px;}
			.ztree li button.switch.bottom_docu {visibility:visible; width:16px;}
			
			 .ftitle{
	  			width:50%;
	  			float : left;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑',
	  		}
	  		 .ftitlenot{
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
	  		.ftitlenot label{
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
	  		.ftitlenot .commonInput{
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
	  		
	  		ul li{
    			text-align:left;/*设置附件展示列表样式*/
    		}
		</style>
	
	 
</head>
<body class="easyui-layout">



	<!-- 模糊查询 -->
	<div   data-options="region:'north'" style="height:90px;border:1px solid #95b8e7; background-color:white;">
	    	<table style="border: none;height: 80px;">
		    	<tr>
		    		<td width="7%" class="td_font">应用公告名称：</td>
		    		<td width="15%">
		    			<input id="noticeNameC" class="input_border"  type="text" name="noticeNameC"  />  
		    		</td>
		    		
		    		<td width="7%" class="td_font">有效开始时间：</td>
		    		<td width="15%">
		    			<input class="easyui-datebox commonInput" type="text" id="startTimeC" name="startTimeC" data-options="editable:false" >
		    		</td>
		    		<td width="7%" class="td_font">有效结束时间：</td>
		    		<td width="15%">
		    			<input class="easyui-datebox commonInput" type="text" id="endTimeC" name="endTimeC" data-options="editable:false" >
		    		</td>
		    		
		    	</tr>
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
		    		<td width="7%" class="td_font">公告状态：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox" id="noticeStatusC" name="noticeStatusC" style="width:100px;">
								<option value="">全部</option>
								<option value="0" >保存</option>
								<option value="1">发布</option>
						</select>
		    		</td>
		    		<!-- <td width="7%" class="td_font">省：</td>
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
		    		</td> -->
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" id="search" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>
		<!-- 占位用的，没有实际意义s -->
		<div id="hiddenNotice" class="easyui-dialog" fit="true" title="应用公告信息" style="width:800px;height:600px;padding:0px;border:0;top:1px;">
			 <table id="hiddendatagrid" class="easyui-datagrid"  title="测试列表" ></table>
		</div>
    	
 		 <!-- 占位用的，没有实际意义e -->
  
     <!-- 修改应用弹框 -->
     <div id="updateNotice" class="easyui-dialog"  fit="true" title="修改应用公告信息" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateNotice('0');
                    }
                },{
                    text:'发布',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateNotice('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateNotice').dialog('close');
                    }
                }]
            ">
	    <div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:25%;" title="应用公告基本内容" hide="false">
	    	 		<form id="ffUpdate" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeU">公告名称:</label>
				            <input type="hidden" name="id" id="idU"/>
				            <input type="hidden" name="appCategory" id="appCategoryhiddenU"/>
				            <input class="easyui-validatebox commonInput" type="text" id="appNoticeNameU" name="appNoticeName" style="width:200px"  
				             data-options="required:true" validType="length[1,15]" invalidMessage="名称字数最多可输入15个字"   missingMessage="公告名称不可以为空" ></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">有效开始时间:</label>
				            <div style="float:left;margin-left:30px;">
						            <input class="easyui-datebox commonInput" type="text" id="startTimeU" name="startTime" data-options="required:true,editable:false" 
						           ></input>
					          </div>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">有效结束时间:</label>
				            <div style="float:left;margin-left:30px;">
					            <input class="easyui-datebox commonInput" type="text" id="endTimeU" validType="md['#startTimeU']" name="endTime" data-options="required:true,editable:false" 
					             ></input>
					         </div>
				        </div>
				        <div class="ftitle">
				            <label for="lotteryTypeU">彩种分类:</label>
				             <div id="lUI" style="float : left;margin-left:30px;">
				             		<input id="luiLName"  type="text" style="width:200px;" readonly="readonly">
				             </div>
				             <div id="lUS" style="float : left;margin-left:30px;">
				             	 <select class="easyui-combobox " id="lotteryTypeU" name="lotteryType"  
					          	  data-options="editable:false" style="width:200px;" >
					          	  <option value="1" >体彩</option>
					          	  <option value="2" >福彩</option>
								</select>
				             </div>
				           
				        </div>
				         
				        
				         <div id="appCatoryDivU" class="ftitlenot">
				            <label for="appCategoryU">公告类型:</label>
				             <div style="float : left;margin-left:30px;">
				             	 <select class="easyui-combobox " id="appCategoryU"  data-options="editable:false" style="width:200px;" >
						          	  <option value="2" >普通公告</option>
						          	  <!-- <option value="3" >开奖公告</option> -->
						          	  <option value="4" >预测信息公告</option>
								</select>
				             </div>
				           
				        </div>
				        <div class="ftitlenot">
				            <label for="priceA">公告内容:</label>
				            <textarea id="appNoticeWordU" name="appNoticeWord" class="easyui-validatebox" data-options="required:true" missingMessage="公告内容不可以为空"	
				         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
				        </div>
				       
				        
				      </form>
	    	 	</div>
	    	 	<div region="center" style="height:75%;padding:0;" >
	    	 		<div style="width:100%;min-height:30%;">
	    	 			<table id="appDataGridU" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布应用"></table>
	    	 		</div>
	    	 		<div id="txzDivU" style="width:100%;height:30%;">
	    	 			<table id="stationDataGridU" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布公告的通行证组" ></table>
	    	 		</div>
	    	 		<div id="forecastDivU" style="width:100%;height:30%;">
	    	 			<table id="forcastDataGridU" class="easyui-datagrid" style="width:100%;height:100%;" title="选择预测信息" ></table>
	    	 		</div>
	    	 		<div style="width:100%;height:10%;" id="areaDivU">
	    	 			<label for="areaDataGridU">选择发布的区域:</label>
	    	 			<ul id="areaDataGridU" class="ztree"></ul>
	    	 		</div>
	    	 		
	    	 		
	    	 	</div>
    		</div>
    </div>
    
     <!-- 查看应用公告详情 -->
     <div id="detailNotice" class="easyui-dialog"  fit="true" title="查看应用公告详情" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'关闭',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#detailNotice').dialog('close');
                    }
                }]
            ">
	    <div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="center" style="height:100%;" title="应用公告基本内容" hide="false">
	    	 		<form id="ffDetail" method="get" novalidate style="margin-top:5px;height:25%;">
		    	 		<div class="ftitle">
				            <label for="codeU">公告名称:</label>
				            <input type="hidden" name="id" id="idD"/>
				            <input type="hidden" name="appCategory" id="appCategoryhiddenD"/>
				            <input class="easyui-validatebox commonInput" type="text" id="appNoticeNameD" name="appNoticeName" style="width:200px"  
				             readonly="readonly"></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">有效开始时间:</label>
				            <div style="float:left;margin-left:30px;">
						            <input class="easyui-datebox commonInput" type="text" id="startTimeD" name="startTime"readonly="readonly"
						           ></input>
					          </div>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">有效结束时间:</label>
				            <div style="float:left;margin-left:30px;">
					            <input class="easyui-datebox commonInput" type="text" id="endTimeD" readonly="readonly" name="endTime" 
					             ></input>
					         </div>
				        </div>
				        <div class="ftitle">
				            <label for="lotteryTypeD">彩种分类:</label>
				             <div id="lDI" style="float : left;margin-left:30px;">
				          		    <input type="hidden" name="lotteryType" id="lotteryTypeD"/>
				             		<input id="ldiLName"  type="text" style="width:200px;" readonly="readonly">
				             </div>
				           
				        </div>
				         
				        
				         <div id="appCatoryDivD" class="ftitlenot">
				            <label for="appCategoryD">公告类型:</label>
				             <div style="float : left;margin-left:30px;">
				             <input id="appCategoryD"  type="text" style="width:200px;" readonly="readonly">
				             </div>
				           
				        </div>
				        <div class="ftitlenot">
				            <label for="priceA">公告内容:</label>
				            <textarea id="appNoticeWordD" name="appNoticeWord" class="easyui-validatebox" 	
				         	 readonly="readonly" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
				        </div>
				       
				        
				      </form>
	    	 	<!-- </div>
	    	 	<div id="downDDiv" region="center" style="height:75%;padding:0;width:95%;" > -->
	    	 		<div style="width:100%;height:25%;">
	    	 			<table id="appDataGridD" class="easyui-datagrid" style="width:100%;height:100%;" title="发布应用"></table>
	    	 		</div>
	    	 		 <div id="txzDivD" style="height:25%;">
	    	 			<table id="stationDataGridD" class="easyui-datagrid" style="width:100%;height:100%;" title="发布公告的通行证组" ></table>
	    	 		</div>
	    	 		<div id="forecastDivD" style="height:25%;">
	    	 			<table id="forcastDataGridD" class="easyui-datagrid" style="width:100%;height:100%;" title="预测信息" ></table>
	    	 		</div>
	    	 		<div style="width:100%;height:10%;" id="areaDivD">
	    	 			<label for="areaDataGridD">选择发布的区域:</label>
	    	 			<ul id="areaDataGridD" class="ztree"></ul>
	    	 		</div>
	    	 		
	    	 		
	    	 	</div>
    		</div>
    </div> 
    
      <!-- 添加应用公告弹框 -->
  <div id="addNotice" class="easyui-dialog"  fit="true" title="添加应用公告" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddNotice('0');
                    }
                },{
                    text:'发布',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddNotice('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addNotice').dialog('close');
                        $('#ff').form('clear');//清空表单内容
                        clearLists();
                        $('#lotteryTypeA').combobox('setValue','1');
                        $('#appCategoryA').combobox('select','2');
                    }
                }]
            ">
		
			<div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:25%;" title="应用公告基本内容" hide="false" >
	    	 		<form id="ff" method="get"  style="margin-top:5px;" ><!-- enctype="multipart/form-data":上传文件需要,这是提交媒体文件的声明 -->
		    	 		<div class="ftitle">
				            <label for="codeA">公告名称:</label>
				            <input type="hidden" name="id" id="idA"/>
				            <input type="hidden" name="appCategory" id="appCategoryhiddenA"/>
				            <input class="easyui-validatebox commonInput" type="text" id="appNoticeNameA" name="appNoticeName" style="width:200px"  
				             data-options="required:true" validType="length[1,15]" invalidMessage="名称字数最多可输入15个字"   missingMessage="公告名称不可以为空" ></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">有效开始时间:</label>
				            <div style="float:left;margin-left:30px;">
						            <input class="easyui-datebox commonInput" type="text" id="startTimeA" name="startTime" data-options="required:true,editable:false" 
						           ></input>
					          </div>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">有效结束时间:</label>
				            <div style="float:left;margin-left:30px;">
					            <input class="easyui-datebox commonInput" type="text" id="endTimeA" validType="md['#startTimeA']" name="endTime" data-options="required:true,editable:false" 
					             ></input>
					         </div>
				        </div>
				       <div class="ftitle" id="lAI">
				            <label for="lotteryTypeA">彩种分类:</label>
				            <div  style="float : left;margin-left:30px;">
				             		<input id="laiLName"  type="text" style="width:200px;" readonly="readonly">
				             </div>
				             
				           
				        </div>
				         <div class="ftitle" id="lA">
				        	 <label for="lotteryTypeA">彩种分类:</label>
				         	<div  style="float:left;margin-left:30px;">
				             	 <select class="easyui-combobox " id="lotteryTypeA" name="lotteryType"  data-options="editable:false" style="width:200px;" >
						          	  <option value="1">体彩</option>
						          	  <option value="2">福彩</option>
								</select>
				             </div>
				         </div>
				         
				         <div id="appCatoryDivA" class="ftitlenot">
				            <label for="appCategoryA">公告类型:</label>
				             <div style="float : left;margin-left:30px;">
				             	 <select class="easyui-combobox " id="appCategoryA" data-options="editable:false" style="width:200px;" >
						          	  <option value="2" >普通公告</option>
						          	  <!-- <option value="3" >开奖公告</option> -->
						          	  <option value="4" >预测信息公告</option>
								</select>
				             </div>
				           
				        </div>
				        
				        <div class="ftitlenot">
				            <label for="priceA">公告内容:</label>
				            <textarea id="appNoticeWordA" name="appNoticeWord" class="easyui-validatebox" data-options="required:true" missingMessage="公告内容不可以为空"
				         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
				        </div>
				       
				        
				      </form>
	    	 	</div>
	    	 	<div region="center" style="height:75%;padding:0;" >
	    	 		<div style="width:100%;min-height:30%;">
	    	 			<table id="appDataGridA" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布应用"></table>
	    	 		</div>
	    	 		<div id="txzDivA" style="width:100%;height:30%;">
	    	 			<table id="stationDataGridA" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布公告的通行证组" ></table>
	    	 		</div>
	    	 		<div id="forecastDivA" style="width:100%;height:30%;">
	    	 			<table id="forcastDataGridA" class="easyui-datagrid" style="width:100%;height:100%;" title="选择预测信息" ></table>
	    	 		</div>
	    	 		<div style="width:100%;height:10%;" id="areaDivA">
	    	 			<label for="areaDataGridA">选择发布的区域:</label>
	    	 			<ul id="areaDataGridA" class="ztree"></ul>
	    	 		</div>
	    	 		
	    	 		
	    	 	</div>
    		</div>
			
    		
	     
    </div>
    
    <div  data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="应用公告列表" >
			</table>
 	</div>  
    
   
   
</body>
 	
	
</html>