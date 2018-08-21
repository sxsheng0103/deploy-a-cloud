function load(){
    $(".image").each(function(){
        $(this).click(function(){
            var image,container, kit,imgSrc;
            imgSrc = $(this).attr("src");
            image = new Image();
            image.src = imgSrc;

            container = document.querySelector("div#container");
            kit = new ImglyKit({
                image: image,
                container: container,
                assetsUrl: "./assets", // Change this to where your assets(��Դ) are
                ui: {
                    enabled: true // UI is disabled per default
                }
            });
            kit.run();
        })
    });
}
$(document).ready(function(){
        load();
        var flag = true;
        $("#middle").click(function(){
            if(flag == true) {
                $("#left").hide();
                $("#middle").css('left', '0');
                $("#picVer").attr('src','./assets/zuo.png');
                $("#center").css('width', '99.1%');
                var zoomOut = $(".imglykit-zoom-out");
                zoomOut.click();
                flag = false;
            }else{
                $("#left").show();
                $("#picVer").attr('src','./assets/you.png');
                $("#middle").css('left', '9.2%');
                $("#center").css('width', '90%');
                zoomOut = $(".imglykit-zoom-out");
                zoomOut.click();
                flag = true;
            }
        });
        var flag1 = true;
        $("#slideDown").click(function(){
             if(flag1 == true) {
                 var zoomOut = $(".imglykit-zoom-in");
                 console.log(zoomOut);
                 $(".image").hide();
                 $("#centerDown").hide();
                 $("#shang").attr('src',"./assets/xia.png");
                 $("#slideDown").css('margin-bottom','0');
                 $("#centerContainer").css('height', '97.6%');
                 //触发点击事件，使图片充满编辑窗口

                 zoomOut.click();
                 flag1 = false;
             }else{
                 $(".image").show();
                 $("#centerDown").show();
                 $("#shang").attr('src','./assets/shang.png');
                 $("#slideDown").css('margin-bottom','1px');
                 $("#centerContainer").css('height', '82%');
                 zoomOut = $(".imglykit-zoom-out");
                 zoomOut.click();
                 flag1 = true;
             }
        })
 });