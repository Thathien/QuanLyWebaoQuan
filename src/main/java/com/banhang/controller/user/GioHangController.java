package com.banhang.controller.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoaDonId;
import com.banhang.entity.GioHang;
import com.banhang.entity.HoaDon;
import com.banhang.service.ChiTietHoaDonService;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.HoaDonService;
import com.banhang.service.NhanVienService;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
public class GioHangController {
	
	@Autowired
	ChiTietSanPhamService chiTietSanPhamService;
	
	List<GioHang> gioHangs= new ArrayList<GioHang>();
	
	@Autowired
	HoaDonService hoaDonService;
	
	@Autowired
	ChiTietHoaDonService chiTietHoaDonService;
	
	@Autowired
	private JavaMailSender mailsender;
	
	@Autowired
	NhanVienService nhanVienService;
	
	@GetMapping("themgiohang")
	@ResponseBody
	public String themgiohang(@RequestParam int machitietsanpham, @RequestParam int masp,@RequestParam int masize,@RequestParam int mamau,@RequestParam String tensp,@RequestParam String giatien,@RequestParam String tenmau,@RequestParam String tensize,@RequestParam int soluong,HttpSession httpSession, ModelMap map) {
		if(httpSession.getAttribute("giohang")==null) {
			GioHang gioHang= new GioHang();
			gioHang.setMachitietsanpham(machitietsanpham);
			gioHang.setMamau(mamau);
			gioHang.setMasize(masize);
			gioHang.setMasp(masp);
			gioHang.setTenmau(tenmau);
			gioHang.setTensize(tensize);
			gioHang.setTensp(tensp);
			gioHang.setGiatien(giatien);
			gioHang.setSoluong(soluong);
			gioHangs.add(gioHang);
//			System.out.println(gioHangs.toString());
//			httpSession.setAttribute("giohang", gioHangs);
			map.addAttribute("giohang", gioHangs);
		}else {
			int vitri=kiemTraSanPhamTonTai(masp, masize, mamau, httpSession);
			if(vitri == -1) {
				List<GioHang> listGioHang=(List<GioHang>) httpSession.getAttribute("giohang");
				GioHang gioHang= new GioHang();
				gioHang.setMachitietsanpham(machitietsanpham);
				gioHang.setMamau(mamau);
				gioHang.setMasize(masize);
				gioHang.setMasp(masp);
				gioHang.setTenmau(tenmau);
				gioHang.setTensize(tensize);
				gioHang.setTensp(tensp);
				gioHang.setGiatien(giatien);
				gioHang.setSoluong(soluong);
				listGioHang.add(gioHang);
//				System.out.println(listGioHang.toString());
			}else {
				List<GioHang> listGioHang=(List<GioHang>) httpSession.getAttribute("giohang");
				int soluongmoi=listGioHang.get(vitri).getSoluong()+soluong;
//				System.out.println("soluong moi "+soluongmoi);
				listGioHang.get(vitri).setSoluong(soluongmoi);
			}
		}
		return "true";
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
	private int kiemTraSanPhamTonTaiTheoMaChiTiet(int machitietsanpham,HttpSession httpSession) {
		List<GioHang> listGioHang=(List<GioHang>) httpSession.getAttribute("giohang");
		for(int i=0;i<listGioHang.size();i++) {
			if(listGioHang.get(i).getMachitietsanpham()==machitietsanpham) {
				return i;
			}
		}
		return -1;
	}
	@GetMapping("giohang")
	public String loadGioHang(HttpSession httpSession,ModelMap map) {
		int temp=checkSecurityUser(httpSession);
		if(temp==1) {
			return "redirect:/admin";
		}else {
			if(httpSession.getAttribute("giohang")!=null) {
				List<GioHang> gioHangs=(List<GioHang>) httpSession.getAttribute("giohang");
				boolean isEmpty=gioHangs.isEmpty();
				map.addAttribute("isEmpty",isEmpty);
				map.addAttribute("soluonggiohang", gioHangs.size());
				map.addAttribute("gioHangs", gioHangs);
			}
			return "cart_user";
		}
	}
	
	@GetMapping("giohang/delete/")
	public String xoaSanPhamKhoiGio(@RequestParam int masp,@RequestParam int mamau,@RequestParam int masize,HttpSession httpSession,ModelMap map) {
		List<GioHang> listGioHangs=(List<GioHang>) httpSession.getAttribute("giohang");
		int vitri=kiemTraSanPhamTonTai(masp, masize, mamau, httpSession);
		listGioHangs.remove(vitri);
		return "true";
	}
	@GetMapping("giohang/update/")
	@ResponseBody
	public String upadateSanPhamKhoiGio(@RequestParam int masp,@RequestParam int masize,@RequestParam int mamau,@RequestParam int soluong,HttpSession httpSession,ModelMap map) {
		List<GioHang> listGioHangs=(List<GioHang>) httpSession.getAttribute("giohang");
		int vitri=kiemTraSanPhamTonTai(masp, masize, mamau, httpSession);
		listGioHangs.get(vitri).setSoluong(soluong);
		return "true";
	}
//	
//	@GetMapping("giohang/update/")
//	@ResponseBody
//	public Boolean upadateSanPhamKhoiGio(@RequestParam int machitietsanpham,@RequestParam int soluong,HttpSession httpSession,ModelMap map) {
//		List<GioHang> listGioHangs=(List<GioHang>) httpSession.getAttribute("giohang");
////		int vitri=kiemTraSanPhamTonTai(masp, masize, mamau, httpSession);
//		int vitri=kiemTraSanPhamTonTaiTheoMaChiTiet(machitietsanpham, httpSession);
//		listGioHangs.get(vitri).setSoluong(soluong);
//		return true;
//	}
	@PostMapping("giohang")
	public String themHoaDon(@RequestParam String tenkhachhang,@RequestParam String emailkhachang,@RequestParam String sodienthoai,@RequestParam String hinhthucgiao,@RequestParam String diachigiaohang,@RequestParam String ghichu,HttpSession httpSession) throws MessagingException {
		if(null!=httpSession.getAttribute("giohang")) {
			List<GioHang> listGioHangs=(List<GioHang>) httpSession.getAttribute("giohang");
			HoaDon hoaDon = new HoaDon();
			hoaDon.setTenkhachhang(tenkhachhang);
			hoaDon.setSdt(sodienthoai);
			hoaDon.setDiachigiaohang(diachigiaohang);
			hoaDon.setGhichu(ghichu);
			String hinhthucgiaohang;
			if(hinhthucgiao.equals("0")) {
				hinhthucgiaohang="Giao hàng tận nơi";
			}else {
				hinhthucgiaohang="Nhận tại cửa hàng";
			}
			hoaDon.setHinhthucgiaohang(hinhthucgiaohang);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
			 Date date = new Date();  
			String ngaylap=formatter.format(date);
			hoaDon.setNgaylap(ngaylap);
			hoaDon.setTinhtrang(0);
			int idHoaDon=hoaDonService.addNewHoaDonUser(hoaDon);
			if(idHoaDon>0) {
				Set<ChiTietHoaDon> listChiTietHoaDons= new HashSet<ChiTietHoaDon>();
				for (int i = 0; i <listGioHangs.size(); i++) {
					ChiTietHoaDonId chiTietHoaDonId= new ChiTietHoaDonId();
					chiTietHoaDonId.setMachitietsanpham(listGioHangs.get(i).getMachitietsanpham());
					ChiTietHoaDon chiTietHoaDon= new ChiTietHoaDon();
					chiTietHoaDon.setChiTietHoaDonId(chiTietHoaDonId);
					chiTietHoaDon.setGiatien(listGioHangs.get(i).getGiatien());
					chiTietHoaDonService.addChiTietHoaDon(chiTietHoaDon);
				}
				String temp="";
				double tongtien=0;
				for(int i=0;i<gioHangs.size();i++) {
					int masp=gioHangs.get(i).getMasp();
					String tensp=gioHangs.get(i).getTensp();
					String sosize=gioHangs.get(i).getTensize();
					String somau=gioHangs.get(i).getTenmau();
					int soluong=gioHangs.get(i).getSoluong();
					String giasp=gioHangs.get(i).getGiatien();
					String thanhtien= String.valueOf(soluong+Integer.parseInt(giasp));
					tongtien+=(soluong+Integer.parseInt(giasp));
					temp+="<tr>"
							+"<td>" +masp+"</td>"
							+"<td>" +tensp+"</td>"
							+"<td>" +sosize+"</td>"
							+"<td>" +somau+"</td>"
							+"<td>" +soluong+"</td>"
							+"<td>" +giasp +",000 VNĐ"+"</td>"
							+"<td>" +giasp+",000 VNĐ"+"</td>"
						+"</tr>" +"/n";
				}
				MimeMessage message= mailsender.createMimeMessage();
				boolean multipart = true;
				MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
				String messinfor ="<h1>Hello: " +tenkhachhang+ "</h1>"
						+"<h2>Cảm ơn bạn đã tin dùng mua hàng của chúng tôi </h2>"
						+"<h3 style ='color:red'>Mã hóa đơn:  "+ idHoaDon+ "</h3>"
						+"<h3 style ='color:red'>Ngày đặt: "+ ngaylap+ "</h3>"
						+"<h3>Hình thức giao hàng:  "+ hinhthucgiaohang+ "</h3>"
						+"<h3>Số điện thoại liên hệ:  "+ sodienthoai+ "</h3>"
						+"<h3>Ghi chú:  "+ ghichu+ "</h3>"
						+"</br>"
						+"<table>"
							+"<caption> Thông tin sản phẩm</caption>"
							+"<thead>"
								+"<tr>"
									+"<th>Mã sản phẩm</th>"
									+"<th>Tên sản phẩmm</th>"
									+"<th>Số Size</th>"
									+"<th>Màu</th>"
									+"<th>Số lượng mua</th>"
									+"<th>Giá sản phẩm</th>"
									+"<th>Thành tiền</th>"
								+"</tr>"
							+"</thead>"
							+"<tbody>"
							+temp
							+"</tbody>"
						+"</table>"
						+"<h2 style ='color:red'>Tổng tiền:  "+ tongtien+",000 VNĐ"+"</h3>"
						
						;
				message.setContent(messinfor, "text/html");
				helper.setSubject("Cảm ơn bạn đã mua tin tưởng đặt hàng chúng tôi!");
				helper.setTo(emailkhachang);
				
				mailsender.send(message);
				return "true";
			}
		}
		return "fasle";
	}
	public int checkSecurityUser(HttpSession httpSession) {
		TaiKhoanLogin taiKhoanLogin=(TaiKhoanLogin) httpSession.getAttribute("taikhoan");
			if(taiKhoanLogin!=null) {
				return taiKhoanLogin.getMachucvu();
			}
		return 0;
	}
}
