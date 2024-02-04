public class Author {
    private String name;
    private String activeYear;

    public Author() {}

    public Author(String name, String activeYear) {
        this.name= name;
        this.activeYear = activeYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveYear() {
        return activeYear;
    }

    public void setActiveYear(String publishedYear) {
        this.activeYear = publishedYear;
    }

}
