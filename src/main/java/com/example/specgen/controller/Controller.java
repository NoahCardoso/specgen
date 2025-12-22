package com.example.specgen.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.specgen.service.SpecService;

@RestController
@RequestMapping("/spec")
public class Controller {
	private final SpecService service;

    public Controller(SpecService service) {
        this.service = service;
    }

	@PostMapping(
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public ResponseEntity<Void> uploadSpec(@RequestPart("spec") MultipartFile file) throws IOException {

		String yaml = new String(file.getBytes(), StandardCharsets.UTF_8);

		// parse yaml here
		System.out.println(yaml);
		service.process(yaml);

		return ResponseEntity.ok().build();
	}

}