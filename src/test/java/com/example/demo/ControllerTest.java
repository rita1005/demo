package com.example.demo;

import com.example.demo.controller.AccountController;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.ChangePasswordService;
import com.example.demo.service.LoginService;
import com.example.demo.service.RegisterService;
import com.example.demo.util.Translator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "com.example.demo")
public class ControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountController accountController;
    @MockBean
    private RegisterService registerService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private ChangePasswordService changePasswordService;

    private AccountDto accountDto;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.accountController).build();
        accountDto = new AccountDto("Eland", "123");
    }

    @Test
    public void registerSuccessTest() throws Exception {
        when(registerService.register(any(AccountDto.class))).thenReturn(StatusCode.Created);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(StatusCode.Created.getStatus())))
                .andExpect(jsonPath("$.data[0]",
                        is(Translator.toLocale(StatusCode.Created.getDescription()))));
    }

    @Test
    public void registerFailTest() throws Exception {
        when(registerService.register(any(AccountDto.class))).thenReturn(StatusCode.ExistingAccount);

        mockMvc.perform(post("/api/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(accountDto)))
                .andExpect(jsonPath("$.status", is(StatusCode.ExistingAccount.getStatus())))
                .andExpect(jsonPath("$.errorMessage",
                        is(Translator.toLocale(StatusCode.ExistingAccount.getDescription()))));
    }

    @Test
    public void loginSuccessTest() throws Exception {
        when(loginService.login(any(AccountDto.class)))
                .thenReturn(new HashMap<String, Object>(){{put("statusCode", StatusCode.LoginSuccess);}});

        mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(accountDto)))
                .andExpect(jsonPath("$.status", is(StatusCode.LoginSuccess.getStatus())))
                .andExpect(jsonPath("$.data[0]", is(Translator.toLocale(StatusCode.LoginSuccess.getDescription()))));
    }

    @Test
    public void loginFailTest() throws Exception {
        when(loginService.login(any(AccountDto.class)))
                .thenReturn(new HashMap<String, Object>(){{put("statusCode", StatusCode.InvalidData);}});

        mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(accountDto)))
                .andExpect(jsonPath("$.status", is(StatusCode.InvalidData.getStatus())))
                .andExpect(jsonPath("$.errorMessage", is(Translator.toLocale(StatusCode.InvalidData.getDescription()))));

    }

    @Test
    public void changePasswordSuccessTest() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null));

        when(changePasswordService.changePassword(any(String.class), any(String.class), any(String.class)))
                .thenReturn(StatusCode.ChangePasswordSuccess);

        mockMvc.perform(post("/api/changePassword")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new ChangePasswordDto("123", "456"))))
                .andExpect(jsonPath("$.status", is(StatusCode.ChangePasswordSuccess.getStatus())))
                .andExpect(jsonPath("$.data[0]", is(Translator.toLocale(StatusCode.ChangePasswordSuccess.getDescription()))));
    }

    @Test
    public void changePasswordFailTest1() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null));

        when(changePasswordService.changePassword(any(String.class), any(String.class), any(String.class)))
                .thenReturn(StatusCode.InvalidOldPassword);

        mockMvc.perform(post("/api/changePassword")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new ChangePasswordDto("123", "456"))))
                .andExpect(jsonPath("$.status", is(StatusCode.InvalidOldPassword.getStatus())))
                .andExpect(jsonPath("$.errorMessage", is(Translator.toLocale(StatusCode.InvalidOldPassword.getDescription()))));
    }

    @Test
    public void changePasswordFailTest2() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null));

        mockMvc.perform(post("/api/changePassword")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new ChangePasswordDto("123", "123"))))
                .andExpect(jsonPath("$.status", is(StatusCode.SamePassword.getStatus())))
                .andExpect(jsonPath("$.errorMessage", is(Translator.toLocale(StatusCode.SamePassword.getDescription()))));
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() {
        assertNotNull(registerService);
        when(registerService.register(accountDto)).thenReturn(StatusCode.Created);
        StatusCode statusCode = registerService.register(accountDto);
        assertEquals(StatusCode.Created, statusCode);
    }

}
