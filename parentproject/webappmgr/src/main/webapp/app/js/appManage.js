var idArr = [];//选中的应用id数据

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			initDatagrid();//初始化数据列表
			bindComboboxChange();//为应用省份绑定区域级联事件
			idArr = [];
		});



/**
 * 初始化模糊查询“省”下拉框数据
 */
function initQueryProvince()
{
	$('#privinceC').combobox('clear');//清空combobox值
	
	var data = new Object();
	data.isHasall = true;//包含"全部"
	
	$('#privinceC').combobox({
			queryParams:data,
			url:contextPath+'/product/getProvinceList.action',
			valueField:'pcode',
			textField:'pname',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
				 $('#privinceC').combobox('select',data1[data1.length-1].pcode);
					
             }
		}); 
}

/**
 * 绑定上级角色下拉框改变事件
 */
function bindComboboxChange()
{
	//添加表单中的省份级联
	$("#privinceA").combobox({

		onSelect: function (rec) {
			
			 //加载市级数据
			 initCities('add','cityA','',rec.pcode);
			 $("#ff").form('validate');
		}

		}); 
	//修改表单中的省份级联
	$("#privinceU").combobox({

		onSelect: function (rec) {
			//加载市级数据
			initCities('update','cityU','',rec.pcode);
			 $("#ffUpdate").form('validate');
		}

		}); 
	
	
}

/**
 * 初始化省下拉框
 * @param addOrUpdate
 * @param provinceId
 * @param pcode
 */
function initAppProvince(addOrUpdate,provinceId,pcode)
{
	$('#'+provinceId).combobox('clear');//清空combobox值
	
	var data = new Object();
	data.isHasall = false;//不包含"全部"
	
	$('#'+provinceId).combobox({
			queryParams:data,
			url:contextPath+'/product/getProvinceList.action',
			valueField:'pcode',
			textField:'pname',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
                 if (data1.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#"+provinceId).combobox('select',data1[0].pcode);
                 }
                 else
            	 {
                	//使用“setValue”设置选中值不会触发绑定事件导致多次加载市级数据，否则会多次触发产生错误
            	 	$("#"+provinceId).combobox('setValue', pcode);
            	 }
					
             }
		}); 
}

/**
 * 初始化市数据
 * @param addOrUpdate:标记当前是添加表单操作还是修改表单的操作,值为"add" 和"update"
 * @param cityId:当前操作的form表单的id
 * @param oldccode:应该选中的市数据code
 * @param pcode:级联的上级省code
 */
function initCities(addOrUpdate,cityId,oldccode,pcode)
{
	$('#'+cityId).combobox('clear');//清空combobox值
	var data = new Object();
	
	data.pcode = pcode;
	data.isHasall=true;
	$('#'+cityId).combobox({
			queryParams:data,
			url:contextPath+'/product/getCityList.action',
			valueField:'ccode',
			textField:'cname',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
                 if (data1.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#"+cityId).combobox('setValue',data1[data1.length-1].ccode);
                 }
                 else
            	 {
                	 
                	 if(data1.length > 0 &&"update" == addOrUpdate&&"" == oldccode)
                		 {//在修改表单中级联加载市级数据时也要默认选中全部
                		 	$("#"+cityId).combobox('select',data1[data1.length-1].ccode);
                		 }
                	 else
                		 {//当修改产品初始化市级数据时设置选中当前数据值
                		 	$("#"+cityId).combobox('select', oldccode);
                		 }
            	 }
					
             }
		}); 
}


/**
 * 重置
 */
function reset()
{
	//重置应用名称
	$("#appNameC").val("");
	//重置应用编码
	$("#appcodeC").val("");
}

/**
 * 获取省份的“全部”选项code
 * @returns {String}
 */
function getProvinceAllId()
{
	var url = contextPath + '/menu/getConstant.action';
	var data1 = new Object();
	data1.constantName='PROVINCE_ALL';//省“全部”编码
	
	var proAllId = '';
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
        	proAllId = data.message;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return proAllId;
}



//关闭弹框
function closeDialog()
{
	$("#addApp").dialog('close');//初始化添加角色弹框关闭
	$("#updateApp").dialog('close');
}

