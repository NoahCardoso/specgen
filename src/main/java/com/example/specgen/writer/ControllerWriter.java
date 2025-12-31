package com.example.specgen.writer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class ControllerWriter{
	private final StringBuilder stringFile;

	public ControllerWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}

	public void createClass(String entity, String table){
		stringFile.insert(0, "@RestController\n@RequestMapping(\"/"+table+"\")\npublic class "+entity+"Controller {\n");
		stringFile.insert(0, "import org.springframework.web.bind.annotation.*;\nimport java.util.List;\n");
		stringFile.append("}\n");
	}

	public void addRepo(String entity){
		stringFile.append("private final "+entity+"Repository repo;\n");
	}

	public void addConstructor(String entity){
		stringFile.append("public "+entity+"Controller("+entity+"Repository repo) {\n")
				  .append("this.repo = repo;\n}\n");
	}

	public void addCreateRoute(String entity){
		stringFile.append("@PostMapping\npublic "+entity+" create(@RequestBody "+entity+" "+entity.toLowerCase()+") {\nreturn repo.save("+entity.toLowerCase()+");\n}\n");
	}
	public void addGetAllRoute(String entity){
		stringFile.append("@GetMapping\npublic List<"+entity+"> getAll() {\nreturn repo.findAll();\n}\n");
	}
	public void addGetOneRoute(String entity, String primaryKeyType, String primaryKey){
		stringFile.append("@GetMapping(\"/{"+primaryKey+"}\")\npublic "+entity+" getOne(@PathVariable "+primaryKeyType+" "+primaryKey+") {\nreturn repo.findById("+primaryKey+");\n}\n");
	}
	public void addUpdateRoute(String entity, String primaryKeyType, String primaryKey){
		stringFile.append("@PutMapping(\"/{"+primaryKey+"}\")\npublic "+entity+" update(@PathVariable "+primaryKeyType+" "+primaryKey+", @RequestBody "+entity+" updated) {\n")
		.append(entity+" "+entity.toLowerCase()+" = repo.findById("+primaryKey+");\n");
		//TODO set all other fields
		stringFile.append("return repo.save("+primaryKey+");\n}\n");
	}
	public void addDeleteRoute(String entity, String primaryKeyType, String primaryKey){
		stringFile.append("@DeleteMapping(\"/{"+primaryKey+"}\")\npublic void delete(@PathVariable "+primaryKeyType+" "+primaryKey+") {\n")
		.append("repo.deleteById("+primaryKey+");\n}\n");

	}

	public String getStringFile(){
		return stringFile.toString();
	}
	
	public boolean toFile(String filename){
		
        try {
            File file = new File(filename);
            if (file.createNewFile()) {          
                //Created
                try {
                    FileWriter myWriter = new FileWriter(filename);
                    myWriter.write(this.stringFile.toString());
                    myWriter.close();  // must close manually
                    return true;
                } catch (IOException e) {
                	System.out.println("An error occurred.");
                //e.printStackTrace();
                }
            
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            //e.printStackTrace(); // Print error details
        }

    	return false;
    
	}

}