package project.components;

import project.database.Document;

public class TempFileData {
	String path;
	String name;
	String contentType;
	Document document;
	String type;
	
	public TempFileData(String path, String name, String contentType) {
		this.path = path;
		this.name = name;
		this.contentType = contentType;
		setTypeFromName(name);
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	public void setTypeFromName(String name) {
		type = name.substring(name.lastIndexOf(".") + 1);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
