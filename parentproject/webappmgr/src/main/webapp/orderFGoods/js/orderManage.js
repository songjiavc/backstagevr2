var proOfgoods  = new map();

$(document).ready(function(){
	
			closeDialog();
			initDatagrid();//初始化数据列表
			bindStationCombobox();
			getLoginuserRole();
			
		});


/**
 * 初始化站点下拉框（加载站点的约束条件：1.上级代理下属的所有站点）
 */
function initStationList(stationinput,stationId)
{
	var data = new Object();
	
	$('#'+stationinput).combobox({
		queryParams:data,
		url:contextPath+'/order/getStationList.action',
		valueField:'id',
		textField:'stationNumber',
		 onLoadSuccess: function (data1) { //数据加载完毕事件
			 $('#'+stationinput).combobox('setValue',stationId);//初始化选中的stationlist时用setvalue，避免触发级联事件
				
         }
	}); 
}

/**
 * 获取当前登录用户的角色
 */
function getLoginuserRole()
{
	var isProxy = false;//是否拥有代理角色
	var isFinancialManager = false;//是否拥有财务管理员角色
	var currentId = "";
	var returnArr = new Array();
	
	var data1 = new Object();
	var url = contextPath + '/order/getLoginuserRole.action';
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	isProxy = data.proxy;
        	isFinancialManager = data.financialManager;
        	currentId = data.message;
        	
        	returnArr.push(isProxy);
        	returnArr.push(isFinancialManager);
        	returnArr.push(currentId);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        		window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	
	return returnArr;
	
}

/**
 * 清空全局变量
 */
function clearGoodsArray()
{
	goodsList = new Array();
	proOfgoods  = new map();
	countPrice = 0;//清零商品总价
	$('#goodsDatagridU').datagrid('loadData', { total: 0, rows: [] });//清空商品datagrid内容，避免异常，在再次打开弹框时，可以重新加载内容
	$('#goodsPTDatagridD').datagrid('loadData', { total: 0, rows: [] });//清空商品datagrid内容，避免异常，在再次打开弹框时，可以重新加载内容
	$('#goodsDatagridD').datagrid('loadData', { total: 0, rows: [] });//清空商品datagrid内容，避免异常，在再次打开弹框时，可以重新加载内容
}

/**
 * 为站点下拉框绑定级联事件
 */
function bindStationCombobox()
{
	//给站点更改绑定级联事件
	$("#userYearU").combobox({

		onSelect: function (rec){
			
				var sellprice = calculatePrice(rec.id);
				
				$("#priceU").textbox('setValue',sellprice);
			}

	}); 

}


/**
 * 
 * @param userYearId:选择的使用年限id
 * @param stationId：通行证id
 * @param indexrow：选中app行索引
 * @returns
 */
