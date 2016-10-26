<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>图谜字谜底板管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/figureAndPuzzleApp/js/floorOfFigureAndPuzlleManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加图谜字谜底板',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	addDialogOpen();
  	    	
  	    }
  	}  ];
  	  
  	
		
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
	  		
	  		.ftitle .numberInput{
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
		    		<td width="7%" class="td_font">底板名称：</td>
		    		<td width="15%">
		    			<input id="floorNameC" class="input_border"  type="text" name="floorNameC"  />  
		    		</td>
		    		<td width="7%" class="td_font">图谜/字谜：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox " id="figureOrPuzzlesC" name="figureOrPuzzlesC"  
			          	  data-options="editable:false" style="width:150px;" >
			          	  <option value="" >请选择</option>
			          	  <option value="1" >图谜</option>
			          	  <option value="2" >字谜</option>
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
    	 <table id="datagrid" class="easyui-datagrid"  title="图谜字谜底板列表" >
			</table>
 	</div>  
  
  
    <!-- 添加图谜字谜底板 -->
  <div id="addFloorOfFAPApp" class="easyui-dialog"  title="添加图谜字谜底板" style="width:600px;height:430px;padding:10px;top:10px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddFloorOfFAPApp();
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
	            <label for="codeA">底板名称:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <input class="easyui-validatebox commonInput" type="text" id="floorNameA" name="floorName"  
	                data-options="required:true" ></input>
	        </div>
	        <div class="ftitle">
		            <label for="figureOrPuzzlesA">图谜/字谜:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="figureOrPuzzlesA" name="figureOrPuzzles"  data-options="editable:false" style="width:200px;" >
				          	<!--  <option value="0" >全部</option> -->
				          	  <option value="1" >图谜</option>
				          	  <option value="2">字谜</option>
						</select>
		             </div>
				           
			 </div>
			 <div class="ftitle" id="zmA">
		            <label for="puzzlesTypeIdA">字谜类型:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="puzzlesTypeIdA" name="puzzlesTypeId"  data-options="editable:false" style="width:200px;" >
						</select>
		             </div>
				           
			 </div>
			 
			 <div class="ftitle">
				 <label for="startCoordinateA">起始坐标:</label>
	            	<input class="easyui-validatebox commonInput" type="text" id="startCoordinateA" name="startCoordinate"  
	                data-options="required:true" ></input>
	        </div>
	        
	         <div class="ftitle">
				 <label for="startCoordinateA">字体样式:</label>
	            	<input class="easyui-validatebox commonInput" type="text" id="fontCssA" name="fontCss"  
	                data-options="required:true" ></input>
	         </div>
	        
	       <div class="ftitle">
	            <label for="versionDescriptionA">底板描述:</label>
	            <textarea id="floorDescriptionA" name="floorDescription" class="easyui-validatebox" 
	         	 validType="length[0,200]" style="resize:none;width:350px;height:60px;border-radius:5px;margin-left:30px;"></textarea>
	        </div>
	        
	        <div class="ftitle">
	            <label for="appVersionUrlU">上传底板图片:</label>
	            <input type="hidden" name="floorImg" id="floorImgA"/>
	             <a href="#" id="uploadA" class="l-btn l-btn-small" style="margin-left:30px;" plain="true" onclick="openDialog('ddA','add')" style="width:200px;">点击上传底板图片</a>
	        </div>
	        
	         <div class="ftitle">
	            <label for="imgshowA">底板图片:</label>
	            <div style="float:left;margin-left:30px;">
		             <div id="imgshowA">
		            
		            </div>
	            </div>
	           
	        </div>
	        
	      </form>
    </div>
     <!-- 修改图谜字谜底板-->
     <div id="updateFloorOfFAPApp" class="easyui-dialog"  title="修改图谜字谜底板" style="width:600px;height:430px;padding:10px;top:10px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateFloorOfFAPApp();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateFloorOfFAPApp').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	        <div class="ftitle">
	            <label for="floorNameA">底板名称:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <input class="easyui-validatebox commonInput" type="text" id="floorNameU" name="floorName"  
	                data-options="required:true" ></input>
	        </div>
	        <div class="ftitle">
		            <label for="figureOrPuzzlesU">图谜/字谜:</label>
		             	 <input class="easyui-validatebox commonInput" type="text" id="figureOrPuzzlesNameU" 
	               		readonly="readonly" ></input>
				           
			 </div>
			 <div class="ftitle" id="zmU">
		            <label for="puzzlesTypeIdU">字谜类型:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="puzzlesTypeIdU" name="puzzlesTypeId"  data-options="editable:false" style="width:200px;" >
						</select>
		             </div>
				           
			 </div>
			 
			  <div class="ftitle">
				 <label for="startCoordinateU">起始坐标:</label>
	            	<input class="easyui-validatebox commonInput" type="text" id="startCoordinateU" name="startCoordinate"  
	                data-options="required:true" ></input>
	        </div>
	        
	         <div class="ftitle">
				 <label for="startCoordinateU">字体样式:</label>
	            	<input class="easyui-validatebox commonInput" type="text" id="fontCssU" name="fontCss"  
	                data-options="required:true" ></input>
	         </div>
			 
	       <div class="ftitle">
	            <label for="versionDescriptionA">底板描述:</label>
	            <textarea id="floorDescriptionU" name="floorDescription" class="easyui-validatebox" 
	         	 validType="length[0,200]" style="resize:none;width:350px;height:60px;border-radius:5px;margin-left: 30px;"></textarea>
	        </div>
	        
	         <div class="ftitle">
	            <label for="appVersionUrlU">上传底板图片:</label>
	             <input type="hidden" name="floorImg" id="floorImgU"/>
	             <a href="#" id="uploadA" class="l-btn l-btn-small" style="margin-left:30px;" plain="true" onclick="openDialog('ddA','update')" style="width:200px;">点击上传底板图片</a>
	        </div>
	         <div class="ftitle">
	            <label for="imgshowU">底板图片:</label>
	             <div style="float:left;margin-left:30px;">
	            <div id="imgshowU">
	            
	            </div>
	            </div>
	        </div>
	        
	      </form>
    </div>
    
     <div id="ddA">Dialog Content.</div>
     <div id="uploadShowAimgPreview" title="图片浏览" class="easyui-dialog" data-options="modal:true"  style="width:600px; height:400px;"> </div>
</body>
	
	
</html>