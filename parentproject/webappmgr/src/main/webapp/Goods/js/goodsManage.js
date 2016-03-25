var idArr = [];//选中的产品id数组
var productList = new map();//选中的产品数据

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			initDatagrid();//初始化数据列表
			initQueryProvince();//初始化模糊查询省数据
			bindComboboxChange();//给下拉框绑定选中事件
			
			idArr = [];
			productList = new map();
		});

/**
 * 清空全局变量
 */
function clearProductList()
{
	productList = new map();
}

/**
 * 绑定上级角色下拉框改变事件
 */
function bindComboboxChange()
{
	/*产品区域级联绑定*/
	//添加表单中的省份级联
	$("#privinceA").combobox({

		onSelect: function (rec) {
			 //加载市级数据
			 initCities('add','cityA','',rec.pcode);
			 initProductDatagrid('privinceA','cityA','productDatagridA','goodTypeAhidden','goodTypeA','goodTypeAhidden','goodTypeA');//初始化待选择产品列表
		}

		}); 
	//修改表单中的省份级联
	$("#privinceU").combobox({

		onSelect: function (rec) {
			//加载市级数据
			initCities('update','cityU','',rec.pcode);
			initProductDatagrid('privinceU','cityU','productDatagridU','goodTypeUhidden','goodTypeU','goodTypeUhidden','goodTypeU');//初始化待选择产品列表
		}

		}); 
	
	//模糊查询“省”级联
	$("#privinceC").combobox({

		onSelect: function (rec) {
			//加载市级数据
			initQueryCities(rec.pcode);
		}

		}); 
	
	$("#cityA").combobox({

		onSelect: function (rec) {
			
				initProductDatagrid('privinceA','cityA','productDatagridA','goodTypeAhidden','goodTypeA');//初始化待选择产品列表
			}

		}); 
	
	$("#cityU").combobox({

		onSelect: function (rec) {
			
				initProductDatagrid('privinceU','cityU','productDatagridU','goodTypeUhidden','goodTypeU');//初始化待选择产品列表
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
 * 初始化省数据
 * @param addOrUpdate:标记当前是添加表单操作还是修改表单的操作,值为"add" 和"update"
 * @param provinceId:当前操作的省份combobox标签的id
 * @param pcode:应该选中的省份的code
 */
function initProvince(addOrUpdate,provinceId,pcode)
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
 * 初始化市数据
 * @param addOrUpdate:标记当前是添加表单操作还是修改表单的操作,值为"add" 和"update"
 * @param cityId:当前操作的form表单的id
 * @param ccode:应该选中的市数据code
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
	//重置产品名称
	$("#goodsNameC").val("");
	//重置产品编码
	$("#goodscodeC").val("");
	//重置省份选中“全部”
	var proAllId = getProvinceAllId();
	$("#privinceC").combobox("select",proAllId);
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
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return proAllId;
}



//关闭弹框
function closeDialog()
{
	$("#addProduct").dialog('close');//初始化添加角色弹框关闭
	$("#updateProduct").dialog('close');
}

/**
 * 初始化商品列表数据
 */
function initDatagrid()
{
	var params = new Object();
	params.goodsName = $("#goodsNameC").val().trim();
	params.goodscode = $("#goodscodeC").val().trim();
	params.province = $("#privinceC").combobox('getValue');
	params.city = $("#cityC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/goods/getGoodsList.action',//'datagrid_data1.json',
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
				{field:'connectOrders',hidden:true},//是否与有效订单关联
				{field:'code',title:'商品编码',width:150,align:'center'},
		        {field:'name',width:120,title:'商品名称',align:'center'},
				{field:'price',title:'价格(元)',width:60,align:'center'},
				{field:'provinceName',title:'省级区域',width:80,align:'center'},
				{field:'cityName',title:'市级区域',width:80,align:'center'},
				{field:'createTime',title:'创建时间',width:130,align:'center'},
				{field:'goodsDesprition',title:'商品描述',width:120,align:'center'},
				{field:'status',title:'商品状态',width:70,align:'center',
					formatter:function(value,row,index){
							var showStatus = "";
							switch(row.status)
							{
								case '0':showStatus="待上架";break;
								case '1':showStatus="上架";break;
								case '2':showStatus="下架";break;
							}
							return showStatus;
						}
					},
					{field:'opt',title:'操作',width:160,align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateGoods(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteGoods(&quot;'+row.id+'&quot;,&quot;'+row.status+'&quot;,&quot;'+row.connectOrders+'&quot;)" href="javascript:void(0)">删除</a>';
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
	        
	        
	    },
	    rowStyler:function(index,row){//设置行样式
	        if (row.status==1){//上架
	    	 		 return 'background-color:#cbdcf8;color:black;';
			}
    	 	else	if (row.status==2){//下架
				 return 'background-color:#dddcdc;color:black;';
			}
    	 	
			else  if (row.status==0){//待上架
				return 'background-color:#6293BB;color:black;';
			}
				
			},
	});
}

/**
 * 初始化选择产品列表
 * @param provinceId
 * @param cityId
 */
function initProductDatagrid(provinceId,cityId,productDatagrid,hiddenGTId,showGTId)
{
	var params = new Object();
	params.province = $("#"+provinceId).combobox('getValue');
	params.orCity = $("#"+cityId).combobox('getValue');
	
	if("productDatagridU" == productDatagrid)//修改商品
		{
			params.goodsId = $("#idU").val();
		}
	
	$('#'+productDatagrid).datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/goods/getProductList.action',//'datagrid_data1.json',
		method:'get',
		border:false,
		singleSelect:false,
		fitColumns:true,
		pagination:true,
		collapsible:false,
		pageSize:5,//初始化页面显示条数的值是根据pageList的数组中的值来设置的，否则无法正确设置
		pageList:[5,10],
		columns:[[
				{field:'ck',checkbox:true},
				{field:'id',hidden:true},
		        {field:'name',width:120,title:'产品名称'},
				{field:'code',title:'产品编码',width:120,align:'center'},
				{field:'price',title:'参考价格(元)',width:100,align:'center'},
				{field:'provinceName',title:'省级区域',width:120,align:'center'},
				{field:'cityName',title:'市级区域',width:120,align:'center'},
				{field:'lotteryType',width:50,title:'彩种',  
		            formatter:function(value,row,index){  
		            	var lotteryTypeName ='';
		            	switch(value)
		            	{
		            		case '1':lotteryTypeName='体彩';break;
		            		case '2':lotteryTypeName='福彩';break;
		            	}
		            	return lotteryTypeName;  
		            }  },
				{field:'cpdlName',title:'大类别',width:100,align:'center'},
				{field:'cpzlName',title:'中类别',width:100,align:'center'},
				{field:'durationOfUsername',title:'使用期',width:80,align:'center'},
//				{field:'cpxlName',title:'小类别',width:100,align:'center'},
//				{field:'createTime',title:'创建时间',width:120,align:'center'},
				{field:'sellPrice',title:'销售价格(元)',width:100,align:'center',editor: {  
                    type: 'text',  
                    options: {  
                        required: true
//                        ,  
//                        validType: ['money']  
                    }  
                }}, 
				{field:'probation',title:'试用期(天)',width:100,align:'center',editor: {  
					type: 'text',  
                    options: {  
                        required: true
//                        ,  
//                        validType: ['number']  
                    }  
                }}
		    ]],  
	    onLoadSuccess:function(data){  
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        else
	        	{
	        		for(var i=0;i<data.rows.length;i++)
	        			{
	        				$('#'+productDatagrid).datagrid('beginEdit', i);
	        				 var editors = $('#'+productDatagrid).datagrid('getEditors', i);//获取当前行可编辑的值
	        				 if(null != editors)
	        					 {
	        					 	var cpzlDm = data.rows[i].cpzlDm;
	        					 	if('0201' == cpzlDm)
	        					 		{//产品的中类为‘走势图’的其试用期的值默认为‘7’
	        					 			editors[1].target.val('7');//设置试用期的默认值为‘0’
	        					 		}
	        					 	else
	        					 		{//非走势图的超试用期的默认值为‘0’
	        					 			editors[1].target.val('0');//设置试用期的默认值为‘0’
	        					 		}
	        					 	
	        					 }
	        			}
	        	}
	        
	        //选中（写入后台读取的产品数据进行选中）
	        var selectedRows = $('#'+productDatagrid).datagrid('getRows');
	        if("productDatagridU" == productDatagrid)//修改商品
	        	{
		            var  pdList = checkProducts(params.goodsId,productDatagrid);
			        for(var i=0;i<pdList.length;i++)
					{
			        	var	proId = pdList[i].productId;
			        	var	probation = pdList[i].probation; 
			        	var	price  = pdList[i].price;
			        	
			        	$.each(selectedRows,function(j,selectedRow){
			        			
			        		
							if(selectedRow.id == proId){
								 var selled = $('#'+productDatagrid).datagrid('getEditor', {index:j,field:'sellPrice'});
								 var probationed = $('#'+productDatagrid).datagrid('getEditor', {index:j,field:'probation'});
								 selled.target.val(price);//※只有当editor的type是text的时候才可以设置值成功
								 probationed.target.val(probation);
								 $('#'+productDatagrid).datagrid('selectRow',j);
							}
			        	});
					}
	        	}
	       
	        //写入和选中当前数据选中的产品（写入productlist选中的）
	        if(productList.keys.length>0)
	        	{
	        		var editvalue = [];
	        		for(var i=0;i<productList.keys.length;i++)
	        			{
	        				editvalue = productList.get(productList.keys[i]);
	        				$.each(selectedRows,function(j,selectedRow){
	        					var	proId = productList.keys[i];
	    			        	var	probation = editvalue[1]; 
	    			        	var	price  = editvalue[0];
				        		
								if(selectedRow.id == proId){
									 var selled = $('#'+productDatagrid).datagrid('getEditor', {index:j,field:'sellPrice'});
									 var probationed = $('#'+productDatagrid).datagrid('getEditor', {index:j,field:'probation'});
									 selled.target.val(price);//※只有当editor的type是text的时候才可以设置值成功
									 probationed.target.val(probation);
									 $('#'+productDatagrid).datagrid('selectRow',j);
								}
				        	});
	        			}
	        	}
	        
	        //给每一行绑定校验事件
	        $.each(selectedRows,function(index,row){
		        	 var editors = $('#'+productDatagrid).datagrid('getEditors', index);//获取当前行可编辑的值
		 			//校验
		 			editors[0].target.bind('change',function () {//sellprice校验
		 				
		 				var oldvalue = [];
		 				if(checkEditorSellprice(editors[0].target.val()))
		 					{//符合输入的金额校验
		 					  oldvalue = productList.get(row.id);
		 					  if(null != oldvalue)
		 						  {
		 						  	oldvalue[0] = editors[0].target.val();
		 						  }
		 					  
		 					}
		 				else
		 					{
		 						$.messager.alert('提示', "产品列表中第"+(index+1)+"行数据的销售价格不符合金额的输入规则");
//		 						productList.remove(row.id);
		 						$('#'+productDatagrid).datagrid('unselectRow', index);
		 					}
		 				
		 			});
		 			editors[1].target.bind('change',function () {//probation校验
	
		 				var oldvalue = [];
		 				if(checkEditorProbation(editors[1].target.val()))
		 					{//符合输入的金额校验
		 					  oldvalue = productList.get(row.id);
		 					  if(null != oldvalue)
	 						  {
		 						 oldvalue[1] = editors[1].target.val();
	 						  }
		 					  
		 					}
		 				else
		 					{
		 						$.messager.alert('提示', "产品列表中第"+(index+1)+"行数据的试用天数应输入0或正整数");
//		 						productList.remove(row.id);
		 						$('#'+productDatagrid).datagrid('unselectRow', index);
		 					}
		 			});
		        	
	    	});
	       
	        
	        
	    },
	   /* onClickRow:function(index,row){
			var editors = $('#'+productDatagrid).datagrid('getEditors', index);//获取当前行可编辑的值
			
		},*/
	    onSelect:function(index,row){
			//获取选中行的填写值
			var editors = $('#'+productDatagrid).datagrid('getEditors', index);//获取当前行可编辑的值
			var errRow = index;
			if(null != editors && ""!=editors)
				{
					if("" == editors[0].target.val() || "" == editors[1].target.val())
					{
						$.messager.alert('提示', "请填写选中产品的销售价格和试用期!");
						$('#'+productDatagrid).datagrid('unselectRow', index);
						$('#'+productDatagrid).datagrid('onUncheck', index);
					}
					else
					{
						var pushFlag = false;
						if(!productList.contain(row.id))//新选中的产品数据
							{
								var editvalue = [];//当前选中行的编辑值
								var pushFlag = true;
								if(checkEditorSellprice(editors[0].target.val()))
								{//符合输入的金额校验
									editvalue.push(editors[0].target.val());//放置sellprice销售价格
									
									if(checkEditorProbation(editors[1].target.val()))
									{//符合输入的金额校验
										editvalue.push(editors[1].target.val());//放置probation试用期
									}
									else
									{
										pushFlag = false;
										$.messager.alert('提示', "1产品列表中第"+(errRow+1)+"行数据的试用天数应输入0或正整数");
									}
								}
								else
								{
									pushFlag = false;
									$.messager.alert('提示', "1产品列表中第"+(errRow+1)+"行数据的销售价格不符合金额的输入规则");
								}
								
								
								
								if(pushFlag)
									{
										productList.put(row.id, editvalue);
										//更新商品彩种显示
										fillShowGoodstype(productDatagrid,hiddenGTId,showGTId);
									}
								else
									{
										$('#'+productDatagrid).datagrid('unselectRow', index);
										$('#'+productDatagrid).datagrid('onUncheck', index);
									}
							}
						
					}
				}
			
		},
		onUnselect:function(index,row){
			
			if(productList.contain(row.id))//新选中的产品数据
			{
				//更新商品彩种显示
				fillShowGoodstype(productDatagrid,hiddenGTId,showGTId);
			}
			productList.remove(row.id);
			
		},
		onSelectAll:function(rows){
			
				$.each(rows,function(index,row){
					//获取选中行的填写值
					var editors = $('#'+productDatagrid).datagrid('getEditors', index);//获取当前行可编辑的值
					var errRow = index;
					if(null != editors && ""!=editors)
						{
							if("" == editors[0].target.val() || "" == editors[1].target.val())
							{
								$.messager.alert('提示', "请填写选中的第"+(errRow+1)+"行产品的销售价格和试用期!");
								$('#'+productDatagrid).datagrid('unselectRow', index);
								$('#'+productDatagrid).datagrid('onUncheck', index);
								
							}
							else
							{
								var pushFlag = false;
								if(!productList.contain(row.id))//新选中的产品数据
									{
										var editvalue = [];//当前选中行的编辑值
										var pushFlag = true;
										if(checkEditorSellprice(editors[0].target.val()))
										{//符合输入的金额校验
											editvalue.push(editors[0].target.val());//放置sellprice销售价格
											
											if(checkEditorProbation(editors[1].target.val()))
											{//符合输入的金额校验
												editvalue.push(editors[1].target.val());//放置probation试用期
											}
											else
											{
												pushFlag = false;
												$.messager.alert('提示', "1产品列表中第"+(errRow+1)+"行数据的试用天数应输入0或正整数");
											}
										}
										else
										{
											pushFlag = false;
											$.messager.alert('提示', "1产品列表中第"+(errRow+1)+"行数据的销售价格不符合金额的输入规则");
										}
										if(pushFlag)
											{
												productList.put(row.id, editvalue);
												//更新商品彩种显示
												fillShowGoodstype(productDatagrid,hiddenGTId,showGTId);
											}
										else
											{
												$('#'+productDatagrid).datagrid('unselectRow', index);
												$('#'+productDatagrid).datagrid('onUncheck', index);
											}
									}
								
							}
						}
				});
		},
		onUnselectAll:function(rows){
			$.each(rows,function(i,row){
					
				if(productList.contain(row.id))//新选中的产品数据
				{
					//更新商品彩种显示
					fillShowGoodstype(productDatagrid,hiddenGTId,showGTId);
				}
				productList.remove(row.id);
			});
		}
		
	});
}

/**
 * 填充商品彩种显示框
 */
function fillShowGoodstype(productDatagrid,hiddenId,showId)
{
	var gType;
	var selectedRows = $('#'+productDatagrid).datagrid('getSelections');//获取datagrid中选中的所有行
	var flag;
	$.each(selectedRows,function(i,selectedRow){
		
		if(i == 0)
			{
				flag = selectedRow.lotteryType;
				gType = selectedRow.lotteryType;
			}
		else
			{
				if(flag != selectedRow.lotteryType)
					{
						gType = '0';//双机
					}
			}
		
	});
	
	
	var showGoodstypeText = '';
	$("#"+hiddenId).val(gType);
	switch(gType)
	{
		case '1':showGoodstypeText='体彩';break;
		case '2':showGoodstypeText='福彩';break;
		case '0':showGoodstypeText='双机';break;
	}
	$("#"+showId).val(showGoodstypeText);
}

//校验销售价格
function checkEditorSellprice(value)
{
	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
}

//校验试用期天数为正整数
function checkEditorProbation(value)
{
	return /^[0-9]+.?[0-9]*$/.test(value);
}

/**
 *商品修改
 */
function updateGoods(id)
{
	clearProductList();
	var url = contextPath + '/goods/getDetailGoods.action';
	var data1 = new Object();
	data1.id=id;//权限的id
	
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
						price:data.price,
						privince:data.privince,
						productDesprition:data.productDesprition,
						city:data.city,
						status:data.status
					});
					
					//初始化省份combobox
					initProvince("update", "privinceU", data.provinceDm);
					//初始化市级区域combobox
					initCities('update','cityU',data.cityDm,data.provinceDm);
					//初始化待选择产品列表
					initProductDatagrid('privinceU','cityU','productDatagridU','goodTypeUhidden','goodTypeU');//初始化待选择产品列表
					
					
			
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
		
		//选中当前商品对应产品
