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
					<c:when test="${gioHangs.size()!=0}">
					<table class="table table-condensed">
						<thead>
							<tr class="cart_menu">
								<td><a href=""><input type="checkbox"></a></td>
								<td class="image">Ảnh</td>
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
							<tr>
								<td class="cart_price">
									<input type="checkbox">
								</td>
								<td class="cart_product">
								<c:if test="${gioHangs.size()!=0}"></c:if>
								<a href=""><img src="<c:url value ="/resourcess/user/images/cart/one.png"></c:url>" alt="" /></a>
								
								</td>
								<td class="cart_description">
									<h4><a href="">${gioHangs.getTensp()}</a></h4>
									<p>Mã sản phẩm: ${gioHangs.getMasp()}</p>
								</td>
								<td class="cart_price">
									<p>${gioHangs.getTenmau()}</p>
								</td>
								<td class="cart_price">
									<p>size : ${gioHangs.getTensize()}</p>
								</td>
								<td class="cart_price">
									<p class="cart_total_price">${gioHangs.getGiatien()}</p>
								</td>
								<td class="cart_quantity">
									<div class="cart_quantity_button">
										<a class="cart_quantity_up" href=""> + </a>
										<input class="cart_quantity_input" type="text" name="quantity" value="1" autocomplete="off" size="2">
										<a class="cart_quantity_down" href=""> - </a>
									</div>
								</td>
								<td class="cart_total">
									<p class="cart_total_price"></p>
								</td>
								<td class="cart_delete">
									<a class="cart_quantity_delete" href=""><i class="fa fa-times"></i></a>
								</td>
							</tr>
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
	<c:if test="${gioHangs.size()!=0}">
			<section id="do_action">
				<div class="container">
					<div class="heading">
						<h3>What would you like to do next?</h3>
						<p>Choose if you have a discount code or reward points you want to use or would like to estimate your delivery cost.</p>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="total_area">
								<ul>
									<li>Cart Sub Total <span>$59</span></li>
									<li>Eco Tax <span>$2</span></li>
									<li>Shipping Cost <span>Free</span></li>
									<li>Total <span>$61</span></li>
								</ul>
									<a class="btn btn-default update" href="">Tiếp tục mua sắm</a>
									<a class="btn btn-default check_out" href="">Thanh toán</a>
							</div>
						</div>
					</div>
				</div>
			</section><!--/#do_action-->
	</c:if>


<jsp:include page="Footer_user.jsp"></jsp:include>