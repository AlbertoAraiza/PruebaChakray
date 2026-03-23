package com.mx.PruebaChakray.Entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Objeto de transferencia de datos para recibir informacion de inicio de sesión")
public class LoginDTO {
	@Schema(description = "Tax id del usuario que intenta realizar el inicio de sesión.", example = "ABCD998877XYZ")
	String username;
	@Schema(description = "Contraseña desencriptada relacionada con el tax id proporcionado", example = "Pasword1")
	String password;
}
