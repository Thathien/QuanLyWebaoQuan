package com.banhang.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/admin/product")
public class QuanLySanPhamAdminController {
	
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
	
	@GetMapping()
	public String Default(ModelMap modelMap,HttpSession httpSession) {
		TaiKhoanLogin taiKhoanLogin=(TaiKhoanLogin) httpSession.getAttribute("taikhoan");
		if(taiKhoanLogin!=null) {
			if(taiKhoanLogin.getMachucvu()==1) {
				List<SanPham> listSanPhams=sanPhamService.getAllSanPham();
				modelMap.addAttribute("listSanPhamss", listSanPhams);
				System.out.println(listSanPhams.size());
				return "MG_product_admin";
			}else {
				return "401";
			}
		}
		return "redirect:/admin/dangnhap";
	}
	@GetMapping("/detal/{id}")
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
	@PostMapping("/delete")
	public String xoasanpham(@RequestParam String listIdSanPham,HttpSession httpSession) {
		String[] maSanPham=listIdSanPham.trim().split(",");
		List<SanPham> sanPham = new ArrayList<SanPham>();
		for(int i=0;i<maSanPham.length;i++) {
			if(isNumeric(maSanPham[i])==true) {
				SanPham sp= sanPhamService.getListSanPhamById(Integer.parseInt(maSanPham[i]));
				sanPham.add(sp);
			}
		}
//		for(int i=0;i<sanPham.size();i++) {
//			List<Integer> listChiTietSP=sanPham.get(i).getChiTietSanPham();
//			
//		}
//		
		return null;
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
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
}
