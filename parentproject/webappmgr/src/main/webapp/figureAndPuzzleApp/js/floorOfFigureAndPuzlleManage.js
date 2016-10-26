$(function()
		{
			
			initDatagrid();
			
			
			//在点击添加文章关闭按钮时（右上角的小x）触发的事件
			$("#addFloorOfFAPApp").dialog({  
			    onClose: function () {  
			    	checkDeleteFujian();
			    }  
			}); 
			
			closeDialog();//页面加载时关闭弹框
			
			bindCombobox();
			
			
		});

function addDialogOpen()
{
	$("#addFloorOfFAPApp").dialog('open');
  	initPuzzleTypeCombobox('puzzlesTypeIdA','');
  	$("#figureOrPuzzlesA").combobox('select','2');//默认选择字谜
}

function closeDialog()
{
	$('#addFloorOfFAPApp').dialog('close');
	$('#updateFloorOfFAPApp').dialog('close');
	$("#uploadShowAimgPreview").dialog('close');
}

function bindCombobox()
{
	$("#figureOrPuzzlesA").combobox({

		onSelect: function (rec) {
			
			if('2' == rec.value)
				{
					$('#zmA').show();
				}
			else
				{
					$('#zmA').hide();
				}
		}

		});
	
	$("#figureOrPuzzlesU").combobox({

		onSelect: function (rec) {
			
			if('2' == rec.value)
				{
					$('#zmU').show();
				}
			else
				{
					$('#zmU').hide();
				}
		}

		});
}

function reset()
{
	$("#floorNameC").val("");
	$("#figureOrPuzzlesC").combobox('select','');
}

//取消添加应用弹框触发方法
function addDialogCancel()
{
	 $('#addFloorOfFAPApp').dialog('close');
     $('#ff').form('clear');//清空表单内容
     $('#figureOrPuzzlesA').combobox('setValue','');
     
     checkDeleteFujian();
}

/**
 * 校验是否需要删除冗余附件数据
 */
function checkDeleteFujian()
{
	//若点击添加文章后已经上传了附件，则要将附件数据删除，若没有上传附件则可以正常退出
	var uploadId = '';//上传附件id
	if(''!=$("#floorImgA").val())
	{
		uploadId = $("#floorImgA").val();
		//调用删除方法
		deleteImgsByNewsuuid(uploadId);
	}
}


//删除图片
function deleteImgsByNewsuuid(newsUuid)
{
	
	var data = new Object();
	data.newsUuid = newsUuid;
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/fmpApp/deleteImgsByNewsuuid.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
      			console.log("删除成功");
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
	        	
	
	
}


