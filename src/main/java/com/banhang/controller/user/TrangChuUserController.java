package com.banhang.controller.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.DanhMucSanPham;
import com.banhang.entity.GioHang;
import com.banhang.entity.SanPham;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.DanhMucSanPhamService;
import com.banhang.service.SanPhamService;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
public class TrangChuUserController {
	
	@Autowired
	DanhMucSanPhamService danhMucSanPhamService;
	
	@Autowired
	SanPhamService sanPhamService;
	
	@Autowired
	ChiTietSanPhamService chiTietSanPhamService;
	
	List<GioHang> gioHangs= new ArrayList<GioHang>();
	
	@GetMapping("/")
	public String Default(ModelMap map, HttpSession htSession) {
		//get list Danh muc San Pham
		List<DanhMucSanPham> showListDanhMucSP= danhMucSanPhamService.getAllDanhMucSanPham();
		
		//get list San Pham
		List<SanPham> showListSanPham=sanPhamService.getAllSanPham();
		
		//get acount
		TaiKhoanLogin taiKhoan =(TaiKhoanLogin) htSession.getAttribute("taikhoan");
		
		map.addAttribute("showListDanhMucSP",showListDanhMucSP);
		map.addAttribute("showListSanPham",showListSanPham);
		map.addAttribute("taiKhoan",taiKhoan);
		
		return "index_user";
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
	
	@GetMapping("themgiohang")
	public String themgiohang(@RequestParam("masp") int masp, @RequestParam("masize") int masize,@RequestParam("mamau") int mamau,
			@RequestParam("tensp") String tensp,@RequestParam("giatien") String giatien,@RequestParam("tenmau") String tenmau,@RequestParam("tensize") String tensize,
			@RequestParam("soluong")int soluong,HttpSession httpSession) {
		if(httpSession.getAttribute("giohang")==null) {
			GioHang gioHang= new GioHang();
			gioHang.setMamau(mamau);
			gioHang.setMasize(masize);
			gioHang.setMasp(masp);
			gioHang.setTenmau(tenmau);
			gioHang.setTensize(tensize);
			gioHang.setTensp(tensp);
			gioHang.setGiatien(giatien);
			gioHang.setSoluong(soluong);
			gioHangs.add(gioHang);
			httpSession.setAttribute("giohang", gioHangs);
		}else {
			int vitri=kiemTraSanPhamTonTai(masp, masize, mamau, httpSession);
			if(vitri == -1) {
				List<GioHang> listGioHang=(List<GioHang>) httpSession.getAttribute("giohang");
				GioHang gioHang= new GioHang();
				gioHang.setMamau(mamau);
				gioHang.setMasize(masize);
				gioHang.setMasp(masp);
				gioHang.setTenmau(tenmau);
				gioHang.setTensize(tensize);
				gioHang.setTensp(tensp);
				gioHang.setGiatien(giatien);
				gioHang.setSoluong(soluong);
				listGioHang.add(gioHang);
			}else {
				List<GioHang> listGioHang=(List<GioHang>) httpSession.getAttribute("giohang");
				int soluongmoi=listGioHang.get(vitri).getSoluong()+soluong;
				listGioHang.get(vitri).setSoluong(soluongmoi);
			}
		}
		return "success";
	}
	
	private int kiemTraSanPhamTonTai(int masp,int masize,int mamau,HttpSession httpSession) {
		List<GioHang> listGioHang=(List<GioHang>) httpSession.getAttribute("giohang");
		for(int i=0;i<listGioHang.size();i++) {
			if(listGioHang.get(i).getMasp()==masp&& listGioHang.get(i).getMasize()==masize&&listGioHang.get(i).getMamau()== mamau) {
				return i;
			}
		}
		return -1;
	}
	@GetMapping("giohang")
	public String loadGioHang(HttpSession httpSession,ModelMap map) {
		if(httpSession.getAttribute("giohang")!=null) {
			List<GioHang> gioHangs=(List<GioHang>) httpSession.getAttribute("giohang");
			System.out.println("Size" + gioHangs.size());
			System.out.println(gioHangs.get(0).toString());
			map.addAttribute("soluonggiohang", gioHangs.size());
			map.addAttribute("gioHangs", gioHangs);
		}
		return "cart_user";
	}
	
	@GetMapping("giohang/delete/{masp}{mamau}{masize}")
	public String xoaSanPhamKhoiGio(int masp,int mamau,int masize,HttpSession httpSession,ModelMap map) {
		return null;
	}
	@GetMapping("giohang/update/{masp}{mamau}{masize}{soluong}")
	public String upadateSanPhamKhoiGio(int masp,int mamau,int masize,HttpSession httpSession,ModelMap map) {
		return null;
	}
	
}
