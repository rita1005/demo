package com.example.demo;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.demo.controller.AccountController;
import com.example.demo.controller.CheckInController;
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

import org.slf4j.LoggerFactory;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    @Autowired
    private CheckInController checkInController;
    @MockBean
    private RegisterService registerService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private ChangePasswordService changePasswordService;

    private AccountDto accountDto;

    private ListAppender<ILoggingEvent> listAppender;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.accountController).build();
        accountDto = new AccountDto("Eland", "123");

        Logger logger = (Logger) LoggerFactory.getLogger(AccountController.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
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
                        is(Translator.toLocale(StatusCode.Created.getDescription()))))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        List<ILoggingEvent> logEvent = listAppender.list;
    }

    @Test
    public void registerFailTest1() throws Exception {
        when(registerService.register(any(AccountDto.class))).thenReturn(StatusCode.ExistingAccount);

        mockMvc.perform(post("/api/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(accountDto)))
                .andExpect(jsonPath("$.status", is(StatusCode.ExistingAccount.getStatus())))
                .andExpect(jsonPath("$.errorMessage",
                        is(Translator.toLocale(StatusCode.ExistingAccount.getDescription()))))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void registerFailTest2() throws Exception {
        when(registerService.register(any(AccountDto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountDto)))
                .andExpect(jsonPath("$.status", is(StatusCode.UnknownError.getStatus())))
                .andExpect(jsonPath("$.errorMessage",
                        is(Translator.toLocale(StatusCode.UnknownError.getDescription()))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void loginSuccessTest() throws Exception {
        when(loginService.login(any(AccountDto.class)))
                .thenReturn(new HashMap<String, Object>(){{put("statusCode", StatusCode.LoginSuccess);}});

        mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(accountDto)))
                .andExpect(jsonPath("$.status", is(StatusCode.LoginSuccess.getStatus())))
                .andExpect(jsonPath("$.data[0]", is(Translator.toLocale(StatusCode.LoginSuccess.getDescription()))))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void loginFailTest() throws Exception {
        when(loginService.login(any(AccountDto.class)))
                .thenReturn(new HashMap<String, Object>(){{put("statusCode", StatusCode.InvalidData);}});

        mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(accountDto)))
                .andExpect(jsonPath("$.status", is(StatusCode.InvalidData.getStatus())))
                .andExpect(jsonPath("$.errorMessage", is(Translator.toLocale(StatusCode.InvalidData.getDescription()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void changePasswordSuccessTest() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null));

        when(changePasswordService.changePassword(anyString(), anyString(), anyString()))
                .thenReturn(StatusCode.ChangePasswordSuccess);

        mockMvc.perform(post("/api/changePassword")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new ChangePasswordDto("123", "456"))))
                .andExpect(jsonPath("$.status", is(StatusCode.ChangePasswordSuccess.getStatus())))
                .andExpect(jsonPath("$.data[0]", is(Translator.toLocale(StatusCode.ChangePasswordSuccess.getDescription()))))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void changePasswordFailTest1() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null));

        when(changePasswordService.changePassword(anyString(), anyString(), anyString()))
                .thenReturn(StatusCode.InvalidOldPassword);

        mockMvc.perform(post("/api/changePassword")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new ChangePasswordDto("123", "456"))))
                .andExpect(jsonPath("$.status", is(StatusCode.InvalidOldPassword.getStatus())))
                .andExpect(jsonPath("$.errorMessage", is(Translator.toLocale(StatusCode.InvalidOldPassword.getDescription()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void changePasswordFailTest2() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null));

        mockMvc.perform(post("/api/changePassword")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new ChangePasswordDto("123", "123"))))
                .andExpect(jsonPath("$.status", is(StatusCode.SamePassword.getStatus())))
                .andExpect(jsonPath("$.errorMessage", is(Translator.toLocale(StatusCode.SamePassword.getDescription()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void checkInTest() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.checkInController).build();

        mockMvc.perform(post("/api/check-in"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