function calculatePrice(userYearId)
{
	var param = new Object();
	param.appId = $("#appIdU").val();
	param.stationId = $("#stationIdU").val();
	param.province = $("#appprovinceU").val();
	param.city = $("#appCityU").val();
	param.useYearId = userYearId;
	
	var returnPrice;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:param,
        url: contextPath+'/order/calculateSellprice.action',
        dataType: "json",
        success: function (data) {
        		
        	returnPrice = data.message;
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return returnPrice;
	
}

/**
 * 重置
 */
function reset()
{
	//重置订单名称
	$("#orderNameC").val("");
}




//关闭弹框
function closeDialog()
{
	$("#updateOrders").dialog('close');//修改订单详情dialog
	$("#detailOrders").dialog('close');//财务管理员查看订单详情dialog
	$("#detailPTOrders").dialog('close');//普通用户查看订单详情dialog
}

/**
 * 初始化订单列表数据
 */
function initDatagrid()
{
	var roleArr = getLoginuserRole();//获取当前登录用户的角色(财务/代理)
	var isProxy = roleArr[0];//是否拥有代理角色
	var isFinancialManager = roleArr[1];//是否拥有财务管理员角色
	var currentId = roleArr[2];
	
	var params = new Object();
	params.orderName = $("#orderNameC").val().trim();
	params.isProxy = isProxy;
	params.isFinancialManager = isFinancialManager;
	params.currentId = currentId;
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/order/getOrdersList.action',//'datagrid_data1.json',
		method:'get',
		border:false,
		singleSelect:false,
		fit:true,//datagrid自适应
		fitColumns:true,
		pagination:true,
		collapsible:false,
		toolbar:toolbar,
		columns:[[
				{field:'ck',checkbox:true},
				{field:'id',hidden:true},
				{field:'status',hidden:true},
				{field:'creater',hidden:true},
				{field:'code',title:'订单编码',width:120,align:'center'},
		        {field:'name',width:120,title:'订单名称',align:'center'},
				{field:'price',title:'订单金额(元)',width:60,align:'center'},
//				{field:'payMode',title:'支付方式',width:100,align:'center',  
//		            formatter:function(value,row,index){  
//		                var showPaymode = "";
//		                if("0"==value)
//		                	{
//		                		showPaymode = "现金支付";
//		                	}
//		                else if("1"==value)
//		                	{
//		                		showPaymode = "转账支付";
//		                	}
//		                return showPaymode;  
//		            }  },
				{field:'creator',title:'创建人',width:70,align:'center'},
				{field:'createTime',title:'创建时间',width:130,align:'center'},
				{field:'operator',title:'操作人',width:70,align:'center'},
				{field:'statusName',title:'状态',width:100,align:'center'},
				{field:'opt',title:'操作',width:200,align:'center',  
		            formatter:function(value,row,index){  
		            	
		            	
		            	var status = row.status;
		            	var ordercreater = row.creater;
		                var btn = ''
		                	if(isProxy && ('01'==status||'02'==status) /*&& currentId==ordercreater*/)//当前角色为“代理”且订单状态为“代理保存订单”或“财务管理员驳回”时，显示以下按钮
		                		{
		                			btn=btn+'<a class="editcls" onclick="updateOrders(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="编辑">编辑</a>'//代理编辑
				                	+'<a class="deleterole" onclick="deleteOrders(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="删除">删除</a>'//代理删除
				                	+'<a class="submitOrder" onclick="approveOrders(&quot;'+row.id+'&quot;,1)" href="javascript:void(0)" title="提交">提交</a>'//代理提交
		                		}
		                	else if(isFinancialManager && '11'==status)//当前角色为“财务管理员”且订单状态为“提交财务管理员审批”时，显示以下按钮
	                			{//财务管理员必须只有一个
	                				btn=btn+'<a class="detailcls" onclick="viewOrdersDetail(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="查看详情">详情</a>'//财务管理员可以查看订单详情但是不可以修改内容，可以审批
				                	+'<a class="rejectOrder" onclick="approveOrders(&quot;'+row.id+'&quot;,3)" href="javascript:void(0)" title="审批驳回">驳回</a>'//财务管理员驳回
				                	+'<a class="stopOrder" onclick="approveOrders(&quot;'+row.id+'&quot;,4)" href="javascript:void(0)" title="不通过">不通过</a>'//财务管理员不通过，终止订单的审批流程且流程不可恢复
				                	+'<a class="throughOrder" onclick="approveOrders(&quot;'+row.id+'&quot;,2)" href="javascript:void(0)" title="审批通过">通过</a>';//财务管理员审批通过
	                			}
		                		else
	                			{
	                				btn=btn+'<a class="detailcls" onclick="viewPTOrdersDetail(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="查看详情">详情</a>';//财务管理员可以查看订单详情但是不可以修改内容，可以审批
	                			}
		                	
		                return btn;  
		            }  
		        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.detailcls').linkbutton({text:'详情',plain:true,iconCls:'icon-edit'}); 
	        $('.submitOrder').linkbutton({text:'提交',plain:true,iconCls:'icon-edit'});  
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        $('.rejectOrder').linkbutton({text:'驳回',plain:true,iconCls:'icon-remove'});  
	        $('.throughOrder').linkbutton({text:'通过',plain:true,iconCls:'icon-remove'});
	        $('.stopOrder').linkbutton({text:'不通过',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="10">没有数据</td></tr>');
			}
	        
	        
	    },
	    rowStyler:function(index,row){//设置行样式
	    		
	    	}
	});
}

/**
 * 处理订单状态
 * @param orderId
 * @param operortype（1：代理提交  2：财管审批通过 3：财管审批驳回 4：财管不通过）
 */
function approveOrders(orderId,operortype)
{
	var data1 = new Object();
	
	data1.operortype = operortype;
	data1.orderId = orderId;
	
	var url = contextPath + '/order/approveOrders.action';
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	initDatagrid();
        	$.messager.alert('提示', data.message);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
}

/**
 * 财管在弹框中审批订单
 * @param operortype
 */
function approveOrdersInDialog(operortype)
{
	var orderId = $("#idD").val();
	approveOrders(orderId, operortype);
	$('#detailOrders').dialog('close');//关闭查看订单详情的dialog
	initDatagrid();
}

/**
 * 查看订单详情（财务管理员权限）
 * @param orderId
 */
function viewOrdersDetail(orderId)
{
	
	var url = contextPath + '/order/getDetailOrders.action';
	var data1 = new Object();
	data1.id=orderId;//订单id
	
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "get",
	        url: url,
	        data:data1,
	        dataType: "json",
	        success: function (data) {
	        	
					$('#ffDetail').form('load',{
						id:data.id,
						code:data.code,
						appName:data.appName,//购买的应用的名称
						creator:data.creator,
						price:data.price,
						price:data.price,
						appprovince:data.appprovince,
						stationCode:data.stationCode,
						userYearName:data.userYearName
					});
					
					
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
		
		
	
	$('#detailOrders').dialog('open');//打开查看订单详情的dialog
}


/**
 * 查看普通用户订单详情（普通用户权限）
 * @param orderId
 */
function viewPTOrdersDetail(orderId)
{
	
	var url = contextPath + '/order/getDetailOrders.action';
	var data1 = new Object();
	data1.id=orderId;//订单id
	
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "get",
	        url: url,
	        data:data1,
	        dataType: "json",
	        success: function (data) {
	        	
					$('#ffPTDetail').form('load',{
						id:data.id,
						code:data.code,
						appName:data.appName,//购买的应用的名称
						creator:data.creator,
						price:data.price,
						price:data.price,
						appprovince:data.appprovince,
						stationCode:data.stationCode,
						userYearName:data.userYearName
					});
					
			
					
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
		
		
	
	$('#detailPTOrders').dialog('open');//打开查看订单详情的dialog
}




/**
 * 修改订单
 * @param id
 */
function updateOrders(id)
{
	var url = contextPath + '/order/getDetailOrders.action';
	var data1 = new Object();
	data1.id=id;//权限的id
	
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "get",
	        url: url,
	        data:data1,
	        dataType: "json",
	        success: function (data) {
	        	
					$('#ffUpdate').form('load',{
						id:data.id,
						code:data.code,
						appName:data.appName,//购买的应用的名称
						creator:data.creator,
						price:data.price,
						stationCode:data.stationCode,//填充选中订单中站点编码
						price:data.price,
						appCity:data.appCity,
						appId:data.appId,
						appprovince:data.appprovince,
						station:data.stationId
					});
					
					loadUserYear();
					$("#userYearU").combobox('setValue',data.userYear);
					
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
		
		$("#updateOrders").dialog('open');
		
	
}

/**
 * 加载使用年限下拉框数据
 */
function loadUserYear()
{
	$('#userYearU').combobox({
//		queryParams:data,
		url:contextPath + '/order/getUserYearDiscounts.action',
		valueField:'id',
		textField:'userYearName',
		 onLoadSuccess: function (data1) {
			 
		 }
	});
}


/**
 * 获取站点详情
 * @param stationId：站点id
 */
function getDetailStation(stationId)
{
	var returnArr = new Array();
	var url = contextPath + '/station/getStationDetail.action';
	var paramData = new Object();
	paramData.id=stationId;
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        cache:false,
        url: url,
        data:paramData,
        dataType: "json",
        success: function (data) {
			
        	var province = data.addFormProvince;
        	var city = data.addFormCity;
        	var stationType = data.addFormStationStyle;//站点类型：1：体彩 2：福彩
        	var stationNumber = data.addFormStationNumber;//站点号
        	
        	returnArr.push(province);//站点所属省
        	returnArr.push(city);//站点所属市
        	returnArr.push(stationType);//站点类型：1：体彩 2：福彩
        	returnArr.push(stationNumber);//站点类型：1：体彩 2：福彩
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	window.parent.location.href = contextPath + "/error.jsp";

        }
	});
	
	return returnArr;
}






/**
 * 提交修改购买应用表单
 */
function submitUpdateOrders(operatype)
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/order/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			
			if($('#ffUpdate').form('enableValidation').form('validate'))
				{
					flag = true;
				}
			param.operatype = operatype;//0:保存 1：保存并提交
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateOrders").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}

/**
 * 删除商品数据
 * @param id
 */
function deleteOrders(id)
{
	var url = contextPath + '/order/deleteOrders.action';
	var data1 = new Object();
	
	var codearr = [];
	codearr.push(id);
	
	data1.ids=codearr.toString();
		
	if(codearr.length>0)
	{
		var finishFlag = checkOrderFinish(id);
		if(!finishFlag)
			{
				$.messager.confirm("提示", "您确认删除选中数据？", function (r) {  
			        if (r) {  
				        	$.ajax({
				        		async: false,   //设置为同步获取数据形式
				                type: "post",
				                url: url,
				                data:data1,
				                dataType: "json",
				                success: function (data) {
				                	initDatagrid();
				                	$.messager.alert('提示', data.message);
				                },
				                error: function (XMLHttpRequest, textStatus, errorThrown) {
				                    window.parent.location.href = contextPath + "/error.jsp";
				                }
				           });
				        	
			        }  
			    });  
			}
		else
			{
				$.messager.alert('提示', "待删除订单已完成审批，不可进行删除操作!");
			}
		
	}
	else
	{
		$.messager.alert('提示',"请选择数据后操作!");
	}
	
}


/**
 * 批量删除商品数据
 */
function deleteOrdersList()
{
	var url = contextPath + '/order/deleteOrders.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	
	for(var i=0; i<rows.length; i++)
	{
		var finishFlag = checkOrderFinish(rows[i].id);
		if(finishFlag)
			{
				deleteFlag = false;
				$.messager.alert('提示', "待删除订单中订单编码为："+rows[i].code+"已完成审批，不可以被删除");
				break;
			}
		codearr.push(rows[i].id);//code
	}
	
	if(deleteFlag)//选中的待删除权限中没有拥有子级权限的权限时可以进行删除操作
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台
				
				$.messager.confirm("提示", "您确认删除选中数据？", function (r) {  
			        if (r) {  
			        	
				        	$.ajax({
				        		async: false,   //设置为同步获取数据形式
				                type: "post",
				                url: url,
				                data:data1,
				                dataType: "json",
				                success: function (data) {
				                	initDatagrid();
				                	
				                	$.messager.alert('提示', data.message);
				                	
				                },
				                error: function (XMLHttpRequest, textStatus, errorThrown) {
				                    window.parent.location.href = contextPath + "/error.jsp";
				                }
				           });
				        	
			        }  
			    });  
				
			}
			else
			{
				$.messager.alert('提示', "请选择数据后操作!");
			}
		}
}

/*********校验************/

/**
 * 自定义校验商品名称？？？（暂定商品名称校验规则：全局唯一）
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkAname: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAname.message = "当前商品名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAname.message = "当前商品名称已存在"; 
        		
                return !checkProName($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});

/**
 * 自定义校验商品编码
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkCodes: {//自定义校验code
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>15){  
        		rules.checkCodes.message = "当前商品编码不可为空且长度不可以超过15个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkCodes.message = "当前商品编码已存在"; 
                return !checkCode($("#"+param[1]).val(),value);
    		}
        	
        	
        }
    }
});

/**
 * 校验当前订单是否审批完成
 * @param id
 * @returns {Boolean}
 */
function checkOrderFinish(id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/order/checkOrderStatus.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	
        	flag = data.exist;//true:订单审批已完成
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return flag;
}




/**
 * 校验code,name唯一性
 */
function checkCode(id,code)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.code = code;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/goods/checkCode.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	if(data.exist)//若data.isExist==true,则当前校验值已存在，则不可用使用
        		{
        			flag = true;
        		}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return flag;
}

