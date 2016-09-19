var lpbuId = '';//方案id

$(function()
		{
			initQueryProvince();
			
			closeDialog();//页面加载时关闭弹框
			
			
			initDatagrid();//初始化数据列表
			
			
		});





//关闭弹框
function closeDialog()
{
	$("#addLotteryPlay").dialog('close');//初始化添加角色弹框关闭
	$("#updateLotteryPlay").dialog('close');
}

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

function reset()
{
	$("#lotteryTypeC").combobox('setValue','');
	
	$("#nameC").val("");
	
	
	initQueryProvince();
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
 * 初始化补录信息数据列表
 */
function initDatagrid()
{
	
	var params = new Object();
	params.name = $("#nameC").val().trim();//获取模糊查询条件“应用名称”
	params.lotteryType = $("#lotteryTypeC").combobox('getValue');//获取模糊查询条件“应用编码”
	params.provinceCode = $("#privinceC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/weixincontrol/getLotteryPlayList.action',//'datagrid_data1.json',
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
				{field:'name',title:'彩种名称',width:'15%',align:'center'},
		        {field:'provinceName',width:'12%',title:'省',align:'center'},
				{field:'lotteryType',width:'12%',title:'彩种',align:'center',  
		            formatter:function(value,row,index){  
		            	var lotteryTypeName ='';
		            	switch(value)
		            	{
		            		case '1':lotteryTypeName='体彩';break;
		            		case '2':lotteryTypeName='福彩';break;
		            	}
		            	return lotteryTypeName;  
		            }  },
				{field:'correspondingTable',title:'对应的补录表',width:'15%',align:'center'},
				{field:'lotteryNumber',title:'开奖号码个数',width:'10%',align:'center'},
				{field:'issueNumLen',title:'期号长度',width:'5%',align:'center'},
				{field:'createTime',title:'创建时间',width:'15%',align:'center'},
				{field:'opt',title:'操作',width:'15%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateLotteryPlay(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteLotteryPlay(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        
	        
	    },
	    rowStyler:function(index,row){//设置行样式
				
			},
	});
}

function addLotteryPlay()
{
	$("#addLotteryPlay").dialog('open');
	initBuluDatagrid('buluPlanA','');
	initAppProvince("add", "privinceA", '');
}

/**
 * 修改补录信息数据
 * @param id
 */
function updateLotteryPlay(id)
{
	var url = contextPath + '/weixincontrol/getDetailLotteryPlay.action';
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
					province:data.province,
					correspondingTable:data.correspondingTable,
					lotteryNumber:data.lotteryNumber,
					lotteryType:data.lotteryType,
					issueNumLen:data.issueNumLen
					
				});
				
				initAppProvince("update", "privinceU", data.province);
				initBuluDatagrid('buluPlanU',id);
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
	});
	
	
	$("#updateLotteryPlay").dialog('open');
	
	
}
/**
 * 删除补录信息数据
 * @param id
 */
function deleteLotteryPlay(id)
{
	var url = contextPath + '/weixincontrol/deleteLotteryPlays.action';
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
	                    window.parent.location.href = contextPath + "/menu/error.action";
	                }
	           });
	        	
        }  
    });  
}

/**
 * 初始化补录数据列表
 * buluDatagridId:补录table的id
 * lpId：补录信息id
 */
function initBuluDatagrid(buluDatagridId,lpId)
{
	var params = new Object();
	
	lpbuId = getCheckIpbuId(lpId);
	
	
	$('#'+buluDatagridId).datagrid({
		singleSelect:true,
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
		pageSize:5,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[5,10],
		columns:[[
				{  
                field: 'oid', title: '', width: '5%', align: 'center',  
  
                //调用formater函数对列进行格式化，使其显示单选按钮（所有单选按钮name属性设为统一，这样就只能有一个处于选中状态）  
                formatter: function (value, row, index) {  
                     var s = '<input name="selectRadio"  type="radio" id="selectRadio"'+index+'  /> ';  
  
                    return s;  
                }  
  
            }  ,
				{field:'id',hidden:true},
				{field:'planName',title:'方案名称',width:'25%',align:'center'},
				{field:'numOrChar',width:'25%',title:'方案类别',align:'center',  
		            formatter:function(value,row,index){  
		            	var numOrCharName ='';
		            	switch(value)
		            	{
		            		case '0':numOrCharName='数字方案';break;
		            		case '1':numOrCharName='其他方案';break;
		            	}
		            	return numOrCharName;  
		            }  },
				{field:'createTime',title:'创建时间',width:'25%',align:'center'}
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="4">没有数据</td></tr>');
			}
	        
	        if('' != lpbuId)
	        	{
		        	 var selectedRows = $('#'+buluDatagridId).datagrid('getRows');
		 			$.each(selectedRows,function(j,selectedRow){
		 				
		 				if(selectedRow.id == lpbuId){
		 					
		 					$("input[type='radio']")[j].checked = true;
		 				}
		 			});
	        	}
	       
	        
	        
	    },
        onClickRow: function(rowIndex, rowData){
            //加载完毕后获取所有的checkbox遍历
            var radio = $("input[type='radio']")[rowIndex].disabled;
            //如果当前的单选框不可选，则不让其选中
            if (radio!= true) {
                //让点击的行单选按钮选中
                $("input[type='radio']")[rowIndex].checked = true;
                lpbuId = rowData.id;//返回该行的id 
            }
            else {
                $("input[type='radio']")[rowIndex].checked = false;
                lpbuId = '';//返回该行的id 
            }
        }
	});
}


/**
 * 获取当前补录信息的补录方案id
 * @param lpId，补录id
 */
function getCheckIpbuId(lpId)
{
	var ipbuId = '';
	var data = new Object();
	
	data.id = lpId;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/weixincontrol/getLotteryPlayPlanId.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	ipbuId = data.lpBuluId;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return ipbuId;
}


/**
 * 提交保存补录信息数据
 */
function submitAddLotteryPlay()
{
	$('#ff').form('submit',{
		url:contextPath+'/weixincontrol/saveOrUpdateLotteryPlay.action',
		onSubmit:function(param){
			var flag = false;
			
			param.lpbuId=lpbuId;
			if($('#ff').form('enableValidation').form('validate') )
				{
					if(null != lpbuId && '' != lpbuId)
						{
							param.lpbuId = lpbuId;
							flag = true;
						}
					else
						{
							$.messager.alert('提示', "请选择补录方案!");
						}
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addLotteryPlay").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	$("#lotteryTypeA").combobox('setValue',"1");
	    	initDatagrid();
	    	lpbuId = '';	   
	    	
	    }
	});
}



/**
 * 提交修改补录信息数据
 */
function submitUpdateLotteryPlay()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/weixincontrol/saveOrUpdateLotteryPlay.action',
		onSubmit:function(param){
			var flag = false;
			param.lpbuId=lpbuId;
			if($('#ffUpdate').form('enableValidation').form('validate') )
				{
					if(null != lpbuId && '' != lpbuId)
					{
						param.lpbuId = lpbuId;
						flag = true;
					}
					else
					{
						$.messager.alert('提示', "请选择补录方案!");
					}
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#updateLotteryPlay").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ffUpdate').form('clear');//清空表单内容
	    	$("#lotteryTypeU").combobox('setValue',"1");
	    	initDatagrid();
	    	lpbuId = '';	    	
	    	
	    }
	});
}