package com.banhang.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.banhang.dao.*;
import com.banhang.entity.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceUnitTest {
  @Mock private ChiTietHoaDonDao chiTietHoaDonDao;
  @Mock private ChiTietKhuyenMaiDao chiTietKhuyenMaiDao;
  @Mock private ChiTietSanPhamDao chiTietSanPhamDao;
  @Mock private ChucVuDao chucVuDao;
  @Mock private DanhMucSanPhamDao danhMucSanPhamDao;
  @Mock private HoaDonDao hoaDonDao;
  @Mock private KhuyenMaiDao khuyenMaiDao;
  @Mock private MauSanPhamDao mauSanPhamDao;
  @Mock private NhanVienDao nhanVienDao;
  @Mock private SanPhamDao sanPhamDao;
  @Mock private SizeSanPhamDao sizeSanPhamDao;

  private ChiTietHoaDonService chiTietHoaDonService;
  private ChiTietKhuyenMaiService chiTietKhuyenMaiService;
  private ChiTietSanPhamService chiTietSanPhamService;
  private ChucVuService chucVuService;
  private DanhMucSanPhamService danhMucSanPhamService;
  private HoaDonService hoaDonService;
  private KhuyenMaiService khuyenMaiService;
  private MauSanPhamService mauSanPhamService;
  private NhanVienService nhanVienService;
  private SanPhamService sanPhamService;
  private SizeSanPhamService sizeSanPhamService;

  @BeforeEach
  void setUp() {
    chiTietHoaDonService = new ChiTietHoaDonService(chiTietHoaDonDao);
    chiTietKhuyenMaiService = new ChiTietKhuyenMaiService(chiTietKhuyenMaiDao);
    chiTietSanPhamService = new ChiTietSanPhamService(chiTietSanPhamDao);
    chucVuService = new ChucVuService(chucVuDao);
    danhMucSanPhamService = new DanhMucSanPhamService(danhMucSanPhamDao);
    hoaDonService = new HoaDonService(hoaDonDao);
    khuyenMaiService = new KhuyenMaiService(khuyenMaiDao);
    mauSanPhamService = new MauSanPhamService(mauSanPhamDao);
    nhanVienService = new NhanVienService(nhanVienDao);
    sanPhamService = new SanPhamService(sanPhamDao);
    sizeSanPhamService = new SizeSanPhamService(sizeSanPhamDao);
  }

  @Test
  void chiTietHoaDonMethodsDelegateToDao() {
    ChiTietHoaDon detail = new ChiTietHoaDon();
    List<ChiTietHoaDon> details = List.of(detail);
    when(chiTietHoaDonDao.getListCTHoaDonByID(7)).thenReturn(details);
    when(chiTietHoaDonDao.getInforChiTietHoaDon(7, 9)).thenReturn(detail);
    when(chiTietHoaDonDao.checkExitsSanPham(7, 9)).thenReturn(true);
    when(chiTietHoaDonDao.addChiTietHoaDon(detail)).thenReturn(1);
    when(chiTietHoaDonDao.updateChiTietHoaDon(detail)).thenReturn(true);
    when(chiTietHoaDonDao.deleteChiTietHoaDon(detail)).thenReturn(true);

    assertSame(details, chiTietHoaDonService.getListCTHoaDonByID(7));
    assertSame(detail, chiTietHoaDonService.getInforChiTietHoaDon(7, 9));
    assertTrue(chiTietHoaDonService.checkExitsSanPham(7, 9));
    assertEquals(1, chiTietHoaDonService.addChiTietHoaDon(detail));
    assertTrue(chiTietHoaDonService.updateChiTietHoaDon(detail));
    assertTrue(chiTietHoaDonService.deleteChiTietHoaDon(detail));
    verifyNoMoreInteractions(chiTietHoaDonDao);
  }

  @Test
  void chiTietKhuyenMaiMethodsDelegateToDao() {
    ChiTietKhuyenMai detail = new ChiTietKhuyenMai();
    List<ChiTietKhuyenMai> details = List.of(detail);
    when(chiTietKhuyenMaiDao.getAllChiTietKhuyenMai()).thenReturn(details);
    when(chiTietKhuyenMaiDao.addNewKhuyenMaiChoSP(detail)).thenReturn(4);
    when(chiTietKhuyenMaiDao.deletKhuyenMaiSP(detail)).thenReturn(true);

    assertSame(details, chiTietKhuyenMaiService.getAllChiTietKhuyenMai());
    assertEquals(4, chiTietKhuyenMaiService.addNewKhuyenMaiChoSP(detail));
    assertTrue(chiTietKhuyenMaiService.deletKhuyenMaiSP(detail));
    verifyNoMoreInteractions(chiTietKhuyenMaiDao);
  }

  @Test
  void chiTietSanPhamMethodsDelegateToDao() {
    ChiTietSanPham detail = new ChiTietSanPham();
    List<ChiTietSanPham> details = List.of(detail);
    when(chiTietSanPhamDao.getInforCTSanPhambyID(3)).thenReturn(details);
    when(chiTietSanPhamDao.addCTSanPham(detail)).thenReturn(5);
    when(chiTietSanPhamDao.updateCTSanPham(detail)).thenReturn(true);
    when(chiTietSanPhamDao.deleteCTSanPham(detail)).thenReturn(true);
    when(chiTietSanPhamDao.deleteCTSanPhamByIdSanPham(3)).thenReturn(true);

    assertSame(details, chiTietSanPhamService.getInforCTSanPhambyID(3));
    assertEquals(5, chiTietSanPhamService.addCTSanPham(detail));
    assertTrue(chiTietSanPhamService.updateCTSanPham(detail));
    assertTrue(chiTietSanPhamService.deleteCTSanPham(detail));
    assertTrue(chiTietSanPhamService.deleteCTSanPhamByIdSanPham(3));
    verifyNoMoreInteractions(chiTietSanPhamDao);
  }

  @Test
  void chucVuServiceLooksUpIdAndDelegatesMutations() {
    ChucVu role = ChucVu.builder().machucvu(3).tenchucvu("Customer").build();
    List<ChucVu> roles = List.of(ChucVu.builder().machucvu(1).build(), role);
    when(chucVuDao.getAllChucVu()).thenReturn(roles);
    when(chucVuDao.addChucVu(role)).thenReturn(3);
    when(chucVuDao.updateChucVu(role)).thenReturn(true);
    when(chucVuDao.deleteChucVu(role)).thenReturn(true);
    when(chucVuDao.checkNameChucVu("Customer")).thenReturn(true);

    assertSame(role, chucVuService.getById(3));
    assertNull(chucVuService.getById(99));
    assertSame(roles, chucVuService.getAllChucVu());
    assertEquals(3, chucVuService.addChucVu(role));
    assertTrue(chucVuService.updateChucVu(role));
    assertTrue(chucVuService.deleteChucVu(role));
    assertTrue(chucVuService.checkNameChucVu("Customer"));
    verify(chucVuDao, times(3)).getAllChucVu();
    verifyNoMoreInteractions(chucVuDao);
  }

  @Test
  void danhMucMethodsDelegateToDao() {
    DanhMucSanPham category = new DanhMucSanPham();
    List<DanhMucSanPham> categories = List.of(category);
    when(danhMucSanPhamDao.getAllDanhMucSanPham()).thenReturn(categories);
    when(danhMucSanPhamDao.addDanhMucSanPham(category)).thenReturn(2);
    when(danhMucSanPhamDao.updateDanhMucSanPham(category)).thenReturn(true);
    when(danhMucSanPhamDao.deleteDanhMucSanPham(category)).thenReturn(true);
    when(danhMucSanPhamDao.checkNameDanhMucBeforeAdd("Shirts")).thenReturn(false);

    assertSame(categories, danhMucSanPhamService.getAllDanhMucSanPham());
    assertEquals(2, danhMucSanPhamService.addDanhMucSanPham(category));
    assertTrue(danhMucSanPhamService.updateDanhMucSanPham(category));
    assertTrue(danhMucSanPhamService.deleteDanhMucSanPham(category));
    assertFalse(danhMucSanPhamService.checkNameDanhMucBeforeAdd("Shirts"));
    verifyNoMoreInteractions(danhMucSanPhamDao);
  }

  @Test
  void hoaDonMethodsDelegateToDao() {
    HoaDon invoice = new HoaDon();
    List<HoaDon> invoices = List.of(invoice);
    when(hoaDonDao.getListCanDuyetHoaDonAdmin()).thenReturn(invoices);
    when(hoaDonDao.getListHoaDonDaDuyet()).thenReturn(invoices);
    when(hoaDonDao.getListHoaDonUser("Customer")).thenReturn(invoices);
    when(hoaDonDao.addNewHoaDonUser(invoice)).thenReturn(8);
    when(hoaDonDao.updateHoaDon(invoice)).thenReturn(true);

    assertSame(invoices, hoaDonService.getListCanDuyetHoaDonAdmin());
    assertSame(invoices, hoaDonService.getListHoaDonDaDuyet());
    assertSame(invoices, hoaDonService.getListHoaDonUser("Customer"));
    assertEquals(8, hoaDonService.addNewHoaDonUser(invoice));
    assertTrue(hoaDonService.updateHoaDon(invoice));
    verifyNoMoreInteractions(hoaDonDao);
  }

  @Test
  void khuyenMaiMethodsDelegateToDao() {
    KhuyenMai promotion = new KhuyenMai();
    List<KhuyenMai> promotions = List.of(promotion);
    when(khuyenMaiDao.getAllKhuyenMai()).thenReturn(promotions);
    when(khuyenMaiDao.addKhuyenMai(promotion)).thenReturn(6);
    when(khuyenMaiDao.updateKhuyenMai(promotion)).thenReturn(true);
    when(khuyenMaiDao.deleteKhuyenMai(promotion)).thenReturn(true);
    when(khuyenMaiDao.checkNameKhuyenMai("Sale")).thenReturn(true);

    assertSame(promotions, khuyenMaiService.getAllKhuyenMai());
    assertEquals(6, khuyenMaiService.addKhuyenMai(promotion));
    assertTrue(khuyenMaiService.updateKhuyenMai(promotion));
    assertTrue(khuyenMaiService.deleteKhuyenMai(promotion));
    assertTrue(khuyenMaiService.checkNameKhuyenMai("Sale"));
    verifyNoMoreInteractions(khuyenMaiDao);
  }

  @Test
  void mauSanPhamMethodsDelegateToDao() {
    MauSanPham color = new MauSanPham();
    List<MauSanPham> colors = List.of(color);
    when(mauSanPhamDao.getAllMauSanPham()).thenReturn(colors);
    when(mauSanPhamDao.addMauSanPham(color)).thenReturn(5);
    when(mauSanPhamDao.updateMauSanPham(color)).thenReturn(true);
    when(mauSanPhamDao.deleteMauSanPham(color)).thenReturn(true);
    when(mauSanPhamDao.checkMauSanPham("Blue")).thenReturn(true);

    assertSame(colors, mauSanPhamService.getAllMauSanPham());
    assertEquals(5, mauSanPhamService.addMauSanPham(color));
    assertTrue(mauSanPhamService.updateMauSanPham(color));
    assertTrue(mauSanPhamService.deleteMauSanPham(color));
    assertTrue(mauSanPhamService.checkMauSanPham("Blue"));
    verifyNoMoreInteractions(mauSanPhamDao);
  }

  @Test
  void nhanVienMethodsDelegateToDao() {
    NhanVien employee = new NhanVien();
    List<NhanVien> employees = List.of(employee);
    when(nhanVienDao.login("a@b.com", "pw")).thenReturn(true);
    when(nhanVienDao.checkUser("a@b.com", "pw")).thenReturn(true);
    when(nhanVienDao.getInforby_User_Pass("a@b.com", "pw")).thenReturn(employee);
    when(nhanVienDao.checkEmail("a@b.com")).thenReturn(true);
    when(nhanVienDao.checkUserName("user")).thenReturn(true);
    when(nhanVienDao.getInforbyId(2)).thenReturn(employee);
    when(nhanVienDao.register(employee)).thenReturn(2);
    when(nhanVienDao.getallTaiKhoan()).thenReturn(employees);
    when(nhanVienDao.checkAdmin("a@b.com", "pw")).thenReturn(true);
    when(nhanVienDao.verifyEmailToken("token", LocalDateTime.MIN)).thenReturn(true);
    when(nhanVienDao.updateInfor(employee)).thenReturn(true);
    when(nhanVienDao.deleteNhanVien(employee)).thenReturn(true);

    assertTrue(nhanVienService.login("a@b.com", "pw"));
    assertTrue(nhanVienService.checkUser("a@b.com", "pw"));
    assertSame(employee, nhanVienService.getInforby_User_Pass("a@b.com", "pw"));
    assertTrue(nhanVienService.checkEmail("a@b.com"));
    assertTrue(nhanVienService.checkUserName("user"));
    assertSame(employee, nhanVienService.getInforbyId(2));
    assertEquals(2, nhanVienService.register(employee));
    assertSame(employees, nhanVienService.getallTaiKhoan());
    assertTrue(nhanVienService.checkAdmin("a@b.com", "pw"));
    assertTrue(nhanVienService.verifyEmailToken("token", LocalDateTime.MIN));
    assertTrue(nhanVienService.updateInfor(employee));
    assertTrue(nhanVienService.deleteNhanVien(employee));
    verifyNoMoreInteractions(nhanVienDao);
  }

  @Test
  void sanPhamRemainingMethodsDelegateToDao() {
    SanPham product = new SanPham();
    List<SanPham> products = List.of(product);
    when(sanPhamDao.getAllSanPhamByIdDanhMuc(4)).thenReturn(products);
    when(sanPhamDao.updateSanPham(product)).thenReturn(true);
    when(sanPhamDao.deleteSanPham(product)).thenReturn(true);
    when(sanPhamDao.checkNameSanPhamBeforeAdd("Shirt")).thenReturn(false);

    assertSame(products, sanPhamService.getAllSanPhamByIdDanhMuc(4));
    assertTrue(sanPhamService.updateSanPham(product));
    assertTrue(sanPhamService.deleteSanPham(product));
    assertFalse(sanPhamService.checkNameSanPhamBeforeAdd("Shirt"));
    verifyNoMoreInteractions(sanPhamDao);
  }

  @Test
  void sizeSanPhamMethodsDelegateToDao() {
    SizeSanPham size = new SizeSanPham();
    List<SizeSanPham> sizes = List.of(size);
    when(sizeSanPhamDao.getAllSizeSanPham()).thenReturn(sizes);
    when(sizeSanPhamDao.addSizeSanPham(size)).thenReturn(4);
    when(sizeSanPhamDao.updateSizeSanPham(size)).thenReturn(true);
    when(sizeSanPhamDao.deleteSizeSanPham(size)).thenReturn(true);

    assertSame(sizes, sizeSanPhamService.getAllSizeSanPham());
    assertEquals(4, sizeSanPhamService.addSizeSanPham(size));
    assertTrue(sizeSanPhamService.updateSizeSanPham(size));
    assertTrue(sizeSanPhamService.deleteSizeSanPham(size));
    verifyNoMoreInteractions(sizeSanPhamDao);
  }
}
