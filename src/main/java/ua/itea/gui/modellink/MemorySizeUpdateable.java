package ua.itea.gui.modellink;

import ua.itea.model.MemorySize;

public class MemorySizeUpdateable extends MemorySize {
	private static final long serialVersionUID = -5411896761074220292L;
	private Runnable update;
	
	public MemorySizeUpdateable() {
		/* empty */
	}
	
	public MemorySizeUpdateable(long value) {
		super(value);
	}

	public void setUpdateCallback(Runnable update) {
		this.update = update;
	}
	
	public void update() {
		update.run();
	}
	
	@Override
	public void setSize(long size) {
		super.setSize(size);
		if (update != null) {
			update();	
		}
	}
}
