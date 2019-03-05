package com.demo.controller;

import com.demo.domain.Customer;
import com.demo.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static com.demo.controller.CustomerController.LIST;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class CustomerControllerTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ApplicationContext context;
    @MockBean
    private CustomerService service;

    private static final String PATH = "/"+ LIST;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getInvitationListWhenHasNoCustomers() throws Exception {
        doReturn(new ArrayList<>())
                .when(this.service)
                .getInvitationList(anyInt(), anyDouble(), anyDouble());
        String emptyListMessage = this.context.getMessage("emptyList", new Object[]{}, Locale.getDefault());
        this.mockMvc.perform(get(PATH))
                    .andExpect(content().string(containsString(emptyListMessage)));
    }

    @Test
    public void getInvitationListWhenHasCustomers() throws Exception {
        doReturn(Collections.singletonList(Customer.builder().userId(1L).name("name").build()))
                .when(this.service)
                .getInvitationList(anyInt(), anyDouble(), anyDouble());
        this.mockMvc.perform(get(PATH))
                .andExpect(content().string(containsString("name")));
    }
}