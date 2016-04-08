var idArr = [];//选中的应用id
var stationGList = new map();//选中的通行证组id数组集合
var appList = new map();//选中的应用数组集合
var areaList = new map();
var forecastList = new map();

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			/*initQueryProvince();//初始化模糊查询的省份
*/			initDatagrid();//初始化数据列表
			clearLists();
			bindComboboxChange();//为通行证的模糊查询的省份条件绑定下拉框级联事件
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
	$("#appCategoryA").combobox({

		onSelect: function (rec) {
			//控制是否显示预测信息的div
			initForecastDiv('appCategoryA','forecastDivA','appCategoryhiddenA','forcastDataGridA');
		}

		});
	
	$("#appCategoryU").combobox({

		onSelect: function (rec) {
			//控制是否显示预测信息的div
			initForecastDiv('appCategoryU','forecastDivU','appCategoryhiddenU','forcastDataGridU');
		}

		});
	
	$("#lotteryTypeA").combobox({

		onSelect: function (rec) {
			//控制是否显示预测信息的div
			initForecastDatagird('forcastDataGridA');
			initAppDatagrid('','appDataGridA');//更新带彩种的应用列表数据
			clearAppAndForecastLists();
		}

		});
	
	$("#lotteryTypeU").combobox({

		onSelect: function (rec) {
			//控制是否显示预测信息的div
			var id = $("#idU").val();//获取当前修改的应用公告id
			initForecastDatagird('forcastDataGridU');
			initAppDatagrid(id,'appDataGridU');//更新带彩种的应用列表数据
			clearAppAndForecastLists();
		}

		});
	
	//有效时间的范围是当前日期的60天内可选
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
	    }); 
	 
	 $("#endTimeA").datebox({  
	        onChange: function(date){  
	        	
	        	initForecastDatagird('forcastDataGridA');
	        }  
	    }); 
	 $("#endTimeU").datebox({  
	        onChange: function(date){  
	        	
	        	initForecastDatagird('forcastDataGridU');
	        }  
	    }); 
}

/**
 * 根据应用公告类别的下拉框的选中项控制预测信息列表的显示
 * @param appCategoryId:应用公告类别的下拉框的id
 * @param forcastDivId：预测信息列表的div的id
 * @param forcastDatagridId：预测信息datagrid的id
 */
function initForecastDiv(appCategoryId,forcastDivId,appCategoryHiddenId,forcastDatagridId)
{
	var appCategory = $("#"+appCategoryId).combobox('getValue');
	$("#"+appCategoryHiddenId).val(appCategory);
	if('4' == appCategory)
		{
			//当前选项是要创建预测信息公告
			$("#"+forcastDivId).show();
			initForecastDatagird(forcastDatagridId);
		}
	else
		{
			forecastList = new map();
			$("#"+forcastDivId).hide();
		}
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
	$("#noticeNameC").val("");
	$("#startTimeC").datebox('setValue','');
	$("#endTimeC").datebox('setValue','');
	$("#lotterytypeC").combobox('setValue','');
	$("#noticeStatusC").combobox('setValue','');
	
//	initQueryProvince();//重新初始化模糊查询省份
}




//关闭弹框
function closeDialog()
{
	$("#addNotice").dialog('close');
	$("#updateNotice").dialog('close');
}

/**
 * 校验当前用户是否可以选择通行证组的公告发布范围
 * 只有省中心不可以选择通行证组，公司内部和市中心都可以选择通行证组
 * @param ugroupId
 */
function checkNoticeUseUgroup(ugroupId,adId,stationDataGridId)
{
	var data = new Object();
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/notice/checkUseUgroup.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	var flag = returndata.exist;//当前用户是否可以选择通行证组的范围
        	
        	if(flag)
        		{
        			$("#"+ugroupId).show();
        			//若为市中心用户或公司用户，则可以选择当前应用公告给哪些通行证组使用
        			initStationGList(adId,stationDataGridId);
        		}
        	else
        		{
        			$("#"+ugroupId).hide();
        		}
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
}

