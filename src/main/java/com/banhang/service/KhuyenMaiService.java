package com.banhang.service;

import com.banhang.dao.KhuyenMaiDao;
import com.banhang.entity.KhuyenMai;
import com.banhang.imp.KhuyenMaiImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KhuyenMaiService implements KhuyenMaiImp {
  private final KhuyenMaiDao khuyenMaiDao;

  public List<KhuyenMai> getAllKhuyenMai() {
    return khuyenMaiDao.getAllKhuyenMai();
  }

  public int addKhuyenMai(KhuyenMai km) {
    return khuyenMaiDao.addKhuyenMai(km);
  }

  public boolean updateKhuyenMai(KhuyenMai km) {
    return khuyenMaiDao.updateKhuyenMai(km);
  }

  public boolean deleteKhuyenMai(KhuyenMai km) {
    return khuyenMaiDao.deleteKhuyenMai(km);
  }

  public boolean checkNameKhuyenMai(String name) {
    return khuyenMaiDao.checkNameKhuyenMai(name);
  }
}