//校验产品名称（暂定规则：全局唯一）
function checkProName(id,name)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.name = name;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/goods/checkGoodsName.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	if(data.exist)//若data.isExist==true,则当前校验值已存在，则不可用使用
        		{
        			flag = true;
        		}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return flag;
}


/**
 * 自定义校验商品销售价格
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkSellprice: {//自定义校验sellprice
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkSellprice.message = "当前商品销售价格不可为空且长度不可以超过10个字符";  
        		productList.remove(param[0]);//row.id
				$('#'+param[2]).datagrid('unselectRow', param[1]);//productDatagrid,index
                return false;  
            }
        	else
    		{
        		rules.checkSellprice.message = "当前商品销售价格不符合金额的输入规则"; 
        		var flag = checkEditorSellprice(value);
        		if(!flag)
        			{
	        			productList.remove(param[0]);//row.id
	    				$('#'+param[2]).datagrid('unselectRow', param[1]);//productDatagrid,index
        			}
        		
                return flag;
    		}
        	
        	
        }
    }
});

/**
 * 自定义校验商品使用天数
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkProbation: {//自定义校验probation
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkProbation.message = "当前商品试用天数不可为空且长度不可以超过10个字符";  
        		productList.remove(param[0]);//row.id
				$('#'+param[2]).datagrid('unselectRow', param[1]);//productDatagrid,index
                return false;  
            }
        	else
    		{
        		rules.checkProbation.message = "当前商品试用天数只可以输入0或正整数"; 
                var flag = checkEditorProbation(value);
        		if(!flag)
        			{
	        			productList.remove(param[0]);//row.id
	    				$('#'+param[2]).datagrid('unselectRow', param[1]);//productDatagrid,index
        			}
        		
                return flag;
    		}
        	
        	
        }
    }
});

/****封装map****/
function map()
{
	this.keys = [];
    this.data = {};
 
    /**
     * 放入一个键值对
     * @param {String} key
     * @param {Object} value
     */
    this.put = function(key, value) {
        if (this.data[key] == null) {
            this.keys.push(key);
        }
        this.data[key] = value;
    };
 
    /**
     * 获取某键对应的值
     * @param {String}  key
     * @return {Object} value
     */
    this.get = function(key) {
        return this.data[key];
    };
 
    /**
     * 是否包含该键
     */
    this.contain = function(key) {
        
        var value = this.data[key];
        if (value)
            return true;
        else
            return false;
    };
    
    this.getKeys = function()
    {
    	return keys;
    };
 
    /**
     * 删除一个键值对
     * @param {String} key
     */
    this.remove = function(key) {
        for(var index=0;index<this.keys.length;index++){
            if(this.keys[index]==key){
                this.keys.splice(index,1);
                break;
            }
        }
        //this.keys.remove(key);
        this.data[key] = null;
    };
}

