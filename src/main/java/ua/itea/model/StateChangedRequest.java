package ua.itea.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class StateChangedRequest implements Serializable, Iterable<FileStateChangedRequest> {
	private static final long serialVersionUID = 7755085662387952038L;
	private Set<FileStateChangedRequest> requests;
	
	public StateChangedRequest() {
		requests = new TreeSet<>();
	}

	@Override
	public Iterator<FileStateChangedRequest> iterator() {
		return requests.iterator();
	}
	
	
}
