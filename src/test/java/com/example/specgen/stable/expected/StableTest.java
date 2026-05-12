// package com.example.specgen.stable;
// import java.io.IOException;
// import java.io.InputStream;
// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.nio.file.Path;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.Test;

// import com.example.specgen.generator.ControllerGenerator;
// import com.example.specgen.generator.EntityGenerator;
// import com.example.specgen.generator.Generator;
// import com.example.specgen.generator.PostgreSqlGenerator;
// import com.example.specgen.generator.RepositoryGenerator;
// import com.example.specgen.model.Entity;
// import com.example.specgen.parser.YamlParser;
// public class StableTest {

// 	private final String YamlPath = "/src/test/java/com/example/specgen/stable/input/example.yml";
// 	private final String ControllerPath = "./expected/UserController.txt";
// 	private final String RepositoryPath = "./expected/UserRepository.txt";
// 	private final String EntityPath = "./expected/User.txt";
// 	private final String SchemaPath = "./expected/schema.sql";

// 	@Test
// 	public void controllerTest() throws IOException, Exception{
// 		String yaml = Files.readString(Path.of(YamlPath));
// 		Entity spec = YamlParser.parse(yaml);
// 		Generator g = new ControllerGenerator(spec);
// 		g.generate();
// 		String javaController = g.getContent();
// 		assertEquals(javaController, load(ControllerPath));
		
// 	}

// 	@Test
// 	public void postgreSqlTest() throws IOException, Exception {
// 		String yaml = Files.readString(Path.of(YamlPath));
// 		Entity spec = YamlParser.parse(yaml);
// 		Generator g = new PostgreSqlGenerator(spec);
// 		g.generate();
// 		String schema = g.getContent();
// 		assertEquals(schema, load(SchemaPath));
// 	}

// 	@Test
// 	public void entityTest() throws IOException, Exception {
// 		String yaml = Files.readString(Path.of(YamlPath));
// 		Entity spec = YamlParser.parse(yaml);
// 		Generator g = new EntityGenerator(spec);
// 		g.generate();
// 		String javaEntity = g.getContent();
// 		assertEquals(javaEntity, load(EntityPath));
// 	}

// 	@Test
// 	public void repositoryTest() throws IOException, Exception {
// 		String yaml = Files.readString(Path.of(YamlPath));
// 		Entity spec = YamlParser.parse(yaml);
// 		Generator g = new RepositoryGenerator(spec);
// 		g.generate();
// 		String javaRepository = g.getContent();
// 		assertEquals(javaRepository, load(RepositoryPath));
// 	}

// 	private String load(String path) throws Exception {
//     try (InputStream is =
//              getClass().getClassLoader().getResourceAsStream(path)) {

//         if (is == null) {
//             throw new IllegalArgumentException("Resource not found: " + path);
//         }

//         return new String(is.readAllBytes(), StandardCharsets.UTF_8);
//     }
// }
// }

