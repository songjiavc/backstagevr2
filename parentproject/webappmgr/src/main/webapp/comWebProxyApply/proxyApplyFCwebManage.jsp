<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>代理申请信息管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/comWebProxyApply/js/proxyApplyFCwebManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	 var toolbar = [{
  	    text:'回访不符合',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	updateApplyProxyStatus('1');
  	    	
  	    }
  	}/* ,
  	 {
  	    text:'回访符合',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	updateApplyProxyStatus('2');
  	    	
  	    }
  	}  */]; 
  	  
  	
		
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
	  			width:150px;
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
	<div   data-options="region:'north'" style="height:90px;border:1px solid #95b8e7; background-color:white;">
	    	<table style="border: none;height: 80px;">
		    	<tr>
		    		<td width="7%" class="td_font">状态：</td>
		    		<td width="15%">
						<select class="easyui-combobox " id="statusC" name="status"  
			          	  data-options="editable:false" style="width:150px;" >
			          	  <option value="" >全部</option>
			          	  <option value="0" >未回访</option>
			          	  <option value="1" >回访不符合</option>
			          	  <option value="2" >回访符合</option>
						</select>
		    		</td>
		    		<td width="7%" class="td_font">省：</td>
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
		    		</td>
		    		
		    		<td class="td_font" width="12%">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left" type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	</div>

    <div  data-options="region:'center'" data-options="border:false" >
    	 <table id="datagrid" class="easyui-datagrid"  title="代理申请信息列表" >
			</table>
 	</div>  
  
  
   
     <!-- 代理申请详情 -->
     <div id="detailApplyProxy" class="easyui-dialog"  title="代理申请信息列表" style="width:600px;height:500px;padding:10px;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#detailApplyProxy').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeA">姓名:</label>
	            <input type="hidden" name="id" id="idU"/>
	             <div style="float:left;margin-left:30px;">
		            <input class="easyui-textbox " type="text" id="nameU" name="name"  style="width:200px"  
		               readonly="readonly"/>
		         </div>
	        </div>
	        <div class="ftitle">
	            <label for="subject">地址:</label>
	             <div style="float:left;margin-left:30px;">
		            <input class="easyui-textbox " type="text" id="addressU" name="address"  style="width:200px"  
		               readonly="readonly"/>
		        </div>
		     </div>
		     
		     <div class="ftitle">
	            <label for="subject">是否从事彩票相关工作:</label>
	             <div style="float:left;margin-left:30px;">
		            <input class="easyui-textbox " type="text" id="isConnectNameU" name=isConnectName  style="width:200px"  
		               readonly="readonly"/>
		        </div>
		     </div>
		     
		     <div class="ftitle">
	            <label for="subject">手机:</label>
	             <div style="float:left;margin-left:30px;">
		            <input class="easyui-textbox " type="text" id="telephoneU" name="telephone"  style="width:200px"  
		               readonly="readonly"/>
		        </div>
		     </div>
		     
		      <div class="ftitle">
	            <label for="subject">状态:</label>
	             <div style="float:left;margin-left:30px;">
		            <input class="easyui-textbox " type="text" id="statusNameU" name="statusName"  style="width:200px"  
		               readonly="readonly"/>
		        </div>
		     </div>
	        
	         <div class="ftitle">
	            <label for="subject" style="width:50px;">地域:</label>
	             <div style="float:left;margin-left:30px;">
		            <input class="easyui-textbox " type="text" id="procinveU" name="provinceName"  style="width:200px"  
		               readonly="readonly"/>
		            <input class="easyui-textbox " type="text" id="cityU" name="cityName"  style="width:200px"  
		               readonly="readonly"/>
	            </div>
	        </div>
	         <div class="ftitle">
	         	 <label for="forecastContentA" style="width:50px;">备注:</label>
	         	 <!-- ※textarea两个标签间有空格时焦点会不落在首字符上※ -->
	         	 <textarea id="remarkU" name="remark" class="easyui-validatebox" data-options="editable:false"
	         	 validType="length[0,300]" style="resize:none;width:400px;height:100px;border-radius:5px;margin-left:30px;"></textarea>
	         </div>
	         
	     </form>
    </div>
    
    <div id="addOrUpdateAgent" class="easyui-dialog" title="代理编辑" style="width:480px;height:530px;padding:20px;"
            data-options="
           	modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddAgent();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addOrUpdateAgent').dialog('close');
                    }
                }]
            ">
		<form id="addOrUpdateAgentForm" method="post" >
	        <input type="hidden" name="id" />
	        <div class="ftitle">
	            <label for="addFormAgentCode">登录帐号:</label>
	            <input type="hidden" id="applyProxyId" name="applyProxyId">
	            <input class="easyui-validatebox textbox" type="text" id="addFormAgentCode" name="addFormAgentCode"  data-options="required:true,validType:['englishOrNum','length[0,20]',]"" 
	              missingMessage="代理编码不可以为空" invalidMessage="代理编码长度不可以超过20个字符"></input>
	        </div>
	         <div class="ftitle">
	            <label for="addFormProvince">所属专员:</label>
	            <input id="addFormParentId" name="addFormParentId" class="easyui-combobox textbox"  />
	        </div>
	         <div class="ftitle">
	            <label for="addFormName" >代理姓名:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormName"  data-options="required:true,validType:['CHS','length[0,20]']"  missingMessage="站主真实姓名"></input>
	        </div>
	        <div class="ftitle">
	            <label for="addFormProvince">省:</label>
	            <input id="addFormProvince" name="addFormProvince" class="easyui-combobox textbox"  />
	        </div>
	        <div class="ftitle">
	        	<label for="addFormCity">市:</label>
	        	<input id="addFormCity" name="addFormCity" class="easyui-combobox textbox"   />
	        </div>
	        <div class="ftitle">
	        	<label for="addFormRegion">区:</label>
	        	<input id="addFormRegion" name="addFormRegion" class="easyui-combobox textbox"   />
	        </div>
	        <div class="ftitle">
	            <label for="addFormAddress" >详细地址:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormAddress"  data-options="multiline:true,required:true,validType:['length[0,40]']"  missingMessage="代理详细地址"></input>
	        </div>
	        <div class="ftitle">
	        	<label for="addFormTelephone" >代理手机号:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormTelephone"  data-options="required:true,validType:['mobile']" missingMessage="站主手机号"></input>
	        </div>
	        <div class="ftitle">
	            <label  for="password">密码:</label>
	            <input class="easyui-validatebox textbox" type="password" id="password" name="password" data-options="required:true"
	            validType="length[6,15]"   ></input>
	        </div>
	        <div class="ftitle">
	            <label for="confirmPassword" >确认密码:</label>
	            <input class="easyui-validatebox textbox" type="password" name ="confirmPassword"  data-options="required:true" validType="equalTo['#password']" invalidMessage="两次输入密码不匹配"></input>
	        </div>
          </form>
     </div> 
</body>
	
	
</html>