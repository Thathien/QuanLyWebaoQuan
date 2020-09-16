/*!
    * Start Bootstrap - SB Admin v6.0.1 (https://startbootstrap.com/templates/sb-admin)
    * Copyright 2013-2020 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    (function($) {
    "use strict";

    // Add active state to sidbar nav links
    var path = window.location.href; // because the 'href' property of the DOM element is the absolute path
        $("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function() {
            if (this.href === path) {
                $(this).addClass("active");
            }
        });

    // Toggle the side navigation
    $("#sidebarToggle").on("click", function(e) {
        e.preventDefault();
        $("body").toggleClass("sb-sidenav-toggled");
    });
    
    init_reload();
    function init_reload(){
        setInterval( function() {
                   window.location.reload();
 
          },30000);
    }
    
    //select all  -? delete
	$("#checkAll").click(function () {
		if(this.checked){
			$(":checkbox").prop('checked', true);
		}else{
			$(":checkbox").prop('checked', false);
		}
	});	
	$("#xoasanpham").click(function () {
		var listIdSanPham=" ";
		for(var i=0; i<$(":checkbox:checked").length;i++){
			if($(":checkbox:checked").eq(i).attr('id') != "checkAll"){
				listIdSanPham+=$(":checkbox:checked").eq(i).attr('id')+",";
			}
		}
		alert(listIdSanPham);
		if($(":checkbox:checked").length != 0){
			var x = confirm("Bạn muốn xóa những sản phẩm này không?");
			if(x){
				$.ajax({
					url:"/banaoquan/admin/product/delete",
					type: "POST",
					data:{
						listIdSanPham:listIdSanPham
					},
					success: function(value){
						if(value === 'true'){
							alert("Xóa sản phẩm thành công")
						}
					}
				});
				
			}else{
				return false
			}
		}else{
			alert("Bạn chưa chọn sản phẩm nào để xóa")
		}
	});
})(jQuery);
