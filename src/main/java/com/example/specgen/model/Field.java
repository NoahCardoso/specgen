package com.example.specgen.model;

public class Field{
	private String type;
    private boolean primary;
    private boolean unique;
    private boolean nullable = true;
	private String ref;
    private String joinColumn;
	private String relationType;
	private boolean relation;

	public Field(){}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
		this.relation = "relation".equals(type);
	}

	public String getRef(){
		return ref;
	}

	public void setRef(String ref){
		this.ref = ref;
	}

	public String getJoinColumn(){
		return joinColumn;
	}

	public void setJoinColumn(String joinColumn){
		this.joinColumn = joinColumn;
	}

	public String getRelationType(){
		return relationType;
	}

	public void setRelationType(String relationType){
		this.relationType = relationType;
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

	public boolean isRelation() {
		return "relation".equals(type);
	}

	
}