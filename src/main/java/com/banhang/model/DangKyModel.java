package com.banhang.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class đại diện cho thông tin đăng ký của người dùng.
 *
 * @author System
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DangKyModel {
  private int id;

  @Size(max = 40, min = 5, message = "Yêu cầu nhập lại họ tên") private String hotendk;

  @Size(min = 10, message = "Email không hợp lệ") private String emaildk;

  @Size(min = 6, message = "Mật khẩu không hợp lệ") private String matkhaudk;

  private String nhaplaimkdk;

  private String gioitinhdk;

  @Size(min = 6, message = "Địa chỉ không hợp lệ") private String diachidk;
}
