package ua.itea.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class StateChangedAnswer implements Serializable, Iterable<FileStateChangedAnswer> {
	private static final long serialVersionUID = -7920679487533523448L;
	private Set<FileStateChangedAnswer> answers;
	
	public StateChangedAnswer() {
		answers = new TreeSet<>();
	}

	@Override
	public Iterator<FileStateChangedAnswer> iterator() {
		return answers.iterator();
	}

}
