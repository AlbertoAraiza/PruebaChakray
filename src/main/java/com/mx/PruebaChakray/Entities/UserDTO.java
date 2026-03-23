package com.mx.PruebaChakray.Entities;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Objeto de transferencia de datos para enviar y recibir peticiones de Usuarios")
public class UserDTO {
	@Schema(description = "Identificador unico de cada usuario", example = "1dded8fb-cae9-485c-aabc-09b27f7056ac")
	private String id;
	@Schema(description = "Correo electronico personal del usuario", example = "pruebas@hola.mundo")
	private String email;
	@Schema(description = "Nombre completo del usuario", example = "Alberto Araiza")
	private String name;
	@Schema(description = "Telefono a 10 digos + lada internacional opcional", example = "+524494449999")
	private String phone;
	@Schema(description = "RFC del usuario", example = "ABCD998877XYZ")
	private String taxId;
	@Schema(description = "Fecha y hora en que se dio de alta el usuario", example = "20/06/2026 20:05:00")
	private String createdAt;
	@Schema(hidden = true)
	private List<Address> addresses;
}
