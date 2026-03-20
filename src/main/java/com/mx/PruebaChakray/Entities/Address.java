package com.mx.PruebaChakray.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ADDRESSES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String street;

	@Column(name = "COUNTRY_CODE")
	private String countryCode;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private Users user;

	public AddressDTO toDTO() {
		return new AddressDTO(user.getId(), name, street, countryCode);
	}
}
