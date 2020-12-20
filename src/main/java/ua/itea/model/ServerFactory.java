package ua.itea.model;

import java.io.IOException;

public interface ServerFactory {
	public Server create() throws IOException;
}
