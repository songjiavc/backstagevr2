var idArr = [];//选中的应用id数据

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			
			initParentAppList('appIdC','','',true);
			
			initDatagrid();//初始化数据列表
			
			idArr = [];
		});





/**
 * 重置
 */
function reset()
{
	//重置应用版本名称
	$("#appVerNameC").val("");
	//重置所属应用的combobox
	initParentAppList('appIdC','','',true);
}

//取消添加应用版本弹框触发方法
function addDialogCancel()
{
	$('#addAppVersion').dialog('close');
    $('#ff').form('clear');//清空表单内容
     $('#ff [name="appVersionStatus"]:radio').each(function() {   //设置“待上架”为默认选中radio
	            if (this.value == '0'){   
	               this.checked = true;   
	            }       
	         }); 
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
        url: contextPath+'/advertisement/getFileOfAppad.action',
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


//关闭弹框
function closeDialog()
{
	$("#addAppVersion").dialog('close');
	$("#updateAppVersion").dialog('close');
}

/**
 * 初始化应用列表数据
 */
function initDatagrid()
{
	
	var params = new Object();
	params.appName = $("#appVerNameC").val().trim();//获取模糊查询条件“应用版本名称”
	params.appId = $("#appIdC").combobox('getValue');//所属应用的id
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/appversion/getAppversionList.action',//'datagrid_data1.json',
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
				{field:'appVersionStatus',hidden:true},//应用版本状态(0:待上架1:上架2:下架3:更新)
				{field:'appVersionCode',title:'应用版本编码',width:150,align:'center'},
		        {field:'appVersionName',width:120,title:'应用版本名称',align:'center'},
		        {field:'versionCode',width:100,title:'版本号',align:'center'},
		        {field:'appName',width:100,title:'所属应用',align:'center'},
				{field:'appVersionStatusName',title:'应用版本状态',width:100,align:'center'},
				{field:'appDeveloper',title:'应用版本开发者',width:100,align:'center'},
				{field:'createTime',title:'创建时间',width:130,align:'center'},
				{field:'opt',title:'操作',width:160,align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateAppVersion(&quot;'+row.id+'&quot;,&quot;'+row.appVersionStatus+'&quot)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteAppVersion(&quot;'+row.id+'&quot;,&quot;'+row.appVersionStatus+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="9">没有数据</td></tr>');
			}
	        
	        
	    },
	    rowStyler:function(index,row){//设置行样式
	        if (row.appVersionStatus==1){//上架
	    	 		 return 'background-color:#cbdcf8;color:black;';
			}
    	 	else	if (row.appVersionStatus==2){//下架
				 return 'background-color:#dddcdc;color:black;';
			}
    	 	
			else  if (row.appVersionStatus==0){//待上架
				return 'background-color:#6293BB;color:black;';
			}
	        
			else  if (row.appVersionStatus==3){//更新
				return 'background-color:#FFFF00;color:black;';
			}
				
			},
	});
}

/**
 * 初始化所属应用的combobox
 * @param appId:所属应用标签id
 * @param addOrUpdate：“add” 或 “update”
 * @param isAll:查询的返回数据是否带“全部”
 * @param oldValue:应用版本原来选中的上级应用版本值
 */
function initParentAppList(appId,addOrUpdate,oldValue,isAll)
{
	$('#'+appId).combobox('clear');//清空combobox值
	
	var data = new Object();
	
	data.isAll = isAll;
	
	$('#'+appId).combobox({
			queryParams:data,
			method:'get',
			url:contextPath+'/appversion/getAppcomboList.action',
			valueField:'id',
			textField:'appName',
			 onLoadSuccess: function (data1) { //数据加载完毕事件
                 if (data1.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#"+appId).combobox('select',data1[data1.length-1].id);
                 }
                 else
                 if (data1.length > 0 && "update" == addOrUpdate)
            	 {
                	 $("#"+appId).combobox('select',oldValue);
            	 }
                 else
            	 {
                	 $("#"+appId).combobox('select',data1[data1.length-1].id);
            	 }
					
             }
		}); 
}




/**
 *应用版本修改
 */
