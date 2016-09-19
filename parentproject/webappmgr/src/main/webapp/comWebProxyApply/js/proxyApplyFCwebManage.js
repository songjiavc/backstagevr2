var idArr = [];//选中的应用id数据

$(document).ready(function(){
	
			bindComboboxChange();//为应用省份绑定区域级联事件
			initQueryProvince();
			closeDialog();//页面加载时关闭弹框
			initDatagrid();//初始化数据列表
			
			idArr = [];
		});



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
 * 绑定上级角色下拉框改变事件
 */
function bindComboboxChange()
{
	
	//模糊查询“省”级联
	$("#privinceC").combobox({

		onSelect: function (rec) {
			//加载市级数据
			initQueryCities(rec.pcode);
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
	data.isHasall = false;//包含"全部"
	data.pcode = proId;
	
	$('#cityC').combobox({
			queryParams:data,
			url:contextPath+'/product/getCityList.action',
			valueField:'ccode',
			textField:'cname',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
				 	
				 if(data1.length>0)
					 {
					 	$('#cityC').combobox('select',data1[data1.length-1].ccode);
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
 * 重置
 */
function reset()
{
	initQueryProvince();
	$("#statusC").combobox('select','');
}

/**
 * 获取省份的“全部”选项code
 * @returns {String}
 */
function getProvinceAllId()
{
	var url = contextPath + '/menu/getConstant.action';
	var data1 = new Object();
	data1.constantName='PROVINCE_ALL';//省“全部”编码
	
	var proAllId = '';
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
        	proAllId = data.message;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return proAllId;
}



//关闭弹框
function closeDialog()
{
	$("#detailApplyProxy").dialog('close');
	$("#addOrUpdateAgent").dialog('close');
}

/**
 * 初始化应用列表数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.status = $("#statusC").combobox('getValue');//获取省份
	params.province = $("#privinceC").combobox('getValue');//获取省份
	params.city = $("#cityC").combobox('getValue');//获取市
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/cWebProxy/getApplyProxyList.action',//'datagrid_data1.json',
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
		        {field:'name',width:'10%',title:'姓名',align:'center'},
		        {field:'isConnectName',width:'10%',title:'是否从事彩票行业',align:'center'  },
		        {field:'statusName',width:'10%',title:'状态',align:'center'  },
				{field:'provinceName',title:'省',width:'8%',align:'center'},
				{field:'cityName',title:'市',width:'8%',align:'center'},
				{field:'createTime',title:'创建时间',width:'15%',align:'center'},
				{field:'opt',title:'操作',width:'37%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="detailApplyProxy(&quot;'+row.id+'&quot;)" href="javascript:void(0)">详情</a>'
			                +'<a class="deleterole" onclick="deleteApplyProxy(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
			                
			                if(row.status=='2')//回访符合状态
			                	{
			                		if(null!=row.proxyId)//修改代理信息updateProxyOfApplyProxy
			                			{
			                				btn +='<a class="updateProxy" onclick="updateProxyOfApplyProxy(&quot;'+row.proxyId+'&quot;,&quot;'+row.id+'&quot;)" href="javascript:void(0)">修改代理信息</a>';
			                			}
			                		else
			                			{//添加代理信息addProxyOfApplyProxy
			                				btn +='<a class="addProxy" onclick="addProxyOfApplyProxy(&quot;'+row.id+'&quot;)" href="javascript:void(0)">添加代理信息</a>';
			                			}
			                	}
			                else
			                	if(row.status=='0')//待回访状态
			                		 {
			                		 	btn += '<a class="ok" onclick="updateApplyProxyById(&quot;'+row.id+'&quot;,2)" href="javascript:void(0)">回访符合</a>'
						                +'<a class="notok" onclick="updateApplyProxyById(&quot;'+row.id+'&quot;,1)" href="javascript:void(0)">回访不符合</a>';
			                		 }
			                
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	    	 $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
		        $('.editcls').linkbutton({text:'查看详情',plain:true,iconCls:'icon-filter'}); 
		        $('.ok').linkbutton({text:'回访符合',plain:true,iconCls:'icon-redo'}); 
		        $('.notok').linkbutton({text:'回访不符合',plain:true,iconCls:'icon-undo'}); 
		        
		        $('.updateProxy').linkbutton({text:'修改代理信息',plain:true,iconCls:'icon-tip'}); 
		        $('.addProxy').linkbutton({text:'添加代理信息',plain:true,iconCls:'icon-tip'}); 
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}

/**
 * 
 * @param id
 * @param operatype
 */
function updateApplyProxyById(id,operatype)
{
	var url = contextPath + '/cWebProxy/updateApplyProxys.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	codearr.push(id);//code
		
		
	
	if(deleteFlag)
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台data
				
				data1.operatype = operatype;
				
				var alertMsg = "您确认更新选中数据状态？";//默认的提示信息为删除数据的提示信息
				
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
				                	
				                	if('2'==operatype)//若为回访符合操作，则要填写代理相关信息
				            		{
				            			addProxyOfApplyProxy(id);
				            			
				            		}
				                	
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
 * 添加“回访符合”状态的代理信息的代理登陆号信息
 * @param applyProxyId
 */
function addProxyOfApplyProxy(applyProxyId)
{
  	$('.panel-title.panel-with-icon').html('添加代理');
  	$('#addOrUpdateAgentForm').form('clear');
  	$("#addOrUpdateAgent").dialog('open');
  	initAddFormParentId('');
  	
	var url = contextPath + '/cWebProxy/getDetailApplyProxy.action';
	var data1 = new Object();
	data1.id=applyProxyId;//应用的id
	
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "get",
	        url: url,
	        data:data1,
	        dataType: "json",
	        success: function (data) {
	        	
	        	
					$('#addOrUpdateAgentForm').form('load',{
						addFormName:data.name,
						addFormAddress:data.address,
						addFormTelephone:data.telephone,
						addFormProvince:data.province,
						addFormCity:data.city,
						applyProxyId:applyProxyId//填充代理申请信息id
						
					});
					
					initProvince('update',data.province,data.city,'');
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/menu/error.action";
	        }
		});
		
}

function initAddFormParentId(parentId){
	$('#addFormParentId').combobox('clear');//清空combobox值
	$('#addFormParentId').combobox({
		queryParams:{
			
		},//暂时没有任何需要查询的条件
		url:contextPath+'/agent/getSczyList.action',
		valueField : 'id',
		textField : 'name',
		onLoadSuccess: function (data) { //数据加载完毕事件
			if (parentId == '')
             {
            	 $("#addFormParentId").combobox('select',data[0].id);
             }
             else
        	 {
            	//使用“setValue”设置选中值不会触发绑定事件导致多次加载市级数据，否则会多次触发产生错误
            	 $("#addFormParentId").combobox('setValue', parentId);
            	 $("#addFormParentId").combobox('readonly');
        	 }
         }
	});
}

/**
 * 修改“回访符合”状态的代理信息的代理登陆号信息
 * @param applyProxyId
 */
function updateProxyOfApplyProxy(proxyId,applyProxyId)
{
		/**
		 * 站点修改
		 */
		$('.panel-title.panel-with-icon').html('修改代理信息');
		var url = contextPath + '/agent/getAgentDetail.action';
		var paramData = new Object();
		paramData.id=proxyId;
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "get",
	        cache:false,
	        url: url,
	        data:paramData,
	        dataType: "json",
	        success: function (data) {
				$('#addOrUpdateAgentForm').form('load',data);
				$("#applyProxyId").val(applyProxyId);
				initProvince('update',data.addFormProvince,data.addFormCity,data.addFormRegion);
				initAddFormParentId(data.addFormParentId);
				$('#addFormAgentCode').attr('readonly',true);
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            alert(errorThrown);
	        }
		});
		$("#addOrUpdateAgent").dialog("open");//打开修改用户弹框
}



/**
 *代理申请信息详情
 */
function detailApplyProxy(id)
{
	var url = contextPath + '/cWebProxy/getDetailApplyProxy.action';
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
						name:data.name,
						address:data.address,
						telephone:data.telephone,
						statusName:data.statusName,
						provinceName:data.provinceName,
						cityName:data.cityName,
						isConnectName:data.isConnectName,
						remark:data.remark
						
					});
					
			
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/menu/error.action";
	        }
		});
		
		
		$("#detailApplyProxy").dialog('open');
	
		
}


