package com.banhang.service;

import com.banhang.dao.SanPhamDao;
import com.banhang.entity.SanPham;
import com.banhang.imp.SanPhamImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class cho entity SanPham. Cung cấp các phương thức business logic cho quản lý sản phẩm.
 *
 * @author System
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SanPhamService implements SanPhamImp {
  private final SanPhamDao sanPhamDao;

  /**
   * Lấy danh sách tất cả sản phẩm.
   *
   * @return Danh sách sản phẩm
   */
  @Override
  public List<SanPham> getAllSanPham() {
    return sanPhamDao.getAllSanPham();
  }

  /**
   * Lấy danh sách sản phẩm theo danh mục.
   *
   * @param id Mã danh mục
   * @return Danh sách sản phẩm của danh mục
   */
  @Override
  public List<SanPham> getAllSanPhamByIdDanhMuc(int id) {
    return sanPhamDao.getAllSanPhamByIdDanhMuc(id);
  }

  /**
   * Lấy thông tin sản phẩm theo ID.
   *
   * @param id Mã sản phẩm
   * @return Thông tin sản phẩm
   */
  @Override
  public SanPham getListSanPhamById(int id) {
    return sanPhamDao.getListSanPhamById(id);
  }

  /**
   * Thêm sản phẩm mới.
   *
   * @param sp Sản phẩm cần thêm
   * @return ID của sản phẩm vừa thêm
   */
  @Override
  public int addSanPham(SanPham sp) {
    return sanPhamDao.addSanPham(sp);
  }

  /**
   * Cập nhật thông tin sản phẩm.
   *
   * @param sp Sản phẩm cần cập nhật
   * @return true nếu thành công
   */
  @Override
  public boolean updateSanPham(SanPham sp) {
    return sanPhamDao.updateSanPham(sp);
  }

  /**
   * Xóa sản phẩm.
   *
   * @param sp Sản phẩm cần xóa
   * @return true nếu thành công
   */
  @Override
  public boolean deleteSanPham(SanPham sp) {
    return sanPhamDao.deleteSanPham(sp);
  }

  /**
   * Kiểm tra tên sản phẩm trước khi thêm mới.
   *
   * @param tenSp Tên sản phẩm
   * @return true nếu sản phẩm đã tồn tại
   */
  @Override
  public boolean checkNameSanPhamBeforeAdd(String tenSp) {
    return sanPhamDao.checkNameSanPhamBeforeAdd(tenSp);
  }
}
