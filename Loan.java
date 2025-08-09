import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Loan {
    private String isbn;           
    private String memberId;       
    private LocalDate issueDate;   
    private LocalDate dueDate;     
    private boolean returned;      

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE; 

    public Loan(String isbn, String memberId, LocalDate issueDate, LocalDate dueDate, boolean returned) {
        this.isbn = isbn;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returned = returned;
    }

    public String getIsbn() { return isbn; }
    public String getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }

    public String toFileString() {
        return escape(isbn) + "|" + escape(memberId) + "|" + issueDate.format(FMT) + "|" + dueDate.format(FMT) + "|" + returned;
    }

    public static Loan fromFileString(String line) {
        if (line == null) return null;
        String[] parts = line.split("\\|", -1);
        if (parts.length < 5) return null;
        String isbn = unescape(parts[0]);
        String memberId = unescape(parts[1]);
        LocalDate issue = LocalDate.parse(parts[2], FMT);
        LocalDate due = LocalDate.parse(parts[3], FMT);
        boolean ret = Boolean.parseBoolean(parts[4]);
        return new Loan(isbn, memberId, issue, due, ret);
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
        return "ISBN: " + isbn + ", Member: " + memberId + ", Issue: " + issueDate + ", Due: " + dueDate + ", Returned: " + returned;
    }
}