/**
 * 初始化应用列表数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.appNoticeName = $("#noticeNameC").val().trim();
	params.startTime = $("#startTimeC").datebox('getValue');
	params.endTime = $("#endTimeC").datebox('getValue');
	params.noticeStatus = $("#noticeStatusC").combobox('getValue');
	params.lotteryType = $("#lotterytypeC").combobox('getValue');
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
				{field:'appNoticeName',title:'应用公告名称',width:'15%',align:'center'},
				{field:'noticeStatusName',title:'应用公告状态',width:'8%',align:'center'},
				{field:'lotteryType',width:'7%',title:'彩种',align:'center',  
		            formatter:function(value,row,index){  
		            	var lotteryTypeName ='';
		            	switch(value)
		            	{
		            		case '1':lotteryTypeName='体彩';break;
		            		case '2':lotteryTypeName='福彩';break;
		            	}
		            	return lotteryTypeName;  
		            }  },
		        {field:'startTimestr',width:'15%',title:'有效开始时间',align:'center'},
				{field:'endTimestr',title:'有效结束时间',width:'15%',align:'center'},
				{field:'createTime',title:'创建时间',width:'15%',align:'center'},
				{field:'opt',title:'操作',width:'23%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateNotice(&quot;'+row.id+'&quot;,&quot;'+row.noticeStatus+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteNotice(&quot;'+row.id+'&quot;,&quot;'+row.noticeStatus+'&quot;)" href="javascript:void(0)">删除</a>';
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
	        
	        
	    }
	});
}

/**
 * 初始化新建应用公告弹框内容显示
 */
function initAddPage()
{
	//初始化彩种下拉框选择值
//	$("#lotteryTypeA").combobox('setValue','1');
	
	var areamsg = getLoginArea();
	var appcategory = areamsg.appcategory;
	if('1'==appcategory ||'0'==appcategory)//在登录用户是市中心或者是省中心用户时赋予区域
	{
		$("#appCategoryhiddenA").val(appcategory);//设置应用公告的类别值
		$("#appCatoryDivA").hide();//公告类别下拉框隐藏
		$("#forecastDivA").hide();//预测信息列表所属div隐藏
	}
	else
		{//若登录用户不是省中心或者市中心，则应用公告类别的值是和下拉框关联的
			$("#appCatoryDivA").show();
			var appCatogoryCombo = $("#appCategoryA").combobox('getValue');
			$("#appCategoryA").combobox('setValue',appCatogoryCombo);
			if('4' == appCatogoryCombo)
				{//若默认值是预测信息公告类型，则显示预测信息div
					$("#forecastDivA").show();//预测信息列表所属div隐藏
				}
			else
				{
					$("#forecastDivA").hide();//预测信息列表所属div隐藏
				}
			
			$("#appCategoryhiddenA").val(appCatogoryCombo);
		}
}

/**
 * 获取当前登录用户的区域信息
 * @returns
 */
function getLoginArea()
{
	var returnarea ;
	var data = new Object();
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/notice/getLoginArea.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	returnarea = returndata;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return returnarea;
}

/**
 * 初始化appdatagrid数据
 * @param appDatagridId
 */
