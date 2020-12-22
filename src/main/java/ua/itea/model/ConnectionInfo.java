package ua.itea.model;

public class ConnectionInfo {
	private final String name;
	private final byte[] address;
	private final int port;
	
	public ConnectionInfo(String name, byte[] address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	
	public byte[] getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
}
