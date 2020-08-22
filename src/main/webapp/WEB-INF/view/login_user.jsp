<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<jsp:include page="Header_user.jsp"></jsp:include>
<jsp:include page="Menu_user.jsp"></jsp:include>
	
<section id="form"><!--form-->
		<div class="container">
			<div class="row">
				<div class="col-sm-5 col-sm-offset-1" id="frmdangnhap1">
					<div class="login-form" id="frmdangnhap"><!--login form-->
						<h2 class="text-center">Đăng nhập</h2>
						<form:form  action="dangnhap" modelAttribute="dangnhapuser" method ="post">
							<form:input path="email" type="email" name="emaildn" id="emaildn" placeholder="Địa chỉ Email" />
							<span class="text-danger" id="email-eror"></span>
							 <form:input path="matKhau" type="Password" placeholder="Mật khẩu" name="passworddn" id="passworddn" />
							<span class="text-danger" id="password-eror">${resultDangNhap}</span>
							</br>
							<span id="taomoitaikhoan" class="text-center">Tạo mới tài khoản</span>
							<form:button type="submit" class="btn btn-default">Đăng nhập</form:button>
						 </form:form>
					</div><!--/login form-->
				</div>
			</div>
		</div>
	</section><!--/form-->

<jsp:include page="Footer_user.jsp"></jsp:include>