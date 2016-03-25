$(document).ready(
		function()
		{
			initDatagrid();
			bindComboboxChange();//给下拉框绑定选中事件
			closeDialog();
		}
);
//是否刚开始加载页面
var initFlag;

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
		}

		}); 
	//修改表单中的省份级联
	$("#privinceU").combobox({

		onSelect: function (rec) {
			//加载市级数据
			initCities('update','cityU','',rec.pcode);
		}

		}); 
	
	
}

/*
 * 	@desc	 
 */
function initDatagrid()
{
	$('#accountDataGrid').datagrid({
		singleSelect:false,
//		queryParams: params,
		url: contextPath + '/account/getUserList.action',
		method:'get',
		border:false,
		fit:true,
		//fitColumns:true,
		pagination:true,
		pageSize:10,
		striped:true,
		columns:[[
				{field:'id',checkbox:true	},
				{field:'code',title:'登录帐号',width:'10%',align:'center'},
				{field:'name',title:'名称',width:'10%',align:'center'},
				{field:'telephone',title:'电话',width:'15%',align:'center'},
				{field:'creater',title:'录入人',width:'10%',align:'center'},
				{field:'createrTime',title:'录入时间',width:'10%',align:'center'},
				{field:'status',title:'启用',align:'center',width:'5%',
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
				{field:'roles',title:'角色',align:'center',width:'15%',
					formatter:function(value,row,index){
						if(value!= undefined){
							var lis = '<ol 	style="padding:0 0 0 15;">';
							$.each(value,function(i,data){
								lis=lis+'<li style="align:left">'+data.roleName;
			                });
							return lis+'</ol>';
						}
					}
				},
				{field:'opt',title:'操作',width:'150',align:'center', 
		            formatter:function(value,row,index){
		                var btn = '<a class="editcls" onclick="updateAccount(&quot;'+row.id+'&quot;)" href="javascript:void(0)"></a>'
		                	+'<a class="setRoles" onclick="setRoles(&quot;'+row.id+'&quot;,&quot;'+row.roles+'&quot;)" href="javascript:void(0)"></a>';
		                return btn;
		            }
		        }
		    ]],
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
	        $('.setRoles').linkbutton({text:'设置角色',plain:true,iconCls:'icon-search'});
	    }  
	});
}


	//关闭弹框
	function closeDialog()
	{
		$("#addAccount").dialog('close'); 
		$("#updateAccount").dialog('close');
		$("#selectRoleDiv").dialog('close');
	}

	/**
	 * 权限修改
	 */
	function updateAccount(id)
	{
			/**
			 * 用户修改
			 */
			var url = contextPath + '/account/getDetailAccount.action';
			var paramData = new Object();
			paramData.id=id;
			$.ajax({
				async: false,   //设置为同步获取数据形式
		        type: "get",
		        cache:false,
		        url: url,
		        data:paramData,
		        dataType: "json",
		        success: function (data) {
					$('#updateAccountForm').form('load',{
						id:data.id,
						code:data.code,
						name:data.name,
						password:data.password,
						confirmPassword:data.password,
						telephone:data.telephone,
						status:data.status,
						privince:data.privince,
						city:data.city
					});
					
					//初始化省份combobox
					initProvince("update", "privinceU", data.province);
					//初始化市级区域combobox
					initCities('update','cityU',data.city,data.province);
					
		        },
		        error: function (XMLHttpRequest, textStatus, errorThrown) {
		            alert(errorThrown);
		        }
			});
			$("#updateAccount").dialog("open");//打开修改用户弹框
	}

	/**
	 * 初始化省下拉框
	 * @param addOrUpdate
	 * @param provinceId
	 * @param pcode
	 */
	function initProvince(addOrUpdate,provinceId,pcode)
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
		data.isHasall=false;
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

	//提交添加权限form表单
	function submitAddAccount()
	{
		$('#addAccountForm').form('submit',{
			url:contextPath+'/account/saveOrUpdate.action',
			onSubmit:function(param){
				return $('#addAccountForm').form('validate');
			},
			success:function(data){
				$.messager.alert('提示', eval("(" + data + ")").message);
				$('#addAccountForm').form('clear');
				$('#addAccountForm [name="status"]:radio').each(function() {   //设置“是”为默认选中radio
		            if (this.value == '1'){   //默认选中“是”
		               this.checked = true;   
		            }       
		         }); 
				closeDialog();
	        	initDatagrid();
	        	
			}
		});
	}
	
	//修改帐号form表单
	function submitUpdateAccount()
	{
		$('#updateAccountForm').form('submit',{
			url:contextPath+'/account/saveOrUpdate.action',
			onSubmit:function(param){
				return $('#updateAccountForm').form('enableValidation').form('validate');
			},
		    success:function(data){
		    	//data从后台返回后的类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
		    	$.messager.alert('提示', eval("(" + data + ")").message);
		    	$("#updateAccount").dialog('close');//初始化修改权限弹框关闭
		    	//在修改权限后刷新权限数据列表
		    	initDatagrid();
		    	$('#updateAccountForm').form('clear');
		    }
		});
	}
	
	/**
	 * 校验code唯一性
	 */
	function checkCode(code)
	{
		var flag = false;//当前值可用，不存在
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "get",
	        url: contextPath+'/account/checkValue.action',
	        data:{
	        	code : code
	        },
	        dataType: "json",
	        success: function (data) {
	        	if(data.exist)//若data.isExist==true,则当前校验值已存在，则不可用使用
	        		{
	        			flag = true;
	        		}
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            alert(errorThrown);
	        }
	   });
		
		return flag;
	}


	/**
	 * 批量删除
	 * @param code
	 */
	function deleteAccountByIds()
	{
		var url = contextPath + '/account/deleteAccountByIds.action';
		var paramObj = new Object();
		
		var idArr = new Array();
		var rows = $('#accountDataGrid').datagrid('getSelections');
		var deleteFlag = true;
		
		for(var i=0; i<rows.length; i++)
		{
			idArr.push(rows[i].id);//code
		}
		
		if(idArr.length>0)
		{
			paramObj.ids=idArr.toString();	//将id数组转换为String传递到后台
			
			$.messager.confirm(" ", "您确认删除选中数据？", function (r) {  
		        if (r) {  
			        	$.ajax({
			        		async: false,   //设置为同步获取数据形式
			                type: "post",
			                url: url,
			                data:paramObj,
			                dataType: "json",
			                success: function (data) {
			                	initDatagrid('');
			                	//批量删除权限后重新加载树
			                	$.messager.alert('提示', data.message);
			                	
			                },
			                error: function (XMLHttpRequest, textStatus, errorThrown) {
			                    alert(errorThrown);
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
	
	
	function initRoleGrid(){
		$('#selectRoleGrid').datagrid({
			singleSelect:true,
			rownumbers:false,
			url:contextPath + '/role/getRoleList.action',//'datagrid_data1.json',
			method:'get',
			border:false,
			fitColumns:true,
			pagination:true,
			collapsible:false,
//				pageSize:10,
//				striped:true,
			columns:[[
					{field:'id',hidden:true},
			        {field:'name',width:120,title:'角色名称'},
					{field:'code',title:'角色编码',width:120,align:'left'},
					{field:'parentRole',hidden : true},
					{field:'parentRolename',title:'角色上级角色',width:120,align:'left'}
			    ]],  
		    onLoadSuccess:function(data){  
		    	if(data.rows.length==0){
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
				}else{
					//获取已经选择的角色数据
					var selectedRows = $('#selectedRoleGrid').datagrid('getRows');
					//获取待选择的角色列表
					var selectRows = $('#selectRoleGrid').datagrid('getRows');
					$.each(selectedRows,function(i,selectedRow){
						$.each(selectRows,function(j,selectRow){
							if(selectedRow.roleId == selectRow.id){
								$('#selectRoleGrid').datagrid('checkRow',j);
							}
						});
					});
				}
		    },
		    onCheck : function(rowIndex,rowData){
		    	//由于是单选所以如果单击已经选中的角色将会自动删除
		    	var index = getRowIndex(rowData);
		    	if(index == -1){
			    	$('#selectedRoleGrid').datagrid('insertRow',{
			            row :  {
			            	roleId : rowData.id,
			            	roleCode : rowData.code,
			            	roleName : rowData.name,
			            	parentRolename : rowData.parentRolename,
			            	parentRole : rowData.parentRole
			            }
			         });
			    	var selectedRows = $('#selectedRoleGrid').datagrid('getRows');
			    	$('#selectedRoleGrid').datagrid('beginEdit', selectedRows.length-1);
		    	}
		    },
		    onUncheck : function(rowIndex,rowData){
		    	if(!initFlag){
		    		var index = getRowIndex(rowData);
			    	 $('#selectedRoleGrid').datagrid('deleteRow',index);
		    	}
		    },
		    onCheckAll : function(rows){
		    	$.each(rows,function(i,row){
		    		var index = getRowIndex(row);
		    		if(index == -1){
			    		$('#selectedRoleGrid').datagrid('insertRow',{
				            row: {
				            	roleId : rowData.id,
				            	roleCode : rowData.code,
				            	roleName : rowData.name,
				            	parentRolename : rowData.parentRolename,
				            	parentRole : rowData.parentRole
				            }
				        });
		    		}
		    		var selectedRows = $('#selectedRoleGrid').datagrid('getRows');
			    	$('#selectedRoleGrid').datagrid('beginEdit', selectedRows.length-1);
                });
		    },
		    onUncheckAll : function(rows){
		    	if(!initFlag){
			    	$.each(rows,function(i,row){
			    		var index = getRowIndex(row);
			    		if(index != -1){
				    		$('#selectedRoleGrid').datagrid('deleteRow',index);
			    		}
			    	});
		    	}
		}
	});
	}
	///扩展datagrid的方法  处理当翻页时getRowIndex方法失效的问题	
	function getRowIndex(row){
		var selectedRows = $('#selectedRoleGrid').datagrid('getRows');
    	var index = -1;
    	$.each(selectedRows,function(i,selectedRow){
    		if(row.id == selectedRow.roleId){
    			index = i;
    		}
    	});
    	return index;
	}
	//定义全局变量parentUid 存放上级编码
	function initSelectedRoleGrid(id){
		var comboboxUrl = '';
		$('#selectedRoleGrid').datagrid({
			singleSelect:false,
			rownumbers:false,
			url:contextPath + '/account/getUserRelaRoleList.action',//'datagrid_data1.json',
			method:'get',
			queryParams : {
				id : id
			},
			border:false,
			fitColumns:true,
			collapsible:false,
			columns:[[  //{"roleId":"ff808181516ab01e01516ab2bd7e0000","code":"SC_DL","name":"市场代理","parentRolename":"市场专员","parentRole":"ff808181514698fb015146a085460001"}
					{field:'id',hidden:true},    //userRelaRole 关联关系表主键
					{field:'userId',hidden:true},	//userId  用户主键
					{field:'roleId',hidden:true},	//角色主键
			        {field:'roleName',width:120,title:'角色名称'},
					{field:'roleCode',title:'角色编码',width:120,align:'left'},
					{field:'parentRolename',title:'角色上级角色',width:120,align:'left'},
					{field:'parentRole',hidden:true},
					{ field: 'parentUid', title: '上级名称', width: 100, align: 'left', 
						editor: { 
							type: 'combobox', 
							options: { 
								url : contextPath + '/account/getRoleRelaUserList.action',
								onBeforeLoad:function(param){
									var parentRoleId = $(this).closest('td[field=parentUid]').prevAll('td[field=parentRole]').text();
						            param.parentRoleId=parentRoleId;
						        },
						        onLoadSuccess:function(data){
						        	if(data.length > 0){
						        		var selled = $('#selectedRoleGrid').datagrid('getEditor', {index:0,field:'parentUid'});
							        	var parentUid = selled.target.val();
							        	if(parentUid!=''){
							        		$(this).combobox('select',parentUid);
							        	}else{
							        		$(this).combobox('select',data[0].id);
							        	}
						        	}
						        },
						        onSelect:function(record){
						        	var selled = $('#selectedRoleGrid').datagrid('getEditor', {index:0,field:'parentUid'});
						    		selled.target.val(record.id);
						        },
								valueField: "id", 
								textField: "name" 
							},
					} 
				}]],  
		    onLoadSuccess:function(data){
		    	if(data.rows.length==0){
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
				}else{
					for(var i=0;i<data.rows.length;i++){
        				$('#selectedRoleGrid').datagrid('beginEdit', i);
        				var selled = $('#selectedRoleGrid').datagrid('getEditor', {index:0,field:'parentUid'});
			    		selled.target.val(data.rows[i].parentUid);
        			}
					//获取已经选择的角色数据
					var selectedRows = $('#selectedRoleGrid').datagrid('getRows');
					//获取待选择的角色列表
					var selectRows = $('#selectRoleGrid').datagrid('getRows');
					$.each(selectedRows,function(i,selectedRow){
						$.each(selectRows,function(j,selectRow){
							if(selectedRow.roleId == selectRow.id){
								$('#selectRoleGrid').datagrid('selectRow',j);
							}
						});
					});
				}
		    	initFlag = false;
		    }
		});
	}
	/**
	 * @desc 设置角色
	 * @param userId
	 * @param roles
	 */
	function setRoles(userId,roles){
		initFlag = true;
		initRoleGrid();
		initSelectedRoleGrid(userId);
		$("#userId").val(userId);
		$("#selectRoleDiv").dialog("open");     //设定权限
	}

	function submitSelRoles(){
		//undo 保存选择角色列表
		var rows = $('#selectedRoleGrid').datagrid('getRows')[0];
		var selled = $('#selectedRoleGrid').datagrid('getEditor', {index:0,field:'parentUid'});
		rows.parentUid = selled.target.val();
		var userId = 	$("#userId").val();
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "post",
	        url: contextPath+'/account/saveUserRleaRole.action',
	        data: {
	        	userId : userId,                  //传入将要设定角色的用户
	        	role : JSON.stringify(rows)	//传入已经选择好的用户
	        },
	        dataType: "json",
	        success: function (data) {
	        	$.messager.alert('提示', data.message);
	        	closeDialog();
	        	initDatagrid();
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	        	$.messager.alert('提示', errorThrown);
	        }
	   });
	}
	//  初始化角色选择dialog
	
	function selectRoleBeforeClose(){
		var selectedRows = $('#selectedRoleGrid').datagrid('getRows');
		$.each(selectedRows,function(i,selectedRow){
			$('#selectedRoleGrid').datagrid('deleteRow',0);
		});
	}
	
/**
 * 自定义校验code
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkCodes: {//自定义校验code
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
    		rules.checkCodes.message = "用户编码已存在"; 
            return !checkCode($("#"+param[1]).val(),value,'');
        }
    },
    equalTo: { 
    	validator: function (value, param) { 
    		return $(param[0]).val() == value;
    		}, message: '两次密码输入不一致！' }
});
