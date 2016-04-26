<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>应用版本管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/appversion/js/appVersionManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加应用版本',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	generateCode();//生成应用版本编码
  	    	initParentAppList('appIdA','add','',false);
  	    	$("#addAppVersion").dialog('open');
  	    	
  	    }
  	} ,{
  	    text:'批量上架',
  	    iconCls:'icon-redo',
  	    handler:function(){
  	    		updateAppVerStatus('1');
  	    	}
  	} ,{
  	    text:'批量下架',
  	    iconCls:'icon-undo',
  	    handler:function(){
  	    		updateAppVerStatus('2');
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
	  			width:115px;
	  		}
	  		.ftitle .commonInput{
	  			float : left;
	  			width: 200px;
	  			margin-left: 30px;
	  			border-radius : 5px;
	  		}
	  		
	  		.ftitle .urlInput{
	  			float : left;
	  			width: 250px;
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
		    		<td width="7%" class="td_font">应用版本名称：</td>
		    		<td width="15%">
		    			<input id="appVerNameC" class="input_border"  type="text" name="appVerNameC"  />  
		    		</td>
		    		<td width="7%" class="td_font">所属应用：</td>
		    		<td width="15%">
		    			 <select class="easyui-combobox " id="appIdC" name="appIdC"  
				          	  data-options="editable:false" style="width:200px;" >
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
    	 <table id="datagrid" class="easyui-datagrid"  title="应用版本列表" >
			</table>
 	</div>  
  
  
    <!-- 添加应用版本弹框 -->
  <div id="addAppVersion" class="easyui-dialog" title="添加应用版本" style="width:600px;height:430px;padding:10px;top:1px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddAppVersion();
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
	            <label for="codeA">应用版本编码:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <div style="float:left;margin-left: 30px;">
		            <input name="appVersionCode" id="codehidden" type="hidden" >
		            <input class="easyui-textbox" type="text" id="codeA"  style="width:200px"  readonly="readonly" 
		               ></input>
		        </div>
	        </div>
	        <div class="ftitle">
	            <label for="appVersionNameA">应用版本名称:</label>
		            <input class="easyui-validatebox commonInput" type="text" id="appVersionNameA" name="appVersionName" data-options="required:true"
		             validType="checkAname['#appVersionNameA','idA']" missingMessage="应用版本名称不可以为空" ></input>
	        </div>
	        
	        <div class="ftitle">
	            <label for="appId">所属应用:</label>
	            <div style="float:left;margin-left: 5%;">
		            <select class="easyui-combobox " id="appIdA" name="appId"  
				          	  data-options="editable:false" style="width:200px;" >
					</select>
				</div>
	        </div>
	        
	         <div class="ftitle">
	            <label for="versionCodeA">应用版本号:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="versionCodeA" name="versionCode" data-options="required:true"
	             	validType="checkAVernum['#versionCodeA','appIdA','idA']"  missingMessage="应用版本号不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="appVersionUrlU">上传应用安装包:</label>
	            <input type="hidden" name="appVersionUrl"  id= "urlHiddenA">
	             <a href="#" id="uploadA" class="l-btn l-btn-small" style="margin-left:30px;" plain="true" onclick="openDialog('ddA','add')" style="width:200px;">点击上传应用安装包</a>
	        </div>
	         <div class="ftitle">
	            <label for="appVersionUrlA">应用版本安装包位置:</label>
	           <input class="easyui-validatebox urlInput" type="text" id="appVersionUrlA"  
	              readonly="readonly" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="appDeveloperA">应用开发商:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appDeveloperA" name="appDeveloper" data-options="required:true"
	            missingMessage="应用开发商不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="appVersionStatus">应用版本状态:</label>
	            <div style="float:left;margin-left: 5%;">
		            <input class="easyui-validatebox" type="radio" name="appVersionStatus"  value="1" >上架</input>
		            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appVersionStatus" value="2">下架</input>
		            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appVersionStatus" value="0" checked>待上架</input>
	        		<input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appVersionStatus" value="3" >更新</input>
	        	</div>
	        </div>
	        <!--版本描述是之后要添加的应用版本描述部分 addDate：2016-4-26  -->
	       <!--
	         <div class="ftitle">
	            <label for="versionDescriptionA">版本描述:</label>
	            <textarea id="versionDescriptionA" name="versionDescription" class="easyui-validatebox" 
	         	 validType="length[0,500]" style="resize:none;width:350px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
	        </div> -->
	        
	        
	      </form>
    </div>
     <!-- 修改应用弹框 -->
     <div id="updateAppVersion" class="easyui-dialog" title="修改应用版本信息" style="width:600px;height:430px;padding:10px;top:1px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateAppVersion();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateAppVersion').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeU">应用版本编码:</label>
	             <input type="hidden" name="id" id="idU"/>
	            <div style="float:left;margin-left: 30px;">
		            <input name="appVersionCode" id="codehidden" type="hidden" >
		            <input class="easyui-textbox" type="text" id="codeU"  style="width:200px"  readonly="readonly" 
		               ></input>
		        </div>
	        </div>
	        <div class="ftitle">
	            <label for="appVersionNameU">应用版本名称:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appVersionNameU" name="appVersionName" data-options="required:true"
	             validType="checkAname['#appVersionNameU','idU']" missingMessage="应用版本名称不可以为空" ></input>
	        </div>
	        
	        <div class="ftitle">
	            <label for="appId">所属应用:</label>
	            <div style="float:left;margin-left: 5%;">
		            <select class="easyui-combobox " id="appIdU" name="appId"  
				          	  data-options="editable:false" style="width:200px;" >
					</select>
				</div>
	        </div>
	        
	         <div class="ftitle">
	            <label for="versionCodeU">应用版本号:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="versionCodeU" name="versionCode" data-options="required:true"
	           	 validType="checkAVernum['#versionCodeU','appIdU','idU']"  missingMessage="应用版本号不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="appVersionUrlU">上传应用安装包:</label>
	            <input type="hidden" name="appVersionUrl"  id= "urlHiddenU">
	             <a href="#" id="uploadU" class="l-btn l-btn-small" style="margin-left:30px;" plain="true" onclick="openDialog('ddA','update')" style="width:200px;">点击上传应用安装包</a>
	        </div>
	        
	         <div class="ftitle">
	            <label for="appVersionUrlU">应用版本安装包位置:</label>
	            <input class="easyui-validatebox urlInput" type="text" id="appVersionUrlU"  
	              readonly="readonly" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="appDeveloperU">应用开发商:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="appDeveloperU" name="appDeveloper" data-options="required:true"
	            missingMessage="应用开发商不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="appVersionStatus">应用版本状态:</label>
	            <div style="float:left;margin-left: 5%;">
		            <input class="easyui-validatebox" type="radio" name="appVersionStatus"  value="1" >上架</input>
		            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appVersionStatus" value="2">下架</input>
		            <input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appVersionStatus" value="0" checked>待上架</input>
	        		<input class="easyui-validatebox" style="margin-left:7px;" type="radio" name="appVersionStatus" value="3" >更新</input>
	        	</div>
	        </div>
	        <!--版本描述是之后要添加的应用版本描述部分 addDate：2016-4-26  -->
	         <!-- <div class="ftitle">
	            <label for="priceA">版本描述:</label>
	            <textarea id="versionDescriptionU" name="versionDescription" class="easyui-validatebox" 
	         	 validType="length[0,500]" style="resize:none;width:350px;height:100px;border-radius:5px;margin-left: 30px;"></textarea>
	        </div> -->
	        
	         
	      </form>
    </div>
    
    <div id="ddA">Dialog Content.</div>
</body>
	
	
</html>