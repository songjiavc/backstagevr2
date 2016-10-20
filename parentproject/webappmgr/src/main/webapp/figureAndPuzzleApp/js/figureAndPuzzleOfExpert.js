var typeWordsNum = 0;//当前字谜类型可以输入的字谜字数

$(function() {
	
	
	var message = $("#message").val();//获取登录返回信息
	
	
	if("success" == message)//登录信息正确
		{
			$('#editpass').click(function() {
		         $('#w').window('open');
		     });
	
		     $('#btnEp').click(function() {
		    	 updatePassword();
		     })
	
				$('#btnCancel').click(function(){closePwd();})
				
				initLoginMes();//初始化登录人信息
		
			initDatagrid();
		}
	else
		{
			//登录失败，跳转回登录页
			window.location.href=contextPath + "/fmpApp/logout.action?alertmsg="+message;
		}
	
	bindCombobox();
	
	closeDialog();
	
	
	
});

function bindCombobox()
{
	$("#figureOrPuzzlesA").combobox({

		onSelect: function (rec) {
			
			if('2' == rec.value)
				{//字谜
					
					var val = $("input[name='zimiStatus']:checked").val();//获取选中的radio的值
					
					$("#zimiStatusDivA").show();
					if('0' == val)
						{//输入文字
							addZimiShow();
							addTumiHide();
						}
					else if('1' == val)
						{//上传字谜图片
							addZimiHide();
							addTumiShow();
						}
				}
			else
				{//图谜
					addZimiHide();
					$("#zimiStatusDivA").hide();
					addTumiShow();
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
	
	//字谜类型级联数据
	$("#puzzlesTypeIdA").combobox({

		onSelect: function (rec) {
			
			initFloorOfFAPAppCombobox('floorOfFigureAndPuzzleA','','puzzlesTypeIdA','');//初始化底板数据
			getPuzzletype(rec.id);
		}

		});
	
	
	$("#puzzlesTypeIdU").combobox({

		onSelect: function (rec) {
			
			initFloorOfFAPAppCombobox('floorOfFigureAndPuzzleU','','puzzlesTypeIdU','');//初始化底板数据
			getPuzzletype(rec.id);
		}

		});
	
	$("#floorOfFigureAndPuzzleA").combobox({

		onSelect: function (rec) {
			
			initImgList(rec.floorImg, 'imgshowA');
		}

		});
	
	$("#floorOfFigureAndPuzzleU").combobox({

		onSelect: function (rec) {
			
			initImgList(rec.floorImg, 'imgshowU');
		}

		});
	
	$("#playNameA").combobox({

		onSelect: function (rec) {
			
			initPlayNum(rec.value);
		}

		});
	
	$("#ff input[name='zimiStatus']").click(function()
			{
				var val = $("#ff input[name='zimiStatus']:checked").val();//获取选中的radio的值
				
				if('0' == val)
				{//输入文字
					addZimiShow();
					addTumiHide();
				}
				else if('1' == val)
				{//上传字谜图片
					addZimiHide();
					addTumiShow();
				}
			});
	
	$("#ffUpdate input[name='zimiStatus']").click(function()
			{
				var val = $("#ffUpdate input[name='zimiStatus']:checked").val();//获取选中的radio的值
				
				if('0' == val)
				{//输入文字
					updateZimiShow();
					updateTumiHide();
					initPuzzleTypeCombobox('puzzlesTypeIdU','');
				}
				else if('1' == val)
				{//上传字谜图片
					updateZimiHide();
					updateTumiShow();
				}
			});
	
	//在点击添加文章关闭按钮时（右上角的小x）触发的事件
	$("#addFigureAndPuzzle").dialog({  
	    onClose: function () {  
	    	checkDeleteFujian();
	    }  
	}); 
}

/**
 * 初始化当前发布到哪期的期号
 */
function initPlayNum(playName)
{
	var data = new Object();
	
	data.playName = playName;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
		type: "get",
		data:data,
		url: contextPath + '/fmpApp/getPlaynumOfPlayname.action',
		dataType: "json",
		success: function (dataresult) {
			
			$("#playNumA").val(dataresult.playNum);
			
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
   });
}

/**
 * 添加弹框字谜内容显示
 */
function addZimiShow()
{
//	$("#zimiStatusDivA").show();
	$('#zmA').show();
	$("#zmFloorA").show();
	$("#floorA").show();
	$("#zimiContentA").show();
}

/**
 * 添加弹框字谜内容隐藏
 */
function addZimiHide()
{
	$('#zmA').hide();
	$("#zmFloorA").hide();
	$("#floorA").hide();
	$("#zimiContentA").hide();
}


/**
 * 添加弹框图谜内容显示
 */
function addTumiShow()
{
	$("#tumiDivImg").show();
	$("#tumiDivImgShow").show();
}

/**
 * 添加弹框图谜内容显示
 */
function addTumiHide()
{
	$("#tumiDivImg").hide();
	$("#tumiDivImgShow").hide();
}

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


/**
 * 修改弹框字谜内容显示
 */
function updateZimiShow()
{
//	$("#zimiStatusDivA").show();
	$('#zmU').show();
	$("#zmFloorU").show();
	$("#floorU").show();
	$("#zimiContentU").show();
}

/**
 * 修改弹框字谜内容隐藏
 */
function updateZimiHide()
{
	$('#zmU').hide();
	$("#zmFloorU").hide();
	$("#floorU").hide();
	$("#zimiContentU").hide();
}


/**
 * 修改弹框图谜内容显示
 */
function updateTumiShow()
{
	$("#tumiDivImgU").show();
	$("#tumiDivImgShowU").show();
}

/**
 * 修改弹框图谜内容显示
 */
function updateTumiHide()
{
	$("#tumiDivImgU").hide();
	$("#tumiDivImgShowU").hide();
}

//修改密码提交内容
function updatePassword()
{
	$('#updatePasswordForm').form('submit',{
		onSubmit:function(param){
			return $('#updatePasswordForm').form('validate');
		},
		success:function(data){
			$.messager.alert('提示', eval("(" + data + ")").message);
			$('#updatePasswordForm').form('clear');
			$("#w").dialog('close');
			window.location.href=contextPath + '/fmpApp/logout.action?alertmsg='+'';
		}
	});
}

/**
 * 初始化登陆人信息
 */
function initLoginMes()
{
	$.ajax({
		async: false,   //设置为同步获取数据形式
		type: "post",
		url: contextPath + '/fmpApp/getLoginExpertmsg.action',
//		data:data,
		dataType: "json",
		success: function (dataresult) {
			var username = dataresult.message;
		
			$("#loginuser").html(username);
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
   });
}

//安全退出
function logout()
{
	$.messager.confirm('系统提示','您确认退出吗?',function(r){
	    if (r){
	    	var message = '3';
	    	window.location.href=contextPath + '/fmpApp/logout.action?alertmsg='+message;
	    }
	});
	
}

function closeDialog()
{
	$("#rejectResonDiv").dialog('close');
	$("#detailFigureAndPuzzle").dialog('close');
	$("#addFigureAndPuzzle").dialog('close');
	$("#updateFigureAndPuzzle").dialog('close');
	$('#w').window('close');
	$("#uploadShowAimgPreview").dialog('close');
	$("#ddA").dialog('close');
	
}


function addDialogOpen()
{
	$("#addFigureAndPuzzle").dialog('open');
  	initPuzzleTypeCombobox('puzzlesTypeIdA','');
  	$("#figureOrPuzzlesA").combobox('select','2');//默认选择字谜
  	//默认选择输入文字
  	$("#zimiStatusDivA").show();
	addZimiShow();
	addTumiHide();
  	initFloorOfFAPAppCombobox('floorOfFigureAndPuzzleA','','puzzlesTypeIdA','');//初始化底板数据
  	$("#playNameA").combobox("setValue","3D");
  	var playName = "3D";
  	initPlayNum(playName);
}


//取消添加弹框触发方法
function addDialogCancel()
{
	 $('#addFigureAndPuzzle').dialog('close');
     $('#ff').form('clear');//清空表单内容
   	 $("#figureOrPuzzlesA").combobox('select','2');//默认选择字谜
   	 
     
     checkDeleteFujian();
}

/**
 * 获取当前字谜类型可以输入的最多字数
 * @param id
 */
function getPuzzletype(id)
{
	var data1 = new Object();
	
	data1.id = id;
	
	var url = contextPath + '/fmpApp/getDetailPuzzlesType.action';
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        url: url,
        data:data1,
        dataType: "json",
        success: function (data) {
        	typeWordsNum = data.typeWordsNum;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	window.parent.location.href = contextPath + "/menu/errorExpert.action";
        }
   });
}


/**
 * 校验是否需要删除冗余附件数据
 */
function checkDeleteFujian()
{
	//若点击添加文章后已经上传了附件，则要将附件数据删除，若没有上传附件则可以正常退出
	var uploadId = '';//上传附件id
	if(''!=$("#figureImgA").val())
	{
		uploadId = $("#figureImgA").val();
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
        url: contextPath+'/article/deleteImgsByNewsuuid.action',
        data:data,
        dataType: "json",
        success: function (returndata) {
        	
      			console.log("删除成功");
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/errorExpert.action";
        }
   });
	        	
	
	
}


function initDatagrid()
{
	var params = new Object();
	params.name = $("#nameC").val().trim();
	params.figureOrPuzzles = $("#figureOrPuzzlesC").combobox('getValue');
	params.lotterytype = $("#lotterytypeC").combobox('getValue');
	
	$('#datagrid').datagrid({
		singleSelect:false,
		rownumbers:false,
		queryParams: params,
		url:contextPath + '/fmpApp/getFigureAndPuzzlesListOfExpert.action',//'datagrid_data1.json',
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
		        {field:'figureOrPuzzlesName',width:'15%',title:'图谜/字谜',align:'center'},
		        {field:'lotteryType',width:'10%',title:'彩种',align:'center',  
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
		        {field:'statusName',title:'审批状态',width:'15%',align:'center'},
				{field:'opt',title:'操作',width:'20%',align:'center',  
			            formatter:function(value,row,index){  
			            	var status = row.status;
			            	var ordercreater = row.creater;
			            	var btn = '';
			            	if('01'==status||'02'==status)
			            		{
				            		btn='<a class="editcls" onclick="updateFigureAndPuzzle(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="编辑">编辑</a>'//代理编辑
				                	+'<a class="deleterole" onclick="deleteFigureAndPuzzle(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="删除">删除</a>'//代理删除
				                	+'<a class="submitFigureAndPuzzle" onclick="approveFigureAndPuzzle(&quot;'+row.id+'&quot;,1)" href="javascript:void(0)" title="提交">提交</a>'//代理提交
				                	
				                	if('02'==status)
			                		{
			                			btn=btn+'<a class="rejectOrder" onclick="showRejectReason(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="驳回理由">查看驳回理由</a>';
			                		}
			            		}
			            	else
			            		{
			            			btn='<a class="detailcls" onclick="detailFigureAndPuzzle(&quot;'+row.id+'&quot;)" href="javascript:void(0)" title="详情">详情</a>'//代理编辑
			            		}
			            	
		                	return btn;  
			            }  
			        }  
		    ]],  
	    onLoadSuccess:function(data){  
	    	$('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'}); 
	        $('.detailcls').linkbutton({text:'详情',plain:true,iconCls:'icon-edit'}); 
	        $('.submitFigureAndPuzzle').linkbutton({text:'提交',plain:true,iconCls:'icon-edit'});  
	        $('.deleterole').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});   
	        $('.rejectOrder').linkbutton({text:'查看驳回理由',plain:true,iconCls:'icon-help'});
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="7">没有数据</td></tr>');
			}
	        
	        
	    }
	});
}

