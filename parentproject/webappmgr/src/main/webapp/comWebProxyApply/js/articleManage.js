var idArr = [];//选中的应用id数据

$(document).ready(function(){
	
			closeDialog();//页面加载时关闭弹框
			
			
			initDatagrid();//初始化数据列表
			
		});

function addComwebArticle()
{
	$("#addArticle").dialog('open');
}



/**
 * 重置
 */
function reset()
{
	$('#titleC').textbox('setValue','');
}

function addDialogCancel()
{
	$('#addArticle').dialog('close');
    $('#ff').form('clear');//清空表单内容
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
	$("#addArticle").dialog('close');
	$("#updateArticle").dialog('close');
	$("#uploadShowAimgPreview").dialog('close');
	$("#uploadShowUimgPreview").dialog('close');
}
/**
 * 初始化文章列表
 */
function initDatagrid()
{
	
	var params = new Object();
	params.title = $("#titleC").textbox('getValue').trim();
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/article/getArticleList.action',//'datagrid_data1.json',
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
		        {field:'title',width:'30%',title:'标题',align:'center'},
				{field:'createrTime',title:'创建时间',width:'30%',align:'center'},
				{field:'opt',title:'操作',width:'30%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateArticle(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteArticle(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
			                return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="7">没有数据</td></tr>');
			}
	        
	        
	    }
	});
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
			
			uploadId = $("#imgU").val();
			
			if(null == uploadId || '' == uploadId)//若附件id为空，则生成附件id并放入值中
				{
					uploadId = createUUID();
					$("#imgU").val(uploadId);
				}
			
			uploadShowDivId="uploadShowU";
		}
	else
		if('add'==addorupdate)
		{
			uploadShowDivId="uploadShowA";
			if(''==$("#imgA").val())
				{
					uploadId = createUUID();
					$("#imgA").val(uploadId);
				}
			else
				{
					uploadId = $("#imgA").val();
				}
			
		}
	
	var url = 'uploadArticleImg.jsp?uploadId='+uploadId;
	$('#'+dialogId).dialog({
	    title: '上传文章图片',
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
        url: contextPath+'/article/getFileOfAppad.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
        	if(null!=returndata&&null!=returndata[0].id)
        		{
	        		$("#"+listId+" ul").html("");
	        		var html='';
				  	var upload=returndata;
				  	for(var i=0;i<upload.length;i++)
				  		{
				  			if(upload[0].id == 0)
				  				{
				  					break;
				  				}
				  			else
				  				{
					  				var fileName = upload[i].uploadFileName;
									var realName = upload[i].uploadRealName;
									var id = upload[i].id;
					        	
									html+='<li><a title="点击预览图片"  id="'+upload[i].id+'" class="fujian"  onclick="previewImage(&quot;'+imgId+'&quot;,&quot;'+realName+'&quot;)">'+fileName+'</a>'+
									'<a href="#" class="deletecls"  onclick="deleteImg(&quot;'+id+'&quot;,&quot;'+upId+'&quot;,&quot;'+listId+'&quot;)">'+
									'<img style="width:16px;height:16px;" src="'+ contextPath+'/images/delete.jpg"></a></li>';
						  		
				  				}
				  			
				  		}
				  	 
				  	
				  	$("#"+listId+" ul").html(html);
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
	var path = contextPath+"/uploadArticleImg/"+realName;
	$("#"+imgId).html("<img style='width:500px;height:400px;' src='"+path+"'/>");
}
/**
 *文章修改
 */
function updateArticle(id)
{
	var url = contextPath + '/article/getDetailArticle.action';
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
						content:data.content,
						img:data.img
						
					});
			
					initImgList(data.img, "uploadShowU");
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            window.parent.location.href = contextPath + "/error.jsp";
	        }
		});
		
		
		
		$("#updateArticle").dialog('open');
	
		
}

//删除图片
function deleteImg(id,upId,listId)
{
	$.messager.confirm("提示", "您确认删除选中的图片？", function (r) {  
        if (r) {  
        	var data = new Object();
        	data.id = id;
        	$.ajax({
        		async: false,   //设置为同步获取数据形式
                type: "get",
                url: contextPath+'/article/deleteImg.action',
                data:data,
                dataType: "json",
                success: function (returndata) {
                	
                	initImgList(upId,listId);//删除成功后重新加载图片列表
              			
                	
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    window.parent.location.href = contextPath + "/error.jsp";
                }
           });
	        	
        }  
    });  
	
	
}

function submitAddArticle()
{
	$('#ff').form('submit',{
		url:contextPath+'/article/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			if($('#ff').form('enableValidation').form('validate') )
				{
					flag = true;
				}
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#addArticle").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	initDatagrid();
	    	
	    	
	    }
	});
}

function submitUpdateArticle()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/article/saveOrUpdate.action',
		onSubmit:function(param){
			var flag = false;
			if($('#ffUpdate').form('enableValidation').form('validate') )
				{
					flag = true;
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateArticle").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


/**
 * 删除文章数据
 * @param id
 */
function deleteArticle(id)
{
	var url = contextPath + '/article/deleteArticles.action';
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

