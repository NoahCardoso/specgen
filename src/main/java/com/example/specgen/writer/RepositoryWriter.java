package com.example.specgen.writer;

public class RepositoryWriter{
	
	private final StringBuilder stringFile;

	public RepositoryWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}

	public void createRepository(String mvnPackage, String entity, String primaryType){
		stringFile.append("package "+mvnPackage+";\n")
                    .append("import org.springframework.data.jpa.repository.JpaRepository;\n")
		            .append("public interface "+entity+"Repository extends JpaRepository<"+entity+", "+primaryType+"> {}\n");
	}

    public String getStringFile(){
		return stringFile.toString();
	}

}