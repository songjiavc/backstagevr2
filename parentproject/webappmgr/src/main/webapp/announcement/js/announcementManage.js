var idArr = [];//选中的应用id
var stationGList = new map();//选中的通行证组id数组集合
var areaList = new map();

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
	$("#announcementNameC").val("");
	$("#startTimeC").datebox('setValue','');
	$("#endTimeC").datebox('setValue','');
	$("#lotterytypeC").combobox('setValue','');
	$("#announceStatusC").combobox('setValue','');
	
//	initQueryProvince();//重新初始化模糊查询省份
}




//关闭弹框
function closeDialog()
{
	$("#addAnnouncement").dialog('close');
	$("#updateAnnouncement").dialog('close');
	$('#checkAReceipt').dialog('close');
	$('#detailAnnouncement').dialog('close');
}


/**
 * 初始化应用列表数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.announcementName = $("#announcementNameC").val().trim();
	params.startTime = $("#startTimeC").datebox('getValue');
	params.endTime = $("#endTimeC").datebox('getValue');
	params.announceStatus = $("#announceStatusC").combobox('getValue');
	params.lotteryType = $("#lotterytypeC").combobox('getValue');
	/*params.province = $("#privinceC").combobox('getValue');//获取省份
	params.city = $("#cityC").combobox('getValue');//获取市
*/	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/announcement/getAnnouncementList.action',//'datagrid_data1.json',
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
				{field:'announceStatus',hidden:true},
				{field:'announcementName',title:'通告名称',width:'15%',align:'center'},
				{field:'announceStatusName',title:'通告状态',width:'8%',align:'center'},
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
			                var btn = '<a class="editcls" onclick="updateAnnouncement(&quot;'+row.id+'&quot;,&quot;'+row.announceStatus+'&quot;)" href="javascript:void(0)">编辑</a>'
			                		+'<a class="deleterole" onclick="deleteAnnouncement(&quot;'+row.id+'&quot;,&quot;'+row.announceStatus+'&quot;)" href="javascript:void(0)">删除</a>'
			                	+'<a class="checkReceipt" onclick="checkReceipts(&quot;'+row.id+'&quot;)" href="javascript:void(0)">回执查看</a>'
			                	+'<a class="detailrole" onclick="detailAnnouncement(&quot;'+row.id+'&quot;,&quot;'+row.announceStatus+'&quot;)" href="javascript:void(0)">查看详情</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        $('.checkReceipt').linkbutton({text:'回执查看',plain:true,iconCls:'icon-remove'});  
	        $('.detailrole').linkbutton({text:'查看详情',plain:true,iconCls:'icon-filter'}); 
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}

/**
 * 查看当前通告的回执情况
 * @param id
 */
