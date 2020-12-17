package ua.itea.model;

public class Priority {
	private int priority;

	public Priority(int priority) {
		setPriority(priority);
	}

	public int get() {
		return priority;
	}

	public void setPriority(int priority) {
		if (priority < 0) {
			throw new IllegalArgumentException("priority < 0");
		}
		
		this.priority = priority;
	}
	
	public float percent(Priority totalPriority) {
		return priority * 100.f / totalPriority.priority;
	}
}
