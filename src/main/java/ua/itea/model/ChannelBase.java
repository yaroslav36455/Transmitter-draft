package ua.itea.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChannelBase implements Iterable<Channel> {
	private List<Channel> channels;
	
	public ChannelBase() {
		channels = new ArrayList<>();
	}
	
	public void add(Channel channel) {
		channels.add(channel);
	}

	@Override
	public Iterator<Channel> iterator() {
		return channels.iterator();
	}
}
