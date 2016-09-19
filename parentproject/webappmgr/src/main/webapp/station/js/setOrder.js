var goodsList = new Array();//选中的商品数据
var countPrice = 0;//选中商品总价
var currentRowId = '';//当前选中的商品id
var stationtype='';
var proOfgoods  = new map();
/**
	 * 商品选购入口，留给班娜补充
	 */
function setOrder(stationId,stationNumber,stationStyle)
{
//	var stationId = row.id;//站点id
//	var stationNumber = row.stationNumber; //站点号
	
	stationtype = stationStyle;
	
	$("#stationAhidden").val(stationId);
	$("#stationA").textbox('setText',stationNumber);
	
	dialogOPenDo();
	$("#pricehidden").val('');
	$("#priceA").textbox('setText','');
	var returnArr = new Array();
	returnArr = getDetailStation(stationId);//rec.id is stationId
	//根据站点的区域和彩种加载商品信息列表
	initGoodsDatagrid(returnArr[0], returnArr[1], 'goodsDatagridU', returnArr[2]);
	
	$('.panel-title.panel-with-icon').html('购买商品');
	$("#setOrder").dialog("open");
}




function dialogOPenDo()
{
	clearGoodsArray();
	
	generateCode();
	
}

/**
 * 清空全局变量
 */
function clearGoodsArray()
{
	goodsList = new Array();
	proOfgoods  = new map();
	countPrice = 0;//清零商品总价
	$('#goodsDatagridU').datagrid('loadData', { total: 0, rows: [] });//清空商品datagrid内容，避免异常，在再次打开弹框时，可以重新加载内容
}


/**
 * 初始化选择商品列表（加载条件：1.区域（根据选中的站点的区域值）2.彩种（根据选中的站点的彩种--》加载当前彩种和双机商品））
 * @param provinceId:选中站点的省id
 * @param cityId:选中站点的市id
 */
