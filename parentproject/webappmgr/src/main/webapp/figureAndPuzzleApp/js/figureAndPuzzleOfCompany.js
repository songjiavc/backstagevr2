$(function() {
	
	initDatagrid();
	closeDialog();
	
	
});



function detailZimiShow()
{
//	$("#zimiStatusDivA").show();
	$('#zmD').show();
	$("#zmFloorD").show();
	$("#floorD").show();
	$("#zimiContentD").show();
}

/**
 * 修改弹框字谜内容隐藏
 */
function detailZimiHide()
{
	$('#zmD').hide();
	$("#zmFloorD").hide();
	$("#floorD").hide();
	$("#zimiContentD").hide();
}


/**
 * 修改弹框图谜内容显示
 */
function detailTumiShow()
{
	$("#tumiDivImgD").show();
	$("#tumiDivImgShowD").show();
}

/**
 * 修改弹框图谜内容显示
 */
function detailTumiHide()
{
	$("#tumiDivImgD").hide();
	$("#tumiDivImgShowD").hide();
}

function viewZimiShow()
{
//	$("#zimiStatusDivA").show();
	$('#zmV').show();
	$("#zmFloorV").show();
	$("#floorV").show();
	$("#zimiContentV").show();
}

function viewZimiHide()
{
	$('#zmV').hide();
	$("#zmFloorV").hide();
	$("#floorV").hide();
	$("#zimiContentV").hide();
}


function viewlTumiShow()
{
	$("#tumiDivImgV").show();
	$("#tumiDivImgShowV").show();
}

function viewTumiHide()
{
	$("#tumiDivImgV").hide();
	$("#tumiDivImgShowV").hide();
}



function closeDialog()
{
	$("#wShow").dialog('close');
	$("#rejectResonShowDiv").dialog('close');
	$("#rejectResonDiv").dialog('close');
	$("#w").dialog('close');
	$("#detailFigureAndPuzzle").dialog('close');
	$("#viewFigureAndPuzzle").dialog('close');
	$("#uploadShowAimgPreview").dialog('close');
	$("#ddA").dialog('close');
}





