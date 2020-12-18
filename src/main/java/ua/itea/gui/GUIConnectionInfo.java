package ua.itea.gui;

public class GUIConnectionInfo {
	private String address;
	private String port;
	private String name;
	
	public GUIConnectionInfo(String address, String port, String name) {
		this.address = address;
		this.port = port;
		setNameImpl(name);
	}

	public String getAddress() {
		return address;
	}
	
	public String getPort() {
		return port;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		setNameImpl(name);
	}
	
	private void setNameImpl(String name) {
		this.name = name != null && !name.isEmpty() ? name : address;
	}
}
