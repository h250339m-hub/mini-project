import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library {
    private static final Path DATA_DIR = Path.of("data");
    private static final Path ITEMS_FILE = DATA_DIR.resolve("items.csv");
    private static final Path MEMBERS_FILE = DATA_DIR.resolve("members.csv");
    private static final Path LOANS_FILE = DATA_DIR.resolve("loans.csv");

    private final Map<String, LibraryItem> items = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();
    private final List<Loan> loans = new ArrayList<>();

    public Map<String, LibraryItem> getItems() {
        return items;
    }

    public Map<String, Member> getMembers() {
        return members;
    }

    public void addItem(LibraryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is required.");
        }
        if (items.containsKey(item.getId())) {
            throw new IllegalArgumentException("An item with this ID already exists.");
        }
        items.put(item.getId(), item);
    }

    public void registerMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member is required.");
        }
        if (members.containsKey(member.getMemberId())) {
            throw new IllegalArgumentException("A member with this ID already exists.");
        }
        members.put(member.getMemberId(), member);
    }

    public void borrowItem(String itemId, String memberId)
            throws ItemNotAvailableException, MemberLimitExceededException {
        LibraryItem item = items.get(itemId);
        Member member = members.get(memberId);

        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member not found.");
        }

        item.borrow(member);
        LocalDate today = LocalDate.now();
        loans.add(new Loan(itemId, memberId, today, today.plusDays(item.loanPeriodDays()), null));
    }

    public void returnItem(String itemId, String memberId) {
        Loan activeLoan = loans.stream()
                .filter(Loan::isActive)
                .filter(loan -> loan.getItemId().equals(itemId))
                .filter(loan -> loan.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active loan matches those IDs."));

        activeLoan.markReturned();
        LibraryItem item = items.get(itemId);
        if (item != null) {
            item.giveBack();
        }
    }

    public List<Loan> getOverdueLoans() {
        return loans.stream().filter(Loan::isOverdue).collect(Collectors.toList());
    }

    public List<LibraryItem> searchByTitle(String searchTerm) {
        String term = searchTerm == null ? "" : searchTerm.trim().toLowerCase();
        return items.values().stream()
                .filter(item -> item.getTitle().toLowerCase().contains(term))
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public static int getActiveLoanCount(String memberId) {
        Library library = Main.getLibraryInstance();
        if (library == null) {
            return 0;
        }
        return (int) library.loans.stream()
                .filter(Loan::isActive)
                .filter(loan -> loan.getMemberId().equals(memberId))
                .count();
    }

    public void saveData() throws IOException {
        Files.createDirectories(DATA_DIR);
        List<String> itemLines = new ArrayList<>();
        for (LibraryItem item : items.values()) {
            itemLines.add(item.getType() + "," + csvSafe(item.getId()) + "," + csvSafe(item.getTitle()));
        }
        Files.write(ITEMS_FILE, itemLines);

        List<String> memberLines = members.values().stream()
                .map(member -> csvSafe(member.getMemberId()) + "," + csvSafe(member.getName()))
                .collect(Collectors.toList());
        Files.write(MEMBERS_FILE, memberLines);

        List<String> loanLines = loans.stream().map(Loan::toCsv).collect(Collectors.toList());
        Files.write(LOANS_FILE, loanLines);
    }

    public void loadData() throws IOException {
        Files.createDirectories(DATA_DIR);

        if (Files.exists(ITEMS_FILE)) {
            try (Stream<String> lines = Files.lines(ITEMS_FILE)) {
                lines.filter(line -> !line.isBlank()).forEach(line -> {
                    String[] parts = line.split(",", 3);
                    if (parts.length == 3) {
                        try {
                            LibraryItem item = parts[0].equalsIgnoreCase("DVD")
                                    ? new Dvd(parts[1], parts[2]) : new Book(parts[1], parts[2]);
                            items.put(item.getId(), item);
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                });
            }
        }

        if (Files.exists(MEMBERS_FILE)) {
            try (Stream<String> lines = Files.lines(MEMBERS_FILE)) {
                lines.filter(line -> !line.isBlank()).forEach(line -> {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        try {
                            Member member = new Member(parts[0], parts[1]);
                            members.put(member.getMemberId(), member);
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                });
            }
        }

        if (Files.exists(LOANS_FILE)) {
            try (Stream<String> lines = Files.lines(LOANS_FILE)) {
                lines.filter(line -> !line.isBlank()).forEach(line -> {
                    String[] parts = line.split(",", -1);
                    if (parts.length == 5 && items.containsKey(parts[0]) && members.containsKey(parts[1])) {
                        try {
                            LocalDate returned = parts[4].isBlank() ? null : LocalDate.parse(parts[4]);
                            Loan loan = new Loan(parts[0], parts[1], LocalDate.parse(parts[2]),
                                    LocalDate.parse(parts[3]), returned);
                            loans.add(loan);
                            if (loan.isActive()) {
                                items.get(parts[0]).borrow(members.get(parts[1]));
                            }
                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        }
    }

    private String csvSafe(String value) {
        return value.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }
}
