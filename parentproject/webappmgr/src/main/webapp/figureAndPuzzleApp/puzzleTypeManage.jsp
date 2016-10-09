<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>字谜类型管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/figureAndPuzzleApp/js/puzzleTypeManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加字谜类型',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	$("#addPuzzleType").dialog('open');
  	    	
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
		    		<td width="7%" class="td_font">字谜类型名称：</td>
		    		<td width="15%">
		    			<input id="typeNameC" class="input_border"  type="text" name="typeNameC"  />  
		    		</td>
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div  data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="字谜类型列表" >
			</table>
 	</div>  
  
  
    <!-- 添加字谜类型 -->
  <div id="addPuzzleType" class="easyui-dialog"  title="添加字谜类型" style="width:500px;height:400px;padding:10px;top:10px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddPuzzleType();
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
	            <label for="codeA">字谜类型名称:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <input class="easyui-validatebox commonInput" type="text" id="typeNameA" name="typeName"  
	                data-options="required:true" validType="checkTypeName['#typeNameA','idA']"></input>
	        </div>
	        <div class="ftitle">
	            <label for="nameA">字谜行数:</label>
	            <input class="easyui-numberbox numberInput" type="text" id="typeColA" name="typeCol" data-options="required:true"
	              missingMessage="字谜行数不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="passwordA">字谜每行最多字数:</label>
	            <input class="easyui-numberbox numberInput" type="text" id="typeColWordsNumA" name="typeColWordsNum" data-options="required:true"
	             missingMessage="字谜每行最多字数不可以为空" ></input>
	        </div>
	        
	      </form>
    </div>
     <!-- 修改字谜类型-->
     <div id="updatePuzzleType" class="easyui-dialog"  title="修改字谜类型" style="width:500px;height:400px;padding:10px;top:10px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdatePuzzleType();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updatePuzzleType').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	       <div class="ftitle">
	            <label for="typeNameU">字谜类型名称:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <input class="easyui-validatebox commonInput" type="text" id="typeNameU" name="typeName"  
	                data-options="required:true" validType="checkTypeName['#typeNameU','idU']"></input>
	        </div>
	        <div class="ftitle">
	            <label for="typeColU">字谜行数:</label>
	            <input class="easyui-numberbox numberInput" type="text" id="typeColU" name="typeCol" data-options="required:true"
	              missingMessage="字谜行数不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="typeWordsNumU">字谜每行最多字数:</label>
	            <input class="easyui-numberbox numberInput" type="text" id="typeColWordsNumU" name="typeColWordsNum" data-options="required:true"
	             missingMessage="字谜每行最多字数不可以为空" ></input>
	        </div>
	      </form>
    </div>
</body>
	
	
</html>