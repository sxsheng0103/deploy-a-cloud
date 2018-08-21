var myReportAPI; //定义MyReport接口对象
var myReportInit = false; //定义MyReport初始化变量
var perView;
//页面加载完成时调用
function onPageLoad(){
    myReportAPI = document.getElementById("FormReportPrintApp");
   //loadReport();
}
function onMyReportInitialized(){
    myReportInit = true;
    //以下是自定义代码
    loadReport();
}
function onMyReportClosed() {
    //以下是自定义代码
    alert("MyReport关闭。");
}
function onMyReportPrinted() {
    //以下是自定义代码
    //alert("MyReport打印。");
}
//自定义加载方法1
function loadReport() {
    if (!myReportInit)// 要先判断插件是否初始化
        return;
	var host = window.location.host ;
	var params = {};
	var table = new Array();

	var jsonStr = window.parent.json();

	var json = eval("("+jsonStr+")");
	var reportID = json['printID'];
	//打印格式xml
	var formXml = json['formXML'];
	var url = getCtrlService(reportID);
	var paramJson = json['Head'];
	var tableJson= json['Item'];
	perView = json['isPerView'];
    myReportLoad(url, formXml, paramJson, tableJson);
}
function myReportLoad(url, formXml, params, table) {
    if (!myReportAPI || !myReportInit)
	{
		return;
	}
	if(perView == "0"){
		myReportAPI.LoadAndPrint(url, params, table);
		return;
	}
    myReportAPI.loadReport(formXml, params, table);
}