<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/view/user/Header_user.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/user/Menu_user.jsp"></jsp:include>

<section class="container">
  <h2>Yêu cầu đổi/trả đơn #<c:out value="${order.mahoadon}" /></h2>
  <p>Yêu cầu chỉ áp dụng cho đơn đã hoàn thành trong vòng 30 ngày. Vui lòng chọn sản phẩm và số lượng cần đổi/trả.</p>
  <c:if test="${not empty returnError}">
    <div class="alert alert-danger" role="alert"><c:out value="${returnError}" /></div>
  </c:if>
  <form method="post" action="<c:url value='/don-hang/${order.mahoadon}/doi-tra' />">
    <div class="form-group">
      <label for="loaiYeuCau">Loại yêu cầu</label>
      <select class="form-control" id="loaiYeuCau" name="loaiYeuCau" required>
        <option value="TRA">Trả hàng</option>
        <option value="DOI">Đổi size/màu</option>
      </select>
    </div>
    <div class="table-responsive">
      <table class="table table-bordered">
        <thead>
          <tr><th>Sản phẩm</th><th>Màu / size hiện tại</th><th>SL đã mua</th><th>SL đổi/trả</th><th class="replacement-column">Biến thể muốn đổi sang</th></tr>
        </thead>
        <tbody>
          <c:forEach var="line" items="${order.chiTietHoaDon}">
            <tr>
              <td>
                <c:out value="${line.bienThe.sanPham.tensanpham}" />
                <input type="hidden" name="maBienThe" value="${line.chiTietHoaDonId.machitietsanpham}">
              </td>
              <td><c:out value="${line.bienThe.mauSanPham.tenmau}" /> / <c:out value="${line.bienThe.sizeSanPham.size}" /></td>
              <td><c:out value="${line.soluong}" /></td>
              <td><input class="form-control request-quantity" type="number" name="soLuong" min="0" max="${line.soluong}" value="0" required></td>
              <td class="replacement-column">
                <select class="form-control replacement-variant" name="maBienTheThayThe">
                  <option value="">Chọn biến thể</option>
                  <c:forEach var="variant" items="${replacementVariants[line.chiTietHoaDonId.machitietsanpham]}">
                    <option value="${variant.machitietsanpham}">
                      <c:out value="${variant.mauSanPham.tenmau}" /> / <c:out value="${variant.sizeSanPham.size}" /> (còn <c:out value="${variant.soluong}" />)
                    </option>
                  </c:forEach>
                </select>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <div class="form-group">
      <label for="lyDo">Lý do / mô tả</label>
      <textarea class="form-control" id="lyDo" name="lyDo" maxlength="255" rows="3" required></textarea>
    </div>
    <button class="btn btn-primary" type="submit">Gửi yêu cầu</button>
    <a class="btn btn-default" href="<c:url value='/don-hang/${order.mahoadon}' />">Quay lại đơn hàng</a>
  </form>
</section>
<script>
  (function () {
    const requestType = document.getElementById('loaiYeuCau');
    const columns = document.querySelectorAll('.replacement-column');
    const selects = document.querySelectorAll('.replacement-variant');
    function updateReplacementFields() {
      const isExchange = requestType.value === 'DOI';
      columns.forEach(function (column) { column.hidden = !isExchange; });
      selects.forEach(function (select) {
        select.required = isExchange && Number(select.closest('tr').querySelector('.request-quantity').value) > 0;
      });
    }
    requestType.addEventListener('change', updateReplacementFields);
    document.querySelectorAll('.request-quantity').forEach(function (quantity) {
      quantity.addEventListener('input', updateReplacementFields);
    });
    updateReplacementFields();
  })();
</script>
<jsp:include page="/WEB-INF/view/user/Footer_user.jsp"></jsp:include>
