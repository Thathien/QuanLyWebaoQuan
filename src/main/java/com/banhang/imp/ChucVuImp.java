package com.banhang.imp;

import java.util.List;

import org.apache.tomcat.websocket.AsyncChannelGroupUtil;

import com.banhang.entity.ChucVu;

public interface ChucVuImp {
	public List<ChucVu> getAllChucVu();
	public int addChucVu(ChucVu cv);
	public boolean updateChucVu(ChucVu cv);
	public boolean deleteChucVu(ChucVu cv);
	public boolean checkNameChucVu(String name);
	public ChucVu getById(int id);
}
