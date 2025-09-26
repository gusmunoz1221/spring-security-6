package com.rosanegra.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigInteger;


@Data
@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    private BigInteger id;
    private String email;
    @Column(name = "pwd")
    private String pwd;
    @Column(name = "rol")
    private String role;
}
