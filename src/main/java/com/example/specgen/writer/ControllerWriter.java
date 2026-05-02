package com.example.specgen.writer;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.example.specgen.model.Field;

public class ControllerWriter{
	private final StringBuilder stringFile;

	public ControllerWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}

	public void createClass(String mvnPackage, String entity, String table){
		stringFile.insert(0, "@RestController\n@RequestMapping(\"/"+table+"\")\npublic class "+entity+"Controller {\n\n");
		stringFile.insert(0, "import org.springframework.web.bind.annotation.*;\nimport java.util.List;\n\n");
		stringFile.insert(0, "package "+mvnPackage+";\n");
		stringFile.append("}");
	}

	public void addRepo(String entity){
		stringFile.append("private final "+entity+"Repository repo;\n");
	}

	public void addConstructor(String entity){
		stringFile.append("\npublic "+entity+"Controller("+entity+"Repository repo) {\n")
				  .append("this.repo = repo;\n}\n\n");
	}

	public void addCreateRoute(String entity){
		stringFile.append("@PostMapping\npublic "+entity+" create(@RequestBody "+entity+" "+entity.toLowerCase()+") {\nreturn repo.save("+entity.toLowerCase()+");\n}\n\n");
	}
	public void addGetAllRoute(String entity){
		stringFile.append("@GetMapping\npublic List<"+entity+"> getAll() {\nreturn repo.findAll();\n}\n\n");
	}
	public void addGetOneRoute(String entity, String primaryKeyType, String primaryKey){
		stringFile.append("@GetMapping(\"/{"+primaryKey+"}\")\npublic "+entity+" getOne(@PathVariable "+primaryKeyType+" "+primaryKey+") {\nreturn repo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());\n}\n\n");
	}
	public void addUpdateRoute(String entity, String primaryKeyType, String primaryKey, Map<String, Field> fields){
		stringFile.append("@PutMapping(\"/{"+primaryKey+"}\")\npublic "+entity+" update(@PathVariable "+primaryKeyType+" "+primaryKey+", @RequestBody "+entity+" updated) {\n")
		.append(entity+" "+entity.toLowerCase()+" = repo.map(existing -> { ...setters...; return ResponseEntity.ok(repo.save(existing)); }).orElse(ResponseEntity.notFound().build());\n");
		for (String key: fields.keySet()){
			if(fields.get(key).isPrimary()){
				continue;
			}
			stringFile.append(entity.toLowerCase()+".set"+StringUtils.capitalize(key)+"(updated.get"+StringUtils.capitalize(key)+"());\n");
		}
		stringFile.append("return repo.save("+entity.toLowerCase()+");\n}\n\n");
	}
	public void addDeleteRoute(String entity, String primaryKeyType, String primaryKey){
		stringFile.append("@DeleteMapping(\"/{"+primaryKey+"}\")\npublic void delete(@PathVariable "+primaryKeyType+" "+primaryKey+") {\n")
		.append("repo.deleteById("+primaryKey+");\n}\n\n");

	}

	public String getStringFile(){
		return stringFile.toString();
	}
	
}