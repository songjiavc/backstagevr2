<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>图谜字谜专家管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/figureAndPuzzleApp/js/expertOfFMPAppManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加图谜字谜专家',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	initExpertProvince('add','provinceCodeA','');
  	    	$("#addExpert").dialog('open');
  	    	
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
	  			width: 300px;
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
		    		<td width="7%" class="td_font">专家姓名：</td>
		    		<td width="15%">
		    			<input id="nameC" class="input_border"  type="text" name="nameC"  />  
		    		</td>
		    		<td width="7%" class="td_font">图谜/字谜：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox " id="figureOrPuzzlesC" name="figureOrPuzzlesC"  
			          	  data-options="editable:false" style="width:150px;" >
			          	  <option value="" >请选择</option>
			          	  <option value="0" >全部</option>
			          	  <option value="1" >图谜</option>
			          	  <option value="2" >字谜</option>
						</select>
		    		</td>
		    		<td width="7%" class="td_font">彩种：</td>
		    		<td width="15%">
						<select class="easyui-combobox " id="lotterytypeC" name="lotterytypeC"  
			          	  data-options="editable:false" style="width:150px;" >
			          	   <option value="" >请选择</option>
			          	  <option value="0" >全部</option>
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
    	 <table id="datagrid" class="easyui-datagrid"  title="图谜字谜专家列表" >
			</table>
 	</div>  
  
  
    <!-- 添加图谜字谜专家 -->
  <div id="addExpert" class="easyui-dialog"  title="添加图谜字谜专家" style="width:600px;height:480px;padding:10px;top:10px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddExpert();
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
	            <label for="codeA">专家登录名:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <input class="easyui-validatebox commonInput" type="text" id="codeA" name="code"  
	               validType="checkExpertCode['#codeA','idA']" data-options="required:true"></input>
	        </div>
	        <div class="ftitle">
	            <label for="nameA">专家姓名:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="nameA" name="name" data-options="required:true"
	              missingMessage="专家姓名不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="passwordA">登录密码:</label>
	            <input class="easyui-validatebox commonInput" type="password" id="passwordA" name="password" data-options="required:true"
	             validType="length[6,20]" missingMessage="登录密码不可以为空" onblur="$('#ff').form('enableValidation').form('validate');"></input>
	        </div>
	        <div class="ftitle">
	            <label for="passwordA">确认密码:</label>
	            <input class="easyui-validatebox commonInput" type="password" id="identityPasswordA"  data-options="required:true"
	            validType="equalTo['#passwordA']"   missingMessage="两次输入密码不匹配" ></input>
	        </div>
	        <div class="ftitle">
		            <label for="figureOrPuzzlesA">图谜/字谜:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="figureOrPuzzlesA" name="figureOrPuzzles"  data-options="editable:false" style="width:300px;" >
				          	 <option value="0" checked>全部</option>
				          	  <option value="1" >图谜</option>
				          	  <option value="2">字谜</option>
						</select>
		             </div>
				           
			 </div>
	         <div class="ftitle">
		            <label for="lotteryTypeA">彩种分类:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="lotteryTypeA" name="lotteryType"  data-options="editable:false" style="width:300px;" >
				          	 <option value="0" checked>全部</option>
				          	  <option value="1" >体彩</option>
				          	  <option value="2">福彩</option>
						</select>
		             </div>
				           
			 </div>
			 <div class="ftitle">
	        	<label for="telephone" >用户电话:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="telephoneA" name="telephone"  data-options="required:true,validType:['mobile']" missingMessage="专家手机号"></input>
	        </div>
	         <div class="ftitle">
	            <label for="subject">专家地域:</label>
	             <div style="float:left;margin-left:30px;">
	           		<!-- <label for="privinceA">省:</label> -->
		            <select class="easyui-combobox " id="provinceCodeA" name="provinceCode"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="cityCodeA" name="cityCode"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
	            </div>
	        </div>
	         <div class="ftitle">
	            <label for="addFormAddress" >详细地址:</label>
	            <input class="easyui-validatebox commonInput" type="text" name="address" id="addressA"  data-options="multiline:true,required:true,validType:['length[0,40]']"  missingMessage="专家详细地址"></input>
	        </div>
	      </form>
    </div>
     <!-- 修改图谜字谜专家数据-->
     <div id="updateExpert" class="easyui-dialog"  title="修改图谜字谜专家信息" style="width:600px;height:480px;padding:10px;top:10px;"
            data-options="
               modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateExpert();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateExpert').dialog('close');
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeU">专家登录名:</label>
	            <input type="hidden" name="id" id="idA"/>
		            <input class="easyui-validatebox commonInput" type="text" id="codeU" name="code"   readonly="readonly" 
		               ></input>
	        </div>
	        <div class="ftitle">
	            <label for="nameU">专家姓名:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="nameU" name="name" data-options="required:true"
	              missingMessage="专家姓名不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="passwordU">登录密码:</label>
	            <input class="easyui-validatebox commonInput" type="password" id="passwordU" name="password" data-options="required:true"
	            validType="length[6,20]"  missingMessage="登录密码不可以为空" onblur="$('#ffUpdate').form('enableValidation').form('validate');"></input>
	        </div>
	         <div class="ftitle">
	            <label for="passwordA">确认密码:</label>
	            <input class="easyui-validatebox commonInput" type="password" id="identityPasswordU"  data-options="required:true"
	            validType="equalTo['#passwordU']"   missingMessage="两次输入密码不匹配" ></input>
	        </div>
	        <div class="ftitle">
		            <label for="figureOrPuzzlesU">图谜/字谜:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="figureOrPuzzlesU" name="figureOrPuzzles"  data-options="editable:false" style="width:300px;" >
				          	 <option value="0" checked>全部</option>
				          	  <option value="1" >图谜</option>
				          	  <option value="2">字谜</option>
						</select>
		             </div>
				           
			 </div>
	         <div class="ftitle">
		            <label for="lotteryTypeU">彩种分类:</label>
		             <div style="float:left;margin-left:30px;">
		             	 <select class="easyui-combobox " id="lotteryTypeU" name="lotteryType"  data-options="editable:false" style="width:300px;" >
				          	 <option value="0" checked>全部</option>
				          	  <option value="1" >体彩</option>
				          	  <option value="2">福彩</option>
						</select>
		             </div>
				           
			 </div>
			 <div class="ftitle">
	        	<label for="telephoneU" >用户电话:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="telephoneU" name="telephone"  data-options="required:true,validType:['mobile']" missingMessage="专家手机号"></input>
	        </div>
	         <div class="ftitle">
	            <label for="subject">专家地域:</label>
	             <div style="float:left;margin-left:30px;">
	           		<!-- <label for="privinceA">省:</label> -->
		            <select class="easyui-combobox " id="provinceCodeU" name="provinceCode"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="cityCodeU" name="cityCode"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
	            </div>
	        </div>
	         <div class="ftitle">
	            <label for="addFormAddressU" >详细地址:</label>
	            <input class="easyui-validatebox commonInput" type="text" name="address" id="addressU"  data-options="multiline:true,required:true,validType:['length[0,40]']"  missingMessage="站点详细地址"></input>
	        </div>
	      </form>
    </div>
</body>
	
	
</html>