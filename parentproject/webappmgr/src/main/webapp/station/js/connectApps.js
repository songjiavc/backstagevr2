
/**
 * 关联通行证和应用版本数据
 * @param stationId
 */
function connectApps(stationId,province,city)
{
	initFufeiDatagrid(stationId);
	initXufeiDatagrid(stationId);
	
	$("#w").dialog('open');
}

/**
 * 初始化可以续费的付费应用列表
 * @param stationId
 */
function initXufeiDatagrid(stationId,province,city)
{
	var params = new Object();
	
	params.stationId =stationId;
//	params.province =province;
//	params.city =city;
	
	$('#appXufei').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/order/getXufeiAppList.action',//'datagrid_data1.json',
		method:'get',
		border:false,
		singleSelect:false,
		fit:true,//datagrid自适应
		fitColumns:true,
		pagination:true,
		collapsible:false,
		pageSize:5,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[2,5,10],
		columns:[[
				{field:'id',hidden:true},
				{field:'appName',title:'应用名称',width:80,align:'center'},
				{field:'provinceName',title:'省级区域',width:70,align:'center'},
				{field:'cityName',title:'市级区域',width:70,align:'center'},
				{field:'lastPurchaseTime',title:'上次购买时间',width:140,align:'center'},
				{field:'surplusDays',title:'剩余使用天数',width:90,align:'center'},
				{field:'userYears',title:'使用年限',width:70,align:'center',
					editor: {  
						type : 'combobox',  
	                    options: {  
	//                    	data: contextPath + '/order/getUserYearDiscounts.action',// $(stationcombobox.target).combobox( 'loadData' , sdata); 
	                    	valueField: "id", textField: "userYearName" ,
	                    	panelHeight: 'auto',  
	                        required: false ,  
	                        editable:false  
	                    }  
                }}, 
				{field:'sellPrice',title:'销售价格(元)',width:100,align:'center',
                	editor: {  
	                    type: 'text',  
	                    options: {  
	                        required: true
                    }  
                }}, 
				{field:'opt',title:'操作',width:180,align:'center',  
		            formatter:function(value,row,index){  
		                var btn = '<a class="editcls" onclick="saveOrSubmitXufei(&quot;'+stationId+'&quot;,0,&quot;'+index+'&quot;)" href="javascript:void(0)">保存</a>'
		                	+'<a class="deleterole" onclick="saveOrSubmitXufei(&quot;'+stationId+'&quot;,1,&quot;'+index+'&quot;)" href="javascript:void(0)">保存并提交</a>';
		                return btn;  
		            }  
		        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'保存',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'保存并提交',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        
	        //处理datagrid中下拉框和可编辑框的数据
        	for(var i=0;i<data.rows.length;i++)
			{
            	 $('#appXufei').datagrid('beginEdit', i);
            	 var editors = $('#appXufei').datagrid('getEditors', i);
            	 editors[1].target.val('0');//设置销售价格为0
            	 editors[1].target.prop('readonly',true); // 设值只读
            	 
			}
            
            //绑定editor校验
            var selectedproRows = $('#appXufei').datagrid('getRows');
            $.each(selectedproRows,function(indexp,row){
            	var editors = $('#appXufei').datagrid('getEditors', indexp);//获取当前行可编辑的值

            		//给站点更改绑定级联事件
            		editors[0].target.combobox({

            			onChange : function (n,o) {
                				//n是id值，o是索引
            					var userYearId = n;
            					var indexrow = o;
            					var sellprice = calculatePriceXufei(userYearId,stationId,indexp);
	                       	 	editors[1].target.val(sellprice);//设置销售价格为0
	                       	 	
                			}

            		}); 
            	
	        	
            	});
	        
	        for(var i=0;i<data.rows.length;i++)
			{
            	//处理站点combobox
				 var uYearcombobox = $('#appXufei').datagrid('getEditors',i);
				 var sdata = getUseYearsData();
				 $(uYearcombobox[0].target).combobox( 'loadData' , sdata); 
				 if(sdata.length>0)
					 {
					 	$(uYearcombobox[0].target).combobox( 'setValue' ,sdata[0].id); //默认选中第一项
					 }
			}
	        
	        
	        
	    },
	    rowStyler:function(index,row){//设置行样式
				
			}
	});
}

/**
 * 获取使用年限数据
 * @returns
 */
