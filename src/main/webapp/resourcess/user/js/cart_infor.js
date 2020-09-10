$(document).ready(function(){
	updateTongTienGio();
	function updateTongTienGio(isEventChange){
		var tongtiensp=0;
		var tongsosp=0;
		$('.giatien-s').each(function(){
			var giatien=$(this).attr("data-value");
			var soluong=$(this).closest("tr").find(".soluong-sp").val();
			var tongtien=parseFloat(giatien)*soluong;
			var fomat=parseInt(tongtien).toFixed(3).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
			tongtiensp=tongtiensp+tongtien;
			tongsosp=tongsosp+soluong;
			alert("giatien"+ giatien);
			alert("soluong"+ soluong);
			alert("tongtien"+ tongtien);
			alert("fomat"+ fomat);
			if(!isEventChange){
				$(this).html(fomat);
			}
			var formattongtien=tongtiensp.toFixed().replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
			$("#tongtiensp").html(formattongtien+" VNĐ");
			$("#tongsoluongsp").html(tongsosp);
		})
	}
	$('.soluong-sp').change(function(){
		var machitietsanpham=$(this).closest("tr").attr("data-value");
		var masp=$(this).closest("tr").find(".ten-sp").attr("data-value");
		var mamau=$(this).closest("tr").find(".mamau-sp").attr("data-value");
		var masize=$(this).closest("tr").find(".size-sp").attr("data-value");
		var soluong=$(this).val();
		var giatien=$(this).closest("tr").find(".giatien-sp").attr("data-value").replace(",",".");
		var tongtien=soluong * parseInt(giatien);
		var fomat=tongtien.toFixed(3).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
		$(this).closest("tr").find(".tongtien-sp").html(fomat);
		updateTongTienGio(true);
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
		var x = confirm("Bạn muốn xóa sản phẩm này khỏi giỏ hàng ?");
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
	$('#thanhtoan').click(function(){
		var tenkhachhang=$('#tenkhachhang').val();
		var emailkhachang=$('#emailkhachang').val();
		var sodienthoai=$("#sodienthoai").val();
		var hinhthuc=$('#hinhthuc').val;
		var diachigiaohang=$('#diachigiaohang').val();
		var ghichu=$('#ghichu').val();
		if(!isValidTen(tenkhachhang)){
			$('#tenkhachhang_error').text("Tên không hợp lệ ")
			$('#tenkhachhang').focus();
		}else if(emailkhachang==""){
			$('#emailkhachang_error').text("Email hợp lệ ")
			$('#emailkhachang').focus();
		}else if(!isValidSDT(sodienthoai)){
			$('#sodienthoai_error').text("Số điện thoại hợp lệ ")
			$('#sodienthoai').focus();
		}else if(!isValidDiaChi(diachigiaohang)){
			$('#diachigiaohang_error').text("Địa chỉ hợp lệ ")
			$('#diachigiaohang').focus();
		}else{
			var x = confirm("Bạn muốn thanh toán tất cả sản phẩm trong giỏ ?");
			if(x){
				$.ajax({
					url:"/banaoquan/giohang/",
					type: "POST",
					data:{
						tenkhachhang: tenkhachhang,
						emailkhachang: emailkhachang,
						sodienthoai: sodienthoai,
						hinhthuc: hinhthuc,
						diachigiaohang: diachigiaohang,
						ghichu: ghichu
					},
					success: function(value){
						
					}
				});
			}
		}	
	
	});
	function isValidTen(v){
		var kt=0;
		for(var i=0;i<v.length;i++){
			if (v[i]!=" ") kt=1;
		}
		return (v.search(/[<]|[>]|[#]|[-]/ig) == -1) && (v.length>0) && (v.length<=50) && (kt==1);
	};
	function isValidDiaChi(v){
		var kt=0;
		for(var i=0;i<v.length;i++){
			if (v[i]!=" ") kt=1;
		}
		return (v.search(/[<]|[>]|[#]/ig) == -1) && (v.length>0) && (kt==1);
	};
	function isValidSDT(v){
		var kt=0;
		for(var i=0;i<v.length;i++){
			if (v[i]!=" ") kt=1;
		}
		return (v.search(/((09|03|07|08|05)+([0-9]{8})\b)/g) == -1) && (v.length>0) && (kt==1);
	};
});