function initAppDatagrid(noticeId,appDatagridId)
{
	var params = new Object();
	params.appType = '1';//只对已经上架的应用发布广告
	
	var areamsg = getLoginArea();
	var appcategory = areamsg.appcategory;
	if('1'==appcategory ||'0'==appcategory)//在登录用户是市中心或者是省中心用户时赋予区域
		{
			params.province=areamsg.province;
			/*目前定义的规则为：省中心和市中心都可以看见当前省级区域内的应用列表
			（※：因为市可以有定制应用也可以使用通用的省级应用，所以应该省级范围内的应用都可以显示在列表内）*/
			if('1'==appcategory)
				{//若当前登录用户是市中心用户时，则也赋予市级区域信息
					params.city=areamsg.city;
				}
		}
	
	
	if('appDataGridU' == appDatagridId)
		{
			var apps = getAppsOfNotice(noticeId);
			var appId;
			for (var i = 0; i < apps.length; i++) {
				
				appId = apps[i].id;
				appList.put(appId, appId);
			}
			params.lotteryType = $("#lotteryTypeU").combobox('getValue');
		}
	else
		{
			params.lotteryType = $("#lotteryTypeA").combobox('getValue');
		}
	
	$('#'+appDatagridId).datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/app/getAppList.action',//'datagrid_data1.json',
		method:'get',
		border:false,
		singleSelect:false,
		fit:true,//datagrid自适应
		fitColumns:true,
		pagination:true,
		collapsible:false,
		pageSize:2,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[2,5,10],
//		toolbar:toolbar,
		columns:[[
				{field:'ck',checkbox:true},
				{field:'id',hidden:true},
				{field:'appStatus',hidden:true},//应用状态(0:待上架1:上架2:下架3:更新)
		        {field:'appName',width:120,title:'应用名称',align:'center'},
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
				{field:'provinceName',title:'省',width:80,align:'center'},
				{field:'cityName',title:'市',width:80,align:'center'}
		    ]],  
	    onLoadSuccess:function(data){  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="5">没有数据</td></tr>');
			}
	        
	        var selectedRows = $('#'+appDatagridId).datagrid('getRows');
	    	if(appList.keys.length>0)
	    		{
	    			var appId ;
		    		for(var i=0;i<appList.keys.length;i++)
	    			{
		    			appId = appList.keys[i];
		    			$.each(selectedRows,function(j,selectedRow){
		    				
		    				if(selectedRow.id == appId){
		    					
		    					 $('#'+appDatagridId).datagrid('selectRow',j);
		    				}
		    			});
	    			}
	    		}
	    	
	    } ,
	    onSelect:function(index,row){
	    	
	    	if(!appList.contain(row.id))//新数据
	    		{
	    			appList.put(row.id, row.id);
//	    			initAreaData();//初始化区域数据
	    		}
	    	
	    },
	    onUnselect:function(index,row){
			
	    	appList.remove(row.id);
//	    	initAreaData();//初始化区域数据
			
		},
		onSelectAll:function(rows){
			$.each(rows,function(i,row){
				
				if(!appList.contain(row.id))//放入不存在新数据
	    		{
					appList.put(row.id, row.id);
	    		}
			});
//			initAreaData();//初始化区域数据
		},
		onUnselectAll:function(rows){
			$.each(rows,function(i,row){
				if(appList.contain(row.id))//移除已存在数据
	    		{
					appList.remove(row.id);
	    		}
				
			});
//			initAreaData();//初始化区域数据
		}
	});
}

/**
 * 初始化区域数据
 */
