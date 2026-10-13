package com.banhang.imp;

import com.banhang.entity.SanPham;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SanPhamImp {
  public List<SanPham> getAllSanPham();

  public Page<SanPham> getStorefrontProducts(
      Integer categoryId, String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

  public List<SanPham> getAllSanPhamByIdDanhMuc(int id);

  public SanPham getListSanPhamById(int id);

  public int addSanPham(SanPham sp);

  public boolean updateSanPham(SanPham sp);

  public boolean deleteSanPham(SanPham sp);

  public boolean checkNameSanPhamBeforeAdd(String tenSp);
}
