package utilities;

public enum Environments {

	LOCAL_HTTP("http", "localhost", "8008") {
		@Override
		public String generateURL() {
			return getProtocol() + "://" + getServer() + ":" + getPort() + "/";

		}
	},
	LOCAL_HTTPS("https", "localhost", "8443") {
		@Override
		public String generateURL() {
			return getProtocol() + "://" + getServer() + ":" + getPort() + "/";
		}
	},
	
	DEV("https",System.getenv("SERVER"),System.getenv("CLAIM_PORT")) {
		
		@Override
		public String generateURL() {
			return getProtocol() + "://" +  getServer() + ":" + getPort() + "/";
		}				
	},
		SIT("https", System.getenv("SERVER"),System.getenv("CLAIM_PORT")) {
			
			@Override
			public String generateURL() {
				return getProtocol() + "://" +  getServer() + ":" + getPort() + "/";
			}
	},
		UAT("https", System.getenv("SERVER"),System.getenv("CLAIM_PORT")) {
		
		@Override
		public String generateURL() {
			return getProtocol() + "://" +  getServer() + ":" + getPort() + "/";
		}
},
	PPD("https",System.getenv("SERVER"),System.getenv("CLAIM_PORT")) {
		
		@Override
		public String generateURL() {
			return getProtocol() + "://" +  getServer() + ":" + getPort() + "/";
		}
};



	private String protocol;
	private String server;
	private String port;

	Environments(String protocol, String server, String port) {

		this.protocol = protocol;
		this.server = server;
		this.port = port;

	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	abstract public String generateURL();

}