//		checkProducts(id,'productDatagridU');
		
		$("#updateProduct").dialog('open');
	
		
}

/**
 * 选中当前商品对应产品
 * @param id
 */
function checkProducts(id,productDatagrid)
{
	var data = new Object();
	data.id = id;
	
	var proList ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/goods/getProductsOfGoods.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	proList = returndata;
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return proList;
}

/**
 * 提交保存商品表单
 */
function submitAddgoods()
{
	$('#ff').form('submit',{
		url:contextPath+'/goods/saveOrUpdate.action',
		onSubmit:function(param){
//			param.idArr = JSON.stringify(idArr);
			param.productList = JSON.stringify(productList);
			var flag = false;
			if($('#ff').form('enableValidation').form('validate') && productList.keys.length>0)
				{
					flag = true;
				}
			else if(productList.keys.length==0)
				{
					$.messager.alert('提示', "请选择当前商品配套的产品!");
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addProduct").dialog('close');//初始化添加角色弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	clearProductList();
	    	initDatagrid();
	    	$('#ff [name="status"]:radio').each(function() {   //设置“上架”为默认选中radio
	            if (this.value == '0'){   
	               this.checked = true;   
	            }       
	         }); 
	    	
	    	
	    }
	});
}

/**
 * 提交修改商品表单
 */
function submitUpdategoods()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/goods/saveOrUpdate.action',
		onSubmit:function(param){
//			param.idArr = JSON.stringify(idArr);
			param.productList = JSON.stringify(productList);
			var flag = false;
			if($('#ffUpdate').form('enableValidation').form('validate') && productList.keys.length>0)
				{
					flag = true;
				}
			else if(productList.keys.length==0)
			{
				$.messager.alert('提示', "请选择当前商品配套的产品!");
			}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateProduct").dialog('close');
	    	//修改角色后刷新数据列表
	    	clearProductList();
	    	initDatagrid();
	    	
	    }
	});
}

