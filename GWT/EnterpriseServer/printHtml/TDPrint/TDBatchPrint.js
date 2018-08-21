var myReportAPI; //定义MyReport接口对象
var myReportInit = false; //定义MyReport初始化变量
//页面加载完成时调用
function onPageLoad(){
    myReportAPI = document.getElementById("FormReportTDPrintApp");
    //loadTDBatchReport();
}
function onMyReportInitialized(){
    myReportInit = true;
    //以下是自定义代码
    loadTDBatchReport();
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
function loadTDBatchReport() {
    if (!myReportInit)// 要先判断插件是否初始化
        return;
	var host = window.location.host ;
	var bill = new Array();
	var table = new Array();

	//var url = "ReportTDStyle_SFSY.xml";
	
	var jsonStr = window.parent.getTDBatchJson();

	var json = eval("("+jsonStr+")");
	var reportID = json['printID'];
	//打印格式xml
	var formXml = json['formXML'];
	var url = getCtrlService(reportID);
	var billJson = json['headList'];

	var tableJson= json['itemList'];
    myReportLoad(url, formXml, billJson, tableJson);
}
function myReportLoad(url, formXml, params, table) {
    if (!myReportAPI || !myReportInit)
	{
		return;
	}
    myReportAPI.loadReport(formXml, params, table);
}