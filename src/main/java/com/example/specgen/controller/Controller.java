package com.example.specgen.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
	public ResponseEntity<StreamingResponseBody> uploadSpec(@RequestPart("spec") MultipartFile file) throws IOException {

		String yaml = new String(file.getBytes(), StandardCharsets.UTF_8);

		try {
            // Assuming `service.process(yaml)` generates a Map of filenames and their respective content
            Map<String, String> generatedStringFiles = service.process(yaml);
            
            StreamingResponseBody stream = outputStream -> {
                try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
                    // Loop through generated files and add them to the zip output stream
                    for (Map.Entry<String, String> entry : generatedStringFiles.entrySet()) {
                        String filename = entry.getKey();
                        String content = entry.getValue();

                        ZipEntry zipEntry = new ZipEntry(filename);
                        zip.putNextEntry(zipEntry);
                        zip.write(content.getBytes(StandardCharsets.UTF_8));
                        zip.closeEntry();
                    }
                } catch (IOException e) {
                    
                    throw new RuntimeException("Error writing zip output");
                }
            };

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"export.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(stream);

        } catch (Exception e) {
        
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(outputStream -> outputStream.write(e.toString().getBytes()));
        }
    }

	

}