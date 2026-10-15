import java.time.LocalDate;

public class Loan {
    private final String itemId;
    private final String memberId;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(String itemId, String memberId, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        if (itemId == null || itemId.isBlank() || memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Item ID and member ID are required.");
        }
        if (borrowDate == null || dueDate == null || dueDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException("Invalid loan dates.");
        }
        this.itemId = itemId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public String getItemId() {
        return itemId;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isActive() {
        return returnDate == null;
    }

    public boolean isOverdue() {
        return isActive() && LocalDate.now().isAfter(dueDate);
    }

    public void markReturned() {
        if (isActive()) {
            returnDate = LocalDate.now();
        }
    }

    public String toCsv() {
        return itemId + "," + memberId + "," + borrowDate + "," + dueDate + ","
                + (returnDate == null ? "" : returnDate);
    }

    @Override
    public String toString() {
        return "Loan [item=" + itemId + ", member=" + memberId + ", borrowed=" + borrowDate
                + ", due=" + dueDate + ", returned=" + (returnDate == null ? "No" : returnDate) + "]";
    }
}