var setting ;
var zNodes ;//放置树节点的全局变量
function initAreaData(areaDataGridId,isProvinceManager,province)
{
	
	var data = new Object();
	
	if(isProvinceManager)//只有省中心角色的用户才会根据当前用户的区域进行区域数据加载，若为公司用户则不会传入省份参数
		{
			data.isProvince = isProvinceManager;
			data.provinceCode = province;
		}
	
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
 * 获取当前应用公告的应用关联
 * @param adId
 */
function getAppsOfNotice(noticeId)
{
	var returnlist ;
	var data = new Object();
	data.id = noticeId;
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/notice/getAppsOfNotice.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	returnlist = returndata;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return returnlist;
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






/**
 * 初始化通行证组列表数据
 */
function initStationGList(id,stationDataGridId)
{
	var params = new Object();
	if('stationDataGridU' == stationDataGridId)
	{
		
		var ugroups = checkStations(id, stationDataGridId);
		var ugId;
		for (var i = 0; i < ugroups.length; i++) {
			
			ugId = ugroups[i].id;
			stationGList.put(ugId, ugId);
		}
	}
	
	//渲染列表
	$('#'+stationDataGridId).datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url: contextPath + '/userGroup/getUsergroupList.action',
		method:'get',
		border:false,
		singleSelect:false,
		fitColumns:true,
		pagination:true,
		collapsible:false,
		pageSize:2,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[2,5,10],
		columns:[[
					{field:'ck',checkbox:true},
					{field:'id',hidden:true},
					{field:'userGroupCode',title:'通行证组编码',width:150,align:'center'},
					{field:'userGroupName',width:120,title:'通行证组名称',align:'center'},
					{field:'createTime',title:'创建时间',width:130,align:'center'}
		    ]],
	    onLoadSuccess:function(data){ 
	    	 if(data.rows.length==0){
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="4">没有数据</td></tr>');
				}
	    	
	    	var selectedRows = $('#'+stationDataGridId).datagrid('getRows');
	    	if(stationGList.keys.length>0)
	    		{
	    			var ugId ;
		    		for(var i=0;i<stationGList.keys.length;i++)
	    			{
		    			ugId = stationGList.keys[i];
		    			$.each(selectedRows,function(j,selectedRow){
		    				
		    				if(selectedRow.id == ugId){
		    					
		    					 $('#'+stationDataGridId).datagrid('selectRow',j);
		    				}
		    			});
	    			}
	    		}
	    	
	    } ,
	    onSelect:function(index,row){
	    	
	    	if(!stationGList.contain(row.id))//新数据
	    		{
	    			stationGList.put(row.id, row.id);
	    		}
	    	
	    },
	    onUnselect:function(index,row){
			
	    	stationGList.remove(row.id);
			
		},
		onSelectAll:function(rows){
			$.each(rows,function(i,row){
				
				if(!stationGList.contain(row.id))//放入不存在新数据
	    		{
					stationGList.put(row.id, row.id);
	    		}
			});
		},
		onUnselectAll:function(rows){
			$.each(rows,function(i,row){
				if(stationGList.contain(row.id))//移除已存在数据
	    		{
					stationGList.remove(row.id);
	    		}
				
			});
		}
		
	});
}

/**
 * 获取当前应用广告发布的通行证组数据
 * @param id
 * @param stationDataGridId
 * @returns
 */
function checkStations(id,stationDataGridId)
{
	var data = new Object();
	data.id = id;
	
	var stationList ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/notice/getStationOfUsergroup.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	stationList = returndata;
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return stationList;
}

/**
 * 获取当前应用公告对应的预测信息数据
 * @param id
 * @param stationDataGridId
 * @returns
 */
function checkForecast(id,stationDataGridId)
{
	var data = new Object();
	data.id = id;
	
	var forecastList ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/notice/getForecastsOfNotice.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	forecastList = returndata;
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return forecastList;
}

function clearLists()
{
	stationGList = new map();
	appList = new map();
	forecastList = new map();
	areaList = new map();
}

function clearAppAndForecastLists()
{
	appList = new map();
	forecastList = new map();
}



/**
 *应用公告修改
 */
