package com.banhang.controller.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.DanhMucSanPham;
import com.banhang.entity.MauSanPham;
import com.banhang.entity.SanPham;
import com.banhang.entity.SizeSanPham;
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
public class QuanLyAdminController {
	
	@Autowired
	ServletContext context;
	
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
				List<DanhMucSanPham> danhMucSanPhams=danhMucSanPhamService.getAllDanhMucSanPham();
				modelMap.addAttribute("danhMucSanPhams", danhMucSanPhams);
				List<MauSanPham> mauSanPhams=mauSanPhamService.getAllMauSanPham();
				modelMap.addAttribute("mauSanPhams", mauSanPhams);
				List<SizeSanPham> sizeSanPhams= sizeSanPhamService.getAllSizeSanPham();
				modelMap.addAttribute("sizeSanPhams", sizeSanPhams);
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
		for(int i=0;i<sanPham.size();i++) {
			boolean results =chiTietSanPhamServie.deleteCTSanPhamByIdSanPham(sanPham.get(i).getMasanpham());
			if(results==true) {
				sanPhamService.deleteSanPham(sanPham.get(i));
				System.out.println("Đã xóa sp có mã " + sanPham.get(i).getMasanpham());
			}else {
				System.out.println("Loi");
			}
			
		}
		return "true";
	}
	
	
	
	@PostMapping("/update")
	public String updatesanpham(@RequestParam int masanpham,ModelMap map, HttpSession httpSession) {
		boolean temp=checkSecurity(httpSession);
		if(temp==true) {
			if(isNumeric(String.valueOf(masanpham))==true) {
				SanPham sanPhamUpdate= new SanPham();
				sanPhamUpdate = sanPhamService.getListSanPhamById(masanpham);
				
				
				return "MG_product_admin";
			}else {
				return "404";
			}
		}else {
			return "401";
		}
	}
	@PostMapping("addimage")
	@ResponseBody
	public String addImage(MultipartHttpServletRequest request) {
		String path_save_files = context.getRealPath("/resourcess/admin/images/");
		Iterator<String> listImageNames = request.getFileNames();
		MultipartFile mpf = request.getFile(listImageNames.next());
		File file_save = new File(path_save_files + mpf.getOriginalFilename());
		try {
			mpf.transferTo(file_save);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(path_save_files);
		return "true";
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
