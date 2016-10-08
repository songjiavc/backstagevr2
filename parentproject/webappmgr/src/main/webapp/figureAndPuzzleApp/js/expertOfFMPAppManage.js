$(function()
		{
			closeDialog();//页面加载时关闭弹框
			bindComboboxChange();
			initDatagrid();
		});


/**
 * 绑定上级角色下拉框改变事件
 */
function bindComboboxChange()
{
	//添加表单中的省份级联
	$("#provinceCodeA").combobox({

		onSelect: function (rec) {
			 //加载市级数据
			 initCities('add','cityCodeA','',rec.pcode);
		}

		}); 
	//修改表单中的省份级联
	$("#provinceCodeU").combobox({

		onSelect: function (rec) {
			//加载市级数据
			initCities('update','cityCodeU','',rec.pcode);
		}

		}); 
	
	
}
function reset()
{
	$("#nameC").val("");
	$("#figureOrPuzzlesC").combobox('setValue','');
	$("#lotterytypeC").combobox('setValue','');
}

/**
 * 初始化省下拉框
 * @param addOrUpdate
 * @param provinceId
 * @param pcode
 */
function initExpertProvince(addOrUpdate,provinceId,pcode)
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
            	 	$("#"+provinceId).combobox('select', pcode);
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
 * 初始化图谜字谜专家数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.name = $("#nameC").val().trim();//获取模糊查询条件“应用名称”
	params.figureOrPuzzles = $("#figureOrPuzzlesC").combobox('getValue');
	params.lotterytype = $("#lotterytypeC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/fmpApp/getExpertList.action',//'datagrid_data1.json',
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
				{field:'code',title:'登录名',width:'10%',align:'center'},
		        {field:'name',width:'10%',title:'专家姓名',align:'center'},
		        {field:'figureOrPuzzlesName',width:'10%',title:'图谜/字谜',align:'center'},
				{field:'lotteryType',width:'10%',title:'彩种',align:'center',  
		            formatter:function(value,row,index){  
		            	var lotteryTypeName ='';
		            	switch(value)
		            	{
		            		case '0':lotteryTypeName='全部';break;
		            		case '1':lotteryTypeName='体彩';break;
		            		case '2':lotteryTypeName='福彩';break;
		            	}
		            	return lotteryTypeName;  
		            }  },
		        {field:'telephone',title:'联系方式',width:'10%',align:'center'},
				{field:'provinceName',title:'省',width:'6%',align:'center'},
				{field:'cityName',title:'市',width:'6%',align:'center'},
				{field:'createTime',title:'创建时间',width:'14%',align:'center'},
				{field:'opt',title:'操作',width:'14%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateExpert(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteExpert(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
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

function updateExpert(id)
{
	var url = contextPath + '/fmpApp/getDetailExpertOfFMAPP.action';
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
					code:data.code,
					name:data.name,
					password:data.password,
					figureOrPuzzles:data.figureOrPuzzles,
					lotteryType:data.lotteryType,
					telephone:data.telephone,
//					provinceCode:data.provinceCode,
//					cityCode:data.cityCode,
					address:data.address
					
				});
				
				$("#identityPasswordU").val(data.password);
				$('#ffUpdate').form('enableValidation').form('validate');//因为确认密码和密码有一个比较的校验，所以在填充确认密码数据后，触发表单校验，可以正确的显示这个校验的结果
				
				//初始化省份combobox
				initExpertProvince("update", "provinceCodeU", data.provinceCode);
				//初始化市级区域combobox
				initCities('update','cityCodeU',data.cityCode,data.provinceCode);
		
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
	});
	
	
	$("#updateExpert").dialog('open');
	
		
	
}

function deleteExpert(id)
{
	var url = contextPath + '/fmpApp/deleteExpertsOfFMPAPPs.action';
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
	$('#addExpert').dialog('close');
	$('#updateExpert').dialog('close');
}

//取消添加应用弹框触发方法
function addDialogCancel()
{
	 $('#addExpert').dialog('close');
     $('#ff').form('clear');//清空表单内容
     $('#lotteryTypeA').combobox('setValue','');
     $('#figureOrPuzzlesA').combobox('setValue','');
}


/**
 * 提交添加图谜字谜专家
 */
function submitAddExpert()
{
	$('#ff').form('submit',{
		url:contextPath+'/fmpApp/saveOrUpdate.action',
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
	    	$("#addExpert").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	$("#lotteryTypeA").combobox('setValue',"0");
	    	$("#figureOrPuzzlesA").combobox('setValue',"0");
	    	initDatagrid();
	    	
	    	
	    }
	});
}

function submitUpdateExpert()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/fmpApp/saveOrUpdate.action',
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
	    	
	    	$("#updateExpert").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


/**
 * 自定义校验
 */
$.extend($.fn.validatebox.defaults.rules, {
	checkExpertCode: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkExpertCode.message = "当前登录名不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkExpertCode.message = "当前登录名已存在"; 
        		
                return !checkExpertCode($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});

/**
 * 校验登录名是否已存在
 */
function checkExpertCode(id,code)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.code = code;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/fmpApp/checkExpertCode.action',
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