function updateAppVersion(id,appversionStatus)
{
	var url = contextPath + '/appversion/getDetailAppversion.action';
	var data1 = new Object();
	data1.id=id;//应用的id
	var updateFlag = true;
	
	if('1' == appversionStatus)//应用当前状态为“上架”
	{
		$.messager.alert('提示',"当前待编辑应用版本已上架,不可编辑!");
		updateFlag = false;
	}
	if(updateFlag)
		{
			$.ajax({
				async: false,   //设置为同步获取数据形式
		        type: "get",
		        url: url,
		        data:data1,
		        dataType: "json",
		        success: function (data) {
		        	
						$('#ffUpdate').form('load',{
							id:data.id,
							appVersionCode:data.appVersionCode,
							appVersionName:data.appVersionName,
							versionCode:data.versionCode,
							appVersionUrl:data.appVersionUrl,
							appDeveloper:data.appDeveloper,
							appVersionStatus:data.appVersionStatus,
							versionDescription:data.versionDescription //版本描述为之后要添加的字段，用来存储应用版本描述
						});
						
						initApkList(data.appVersionUrl,'appVersionUrlU');
						
						var appId = data.appId;//所属的应用id
						$("#codeU").textbox('setText',data.appVersionCode);
						initParentAppList('appIdU','update',appId,false);
		        	
		        },
		        error: function (XMLHttpRequest, textStatus, errorThrown) {
		            window.parent.location.href = contextPath + "/error.jsp";
		        }
			});
			
			
			$("#updateAppVersion").dialog('open');
		}
	
		
	
		
}


/**
 * 提交保存应用版本表单
 */
function submitAddAppVersion()
{
	$('#ff').form('submit',{
		url:contextPath+'/appversion/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			
			var upId = $("#urlHiddenA").val();
			var haveFujian = upIdHaveFujian(upId);//返回值为false时则没有附件
			var fujianFlag = true;
			if(null == upId||''==upId ||!haveFujian)
			{
				fujianFlag = false;
				$.messager.alert('提示', "请上传应用版本安装包!");
			}
			if($('#ff').form('enableValidation').form('validate') && fujianFlag)
				{
					flag = true;
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addAppVersion").dialog('close');//初始化添加应用弹框关闭
	    	
	    	$('#ff').form('clear');//清空表单内容
	    	initDatagrid();
	    	$('#ff [name="appVersionStatus"]:radio').each(function() {   //设置“待上架”为默认选中radio
	            if (this.value == '0'){   
	               this.checked = true;   
	            }       
	         }); 
	    	
	    	
	    }
	});
}

/**
 * 提交修改商品表单
 * 
 */
