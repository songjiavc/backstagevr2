
$(document).ready(
		function()
		{
			closeDialog();
			initQueryProvince();//初始化模糊查询省数据]
			initSearchFormAgent(initParam);
			
			bindCombobox();
		}
);


function bindCombobox()
{
	
	//切换彩种时要清空登录账号和站点号的数据
	$("#addFormStationStyle").combobox({

		onSelect: function (rec) {
			$("#addFormStationNumber").val("");//清空站点号
			$("#addOrUpdateStationForm").form('validate');//清空站点号后校验，提示要重新输入站点号
			$("#addFormStationCode").val("");//清空生成的登录账号
		}

		}); 
}

/**
 * 初始化模糊查询“省”下拉框数据
 */
function initQueryProvince()
{
	$('#searchFormProvince').combobox('clear');//清空combobox值
	var param = new Object();
	param.isHasall = true;//包含"全部"
	$('#searchFormProvince').combobox({
			queryParams:param,
			url:contextPath+'/product/getProvinceList.action',
			valueField:'pcode',
			textField:'pname',
			onLoadSuccess: function (data) { //数据加载完毕事件
				$('#searchFormProvince').combobox('select',data[data.length-1].pcode);
            },
            onSelect: function(rec){
            	var url = contextPath+'/product/getCityList.action?pcode='+rec.pcode;
		        $('#searchFormCity').combobox('reload', url);
		    }
		});
	$('#searchFormCity').combobox({
		valueField:'ccode',
		queryParams:param,
		textField:'cname',
		onLoadSuccess: function (data) { //数据加载完毕事件
			$('#searchFormCity').combobox('select',data[data.length-1].ccode);
        }
	}); 
}

/**
 * 初始化省数据
 * @param addOrUpdate:标记当前是添加表单操作还是修改表单的操作,值为"add" 和"update"
 * @param provinceId:当前操作的省份combobox标签的id
 * @param pcode:应该选中的省份的code
 */
function initProvince(addOrUpdate,pcode,oldccode,oldacode)
{
	$('#addFormProvince').combobox('clear');//清空combobox值
	$('#addFormProvince').combobox({
			queryParams:{
				isHasall : false
			},
			url:contextPath+'/product/getProvinceList.action',
			valueField:'pcode',
			textField:'pname',
			 onLoadSuccess: function (data) { //数据加载完毕事件
                 if (data.length > 0 && "add" == addOrUpdate) 
                 {
                	 $("#addFormProvince").combobox('select',data[0].pcode);
                 }
                 else
            	 {
                	//使用“setValue”设置选中值不会触发绑定事件导致多次加载市级数据，否则会多次触发产生错误
                	 $("#addFormProvince").combobox('select', pcode);
            	 }
             },
             onSelect: function(rec){
            	 if(rec.pcode!=pcode){
            		 oldccode="";
            		 oldacode="";
            	 }
            	 initAddFormCity(addOrUpdate,rec.pcode,oldccode,oldacode);
            	 
            	 generateStationCode();//调用生成登录账号方法
 		    }
		});
}

function initAddFormCity(addOrUpdate,pcode,oldccode,oldacode){
	//初始化城市combo
	$('#addFormCity').combobox({
		url : contextPath+'/product/getCityList.action',
		queryParams:{
			isHasall : false,    //不包含"全部",
			pcode : pcode
		},
		valueField:'ccode',
		textField:'cname',
		 onLoadSuccess: function (data) { //数据加载完毕事件
             if (data.length > 0 && "add" == addOrUpdate) 
             {
            	 $("#addFormCity").combobox('select',data[0].ccode);
             }
             else
        	 {
            	 if(data.length > 0 &&"update" == addOrUpdate&&"" == oldccode)
            		 {//在修改表单中级联加载市级数据时也要默认选中全部
            		 $("#addFormCity").combobox('select',data[data.length-1].ccode);
            		 }
            	 else
            		 {//当修改产品初始化市级数据时设置选中当前数据值
            		 	$("#addFormCity").combobox('select', oldccode);
            		 }
        	 }
         },
         onSelect: function(rec){
        	 if(rec.ccode!=oldccode){
        		 oldacode="";
        	 }
        	 initAddFormRegion(addOrUpdate,rec.ccode,oldacode);
        	 
        	 generateStationCode();//调用生成登录账号方法
		    }
	}); 
}

