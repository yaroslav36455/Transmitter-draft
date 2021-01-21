package ua.itea.model.message;

import java.io.Serializable;

import ua.itea.model.Messenger;

public interface Message extends Serializable {
	void execute(Messenger messenger);
}
