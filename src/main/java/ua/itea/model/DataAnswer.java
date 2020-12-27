package ua.itea.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class DataAnswer implements Serializable, Iterable<DataFileAnswer> {
	private static final long serialVersionUID = -3788291392964281822L;
	private Set<DataFileAnswer> answer;
	
	public DataAnswer() {
		answer = new TreeSet<>();
	}
	
	public void add(DataFileAnswer dataFileAnswer) {
		answer.add(dataFileAnswer);
	}

	@Override
	public Iterator<DataFileAnswer> iterator() {
		return answer.iterator();
	}
	
	public int size() {
		return answer.size();
	}
}
