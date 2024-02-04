import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Author[] authors = new Author[20];


        authors[0] = new Author("John Doe", "1995-2012");
        authors[1] = new Author("Jane Smith", "1968-1998");
        authors[2] = new Author("Michael Johnson", "1975-");
        authors[3] = new Author("Emily Davis", "1982-");
        authors[4] = new Author("Robert Wilson", "1950-2016");


        Book[] books = new Book[100];


        books[0] = new Book("Title 1", authors[0], LocalDate.now(), true);
        books[1] = new Book("Title 2", authors[1], LocalDate.now(), true);
        books[2] = new Book("Title 3", authors[4], LocalDate.now(), true);
        books[3] = new Book("Title 4", authors[2], LocalDate.now(), true);
        books[4] = new Book("Title 5", authors[4], LocalDate.now(), true);


        System.out.println("=============== SET UP LIBRARY ===============");
        System.out.print("Enter Library's Name: ");
        String libraryName = scanner.nextLine().toUpperCase();
        System.out.print("Enter Library's Address: ");
        String libraryAddress = scanner.nextLine();


        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String formattedDate = dateFormat.format(new Date());
        System.out.println("\"" + libraryName + "\" Library is already created in \"" +
                libraryAddress + "\" address" + " " + "successfully on " + formattedDate);

        int booksPerPage = 3;
        int option = 0;
        do {
            System.out.println("======== "+ libraryName + ", " + libraryAddress + " ========");
            System.out.println("1- Add Book");
            System.out.println("2- Show All Books");
            System.out.println("3- Show Available Books");
            System.out.println("4- Set Row to Display Book");
            System.out.println("5- Borrow Book");
            System.out.println("6- Return Book");
            System.out.println("7- Remove Book");
            System.out.println("8- Exit");

            option = getValidInt("Choose Option(1-6): ", "\\d+", scanner);


            switch (option) {
                case 1 :
                    addBook(books, authors, scanner);
                    break;

                case 2 :

                    displayAllBooks(books, booksPerPage, scanner);
                    break;


                case 3 :
                    displayAvailableBooks(books);
                    break;
                case 4:
                    booksPerPage = getValidInt("Enter Record You want to display each pages: ", "\\d+", scanner);
                    displayAllBooks(books, booksPerPage, scanner);
                case 5:
                    borrowBook(books, scanner);
                    break;

                case 6 :
                    returnBook(books, scanner);
                    break;

                case 7 :
                    deleteBook(books, scanner);
                    break;

                case 8 :
                    System.out.println("-^- Good Bye -^-");
                    System.exit(0);
                    break;


                default : System.out.println("Wrong option");
            }
        } while (option != 9);

    }

    private static void displayAvailableBooks(Book[] books) {
        System.out.println("\nAvailable Books:");
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table tblAvailableBooks = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        tblAvailableBooks.setColumnWidth(0, 5, 10);
        tblAvailableBooks.setColumnWidth(1, 20, 30);
        tblAvailableBooks.setColumnWidth(2, 30, 40);

        tblAvailableBooks.addCell("\u001B[34mID\u001B[0m", cellStyle);
        tblAvailableBooks.addCell("\u001B[32mTITLE\u001B[0m", cellStyle);
        tblAvailableBooks.addCell("\u001B[33mAUTHOR\u001B[0m", cellStyle);

        for (Book book : books) {
            if (book != null && book.getStatus() && !book.isRemoved()) {
                tblAvailableBooks.addCell("\u001B[34m" + Integer.toString(book.getId()) + "\u001B[0m", cellStyle);
                tblAvailableBooks.addCell("\u001B[32m" + book.getTitle() + "\u001B[0m", cellStyle);
                tblAvailableBooks.addCell("\u001B[33m" + book.getAuthor().getName() + " " +
                        "(" + book.getAuthor().getActiveYear() + ")" + "\u001B[0m", cellStyle);
            }
        }

        System.out.println(tblAvailableBooks.render());
    }


    private static void displayAllBooks(Book[] books, int booksPerPage, Scanner scanner) {
        int totalPages = (int) Math.ceil((double) countAvailableBooks(books) / booksPerPage);


        int currentPage = 1;
        boolean exit = false;

        while (!exit) {
            System.out.println("All Books (Page " + currentPage + "/" + totalPages + "):");
            CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
            Table tblAllBooks = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

            tblAllBooks.setColumnWidth(0, 5, 10);
            tblAllBooks.setColumnWidth(1, 20, 30);
            tblAllBooks.setColumnWidth(2, 30, 40);
            tblAllBooks.setColumnWidth(3, 15, 20);
            tblAllBooks.setColumnWidth(4, 15, 20);

            tblAllBooks.addCell("\u001B[34mID\u001B[0m", cellStyle);
            tblAllBooks.addCell("\u001B[32mTITLE\u001B[0m", cellStyle);
            tblAllBooks.addCell("\u001B[33mAUTHOR\u001B[0m", cellStyle);
            tblAllBooks.addCell("\u001B[35mPUBLISH DATE\u001B[0m", cellStyle);
            tblAllBooks.addCell("\u001B[36mSTATUS\u001B[0m", cellStyle);

            int startIndex = (currentPage - 1) * booksPerPage;
            int endIndex = Math.min(startIndex + booksPerPage, countAvailableBooks(books));

            for (int i = startIndex; i < endIndex; i++) {
                Book book = books[i];
                if (book != null) {
                    tblAllBooks.addCell((book.isRemoved()) ? "\u001B[31mRemoved\u001B[0m" : "\u001B[34m" + Integer.toString(book.getId()) + "\u001B[0m", cellStyle);
                    tblAllBooks.addCell((book.isRemoved()) ? "\u001B[31mRemoved\u001B[0m" : "\u001B[32m" + book.getTitle() + "\u001B[0m", cellStyle);
                    tblAllBooks.addCell((book.isRemoved()) ? "\u001B[31mRemoved\u001B[0m" : "\u001B[33m" + book.getAuthor().getName() + " " +
                            "(" + book.getAuthor().getActiveYear() + ")" + "\u001B[0m", cellStyle);
                    tblAllBooks.addCell((book.isRemoved()) ? "\u001B[31mRemoved\u001B[0m" :
                            "\u001B[35m" + book.getPublishedYear().toString() + "\u001B[0m", cellStyle);
                    tblAllBooks.addCell((book.isRemoved()) ? "Removed" : (book.getStatus() ?
                            "\u001B[34mAvailable\u001B[0m" : "\u001B[31mUnavailable\u001B[0m"), cellStyle);
                }
            }

            System.out.println(tblAllBooks.render());

            System.out.print("Enter 'n' for next page, 'p' for previous page, or 'q' to quit: ");
            scanner.nextLine();
            String choice = scanner.nextLine();

            switch (choice.toLowerCase()) {
                case "n":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println("You're already on the last page.");
                    }
                    break;
                case "p":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("You're already on the first page.");
                    }
                    break;
                case "q":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static int countAvailableBooks(Book[] books) {
        int count = 0;
        for (Book book : books) {
            if (book != null && book.getStatus() && !book.isRemoved()) {
                count++;
            }
        }
        return count;
    }


    private static void returnBook(Book[] books,Scanner scanner) {
        String title = getValidString("Enter Book Title to Return: ", "[A-Za-z]+", scanner);

        for (int i = 0; i < books.length; i++) {
            if (books[i] != null && books[i].getTitle().equalsIgnoreCase(title)) {
                if (!books[i].getStatus()) {
                    books[i].setStatus(true);
                    System.out.println("Book returned successfully!");
                } else {
                    System.out.println("This book is already available. No need to return.");
                }
                return;
            }
        }
        System.out.println("Book not found.");
    }


    private static void borrowBook(Book[] books,Scanner scanner) {
        String title = getValidString("Enter Book Title to Borrow: ", "[A-Za-z]+", scanner);

        for (Book book : books) {
            if (book != null && book.getTitle().equalsIgnoreCase(title) && book.getStatus()) {
                book.setStatus(false);
                System.out.println("Book borrowed successfully!");
                return;
            }
        }
        System.out.println("Book not found or not available for borrowing.");
    }

    public static void deleteBook(Book[] books, Scanner scanner) {
        String title = getValidString("Enter Book Title to Remove: ", "[A-Za-z]+", scanner);

        for (int i = 0; i < books.length; i++) {
            if (books[i] != null && books[i].getTitle().equalsIgnoreCase(title)) {
                books[i].setRemoved(true);  // Set the removed flag
                System.out.println("Book removed successfully!");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    private static void addBook(Book[] books, Author[] authors, Scanner scanner) {
        scanner.nextLine();
        String title = getValidString("Enter Book Title: ", "[A-Za-z]+", scanner);


        String authorName;
        Pattern pattern = Pattern.compile("[A-Za-z\\s]+");
        Matcher matcher;
        do {
            System.out.print("Enter Author Name: ");
            authorName = scanner.nextLine();
            matcher = pattern.matcher(authorName);
            if (!matcher.matches()) {
                System.out.println("Invalid author name. Please enter a valid name.");
            }
        } while (!matcher.matches());


        String activeDate;
        pattern = Pattern.compile("\\d{4}-\\d{4}|\\d{4}-");
        do {
            System.out.print("Enter Active Date (YYYY-YYYY or YYYY-): ");
            activeDate = scanner.nextLine();
            matcher = pattern.matcher(activeDate);
            if (!matcher.matches()) {
                System.out.println("Invalid Active date. Please enter a valid date.");
            }
        } while (!matcher.matches());
        Author author = new Author(authorName, "");
        author.setActiveYear(activeDate);


        String publicationDate;
        pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        do {
            System.out.print("Enter Publication Date (YYYY-MM-DD): ");
            publicationDate = scanner.nextLine();
            matcher = pattern.matcher(publicationDate);
            if (!matcher.matches()) {
                System.out.println("Invalid publication date. Please enter a valid date in the format YYYY-MM-DD.");
            }
        } while (!matcher.matches());

        LocalDate date = LocalDate.parse(publicationDate);

        Book newBook = new Book(title, author, date, true);
        for (int i = 0; i < books.length; i++) {
            if (books[i] == null) {
                books[i] = newBook;
                System.out.println("Book added successfully!");
                break;
            }
        }
    }


    private static int getValidInt(String message, String regex, Scanner scanner) {
        int userInput;
        while (true) {
            System.out.print(message);
            String input = scanner.next();

            if (input.matches(regex)) {
                userInput = Integer.parseInt(input);
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return userInput;
    }


    private static String getValidString(String message, String regex, Scanner scanner) {
        String userInput;
        while (true) {
            System.out.print(message);
            userInput = scanner.nextLine();

            if (userInput.matches(regex)) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid string.");
            }
        }
        return userInput;
    }
}