function initDatagrid()
{
	var params = new Object();
	params.name = $("#nameC").val().trim();
	params.figureOrPuzzles = $("#figureOrPuzzlesC").combobox('getValue');
	params.lotteryType = $("#lotterytypeC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/fmpApp/getFigureAndPuzzlesList.action',//'datagrid_data1.json',
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
				{field:'name',title:'图谜字谜名称',width:'15%',align:'center'},
		        {field:'figureOrPuzzlesName',width:'7%',title:'图谜/字谜',align:'center'},
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
		        {field:'puzzlesTypeName',title:'字谜类型名称',width:'15%',align:'center'},
		        {field:'createrName',title:'发布专家',width:'15%',align:'center'},
		        {field:'statusName',title:'审批状态',width:'15%',align:'center'},
				{field:'opt',title:'操作',width:'25%',align:'center',  
			            formatter:function(value,row,index){  
			            	var status = row.status;
			            	var ordercreater = row.creater;
			            	var btn = '';
			            	if('11'==status)
			            		{
				            		btn=btn+'<a class="detailcls" onclick="detailFigureAndPuzzle(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="查看详情">详情</a>'
				                	+'<a class="rejectOrder" onclick="approveFigureAndPuzzle(&quot;'+row.id+'&quot;,3)" href="javascript:void(0)" title="审批驳回">驳回</a>'
				                	+'<a class="stopOrder" onclick="approveFigureAndPuzzle(&quot;'+row.id+'&quot;,4)" href="javascript:void(0)" title="不通过">不通过</a>'
				                	+'<a class="throughOrder" onclick="approveFigureAndPuzzle(&quot;'+row.id+'&quot;,2)" href="javascript:void(0)" title="审批通过">通过</a>';
			            		}
			            	else
			            		if('02'==status)
			            		{//审批驳回，显示驳回理由查看按钮
				            		btn=btn+'<a class="detailcls" onclick="viewFigureAndPuzzle(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="查看详情">详情</a>'
				            		+'<a class="rejectResonOrder" onclick="showRejectReason(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="驳回理由">查看驳回理由</a>';
			            		}
			            		else
				            		if('21'==status)
				            		{//审批完成，显示查看发布区域按钮
					            		btn=btn+'<a class="detailcls" onclick="viewFigureAndPuzzle(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="查看详情">详情</a>'
					            		+'<a class="deleterole" onclick="deleteFigureAndPuzzleByCompany(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="删除">删除</a>'
					            		+'<a class="showarea" onclick="showArea(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="查看发布区域">查看发布区域</a>';
				            		}
				            	else
				            		{
				            			btn='<a class="detailcls" onclick="viewFigureAndPuzzle(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="查看详情">详情</a>';//代理编辑
				            		}
			            	
		                	return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	    	$('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.detailcls').linkbutton({text:'详情',plain:true,iconCls:'icon-edit'}); 
	        $('.submitOrder').linkbutton({text:'提交',plain:true,iconCls:'icon-edit'});  
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});  
	        $('.rejectOrder').linkbutton({text:'驳回',plain:true,iconCls:'icon-remove'});  
	        $('.throughOrder').linkbutton({text:'通过',plain:true,iconCls:'icon-remove'});
	        $('.stopOrder').linkbutton({text:'不通过',plain:true,iconCls:'icon-remove'});  
	        $('.rejectResonOrder').linkbutton({text:'查看驳回理由',plain:true,iconCls:'icon-help'}); 
	        $('.showarea').linkbutton({text:'查看发布区域',plain:true,iconCls:'icon-tip'});
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="8">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}

/**
 *公司对审批完成的图谜字谜数据可以进行删除操作
 * @param id
 */
function deleteFigureAndPuzzleByCompany(id)
{
	var url = contextPath + '/fmpApp/deleteFigureAndPuzzleByCompany.action';
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


/**
 * 查看发布区域
 * @param id
 */
function showArea(id)
{
	initAreaData("treeDemoShow");
	//选中当前应用广告发布的区域
	var zTree = $.fn.zTree.getZTreeObj("treeDemoShow");
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
	$('#wShow').dialog('open');
}

/**
 * 获取当前图谜字谜的发布区域数据
 * @param id
 * @returns
 */
function checkAreas(id)
{
	var data = new Object();
	data.id = id;
	
	var area ;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: contextPath+'/fmpApp/getAreasOfFigureAndPuzzle.action',
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

/**
 * 查看驳回理由
 * @param id
 */
function showRejectReason(id)
{
	$("#rejectResonShowDiv").dialog('open');
	var url = contextPath + '/fmpApp/getDetailFigureAndPuzzles.action';
	var data1 = new Object();
	data1.id=id;
	
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
        	$("#rejectResonShow").val(data.rejectReason);
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
	});
        	
}

/**
 * 带审核的查看图谜字谜详情
 * @param id
 */
function detailFigureAndPuzzle(id)
{
	$("#detailFigureAndPuzzle").dialog('open');
	var url = contextPath + '/fmpApp/getDetailFigureAndPuzzles.action';
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
					name:data.name,
					playName:data.playName,
					figureOrPuzzles:data.figureOrPuzzles,
					puzzlesTypeId:data.puzzlesTypeId,
					figureImg:data.figureImg,//底板图片newsUuid
					zimiStatus:data.zimiStatus,
					playNum:data.playNum,
					figureOrPuzzlesName:data.figureOrPuzzlesName,
					puzzleContent:data.puzzleContent//字谜内容
					
				});
				
				if('1' == data.lotteryType)
					{
						$("#lotteryTypeNameD").val("体彩");
					}
				else
					if('2' == data.lotteryType)
					{
						$("#lotteryTypeNameD").val("福彩");
					}
				
				
				$("#figureOrPuzzlesNameD").val(data.figureOrPuzzlesName);
				
				$("#tumiD").html("");//清空图谜字谜图片显示div的内容
				if(data.figureOrPuzzles == '1')//若为图谜则隐藏字谜类型下拉框选择
					{
						initTumiOrZimiImgList(data.figureImg,'tumiD');//初始化图谜/字谜图片
						
						detailZimiHide();
						$("#zimiStatusDivD").hide();
						detailTumiShow();
					}
				else
					if(data.figureOrPuzzles == '2')
					{//字谜
						if('0' == data.zimiStatus)
							{//输入文字类型的字谜
							
								$("#zimiStatusNameD").val("输入文字");
								$("#puzzlesTypeNameD").val(data.puzzlesTypeName);
								$("#figureOrPuzzlesNameD").val(data.figureOrPuzzlesName);
								detailZimiShow();
								detailTumiHide();
								$("#zimiStatusDivD").show();
								initImgById(data.floorUploadid, 'imgshowD');
								
							}
						else
							if('1' == data.zimiStatus)
							{//上传图片类型的字谜
								$("#zimiStatusNameD").val("上传字谜图片");
								detailZimiHide();
								$("#zimiStatusDivD").show();
								detailTumiShow();
								initTumiOrZimiImgList(data.figureImg,'tumiD');//初始化图谜/字谜图片
							}
						
						
					}
				
				
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
	});
	
	
}

