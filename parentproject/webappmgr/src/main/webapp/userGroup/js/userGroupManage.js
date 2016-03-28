var idArr = [];//选中的应用id
var stationList = new map();//选中的通行证id数组集合

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			initDatagrid();//初始化数据列表
			clearstationList();
			idArr = [];
			bindComboboxChange();//为通行证的模糊查询的省份条件绑定下拉框级联事件
		});



function bindComboboxChange()
{
	//添加表单中的省份级联
	$("#searchFormProvinceA").combobox({

		onSelect: function (rec) {
			 //加载市级数据
			 initCities('searchFormCityA',rec.pcode);
		}

		}); 
	//修改表单中的省份级联
	$("#searchFormProvinceU").combobox({

		onSelect: function (rec) {
			//加载市级数据
			initCities('searchFormCityU',rec.pcode);
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




//关闭弹框
function closeDialog()
{
	$("#addUgroup").dialog('close');//初始化添加角色弹框关闭
	$("#updateUgroup").dialog('close');
}

/**
 * 初始化应用列表数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.userGroupName = $("#userGroupNameC").val().trim();//获取模糊查询条件“应用名称”
	params.userGroupCode = $("#userGroupCodeC").val().trim();//获取模糊查询条件“应用编码”
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/userGroup/getUsergroupList.action',//'datagrid_data1.json',
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
				{field:'userGroupCode',title:'通行证组编码',width:150,align:'center'},
		        {field:'userGroupName',width:120,title:'通行证组名称',align:'center'},
				{field:'createTime',title:'创建时间',width:130,align:'center'},
				{field:'opt',title:'操作',width:160,align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateUgroup(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteUgroup(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="5">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}

/**
 * 初始化省下拉框
 * @param provinceId
 */
function initProvince(provinceId)
{
	$('#'+provinceId).combobox('clear');//清空combobox值
	
	var data = new Object();
	data.isHasall = true;//不包含"全部"
	
	$('#'+provinceId).combobox({
			queryParams:data,
			url:contextPath+'/product/getProvinceList.action',
			valueField:'pcode',
			textField:'pname',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
                 if (data1.length > 0 ) 
                 {
                	 $("#"+provinceId).combobox('select',data1[data1.length-1].pcode);
                 }
					
             }
		}); 
}

/**
 * 初始化市数据
 * @param pcode:级联的上级省code
 */
function initCities(cityId,pcode)
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
                 if (data1.length > 0 ) 
                 {
                	 $("#"+cityId).combobox('setValue',data1[data1.length-1].ccode);
                 }
					
             }
		}); 
}



function dosearch(addOrUpdate)
{
	if('1' == addOrUpdate)
		{
			initStationList($("#idU").val(),'stationDataGridU');
		}
	else
		if('0' == addOrUpdate)
			{
				initStationList('','stationDataGridA');
			}

}



/**
 * 初始化通行证列表数据
 */
function initStationList(id,stationDataGridId)
{
	var params = new Object();
	if('stationDataGridU' == stationDataGridId)
	{
		params.searchFormProvince = $("#searchFormProvinceU").combobox('getValue');
		params.searchFormCity = $("#searchFormCityU").combobox('getValue');
		params.searchFormStyle = $("#searchFormStyleU").combobox('getValue');
		
		var stations = checkStations(id, stationDataGridId);
		
		for (var i = 0; i < stations.length; i++) {
			
			stationId = stations[i].id;
			stationList.put(stationId, stationId);
		}
	}
	else
		if('stationDataGridA' == stationDataGridId)
		{
			params.searchFormProvince = $("#searchFormProvinceA").combobox('getValue');
			params.searchFormCity = $("#searchFormCityA").combobox('getValue');
			params.searchFormStyle = $("#searchFormStyleA").combobox('getValue');
		}
	
	//渲染列表
	$('#'+stationDataGridId).datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url: contextPath + '/station/getStationList.action',
		method:'get',
		border:false,
		singleSelect:false,
		fitColumns:true,
		pagination:true,
		collapsible:false,
		pageSize:5,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[5,10],
		columns:[[
				{field:'id',checkbox:true},
				{field:'stationNumber',title:'站点号',width:'20%',align:'center'},
				{field:'province',title:'省',width:'10%',align:'center'},
				{field:'city',title:'市',width:'10%',align:'center'},
				{field:'stationStyle',title:'站点类型',width:'20%',align:'center'},
				{field:'name',title:'站主名称',width:'20%',align:'center'},
				{field:'telephone',title:'站主电话',width:'20%',align:'center'},
		    ]],
	    onLoadSuccess:function(data){ 
	    	
	    	
	    	var selectedRows = $('#'+stationDataGridId).datagrid('getRows');
	    	if(stationList.keys.length>0)
	    		{
	    			var stationId ;
		    		for(var i=0;i<stationList.keys.length;i++)
	    			{
		    			stationId = stationList.keys[i];
		    			$.each(selectedRows,function(j,selectedRow){
		    				
		    				if(selectedRow.id == stationId){
		    					
		    					 $('#'+stationDataGridId).datagrid('selectRow',j);
		    				}
		    			});
	    			}
	    		}
	    	
	    } ,
	    onSelect:function(index,row){
	    	
	    	if(!stationList.contain(row.id))//新数据
	    		{
	    			stationList.put(row.id, row.id);
	    		}
	    	
	    },
	    onUnselect:function(index,row){
			
			stationList.remove(row.id);
			
		},
		onSelectAll:function(rows){
			$.each(rows,function(i,row){
				
				if(!stationList.contain(row.id))//放入不存在新数据
	    		{
	    			stationList.put(row.id, row.id);
	    		}
			});
		},
		onUnselectAll:function(rows){
			$.each(rows,function(i,row){
				if(stationList.contain(row.id))//移除已存在数据
	    		{
	    			stationList.remove(row.id);
	    		}
				
			});
		}
		
	});
}

