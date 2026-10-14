<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="/WEB-INF/view/admin/Header_admin.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/admin/Menu_admin.jsp"></jsp:include>

<div id="layoutSidenav_content">
  <main class="container-fluid">
    <div class="d-flex flex-wrap align-items-center justify-content-between mt-4">
      <h1>Quản lý sản phẩm</h1>
      <button class="btn btn-primary mb-2" id="open-product-modal" type="button">Thêm sản phẩm</button>
    </div>
    <c:if test="${not empty formError}">
      <div class="alert alert-danger" role="alert"><c:out value="${formError}" /></div>
    </c:if>
    <c:if test="${param.error == 'save'}">
      <div class="alert alert-danger" role="alert">Không thể lưu sản phẩm. Vui lòng kiểm tra dữ liệu và thử lại.</div>
    </c:if>
    <c:if test="${param.error == 'delete'}">
      <div class="alert alert-danger" role="alert">Không thể xóa sản phẩm đang được tham chiếu bởi dữ liệu khác. Hãy ẩn sản phẩm thay vì xóa.</div>
    </c:if>
    <c:if test="${param.saved == 'true'}">
      <div class="alert alert-success" role="status">Đã lưu sản phẩm thành công.</div>
    </c:if>
    <c:if test="${param.deleted == 'true'}">
      <div class="alert alert-success" role="status">Đã xóa sản phẩm thành công.</div>
    </c:if>
    <div id="product-delete-result" class="alert" role="status" hidden></div>

    <section class="card my-4">
      <div class="card-header d-flex flex-wrap align-items-center justify-content-between">
        <span>Danh sách sản phẩm</span>
        <span class="text-muted small">Đang hiển thị <strong id="product-count">${fn:length(listSanPhamss)}</strong> sản phẩm</span>
      </div>
      <div class="card-body">
        <div class="form-row mb-3">
          <div class="col-md-5 mb-2">
            <label class="sr-only" for="product-search">Tìm sản phẩm</label>
            <input class="form-control" id="product-search" type="search" placeholder="Tìm theo tên sản phẩm">
          </div>
          <div class="col-md-4 mb-2">
            <label class="sr-only" for="product-category-filter">Lọc danh mục</label>
            <select class="form-control" id="product-category-filter">
              <option value="">Tất cả danh mục</option>
              <c:forEach var="category" items="${danhMucSanPhams}">
                <option value="${category.madanhmucsanpham}"><c:out value="${category.tendanhmuc}" /></option>
              </c:forEach>
            </select>
          </div>
          <div class="col-md-3 mb-2">
            <label class="sr-only" for="product-status-filter">Lọc trạng thái</label>
            <select class="form-control" id="product-status-filter">
              <option value="">Mọi trạng thái</option>
              <option value="active">Đang bán</option>
              <option value="hidden">Đang ẩn</option>
            </select>
          </div>
        </div>
        <div class="d-flex flex-wrap align-items-center justify-content-between mb-3">
          <button class="btn btn-danger btn-sm mb-2" id="delete-selected" type="button" disabled>Xóa sản phẩm đã chọn</button>
          <label class="mb-2">
            Số dòng mỗi trang
            <select class="form-control form-control-sm d-inline-block ml-1" id="product-page-size" style="width:auto">
              <option value="10">10</option>
              <option value="25">25</option>
              <option value="50">50</option>
            </select>
          </label>
        </div>
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead>
              <tr>
                <th><input type="checkbox" id="select-visible-products" aria-label="Chọn sản phẩm trên trang hiện tại"></th>
                <th>Mã</th>
                <th>Ảnh</th>
                <th>Tên</th>
                <th>Danh mục</th>
                <th>Giá</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody id="products-table">
              <c:forEach var="product" items="${listSanPhamss}">
                <c:url var="productImageUrl" value="/resourcess/user/images/shop/${product.hinhsanpham}" />
                <tr data-name="<c:out value='${product.tensanpham}' />"
                    data-category="${product.danhMucSanPham.madanhmucsanpham}"
                    data-status="${product.hiden ? 'hidden' : 'active'}"
                    data-price="<c:out value='${product.giatien}' />">
                  <td><input type="checkbox" class="product-select" value="${product.masanpham}" aria-label="Chọn sản phẩm ${product.masanpham}"></td>
                  <td><c:out value="${product.masanpham}" /></td>
                  <td>
                    <img class="product-thumbnail"
                         src="<c:out value='${productImageUrl}' />"
                         data-default-src="<c:url value='/resourcess/user/images/shop/product12.jpg' />"
                         alt="Ảnh <c:out value='${product.tensanpham}' />"
                         width="64" height="64" loading="lazy">
                  </td>
                  <td><c:out value="${product.tensanpham}" /></td>
                  <td><c:out value="${product.danhMucSanPham.tendanhmuc}" /></td>
                  <td class="text-nowrap"><span class="formatted-price"><c:out value="${product.giatien}" /></span> ₫</td>
                  <td><c:out value="${product.hiden ? 'Đang ẩn' : 'Đang bán'}" /></td>
                  <td class="text-nowrap">
                    <a class="btn btn-info btn-sm mb-1" href="<c:url value='/admin/product/detal/${product.masanpham}' />">Sửa</a>
                    <form method="post" action="<c:url value='/admin/product/${product.masanpham}/visibility' />">
                      <input type="hidden" name="hidden" value="${!product.hiden}">
                      <button class="btn btn-secondary btn-sm mb-1" type="submit"><c:out value="${product.hiden ? 'Đã Ẩn' : 'Hiện bán'}" /></button>
                    </form>
                    <form method="post" action="<c:url value='/admin/product/${product.masanpham}/delete' />" onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">
                      <button class="btn btn-danger btn-sm mb-1" type="submit">Xóa</button>
                    </form>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
        <p id="product-empty-filter" class="text-center text-muted" hidden>Không tìm thấy sản phẩm phù hợp.</p>
        <nav aria-label="Phân trang sản phẩm">
          <ul class="pagination justify-content-center mb-0">
            <li class="page-item" id="previous-page-item"><button class="page-link" id="previous-page" type="button">Trước</button></li>
            <li class="page-item disabled"><span class="page-link" id="product-page-info">Trang 1 / 1</span></li>
            <li class="page-item" id="next-page-item"><button class="page-link" id="next-page" type="button">Sau</button></li>
          </ul>
        </nav>
      </div>
    </section>
  </main>
