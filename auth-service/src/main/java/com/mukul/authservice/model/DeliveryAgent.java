package com.mukul.authservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryAgent {

    private String name;
    private Address address;
    private Long phoneNumber;
}