function updateNotice(id,noticeStatus)
{
	var updateFlag = true;
	if('1' == noticeStatus)
		{
			$.messager.alert('提示',"当前应用公告已经发布,不可以进行修改操作!");
			updateFlag = false;
		}
	if(updateFlag)
		{
			$("#updateNotice").dialog('open');
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
								startTime:data.startTimestr,
								endTime:data.endTimestr,//通行证组描述
								appNoticeWord:data.appNoticeWord,
								lotteryType:data.lotteryType,//彩种
								appCategory:data.appCategory//公告类别
							});
							//初始化通行证列表数据
							checkNoticeUseUgroup('txzDivU', id, 'stationDataGridU');//判断当前用户是否为市中心用户，若为市中心用户则加载通行证组数据
							initAppDatagrid(id,'appDataGridU');
							
							
							//初始化区域树
							var roleArr = getLoginuserRole();
						  	var isCityManager = roleArr[0];//是否拥有市中心角色
							var isProvinceManager = roleArr[1];//是否拥有省中心角色
							var currentcode = roleArr[2];
							var province = roleArr[3];
							
							if(!isCityManager)
							{
								$("#areaDivU").show();// id="areaDivA"
								//展示所有的区域信息，树的形式
							  	initAreaData('areaDataGridU',isProvinceManager,province);
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
								
							}
							else
							{
								$("#areaDivU").hide();
								/*var cityIds = checkAreas(id);
								$.each(cityIds,function(j,cityId){
									areaList.put(cityId, cityId);
				    			});*/
							}
							
							//设置公告类型div显示
	//						var areamsg = getLoginArea();
							var appcategory = data.appCategory;//areamsg.appcategory;
							if('1'!=appcategory &&'0'!=appcategory)//在登录用户不是市中心且不是省中心用户时赋予区域
								{
									$("#appCatoryDivU").show();
									//若显示则赋值
									$("#appCategoryU").combobox('setValue',appcategory);
									if('4' == appcategory)
										{//预测类公告
											$("#forecastDivU").show();
											initForecastDatagird('forcastDataGridU');
										}
									else
										{
											$("#forecastDivU").hide();
										}
								}
							else
								{
									$("#appCatoryDivU").hide();
									$("#forecastDivU").hide();
								}
								
							
							/**绑定修改框中的开始日期可选范围**/
							var startFanwei = data.startTimestr;
							$('#startTimeU').datebox().datebox('calendar').calendar({
								validator: function(date){
									var now = new Date();
									var d1 = new Date(startFanwei);
									var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate()+60);//获取当前日期60天后的日期
									return d1<=date && date<=d2;
								}
							});
							$('#startTimeU').datebox('setValue',startFanwei);
			        	
			        },
			        error: function (XMLHttpRequest, textStatus, errorThrown) {
			            window.parent.location.href = contextPath + "/error.jsp";
			        }
				});
		}
	
		
		
}

/**
 * 初始化预测信息列表数据
 * @param forecastDatagirdId：预测信息datagrid的id
 */
function initForecastDatagird(forecastDatagirdId)
{
	forecastList = new map();
	//?※应用的区域应该对预测信息的显示有限制吗？？？目前代码中没有约束，TODO:之后把应用和预测信息做成级联
	var params = new Object();
	if('forcastDataGridU' == forecastDatagirdId)
	{
		var id = $("#idU").val();
		var forecasts = checkForecast(id, forecastDatagirdId);
		var forecastId;
		for (var i = 0; i < forecasts.length; i++) {
			
			forecastId = forecasts[i].id;
			forecastList.put(forecastId, forecastId);
		}
		params.lottertyType = $("#lotteryTypeU").combobox('getValue');
		
		params.startTime = $("#startTimeU").datebox('getValue');
		params.endTime = $("#endTimeU").datebox('getValue');
	}
	else
		{
			params.lottertyType = $("#lotteryTypeA").combobox('getValue');
			params.startTime = $("#startTimeA").datebox('getValue');
			params.endTime = $("#endTimeA").datebox('getValue');
		}
	
	params.noticeData = '1';//应用公告获取预测信息数据
	
	
	//渲染列表
	$('#'+forecastDatagirdId).datagrid({
		singleSelect:false,//true:单选
		rownumbers:false,
		queryParams: params,
		url: contextPath + '/forecast/getForecastList.action',
		method:'get',
		border:false,
		singleSelect:false,
		fitColumns:true,
		pagination:true,
		collapsible:false,
		pageSize:2,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[2,5,10],
		columns:[[
					{field:'ck',checkbox:true},
					{field:'id',hidden:true},
			        {field:'forecastName',width:120,title:'预测信息名称',align:'center'},
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
					{field:'provinceName',title:'省',width:80,align:'center'},
					{field:'cityName',title:'市',width:80,align:'center'}
		    ]],
	    onLoadSuccess:function(data){ 
	    	if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="5">没有数据</td></tr>');
			}
	    	
	    	var selectedRows = $('#'+forecastDatagirdId).datagrid('getRows');
	    	if(forecastList.keys.length>0)
	    		{
	    			var forecastId ;
		    		for(var i=0;i<forecastList.keys.length;i++)
	    			{
		    			forecastId = forecastList.keys[i];
		    			$.each(selectedRows,function(j,selectedRow){
		    				
		    				if(selectedRow.id == forecastId){
		    					
		    					 $('#'+forecastDatagirdId).datagrid('selectRow',j);
		    				}
		    			});
	    			}
	    		}
	    	
	    } ,
	    onSelect:function(index,row){
	    	
	    	if(!forecastList.contain(row.id))//新数据
	    		{
//	    			forecastList = new map();//单选预测信息时使用
	    			forecastList.put(row.id, row.id);
	    		}
	    	
	    },
	    onUnselect:function(index,row){
			
	    	forecastList.remove(row.id);
			
		},
		onSelectAll:function(rows){
			$.each(rows,function(i,row){
				
				if(!forecastList.contain(row.id))//放入不存在新数据
	    		{
//					forecastList = new map();//单选预测信息时使用
					forecastList.put(row.id, row.id);
	    		}
			});
		},
		onUnselectAll:function(rows){
			$.each(rows,function(i,row){
				if(forecastList.contain(row.id))//移除已存在数据
	    		{
					forecastList.remove(row.id);
	    		}
				
			});
		}
		
	});
}




