package hw.reflection;

import org.junit.jupiter.api.Test;

import java.util.Objects;

//sample bean class taken from https://dzone.com/articles/the-bean-class-for-java-programming
class Book {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int pages;
    /**
     * Default constructor
     */
    public Book() {
        this.isbn = "";
        this.title = "";
        this.author = "";
        this.publisher = "";
        this.pages = 0;
    }
    /**
     * Non-default constructor
     *
     * @param isbn
     * @param title
     * @param author
     * @param publisher
     * @param pages
     */
    public Book(final String isbn, final String title, final String author, final String publisher, final int pages) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pages = pages;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(final String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(final String author) {
        this.author = author;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(final String publisher) {
        this.publisher = publisher;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(final int pages) {
        this.pages = pages;
    }
    @Override
    public String toString() {
        return "Book{" + "isbn=" + isbn + ", title=" + title + ", author=" + author + ", publisher=" + publisher + ", pages=" + pages + '}';
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.isbn);
        hash = 61 * hash + Objects.hashCode(this.title);
        hash = 61 * hash + Objects.hashCode(this.author);
        hash = 61 * hash + Objects.hashCode(this.publisher);
        hash = 61 * hash + this.pages;
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Book other = (Book) obj;
        if (this.pages != other.pages) {
            return false;
        }
        if (!Objects.equals(this.isbn, other.isbn)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.publisher, other.publisher)) {
            return false;
        }
        return true;
    }
}

class Book2 {
    private Object title;

    Book2(Object title) {
        this.title = title;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public void setTitle(Object obj1, Object obj2) throws Exception {
        throw new Exception("This shouldn't be called during tests");
    }
}

public class BeanUtilsTest {

    @Test
    public void assignSameClassTest() {
        Book b1 = new Book("200", "Xenon", "Hamilton", "Harcourt", 99);
        Book b2 = new Book("500", "Boron", "Bradbury", "Prentice", 108);
        Book temp = new Book();
        assert (!b1.equals(b2));
        assert (!temp.equals(b1));
        assert (!temp.equals(b2));
        try {
            BeanUtils.assign(temp, b1);
            assert (temp.equals(b1));
            BeanUtils.assign(temp, b2);
            assert (temp.equals(b2));
        }
        catch (Exception e){
            assert (false);
        }
    }
    @Test
    public void assignDifferentClassesTest() {
        Book b1 = new Book("200", "Xenon", "Hamilton", "Harcourt", 99);
        Book b2 = new Book("500", "Boron", "Bradbury", "Prentice", 108);
        Book2 temp = new Book2("");
        assert (!b1.equals(b2));
        assert (!b1.getTitle().equals(temp.getTitle()));
        assert (!b2.getTitle().equals(temp.getTitle()));
        try {
            BeanUtils.assign(temp, b1);
            assert (b1.getTitle().equals(temp.getTitle()));
            BeanUtils.assign(b2, temp);
            assert (!b2.getTitle().equals(temp.getTitle()));
        }
        catch (Exception e){
            assert (false);
        }
    }
}