function initGoodsDatagrid(provinceId,cityId,productDatagrid,stationType)
{
	var params = new Object();
	params.province = provinceId;
	params.city = cityId;
	params.status = '1';//上架商品
	params.stationType = stationType;//站点类型：1：体彩 2：福彩
	
	$('#'+productDatagrid).datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/goods/getGoodsList.action',//'datagrid_data1.json',
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
				{field:'code',title:'商品编码',width:120,align:'center'},
		        {field:'name',width:120,title:'商品名称',align:'center'},
		        {field:'goodType',width:50,title:'彩种',align:'center', 
		            formatter:function(value,row,index){  
		            	var goodTypeName ='';
		            	switch(value)
		            	{
		            		case '1':goodTypeName='体彩';break;
		            		case '2':goodTypeName='福彩';break;
		            		case '0':goodTypeName='双机';break;
		            	}
		            	return goodTypeName;  
		            }  },
				{field:'price',title:'价格(元)',width:80,align:'center'},
				{field:'provinceName',title:'省级区域',width:80,align:'center'},
				{field:'cityName',title:'市级区域',width:80,align:'center'},
				{field:'createTime',title:'创建时间',width:150,align:'center'},
				{field:'goodsDesprition',title:'商品描述',width:120,align:'center'}
		    ]],  
	    onLoadSuccess:function(data){  
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        
	        //选中（写入后台读取的产品数据进行选中）
	        var selectedRows = $('#'+productDatagrid).datagrid('getRows');
	        
	        $.each(selectedRows,function(j,selectRow){
	        	$('#'+productDatagrid).datagrid('expandRow',j);//将子网格展开
	        	
        	});
	       
	        //写入和选中当前数据选中的产品（写入productlist选中的）
	        if(goodsList.length>0)
	        	{
	        		for(var i=0;i<goodsList.length;i++)
	        			{
	        				$.each(selectedRows,function(j,selectedRow){
	        					var	goodId = goodsList[i];
								if(selectedRow.id == goodId){
									
									 $('#'+productDatagrid).datagrid('selectRow',j);
								}
				        	});
	        			}
	        	}
	        
	       
	        
	        
	    },
	    onSelect:function(index,row){
	    	var pushFlag = goodListExist(row.id);
	    	
			var goodType = row.goodType;//获取选中行的彩种，若为"双机"，则要选另一个站点
			if('0' == goodType)
				{
					var selectedFlag = true;//是否可选中
					 var selectedRows = $('#ddv-'+row.id).datagrid('getRows');
		                $.each(selectedRows,function(editIndex,selectedRow){
		                	if(selectedFlag)
		                		{
			                		var stationcombobox = $('#ddv-'+row.id).datagrid('getEditor', {index:editIndex,field:'stationOfPro'});
				                	var productname = $(stationcombobox.target).combobox('getText');
				                	var value = $(stationcombobox.target).combobox('getValue');
				                	if(value==""&&productname==""){
				                		selectedFlag = false;
				                	}
		                		}
		                	
		                });
		                
		               if(!selectedFlag)
		            	   {
			            	   $.messager.alert('提示', "当前选中的商品为双机商品且当前选中站点的站主没有其他可选彩种站点!");
								pushFlag = false;
								$('#'+productDatagrid).datagrid('unselectRow', index);
								$('#'+productDatagrid).datagrid('onUncheck', index);
		            	   }
		               else
		            	   {
		            	   		$.messager.alert('提示', "当前选中的商品为双机商品，请配置其他彩种站点!");
		            	   }
		               	
		                
					
				}
			
			var price = 0;
			if(pushFlag)
				{
					goodsList.push(row.id);
					addGoodList(row.id);
					price = parseInt(row.price);//js中将字符串转换为数字
					countPrice = countPrice+price;
					addCountPrice(countPrice);//更新商品总价
				}
			
		},
		onUnselect:function(index,row){
			var pushFlag = goodListExist(row.id);
			if(!pushFlag)//false为不允许放入list，则表示当前值做过价格处理
				{
					var price = 0;
					price = parseInt(row.price);
					removeGoodList(row.id);
					countPrice = countPrice-price;
					addCountPrice(countPrice);//更新商品总价
				}
			
			
		},
		onUnselectAll:function(rows){
			var price = 0;
			
			for(var i=0;i<rows.length;i++)
			{
				var pushFlag = goodListExist(row.id);
				if(!pushFlag)
					{
						price = parseInt(rows[i].price);
						removeGoodList(rows[i].id);
						countPrice = countPrice-price;
					}
				
			}
			addCountPrice(countPrice);//更新商品总价
			
		},
		onSelectAll:function(rows){
			var price = 0;
			for(var i=0;i<rows.length;i++)
			{
				price = parseInt(rows[i].price);
				var pushFlag = goodListExist(rows[i].id);
				if(pushFlag)
				{
					goodsList.push(rows[i].id);
					addGoodList(rows[i].id);
					countPrice = countPrice+price;
				}
			}
			addCountPrice(countPrice);//更新商品总价
		},
		//创建子网格
		view: detailview,
	    detailFormatter:function(index,row){//注意2  
         return '<div style="padding:2px"><table id="ddv-' + row.id + '"></table></div>';  
        }, 
	    onExpandRow: function(index,row){
	    	
	    	var tableId = row.id;
	    	 $('#ddv-'+tableId).datagrid({
	    		method:'get',
	            url:contextPath+'/goods/getProductsOfGoods.action?id='+row.id,
	            fitColumns:true,
	            loadMsg:'加载中...',
	            height:'auto',
	            columns:[[
	                {field:'id',hidden:true},//商品和产品关联id
					{field:'goodId',hidden:true},
					{field:'productId',hidden:true},
					{field:'name',width:120,title:'产品名称',align:'center'},
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
			        {field:'durationOfUsername',width:50,title:'产品使用期',align:'center'},
			        {field:'probation',width:50,title:'试用期最大值',align:'center'},
		            {field:'orderProbation',title:'试用期(天)',width:50,align:'center',editor: {  
						type: 'text',  
	                    options: {  
	                        required: true
	                    }  
	                }},
	                {field:'stationOfPro',width:50,title:'站点',align:'center',
	                    editor : {  
		                        type : 'combobox',  
		                        options : {  
			                        valueField:'id',
									textField:'stationNumber',   
		                            panelHeight: 'auto',  
		                            required: false ,  
		                            editable:false  
	                        }  
	                    }  
	                }
	            ]],
	            onResize:function(){
	            	$('#'+productDatagrid).datagrid('fixDetailRowHeight',index);
	            },
	            onLoadSuccess:function(prodata){
	                
	                for(var i=0;i<prodata.rows.length;i++)
        			{
	                	 $('#ddv-'+tableId).datagrid('beginEdit', i);
	                	 var probationed = $('#ddv-'+tableId).datagrid('getEditor', {index:i,field:'orderProbation'});
						 probationed.target.val('0');
						 
        			}
	                
	                //绑定editor校验
	                var selectedproRows = $('#ddv-'+tableId).datagrid('getRows');
	                $.each(selectedproRows,function(indexp,row){
	                	var editors = $('#ddv-'+tableId).datagrid('getEditors', indexp);//获取当前行可编辑的值
			 			//校验
	                	var oldValue = row.probation;//当前产品可以设定的最大试用期
	                	
	                	editors[0].target.bind('change',function () {//sellprice校验
			 				
	                		if(editors[0].target.val()>oldValue)
	                			{
	                				$.messager.alert('提示', "产品中第"+(indexp+1)+"行数据的试用期天数大于试用期最大值");
	                				editors[0].target.val('0');
	                			}
	                		
		                		//更新goodlist内容
		                		var pushflag = goodListExist(tableId);
		                		if(!pushflag)
	                			{//当前更改的所属商品id已存在
	                				removeGoodList(tableId);
	                				goodsList.push(tableId);//※每次在操作goodlist的内容时就要同时操作商品总价
									addGoodList(tableId);//添加商品下的产品数据
	                			}
            		
			 				
			 				});
	                	
		                	//给站点更改绑定级联事件
	                		editors[1].target.combobox({
	                			onChange : function (n,o) {
		                			//更新goodlist内容
			                		var pushflag = goodListExist(tableId);
			                		if(!pushflag)
			                			{//当前更改的所属商品id已存在
			                				removeGoodList(tableId);
			                				goodsList.push(tableId);//※每次在操作goodlist的内容时就要同时操作商品总价
											addGoodList(tableId);//添加商品下的产品数据
			                			}
		                			
		                			}
	
	                		}); 
	                	
			        	
	                	});
	                
	              //处理站点combobox的默认值选中，这个处理只能放在"站点更改绑定级联事件"的后面，否则无法设定默认值
	                for(var i=0;i<prodata.rows.length;i++)
        			{
	                	 //处理站点combobox
						 var stationcombobox = $('#ddv-'+tableId).datagrid('getEditor', {index:i,field:'stationOfPro'});
						 var lotteryType = prodata.rows[i].lotteryType;
						 var sdata = getSelectedOtherStation(lotteryType);
						 $(stationcombobox.target).combobox( 'loadData' , sdata); 
						 if(sdata.length>0)
							 {
							 	$(stationcombobox.target).combobox('setValue' , sdata[0].id); //默认选中第一项
							 }
        			}
	                
	                $('#'+productDatagrid).datagrid('fixRowHeight',index);
		       
	            }
	        });
	        $('#'+productDatagrid).datagrid('fixDetailRowHeight',index);
	        
	    }
		
	});
}

