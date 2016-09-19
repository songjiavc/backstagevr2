var zNodes ;//放置树节点的全局变量
$(document).ready(
		function()
		{
			initDatagrid('');
			closeDialog();
			initZnodes();
		}
);


function initDatagrid(parentCode)
{
	var params = new Object();
	params.parentCode = parentCode;//父级id
	
	$('#datagrid').datagrid({
		singleSelect:false,
		queryParams: params,
		url:contextPath + '/menu/getAuthList.action',//'datagrid_data1.json',
		method:'get',
		border:false,
		fit:true,//datagrid自适应
		fitColumns:true,
//		pagination:true,
		pageSize:10,
//		striped:true,
		loadMsg:'全力加载中...',
		columns:[[
				{field:'ck',checkbox:true},
				{field:'id',hidden:true},
				{field:'connectRole',hidden:true},//当前权限是否关联了有效的角色数据
				{field:'isSystem',hidden:true},
		        {field:'authName',width:120,title:'权限名称'},
				{field:'code',title:'权限编码',width:120,align:'left'},
				{field:'url',title:'权限url',width:120,align:'left'},
				{field:'authImg',title:'权限图片',width:80,align:'left'},
				{field:'status',title:'是否启用',width:80,align:'left',
					formatter:function(value,row,index){
							var showStatus = "";
							if("1" == row.status)
								{
									showStatus = "是";
								}
							else
								{
									showStatus = "否";
								}
							return showStatus;
						}
					},
				{field:'opt',title:'操作',width:160,align:'center',  
		            formatter:function(value,row,index){  
		                var btn = '<a class="editcls" onclick="updateAuth(&quot;'+row.id+'&quot;,&quot;'+row.isSystem+'&quot;)" href="javascript:void(0)">编辑</a>'
		                	+'<a class="auth" onclick="deleteAuth(&quot;'+row.id+'&quot;,&quot;'+row.isSystem+'&quot;,&quot;'+row.connectRole+'&quot;)" href="javascript:void(0)">删除</a>';
		                return btn;  
		            }  
		        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});  
	        $('.auth').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	    	if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
				}
	        
	    }  
	});
}


//关闭弹框
function closeDialog()
{
	$("#addAuth").dialog('close');//初始化添加权限弹框关闭
	$("#updateAuth").dialog('close');//初始化修改权限弹框关闭
}

/**
 * 权限修改
 */
function updateAuth(code,isSystem)
{
	var url = contextPath + '/menu/getDetailAuth.action';
	var data1 = new Object();
	data1.code=code;//权限的id
	
	if("1"!=isSystem)
		{
			$.ajax({
				async: false,   //设置为同步获取数据形式
		        type: "get",
		        url: url,
		        data:data1,
		        dataType: "json",
		        success: function (data) {
		        	
						$('#ffupdate').form('load',{
							id:data.id,
							code:data.code,
							authName:data.authName,
							parentAuth:data.parentAuth,
							url:data.url,
							status:data.status,
							authImg:data.authImg
						});
						
						initParentAuthList('update',code,data.parentAuth);
				
		        	
		        	
		        },
		        error: function (XMLHttpRequest, textStatus, errorThrown) {
		            window.parent.location.href = contextPath + "/menu/error.action";
		        }
		   });
			
			
			$("#updateAuth").dialog("open");//打开修改权限弹框
		}
	else
	{
		$.messager.alert('提示',"当前待修改数据是系统数据不可以进行修改操作!");
	}
	
	
}

/**
 * 批量删除权限数据
 * @param code
 */
