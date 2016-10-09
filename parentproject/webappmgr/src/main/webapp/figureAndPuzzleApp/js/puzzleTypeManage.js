$(function()
		{
			closeDialog();//页面加载时关闭弹框
			initDatagrid();
		});

function reset()
{
	$("#typeNameC").val("");
}


/**
 * 初始化图谜字谜专家数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.typeName = $("#typeNameC").val().trim();
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/fmpApp/getPuzzlesTypeList.action',//'datagrid_data1.json',
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
				{field:'typeName',title:'字谜类型名称',width:'15%',align:'center'},
		        {field:'typeCol',width:'15%',title:'字谜行数',align:'center'},
				{field:'typeColWordsNum',width:'15%',title:'字谜每行最多字数',align:'center'},
		        {field:'typeWordsNum',title:'字谜字数',width:'15%',align:'center'},
				{field:'opt',title:'操作',width:'15%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updatePuzzleType(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deletePuzzleType(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="10">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}

function updatePuzzleType(id)
{
	var url = contextPath + '/fmpApp/getDetailPuzzlesType.action';
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
					typeName:data.typeName,
					typeCol:data.typeCol,
					typeColWordsNum:data.typeColWordsNum
					
				});
				
				
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
	});
	
	
	$("#updatePuzzleType").dialog('open');
	
		
	
}

function deletePuzzleType(id)
{
	var url = contextPath + '/fmpApp/deletePuzzleType.action';
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



function closeDialog()
{
	$('#addPuzzleType').dialog('close');
	$('#updatePuzzleType').dialog('close');
}

//取消添加字谜类型弹框触发方法
function addDialogCancel()
{
	 $('#addPuzzleType').dialog('close');
     $('#ff').form('clear');//清空表单内容
}


/**
 * 提交添加字谜类型
 */
function submitAddPuzzleType()
{
	$('#ff').form('submit',{
		url:contextPath+'/fmpApp/saveOrUpdatePuzzleType.action',
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
	    	$("#addPuzzleType").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	initDatagrid();
	    	
	    	
	    }
	});
}

function submitUpdatePuzzleType()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/fmpApp/saveOrUpdatePuzzleType.action',
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
	    	
	    	$("#updatePuzzleType").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


/**
 * 自定义校验
 */
$.extend($.fn.validatebox.defaults.rules, {
	checkTypeName: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkTypeName.message = "当前字谜类型名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkTypeName.message = "当前字谜类型名称已存在"; 
        		
                return !checkTypeName($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});

/**
 * 校验登录名是否已存在
 */
function checkTypeName(id,typeName)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.typeName = typeName;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/fmpApp/checkTypeName.action',
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
