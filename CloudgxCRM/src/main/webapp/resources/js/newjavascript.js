var ticket=getUrlParam('ticket'),token;

function generateRandomString() {
     return (Math.random().toString(36).substr(2));
}
	
// 使用CORS跨域认证
function gettoken(){
     var timestamp = new Date().getTime();
     var nonce = encodeURIComponent(generateRandomString());
     var appid = encodeURIComponent("110110");
     var appSecret = "110110";
     var version = "1.1";
     var array = new Array(version,timestamp, nonce, appid, appSecret);
     array.sort();
			
     var str = array.join("");
     var sign = encodeURIComponent(CryptoJS.SHA1(str).toString());
			
     var authorization = "OpenAuth2 version=\"" + version + "\", appid=\"" + appid + "\", timestamp=" + timestamp + ", nonce=\"" + nonce + "\", sign=\"" + sign + "\"";
		    
     var xhr = new XMLHttpRequest();
     xhr.onreadystatechange = function() {
           console.log(xhr.readyState);
           if(xhr.readyState == 4) {
	        if(xhr.status == 200 || xhr.status == 304) {
		     var resp = JSON.parse(xhr.responseText);
		     $('#tk').html(JSON.stringify(resp));
                     if((resp != undefined) && (resp.success != undefined) && (resp.success) && (resp.data != undefined) && (resp.data.access_token != undefined) ) {
                            token = resp.data.access_token;
                     }
	        }
            }
     };
     
     var url = "http://do.kdweibo.com/openauth2/api/appAuth2";
     xhr.open("POST", url, false);
     xhr.setRequestHeader("authorization", authorization);
     xhr.send(null);
}

function getcontext(){
    $.ajax({
        type: "GET",
        dataType:"JSONP", 
        data:{
          ticket:ticket,
          "access_token":token
        },
        url: "http://do.kdweibo.com/openauth2/api/getcontext"
    }).done(function(ok) {
        
       $('#ct').html('用户:'+ok.username+',企业:'+ok.eid);
       $('#ct').html(JSON.stringify(ok));

    }).fail(function(a) {
       
        alert(a.responseText);


    });
}
//获取url中的参数
function getUrlParam(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
  var r = window.location.search.substr(1).match(reg);  //匹配目标参数
  if (r != null) return unescape(r[2]); return null; //返回参数值
}    

