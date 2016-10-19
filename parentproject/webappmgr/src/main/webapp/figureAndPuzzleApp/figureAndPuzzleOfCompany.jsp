<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>公司的图谜字谜审核管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <jsp:include page="../common/top.jsp" flush="true" />
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" /> 
 	<link rel="shortcut icon" href="<%=request.getContextPath() %>/images/favicon.ico">
    <script src="<%=request.getContextPath() %>/figureAndPuzzleApp/js/figureAndPuzzleOfCompany.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [/* {
  	    text:'添加图谜字谜',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	addDialogOpen();
  	    	
  	    }
  	}  */ ];
  	  
  	
		
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
	  			width:130px;
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
		    		<td width="7%" class="td_font">图谜字谜名称：</td>
		    		<td width="15%">
		    			<input id="nameC" class="input_border"  type="text" name="nameC"  />  
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
		    		<td width="7%" class="td_font">彩种：</td>
		    		<td width="15%">
						<select class="easyui-combobox " id="lotterytypeC" name="lotterytypeC"  
			          	  data-options="editable:false" style="width:150px;" >
			          	   <option value="" >请选择</option>
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
    	 <table id="datagrid" class="easyui-datagrid"  title="图谜字谜列表" >
			</table>
 	</div>  
  

   
    <!-- 详情图谜字谜-->
     <div id="detailFigureAndPuzzle" class="easyui-dialog"  title="图谜字谜详情" style="width:650px;height:500px;padding:10px;top:10px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'审批通过',
                    iconCls:'icon-ok',
                    handler:function(){
                        approveFigureAndPuzzlesInDialog('2');
                    }
                },{
                    text:'审批驳回',
                    iconCls:'icon-ok',
                    handler:function(){
                        approveFigureAndPuzzlesInDialog('3');
                    }
                },{
                    text:'不通过',
                    iconCls:'icon-ok',
                    handler:function(){
                       approveFigureAndPuzzlesInDialog('4');
                    }
                },{
                    text:'关闭',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#detailFigureAndPuzzle').dialog('close');
                    }
                }]
            ">
		<form id="ffDetail" method="get" novalidate>
	       <div class="ftitle">
	            <label for="nameD">图谜字谜名称:</label>
	            <input type="hidden" name="id" id="idD"/>
	            <input class="easyui-validatebox commonInput" type="text" id="nameD" name="name"  
	                data-options="required:true" />
	        </div>
	        
	         <div class="ftitle">
		            <label for="playNameD">彩种玩法:</label><!-- 用来拼接当前图谜字谜的名称，用于应用中显示 -->
		            <input class="easyui-validatebox commonInput" type="text" id="playNameD" name="playName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="playNameU" name="playName"  data-options="editable:false" style="width:200px;" >
				          	  <option value="3D" >3D</option>
				          	  <option value="双色球">双色球</option>
						</select>
		             </div> -->
			 </div>
	        
	        <div class="ftitle">
		            <label for="lotteryTypeD">彩种分类:</label>
		             <input class="easyui-validatebox commonInput" type="text" id="lotteryTypeNameD" name="lotteryTypeName" readonly="readonly" />
		            <!--  <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="lotteryTypeU" name="lotteryType"  data-options="editable:false" style="width:200px;" >
				          	  <option value="1" >体彩</option>
				          	  <option value="2">福彩</option>
						</select>
		             </div> -->
				           
			 </div>
	        
	         <div class="ftitle">
		            <label for="figureOrPuzzlesD">图谜/字谜:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="figureOrPuzzlesNameD" name="figureOrPuzzlesName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="figureOrPuzzlesU" name="figureOrPuzzles"  data-options="editable:false" style="width:200px;" >
				          	  <option value="1" >图谜</option>
				          	  <option value="2">字谜</option>
						</select>
		             </div> -->
			 </div>
			 
			 <div class="ftitle" id="zimiStatusDivD">
		            <label for="puzzlesTypeIdD">输入文字/上传字谜图片:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="zimiStatusNameD"
		             name="zimiStatusName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left: 5%;">
			            <input class="easyui-validatebox" type="radio" name="zimiStatus" id="zs1"  value="0" ><span for="zs1">输入文字</span></input>
			            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" id="zs2" name="zimiStatus" value="1"><span for="zs2">上传字谜图片</span></input>
	        		</div> -->
			 </div>
	         
			 <div class="ftitle" id="zmD">
		            <label for="puzzlesTypeIdD">字谜类型:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="puzzlesTypeNameD"
		             name="puzzlesTypeName" readonly="readonly" />
		            <!--  <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="puzzlesTypeIdD" name="puzzlesTypeId"  data-options="editable:false" style="width:200px;" >
					    	</select>
		             </div> -->
			 </div>
			 
			 <div class="ftitle" id="zmFloorD">
		            <label for="puzzlesTypeIdD">字谜底板:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="figureOrPuzzlesNameD"
		             name="figureOrPuzzlesName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="floorOfFigureAndPuzzleD" name="floorOfFigureAndPuzzlesId"  data-options="editable:false" style="width:200px;" >
						</select>
		             </div> -->
			 </div>
			 
			  <div class="ftitle" id="floorD">
	            <label for="imgshowD">字谜底板图片:</label>
	            <div style="float:left;margin-left:30px;">
		             <div id="imgshowD">
		            
		            </div>
	            </div>
	           
	       	 </div>
	       	 
	        <div class="ftitle" id="zimiContentD">
	            <label for="versionDescriptionD">字谜内容:</label>
	            <textarea id="puzzleContentD" name="puzzleContent" class="easyui-validatebox" 
	         	 readonly="readonly" style="resize:none;width:350px;height:60px;border-radius:5px;margin-left:30px;"></textarea>
	        </div>
			 
	        
	         <div class="ftitle" id="tumiDivImgShowD">
	            <label for="tumiD">字谜/图谜图片:</label>
	             <input type="hidden" name="figureImg" id="figureImgD"/>
	            <div style="float:left;margin-left:30px;">
		             <div id="tumiD">
		            
		            </div>
	            </div>
	           
	        </div>
	      </form>
    </div>
    
     <!-- 查看详情图谜字谜-->
     <div id="viewFigureAndPuzzle" class="easyui-dialog"  title="图谜字谜详情" style="width:650px;height:500px;padding:10px;top:10px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'关闭',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#viewFigureAndPuzzle').dialog('close');
                    }
                }]
            ">
		<form id="ffViewDetail" method="get" novalidate>
	       <div class="ftitle">
	            <label for="nameD">图谜字谜名称:</label>
	            <input type="hidden" name="id" id="idV"/>
	            <input class="easyui-validatebox commonInput" type="text" id="nameV" name="name"  
	                data-options="required:true" />
	        </div>
	        
	         <div class="ftitle">
		            <label for="playNameD">彩种玩法:</label><!-- 用来拼接当前图谜字谜的名称，用于应用中显示 -->
		            <input class="easyui-validatebox commonInput" type="text" id="playNameV" name="playName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="playNameU" name="playName"  data-options="editable:false" style="width:200px;" >
				          	  <option value="3D" >3D</option>
				          	  <option value="双色球">双色球</option>
						</select>
		             </div> -->
			 </div>
	        
	        <div class="ftitle">
		            <label for="lotteryTypeNameV">彩种分类:</label>
		             <input class="easyui-validatebox commonInput" type="text" id="lotteryTypeNameV" name="lotteryTypeName" readonly="readonly" />
		            <!--  <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="lotteryTypeU" name="lotteryType"  data-options="editable:false" style="width:200px;" >
				          	  <option value="1" >体彩</option>
				          	  <option value="2">福彩</option>
						</select>
		             </div> -->
				           
			 </div>
	        
	         <div class="ftitle">
		            <label for="figureOrPuzzlesV">图谜/字谜:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="figureOrPuzzlesNameV" name="figureOrPuzzlesName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="figureOrPuzzlesU" name="figureOrPuzzles"  data-options="editable:false" style="width:200px;" >
				          	  <option value="1" >图谜</option>
				          	  <option value="2">字谜</option>
						</select>
		             </div> -->
			 </div>
			 
			 <div class="ftitle" id="zimiStatusDivV">
		            <label for="zimiStatusNameV">输入文字/上传字谜图片:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="zimiStatusNameV"
		             name="zimiStatusName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left: 5%;">
			            <input class="easyui-validatebox" type="radio" name="zimiStatus" id="zs1"  value="0" ><span for="zs1">输入文字</span></input>
			            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" id="zs2" name="zimiStatus" value="1"><span for="zs2">上传字谜图片</span></input>
	        		</div> -->
			 </div>
	         
			 <div class="ftitle" id="zmV">
		            <label for="puzzlesTypeIdV">字谜类型:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="puzzlesTypeNameV"
		             name="puzzlesTypeName" readonly="readonly" />
		            <!--  <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="puzzlesTypeIdD" name="puzzlesTypeId"  data-options="editable:false" style="width:200px;" >
					    	</select>
		             </div> -->
			 </div>
			 
			 <div class="ftitle" id="zmFloorV">
		            <label for="puzzlesTypeIdV">字谜底板:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="figureOrPuzzlesNameV"
		             name="figureOrPuzzlesName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="floorOfFigureAndPuzzleD" name="floorOfFigureAndPuzzlesId"  data-options="editable:false" style="width:200px;" >
						</select>
		             </div> -->
			 </div>
			 
			  <div class="ftitle" id="floorV">
	            <label for="imgshowV">字谜底板图片:</label>
	            <div style="float:left;margin-left:30px;">
		             <div id="imgshowV">
		            
		            </div>
	            </div>
	           
	       	 </div>
	       	 
	        <div class="ftitle" id="zimiContentV">
	            <label for="versionDescriptionV">字谜内容:</label>
	            <textarea id="puzzleContentV" name="puzzleContent" class="easyui-validatebox" 
	         	 readonly="readonly" style="resize:none;width:350px;height:60px;border-radius:5px;margin-left:30px;"></textarea>
	        </div>
			 
	        
	         <div class="ftitle" id="tumiDivImgShowV">
	            <label for="tumiD">字谜/图谜图片:</label>
	             <input type="hidden" name="figureImg" id="figureImgV"/>
	            <div style="float:left;margin-left:30px;">
		             <div id="tumiV">
		            
		            </div>
	            </div>
	           
	        </div>
	      </form>
    </div>
    
     <!-- 发布区域弹框 -->
  <div id="w" class="easyui-dialog" title="选择发布区域" style="width:400px;height:300px;padding:10px"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'确认',
                    iconCls:'icon-ok',
                    handler:function(){
                        areaSelect();
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
        	<input type="hidden" id="fApIdD" />
        	<input type="hidden" id="operortypeD" />
			<ul id="treeDemo" class="ztree"></ul>
		</div>
    </div>
    
    
	<div id="ddA">Dialog Content.</div>
     <div id="uploadShowAimgPreview" title="图片浏览" class="easyui-dialog" data-options="modal:true"  style="width:600px; height:400px;"> </div>
</body>

 <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.core-3.5.js" type="text/javascript"></script>	
    <script src="<%=request.getContextPath() %>/js/ztree/jquery.ztree.excheck-3.5.js" type="text/javascript"></script>

	
	
</html>