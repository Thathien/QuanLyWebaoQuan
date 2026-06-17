package com.banhang.service;

import com.banhang.dao.ChiTietKhuyenMaiDao;
import com.banhang.entity.ChiTietKhuyenMai;
import com.banhang.imp.ChiTietKhuyenMaiImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChiTietKhuyenMaiService implements ChiTietKhuyenMaiImp {
  private final ChiTietKhuyenMaiDao chiTietKhuyenMaiDao;

  public List<ChiTietKhuyenMai> getAllChiTietKhuyenMai() {
    return chiTietKhuyenMaiDao.getAllChiTietKhuyenMai();
  }

  public int addNewKhuyenMaiChoSP(ChiTietKhuyenMai ctkm) {
    return chiTietKhuyenMaiDao.addNewKhuyenMaiChoSP(ctkm);
  }

  public boolean deletKhuyenMaiSP(ChiTietKhuyenMai ctkm) {
    return chiTietKhuyenMaiDao.deletKhuyenMaiSP(ctkm);
  }
}
