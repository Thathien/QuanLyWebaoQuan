<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/view/user/Header_user.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/user/Menu_user.jsp"></jsp:include>

<section class="container">
  <h2>Lịch sử đơn hàng</h2>
  <c:if test="${not empty orderResult}">
    <div class="alert alert-success" role="status"><c:out value="${orderResult}" /></div>
  </c:if>
  <c:if test="${not empty orderError}">
    <div class="alert alert-danger" role="alert"><c:out value="${orderError}" /></div>
  </c:if>
  <c:choose>
    <c:when test="${empty orders}">
      <p>Bạn chưa có đơn hàng nào.</p>
    </c:when>
    <c:otherwise>
      <div class="table-responsive">
        <table class="table table-bordered">
          <thead>
            <tr><th>Mã đơn</th><th>Ngày đặt</th><th>Hình thức nhận</th><th>Địa chỉ</th><th>Trạng thái</th><th>Thao tác</th></tr>
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
                    <c:when test="${order.tinhtrang == 4}">Đã hủy</c:when>
                    <c:otherwise>Không xác định</c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <a class="btn btn-default btn-sm" href="<c:url value='/don-hang/${order.mahoadon}' />">Chi tiết</a>
                  <c:if test="${order.tinhtrang == 0}">
                    <form method="post" action="<c:url value='/don-hang/${order.mahoadon}/huy' />" style="display:inline">
                      <button class="btn btn-danger btn-sm" type="submit">Hủy đơn</button>
                    </form>
                  </c:if>
                  <c:if test="${order.tinhtrang == 3}">
                    <a class="btn btn-warning btn-sm" href="<c:url value='/don-hang/${order.mahoadon}/doi-tra' />">Yêu cầu đổi/trả</a>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</section>

<jsp:include page="/WEB-INF/view/user/Footer_user.jsp"></jsp:include>
