package com.mx.PruebaChakray.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mx.PruebaChakray.Dao.UsersDao;
import com.mx.PruebaChakray.Entities.Address;
import com.mx.PruebaChakray.Entities.LoginDTO;
import com.mx.PruebaChakray.Entities.Users;
import com.mx.PruebaChakray.core.AESEcnryption;

@ExtendWith(MockitoExtension.class)
public class ImpUsersTest {
	@Mock
	private UsersDao dao;
	@InjectMocks
	ImpUsers imp;
	
	private Users user;
	final String decryptedPassword = "GenericPassword1";
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		//Usuario
		String encryptedPassword = AESEcnryption.encrypt(decryptedPassword, "Password2");
		user = new Users();
		user.setId("4f142689-9d71-4285-a749-21f4ef22b8a6");
		user.setEmail("alberto@araiza.mx");
		user.setName("Alberto Araiza");
		user.setPhone("+52 4491509312");
		user.setPassword(encryptedPassword);
		user.setTaxId("ABCD123456XYZ");
		//Direcciones
		//Direccion 1
		Address add1 = new Address();
		add1.setId(1);
		add1.setName("Casa");
		add1.setStreet("calle #numero, colonia, ciudad");
		add1.setCountryCode("MX");
		add1.setUser(user);
		//Direccion 2
		Address add2 = new Address();
		add2.setId(2);
		add2.setName("Trabajo");
		add2.setStreet("Aguascalientes 101, Colinas, Ags");
		add2.setCountryCode("CO");
		
		user.setAddresses(List.of(add1, add2));
		user.setCreatedAt(ZonedDateTime.now(ZoneId.of("Indian/Antananarivo")));
	}
	
	@Test
	void createSuccess() throws Exception{
		when(dao.findByTaxId(any())).thenReturn(Optional.empty());
		when(dao.save(any())).thenReturn(user);
		var result = imp.create(user);
		assertNotNull(result);
		verify(dao).save(any());
	}
	
	@Test
	void createShouldThrowWhenTaxIdExists() {
		when(dao.findByTaxId(any())).thenReturn(Optional.of(user));
		Exception ex = assertThrows(Exception.class, ()->imp.create(user));
		assertEquals("Tax id registrado con anterioridad", ex.getLocalizedMessage());
	}
	
	@Test
	void createShouldThrowWhenInvalidPhone() {
		user.setPhone("1234");
		Exception ex = assertThrows(Exception.class, ()->imp.create(user));
		assertEquals("Numero de telefono invalido", ex.getLocalizedMessage());
	}
	
	@Test
	void updateSuccess() throws Exception{
		when(dao.findById(any())).thenReturn(Optional.of(user));
		Users update = new Users();
		update.setName("Nuevo Nombre");
		update.setPassword("newPass");
		update.setPhone("4491234567");
		update.setEmail("hola@mundo.mx");
		imp.update("4f142689-9d71-4285-a749-21f4ef22b8a6", update);
		verify(dao).save(any());
	}
	
	@Test
	void updateShouldThrowWhenInvalidPhone() {
		when(dao.findById(any())).thenReturn(Optional.of(user));
		user.setPhone("invalid");
		Exception ex = assertThrows(Exception.class, ()-> imp.update("4f142689-9d71-4285-a749-21f4ef22b8a6", user));
		assertEquals("Numero de telefono invalido", ex.getLocalizedMessage());
	}
	
	@Test
	void delete_success() throws Exception{
		when(dao.findById(any())).thenReturn(Optional.of(user));
		imp.delete("4f142689-9d71-4285-a749-21f4ef22b8a6");
		verify(dao).deleteById("4f142689-9d71-4285-a749-21f4ef22b8a6");
	}
	
	@Test
	void find_success() throws Exception {
		when(dao.findById(any())).thenReturn(Optional.of(user));
		var result = imp.find("4f142689-9d71-4285-a749-21f4ef22b8a6");
		assertNotNull(result);
	}
	
	@Test
	void login_success() throws Exception{
		when(dao.findByTaxId(any())).thenReturn(Optional.of(user));
		var login = new LoginDTO();
		login.setUsername("ABCD123456XYZ");
		login.setPassword("GenericPassword1");
		var result = imp.login(login);
		assertTrue(result);
	}
	
	@Test
	void loginShouldThrowWhenUserNotFound() {
		when(dao.findByTaxId(any())).thenReturn(Optional.empty());
		var login = new LoginDTO();
		login.setUsername("ABCD123456XYZ");
		login.setPassword("GenericPassword1");
		Exception ex = assertThrows(Exception.class, ()-> imp.login(login));
		assertEquals("Invalid tax id", ex.getLocalizedMessage());
	}
}