function getUseYearsData()
{
	var data = new Object();
	
	var returnData ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath + '/order/getUserYearDiscounts.action',
        data:data,
        dataType: "json",
        success: function (data1) {
        	returnData = data1;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	return returnData;
}

/**
 * 初始化可以购买的付费应用datagrid数据，可以购买的付费应用
 * @param id
 */
function initFufeiDatagrid(stationId,province,city)
{
	var params = new Object();
	
	params.stationId =stationId;
//	params.province =province;
//	params.city =city;
	
	$('#appGoumai').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/order/getFufeiAppList.action',//'datagrid_data1.json',
		method:'get',
		border:false,
		singleSelect:false,
		fit:true,//datagrid自适应
		fitColumns:true,
		pagination:true,
		collapsible:false,
		pageSize:5,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[2,5,10],
		columns:[[
				{field:'id',hidden:true},
				{field:'appName',title:'应用名称',width:150,align:'center'},
				{field:'provinceName',title:'省级区域',width:80,align:'center'},
				{field:'cityName',title:'市级区域',width:80,align:'center'},
				{field:'userYears',title:'使用年限',width:100,align:'center',editor: {  
                    type: 'combobox',  
                    options: {  
                    	valueField: "id", textField: "userYearName" ,
                    	panelHeight: 'auto',  
                        required: false ,  
                        editable:false  
                    }  
                }}, 
				{field:'sellPrice',title:'销售价格(元)',width:100,align:'center',editor: {  
                    type: 'text',  
                    options: {  
                        required: true
                    }  
                }}, 
				{field:'opt',title:'操作',width:160,align:'center',  
		            formatter:function(value,row,index){  
		                var btn = '<a class="editcls" onclick="saveOrSubmitFufei(&quot;'+stationId+'&quot;,0,&quot;'+index+'&quot;)" href="javascript:void(0)">保存</a>'
		                	+'<a class="deleterole" onclick="saveOrSubmitFufei(&quot;'+stationId+'&quot;,1,&quot;'+index+'&quot;)" href="javascript:void(0)">保存并提交</a>';
		                return btn;  
		            }  
		        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'保存',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'保存并提交',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="6">没有数据</td></tr>');
			}
	        
	        //处理datagrid中下拉框和可编辑框的数据
        	for(var i=0;i<data.rows.length;i++)
			{
            	 $('#appGoumai').datagrid('beginEdit', i);
            	 var editors = $('#appGoumai').datagrid('getEditors', i);
            	 editors[1].target.val('0');//设置销售价格为0
            	 editors[1].target.prop('readonly',true); // 设值只读
//            	 $('#appGoumai').datagrid('endEdit', i);
            	 
			}
            
            //绑定editor校验
            var selectedproRows = $('#appGoumai').datagrid('getRows');
            $.each(selectedproRows,function(indexp,row){
            	var editors = $('#appGoumai').datagrid('getEditors', indexp);//获取当前行可编辑的值

            		//给站点更改绑定级联事件
            		editors[0].target.combobox({

            			onChange : function (n,o) {
                				//n是id值，o是索引
            					var userYearId = n;
            					var indexrow = o;
            					var sellprice = calculatePrice(userYearId,stationId,indexp);
	                       	 	editors[1].target.val(sellprice);//设置销售价格为0
	                       	 	
                			}

            		}); 
            	
	        	
            	});
	        
	        for(var i=0;i<data.rows.length;i++)
			{
            	//处理站点combobox
				 var uYearcombobox = $('#appGoumai').datagrid('getEditors',i);
				 var sdata = getUseYearsData();
				 $(uYearcombobox[0].target).combobox( 'loadData' , sdata); 
				 if(sdata.length>0)
					 {
					 	$(uYearcombobox[0].target).combobox( 'setValue' ,sdata[0].id); //默认选中第一项
					 }
			}
	        
	        
	    },
	    rowStyler:function(index,row){//设置行样式
				
			}
	});
}


/**
 * 
 * @param userYearId:选择的使用年限id
 * @param stationId：通行证id
 * @param indexrow：选中app行索引
 * @returns
 */
function calculatePriceXufei(userYearId,stationId,indexrow)
{
	var selectedRows = $('#appXufei').datagrid('getRows');
	var row = selectedRows[indexrow];//操作行数据
	
	var param = new Object();
	param.appId = row.appId;
	param.stationId = stationId;
	param.province = row.province;
	param.city = row.city;
	param.useYearId = userYearId;
	
	var returnPrice;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:param,
        url: contextPath+'/order/calculateSellprice.action',
        dataType: "json",
        success: function (data) {
        		
        	returnPrice = data.message;
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return returnPrice;
	
}

/**
 * 
 * @param userYearId:选择的使用年限id
 * @param stationId：通行证id
 * @param indexrow：选中app行索引
 * @returns
 */
function calculatePrice(userYearId,stationId,indexrow)
{
	var selectedRows = $('#appGoumai').datagrid('getRows');
	var row = selectedRows[indexrow];//操作行数据
	
	var param = new Object();
	param.appId = row.id;
	param.stationId = stationId;
	param.province = row.province;
	param.city = row.city;
	param.useYearId = userYearId;
	
	var returnPrice;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:param,
        url: contextPath+'/order/calculateSellprice.action',
        dataType: "json",
        success: function (data) {
        		
        	returnPrice = data.message;
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	
	return returnPrice;
	
}


/**
 * 
 * @param stationId
 * @param operatype://0:保存 1：保存并提交
 */
function saveOrSubmitFufei(stationId,operatype,rowindex)
{
	var selectedRows = $('#appGoumai').datagrid('getRows');
	var row = selectedRows[rowindex];//操作行数据
	var editors = $('#appGoumai').datagrid('getEditors', rowindex);
	
	var param = new Object();
	param.appId = row.id;//应用id
	param.station = stationId;
	param.operatype = operatype;////0:保存 1：保存并提交
	param.price = editors[1].target.val() ;
	param.userYearId = $(editors[0].target).combobox('getValue');
	
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:param,
        url: contextPath+'/order/saveOrUpdate.action',
        dataType: "json",
        success: function (data) {
        		
        	$.messager.alert('提示', data.message);
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
}

/**
 * 续费操作的保存或保存提交
 * @param stationId
 * @param operatype
 * @param rowindex
 */
function saveOrSubmitXufei(stationId,operatype,rowindex)
{
	var selectedRows = $('#appXufei').datagrid('getRows');
	var row = selectedRows[rowindex];//操作行数据
	var editors = $('#appXufei').datagrid('getEditors', rowindex);
	
	var param = new Object();
	param.appId = row.appId;//应用id
	param.station = stationId;
	param.operatype = operatype;////0:保存 1：保存并提交
	param.price = editors[1].target.val() ;
	param.userYearId = $(editors[0].target).combobox('getValue');
	
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:param,
        url: contextPath+'/order/saveOrUpdate.action',
        dataType: "json",
        success: function (data) {
        		
        	$.messager.alert('提示', data.message);
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
}


