package com.demo.service;

import com.demo.domain.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getInvitationList(int distance, double latitude, double longitude);
}