package com.banhang.model;

import jakarta.validation.constraints.NotEmpty;
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

  @Size(min = 8, message = "Yêu cầu nhập tài khoản") @NotEmpty(message = "Không được để trống") private String email;

  @Size(min = 2, message = "Mật khẩu ít nhất 6 ký tự") @NotEmpty(message = "Không được để trống") private String matKhau;
}
