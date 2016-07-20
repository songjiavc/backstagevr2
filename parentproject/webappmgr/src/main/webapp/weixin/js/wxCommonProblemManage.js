var idArr = [];//选中的应用id
var cpList = new map();
$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			initDatagrid();//初始化数据列表
			idArr = [];
		});





/**
 * 重置
 */
function reset()
{
	$("#titleC").val("");
}




//关闭弹框
function closeDialog()
{
	$("#addCproblem").dialog('close');//初始化添加角色弹框关闭
	$("#updateCproblem").dialog('close');
}

/**
 * 初始化公众号的常见问题的数据列表
 */
function initDatagrid()
{
	
	var params = new Object();
	params.title = $("#titleC").val().trim();
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/weixincontrol/getWXcommonproblemList.action',//'datagrid_data1.json',
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
				{field:'title',title:'标题',width:'30%',align:'center'},
				{field:'createTime',title:'创建时间',width:'30%',align:'center'},
				{field:'opt',title:'操作',width:'30%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateCproblem(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteWxcommonProblems(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="5">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}


//添加通行证组弹框
function addCommonproblem()
{
	cpList = new map();
  	
	initcpList('','cproblemsA');
  	$("#addCproblem").dialog('open');
}







/**
 * 初始化待选择常见问题列表
 */
function initcpList(id,stationDataGridId)
{
	var params = new Object();
	
	cpList = new map();
	if('cproblemsU' == stationDataGridId)
	{
		params.id = id;//若为修改常见问题数据，则要带id，因为相关问题不可以选择本身的常见问题数据
		
		var stations = checkCproblem(id, stationDataGridId);
		
		for (var i = 0; i < stations.length; i++) {
			
			var cpid = stations[i].id;
			cpList.put(cpid, cpid);
		}
	}
	
	//渲染列表
	$('#'+stationDataGridId).datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url: contextPath + '/weixincontrol/getWXcommonproblemList.action',
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
				{field:'title',title:'标题',width:'90%',align:'center'}
		    ]],
	    onLoadSuccess:function(data){ 
	    	
	    	if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="7">没有数据</td></tr>');
			}
	    	
	    	var selectedRows = $('#'+stationDataGridId).datagrid('getRows');
	    	if(cpList.keys.length>0)
	    		{
	    			var cpId ;
		    		for(var i=0;i<cpList.keys.length;i++)
	    			{
		    			cpId = cpList.keys[i];
		    			$.each(selectedRows,function(j,selectedRow){
		    				
		    				if(selectedRow.id == cpId){
		    					
		    					 $('#'+stationDataGridId).datagrid('selectRow',j);
		    				}
		    			});
	    			}
	    		}
	    	
	    } ,
	    onSelect:function(index,row){
	    	
	    	if(!cpList.contain(row.id))//新数据
	    		{
	    		cpList.put(row.id, row.id);
	    		}
	    	
	    },
	    onUnselect:function(index,row){
			
	    	cpList.remove(row.id);
			
		},
		onSelectAll:function(rows){
			$.each(rows,function(i,row){
				
				if(!cpList.contain(row.id))//放入不存在新数据
	    		{
					cpList.put(row.id, row.id);
	    		}
			});
		},
		onUnselectAll:function(rows){
			$.each(rows,function(i,row){
				if(cpList.contain(row.id))//移除已存在数据
	    		{
					cpList.remove(row.id);
	    		}
				
			});
		}
		
	});
}

/**
 * 获取当前常见问题的相关问题数据
 * @param id
 * @param stationDataGridId
 * @returns
 */
function checkCproblem(id,stationDataGridId)
{
	var data = new Object();
	data.id = id;
	
	var stationList ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/weixincontrol/getRelatedCproblems.action',
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
 *常见问题修改
 */
function updateCproblem(id)
{
	var url = contextPath + '/weixincontrol/getDetailWxcommonproblem.action';
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
						title:data.title,
						content:data.content
					});
					
					initcpList(id,'cproblemsU');
					
	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
		
		
		$("#updateCproblem").dialog('open');
	
		
}


/**
 * 提交保存通行证组
 */
function submitAddCproblem()
{
	$('#ff').form('submit',{
		url:contextPath+'/weixincontrol/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			if($('#ff').form('enableValidation').form('validate') )
				{
					flag = true;
				}
			
			if(cpList.keys.length>0)//若选择了相关数据，则要将相关数据传递到后台
				{
					var relatedpArr = new Array();
					
					$.each(cpList.keys,function(j,cpId){
	    				
						relatedpArr.push(cpId);
	    			});
				
					param.relatedProblems = relatedpArr.toString();
				}
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addCproblem").dialog('close');//初始化添加通行证组弹框关闭
	    	
	    	$('#ff').form('clear');//清空表单内容
	    	initDatagrid();
	    	
	    	
	    }
	});
}

/**
 * 提交修改通行证组
 */
function submitUpdateCproblem()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/weixincontrol/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			if($('#ffUpdate').form('enableValidation').form('validate')  )
				{
					flag = true;
				}
			
			if(cpList.keys.length>0)//若选择了相关数据，则要将相关数据传递到后台
			{
				var relatedpArr = new Array();
				
				$.each(cpList.keys,function(j,cpId){
    				
					relatedpArr.push(cpId);
    			});
			
				param.relatedProblems = relatedpArr.toString();
			}
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateCproblem").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


/**
 * 删除常见问题数据
 */
function deleteWxcommonProblems(id)
{
	var url = contextPath + '/weixincontrol/deleteWxcommonProblems.action';
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
		                    window.parent.location.href = contextPath + "/error.jsp";
		                }
		           });
		        	
	        }  
	    });  
	}
		
	
}

/**
 * 校验当前用户组是否有其他数据关联，若有关联则不可以删除
 * @param id
 */
function checkDeletedvisible(id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/userGroup/checkDeletedvisible.action',
        data:data,
        dataType: "json",
        success: function (data) {
        		flag = data.exist;//true为可以删除，false不可以删除
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
	
	return flag;
}



/**
 * 批量删除常见问题数据
 */
function deleteWxcommonproblemsList(operaType)
{
	var url = contextPath + '/weixincontrol/deleteWxcommonProblems.action';
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
 * 自定义校验常见问题标题
 */
$.extend($.fn.validatebox.defaults.rules, {
	checkTitleUnique: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>15){  
        		rules.checkTitleUnique.message = "当前常见问题标题不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkTitleUnique.message = "当前常见问题标题已存在"; 
        		
                return !checkTitle($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});




/**
 * 校验常见问题标题是否唯一，避免添加同一个常见问题
 * @param id
 * @param name
 * @returns {Boolean}
 */
function checkTitle(id,name)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.title = name;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/weixincontrol/checkTitle.action',
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

