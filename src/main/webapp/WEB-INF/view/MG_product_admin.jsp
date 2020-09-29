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
                                 <sub class=" btn float-right">---</sub>
                                  <a href="" class="btn btn-primary float-right" title="" style="margin-left: 10px;" id= "xoasanpham" data-toggle="modal" data-target="#addsanphammodel"> Thêm mới</a>
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
	                                             	<a href="" class="btn btn-info " title="" id="chitietsanpham" data-toggle="modal" data-target="#updatesanphammodel" data-masanpham="${listSanPhamss.getMasanpham()}">Chi tiết</a>
                                              <!--   <a href="" class="btn btn-primary" title="" id="ansanpham">Chỉnh sửa</a> -->
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

            <!-- model addsanpham -->
            <div class="modal fade bd-example-modal-lg overflow-auto" id="addsanphammodel" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-lg" role="document" style="overflow-y: scroll; max-height:85%;  margin-top: 50px; margin-bottom:50px;">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Thêm mới sản phẩm</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                  </button>
              </div>
              <div class="modal-body overflow-auto">
                 <form>
                  <div class="form-inline">
                    <div class="form-group ">
                        <label for="recipient-name" class="col-form-label" style="margin-right: 10px;">Tên sản phẩm</label>
                        <input type="text" class="form-control form-control-sm" id="tensanphamadd">
                        <span class="text-danger" id="tensanphamadd_error">*</span>
                    </div>
                    <div class="form-group " style="margin-left: 20px;">  
                     <label for="recipient-name" class="col-form-label" style="margin-right: 10px;">Danh mục</label>
                     <select class="form-control " id="danhmucadd">
                     <c:choose>
                     	<c:when test="${danhMucSanPhams.size()!=0}">
                     		<c:forEach var="danhMucSanPhams" items="${danhMucSanPhams}">
                     			 <option value="${danhMucSanPhams.getMadanhmucsanpham()}">${danhMucSanPhams.getTendanhmuc()}</option>
                     		</c:forEach>
                     	</c:when>
                     	<c:otherwise>
                     		 <option disabled="disabled">Danh mục sản phẩm trống</option>
                     	</c:otherwise>
                     </c:choose>
                       
                    </select>
                    <span class="text-danger" id="danhmucadd_error">*</span>
                </div>
            </div>
            <div class="form-group">
                <label for="message-text" class="col-form-label">Mô tả:</label>
                <textarea class="form-control form-control-sm" id="motaadd" name="editor1"></textarea>
                <script>
                    CKEDITOR.replace( 'editor1' );
                </script>
                <span class="text-danger" id="motaadd_error">*</span>
            </div>
            <div class="form-inline form-group">
                <div class="form-group">
                    <label for="recipient-name" class="col-form-label" style="margin-right: 10px;">Giá tiền: (VNĐ)</label>
                    <input type="number" class="form-control form-control-sm" id="giatienadd" min="10000">
                    <span class="text-danger" id="giatienadd_error">*</span>
                </div>
                <div class="form-group" style="margin-left: 20px;">
                    <label for="recipient-name" class="col-form-label" style="margin-left: 20px;margin-right: 20px;">Đối tượng</label>
                    <select class="form-control form-control-sm" id="danhchoadd">
                        <option value="nam">Nam </option>
                        <option value="nu">Nữ</option>
                        <option value="treem">Trẻ em</option>
                    </select>
                    <span class="text-danger" id="danhchoadd_error">*</span>
                </div>
            </div>

            <div class="form-group">
                <label for="fileanh">Đường dẫn</label>
                <input type="file" class="form-control-file" id="fileanhadd">
                <span class="text-danger" id="fileanhadd_error">*</span>
            </div>
            <div class="form-inline form-group " id="contchitietsanpham">
               <div class="form-group" style="margin-left: 20px;">
                <label for="recipient-name" class="col-form-label" style="margin-right: 5px;">Màu sắc</label>
                <select class="form-control form-control-sm" id="mausacadd" class="required" >
                     <c:choose>
                     	<c:when test="${mauSanPhams.size()!=0}">
                     		<c:forEach var="mauSanPhams" items="${mauSanPhams}">
                     			 <option value="${mauSanPhams.getMamau()}">${mauSanPhams.getTenmau()}</option>
                     		</c:forEach>
                     	</c:when>
                     	<c:otherwise>
                     		 <option disabled="disabled">Màu sản phẩm trống</option>
                     	</c:otherwise>
                     </c:choose>
                </select>
                <span class="text-danger" id="mausacadd_error">*</span>
            </div>
            <div class="form-group" >
                <label for="recipient-name" class="col-form-label" style="margin-left: 20px;margin-right: 10px;">Size</label>
                <select class="form-control form-control-sm" id="sizeadd" class="required">
                   <c:choose>
                     	<c:when test="${sizeSanPhams.size()!=0}">
                     		<c:forEach var="sizeSanPhams" items="${sizeSanPhams}">
                     			 <option value="${sizeSanPhams.getMasize()}">${sizeSanPhams.getSize()}</option>
                     		</c:forEach>
                     	</c:when>
                     	<c:otherwise>
                     		 <option disabled="disabled">Size sản phẩm trống</option>
                     	</c:otherwise>
                     </c:choose>
                </select>
                <span class="text-danger" id="sizeadd_error">*</span>
            </div>
            <div class="form-group">
                <label for="recipient-name" class="col-form-label" style="margin-left: 20px;margin-right: 10px;">Số lượng:</label>
                <input type="number" class="form-control form-control-sm" id="soluongadd" style="margin-left: 10px;" min="1">
                <span class="text-danger" id="soluongadd_error">*</span>
            </div>
            <div class="form-group">
                <input type="button" class="btn btn-primary" title="" id="themchitietsanpham" style="margin-left: 10px;margin-right: 10px;" value="Thêm">
            </div>
			 <div class="form-group" id="chitietsanpham">
            <table class="table" id="tableaddsp">
                <caption>Thông tin chi tiết sản phẩm</caption>
                <thead>
                    <tr>
                        <th>Màu</th>
                        <th>Size</th>
                        <th>Số lượng</th>
                        <th>Ngày nhập</th>
                    </tr>
                </thead>
                <tbody>
                   <!--   <tr>
                        <td>Trống</td>
                        <td>Trống</td>
                        <td>Trống</td>
                        <td>Trống</td>
                    </tr>
                    -->
                </tbody>
            </table>
        </div>
        </div>
       
		<div class="modal-footer">
		    <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
		    <button type="button" class="btn btn-primary" id="addnewsanpham">Thêm mới</button>
		</div>
    </form>
