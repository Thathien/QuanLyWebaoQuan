<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/view/user/Header_user.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/user/Menu_user.jsp"></jsp:include>

<section class="container">
  <h2>Thông tin tài khoản</h2>
  <c:if test="${not empty accountResult}"><p class="text-success"><c:out value="${accountResult}" /></p></c:if>
  <c:if test="${not empty accountError}"><p class="text-danger"><c:out value="${accountError}" /></p></c:if>
  <form method="post" action="<c:url value='/tai-khoan' />">
    <div class="form-group">
      <label for="hoten">Họ tên</label>
      <input class="form-control" id="hoten" name="hoten" maxlength="50" required value="<c:out value='${customer.hoten}' />">
    </div>
    <div class="form-group">
      <label for="email">Email</label>
      <input class="form-control" id="email" readonly value="<c:out value='${customer.email}' />">
    </div>
    <div class="form-group">
      <label for="diachi">Địa chỉ</label>
      <textarea class="form-control" id="diachi" name="diachi" maxlength="255" required><c:out value="${customer.diachi}" /></textarea>
    </div>
    <button class="btn btn-primary" type="submit">Lưu thay đổi</button>
  </form>
  <hr>
  <h3>Đổi mật khẩu</h3>
  <form method="post" action="<c:url value='/tai-khoan/doi-mat-khau' />">
    <div class="form-group">
      <label for="matKhauHienTai">Mật khẩu hiện tại</label>
      <input class="form-control" id="matKhauHienTai" name="matKhauHienTai" type="password" autocomplete="current-password" required>
    </div>
    <div class="form-group">
      <label for="matKhauMoi">Mật khẩu mới</label>
      <input class="form-control" id="matKhauMoi" name="matKhauMoi" type="password" minlength="8" maxlength="100" autocomplete="new-password" required>
    </div>
    <div class="form-group">
      <label for="xacNhanMatKhauMoi">Xác nhận mật khẩu mới</label>
      <input class="form-control" id="xacNhanMatKhauMoi" name="xacNhanMatKhauMoi" type="password" minlength="8" maxlength="100" autocomplete="new-password" required>
    </div>
    <button class="btn btn-default" type="submit">Đổi mật khẩu</button>
  </form>
</section>

<jsp:include page="/WEB-INF/view/user/Footer_user.jsp"></jsp:include>
