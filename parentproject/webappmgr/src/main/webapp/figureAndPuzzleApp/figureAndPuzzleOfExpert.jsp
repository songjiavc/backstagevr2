<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>专家的图谜字谜管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <jsp:include page="../common/top.jsp" flush="true" /> 
 	<link rel="shortcut icon" href="<%=request.getContextPath() %>/images/favicon.ico">
    <script src="<%=request.getContextPath() %>/figureAndPuzzleApp/js/figureAndPuzzleOfExpert.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加图谜字谜',
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
	<noscript>
<div style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
    <img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
</div></noscript>
    <div region="north" split="false" border="false" style="overflow: hidden; height: 50px;
        background: url('../images/1.jpg')  repeat-x center 50%;
        color: #fff; font-family: Verdana, 微软雅黑,黑体">
        <span style="float:right; padding-right:20px;" class="head"> 
       	 <a href="#" id="editpass">修改密码</a> <a href="#" id="loginOut" onclick="logout()">安全退出</a>
        </span>
         <span style="float:right; padding-right:20px;" class="head"> 
         		当前登录专家：<span id="loginuser">admin</span>
         </span>
		<img alt="" src="<%=request.getContextPath() %>/images/clogo.png" style="float:left;">
        <span style="padding-left:10px; font-size: 36px; float:left;font-family:隶书;">图谜字谜发布平台</span>
    </div>
    <div region="south" split="false" style="height: 30px; background: #D2E0F2; ">
        <div class="footer"><center>佰艺霖专家图谜字谜发布管理平台@2016</center></div>
    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden;">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<div title="欢迎使用" style=" " id="home">
				<!-- 模糊查询 -->
			<div   data-options="region:'north'" style="height:10%;border:1px solid white; background-color:white;">
	    	<table style="border: none;">
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
			
		    <div  data-options="region:'center'" data-options="border:false" style="height:89%;">
		    	 <table id="datagrid" class="easyui-datagrid"  title="图谜字谜列表" >
					</table>
		 	</div>  
 	
			</div>
		</div>
    </div>
	
 	<!-- message中存放的是登录信息 -->
	<input type="hidden" name="message" id="message" value="${message}">
  
  
    <!-- 添加图谜字谜-->
  <div id="addFigureAndPuzzle" class="easyui-dialog"  title="添加图谜字谜" style="width:650px;height:500px;padding:10px;top:10px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddFigureAndPuzzle('0');
                    }
                },{
                    text:'保存并提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddFigureAndPuzzle('1');
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
	            <label for="codeA">图谜字谜名称:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <input class="easyui-validatebox commonInput" type="text" id="nameA" name="name"  
	                data-options="required:true" ></input>
	        </div>
	        
	         <div class="ftitle">
		            <label for="playNameA">彩种玩法:</label><!-- 用来拼接当前图谜字谜的名称，用于应用中显示 -->
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="playNameA" name="playName"  data-options="editable:false" style="width:200px;" >
				          	  <option value="3D" selected="selected">3D</option>
				          	  <option value="双色球">双色球</option>
						</select>
		             </div>
			 </div>
			 
			  <div class="ftitle">
		            <label for="playNameA">发布期号:</label>
		             <input class="easyui-validatebox commonInput" type="text" id="playNumA" name="playNum"  
	                  readonly="readonly"/>
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
		            <label for="figureOrPuzzlesA">图谜/字谜:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="figureOrPuzzlesA" name="figureOrPuzzles"  data-options="editable:false" style="width:200px;" >
				          	  <option value="1" >图谜</option>
				          	  <option value="2">字谜</option>
						</select>
		             </div>
			 </div>
			 
			 <div class="ftitle" id="zimiStatusDivA">
		            <label for="puzzlesTypeIdA">输入文字/上传字谜图片:</label>
		             <div style="float:left;margin-left: 5%;">
			            <input class="easyui-validatebox" type="radio" name="zimiStatus" id="zs1"  value="0" checked><span for="zs1">输入文字</span></input>
			            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" id="zs2" name="zimiStatus" value="1"><span for="zs2">上传字谜图片</span></input>
	        		</div>
			 </div>
	         
			 <div class="ftitle" id="zmA">
		            <label for="puzzlesTypeIdA">字谜类型:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="puzzlesTypeIdA" name="puzzlesTypeId"  data-options="editable:false" style="width:200px;" >
					    	</select>
		             </div>
			 </div>
			 
			 <div class="ftitle" id="zmFloorA">
		            <label for="puzzlesTypeIdA">字谜底板:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="floorOfFigureAndPuzzleA" name="floorOfFigureAndPuzzlesId"  data-options="editable:false" style="width:200px;" >
						</select>
		             </div>
			 </div>
			 
			  <div class="ftitle" id="floorA">
	            <label for="imgshowA">字谜底板图片:</label>
	            <div style="float:left;margin-left:30px;">
		             <div id="imgshowA">
		            
		            </div>
	            </div>
	           
	       	 </div>
	       	 
	        <div class="ftitle" id="zimiContentA">
	            <label for="versionDescriptionA">字谜内容:</label>
	            <textarea id="puzzleContentA" name="puzzleContent" class="easyui-validatebox" 
	         	 validType="checkPuzzleContent['#puzzleContentA']" style="resize:none;width:350px;height:60px;border-radius:5px;margin-left:30px;"></textarea>
	        </div>
			 
			 <div class="ftitle" id="tumiDivImg">
	            <label for="appVersionUrlU">上传字谜/图谜图片:</label>
	            <input type="hidden" name="figureImg" id="figureImgA"/>
	             <a href="#" id="uploadA" class="l-btn l-btn-small" style="margin-left:30px;" plain="true" onclick="openDialog('ddA','add')" style="width:200px;">上传字谜/图谜图片</a>
	        </div>
	        
	         <div class="ftitle" id="tumiDivImgShow">
	            <label for="tumiA">字谜/图谜图片:</label>
	            <div style="float:left;margin-left:30px;">
		             <div id="tumiA">
		            
		            </div>
	            </div>
	           
	        </div>
			 
	      </form>
    </div>
     <!-- 修改图谜字谜-->
     <div id="updateFigureAndPuzzle" class="easyui-dialog"  title="修改图谜字谜" style="width:650px;height:500px;padding:10px;top:10px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateFigureAndPuzzle('0');
                    }
                },{
                    text:'保存并提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateFigureAndPuzzle('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateFigureAndPuzzle').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	       <div class="ftitle">
	            <label for="nameU">图谜字谜名称:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <input class="easyui-validatebox commonInput" type="text" id="nameU" name="name"  
	                data-options="required:true" ></input>
	        </div>
	        
	         <div class="ftitle">
		            <label for="playNameU">彩种玩法:</label><!-- 用来拼接当前图谜字谜的名称，用于应用中显示 -->
		            <input class="easyui-validatebox commonInput" type="text" id="playNameU" name="playName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="playNameU" name="playName"  data-options="editable:false" style="width:200px;" >
				          	  <option value="3D" >3D</option>
				          	  <option value="双色球">双色球</option>
						</select>
		             </div> -->
			 </div>
			 
			 <div class="ftitle">
		            <label for="playNameA">发布期号:</label>
		             <input class="easyui-validatebox commonInput" type="text" id="playNumU" name="playNum"  
	                  readonly="readonly"/>
			 </div>
			 
	        
	        <div class="ftitle">
		            <label for="lotteryTypeU">彩种分类:</label>
		             <input class="easyui-validatebox commonInput" type="text" id="lotteryTypeNameU" name="lotteryTypeName" readonly="readonly" />
		            <!--  <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="lotteryTypeU" name="lotteryType"  data-options="editable:false" style="width:200px;" >
				          	  <option value="1" >体彩</option>
				          	  <option value="2">福彩</option>
						</select>
		             </div> -->
				           
			 </div>
	        
	         <div class="ftitle">
		            <label for="figureOrPuzzlesU">图谜/字谜:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="figureOrPuzzlesNameU" name="figureOrPuzzlesName" readonly="readonly" />
		             <!-- <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="figureOrPuzzlesU" name="figureOrPuzzles"  data-options="editable:false" style="width:200px;" >
				          	  <option value="1" >图谜</option>
				          	  <option value="2">字谜</option>
						</select>
		             </div> -->
			 </div>
			 
			 <div class="ftitle" id="zimiStatusDivU">
		            <label for="puzzlesTypeIdU">输入文字/上传字谜图片:</label>
		             <div style="float:left;margin-left: 5%;">
			            <input class="easyui-validatebox" type="radio" name="zimiStatus" id="zs1"  value="0" ><span for="zs1">输入文字</span></input>
			            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" id="zs2" name="zimiStatus" value="1"><span for="zs2">上传字谜图片</span></input>
	        		</div>
			 </div>
	         
			 <div class="ftitle" id="zmU">
		            <label for="puzzlesTypeIdU">字谜类型:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="puzzlesTypeIdU" name="puzzlesTypeId"  data-options="editable:false" style="width:200px;" >
					    	</select>
		             </div>
			 </div>
			 
			 <div class="ftitle" id="zmFloorU">
		            <label for="puzzlesTypeIdU">字谜底板:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="floorOfFigureAndPuzzleU" name="floorOfFigureAndPuzzlesId"  data-options="editable:false" style="width:200px;" >
						</select>
		             </div>
			 </div>
			 
			  <div class="ftitle" id="floorU">
	            <label for="imgshowU">字谜底板图片:</label>
	            <div style="float:left;margin-left:30px;">
		             <div id="imgshowU">
		            
		            </div>
	            </div>
	           
	       	 </div>
	       	 
	        <div class="ftitle" id="zimiContentU">
	            <label for="versionDescriptionU">字谜内容:</label>
	            <textarea id="puzzleContentU" name="puzzleContent" class="easyui-validatebox" 
	         	 validType="checkPuzzleContent['#puzzleContentU']" style="resize:none;width:350px;height:60px;border-radius:5px;margin-left:30px;"></textarea>
	        </div>
			 
			 <div class="ftitle" id="tumiDivImgU">
	            <label for="appVersionUrlU">上传字谜/图谜图片:</label>
	            <input type="hidden" name="figureImg" id="figureImgU"/>
	             <a href="#" id="uploadA" class="l-btn l-btn-small" style="margin-left:30px;" plain="true" onclick="openDialog('ddA','update')" style="width:200px;">上传字谜/图谜图片</a>
	        </div>
	        
	         <div class="ftitle" id="tumiDivImgShowU">
	            <label for="tumiU">字谜/图谜图片:</label>
	            <div style="float:left;margin-left:30px;">
		             <div id="tumiU">
		            
		            </div>
	            </div>
	           
	        </div>
	      </form>
    </div>
    
    <!-- 详情图谜字谜-->
     <div id="detailFigureAndPuzzle" class="easyui-dialog"  title="图谜字谜详情" style="width:650px;height:500px;padding:10px;top:10px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
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
	            <input type="hidden" name="id" id="idU"/>
	            <input class="easyui-validatebox commonInput" type="text" id="nameD" name="name"  
	                data-options="required:true" ></input>
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
		            <label for="playNameA">发布期号:</label>
		             <input class="easyui-validatebox commonInput" type="text" id="playNumD" name="playNum"  
	                  readonly="readonly"/>
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
    
    <!--修改密码窗口-->
    <div id="w" class="easyui-window" title="修改密码" collapsible="false" minimizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <form id="updatePasswordForm" action="<%=request.getContextPath() %>/fmpApp/updateExpertPassword.action" method="post">
                <table cellpadding=3>
                    <tr>
                        <td>新密码：</td>
                        <td><input class="easyui-validatebox textbox" type="password" id="password" name="password" data-options="required:true" validType="length[6,15]"   ></input></td>
                    </tr>
                    <tr>
                        <td>确认密码：</td>
                        <td><input class="easyui-validatebox textbox" type="password" id="confirmPassword" name="confirmPassword" data-options="required:true"  validType="equalTo['#password']"   ></input></td>
                    </tr>
                </table>
                </form>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消</a>
            </div>
        </div>
    </div>
    
     <div id="rejectResonDiv" class="easyui-dialog" title="驳回理由" style="width:400px;height:300px;padding:10px"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'关闭',
                    iconCls:'icon-ok',
                    handler:function(){
                        $('#rejectResonDiv').dialog('close');
                    }
                }]
            ">
        <div class="zTreeDemoBackground left" style="float:left;width: 200px;">
        	<input type="hidden" id="fApIdRr" />
        	<input type="hidden" id="operortypeRr" />
       		 <textarea id="rejectResonV" name="rejectReson" class="easyui-validatebox" 
	         	readonly="readonly" style="resize:none;width:360px;height:190px;"></textarea>
		</div>
    </div>
    
	<div id="ddA">Dialog Content.</div>
     <div id="uploadShowAimgPreview" title="图片浏览" class="easyui-dialog" data-options="modal:true"  style="width:600px; height:400px;"> </div>
</body>



	
	
</html>