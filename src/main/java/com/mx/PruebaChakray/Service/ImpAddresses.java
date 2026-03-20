package com.mx.PruebaChakray.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mx.PruebaChakray.Dao.AddressesDao;
import com.mx.PruebaChakray.Dao.UsersDao;
import com.mx.PruebaChakray.Entities.Address;
import com.mx.PruebaChakray.Entities.AddressDTO;
import com.mx.PruebaChakray.Entities.Users;

@Service
public class ImpAddresses implements IMethods {
	@Autowired
	AddressesDao addressDao;
	
	@Autowired
	UsersDao usersDao;

	@Override
	public Object create(Object obj) throws Exception {
		AddressDTO address = (AddressDTO) obj;
		Users user = usersDao.findById(address.getUserId()).orElse(null);
		if (user == null) throw new Exception("Usuario con id: " + address.getUserId() + " inexistente");
		var newAddress = address.toEntity(user);
		addressDao.save(newAddress);
		//Address added = addressDao.save(address);
		return newAddress.toDTO();
	}

	@Override
	public void update(String id, Object obj) throws Exception {
		Address address = (Address) obj;
		Address found = addressDao.findById(Integer.valueOf(id)).orElseThrow();
		found.setCountryCode(address.getCountryCode());
		found.setName(address.getName());
		found.setStreet(address.getStreet());
		addressDao.save(found);
	}

	@Override
	public void delete(String id) throws Exception {
		addressDao.findById(Integer.valueOf(id)).orElseThrow();
		addressDao.deleteById(Integer.valueOf(id));
	}

	@Override
	public Object find(String id) throws Exception {
		return addressDao.findById(Integer.valueOf(id)).orElseThrow();
	}

	@Override
	public List<Object> list() {
		//List<AddressDTO> list = addressDao.findAll().stream().map(addr -> addr.toDTO()).collect(Collectors.toList());
		return addressDao.findAll().stream().map(addr -> addr.toDTO()).collect(Collectors.toList());
	}

}
