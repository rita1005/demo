package com.example.demo;

import com.example.demo.dao.entity.Account;
import com.example.demo.dao.mapper.AccountMapper;
import com.example.demo.dao.repository.AccountRepository;
import com.example.demo.dao.repository.CheckInRepository;
import com.example.demo.dto.AccountDto;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.ChangePasswordService;
import com.example.demo.service.CheckInService;
import com.example.demo.service.LoginService;
import com.example.demo.service.RegisterService;
import com.example.demo.service.impl.LoginServiceImpl;
import com.example.demo.service.impl.RegisterServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {
    @Autowired
    private RegisterService registerService;

    private LoginServiceImpl loginService;
    @Autowired
    private ChangePasswordService changePasswordService;
    @Autowired
    private CheckInService checkInService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private CheckInRepository checkInRepository;
    @MockBean
    private AccountMapper accountMapper;

    private AccountDto accountDto;
    private Account account;


    @Before
    public void init() {
        accountDto = new AccountDto("Amy", "123");
        account = new Account(Long.parseLong("1"),"Amy", BCrypt.hashpw("123", BCrypt.gensalt()));
        accountMapper = mock(AccountMapper.class);
        loginService = new LoginServiceImpl(accountMapper);
    }

    @Test
    public void registerSuccessTest() {
        when(accountRepository.findByUsername(accountDto.getUsername())).thenReturn(Optional.empty());

        StatusCode statusCode = registerService.register(accountDto);

        assertEquals(StatusCode.Created, statusCode);
    }

    @Test
    public void registerFailTest() {
        when(accountRepository.findByUsername(accountDto.getUsername())).thenReturn(Optional.of(new Account()));

        StatusCode statusCode1 = registerService.register(accountDto);

        assertEquals(StatusCode.ExistingAccount, statusCode1);
    }

    @Test
    public void loginSuccessTest() {
        when(accountMapper.queryByUsername(accountDto.getUsername())).thenReturn(Optional.of(account));

        Map<String, Object> map = loginService.login(accountDto);

        assertEquals(StatusCode.LoginSuccess, map.get("statusCode"));
        assertTrue(map.containsKey("token"));

    }

    @Test
    public void loginFailTest() {
        when(accountMapper.queryByUsername(accountDto.getUsername())).thenReturn(Optional.empty());

        Map<String, Object> map = loginService.login(accountDto);

        assertEquals(StatusCode.InvalidData, map.get("statusCode"));
    }

    @Test
    public void changePasswordSuccessTest() {
        String id = "1";
        String oldPassword = "123";
        String newPassword = "456";

        when(accountRepository.getById(Long.parseLong(id))).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);

        StatusCode statusCode = changePasswordService.changePassword(id, oldPassword, newPassword);

        assertEquals(StatusCode.ChangePasswordSuccess, statusCode);
        verify(accountRepository, times(1)).getById(Long.parseLong(id));
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void changePasswordFailTest() {
        String id = "1";
        String oldPassword = "789";
        String newPassword = "456";

        when(accountRepository.getById(Long.parseLong(id))).thenReturn(account);

        StatusCode statusCode = changePasswordService.changePassword(id, oldPassword, newPassword);

        assertEquals(StatusCode.InvalidOldPassword, statusCode);
        verify(accountRepository, times(1)).getById(Long.parseLong(id));
        verify(accountRepository, never()).save(account);
    }
}