/**
 * 查看图谜字谜详情
 * @param id
 */
function viewFigureAndPuzzle(id)
{
	$("#viewFigureAndPuzzle").dialog('open');
	var url = contextPath + '/fmpApp/getDetailFigureAndPuzzles.action';
	var data1 = new Object();
	data1.id=id;//应用的id
	
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
				$('#ffViewDetail').form('load',{
					id:data.id,
					name:data.name,
					playName:data.playName,
					figureOrPuzzles:data.figureOrPuzzles,
					puzzlesTypeId:data.puzzlesTypeId,
					figureImg:data.figureImg,//底板图片newsUuid
					zimiStatus:data.zimiStatus,
					playNum:data.playNum,
					figureOrPuzzlesName:data.figureOrPuzzlesName,
					puzzleContent:data.puzzleContent//字谜内容
					
				});
				
				if('1' == data.lotteryType)
					{
						$("#lotteryTypeNameV").val("体彩");
					}
				else
					if('2' == data.lotteryType)
					{
						$("#lotteryTypeNameV").val("福彩");
					}
				
				
				$("#figureOrPuzzlesNameV").val(data.figureOrPuzzlesName);
				
				$("#tumiV").html("");//清空图谜字谜图片显示div的内容
				if(data.figureOrPuzzles == '1')//若为图谜则隐藏字谜类型下拉框选择
					{
						initTumiOrZimiImgList(data.figureImg,'tumiV');//初始化图谜/字谜图片
						
						viewZimiHide();
						$("#zimiStatusDivV").hide();
						viewTumiShow();
					}
				else
					if(data.figureOrPuzzles == '2')
					{//字谜
						if('0' == data.zimiStatus)
							{//输入文字类型的字谜
							
								$("#zimiStatusNameV").val("输入文字");
								$("#puzzlesTypeNameV").val(data.puzzlesTypeName);
								$("#figureOrPuzzlesNameV").val(data.figureOrPuzzlesName);
								viewZimiShow();
								viewTumiHide();
								$("#zimiStatusDivV").show();
								initImgById(data.floorUploadid, 'imgshowV');
								
							}
						else
							if('1' == data.zimiStatus)
							{//上传图片类型的字谜
								$("#zimiStatusNameV").val("上传字谜图片");
								viewZimiHide();
								$("#zimiStatusDivV").show();
								viewTumiShow();
								initTumiOrZimiImgList(data.figureImg,'tumiV');//初始化图谜/字谜图片
							}
						
						
					}
				
				
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
	});
	
	
}

function reset()
{
	$("#nameC").val("");
	$("#figureOrPuzzlesC").combobox('setValue','');
	$("#lotterytypeC").combobox('setValue','');
}

function areaSelect()
{
	
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
					 if('puzzlesTypeIdU'==pComboId && ''!=value)
						 {
						 	$('#'+pComboId).combobox('setValue',value);
						 }
					 else
						 {
						 	$('#'+pComboId).combobox('select',data1[data1.length-1].id);
						 }
						
	             }
			}); 
}