</div>

<dialog id="product-modal"
        data-open-on-load="${editingProduct or not empty formError}"
        data-editing="${editingProduct}"
        aria-labelledby="product-modal-title">
  <div class="product-modal-content">
    <div class="d-flex align-items-center justify-content-between mb-3">
      <h2 class="h4 mb-0" id="product-modal-title">
        <c:choose>
          <c:when test="${editingProduct}">Cập nhật sản phẩm</c:when>
          <c:otherwise>Thêm sản phẩm mới</c:otherwise>
        </c:choose>
      </h2>
      <button class="close" id="close-product-modal" type="button" aria-label="Đóng">&times;</button>
    </div>
    <form method="post" enctype="multipart/form-data" action="<c:url value='/admin/product/save' />">
      <c:if test="${not empty sanphamRS.masanpham}">
        <input type="hidden" name="masanpham" value="${sanphamRS.masanpham}">
      </c:if>
      <div class="form-group">
        <label for="tensanpham">Tên sản phẩm</label>
        <input class="form-control" id="tensanpham" name="tensanpham" maxlength="100" required value="<c:out value='${sanphamRS.tensanpham}' />">
      </div>
      <div class="form-group">
        <label for="madanhmucsanpham">Danh mục</label>
        <select class="form-control" id="madanhmucsanpham" name="madanhmucsanpham" required>
          <c:forEach var="category" items="${danhMucSanPhams}">
            <option value="${category.madanhmucsanpham}" <c:if test="${sanphamRS.danhMucSanPham.madanhmucsanpham == category.madanhmucsanpham}">selected</c:if>><c:out value="${category.tendanhmuc}" /></option>
          </c:forEach>
        </select>
      </div>
      <div class="form-group">
        <label for="giatien">Giá (VNĐ)</label>
        <input class="form-control" type="text" inputmode="numeric" pattern="[0-9]+" id="giatien" name="giatien" required value="<c:out value='${sanphamRS.giatien}' />">
      </div>
      <div class="form-group">
        <label for="mota">Mô tả</label>
        <textarea class="form-control" id="mota" name="mota" maxlength="500"><c:out value="${sanphamRS.mota}" /></textarea>
      </div>
      <div class="form-group">
        <label for="doituong">Đối tượng</label>
        <input class="form-control" id="doituong" name="doituong" maxlength="100" required value="<c:out value='${sanphamRS.doituong}' />">
      </div>
      <div class="form-group">
        <label for="imageFile">Ảnh sản phẩm</label>
        <div class="mb-2">
          <img id="product-image-preview"
               src="<c:url value='/resourcess/user/images/shop/${sanphamRS.hinhsanpham}' />"
               data-default-src="<c:url value='/resourcess/user/images/shop/product12.jpg' />"
               alt="Xem trước ảnh sản phẩm" class="img-thumbnail" width="100" height="100">
        </div>
        <input class="form-control-file" type="file" id="imageFile" name="imageFile" accept="image/jpeg,image/png,image/gif,image/webp">
        <small class="form-text text-muted">JPG, PNG, GIF hoặc WebP; tối đa 5 MB. Không chọn ảnh mới sẽ giữ ảnh hiện tại.</small>
        <input type="hidden" name="hinhsanpham" value="<c:out value='${sanphamRS.hinhsanpham}' />">
      </div>
      <c:if test="${not editingProduct}">
        <div class="form-row">
          <div class="form-group col-md-4">
            <label for="masize">Size</label>
            <select class="form-control" id="masize" name="masize" required>
              <c:forEach var="size" items="${sizeSanPhams}">
                <option value="${size.masize}" <c:if test="${selectedSizeId == size.masize}">selected</c:if>><c:out value="${size.size}" /></option>
              </c:forEach>
            </select>
          </div>
          <div class="form-group col-md-4">
            <label for="mamau">Màu</label>
            <select class="form-control" id="mamau" name="mamau" required>
              <c:forEach var="color" items="${mauSanPhams}">
                <option value="${color.mamau}" <c:if test="${selectedColorId == color.mamau}">selected</c:if>><c:out value="${color.tenmau}" /></option>
              </c:forEach>
            </select>
          </div>
          <div class="form-group col-md-4">
            <label for="soluong">Số lượng ban đầu</label>
            <input class="form-control" type="number" id="soluong" name="soluong" min="0" required value="<c:out value='${empty initialQuantity ? 0 : initialQuantity}' />">
          </div>
        </div>
      </c:if>
      <div class="d-flex justify-content-end">
        <button class="btn btn-secondary mr-2" id="cancel-product-modal" type="button">Hủy</button>
        <button class="btn btn-primary" type="submit">
          <c:choose>
            <c:when test="${editingProduct}">Lưu thay đổi</c:when>
            <c:otherwise>Thêm sản phẩm</c:otherwise>
          </c:choose>
        </button>
      </div>
    </form>
  </div>
