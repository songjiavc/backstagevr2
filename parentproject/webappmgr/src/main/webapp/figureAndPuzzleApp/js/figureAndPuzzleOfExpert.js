$(function() {
	
	var message = $("#message").val();//获取登录返回信息
	
	closeDialog();
	
	if("success" == message)//登录信息正确
		{
			$('#editpass').click(function() {
		         $('#w').window('open');
		     });
	
		     $('#btnEp').click(function() {
		    	 updatePassword();
		     })
	
				$('#btnCancel').click(function(){closePwd();})
				
				initLoginMes();//初始化登录人信息
		
			initDatagrid();
		}
	else
		{
			//登录失败，跳转回登录页
			window.location.href=contextPath + "/fmpApp/logout.action?alertmsg="+message;
		}
	
	
});


//修改密码提交内容
function updatePassword()
{
	$('#updatePasswordForm').form('submit',{
		onSubmit:function(param){
			return $('#updatePasswordForm').form('validate');
		},
		success:function(data){
			$.messager.alert('提示', eval("(" + data + ")").message);
			$('#updatePasswordForm').form('clear');
			$("#w").dialog('close');
			window.location.href=contextPath + '/fmpApp/logout.action?alertmsg='+'';
		}
	});
}

/**
 * 初始化登陆人信息
 */
function initLoginMes()
{
	$.ajax({
		async: false,   //设置为同步获取数据形式
		type: "post",
		url: contextPath + '/fmpApp/getLoginExpertmsg.action',
//		data:data,
		dataType: "json",
		success: function (dataresult) {
			var username = dataresult.message;
		
			$("#loginuser").html(username);
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
   });
}

//安全退出
function logout()
{
	$.messager.confirm('系统提示','您确认退出吗?',function(r){
	    if (r){
	    	var message = '3';
	    	window.location.href=contextPath + '/fmpApp/logout.action?alertmsg='+message;
	    }
	});
	
}

function closeDialog()
{
	$("#addPuzzleType").dialog('close');
	$("#updatePuzzleType").dialog('close');
	$('#w').window('close');
}


function initDatagrid()
{
	var params = new Object();
//	params.typeName = $("#typeNameC").val().trim();
	
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