function initAddFormRegion(addOrUpdate,ccode,oldacode){
	
	//初始化乡镇区combo
	$('#addFormRegion').combobox({
		url :  contextPath+'/product/getRegionList.action',
		queryParams:{
			isHasall : false,    //不包含"全部",
			ccode : ccode
		},
		valueField:'acode',
		textField:'aname',
		 onLoadSuccess: function (data) { //数据加载完毕事件
             if (data.length > 0 && "add" == addOrUpdate) 
             {
            	 $("#addFormRegion").combobox('select',data[0].acode);
             }
             else
        	 {
            	 if(data.length > 0 &&"update" == addOrUpdate&&"" == oldacode)
            		 {
            		 	$("#addFormRegion").combobox('select',data[0].acode);
            		 }
            	 else
            		 {
            		 	$("#addFormRegion").combobox('select', oldacode);
            		 }
        	 }
         }
	}); 
}

	/**
	 * add by songj@sdfcp.com
	 * date 2015-11-27 
	 * desc 初始化上级代理列表
	 */
	function initSearchFormAgent(initParam){
		$('#searchFormAgent').combobox('clear');//清空combobox值
		$('#searchFormAgent').combobox({
			queryParams:{
				isHasall : true 
			},//暂时没有任何需要查询的条件
			url:contextPath+'/agent/getScdlList.action',
			valueField : 'id',
			textField : 'name',
			onLoadSuccess: function (data) { //数据加载完毕事件
				if (initParam.flag == 'false')
	             {
	            	 $("#searchFormAgent").combobox('select',data[0].id);
	             }
	             else
	        	 {
	            	//使用“setValue”设置选中值不会触发绑定事件导致多次加载市级数据，否则会多次触发产生错误
	            	 $("#searchFormAgent").combobox('setValue', initParam.agentId);
	            	 $("#searchFormAgent").combobox('readonly');
	        	 }
				 initDatagrid();
	         }
		});
	}
	
	/**
	 * add by songj@sdfcp.com
	 * date 2015-11-27 
	 * desc 初始化上级代理列表
	 */
	function initAddFormAgent(initParam,addFormAgent){
		$('#addFormAgent').combobox('clear');//清空combobox值
		$('#addFormAgent').combobox({
			url:contextPath+'/agent/getScdlList.action',
			valueField : 'id',
			textField : 'name',
			onLoadSuccess: function (data) { //数据加载完毕事件
				if (initParam.flag == 'false')
	             {
	            	if(addFormAgent == undefined){
	            		$("#addFormAgent").combobox('select',data[0].id);
	            	}
	            	else{
	            		$("#addFormAgent").combobox('select',addFormAgent);
	            	}
	             }
	             else
	        	 {
	            	//使用“setValue”设置选中值不会触发绑定事件导致多次加载市级数据，否则会多次触发产生错误
	            	 $("#addFormAgent").combobox('setValue', initParam.agentId);
	            	 $("#addFormAgent").combobox('readonly');
	        	 }
	         }
		});
	}


/*
 * 	@desc	 
 */