/**
 * 根据当前登录用户的角色判断发布的是什么类型的应用广告
 * @returns
 */
function getAdtypeOfLoginRole()
{
	var data = new Object();
	var adType ;
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/advertisement/getAdtypeOfLoginRole.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	adType = returndata.message;
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return adType;
}


/*
 * 获取当前应用公告发布的区域数据
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
 * 打开弹框上传附件
 * @param dialogId
 * @param addorupdate
 */
/*function openDialog(dialogId,addorupdate){
	var createUUID = (function (uuidRegEx, uuidReplacer) { 
		 return function () { 
		 return"xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(uuidRegEx, uuidReplacer).toUpperCase(); 
		};
		 })(/[xy]/g, function (c) { 
		 var r = Math.random() * 16 | 0, 
		 v = c =="x"? r : (r & 3 | 8); 
		 return v.toString(16); 
		});
	
	var uploadId ;
	var uploadShowDivId;
	if('update'==addorupdate)
		{
			
			uploadId = $("#appImgUrlU").val();
			
			if(null == uploadId || '' == uploadId)//若附件id为空，则生成附件id并放入值中
				{
					uploadId = createUUID();
					$("#appImgUrlU").val(uploadId);
				}
			
			uploadShowDivId="uploadShowU";
		}
	else
		if('add'==addorupdate)
		{
			uploadShowDivId="uploadShowA";
			if(''==$("#appImgUrlA").val())
				{
					uploadId = createUUID();
					$("#appImgUrlA").val(uploadId);
				}
			else
				{
					uploadId = $("#appImgUrlA").val();
				}
			
		}
	
	var url = 'uploadFile.jsp?uploadId='+uploadId;
	$('#'+dialogId).dialog({
	    title: '上传广告图片',
	    width: 500,
	    height: 300,
	    closed: false,
	    cache: false,
	    content: '<iframe id="'+uploadId+'"src="'+url+'" width="100%" height="100%" frameborder="0" scrolling="auto" ></iframe>',  
//	    href: 'uploadFile.jsp?uploadId='+uploadId,
	    modal: true,
	    onClose:function(){
	    		initImgList(uploadId,uploadShowDivId);
	    	}
	});
	
	
	
	
}*/

/**
 * 提交保存应用公告
 */
