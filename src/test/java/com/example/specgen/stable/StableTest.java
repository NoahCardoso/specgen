// package com.example.specgen.stable;

// import com.example.specgen.generator.Generator;
// import com.example.specgen.model.Entity;
// import com.example.specgen.parser.YamlParser;
// import com.example.specgen.service.SpecService;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.List;

// import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest
// class StableTest {

//     @Autowired SpecService specService;
//     @Autowired YamlParser yamlParser;

//     private static final String BASE = "src/test/java/com/example/specgen/stable/expected/";
//     private static final String INPUT = "src/test/java/com/example/specgen/stable/input/example.yml";

//     @Test
//     void allGeneratedFilesMatchGoldenFiles() throws Exception {
//         String yaml = Files.readString(Path.of(INPUT));
//         Entity spec = yamlParser.parse(yaml);
//         List<Generator> generators = specService.buildGenerators(spec);

//         for (Generator g : generators) {
//             g.generate();
//         }

//         assertGolden(generators, "User.java",                 "User.java");
//         assertGolden(generators, "UserController.java",       "UserController.java");
//         assertGolden(generators, "UserRepository.java",       "UserRepository.java");
//         assertGolden(generators, "UserService.java",          "UserService.java");
//         assertGolden(generators, "GlobalExceptionHandler.java","GlobalExceptionHandler.java");
//         assertGolden(generators, "schema.sql",                "schema.sql");
//     }

//     private void assertGolden(List<Generator> generators, String fileName, String goldenFile) throws Exception {
//         Generator gen = generators.stream()
//             .filter(g -> g.getName().equals(fileName))
//             .findFirst()
//             .orElseThrow(() -> new AssertionError("No generator found for: " + fileName));

//         String expected = Files.readString(Path.of(BASE + goldenFile));
//         assertThat(gen.getContent())
//             .as("Mismatch for %s", fileName)
//             .isEqualToIgnoringWhitespace(expected);
//     }
// }