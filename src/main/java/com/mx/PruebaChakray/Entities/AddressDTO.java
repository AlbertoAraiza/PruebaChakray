package com.mx.PruebaChakray.Entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Objeto de transferencia de datos para enviar y recibir informacion de las direcciones")
public class AddressDTO {
	@Schema(description = "Identificador unico de cada usuario para referenciarlo", example = "1dded8fb-cae9-485c-aabc-09b27f7056ac")
	private String userId;
	
	@Schema(description = "Nombre de la direccion", example = "Alberto Araiza")
	private String name;
	
	@Schema(description = "Detalles de la direccion", example = "Rosa Porcelina #150, El Rosedal, Aguascalientes, Ags.")
	private String street;
	
	@Schema(description = "Codigo identificador del pais", example = "MX")
	private String countryCode;
	
	public Address toEntity(Users user) {
		return new Address(0, name, street, countryCode, user);
	}
}
