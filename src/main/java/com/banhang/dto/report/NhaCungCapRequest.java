package com.banhang.dto.report;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NhaCungCapRequest(
    @NotBlank @Size(max = 150) String ten,
    @Email @Size(max = 255) String email,
    @Size(max = 50) String soDienThoai,
    @Size(max = 255) String diaChi) {}