/**
 * 查看驳回理由
 * @param id
 */
function showRejectReason(id)
{
	$("#rejectResonV").val("");
	$("#rejectResonDiv").dialog('open');
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
        	
        	$("#rejectResonV").val(data.rejectReason);
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/errorExpert.action";
        }
	});
        	
}

/**
 * 查看图谜字谜详情
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
            window.parent.location.href = contextPath + "/menu/errorExpert.action";
        }
	});
	
	
}

function reset()
{
	$("#nameC").val("");
	$("#figureOrPuzzlesC").combobox('setValue','');
	$("#lotterytypeC").combobox('setValue','');
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
 * 
 * 修改图谜字谜
 * @param id
 */
function updateFigureAndPuzzle(id)
{
	$("#updateFigureAndPuzzle").dialog('open');
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
        	
				$('#ffUpdate').form('load',{
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
						$("#lotteryTypeNameU").val("体彩");
					}
				else
					if('2' == data.lotteryType)
					{
						$("#lotteryTypeNameU").val("福彩");
					}
				
				
				$("#figureOrPuzzlesNameU").val(data.figureOrPuzzlesName);
				
				$("#tumiU").html("");//清空图谜字谜图片显示div的内容
				if(data.figureOrPuzzles == '1')//若为图谜则隐藏字谜类型下拉框选择
					{
						initTumiOrZimiImgList(data.figureImg,'tumiU');//初始化图谜/字谜图片
						
						updateZimiHide();
						$("#zimiStatusDivU").hide();
						updateTumiShow();
					}
				else
					if(data.figureOrPuzzles == '2')
					{//字谜
						if('0' == data.zimiStatus)
							{//输入文字类型的字谜
								updateZimiShow();
								updateTumiHide();
								$("#zimiStatusDivU").show();
								initPuzzleTypeCombobox('puzzlesTypeIdU',data.puzzlesTypeId);//初始化字谜类型下拉框的值
								getPuzzletype(data.puzzlesTypeId);
								//初始化字谜底板选中值
								initFloorOfFAPAppCombobox('floorOfFigureAndPuzzleU',data.floorOfFigureAndPuzzlesId,'puzzlesTypeIdU',data.puzzlesTypeId);//初始化底板数据
//								$('#floorOfFigureAndPuzzleU').combobox('select',data.floorOfFigureAndPuzzlesId);
								$('#ffUpdate').form('enableValidation').form('validate');
								
								setTimeout(function()
								{
									//选中底板图片
									var radioId = 'radio'+data.floorUploadid;
									$("#"+radioId).attr("checked","checked");
										}, 100);
							}
						else
							if('1' == data.zimiStatus)
							{//上传图片类型的字谜
								updateZimiHide();
								$("#zimiStatusDivU").show();
								updateTumiShow();
								initTumiOrZimiImgList(data.figureImg,'tumiU');//初始化图谜/字谜图片
							}
						
						
					}
				
				
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.parent.location.href = contextPath + "/menu/errorExpert.action";
        }
	});
	
}


