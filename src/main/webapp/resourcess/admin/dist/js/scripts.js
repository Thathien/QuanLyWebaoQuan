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
 
          },60000);
    }
    
    function getdatatable(){
    	var chitietsp="";
    	$("#tableaddsp tr").each(function () {
    		var datarow=$(this).closest("tr").attr("data-value");
    		chitietsp=chitietsp+datarow+",";
    	});
    	return chitietsp;
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
							location.reload();
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
	$("#chitietsanpham").click(function () {
		var masanpham=$(this).attr("data-masanpham");
		$.ajax({
			url:"/banaoquan/admin/product/update",
			type: "POST",
			data:{
				masanpham:masanpham
			}
		});
	});
	
	$("#themchitietsanpham").click(function () {
//		var chitietclone=$("#chitietsanpham").clone().removeAttr("id");
//		$("#contchitietsanpham").append(chitietclone);
		var sizeaddname=$( "#sizeadd option:selected" ).text();
		var sizeadd=$("#sizeadd").val();
		var mausacaddname=$( "#mausacadd option:selected" ).text();
		var mausacadd=$("#mausacadd").val();
		var soluong =$("#soluongadd").val();
		var now = new Date();
		$("#tableaddsp tbody").append(
				"<tr data-value="+sizeadd+"/"+mausacadd+"/"+soluong+"/"+now+">"+
			        "<td>"+mausacaddname+"</td>"+
			        "<td>"+sizeaddname+"</td>"+
			        "<td>"+soluong+"</td>"+
			        "<td>"+now+"</td>"+
				"</tr>");
	});
	
	
	
	$("#addnewsanpham").click(function () {
		var tensanphamadd=$("#tensanphamadd").val();
		var danhmucadd=$("#danhmucadd").children("option:selected").val();
		var motaadd=$("#motaadd").val();
		var giatienadd=$("#giatienadd").val();
		var listchitet=getdatatable();
		var hinhanh=$("fileanhadd").val();
		
	});
	
	var files = [];
	$("#fileanhadd").change(function(event){
		files = event.target.files;
//		filename = event.target.files[0].name;
		var forms = new FormData();
		forms.append("file", files[0]);
		var urlcurrent = window.location.href;
//		var id = urlcurrent.substring(urlcurrent.lastIndexOf('/') + 1);
		$.ajax({
			url: "/banaoquan//admin/product/addimage",
			type : "POST",
			data: forms,
			contentType: false,
			processData: false,
			enctype: "multipath/form-data",
			success: function(value){
//				$.ajax({
//					url : "/banaoquan/admin/addimagecsdl",
//					type : "POST",
//					data : {
//						id : id,
//						filename : filename
//						
//					},
//					success : function(value){
//						
//					}
//				})
			}
		})
	});
	
})(jQuery);
