var idArr = [];//选中的应用id数据

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			initAppList('appC','','',true);//初始化上级应用数据下拉列表
			
			bindComboboxChange();//为应用省份绑定区域级联事件
			initDatagrid();//初始化数据列表
			
			idArr = [];
		});




/**
 * 初始化所属应用的combobox
 * @param appId:所属应用标签id
 * @param addOrUpdate：“add” 或 “update”
 * @param isAll:查询的返回数据是否带“全部”
 * @param oldValue:应用版本原来选中的上级应用版本值
 */
function initAppList(appId,addOrUpdate,oldValue,isAll)
{
	$('#'+appId).combobox('clear');//清空combobox值
	
	var data = new Object();
	
	data.isAll = isAll;
	data.checkCity = true;//移除区域信息拥有“省”和“市”的应用
	
	$('#'+appId).combobox({
			queryParams:data,
			method:'get',
			url:contextPath+'/appversion/getAppcomboList.action',
			valueField:'id',
			textField:'appName',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
                 if (data1.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#"+appId).combobox('select',data1[data1.length-1].id);
                 }
                 else
                 if (data1.length > 0 && "update" == addOrUpdate)
            	 {
                	 $("#"+appId).combobox('select',oldValue);
            	 }
                 else
            	 {
                	 $("#"+appId).combobox('select',data1[data1.length-1].id);
            	 }
					
             }
		}); 
}

/**
 * 绑定上级角色下拉框改变事件
 */
function bindComboboxChange()
{
	//添加表单中的省份级联
	$("#appIdA").combobox({

		onSelect: function (rec) {
			
			 var provinceName = rec.provinceName;
			 
			 $("#privinceA").textbox('setText',provinceName);
			 $("#provinceAhidden").val(rec.province);
			 initCities('add','cityA','',rec.province);
		}

		}); 
	
	
	$("#appC").combobox({

		onSelect: function (rec) {
			
			var provinceName = rec.provinceName;
			 
			initCities('','cityC','',rec.province);
		}

		}); 
	
	
	$("#cityA").combobox({

		onSelect: function (rec) {
			
			$("#unitPriceA").val("");
		}

		}); 
	$("#cityU").combobox({

		onSelect: function (rec) {
		
			$("#unitPriceU").val("");
		}

		}); 
	
	
}

/**
 * 初始化省下拉框
 * @param addOrUpdate
 * @param provinceId
 * @param pcode
 */
function initAppUpriceProvince(addOrUpdate,provinceId,pcode)
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
	if(""!=addOrUpdate)
		{
			data.isHasall=false;//在添加和修改应用的区域单价时，必须要设置到市，要是读全省的信息的话就直接读应用的默认单价
		}
	else
		{
			data.isHasall=true;
		}
	$('#'+cityId).combobox({
			queryParams:data,
			url:contextPath+'/product/getCityList.action',
			valueField:'ccode',
			textField:'cname',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
                 if (data1.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#"+cityId).combobox('select',data1[data1.length-1].ccode);
                 }
                 else
                	 {
	                	 if (data1.length > 0 && "" == addOrUpdate)
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
	                    		 	$("#"+cityId).combobox('setValue', oldccode);
	                    		 }
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
	//重置应用列表
	initAppList('appC','','',true);//初始化上级应用数据下拉列表
	$('#cityC').combobox('clear');
}




//关闭弹框
function closeDialog()
{
	$("#addAppUnitPrice").dialog('close');//初始化添加角色弹框关闭
	$("#updateAppUnitPrice").dialog('close');
}

/**
 * 初始化应用列表数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.appId = $("#appC").combobox('getValue');//获取模糊查询条件“应用名称”
	params.city = $("#cityC").combobox('getValue');//获取模糊查询条件“应用编码”
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/appUprice/getAppUnitPriceList.action',//'datagrid_data1.json',
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
				{field:'appId',hidden:true},
		        {field:'appName',width:120,title:'应用名称',align:'center'},
				{field:'provinceName',title:'省',width:80,align:'center'},
				{field:'cityName',title:'市',width:80,align:'center'},
				{field:'unitPrice',title:'单价',width:80,align:'center'},
				{field:'createrTime',title:'创建时间',width:130,align:'center'},
				{field:'opt',title:'操作',width:160,align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateAppUnitPrice(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteAppUnitPrice(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
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
	        
	        
	    }
	});
}




/**
 *应用修改
 */
function updateAppUnitPrice(id)
{
	var url = contextPath + '/appUprice/getDetailAppUprice.action';
	var data1 = new Object();
	data1.id=id;//应用的id
	
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "get",
	        url: url,
	        data:data1,
	        dataType: "json",
	        success: function (data) {
		        	
					$('#ffUpdate').form('load',{
						id:data.id,
						appId:data.appId,
						province:data.province,
						city:data.city,
						unitPrice:data.unitPrice
						
					});
					 $("#appIdU").textbox('setText',data.appName);
					 $("#privinceU").textbox('setText',data.provinceName);
					 $("#privinceUhidden").val(data.province);
					//初始化市级区域combobox
					initCities('update','cityU',data.city,data.province);
			
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/menu/error.action";
	        }
		});
		
		
		$("#updateAppUnitPrice").dialog('open');
	
		
}

/**
 * 校验是否为当前应用在此区域设置过单价
 * @param id
 * @param appId
 * @param province
 * @param city
 * @returns
 */
function checkAppUprice(id,appId,province,city)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = $("#"+id).val();
	if('appIdUhidden' == appId)
		{
			data.appId = $("#"+appId).val();
		}
	else
		{
			data.appId = $("#"+appId).combobox('getValue');
		}
	
	data.province =$("#"+province).val();
	data.city = $("#"+city).combobox('getValue');;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/appUprice/checkAppUprices.action',
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

function submitAddappUnitPrice()
{
	$('#ff').form('submit',{
		url:contextPath+'/appUprice/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			var flagOnly = !checkAppUprice('idA','appIdA','provinceAhidden','cityA');
			if(!flagOnly)
			{
				$.messager.alert('提示',"当前应用在此区域已经设置过单价！");
			}
			if($('#ff').form('enableValidation').form('validate') &&flagOnly)
				{
					flag = true;
				}
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addAppUnitPrice").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	initDatagrid();
	    	
	    	
	    }
	});
}

function submitUpdateappUnitPrice()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/appUprice/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			var flagOnly =!checkAppUprice('idU','appIdUhidden','provinceUhidden','cityU');
			if(!flagOnly)
				{
					$.messager.alert('提示',"当前应用在此区域已经设置过单价！");
				}
			if($('#ffUpdate').form('enableValidation').form('validate') &&flagOnly)
				{
					flag = true;
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateAppUnitPrice").dialog('close');
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
function deleteAppUnitPrice(id)
{
	var url = contextPath + '/appUprice/deleteAppUprices.action';
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
				$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用已上架,不可删除!");
				deleteFlag = false;
			}
		else
			if('3' == appStatus)//应用当前状态为”更新“
				{
					$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用为更新状态,不可删除!");
					deleteFlag = false;
				}
			else
			{
				var delFlag = checkAppIsableDel(rows[i].id);//若返回true则不可以删除
				if(!delFlag)
					{
						deleteFlag = false;
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
        		rules.checkAname.message = "当前应用名称已存在"; 
        		
                return !checkAppName($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});





//校验应用名称（应用名称全局唯一）
function checkAppName(id,name)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.name = name;
	
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

