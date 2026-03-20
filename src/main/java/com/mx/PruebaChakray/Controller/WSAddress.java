package com.mx.PruebaChakray.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mx.PruebaChakray.Entities.AddressDTO;
import com.mx.PruebaChakray.Service.ImpAddresses;

@RestController
@RequestMapping(path = "api/address")
@CrossOrigin("*")
public class WSAddress {
	@Autowired
	ImpAddresses imp;

	@GetMapping
	public ResponseEntity<?> read() {
		try {
			return ResponseEntity.ok(imp.list());
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody AddressDTO body) {
		try {
			return ResponseEntity.ok(imp.create(body));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getLocalizedMessage());
		}
	}
}
