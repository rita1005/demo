package com.example.demo.enums;

public enum StatusCode {

    Created(1, "account.created.successfully"),
    LoginSuccess(2,"login.success"),
    ChangePasswordSuccess(3, "password.changed.successfully"),
    InvalidData(-1, "invalid.username.or.password"),
    ExistingAccount(-2, "username.already.exists"),
    SamePassword(-3, "same.password"),
    UnknownError(-4, "unknown.error"),
    InvalidOldPassword(-5,"wrong.old.password");


    private Integer status;
    private String description;

    StatusCode(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

}