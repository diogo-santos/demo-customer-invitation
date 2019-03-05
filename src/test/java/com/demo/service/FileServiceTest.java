package com.demo.service;

import com.demo.domain.Customer;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class FileServiceTest {
    @Autowired
    private FileService fileService;

    @Test
    public void getListFromFileWhenInvalidTypeReference() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> this.fileService.getListFromFile("url", null))
                .withMessageContaining("Type reference not provided");
    }

    @Test
    public void getListFromFileWhenInvalidUrl() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> this.fileService.getListFromFile("invalid", new TypeReference<Customer>() {}))
                .withMessageContaining("Error accessing");
    }

    @Test
    public void getListFromFile() throws IOException {
        String customerJson="{\"latitude\": \"1\", \"user_id\": 1, \"name\": \"test\", \"longitude\": \"-1\"}";

        FileService fileServiceSpy = Mockito.spy(this.fileService);
        doReturn(new ByteArrayInputStream(customerJson.getBytes()))
                .when(fileServiceSpy)
                .getFile(anyString());

        List<?> list = fileServiceSpy.getListFromFile("url", new TypeReference<Customer>(){});

        Customer customer = Customer.builder()
                                    .latitude(1d)
                                    .longitude(-1d)
                                    .userId(1L)
                                    .name("test")
                                    .build();

        assertThat(list.stream().anyMatch(c->c.equals(customer)));
    }

    @Test
    public void getListFromFileWhenInvalidLines() throws IOException {
        String customerJson="invalid line";
        FileService fileServiceSpy = Mockito.spy(this.fileService);
        doReturn(new ByteArrayInputStream(customerJson.getBytes()))
                .when(fileServiceSpy)
                .getFile(anyString());
        List<?> list = fileServiceSpy.getListFromFile("url", new TypeReference<Customer>(){});
        assertThat(list.isEmpty());
    }
}