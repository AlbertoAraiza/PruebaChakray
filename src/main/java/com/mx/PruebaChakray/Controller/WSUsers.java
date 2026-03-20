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
import com.mx.PruebaChakray.Entities.Users;
import com.mx.PruebaChakray.Service.ImpUsers;

@RestController
@RequestMapping(path = "api/users")
@CrossOrigin("*")
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
	ResponseEntity<?> find(@RequestParam("sortedBy") Optional<String> sortedBy, @RequestParam("filter") String filter) {
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

	@PostMapping
	ResponseEntity<?> create(@RequestBody Users body) {
		try {
			System.out.println("body: " + body);
			return ResponseEntity.ok(imp.create(body));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getLocalizedMessage());
		}
	}
	
	@PatchMapping("/{id}")
	ResponseEntity<?> update(@PathVariable String id, @RequestBody Users body){
		try {
			imp.update(id, body);
			return ResponseEntity.ok("Actualizacion exitosa");
		}catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<?> delete(@PathVariable String id){
		try {
			imp.delete(id);
			return ResponseEntity.ok("Eliminacion exitosa");
		}catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
		}
	}
	
	@PostMapping("/login")
	ResponseEntity<?> login(@RequestBody LoginDTO body){
		try {
			return ResponseEntity.ok(imp.login(body));
		}catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
		}
	}
}
