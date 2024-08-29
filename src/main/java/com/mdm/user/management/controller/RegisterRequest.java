package com.mdm.user.management.controller;

import com.mdm.user.management.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String name;

    private BigDecimal phno;

    private String email;

    private String address;

    private String password;

    private Role role;

}
