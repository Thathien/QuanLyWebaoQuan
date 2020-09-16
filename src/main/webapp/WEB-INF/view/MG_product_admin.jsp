<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="Header_admin.jsp"></jsp:include>
<jsp:include page="Menu_admin.jsp"></jsp:include>

            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Trang quản lý</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">Sản phẩm</li>
                        </ol>
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-table mr-1"></i>
                               Danh sách sản phẩm
                               <a href="" class="btn btn-secondary float-right" title="" id="ansanpham">Ẩn</a>
                                <sub class=" btn float-right">---</sub>
                                <a href="" class="btn btn-danger float-right" title="" style="margin-left: 10px;" id= xoasanpham>Xóa</a>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                   <c:choose>
                                   	<c:when test="${listSanPhamss.size()!=0 }">
                                   		 <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                        <thead>
                                            <tr>
                                             	<th>
                                                    <input id='checkAll' type="checkbox" value="">
                                                </th>
                                                <th>Tên sản phẩm</th>
                                                <th>Danh mục sản phẩm</th>
                                                <th>giá tiền</th>
                                                <th>Mô tả</th>
                                                <th>Ngày cập nhật</th>
                                                <th>----</th>
                                            </tr>
                                        </thead>
                                        <tfoot>
                                            <tr>
                                            	<th>
                                                    <input type="checkbox" name="">
                                                </th>
                                                 <th>Tên sản phẩm</th>
                                                <th>Danh mục sản phẩm</th>
                                                <th>giá tiền</th>
                                                <th>Mô tả</th>
                                                <th>Ngày cập nhật</th>
                                                <th>----</th>
                                            </tr>
                                        </tfoot>
                                        <tbody>
                                         <c:forEach var="listSanPhamss" items="${listSanPhamss}">
	                                         <tr data-masanpham="${listSanPhamss.getMasanpham()}">
	                                         	<th>
                                                    <input type="checkbox" name="" id="${listSanPhamss.getMasanpham()}">
                                                </th>
	                                             <td >
	                                             	${listSanPhamss.getTensanpham()}
	                                             </td>
	                                             <td>
	                                             	${listSanPhamss.getDanhMucSanPham().getTendanhmuc()}
	                                             </td>
	                                             <td>${listSanPhamss.getGiatien()}</td>
	                                             <td>2011/01/25</td>
	                                             <td>2011/01/25</td>
	                                             <td>
	                                             	<a href="" class="btn btn-info " title="" id="chitietsanpham">Chi tiết</a>
	                                             	<a href="" class="btn btn-primary t" title="" id="capnhatsanpham">Chỉnh sửa</a>
	                                             </td>
	                                          </tr>
                                         </c:forEach>
                                        </tbody>
                                    </table>
                                   	</c:when>
                                   	<c:otherwise>
                                   		<h3 style="text-align: center;">Dữ liệu trống</h3>
                                   	</c:otherwise>
                                   </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>

<jsp:include page="Footer_admin.jsp"></jsp:include>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.min.js" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="<c:url value = "/resourcess/admin/dist/js/scripts.js"/>"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" crossorigin="anonymous"></script>
        <script src="<c:url value = "/resourcess/admin/dist/assets/demo/datatables-demo.js"/>"></script>
    </body>
</html>
