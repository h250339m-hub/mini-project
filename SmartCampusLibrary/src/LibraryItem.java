public abstract class LibraryItem implements Borrowable {
    private final String id;
    private final String title;
    private boolean available = true;

    protected LibraryItem(String id, String title) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Item ID cannot be empty.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.id = id.trim();
        this.title = title.trim();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return available;
    }

    protected void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract int loanPeriodDays();

    @Override
    public void borrow(Member member) throws ItemNotAvailableException, MemberLimitExceededException {
        if (!available) {
            throw new ItemNotAvailableException("Item is already on loan.");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member is required.");
        }
        if (member.getActiveLoanCount() >= 3) {
            throw new MemberLimitExceededException("A member cannot have more than 3 active loans.");
        }
        available = false;
    }

    @Override
    public void giveBack() {
        available = true;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return getType() + " [ID=" + id + ", title=" + title + ", available=" + available + "]";
    }
}
