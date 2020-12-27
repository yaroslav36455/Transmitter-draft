package ua.itea.model;

import java.io.Serializable;

public enum Mark implements Serializable {
	START, STOP_AS_REQUESTER, STOP_AS_RECEIVER, STOP_SERVER, END, ACCEPT, REJECT, DATA, NEW_FILES, ERROR, IGNORE
}
