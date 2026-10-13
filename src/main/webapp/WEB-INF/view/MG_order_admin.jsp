<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="Header_admin.jsp"></jsp:include>
<jsp:include page="Menu_admin.jsp"></jsp:include>

<div id="layoutSidenav_content">
  <main class="container-fluid">
    <h1 class="mt-4">Quản lý đơn hàng</h1>
    <c:if test="${not empty orderResult}">
      <div class="alert alert-success" role="status"><c:out value="${orderResult}" /></div>
    </c:if>
    <c:if test="${not empty orderError}">
      <div class="alert alert-danger" role="alert"><c:out value="${orderError}" /></div>
    </c:if>
    <div class="row mb-3">
      <div class="col-md-6 mb-2">
        <label class="sr-only" for="order-search">Tìm đơn hàng</label>
        <input class="form-control" id="order-search" type="search" placeholder="Tìm mã đơn, khách hàng, email hoặc số điện thoại">
      </div>
      <div class="col-md-4 mb-2">
        <label class="sr-only" for="order-status-filter">Lọc trạng thái</label>
        <select class="form-control" id="order-status-filter">
          <option value="">Tất cả trạng thái</option>
          <option value="0">Chờ xác nhận</option>
          <option value="1">Đã xác nhận</option>
          <option value="2">Đang giao</option>
          <option value="3">Đã hoàn thành</option>
        </select>
      </div>
      <div class="col-md-2 mb-2 text-md-right">
        <span class="text-muted">Đơn hiển thị: <strong id="visible-order-count">${fn:length(orders)}</strong></span>
      </div>
    </div>
    <div class="card">
      <div class="card-header">Danh sách đơn hàng</div>
      <div class="card-body table-responsive">
      <table class="table table-bordered table-hover">
        <thead>
          <tr><th>Mã đơn</th><th>Khách hàng</th><th>Email</th><th>Điện thoại</th><th>Ngày đặt</th><th>Địa chỉ</th><th>Trạng thái</th><th>Thao tác</th></tr>
        </thead>
        <tbody id="orders-table">
          <c:forEach var="order" items="${orders}">
            <tr data-search="<c:out value='${order.mahoadon} ${order.tenkhachhang} ${order.emailkhachhang} ${order.sdt}' />"
                data-status="${order.tinhtrang}">
              <td><c:out value="${order.mahoadon}" /></td>
              <td><c:out value="${order.tenkhachhang}" /></td>
              <td><c:out value="${order.emailkhachhang}" /></td>
              <td><c:out value="${order.sdt}" /></td>
              <td><c:out value="${order.ngaylap}" /></td>
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
              <td>
                <c:if test="${order.tinhtrang < 3}">
                  <form method="post" action="<c:url value='/admin/orders/${order.mahoadon}/status' />">
                    <input type="hidden" name="status" value="${order.tinhtrang + 1}">
                    <button class="btn btn-primary btn-sm" type="submit">
                      <c:choose>
                        <c:when test="${order.tinhtrang == 0}">Xác nhận</c:when>
                        <c:when test="${order.tinhtrang == 1}">Bắt đầu giao</c:when>
                        <c:otherwise>Hoàn tất</c:otherwise>
                      </c:choose>
                    </button>
                  </form>
                </c:if>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty orders}">
            <tr><td colspan="8" class="text-center text-muted">Chưa có đơn hàng.</td></tr>
          </c:if>
        </tbody>
      </table>
      <p id="order-empty-filter" class="text-center text-muted" hidden>Không tìm thấy đơn hàng phù hợp.</p>
      </div>
    </div>
  </main>
</div>

<script>
  (function () {
    const search = document.getElementById('order-search');
    const status = document.getElementById('order-status-filter');
    const rows = Array.from(document.querySelectorAll('#orders-table tr[data-search]'));
    const empty = document.getElementById('order-empty-filter');
    const count = document.getElementById('visible-order-count');
    function filterOrders() {
      const query = search.value.trim().toLocaleLowerCase();
      let visible = 0;
      rows.forEach(function (row) {
        const matches = row.dataset.search.toLocaleLowerCase().includes(query)
          && (!status.value || row.dataset.status === status.value);
        row.hidden = !matches;
        if (matches) visible += 1;
      });
      count.textContent = String(visible);
      empty.hidden = visible !== 0 || rows.length === 0;
    }
    search.addEventListener('input', filterOrders);
    status.addEventListener('change', filterOrders);
  })();
</script>
<jsp:include page="Footer_admin.jsp"></jsp:include>
