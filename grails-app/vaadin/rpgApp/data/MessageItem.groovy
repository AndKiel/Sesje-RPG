package rpgApp.data

class MessageItem implements Serializable {
	private Integer id
	private Date dateCreated
	private String topic
	private String content
	private Boolean wasRead
	private String sender
	private String addressee
	
	public Integer getId() {
		return id
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDateCreated() {
		return dateCreated.toString();
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Boolean getWasRead() {
		return wasRead;
	}
	public void setWasRead(Boolean wasRead) {
		this.wasRead = wasRead;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
}
