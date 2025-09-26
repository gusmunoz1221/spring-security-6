package com.rosanegra.repositories;

import com.rosanegra.entities.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public interface CustomerRespository extends CrudRepository<CustomerEntity, BigInteger> {
    Optional<CustomerEntity> findByEmail(String email);
}
