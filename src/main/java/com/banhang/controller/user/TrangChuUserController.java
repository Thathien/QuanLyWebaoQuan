package com.banhang.controller.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.dao.ChiTietHoaDonDao;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoaDonId;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.ChucVu;
import com.banhang.entity.DanhMucSanPham;
import com.banhang.entity.GioHang;
import com.banhang.entity.HoaDon;
import com.banhang.entity.NhanVien;
import com.banhang.entity.SanPham;
import com.banhang.model.DangKyModel;
import com.banhang.model.DangNhapModel;
import com.banhang.service.ChiTietHoaDonService;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.DanhMucSanPhamService;
import com.banhang.service.HoaDonService;
import com.banhang.service.NhanVienService;
import com.banhang.service.SanPhamService;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
@RequestMapping("/")
public class TrangChuUserController {
	
	@Autowired
	DanhMucSanPhamService danhMucSanPhamService;
	
	@Autowired
	SanPhamService sanPhamService;

	//@SessionAttribute("taikhoan") TaiKhoanLogin taikhoan
	@GetMapping
	@Transactional
	public String Default(ModelMap map, HttpSession httpSession) {
		int temp=checkSecurityUser(httpSession);
		if(temp==1) {
			return "redirect:/admin";
		}else {
			//get list Danh muc San Pham
			List<DanhMucSanPham> showListDanhMucSP= danhMucSanPhamService.getAllDanhMucSanPham();
			
			//get list San Pham
			List<SanPham> showListSanPham=sanPhamService.getAllSanPham();
			map.addAttribute("showListDanhMucSP",showListDanhMucSP);
			map.addAttribute("showListSanPham",showListSanPham);
			
			return "index_user";
		}
		
	}
	@GetMapping("chi-tiet-san-pham/{id}")
	public String chitietsanpham(@PathVariable("id") String id, ModelMap map) {
		int masanpham=Integer.parseInt(id);
		SanPham sanphamRS=sanPhamService.getListSanPhamById(masanpham);
		List<DanhMucSanPham> showListDanhMucSP= danhMucSanPhamService.getAllDanhMucSanPham();
		map.addAttribute("sanphamRS",sanphamRS);
		map.addAttribute("showListDanhMucSP",showListDanhMucSP);
		
		
		return "product-details_user";
	}
	public int checkSecurityUser(HttpSession httpSession) {
		TaiKhoanLogin taiKhoanLogin=(TaiKhoanLogin) httpSession.getAttribute("taikhoan");
			if(taiKhoanLogin!=null) {
				return taiKhoanLogin.getMachucvu();
			}
		return 0;
	}
	
}
