package com.rosanegra.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;


@Data
@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String email;
    @Column(name = "pwd")
    private String password;

    @OneToMany(fetch = FetchType.EAGER) //solo por que es small proyect
    @JoinColumn(name = "id_customer")
    private List<RoleEntity> roles;
}
