# Smart Campus Library System — Mini-Project Report

## 1. Introduction
The Smart Campus Library System is a console-based Java application designed to manage library items, registered members and borrowing activity. It supports books and DVDs and stores records in CSV files so data can be reloaded when the application starts again.

## 2. Objectives
- Apply object-oriented programming principles in Java.
- Manage library items and members using maps keyed by unique IDs.
- Enforce item availability and a maximum of three active loans per member.
- Identify overdue loans and search titles using Java Streams.
- Persist items, members and loan records using CSV files.

## 3. Design
`LibraryItem` is an abstract superclass containing common item information and borrowing behaviour. `Book` and `Dvd` inherit from it and implement their own loan periods. `Borrowable` defines the borrowing and return operations. `Member` stores member details, while `Loan` records the item, member, borrowing date, due date and return date. `Library` manages collections and business operations. `Main` provides the menu-driven user interface.

## 4. Object-Oriented Concepts
- **Encapsulation:** Fields are private and accessed through methods.
- **Abstraction:** `LibraryItem` defines shared features and the abstract `loanPeriodDays()` method.
- **Inheritance:** `Book` and `Dvd` extend `LibraryItem`.
- **Polymorphism:** Both item types are managed through `LibraryItem` references and provide different loan periods.
- **Interface:** `Borrowable` specifies borrowing and return operations.
- **Exception handling:** Custom exceptions report unavailable items and member loan-limit violations.
- **Collections and streams:** Maps store items and members; streams filter title searches and overdue loans.
- **File handling:** CSV files persist items, members and loans.

## 5. Validation and Business Rules
Item IDs and member IDs must be non-empty and unique. Titles and member names must not be blank. A member may have at most three active loans. An item already on loan cannot be borrowed again. Books have a 14-day loan period and DVDs have a 7-day loan period. An active loan is overdue when the current date is later than its due date.

## 6. Testing
| Test | Action | Expected result |
|---|---|---|
| Add item | Add an item with a new ID | Item is saved in the in-memory collection |
| Duplicate item | Add another item with the same ID | Validation error is displayed |
| Register member | Enter a unique ID and name | Member is registered |
| Borrow available item | Borrow an available item for a registered member | Loan is created with the correct due date |
| Borrow unavailable item | Attempt to borrow an item already on loan | `ItemNotAvailableException` message is displayed |
| Exceed loan limit | Attempt a fourth active loan for one member | `MemberLimitExceededException` message is displayed |
| Return item | Return an active loan using its item and member IDs | Loan is marked returned and item becomes available |
| Search title | Search using part of a title | Matching items are displayed |
| Restart application | Save, exit and reopen | Saved items, members and loans are loaded |

## 7. Limitations
This mini-project uses a console interface and simple CSV storage. Commas in entered fields are replaced with spaces to keep the CSV format simple. It does not include user accounts, fines, network access or a database.

## 8. Conclusion
The project demonstrates the required Java OOP concepts in a small, practical application. Its menu, validation, borrowing rules, stream-based search and CSV persistence provide a foundation that could be extended in a future project.
