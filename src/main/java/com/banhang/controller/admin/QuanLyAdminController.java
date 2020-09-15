package com.banhang.controller.admin;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.SanPham;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.DanhMucSanPhamService;
import com.banhang.service.HoaDonService;
import com.banhang.service.KhuyenMaiService;
import com.banhang.service.MauSanPhamService;
import com.banhang.service.NhanVienService;
import com.banhang.service.SanPhamService;
import com.banhang.service.SizeSanPhamService;

@Controller
@SessionAttributes({"taikhoan"})
public class QuanLyAdminController {
	
	@Autowired
	SanPhamService sanPhamService;
	
	@Autowired
	ChiTietSanPhamService chiTietSanPhamServie;
	
	@Autowired
	DanhMucSanPhamService danhMucSanPhamService;
	
	@Autowired
	HoaDonService hoDonService;
	
	@Autowired
	KhuyenMaiService khuyenMaiSerVice;
	
	@Autowired
	MauSanPhamService mauSanPhamService;
	
	@Autowired
	SizeSanPhamService sizeSanPhamService;
	
	@Autowired
	NhanVienService nhanVienService;
	
	@GetMapping("admin/product")
	public String index(ModelMap modelMap,HttpSession httpSession) {
		TaiKhoanLogin taiKhoanLogin=(TaiKhoanLogin) httpSession.getAttribute("taikhoan");
		if(taiKhoanLogin!=null) {
			if(taiKhoanLogin.getMachucvu()==1) {
				List<SanPham> listSanPhams=sanPhamService.getAllSanPham();
				modelMap.addAttribute("listSanPhams", listSanPhams);
				return "MG_product_admin";
			}else {
				return "401";
			}
		}
		return "login_admin";
	}
	@GetMapping("admin/product/detal/{id}")
	public String chitietsanpham(@PathVariable("id") String id, ModelMap map,HttpSession httpSession) {
		boolean checkS=checkSecurity(httpSession);
		if(checkS==true) {
			int masanpham=Integer.parseInt(id);
			SanPham sanphamRS=sanPhamService.getListSanPhamById(masanpham);
			map.addAttribute("sanphamRS",sanphamRS);
			return "MG_product_admin";
		}else {
			return "401";
		}
		
	}
	
	
	//admin -> tru  // other  -> false
	public boolean checkSecurity(HttpSession httpSession) {
		TaiKhoanLogin taiKhoanLogin=(TaiKhoanLogin) httpSession.getAttribute("taikhoan");
		if(taiKhoanLogin!=null) {
			if(taiKhoanLogin.getMachucvu()==1) {
				return true;
			}
		}
		return false;
	}
}
