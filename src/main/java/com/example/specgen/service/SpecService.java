package com.example.specgen.service;

import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.formatter.PostgreSqlFormatter;
import com.example.specgen.generator.*;
import com.example.specgen.model.Entity;
import com.example.specgen.parser.YamlParser;
import com.example.specgen.validator.SpecValidator;
import com.example.specgen.writer.TemplateWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SpecService {

    private static final Logger log = LoggerFactory.getLogger(SpecService.class);

    private final TemplateWriter writer;
    private final JavaFormatter javaFormatter;
    private final PostgreSqlFormatter postgreSqlFormatter;

    public SpecService(TemplateWriter writer,
                       JavaFormatter javaFormatter,
                       PostgreSqlFormatter postgreSqlFormatter) {
        this.writer = writer;
        this.javaFormatter = javaFormatter;
        this.postgreSqlFormatter = postgreSqlFormatter;
    }

    public byte[] generate(String yaml) throws Exception {
        log.info("Parsing yaml");
        Entity spec = YamlParser.parse(yaml);

        log.info("Validating spec");
        SpecValidator validator = new SpecValidator(javaFormatter, postgreSqlFormatter);
        validator.setSpec(spec);
        if (!validator.check()) {
            throw new IllegalArgumentException("Invalid yaml format");
        }

        log.info("Starting generation for entity: {}", spec.getName());
        List<Generator> generators = buildGenerators(spec);

        return buildZip(generators);
    }

    // public so StableTest can call it directly
    public List<Generator> buildGenerators(Entity spec) throws Exception {
        List<Generator> generators = List.of(
            new PostgreSqlGenerator(writer, postgreSqlFormatter),
            new EntityGenerator(writer, javaFormatter),
            new ServiceGenerator(writer, javaFormatter),
            new ControllerGenerator(writer, javaFormatter),
            new RepositoryGenerator(writer, javaFormatter),
            new ExceptionHandlerGenerator(writer)
        );

        for (Generator generator : generators) {
            generator.setEntity(spec);
            generator.generate();
        }

        return generators;
    }

    private byte[] buildZip(List<Generator> generators) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Generator generator : generators) {
                log.info("Writing file: {}", generator.getName());
                zos.putNextEntry(new ZipEntry(generator.getName()));
                zos.write(generator.getContent().getBytes());
                zos.closeEntry();
            }
        }
        return baos.toByteArray();
    }
}