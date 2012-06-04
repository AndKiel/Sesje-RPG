package rpgApp.data

class NotificationItem  implements Serializable{
	private Integer id 
	private Boolean role	// true - master , false - player
	private Boolean type	// true - invintation, false - acceptation
	private String sender
	private String receiver
	private Integer session
	
	public Integer getId() {
		return id
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getRole() {
		return role;
	}
	public void setRole(Boolean role) {
		this.role = role;
	}
	public Boolean getType() {
		return type;
	}
	public void setType(Boolean type) {
		this.type = type;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Integer getSession() {
		return session;
	}
	public void setSession(Integer session) {
		this.session = session;
	}
}
