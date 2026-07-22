package com.example.BharatMerchantPayments.model;

import com.example.BharatMerchantPayments.enums.UserCreationStatus;
import com.example.BharatMerchantPayments.enums.UserLoginStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    private UUID userId;
    private String username;
    private String email;
    private String phoneNo;
    private String password;
    private UserCreationStatus status;

    @Enumerated(EnumType.STRING)
    private UserLoginStatus logonStatus;

    @Column(length = 1000)
    private String activeSession;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Payment> payments;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserCreationStatus getStatus() {
        return status;
    }

    public void setStatus(UserCreationStatus status) {
        this.status = status;
    }

    public UserLoginStatus getLogonStatus() {
        return logonStatus;
    }

    public void setLogonStatus(UserLoginStatus logonStatus) {
        this.logonStatus = logonStatus;
    }

    public String getActiveSession() {
        return activeSession;
    }

    public void setActiveSession(String activeSession) {
        this.activeSession = activeSession;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
