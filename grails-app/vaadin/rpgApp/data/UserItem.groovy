package rpgApp.data

class UserItem implements Serializable{
	private String login
	private Boolean state
	private Set<String> roles
	private Date dateCreated
	private String nickname
	private String location
	private Date birthday
	private String homepage
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles
	}
	public String getDateCreated() {
		return dateCreated.toString();
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public boolean isAdmin() {
		boolean flag = false
		for(String role in roles) {
			if(role == "Administrator"){
				flag = true
			}
		}
		return flag
	}
}