function checkStations(id,stationDataGridId)
{
	var data = new Object();
	data.id = id;
	
	var stationList ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/userGroup/getStationOfUsergroup.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	stationList = returndata;
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return stationList;
}

function clearstationList()
{
	stationList = new map();
}



/**
 *通行证组修改
 */
function updateUgroup(id)
{
	clearstationList();
	var url = contextPath + '/userGroup/getDetailUserGroup.action';
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
						userGroupCode:data.userGroupCode,
						userGroupName:data.userGroupName,
						userGroupDescription:data.userGroupDescription//通行证组描述
					});
					//初始化通行证列表数据
					initProvince('searchFormProvinceU');//初始化通行证的查询条件
					$("#searchFormStyleU").combobox('setValue','');
					initStationList(id,'stationDataGridU');
					
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
		
		
		$("#updateUgroup").dialog('open');
	
		
}


/**
 * 提交保存通行证组
 */
function submitAddUgroup()
{
	$('#ff').form('submit',{
		url:contextPath+'/userGroup/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			param.stationdata = JSON.stringify(stationList);
			if($('#ff').form('enableValidation').form('validate')&&stationList.keys.length>0 )
				{
					flag = true;
				}
			else
				if(stationList.keys.length==0)
				{
					flag = false;
					$.messager.alert('提示', "请选择通行证数据!");
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addUgroup").dialog('close');//初始化添加通行证组弹框关闭
	    	
	    	$('#ff').form('clear');//清空表单内容
	    	initDatagrid();
	    	
	    	
	    }
	});
}

/**
 * 提交修改通行证组
 */
function submitUpdateUgroup()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/userGroup/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			param.stationdata = JSON.stringify(stationList);
			if($('#ffUpdate').form('enableValidation').form('validate') &&stationList.keys.length>0 )
				{
					flag = true;
				}
			else
				if(stationList.keys.length==0)
				{
					flag = false;
					$.messager.alert('提示', "请选择通行证数据!");
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateUgroup").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}

///**
// * 生成应用编码
// */
//function generateCode()
//{
//	$.ajax({
//		async: false,   //设置为同步获取数据形式
//        type: "post",
//        url: contextPath+'/app/generateAppcode.action',
//        dataType: "json",
//        success: function (data) {
//        	
//        	var appCode = data.code;
//        	$("#codeA").textbox('setText',appCode);
//        	$("#codehidden").val(appCode);
//        	
//        },
//        error: function (XMLHttpRequest, textStatus, errorThrown) {
//            window.parent.location.href = contextPath + "/error.jsp";
//        }
//   });
//}

/**
 * 删除通行证组数据（TODO:校验和通告等数据无关联时可以删除）
 */
function deleteUgroup(id)
{
	var url = contextPath + '/userGroup/deleteUsergroups.action';
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
		{
			var isDeletedvisible = checkDeletedvisible(id);//校验用户组是否可以被删除，若为true则可以删除，若为false则不可删除
			if(!isDeletedvisible)
				{
					deleteFlag = false;
					$.messager.alert('提示',"当前待删除的通行证组正在使用，不可以被删除!");
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
		                    window.parent.location.href = contextPath + "/error.jsp";
		                }
		           });
		        	
	        }  
	    });  
	}
		
	
}

/**
 * 校验当前用户组是否有其他数据关联，若有关联则不可以删除
 * @param id
 */
function checkDeletedvisible(id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/userGroup/checkDeletedvisible.action',
        data:data,
        dataType: "json",
        success: function (data) {
        		flag = data.exist;//true为可以删除，false不可以删除
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return flag;
}

/**
 * TODO:校验当前通行证组是否可以删除
 * @param id
 * @returns {Boolean}
 */
function checkUgroupIsableDel(id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/userGroup/checkAppIsableDel.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	if(data.exist)//若data.isExist==true,则当前应用拥有下属的应用版本数据不可以被删除
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
 * 批量删除用户组数据
 */
function deleteUgroupList(operaType)
{
	var url = contextPath + '/userGroup/deleteUsergroups.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		var isDeletedvisible = checkDeletedvisible(rows[i].id);//校验用户组是否可以被删除，若为true则可以删除，若为false则不可删除
		if(!isDeletedvisible)
			{
				deleteFlag = false;
				$.messager.alert('提示',"当前通行证组id为"+rows[i].id+"的通行证组正在使用，不可以被删除!");
				break;
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
 * 自定义校验通行证组名称
 */
$.extend($.fn.validatebox.defaults.rules, {
	checkUgname: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkUgname.message = "当前通行证组名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkUgname.message = "当前通行证组名称已存在"; 
        		
                return !checkUGroupName($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});

/**
 * 自定义校验通行证组编码唯一性
 */
$.extend($.fn.validatebox.defaults.rules, {
	checkUgcode: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkUgcode.message = "当前通行证组编码不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkUgcode.message = "当前通行证组编码已存在"; 
        		
                return !checkUGroupCode($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});





//校验 通行证组名称唯一 
function checkUGroupName(id,name)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.name = name;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/userGroup/checkUsergroupName.action',
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
 * 校验通行证组编码的唯一性
 * @param id
 * @param code
 * @returns {Boolean}
 */
function checkUGroupCode(id,code)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.code = code;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/userGroup/checkUsergroupName.action',
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
