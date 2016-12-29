$(document).ready(function(){
			closeDialog();
			initDatagrid();
			
			bindComboboxChange();
		});

/**
 * 绑定上级角色下拉框改变事件
 */
function bindComboboxChange ()
{
	//添加角色的角色上级权限绑定
	$("#parentRoleA").combobox({

		 onSelect: function (n,o) {
			 $("#parentRoleAname").val($("#parentRoleA").combobox("getText"));
		}

		}); 
	//修改角色的角色上级权限绑定
	$("#parentRoleU").combobox({

		 onSelect: function (n,o) {
			 $("#parentRoleUname").val($("#parentRoleU").combobox("getText"));
		}

		}); 
}

//关闭弹框
function closeDialog()
{
	$('#w').dialog('close');//初始化权限设置弹框关闭
	$("#addRole").dialog('close');//初始化添加角色弹框关闭
	$("#updateRole").dialog('close');
}


function initDatagrid()
{
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		url:contextPath + '/role/getRoleList.action',//'datagrid_data1.json',
		method:'get',
		border:false,
		singleSelect:false,
		fit:true,//datagrid自适应
		fitColumns:true,
		pagination:true,
		collapsible:false,
		toolbar:toolbar,
//		pageSize:10,
//		striped:true,
		columns:[[
				{field:'ck',checkbox:true},
				{field:'id',hidden:true},
				{field:'isSystem',hidden:true},
		        {field:'name',width:120,title:'角色名称'},
				{field:'code',title:'角色编码',width:120,align:'left'},
				{field:'parentRolename',title:'角色上级角色',width:120,align:'left'},
//				{field:'status',title:'是否启用',width:80,align:'left',
//					formatter:function(value,row,index){
//							var showStatus = "";
//							if("1" == row.status)
//								{
//									showStatus = "是";
//								}
//							else
//								{
//									showStatus = "否";
//								}
//							return showStatus;
//						}
//					},
					{field:'opt',title:'操作',width:160,align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateRole(&quot;'+row.id+'&quot;,&quot;'+row.isSystem+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteRole(&quot;'+row.id+'&quot;,&quot;'+row.isSystem+'&quot;)" href="javascript:void(0)">删除</a>'
			                	+'<a class="manage" onclick="authManage(&quot;'+row.id+'&quot;,&quot;'+row.parentRole+'&quot;)" href="javascript:void(0)">权限设置</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        $('.manage').linkbutton({text:'权限设置',plain:true,iconCls:'icon-auth'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        
	    }  
	});
}

/**
 * 角色修改
 */
function updateRole(id,isSystem)
{
	var url = contextPath + '/role/getDetailRole.action';
	var data1 = new Object();
	data1.id=id;//权限的id
	
	if("1"!=isSystem)
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
						code:data.code,
						name:data.name,
						parentRole:data.parentRole
//						status:data.status
					});
					
					getParentRole('update',id,data.parentRole);
			
	        	
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/menu/error.action";
	        }
		});
		
		$("#updateRole").dialog('open');
	}
	else
	{
		$.messager.alert('提示',"当前待修改数据是系统数据不可以进行修改操作!");
	}
	
}


/**
 * 权限设置
 * @param id:当前待设置权限的id
 * @param parentRole:父级角色id（根节点的父级id是“”）
 */
