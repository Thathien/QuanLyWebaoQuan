$(document).ready(function(){
	updateTongTienGio();
	function updateTongTienGio(isEventChange){
		var tongtiensp=0;
		var tongsosp=0;
		$('.giatien-sp').each(function(){
			var giatien=parseInt($(this).attr("data-value"), 10) || 0;
			var soluong=parseInt($(this).closest("tr").find(".soluong-sp").val(), 10) || 0;
			var tongtien=giatien*soluong;
			var fomat=parseInt(tongtien).toFixed(3).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
			tongtiensp=tongtiensp+tongtien;
			tongsosp+=soluong;
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
		var soluong=parseInt($(this).val(), 10);
		if (!Number.isInteger(soluong) || soluong < 1 || soluong > 99) {
			alert("Số lượng phải từ 1 đến 99.");
			$(this).val(1);
			return;
		}
		var giatien=parseInt($(this).closest("tr").find(".giatien-sp").attr("data-value"), 10);
		var tongtien=soluong * giatien;
		var fomat=tongtien.toFixed(3).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
		$(this).closest("tr").find(".tongtien-sp").html(fomat);
		updateTongTienGio(true);
		$.ajax({
			url:"/giohang/update/",
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
				} else {
					alert("Số lượng vượt tồn kho hoặc sản phẩm không còn khả dụng.");
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
				url:"/giohang/delete/",
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
						if ($(".soluong-sp").length === 0) {
							window.location.reload();
						}
					}
				}
			});
		}
	});
	$('#thanhtoan').click(function(){
		var tenkhachhang=$('#tenkhachhang').val();
		var emailkhachang=$('#emailkhachang').val();
		var sodienthoai=$("#sodienthoai").val();
		var hinhthucgiao=$('#hinhthuc').val();
		var diachigiaohang=$('#diachigiaohang').val();
		var ghichu=$('#ghichu').val();
		if(!isValidTen(tenkhachhang)){
			$('#tenkhachhang_error').text("Tên không hợp lệ ")
			$('#tenkhachhang').focus();
		}else if(!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailkhachang)){
			$('#emailkhachang_error').text("Email không hợp lệ")
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
					url:"/giohang",
					type: "POST",
					data:{
						tenkhachhang: tenkhachhang,
						emailkhachang: emailkhachang,
						sodienthoai: sodienthoai,
						hinhthucgiao: hinhthucgiao,
						diachigiaohang: diachigiaohang,
						ghichu: ghichu
					},
					success: function(value){
						if (value === "true") {
							alert("Đặt hàng thành công.");
							window.location.href = "/";
						} else if (value === "email-failed") {
							alert("Đơn hàng đã được tạo nhưng email xác nhận chưa gửi được.");
							window.location.href = "/";
						} else {
							alert("Không thể đặt hàng. Vui lòng kiểm tra thông tin và tồn kho.");
						}
					},
					error: function(){
						alert("Không nhận được kết quả đặt hàng. Vui lòng kiểm tra lịch sử đơn hàng trước khi gửi lại.");
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
		return /^(09|03|07|08|05)\d{8}$/.test(v.trim());
	};
});