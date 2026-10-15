public class Book extends LibraryItem {
    public Book(String id, String title) {
        super(id, title);
    }

    @Override
    public int loanPeriodDays() {
        return 14;
    }

    @Override
    public String getType() {
        return "Book";
    }
}
