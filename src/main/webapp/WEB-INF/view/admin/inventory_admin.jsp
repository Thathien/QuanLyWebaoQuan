<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/view/admin/Header_admin.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/admin/Menu_admin.jsp"></jsp:include>

<div id="layoutSidenav_content">
  <main class="container-fluid">
    <h1 class="mt-4">Nhập kho và tồn kho</h1>
    <div id="inventory-message" class="alert" role="status" hidden></div>
    <div class="row">
      <div class="col-lg-6">
        <section class="card my-3">
          <div class="card-header">Tạo phiếu nhập hàng</div>
          <div class="card-body">
            <form id="receipt-form">
              <div class="form-group">
                <label for="receipt-supplier">Nhà cung cấp</label>
                <select class="form-control" id="receipt-supplier" required></select>
              </div>
              <div class="d-flex align-items-center justify-content-between mb-2">
                <strong>Sản phẩm nhập</strong>
                <button class="btn btn-outline-primary btn-sm" id="add-receipt-line" type="button">Thêm dòng hàng</button>
              </div>
              <div id="receipt-lines"></div>
              <button class="btn btn-primary mt-2" type="submit">Lưu phiếu nhập nháp</button>
              <p class="form-text text-muted mb-0">Kho và giá vốn chỉ được cập nhật khi xác nhận phiếu nhập.</p>
            </form>
          </div>
        </section>
        <section class="card my-3">
          <div class="card-header">Thêm nhà cung cấp</div>
          <div class="card-body">
            <form id="supplier-form">
              <div class="form-group">
                <label for="supplier-name">Tên nhà cung cấp</label>
                <input class="form-control" id="supplier-name" maxlength="150" required>
              </div>
              <div class="form-row">
                <div class="form-group col-md-6">
                  <label for="supplier-email">Email</label>
                  <input class="form-control" type="email" id="supplier-email" maxlength="255">
                </div>
                <div class="form-group col-md-6">
                  <label for="supplier-phone">Số điện thoại</label>
                  <input class="form-control" id="supplier-phone" maxlength="50">
                </div>
              </div>
              <div class="form-group">
                <label for="supplier-address">Địa chỉ</label>
                <input class="form-control" id="supplier-address" maxlength="255">
              </div>
              <button class="btn btn-outline-primary" type="submit">Lưu nhà cung cấp</button>
            </form>
          </div>
        </section>
      </div>
      <div class="col-lg-6">
        <section class="card my-3">
          <div class="card-header">Điều chỉnh tồn kho</div>
          <div class="card-body">
            <form id="adjustment-form">
              <div class="form-group">
                <label for="adjustment-variant">Biến thể sản phẩm</label>
                <select class="form-control variant-select" id="adjustment-variant" required></select>
              </div>
              <div class="form-row">
                <div class="form-group col-md-6">
                  <label for="adjustment-quantity">Số lượng thay đổi (+/-)</label>
                  <input class="form-control" type="number" id="adjustment-quantity" required min="-2147483648" max="2147483647" value="1">
                </div>
                <div class="form-group col-md-6">
                  <label for="adjustment-cost">Đơn giá vốn nếu tăng kho</label>
                  <input class="form-control" type="number" id="adjustment-cost" min="0" step="0.01">
                </div>
              </div>
              <div class="form-group">
                <label for="adjustment-reason">Lý do</label>
                <input class="form-control" id="adjustment-reason" maxlength="255" required>
              </div>
              <button class="btn btn-outline-primary" type="submit">Ghi nhận điều chỉnh</button>
            </form>
          </div>
        </section>
        <section class="card my-3">
          <div class="card-header">Thiết lập tồn đầu kỳ</div>
          <div class="card-body">
            <form id="opening-form">
              <div class="form-group">
                <label for="opening-variant">Biến thể chưa có lịch sử kho</label>
                <select class="form-control variant-select" id="opening-variant" required></select>
              </div>
              <div class="form-row">
                <div class="form-group col-md-6">
                  <label for="opening-quantity">Số lượng tồn</label>
                  <input class="form-control" type="number" id="opening-quantity" min="0" required value="0">
                </div>
                <div class="form-group col-md-6">
                  <label for="opening-cost">Giá vốn bình quân</label>
                  <input class="form-control" type="number" id="opening-cost" min="0" step="0.01" required>
                </div>
              </div>
              <button class="btn btn-outline-primary" type="submit">Thiết lập tồn đầu kỳ</button>
              <p class="form-text text-muted mb-0">Chỉ dùng cho biến thể chưa có bất kỳ biến động kho nào.</p>
            </form>
          </div>
        </section>
      </div>
    </div>

    <section class="card my-3">
      <div class="card-header">Các phiếu nhập gần đây</div>
      <div class="card-body table-responsive">
        <table class="table table-bordered table-hover">
          <thead>
            <tr><th>Mã phiếu</th><th>Nhà cung cấp</th><th>Ngày tạo</th><th>Sản phẩm</th><th>Tổng tiền nhập</th><th>Trạng thái / thao tác</th></tr>
          </thead>
          <tbody id="receipts-table">
            <tr><td colspan="6" class="text-center text-muted">Đang tải phiếu nhập…</td></tr>
          </tbody>
        </table>
      </div>
    </section>
    <section class="card my-3">
      <div class="card-header">Tồn kho theo biến thể</div>
      <div class="card-body table-responsive">
        <table class="table table-bordered table-hover">
          <thead><tr><th>Mã biến thể</th><th>Sản phẩm</th><th>Size</th><th>Màu</th><th>Tồn</th><th>Giá vốn bình quân</th></tr></thead>
          <tbody id="variants-table">
            <tr><td colspan="6" class="text-center text-muted">Đang tải tồn kho…</td></tr>
          </tbody>
        </table>
      </div>
    </section>
    <section class="card my-3 mb-5">
      <div class="card-header">Lịch sử biến động kho gần đây</div>
      <div class="card-body table-responsive">
        <table class="table table-bordered table-hover">
          <thead><tr><th>Thời điểm</th><th>Sản phẩm</th><th>Mã biến thể</th><th>Loại</th><th>Thay đổi</th><th>Đơn giá vốn</th><th>Tham chiếu</th></tr></thead>
          <tbody id="movements-table">
            <tr><td colspan="7" class="text-center text-muted">Đang tải lịch sử…</td></tr>
          </tbody>
        </table>
      </div>
    </section>
  </main>
