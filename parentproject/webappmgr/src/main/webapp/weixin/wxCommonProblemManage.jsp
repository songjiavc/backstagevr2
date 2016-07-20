<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>公众号常见问题管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/weixin/js/wxCommonProblemManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加公众号常见问题',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	
  	    	addCommonproblem();
  	    	
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
	  		
	  		.toolbarTb div{
	  			float:left;
	  		}
	  		
	  		.showName
	  		{
	  			border:none;
	  			
	  		}
	  		
		</style>
		
	 
</head>
<body class="easyui-layout">
	<!-- 模糊查询 -->
	<div   data-options="region:'north'" style="height:90px;border:1px solid #95b8e7; background-color:white;">
	    	<table style="border: none;height: 80px;">
		    	<tr>
		    		<td width="7%" class="td_font">常见问题标题：</td>
		    		<td width="15%">
		    			<input id="titleC" class="input_border"  type="text" name="titleC"  />  
		    		</td>
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div  data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="常见问题数据列表" >
			</table>
 	</div>  
  
  
    <!-- 添加常见问题弹框 -->
  <div id="addCproblem" class="easyui-dialog" fit="true" title="添加常见问题" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddCproblem();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addCproblem').dialog('close');
                        $('#ff').form('clear');//清空表单内容
                    }
                }]
            ">
		
			<div class="easyui-layout" style="height:100%;padding:0;width:100%;" >
	    	 	<div region="north" style="height:45%;" title="常见问题基本内容" hide="false">
	    	 		<form id="ff" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeA">常见问题标题:</label>
				            <input type="hidden" name="id" id="idA"/>
				            <input class="easyui-validatebox commonInput" type="text" id="titleA" name="title" style="width:200px"  
				             data-options="required:true"  validType="checkTitleUnique['#titleA','idA']"  ></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">问题解决内容:</label>
				            <textarea id="contentA" name="content" class="easyui-validatebox" 
				         	 validType="length[1,500]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;" data-options="required:true"></textarea>
				        </div>
				       </form>
	    	 	</div>
	    	 	<div region="center" style="height:55%;padding:0;width:99%;" title="选择相关问题">
	    	 		<table id="cproblemsA" class="easyui-datagrid" style="width:100%;height:95%;"  title="常见问题列表" ></table>
	    	 	</div>
    		</div>
			
    		
	     
    </div>
     <!-- 修改应用弹框 -->
     <div id="updateCproblem" class="easyui-dialog"  fit="true" title="修改通行证组信息" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateCproblem();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateCproblem').dialog('close');
                    }
                }]
            ">
	      <div class="easyui-layout" style="height:100%;padding:0;width:100%;" >
	    	 	<div region="north" style="height:45%;" title="常见问题基本内容" hide="false">
	    	 		<form id="ffUpdate" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeA">常见问题标题:</label>
				            <input type="hidden" name="id" id="idU"/>
				            <input class="easyui-validatebox commonInput" type="text" id="titleU" name="title" style="width:200px"  
				             data-options="required:true"  validType="checkTitleUnique['#titleU','idU']"  ></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">问题解决内容:</label>
				            <textarea id="contentU" name="content" class="easyui-validatebox" 
				         	 validType="length[1,500]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left: 30px;" data-options="required:true"></textarea>
				        </div>
				       </form>
	    	 	</div>
	    	 	<div region="center" style="height:55%;padding:0;width:99%;" title="选择相关问题">
	    	 		<table id="cproblemsU" class="easyui-datagrid" style="width:100%;height:95%;"  title="常见问题列表" ></table>
	    	 	</div>
    		</div>
    </div>
    
   
</body>
	
	
</html>