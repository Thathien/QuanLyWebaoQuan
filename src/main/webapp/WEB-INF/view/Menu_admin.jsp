<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<body class="sb-nav-fixed">
  <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
    <a class="navbar-brand" href="<c:url value='/admin' />">Quản trị cửa hàng</a>
    <button class="btn btn-link btn-sm" id="sidebarToggle" type="button" aria-label="Ẩn hoặc hiện menu">
      <i class="fas fa-bars"></i>
    </button>
    <span class="navbar-text ml-auto mr-3 d-none d-sm-inline">
      <c:out value="${taikhoan.hoten}" />
    </span>
    <a class="btn btn-outline-light btn-sm" href="<c:url value='/admin/dangxuat' />">Đăng xuất</a>
  </nav>
  <div id="layoutSidenav">
    <div id="layoutSidenav_nav">
      <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion" aria-label="Điều hướng quản trị">
        <div class="sb-sidenav-menu">
          <div class="nav">
            <div class="sb-sidenav-menu-heading">Tổng quan</div>
            <a class="nav-link" href="<c:url value='/admin' />">
              <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
              Trang chủ
            </a>
            <div class="sb-sidenav-menu-heading">Quản lý</div>
            <a class="nav-link" href="<c:url value='/admin/product' />">
              <div class="sb-nav-link-icon"><i class="fas fa-box"></i></div>
              Sản phẩm
            </a>
            <a class="nav-link" href="<c:url value='/admin/inventory' />">
              <div class="sb-nav-link-icon"><i class="fas fa-warehouse"></i></div>
              Nhập kho & tồn kho
            </a>
            <a class="nav-link" href="<c:url value='/admin/orders' />">
              <div class="sb-nav-link-icon"><i class="fas fa-receipt"></i></div>
              Đơn hàng
            </a>
            <div class="sb-sidenav-menu-heading">Phân tích</div>
            <a class="nav-link" href="<c:url value='/admin/reports' />">
              <div class="sb-nav-link-icon"><i class="fas fa-chart-line"></i></div>
              Báo cáo thống kê
            </a>
          </div>
        </div>
        <div class="sb-sidenav-footer">
          <div class="small">Đang đăng nhập</div>
          <c:out value="${taikhoan.tendangnhap}" />
        </div>
      </nav>
    </div>
    <script>
      document.getElementById('sidebarToggle').addEventListener('click', function () {
        document.body.classList.toggle('sb-sidenav-toggled');
      });
      document.querySelectorAll('#layoutSidenav_nav a.nav-link').forEach(function (link) {
        if (link.href === window.location.href) link.classList.add('active');
      });
    </script>
