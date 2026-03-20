package com.mx.PruebaChakray.Entities;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {
	@Id
	private String id;
	private String email;
	private String name;
	private String phone;
	private String password;

	@Column(name = "TAX_ID")
	private String taxId;

	@Column(name = "CREATED_AT")
	private ZonedDateTime createdAt;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addresses;
	
	public Users(String id) {
		this.id = id;
	}

	public void addAddress(Address address) {
		addresses.add(address);
		address.setUser(this);
	}

	public void removeAddress(Address address) {
		addresses.remove(address);
		address.setUser(null);
	}

	public UserResponseDTO toDTO() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm VV");
		UserResponseDTO tmp = new UserResponseDTO();
		tmp.setId(this.id);
		tmp.setName(this.name);
		tmp.setEmail(this.email);
		tmp.setPhone(this.phone);
		tmp.setTaxId(this.taxId);
		tmp.setCreatedAt(this.createdAt.format(format));
		tmp.setAddresses(this.addresses);
		return tmp;
	}
}