function initDatagrid()
{
	//获取查询form中所有值 
	var queryParams = {
			searchFormNumber : $('#searchFormNumber').val(),
			searchFormStyle : $('#searchFormStyle').combobox('getValue'),
			searchFormProvince : $('#searchFormProvince').combobox('getValue'),
			searchFormCity : $('#searchFormCity').combobox('getValue'),
			searchFormName : $('#searchFormName').val(),
			searchFormTelephone : $('#searchFormTelephone').val(),
			searchFormAgent : $('#searchFormAgent').combobox('getValue'),
			searchEndtime:$('#searchEndtime').combobox('getValue')
	};
	//渲染列表
	$('#stationDataGrid').datagrid({
		singleSelect:false,
		queryParams: queryParams,
		url: contextPath + '/station/getStationList.action',
		method:'get',
		border:false,
		fit:true,
		//fitColumns:true,
		pagination:true,
		pageSize:10,
		striped:true,
		columns:[[
				{field:'id',checkbox:true},
				{field:'stationCode',title:'登录帐号',width:'10%',align:'center'},
				{field:'stationNumber',title:'站点号',width:'10%',align:'center'},
				{field:'province',title:'省',width:'10%',align:'center'},
				{field:'city',title:'市',width:'10%',align:'center'},
				{field:'stationStyle',title:'站点类型',width:'10%',align:'center'},
				{field:'name',title:'站主名称',width:'10%',align:'center'},
				{field:'telephone',title:'站主电话',width:'10%',align:'center'},
//				{field:'stationCode',title:'登录帐号',width:'10%',align:'center'},
				{field:'createTime',title:'录入时间',width:'10%',align:'center'},
				{field:'opt',title:'操作',width:'130',align:'center', 
		            formatter:function(value,row,index){
		                var btn = '<a class="editcls" onclick="updateStation(&quot;'+row.id+'&quot;)" href="javascript:void(0)"></a>'
		                	+'<a class="delcls" onclick="delStationById(&quot;'+row.id+'&quot;)" href="javascript:void(0)"></a>'
		                	+'<a class="connectApp" onclick="connectApps(&quot;'+row.id+'&quot;,&quot;'+row.provinceCode+'&quot;,&quot;'+row.cityCode+'&quot;)" href="javascript:void(0)"></a>';
//		                	+'<a class="setOrder" onclick="setOrder(&quot;'+row.id+'&quot;,&quot;'+row.stationNumber+'&quot;,&quot;'+row.stationStyle+'&quot;)" href="javascript:void(0)"></a>';
		                return btn;
		            }
		        }
		    ]],
	    onLoadSuccess:function(data){  
	        $('.editcls').linkbutton({plain:true,iconCls:'icon-edit'});
	        $('.delcls').linkbutton({plain:true,iconCls:'icon-remove'});
	        $('.connectApp').linkbutton({plain:true,iconCls:'icon-filter'});
//	        $('.setOrder').linkbutton({plain:true,iconCls:'icon-search'});
	        
	        if(data.rows.length==0){
				var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+body.width()+'" style="height: 25px; text-align: center;" colspan="10">没有数据</td></tr>');
			}
	    }  
	});
}


	//关闭弹框
	function closeDialog()
	{	
		$("#setOrder").dialog('close');
		$("#addOrUpdateStation").dialog('close');
		$("#w").dialog('close');
	}

	/**
	 * 权限修改
	 */
	function updateStation(id)
	{
			/**
			 * 站点修改
			 */
			$('.panel-title.panel-with-icon').html('修改站点信息');
			$('#addFormStationCode').attr('readonly','readonly');
			var url = contextPath + '/station/getStationDetail.action';
			var paramData = new Object();
			paramData.id=id;
			$.ajax({
				async: false,   //设置为同步获取数据形式
		        type: "get",
		        cache:false,
		        url: url,
		        data:paramData,
		        dataType: "json",
		        success: function (data) {
					$('#addOrUpdateStationForm').form('load',data);
					initProvince('update',data.addFormProvince,data.addFormCity,data.addFormRegion);
					$('#addFormStationCode').attr("readonly", true);
					initAddFormAgent(initParam,data.addFormAgent);
		        },
		        error: function (XMLHttpRequest, textStatus, errorThrown) {
		            alert(errorThrown);
		        }
			});
			$("#addOrUpdateStation").dialog("open");//打开修改用户弹框
	}

	//提交添加权限form表单
	function submitAddStation()
	{
		$('#addOrUpdateStationForm').form('submit',{
			url:contextPath+'/station/saveOrUpdate.action',
			onSubmit:function(param){
				return $('#addOrUpdateStationForm').form('validate');
			},
			success:function(data){
				$.messager.alert('提示', eval("(" + data + ")").message);
				closeDialog();
	        	initDatagrid();
			}
		});
	}
	
	//修改帐号form表单
	function submitUpdatestation()
	{
		$('#addOrUpdateStationForm').form('submit',{
			url:contextPath+'/station/saveOrUpdate.action',
			onSubmit:function(param){
				return $('#updatestationForm').form('enableValidation').form('validate');
			},
		    success:function(data){
		    	//data从后台返回后的类型为String，要获取信息需要将其转换为json类型，使用eval("(" + data + ")")方法转换
		    	$.messager.alert('提示', eval("(" + data + ")").message);
		    	$("#updatestation").dialog('close');//初始化修改权限弹框关闭
		    	//在修改权限后刷新权限数据列表
		    	initDatagrid();
		    	$('#updatestationForm').form('clear');
		    }
		});
	}
	
	/**
	 * 校验code,name唯一性
	 */
	function checkCode(id,code)
	{
		var flag = false;//当前值可用，不存在
		var paramData = {
				id : id,
				code : code
		};
		$.ajax({
			async: false,   //设置为同步获取数据形式
	        type: "post",
	        url: contextPath+'/station/checkCode.action',
	        data:paramData,
	        dataType: "json",
	        success: function (data) {
	        	if(data.exist)//若data.isExist==true,则当前校验值已存在，则不可用使用
	        		{
	        			flag = true;
	        		}
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            alert(errorThrown);
	        }
	   });
		return flag;
	}
