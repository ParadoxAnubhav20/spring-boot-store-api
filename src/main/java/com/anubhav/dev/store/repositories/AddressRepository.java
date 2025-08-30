package com.anubhav.dev.store.repositories;

import com.anubhav.dev.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}