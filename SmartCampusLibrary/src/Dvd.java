public class Dvd extends LibraryItem {
    public Dvd(String id, String title) {
        super(id, title);
    }

    @Override
    public int loanPeriodDays() {
        return 7;
    }

    @Override
    public String getType() {
        return "DVD";
    }
}