/**
 * 获取站点详情
 * @param stationId：站点id
 */
function getDetailStation(stationId)
{
	var returnArr = new Array();
	var url = contextPath + '/station/getStationDetail.action';
	var paramData = new Object();
	paramData.id=stationId;
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        cache:false,
        url: url,
        data:paramData,
        dataType: "json",
        success: function (data) {
			
        	var province = data.addFormProvince;
        	var city = data.addFormCity;
        	var stationType = data.addFormStationStyle;//站点类型：1：体彩 2：福彩
        	
        	returnArr.push(province);//站点所属省
        	returnArr.push(city);//站点所属市
        	returnArr.push(stationType);//站点类型：1：体彩 2：福彩
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
	});
	
	return returnArr;
}

/**
 * 获取选中的其他彩种的站点的id
 * lotteryType:产品彩种
 */
function getSelectedOtherStation(lotteryType)
{
	var otherStation ;
	var returnArr = new Array();
	var selectStation = $("#stationAhidden").val();//获取选中的站点的id
	returnArr = getDetailStation(selectStation);//rec.id is stationId
	var currentStationType = returnArr[2];
	
	if(currentStationType == lotteryType)
		{
			var arr = new Array();
			
			var currentStationText = $("#stationA").textbox('getText');//当前选中站点站点号
			//替换加载editor的值为当前选中站点值
			otherStation = JSON.parse('[{ \"id\": \"'+selectStation+'\", \"stationNumber\": \"'+stationtype+'--'+currentStationText+'\"}]') ;
		}
	else
		{
			otherStation = getOtherStation();//当前选中的station的站主的其他站点
		}
	
	return otherStation;
}

/**
 * 根据当前选中站点的信息获取其另一类别站点
 * @returns
 */
