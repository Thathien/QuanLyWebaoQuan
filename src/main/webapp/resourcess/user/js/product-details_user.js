$(document).ready(function(){
	$('#giatien').each(function(){
		var giatiensp=$(this).text();
		var fomat=parseInt(giatiensp).toFixed(3).replace(/(\d)(?=(\d{3})+\.)/g, "$1.").toString();
		$(".giatiensp").html(fomat+" VNĐ");
	});
});