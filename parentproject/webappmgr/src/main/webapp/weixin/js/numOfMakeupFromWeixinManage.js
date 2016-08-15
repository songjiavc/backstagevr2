$(function()
		{
			initQueryProvince();//初始化可以补录的省份列表数据
			
			initLotteryPlay();//初始化可以补录的彩种
			
			
			initDatagrid();//初始化数据列表
			
			bindCombobox();
			
			
		});



function bindCombobox()
{
	$("#provinceC").combobox({

		onSelect: function (rec) {
			initLotteryPlay();//初始化可以补录的彩种
			initDatagrid();//初始化数据列表
		}

		}); 
	
	$("#lotteryTypeC").combobox({

		onSelect: function (rec) {
			initLotteryPlay();//初始化可以补录的彩种
			initDatagrid();//初始化数据列表
		}

		}); 
	
	$("#lotteryPlayC").combobox({

		onSelect: function (rec) {
			initDatagrid();//初始化数据列表
		}

		}); 
}



function reset()
{
	$("#lotteryTypeC").combobox('setValue','1');
	initQueryProvince();//初始化可以补录的省份列表数据
	
	initLotteryPlay();//初始化可以补录的彩种
}


/**
 * 初始化彩种类型数据。根据省份和彩种类型
 */
function initLotteryPlay()
{
	$("#lotteryPlayC").combobox('clear');
	
	var data = new Object();
	
	data.province = $("#provinceC").combobox('getValue');
	data.lotteryType = $("#lotteryTypeC").combobox('getValue');
	
	$("#lotteryPlayC").combobox({
		queryParams:data,
		url:contextPath+'/weixincontrol/getContactLottery.action',
		valueField:'id',
		textField:'name',
		 onLoadSuccess: function (data1) { //数据加载完毕事件
			 $('#lotteryPlayC').combobox('select',data1[0].id);
				
         }
	}); 
	
}

/**
 * 初始化省份数据下拉框
 */
function initQueryProvince()
{
	var data = new Object();
	$('#provinceC').combobox({
		queryParams:data,
		url:contextPath+'/weixincontrol/getProvinceList.action',
		valueField:'pcode',
		textField:'pname',
		 onLoadSuccess: function (data1) { //数据加载完毕事件
			 $('#provinceC').combobox('select',data1[0].pcode);
				
         }
	}); 
	
	
}

/**
 * 初始化补录信息数据列表
 */
function initDatagrid()
{
	
	var params = new Object();
	params.lotteryPlay = $("#lotteryPlayC").combobox('getValue');
	params.lotteryType = $("#lotteryTypeC").combobox('getValue');
	params.province = $("#provinceC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/weixincontrol/getNumofMakeupList.action',//'datagrid_data1.json',
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
				{field:'ISSUE_NUMBER',title:'期号',width:'15%',align:'center'},
		        {field:'kjNum',width:'40%',title:'开奖号码',align:'center'},
				{field:'CREATE_TIME',title:'创建时间',width:'15%',align:'center'},
				{field:'opt',title:'操作',width:'15%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="deleterole" onclick="deleteNumofMakeUpById(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="4">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}


/**
 * 删除补录信息数据
 * @param id
 */
function deleteNumofMakeUpById(id)
{
	var url = contextPath + '/weixincontrol/deleteNumofMakeUp.action';
	var data1 = new Object();
	
	var codearr = [];
	codearr.push(id);
	
	data1.ids=codearr.toString();
	data1.lotteryPlay=$("#lotteryPlayC").combobox('getValue');
	
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
 * 批量删除补录数据
 */
function deleteNumofMakeUp()
{
	var url = contextPath + '/weixincontrol/deleteNumofMakeUp.action';
	var data1 = new Object();
	
	var rows = $('#datagrid').datagrid('getSelections');
	
	var codearr = [];
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
	}
	
	
	data1.ids=codearr.toString();
	data1.lotteryPlay=$("#lotteryPlayC").combobox('getValue');
	
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
 * 根据id获取补录信息详情
 */
function getLotteryPlayDetail()
{
	var data = new Object();
	data.id=$("#lotteryPlayC").combobox('getValue');
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/weixincontrol/getDetailLotteryPlay.action',
        data:data,
        dataType: "json",
        success: function (result) {
        	
        	issueNumLen = result.issueNumLen;
        	lotteryNumber = result.lotteryNumber;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
   });
	
	
}



