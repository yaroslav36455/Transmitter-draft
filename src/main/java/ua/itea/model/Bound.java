package ua.itea.model;

class Bound {
	private int lower;
	private int upper;
	
	public Bound() {
		/* empty */
	}
	
	public Bound(int value1, int value2) {
		if (value1 < value2) {
			lower = value1;
			upper = value2;
		} else {
			lower = value2;
			upper = value1;
		}
	}
	
	public void setLower(int value) {
		lower = value;
		
		if (upper < lower) {
			upper = lower;
		}
	}
	
	public void setUpper(int value) {
		upper = value;
		
		if (upper < lower) {
			lower = upper;
		}
	}
	
	public int lower() {
		return lower;
	}
	
	public int upper() {
		return upper;
	}
}
