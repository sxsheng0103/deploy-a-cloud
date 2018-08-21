function scada(options){
	var iframes=document.getElementById(options.renderTo);
	iframes.width=options.width;
	iframes.height=options.height;
	iframes.src=options.url+"?JH="+options.JSMC+"&type=0";
}