function deleteAuthList()
{
	var url = contextPath + '/menu/deleteAuth.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		
		//判断当前权限是否有子级权限
		var haveChildFlag = checkHaveChildAuth(rows[i].id);//判断当前待删除权限是否拥有子级权限，若拥有子级权限则不可以删除
		
		var issystem = rows[i].isSystem;//当前数据的是否为系统数据的标志位
		
		var connectRole = rows[i].connectRole;//当前数据是否和有效的角色数据有关联
		
		if(haveChildFlag)
			{
				$.messager.alert('提示', "当前待删除权限:'"+rows[i].authName+"'拥有子级权限,不可以进行删除操作!");
				deleteFlag = false;
				break;
			}
		else
			if("1"==issystem)
				{
					$.messager.alert('提示', "当前待删除权限:'"+rows[i].authName+"'是系统数据,不可以进行删除操作!");
					deleteFlag = false;
					break;
				}
			else 
				if("1"==connectRole)
				{
					$.messager.alert('提示', "当前待删除权限:'"+rows[i].authName+"'和'"+rows[i].bindRoles+"'的角色数据有关联,不可以进行删除操作!");
					deleteFlag = false;
					break;
				}
	}
	
	if(deleteFlag)//选中的待删除权限中没有拥有子级权限的权限时可以进行删除操作
		{
			if(codearr.length>0)
			{
				data1.codes=codearr.toString();//将id数组转换为String传递到后台
				
				$.messager.confirm("提示", "您确认删除选中数据？", function (r) {  
			        if (r) {  
			        	
				        	$.ajax({
				        		async: false,   //设置为同步获取数据形式
				                type: "post",
				                url: url,
				                data:data1,
				                dataType: "json",
				                success: function (data) {
				                	initDatagrid('');
				                	//批量删除权限后重新加载树
				                	initZnodes();
				                	
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
 * 删除权限
 */
function deleteAuth(code,isSystem,connectRole)
{
	var url = contextPath + '/menu/deleteAuth.action';
	var data1 = new Object();
	var deleteFlag = true;//是否可以进行删除标志位
	
	if("1"==isSystem)
	{
		$.messager.alert('提示',"当前待删除数据是系统数据不可以进行删除操作!");
		deleteFlag = false;
	}
	else
		{
			var haveChildFlag = checkHaveChildAuth(code);//判断当前待删除权限是否拥有子级权限，若拥有子级权限则不可以删除
			if(haveChildFlag)
			{
				$.messager.alert('提示', "当前待删除权限有子级权限,不可以进行删除操作!");
				deleteFlag = false;
			}
			else
				{
					if("1"==connectRole)//当connectRole=1时则表示当前权限和有效的角色数据有关联
					{
						$.messager.alert('提示', "当前权限和有效的角色数据有关联,不可以进行删除操作!");
						deleteFlag = false;
					}
				}
		}
		
	
	var codearr = [];
	codearr.push(code);
	data1.codes=codearr.toString();
	
	
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
			                	initDatagrid('');
			                	//删除权限后重新加载树
			                	initZnodes();
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
 * 初始化上级权限combobox数据
 */
function initParentAuthList(addOrUpdate,code,parentauth)
{
	var parentAuthId = "parentAuthA";
	
	
	var data = new Object();
	data.status = "1";
	
	if("update" == addOrUpdate)
		{//修改
			data.code = code;
			parentAuthId = "parentAuthU";
		}
	
	$('#'+parentAuthId).combobox('clear');//清空combobox值
	
	$('#'+parentAuthId).combobox({
			queryParams:data,
			url:contextPath+'/menu/getParentAuth.action',
			valueField:'code',
			textField:'authName',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
                 if (data1.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#"+parentAuthId).combobox('select',data1[0].code);
                 }
                 else
            	 {
            	 	$("#"+parentAuthId).combobox('select', parentauth);
            	 }
					
             }
		}); 
}


//提交添加权限form表单
function submitAddauth()
{
	$('#ff').form('submit',{
		url:contextPath+'/menu/saveOrUpdate.action',
		onSubmit:function(param){
				
			return $('#ff').form('enableValidation').form('validate');
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addAuth").dialog('close');//初始化修改权限弹框关闭
	    	//在添加权限后刷新权限数据列表
	    	initDatagrid('');
	    	//在添加权限后刷新权限树
	    	initZnodes();
	    	$('#ff').form('clear');
	    	$("#parentAuth").combobox('select','0');
	    	$('#ff [name="status"]:radio').each(function() {   //设置“是”为默认选中radio
	            if (this.value == '1'){   
	               this.checked = true;   
	            }       
	         }); 
	    }
	});
}

//提交修改权限form表单
function submitUpdateauth()
{
	$('#ffupdate').form('submit',{
		url:contextPath+'/menu/saveOrUpdate.action',
		onSubmit:function(param){
			return $('#ffupdate').form('enableValidation').form('validate');
		},
	    success:function(data){
	    	//data从后台返回后的类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#updateAuth").dialog('close');//初始化修改权限弹框关闭
	    	//在修改权限后刷新权限数据列表
	    	initDatagrid('');
	    	//在修改权限后刷新权限树
	    	initZnodes();
	    	$('#ffupdate').form('clear');
	    }
	});
}

/**
 * 校验code,authname唯一性
 */
function checkCode(id,code,authname)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.code = code;
	data.authname = authname;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/menu/checkValue.action',
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
 * 判断是否有子级权限
 * @param id
 */
function checkHaveChildAuth(id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.parentAuth = id;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/menu/checkValue.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	if(data.exist)//若data.isExist==true则当前权限下有子级权限
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
 * 自定义校验authname
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkAname: {//自定义校验authname
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAname.message = "当前权限名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAname.message = "当前权限名称已存在"; 
                return !checkCode($("#"+param[1]).val(),'',value);
    		}
        	
        }
    }
});

/**
 * 自定义校验code
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkCodes: {//自定义校验code
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkCodes.message = "当前权限编码不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkCodes.message = "当前权限编码已存在"; 
                return !checkCode($("#"+param[1]).val(),value,'');
    		}
        	
        	
        }
    }
});




/***********树配置************/

var setting ;
/**
 * 初始化树节点数据
 */
function initZnodes()
{
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/menu/getTreedata.action',
        dataType: "json",
        success: function (data) {
        	setting = {
        			view: {
        				showLine: false
        			},
        			data: {
        				simpleData: {
        					enable: true
        				}
        			},
        			callback: {
        				onClick: zTreeOnClick
        			}
        		};
        		zNodes = data;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
}

/**
 * 树节点点击事件
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnClick(event, treeId, treeNode) 
{
    initDatagrid(treeNode.id)
}
 

