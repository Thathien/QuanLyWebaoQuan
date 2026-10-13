<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Header_admin.jsp"></jsp:include>
<jsp:include page="Menu_admin.jsp"></jsp:include>

<div id="layoutSidenav_content">
  <main class="container-fluid">
    <h1 class="mt-4">Báo cáo thống kê</h1>
    <p class="text-muted">Dữ liệu doanh thu tính từ đơn hàng hoàn thành trong khoảng ngày đã chọn.</p>

    <div class="card mb-4">
      <div class="card-body">
        <form id="report-filter" class="form-inline">
          <div class="form-group mr-3 mb-2">
            <label for="start-date" class="mr-2">Từ ngày</label>
            <input id="start-date" class="form-control" type="date" required>
          </div>
          <div class="form-group mr-3 mb-2">
            <label for="end-date" class="mr-2">Đến ngày</label>
            <input id="end-date" class="form-control" type="date" required>
          </div>
          <button class="btn btn-primary mb-2" type="submit">Xem báo cáo</button>
        </form>
      </div>
    </div>

    <div id="report-error" class="alert alert-danger" role="alert" hidden></div>
    <div id="report-loading" class="alert alert-info" role="status" hidden>Đang tải dữ liệu...</div>

    <div class="row">
      <div class="col-xl-3 col-md-6 mb-4">
        <div class="card bg-primary text-white h-100"><div class="card-body">Doanh thu ròng</div><div class="card-footer"><strong id="net-revenue">—</strong></div></div>
      </div>
      <div class="col-xl-3 col-md-6 mb-4">
        <div class="card bg-success text-white h-100"><div class="card-body">Đơn hoàn thành</div><div class="card-footer"><strong id="completed-orders">—</strong></div></div>
      </div>
      <div class="col-xl-3 col-md-6 mb-4">
        <div class="card bg-info text-white h-100"><div class="card-body">Sản phẩm đã bán</div><div class="card-footer"><strong id="products-sold">—</strong></div></div>
      </div>
      <div class="col-xl-3 col-md-6 mb-4">
        <div class="card bg-warning text-dark h-100"><div class="card-body">Lợi nhuận gộp</div><div class="card-footer"><strong id="gross-profit">—</strong><span id="gross-margin"></span></div></div>
      </div>
    </div>

    <div class="card mb-4">
      <div class="card-header"><i class="fas fa-chart-bar mr-1"></i>Doanh thu theo kỳ</div>
      <div class="card-body"><div id="revenue-chart" class="d-flex align-items-end" style="min-height:220px;gap:8px;overflow-x:auto"></div></div>
    </div>

    <div class="row">
      <div class="col-xl-6 mb-4">
        <div class="card h-100">
          <div class="card-header">Sản phẩm bán chạy</div>
          <div class="card-body table-responsive">
            <table class="table table-bordered table-sm"><thead><tr><th>Sản phẩm</th><th>Số lượng</th><th>Doanh thu ròng</th></tr></thead>
              <tbody id="best-selling-rows"><tr><td colspan="3">Chưa có dữ liệu.</td></tr></tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="col-xl-6 mb-4">
        <div class="card h-100">
          <div class="card-header">Sản phẩm nhập nhiều</div>
          <div class="card-body table-responsive">
            <table class="table table-bordered table-sm"><thead><tr><th>Sản phẩm</th><th>Số lượng nhập</th><th>Giá trị nhập</th></tr></thead>
              <tbody id="most-imported-rows"><tr><td colspan="3">Chưa có dữ liệu.</td></tr></tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-xl-6 mb-4">
        <div class="card h-100">
          <div class="card-header">Tình hình tồn kho</div>
          <div class="card-body">
            <p>Tổng số lượng tồn: <strong id="stock-quantity">—</strong></p>
            <p>Biến thể sắp hết hàng: <strong id="low-stock-count">—</strong></p>
            <p>Biến thể hết hàng: <strong id="zero-stock-count">—</strong></p>
            <p>Giá trị tồn kho: <strong id="inventory-value">—</strong></p>
            <small id="inventory-note" class="text-muted"></small>
          </div>
        </div>
      </div>
      <div class="col-xl-6 mb-4">
        <div class="card h-100">
          <div class="card-header">Sản phẩm dưới ngưỡng tồn tối thiểu</div>
          <div class="card-body table-responsive">
            <table class="table table-bordered table-sm"><thead><tr><th>Sản phẩm</th><th>Tồn hiện tại</th><th>Mức tối thiểu</th></tr></thead>
              <tbody id="low-stock-rows"><tr><td colspan="3">Chưa có dữ liệu.</td></tr></tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </main>
</div>

