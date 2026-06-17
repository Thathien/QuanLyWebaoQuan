package com.banhang.service;

import com.banhang.dao.SizeSanPhamDao;
import com.banhang.imp.SizeSanPhamImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeSanPhamService implements SizeSanPhamImp {
  private final SizeSanPhamDao sizeSanPhamDao;

  public List<com.banhang.entity.SizeSanPham> getAllSizeSanPham() {
    return sizeSanPhamDao.getAllSizeSanPham();
  }

  public int addSizeSanPham(com.banhang.entity.SizeSanPham ssp) {
    return sizeSanPhamDao.addSizeSanPham(ssp);
  }

  public boolean updateSizeSanPham(com.banhang.entity.SizeSanPham ssp) {
    return sizeSanPhamDao.updateSizeSanPham(ssp);
  }

  public boolean deleteSizeSanPham(com.banhang.entity.SizeSanPham ssp) {
    return sizeSanPhamDao.deleteSizeSanPham(ssp);
  }
}
