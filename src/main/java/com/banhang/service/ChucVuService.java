package com.banhang.service;

import com.banhang.dao.ChucVuDao;
import com.banhang.entity.ChucVu;
import com.banhang.imp.ChucVuImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChucVuService implements ChucVuImp {
  private final ChucVuDao chucVuDao;

  public List<ChucVu> getAllChucVu() {
    return chucVuDao.getAllChucVu();
  }

  public int addChucVu(ChucVu cv) {
    return chucVuDao.addChucVu(cv);
  }

  public boolean updateChucVu(ChucVu cv) {
    return chucVuDao.updateChucVu(cv);
  }

  public boolean deleteChucVu(ChucVu cv) {
    return chucVuDao.deleteChucVu(cv);
  }

  public boolean checkNameChucVu(String name) {
    return chucVuDao.checkNameChucVu(name);
  }

  @Override
  public ChucVu getById(int id) {
    List<ChucVu> chucVus = null;
    chucVus = getAllChucVu();

    ChucVu cv = null;
    for (ChucVu c : chucVus) {
      if (c.getMachucvu() == id) {
        cv = c;
      }
    }
    return cv;
  }
}
