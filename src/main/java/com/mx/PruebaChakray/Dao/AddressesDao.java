package com.mx.PruebaChakray.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mx.PruebaChakray.Entities.Address;

@Repository
public interface AddressesDao extends JpaRepository<Address, Integer>{

}
