package com.example.specgen.writer;

//import java.util.StringBuilder;

public class PostgresSqlWriter{
	
	private final StringBuilder stringFile;

	public PostgresSqlWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}

	public void createTable(String table){
		stringFile.append("CREATE TABLE ").append(table).append(" (\n");
	}

	public void addField(String name, String type, String flag){
		stringFile.append("\t").append(name).append(" ").append(type).append(" ").append(flag).append(",\n");
	}

	public void closeTable(){
		stringFile.append(");\n");
	}

	public String getStringFile(){
		return stringFile.toString();
	}


}