function submitUpdateAppVersion()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/appversion/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			
			var upId = $("#urlHiddenU").val();
			var haveFujian = upIdHaveFujian(upId);//返回值为false时则没有附件
			var fujianFlag = true;
			if(null == upId||''==upId ||!haveFujian)
			{
				fujianFlag = false;
				$.messager.alert('提示', "请上传应用版本安装包!");
			}
			if($('#ffUpdate').form('enableValidation').form('validate') && fujianFlag)
				{
					flag = true;
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateAppVersion").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}

/**
 * 生成应用编码
 */
function generateCode()
{
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/appversion/generateAppVercode.action',
        dataType: "json",
        success: function (data) {
        	
        	var appVerCode = data.code;
        	$("#codeA").textbox('setText',appVerCode);
        	$("#codehidden").val(appVerCode);
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
}

/**
 * 删除应用版本数据
 * @param id
 * @param appStatus:应用版本状态（0：待上架1：上架2：下架3：更新）只有在0和2时可以删除，1和3时不可以进行删除操作
 */
function deleteAppVersion(id,appVersionStatus)
{
	var url = contextPath + '/appversion/deleteAppversions.action';
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
		if('1' == appVersionStatus)//应用当前状态为“上架”
			{
				$.messager.alert('提示',"当前待删除应用版本已上架,不可删除!");
				deleteFlag = false;
			}
	else
		if('3' == appVersionStatus)//应用当前状态为”更新“
			{
				$.messager.alert('提示',"当前待删除应用版本为更新状态,不可删除!");
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
 * 批量删除应用版本数据
 */
function deleteAppVerList(operaType)
{
	var url = contextPath + '/appversion/deleteAppversions.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	for(var i=0; i<rows.length; i++)
	{
		codearr.push(rows[i].id);//code
		
		var appVersionStatus = rows[i].appVersionStatus;
		
		if('1' == appVersionStatus)//应用当前状态为“上架”
			{
				var appVersionCode= rows[i].appVersionCode;
				$.messager.alert('提示',"当前待删除应用编码为:"+appVersionCode+"的应用已上架,不可删除!");
				deleteFlag = false;
				break;
			}
		else
			if('3' == appVersionStatus)//应用当前状态为”更新“
				{
					$.messager.alert('提示',"当前待删除应用编码为:"+appVersionCode+"的应用为更新状态,不可删除!");
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
 * 批量更新应用版本状态
 * @param appStatus
 */
function updateAppVerStatus(appVersionStatus)
{
	var url = contextPath + '/appversion/updateAppversionStatus.action';
	var data1 = new Object();
	
	var codearr = new Array();
	var rows = $('#datagrid').datagrid('getSelections');
	
	var deleteFlag = true;
	
	if('1'==appVersionStatus)//上架
		{
			for(var i=0; i<rows.length; i++)
			{
				codearr.push(rows[i].id);//code
				
				var cappVersionStatus = rows[i].appVersionStatus;
				
				
				/*if('1' == cappStatus)//应用当前状态为“上架”
					{
						var appcode= rows[i].appCode;
						$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用已上架,不可删除!");
						deleteFlag = false;
					}
				else
					if('3' == cappStatus)//应用当前状态为”更新“
						{
							$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用为更新状态,不可删除!");
							deleteFlag = false;
						}*/
			}
		}
	else
		if('2'==appVersionStatus)//下架
		{
			for(var i=0; i<rows.length; i++)
			{
				codearr.push(rows[i].id);//code
				
				var cappVersionStatus = rows[i].appVersionStatus;
				
				
				/*if('1' == cappStatus)//应用当前状态为“上架”
					{
						var appcode= rows[i].appCode;
						$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用已上架,不可删除!");
						deleteFlag = false;
					}
				else
					if('3' == cappStatus)//应用当前状态为”更新“
						{
							$.messager.alert('提示',"当前待删除应用编码为:"+appcode+"的应用为更新状态,不可删除!");
							deleteFlag = false;
						}*/
			}
		}
	
	
	
	if(deleteFlag)
		{
			if(codearr.length>0)
			{
				data1.ids=codearr.toString();//将id数组转换为String传递到后台data
				data1.appVerStatus = appVersionStatus;//要变更的应用状态
				
				var alertMsg = "您确认更新选中数据状态？";
				
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
 * 自定义校验应用版本名称
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkAname: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAname.message = "当前应用版本名称不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAname.message = "当前应用版本名称已存在"; 
        		
                return !checkAppVerName($("#"+param[1]).val(),value);
    		}
        	
        }
    }
});

$.extend($.fn.validatebox.defaults.rules, {
    checkAVernum: {//自定义校验版本号
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length==0||value.length>10){  
        		rules.checkAVernum.message = "当前应用版本号不可为空且长度不可以超过10个字符";  
                return false;  
            }
        	else
    		{
        		rules.checkAVernum.message = "当前应用版本号已存在"; 
        		
                return !checkAppVerNum($("#"+param[1]).combobox('getValue'),value,$("#"+param[2]).val());
    		}
        	
        }
    }
});

/**
 * 校验应用下版本号唯一
 * @param appId
 * @param versionCode
 * @returns {Boolean}
 */
function checkAppVerNum(appId,versionCode,id)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.appId = appId;
	data.versionCode = versionCode;
	data.id=id;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/appversion/checkAppVersionName.action',
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




//校验应用版本名称（应用版本名称全局唯一）
function checkAppVerName(id,name)
{
	var flag = false;//当前值可用，不存在
	var data = new Object();
	
	data.id = id;
	data.name = name;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "post",
        url: contextPath+'/appversion/checkAppVersionName.action',
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
			
			uploadId = $("#urlHiddenU").val();
			
			if(null == uploadId || '' == uploadId)//若附件id为空，则生成附件id并放入值中
				{
					uploadId = createUUID();
					$("#urlHiddenU").val(uploadId);
				}
			
			uploadShowDivId="appVersionUrlU";
		}
	else
		if('add'==addorupdate)
		{
			uploadShowDivId="appVersionUrlA";
			if(''==$("#urlHiddenA").val())
				{
					uploadId = createUUID();
					$("#urlHiddenA").val(uploadId);
				}
			else
				{
					uploadId = $("#urlHiddenA").val();
				}
			
		}
	
	var url = 'uploadApkFile.jsp?uploadId='+uploadId;
	$('#'+dialogId).dialog({
	    title: '上传应用安装包文件',
	    width: 500,
	    height: 300,
	    closed: false,
	    cache: false,
	    content: '<iframe id="'+uploadId+'"src="'+url+'" width="100%" height="100%" frameborder="0" scrolling="auto" ></iframe>',  
//	    href: 'uploadFile.jsp?uploadId='+uploadId,
	    modal: true,
	    onClose:function(){
	    		initApkList(uploadId,uploadShowDivId);
	    	}
	});
	
}

/**
 * 初始化应用安装包附件
 * @param upId：附件id
 * @param listId：附件列表容器id
 */
function initApkList(upId,listId)
{
	var data = new Object();
	data.uplId = upId;
	
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/advertisement/getFileOfAppad.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	if(null!=returndata.id&&0!=returndata.id)
        		{
				  	var upload=returndata;
				  	var fileName = upload.uploadFileName;
					var realName = upload.uploadRealName;
					var filepath = upload.uploadfilepath;
					
					$("#"+listId).val(filepath+fileName);
	        	
        		}
        	
        	
      			
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/error.jsp";
        }
   });
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

