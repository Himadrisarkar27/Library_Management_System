import java.util.List;
import java.util.Scanner;


public class LibraryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LibraryService service = new LibraryService();

    public static void main(String[] args) {
       
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1: addBookFlow(); break;
                case 2: addMemberFlow(); break;
                case 3: issueBookFlow(); break;
                case 4: returnBookFlow(); break;
                case 5: searchBookFlow(); break;
                case 6: checkAvailabilityFlow(); break;
                case 7: listAllBooks(); break;
                case 8: listAllMembers(); break;
                case 9: listAllLoans(); break;
                case 0: System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid option. Try again.");
            }
            System.out.println(); 
        }
    }

    private static void printMenu() {
        System.out.println("==============================");
        System.out.println(" Library Management System ");
        System.out.println("==============================");
        System.out.println("1. Add Book");
        System.out.println("2. Add Member");
        System.out.println("3. Issue Book");
        System.out.println("4. Return Book");
        System.out.println("5. Search Books (title/author)");
        System.out.println("6. Check Book Availability");
        System.out.println("7. Display All Books");
        System.out.println("8. Display All Members");
        System.out.println("9. Display All Loans");
        System.out.println("0. Exit");
    }

    
    private static int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                String line = scanner.nextLine();
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private static String readNonEmpty(String msg) {
        while (true) {
            System.out.print(msg);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Value cannot be empty.");
        }
    }

    
    private static void addBookFlow() {
        
        String isbn = readNonEmpty("Enter ISBN: ");
        String title = readNonEmpty("Enter Title: ");
        String author = readNonEmpty("Enter Author: ");
        int qty = readInt("Enter Quantity: ");
        if (qty < 0) qty = 0;
     
        Book b = new Book(isbn, title, author, qty);
        service.addBook(b);
        System.out.println("Book added/updated successfully.");
    }

    private static void addMemberFlow() {
        String id = readNonEmpty("Enter Member ID: ");
        String name = readNonEmpty("Enter Name: ");
        String contact = readNonEmpty("Enter Contact Info: ");
        Member m = new Member(id, name, contact);
        service.addMember(m);
        System.out.println("Member added successfully.");
    }

    private static void issueBookFlow() {
        String isbn = readNonEmpty("Enter ISBN: ");
        String memberId = readNonEmpty("Enter Member ID: ");
        int days = readInt("Enter number of days (loan period): ");
        boolean ok = service.issueBook(isbn, memberId, days);
        if (ok) System.out.println("Book issued successfully.");
        else System.out.println("Cannot issue. Check ISBN/member or availability.");
    }

    private static void returnBookFlow() {
        String isbn = readNonEmpty("Enter ISBN: ");
        String memberId = readNonEmpty("Enter Member ID: ");
        double fine = service.returnBook(isbn, memberId);
        if (fine == -1) {
            System.out.println("Active loan not found for this book and member.");
        } else if (fine > 0) {
            System.out.println("Book returned. Late fine: Rs." + fine);
        } else {
            System.out.println("Book returned. No fine.");
        }
    }

    private static void searchBookFlow() {
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        int c = readInt("Choose: ");
        if (c == 1) {
            String t = readNonEmpty("Enter title keyword: ");
            List<Book> list = service.searchBooksByTitle(t);
            if (list.isEmpty()) System.out.println("No books found.");
            for (Book b : list) System.out.println(b);
        } else if (c == 2) {
            String a = readNonEmpty("Enter author keyword: ");
            List<Book> list = service.searchBooksByAuthor(a);
            if (list.isEmpty()) System.out.println("No books found.");
            for (Book b : list) System.out.println(b);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void checkAvailabilityFlow() {
        String isbn = readNonEmpty("Enter ISBN: ");
        boolean available = service.isBookAvailable(isbn);
        System.out.println(available ? "Available" : "Not available");
    }

    private static void listAllBooks() {
        List<Book> list = service.getAllBooks();
        if (list.isEmpty()) System.out.println("No books added yet.");
        for (Book b : list) System.out.println(b);
    }

    private static void listAllMembers() {
        List<Member> list = service.getAllMembers();
        if (list.isEmpty()) System.out.println("No members added yet.");
        for (Member m : list) System.out.println(m);
    }

    private static void listAllLoans() {
        List<Loan> list = service.getAllLoans();
        if (list.isEmpty()) System.out.println("No loans yet.");
        for (Loan l : list) System.out.println(l);
    }
}