function authManage(id,parentRole)
{
	initZnodes(id);
	
	//初始化已拥有的权限
	var url = contextPath + '/role/getAuthListOfRole.action';
	var data1 = new Object();
	data1.id=id;//权限的id
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var authList;//当前角色拥有权限list
	
	//初始化权限树
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
        	authList = data;//获取权限
        	var node;//ztree树节点变量
        	var flag = false;
        	if(authList.length>0)
        		{
        			//设置默认权限选中
        			var auth  ;
        			for(var i=0;i<authList.length;i++)
        				{
        					auth = authList[i];
        					flag = checkHaveChildAuth(auth.id);//flag=true时则有子节点，不能选中
        					if(!flag)
        						{
	        						node = zTree.getNodeByParam("id",auth.id);
	        						if(null != node)
	        							{
	        								zTree.checkNode(node, true, true);//设置树节点被选中
	        							}
        						}
        				}
        		}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	//超级管理员即根节点是可以设置所有权限的,on 2016/12/29,放开权限，所有权限都可以被设置
	/*if(null != parentRole&&""!=parentRole)
		{
			//设置根节点不可用，直接关联子节点
			var authId = getOriginAuthId();//树根节点的id是‘1’（将写死的变量 写成静态变量，改为从后台获取，获取方法在MenuController）
			var node0 = zTree.getNodeByParam("id",authId);
			
			*//**禁用 或 解禁 某个节点的 checkbox / radio [setting.check.enable = true 时有效]
			 * zTreeObj.setChkDisabled: Function(node, disabled, inheritParent, inheritChildren)
			 *//*
			//设置父级角色的子级权限可操作选择
			var parentauthlist = new Object();
			parentauthlist = getParentAuth(parentRole);
			zTree.setChkDisabled(node0, true,false,true);
			var authparent;
			var nodeparent;	
			for(var i=0;i<parentauthlist.length;i++)
			{
				authparent = parentauthlist[i];
				nodeparent = zTree.getNodeByParam("id",authparent.id);
				if(null != nodeparent)为了避免当上级角色拥有的权限已经被设置为不启用状态或已删除时而关联表的数据还在，
										则此时是获取不到树节点的，因为当前树不会加载不启用状态的权限和删除的权限
					{
						zTree.setChkDisabled(nodeparent, false);
					}
				
			}
			
		}*/
	
	$('#w').dialog('open');//打开弹框
}

/**
 * 获取权限树根节点数据
 */
function getOriginAuthId()
{
	var url = contextPath + '/menu/getConstant.action';
	var data1 = new Object();
	data1.constantName='ORIGIN_AUTH_ID';//权限根节点名称
	
	var authId = '';
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
        	authId = data.message;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return authId;
}

/**
 * 获取当前权限设置的上级权限
 * @param parentRole
 */
function getParentAuth(parentRole)
{
	var url = contextPath + '/role/getAuthListOfRole.action';
	var data1 = new Object();
	data1.id=parentRole;//权限的id
	
	var returnObject = new Object();//返回数据变量
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
        	returnObject = data;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return returnObject;
}

/**
 * 确认权限设置
 */
function roleManage()
{
	var ztreeId = $("#ztreeId").val();//获取当前设置权限的角色
	
	var url = contextPath + '/role/manageRoleAndauth.action';
	var data1 = new Object();
	
	var codearr = new Array();
	 var treeObj=$.fn.zTree.getZTreeObj("treeDemo"),
     nodes=treeObj.getCheckedNodes(true),
     v="";
	
	
	for(var i=0; i<nodes.length; i++)
	{
		codearr.push(nodes[i].id);//code
	}
	
	
	data1.id = ztreeId;
	data1.authes = codearr.toString();
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	initDatagrid();
        	
        	$.messager.alert('提示', data.message);
        	
        	$('#w').dialog('close');//初始化权限设置弹框关闭
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	
}


/**
 * 获取父级角色列表
 * @param id
 */
function getParentRole(addOrUpdate,id,parentrole)
{
	var parentRoleId = "parentRoleA";
	
	
	var data = new Object();
//	data.status = "1";
	
	if("update" == addOrUpdate)
		{//修改
			data.id = id;
			parentRoleId = "parentRoleU";
		}
	
	$('#'+parentRoleId).combobox('clear');//清空combobox值
	
	$('#'+parentRoleId).combobox({
			queryParams:data,
			url:contextPath+'/role/getParentRole.action',
			valueField:'id',
			textField:'name',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
                 if (data1.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#"+parentRoleId).combobox('select',data1[0].id);
                 }
                 else
            	 {
            	 	$("#"+parentRoleId).combobox('select', parentrole);
            	 }
					
             }
		}); 
}


/**
 * 提交role表单
 */
