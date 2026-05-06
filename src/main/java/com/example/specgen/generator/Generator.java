package com.example.specgen.generator;

import com.example.specgen.model.Entity;

public interface Generator{
	public void setEntity(Entity e);
	public void generate() throws Exception;
	public String getName();
	public String getContent();
}