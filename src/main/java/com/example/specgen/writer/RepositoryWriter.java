package com.example.specgen.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RepositoryWriter{
	
	private final StringBuilder stringFile;

	public RepositoryWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}

	public void createRepository(String entity, String primaryType){
		stringFile.append("import org.springframework.data.jpa.repository.JpaRepository;\n")
		.append("public interface "+entity+"Repository extends JpaRepository<"+entity+", "+primaryType+"> {}\n");
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