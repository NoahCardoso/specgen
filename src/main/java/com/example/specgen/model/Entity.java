package com.example.specgen.model;

import java.util.HashMap;
import java.util.Map;

public class Entity{
	private String entity;
	private String table;
	private HashMap<String, Field> fields;
	private boolean create;
	private boolean read;
	private boolean update;
	private boolean delete;

    public Entity(){}

	public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, Field> getFields() {
		Map<String, Field> copy = (HashMap<String, Field>)fields.clone();
        return copy;
    }

    public void setFields(Map<String, Field> fields) {
		this.fields = new HashMap<>();
        for (String key: fields.keySet()){
			this.fields.put(key, fields.get(key));
		}
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

	
}

