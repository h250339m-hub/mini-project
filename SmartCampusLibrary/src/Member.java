public class Member {
    private final String memberId;
    private final String name;

    public Member(String memberId, String name) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID cannot be empty.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name cannot be empty.");
        }
        this.memberId = memberId.trim();
        this.name = name.trim();
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public int getActiveLoanCount() {
        return Library.getActiveLoanCount(memberId);
    }

    @Override
    public String toString() {
        return memberId + " - " + name + " (active loans: " + getActiveLoanCount() + ")";
    }
}
