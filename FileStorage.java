import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * FileStorage is a tiny helper that reads/writes plain text files line-by-line.
 * We keep it simple: each object knows how to convert itself to/from a line.
 */
public class FileStorage {
    // Paths for our data files (you can change them if you want)
    public static final String BOOKS_FILE = "books.txt";
    public static final String MEMBERS_FILE = "members.txt";
    public static final String LOANS_FILE = "loans.txt";

    // --------------- Book ---------------
    public static void saveBooks(List<Book> books) {
        writeLines(BOOKS_FILE, toBookLines(books));
    }

    public static List<Book> loadBooks() {
        List<String> lines = readLines(BOOKS_FILE);
        List<Book> books = new ArrayList<>();
        for (String line : lines) {
            Book b = Book.fromFileString(line);
            if (b != null) books.add(b);
        }
        return books;
    }

    // --------------- Member ---------------
    public static void saveMembers(List<Member> members) {
        writeLines(MEMBERS_FILE, toMemberLines(members));
    }

    public static List<Member> loadMembers() {
        List<String> lines = readLines(MEMBERS_FILE);
        List<Member> members = new ArrayList<>();
        for (String line : lines) {
            Member m = Member.fromFileString(line);
            if (m != null) members.add(m);
        }
        return members;
    }

    // --------------- Loan ---------------
    public static void saveLoans(List<Loan> loans) {
        writeLines(LOANS_FILE, toLoanLines(loans));
    }

    public static List<Loan> loadLoans() {
        List<String> lines = readLines(LOANS_FILE);
        List<Loan> loans = new ArrayList<>();
        for (String line : lines) {
            Loan l = Loan.fromFileString(line);
            if (l != null) loans.add(l);
        }
        return loans;
    }

    // --------------- Helpers ---------------
    private static List<String> toBookLines(List<Book> books) {
        List<String> list = new ArrayList<>();
        for (Book b : books) list.add(b.toFileString());
        return list;
    }

    private static List<String> toMemberLines(List<Member> members) {
        List<String> list = new ArrayList<>();
        for (Member m : members) list.add(m.toFileString());
        return list;
    }

    private static List<String> toLoanLines(List<Loan> loans) {
        List<String> list = new ArrayList<>();
        for (Loan l : loans) list.add(l.toFileString());
        return list;
    }

    private static List<String> readLines(String path) {
        List<String> lines = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return lines; // empty if file not found
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + path + " -> " + e.getMessage());
        }
        return lines;
    }

    private static void writeLines(String path, List<String> lines) {
        File file = new File(path);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + path + " -> " + e.getMessage());
        }
    }
}


