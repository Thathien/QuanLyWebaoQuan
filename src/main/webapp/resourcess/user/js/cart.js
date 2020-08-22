$(document).ready(function(){
	$('#card').click(function(){
		var mamau=$(this).closest('b').find(".mau").attr("data-mamau");
		var tenmau=$(this).closest('b').find(".mau").text();
//		var tenmau=$('.mau').text();
		var masize=$(this).closest('b').find(".size").attr("data-size");
		var tensize=$(this).closest('b').find(".size").text();
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