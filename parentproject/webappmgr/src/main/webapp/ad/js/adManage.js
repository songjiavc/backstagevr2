var idArr = [];//选中的应用id
var stationGList = new map();//选中的通行证组id数组集合
var appList = new map();//选中的应用数组集合
var areaList = new map();

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			/*initQueryProvince();//初始化模糊查询的省份
*/			
			clearLists();
			idArr = [];
			bindComboboxChange();//为通行证的模糊查询的省份条件绑定下拉框级联事件
			
			initDatagrid();//初始化数据列表
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
	
	$("#ttA").tabs({
		   onSelect:function(title,index){
		        $("#imgOrWordA").val(index);
		   }
		});
	
	$("#ttU").tabs({
		   onSelect:function(title,index){
		        $("#imgOrWordU").val(index);
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
	$("#adNameC").val("");
	$("#startTimeC").datebox('setValue','');
	$("#endTimeC").datebox('setValue','');
	$("#imgOrwordC").combobox('setValue','');
	$("#adStatusA").combobox('setValue','');
	
//	initQueryProvince();//重新初始化模糊查询省份
}




//关闭弹框
function closeDialog()
{
	$("#addAd").dialog('close');//初始化添加角色弹框关闭
	$("#updateAd").dialog('close');
	//关闭图片预览弹框
	$("#uploadShowAimgPreview").dialog('close');
	$("#uploadShowUimgPreview").dialog('close');
	$("#uploadShowDimgPreview").dialog('close');
	$('#detailAd').dialog('close');
}

/**
 * 校验当前用户是否可以选择通行证组的广告发布范围
 * @param ugroupId
 */
function checkUseUgroup(ugroupId,adId,stationDataGridId)
{
	var data = new Object();
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/appAd/checkUseUgroup.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	var flag = returndata.resultBean.exist;//当前用户是否可以选择通行证组的范围
        	
        	if(flag)
        		{
        			$("#"+ugroupId).show();
        			//若为市中心用户，则可以选择当前应用广告给哪些通行证组使用
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
	params.adName = $("#adNameC").val().trim();
	params.startTime = $("#startTimeC").datebox('getValue');
	params.endTime = $("#endTimeC").datebox('getValue');
	params.imgOrword = $("#imgOrwordC").combobox('getValue');
	params.adStatus = $("#adStatusC").combobox('getValue');
	/*params.province = $("#privinceC").combobox('getValue');//获取省份
	params.city = $("#cityC").combobox('getValue');//获取市
*/	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/appAd/getAdvertisementList.action',//'datagrid_data1.json',
		method:'get',
		border:false,
		fit:true,//datagrid自适应
		fitColumns:true,
		pagination:true,
		collapsible:false,
		toolbar:toolbar,
		columns:[[
				{field:'ck',checkbox:true},
				{field:'id',hidden:true},
				{field:'adStatus',hidden:true},
				{field:'appAdName',title:'应用广告名称',width:'20%',align:'center'},
				{field:'adStatusName',title:'应用广告状态',width:'10%',align:'center'},
		        {field:'startTimestr',width:'15%',title:'有效开始时间',align:'center'},
				{field:'endTimestr',title:'有效结束时间',width:'15%',align:'center'},
				{field:'createTime',title:'创建时间',width:'20%',align:'center'},
				{field:'opt',title:'操作',width:'15%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateAd(&quot;'+row.id+'&quot;,&quot;'+row.adStatus+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteAd(&quot;'+row.id+'&quot;,&quot;'+row.adStatus+'&quot;)" href="javascript:void(0)">删除</a>'
			                	+'<a class="detailrole" onclick="detailAd(&quot;'+row.id+'&quot;,&quot;'+row.adStatus+'&quot;)" href="javascript:void(0)">查看详情</a>';
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
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="7">没有数据</td></tr>');
			}
	        
	        
	    }
	});
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
        url: contextPath+'/appAd/getLoginArea.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	returnarea = returndata.resultdata;
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
function initAppDatagrid(adId,appDatagridId)
{
	var params = new Object();
	appList = new map();
	params.appType = '1';//只对已经上架的应用发布广告
	
	var areamsg = getLoginArea();
	var adtype = areamsg.adType;
	if(''!=adtype&&'3'!=adtype)//在登录用户不是公司用户时赋予区域信息
		{
			params.province=areamsg.province;
			/*目前定义的规则为：省中心和市中心都可以看见当前省级区域内的应用列表
			（※：因为市可以有定制应用也可以使用通用的省级应用，所以应该省级范围内的应用都可以显示在列表内）*/
			if('1'==adtype)
				{//若当前登录用户是市中心用户时，则也赋予市级区域信息
					params.city=areamsg.city;
				}
			
			
		}
	
	if('0'!= areamsg.lotteryType)
		{
			//如果是‘省中心’或‘市中心’用户则关联lotteryType数据或者是特定彩种类型的公司用户也按照用户彩种加载app数据
			params.lotteryType = areamsg.lotteryType;
		}
	
	
	if('appDataGridU' == appDatagridId || 'appDataGridD' == appDatagridId)
		{
			var apps = getAppsOfAd(adId);
			var appId;
			for (var i = 0; i < apps.length; i++) {
				
				appId = apps[i].id;
				appList.put(appId, appId);
			}
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
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	$.fn.zTree.init($("#"+areaDataGridId), setting, zNodes);
}


/**
 * 获取当前应用广告的关联
 * @param adId
 */
function getAppsOfAd(adId)
{
	var returnlist ;
	var data = new Object();
	data.id = adId;
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/appAd/getAppsOfAdvertisement.action',
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



function dosearch(addOrUpdate)
{
	if('1' == addOrUpdate)
		{
			initStationList($("#idU").val(),'stationDataGridU');
		}
	else
		if('0' == addOrUpdate)
			{
				initStationList('','stationDataGridA');
			}

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
		pageList:[2,3,5,10],
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
        url: contextPath+'/appAd/getStationOfUsergroup.action',
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

function clearLists()
{
	stationGList = new map();
	appList = new map();
	areaList = new map();
}

//添加应用广告
function addAppad()
{
	$("#addAd").dialog('open');//注意：要先将dialog打开，否则在设置tabs默认选中时是无法找到tabs的，导致无法正确选择
	//清空数据列表
  	clearLists();
  	//默认的广告形式为“图片广告”‘’
  	$("#ttA").tabs('select',0);//title 或  index(默认从0开始) 
  	$("#imgOrWordA").val('0');
  	$("#uploadShowA ul").html("");//清空上一次的图片显示列表
  	
  	//判断当前登录用户可以创建什么类型的应用广告，根据类型加载弹框,除了市中心用户，其他用户不可以显示通行证组的选择
  	checkUseUgroup('txzDivA','','stationDataGridA');
  	//加载应用列表
  	initAppDatagrid('','appDataGridA');
  	
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
 * 查看应用广告详情
 * @param id
 * @param adStatus
 */
function detailAd(id,adStatus)
{
	var updateFlag = true;
	$("#detailAd").dialog('open');
	var url = contextPath + '/appAd/getDetailAdvertisement.action';
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
						appAdName:data.appAdName,
						startTime:data.startTimestr,
						endTime:data.endTimestr,//通行证组描述
//							appImgUrl:data.appImgUrl,//读取的是图片附件id
//							addWord:data.addWord,
						imgOrWord:data.imgOrWord//当前的广告形式
					});
					if('1' == data.imgOrWord)
						{//文字形式的广告填充文字内容
							$("#addWordD").val(data.addWord);
							$("#ttD").tabs('select',1);//title 或  index(默认从0开始) 
							$("#uploadShowD ul").html("");//清空上一次的图片显示列表
						}
					else
						{
							$("#appImgUrlD").val(data.appImgUrl);
							$("#ttD").tabs('select',0);//title 或  index(默认从0开始) 
							/*初始化附件列表*/
							initImgList(data.appImgUrl,'uploadShowD');
							$("#addWordD").val('');//清空上一次的文字广告
						}
					//初始化通行证列表数据
					checkUseUgroup('txzDivD', id, 'stationDataGridD');//判断当前用户是否为市中心用户，若为市中心用户则加载通行证组数据
					initAppDatagrid(id,'appDataGridD');
					//初始化区域树
					//初始化区域树
					var roleArr = getLoginuserRole();
				  	var isCityManager = roleArr[0];//是否拥有市中心角色
					var isProvinceManager = roleArr[1];//是否拥有省中心角色
					var currentcode = roleArr[2];
					var province = roleArr[3];
					
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
						
					}
					else
					{
						$("#areaDivD").hide();
					}
					
					
						
					
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
	
	
		
		
}

/**
 *应用广告修改
 */
function updateAd(id,adStatus)
{
	var updateFlag = true;
	if('1' == adStatus)
		{
			updateFlag =false;
			$.messager.alert('提示',"当前应用广告已经发布,不可以进行修改操作!");
		}
	if(updateFlag)
		{
			$("#updateAd").dialog('open');
			var url = contextPath + '/appAd/getDetailAdvertisement.action';
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
								appAdName:data.appAdName,
								startTime:data.startTimestr,
								endTime:data.endTimestr,//通行证组描述
	//							appImgUrl:data.appImgUrl,//读取的是图片附件id
	//							addWord:data.addWord,
								imgOrWord:data.imgOrWord//当前的广告形式
							});
							if('1' == data.imgOrWord)
								{//文字形式的广告填充文字内容
									$("#addWordU").val(data.addWord);
									$("#ttU").tabs('select',1);//title 或  index(默认从0开始) 
									$("#uploadShowU ul").html("");//清空上一次的图片显示列表
								}
							else
								{
									$("#appImgUrlU").val(data.appImgUrl);
									$("#ttU").tabs('select',0);//title 或  index(默认从0开始) 
									/*初始化附件列表*/
									initImgList(data.appImgUrl,'uploadShowU');
									$("#addWordU").val('');//清空上一次的文字广告
								}
							//初始化通行证列表数据
							checkUseUgroup('txzDivU', id, 'stationDataGridU');//判断当前用户是否为市中心用户，若为市中心用户则加载通行证组数据
							initAppDatagrid(id,'appDataGridU');
							//初始化区域树
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
 * 初始化图片附件列表
 * @param upId：附件id
 * @param listId：附件列表容器id
 */
function initImgList(upId,listId)
{
	var data = new Object();
	data.uplId = upId;
	
	var imgId = listId+"imgPreview";
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/appAd/getFileOfAppad.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	if(null!=returndata.id&&0!=returndata.id)
        		{
	        		$("#"+listId+" ul").html("");
				  	var upload=returndata;
				  	var fileName = upload.uploadFileName;
					var realName = upload.uploadRealName;
	        	
					$("#"+listId+" ul").append('<li><a title="点击预览图片"  id="'+upload.id+'" class="fujian" onclick="previewImage(&quot;'+imgId+'&quot;,&quot;'+realName+'&quot;)">'+fileName+'</a></li>');
        		}
        	
        	
      			
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
}

/**
 * 图片预览
 * @param imgId：图片div的id
 * @param realName：图片真实路径
 */
function previewImage(imgId,realName)
{
	$("#"+imgId).dialog('open');//打开鼠标预览弹框
	var path = contextPath+"/upload/"+realName;
	$("#"+imgId).html("<img style='width:500px;height:400px;' src='"+path+"'/>");
}

/**
 * 校验当前附件id下是否有附件数据
 * @param upId
 * @returns {Boolean}
 */
function upIdHaveFujian(upId)
{
	var data = new Object();
	data.uplId = upId;
	var returnFlag = false;
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/appAd/getFileOfAppad.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	if(0 != returndata.id)//若没有附件，则生成一个附件实体，且id=0
        		{
        			returnFlag = true;
        		}
      			
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return returnFlag;
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
        url: contextPath+'/appAd/getAdtypeOfLoginRole.action',
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
 * 获取当前应用广告发布的区域数据
 */
function checkAreas(id)
{
	var data = new Object();
	data.id = id;
	
	var area ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/appAd/getAreasOfAdvertisement.action',
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
function openDialog(dialogId,addorupdate){
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
	
	
	
	
}

/**
 * 提交保存通行证组
 */
function submitAddAd(operatype)
{
	$('#ff').form('submit',{
		url:contextPath+'/appAd/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			param.adStatus = operatype;
			param.appsdata = JSON.stringify(appList);
			param.stationGdata = JSON.stringify(stationGList);
			param.addType=getAdtypeOfLoginRole();//获取应用广告类型
			
			
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
						$.messager.alert('提示', "请选择当前广告要发布给哪些区域!");
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
			
			var imgOrWord = $("#imgOrWordA").val();
			var imgOrwordFlag = true;
			if('0'==imgOrWord)
				{
					var upId = $("#appImgUrlA").val();
					var haveFujian = upIdHaveFujian(upId);//返回值为false时则没有附件
					if(null == upId||''==upId ||!haveFujian)
					{
						imgOrwordFlag = false;
						$.messager.alert('提示', "您要发布的是图片广告,请选择要上传的图片!");
					}
				}
			else if('1'==imgOrWord)
				{
					//文字广告
					if(null == $("#addWordA").val()||''==$("#addWordA").val().trim())
						{
							imgOrwordFlag = false;
							$.messager.alert('提示', "您要发布的是文字广告,请输入广告内容!");
						}
				}
			
			if($('#ff').form('enableValidation').form('validate')&&appList.keys.length>0 &&imgOrwordFlag && areaFlag)
				{
					flag = true;
				}
			else
				if(appList.keys.length==0)
				{
					flag = false;
					$.messager.alert('提示', "请选择当前广告要发布给哪些应用数据!");
				}
					
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addAd").dialog('close');//初始化添加通行证组弹框关闭
	    	$('#ff').form('clear');//清空表单内容
	    	initDatagrid();
	    	
	    	
	    }
	});
}

/**
 * 提交修改应用广告
 */
function submitUpdateAd(operatype)
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/appAd/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			param.adStatus = operatype;
			param.appsdata = JSON.stringify(appList);
			param.stationGdata = JSON.stringify(stationGList);
			
			
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
						$.messager.alert('提示', "请选择当前广告要发布给哪些区域!");
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
			
			
			var imgOrWord = $("#imgOrWordU").val();
			var imgOrwordFlag = true;
			if('0'==imgOrWord)
				{
					var upId = $("#appImgUrlU").val();
					var haveFujian = upIdHaveFujian(upId);//返回值为false时则没有附件
					if(null == upId||''==upId ||!haveFujian)
					{
						imgOrwordFlag = false;
						$.messager.alert('提示', "您要发布的是图片广告,请选择要上传的图片!");
					}	
				}
			else if('1'==imgOrWord)
				{
					//文字广告
					if(null == $("#addWordU").val()||''==$("#addWordU").val().trim())
						{
							imgOrwordFlag = false;
							$.messager.alert('提示', "您要发布的是文字广告,请输入广告内容!");
						}
				}
			
			if($('#ffUpdate').form('enableValidation').form('validate') &&appList.keys.length>0  && imgOrwordFlag && areaFlag)
				{
					flag = true;
				}
			else
				if(appList.keys.length==0)
				{
					flag = false;
					$.messager.alert('提示', "请选择当前广告要发布给哪些应用数据!");
				}
					
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateAd").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


/**
 * 删除应用广告数据
 */
function deleteAd(id,adStatus)
{
	var url = contextPath + '/appAd/deleteAdvertisements.action';
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
		if('1' == adStatus)
		{
			deleteFlag =false;
			$.messager.alert('提示',"当前应用广告已经发布,不可以进行修改操作!");
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
		                    window.parent.location.href = contextPath + "/error.jsp";
		                }
		           });
		        	
	        }  
	    });  
	}
		
	
}



