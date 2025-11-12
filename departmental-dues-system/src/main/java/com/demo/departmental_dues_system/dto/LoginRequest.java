package com.demo.departmental_dues_system.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String emailOrMatric;
    private String password;

    public String getEmailOrMatric() {
        return emailOrMatric;
    }

    public void setEmailOrMatric(String emailOrMatric) {
        this.emailOrMatric = emailOrMatric;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
