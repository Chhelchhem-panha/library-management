import java.time.LocalDate;

public class Book {

    public static int lastAssignedId = 0;
    private int id;
    private String title;
    private Author author;

    private LocalDate publishedYear;

    private boolean status;

    private boolean isRemoved;

    public Book() {
     lastAssignedId++;
     this.id = lastAssignedId;
    }

    public Book(String title, Author author, LocalDate publishedYear, boolean status) {
        lastAssignedId++;
        this.id = lastAssignedId;
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.status = status;
        this.isRemoved = false;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDate getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(LocalDate publishedYear) {
        this.publishedYear = publishedYear;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
