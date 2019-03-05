package com.demo.controller;

import com.demo.service.CustomerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CustomerController.class);
    public  static final String LIST = "list";

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping(LIST)
    public String getInvitationList(Model model,
                                    @RequestParam(required = false, defaultValue="100") int distance,
                                    @RequestParam(required = false, defaultValue="53.339428") double latitude,
                                    @RequestParam(required = false, defaultValue="-6.257664") double longitude) {
        logger.debug("In getInvitationList with '{}' '{}' '{}'", distance,latitude,longitude);
        model.addAttribute("customers", service.getInvitationList(distance, latitude, longitude));
        logger.debug("Out getInvitationList with '{}'", LIST);
        return LIST;
    }
}