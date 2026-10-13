<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Header_user.jsp"></jsp:include>
<jsp:include page="Menu_user.jsp"></jsp:include>

<section class="container">
  <h2>Lịch sử đơn hàng</h2>
  <c:choose>
    <c:when test="${empty orders}">
      <p>Bạn chưa có đơn hàng nào.</p>
    </c:when>
    <c:otherwise>
      <div class="table-responsive">
        <table class="table table-bordered">
          <thead>
            <tr><th>Mã đơn</th><th>Ngày đặt</th><th>Hình thức nhận</th><th>Địa chỉ</th><th>Trạng thái</th></tr>
          </thead>
          <tbody>
            <c:forEach var="order" items="${orders}">
              <tr>
                <td><c:out value="${order.mahoadon}" /></td>
                <td><c:out value="${order.ngaylap}" /></td>
                <td><c:out value="${order.hinhthucgiaohang}" /></td>
                <td><c:out value="${order.diachigiaohang}" /></td>
                <td>
                  <c:choose>
                    <c:when test="${order.tinhtrang == 0}">Chờ xác nhận</c:when>
                    <c:when test="${order.tinhtrang == 1}">Đã xác nhận</c:when>
                    <c:when test="${order.tinhtrang == 2}">Đang giao</c:when>
                    <c:when test="${order.tinhtrang == 3}">Đã hoàn thành</c:when>
                    <c:otherwise>Không xác định</c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</section>

<jsp:include page="Footer_user.jsp"></jsp:include>
