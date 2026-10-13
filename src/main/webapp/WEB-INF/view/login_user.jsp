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
							<form:errors path="email" cssClass="text-danger" />
							<form:input path="email" type="text" name="emaildn" id="emaildn" autocomplete="username" placeholder="Email hoặc tên đăng nhập" />
							<span class="text-danger" id="email-error"></span>
							<form:errors path="matKhau" cssClass="text-danger" />
							<form:password path="matKhau" placeholder="Mật khẩu" name="passworddn" id="passworddn" autocomplete="current-password" />
							<span class="text-danger" id="password-error" role="alert"><c:out value="${resultDangNhap}" /></span>
							<br/>
							<span id="taomoitaikhoan" class="text-center">Tạo mới tài khoản</span>
							<form:button type="submit" class="btn btn-default">Đăng nhập</form:button>
						 </form:form>
					</div><!--/login form-->
				</div>
			</div>
		</div>
	</section><!--/form-->

<jsp:include page="Footer_user.jsp"></jsp:include>