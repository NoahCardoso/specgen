package com.example.specgen.writer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class EntityWriter{
	private final StringBuilder stringFile;

	public EntityWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}
	//Create class // handles @ and imports and public class _ {}
	public void createClass(String entity, String table){
		stringFile.insert(0, "@Entity\n@Table(name = \""+table+"\")\npublic class"+entity+" {\n");
		stringFile.append("}\n");
	}
	//maybe constructor's????
	//addGlobal
	public void addGlobal(String type, String name){
		stringFile.insert(0,"private "+type+" "+name+";\n");
	}
	//addGetter
	public void addGetter(String type, String name){
		
		stringFile.append("public ")
				  .append(type)
				  .append(" ")
				  .append(name)
				  .append("() {\n")
				  .append("return "+name+";\n")
				  .append("}\n");
	}
	//addSetter
	public void addSetter(String type, String name){
		stringFile.append("public ")
                    .append(type)
                    .append(" ")
                    .append(name).append("(")
                    .append(type)
                    .append(" ")
                    .append(name)
                    .append(") {\n")
					.append("this.")
					.append(name)
					.append(" = ")
					.append(name)
					.append(";\n")
					.append("}\n");
	}

	public String getStringFile(){
		return stringFile.toString();
	}

	public boolean toFile(String fileName){
		
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {          
                //Created
                try {
                    FileWriter myWriter = new FileWriter(fileName);
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