/**
 * 删除单个记录
 */
	function delStationById(id){
		var url = contextPath + '/station/deleteStationByIds.action';
		$.messager.confirm(" ", "您确认删除选中数据？", function (r) {  
	        if (r) {  
		        	$.ajax({
		        		async: false,   //设置为同步获取数据形式
		                type: "post",
		                url: url,
		                data:{
		                	ids : id
		                },
		                dataType: "json",
		                success: function (data) {
		                	initDatagrid('');
		                	$.messager.alert('提示', data.message);
		                },
		                error: function (XMLHttpRequest, textStatus, errorThrown) {
		                    alert(errorThrown);
		                }
		           });
	        	}  
	    	});  
		}
	/**
	 * 
	 * 批量删除
	 * @param code
	 */
	function deleteStationByIds()
	{
		var url = contextPath + '/station/deleteStationByIds.action';
		var paramObj = new Object();
		
		var idArr = new Array();
		var rows = $('#stationDataGrid').datagrid('getSelections');
		var deleteFlag = true;
		
		for(var i=0; i<rows.length; i++)
		{
			idArr.push(rows[i].id);//code
		}
		
		if(idArr.length>0)
		{
			paramObj.ids=idArr.toString();	//将id数组转换为String传递到后台
			
			$.messager.confirm(" ", "您确认删除选中数据？", function (r) {  
		        if (r) {  
			        	$.ajax({
			        		async: false,   //设置为同步获取数据形式
			                type: "post",
			                url: url,
			                data:paramObj,
			                dataType: "json",
			                success: function (data) {
			                	initDatagrid('');
			                	$.messager.alert('提示', data.message);
			                },
			                error: function (XMLHttpRequest, textStatus, errorThrown) {
			                    alert(errorThrown);
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

/**
 * 自定义校验code
 */
$.extend($.fn.validatebox.defaults.rules, {
    checkCodes: {//自定义校验code
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
    		rules.checkCodes.message = "站点编码已存在"; 
            return !checkCode($("#"+param[1]).val(),value,'');
        }
    },
    equalTo: { 
    	validator: function (value, param) { 
    		return $(param[0]).val() == value;
    		}, message: '两次密码输入不一致！' },
	checkSNum: 
	{//自定义校验站点号（福彩5位数字，体彩8位数字）
        validator: function(value,param){
        	var rules = $.fn.validatebox.defaults.rules;  
        	
        	var message ="";
        	var validateFlag = true;//校验结果标记
        	var numberFlag = /^[0-9]*$/.test(value);//校验是否为数字
        	//获取当前彩种（根据是体彩还是福彩确定站点号的校验规则）。都是数字；体彩5位，福彩8位(1:体彩 2：福彩)
        	var stationStyle = $("#addFormStationStyle").combobox('getValue');
        	var sNumLen = 0;//站点号长度
        	if("1" == stationStyle)
    		{//体彩
    			sNumLen = 5;//体彩站点号8位数字
    			
    		}
        	else if("2" == stationStyle)
    		{//福彩
    			sNumLen = 8;//福彩站点号5位
    			
    		}
        	
        	if(numberFlag)
        		{
	        		
	            	var sNum = value.length;//输入的站点号的长度
	            	
	            	if(sNumLen != sNum)
	            		{
		            		message = "站点号必须是"+sNumLen+"位数字";
		        			validateFlag = false;
	            		}
    	        	
        		}
        	else
        		{
        			message = "站点号必须是数字";
        			validateFlag = false;
        		}
        	
    		rules.checkSNum.message = message; 
            return validateFlag;
        }
    }
});

function trimAll(str) {
	  return str.replace(/(^\s+)|(\s+$)/g, "");
	}

//根据彩种，省份和站点号生成登录账号
function generateStationCode()
{
	var gerFlag = true;//是否生成登录账号flag
	
	//获取当前彩种(1:体彩 2：福彩)
	var sNum = $("#addFormStationNumber").val().trim();
	var numberFlag = /^[0-9]*$/.test(sNum);//校验是否为数字
	var stationStyle = $("#addFormStationStyle").combobox('getValue');
	
	
	if(null == sNum  ||sNum.length == "" || !numberFlag)
		{
			gerFlag = false;
		}
	
	if(gerFlag)
		{//可以生成登录账号
			var stationCode = '';
			if("1" == stationStyle)
			{//体彩（城市编码截取前4位+站点号）
				var cityCode = $("#addFormCity").combobox('getValue');
				stationCode = cityCode.substring(0,4)+sNum;//截取城市编码前4位和站点号组合
			}
			else if("2" == stationStyle)
			{//福彩（站点号）
				stationCode = sNum;
			}
			
			$("#addFormStationCode").val(stationCode);//放置生成的登录账号
		}
	
	
	
	
}