function initFloorOfFAPAppCombobox(floorComboId,value,puzzlesTypeId,puzzleTypeVal)
{
	$('#'+floorComboId).combobox('clear');//清空combobox值
		
		var data = new Object();
		if('floorOfFigureAndPuzzleU'==floorComboId && '' != puzzleTypeVal)
		 {
			data.id = puzzleTypeVal;
		 }
		else
			{
				data.id = $("#"+puzzlesTypeId).combobox('getValue');//获取当前选中的字谜类型值
			}
	
		
		
		$('#'+floorComboId).combobox({
				queryParams:data,
				url:contextPath+'/fmpApp/getFloorOfFAPAppOfPuzzlesType.action',
				valueField:'id',
				textField:'floorName',
				 onLoadSuccess: function (data1) { //数据加载完毕事件
					 if('floorOfFigureAndPuzzleU'==floorComboId && '' != puzzleTypeVal)
						 {
						 	$('#'+floorComboId).combobox('select',value);
						 }
					 else
						 {
						 	if(data1.length > 0 )
						 		{
						 			$('#'+floorComboId).combobox('select',data1[0].id);
						 			/*getPuzzletype(data1[0].id);*/
						 		}
						 	
						 	/*$('#'+floorComboId).combobox('select',data1[data1.length-1].id);
						 	alert(data1[data1.length-1].id);*/
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
			
			uploadId = $("#figureImgU").val();
			
			if(null == uploadId || '' == uploadId)//若附件id为空，则生成附件id并放入值中
				{
					uploadId = createUUID();
					$("#figureImgU").val(uploadId);
				}
			
			uploadShowDivId="tumiU";
		}
	else
		if('add'==addorupdate)
		{
			uploadShowDivId="tumiA";
			if(''==$("#figureImgA").val())
				{
					uploadId = createUUID();
					$("#figureImgA").val(uploadId);
				}
			else
				{
					uploadId = $("#figureImgA").val();
				}
			
		}
	
//	var url = 'uploadApkFile.jsp?uploadId='+uploadId;
	var url = contextPath+'/appversion/uploadFAPAppImg.action?uploadId='+uploadId;//一个图谜字谜只能有一个图谜图片
	$('#'+dialogId).dialog({
	    title: '上传图谜字谜图片',
	    width: 500,
	    height: 300,
	    closed: false,
	    cache: false,
	    content: '<iframe id="'+uploadId+'"src="'+url+'" width="100%" height="100%" frameborder="0" scrolling="auto" ></iframe>',  
//	    href: 'uploadFile.jsp?uploadId='+uploadId,
	    modal: true,
	    onClose:function(){
	    		initTumiOrZimiImgList(uploadId,uploadShowDivId);
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
        					'<input  id="radio'+img.id+'"  type="radio" name="floorUploadid"  value="'+img.id+'" style="margin-right:10px;margin-bottom:10px;cursor:pointer;    width: 15px;height: 15px ;   float: left;"/><br/>'; 
            				
        				}
        			else
        				{
        					if(i == 0)
        						{
	        						html+= '<img class="imgs" id="'+img.id+'" src='+imgurl+' style="margin-left:10px;margin-right:10px; cursor:pointer;   float: left;margin-bottom:10px;width:70px;height:50px;" title="点击查看图片大图" onclick="biggerImg(&quot;'+imgurl+'&quot;)">'+
	    	        				'<input  id="radio'+img.id+'"  type="radio" name="floorUploadid"  value="'+img.id+'" checked style="margin-right:10px;margin-bottom:10px;cursor:pointer;     width: 15px;height: 15px ;   float: left;"/>'; 
            				
        						}
        					else
        						{
	        						html+= '<img class="imgs" id="'+img.id+'" src='+imgurl+' style="margin-left:10px;margin-right:10px; cursor:pointer;   float: left;margin-bottom:10px;width:70px;height:50px;" title="点击查看图片大图" onclick="biggerImg(&quot;'+imgurl+'&quot;)">'+
	    	        				'<input  id="radio'+img.id+'"  type="radio" name="floorUploadid"  value="'+img.id+'" style="margin-right:10px;margin-bottom:10px;cursor:pointer;     width: 15px;height: 15px ;   float: left;"/>'; 
            				
        						}
	        				
        				}
        			
        		}
        	
        	$("#"+uploadShowDivId).html(html);
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
}

function initImgById(id,uploadShowDivId)
{
	var data1 = new Object();
	
	data1.id = id;
	
	var url = contextPath + '/fmpApp/getImgById.action';
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	
        	var html = '';
			var img = data.uploadfile;
			
			var uploadfilepath = img.uploadfilepath;//存储路径
			var uploadRealName = img.uploadRealName;//存储真实名称
			var imgurl = contextPath + uploadfilepath +uploadRealName;
			var uploadFileName = img.uploadFileName;
			
			
			html+= '<img class="imgs" id="'+img.id+'" src='+imgurl+' style="margin-left:10px;margin-right:10px; cursor:pointer;   float: left;margin-bottom:10px;width:70px;height:50px;" title="点击查看图片大图" onclick="biggerImg(&quot;'+imgurl+'&quot;)">'+
			'<br/>'; 
			
			
        	
        	$("#"+uploadShowDivId).html(html);
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
        }
   });
}

/**
 * 初始化图谜/字谜图片
 * @param newsUuid
 * @param uploadShowDivId
 */
function initTumiOrZimiImgList(newsUuid,uploadShowDivId)
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
        			
					html = '<img class="imgs" id="'+img.id+'" src='+imgurl+' '+
					' style="margin-left:10px;margin-right:10px; cursor:pointer;  '+
					' float: left;margin-bottom:10px;width:100px;height:70px;" title="点击查看图片大图"  '+
					' onclick="biggerImg(&quot;'+imgurl+'&quot;)">';
            				
        			
        		}
        	
        	$("#"+uploadShowDivId).html(html);
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/error.action";
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
 * 详情弹框审批
 * @param operortype
 */
