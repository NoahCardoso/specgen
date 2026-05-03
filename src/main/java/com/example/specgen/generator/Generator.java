package com.example.specgen.generator;
public interface Generator{
	public void generate() throws Exception;
	public String getName();
	public String getContent();
}