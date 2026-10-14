<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/view/admin/Header_admin.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/admin/Menu_admin.jsp"></jsp:include>

<div id="layoutSidenav_content">
  <main class="container-fluid">
    <h1 class="mt-4">Tổng quan</h1>
    <div class="row">
      <div class="col-xl-4 col-md-6 mb-4">
        <div class="card bg-primary text-white h-100">
          <div class="card-body">Tổng sản phẩm: <strong><c:out value="${productCount}" /></strong></div>
          <div class="card-footer"><a class="text-white" href="<c:url value='/admin/product' />">Quản lý sản phẩm</a></div>
        </div>
      </div>
      <div class="col-xl-4 col-md-6 mb-4">
        <div class="card bg-warning text-dark h-100">
          <div class="card-body">Đơn chờ xác nhận: <strong><c:out value="${pendingInvoiceCount}" /></strong></div>
          <div class="card-footer"><a class="text-dark" href="<c:url value='/admin/orders' />">Xem đơn hàng</a></div>
        </div>
      </div>
      <div class="col-xl-4 col-md-6 mb-4">
        <div class="card bg-success text-white h-100">
          <div class="card-body">Đơn hoàn thành: <strong><c:out value="${completedInvoiceCount}" /></strong></div>
          <div class="card-footer"><a class="text-white" href="<c:url value='/admin/orders' />">Xem đơn hàng</a></div>
        </div>
      </div>
    </div>
    <div class="card mb-4">
      <div class="card-header">Đơn hàng gần đây</div>
      <div class="card-body table-responsive">
        <table class="table table-bordered">
          <thead><tr><th>Mã đơn</th><th>Khách hàng</th><th>Ngày đặt</th><th>Trạng thái</th></tr></thead>
          <tbody>
            <c:forEach var="order" items="${recentInvoices}">
              <tr>
                <td><c:out value="${order.mahoadon}" /></td>
                <td><c:out value="${order.tenkhachhang}" /></td>
                <td><c:out value="${order.ngaylap}" /></td>
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
            <c:if test="${empty recentInvoices}"><tr><td colspan="4">Chưa có đơn hàng.</td></tr></c:if>
          </tbody>
        </table>
      </div>
    </div>
  </main>
</div>

<jsp:include page="/WEB-INF/view/admin/Footer_admin.jsp"></jsp:include>