<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Header_user.jsp"></jsp:include>
<jsp:include page="Menu_user.jsp"></jsp:include>

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
</section>

<jsp:include page="Footer_user.jsp"></jsp:include>
