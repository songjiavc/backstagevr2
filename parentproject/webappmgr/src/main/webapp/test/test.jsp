<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>应用管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/test/js/test.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	
  	  
  	
		
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
		</style>
		
	 
</head>
<body class="easyui-layout">
	<!-- 模糊查询 -->
	<div   data-options="region:'center'" title="测试接口"  style="height:100%;border:1px solid #95b8e7; background-color:white;">
	    	<table style="border: none;height: 100%;">
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="checkLogin()">测试登录接口</a> 
		    		</td>
		    	</tr>
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="getAuthOfStationAndApp()">测试通行证的app权限接口</a> 
		    		</td>
		    	</tr>
		    	
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="updatePassword()">测试更新密码接口</a> 
		    		</td>
		    	</tr>
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="getAnnouncementOfStation()">根据通行证获取通告数据接口</a> 
		    		</td>
		    	</tr>
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="getComnoticeOfStation()">根据通行证获取公司公告数据接口</a> 
		    		</td>
		    	</tr>
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="getAdsOfStationAndApp()">获取应用广告数据</a> 
		    		</td>
		    	</tr>
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="getNoticesOfStationAndApp()">获取应用公告数据</a> 
		    		</td>
		    	</tr>
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="getProxyOfStation()">获取通行证的代理数据</a> 
		    		</td>
		    	</tr>
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="getAppversionsOfnew()">获取通当前安装的应用的最新版本数据</a> 
		    		</td>
		    	</tr>
		    	<tr>
		    		<td width="100%">
		    			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="getKaijiangNotice()">获取通当前开奖公告数据</a> 
		    		</td>
		    	</tr>
	    	</table>	
	</div>

  
  
   
</body>
	
	
</html>