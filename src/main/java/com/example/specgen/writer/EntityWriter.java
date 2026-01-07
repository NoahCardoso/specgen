package com.example.specgen.writer;
import org.springframework.util.StringUtils;

public class EntityWriter{
	private final StringBuilder stringFile;

	public EntityWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}
	//Create class // handles @ and imports and public class _ {}
	public void createClass(String mvnPackage, String entity, String table){
		stringFile.insert(0, "@Entity\n@Table(name = \""+table+"\")\npublic class "+entity+" {\n\n");
		stringFile.insert(0,"import jakarta.persistence.*;\n\n");
		stringFile.insert(0,"package "+mvnPackage+";\n\n");
		stringFile.append("}");
	}
	
	//addGlobal
	public void addGlobal(String type, String name){
		stringFile.insert(0,"private "+type+" "+name+";\n");
	}

	public void addConstructor(){
		stringFile.append("\n");
	}
	
	//addGetter
	public void addGetter(String type, String name){
		
		stringFile.append("public ")
                    .append(type)
                    .append(" get")
                    .append(StringUtils.capitalize(name))
                    .append("() {\n")
					.append("return ")
					.append(name)
					.append(";\n")
					.append("}\n\n");
	}
	//addSetter
	public void addSetter(String type, String name){
		stringFile.append("public void set")
                    .append(StringUtils.capitalize(name)).append("(")
                    .append(type)
                    .append(" ")
                    .append(name)
                    .append(") {\n")
					.append("this.")
					.append(name)
					.append(" = ")
					.append(name)
					.append(";\n")
					.append("}\n\n");
	}

	public String getStringFile(){
		return stringFile.toString();
	}

}