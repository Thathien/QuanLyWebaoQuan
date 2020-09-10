<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<jsp:include page="Header_user.jsp"></jsp:include>
<jsp:include page="Menu_user.jsp"></jsp:include>
	
<section id="cart_items">
		<div class="container">
			<div class="breadcrumbs">
				<ol class="breadcrumb">
				  <li><a href="#">Home</a></li>
				  <li class="active">Giỏ hàng</li>
				</ol>
			</div>
			<div class="table-responsive cart_info">
				<c:choose>
					<c:when test="${isEmpty==false}">
					<table class="table table-condensed">
						<thead>
							<tr class="cart_menu">
								<td><a href=""><input type="checkbox"></a></td>
								
								<td class="description">Tên sản phẩm</td>
								<td class="price">Màu sắc</td>
								<td class="price">Kích cỡ</td>
								<td class="price">Giá</td>
								<td class="quantity">Số lượng</td>
								<td class="total">Tổng</td>
								<td></td>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="gioHangs" items="${gioHangs}">
								<tr data-value="${gioHangs.getMachitietsanpham()}">
									<td class="cart_price">
										<input type="checkbox">
									</td>
									</td>
									<td class="cart_description">
										<h4><a href="/banaoquan/chi-tiet-san-pham/${gioHangs.getMasp()}" class="ten-sp" data-value="${gioHangs.getMasp()}">${gioHangs.getTensp()}</a></h4>
										<p class="masp">Mã sản phẩm: ${gioHangs.getMasp()}</p>
									</td>
									<td class="cart_price">
										<p class="mamau-sp" data-value="${gioHangs.getMamau()}">${gioHangs.getTenmau()}</p>
									</td>
									<td class="cart_price">
										<p class="size-sp" data-value="${gioHangs.getMasize()}">size : ${gioHangs.getTensize()}</p>
									</td>
									<td class="cart_price">
										<p class="cart_total_price giatien-sp" data-value="${gioHangs.getGiatien()}">${gioHangs.getGiatien()}</p>
									</td>
									<td class="cart_quantity">
										<div class="cart_quantity_button">
											
											<input class="cart_quantity_input soluong-sp" type="number"  value="${gioHangs.getSoluong()}"  size="2" min=1 max=99>
											
										</div>
									</td>
									<td class="cart_total">
										<p class="cart_total_price tongtien-sp" ></p>
									</td>
									<td class="cart_delete">
										<a class="cart_quantity_delete" href=""><i class="fa fa-times" title="Xóa khỏi giỏ hàng"></i></a>
									</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
					</c:when>
					<c:otherwise>
						<h3 style="text-align: center;">Giỏ hàng của bạn trống hãy quay lại <a href="/banaoquan/">trang chủ</a> để mua sắm</h3>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</section> <!--/#cart_items-->
	<c:if test="${isEmpty==false}">
			<section id="do_action">
				<div class="container">
					<div class="heading">
						<h3>THANH TOÁN</h3>
						<p></p>
					</div>
					<div class="row">
						<div class="col-sm-6">
						<div class="chose_area">
								<ul class="user_info">
									<li class="single_field">
										<label>Tên Người mua nhận</label>
										<input type="text" id="tenkhachhang">
										<span class="text-danger" id="tenkhachhang_error"></span>
										
									</li>
									<li class="single_field">
										<label>Email</label>
										<input type="email" id="emailkhachang">
										<span class="text-danger" id="emailkhachang_error"></span>
									</li>
									<li class="single_field">
										<label>Số điện thoại liên hệ</label>
										<input type="text" id="sodienthoai">
										<span class="text-danger" id="sodienthoai_error"></span>
									</li>
									<li class="single_field">
										<label>Hình thức giao hàng</label>
										<select id="hinhthuc">
											<option selected="true" value="0">Giao hàng tận nơi</option>
											<option value="1">Nhận hàng tại cửa hàng</option>
										</select>
										<span class="text-danger" id="hinhthuc_error"></span>
									</li>
									<li class="single_field">
										<label>Địa chỉ giao hàng</label>
										<textarea name="" rows="2" id="diachigiaohang"></textarea>
										<span class="text-danger" id="diachigiaohang_error"></span>
									</li>
									<li class="single_field">
										<label>Ghi chú</label>
										<textarea name="" rows="4" id="ghichu"></textarea>
										<span class="text-danger" id="ghichu_error"></span>
									</li>
								</ul>
								<a class="btn btn-default update" href="/banaoquan/">Tiếp tục mua sắm</a>
								<a class="btn btn-default check_out" href="" id="thanhtoan">Thanh toán</a>
						</div>
					</div>
						<div class="col-sm-6">
							<div class="total_area">
								<ul>
									<li>Tổng số lượng sản phẩm<span id="tongsoluongsp"></span></li>
									<li>Giảm giá <span id="giamgiasp"></span></li>
									<li>Vận chuyển <span id="freeship"></span></li>
									<li>Tổng tiền <span id="tongtiensp"></span></li>
								</ul>
									
									
							</div>
						</div>
					</div>
				</div>
			</section><!--/#do_action-->
	</c:if>


<jsp:include page="Footer_user.jsp"></jsp:include>