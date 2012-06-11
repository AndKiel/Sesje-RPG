package rpgApp.utils

class ChatEntry {
	private String nickname
	private String message
	private Date timeStamp
	
	ChatEntry(String n, String m, Date tS) {
		nickname = n
		message = m
		timeStamp = tS
	}
	
	public String getNickname() {
		return nickname
	} 
	
	public String getMessage() {
		return message
	}
	
	public Date getTimeStamp() {
		return timeStamp
	}
}
