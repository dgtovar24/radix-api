package com.project.radix.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    
    // Additional fields for patients
    private String phone;
    private String address;
}
