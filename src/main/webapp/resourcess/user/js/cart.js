$(document).ready(function(){
	
	//thêm sp vào giỏ hàng
	$('.add-tocart').click(function(){
		var machitietsanpham=$(this).attr("data-machitietsanpham");
		var mamau=$(this).closest("span").find(".mau").attr("data-value");
		var tenmau=$(this).closest("span").find(".mau").text();
//		var tenmau=$('.mau').text();
		var masize=$(this).closest("span").find(".size").attr("data-value");
		var tensize=$(this).closest("span").find(".size").text();
		var tensp=$('#tensp').text();
		var giatien=$('#giatien').attr("data-value");
		var masp=$('#masp').attr("data-value");
		var soluong=$('#soluong').val();
//		alert("ma chi tiet san pham :" +machitietsanpham);
//		alert("ma màu :" +mamau);
//		alert(" ten mau :"  +tenmau);
//		alert(" ma size :" +masize);
//		alert("ten size :" +tensize);
		$.ajax({
			url:"/banaoquan/themgiohang",
			type: "GET",
			data:{
				machitietsanpham:machitietsanpham,
				masp: masp,
				masize: masize,
				mamau: mamau,
				tensp: tensp,
				giatien: giatien,
				tenmau: tenmau,
				tensize: tensize,
				soluong:soluong
			},
			success: function(value){
				if(value === 'true'){
					alert("Thêm vào giỏ hàng thành công")
				}
			}
		});
	});
	
});