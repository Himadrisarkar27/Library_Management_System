/**
 * Very simple Member class to hold member information.
 */
public class Member {
    private String memberId;   // unique ID for member
    private String name;       // member name
    private String contact;    // contact info (phone/email)

    public Member(String memberId, String name, String contact) {
        this.memberId = memberId;
        this.name = name;
        this.contact = contact;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String toFileString() {
        // Order: id|name|contact
        return escape(memberId) + "|" + escape(name) + "|" + escape(contact);
    }

    public static Member fromFileString(String line) {
        if (line == null) return null;
        String[] parts = line.split("\\|", -1);
        if (parts.length < 3) return null;
        String id = unescape(parts[0]);
        String name = unescape(parts[1]);
        String contact = unescape(parts[2]);
        return new Member(id, name, contact);
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
        return "ID: " + memberId + ", Name: " + name + ", Contact: " + contact;
    }
}


