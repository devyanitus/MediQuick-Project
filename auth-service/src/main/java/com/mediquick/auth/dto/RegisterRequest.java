package com.mediquick.auth.dto;

public class RegisterRequest {

    private String email;
    private String password;
    private String name;

    public RegisterRequest() {}

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() {return name;}

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) {this.name = name;}



    
}