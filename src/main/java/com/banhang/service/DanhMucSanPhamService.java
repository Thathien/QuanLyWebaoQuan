package com.banhang.service;

import com.banhang.dao.DanhMucSanPhamDao;
import com.banhang.entity.DanhMucSanPham;
import com.banhang.imp.DanhMucSanPhamImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DanhMucSanPhamService implements DanhMucSanPhamImp {
  private final DanhMucSanPhamDao danhMucSanPhamDao;

  public List<DanhMucSanPham> getAllDanhMucSanPham() {
    return danhMucSanPhamDao.getAllDanhMucSanPham();
  }

  public int addDanhMucSanPham(DanhMucSanPham dm) {
    return danhMucSanPhamDao.addDanhMucSanPham(dm);
  }

  public boolean updateDanhMucSanPham(DanhMucSanPham dm) {
    return danhMucSanPhamDao.updateDanhMucSanPham(dm);
  }

  public boolean deleteDanhMucSanPham(DanhMucSanPham dm) {
    return danhMucSanPhamDao.deleteDanhMucSanPham(dm);
  }

  public boolean checkNameDanhMucBeforeAdd(String name) {
    return danhMucSanPhamDao.checkNameDanhMucBeforeAdd(name);
  }
}
