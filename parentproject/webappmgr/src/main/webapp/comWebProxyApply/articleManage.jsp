<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>公司网站文章管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/comWebProxyApply/js/articleManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加网站文章',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	addComwebArticle();
  	    	
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
	  		
	  		a
	  		{
	  			cursor:pointer;	
	  		}
	  		
	  		ul
	  		{
	  			/* list-style-type:none; */
	  		}
	  		
	  		li a{
	  			font-size:15px;
	  		}
	  		
		</style>
		
	 
</head>
<body class="easyui-layout">
	<!-- 模糊查询 -->
	<div   data-options="region:'north'" style="height:90px;border:1px solid #95b8e7; background-color:white;">
	    	<table style="border: none;height: 80px;">
		    	<tr>
		    		<td width="7%" class="td_font">标题：</td>
		    		<td width="15%">
						 <input class="easyui-textbox " type="text" id="titleC" name="titleC"  style="width:200px"  
		              />
		    		</td>
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div  data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="网站文章列表" >
			</table>
 	</div>  
 	
 	 <div id="uploadShowAimgPreview" title="图片预览" class="easyui-dialog" data-options="modal:true"  style="width:500px; height:400px;"> </div>
    <div id="uploadShowUimgPreview" title="图片预览" class="easyui-dialog" data-options="modal:true"  style="width:500px; height:400px;"> </div>
  
  
    <!-- 添加弹框 -->
  <div id="addArticle" class="easyui-dialog"  title="添加网站文章" style="width:600px;height:430px;padding:10px;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddArticle();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        addDialogCancel();
                    }
                }]
            ">
		<form id="ff" method="post" novalidate>
	        <div class="ftitle">
	            <label for="codeA">标题:</label>
				            <input type="hidden" name="id" id="idA"/>
				            <input class="easyui-validatebox commonInput" type="text" id="titleA" name="title" style="width:200px"  
				             data-options="required:true"   validType="length[1,10]"></input>
	        </div>
	        <div class="ftitle">
	            <label for="appVersionUrlU">上传文章图片:</label>
	           <input type="hidden" id="imgA" name="img"/>
	             <a href="#" id="uploadU" class="l-btn l-btn-small" style="margin-left:30px;" plain="true" onclick="openDialog('ddA','add')" style="width:200px;">点击上传文章图片</a>
		       	<div id="uploadShowA">
	                    		<ul>
	                    			
	                    		</ul>
	                    	</div>
		        </div>
	        
	        <div class="ftitle">
	            <label for="contentA">文章内容:</label>
	             <textarea id="contentA" name="content" class="easyui-validatebox" validType="length[1,1000]" data-options="required:true" 
				           style="resize:none;width:350px;height:210px;border-radius:5px;margin-left: 30px;"></textarea>
	        </div>
	        
	        
	      </form>
    </div>
     <!-- 修改应用弹框 -->
     <div id="updateArticle" class="easyui-dialog" title="修改文章数据" style="width:600px;height:430px;padding:10px;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateArticle();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateArticle').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="post" novalidate>
	         <div class="ftitle">
	            <label for="codeA">标题:</label>
				            <input type="hidden" name="id" id="idU"/>
				            <input class="easyui-validatebox commonInput" type="text" id="titleU" name="title" style="width:200px"  
				             data-options="required:true"   validType="length[1,10]"  ></input>
	        </div>
	         <div class="ftitle">
	            <label for="appVersionUrlU">上传文章图片:</label>
	           <input type="hidden" id="imgU" name="img"/>
	             <a href="#" id="uploadU" class="l-btn l-btn-small" style="margin-left:30px;" plain="true" onclick="openDialog('ddA','update')" style="width:200px;">点击上传文章图片</a>
		       	<div id="uploadShowU">
	                    		<ul>
	                    			
	                    		</ul>
	                    	</div>
		        </div>
	        
	        <div class="ftitle">
	            <label for="contentA">文章内容:</label>
	             <textarea id="contentA" name="content" class="easyui-validatebox" data-options="required:true"   validType="length[1,1000]" 
				           style="resize:none;width:350px;height:210px;border-radius:5px;margin-left: 30px;"></textarea>
	        </div>
	        
	         
	      </form>
    </div>
    <!-- 上传弹框div -->
    <div id="ddA">Dialog Content.</div>
</body>
	
	
</html>