package com.example.specgen.controller;

import com.example.specgen.service.SpecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/spec")
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private final SpecService service;

    public Controller(SpecService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> uploadSpec(@RequestPart("spec") MultipartFile file) {
        try {
            String yaml = new String(file.getBytes(), StandardCharsets.UTF_8);
            log.info("Received spec file: {}", file.getOriginalFilename());

            byte[] zip = service.generate(yaml);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"export.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zip);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid spec submitted: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            log.error("Unexpected error processing spec", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}