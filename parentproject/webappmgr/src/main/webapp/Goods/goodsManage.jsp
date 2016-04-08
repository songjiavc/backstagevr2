<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>商品管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath() %>/css/ztree/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <jsp:include page="../common/top.jsp" flush="true" /> 
    <script src="<%=request.getContextPath() %>/Goods/js/goodsManage.js" type="text/javascript"></script>
    
    <script type="text/javascript">
  	var toolbar = [{
  	    text:'添加商品',
  	    iconCls:'icon-add',
  	    handler:function(){
  	    	 //初始化上级权限下拉框值
  	    	clearProductList();
  	    	initProvince('add','privinceA','');//默认选中全部，则全部下是没有市数据的
  	    	initProductDatagrid('privinceA','cityA','productDatagridA');//初始化待选择产品列表
  	    	generateCode();//生成商品编码
  	    	$("#addProduct").dialog('open');
  	    	
  	    }
  	},{
  	    text:'批量删除商品',
  	    iconCls:'icon-remove',
  	    handler:function(){
  	    	deleteGoodsList('0');
  	    	}
  	},{
  	    text:'批量上架',
  	    iconCls:'icon-redo',
  	    handler:function(){
  	    	deleteGoodsList('1');
  	    	}
  	},{
  	    text:'批量下架',
  	    iconCls:'icon-undo',
  	    handler:function(){
  	    	deleteGoodsList('2');
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
	  			float : left;
	  			margin-bottom: 20px;
	  			font-family:'微软雅黑',
	  		}
	  		.ftitle label{
	  			float : left;
	  			margin-left: 30px;
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
		    		<td width="7%" class="td_font">商品名称：</td>
		    		<td width="15%">
		    			<input id="goodsNameC" class="input_border"  type="text" name="goodsNameC"  />  
		    		</td>
		    		<td width="7%" class="td_font">商品编码：</td>
		    		<td width="15%">
		    			<input type="text" class="input_border"  name="goodscodeC" id="goodscodeC" >
		    		</td>
		    		<td width="7%"  class="td_font">商品地域：</td>
		    		<td width="35%">
		    			 <label for="privinceC">省:</label>
		    			 <select class="easyui-combobox " id="privinceC" name="privinceC"  
		          	 		 data-options="editable:false" style="width:150px;" >
						</select>
						 <label for="cityC">市:</label>
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
    	 <table id="datagrid" class="easyui-datagrid"  title="商品列表" >
			</table>
 	</div>  
  
  
    <!-- 添加商品弹框 -->
  <div id="addProduct" class="easyui-dialog" title="添加商品" style="width:800px;height:500px;padding:10px;top:40px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitAddgoods();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addProduct').dialog('close');
                        clearProductList();
                        $('#ff').form('clear');//清空表单内容
                    }
                }]
            ">
		<form id="ff" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeA">商品编码:</label>
	            <input type="hidden" name="id" id="idA"/>
	            <div style="float:left;margin-left: 30px;">
		            <input name="code" id="codehidden" type="hidden" >
		            <input class="easyui-textbox" type="text" id="codeA"  style="width:200px"  readonly="readonly" 
		               ></input>
		        </div>
	        </div>
	        <div class="ftitle">
	            <label for="nameA">商品名称:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="nameA" name="name" data-options="required:true"
	             validType="checkAname['#nameA','idA']" missingMessage="商品名称不可以为空" ></input>
	        </div>
	        <div class="ftitle">
	            <label for="priceA">商品价格:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="priceA" name="price" data-options="required:true"
	             validType="money" missingMessage="商品价格不可以为空" ></input>(元)
	        </div>
	        <div class="ftitle">
	            <label for="goodTypeA">商品彩种:</label>
	            <input type="hidden" name="goodType" id="goodTypeAhidden"/>
	            <input class="easyui-validatebox commonInput" readonly="readonly" type="text" id="goodTypeA"  
	              ></input>
	        </div>
	        <div class="ftitle">
	            <label for="subject">商品地域:</label>
	            <div style="float:left;margin-left:30px;">
	           		<!-- <label for="privinceA">省:</label> -->
		            <select class="easyui-combobox " id="privinceA" name="privince"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="cityA" name="city"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
	            </div>
	        </div>
	        <div class="ftitle">
	            <label for="status">状态:</label>
	            <div style="float:left;margin-left: 10%;">
		            <input class="easyui-validatebox" type="radio" name="status"  value="1" >上架</input>
		            <input class="easyui-validatebox" style="margin-left:10px;" type="radio" name="status" value="2">下架</input>
		            <input class="easyui-validatebox" style="margin-left:10px;" type="radio" name="status" value="0" checked>待上架</input>
	        	</div>
	        </div>
	         <div class="ftitle">
	         	 <label for="goodsDespritionA">商品描述:</label>
	         	 <!-- ※textarea两个标签间有空格时焦点会不落在首字符上※ -->
	         	 <textarea id="goodsDespritionA" name="goodsDesprition" class="easyui-validatebox" 
	         	 validType="length[0,100]" style="resize:none;width:400px;height:50px;margin-left:30px;border-radius:5px;"></textarea>
	         </div>
	         <div class="ftitle">
	            <label for="product">选择产品:</label>
	            <div style="float:left;margin-left:30px;width:700px;">
	            	<table id="productDatagridA" class="easyui-datagrid" style="width:700px;"  title="产品列表" >
					</table>
	            </div>
	        </div>
	      </form>
    </div>
     <!-- 修改商品弹框 -->
     <div id="updateProduct" class="easyui-dialog" title="修改商品信息" style="width:800px;height:500px;padding:10px;top:40px;"
            data-options="
                iconCls: 'icon-save',
                buttons: [{
                    text:'提交',
                    iconCls:'icon-ok',
                    handler:function(){
                        submitUpdategoods();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#updateProduct').dialog('close');
                        clearProductList();
                    }
                }]
            ">
		<form id="ffUpdate" method="get" novalidate>
	        <div class="ftitle">
	            <label for="codeU">商品编码:</label>
	            <input type="hidden" name="id" id="idU"/>
	            <input class="easyui-validatebox commonInput" readonly="readonly" type="text" id="codeU" name="code" data-options="required:true"
	              ></input>
	        </div>
	        <div class="ftitle">
	            <label for="nameU">商品名称:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="nameU" name="name" data-options="required:true"
	             validType="checkAname['#nameU','idU']" missingMessage="商品名称不可以为空" ></input>
	        </div>
	       <div class="ftitle">
	            <label for="priceU">商品价格:</label>
	            <input class="easyui-validatebox commonInput" type="text" id="priceU" name="price" data-options="required:true"
	             validType="money" missingMessage="商品价格不可以为空" ></input>(元)
	        </div>
	        <div class="ftitle">
	            <label for="goodTypeU">商品彩种:</label>
	             <input type="hidden" name="goodType" id="goodTypeUhidden"/>
	            <input class="easyui-validatebox commonInput" readonly="readonly" type="text" id="goodTypeU"  
	              ></input>
	        </div>
	        <div class="ftitle">
	            <label for="subject">商品地域:</label>
	            <div style="float:left;margin-left:30px;">
	           		<!-- <label for="privinceA">省:</label> -->
		            <select class="easyui-combobox " id="privinceU" name="privince"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
					<!-- <label for="cityA">市:</label> -->
					<select class="easyui-combobox " id="cityU" name="city"  
		          	  data-options="editable:false" style="width:150px;" >
					</select>
	            </div>
	        </div>
	        <div class="ftitle">
	            <label for="status">状态:</label>
	            <div style="float:left;margin-left: 10%;">
		            <input class="easyui-validatebox" type="radio" name="status"  value="1" checked>上架</input>
		            <input class="easyui-validatebox" style="margin-left:10px;" type="radio" name="status" value="2">下架</input>
		            <input class="easyui-validatebox" style="margin-left:10px;" type="radio" name="status" value="0" checked>待上架</input>
	        	</div>
	        </div>
	         <div class="ftitle">
	         	 <label for="goodsDespritionU">商品描述:</label>
	         	 <textarea id="goodsDespritionU" name="goodsDesprition" class="easyui-validatebox" 
	         	 validType="length[0,100]" style="resize:none;width:400px;height:50px;margin-left:30px;border-radius:5px;"></textarea>
	         </div>
	        <div class="ftitle">
	            <label for="product">选择产品:</label>
	           <div style="float:left;margin-left:30px;width:700px;">
	            	<table id="productDatagridU" class="easyui-datagrid" style="width:700px;"  title="产品列表" >
					</table>
	            </div>
	        </div>
	      </form>
    </div>
</body>
	
	
</html>