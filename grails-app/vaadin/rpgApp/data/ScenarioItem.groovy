package rpgApp.data

class ScenarioItem implements Serializable {
    private Integer id
    private String name
    private String type
    private Integer playersCount
    private String content
    private String owner
    private String system

    public Integer getId() {
        return id
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Integer getPlayersCount() {
        return playersCount;
    }
    public void setPlayersCount(Integer playersCount) {
        this.playersCount = playersCount;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getSystem() {
        return system;
    }
    public void setSystem(String system) {
        this.system = system;
    }
}
