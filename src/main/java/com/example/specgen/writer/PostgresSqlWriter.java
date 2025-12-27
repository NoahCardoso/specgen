package com.example.specgen.writer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostgresSqlWriter{
	
	private final StringBuilder stringFile;

	public PostgresSqlWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}

	public void createTable(String table){
		stringFile.append("CREATE TABLE ").append(table).append(" (\n");
	}

	public void addField(String name, String type, String property){
		stringFile.append("\t").append(name).append(" ").append(type);
		if(!property.isEmpty()){
			stringFile.append(" ").append(property);
		}
		stringFile.append(",\n");
	}

	public void closeTable(){
		stringFile.append(");\n");
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