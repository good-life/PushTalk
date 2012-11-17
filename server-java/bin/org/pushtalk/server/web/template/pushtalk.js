

function getAppDomainByType (type) {
	var domain;
	if (type == KK_APPS) {
		domain = KK_APPS_SERVER + "/" + KK_APPS;
	} else if (type == KK_FILES) {
		domain = KK_APPS_SERVER + "/" + KK_FILES;
	} else if (type == KK_FORYO) {
		domain = KK_APPS_SERVER + "/" + KK_FORYO;
	}
	return domain;
}

function getAppDomain(domainlv2) {
	var domain;
	if (undefined == KK_APPS_SERVER || "" == KK_APPS_SERVER) {
		domain = "http://hp.kktalk.cn/apps";
	} else {
		domain = getAppDomainByType(domainlv2);
	}
	return domain;
}

var os = (this.os = {});

function detect(ua){
    var browser = (this.browser = {}),
      webkit = ua.match(/WebKit\/([\d.]+)/),
      android = ua.match(/(Android)\s+([\d.]+)/),
      ipad = ua.match(/(iPad).*OS\s([\d_]+)/),
      iphone = !ipad && ua.match(/(iPhone\sOS)\s([\d_]+)/),
      webos = ua.match(/(webOS|hpwOS)[\s\/]([\d.]+)/),
      touchpad = webos && ua.match(/TouchPad/),
      blackberry = ua.match(/(BlackBerry).*Version\/([\d.]+)/);

    if (webkit) browser.version = webkit[1];
    browser.webkit = !!webkit;

    if (android) os.android = true, os.version = android[2];
    if (iphone) os.ios = true, os.version = iphone[2].replace(/_/g, '.'), os.iphone = true;
    if (ipad) os.ios = true, os.version = ipad[2].replace(/_/g, '.'), os.ipad = true;
    if (webos) os.webos = true, os.version = webos[2];
    if (touchpad) os.touchpad = true;
    if (blackberry) os.blackberry = true, os.version = blackberry[2];
}

function urlParam(name) {
	var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
	if (null == results) return "";
	return results[1];
}

function alert2(text) {
	alert(text);
}

function log(text) {
	var content = document.getElementById("kk_debug_content");
	if (null === content) {
		content = document.createElement("div");
		content.id = "kk_debug_content";
		content.style.zIndex=9;
		document.body.appendChild(content);
	}
	content.innerHTML = content.innerHTML + "<br />" + text;
}

function isEmptyString(s) {
	if (null === s || '' === s) return true;
	return false;
}

function getAppLink(pageName, osType, type, idCommand, param1, param2, param3) {
	if (KK_CLIENT_VERSION && KK_CLIENT_VERSION < 40700) {
		param1 = 'false';
	}
	var link = "";
	if ("i" == osType
			|| "iphone" == osType || "iPhone" == osType
			|| "iPad" == osType || "ipad" == osType
			|| "ios" == osType || "IOS" == osType) {
		link = "http://nativenav?type=" + type
			+ "&idCommand=" + idCommand
			+ "&param1=" + param1
			+ "&param2=" + param2
			+ "&param3=" + param3;
		
	} else if ("a" == osType
			|| "android" == osType || "Android" == osType) {
		link = "javascript:" + pageName + ".nativenav('" + type
			+ "','" + idCommand
			+ "','" + param1
			+ "','" + param2
			+ "','" + param3
			+ "');";
	}
	return link;
}

/* Resource */
var ZH_CN="ZH_CN";
var ZH_HK="ZH_HK";
var ZH_TW="ZH_TW";
var EN_US="EN_US";
var KK_DEFAULT_LANG = ZH_CN;
function Resource(){
	this.ResourceText={};
	function getLang(){
		return KK_LANG?KK_LANG.toUpperCase():KK_DEFAULT_LANG;
	}
	this.setResource=function( lang,text ){
		this.ResourceText[lang]=text;
	}
	this.getResource=function(){
		var lang = getLang();
		var resc = this.ResourceText[lang];
		if (!resc) {
			if (lang.indexOf("EN")==0) {
				lang = EN_US;
			} else if (lang.indexOf("ZH")==0) {
				var ln = lang.length-3;
				if (lang.indexOf("TW")==ln || lang.indexOf("HK")==ln) {
					lang = ZH_TW;
				} else {
					lang = ZH_CN;
				}
			} else {
				lang = ZH_CN;
			}
			resc = this.ResourceText[lang];
		}
 		return resc;
	}
}