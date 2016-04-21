var idArr = [];//选中的应用id
var areaList = new map();

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			/*initQueryProvince();//初始化模糊查询的省份
*/			initDatagrid();//初始化数据列表
			clearLists();
//			bindComboboxChange();//为通行证的模糊查询的省份条件绑定下拉框级联事件
		});



function bindComboboxChange()
{
	
	/*//模糊查询“省”级联
	$("#privinceC").combobox({

		onSelect: function (rec) {
			//加载市级数据
			initQueryCities(rec.pcode);
		}

		}); */
	
	/*//有效时间的范围是当前日期的60天内可选
	$('#startTimeA').datebox().datebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate()+60);//获取当前日期60天后的日期
			return d1<=date && date<=d2;
		}
	});
	
	//若修改开始时间，则要重新触发结束时间校验
	 $("#startTimeU").datebox({  
	        onSelect: function(date){  
	            $("#endTimeU").datebox('validate');
	        }  
	    });  */
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

/**
 * 根据选中的“省”初始化模糊查询“市”数据
 * @param proId
 */
function initQueryCities(proId)
{
	$('#cityC').combobox('clear');//清空combobox值
	
	var data = new Object();
	data.isHasall = true;//包含"全部"
	data.pcode = proId;
	
	$('#cityC').combobox({
			queryParams:data,
			url:contextPath+'/product/getCityList.action',
			valueField:'ccode',
			textField:'cname',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
				 $('#cityC').combobox('select',data1[data1.length-1].ccode);
					
             }
		}); 
}



/**
 * 重置
 */
function reset()
{
	$("#kjnoticeNameC").val("");
	$("#lotterytypeC").combobox('setValue','');
	$("#kjnoticeStatusC").combobox('setValue','');
	
//	initQueryProvince();//重新初始化模糊查询省份
}




//关闭弹框
function closeDialog()
{
	$("#addKaijiangNotice").dialog('close');
	$("#updateKaijiangNotice").dialog('close');
	$("#detailKaijiangNotice").dialog('close');
}


/**
 * 初始化应用列表数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.appNoticeName = $("#kjnoticeNameC").val().trim();
	params.noticeStatus = $("#kjnoticeStatusC").combobox('getValue');
	params.lotteryType = $("#lotterytypeC").combobox('getValue');
	params.appcategory = '3';//APP_CATEGORY_COMPANY_KAIJIANG="3";//公司开奖公告
	/*params.province = $("#privinceC").combobox('getValue');//获取省份
	params.city = $("#cityC").combobox('getValue');//获取市
*/	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/notice/getNoticeList.action',//'datagrid_data1.json',
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
				{field:'noticeStatus',hidden:true},
				{field:'appNoticeName',title:'应用公告名称',width:150,align:'center'},
				{field:'noticeStatusName',title:'应用公告状态',width:150,align:'center'},
				{field:'lotteryType',width:50,title:'彩种',align:'center',  
		            formatter:function(value,row,index){  
		            	var lotteryTypeName ='';
		            	switch(value)
		            	{
		            		case '1':lotteryTypeName='体彩';break;
		            		case '2':lotteryTypeName='福彩';break;
		            	}
		            	return lotteryTypeName;  
		            }  },
		        {field:'startTimestr',width:120,title:'有效开始时间',align:'center'},
				{field:'endTimestr',title:'有效结束时间',width:120,align:'center'},
				{field:'createTime',title:'创建时间',width:120,align:'center'},
				{field:'opt',title:'操作',width:160,align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updatKjnotice(&quot;'+row.id+'&quot;,&quot;'+row.noticeStatus+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteKjnotice(&quot;'+row.id+'&quot;,&quot;'+row.noticeStatus+'&quot;)" href="javascript:void(0)">删除</a>'
			                	+'<a class="detailrole" onclick="detailKjNotice(&quot;'+row.id+'&quot;)" href="javascript:void(0)">查看详情</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        $('.detailrole').linkbutton({text:'查看详情',plain:true,iconCls:'icon-filter'}); 
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}




/**
 * 初始化区域数据
 */
var setting ;
var zNodes ;//放置树节点的全局变量
function initAreaData(areaDataGridId)
{
	
	var data = new Object();
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/advertisement/getTreedataOfAdvertisement.action',
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
        		};
        		zNodes = data;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	$.fn.zTree.init($("#"+areaDataGridId), setting, zNodes);
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









function clearLists()
{
	areaList = new map();
}

/**
 * 查看开奖公告详情
 * @param id
 */
function detailKjNotice(id)
{
	$("#detailKaijiangNotice").dialog('open');
	var url = contextPath + '/notice/getDetailNotice.action';
	var data1 = new Object();
	data1.id=id;//应用的id
	
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "get",
	        url: url,
	        data:data1,
	        dataType: "json",
	        success: function (data) {
	        	
					$('#ffDetail').form('load',{
						id:data.id,
						appNoticeName:data.appNoticeName,
						appNoticeWord:data.appNoticeWord,
						lotteryType:data.lotteryType//彩种
					});
					
					if('1' == data.lotteryType)
					{
						$("#ldiLName").val("体彩");
					}
					else if('2' == data.lotteryType)
					{
						$("#ldiLName").val("福彩");
					}
					
					//初始化区域树
					initAreaData('areaDataGridD');
					//选中当前应用广告发布的区域
					var zTree = $.fn.zTree.getZTreeObj("areaDataGridD");
					var node;//ztree树节点变量
					var cityIds = checkAreas(id);
					$.each(cityIds,function(j,cityId){
						areaList.put(cityId, cityId);
						node = zTree.getNodeByParam("id",cityId);
						if(null != node)
						{
							zTree.checkNode(node, true, true);//设置树节点被选中
						}
	    			});
					
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
}


/**
 *开奖公告修改
 */
function updatKjnotice(id,status)
{
	var updateFlag = true;
	if('1' == status)
		{
			$.messager.alert('提示',"当前通告已经发布,不可以进行修改操作!");
			updateFlag = false;
		}
	if(updateFlag)
		{
			$("#updateKaijiangNotice").dialog('open');
			var url = contextPath + '/notice/getDetailNotice.action';
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
								appNoticeName:data.appNoticeName,
								appNoticeWord:data.appNoticeWord,
								lotteryType:data.lotteryType//彩种
							});
							//初始化区域树
							initAreaData('areaDataGridU');
							//选中当前应用广告发布的区域
							var zTree = $.fn.zTree.getZTreeObj("areaDataGridU");
							var node;//ztree树节点变量
							var cityIds = checkAreas(id);
							$.each(cityIds,function(j,cityId){
								areaList.put(cityId, cityId);
								node = zTree.getNodeByParam("id",cityId);
								if(null != node)
								{
									zTree.checkNode(node, true, true);//设置树节点被选中
								}
			    			});
							
			        	
			        },
			        error: function (XMLHttpRequest, textStatus, errorThrown) {
			            window.parent.location.href = contextPath + "/error.jsp";
			        }
				});
		}
	
		
		
}







