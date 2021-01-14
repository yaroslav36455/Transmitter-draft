package ua.itea.model.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ua.itea.model.DataFileRequest;

public class DataRequest implements Serializable, Iterable<DataFileRequest> {
	private static final long serialVersionUID = -5650695911641363129L;
	private List<DataFileRequest> requests;
	
	public DataRequest() {
		requests = new ArrayList<>();
	}
	
	public boolean isEmpty() {
		return requests.isEmpty();
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
