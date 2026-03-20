package com.mx.PruebaChakray.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressDTO {
	private String userId;
	private String name;
	private String street;
	private String countryCode;
	public Address toEntity(Users user) {
		return new Address(0, name, street, countryCode, user);
	}
}