function getOtherStation()
{
	var data = new Object();
	
	var selectStation = $("#stationAhidden").val();//获取选中的站点的id
//	var selectStationtext = $("#stationA").textbox('getText');
	
	data.id = selectStation;
	
	var returnData ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/order/getOtherStations.action',
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

//填充商品总价显示值
function addCountPrice(cprice)
{
	$("#pricehidden").val(cprice);
	$("#priceA").textbox('setText',cprice);
}



/**
 * 生成订单编码
 */
function generateCode()
{
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/order/generateOrdercode.action',
        dataType: "json",
        success: function (data) {
        	
        	var orderCode = data.code;
        	$("#codeA").textbox('setText',orderCode);
        	$("#codehidden").val(orderCode);
        	var creator = data.operator;
        	$("#creatorA").textbox('setText',creator);
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
}

/**
 * goodList中是否有这个值
 * @param value
 */
function goodListExist(value)
{
	var pushFlag = true;
	for(var i=0;i<goodsList.length;i++)
		{
			if(value == goodsList[i])
				{
					pushFlag= false;
				}
		}
	return pushFlag;
}

/**
 * 
 * @param value:放入到goodList的商品id
 */
function addGoodList(value)
{
	var goodArr = new Array();//放入的对象是key是和value的组合
	var selectedproRows = $('#ddv-'+value).datagrid('getRows');
	$.each(selectedproRows,function(j,selectedRow){
			var valueArr =  new Array();
			var productId = selectedRow.productId;
			var goodId = selectedRow.goodId;
			var proAndgoodId = selectedRow.id;
			var lotteryType = selectedRow.lotteryType;
			var editors = $('#ddv-'+value).datagrid('getEditors', j);
			var orderProbation = editors[0].target.val();//形成订单填写的试用期
        	var stationvalue = $(editors[1].target).combobox('getValue');
			
			valueArr.push(productId);//0
			valueArr.push(goodId);//1
			valueArr.push(orderProbation);//2
			valueArr.push(stationvalue);//放入站点号//3
			valueArr.push(lotteryType);//4
			valueArr.push(proAndgoodId);//5//商品和产品关联表id
			
			var proOfgoods = new map();
			proOfgoods.put(productId, valueArr);
			
			goodArr.push(proOfgoods);
		}
	);

	//判断是否存在
	if(!proOfgoods.contain(value))
		{//不包括当前值则可以放入
			proOfgoods.put(value, goodArr);
		}
	
}

/**
 * 移除goodList的内容
 * @param value
 */
function removeGoodList(value)
{
	for(var i=0;i<goodsList.length;i++)
	{
		if(value == goodsList[i])
			{
				goodsList.splice(i,1);
				proOfgoods.remove(value);
			}
	}
}

/**
 * 提交保存商品表单
 */
function submitAddgoods(operatype)
{
	$('#ff').form('submit',{
		url:contextPath+'/order/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			if($('#ff').form('enableValidation').form('validate')&& goodsList.length>0 )
				{
					flag = true;
					/*//存入“站点--产品”关联数据
					
					var proOfgoods  = new map();
					for(var i=0;i<goodsList.length;i++)
						{
							var goodArr = new Array();//放入的对象是key是和value的组合
							try
							{
								var selectedproRows = $('#ddv-'+goodsList[i]).datagrid('getRows');
								
								$.each(selectedproRows,function(j,selectedRow){
										var valueArr =  new Array();
										var productId = selectedRow.productId;
										var goodId = selectedRow.goodId;
										var proAndgoodId = selectedRow.id;
										var lotteryType = selectedRow.lotteryType;
										var orderProbationRow = $('#ddv-'+goodsList[i]).datagrid('getEditor', {index:j,field:'orderProbation'});
										var orderProbation = orderProbationRow.target.val();//形成订单填写的试用期
										var stationcombobox = $('#ddv-'+goodsList[i]).datagrid('getEditor', {index:j,field:'stationOfPro'});
					                	var stationvalue = $(stationcombobox.target).combobox('getValue');
										
										valueArr.push(productId);//0
										valueArr.push(goodId);//1
										valueArr.push(orderProbation);//2
										valueArr.push(stationvalue);//放入站点号//3
										valueArr.push(lotteryType);//4
										valueArr.push(proAndgoodId);//5//商品和产品关联表id
										
										var proOfgoods = new map();
										proOfgoods.put(productId, valueArr);
										
										goodArr.push(proOfgoods);
									}
					        	);
							}
							catch(e)
							{
								//商品没有点击展开设置试用期
								console.log('商品没有点击展开设置试用期');
							}
							
							
							proOfgoods.put(goodsList[i], goodArr);
						}*/
				}
			else if(goodsList.length==0)
				{
					$.messager.alert('提示', "请为当前订单选择商品!");
				}
			
			param.goodsList = JSON.stringify(goodsList);
			param.operatype = operatype;//0:保存 1：保存并提交
			param.proList = JSON.stringify(proOfgoods);
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	$("#setOrder").dialog('close');
	    	clearGoodsArray();
	    	initDatagrid();
	    	$('#payModeA').combobox("select",'0');
	    	
	    	
	    }
	});
}

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