</div>

<style>
  .receipt-line { border: 1px solid #dee2e6; border-radius: .25rem; padding: .75rem; margin-bottom: .75rem; }
  .receipt-line .form-group { margin-bottom: .5rem; }
</style>

<script>
  (function () {
    const apiUrls = {
      suppliers: '<c:url value="/api/inventory/suppliers" />',
      variants: '<c:url value="/api/inventory/variants" />',
      receipts: '<c:url value="/api/inventory/receipts" />',
      movements: '<c:url value="/api/reports/inventory/movements?size=20&page=0" />',
      adjustments: '<c:url value="/api/inventory/adjustments" />',
      openingBalances: '<c:url value="/api/inventory/opening-balances" />'
    };
    let suppliers = [];
    let variants = [];
    const message = document.getElementById('inventory-message');
    const priceFormat = new Intl.NumberFormat('en-US', { maximumFractionDigits: 2 });

    function notify(text, isError) {
      message.textContent = text;
      message.className = 'alert ' + (isError ? 'alert-danger' : 'alert-success');
      message.hidden = false;
      message.scrollIntoView({ block: 'nearest' });
    }

    async function request(url, options) {
      const response = await fetch(url, options || {});
      const responseText = await response.text();
      let data = null;
      if (responseText) {
        try { data = JSON.parse(responseText); } catch (error) { data = responseText; }
      }
      if (!response.ok) {
        const errorMessage = data && data.message ? data.message : 'Yêu cầu thất bại (' + response.status + ').';
        throw new Error(errorMessage);
      }
      return data;
    }

    function cell(row, value) {
      const element = document.createElement('td');
      element.textContent = value == null ? '' : String(value);
      row.appendChild(element);
      return element;
    }

    function emptyRow(body, colspan, text) {
      const row = document.createElement('tr');
      const item = cell(row, text);
      item.colSpan = colspan;
      item.className = 'text-center text-muted';
      body.replaceChildren(row);
    }

    function formatMoney(value) {
      return value == null ? '—' : priceFormat.format(Number(value)) + ' ₫';
    }

    function variantLabel(variant) {
      return '#' + variant.variantId + ' · ' + variant.productName + ' · '
        + variant.size + ' · ' + variant.color + ' · tồn ' + variant.quantityOnHand;
    }

    function fillVariantSelect(select) {
      select.replaceChildren();
      const options = select.id === 'opening-variant'
        ? variants.filter(function (variant) { return variant.canSetOpeningBalance; })
        : variants;
      options.forEach(function (variant) {
        const option = document.createElement('option');
        option.value = String(variant.variantId);
        option.textContent = variantLabel(variant);
        select.appendChild(option);
      });
      if (options.length === 0) {
        const option = document.createElement('option');
        option.value = '';
        option.textContent = select.id === 'opening-variant'
          ? 'Không có biến thể phù hợp để thiết lập tồn đầu kỳ'
          : 'Chưa có biến thể sản phẩm';
        select.appendChild(option);
      }
    }

    function appendReceiptLine() {
      const template = document.getElementById('receipt-line-template');
      const line = template.content.firstElementChild.cloneNode(true);
      fillVariantSelect(line.querySelector('select'));
      line.querySelector('.remove-receipt-line').addEventListener('click', function () {
        if (document.querySelectorAll('.receipt-line').length > 1) line.remove();
      });
      document.getElementById('receipt-lines').appendChild(line);
    }

    async function loadSuppliers() {
      suppliers = await request(apiUrls.suppliers);
      const select = document.getElementById('receipt-supplier');
      select.replaceChildren();
      suppliers.forEach(function (supplier) {
        const option = document.createElement('option');
        option.value = String(supplier.id);
        option.textContent = supplier.ten;
        select.appendChild(option);
      });
      if (suppliers.length === 0) {
        const option = document.createElement('option');
        option.value = '';
        option.textContent = 'Hãy thêm nhà cung cấp trước';
        select.appendChild(option);
      }
    }

    async function loadVariants() {
      variants = await request(apiUrls.variants);
      document.querySelectorAll('.variant-select').forEach(fillVariantSelect);
      document.getElementById('receipt-lines').replaceChildren();
      appendReceiptLine();
      const body = document.getElementById('variants-table');
      if (variants.length === 0) {
        emptyRow(body, 6, 'Chưa có biến thể sản phẩm.');
        return;
      }
      body.replaceChildren();
      variants.forEach(function (variant) {
        const row = document.createElement('tr');
        cell(row, variant.variantId);
        cell(row, variant.productName);
        cell(row, variant.size);
        cell(row, variant.color);
        cell(row, variant.quantityOnHand);
        cell(row, formatMoney(variant.averageUnitCost));
        body.appendChild(row);
      });
    }

    async function loadReceipts() {
      const receipts = await request(apiUrls.receipts);
      const body = document.getElementById('receipts-table');
      if (!receipts.length) {
        emptyRow(body, 6, 'Chưa có phiếu nhập.');
        return;
      }
      body.replaceChildren();
      receipts.forEach(function (receipt) {
        const row = document.createElement('tr');
        cell(row, receipt.id);
        cell(row, receipt.supplierName);
        cell(row, receipt.createdAt ? new Date(receipt.createdAt).toLocaleString('vi-VN') : '—');
        const lines = receipt.items || [];
        const details = lines.map(function (line) {
          return line.productName + ' / ' + line.size + ' / ' + line.color + ' × ' + line.quantity;
        }).join('; ');
        cell(row, details || 'Không có dòng hàng');
        const total = lines.reduce(function (sum, line) {
          return sum + Number(line.unitCost || 0) * line.quantity;
        }, 0);
        cell(row, formatMoney(total));
        const actions = document.createElement('td');
        const status = document.createElement('span');
        status.className = 'badge badge-' + (receipt.status === 'CONFIRMED' ? 'success' : receipt.status === 'CANCELLED' ? 'secondary' : 'warning');
        status.textContent = receipt.status;
        actions.appendChild(status);
        if (receipt.status === 'DRAFT') {
          const confirmButton = document.createElement('button');
          confirmButton.type = 'button';
          confirmButton.className = 'btn btn-success btn-sm ml-2';
          confirmButton.textContent = 'Xác nhận nhập';
          confirmButton.dataset.action = 'confirm';
          confirmButton.dataset.id = String(receipt.id);
          actions.appendChild(confirmButton);
          const cancelButton = document.createElement('button');
          cancelButton.type = 'button';
          cancelButton.className = 'btn btn-outline-danger btn-sm ml-1';
          cancelButton.textContent = 'Hủy phiếu';
          cancelButton.dataset.action = 'cancel';
          cancelButton.dataset.id = String(receipt.id);
          actions.appendChild(cancelButton);
        }
        row.appendChild(actions);
        body.appendChild(row);
      });
    }

    async function loadMovements() {
      const result = await request(apiUrls.movements);
      const body = document.getElementById('movements-table');
      if (!result.items || !result.items.length) {
        emptyRow(body, 7, 'Chưa có biến động kho.');
        return;
      }
      body.replaceChildren();
      result.items.forEach(function (movement) {
        const row = document.createElement('tr');
        cell(row, movement.occurredAt ? new Date(movement.occurredAt).toLocaleString('vi-VN') : '—');
        cell(row, movement.productName);
        cell(row, movement.variantId);
        cell(row, movement.movementType);
        cell(row, movement.quantityDelta);
        cell(row, formatMoney(movement.unitCost));
        cell(row, movement.reference);
        body.appendChild(row);
      });
    }

    async function refreshData() {
      try {
        await Promise.all([loadSuppliers(), loadVariants(), loadReceipts(), loadMovements()]);
      } catch (error) {
        notify(error.message, true);
      }
    }

    document.getElementById('add-receipt-line').addEventListener('click', appendReceiptLine);
    document.getElementById('supplier-form').addEventListener('submit', async function (event) {
      event.preventDefault();
      try {
        await request(apiUrls.suppliers, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            ten: document.getElementById('supplier-name').value.trim(),
            email: document.getElementById('supplier-email').value.trim(),
            soDienThoai: document.getElementById('supplier-phone').value.trim(),
            diaChi: document.getElementById('supplier-address').value.trim()
          })
        });
        event.currentTarget.reset();
        notify('Đã thêm nhà cung cấp.', false);
        await loadSuppliers();
      } catch (error) {
        notify(error.message, true);
      }
    });
    document.getElementById('receipt-form').addEventListener('submit', async function (event) {
      event.preventDefault();
      const items = Array.from(document.querySelectorAll('.receipt-line')).map(function (line) {
        return {
          variantId: Number(line.querySelector('[data-field="variant"]').value),
          quantity: Number(line.querySelector('[data-field="quantity"]').value),
          unitCost: Number(line.querySelector('[data-field="unit-cost"]').value)
        };
      });
      try {
        await request(apiUrls.receipts, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            supplierId: Number(document.getElementById('receipt-supplier').value),
            items: items
          })
        });
        notify('Đã lưu phiếu nhập nháp. Hãy xác nhận phiếu để cập nhật tồn kho.', false);
        await Promise.all([loadReceipts(), loadVariants()]);
      } catch (error) {
        notify(error.message, true);
      }
    });
    document.getElementById('receipts-table').addEventListener('click', async function (event) {
      const button = event.target.closest('button[data-action]');
      if (!button) return;
      const id = encodeURIComponent(button.dataset.id);
      try {
        if (button.dataset.action === 'confirm') {
          await request('<c:url value="/api/inventory/receipts/" />' + id + '/confirm', { method: 'POST' });
          notify('Đã xác nhận phiếu nhập và cập nhật tồn kho.', false);
        } else if (window.confirm('Bạn có chắc muốn hủy phiếu nhập nháp này?')) {
          await request('<c:url value="/api/inventory/receipts/" />' + id, { method: 'DELETE' });
          notify('Đã hủy phiếu nhập nháp.', false);
        } else {
          return;
        }
        await Promise.all([loadReceipts(), loadVariants(), loadMovements()]);
      } catch (error) {
        notify(error.message, true);
      }
    });
    document.getElementById('adjustment-form').addEventListener('submit', async function (event) {
      event.preventDefault();
      const quantityDelta = Number(document.getElementById('adjustment-quantity').value);
      const unitCostValue = document.getElementById('adjustment-cost').value;
      try {
        await request(apiUrls.adjustments, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            variantId: Number(document.getElementById('adjustment-variant').value),
            quantityDelta: quantityDelta,
            unitCost: unitCostValue ? Number(unitCostValue) : null,
            reason: document.getElementById('adjustment-reason').value.trim()
          })
        });
        event.currentTarget.reset();
        document.getElementById('adjustment-quantity').value = '1';
        notify('Đã ghi nhận điều chỉnh kho.', false);
        await Promise.all([loadVariants(), loadMovements()]);
      } catch (error) {
        notify(error.message, true);
      }
    });
    document.getElementById('opening-form').addEventListener('submit', async function (event) {
      event.preventDefault();
      try {
        await request(apiUrls.openingBalances, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            items: [{
              variantId: Number(document.getElementById('opening-variant').value),
              quantity: Number(document.getElementById('opening-quantity').value),
              unitCost: Number(document.getElementById('opening-cost').value)
            }]
          })
        });
        notify('Đã thiết lập tồn đầu kỳ.', false);
        await Promise.all([loadVariants(), loadMovements()]);
      } catch (error) {
        notify(error.message, true);
      }
    });

    refreshData();
  })();
</script>

<template id="receipt-line-template">
  <div class="receipt-line">
    <div class="form-group">
      <label>Sản phẩm / biến thể</label>
      <select class="form-control" data-field="variant" required></select>
    </div>
    <div class="form-row align-items-end">
      <div class="form-group col-md-4">
        <label>Số lượng nhập</label>
        <input class="form-control" type="number" data-field="quantity" min="1" required value="1">
      </div>
      <div class="form-group col-md-5">
        <label>Đơn giá vốn (VNĐ)</label>
        <input class="form-control" type="number" data-field="unit-cost" min="0" step="0.01" required>
      </div>
      <div class="form-group col-md-3">
        <button class="btn btn-outline-danger btn-block remove-receipt-line" type="button">Bỏ dòng</button>
      </div>
    </div>
  </div>
</template>
<jsp:include page="/WEB-INF/view/admin/Footer_admin.jsp"></jsp:include>