function checkReceipts(id)
{
	$('#checkAReceipt').dialog('open');
	
	var params = new Object();
	
	params.id=id;
	
	//渲染列表
	$('#announcementReceiptsDatagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url: contextPath + '/announcement/getReceiptsOfAnnouncement.action',
		method:'get',
		border:false,
		singleSelect:false,
		fitColumns:true,
		pagination:true,
		collapsible:false,
		pageSize:10,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[5,10,15],
		columns:[[
//				{field:'id',checkbox:true},
				{field:'stationCode',title:'登录帐号',width:'9%',align:'center'},
				{field:'stationNumber',title:'站点号',width:'9%',align:'center'},
				{field:'statusName',title:'回执状态',width:'11%',align:'center'},
				{field:'statusTime',title:'回执时间',width:'17%',align:'center'},
				{field:'provinceName',title:'省',width:'10%',align:'center'},
				{field:'cityName',title:'市',width:'10%',align:'center'},
				{field:'stationStyle',title:'站点类型',width:'10%',align:'center',  
		            formatter:function(value,row,index){  
		            	var lotteryTypeName ='';
		            	switch(value)
		            	{
		            		case '1':lotteryTypeName='体彩';break;
		            		case '2':lotteryTypeName='福彩';break;
		            	}
		            	return lotteryTypeName;  
		            }},
				{field:'name',title:'站主名称',width:'11%',align:'center'},
				{field:'telephone',title:'站主电话',width:'15%',align:'center'}
				
		    ]],
	    onLoadSuccess:function(data){ 
	    	
	    	if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="9">没有数据</td></tr>');
			}
	    	
	    	
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
	
	if(isProvinceManager)
		{
			data.isProvince = isProvinceManager;
			data.provinceCode = province;
		}
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/appAd/getTreedataOfAdvertisement.action',
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
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	$.fn.zTree.init($("#"+areaDataGridId), setting, zNodes);
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
	var lotteryType = '';
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
        	lotteryType = data.lotteryType;
        	
        	
        	returnArr.push(isCityManager);
        	returnArr.push(isProvinceManager);
        	returnArr.push(currentcode);
        	returnArr.push(province);
        	returnArr.push(city);
        	returnArr.push(lotteryType);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        		window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	
	return returnArr;
	
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
            window.parent.location.href = contextPath + "/menu/error.action";
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
	stationGList = new map();
	if('stationDataGridU' == stationDataGridId || 'stationDataGridD' == stationDataGridId)
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
 * 获取当前通告发布的通行证组数据
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
        url: contextPath+'/announcement/getStationOfUsergroup.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	stationList = returndata;
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return stationList;
}


function clearLists()
{
	stationGList = new map();
	areaList = new map();
}

/**
 * 查看通告详情
 * @param id
 * @param status
 */
function detailAnnouncement(id,status)
{
	$("#detailAnnouncement").dialog('open');
	var url = contextPath + '/announcement/getDetailAnnouncement.action';
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
						announcementName:data.announcementName,
						startTime:data.startTimestr,
						endTime:data.endTimestr,//通行证组描述
						announcementContent:data.announcementContent,
						lotteryType:data.lotteryType//彩种
					});
					//初始化通行证列表数据
					initStationGList(id, 'stationDataGridD');
					
					//初始化区域树
					var roleArr = getLoginuserRole();
				  	var isCityManager = roleArr[0];//是否拥有市中心角色
					var isProvinceManager = roleArr[1];//是否拥有省中心角色
					var currentcode = roleArr[2];
					var province = roleArr[3];
					var lotteryType = roleArr[5];
					
					if(!isCityManager)
					{
						$("#areaDivD").show();// id="areaDivA"
						//展示所有的区域信息，树的形式
					  	initAreaData('areaDataGridD',isProvinceManager,province);
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
						
							$("#lDI").show();
							$("#lDS").hide();
							if('1' == data.lotteryType)
								{
									$("#ldiLName").val("体彩");
								}
							else if('2' == data.lotteryType)
								{
									$("#ldiLName").val("福彩");
								}
						
						
					}
					else
					{
						$("#areaDivD").hide();
						/*var cityIds = checkAreas(id);
						$.each(cityIds,function(j,cityId){
							areaList.put(cityId, cityId);
		    			});*/
						
						$("#lDI").show();
						$("#lDS").hide();
						if('1' == data.lotteryType)
							{
								$("#ldiLName").val("体彩");
							}
						else if('2' == data.lotteryType)
							{
								$("#ldiLName").val("福彩");
							}
							
					}
					
					
				
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/menu/error.action";
	        }
		});
	
		
		
}




/**
 *通告修改
 */
function updateAnnouncement(id,status)
{
	var updateFlag = true;
	if('1' == status)
		{
			$.messager.alert('提示',"当前通告已经发布,不可以进行修改操作!");
			updateFlag = false;
		}
	if(updateFlag)
		{
			$("#updateAnnouncement").dialog('open');
			var url = contextPath + '/announcement/getDetailAnnouncement.action';
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
								announcementName:data.announcementName,
								startTime:data.startTimestr,
								endTime:data.endTimestr,//通行证组描述
								announcementContent:data.announcementContent,
								lotteryType:data.lotteryType//彩种
							});
							//初始化通行证列表数据
							initStationGList(id, 'stationDataGridU');
							
							//初始化区域树
							var roleArr = getLoginuserRole();
						  	var isCityManager = roleArr[0];//是否拥有市中心角色
							var isProvinceManager = roleArr[1];//是否拥有省中心角色
							var currentcode = roleArr[2];
							var province = roleArr[3];
							var lotteryType = roleArr[5];
							
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
								
								if('0'==lotteryType || ''==lotteryType)
									{
										$("#lUI").hide();
										$("#lUS").show();
									}
								else
									{
										$("#lUI").show();
										$("#lUS").hide();
										if('1' == data.lotteryType)
											{
												$("#luiLName").val("体彩");
											}
										else if('2' == data.lotteryType)
											{
												$("#luiLName").val("福彩");
											}
									}
								
								
							}
							else
							{
								$("#areaDivU").hide();
								/*var cityIds = checkAreas(id);
								$.each(cityIds,function(j,cityId){
									areaList.put(cityId, cityId);
				    			});*/
								
								$("#lUI").show();
								$("#lUS").hide();
								if('1' == data.lotteryType)
									{
										$("#luiLName").val("体彩");
									}
								else if('2' == data.lotteryType)
									{
										$("#luiLName").val("福彩");
									}
									
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
			            window.parent.location.href = contextPath + "/menu/error.action";
			        }
				});
		}
	
		
		
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
        url: contextPath+'/announcement/getAreasOfAnnouncement.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	area = returndata;
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return area;
}