/*
 * 获取当前开奖公告发布的区域数据
 */
function checkAreas(id)
{
	var data = new Object();
	data.id = id;
	
	var area ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/notice/getAreasOfNotice.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	area = returndata;
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return area;
}


/**
 * 提交保存公司公告
 */
function submitAddKaijiangNotice(operatype)
{
	$('#ff').form('submit',{
		url:contextPath+'/notice/saveOrUpdateKj.action',
		onSubmit:function(param){
			var flag = false;
			param.noticeStatus = operatype;
			
			var codearr = new Array();
			 var treeObj=$.fn.zTree.getZTreeObj("areaDataGridA"),
		     nodes=treeObj.getCheckedNodes(true),
		     v="";
			
			for(var i=0; i<nodes.length; i++)
			{
				if(!nodes[i].isParent)
					{
						areaList.put(nodes[i].id, nodes[i].id);
					}
				
			}
			param.areadata = JSON.stringify(areaList);
			
			
			if($('#ff').form('enableValidation').form('validate')&&areaList.keys.length>0)
				{
					flag = true;
				}
			else
				if(areaList.keys.length==0)
				{
					flag = false;
					$.messager.alert('提示', "请选择开奖公告要发布给哪些区域!");
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addKaijiangNotice").dialog('close');//初始化添加通行证组弹框关闭
	    	$('#ff').form('clear');//清空表单内容
	    	$("#lotteryTypeA").combobox('setValue',"1");
	    	
	    	initDatagrid();
	    	
	    	
	    }
	});
}

/**
 * 提交修改开奖公告
 */
function submitUpdateKaijiangNotice(operatype)
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/notice/saveOrUpdateKj.action',
		onSubmit:function(param){
			var flag = false;
			param.noticeStatus = operatype;
			var codearr = new Array();
			 var treeObj=$.fn.zTree.getZTreeObj("areaDataGridU"),
		     nodes=treeObj.getCheckedNodes(true),
		     v="";
			 areaList = new map();//在统计区域选中数据之前清空list，因为arealist没有选中触发事件和取消选中触发事件，所以在此处计算选中的区域数据
			for(var i=0; i<nodes.length; i++)
			{
				if(!nodes[i].isParent)
					{
						if(!areaList.contain(nodes[i].id))
							{
								areaList.put(nodes[i].id, nodes[i].id);
							}
						
					}
				
			}
			
			param.areadata = JSON.stringify(areaList);
			
			
			
			if($('#ffUpdate').form('enableValidation').form('validate') &&areaList.keys.length>0 )
				{
					flag = true;
				}
			else
				if(areaList.keys.length==0)
				{
					flag = false;
					$.messager.alert('提示', "请选择开奖公告要发布给哪些区域!");
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateKaijiangNotice").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


/**
 * 删除开奖公告数据
 */
function deleteKjnotice(id,status)
{
	var url = contextPath + '/notice/deleteNotices.action';
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
		if('1' == status)
		{//若为发布状态的应用公告，则不可以删除
			$.messager.alert('提示',"当前开奖公告已经发布,不可以进行删除操作!");
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
		                    window.parent.location.href = contextPath + "/error.jsp";
		                }
		           });
		        	
	        }  
	    });  
	}
		
	
}



/**
 * 批量删除开奖公告
 */
function deleteKjnoticeList(operaType)
{
	var url = contextPath + '/notice/deleteNotices.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		var comnoticeStatus = rows[i].comnoticeStatus;
		if('1' == comnoticeStatus)
			{
				$.messager.alert('提示',"当前id为"+rows[i].id+"的开奖公告已经发布,不可以进行删除操作!");
				deleteFlag = false;
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

/**
 * 批量发布开奖公告
 * @param operaType
 */
function publishKaijiangNoticeList(operaType)
{
	var url = contextPath + '/notice/publishsNotices.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		/*var comnoticeStatus = rows[i].comnoticeStatus;
		if('1' == comnoticeStatus)
			{
				$.messager.alert('提示',"当前id为"+rows[i].id+"的公司公告已经发布,不可以进行删除操作!");
				deleteFlag = false;
				break;
			}*/
		
	}
	
	if(deleteFlag)
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台data
				
				var alertMsg = "您确认发布选中数据？";//默认的提示信息为删除数据的提示信息
				
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
/*$.extend($.fn.validatebox.defaults.rules, {
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

*//**
 * 自定义校验通行证组编码唯一性
 *//*
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
*/






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