function submitAddNotice(operatype)
{
	$('#ff').form('submit',{
		url:contextPath+'/notice/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			param.noticeStatus = operatype;
			param.appsdata = JSON.stringify(appList);
			param.stationGdata = JSON.stringify(stationGList);
			param.forecastdata = JSON.stringify(forecastList);//预测信息数据
			
			var roleArr = getLoginuserRole();
			var isCityManager = roleArr[0];//是否拥有市中心角色
			var isProvinceManager = roleArr[1];//是否拥有省中心角色
			var currentcode = roleArr[2];
			var province = roleArr[3];
			var city = roleArr[4];
			
			var areaFlag = true;
			if(!isCityManager)
				{
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
					
					if(areaList.keys.length==0)
					{
						areaFlag = false;
						$.messager.alert('提示', "请选择当前公告要发布给哪些区域!");
					}
				}
			else
				{
					areaList = new map();
					/*若当前用户的角色为市中心，则可以进行区域和通行证组二选一的填写，否则通行证组则无意义，因为应用广告的区域范围到市，而市中心可以新建通行证组
						的可选通行证范围也是本市，若默认写入当前市中心的区域值，则通行证组无法显示出其特殊性，因为通行证组的数据是市中心管辖通行证的子集；而其他角色
						包括省中心和公司则不会发生这种情况，因为省中心不可以选择通行证组，若可选择通行证组可以跨越市级区域，通行证组不一定是选择的区域下通行证的子集，
						公司角色与省中心同理*/		
					if(stationGList.keys.length==0)
						{//※若当前市中心角色用户没有选择通行证组，那么当前应用广告默认向全市发布
							areaList.put(city,city);//若为市中心发布的通告，则区域写入自己所在城市
						}
					
				}
			
			param.areadata = JSON.stringify(areaList);
			
			var appCategory = $("#appCategoryhiddenA").val();
			var forecastFlag = true;
			if('4' == appCategory)
				{//若发布的是预测类公告，则要检查是否关联了预测信息
					if(forecastList.keys.length==0)
						{
							forecastFlag = false;
							$.messager.alert('提示', "当前操作的是预测类公告，请选择预测信息!");
						}
				}
			
			
			if($('#ff').form('enableValidation').form('validate')&&appList.keys.length>0&&areaFlag&&forecastFlag)
				{
					flag = true;
				}
			else
				if(appList.keys.length==0)
				{
					flag = false;
					$.messager.alert('提示', "请选择当前公告要发布给哪些应用数据!");
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addNotice").dialog('close');//初始化添加通行证组弹框关闭
	    	$('#ff').form('clear');//清空表单内容
	    	$("#lotteryTypeA").combobox('setValue',"1");
	    	$("#appCategoryA").combobox('select',"2");
	    	initDatagrid();
	    	
	    	
	    }
	});
}

/**
 * 提交修改应用公告
 */