/**
 * 初始化应用列表数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.appName = $("#appNameC").val().trim();//获取模糊查询条件“应用名称”
	params.appCode = $("#appcodeC").val().trim();//获取模糊查询条件“应用编码”
	params.lotteryType = $("#lotterytypeC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/app/getAppList.action',//'datagrid_data1.json',
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
				{field:'appStatus',hidden:true},//应用状态(0:待上架1:上架2:下架3:更新)
				{field:'appCode',title:'应用编码',width:'15%',align:'center'},
		        {field:'appName',width:'15%',title:'应用名称',align:'center'},
		        {field:'appMoney',width:'4%',title:'单价',align:'center'},
				{field:'appTypeName',title:'应用状态',width:'8%',align:'center'},
				{field:'lotteryType',width:'7%',title:'彩种',align:'center',  
		            formatter:function(value,row,index){  
		            	var lotteryTypeName ='';
		            	switch(value)
		            	{
		            		case '1':lotteryTypeName='体彩';break;
		            		case '2':lotteryTypeName='福彩';break;
		            	}
		            	return lotteryTypeName;  
		            }  },
				{field:'appDeveloper',title:'应用开发者',width:'8%',align:'center'},
				{field:'provinceName',title:'省',width:'6%',align:'center'},
				{field:'cityName',title:'市',width:'6%',align:'center'},
				{field:'createTime',title:'创建时间',width:'14%',align:'center'},
				{field:'opt',title:'操作',width:'14%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateApp(&quot;'+row.id+'&quot;,&quot;'+row.appStatus+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteApp(&quot;'+row.id+'&quot;,&quot;'+row.appStatus+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="7">没有数据</td></tr>');
			}
	        
	        
	    },
	    rowStyler:function(index,row){//设置行样式
	        if (row.appStatus==1){//上架
	    	 		 return 'background-color:#cbdcf8;color:black;';
			}
    	 	else	if (row.appStatus==2){//下架
				 return 'background-color:#dddcdc;color:black;';
			}
    	 	
			else  if (row.appStatus==0){//待上架
				return 'background-color:#6293BB;color:black;';
			}
	        
			else  if (row.appStatus==3){//更新
				return 'background-color:#FFFF00;color:black;';
			}
				
			},
	});
}




/**
 *应用修改
 */
function updateApp(id,appStatus)
{
	var url = contextPath + '/app/getDetailApp.action';
	var data1 = new Object();
	data1.id=id;//应用的id
	
	var updateFlag = true;
	
	if('1' == appStatus)//应用当前状态为“上架”
	{
		$.messager.alert('提示',"当前待修改应用已上架,不可进行修改操作!");
		updateFlag = false;
	}
	
	if(updateFlag)
		{
				$.ajax({
					async: false,   //设置为同步获取数据形式
			        type: "get",
			        url: url,
			        data:data1,
			        dataType: "json",
			        success: function (data) {
			        	
							$('#ffUpdate').form('load',{
								id:data.id,
								appCode:data.appCode,
								appName:data.appName,
								appStatus:data.appStatus,
								appDeveloper:data.appDeveloper,
								province:data.province,
								city:data.city,
								appMoney:data.appMoney,
								lotteryType:data.lotteryType
								
							});
							
							$("#codeU").textbox('setText',data.appCode);
							//初始化省份combobox
							initAppProvince("update", "privinceU", data.province);
							//初始化市级区域combobox
							initCities('update','cityU',data.city,data.province);
					
			        	
			        },
			        error: function (XMLHttpRequest, textStatus, errorThrown) {
			            window.parent.location.href = contextPath + "/menu/error.action";
			        }
				});
				
				
				$("#updateApp").dialog('open');
		}
	
		
	
		
}

//取消添加应用弹框触发方法
function addDialogCancel()
{
	 $('#addApp').dialog('close');
     $('#ff').form('clear');//清空表单内容
     $('#lotteryTypeA').combobox('setValue','1');
     $('#ff [name="appStatus"]:radio').each(function() {   //设置“待上架”为默认选中radio
	            if (this.value == '0'){   
	               this.checked = true;   
	            }       
	         }); 
}


/**
 * 提交保存应用表单
 */
function submitAddapp()
{
	$('#ff').form('submit',{
		url:contextPath+'/app/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			if($('#ff').form('enableValidation').form('validate') )
				{
					flag = true;
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addApp").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	$("#lotteryTypeA").combobox('setValue',"1");
	    	initDatagrid();
	    	$('#ff [name="appStatus"]:radio').each(function() {   //设置“待上架”为默认选中radio
	            if (this.value == '0'){   
	               this.checked = true;   
	            }       
	         }); 
	    	
	    	
	    }
	});
}

/**
 * 提交修改商品表单
 */
