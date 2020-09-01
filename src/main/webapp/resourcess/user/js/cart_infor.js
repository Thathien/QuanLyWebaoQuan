$(document).ready(function(){
	
	updateTongTienGio();
	function updateTongTienGio(isEventChange){
		$('.giatien-s').each(function(){
			var tongtiensp=0;
			var giatien=$(this).attr("data-value");
			var fomat=parseInt(giatien).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
			tongtiensp=tongtiensp+parseFloat(giatien);
			if(!isEventChange){
				$(this).html(fomat);
			}
			
			var formattongtien=tongtiensp.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
			$("#tongtiensp").html(formattongtien+"");
		});
		
	}
	
	$('.soluong-sp').change(function(){
		var soluong=$(this).val();
		var giatien=$(this).closest("td").find(".giatien-sp").attr("data-value").replace(",",".");;
		var tongtien=soluong * parseInt(giatien);
		var fomat=tongtien.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
		$(this).closest("td").find(".tongtien-sp").html(fomat);
		updateTongTienGio(true);
	});
});