/**
 * 初始化省数据
 * @param addOrUpdate:标记当前是添加表单操作还是修改表单的操作,值为"add" 和"update"
 * @param provinceId:当前操作的省份combobox标签的id
 * @param pcode:应该选中的省份的code
 */
function initProvince(addOrUpdate,pcode,oldccode,oldacode)
{
	$('#addFormProvince').combobox('clear');//清空combobox值
	$('#addFormProvince').combobox({
			queryParams:{
				isHasall : false
			},
			url:contextPath+'/product/getProvinceList.action',
			valueField:'pcode',
			textField:'pname',
			 onLoadSuccess: function (data) { //数据加载完毕事件
                 if (data.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#addFormProvince").combobox('select',data[0].pcode);
                 }
                 else
            	 {
                	//使用“setValue”设置选中值不会触发绑定事件导致多次加载市级数据，否则会多次触发产生错误
                	 $("#addFormProvince").combobox('select', pcode);
            	 }
             },
             onSelect: function(rec){
            	 if(rec.pcode!=pcode){
            		 oldccode="";
            		 oldacode="";
            	 }
            	 initAddFormCity(addOrUpdate,rec.pcode,oldccode,oldacode);
 		    }
		});
}

function initAddFormCity(addOrUpdate,pcode,oldccode,oldacode){
	//初始化城市combo
	$('#addFormCity').combobox({
		url : contextPath+'/product/getCityList.action',
		queryParams:{
			isHasall : false,    //不包含"全部",
			pcode : pcode
		},
		valueField:'ccode',
		textField:'cname',
		 onLoadSuccess: function (data) { //数据加载完毕事件
             if (data.length > 0 && "add" == addOrUpdate) 
             {
            	 $("#addFormCity").combobox('select',data[0].ccode);
             }
             else
        	 {
            	 if(data.length > 0 &&"update" == addOrUpdate&&"" == oldccode)
            		 {//在修改表单中级联加载市级数据时也要默认选中全部
            		 $("#addFormCity").combobox('select',data[data.length-1].ccode);
            		 }
            	 else
            		 {//当修改产品初始化市级数据时设置选中当前数据值
            		 	$("#addFormCity").combobox('select', oldccode);
            		 }
        	 }
         },
         onSelect: function(rec){
        	 if(rec.ccode!=oldccode){
        		 oldacode="";
        	 }
        	 initAddFormRegion(addOrUpdate,rec.ccode,oldacode);
		    }
	}); 
}

