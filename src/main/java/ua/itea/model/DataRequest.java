package ua.itea.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataRequest implements Serializable, Iterable<DataFileRequest> {
	private static final long serialVersionUID = -8138420421920032816L;
	private List<DataFileRequest> requests;
	
	public DataRequest() {
		requests = new ArrayList<>();
	}
	
	public void add(DataFileRequest dataFileRequest) {
		requests.add(dataFileRequest);
	}

	@Override
	public Iterator<DataFileRequest> iterator() {
		return requests.iterator();
	}
	
	public int size() {
		return requests.size();
	}
}
