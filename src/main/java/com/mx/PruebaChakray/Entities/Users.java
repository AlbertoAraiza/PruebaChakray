package com.mx.PruebaChakray.Entities;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad de JPA Repository para realizar modificaciones en la base de datos y enviar y recibir peticiones de usuarios")
public class Users {
	@Schema(description = "Identificador unico de cada usuario", example = "1dded8fb-cae9-485c-aabc-09b27f7056ac")
	@Id
	private String id;
	@Schema(description = "Direccion de correo electronico del usuario", example = "alberto@hola.mundo")
	private String email;
	@Schema(description = "Nombre completo del usuario", example = "Alberto Araiza")
	private String name;
	@Schema(description = "Numero de telefono a 10 digitos con lada internacional opcional", example = "+524494449999")
	private String phone;
	@Schema(description = "Contraseña a encriptar relacionada con el tax id", example = "Password1")
	private String password;

	@Schema(description = "RFC correspondiente al usuario", example = "ABCD889977XYZ")
	@Column(name = "TAX_ID")
	private String taxId;

	@Schema(description = "Fecha y hora en que se creo el usuario", example = "20/03/2026 20:06:05")
	@Column(name = "CREATED_AT")
	private ZonedDateTime createdAt;

	@Schema(hidden = true)
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

	public UserDTO toDTO() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm VV");
		UserDTO tmp = new UserDTO();
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
