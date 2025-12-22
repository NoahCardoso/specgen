package com.example.specgen.model;

public class Field{
	private String type;
    private boolean primary;
    private boolean unique;
    private boolean nullable = true;

	public Field(){}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public boolean isPrimary(){
		return primary;
	}

	public void setPrimary(boolean primary){
		this.primary = primary;
	}

	public boolean isUnique(){
		return unique;
	}

	public void setUnique(boolean unique){
		this.unique = unique;
	}

	public boolean isNullable(){
		return nullable;
	}

	public void setNullable(boolean nullable){
		this.nullable = nullable;
	}

	
}