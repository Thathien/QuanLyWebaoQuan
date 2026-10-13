<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<jsp:include page="Header_user.jsp"></jsp:include>
<jsp:include page="Menu_user.jsp"></jsp:include>
<section>
		<div class="container">
			<div class="row">
				<div class="col-sm-3">
					<div class="left-sidebar">
						<h2>Danh mục</h2>
						<div class="panel-group category-products" id="accordian"><!--category-productsr-->
						<c:choose>
							<c:when test="${showListDanhMucSP.size()!=0}">
								<c:forEach items="${showListDanhMucSP}" var="ListDanhMucSP">
									<c:if test="${ListDanhMucSP.hiden == false}">
										<div class="panel panel-default">
										<div class="panel-heading">
											<h4 class="panel-title">
												<a href="<c:url value='/'><c:param name='category' value='${ListDanhMucSP.madanhmucsanpham}' /></c:url>">
													<c:out value="${ListDanhMucSP.tendanhmuc}" />
												</a>
											</h4>
										</div>
									</div>
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								 <c:out value="Dữ liệu trống"></c:out>
							</c:otherwise>
						</c:choose>
							
						</div><!--/category-productsr-->
					
						<div class="filter-products">
							<h2>Lọc sản phẩm</h2>
							<form method="get" action="<c:url value='/' />">
								<div class="form-group">
									<label for="detail-filter-category">Danh mục</label>
									<select class="form-control" id="detail-filter-category" name="category">
										<option value="">Tất cả danh mục</option>
										<c:forEach items="${showListDanhMucSP}" var="categoryItem">
											<c:if test="${categoryItem.hiden == false}">
												<option value="<c:out value='${categoryItem.madanhmucsanpham}' />">
													<c:out value="${categoryItem.tendanhmuc}" />
												</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
								<div class="form-group">
									<label for="detail-filter-name">Tên sản phẩm</label>
									<input class="form-control" id="detail-filter-name" name="q" type="search"
										placeholder="Nhập tên sản phẩm">
								</div>
								<div class="form-group">
									<label for="detail-filter-min-price">Giá từ (VNĐ)</label>
									<input class="form-control" id="detail-filter-min-price" name="minPrice" type="number"
										min="0" step="1000">
								</div>
								<div class="form-group">
									<label for="detail-filter-max-price">Giá đến (VNĐ)</label>
									<input class="form-control" id="detail-filter-max-price" name="maxPrice" type="number"
										min="0" step="1000">
								</div>
								<button class="btn btn-primary btn-block" type="submit">Áp dụng bộ lọc</button>
								<a class="btn btn-default btn-block" href="<c:url value='/' />">Xóa bộ lọc</a>
							</form>
						</div>
						
						<div class="shipping text-center"><!--shipping-->
						<img src="<c:url value ="/resourcess/user/images/home/shipping.jpg"></c:url>" alt="" />
						</div><!--/shipping-->
						
					</div>
				</div>
				
				<div class="col-sm-9 padding-right">
					<div class="product-details"><!--product-details-->
						<div class="col-sm-5">
							<div class="view-product">
								<img
									src="<c:url value='/resourcess/user/images/shop/${productImageName}' />"
									data-fallback="<c:url value='/resourcess/user/images/shop/product12.jpg' />"
									alt="<c:out value='${sanphamRS.tensanpham}' />"
									onerror="this.onerror=null;this.src=this.dataset.fallback;" />
							</div>
							<div id="similar-product" class="carousel slide" data-ride="carousel">
								
								  <!-- Wrapper for slides -->
								    <div class="carousel-inner">
										<div class="item active">
										  <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar1.jpg"></c:url>" alt="" /></a>
										  <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar2.jpg"></c:url>" alt="" /></a>
										  <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar3.jpg"></c:url>" alt="" /></a>
										</div>
										<div class="item">
										 <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar1.jpg"></c:url>" alt="" /></a>
										  <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar2.jpg"></c:url>" alt="" /></a>
										  <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar3.jpg"></c:url>" alt="" /></a>
										</div>
										<div class="item">
										  <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar1.jpg"></c:url>" alt="" /></a>
										  <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar2.jpg"></c:url>" alt="" /></a>
										  <a href=""><img src="<c:url value ="/resourcess/user/images/product-details/similar3.jpg"></c:url>" alt="" /></a>
										</div>
										
									</div>

								  <!-- Controls -->
								  <a class="left item-control" href="#similar-product" data-slide="prev">
									<i class="fa fa-angle-left"></i>
								  </a>
								  <a class="right item-control" href="#similar-product" data-slide="next">
									<i class="fa fa-angle-right"></i>
								  </a>
							</div>

						</div>
						<div class="col-sm-7">
							<div class="product-information"><!--/product-information-->
								<img src="<c:url value='/resourcess/user/images/product-details/new.jpg' />" alt="" class="newarrival"/>

								<h2 id="tensp">${sanphamRS.getTensanpham()}</h2>
								<p>Mã sản phẩm : <i id="masp" data-value="${sanphamRS.getMasanpham()}">${sanphamRS.getMasanpham()}</i></p>
								<img src="<c:url value ="/resourcess/user/images/product-details/rating.png"></c:url>" alt="" />
								<span>
									<span id="giatien" data-value="${sanphamRS.getGiatien()}">Giá :${sanphamRS.getGiatien()} VNĐ</span>
								</span>
								<c:forEach items="${sanphamRS.getChiTietSanPham()}" var="chitietsanpham" >
									<span>
										<label>Màu sản phẩm :</label><i >${chitietsanpham.getMauSanPham().getTenmau()}</i> <label>- -Size : </label>
										<i>${chitietsanpham.getSizeSanPham().getSize()}</i><br/>
										<i class="mau" style="display: none;" data-value="${chitietsanpham.getMauSanPham().getMamau()}">${chitietsanpham.getMauSanPham().getTenmau()}</i>
										<i class="size" style="display: none;"data-value="${chitietsanpham.getSizeSanPham().getMasize()}">${chitietsanpham.getSizeSanPham().getSize()}</i>
										<label>Số lượng mua:</label>
										<input type="number" value="1" id="soluong" min="1" max="100" />
										<button type="button" class="btn btn-default cart add-tocart" data-machitietsanpham="${chitietsanpham.getMachitietsanpham()}">
											<i class="fa fa-shopping-cart"></i>
											Thêm vào giỏ
										</button>
									</span>
								</c:forEach>
									
								
								
								<p><b>Chi tiết sản phẩm:</b> In Stock</p>
								<p><b>Condition:</b> New</p>
								<p><b>Brand:</b> E-SHOPPER</p>
								<img src="<c:url value ="/resourcess/user/images/product-details/share.png"></c:url>" alt=""  class="share img-responsive"/>

							</div><!--/product-information-->
						</div>
					</div><!--/product-details-->
					
					<div class="category-tab shop-details-tab"><!--category-tab-->
						<div class="col-sm-12">
							<ul class="nav nav-tabs">
								<li><a href="#details" data-toggle="tab">Details</a></li>
								<li><a href="#companyprofile" data-toggle="tab">Company Profile</a></li>
								<li><a href="#tag" data-toggle="tab">Tag</a></li>
								<li class="active"><a href="#reviews" data-toggle="tab">Reviews (5)</a></li>
							</ul>
						</div>
						<div class="tab-content">
							<div class="tab-pane fade" id="details" >
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery1.jpg"></c:url>" alt="" />
												
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery2.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery3.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery4.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="tab-pane fade" id="companyprofile" >
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery1.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery3.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery2.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery4.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="tab-pane fade" id="tag" >
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery1.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery2.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery3.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="product-image-wrapper">
										<div class="single-products">
											<div class="productinfo text-center">
												<img src="<c:url value ="/resourcess/user/images/home/gallery1.jpg"></c:url>" alt="" />
												<h2>$56</h2>
												<p>Easy Polo Black Edition</p>
												<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="tab-pane fade active in" id="reviews" >
								<div class="col-sm-12">
									<ul>
										<li><a href=""><i class="fa fa-user"></i>EUGEN</a></li>
										<li><a href=""><i class="fa fa-clock-o"></i>12:41 PM</a></li>
										<li><a href=""><i class="fa fa-calendar-o"></i>31 DEC 2014</a></li>
									</ul>
									<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p>
									<p><b>Write Your Review</b></p>
									
									<form action="#">
										<span>
											<input type="text" placeholder="Your Name"/>
											<input type="email" placeholder="Email Address"/>
										</span>
										<textarea name="" ></textarea>
										<b>Rating: </b> <img src="images/product-details/rating.png" alt="" />
										<button type="button" class="btn btn-default pull-right">
											Submit
										</button>
									</form>
								</div>
							</div>
							
						</div>
					</div><!--/category-tab-->
					
					<div class="recommended_items"><!--recommended_items-->
						<h2 class="title text-center">recommended items</h2>
						
						<div id="recommended-item-carousel" class="carousel slide" data-ride="carousel">
							<div class="carousel-inner">
								<div class="item active">	
									<div class="col-sm-4">
										<div class="product-image-wrapper">
											<div class="single-products">
												<div class="productinfo text-center">
													
													<img src="<c:url value ="/resourcess/user/images/home/recommend1.jpg"></c:url>" alt="" />
													<h2>$56</h2>
													<p>Easy Polo Black Edition</p>
													<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
												</div>
											</div>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="product-image-wrapper">
											<div class="single-products">
												<div class="productinfo text-center">
													<img src="<c:url value ="/resourcess/user/images/home/recommend2.jpg"></c:url>" alt="" />
													<h2>$56</h2>
													<p>Easy Polo Black Edition</p>
													<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
												</div>
											</div>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="product-image-wrapper">
											<div class="single-products">
												<div class="productinfo text-center">
													<img src="<c:url value ="/resourcess/user/images/home/recommend3.jpg"></c:url>" alt="" />
													<h2>$56</h2>
													<p>Easy Polo Black Edition</p>
													<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="item">	
									<div class="col-sm-4">
										<div class="product-image-wrapper">
											<div class="single-products">
												<div class="productinfo text-center">
													<img src="<c:url value ="/resourcess/user/images/home/recommend1.jpg"></c:url>" alt="" />
													<h2>$56</h2>
													<p>Easy Polo Black Edition</p>
													<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
												</div>
											</div>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="product-image-wrapper">
											<div class="single-products">
												<div class="productinfo text-center">
													<img src="<c:url value ="/resourcess/user/images/home/recommend2.jpg"></c:url>" alt="" />
													<h2>$56</h2>
													<p>Easy Polo Black Edition</p>
													<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
												</div>
											</div>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="product-image-wrapper">
											<div class="single-products">
												<div class="productinfo text-center">
													<img src="<c:url value ="/resourcess/user/images/home/recommend3.jpg"></c:url>" alt="" />
													<h2>$56</h2>
													<p>Easy Polo Black Edition</p>
													<button type="button" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Add to cart</button>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							 <a class="left recommended-item-control" href="#recommended-item-carousel" data-slide="prev">
								<i class="fa fa-angle-left"></i>
							  </a>
							  <a class="right recommended-item-control" href="#recommended-item-carousel" data-slide="next">
								<i class="fa fa-angle-right"></i>
							  </a>			
						</div>
					</div><!--/recommended_items-->
					
				</div>
			</div>
		</div>
	</section>
<jsp:include page="Footer_user.jsp"></jsp:include>