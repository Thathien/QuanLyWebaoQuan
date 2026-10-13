package com.banhang.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "HoaDon")
@Table(
    name = "HoaDon",
    indexes = {
      @jakarta.persistence.Index(
          name = "IX_HoaDon_Status_CompletedAt",
          columnList = "tinhtrang, completed_at")
    })
public class HoaDon implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mahoadon")
  int mahoadon;

  @Column(name = "tenkhachhang", columnDefinition = "nvarchar(50)")
  String tenkhachhang;

  @Column(name = "emailkhachhang", columnDefinition = "nvarchar(255)")
  String emailkhachhang;

  @Column(name = "sdt", columnDefinition = "nvarchar(50)")
  String sdt;

  String diachigiaohang;

  @Column(name = "tinhtrang")
  int tinhtrang;

  @Column(name = "ghichu", columnDefinition = "nvarchar(50)")
  String ghichu;

  @Column(name = "ngaylap", columnDefinition = "nvarchar(50)")
  String ngaylap;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "completed_at")
  LocalDateTime completedAt;

  @Column(name = "hinhthucgiaohang", columnDefinition = "nvarchar(50)")
  String hinhthucgiaohang;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "mahoadon")
  Set<ChiTietHoaDon> chiTietHoaDon;

  public HoaDon() {
    super();
  }

  public HoaDon(
      int mahoadon,
      String tenkhachhang,
      String sdt,
      String diachigiaohang,
      int tinhtrang,
      String ghichu,
      String ngaylap,
      String hinhthucgiaohang,
      Set<ChiTietHoaDon> chiTietHoaDon) {
    super();
    this.mahoadon = mahoadon;
    this.tenkhachhang = tenkhachhang;
    this.sdt = sdt;
    this.diachigiaohang = diachigiaohang;
    this.tinhtrang = tinhtrang;
    this.ghichu = ghichu;
    this.ngaylap = ngaylap;
    this.hinhthucgiaohang = hinhthucgiaohang;
    this.chiTietHoaDon = chiTietHoaDon;
  }

  public int getMahoadon() {
    return mahoadon;
  }

  public void setMahoadon(int mahoadon) {
    this.mahoadon = mahoadon;
  }

  public String getTenkhachhang() {
    return tenkhachhang;
  }

  public void setTenkhachhang(String tenkhachhang) {
    this.tenkhachhang = tenkhachhang;
  }

  public String getEmailkhachhang() {
    return emailkhachhang;
  }

  public void setEmailkhachhang(String emailkhachhang) {
    this.emailkhachhang = emailkhachhang;
  }

  public String getSdt() {
    return sdt;
  }

  public void setSdt(String sdt) {
    this.sdt = sdt;
  }

  public String getDiachigiaohang() {
    return diachigiaohang;
  }

  public void setDiachigiaohang(String diachigiaohang) {
    this.diachigiaohang = diachigiaohang;
  }

  public int getTinhtrang() {
    return tinhtrang;
  }

  public void setTinhtrang(int tinhtrang) {
    this.tinhtrang = tinhtrang;
  }

  public String getNgaylap() {
    return ngaylap;
  }

  public void setNgaylap(String ngaylap) {
    this.ngaylap = ngaylap;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(LocalDateTime completedAt) {
    this.completedAt = completedAt;
  }

  public Set<ChiTietHoaDon> getChiTietHoaDon() {
    return chiTietHoaDon;
  }

  public void setChiTietHoaDon(Set<ChiTietHoaDon> chiTietHoaDon) {
    this.chiTietHoaDon = chiTietHoaDon;
  }

  public String getGhichu() {
    return ghichu;
  }

  public void setGhichu(String ghichu) {
    this.ghichu = ghichu;
  }

  public String getHinhthucgiaohang() {
    return hinhthucgiaohang;
  }

  public void setHinhthucgiaohang(String hinhthucgiaohang) {
    this.hinhthucgiaohang = hinhthucgiaohang;
  }
}
