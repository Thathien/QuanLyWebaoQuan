package com.banhang.service;

import com.banhang.dao.ChiTietSanPhamDao;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.imp.ChiTietSanPhamImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChiTietSanPhamService implements ChiTietSanPhamImp {
  private final ChiTietSanPhamDao chiTietSanPhamDao;

  public List<ChiTietSanPham> getInforCTSanPhambyID(int masanpham) {
    return chiTietSanPhamDao.getInforCTSanPhambyID(masanpham);
  }

  public boolean updateCTSanPham(ChiTietSanPham chiTietSanPham) {
    return chiTietSanPhamDao.updateCTSanPham(chiTietSanPham);
  }

  public boolean deleteCTSanPham(ChiTietSanPham chiTietSanPham) {
    return chiTietSanPhamDao.deleteCTSanPham(chiTietSanPham);
  }

  public int addCTSanPham(ChiTietSanPham chiTietSanPham) {
    return chiTietSanPhamDao.addCTSanPham(chiTietSanPham);
  }

  @Override
  public boolean deleteCTSanPhamByIdSanPham(int id) {
    return chiTietSanPhamDao.deleteCTSanPhamByIdSanPham(id);
  }
}
