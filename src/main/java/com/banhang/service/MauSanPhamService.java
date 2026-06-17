package com.banhang.service;

import com.banhang.dao.MauSanPhamDao;
import com.banhang.entity.MauSanPham;
import com.banhang.imp.MauSanPhamImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MauSanPhamService implements MauSanPhamImp {
  private final MauSanPhamDao mauSanPhamDao;

  public List<MauSanPham> getAllMauSanPham() {
    return mauSanPhamDao.getAllMauSanPham();
  }

  public int addMauSanPham(MauSanPham ms) {
    return mauSanPhamDao.addMauSanPham(ms);
  }

  public boolean updateMauSanPham(MauSanPham ms) {
    return mauSanPhamDao.updateMauSanPham(ms);
  }

  public boolean deleteMauSanPham(MauSanPham ms) {
    return mauSanPhamDao.deleteMauSanPham(ms);
  }

  public boolean checkMauSanPham(String s) {
    return mauSanPhamDao.checkMauSanPham(s);
  }
}