function submitUpdateapp()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/app/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			if($('#ffUpdate').form('enableValidation').form('validate') )
				{
					flag = true;
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateApp").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}

/**
 * 生成应用编码
 */
function generateCode()
{
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/app/generateAppcode.action',
        dataType: "json",
        success: function (data) {
        	
        	var appCode = data.code;
        	$("#codeA").textbox('setText',appCode);
        	$("#codehidden").val(appCode);
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
}

/**
 * 删除应用数据
 * @param id
 * @param appStatus:应用状态（0：待上架1：上架2：下架3：更新）只有在0和2时可以删除，1和3时不可以进行删除操作
 */
function deleteApp(id,appStatus)
{
	var url = contextPath + '/app/deleteApps.action';
	var data1 = new Object();
	var deleteFlag = true;
	
	var codearr = [];
	codearr.push(id);
	
	data1.ids=codearr.toString();
	
	if(codearr.length == 0)
		{
			$.messager.alert('提示',"请选择数据后操作!");
			deleteFlag = false;
		}
	else
		if('1' == appStatus)//应用当前状态为“上架”
			{
				$.messager.alert('提示',"当前待删除应用已上架,不可删除!");
				deleteFlag = false;
			}
	else
		if('3' == appStatus)//应用当前状态为”更新“
			{
				$.messager.alert('提示',"当前待删除应用为更新状态,不可删除!");
				deleteFlag = false;
			}
		else
			{
				var delFlag = checkAppIsableDel(id);//若返回false则不可以删除
				if(!delFlag)
					{
						deleteFlag = false;
						$.messager.alert('提示',"当前待删除应用拥有应用版本数据,不可删除!");
					}
			}
	
	
	if(deleteFlag)
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
		                    window.parent.location.href = contextPath + "/menu/error.action";
		                }
		           });
		        	
	        }  
	    });  
	}
		
	
}

/**
 * 校验当前应用数据下是否拥有应用版本数据
 * @param id
 * @returns {Boolean}
 */
function checkAppIsableDel(id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/app/checkAppIsableDel.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	if(data.exist)//若data.isExist==true,则当前应用拥有下属的应用版本数据不可以被删除
        		{
        			flag = true;
        		}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return flag;
}


/**
 * 批量删除应用数据
 */
function deleteAppList(operaType)
{
	var url = contextPath + '/app/deleteApps.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		
		var appStatus = rows[i].appStatus;
		
		if('1' == appStatus)//应用当前状态为“上架”
			{
				var appcode= rows[i].appCode;
				break;
				$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用已上架,不可删除!");
				deleteFlag = false;
			}
		else
			if('3' == appStatus)//应用当前状态为”更新“
				{
					$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用为更新状态,不可删除!");
					break;
					deleteFlag = false;
				}
			else
			{
				var delFlag = checkAppIsableDel(rows[i].id);//若返回true则不可以删除
				if(!delFlag)
					{
						deleteFlag = false;
						break;
						$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用拥有应用版本数据,不可删除!");
					}
			}
	}
	
	if(deleteFlag)
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台data
				
				var alertMsg = "您确认删除选中数据？";//默认的提示信息为删除数据的提示信息
				
				$.messager.confirm("提示", alertMsg, function (r) {  
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
				                    window.parent.location.href = contextPath + "/menu/error.action";
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


/**
 * 批量更新应用状态
 * @param appStatus
 */
function updateAppStatus(appStatus)
{
	var url = contextPath + '/app/updateAppStatus.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	if('1'==appStatus)//上架
		{
			for(var i=0; i<rows.length; i++)
			{
				codearr.push(rows[i].id);//code
				
				var cappStatus = rows[i].appStatus;
				
				
				/*if('1' == cappStatus)//应用当前状态为“上架”
					{
						var appcode= rows[i].appCode;
						$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用已上架,不可删除!");
						deleteFlag = false;
					}
				else
					if('3' == cappStatus)//应用当前状态为”更新“
						{
							$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用为更新状态,不可删除!");
							deleteFlag = false;
						}*/
			}
		}
	else
		if('2'==appStatus)//下架
		{
			for(var i=0; i<rows.length; i++)
			{
				codearr.push(rows[i].id);//code
				
				var cappStatus = rows[i].appStatus;
				
				
				/*if('1' == cappStatus)//应用当前状态为“上架”
					{
						var appcode= rows[i].appCode;
						$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用已上架,不可删除!");
						deleteFlag = false;
					}
				else
					if('3' == cappStatus)//应用当前状态为”更新“
						{
							$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用为更新状态,不可删除!");
							deleteFlag = false;
						}*/
			}
		}
	
	
	
	if(deleteFlag)
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台data
				data1.appStatus = appStatus;//要变更的应用状态
				
				var alertMsg = "您确认更新选中数据状态？";
				
				$.messager.confirm("提示", alertMsg, function (r) {  
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
				                    window.parent.location.href = contextPath + "/menu/error.action";
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
 * 自定义校验应用名称
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkAname: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAname.message = "当前应用名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAname.message = "当前选中省,此应用名称已存在"; 
        		
                return !checkAppName($("#"+param[1]).val(),value,$("#"+param[2]).combobox('getValue'));
    		}
        	
        }
    }
});





//校验应用名称（应用名称全局唯一）
function checkAppName(id,name,province)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.name = name;
	data.province = province;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/app/checkAppName.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	if(data.exist)//若data.isExist==true,则当前校验值已存在，则不可用使用
        		{
        			flag = true;
        		}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
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

