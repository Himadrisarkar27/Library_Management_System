import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * LibraryService holds data in memory using ArrayList and provides simple methods.
 * Data is loaded from/saved to text files using FileStorage.
 */
public class LibraryService {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();

    // Fine per day for late returns (you can tweak this)
    private static final double FINE_PER_DAY = 2.0; // simple flat rate

    public LibraryService() {
        // Load saved data when the app starts
        books = FileStorage.loadBooks();
        members = FileStorage.loadMembers();
        loans = FileStorage.loadLoans();
    }

    // ----------------- Book methods -----------------
    public void addBook(Book book) {
        // If ISBN exists, just increase quantity
        Book existing = findBookByIsbn(book.getIsbn());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + book.getQuantity());
        } else {
            books.add(book);
        }
        FileStorage.saveBooks(books);
    }

    public Book findBookByIsbn(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) return b;
        }
        return null;
    }

    public List<Book> searchBooksByTitle(String titlePart) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(titlePart.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> searchBooksByAuthor(String authorPart) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getAuthor().toLowerCase().contains(authorPart.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> getAllBooks() {
        return books;
    }

    // ----------------- Member methods -----------------
    public void addMember(Member member) {
        // Simple: do not add duplicate IDs
        if (findMemberById(member.getMemberId()) == null) {
            members.add(member);
            FileStorage.saveMembers(members);
        }
    }

    public Member findMemberById(String id) {
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(id)) return m;
        }
        return null;
    }

    public List<Member> getAllMembers() {
        return members;
    }

    // ----------------- Loan methods -----------------
    public boolean isBookAvailable(String isbn) {
        Book book = findBookByIsbn(isbn);
        if (book == null) return false;

        // Count how many copies are currently loaned out (not returned)
        int borrowedCount = 0;
        for (Loan l : loans) {
            if (!l.isReturned() && l.getIsbn().equalsIgnoreCase(isbn)) {
                borrowedCount++;
            }
        }
        int available = book.getQuantity() - borrowedCount;
        return available > 0;
    }

    public boolean issueBook(String isbn, String memberId, int days) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);
        if (book == null || member == null) return false;
        if (!isBookAvailable(isbn)) return false;

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(days);
        Loan loan = new Loan(isbn, memberId, issueDate, dueDate, false);
        loans.add(loan);
        FileStorage.saveLoans(loans);
        return true;
    }

    public double returnBook(String isbn, String memberId) {
        // Find the active (not returned) loan for this book and member
        for (Loan l : loans) {
            if (!l.isReturned() && l.getIsbn().equalsIgnoreCase(isbn) && l.getMemberId().equalsIgnoreCase(memberId)) {
                l.setReturned(true);
                FileStorage.saveLoans(loans);
                // Calculate fine if returned after due date
                LocalDate today = LocalDate.now();
                if (today.isAfter(l.getDueDate())) {
                    long lateDays = ChronoUnit.DAYS.between(l.getDueDate(), today);
                    return lateDays * FINE_PER_DAY;
                } else {
                    return 0.0;
                }
            }
        }
        return -1; // not found
    }

    public List<Loan> getAllLoans() { return loans; }
}


