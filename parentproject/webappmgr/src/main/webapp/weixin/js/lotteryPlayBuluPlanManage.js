$(function()
		{
			
			closeDialog();//页面加载时关闭弹框
			
			
			initDatagrid();//初始化数据列表
			
			
			bindCombobox();
		});


function bindCombobox()
{
	
	//绑定”数字或其他“的切换，若为数字方案，则只可以填写开始号码和结束号码，若为其他方案，则只可以填写其他方案，以","隔断
	$("#numOrCharA").combobox({

		onSelect: function (rec) {
			if(rec.value=='0')
				{//数字方案
					$("#opDivA").hide();
					$("#snDivA").show();
					$("#enDivA").show();
					
				}
			else if(rec.value=='1')
				{
					$("#opDivA").show();
					$("#snDivA").hide();
					$("#enDivA").hide();
				}
	
		
		}

		});


	$("#numOrCharU").combobox({

		onSelect: function (rec) {

			if(rec.value=='0')
			{//数字方案
				$("#opDivU").hide();
				$("#snDivU").show();
				$("#enDivU").show();
				
			}
			else if(rec.value=='1')
			{
				$("#opDivU").show();
				$("#snDivU").hide();
				$("#enDivU").hide();
			}
		}

		});
}

//关闭弹框
function closeDialog()
{
	$("#addBuluPlan").dialog('close');//初始化添加角色弹框关闭
	$("#updateBuluPlan").dialog('close');
}



function reset()
{
	$("#numOrCharC").combobox('setValue','');
}

/**
 * 初始化补录信息数据列表
 */
function initDatagrid()
{
	
	var params = new Object();
	params.numOrChar = $("#numOrCharC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/weixincontrol/getLotteryPlayBuluPlanList.action',//'datagrid_data1.json',
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
				{field:'planName',title:'方案名称',width:'25%',align:'center'},
				{field:'numOrChar',width:'15%',title:'彩种',align:'center',  
		            formatter:function(value,row,index){  
		            	var numOrCharName ='';
		            	switch(value)
		            	{
		            		case '0':numOrCharName='数字方案';break;
		            		case '1':numOrCharName='其他方案';break;
		            	}
		            	return numOrCharName;  
		            }  },
				{field:'createTime',title:'创建时间',width:'25%',align:'center'},
				{field:'opt',title:'操作',width:'25%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateLotteryPlayBuluPlan(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteLotteryPlayBuluPlan(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="4">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}

function addLotteryPlayBuluPlan()
{
	$("#numOrCharA").combobox('select',0);
	$("#opDivA").hide();
	$("#snDivA").show();
	$("#enDivA").show();
	$("#addBuluPlan").dialog('open');
	
}

/**
 * 修改补录方案数据
 * @param id
 */
function updateLotteryPlayBuluPlan(id)
{
	var url = contextPath + '/weixincontrol/getDetailLotteryPlayBuluPlan.action';
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
					planName:data.planName,
					numOrChar:data.numOrChar,
					startNumber:data.startNumber,
					endNumber:data.endNumber,
					otherPlan:data.otherPlan,
					otherNum:data.otherNum
					
				});
				$("#numOrCharU").combobox('select',data.numOrChar);
				if(data.numOrChar=='0')
				{//数字方案
					$("#opDivU").hide();
					$("#snDivU").show();
					$("#enDivU").show();
					
				}
				else if(data.numOrChar=='1')
				{
					$("#opDivU").show();
					$("#snDivU").hide();
					$("#enDivU").hide();
				}
		
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
	});
	
	
	$("#updateBuluPlan").dialog('open');
	
	
}
/**
 * 删除补录方案数据
 * @param id
 */
function deleteLotteryPlayBuluPlan(id)
{
	var url = contextPath + '/weixincontrol/deleteLotteryPlayBuluPlans.action';
	var data1 = new Object();
	
	var codearr = [];
	codearr.push(id);
	
	data1.ids=codearr.toString();
	
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


/**
 * 提交保存补录方案数据
 */
function submitAddBuluPlan()
{
	$('#ff').form('submit',{
		url:contextPath+'/weixincontrol/saveOrUpdateLotteryPlayBuluPlan.action',
		onSubmit:function(param){
			var flag = false;
			
			if($('#ff').form('enableValidation').form('validate') )
				{
					var numOrchar = $("#numOrCharA").combobox('getValue');
					if(numOrchar == '0')
						{//数字方案校验数字不可以为空
							var sn = $("#startNumberA").val();
							var en = $("#endNumberA").val();
							if('' == sn || '' == en)
							{
								$.messager.alert('提示', "开始号码和结束号码不可以为空!");
							}
							else
								{
									flag = true;
								}
						}
					else if(numOrchar == '1')
						{//其他方案校验其他方案输入位置不可以为空
							var op = $("#otherPlanA").val();
							if('' == op || '' == op)
							{
								$.messager.alert('提示', "其他方案内容不可以为空!");
							}
							else
							{
								flag = true;
							}
						}
					
				}
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addBuluPlan").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	$("#numOrCharA").combobox('setValue',"0");
	    	initDatagrid();
	    	
	    	
	    }
	});
}




/**
 * 提交修改补录信息数据
 */
function submitUpdateBuluPlan()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/weixincontrol/saveOrUpdateLotteryPlayBuluPlan.action',
		onSubmit:function(param){
			var flag = false;
			if($('#ffUpdate').form('enableValidation').form('validate') )
			{
				
				var numOrchar = $("#numOrCharU").combobox('getValue');
				if(numOrchar == '0')
				{//数字方案校验数字不可以为空
					var sn = $("#startNumberU").val();
					var en = $("#endNumberU").val();
					
					if('' == sn || '' == en)
						{
							$.messager.alert('提示', "开始号码和结束号码不可以为空!");
						}
					else
					{
						flag = true;
					}
				}
				else if(numOrchar == '1')
				{//其他方案校验其他方案输入位置不可以为空
					var op = $("#otherPlanU").val();
					if('' == op || '' == op)
					{
						$.messager.alert('提示', "其他方案内容不可以为空!");
					}
					else
					{
						flag = true;
					}
				}
				
				
			}
			
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#updateBuluPlan").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ffUpdate').form('clear');//清空表单内容
	    	$("#numOrCharU").combobox('setValue',"0");
	    	initDatagrid();
	    	
	    	
	    }
	});
}


/*********校验************/

/**
 * 自定义校验方案名称
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkAname: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAname.message = "当前方案名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAname.message = "当前方案名称已存在"; 
        		
                return !checkPlanName($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});



//校验方案名称（方案名称全局唯一）
function checkPlanName(id,name)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.name = name;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/weixincontrol/checkPlanName.action',
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