package com.banhang.imp;

import java.util.List;

import com.banhang.entity.ChucVu;

public interface ChucVuImp {
	public List<ChucVu> getAllChucVu();
	public int addChucVu(ChucVu cv);
	public boolean updateChucVu(ChucVu cv);
	public boolean deleteChucVu(ChucVu cv);
	public boolean checkNameChucVu(String name);
}
