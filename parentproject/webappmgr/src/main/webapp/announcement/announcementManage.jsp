<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>通告管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/announcement/js/announcementManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加通告',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	//清空数据列表
  	    	clearLists();
  	    	//加载通行证组数据
  	    	initStationGList('','stationDataGridA')
  	    	
  	    	//展示所有的区域信息，树的形式，一级节点为省，二级节点为市,（暂时不做成根据应用加载区域的效果，因为在应用是给市级使用时，不好对区域数据去重）
  	    	initAreaData('areaDataGridA');
  	    	
  	    	
  	    	$("#addAnnouncement").dialog('open');
  	    	
  	    }
  	}/* ,
  	{
  	    text:'批量发布通告',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	publishAnnouncementList('');
	  	    	
  	    }
  	} */];
  	  
  	
		
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
	
	<!-- 上传文件引入start -->	
	
	<!-- 上传文件引入end -->
	 
</head>
<body class="easyui-layout">
	<!-- 模糊查询 -->
	<div   data-options="region:'north'" style="height:90px;border:1px solid #95b8e7; background-color:white;">
	    	<table style="border: none;height: 80px;">
		    	<tr>
		    		<td width="7%" class="td_font">通告名称：</td>
		    		<td width="15%">
		    			<input id="announcementNameC" class="input_border"  type="text" name="announcementNameC"  />  
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
		    		<td width="7%" class="td_font">通告状态：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox" id="announceStatusC" name="announceStatusC" style="width:100px;">
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
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div id="main-layout" data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="通告列表" >
			</table>
 	</div>  
  
  
    <!-- 添加通告弹框 -->
  <div id="addAnnouncement" class="easyui-dialog" title="添加通告" style="width:800px;height:650px;padding:0px;border:0;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddAnnouncement('0');
                    }
                },{
                    text:'发布',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddAnnouncement('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addAnnouncement').dialog('close');
                        $('#ff').form('clear');//清空表单内容
                        clearLists();
                        $('#lotteryTypeA').combobox('setValue','1');
                    }
                }]
            ">
		
			<div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:40%;" title="通告基本内容" hide="false" >
	    	 		<form id="ff" method="get"  style="margin-top:5px;" ><!-- enctype="multipart/form-data":上传文件需要,这是提交媒体文件的声明 -->
		    	 		<div class="ftitle">
				            <label for="codeA">通告名称:</label>
				            <input type="hidden" name="id" id="idA"/>
				            <input class="easyui-validatebox commonInput" type="text" id="announcementNameA" name="announcementName" style="width:200px"  
				             data-options="required:true" validType="length[1,15]" invalidMessage="名称字数最多可输入15个字"   missingMessage="通告名称不可以为空" ></input>
				        </div>
				        <div class="ftitle">
				            <label for="lotteryTypeA">彩种分类:</label>
				             <div style="float:left;margin-left:30px;">
				             	 <select class="easyui-combobox " id="lotteryTypeA" name="lotteryType"  data-options="editable:false" style="width:200px;" >
						          	  <option value="1" >体彩</option>
						          	  <option value="2">福彩</option>
								</select>
				             </div>
				           
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
				        
				        <div class="ftitlenot">
				            <label for="priceA">通告内容:</label>
				            <textarea id="announcementContentA" name="announcementContent" class="easyui-validatebox" data-options="required:true" missingMessage="通告内容不可以为空"
				         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
				        </div>
				       
				        
				      </form>
	    	 	</div>
	    	 	<div region="center" style="height:60%;padding:0;" >
	    	 		<div id="txzDivA" style="width:100%;height:50%;">
	    	 			<table id="stationDataGridA" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布通告的通行证组" ></table>
	    	 		</div>
	    	 		<div style="width:100%;height:50%;">
	    	 			<label for="areaDataGridA">选择发布的区域:</label>
	    	 			<ul id="areaDataGridA" class="ztree"></ul>
	    	 		</div>
	    	 		
	    	 		
	    	 	</div>
    		</div>
			
    		
	     
    </div>
     <!-- 修改通告弹框 -->
     <div id="updateAnnouncement" class="easyui-dialog" title="修改通告信息" style="width:800px;height:600px;padding:0px;border:0;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateAnnouncement('0');
                    }
                },{
                    text:'发布',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateAnnouncement('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateAnnouncement').dialog('close');
                    }
                }]
            ">
	    <div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:40%;" title="通告基本内容" hide="false">
	    	 		<form id="ffUpdate" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeU">通告名称:</label>
				            <input type="hidden" name="id" id="idU"/>
				            <input class="easyui-validatebox commonInput" type="text" id="announcementNameU" name="announcementName" style="width:200px"  
				             data-options="required:true" validType="length[1,15]" invalidMessage="名称字数最多可输入15个字"    missingMessage="通告名称不可以为空" ></input>
				        </div>
				        <div class="ftitle">
				            <label for="lotteryTypeA">彩种分类:</label>
				             <div style="float:left;margin-left:30px;">
				             	 <select class="easyui-combobox " id="lotteryTypeU" name="lotteryType"  data-options="editable:false" style="width:200px;" >
						          	  <option value="1" checked>体彩</option>
						          	  <option value="2">福彩</option>
								</select>
				             </div>
				           
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
				        
				        <div class="ftitlenot">
				            <label for="priceA">通告内容:</label>
				            <textarea id="announcementContentU" name="announcementContent" class="easyui-validatebox" data-options="required:true" missingMessage="通告内容不可以为空"
				         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
				        </div>
				       
				        
				      </form>
	    	 	</div>
	    	 	<div region="center" style="height:60%;padding:0;" >
	    	 		<div id="txzDivA" style="width:100%;height:50%;">
	    	 			<table id="stationDataGridU" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布通告的通行证组" ></table>
	    	 		</div>
	    	 		<div style="width:100%;height:50%;">
	    	 			<label for="areaDataGridU">选择发布的区域:</label>
	    	 			<ul id="areaDataGridU" class="ztree"></ul>
	    	 		</div>
	    	 		
	    	 		
	    	 	</div>
    		</div>
    </div>
    
    
    

</body>
	  <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.core-3.5.js" type="text/javascript"></script>	
    <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>
	
</html>