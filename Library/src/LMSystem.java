import java.util.*;

public class LMSystem {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    public static void main(String[] args) {
        new LMSystem().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Menu();
            int choice = getUserChoice(scanner);
            if (choice == 1) {
                addBook(scanner);
            } else if (choice == 2) {
                searchBooks(scanner);
            } else if (choice == 3) {
                registerMember(scanner);
            } else if (choice == 4) {
                checkoutOrReturnBook(scanner, true);
            } else if (choice == 5) {
                checkoutOrReturnBook(scanner, false);
            } else if (choice == 6) {
                System.exit(0);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void Menu() {
        System.out.println("Library Management System");
        System.out.println("1. Add Book");
        System.out.println("2. Search Books");
        System.out.println("3. Register Library Membership");
        System.out.println("4. Collect Book");
        System.out.println("5. Return a Book");
        System.out.println("6. Exit");
    }

    private int getUserChoice(Scanner scanner) {
        System.out.print("Enter your choice: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input. Please enter a number.");
        }
    }

    private void addBook(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book ISBN: ");
        String isbn = scanner.nextLine();
        books.add(new Book(title, author, isbn));
        System.out.println("Book added successfully.");
    }

    private void searchBooks(Scanner scanner) {
        System.out.print("Enter search term (title or author): ");
        String searchTerm = scanner.nextLine().toLowerCase();
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm) || book.getAuthor().toLowerCase().contains(searchTerm)) {
                matchingBooks.add(book);
            } 
        }
        if (matchingBooks.isEmpty()) {
            System.out.println("No books found.");
        } else {

            System.out.println("Matching books:");

            for (Book book : matchingBooks) {

                System.out.println("- " + book.getTitle() + " by " + book.getAuthor() + " (available: " + book.isAvailable() + ")");

            }
        }
    }

    private void registerMember(Scanner scanner) {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        System.out.print("Enter member email: ");
        String email = scanner.nextLine();
        members.add(new Member(name, email));
        System.out.println("Member registered successfully.");
    }

    private void checkoutOrReturnBook(Scanner scanner, boolean isCheckout) {
        String action = isCheckout ? "checkout" : "return";
        System.out.print("Enter member name: ");
        String memberName = scanner.nextLine();
        System.out.print("Enter book title: ");
        String bookTitle = scanner.nextLine();
        Member member = findMemberByName(memberName);
        Book book = findBookByTitle(bookTitle);
        if (member == null) {
            System.out.println("Member '" + memberName + "' not found.");
        } else if (book == null) {
            System.out.println("Book '" + bookTitle + "' not found.");
        } else {
            try {
                if (isCheckout) {
                    member.checkoutBook(book);
                } else {
                    member.returnBook(book);
                }
                System.out.println(member.getName() + " " + action + "ed '" + book.getTitle() + "'.");
                System.out.println("/n________________________/n " );
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Member findMemberByName(String name) {
        for (Member member : members) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    private Book findBookByTitle(String title) {

        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;

    }

    private static class Book {
        private String title;
        private String author;
        private String isbn;
        private boolean available;

        public Book(String title, String author, String isbn) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.available = true;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getISBN() {
            return isbn;
        }

        public boolean isAvailable() {
            return available;
        }

        public void toggleAvailability() {
            available = !available;
        }
    }

    private static class Member {
        private String name;
        private String email;
        private List<Book> borrowedBooks = new ArrayList<>();

        public Member(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public void checkoutBook(Book book) {
            if (!book.isAvailable()) {
                throw new IllegalStateException("Book '" + book.getTitle() + "' is not available for checkout.");
            }
            book.toggleAvailability();
            borrowedBooks.add(book);
        }

        public void returnBook(Book book) {
            if (!borrowedBooks.contains(book)) {
                throw new IllegalStateException(name + " did not borrow the book '" + book.getTitle() + "'.");
            }
            borrowedBooks.remove(book);
            book.toggleAvailability();
        }
    }
}