<script>
  (() => {
    const apiRoot = '<c:url value="/api/reports" />';
    const form = document.getElementById('report-filter');
    const errorBox = document.getElementById('report-error');
    const loadingBox = document.getElementById('report-loading');
    const formatter = new Intl.NumberFormat('vi-VN');
    const money = value => value == null ? 'Chưa đủ dữ liệu giá vốn' : formatter.format(Number(value)) + ' ₫';
    const text = (elementId, value) => { document.getElementById(elementId).textContent = value; };
    const queryForDates = (start, end, extras = {}) => {
      const params = new URLSearchParams({ startDate: start, endDate: end, ...extras });
      return params.toString();
    };
    const requestJson = async url => {
      const response = await fetch(url, { headers: { Accept: 'application/json' } });
      const body = await response.json();
      if (!response.ok) throw new Error(body.message || 'Yêu cầu thất bại (' + response.status + ').');
      return body;
    };
    const replaceRows = (tableId, rows, columns, emptyText) => {
      const body = document.getElementById(tableId);
      body.replaceChildren();
      if (!rows.length) {
        const row = document.createElement('tr');
        const cell = document.createElement('td');
        cell.colSpan = columns.length;
        cell.textContent = emptyText;
        row.appendChild(cell);
        body.appendChild(row);
        return;
      }
      rows.forEach(item => {
        const row = document.createElement('tr');
        columns.forEach(column => {
          const cell = document.createElement('td');
          cell.textContent = column(item);
          row.appendChild(cell);
        });
        body.appendChild(row);
      });
    };
    const renderChart = buckets => {
      const chart = document.getElementById('revenue-chart');
      chart.replaceChildren();
      if (!buckets.length) {
        chart.textContent = 'Chưa có doanh thu trong kỳ.';
        return;
      }
      const maximum = Math.max(...buckets.map(bucket => Number(bucket.netRevenue) || 0), 1);
      buckets.forEach(bucket => {
        const group = document.createElement('div');
        group.className = 'text-center';
        group.style.cssText = 'min-width:64px;flex:1';
        const bar = document.createElement('div');
        bar.title = money(bucket.netRevenue);
        bar.style.cssText = 'height:' + Math.max(4, (Number(bucket.netRevenue) / maximum) * 170)
          + 'px;background:#007bff;border-radius:4px 4px 0 0';
        const value = document.createElement('small');
        value.className = 'd-block text-truncate';
        value.textContent = bucket.periodStart;
        group.append(bar, value);
        chart.appendChild(group);
      });
    };
    const loadReports = async (start, end) => {
      errorBox.hidden = true;
      loadingBox.hidden = false;
      try {
        const range = queryForDates(start, end);
        const [summary, bestSelling, mostImported, inventory, lowStock] = await Promise.all([
          requestJson(apiRoot + '/revenue/summary?' + range),
          requestJson(apiRoot + '/products/best-selling?' + range + '&sortBy=revenue&size=5'),
          requestJson(apiRoot + '/products/most-imported?' + range + '&size=5'),
          requestJson(apiRoot + '/inventory/summary?size=5'),
          requestJson(apiRoot + '/products/low-stock?size=5')
        ]);
        text('net-revenue', money(summary.netRevenue));
        text('completed-orders', formatter.format(summary.completedOrders));
        text('products-sold', formatter.format(summary.productsSold));
        text('gross-profit', money(summary.grossProfit));
        text('gross-margin', summary.grossMarginPercent == null ? '' : ' (' + summary.grossMarginPercent + '%)');
        renderChart(summary.series || []);
        replaceRows('best-selling-rows', bestSelling.items || [], [
          item => item.productName, item => formatter.format(item.quantitySold), item => money(item.netRevenue)
        ], 'Chưa có sản phẩm bán trong kỳ.');
        replaceRows('most-imported-rows', mostImported.items || [], [
          item => item.productName, item => formatter.format(item.quantityImported), item => money(item.totalImportValue)
        ], 'Chưa có phiếu nhập hoàn tất trong kỳ.');
        text('stock-quantity', formatter.format(inventory.totalQuantity));
        text('low-stock-count', formatter.format(inventory.lowStockVariantCount));
        text('zero-stock-count', formatter.format(inventory.zeroStockVariantCount));
        text('inventory-value', inventory.inventoryValueComplete ? money(inventory.inventoryValue) : 'Chưa đủ dữ liệu giá vốn');
        text('inventory-note', inventory.inventoryValueComplete ? '' : 'Một số biến thể chưa có giá vốn nên không tính được tổng giá trị tồn.');
        replaceRows('low-stock-rows', lowStock.items || [], [
          item => item.productName, item => formatter.format(item.quantityOnHand), item => formatter.format(item.minimumStock)
        ], 'Không có sản phẩm dưới ngưỡng tồn.');
      } catch (error) {
        errorBox.textContent = error.message || 'Không tải được dữ liệu thống kê.';
        errorBox.hidden = false;
      } finally {
        loadingBox.hidden = true;
      }
    };
    const now = new Date();
    const localDate = date => date.getFullYear() + '-'
      + String(date.getMonth() + 1).padStart(2, '0') + '-'
      + String(date.getDate()).padStart(2, '0');
    document.getElementById('start-date').value = localDate(new Date(now.getFullYear(), now.getMonth(), 1));
    document.getElementById('end-date').value = localDate(now);
    form.addEventListener('submit', event => {
      event.preventDefault();
      const start = document.getElementById('start-date').value;
      const end = document.getElementById('end-date').value;
      if (start > end) {
        errorBox.textContent = 'Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc.';
        errorBox.hidden = false;
        return;
      }
      loadReports(start, end);
    });
    loadReports(document.getElementById('start-date').value, document.getElementById('end-date').value);
  })();
</script>
<jsp:include page="Footer_admin.jsp"></jsp:include>
