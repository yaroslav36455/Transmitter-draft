package ua.itea.model.message;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ua.itea.model.DataFileAnswer;

public class DataAnswer implements Serializable, Iterable<DataFileAnswer> {
	private static final long serialVersionUID = -3788291392964281822L;
	private Set<DataFileAnswer> answers;
	
	public DataAnswer() {
		answers = new TreeSet<>();
	}
	
	public boolean isEmpty() {
		return answers.isEmpty();
	}
	
	public void add(DataFileAnswer dataFileAnswer) {
		answers.add(dataFileAnswer);
	}

	@Override
	public Iterator<DataFileAnswer> iterator() {
		return answers.iterator();
	}
	
	public int size() {
		return answers.size();
	}
}
