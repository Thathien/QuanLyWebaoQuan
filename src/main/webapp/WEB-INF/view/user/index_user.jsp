<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/view/user/Header_user.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/user/Menu_user.jsp"></jsp:include>
	
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
									<c:if test="${ListDanhMucSP.hiden == false}">
										<div class="panel panel-default">
										<div class="panel-heading">
											<h4 class="panel-title"><a href="<c:url value='/'><c:param name='category' value='${ListDanhMucSP.madanhmucsanpham}' /></c:url>"><c:out value="${ListDanhMucSP.tendanhmuc}" /></a></h4>
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
									<label for="filter-category">Danh mục</label>
									<select class="form-control" id="filter-category" name="category">
										<option value="">Tất cả danh mục</option>
										<c:forEach items="${showListDanhMucSP}" var="categoryItem">
											<c:if test="${categoryItem.hiden == false}">
												<option value="<c:out value='${categoryItem.madanhmucsanpham}' />"
													<c:if test="${selectedCategory == categoryItem.madanhmucsanpham}">selected</c:if>>
													<c:out value="${categoryItem.tendanhmuc}" />
												</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
								<div class="form-group">
									<label for="filter-name">Tên sản phẩm</label>
									<input class="form-control" id="filter-name" name="q" type="search"
										value="<c:out value='${searchQuery}' />" placeholder="Nhập tên sản phẩm">
								</div>
								<div class="form-group">
									<label for="filter-min-price">Giá từ (VNĐ)</label>
									<input class="form-control" id="filter-min-price" name="minPrice" type="number"
										min="0" step="1000" value="<c:out value='${minPrice}' />">
								</div>
								<div class="form-group">
									<label for="filter-max-price">Giá đến (VNĐ)</label>
									<input class="form-control" id="filter-max-price" name="maxPrice" type="number"
										min="0" step="1000" value="<c:out value='${maxPrice}' />">
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
					<div class="features_items"><!--features_items-->
						<h2 class="title text-center">Sản Phẩm (${productPage.totalElements})</h2>
						<c:choose>
							<c:when test="${not empty showListSanPham}">
								 <c:forEach items="${showListSanPham}" var="ListSanPham"  >
												<div class="col-sm-4">
										<div class="product-image-wrapper">
											<div class="single-products">
												<div class="productinfo text-center">
												<c:choose>
													<c:when test="${empty ListSanPham.hinhsanpham}">
														<img src="<c:url value='/resourcess/user/images/shop/product12.jpg' />" alt="" />
													</c:when>
													<c:otherwise>
														<img src="<c:url value='/resourcess/user/images/shop/${ListSanPham.hinhsanpham}' />" data-fallback="<c:url value='/resourcess/user/images/shop/product12.jpg' />" alt="<c:out value='${ListSanPham.tensanpham}' />" onerror="this.onerror=null;this.src=this.dataset.fallback;" />
													</c:otherwise>
												</c:choose>
													<h2 class="giatiensp" ><c:out value="${ListSanPham.giatien}" /> VNĐ</h2>
													<p><c:out value="${ListSanPham.tensanpham}" /></p>
													<a href="<c:url value='/chi-tiet-san-pham/${ListSanPham.masanpham}' />" class="btn btn-default"><i class="fa fa-search"></i>Xem chi tiết</a>
												</div>
												
													<div class="product-overlay">
													<a href="<c:url value='/chi-tiet-san-pham/${ListSanPham.masanpham}' />" id="infor_product">
														<div class="overlay-content">
															<h2 class="giatiensp"><c:out value="${ListSanPham.giatien}" /> VNĐ</h2>
															<a href="<c:url value='/chi-tiet-san-pham/${ListSanPham.masanpham}' />" id="infor_product"><p><c:out value="${ListSanPham.tensanpham}" /></p></a>
															
															<a href="<c:url value='/chi-tiet-san-pham/${ListSanPham.masanpham}' />" class="btn btn-default"><i class="fa fa-search"></i>Xem chi tiết</a>
														</div>
													</a>
													</div>
											</div>
										</div>
									</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
								 <c:out value="Không tìm thấy sản phẩm phù hợp"></c:out>
								 </br>
							</c:otherwise>
						</c:choose>
					</div><!--features_items-->
					<c:if test="${productPage.totalPages > 1}">
						<nav aria-label="Phân trang sản phẩm">
							<ul class="pagination">
								<c:if test="${productPage.hasPrevious()}">
									<li>
										<c:url var="previousPageUrl" value="/">
											<c:if test="${not empty selectedCategory}"><c:param name="category" value="${selectedCategory}" /></c:if>
											<c:if test="${not empty searchQuery}"><c:param name="q" value="${searchQuery}" /></c:if>
											<c:if test="${not empty minPrice}"><c:param name="minPrice" value="${minPrice}" /></c:if>
											<c:if test="${not empty maxPrice}"><c:param name="maxPrice" value="${maxPrice}" /></c:if>
											<c:param name="page" value="${productPage.number - 1}" />
										</c:url>
										<a href="${previousPageUrl}" aria-label="Trang trước">&laquo;</a>
									</li>
								</c:if>
								<c:forEach var="pageNumber" begin="0" end="${productPage.totalPages - 1}">
									<c:if test="${pageNumber >= productPage.number - 2 && pageNumber <= productPage.number + 2}">
										<li class="${pageNumber == productPage.number ? 'active' : ''}">
											<c:url var="numberedPageUrl" value="/">
												<c:if test="${not empty selectedCategory}"><c:param name="category" value="${selectedCategory}" /></c:if>
												<c:if test="${not empty searchQuery}"><c:param name="q" value="${searchQuery}" /></c:if>
												<c:if test="${not empty minPrice}"><c:param name="minPrice" value="${minPrice}" /></c:if>
												<c:if test="${not empty maxPrice}"><c:param name="maxPrice" value="${maxPrice}" /></c:if>
												<c:param name="page" value="${pageNumber}" />
											</c:url>
											<a href="${numberedPageUrl}">${pageNumber + 1}</a>
										</li>
									</c:if>
								</c:forEach>
								<c:if test="${productPage.hasNext()}">
									<li>
										<c:url var="nextPageUrl" value="/">
											<c:if test="${not empty selectedCategory}"><c:param name="category" value="${selectedCategory}" /></c:if>
											<c:if test="${not empty searchQuery}"><c:param name="q" value="${searchQuery}" /></c:if>
											<c:if test="${not empty minPrice}"><c:param name="minPrice" value="${minPrice}" /></c:if>
											<c:if test="${not empty maxPrice}"><c:param name="maxPrice" value="${maxPrice}" /></c:if>
											<c:param name="page" value="${productPage.number + 1}" />
										</c:url>
										<a href="${nextPageUrl}" aria-label="Trang sau">&raquo;</a>
									</li>
								</c:if>
							</ul>
						</nav>
					</c:if>
				</div>
			</div>
		</div>
	</section>
<jsp:include page="/WEB-INF/view/user/Footer_user.jsp"></jsp:include>
