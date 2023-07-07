package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.ChangePasswordService;
import com.example.demo.service.LoginService;
import com.example.demo.service.RegisterService;
import com.example.demo.dto.vo.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Api(tags = "ALL API")
@RestController
@RequestMapping("/api")
public class AccountController {

    private final MessageSource messageSource;

    private final RegisterService registerService;

    private final LoginService loginService;

    private final ChangePasswordService changePasswordService;

    public AccountController(MessageSource messageSource, RegisterService registerService, LoginService loginService, ChangePasswordService changePasswordService) {
        this.messageSource = messageSource;
        this.registerService = registerService;
        this.loginService = loginService;
        this.changePasswordService = changePasswordService;
    }

    @ApiOperation("註冊")
    @PostMapping("/register")
    public ResponseEntity<CommonResponse<String>> register(@RequestHeader("Accept-Language") String language, @Valid @RequestBody AccountDto accountDTO) {
        StatusCode statusCode = registerService.register(accountDTO);
        Locale locale = selectedLanguage(language);
        String msg = messageSource.getMessage(statusCode.getDescription(), null, Locale.US);
        switch (statusCode) {
            case Created:
                return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(statusCode.getStatus(), null, msg));
            case ExistingAccount:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new CommonResponse<>(statusCode.getStatus(), msg, null));
            default:
                return ResponseEntity.internalServerError().body(new CommonResponse<>(statusCode.getStatus(), msg, null));
        }

    }

    @ApiOperation("登入")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<?>> login(@RequestHeader("Accept-Language") String language, @Valid @RequestBody AccountDto accountDTO) {

        List<?> list = loginService.login(accountDTO);
        StatusCode statusCode = (StatusCode)list.get(0);
        String msg = messageSource.getMessage(statusCode.getDescription(), null, selectedLanguage(language));
        switch(statusCode) {
            case LoginSuccess:
                return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<String[]>(statusCode.getStatus(), null, new String[]{msg, list.get(1).toString()}));
            case InvalidData:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse<>(statusCode.getStatus(), msg, null));
            default:
                return ResponseEntity.internalServerError().body(new CommonResponse<>(statusCode.getStatus(), msg, null));
        }
    }

    @ApiOperation("變更密碼")
    @PostMapping("/changePassword")
    public ResponseEntity<CommonResponse<String>> changePassword(@RequestHeader("Accept-Language") String language, @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();
        StatusCode statusCode = null;

        if (oldPassword.equals(newPassword)) {
            statusCode = StatusCode.SamePassword;
        }

        if (statusCode == null) {
            statusCode = changePasswordService.changePassword(id, oldPassword, newPassword);
        }
        String msg = messageSource.getMessage(statusCode.getDescription(), null, selectedLanguage(language));
        switch(statusCode) {
            case ChangePasswordSuccess:
                return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(statusCode.getStatus(), null, msg));
            case InvalidOldPassword:
            case SamePassword:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse<>(statusCode.getStatus(), msg, null));
            default:
                return ResponseEntity.internalServerError().body(new CommonResponse<>(statusCode.getStatus(), msg, null));
        }
    }

    private Locale selectedLanguage(String language) {
        try {
            List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(language);
            List<Locale> availableLocales = List.of(Locale.getAvailableLocales());
            return Locale.lookup(languageRanges, availableLocales);
        } catch (Exception e) {
            return Locale.TAIWAN;
        }
    }

}
