package com.mx.PruebaChakray.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mx.PruebaChakray.Entities.Users;

@Repository
public interface UsersDao extends JpaRepository<Users, String> {
	public Optional<Users> findByTaxId(String taxId);
}
