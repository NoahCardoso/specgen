package com.example.specgen.writer;
import org.springframework.util.StringUtils;

import com.example.specgen.model.Field;

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
	public void addGlobal(String name, Field field){
		String type = field.getType();
		if (field.isPrimary()){
			stringFile.append("@Id\n");
			stringFile.append("@GeneratedValue\n");
		}
		if (field.isUnique() || !(field.isNullable())){
			stringFile.append("@Column(");
			if (field.isUnique()){
				stringFile.append("unique = true, ");
			}
			if (!(field.isNullable())){
				stringFile.append("nullable = false");
			}
			if(stringFile.substring(stringFile.length()-1,stringFile.length()).equals(", ")){
				stringFile.delete(stringFile.length()-1,stringFile.length());
			}
			stringFile.append(")\n");
			
		}
		stringFile.append("private "+type+" "+name+";\n");
	}

	public void addConstructor(){
		stringFile.append("\n");
	}
	
	//addGetter
	public void addGetter(String name, Field field){
		String type = field.getType();
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
	public void addSetter(String name, Field field){
		String type = field.getType();
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