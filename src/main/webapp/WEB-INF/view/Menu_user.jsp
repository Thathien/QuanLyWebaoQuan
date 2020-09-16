<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<body>
	<header id="header"><!--header-->
		<div class="header_top"><!--header_top-->
			<div class="container">
				<div class="row">
					<div class="col-sm-6">
						<div class="contactinfo">
							<ul class="nav nav-pills">
								<li><a href="#"><i class="fa fa-phone"></i> +84 xx xx 8xx</a></li>
								<li><a href="#"><i class="fa fa-envelope"></i> info@xx.com</a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="social-icons pull-right">
							<ul class="nav navbar-nav">
								<c:if test="${taikhoan!=null}">
									<li><c:out value="Xin chào "><i>${taikhoan.getHoten()}</i></c:out></li>
								</c:if>
								<li><a href="#"><i class="fa fa-facebook"></i></a></li>
								<li><a href="#"><i class="fa fa-twitter"></i></a></li>
								<li><a href="#"><i class="fa fa-linkedin"></i></a></li>
								<li><a href="#"><i class="fa fa-dribbble"></i></a></li>
								<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
								
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header_top-->
		
		<div class="header-middle"><!--header-middle-->
			<div class="container">
				<div class="row">
					<div class="col-md-4 clearfix">
						<div class="logo pull-left">
							<a href="/banaoquan/"><img src="<c:url value = "/resourcess/user/images/home/logo.png"></c:url>"/></a>
						</div>
					</div>
					<div class="col-md-8 clearfix">
						<div class="shop-menu clearfix pull-right">
							<ul class="nav navbar-nav">
								<li>
									<c:choose>
										<c:when test="">
											<a href="/banaoquan/giohang"><i class="fa fa-shopping-cart"></i> Giỏ hàng: 
										<c:if test="${giohang.size()!=0}">
										<i style="color: red">${giohang.size()}</i>
										</c:if>
										</a>
										</c:when>
										<c:otherwise>
											<a href="/banaoquan/giohang"><i class="fa fa-shopping-cart"></i> Giỏ hàng </a>
										</c:otherwise>
									</c:choose>
								</li>
								<c:choose>
									<c:when test="${taikhoan!=null}">
										<li><a href=""><i class="fa fa-user"></i> Tài khoản</a></li>
										<li><a href="/banaoquan/logout"><i class="fa fa-crosshairs"></i> Đăng xuất</a></li>
									</c:when>
									<c:otherwise>
										<li><a href="/banaoquan/dangnhap"><i class="fa fa-lock"></i> Đăng nhập</a></li>
										<li><a href="/banaoquan/dangky"><i class="fa fa-lock"></i> Đăng ký</a></li>
									</c:otherwise>
								</c:choose>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header-middle-->
		
				<div class="header-bottom"><!--header-bottom-->
			<div class="container">
				<div class="row">
					<div class="col-sm-8">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
								<span class="sr-only">Toggle navigation</span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
							</button>
						</div>
						<div class="mainmenu pull-left">
							<ul class="nav navbar-nav collapse navbar-collapse">
								<li><a href="/banaoquan/" class="active">Trang chủ</a></li>
								<li class="dropdown"><a href="#">Sản phẩm<i class="fa fa-angle-down"></i></a>
                                    <ul role="menu" class="sub-menu">
                                        <li><a href="">Products</a></li>
										<li><a href="">Product Details</a></li> 
										<li><a href="">Checkout</a></li> 
										<li><a href="">Cart</a></li> 
										<li><a href="">Login</a></li> 
                                    </ul>
                                </li> 
								<li class="dropdown"><a href="#">Tin tức<i class="fa fa-angle-down"></i></a>
                                    <ul role="menu" class="sub-menu">
                                        <li><a href="">Blog List</a></li>
										<li><a href="">Blog Single</a></li>
                                    </ul>
                                </li> 
								<li><a href="">404</a></li>
								<li><a href="">Liên hệ</a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="search_box pull-right">
							<input type="text" placeholder="Tìm kiếm"/>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header-bottom-->
	</header><!--/header-->