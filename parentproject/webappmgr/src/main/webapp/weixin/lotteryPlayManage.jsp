<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>补录信息管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/weixin/js/lotteryPlayManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加补录信息',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	
  	    	addLotteryPlay();
  	    	
  	    }
  	} ];
  	  
  	
		
	</script>
		<style type="text/css">
			.ztree li button.switch {visibility:hidden; width:1px;}
			.ztree li button.switch.roots_docu {visibility:visible; width:16px;}
			.ztree li button.switch.center_docu {visibility:visible; width:16px;}
			.ztree li button.switch.bottom_docu {visibility:visible; width:16px;}
			
			 .ftitle{
	  			width:50%;
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
	  		
	  		/*非validatebox的样式*/
	  		.textbox{
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
		    		<td width="7%" class="td_font">彩种名称：</td>
		    		<td width="15%">
		    			<input id="nameC" class="input_border"  type="text" name="nameC"  />  
		    		</td>
		    		<td width="7%" class="td_font">彩种分类：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox" id="lotteryTypeC" name="lotteryTypeC" style="width:100px;">
								<option value="">全部</option>
								<option value="1">体彩</option>
								<option value="2">福彩</option>
						</select>
		    		</td>
		    		<td width="7%" class="td_font">省：</td>
		    		<td width="15%">
		    			<select class="easyui-combobox" id="privinceC" name="privinceC" style="width:100px;">
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
    	 <table id="datagrid" class="easyui-datagrid"  title="补录信息数据列表" >
			</table>
 	</div>  
  
  
    <!-- 添加补录信息弹框 -->
  <div id="addLotteryPlay" class="easyui-dialog" fit="true" title="添加补录信息数据" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddLotteryPlay();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addLotteryPlay').dialog('close');
                        $('#ff').form('clear');//清空表单内容
                    }
                }]
            ">
		
			<div class="easyui-layout" style="height:100%;padding:0;width:100%;" >
	    	 	<div region="north" style="height:45%;" title="补录信息基本内容" hide="false">
	    	 		<form id="ff" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeA">补录信息编码:</label>
				            <input type="hidden" name="id" id="idA"/>
				            <input class="easyui-validatebox commonInput" type="text" id="codeA" name="code" style="width:200px"  
				             data-options="required:true"   ></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">彩种名称:</label>
				            <input class="easyui-validatebox commonInput" type="text" id="nameA" name="name" style="width:200px"  
				             data-options="required:true"   ></input>
				        </div>
				         <div class="ftitle">
				            <label for="priceA">省:</label>
				            <select class="easyui-combobox " id="privinceA" name="province"  
				          	  data-options="editable:false" style="width:200px;" >
							</select>
				        </div>
				         <div class="ftitle">
				            <label for="priceA">补录对应表:</label>
				            <input class="easyui-validatebox commonInput" type="text" id="correspondingTableA" name="correspondingTable" style="width:200px"  
				             data-options="required:true"   ></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">开奖号码个数:</label>
				            <input class="easyui-numberbox" precision="0" type="text" id="lotteryNumberA" name="lotteryNumber" style="width:200px"  
				             data-options="required:true"   validType="number" missingMessage="请输入开奖号码个数" invalidMessage="请输入数字"></input>
				        </div>
				         <div class="ftitle">
				            <label for="priceA">期号长度:</label>
				            <input class="easyui-numberbox" precision="0" type="text" id="issueNumLenA" name="issueNumLen" style="width:200px"  
				             data-options="required:true"   validType="number" missingMessage="请输入期号长度" invalidMessage="请输入数字"></input>
				        </div>
				        <div class="ftitle">
				            <label for="priceA">彩种分类:</label>
				            <select class="easyui-combobox" id="lotteryTypeA" name="lotteryType" style="width:200px;">
								<option value="1">体彩</option>
								<option value="2">福彩</option>
						</select>
				        </div>
				       </form>
	    	 	</div>
	    	 	<div region="center" style="height:55%;padding:0;width:99%;" title="选择补录方案(必选)">
	    	 		<table id="buluPlanA" class="easyui-datagrid" style="width:100%;height:95%;"   ></table>
	    	 	</div>
    		</div>
			
    		
	     
    </div>
     <!-- 修改应用弹框 -->
     <div id="updateLotteryPlay" class="easyui-dialog"  fit="true" title="修改补录信息数据" style="width:800px;height:600px;padding:0px;border:0;top:1px;"
            data-options="
            modal:true,
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdateLotteryPlay();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateLotteryPlay').dialog('close');
                    }
                }]
            ">
	      <div class="easyui-layout" style="height:100%;padding:0;width:100%;" >
	    	 <div region="north" style="height:45%;" title="补录信息基本内容" hide="false">
	    	 		<form id="ffUpdate" method="get" novalidate style="margin-top:5px;">
		    	 		<div class="ftitle">
				            <label for="codeU">补录信息编码:</label>
				            <input type="hidden" name="id" id="idU"/>
				            <input class="easyui-validatebox commonInput" type="text" id="codeU" name="code" style="width:200px"  
				             data-options="required:true"   ></input>
				        </div>
				        <div class="ftitle">
				            <label for="nameU">彩种名称:</label>
				            <input class="easyui-validatebox commonInput" type="text" id="nameU" name="name" style="width:200px"  
				             data-options="required:true"   ></input>
				        </div>
				         <div class="ftitle">
				            <label for="privinceU">省:</label>
				            <select class="easyui-combobox " id="privinceU" name="province"  
				          	  data-options="editable:false" style="width:200px;" >
							</select>
				        </div>
				         <div class="ftitle">
				            <label for="correspondingTableU">补录对应表:</label>
				            <input class="easyui-validatebox commonInput" type="text" id="correspondingTableU" name="correspondingTable" style="width:200px"  
				             data-options="required:true"   ></input>
				        </div>
				        <div class="ftitle">
				            <label for="lotteryNumberU">开奖号码个数:</label>
				            <input class="easyui-numberbox" precision="0" type="text" id="lotteryNumberU" name="lotteryNumber" style="width:200px"  
				             data-options="required:true"   ></input>
				        </div>
				         <div class="ftitle">
				            <label for="priceA">期号长度:</label>
				            <input class="easyui-numberbox" precision="0" type="text" id="issueNumLenA" name="issueNumLen" style="width:200px"  
				             data-options="required:true"   validType="number" missingMessage="请输入期号长度" invalidMessage="请输入数字"></input>
				        </div>
				        <div class="ftitle">
				            <label for="lotteryTypeU">彩种分类:</label>
				            <select class="easyui-combobox" id="lotteryTypeU" name="lotteryType" style="width:200px;">
								<option value="1">体彩</option>
								<option value="2">福彩</option>
						</select>
				        </div>
				       </form>
	    	 	</div>
	    	 	<div region="center" style="height:55%;padding:0;width:99%;" title="选择补录方案(必选)">
	    	 		<table id="buluPlanU" class="easyui-datagrid" style="width:100%;height:95%;"  ></table>
	    	 	</div>
    		</div>
    </div>
    
   
</body>
	
	
</html>