function initAddFormRegion(addOrUpdate,ccode,oldacode){
	
	//初始化乡镇区combo
	$('#addFormRegion').combobox({
		url :  contextPath+'/product/getRegionList.action',
		queryParams:{
			isHasall : false,    //不包含"全部",
			ccode : ccode
		},
		valueField:'acode',
		textField:'aname',
		 onLoadSuccess: function (data) { //数据加载完毕事件
             if (data.length > 0 && "add" == addOrUpdate) 
             {
            	 $("#addFormRegion").combobox('select',data[0].acode);
             }
             else
        	 {
            	 if(data.length > 0 &&"update" == addOrUpdate&&"" == oldacode)
            		 {
            		 	$("#addFormRegion").combobox('select',data[0].acode);
            		 }
            	 else
            		 {
            		 	$("#addFormRegion").combobox('select', oldacode);
            		 }
        	 }
         }
	}); 
}

/**
 * 删除预测信息数据
 * @param id
 * @param appStatus
 */
function deleteApplyProxy(id)
{
	var url = contextPath + '/cWebProxy/deleteApplyProxys.action';
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





/**
 * 批量删除代理申请数据
 */
function deleteApplyProxyList(operaType)
{
	var url = contextPath + '/cWebProxy/deleteApplyProxys.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		
		
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
 * 批量更新代理申请数据
 */
function updateApplyProxyStatus(operaType)
{
	var url = contextPath + '/cWebProxy/updateApplyProxys.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		
		
	}
	
	if(deleteFlag)
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台data
				
				data1.operatype = operaType;
				
				var alertMsg = "您确认更新选中数据状态？";//默认的提示信息为删除数据的提示信息
				
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

//提交添加权限form表单
function submitAddAgent()
{
	$('#addOrUpdateAgentForm').form('submit',{
		url:contextPath+'/cWebProxy/saveOrUpdateProxy.action',
		onSubmit:function(param){
			return $('#addOrUpdateAgentForm').form('validate');
		},
		success:function(data){
			$.messager.alert('提示', eval("(" + data + ")").message);
			closeDialog();
        	initDatagrid();
		}
	});
}

//修改帐号form表单
function submitUpdateagent()
{
	$('#addOrUpdateAgentForm').form('submit',{
		url:contextPath+'/cWebProxy/saveOrUpdateProxy.action',
		onSubmit:function(param){
			return $('#updateagentForm').form('enableValidation').form('validate');
		},
	    success:function(data){
	    	//data从后台返回后的类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#updateagent").dialog('close');//初始化修改权限弹框关闭
	    	//在修改权限后刷新权限数据列表
	    	initDatagrid();
	    	$('#updateagentForm').form('clear');
	    	initAddFormParentId(initParam);
	    }
	});
}


/*********校验************/

/**
 * 自定义校验应用名称
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkAname: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAname.message = "当前应用名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAname.message = "当前应用名称已存在"; 
        		
                return !checkAppName($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});







/**
 * 自定义校验商品销售价格
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkSellprice: {//自定义校验sellprice
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkSellprice.message = "当前商品销售价格不可为空且长度不可以超过10个字符";  
        		productList.remove(param[0]);//row.id
				$('#'+param[2]).datagrid('unselectRow', param[1]);//productDatagrid,index
                return false;  
            }
        	else
    		{
        		rules.checkSellprice.message = "当前商品销售价格不符合金额的输入规则"; 
        		var flag = checkEditorSellprice(value);
        		if(!flag)
        			{
	        			productList.remove(param[0]);//row.id
	    				$('#'+param[2]).datagrid('unselectRow', param[1]);//productDatagrid,index
        			}
        		
                return flag;
    		}
        	
        	
        }
    }
});

/**
 * 自定义校验商品使用天数
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkProbation: {//自定义校验probation
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkProbation.message = "当前商品试用天数不可为空且长度不可以超过10个字符";  
        		productList.remove(param[0]);//row.id
				$('#'+param[2]).datagrid('unselectRow', param[1]);//productDatagrid,index
                return false;  
            }
        	else
    		{
        		rules.checkProbation.message = "当前商品试用天数只可以输入0或正整数"; 
                var flag = checkEditorProbation(value);
        		if(!flag)
        			{
	        			productList.remove(param[0]);//row.id
	    				$('#'+param[2]).datagrid('unselectRow', param[1]);//productDatagrid,index
        			}
        		
                return flag;
    		}
        	
        	
        }
    }
});

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

