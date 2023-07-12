package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.vo.CommonResponse;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.ChangePasswordService;
import com.example.demo.service.LoginService;
import com.example.demo.service.RegisterService;
import com.example.demo.util.Translator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.Map;

@Api(tags = "ALL API")
@RestController
@RequestMapping("/api")
public class AccountController {


    private final RegisterService registerService;

    private final LoginService loginService;

    private final ChangePasswordService changePasswordService;

    public AccountController(RegisterService registerService,
                             LoginService loginService,
                             ChangePasswordService changePasswordService) {
        this.registerService = registerService;
        this.loginService = loginService;
        this.changePasswordService = changePasswordService;
    }

    @ApiOperation("註冊")
    @PostMapping("/register")
    public ResponseEntity<CommonResponse<?>> register(@Valid @RequestBody AccountDto accountDTO) {
        try {
            StatusCode statusCode = registerService.register(accountDTO);
            return generateResponse(statusCode);
        } catch (Exception e) {
            return generateResponse(StatusCode.UnknownError);
        }

    }

    @ApiOperation("登入")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<?>> login(@Valid @RequestBody AccountDto accountDTO) {
        try {
            Map<String, Object> map = loginService.login(accountDTO);
            StatusCode statusCode = (StatusCode) map.get("statusCode");
            return map.containsKey("token") ?
                    generateResponse(statusCode, map.get("token").toString()) :generateResponse(statusCode);
        } catch (Exception e) {
            return generateResponse(StatusCode.UnknownError);
        }
    }

    @ApiOperation("變更密碼")
    @PostMapping("/changePassword")
    public ResponseEntity<CommonResponse<?>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            String oldPassword = changePasswordDto.getOldPassword();
            String newPassword = changePasswordDto.getNewPassword();

            if (oldPassword.equals(newPassword)) {
                return generateResponse(StatusCode.SamePassword);
            }

            StatusCode statusCode = changePasswordService.changePassword(id, oldPassword, newPassword);

            return generateResponse(statusCode);
        } catch (Exception e) {
            return generateResponse(StatusCode.UnknownError);
        }
    }

    private ResponseEntity<CommonResponse<?>> generateResponse(StatusCode statusCode, String... args) {
        String[] array = new String[args.length + 1];
        String msg = Translator.toLocale(statusCode.getDescription());
        array[0] = msg;
        System.arraycopy(args, 0, array, 1, args.length);

        CommonResponse<?> res = (statusCode.getStatus() < 0)
                ? new CommonResponse<>(statusCode.getStatus(), msg, null)
                : new CommonResponse<>(statusCode.getStatus(), null, array);

        switch(statusCode) {
            case Created:
                return ResponseEntity.status(HttpStatus.CREATED).body(res);
            case ExistingAccount:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
            case LoginSuccess:
            case ChangePasswordSuccess:
                return ResponseEntity.status(HttpStatus.OK).body(res);
            case InvalidData:
            case SamePassword:
            case InvalidOldPassword:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            default:
                return ResponseEntity.internalServerError().body(res);
        }
    }


}
