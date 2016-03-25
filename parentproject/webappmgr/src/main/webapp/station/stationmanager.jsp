<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
    
    <title>测试页面</title>
	  
	<jsp:include page="../common/top.jsp" flush="true" /> 
    <script type="text/javascript" src="<%=request.getContextPath() %>/station/js/stationManager.js"></script>  
    <script type="text/javascript" src="<%=request.getContextPath() %>/station/js/setOrder.js"></script><!-- 通行证购买商品js -->  
     <script type="text/javascript" src="<%=request.getContextPath() %>/station/js/connectApps.js"></script><!-- 通行证和应用版本做关联js -->  
    <script type="text/javascript">
    var initParam = {
    		agentId : '<%=request.getAttribute("userId")%>',
    		flag : '<%=request.getAttribute("flag")%>'
    };
    var toolbar = [{
	  	    text:'添加',
	  	    iconCls:'icon-add',
	  	    handler:function(){
	  	    	 //初始化上级权限下拉框值
	  	    	initProvince('add','addFormProvince','');//默认选中全部，则全部下是没有市数据的
	  	    	initAddFormAgent(initParam);
	  	    	$('.panel-title.panel-with-icon').html('添加站点');
	  	    	$('#addOrUpdateStationForm').form('clear');
	  	    	$("#addOrUpdateStation").dialog('open');
	  	    	$("#addFormStationStyle").combobox('setValue','1');
	  	    }
	  	},{
	  	    text:'删除',
	  	    iconCls:'icon-remove',
	  	    handler:function(){
	  	    	deleteStationByIds();
	  	    }
	  	}];
	</script>
	  <style type="text/css">
			.ztree li button.switch {visibility:hidden; width:1px;}
			.ztree li button.switch.roots_docu {visibility:visible; width:16px;}
			.ztree li button.switch.center_docu {visibility:visible; width:16px;}
			.ztree li button.switch.bottom_docu {visibility:visible; width:16px;}
			
			.ftitle{
	  			width:100%;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑',
	  		}
	  		.ftitle label{
	  			margin-left: 30px;
	  		}
	  		
	  		.ftitle .textbox{
	  			margin-right: 50px;
	  			float : right;
	  			width: 200px;
	  		}
	  		
	  		/*setorder样式*/
	  		 .fsetOrdertitle{
	  			width:50%;
	  			float : left;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑';
	  		}
	  		
	  		 .fsetOrdertable{
	  			width:100%;
	  			float : left;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑';
	  		}
	  		.fsetOrdertitle  label{
	  			float : left;
	  			margin-left: 30px;
	  			width:75px;
	  		}
	  		
	  		.fsetOrdertable  label{
	  			float : left;
	  			margin-left: 30px;
	  		}
	  		.fsetOrdertitle  .commonInput{
	  			float : left;
	  			width: 200px;
	  			margin-left: 30px;
	  			border-radius : 5px;
	  		}
	  		
	  		 .td_font {
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
	<div  data-options="region:'north'" style="height:100px;">
		<form id="searchForm" method="post" >
	        <table style="border: none;height: 70px;margin:10 10 10 100">
		    	<tr style="margin:10;">
		    		<td colspan="1" class="td_font">站点号：</td>
		    		<td colspan="2">
		    			<input id="searchFormNumber" class="input_border"  type="text" name="searchFormNumber"  />  
		    		</td>
		    		<td colspan="1" class="td_font">站点类型：</td>
		    		<td colspan="2">
		    			<select class="easyui-combobox" id="searchFormStyle" name="searchFormStyle" style="width:200px;">
							<option value="">全部</option>
							<option value="1" >体彩</option>
							<option value="2">福彩</option>
						</select>
		    		</td>
		    		<td colspan="1"  class="td_font">站点地域：</td>
		    		<td colspan="2">
		    			 <label for="searchFormProvince">省:</label>
		    			 <select class="easyui-combobox " id="searchFormProvince" name="searchFormProvince" style="width:150px;" >
						 </select>
						 <label for="searchFormCity">市:</label>
								<select class="easyui-combobox " id="searchFormCity" name="searchFormCity"  style="width:150px;" >
						</select>
		    		</td>
		    		</tr>
		    		<tr>
		    		<td colspan="1" class="td_font">站主姓名：</td>
		    		<td colspan="2">
		    			<input id="searchFormName" class="input_border"  type="text" name="searchFormName"  />  
		    		</td>
		    		<td colspan="1" class="td_font">站主电话：</td>
		    		<td colspan="2">
		    			<input id="searchFormTelephone" class="input_border"  type="text" name="searchFormTelephone"  />  
		    		</td>
		    		<td colspan="1" class="td_font">所属代理：</td>
		    		<td colspan="1">
		    			<input id="searchFormAgent" name="searchFormAgent" class="easyui-combobox textbox"  />
		    		</td>
		    		<td class="td_font" colspan="2">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;width:80;" class="easyui-linkbutton" data-options="iconCls:'icon-search'"  type="button" value="查询" onclick="initDatagrid()">
		    			<input style="cursor:pointer;background-color: #e0ecff;border-radius:5px;float:left;margin-left:5px;width:80;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" type="button" value="重置" onclick="reset()">
		    		</td>
		    	</tr>
	    	</table>	
	    	</form>
	</div>
	<div data-options="region:'center'" style="height:100px;">
		<table id="stationDataGrid" class="easyui-datagrid" title="站点列表"  data-options="toolbar:toolbar" >
		</table>
	</div>
	
	<!-- 通行证应用版本配置弹框 -->
  <div id="w" class="easyui-dialog" title="通行证应用版本配置" style="width:700px;height:600px;padding:0px"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'取消',
                    iconCls:'icon-ok',
                    handler:function(){
                        $('#w').dialog('close');
                    }
                }]
            ">
       		<div class="easyui-layout" style="height:100%;padding:0;" >
	    	 	<div region="north" style="height:50%;" hide="false">
	    	 		<table id="appGoumai" class="easyui-datagrid" style="width:100%;height:95%;"  title="可以购买的付费应用列表" ></table>
	    	 	</div>
	    	 	<div region="center" style="height:50%;padding:0;" >
	    	 		<table id="appXufei" class="easyui-datagrid" style="width:100%;height:95%;"  title="正在使用的付费应用列表" ></table>
	    	 	</div>
    		</div>
    </div>
	
	    <!-- 添加站点 -->
  	<div id="addOrUpdateStation" class="easyui-dialog" title="站点编辑" style="width:480px;height:530px;padding:20px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddStation();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addOrUpdateStation').dialog('close');
                    }
                }]
            ">
		<form id="addOrUpdateStationForm" method="post" >
	        <input type="hidden" name="id" />
	        <div class="ftitle">
	            <label for="addFormAgent">所属代理:</label>
    			<input id="addFormAgent" name="addFormAgent" class="easyui-combobox textbox"  />
    		</div>
	        <div class="ftitle">
	            <label for="addFormStationCode">登录账号:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormStationCode"  id="addFormStationCode" data-options="required:true,validType:['englishOrNum','length[0,20]',]"" 
	              missingMessage="登录账号不可以为空" invalidMessage="登录账号长度不可以超过20个字符"></input>
	        </div>
	        <div class="ftitle">
	            <label for="addFormStationStyle">站点类型:</label>
	            <select class="easyui-combobox" id="addFormStationStyle" name="addFormStationStyle" style="width:200px;">
					<option value="1" selected >体彩</option>
					<option value="2" >福彩</option>
				</select>
	        </div>
	        <div class="ftitle">
	            <label for="addFormStationNumber">站点号:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormStationNumber"  data-options="required:true,validType:['englishOrNum','length[0,20]']""
	              missingMessage="站点号不可以为空" invalidMessage="站点号长度不可以超过20个字符"></input>
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
	            <input class="easyui-validatebox textbox" type="text" name="addFormAddress"  data-options="multiline:true,required:true,validType:['length[0,40]']"  missingMessage="站点详细地址"></input>
	        </div>
	        <div class="ftitle">
	            <label for="addFormName" >站主姓名:</label>
	            <input class="easyui-validatebox textbox" type="text" name="addFormName"  data-options="required:true,validType:['CHS','length[0,20]']"  missingMessage="站主真实姓名"></input>
	        </div>
	        <div class="ftitle">
	        	<label for="addFormTelephone" >站主手机号:</label>
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
     <!-- 站点商品配置弹框-->
  	 <div id="setOrder" class="easyui-dialog" title="购买商品" style="width:800px;height:500px;padding:10px;top:40px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddgoods('0');
                    }
                },{
                    text:'保存并提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddgoods('1');
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#setOrder').dialog('close');
                        clearGoodsArray();
                    }
                }]
            ">
		<form id="ff" method="get" novalidate>
	        <div class="fsetOrdertitle">
	            <label for="idA">订单编码:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <div style="float:left;margin-left: 30px;">
	            	<input name="code" id="codehidden" type="hidden" >
	           	 	<input class="easyui-textbox" readonly="readonly" type="text" id="codeA" style="width:200px"/> 
	           	</div>
	        </div>
	        <div class="fsetOrdertitle">
	            <label for="creatorU">订单创建人:</label><!-- 读取创建订单的代理人姓名 -->
	             <div style="float:left;margin-left: 30px;">
	            	<input class="easyui-textbox" type="text" id="creatorA" name="creator" readonly="readonly" style="width:200px;"
	               ></input>
	              </div>
	        </div>
	        <div class="fsetOrdertitle">
	            <label for="nameU">订单名称:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="nameA" name="name" data-options="required:true"
	            validType="length[1,50]"  missingMessage="订单名称不可以为空" ></input>
	        </div>
	       <div class="fsetOrdertitle">
	            <label for="priceU">支付方式:</label>
	            <div style="float : left;margin-left: 30px;">
		            <select class="easyui-combobox" id="payModeA" name="payMode"  
			          	 		 data-options="editable:false" style="width:200px;" >
			          	 		<option value="0" checked="checked">现金支付</option>
			          	 		<option value="1">转账支付</option>
					</select>
				</div>
	        </div>
	        <div class="fsetOrdertitle">
	            <label for="receiveAddrU">收货人地址:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="receiveAddrA" name="receiveAddr" data-options="required:true"
	             validType="length[1,50]" ></input>
	        </div>
	         <div class="fsetOrdertitle">
	            <label for="receiveTeleU">联系电话:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="receiveTeleA" name="receiveTele" data-options="required:true"
	             validType="mobileAndTel"  ></input>
	        </div>
	       <!--  <div class="fsetOrdertitle">
	            <label for="subject">配送方式:</label>
	             <input class="easyui-validatebox commonInput" type="text" id="priceU" name="price" data-options="required:true"
	             validType="money"  ></input>
	        </div> -->
	      <!--   <div class="fsetOrdertitle">
	            <label for="priceU">运费:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="priceU" name="price" data-options="required:true"
	             validType="money"  ></input>
	        </div> -->
	         <div class="fsetOrdertitle">
	            <label for="priceA">商品总价(元):</label>
	            <div style="float:left;margin-left: 30px;">
	            	<input name="price" id="pricehidden" type="hidden" >
	           	 	<input class="easyui-textbox" readonly="readonly" type="text" id="priceA" style="width:200px"/> 
	           	</div>
	        </div>
	        <div class="fsetOrdertitle">
	            <label for="stationA">站点号:</label>
	             <div style="float:left;margin-left: 30px;">
	             <input name="station" id="stationAhidden" type="hidden" >
	             <input class="easyui-textbox" readonly="readonly" type="text" id="stationA" style="width:200px"/> 
				</div>
	        </div>
	        <div class="fsetOrdertable">
	            <label for="product">选择商品:</label>
	           <div style="float:left;margin-left:30px;width:700px;">
	            	<table id="goodsDatagridU" class="easyui-datagrid" style="width:700px;"  title="商品列表" >
					</table>
	            </div>
	        </div>
	      </form>
    </div>
</body>
</html>