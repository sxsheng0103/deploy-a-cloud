var myReportAPI; //定义MyReport接口对象
var myReportInit = false; //定义MyReport初始化变量
//页面加载完成时调用
function onPageLoad(){
    myReportAPI = document.getElementById("FormBatchPrintApp");
    loadReport();
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
	var params = new Array();
	var table = new Array();

	var jsonStr = window.parent.getBatchJson();

	var json = eval("("+jsonStr+")");
	var reportID = json['printID'];
	//打印格式xml
	var formXml = json['formXML'];
	//var url = getCtrlService(reportID[0]);
	var paramJson = json['Head'];
	var tableJson= json['Item'];
    myReportLoad(reportID, formXml, paramJson, tableJson);
}
function myReportLoad(reportID, formXml, params, table) {
    if (!myReportAPI || !myReportInit)
	{
		return;
	}
    myReportAPI.loadReport(reportID, formXml, params, table);
}