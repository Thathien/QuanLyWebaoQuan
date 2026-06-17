package com.banhang.service;

import com.banhang.dao.ChiTietHoaDonDao;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.imp.ChiTietHoaDonImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChiTietHoaDonService implements ChiTietHoaDonImp {
  private final ChiTietHoaDonDao chiTietHoaDonDao;

  public List<ChiTietHoaDon> getListCTHoaDonByID(int mahoadon) {
    return chiTietHoaDonDao.getListCTHoaDonByID(mahoadon);
  }

  public boolean deleteChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
    return chiTietHoaDonDao.deleteChiTietHoaDon(chiTietHoaDon);
  }

  public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
    return chiTietHoaDonDao.updateChiTietHoaDon(chiTietHoaDon);
  }

  public int addChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
    return chiTietHoaDonDao.addChiTietHoaDon(chiTietHoaDon);
  }

  public ChiTietHoaDon getInforChiTietHoaDon(int mahoadon, int machitietsanpham) {
    return chiTietHoaDonDao.getInforChiTietHoaDon(mahoadon, machitietsanpham);
  }

  public boolean checkExitsSanPham(int mahoadon, int machitietsanpham) {
    return chiTietHoaDonDao.checkExitsSanPham(mahoadon, machitietsanpham);
  }
}
