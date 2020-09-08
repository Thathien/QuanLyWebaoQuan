$(document).ready(function(){
	
	//thêm sp vào giỏ hàng
	$('#card').click(function(){
		var mamau=$(this).closest("tr").find("#mau").attr("data-value");
		var tenmau=$(this).closest("tr").find("#mau").text();
//		var tenmau=$('.mau').text();
		var masize=$(this).closest("tr").find("#size").attr("data-value");
		var tensize=$(this).closest("tr").find("#size").text();
		var tensp=$('#tensp').text();
		var giatien=$('#giatien').attr("data-value");
		var masp=$('#masp').attr("data-value");
		var soluong=$('#soluong').val();
		alert("ma màu :" +mamau);
		alert(" ten mau :"  +tenmau);
		alert(" ma size :" +masize);
		alert("ten size :" +tensize);
		$.ajax({
			url:"/banaoquan/themgiohang",
			type: "GET",
			data:{
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
				if(value === 'success'){
					alert("Thêm vào giỏ hàng thành công")
				}
			}
		});
	});
	
});