function approveFigureAndPuzzlesInDialog(operortype)
{
	var fapId = $("#idD").val();
	approveFigureAndPuzzle(fapId, operortype);
	$('#detailFigureAndPuzzle').dialog('close');//关闭查看订单详情的dialog
}

/**
 * 数据列表中审核
 * @param fApId
 * @param operortype
 */
function approveFigureAndPuzzle(fApId,operortype)
{
	if(operortype == '2')
		{//审批通过选择发布区域
			initAreaData('treeDemo');
			$("#w").dialog('open');
			$("#fApIdD").val(fApId);
			$("#operortypeD").val(operortype);
		}
	else
		if(operortype == '3')
		{//审批驳回输入驳回理由
			$("#rejectResonDiv").dialog('open');
			$("#fApIdRr").val(fApId);
			$("#operortypeRr").val(operortype);
		}
		else
		{
			submitApprove(fApId,operortype,'0');
		}

}

/**
 * 提交审核
 * @param fApId
 * @param operortype
 * @param flag
 */
function submitApprove(fApId,operortype,flag)
{
	
	var data1 = new Object();
	
	data1.operortype = operortype;
	data1.fApId = fApId;
	
	var submitFlag = true;
	
	if('1' == flag)
		{
			if(operortype == '2')
				{//审批通过操作的特殊处理
					areaList = new map();
					var treeObj=$.fn.zTree.getZTreeObj("treeDemo"),
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
						$.messager.alert('提示', "请选择当前广告要发布给哪些区域!");
						submitFlag = false;
					}
					else
						{
							$("#w").dialog('close');
						}
					
					data1.areadata = JSON.stringify(areaList);
				}
			else
				if(operortype == '3')
				{//审批驳回操作的特殊处理
					data1.rejectReson = $("#rejectResonV").val();
					$("#rejectResonDiv").dialog('close');
				}
			
		}
	if(submitFlag)
		{
			var url = contextPath + '/fmpApp/approveFigureAndPuzzle.action';
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
	
}

/**
 * 审核通过后，选择区域确认触发方法
 */
var areaList = new map();
function areaSelect()
{
	var fApId = $("#fApIdD").val();
	var operortype = $("#operortypeD").val();
	submitApprove(fApId,operortype,'1');
}

/**
 * 审核驳回后，输入驳回理由完成后方法
 */
function resonFinish()
{
	var fApId = $("#fApIdRr").val();
	var operortype = $("#operortypeRr").val();
	submitApprove(fApId,operortype,'1');
}


function initAreaData(areaDataGridId)
{
	
	var data = new Object();
	
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