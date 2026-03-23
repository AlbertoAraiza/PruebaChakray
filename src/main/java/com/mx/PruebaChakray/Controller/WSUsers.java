package com.mx.PruebaChakray.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mx.PruebaChakray.Entities.LoginDTO;
import com.mx.PruebaChakray.Entities.UserDTO;
import com.mx.PruebaChakray.Entities.Users;
import com.mx.PruebaChakray.Service.ImpUsers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "api/users")
@CrossOrigin("*")
@Tag(name = "Usuarios", description = "API de administracion de usuarios")
public class WSUsers {
	@Autowired
	ImpUsers imp;

	Map<String, String> availableFields = Map.of("email", "email", "id", "id", "name", "name", "phone", "phone",
			"tax_id", "taxId", "created_at", "createdAt");
	List<String> filterTypes = List.of("co", "eq", "sw", "ew");

	/*
	 * @GetMapping("/list") ResponseEntity<?> read() { try { return
	 * ResponseEntity.ok(imp.list()); } catch (Exception e) { return
	 * ResponseEntity.badRequest().body(e.getLocalizedMessage()); } }
	 */

	@GetMapping
	@Operation(summary = "Buscar usuarios", description = "Busca usuarios filtrados y ordenados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Regresa una lista de usuarios con el filtro proporcionado", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
			@ApiResponse(responseCode = "400", description = "campo de ordenamiento o filtro invalido o filtro no proporcionado") })
	ResponseEntity<?> find(
			@Parameter(description = "Campo de ordenamiento", required = false) @RequestParam("sortedBy") Optional<String> sortedBy,
			@Parameter(description = "Filtro de resultados", required = true) @RequestParam("filter") String filter) {
		try {
			String message = "";
			String[] filterParts = filter.split(" ");
			System.out.println("Filter by: [0]" + filterParts[0] + " [1]" + filterParts[1] + " [2]: " + filterParts[2]);
			String filterBy = filterParts[0];
			String filterType = filterParts[1];
			String filterValue = filterParts[2];
			if (filterTypes.contains(filterType)) {
				if (sortedBy.isEmpty()) {
					return ResponseEntity.ok(imp.listOrderBy(null, filterBy, filterType, filterValue));
				} else {
					String sort = sortedBy.get();
					if (availableFields.containsKey(sort)) {
						sort = availableFields.get(sort);
						return ResponseEntity.ok(imp.listOrderBy(sort, filterBy, filterType, filterValue));
					} else {
						message = sort + " no es un campo valido para ordernar";
					}
				}
			} else {
				message = "tipo de filtro invalido";
			}
			return ResponseEntity.badRequest().body(message);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
		}
	}

	@Operation(summary = "Agregar usuario", description = "Agregar un usuario nuevo")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Se agrego el usuario correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
			@ApiResponse(responseCode = "400", description = "Identificador invalido o duplicado o numero de telefono invalido") })
	@PostMapping
	ResponseEntity<?> create(
			@Parameter(description = "Cuerpo del nuevo usuario a agregar", required = true) @RequestBody Users body) {
		try {
			System.out.println("body: " + body);
			return ResponseEntity.ok(imp.create(body));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getLocalizedMessage());
		}
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Editar usuario", description = "Editar un usuario registrado anteriormente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario editado exitosamente"),
			@ApiResponse(responseCode = "400", description = "No existe un usuario con el identificador o numero de telefono invalido") })
	ResponseEntity<?> update(
			@Parameter(description = "Identificador del usuario a actualizar", required = true) @PathVariable String id,
			@Parameter(description = "Cuerpo del usuario con los campos a remplazar", required = true) @RequestBody Users body) {
		try {
			imp.update(id, body);
			return ResponseEntity.ok("Actualizacion exitosa");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar usuario", description = "Elimina un registro de usuario")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Registro de usuario eliminado con exito"),
			@ApiResponse(responseCode = "400", description = "No existe un usuario con ese identificador") })
	ResponseEntity<?> delete(
			@Parameter(description = "Identificador del usuario a eliminar", required = true) @PathVariable String id) {
		try {
			imp.delete(id);
			return ResponseEntity.ok("Eliminacion exitosa");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
		}
	}

	@PostMapping("/login")
	@Operation(summary = "Inicio de sesion", description = "Realizar intento de iniciar sesion")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Se realizo el intento de inicio de sesion", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "true"), @ExampleObject(value = "false") })),
			@ApiResponse(responseCode = "400", description = "No existe usuario con el identificador") })
	ResponseEntity<?> login(
			@Parameter(description = "Nombre de usuario y contraseña", required = true) @RequestBody LoginDTO body) {
		try {
			return ResponseEntity.ok(imp.login(body));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
		}
	}
}
