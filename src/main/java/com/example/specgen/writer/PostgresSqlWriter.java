package com.example.specgen.writer;

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


}