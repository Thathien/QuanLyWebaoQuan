<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="/WEB-INF/view/admin/Header_admin.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/admin/Menu_admin.jsp"></jsp:include>

<div id="layoutSidenav_content">
  <main class="container-fluid">
    <h1 class="mt-4">Yêu cầu đổi/trả hàng</h1>
    <c:if test="${not empty returnResult}">
      <div class="alert alert-success" role="status"><c:out value="${returnResult}" /></div>
    </c:if>
    <c:if test="${not empty returnError}">
      <div class="alert alert-danger" role="alert"><c:out value="${returnError}" /></div>
    </c:if>
    <div class="card">
      <div class="card-header">Danh sách yêu cầu (<c:out value="${fn:length(returnRequests)}" />)</div>
      <div class="card-body table-responsive">
        <table class="table table-bordered table-hover">
          <thead>
            <tr><th>Mã yêu cầu</th><th>Đơn hàng</th><th>Khách hàng</th><th>Ngày gửi</th><th>Loại</th><th>Sản phẩm</th><th>Lý do</th><th>Trạng thái / xử lý</th></tr>
          </thead>
          <tbody>
            <c:forEach var="request" items="${returnRequests}">
              <tr>
                <td><c:out value="${request.id}" /></td>
                <td><a href="<c:url value='/admin/orders' />">#<c:out value="${request.hoaDon.mahoadon}" /></a></td>
                <td><c:out value="${request.hoaDon.tenkhachhang}" /><br><c:out value="${request.hoaDon.emailkhachhang}" /></td>
                <td><c:out value="${request.createdAt}" /></td>
                <td><c:out value="${request.loaiYeuCau == 'DOI' ? 'Đổi hàng' : 'Trả hàng'}" /></td>
                <td>
                  <ul>
                    <c:forEach var="line" items="${request.chiTiet}">
                      <li>
                        <c:out value="${line.bienThe.sanPham.tensanpham}" />
                        (<c:out value="${line.bienThe.mauSanPham.tenmau}" /> /
                        <c:out value="${line.bienThe.sizeSanPham.size}" />)
                        — SL: <c:out value="${line.soLuong}" />
                        <c:if test="${not empty line.bienTheThayThe}">
                          → Đổi sang <c:out value="${line.bienTheThayThe.mauSanPham.tenmau}" /> /
                          <c:out value="${line.bienTheThayThe.sizeSanPham.size}" />
                        </c:if>
                      </li>
                    </c:forEach>
                  </ul>
                </td>
                <td><c:out value="${request.lyDo}" /></td>
                <td>
                  <c:choose>
                    <c:when test="${request.trangThai == 'CHO_XU_LY'}">
                      <form method="post" action="<c:url value='/admin/doitra/${request.id}/trang-thai' />">
                        <button class="btn btn-success btn-sm" name="trangThai" value="CHAP_NHAN" type="submit">Tiếp nhận</button>
                        <button class="btn btn-danger btn-sm" name="trangThai" value="TU_CHOI" type="submit">Từ chối</button>
                      </form>
                    </c:when>
                    <c:when test="${request.trangThai == 'CHAP_NHAN'}">Đã tiếp nhận</c:when>
                    <c:otherwise>Đã từ chối</c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty returnRequests}">
              <tr><td colspan="8" class="text-center text-muted">Chưa có yêu cầu đổi/trả.</td></tr>
            </c:if>
          </tbody>
        </table>
      </div>
    </div>
  </main>
</div>
<jsp:include page="/WEB-INF/view/admin/Footer_admin.jsp"></jsp:include>
