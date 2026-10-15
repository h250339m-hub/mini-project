import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Library library;

    public static Library getLibraryInstance() {
        return library;
    }

    public static void main(String[] args) {
        library = new Library();
        try {
            library.loadData();
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                showMenu();
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" -> addItem(scanner, false);
                        case "2" -> addItem(scanner, true);
                        case "3" -> registerMember(scanner);
                        case "4" -> borrowItem(scanner);
                        case "5" -> returnItem(scanner);
                        case "6" -> listOverdue();
                        case "7" -> searchByTitle(scanner);
                        case "8" -> listItems();
                        case "9" -> listMembers();
                        case "0" -> {
                            library.saveData();
                            System.out.println("Data saved. Goodbye.");
                            running = false;
                        }
                        default -> System.out.println("Please choose a valid menu option.");
                    }
                } catch (ItemNotAvailableException | MemberLimitExceededException e) {
                    System.out.println("Borrowing failed: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("File error: " + e.getMessage());
                }
                System.out.println();
            }
        }
    }

    private static void showMenu() {
        System.out.println("========================================");
        System.out.println("       SMART CAMPUS LIBRARY SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Add Book");
        System.out.println("2. Add DVD");
        System.out.println("3. Register Member");
        System.out.println("4. Borrow Item");
        System.out.println("5. Return Item");
        System.out.println("6. List Overdue Items");
        System.out.println("7. Search Items by Title");
        System.out.println("8. Display All Items");
        System.out.println("9. Display All Members");
        System.out.println("0. Save and Exit");
    }

    private static void addItem(Scanner scanner, boolean dvd) {
        System.out.print("Item ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        library.addItem(dvd ? new Dvd(id, title) : new Book(id, title));
        System.out.println((dvd ? "DVD" : "Book") + " added successfully.");
    }

    private static void registerMember(Scanner scanner) {
        System.out.print("Member ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Member name: ");
        String name = scanner.nextLine().trim();
        library.registerMember(new Member(id, name));
        System.out.println("Member registered successfully.");
    }

    private static void borrowItem(Scanner scanner)
            throws ItemNotAvailableException, MemberLimitExceededException {
        System.out.print("Item ID: ");
        String itemId = scanner.nextLine().trim();
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();
        library.borrowItem(itemId, memberId);
        System.out.println("Item borrowed successfully.");
    }

    private static void returnItem(Scanner scanner) {
        System.out.print("Item ID: ");
        String itemId = scanner.nextLine().trim();
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();
        library.returnItem(itemId, memberId);
        System.out.println("Item returned successfully.");
    }

    private static void listOverdue() {
        List<Loan> overdue = library.getOverdueLoans();
        if (overdue.isEmpty()) {
            System.out.println("There are no overdue items.");
            return;
        }
        overdue.forEach(loan -> {
            LibraryItem item = library.getItems().get(loan.getItemId());
            Member member = library.getMembers().get(loan.getMemberId());
            System.out.println(item.getTitle() + " | " + member.getName() + " | Due: " + loan.getDueDate());
        });
    }

    private static void searchByTitle(Scanner scanner) {
        System.out.print("Enter title or keyword: ");
        String term = scanner.nextLine();
        List<LibraryItem> results = library.searchByTitle(term);
        if (results.isEmpty()) {
            System.out.println("No matching items found.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private static void listItems() {
        if (library.getItems().isEmpty()) {
            System.out.println("No items registered.");
        } else {
            library.getItems().values().stream()
                    .sorted((a, b) -> a.getId().compareToIgnoreCase(b.getId()))
                    .forEach(item -> System.out.println(item + " | Loan period: " + item.loanPeriodDays() + " days"));
        }
    }

    private static void listMembers() {
        if (library.getMembers().isEmpty()) {
            System.out.println("No members registered.");
        } else {
            library.getMembers().values().stream()
                    .sorted((a, b) -> a.getMemberId().compareToIgnoreCase(b.getMemberId()))
                    .forEach(System.out::println);
        }
    }
}
