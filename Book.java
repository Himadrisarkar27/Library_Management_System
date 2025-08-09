import java.util.Objects;


public class Book {
    private String isbn;       
    private String title;      
    private String author;     
    private int quantity;      

    public Book(String isbn, String title, String author, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
    public String toFileString() {
        
        return escape(isbn) + "|" + escape(title) + "|" + escape(author) + "|" + quantity;
    }

  
    public static Book fromFileString(String line) {
        if (line == null) return null;
        String[] parts = line.split("\\|", -1); 
        if (parts.length < 4) return null;
        String isbn = unescape(parts[0]);
        String title = unescape(parts[1]);
        String author = unescape(parts[2]);
        int quantity = 0;
        try {
            quantity = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            quantity = 0;
        }
        return new Book(isbn, title, author, quantity);
    }

    
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("|", "\\|");
    }

    private static String unescape(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder();
        boolean escaping = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (escaping) {
                out.append(c);
                escaping = false;
            } else if (c == '\\') {
                escaping = true;
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn + ", Title: " + title + ", Author: " + author + ", Quantity: " + quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}



