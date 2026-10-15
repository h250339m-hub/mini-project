# Smart Campus Library System

A console-based Java OOP mini-project for managing books, DVDs, members and loans.

## Requirements
- Java 17 or later
- No external libraries

## Run in VS Code
1. Extract the ZIP file.
2. Open the `SmartCampusLibrary` folder in VS Code.
3. Open the terminal in VS Code.
4. Compile the source files:

   ```bash
   javac -d out src/*.java
   ```

5. Run the application:

   ```bash
   java -cp out Main
   ```

The application creates and uses the `data` folder for CSV persistence. Use option `0` to save and exit.

## Features
- Add books and DVDs
- Register members
- Borrow and return items
- Enforce a maximum of three active loans per member
- List overdue loans
- Search titles using Java Streams
- Save and reload items, members and loan records using CSV files

## Loan periods
- Book: 14 days
- DVD: 7 days

These are sample rules defined for this project.
