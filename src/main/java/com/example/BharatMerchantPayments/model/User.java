package com.example.BharatMerchantPayments.model;

import com.example.BharatMerchantPayments.enums.UserCreationStatus;
import com.example.BharatMerchantPayments.enums.UserLoginStatus;
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

    @OneToMany(mappedBy = "Users")
    private List<Payment> payments;
}
