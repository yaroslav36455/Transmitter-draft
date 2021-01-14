package ua.itea.model;

import java.io.Serializable;

import ua.itea.model.message.DataRequest;

public class DownloaderRequest implements Serializable {
	private static final long serialVersionUID = 2196703204862752026L;
	private DataRequest dataRequest;
	
	public DataRequest getDataRequest() {
		return dataRequest;
	}
	
	public void setDataRequest(DataRequest dataRequest) {
		this.dataRequest = dataRequest;
	}
}
