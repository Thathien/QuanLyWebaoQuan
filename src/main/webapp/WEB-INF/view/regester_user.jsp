<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<jsp:include page="Header_user.jsp"></jsp:include>
<jsp:include page="Menu_user.jsp"></jsp:include>
<section id="form"><!--form-->
		<div class="container">
			<div class="row">
				<div class="col-sm-8  "id="frmdangky1">
					<div class="signup-form" ><!--sign up form-->
						<h2 class="text-center" style="padding-left: 20px; ">Tạo mới tài khoản</h2>
						<span class="text-right" id="gobabck">Đã có tài khoản đăng nhập</span>
						<form:form  action="dangky" id="frmdangky" modelAttribute="dangkyuser" method ="post">
							<label>Họ tên</label>
							<form:input path="hotendk" type="text" placeholder="Nhập họ tên" name="hotendk" id="hotendk" maxlength="40"/>
							<span class="text-danger" id="hoten-eror">${error_hotendk}</span>
							<label>Email</label>
							<form:input  path="emaildk" type="email" placeholder="Địa chỉ Email" name="emaildk" id="emaildk" maxlength="50"/>
							<span class="text-danger">${error_emaildk}</span>
							<label>Địa chỉ</label>
							<form:input path="diachidk" type="text" placeholder="Nhập địa chỉ" name="diachidk" id="diachidk" maxlength="50"/>
							<span class="text-danger" id="diachidk-eror">${error_diachidk}</span>
							<label>Mật khẩu</label>
							<form:input  path="matkhaudk" type="password" placeholder="Mật khẩu" name="matkhaudk" id="matkhaudk" maxlength="10"/>
							<span class="text-danger" id="matkhau-eror">${error_matkhaudk}</span>
							<label>Nhập lại mật khẩu</label>
							<form:input path="nhaplaimkdk" type="password" placeholder="Nhập lại mật khẩu" name="nhaplaimkdk" id="nhaplaimkdk"/>
							<span class="text-danger" id="nhaplaimatkhau-eror">${error_nhaplaimatkhaudk}</span>
<!-- 							<label>Chọn ngày sinh</label>
							<input type="date" name="ngaysinhdk" id="ngaysinhdk" > -->
							<!-- <span class="text-danger" id="ngaysinh-eror"></span> -->
							</br>
							<label>Chọn giới tính</label>
							<form:select path="gioitinhdk" name="gioitinhdk" id="gioitinhdk">
								<option value="null" selected="selected">Chọn giới tính</option>
								<option value="1">Nam</option>
								<option value="0">Nữ</option>
							</form:select>
							<span class="text-danger">${error_gioitinhdk}</span>
							</br>
							<button type="submit" class="btn btn-default" style="margin-top:10px; ">Đăng ký</button>
							<span class="text-danger">${success_fail}</span>
							
						</form:form>
					</div><!--/sign up form-->
				</div>
			</div>
		</div>
	</section><!--/form-->

<jsp:include page="Footer_user.jsp"></jsp:include>