//添加通告
function addAnnouncement()
{
	//清空数据列表
  	clearLists();
  	//加载通行证组数据
  	initStationGList('','stationDataGridA');
  	
  	var roleArr = getLoginuserRole();
  	var isCityManager = roleArr[0];//是否拥有市中心角色
	var isProvinceManager = roleArr[1];//是否拥有省中心角色
	var currentcode = roleArr[2];
	var province = roleArr[3];
	var lotteryType = roleArr[5];
  	
	
	if(!isCityManager)
		{
			$("#areaDivA").show();// id="areaDivA"
			//展示所有的区域信息，树的形式
		  	initAreaData('areaDataGridA',isProvinceManager,province);
		  	
		  	if('0'==lotteryType || ''==lotteryType)
		  		{
			  		$("#lAI").hide();
					$("#lA").show();
		  		}
		  	else
		  		{
			  		//隐藏彩种选择
					$("#lAI").show();
					if('1' == lotteryType)
					{
						$("#laiLName").val("体彩");
					}
					else if('2' == lotteryType)
					{
						$("#laiLName").val("福彩");
					}
					$("#lotteryTypeA").combobox('setValue',lotteryType);
					$("#lA").hide();
		  		}
		  	
		}
	else
		{
			$("#areaDivA").hide();
			
			//隐藏彩种选择
			$("#lAI").show();
			if('1' == lotteryType)
			{
				$("#laiLName").val("体彩");
			}
			else if('2' == lotteryType)
			{
				$("#laiLName").val("福彩");
			}
			$("#lotteryTypeA").combobox('setValue',lotteryType);
			$("#lA").hide();
		}
  
  	
  	
  	
  	
  	$("#addAnnouncement").dialog('open');
}


/**
 * 提交保存通告
 */
function submitAddAnnouncement(operatype)
{
	$('#ff').form('submit',{
		url:contextPath+'/announcement/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			param.announceStatus = operatype;
			param.stationGdata = JSON.stringify(stationGList);
			
//			var codearr = new Array();
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
						$.messager.alert('提示', "请选择当前通告要发布给哪些区域!");
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
			
			
			if($('#ff').form('enableValidation').form('validate') && areaFlag)
				{
					flag = true;
				}
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addAnnouncement").dialog('close');//初始化添加通行证组弹框关闭
	    	$('#ff').form('clear');//清空表单内容
	    	$("#lotteryTypeA").combobox('setValue',"1");
	    	
	    	initDatagrid();
	    	
	    	
	    }
	});
}

/**
 * 提交修改通告
 */
function submitUpdateAnnouncement(operatype)
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/announcement/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			param.announceStatus = operatype;
			param.stationGdata = JSON.stringify(stationGList);
//			var codearr = new Array();
			
			
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
						$.messager.alert('提示', "请选择当前通告要发布给哪些区域!");
					}
				}
			else//在初始化修改弹框时已经向arealist放入区域值
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
			
			
			
			if($('#ffUpdate').form('enableValidation').form('validate') && areaFlag)
				{
					flag = true;
				}
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateAnnouncement").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


/**
 * 删除通告数据
 */
function deleteAnnouncement(id,status)
{
	var url = contextPath + '/announcement/deleteAnnouncement.action';
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
	/*else
		if('1' == status)
		{//若为发布状态的应用公告，则不可以删除
			$.messager.alert('提示',"当前通告已经发布,不可以进行删除操作!");
			deleteFlag = false;
		}*/
	
	
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
 * 批量删除通告
 */
function deleteAnnouncementList(operaType)
{
	var url = contextPath + '/announcement/deleteAnnouncement.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		/*var announceStatus = rows[i].announceStatus;
		if('1' == announceStatus)
			{
				$.messager.alert('提示',"当前id为"+rows[i].id+"的通告已经发布,不可以进行删除操作!");
				deleteFlag = false;
				break;
			}*/
		
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
 * 批量发布通告数据(暂时没添加功能)
 * @param operaType
 */
function publishAnnouncementList(operaType)
{
	var url = contextPath + '/announcement/publishAnnouncement.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		/*var announceStatus = rows[i].announceStatus;
		if('1' == announceStatus)
			{
				$.messager.alert('提示',"当前id为"+rows[i].id+"的通告已经发布,不可以进行删除操作!");
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

