package com.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @JsonProperty("user_id")
    private Long userId;
    private String name;
    private Double latitude;
    private Double longitude;
}