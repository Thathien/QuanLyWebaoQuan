$(document).ready(function(){
	
	updateTongTienGio();
	function updateTongTienGio(isEventChange){
		$('.giatien-s').each(function(){
			var tongtiensp=0;
			var giatien=$(this).attr("data-value");
			var soluong=$(this).closest("tr").find(".soluong-sp").val();
			var tongtien=parseFloat(giatien)*soluong;
			var fomat=parseInt(tongtien).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
			tongtiensp=tongtiensp+tongtien;
			if(!isEventChange){
				$(this).html(fomat);
			}
			var formattongtien=tongtiensp.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
			$("#tongtiensp").html(formattongtien+"");
		});
		
	}
	$('.soluong-sp').change(function(){
		var machitietsanpham=$(this).closest("tr").attr("data-value");
		var masp=$(this).closest("tr").find(".ten-sp").attr("data-value");
		var mamau=$(this).closest("tr").find(".mamau-sp").attr("data-value");
		alert("mamau" + mamau);
		var masize=$(this).closest("tr").find(".size-sp").attr("data-value");
		var soluong=$(this).val();
		var giatien=$(this).closest("tr").find(".giatien-sp").attr("data-value").replace(",",".");
		var tongtien=soluong * parseInt(giatien);
		var fomat=tongtien.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
		$(this).closest("td").find(".tongtien-sp").html(fomat);
//		updateTongTienGio(true);
		$.ajax({
			url:"/banaoquan/giohang/update/",
			type: "GET",
			data:{
				masp: masp,
				masize: masize,
				mamau: mamau,
//				machitietsanpham:machitietsanpham,
				soluong:soluong
			},
			success: function(value){
				if(value === 'true'){
					alert("Cập nhật giỏ hàng thành công")
				}
			}
		});
	});
	$('.cart_quantity_delete').click(function(){
		var self=$(this);
		var masp=$(this).closest("tr").find(".ten-sp").attr("data-value");
		var mamau=$(this).closest("tr").find(".mamau-sp").attr("data-value");
		var masize=$(this).closest("tr").find(".size-sp").attr("data-value");
		var x = confirm("Bạn muốn xóa?");
		if(x){
			$.ajax({
				url:"/banaoquan/giohang/delete/",
				type: "GET",
				data:{
					masp: masp,
					masize: masize,
					mamau: mamau,
				},
				success: function(value){
					if(value === 'true'){
						self.closest("tr").remove();
						updateTongTienGio(true);
					}
				}
			});
		}
	});
});