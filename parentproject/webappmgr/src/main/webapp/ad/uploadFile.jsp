<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!--<c:set var="ctx" value="${pageContext.request.contextPath}"/>-->
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <title>upload</title>
 
    <head>
       <link rel="stylesheet" href="js/jquery.uploadify-v2.1.0/uploadify.css" type="text/css"></link>
       <jsp:include page="../common/top.jsp" flush="true" /> 
       <script type="text/javascript" src="js/jquery.uploadify-v2.1.0/jquery-1.3.2.min.js"></script>
       <script type="text/javascript" src="js/jquery.uploadify-v2.1.0/jquery.uploadify.v2.1.0.min.js"></script>
       <script type="text/javascript" src="js/jquery.uploadify-v2.1.0/swfobject.js"></script>
       
        <script type="text/javascript">
           var  proId="";
          var uploadId="";
        $(document).ready(
        		function()
        		{
        			uploadId=getQueryString("uploadId");//附件id
        		}
        );
         var realname="";//存储的名称
         var filename="";//附件真实名称
         
         	//从一个页面获取另一个页面的url
		function getQueryString(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg);
			if (r != null) 
				return unescape(r[2]); 
			return null;
		}
         	
       $(function(){
 
          
           var num = 0;//选择的文件个数
            $('#file').uploadify({
             'uploader'       : 'js/jquery.uploadify-v2.1.0/uploadify.swf',//uploadify.swf 文件的相对路径，该swf文件是一个带有文字BROWSE的按钮，点击后淡出打开文件对话框，默认值：uploadify.swf
             'script'          : '../servlet/Upload',//后台处理程序的相对路径 。默认值：uploadify.php 
             'cancelImg'      : 'js/jquery.uploadify-v2.1.0/cancel.png',//
             //'checkScript' ：false,//用来判断上传选择的文件在服务器是否存在的后台处理程序的相对路径 
             'fileDataName' : 'file',//设置一个名字，在服务器处理程序中根据该名字来取上传文件的数据。默认为Filedata 
             'folder'          : 'WEB-INF',//上传文件存放的目录
             'fileExt'        : '*.png;*.JPG;*.jpg;*.PNG;*.gif;',//设置可以选择的文件的类型，格式如：'*.doc;*.pdf;*.rar' 
             'fileDesc'       : 'Web Office Files (.jpg, .png,.JPG, .gif,.PNG)',//这个属性值必须设置fileExt属性后才有效，用来设置选择文件对话框中的提示文本，如设置fileDesc为“请选择rar doc pdf文件”
              'queueID'         : 'fileQueue',//文件队列的ID，该ID与存放文件队列的div的ID一致
              'removeCompleted' : false,//上传完成后，是否清除选择的文件
              //'height'        : 26,//浏览按钮的高调
              //'width'         : 44,//浏览按钮的宽度
              //'buttonImg'     :'${ctx}/js/uploadify-v2.1.4/button.png',//浏览按钮的图片地址
              'buttonText'      : "Browse",//按钮的文本
              'multi'           : true,//是否允许多文件上传
              'auto'        : false,//在选择文件之后，是否立即提交
           /**
选项决定在上传期间什么样的数组将被显示在文件上传队列中。默认情况下，将显示上传进度的百分比。你可以将该选项的值设置为"speed"来显示上传速度，单位为KB/s 默认值：'percentage' 取值格式:'percentage' / 'speed'
           */
              'displayData' : 'speed',
             'queueSizeLimit'  : 1,//当允许多文件生成时，设置选择文件的个数，默认值：999 
             'simUploadLimit'  : 1,//允许同时上传的个数 默认值：1
              'sizeLimit'   : 10000*1024*1024,//最大上传文件大小：100MB 
          // 'scriptData'     :{ 'pack':proId}, // 多个参数用逗号隔开 'name':$('#name').val(),'num':$('#num').val(),'ttl':$('#ttl').val() 
           'method'          :'GET',
              /**
                  在单文件或多文件上传时，选择文件时触发。该函数有两个参数event，data，data对象有以下几个属性：
              ?fileCount：选择文件的总数。 
              ?filesSelected：同时选择文件的个数，如果一次选择了3个文件该属性值为3。 
              ?filesReplaced：如果文件队列中已经存在A和B两个文件，再次选择文件时又选择了A和B，该属性值为2。 
              ?allBytesTotal：所有选择的文件的总大小。 */
            
              'onSelectOnce'    : function(event, queueID, fileObj, response, data) {
                     num += data.filesSelected;
                   $('#status-message').text(num + ' 个文件被选中.');
          },  
             'onComplete'   : function(event, queueID, fileObj, response, data) {
                 realname=response;//附件存储名称uploadRealName
                 filename=fileObj.name;//附件真实名称uploadFileName
                     //向附件表中插入数据
                     var uploadFlag=false;
                 var array = new Array('jpg', 'png','JPG','PNG','gif');  //可以上传的文件类型   
                   var fileContentType = filename.match(/^(.*)(\.)(.{1,8})$/)[3]; //这个文件类型正则很有用：）   
                for (var i in array) {  
                  if (fileContentType.toLowerCase() == array[i].toLowerCase()) {  
                        uploadFlag = true;  
                   }  
                }  
                if (uploadFlag == false) {  
                    $("#status-message").text("只可以上传'jpg', 'png','JPG','PNG','gif'的图片");
                }  
                if(uploadFlag)
                	{
                		      $.ajax (
								 	 {
					         				 	  cache: false,
					          					  async: false,   //设置为同步获取数据形式
					          					  dataType: 'json', 
					          					  type: 'get',
					          					  data:{
									  					realname:realname,
									  					filename:filename,
									  					uplId:uploadId//附件表的 
					          					  },
					          					  url: contextPath+"/advertisement/saveFujian.action",
					          					  success: function (data)
					          					  {
									  					$('#status-message').text('上传成功.');
									  					
					          					  }
								 		 });
					             
                	}
          
             	
                 
                  
                   setTimeout(function(){$("#status-message").text("");}, 3000);//3秒后删除提示
          }
           });
           $("#up").click(function(){
              num=0;
           })
           $("#clear").click(function(){
              num=0;
              $("#status-message").text("");
           })
       })
       
    </script>
    </head>
    
       <div id="fileQueue" style="width:350px;"></div>
       <input type="file" name="file" id="file" />
       <%--<input  type="hidden" id="realname" value="<%=response.getWriter()%>"javascript:$('#uploadify').uploadifySettings();  uploadifySettings('scriptData',{'pack':proId})/>
       --%><p>
           <a href="javascript:$('#file').uploadifyUpload()" id="up" style="font-size: 12px;">开始上传</a> |
           <a href="javascript:$('#file').uploadifyClearQueue()" id="clear" style="font-size: 12px;">取消上传</a>
       </p>
       <div id="status-message" style="font-size: 12px;"></div>
       
</html>