function initDatagrid()
{
	
	var params = new Object();
	params.floorName = $("#floorNameC").val().trim();//获取模糊查询条件“应用名称”
	params.figureOrPuzzles = $("#figureOrPuzzlesC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/fmpApp/getFloorOfFAPAppList.action',//'datagrid_data1.json',
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
				{field:'floorName',title:'底板名称',width:'15%',align:'center'},
		        {field:'figureOrPuzzlesName',width:'15%',title:'图谜/字谜',align:'center'},
				{field:'createTime',title:'创建时间',width:'15%',align:'center'},
				{field:'opt',title:'操作',width:'20%',align:'center',  
			            formatter:function(value,row,index){  
			                var btn = '<a class="editcls" onclick="updateFloorOfFAPApp(&quot;'+row.id+'&quot;)" href="javascript:void(0)">编辑</a>'
			                	+'<a class="deleterole" onclick="deleteFloorOfFAPApp(&quot;'+row.id+'&quot;)" href="javascript:void(0)">删除</a>';
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

/**
 * 修改底板数据
 * @param id
 */
function updateFloorOfFAPApp(id)
{
	var url = contextPath + '/fmpApp/getDetailFloorOfFAPApp.action';
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
					floorName:data.floorName,
					figureOrPuzzles:data.figureOrPuzzles,
					puzzlesTypeId:data.puzzlesTypeId,
					floorDescription:data.floorDescription,
					startCoordinate:data.startCoordinate,//2016-10-25 ADD
					fontCss:data.fontCss,//2016-10-25 ADD
					floorImg:data.floorImg//底板图片newsUuid
					
				});
				
				$('#ffUpdate').form('enableValidation').form('validate');//因为确认密码和密码有一个比较的校验，所以在填充确认密码数据后，触发表单校验，可以正确的显示这个校验的结果
				
				initImgList(data.floorImg,'imgshowU');
				
				$("#figureOrPuzzlesNameU").val(data.figureOrPuzzlesName);
				
				if(data.figureOrPuzzles == '1')//若为图谜则隐藏字谜类型下拉框选择
					{
						$("#zmU").hide();
					}
				else
					{
						$("#zmU").show();
						initPuzzleTypeCombobox('puzzlesTypeIdU',data.puzzlesTypeId);//初始化字谜类型下拉框的值
					}
				
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
	});
	
	
	$("#updateFloorOfFAPApp").dialog('open');
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
			
			uploadId = $("#floorImgU").val();
			
			if(null == uploadId || '' == uploadId)//若附件id为空，则生成附件id并放入值中
				{
					uploadId = createUUID();
					$("#floorImgU").val(uploadId);
				}
			
			uploadShowDivId="imgshowU";
		}
	else
		if('add'==addorupdate)
		{
			uploadShowDivId="imgshowA";
			if(''==$("#floorImgA").val())
				{
					uploadId = createUUID();
					$("#floorImgA").val(uploadId);
				}
			else
				{
					uploadId = $("#floorImgA").val();
				}
			
		}
	
//	var url = 'uploadApkFile.jsp?uploadId='+uploadId;
	var url = contextPath+'/appversion/uploadMoreFAPAppImg.action?uploadId='+uploadId;//一个底板数据对应一组底板图片
	$('#'+dialogId).dialog({
	    title: '上传底板图片',
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
 * 初始化图片列表
 * @param newsUuid
 */
function initImgList(newsUuid,uploadShowDivId)
{
	var data1 = new Object();
	
	data1.newsUuid = newsUuid;
	
	var url = contextPath + '/fmpApp/getImgsByNewsuuid.action';
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
        	var imgList = data.imgList;
        	var html = '';
        	for(var i=0;i<imgList.length;i++)
        		{
        			var img = imgList[i];
        			
        			var uploadfilepath = img.uploadfilepath;//存储路径
        			var uploadRealName = img.uploadRealName;//存储真实名称
        			var imgurl = contextPath + uploadfilepath +uploadRealName;
        			var uploadFileName = img.uploadFileName;
        			
        			var deleteimg = contextPath +"/images/opicon-x.png";
        			
        			if(i!=0 && i%3==2)//3个图片一行
        				{
        					html+= '<img class="imgs" id="'+img.id+'" src='+imgurl+' style="margin-left:10px;margin-right:10px; cursor:pointer;   float: left;margin-bottom:10px;width:70px;height:50px;" title="点击查看图片大图" onclick="biggerImg(&quot;'+imgurl+'&quot;)">'+
        					'<img title="点击删除图片:'+uploadFileName+'" src='+deleteimg +' style="margin-right:10px;margin-bottom:10px;cursor:pointer;    width: 20px;height: 20px ;   float: left;"  onclick="deleteImg(&quot;'+img.id+'&quot;,&quot;'+newsUuid+'&quot;,&quot;'+uploadShowDivId+'&quot;)"><br/>'; 
            				
        				}
        			else
        				{
	        				html+= '<img class="imgs" id="'+img.id+'" src='+imgurl+' style="margin-left:10px;margin-right:10px; cursor:pointer;   float: left;margin-bottom:10px;width:70px;height:50px;" title="点击查看图片大图" onclick="biggerImg(&quot;'+imgurl+'&quot;)">'+
	    					'<img title="点击删除图片:'+uploadFileName+'" src='+deleteimg +' style="margin-right:10px;margin-bottom:10px;  cursor:pointer;  width: 20px;height: 20px  ;    float: left;"  onclick="deleteImg(&quot;'+img.id+'&quot;,&quot;'+newsUuid+'&quot;,&quot;'+uploadShowDivId+'&quot;)">'; 
        				
        				}
        			
        		}
        	
        	$("#"+uploadShowDivId).html(html);
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
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
              url: contextPath+'/fmpApp/deleteImg.action',
              data:data,
              dataType: "json",
              success: function (returndata) {
              	
              	initImgList(upId,listId);//删除成功后重新加载图片列表
            			
              	
              },
              error: function (XMLHttpRequest, textStatus, errorThrown) {
                  window.parent.location.href = contextPath + "/menu/error.action";
              }
         });
	        	
      }  
  });  
	
	
}

