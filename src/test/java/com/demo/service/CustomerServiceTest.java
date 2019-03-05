package com.demo.service;

import com.demo.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class CustomerServiceTest {
    @Autowired
    private CustomerService service;
    @MockBean
    private FileService fileService;

    private static final double BASE_LATITUDE  = 53.339428;
    private static final double BASE_LONGITUDE = -6.257664;

    @Test
    public void calculateDistanceTest() {
        double lat2 = 52.986375;
        double lon2 = -6.043701;
        CustomerServiceImpl service = new CustomerServiceImpl(null);
        double distance = service.calculateDistance(BASE_LATITUDE, BASE_LONGITUDE, lat2, lon2);
        assertThat(distance == 41.76872550083619);
    }

    @Test
    public void getInvitationListTest() {
        Customer customer = Customer.builder()
                                    .latitude(52.986375)
                                    .longitude(-6.043701)
                                    .userId(1L)
                                    .name("test")
                                    .build();
        List<?> list = singletonList(customer);

        doReturn(list)
                .when(this.fileService)
                .getListFromFile(anyString(), any());

        List<Customer> customers = this.service.getInvitationList(42, BASE_LATITUDE, BASE_LONGITUDE);

        assertThat(customers)
                .extracting(Customer::getUserId)
                .contains(customer.getUserId());
    }
}