<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/view/user/Header_user.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/user/Menu_user.jsp"></jsp:include>

<section class="container">
  <h2>Chi tiết đơn hàng #<c:out value="${order.mahoadon}" /></h2>
  <c:if test="${not empty orderResult}">
    <div class="alert alert-success" role="status"><c:out value="${orderResult}" /></div>
  </c:if>
  <c:if test="${not empty orderError}">
    <div class="alert alert-danger" role="alert"><c:out value="${orderError}" /></div>
  </c:if>
  <p><strong>Ngày đặt:</strong> <c:out value="${order.ngaylap}" /></p>
  <p><strong>Trạng thái:</strong>
    <c:choose>
      <c:when test="${order.tinhtrang == 0}">Chờ xác nhận</c:when>
      <c:when test="${order.tinhtrang == 1}">Đã xác nhận</c:when>
      <c:when test="${order.tinhtrang == 2}">Đang giao</c:when>
      <c:when test="${order.tinhtrang == 3}">Đã hoàn thành</c:when>
      <c:when test="${order.tinhtrang == 4}">Đã hủy</c:when>
      <c:otherwise>Không xác định</c:otherwise>
    </c:choose>
  </p>
  <p><strong>Người nhận:</strong> <c:out value="${order.tenkhachhang}" /></p>
  <p><strong>Số điện thoại:</strong> <c:out value="${order.sdt}" /></p>
  <p><strong>Hình thức nhận:</strong> <c:out value="${order.hinhthucgiaohang}" /></p>
  <p><strong>Địa chỉ:</strong> <c:out value="${order.diachigiaohang}" /></p>
  <p><strong>Ghi chú:</strong> <c:out value="${order.ghichu}" /></p>

  <div class="table-responsive">
    <table class="table table-bordered">
      <thead>
        <tr><th>Sản phẩm</th><th>Màu</th><th>Kích cỡ</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th></tr>
      </thead>
      <tbody>
        <c:forEach var="line" items="${order.chiTietHoaDon}">
          <tr>
            <td><c:out value="${line.bienThe.sanPham.tensanpham}" /></td>
            <td><c:out value="${line.bienThe.mauSanPham.tenmau}" /></td>
            <td><c:out value="${line.bienThe.sizeSanPham.size}" /></td>
            <td><c:out value="${line.giatien}" /> VNĐ</td>
            <td><c:out value="${line.soluong}" /></td>
            <td><c:out value="${line.giatien * line.soluong}" /> VNĐ</td>
          </tr>
        </c:forEach>
        <c:if test="${empty order.chiTietHoaDon}">
          <tr><td colspan="6" class="text-center">Đơn hàng chưa có chi tiết sản phẩm.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
  <c:if test="${canRequestReturn}">
    <a class="btn btn-warning" href="<c:url value='/don-hang/${order.mahoadon}/doi-tra' />">Yêu cầu đổi/trả</a>
  </c:if>
  <c:if test="${not empty returnRequests}">
    <h3>Yêu cầu đổi/trả</h3>
    <div class="table-responsive">
      <table class="table table-bordered">
        <thead>
          <tr><th>Ngày gửi</th><th>Loại yêu cầu</th><th>Lý do</th><th>Trạng thái</th></tr>
        </thead>
        <tbody>
          <c:forEach var="request" items="${returnRequests}">
            <tr>
              <td><c:out value="${request.createdAt}" /></td>
              <td><c:out value="${request.loaiYeuCau == 'DOI' ? 'Đổi hàng' : 'Trả hàng'}" /></td>
              <td><c:out value="${request.lyDo}" /></td>
              <td>
                <c:choose>
                  <c:when test="${request.trangThai == 'CHO_XU_LY'}">Chờ cửa hàng xác nhận</c:when>
                  <c:when test="${request.trangThai == 'CHAP_NHAN'}">Đã tiếp nhận</c:when>
                  <c:otherwise>Không được chấp nhận</c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>
  <a class="btn btn-default" href="<c:url value='/don-hang' />">Quay lại lịch sử đơn hàng</a>
</section>

<jsp:include page="/WEB-INF/view/user/Footer_user.jsp"></jsp:include>
