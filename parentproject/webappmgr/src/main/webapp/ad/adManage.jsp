<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>应用广告管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/ad/js/adManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加应用广告',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	
  	    	addAppad();
  	    	
  	    }
  	}/* ,
  	{
	    text:'批量发布应用广告',
	    iconCls:'icon-add',
	    handler:function(){
	    	publishAdList('');
  	    	
	    }
	} */ ];
  	  
  	
		
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
	  		
	  		ul li{
    			text-align:left;/*设置附件展示列表样式*/
    		}
    		
    		a{
    			cursor:pointer;/*超链接悬停样式*/
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
		    		<td width="7%" class="td_font">应用广告名称：</td>
		    		<td width="15%">
		    			<input id="adNameC" class="input_border"  type="text" name="adNameC"  />  
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
		    		<td width="7%" class="td_font">广告展示形式：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox" id="imgOrwordC" name="imgOrwordC" style="width:100px;">
								<option value="">全部</option>
								<option value="0" >图片</option>
								<option value="1">文字</option>
						</select>
		    		</td>
		    		<td width="7%" class="td_font">广告状态：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox" id="adStatusC" name="adStatusC" style="width:100px;">
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

    <div  data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="应用广告列表" >
			</table>
 	</div>  
  
  
    <!-- 添加应用广告弹框 --> 
  <div id="addAd" class="easyui-dialog" fit="true" title="添加应用广告" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
                modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddAd('0');
                    }
                },{
                    text:'发布',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddAd('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        addDialogCancel();
                    }
                }]
            ">
		
			<div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:40%;" title="应用广告基本内容" hide="false" >
	    	 		<form id="ff" method="get"  style="margin-top:5px;" ><!-- enctype="multipart/form-data":上传文件需要,这是提交媒体文件的声明 -->
		    	 		<div class="ftitle">
				            <label for="codeA">广告名称:</label>
				            <input type="hidden" name="id" id="idA"/>
				            <input type="hidden" name="imgOrWord" id="imgOrWordA"/>
				            <input class="easyui-validatebox commonInput" type="text" id="appAdNameA" name="appAdName" style="width:200px"  
				             data-options="required:true" validType="length[1,15]" invalidMessage="名称字数最多可输入15个字"    missingMessage="广告名称不可以为空" ></input>
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
				        <!-- tabs -->
				        <div id="ttA" class="easyui-tabs" style="width:100%;height:150px;">
							<div title="图片广告" style="padding:10px;">
								  <div class="ftitle">
							            <label for="nameA">上传广告图片:</label>
							            <input type="hidden" id="appImgUrlA" name="appImgUrl"/>
							            <a href="#" id="uploadA" class="l-btn l-btn-small" plain="true" onclick="openDialog('ddA','add')" style="width:100px;">点击上传广告图片</a>
							       		<div id="uploadShowA">
				                    		<ul>
				                    			
				                    		</ul>
				                    	</div>
				                    	 
							      </div>
							</div>
							<div title="文字广告"  style="padding:10px;"><!-- closable="true"设置当前tab可以被关闭 -->
								 <div class="ftitle">
						            <label for="priceA">广告内容:</label>
						            <textarea id="addWordA" name="addWord" class="easyui-validatebox" validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
						        </div>
							</div>
						</div>
				        
				      </form>
	    	 	</div>
	    	 	<div region="center" style="height:60%;padding:0;" >
		    	 	<div style="width:100%;min-height:40%;">
		    	 			<table id="appDataGridA" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布应用"></table>
		    	 	</div>
	    	 		<div id="txzDivA" style="width:100%;height:40%;">
	    	 			<table id="stationDataGridA" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布广告的通行证组" ></table>
	    	 		</div>
	    	 		<div style="width:100%;height:20%;" id="areaDivA">
	    	 			<label for="areaDataGridA">选择发布的区域:</label>
	    	 			<ul id="areaDataGridA" class="ztree"></ul>
	    	 		</div>
	    	 		
	    	 		
	    	 	</div>
    		</div>
			
    		
    </div>
    
    <!-- 图片预览弹框 -->
    <div id="uploadShowAimgPreview" title="图片预览" class="easyui-dialog" data-options="modal:true"  style="width:500px; height:400px;"> </div>
    <div id="uploadShowUimgPreview" title="图片预览" class="easyui-dialog" data-options="modal:true"  style="width:500px; height:400px;"> </div>
    <div id="uploadShowDimgPreview" title="图片预览" class="easyui-dialog" data-options="modal:true"  style="width:500px; height:400px;"> </div>
    
     <!-- 修改应用弹框 -->
     <div id="updateAd" class="easyui-dialog" fit="true"  title="修改应用广告信息" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateAd('0');
                    }
                },{
                    text:'发布',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateAd('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateAd').dialog('close');
                    }
                }]
            ">
	    <div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:40%;" title="应用广告基本内容" hide="false">
	    	 		<form id="ffUpdate" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeA">广告名称:</label>
				            <input type="hidden" name="id" id="idU"/>
				            <input type="hidden" name="imgOrWord" id="imgOrWordU"/>
				            <input class="easyui-validatebox commonInput" type="text" id="appAdNameU" name="appAdName" style="width:200px"  
				             data-options="required:true" validType="length[1,15]" invalidMessage="名称字数最多可输入15个字"    missingMessage="广告名称不可以为空" ></input>
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
				        <!-- tabs -->
				        <div id="ttU" class="easyui-tabs" style="width:100%;height:150px;">
							<div title="图片广告" style="padding:10px;">
								  <div class="ftitle">
							            <label for="appImgUrl">上传广告图片:</label>
							            <input type="hidden" id="appImgUrlU" name="appImgUrl"/>
							            <a href="#" id="uploadU" class="l-btn l-btn-small" plain="true" onclick="openDialog('ddA','update')" style="width:100px;">点击上传广告图片</a>
							        	<div id="uploadShowU">
				                    		<ul>
				                    			
				                    		</ul>
				                    	</div>
				                    	
							        </div>
							</div>
							<div title="文字广告" style="padding:10px;">
								 <div class="ftitle">
						            <label for="priceA">广告内容:</label>
						            <textarea id="addWordU" name="addWord" class="easyui-validatebox" 
						         	 validType="length[0,100]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
						        </div>
							</div>
						</div>
				        
				      </form>
	    	 	</div>
	    	 	<div region="center" style="height:60%;padding:0;" >
	    	 		<div style="width:100%;min-height:40%;">
	    	 			<table id="appDataGridU" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布应用"></table>
	    	 		</div>
	    	 		<div id="txzDivU" style="width:100%;height:40%;">
	    	 			<table id="stationDataGridU" class="easyui-datagrid" style="width:100%;height:100%;" title="选择发布广告的通行证组" ></table>
	    	 		</div>
	    	 		<div style="width:100%;min-height:20;" id="areaDivU">
	    	 			<label for="areaDataGridU">选择发布的区域:</label>
	    	 			<ul id="areaDataGridU" class="ztree"></ul>
	    	 		</div>
	    	 		
	    	 		
	    	 	</div>
    		</div>
    </div>
    
    <!-- 查看应用广告弹框详情 -->
     <div id="detailAd" class="easyui-dialog"  fit="true" title="查看应用广告信息详情" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'关闭',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#detailAd').dialog('close');
                    }
                }]
            ">
	    <div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:40%;" title="应用广告基本内容" hide="false">
	    	 		<form id="ffDetail" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeA">广告名称:</label>
				            <input type="hidden" name="id" id="idD"/>
				            <input type="hidden" name="imgOrWord" id="imgOrWordD"/>
				            <input class="easyui-validatebox commonInput" type="text" id="appAdNameD" name="appAdName" style="width:200px"  
				             readonly="readonly" ></input>
				        </div>
				         <div class="ftitle">
				            <label for="priceA">有效开始时间:</label>
				            <div style="float:left;margin-left:30px;">
						            <input class="easyui-datebox commonInput" type="text" id="startTimeD" name="startTime" readonly="readonly"
						           ></input>
					          </div>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">有效结束时间:</label>
				            <div style="float:left;margin-left:30px;">
					            <input class="easyui-datebox commonInput" type="text" id="endTimeD"  name="endTime" readonly="readonly"
					             ></input>
					         </div>
				        </div>
				        <!-- tabs -->
				        <div id="ttD" class="easyui-tabs" style="width:100%;height:150px;">
							<div title="图片广告" style="padding:10px;">
								  <div class="ftitle">
							            <label for="appImgUrl">广告图片:</label>
							            <input type="hidden" id="appImgUrlD" name="appImgUrl"/>
							        	<div id="uploadShowD">
				                    		<ul>
				                    			
				                    		</ul>
				                    	</div>
				                    	
							        </div>
							</div>
							<div title="文字广告" style="padding:10px;">
								 <div class="ftitle">
						            <label for="priceA">广告内容:</label>
						            <textarea id="addWordD" name="addWord" class="easyui-validatebox" 
						         	 readonly="readonly" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
						        </div>
							</div>
						</div>
				        
				      </form>
	    	 	</div>
	    	 	<div region="center" style="height:60%;padding:0;" >
	    	 		<div style="width:100%;min-height:40%;">
	    	 			<table id="appDataGridD" class="easyui-datagrid" style="width:100%;height:100%;" title="发布应用"></table>
	    	 		</div>
	    	 		<div id="txzDivD" style="width:100%;height:40%;">
	    	 			<table id="stationDataGridD" class="easyui-datagrid" style="width:100%;height:100%;" title="发布广告的通行证组" ></table>
	    	 		</div>
	    	 		<div style="width:100%;min-height:20%;" id="areaDivD">
	    	 			<label for="areaDataGridD">发布的区域:</label>
	    	 			<ul id="areaDataGridD" class="ztree"></ul>
	    	 		</div>
	    	 		
	    	 		
	    	 	</div>
    		</div>
    </div>
    
    <!-- 自定义工具栏 -->
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
    
    <div id="ddA">Dialog Content.</div>

</body>
	  <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.core-3.5.js" type="text/javascript"></script>	
    <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>
	
</html>