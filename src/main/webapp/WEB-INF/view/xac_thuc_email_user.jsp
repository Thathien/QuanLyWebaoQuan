<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Header_user.jsp"></jsp:include>
<jsp:include page="Menu_user.jsp"></jsp:include>

<section>
  <div class="container">
    <div class="row">
      <div class="col-sm-12 text-center">
        <h2><c:out value="${verificationResult}" /></h2>
        <a href="<c:url value='/dangnhap' />" class="btn btn-default">Đến trang đăng nhập</a>
      </div>
    </div>
  </div>
</section>

<jsp:include page="Footer_user.jsp"></jsp:include>
