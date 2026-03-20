package com.mx.PruebaChakray.Entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDTO {
	private String id;
	private String email;
	private String name;
	private String phone;
	private String taxId;
	private String createdAt;
	private List<Address> addresses;
}
