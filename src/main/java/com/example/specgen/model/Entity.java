package com.example.specgen.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Entity{
	private String entity;
	private String table;
	private LinkedHashMap<String, Field> fields;
	private boolean create;
	private boolean read;
	private boolean update;
	private boolean delete;
	private String mvnPackage;

	public Entity(){}

	public String getName() {
        return entity;
    }

    public void setName(String entity) {
        this.entity = entity;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, Field> getFields() {
		Map<String, Field> copy = (LinkedHashMap<String, Field>)fields.clone();
        return copy;
    }

    public void setFields(Map<String, Field> fields) {
		this.fields = new LinkedHashMap<>();
        for (String key: fields.keySet()){
			this.fields.put(key, fields.get(key));
		}
    }

	public String getPrimaryKey(){
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			String key = entry.getKey();
			Field value = entry.getValue();

			if (value.isPrimary()){
				return key;
			}
		}
		return "";
	}
	//
	public boolean isCreate(){
		return create;
	}

	public void setCreate(boolean create){
		this.create = create;
	}
	//
	public boolean isRead(){
		return read;
	}

	public void setRead(boolean read){
		this.read = read;
	}
	//
	public boolean isUpdate(){
		return update;
	}

	public void setUpdate(boolean update){
		this.update = update;
	}
	//
	public boolean isDelete(){
		return delete;
	}

	public void setDelete(boolean delete){
		this.delete = delete;
	}

	public String getPackage() {
        return mvnPackage;
    }

    public void setPackage(String mvnPackage) {
        this.mvnPackage = mvnPackage;
    }

	
}