function submitUpdateNotice(operatype)
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/notice/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			param.noticeStatus = operatype;
			param.appsdata = JSON.stringify(appList);
			param.stationGdata = JSON.stringify(stationGList);
			param.forecastdata = JSON.stringify(forecastList);//预测信息数据
			var roleArr = getLoginuserRole();
			var isCityManager = roleArr[0];//是否拥有市中心角色
			var isProvinceManager = roleArr[1];//是否拥有省中心角色
			var currentcode = roleArr[2];
			var province = roleArr[3];
			var city = roleArr[4];
			
			var areaFlag = true;
			if(!isCityManager)
				{
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
					if(areaList.keys.length==0)
					{
						areaFlag = false;
						$.messager.alert('提示', "请选择当前公告要发布给哪些区域!");
					}
				}
			else
				{
					areaList = new map();
					/*若当前用户的角色为市中心，则可以进行区域和通行证组二选一的填写，否则通行证组则无意义，因为应用广告的区域范围到市，而市中心可以新建通行证组
					的可选通行证范围也是本市，若默认写入当前市中心的区域值，则通行证组无法显示出其特殊性，因为通行证组的数据是市中心管辖通行证的子集；而其他角色
					包括省中心和公司则不会发生这种情况，因为省中心不可以选择通行证组，若可选择通行证组可以跨越市级区域，通行证组不一定是选择的区域下通行证的子集，
					公司角色与省中心同理*/		
					if(stationGList.keys.length==0)
					{//※若当前市中心角色用户没有选择通行证组，那么当前应用广告默认向全市发布
						areaList.put(city,city);//若为市中心发布的通告，则区域写入自己所在城市
					}
				}
			
			param.areadata = JSON.stringify(areaList);
			
			var appCategory = $("#appCategoryhiddenU").val();
			var forecastFlag = true;
			if('4' == appCategory)
				{//若发布的是预测类公告，则要检查是否关联了预测信息
					if(forecastList.keys.length==0)
						{
							forecastFlag = false;
							$.messager.alert('提示', "当前操作的是预测类公告，请选择预测信息!");
						}
				}
			
			
			
			if($('#ffUpdate').form('enableValidation').form('validate') &&appList.keys.length>0 &&areaFlag && forecastFlag)
				{
					flag = true;
				}
			else
				if(appList.keys.length==0)
				{
					flag = false;
					$.messager.alert('提示', "请选择当前公告要发布给哪些应用数据!");
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateNotice").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


/**
 * 删除应用公告数据
 */
function deleteNotice(id,noticeStatus)
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
		if('1' == noticeStatus)
		{//若为发布状态的应用公告，则不可以删除
			$.messager.alert('提示',"当前应用公告已经发布,不可以进行删除操作!");
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
 * 添加应用公告
 */
function addAppNotice()
{
	$("#addNotice").dialog('open');
  	//清空数据列表
  	clearLists();
  	//初始化页面展示
  	initAddPage();
  	
  	//判断当前登录用户可以创建什么类型的应用广告，根据类型加载弹框,除了市中心用户，其他用户不可以显示通行证组的选择
  	checkNoticeUseUgroup('txzDivA','','stationDataGridA');
  	//加载应用列表
  	initAppDatagrid('','appDataGridA');
  	//加载区域树列表
  	var roleArr = getLoginuserRole();
  	var isCityManager = roleArr[0];//是否拥有市中心角色
	var isProvinceManager = roleArr[1];//是否拥有省中心角色
	var currentcode = roleArr[2];
	var province = roleArr[3];
	if(!isCityManager)
		{
			$("#areaDivA").show();// id="areaDivA"
			//展示所有的区域信息，树的形式
			initAreaData('areaDataGridA',isProvinceManager,province);
		}
	else
		{
			$("#areaDivA").hide();
		}
  	
  	
}



/**
 * 批量删除应用公告数据
 */
function deleteNoticeList(operaType)
{
	var url = contextPath + '/notice/deleteNotices.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		var noticeStatus = rows[i].noticeStatus;
		if('1' == noticeStatus)
			{
				$.messager.alert('提示',"当前id为"+rows[i].id+"的应用公告已经发布,不可以进行删除操作!");
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
 * 批量发布应用公告数据
 * @param operaType
 */
function publishNoticeList(operaType)
{
	var url = contextPath + '/notice/publishsNotices.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		/*var noticeStatus = rows[i].noticeStatus;
		if('1' == noticeStatus)
			{
				$.messager.alert('提示',"当前id为"+rows[i].id+"的应用公告已经发布,不可以进行删除操作!");
				deleteFlag = false;
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

/**
 * 获取当前登录用户的角色
 */
function getLoginuserRole()
{
	var isCityManager = false;//是否拥有市中心角色
	var isProvinceManager = false;//是否拥有省中心角色
	var currentcode = "";
	var province = "";
	var city  = "";
	var returnArr = new Array();
	
	var data1 = new Object();
	var url = contextPath + '/announcement/getLoginuserRole.action';
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	isCityManager = data.cityCenterManager;
        	isProvinceManager = data.provinceCenterManager;
        	currentcode = data.message;
        	province = data.province;
        	city = data.city;
        	
        	
        	returnArr.push(isCityManager);
        	returnArr.push(isProvinceManager);
        	returnArr.push(currentcode);
        	returnArr.push(province);
        	returnArr.push(city);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        		window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	
	return returnArr;
	
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

