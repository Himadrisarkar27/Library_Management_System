import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class LibraryGUI extends JFrame {
    private final LibraryService service = new LibraryService();

    
    private final DefaultTableModel booksModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Author", "Quantity"}, 0);
    private final JTable booksTable = new JTable(booksModel);

    private final DefaultTableModel membersModel = new DefaultTableModel(new String[]{"Member ID", "Name", "Contact"}, 0);
    private final JTable membersTable = new JTable(membersModel);

    private final DefaultTableModel loansModel = new DefaultTableModel(new String[]{"ISBN", "Member ID", "Issue Date", "Due Date", "Returned"}, 0);
    private final JTable loansTable = new JTable(loansModel);

    
    public LibraryGUI() {
        super("Library Management System (Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Books", createBooksTab());
        tabs.addTab("Members", createMembersTab());
        tabs.addTab("Issue / Return", createIssueReturnTab());
        tabs.addTab("Search", createSearchTab());
        tabs.addTab("Loans", createLoansTab());

        add(tabs);

        
        refreshBooksTable();
        refreshMembersTable();
        refreshLoansTable();
    }

    
    private JPanel createBooksTab() {
        JPanel panel = new JPanel(new BorderLayout());

        
        JPanel form = new JPanel(new GridLayout(2, 5, 8, 8));
        JTextField isbnField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField qtyField = new JTextField();
        JButton addBtn = new JButton("Add / Update");

        form.add(new JLabel("ISBN:"));
        form.add(new JLabel("Title:"));
        form.add(new JLabel("Author:"));
        form.add(new JLabel("Quantity:"));
        form.add(new JLabel("")); 

        form.add(isbnField);
        form.add(titleField);
        form.add(authorField);
        form.add(qtyField);
        form.add(addBtn);

        addBtn.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            int qty = parseIntSafe(qtyField.getText().trim());
            if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
                showMsg("Please fill all fields.");
                return;
            }
            if (qty < 0) qty = 0;
            service.addBook(new Book(isbn, title, author, qty));
            showMsg("Book saved.");
            refreshBooksTable();
            
            isbnField.setText("");
            titleField.setText("");
            authorField.setText("");
            qtyField.setText("");
        });

        
        JScrollPane tableScroll = new JScrollPane(booksTable);

        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshBooksTable());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshBtn);

        panel.add(form, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshBooksTable() {
        
        clearModel(booksModel);
        List<Book> list = service.getAllBooks();
        for (Book b : list) {
            booksModel.addRow(new Object[]{b.getIsbn(), b.getTitle(), b.getAuthor(), b.getQuantity()});
        }
    }

    
    private JPanel createMembersTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JButton addBtn = new JButton("Add");

        form.add(new JLabel("Member ID:"));
        form.add(new JLabel("Name:"));
        form.add(new JLabel("Contact:"));
        form.add(new JLabel(""));

        form.add(idField);
        form.add(nameField);
        form.add(contactField);
        form.add(addBtn);

        addBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            if (id.isEmpty() || name.isEmpty() || contact.isEmpty()) {
                showMsg("Please fill all fields.");
                return;
            }
            service.addMember(new Member(id, name, contact));
            showMsg("Member saved.");
            refreshMembersTable();
            idField.setText("");
            nameField.setText("");
            contactField.setText("");
        });

        JScrollPane tableScroll = new JScrollPane(membersTable);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshMembersTable());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshBtn);

        panel.add(form, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshMembersTable() {
        clearModel(membersModel);
        List<Member> list = service.getAllMembers();
        for (Member m : list) {
            membersModel.addRow(new Object[]{m.getMemberId(), m.getName(), m.getContact()});
        }
    }

    
    private JPanel createIssueReturnTab() {
        JPanel panel = new JPanel(new GridLayout(2, 1));

       
        JPanel issue = new JPanel(new GridLayout(2, 5, 8, 8));
        JTextField isbnField = new JTextField();
        JTextField memberField = new JTextField();
        JTextField daysField = new JTextField("14"); 
        JButton issueBtn = new JButton("Issue");

        issue.add(new JLabel("ISBN:"));
        issue.add(new JLabel("Member ID:"));
        issue.add(new JLabel("Days:"));
        issue.add(new JLabel(""));
        issue.add(new JLabel(""));

        issue.add(isbnField);
        issue.add(memberField);
        issue.add(daysField);
        issue.add(issueBtn);
        issue.add(new JLabel(""));

        issueBtn.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            String member = memberField.getText().trim();
            int days = parseIntSafe(daysField.getText().trim());
            if (isbn.isEmpty() || member.isEmpty() || days <= 0) {
                showMsg("Enter valid ISBN, Member ID, and days.");
                return;
            }
            boolean ok = service.issueBook(isbn, member, days);
            if (ok) {
                showMsg("Book issued.");
                refreshLoansTable();
            } else {
                showMsg("Cannot issue. Check ISBN/member or availability.");
            }
        });

        
        JPanel ret = new JPanel(new GridLayout(2, 4, 8, 8));
        JTextField rIsbn = new JTextField();
        JTextField rMember = new JTextField();
        JButton returnBtn = new JButton("Return");

        ret.add(new JLabel("ISBN:"));
        ret.add(new JLabel("Member ID:"));
        ret.add(new JLabel(""));
        ret.add(new JLabel(""));

        ret.add(rIsbn);
        ret.add(rMember);
        ret.add(returnBtn);
        ret.add(new JLabel(""));

        returnBtn.addActionListener(e -> {
            String isbn = rIsbn.getText().trim();
            String member = rMember.getText().trim();
            if (isbn.isEmpty() || member.isEmpty()) {
                showMsg("Enter ISBN and Member ID.");
                return;
            }
            double fine = service.returnBook(isbn, member);
            if (fine == -1) {
                showMsg("Active loan not found.");
            } else if (fine > 0) {
                showMsg("Returned. Late fine: Rs." + fine);
            } else {
                showMsg("Returned. No fine.");
            }
            refreshLoansTable();
        });

        panel.add(wrapWithTitled(issue, "Issue Book"));
        panel.add(wrapWithTitled(ret, "Return Book"));
        return panel;
    }

    
    private JPanel createSearchTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField queryField = new JTextField(25);
        String[] modes = {"By Title", "By Author"};
        JComboBox<String> mode = new JComboBox<>(modes);
        JButton searchBtn = new JButton("Search");
        JButton checkBtn = new JButton("Check Availability");
        JTextField isbnAvail = new JTextField(12);

        top.add(new JLabel("Query:"));
        top.add(queryField);
        top.add(mode);
        top.add(searchBtn);
        top.add(new JLabel("ISBN:"));
        top.add(isbnAvail);
        top.add(checkBtn);

        DefaultTableModel searchModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Author", "Quantity"}, 0);
        JTable searchTable = new JTable(searchModel);

        searchBtn.addActionListener(e -> {
            searchModel.setRowCount(0);
            String q = queryField.getText().trim();
            if (q.isEmpty()) {
                showMsg("Enter a search term.");
                return;
            }
            List<Book> list;
            if (mode.getSelectedIndex() == 0) {
                list = service.searchBooksByTitle(q);
            } else {
                list = service.searchBooksByAuthor(q);
            }
            for (Book b : list) {
                searchModel.addRow(new Object[]{b.getIsbn(), b.getTitle(), b.getAuthor(), b.getQuantity()});
            }
            if (list.isEmpty()) showMsg("No books found.");
        });

        checkBtn.addActionListener(e -> {
            String isbn = isbnAvail.getText().trim();
            if (isbn.isEmpty()) {
                showMsg("Enter an ISBN to check.");
                return;
            }
            boolean available = service.isBookAvailable(isbn);
            showMsg(available ? "Available" : "Not available");
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(searchTable), BorderLayout.CENTER);
        return panel;
    }

    
    private JPanel createLoansTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane tableScroll = new JScrollPane(loansTable);
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshLoansTable());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshBtn);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshLoansTable() {
        clearModel(loansModel);
        List<Loan> list = service.getAllLoans();
        for (Loan l : list) {
            loansModel.addRow(new Object[]{l.getIsbn(), l.getMemberId(), l.getIssueDate(), l.getDueDate(), l.isReturned()});
        }
    }

    
    private static void clearModel(DefaultTableModel model) {
        model.setRowCount(0);
    }

    private static JPanel wrapWithTitled(JComponent comp, String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return -1; }
    }

    private static void showMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
    }
}