/**
 * 批量删除应用广告数据
 */
function deleteAdList(operaType)
{
	var url = contextPath + '/appAd/deleteAdvertisements.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		/*var adStatus= rows[i].adStatus;
			if('1' == adStatus)
			{
				deleteFlag =false;
				$.messager.alert('提示',"当前id为"+rows[i].id+"的应用广告已经发布,不可以进行修改操作!");
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
 * 批量发布应用广告数据
 * @param operaType
 */
function publishAdList(operaType)
{
	var url = contextPath + '/appAd/publishAdvertisements.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		/*var adStatus= rows[i].adStatus;
			if('1' == adStatus)
			{
				deleteFlag =false;
				break;
				$.messager.alert('提示',"当前id为"+rows[i].id+"的应用广告已经发布,不可以进行修改操作!");
			}*/
		
	}
	
	if(deleteFlag)
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台data
				
				var alertMsg = "您确认发布选中的数据？";//默认的提示信息为删除数据的提示信息
				
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
$.extend($.fn.validatebox.defaults.rules, {
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

/**
 * 自定义校验通行证组编码唯一性
 */
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




/**
 * 校验上传文件类型
 * @param obj
 * @returns {Boolean}
 */
function checkFile(obj) {  
    var array = new Array('jpg', 'png','JPG','PNG','gif');  //可以上传的文件类型   
    if (obj.value == '') {  
    	$.messager.alert('提示',"请选择要上传的图片!");  
        return false;  
    }  
    else {  
       var fileContentType = obj.value.match(/^(.*)(\.)(.{1,8})$/)[3]; //这个文件类型正则很有用：）   
        var isExists = false;  
        for (var i in array) {  
          if (fileContentType.toLowerCase() == array[i].toLowerCase()) {  
                isExists = true;  
               return true;  
           }  
        }  
        if (isExists == false) {  
        	$.messager.alert('提示',"上传文件类型不正确!");  
            obj.value ='';
            return false;  
        }  
       return false;  
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

