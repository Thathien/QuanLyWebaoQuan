package com.banhang.service;

import com.banhang.entity.GioHang;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GioHangService {
  private static final String CART_SESSION_KEY = "giohang";

  public List<GioHang> getCart(HttpSession httpSession) {
    Object value = httpSession.getAttribute(CART_SESSION_KEY);
    if (value == null) {
      List<GioHang> cart = new ArrayList<>();
      httpSession.setAttribute(CART_SESSION_KEY, cart);
      return cart;
    }
    return castCart(value);
  }

  public boolean hasCart(HttpSession httpSession) {
    return httpSession.getAttribute(CART_SESSION_KEY) != null;
  }

  public void addItem(
      int machitietsanpham,
      int masp,
      int masize,
      int mamau,
      String tensp,
      String giatien,
      String tenmau,
      String tensize,
      int soluong,
      HttpSession httpSession) {
    List<GioHang> cart = getCart(httpSession);
    int index = findItemIndex(cart, masp, masize, mamau);
    if (index >= 0) {
      GioHang existingItem = cart.get(index);
      existingItem.setSoluong(existingItem.getSoluong() + soluong);
      return;
    }
    cart.add(
        createItem(
            machitietsanpham, masp, masize, mamau, tensp, giatien, tenmau, tensize, soluong));
  }

  public void removeItem(int masp, int masize, int mamau, HttpSession httpSession) {
    List<GioHang> cart = getCart(httpSession);
    int index = findItemIndex(cart, masp, masize, mamau);
    if (index >= 0) {
      cart.remove(index);
    }
  }

  public void updateQuantity(
      int masp, int masize, int mamau, int soluong, HttpSession httpSession) {
    List<GioHang> cart = getCart(httpSession);
    int index = findItemIndex(cart, masp, masize, mamau);
    if (index >= 0) {
      cart.get(index).setSoluong(soluong);
    }
  }

  private GioHang createItem(
      int machitietsanpham,
      int masp,
      int masize,
      int mamau,
      String tensp,
      String giatien,
      String tenmau,
      String tensize,
      int soluong) {
    GioHang gioHang = new GioHang();
    gioHang.setMachitietsanpham(machitietsanpham);
    gioHang.setMamau(mamau);
    gioHang.setMasize(masize);
    gioHang.setMasp(masp);
    gioHang.setTenmau(tenmau);
    gioHang.setTensize(tensize);
    gioHang.setTensp(tensp);
    gioHang.setGiatien(giatien);
    gioHang.setSoluong(soluong);
    return gioHang;
  }

  private int findItemIndex(List<GioHang> cart, int masp, int masize, int mamau) {
    for (int i = 0; i < cart.size(); i++) {
      GioHang item = cart.get(i);
      if (item.getMasp() == masp && item.getMasize() == masize && item.getMamau() == mamau) {
        return i;
      }
    }
    return -1;
  }

  @SuppressWarnings("unchecked")
  private List<GioHang> castCart(Object value) {
    return (List<GioHang>) value;
  }
}
