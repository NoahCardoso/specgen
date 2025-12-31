package com.example.specgen.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.specgen.service.SpecService;

import java.util.zip.*;

import org.springframework.http.HttpHeaders;
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
	public ResponseEntity<StreamingResponseBody> uploadSpec(@RequestPart("spec") MultipartFile file) throws IOException {

		String yaml = new String(file.getBytes(), StandardCharsets.UTF_8);

		// parse yaml here
		System.out.println(yaml);
		Map<String,String> generatedStringFiles = service.process(yaml);
		
		StreamingResponseBody stream = outputStream -> {
			try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
				for(String filename: generatedStringFiles.keySet()){
					String content = generatedStringFiles.get(filename);
					ZipEntry entry = new ZipEntry(filename);

					zip.putNextEntry(entry);

					zip.write(content.getBytes(StandardCharsets.UTF_8));

					zip.closeEntry();
				}
			}
		};


		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"export.zip\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(stream);
	}

	

}