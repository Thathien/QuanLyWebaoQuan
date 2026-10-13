package com.banhang.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class đại diện cho thông tin đăng nhập của người dùng.
 *
 * @author System
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DangNhapModel {

  @Size(max = 100, message = "Tài khoản không được vượt quá 100 ký tự") @NotBlank(message = "Không được để trống") private String email;

  @Size(max = 100, message = "Mật khẩu không được vượt quá 100 ký tự") @NotBlank(message = "Không được để trống") private String matKhau;
}
