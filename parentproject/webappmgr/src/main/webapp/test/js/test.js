
//测试登录接口
function checkLogin()
{
	var data = new Object();
	
	var stationCode='huludao';//传入参数,huludao
	
	data.stationCode = stationCode;
	
	var password='123123';//传入参数
	
	data.password = password;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/login.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}

/**
 * 校验更新密码接口
 */
function updatePassword()
{
	var data = new Object();
	
	var stationCode='huludao';//传入参数,huludao
	
	data.stationCode = stationCode;
	
	var password='123123';//传入参数
	
	data.password = password;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/updatePassword.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}

//测试通行证的app权限接口
function getAuthOfStationAndApp()
{
	var data = new Object();
	
	var stationId='402881eb537d70f401537d7debae000b';//传入参数，huludao402881e452ab44b60152ab45b7480000，北京：402881eb537d70f401537d7debae000b
	
	data.stationId = stationId;
	
	var appId='3a95ba8c-4955-42d9-915f-e6ff22f49a3f';//传入参数，测试23付费，测试单价免费a8797763-dfcb-4313-84e3-38c04b402a42,测试葫芦岛免费：4ddbcc15-640a-48e1-8176-1671b3c403bf
	
	data.appId = appId;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/getAuthOfStationAndApp.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}


//根据通行证获取通告数据接口
function getAnnouncementOfStation()
{
	var data = new Object();
	
	var stationId='402881eb537d70f401537d7debae000b';//传入参数
	
	data.stationId = stationId;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/getAnnouncementOfStation.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}

//根据通行证获取公司公告数据接口
function getComnoticeOfStation()
{
	var data = new Object();
	
	var stationId='402881eb537d70f401537d7debae000b';//传入参数
	
	data.stationId = stationId;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/getComnoticeOfStation.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}

//获取应用广告数据
function getAdsOfStationAndApp()
{
	var data = new Object();
	
	var stationId='402881eb537d70f401537d7debae000b';//传入参数
	
	data.stationId = stationId;
	
	var appId='40288134527c7c7b01527c8235120002';//传入参数，测试23应用
	
	data.appId = appId;
	
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/getAdsOfStationAndApp.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}

//获取应用公告数据
function getNoticesOfStationAndApp()
{
	var data = new Object();
	
	var stationId='402881eb537d70f401537d7debae000b';//传入参数
	
	data.stationId = stationId;
	
	var appId='40288134527c7c7b01527c8235120002';//传入参数
	
	data.appId = appId;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/getNoticesOfStationAndApp.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}

//获取开奖公告数据
function getKaijiangNotice()
{
	var data = new Object();
	
	var stationId='402881e452ab44b60152ab45b7480000';//传入参数:huludao:402881e452ab44b60152ab45b7480000;beijing:402881eb537d70f401537d7debae000b
	
	data.stationId = stationId;
	
	var appId='40288134527c7c7b01527c8235120002';//传入参数
	
	data.appId = appId;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/getKaijiangNoticesOfStationAndApp.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}

//获取通行证的代理数据
function getProxyOfStation()
{
	var data = new Object();
	
	var stationId='4028815c527c61fb01527c667cc70000';//传入参数，帐号11test的id
	
	data.stationId = stationId;
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/getProxyOfStation.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}


//获取通当前安装的应用的最新版本数据
function getAppversionsOfnew()
{
	var data = new Object();
	
	var appIds = new Array();
	
	appIds.push('402881e452f3784b0152f3794a740000');
	appIds.push('40288134527c7c7b01527c8235120002');
	
	data.appIds = appIds.toString();
	
	$.ajax({
		async: false,   //设置为同步获取数据形式
        type: "get",
        data:data,
        url: contextPath+'/outerInterface/getAppversionsOfnew.action',
        dataType: "json",
        success: function (data) {
        	
        	
        	
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	alert("error!");
        }
   });
}





