package com.demo.service;

import com.demo.domain.Customer;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toList;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Value("${url.customers}")
    private String urlCustomers;
    private final FileService fileService;

    public CustomerServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public List<Customer> getInvitationList(int distance, double latitude, double longitude) {
        logger.debug("In getInvitationList with '{}' '{}' '{}'", distance, latitude, longitude);
        List<Customer> customers = this.getCustomers();
        List<Customer> invitationList = customers
                                        .stream()
                                        .filter(c -> this.calculateDistance(latitude,longitude,c.getLatitude(),c.getLongitude())<=100)
                                        .sorted(comparingLong(Customer::getUserId))
                                        .collect(toList());
        logger.debug("Out getInvitationList with '{}'", invitationList);
        return invitationList;
    }

    private List<Customer> getCustomers() {
        logger.debug("In getCustomers");
        List<?> list = fileService.getListFromFile(this.urlCustomers, new TypeReference<Customer>(){});
        logger.debug("Out getCustomers");
        return list
                .stream()
                .map(Customer.class::cast)
                .collect(toList());
    }

    double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        logger.debug("In calculateDistance with '{}' '{}' '{}' '{}'", lat1, lon1, lat2, lon2);
        final int earthRadians = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2)
                * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadians * c;
        distance = Math.pow(distance, 2);
        distance = Math.sqrt(distance);
        logger.debug("Out calculateDistance with '{}'", distance);
        return distance;
    }
}