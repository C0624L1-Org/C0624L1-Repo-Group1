package com.example.shoplaptop.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDTO {
    @NotBlank(message = "Vui lòng nhập mật khẩu cũ")
    private String oldPassword;

    @NotBlank(message = "Vui lòng nhập mật khẩu mới")
    @Size(min = 3, max = 20, message = "Mật khẩu mới phải từ 3 đến 20 ký tự")
    private String newPassword;

    @NotBlank(message = "Vui lòng xác nhận mật khẩu mới")
    private String confirmNewPassword;

    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }
    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