</div>




</div>
</div>
</div>
<!-- modol addsanpham -->


<!-- modol updatéanpham -->
<div class="modal fade bd-example-modal-lg overflow-auto" id="updatesanphammodel" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel1" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document" style="overflow-y: scroll; max-height:85%;  margin-top: 50px; margin-bottom:50px;">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabels">Cập nhật sản phẩm</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
      </button>
  </div>
  <div class="modal-body overflow-auto">
     <form>
      <div class="form-inline">
        <div class="form-group ">
            <label for="recipient-name" class="col-form-label" style="margin-right: 10px;">Tên sản phẩm</label>
            <input type="text" class="form-control form-control-sm" id="tenspadd">
            <span class="text-danger" id="tensanpham_errors">*</span>
        </div>
        <div class="form-group " style="margin-left: 20px;">  
         <label for="recipient-name" class="col-form-label" style="margin-right: 10px;">Danh mục</label>
         <select class="form-control form-control-sm" id="danhmucspadd">
            <option>abafgyfsdgfhggfdghjaf</option>
            <option>adbajsdfghjkdb</option>
            <option>adbadasdfghjkljkjad</option>
            <option>4asfdgfhjk</option>
            <option>adsfdgfhg5</option>
        </select>
        <span class="text-danger" id="danhmuc_errors">*</span>
    </div>
</div>
<div class="form-group">
    <label for="message-text" class="col-form-label">Mô tả:</label>
    <textarea class="form-control form-control-sm" id="motaspadd" name="editor3"></textarea>
    <script>
        CKEDITOR.replace( 'editor3' );
    </script>
    <span class="text-danger" id="mota_errors">*</span>
</div>
<div class="form-inline form-group">
    <div class="form-group">
        <label for="recipient-name" class="col-form-label" style="margin-right: 10px;">Giá tiền: (VNĐ)</label>
        <input type="text" class="form-control form-control-sm" id="giatadada">
        <span class="text-danger" id="giatien_errors">*</span>
    </div>
    <div class="form-group" style="margin-left: 20px;">
        <label for="recipient-name" class="col-form-label" style="margin-left: 20px;margin-right: 20px;">Đối tượng</label>
        <select class="form-control form-control-sm" id="asdfg">
            <option>Nam </option>
            <option>Nữ</option>
            <option>Trẻ em</option>
        </select>
        <span class="text-danger" id="danhcho_errors">*</span>
    </div>
</div>

<div class="form-group">
    <label for="fileanh">Đường dẫn</label>
    <input type="file" class="form-control-file" id="asaas">
    <span class="text-danger" id="danhcho_errosr">*</span>
</div>
<div class="form-inline form-group">
   <div class="form-group" style="margin-left: 20px;">
    <label for="recipient-name" class="col-form-label" style="margin-right: 10px;">Màu sắc</label>
    <select class="form-control form-control-sm" id="adadad" >
        <option>Nam </option>
       
    </select>
    <span class="text-danger" id="mausac_error">*</span>
</div>
<div class="form-group" >
    <label for="recipient-name" class="col-form-label" style="margin-left: 20px;margin-right: 10px;">Size</label>
    <select class="form-control form-control-sm" id="ađa">
        <option>Nam </option>
        
    </select>
    <span class="text-danger" id="size_errors">*</span>
</div>
<div class="form-group">
    <label for="recipient-name" class="col-form-label" style="margin-left: 20px;margin-right: 10px;">Số lượng:</label>
    <input type="number" class="form-control form-control-sm" id="ssaadadsss" style="margin-left: 10px;" min="1">
    <span class="text-danger" id="giatien_errosr">*</span>
</div>
<div class="form-group">
    <a href="" class="btn btn-primary" title="" id="sssss" style="margin-left: 20px;margin-right: 10px;">Thêm </a>
</div>

</div>
<div class="form-group">
    <table class="table">
        <caption>Thông tin chi tiết sản phẩm</caption>
        <thead>
            <tr>
                <th>Màu</th>
                <th>Size</th>
                <th>Số lượng</th>
                <th>Ngày nhập</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Xanh</td>
                <td>XXL</td>
                <td>
                     <input type="number" class="form-control-sm" id="sdagdads" style="margin-left: 10px;" min="1" value="10">
                </td>
                <td>17/09/2020</td>
            </tr>
        </tbody>
    </table>
</div>
<div class="modal-footer">
    <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
    <button type="button" class="btn btn-primary">Cập nhật</button>
</div>
</form>
</div>




</div>
</div>
</div>
<!-- modol updatéanpham -->
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
