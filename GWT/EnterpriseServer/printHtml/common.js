


function getCtrlService(reportID){
	var curWwwPath = window.document.location.href;
	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName = window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8083
	var localhostPaht = curWwwPath.substring(0,pos);
	
	//获取带"/"的项目名，如：/uimcardprj
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	// 项目url，如：
	var productPath = localhostPaht+projectName;
	var dataBaseName = window.parent.getObject("DataBaseName");
	var dbno = window.parent.getObject("DBNO");
	return productPath + "/CtrlService?action=loadFile&fwk=FlexPrintService&iom=loadFlexFormXmlByByte&PO=DataBaseName=undefined&DBNO=undefined&PrintFormatID="+reportID+"&FileName="+reportID+".xml";
}

function convertJson(json, arry)
{
	if(json == null) return;

	for(var i=0;i<json.length;i++)
		{
			var param = {};
			var jsoObj = json[i];
			for(var keyJson in jsoObj)
			{
				param[keyJson] = jsoObj[keyJson];
			}
			arry.push(param);
		}
}
function itemConvertJson(json, arry)
{
	//json为批量打印时多个分录的集合
	if(json == null) return;

	for(var i=0;i<json.length;i++)
	{
		//获取其中一个分录
		var jsoObj = json[i];
		//遍历分录dataset的rowsetList
		for(var j=0;j<jsoObj.length;j++)
		{
			//获取一条分录
			var jo = jsoObj[i];
			//一个分录的rowset集合
			var itemArray = new Array();
			var param = {};
			for(var keyJson in jo)
			{
				param[keyJson] = jsoObj[keyJson];
			}
			itemArray.push(param);
		}
		arry.push(itemArray);
		/**for(var keyJson in jsoObj)
		{
			param[keyJson] = jsoObj[keyJson];
		}
		arry.push(param);*/
	}
}