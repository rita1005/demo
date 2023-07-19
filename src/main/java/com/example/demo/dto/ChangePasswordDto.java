package com.example.demo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ChangePasswordDto {

    @NotNull(message = "舊密碼不得為空")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}", message = "舊密碼須為長度3到10,大小寫英文或數字的組合")
    public String oldPassword;

    @NotNull(message = "新密碼不得為空")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}", message = "新密碼須為長度3到10,大小寫英文或數字的組合")
    public String newPassword;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

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
}