function submitAddrole()
{
	$('#ff').form('submit',{
		url:contextPath+'/role/saveOrUpdate.action',
		onSubmit:function(param){
				
			return $('#ff').form('enableValidation').form('validate');
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addRole").dialog('close');//初始化添加角色弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	initDatagrid();
	    	
	    	$('#ff').form('clear');
	    	$("#parentRoleA").combobox('select','0');
//	    	$('#ff [name="status"]:radio').each(function() {   //设置“是”为默认选中radio
//	            if (this.value == '1'){   
//	               this.checked = true;   
//	            }       
//	         });
	    	//清空上级角色名称隐藏值
	    	$("#parentRoleAname").val("");
	    }
	});
}

/**
 * 提交修改角色表单
 */
function submitUpdaterole()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/role/saveOrUpdate.action',
		onSubmit:function(param){
				
			return $('#ffUpdate').form('enableValidation').form('validate');
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateRole").dialog('close');
	    	
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    	//恢复form表单
	    	$('#ffUpdate').form('clear');
	    	$("#parentRoleU").combobox('select','0');
//	    	$('#ffUpdate [name="status"]:radio').each(function() {   //设置“是”为默认选中radio
//	            if (this.value == '1'){   
//	               this.checked = true;   
//	            }       
//	         });
	    	//清空上级角色名称隐藏值
	    	$("#parentRoleUname").val("");
	    }
	});
}

/**
 * 删除角色数据
 * @param id
 */
function deleteRole(id,isSystem)
{
	var url = contextPath + '/role/deleteRole.action';
	var data1 = new Object();
	
	if("1"!=isSystem)
	{
		var codearr = [];
		codearr.push(id);
		
		data1.ids=codearr.toString();
		
		var haveChildFlag = checkHaveChildRole(id);//checkHaveChildRole(code);//判断当前待删除角色是否拥有子级角色，若拥有子级角色则不可以删除
		
		if(haveChildFlag)
			{
				$.messager.alert('提示', "当前待删除角色有子级角色,不可以进行删除操作!");
			}
		else
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
	else
	{
		$.messager.alert('提示',"当前待删除数据是系统数据不可以进行删除操作!");
	}
	
}

/**
 * 判断是否有子级角色
 * @param id
 */
function checkHaveChildRole(id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.parentRole = id;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/role/checkRoleValue.action',
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
 * 判断是否有子级权限
 * @param id
 */
function checkHaveChildAuth(id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.parentAuth = id;
	data.status = "1";
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
 * 校验code,name唯一性
 */
function checkCodeOrName(id,code,name)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.code = code;
	data.name = name;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/role/checkRoleValue.action',
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
 * 批量删除角色数据
 */
function deleteRoleList()
{
	var url = contextPath + '/role/deleteRole.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		
		//判断当前角色是否有子级角色
		var haveChildFlag = checkHaveChildRole(rows[i].id);
		
		var issystem = rows[i].isSystem;//当前数据的是否为系统数据的标志位
		
		if(haveChildFlag)
			{
				$.messager.alert('提示', "当前待删除角色:'"+rows[i].name+"'拥有子级角色,不可以进行删除操作!");
				deleteFlag = false;
				break;
			}
		else
			if("1"==issystem)
				{
					$.messager.alert('提示', "当前待删除角色:'"+rows[i].authName+"'是系统数据,不可以进行删除操作!");
					deleteFlag = false;
					break;
				}
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
 * 自定义校验authname
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkAname: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAname.message = "当前角色名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAname.message = "当前角色名称已存在"; 
                return !checkCodeOrName($("#"+param[1]).val(),'',value);
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
        		rules.checkCodes.message = "当前角色编码不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkCodes.message = "当前角色编码已存在"; 
                return !checkCodeOrName($("#"+param[1]).val(),value,'');
    		}
        	
        	
        }
    }
});


/*******树方法********/

	
var setting ;
/**
 * 初始化树节点数据
 */
function initZnodes(id)
{
	var data = new Object();
	
//	data.id = id;
	$("#ztreeId").val(id);
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        data:data,
        url: contextPath+'/role/getTreedata.action',
        dataType: "json",
        success: function (data) {
        	setting = {
        			check: {
        				enable: true
        			},
        			view: {
        				showLine: false
        			},
        			data: {
        				simpleData: {
        					enable: true
        				}
        			}
//        			,
//        			callback: {
//        				onClick: zTreeOnClick
//        			}
        		};
        		zNodes = data;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
}	


/***************/