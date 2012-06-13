package rpgApp.data

class NewsItem implements Serializable {
    private Integer id
    private Date dateCreated
    private String title
    private String content
    private String author

    public Integer getId() {
        return id
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
