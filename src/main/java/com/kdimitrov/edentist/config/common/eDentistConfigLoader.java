package com.kdimitrov.edentist.config.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(eDentistConfigLoader.CONFIG_ROOT)
public class eDentistConfigLoader implements eDentistConfig {

    public static final String CONFIG_ROOT = "edentist";

    private String username;
    private String password;
    private String emailRecep;
    private String emailSender;
    private String passwordStrength;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmailRecep() {
        return emailRecep;
    }

    public void setEmailRecep(String emailRecep) {
        this.emailRecep = emailRecep;
    }

    @Override
    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public int getPasswordStrength() {
        return Integer.parseInt(passwordStrength);
    }

    public void setPasswordStrength(String passwordStrength) {
        this.passwordStrength = passwordStrength;
    }
}