/**
 * 生成商品编码
 */
function generateCode()
{
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/goods/generateGoodscode.action',
        dataType: "json",
        success: function (data) {
        	
        	var proCode = data.code;
        	$("#codeA").textbox('setText',proCode);
        	$("#codehidden").val(proCode);
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
}

/**
 * 删除商品数据
 * @param id
 * @param status:当前待删除商品状态（0：待上架，1：上架 2：下架）
 * @param connectOrders:是否与有效订单数据关联
 */
function deleteGoods(id,status,connectOrders)
{
	var url = contextPath + '/goods/deleteGoods.action';
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
		if('1' == status)//商品当前状态为“上架”
			{
				$.messager.alert('提示',"当前待删除商品已上架,不可删除!");
				deleteFlag = false;
			}
	else
		if('1' == connectOrders)//商品与有效订单数据有关联
			{
				$.messager.alert('提示',"当前待删除商品已被购买,不可删除!");
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
 * 批量操作商品数据状态
 * @param operaType:操作类别（0：删除 1：上架 2：下架）
 */
function deleteGoodsList(operaType)
{
	var url = contextPath + '/goods/deleteGoods.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		
		if('0' == operaType)
			{
				if('1' == rows[i].status)//商品当前状态为“上架”
					{
						deleteFlag = false;
						$.messager.alert('提示', "当前待删除商品编码为:"+rows[i].code+"的商品已上架,不可进行删除操作!");
						break;
					}
				else
					if('1' == rows[i].connectOrders)//商品与有效订单数据有关联
						{
							$.messager.alert('提示',"当前待删除商品编码为:"+rows[i].code+"已被购买,不可进行删除操作!");
							deleteFlag = false;
							break;
						}
			}
	}
	
	if(deleteFlag)
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台data
				data1.operaType = operaType;
				
				var alertMsg = "您确认删除选中数据？";//默认的提示信息为删除数据的提示信息
				if('1'==operaType)
					{
						alertMsg = "您确认上架选中数据？";
					}
				else if("2"==operaType)
					{
						alertMsg = "您确认下架选中数据？";
					}
				
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
 * 自定义校验商品名称？？？（暂定商品名称校验规则：全局唯一）
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkAname: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAname.message = "当前商品名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAname.message = "当前商品名称已存在"; 
        		
                return !checkProName($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});

/**
 * 自定义校验商品编码
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkCodes: {//自定义校验code
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>15){  
        		rules.checkCodes.message = "当前商品编码不可为空且长度不可以超过15个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkCodes.message = "当前商品编码已存在"; 
                return !checkCode($("#"+param[1]).val(),value);
    		}
        	
        	
        }
    }
});





/**
 * 校验code,name唯一性
 */
function checkCode(id,code)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.code = code;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/goods/checkCode.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	if(data.exist)//若data.isExist==true,则当前校验值已存在，则不可用使用
        		{
        			flag = true;
        		}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return flag;
}

//校验产品名称（暂定规则：全局唯一）
function checkProName(id,name)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.name = name;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/goods/checkGoodsName.action',
        data:data,
        dataType: "json",
        success: function (data) {
        	if(data.exist)//若data.isExist==true,则当前校验值已存在，则不可用使用
        		{
        			flag = true;
        		}
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return flag;
}


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

