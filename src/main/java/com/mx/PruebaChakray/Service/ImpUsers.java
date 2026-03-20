package com.mx.PruebaChakray.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mx.PruebaChakray.Dao.UsersDao;
import com.mx.PruebaChakray.Entities.LoginDTO;
import com.mx.PruebaChakray.Entities.UserResponseDTO;
import com.mx.PruebaChakray.Entities.Users;
import com.mx.PruebaChakray.core.AESEcnryption;

@Service
public class ImpUsers implements IMethods {
	@Autowired
	UsersDao dao;
	ZoneId madagascarZone = ZoneId.of("Indian/Antananarivo");
	DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm VV");
	final String rfcPattern = "^[A-ZÑ&]{4}\\d{6}[A-Z0-9]{3}$";
	final String phonePAttern = "^(\\+52)?\\d{10}$";

	@Override
	public Object create(Object obj) throws Exception {
		Users user = (Users) obj;
		Users found = dao.findByTaxId(user.getTaxId()).orElse(null);
		user.setPhone(user.getPhone().replaceAll("\\s+", ""));
		if (found != null) {
			throw new Exception("Tax id registrado con anterioridad");
		}else if (!user.getTaxId().matches(rfcPattern)) {
			throw new Exception("Tax id invalido");
		}else if(!user.getPhone().matches(phonePAttern)) {
			throw new Exception("numero de telefono invalido");
		}else {
			user.setId(UUID.randomUUID().toString());
			String encrypt = AESEcnryption.encrypt(user.getPassword(), "Password2");
			user.setPassword(encrypt);
			ZonedDateTime now = ZonedDateTime.now(madagascarZone);
			user.setCreatedAt(now);
			Users newUser = dao.save(user);
			return newUser.toDTO();
		} 
	}

	@Override
	public void update(String id, Object obj) throws Exception {
		Users user = (Users) obj;
		Users old = dao.findById(id).orElseThrow();
		if (!user.getName().isEmpty()) {
			old.setName(user.getName());
		}
		if (!user.getPassword().isEmpty()) {
			String encrypt = AESEcnryption.encrypt(user.getPassword(), "Password2");
			old.setPassword(encrypt);
		}
		if (!user.getPhone().isEmpty()) {

		}

		dao.save(old);
	}

	@Override
	public void delete(String id) throws Exception {
		dao.findById(id).orElseThrow();
		dao.deleteById(id);
	}

	@Override
	public Object find(String id) throws Exception {
		Users tmp = dao.findById(id).orElseThrow();
		return tmp.toDTO();
	}

	@Override
	public List<Object> list() {
		List<UserResponseDTO> users = (List<UserResponseDTO>) dao.findAll().stream().map(x -> x.toDTO()).toList();
		return users.stream().collect(Collectors.toList());
	}

	public List<UserResponseDTO> listOrderBy(String orderBy, String filterBy, String filterType, String filterValue)
			throws Exception {
		List<Users> orderedList;
		if (orderBy == null || orderBy.isEmpty()) {
			orderedList = dao.findAll();
		} else {
			orderedList = dao.findAll(Sort.by(orderBy));
		}
		return orderedList.stream().filter(user -> filter(user, filterBy, filterType, filterValue)).map(x -> x.toDTO())
				.collect(Collectors.toList());
	}

	public boolean filter(Users user, String filterBy, String filterType, String filterValue) {
		String attribute = "";
		switch (filterBy) {
		case "email":
			attribute = user.getEmail();
			break;
		case "id":
			attribute = user.getId();
			break;
		case "name":
			attribute = user.getName();
			break;
		case "phone":
			attribute = user.getPhone();
			break;
		case "tax_id":
			attribute = user.getTaxId();
			break;
		case "created_at":
			attribute = format.format(user.getCreatedAt());
			break;
		}
		switch (filterType) {
		case "eq":
			return attribute.equalsIgnoreCase(filterValue);
		case "co":
			return attribute.contains(filterValue);
		case "sw":
			return attribute.startsWith(filterValue);
		case "ew":
			return attribute.endsWith(filterValue);
		}
		return false;
	}
	
	public boolean login(LoginDTO login) throws Exception {
		Users user = dao.findByTaxId(login.getUsername()).orElse(null);
		if (user == null) throw new Exception("Invalid tax id");
		String unencryptedPassword = AESEcnryption.decrypt(user.getPassword(), "Password2");
		return login.getPassword().equals(unencryptedPassword);
	}

}
