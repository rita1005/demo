package com.example.demo;

import com.example.demo.controller.AccountController;
import com.example.demo.dto.AccountDto;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    public void registerTest() throws Exception {
        when(registerService.register(accountDto)).thenReturn(StatusCode.Created);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.status", is(StatusCode.Created.getStatus())))
                .andExpect((ResultMatcher) jsonPath("$.message",
                        is(Translator.toLocale(StatusCode.Created.getDescription()))))
                .andExpect(jsonPath("$.data").doesNotExist());
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