/**
 * 图片放大浏览
 * @param imgUrl
 */
function biggerImg(imgUrl)
{
	$("#uploadShowAimgPreview").dialog('open');
	$("#uploadShowAimgPreview").html("<img style='width:600px;height:400px;' src='"+imgUrl+"'/>");
}

/**
 * 删除底板数据
 * @param id
 */
function deleteFloorOfFAPApp(id)
{
	var url = contextPath + '/fmpApp/deleteFloorOfFAPApp.action';
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

/*
 *初始化字谜类型下拉框 
 */
function initPuzzleTypeCombobox(pComboId,value)
{
	$('#'+pComboId).combobox('clear');//清空combobox值
		
		var data = new Object();
		
		$('#'+pComboId).combobox({
				queryParams:data,
				url:contextPath+'/fmpApp/getAllPuzzlesType.action',
				valueField:'id',
				textField:'typeName',
				 onLoadSuccess: function (data1) { //数据加载完毕事件
					 if('puzzlesTypeIdU'==pComboId)
						 {
						 	$('#'+pComboId).combobox('select',value);
						 }
					 else
						 {
						 	$('#'+pComboId).combobox('select',data1[data1.length-1].id);
						 }
						
	             }
			}); 
}

/**
 * 提交添加图谜字谜专家
 */
function submitAddFloorOfFAPApp()
{
	$('#ff').form('submit',{
		url:contextPath+'/fmpApp/saveOrUpdateFloorOfFAPApp.action',
		onSubmit:function(param){
			var flag = false;
			
			var imgLength = $("#imgshowA").find("img[class='imgs']").length;
			
			if($('#ff').form('enableValidation').form('validate') )
				{
					if(imgLength == 0)
						{
							$.messager.alert('提示', "请上传底板图片!");
						}
					else
						{
							flag = true;
						}
					
				}
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#floorImgA").val("");//因为关闭弹框会触发判断是否删除无效附件数据的操作，所以在关闭弹框之前先将附件id的值置为空
	    	$("#addFloorOfFAPApp").dialog('close');//初始化添加应用弹框关闭
	    	
	    	//添加角色后刷新数据列表
	    	$('#ff').form('clear');//清空表单内容
	    	$("#figureOrPuzzlesA").combobox('setValue',"0");
	    	$("#imgshowA").html("");
	    	initDatagrid();
	    	
	    	
	    }
	});
}

function submitUpdateFloorOfFAPApp()
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/fmpApp/saveOrUpdateFloorOfFAPApp.action',
		onSubmit:function(param){
			var flag = false;
			var imgLength = $("#imgshowU").find("img[class='imgs']").length;
			if($('#ffUpdate').form('enableValidation').form('validate') )
				{
					if(imgLength == 0)
					{
						$.messager.alert('提示', "请上传底板图片!");
					}
					else
					{
						flag = true;
					}
				}
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateFloorOfFAPApp").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}