function deleteFigureAndPuzzle(id)
{
	var url = contextPath + '/fmpApp/deleteFigureAndPuzzle.action';
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
		                    window.parent.location.href = contextPath + "/menu/errorExpert.action";
		                }
		           });
		        	
	        }  
	    });  
	}
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
            window.parent.location.href = contextPath + "/menu/errorExpert.action";
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
            window.parent.location.href = contextPath + "/menu/errorExpert.action";
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
            window.parent.location.href = contextPath + "/menu/errorExpert.action";
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

function approveFigureAndPuzzle(fApId,operortype)
{
	var data1 = new Object();
	
	data1.operortype = operortype;
	data1.fApId = fApId;
	
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
        	window.parent.location.href = contextPath + "/menu/errorExpert.action";
        }
   });
}

function submitAddFigureAndPuzzle(operatype)
{
	$('#ff').form('submit',{
		url:contextPath+'/fmpApp/saveOrUpdateFigureAndPuzzles.action',
		onSubmit:function(param){
			var flag = false;
			
			if($('#ff').form('enableValidation').form('validate'))
				{
					flag = true;
				}
			param.operatype = operatype;//0:保存 1：保存并提交
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	$("#figureImgA").val("");
	    	$('#ff').form('clear');//清空表单内容
	    	
	    	$("#lotteryTypeA").combobox('setValue','1');
	    	$("#figureOrPuzzlesA").combobox('select','2');//默认选中字谜
	    	
	    	$('#ff [name="zimiStatus"]:radio').each(function() {   //设置“待上架”为默认选中radio
	            if (this.value == '0'){   
	               this.checked = true;   
	               addZimiShow();
					addTumiHide();
	            }       
	         }); 
	    	
	    	
	    	$("#addFigureAndPuzzle").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}


function submitUpdateFigureAndPuzzle(operatype)
{
	$('#ffUpdate').form('submit',{
		url:contextPath+'/fmpApp/saveOrUpdateFigureAndPuzzles.action',
		onSubmit:function(param){
			var flag = false;
			
			if($('#ffUpdate').form('enableValidation').form('validate'))
				{
					flag = true;
				}
			param.operatype = operatype;//0:保存 1：保存并提交
			
			return flag;
		},
	    success:function(data){
	    	//提交表单后，从后台返回的data类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
	    	$.messager.alert('提示', eval("(" + data + ")").message);
	    	
	    	$("#updateFigureAndPuzzle").dialog('close');
	    	//修改角色后刷新数据列表
	    	initDatagrid();
	    	
	    }
	});
}

/**
 * 自定义校验
 */
$.extend($.fn.validatebox.defaults.rules, {
	checkPuzzleContent: {//自定义校验name
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	if(value.length>typeWordsNum){  
        		rules.checkPuzzleContent.message = "当前字谜内容不可为空且长度不可以超过"+typeWordsNum+"个字符";  
                return false;  
            }
        	else
        		{
        			return true;
        		}
        	
        }
    }
});
