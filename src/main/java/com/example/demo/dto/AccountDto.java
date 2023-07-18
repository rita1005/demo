package com.example.demo.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AccountDto {

    @NotNull(message = "帳號不得為空")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}", message = "帳號須為長度3到10,大小寫英文或數字的組合")
    private String username;

    @NotNull(message = "密碼不得為空")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}", message = "密碼須為長度3到10,大小寫英文或數字的組合")
    private String password;

    public AccountDto() {
    }

    public AccountDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
