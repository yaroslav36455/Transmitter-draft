package ua.itea.model;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class ServerProvider {
	private Set<Server> servers;
	private ServerFactory factory;
	
	public ServerProvider(ServerFactory factory) {
		servers = new TreeSet<>((o1, o2)->o1.hashCode() - o2.hashCode());
		this.factory = factory;
	}
	
	public Server createAndRun() throws IOException {
		Server newServer = factory.create();
		
		servers.add(newServer);
		return newServer;
	}
	
	public boolean isPresent(Server server) {
		return servers.contains(server);
	}
	
	public boolean remove(Server server) {
		return servers.remove(server);
	}
	
	public void stopAll() {
		
	}
}