</dialog>

<style>
  .product-thumbnail { object-fit: cover; border-radius: .25rem; }
  #product-modal { border: 0; border-radius: .4rem; box-shadow: 0 1rem 3rem rgba(0,0,0,.3); max-height: 90vh; max-width: 680px; overflow-y: auto; padding: 0; width: calc(100% - 2rem); }
  #product-modal::backdrop { background: rgba(0,0,0,.55); }
  .product-modal-content { padding: 1.5rem; }
</style>

<script>
  (function () {
    const search = document.getElementById('product-search');
    const category = document.getElementById('product-category-filter');
    const status = document.getElementById('product-status-filter');
    const allRows = Array.from(document.querySelectorAll('#products-table tr[data-name]'));
    const empty = document.getElementById('product-empty-filter');
    const count = document.getElementById('product-count');
    const pageSize = document.getElementById('product-page-size');
    const pageInfo = document.getElementById('product-page-info');
    const previousPageItem = document.getElementById('previous-page-item');
    const nextPageItem = document.getElementById('next-page-item');
    const selectVisible = document.getElementById('select-visible-products');
    const deleteSelected = document.getElementById('delete-selected');
    const modal = document.getElementById('product-modal');
    const imageInput = document.getElementById('imageFile');
    const imagePreview = document.getElementById('product-image-preview');
    let currentPage = 1;
    let filteredRows = allRows;

    function selectedProducts() {
      return Array.from(document.querySelectorAll('.product-select:checked'));
    }

    function updateSelectionControls() {
      const selectedCount = selectedProducts().length;
      deleteSelected.disabled = selectedCount === 0;
      deleteSelected.textContent = selectedCount
        ? 'Xóa ' + selectedCount + ' sản phẩm đã chọn'
        : 'Xóa sản phẩm đã chọn';
      const visibleChecks = Array.from(document.querySelectorAll('.product-select'))
        .filter(function (checkbox) { return !checkbox.closest('tr').hidden; });
      selectVisible.checked = visibleChecks.length > 0 && visibleChecks.every(function (checkbox) { return checkbox.checked; });
      selectVisible.indeterminate = visibleChecks.some(function (checkbox) { return checkbox.checked; }) && !selectVisible.checked;
    }

    function renderPage() {
      const size = Number(pageSize.value);
      const totalPages = Math.max(1, Math.ceil(filteredRows.length / size));
      currentPage = Math.min(currentPage, totalPages);
      const start = (currentPage - 1) * size;
      const pageRows = new Set(filteredRows.slice(start, start + size));
      allRows.forEach(function (row) { row.hidden = !pageRows.has(row); });
      count.textContent = String(filteredRows.length);
      pageInfo.textContent = 'Trang ' + currentPage + ' / ' + totalPages;
      previousPageItem.classList.toggle('disabled', currentPage <= 1);
      nextPageItem.classList.toggle('disabled', currentPage >= totalPages);
      empty.hidden = filteredRows.length !== 0;
      updateSelectionControls();
    }

    function filterProducts() {
      const query = search.value.trim().toLocaleLowerCase();
      filteredRows = allRows.filter(function (row) {
        return row.dataset.name.toLocaleLowerCase().includes(query)
          && (!category.value || row.dataset.category === category.value)
          && (!status.value || row.dataset.status === status.value);
      });
      currentPage = 1;
      renderPage();
    }

    function closeModal() {
      if (modal.open) modal.close();
    }

    document.querySelectorAll('.formatted-price').forEach(function (price) {
      price.textContent = price.textContent.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    });
    document.querySelectorAll('.product-thumbnail').forEach(function (image) {
      image.addEventListener('error', function () {
        image.src = image.dataset.defaultSrc;
      }, { once: true });
    });

    search.addEventListener('input', filterProducts);
    category.addEventListener('change', filterProducts);
    status.addEventListener('change', filterProducts);
    pageSize.addEventListener('change', function () { currentPage = 1; renderPage(); });
    document.getElementById('previous-page').addEventListener('click', function () {
      if (currentPage > 1) { currentPage -= 1; renderPage(); }
    });
    document.getElementById('next-page').addEventListener('click', function () {
      if (currentPage < Math.ceil(filteredRows.length / Number(pageSize.value))) { currentPage += 1; renderPage(); }
    });
    document.querySelectorAll('.product-select').forEach(function (checkbox) {
      checkbox.addEventListener('change', updateSelectionControls);
    });
    selectVisible.addEventListener('change', function () {
      document.querySelectorAll('.product-select').forEach(function (checkbox) {
        if (!checkbox.closest('tr').hidden) checkbox.checked = selectVisible.checked;
      });
      updateSelectionControls();
    });
    deleteSelected.addEventListener('click', function () {
      const ids = selectedProducts().map(function (checkbox) { return checkbox.value; });
      if (!ids.length || !window.confirm('Bạn có chắc muốn xóa ' + ids.length + ' sản phẩm đã chọn?')) return;
      fetch('<c:url value="/admin/product/delete" />', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
        body: new URLSearchParams({ listIdSanPham: ids.join(',') })
      }).then(function (response) {
        return response.json().then(function (result) {
          if (!response.ok) throw new Error(result.message || 'Không thể gửi yêu cầu xóa sản phẩm.');
          return result;
        });
      }).then(function (result) {
        const deletedIds = new Set((result.deletedIds || []).map(String));
        if (deletedIds.size) {
          for (let index = allRows.length - 1; index >= 0; index -= 1) {
            const checkbox = allRows[index].querySelector('.product-select');
            if (checkbox && deletedIds.has(checkbox.value)) {
              allRows[index].remove();
              allRows.splice(index, 1);
            }
          }
          filteredRows = filteredRows.filter(function (row) {
            return !deletedIds.has(row.querySelector('.product-select').value);
          });
        }
        const failures = Object.entries(result.failedIds || {});
        const resultMessages = [];
        if (deletedIds.size) resultMessages.push('Đã xóa ' + deletedIds.size + ' sản phẩm.');
        if (failures.length) {
          resultMessages.push('Không thể xóa: ' + failures.map(function (entry) {
            return entry[0] + ' (' + entry[1] + ')';
          }).join('; '));
        }
        const resultBox = document.getElementById('product-delete-result');
        resultBox.className = failures.length ? 'alert alert-warning' : 'alert alert-success';
        resultBox.textContent = resultMessages.join(' ');
        resultBox.hidden = false;
        renderPage();
      }).catch(function (error) {
        window.alert(error.message);
      });
    });

    document.getElementById('open-product-modal').addEventListener('click', function () {
      if (!modal.open) modal.showModal();
      document.getElementById('tensanpham').focus();
    });
    document.getElementById('close-product-modal').addEventListener('click', closeModal);
    document.getElementById('cancel-product-modal').addEventListener('click', closeModal);
    imageInput.addEventListener('change', function () {
      const file = imageInput.files && imageInput.files[0];
      if (!file) return;
      imagePreview.src = URL.createObjectURL(file);
    });
    imagePreview.addEventListener('error', function () {
      imagePreview.src = imagePreview.dataset.defaultSrc;
    }, { once: true });
    if (modal.dataset.openOnLoad === 'true') modal.showModal();
    filterProducts();
  })();
</script>
<jsp:include page="/WEB-INF/view/admin/Footer_admin.jsp"></jsp:include>
