# Class Diagram

```text
                 <<abstract>>
                  LibraryItem
          ---------------------------
          - id: String
          - title: String
          - available: boolean
          ---------------------------
          + loanPeriodDays(): int
          + borrow(Member): void
          + giveBack(): void
                    ^
                    |
             +------+------+
             |             |
           Book           Dvd
       loanPeriodDays  loanPeriodDays

Borrowable <<interface>>
+ borrow(Member): void
+ giveBack(): void

Member
-----------------------
- memberId: String
- name: String
+ getActiveLoanCount(): int
+ toString(): String

Loan
-----------------------
- itemId: String
- memberId: String
- borrowDate: LocalDate
- dueDate: LocalDate
- returnDate: LocalDate
+ isActive(): boolean
+ isOverdue(): boolean
+ markReturned(): void

Library
-----------------------
- items: Map<String, LibraryItem>
- members: Map<String, Member>
- loans: List<Loan>
+ addItem(LibraryItem): void
+ registerMember(Member): void
+ borrowItem(String, String): void
+ returnItem(String, String): void
+ getOverdueLoans(): List<Loan>
+ searchByTitle(String): List<LibraryItem>
+ saveData(): void
+ loadData(): void

Main
-----------------------
+ main(String[]): void
```

`Book` and `Dvd` extend `LibraryItem`. `LibraryItem` implements `Borrowable`. `Library` manages items, members and loan records. Each `Loan` refers to an item ID and member ID.
