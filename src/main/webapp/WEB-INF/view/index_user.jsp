<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="Header_user.jsp"></jsp:include>
<jsp:include page="Menu_user.jsp"></jsp:include>
	<section id="advertisement">
		<div class="container">
		<img src="<c:url value ="/resourcess/user/images/shop/advertisement.jpg"></c:url>" alt="" />
		</div>
	</section>
	
	<section>
		<div class="container">
			<div class="row">
				<div class="col-sm-3">
					<div class="left-sidebar">
						<h2>Danh mục</h2>
						<div class="panel-group category-products" id="accordian"><!--category-productsr-->
						<c:choose>
							<c:when test="${showListDanhMucSP.size()!=0}">
								
								 <c:forEach items="${showListDanhMucSP}" var="ListDanhMucSP"  >
									<div class="panel panel-default">
										<div class="panel-heading">
											<h4 class="panel-title"><a href="">${ListDanhMucSP.getTendanhmuc()}</a></h4>
										</div>
									</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
								 <c:out value="Dữ liệu trống"></c:out>
							</c:otherwise>
						</c:choose>
							
						</div><!--/category-productsr-->
					
						<div class="brands_products"><!--brands_products-->
							<h2>Thương hiệu</h2>
							<div class="brands-name">
								<ul class="nav nav-pills nav-stacked">
									<li><a href=""> <span class="pull-right">(50)</span>Acne</a></li>
									<li><a href=""> <span class="pull-right">(56)</span>Grüne Erde</a></li>
									<li><a href=""> <span class="pull-right">(27)</span>Albiro</a></li>
									<li><a href=""> <span class="pull-right">(32)</span>Ronhill</a></li>
									<li><a href=""> <span class="pull-right">(5)</span>Oddmolly</a></li>
									<li><a href=""> <span class="pull-right">(9)</span>Boudestijn</a></li>
									<li><a href=""> <span class="pull-right">(4)</span>Rösch creative culture</a></li>
								</ul>
							</div>
						</div><!--/brands_products-->
						
						<div class="price-range"><!--price-range-->
							<h2>Giá</h2>
							<div class="well">
								 <input type="text" class="span2" value="" data-slider-min="0" data-slider-max="600" data-slider-step="5" data-slider-value="[250,450]" id="sl2" ><br />
								 <b>$ 0</b> <b class="pull-right">$ 600</b>
							</div>
						</div><!--/price-range-->
						
						<div class="shipping text-center"><!--shipping-->
						<img src="<c:url value ="/resourcess/user/images/home/shipping.jpg"></c:url>" alt="" />
						</div><!--/shipping-->
						
					</div>
				</div>
				
				<div class="col-sm-9 padding-right">
					<div class="features_items"><!--features_items-->
						<h2 class="title text-center">Sản Phẩm</h2>
						
						<c:choose>
							<c:when test="${showListSanPham.size()!=0}">
								 <c:forEach items="${showListSanPham}" var="ListSanPham"  >
									<div class="col-sm-4">
										<div class="product-image-wrapper">
											<div class="single-products">
												<div class="productinfo text-center">
												<c:choose>
													<c:when test="${ListSanPham.getHinhsanpham()==null}">
														<img src="<c:url value ="/resourcess/user/images/shop/product12.jpg"></c:url>" alt="" />
													</c:when>
													<c:otherwise>
														<img src="<c:url value ="/resourcess/user/images/shop/${ListSanPham.getHinhsanpham()}"></c:url>" alt="" />
													</c:otherwise>
												</c:choose>
													<h2 class="giatiensp" >${ListSanPham.getGiatien()} VNĐ</h2>
													<p>${ListSanPham.getTensanpham()}</p>
													<a href="#" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Thêm vào giỏ</a>
												</div>
												
													<div class="product-overlay">
													<a href="/banaoquan/chi-tiet-san-pham/${ListSanPham.getMasanpham()}" id="infor_product">
														<div class="overlay-content">
															<h2 class="giatiensp">${ListSanPham.getGiatien()} VNĐ</h2>
															<a href="/banaoquan/chi-tiet-san-pham/${ListSanPham.getMasanpham()}" id="infor_product"><p>${ListSanPham.getTensanpham()}</p></a>
															
															<a href="/banaoquan/chi-tiet-san-pham/${ListSanPham.getMasanpham()}" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Xem chi tiết</a>
														</div>
													</a>
													</div>
												
											</div>
										</div>
									</div>
								
							
								</c:forEach>
								</br>
							<ul class="pagination">
								<li class="active"><a href="">1</a></li>
								<li><a href="">2</a></li>
								<li><a href="">3</a></li>
								<li><a href="">&raquo;</a></li>
							</ul>
							</c:when>
							<c:otherwise>
								 <c:out value="Dữ liệu trống"></c:out>
								 </br>
							</c:otherwise>
						</c:choose>
						
						
					</div><!--features_items-->
				</div>
			</div>
		</div>
	</section>
<jsp:include page="Footer_user.jsp"></jsp:include>