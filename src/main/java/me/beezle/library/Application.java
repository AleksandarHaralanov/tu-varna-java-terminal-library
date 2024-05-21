package me.beezle.library;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import jline.TerminalFactory;
import me.beezle.library.prompts.*;
import me.beezle.library.util.*;
import org.fusesource.jansi.AnsiConsole;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static org.fusesource.jansi.Ansi.ansi;

public class Application
{
    private static Application instance;

    public static Application getInstance()
    {
        return instance;
    }

    public static void main(String[] args)
    {
        new Application().init();
    }

    private User loggedInUser;
    private BookManager bookManager;
    private UserDB userDB;

    public void init()
    {
        instance = this;
        bookManager = new BookManager();
        userDB = new UserDB();
        AnsiConsole.systemInstall();

        File books = new File(Util.getDataFolder(), "books.json");
        if (!books.exists())
        {
            try (InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/books.json"))))
            {
                Type listType = new TypeToken<ArrayList<Book>>(){}.getType();
                ArrayList<Book> bookList = new GsonBuilder().setPrettyPrinting().create().fromJson(isr, listType);
                bookManager.addAllBooks(bookList);
                bookManager.saveBooks();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        bookManager.loadBooks();
        userDB.loadUsers();

        reset();

        File init = new File(Util.getDataFolder(), "initialized.dat");
        if (!init.exists()) promptInitialSetup();
        else promptMainMenu();
    }

    private void promptInitialSetup()
    {
        try
        {
            InitialSetup initialSetup = new InitialSetup();
            initialSetup.build();
            HashMap<String, ? extends PromtResultItemIF> result = initialSetup.showPrompt();
            String username = ((InputResult) result.get("adminusername")).getInput();
            String password = ((InputResult) result.get("adminpassword")).getInput();

            User adminUser = new User();
            adminUser.username = username;
            adminUser.hashedPassword = Util.sha256str(password);
            adminUser.accessLevel = 2;
            userDB.addUser(adminUser);
            new File(Util.getDataFolder(), "initialized.dat").createNewFile();
            anyKey("Your admin account has been setup successfully!");
            reset();
            promptMainMenu();
        } catch (IOException e) {
            System.out.println("Unexpected Error: " + e.getMessage());
            System.out.println("Please try running the program again.");
            System.exit(1);
        } finally {
            tryRestore();
        }
    }

    private void promptMainMenu()
    {
        try
        {
            MainMenu mainMenu = new MainMenu();
            mainMenu.build();
            HashMap<String, ? extends PromtResultItemIF> result = mainMenu.showPrompt();

            result.forEach((r, a) ->
            {
                String selectedId = ((ListResult) a).getSelectedId();
                switch (selectedId)
                {
                    default:
                        anyKey("Sorry, this operation is either broken or not implemented.");
                        reset();
                        promptMainMenu();
                        break;
                    case "Create user":
                        CredentialsPrompt registerPrompt = new CredentialsPrompt();
                        registerPrompt.build();
                        HashMap<String, ? extends PromtResultItemIF> registerResult = registerPrompt.showPrompt();
                        String username = ((InputResult) registerResult.get("username")).getInput();
                        String password = ((InputResult) registerResult.get("password")).getInput();
                        User newUser = new User();
                        newUser.username = username;
                        newUser.hashedPassword = Util.sha256str(password);
                        newUser.accessLevel = 1;

                        userDB.addUser(newUser);
                        anyKey("Your new account has been registered successfully!");
                        loggedInUser = newUser;
                        reset();
                        promptLibrary();
                        break;
                    case "User sign-in":
                        try
                        {
                            CredentialsPrompt loginPrompt = new CredentialsPrompt();
                            loginPrompt.build();
                            HashMap<String, ? extends PromtResultItemIF> loginResult = loginPrompt.showPrompt();
                            String loginUsername = ((InputResult) loginResult.get("username")).getInput();
                            String loginPassword = ((InputResult) loginResult.get("password")).getInput();
                            User user = userDB.getUser(loginUsername);

                            if (!user.isPasswordValid(loginPassword))
                            {
                                anyKey("Unable to login: invalid password!");
                                reset();
                                promptMainMenu();
                                return;
                            }

                            loggedInUser = user;

                            reset();
                            promptLibrary();
                        } catch (UnknownUserException uuex) {
                            anyKey(uuex.getMessage());
                            reset();
                            promptMainMenu();
                        } catch (Exception ex) {
                            anyKey("Failed to login!",
                                    "Error: " + ex.getMessage());
                            reset();
                            promptMainMenu();
                        }
                        break;
                    case "Exit":
                        System.out.println("Goodbye! You can now exit the application.");
                        AnsiConsole.systemUninstall();
                        Runtime.getRuntime().exit(0);
                }
            });
        } finally {
            tryRestore();
        }
    }

    private void promptLibrary()
    {
        try
        {
            LibraryMenu libraryMenu = new LibraryMenu();
            libraryMenu.build();
            HashMap<String, ? extends PromtResultItemIF> result = libraryMenu.showPrompt();

            result.forEach((r, a) ->
            {
                String selectedId = ((ListResult) a).getSelectedId();
                switch (selectedId)
                {
                    default:
                        anyKey("Sorry, this operation is either broken or not implemented.");
                        reset();
                        promptLibrary();
                        break;
                    case "books-all":
                        Book book = promptSelectBook();
                        if (book == null)
                        {
                            anyKey("The library does not currently have any books. Sorry about that!");
                            reset();
                            promptLibrary();
                            return;
                        }
                        ViewBook viewBookPrompt = new ViewBook(book);
                        viewBookPrompt.build();
                        reset();
                        promptLibrary();
                        break;
//                    case "books-find":
//                        break;
//                    case "books-sort":
                        //TODO: sort books
//                        break;
                    case "books-add":
                        try
                        {
                            EditBook addBook = new EditBook(null);
                            addBook.build();
                            HashMap<String, ? extends PromtResultItemIF> newBookResult = addBook.showPrompt();
                            String bookName = ((InputResult)newBookResult.get("bookName")).getInput();
                            String bookAuthor = ((InputResult)newBookResult.get("bookAuthor")).getInput();
                            String bookGenre = ((InputResult)newBookResult.get("bookGenre")).getInput();
                            String bookDescription = ((InputResult)newBookResult.get("bookDescription")).getInput();
                            String bookYear = ((InputResult)newBookResult.get("bookYear")).getInput();
                            String bookKeywords = ((InputResult)newBookResult.get("bookKeywords")).getInput();
                            String bookRating = ((InputResult)newBookResult.get("bookRating")).getInput();
                            String isbn = ((InputResult)newBookResult.get("isbn")).getInput();
                            Book newAddBook = new Book();
                            newAddBook.title = bookName;
                            newAddBook.author = bookAuthor;
                            newAddBook.genre = bookGenre;
                            newAddBook.description = bookDescription;
                            newAddBook.publishingYear = Integer.parseInt(bookYear);
                            ArrayList<String> newKeywords = new ArrayList<>();
                            newKeywords.addAll(Arrays.asList(bookKeywords.split(",")));
                            newAddBook.keywords = newKeywords;
                            newAddBook.rating = Double.parseDouble(bookRating);
                            newAddBook.isbn = isbn;
                            bookManager.addBook(newAddBook);
                            anyKey("The book has been added to the system!");
                            reset();
                            promptLibrary();
                        } catch (Exception ex) {
                            anyKey("Error: " + ex.getMessage());
                            reset();
                            promptLibrary();
                        }
                        break;
                    case "books-delete":
                        Book deleteBook = promptSelectBook();
                        bookManager.deleteBook(deleteBook.isbn);
                        anyKey("The book has been permanently deleted from the system!");
                        reset();
                        promptLibrary();
                        break;
                    case "books-edit":
                        try
                        {
                            EditBook editBook = new EditBook(promptSelectBook());
                            editBook.build();
                            HashMap<String, ? extends PromtResultItemIF> newBookResult = editBook.showPrompt();
                            String bookNameE = ((InputResult)newBookResult.get("bookName")).getInput();
                            String bookAuthorE = ((InputResult)newBookResult.get("bookAuthor")).getInput();
                            String bookGenreE = ((InputResult)newBookResult.get("bookGenre")).getInput();
                            String bookDescriptionE = ((InputResult)newBookResult.get("bookDescription")).getInput();
                            String bookYearE = ((InputResult)newBookResult.get("bookYear")).getInput();
                            String bookKeywordsE = ((InputResult)newBookResult.get("bookKeywords")).getInput();
                            String bookRatingE = ((InputResult)newBookResult.get("bookRating")).getInput();
                            String isbnE = ((InputResult)newBookResult.get("isbn")).getInput();
                            Book newEditBook = new Book();
                            newEditBook.title = bookNameE;
                            newEditBook.author = bookAuthorE;
                            newEditBook.genre = bookGenreE;
                            newEditBook.description = bookDescriptionE;
                            newEditBook.publishingYear = Integer.parseInt(bookYearE);
                            ArrayList<String> newKeywordsE = new ArrayList<>();
                            newKeywordsE.addAll(Arrays.asList(bookKeywordsE.split(",")));
                            newEditBook.keywords = newKeywordsE;
                            newEditBook.rating = Double.parseDouble(bookRatingE);
                            newEditBook.isbn = isbnE;
                            bookManager.editBook(newEditBook.isbn, newEditBook);
                            anyKey("The book data has been updated!");
                            reset();
                            promptLibrary();
                        } catch (Exception ex) {
                            anyKey("Error: " + ex.getMessage());
                            reset();
                            promptLibrary();
                        }
                        break;
                    case "user-admindelete":
                        try
                        {
                            User deleteUser = promptSelectUser();
                            if (deleteUser == null) // user wanted to cancel & exit
                            {
                                reset();
                                promptLibrary();
                                return;
                            }
                            userDB.removeUser(deleteUser.username);
                            anyKey("Successfully deleted user from the system!");
                            reset();
                            promptLibrary();
                        } catch (SelfModificationException ex) {
                            anyKey("Sorry, admins can't delete their own accounts!");
                            reset();
                            promptLibrary();
                        } catch (UnknownUserException ex) {
                            anyKey("Unknown error: " + ex.getMessage());
                            reset();
                            promptLibrary();
                        }
                        break;
                    case "user-adminedit":
                        try
                        {
                            User adminEditUser = promptSelectUser();
                            if (adminEditUser == null) // user wanted to cancel & exit
                            {
                                reset();
                                promptLibrary();
                                return;
                            }
                            adminEditUser.accessLevel = askForAccessLevel();
                            userDB.updateUser(adminEditUser.username, adminEditUser);
                            anyKey("The user's access level has been updated!");
                            reset();
                            promptLibrary();
                        } catch (SelfModificationException ex) {
                            anyKey("Sorry, admins can't modify their own access level!");
                            reset();
                            promptLibrary();
                        } catch (UnknownUserException e) {
                            anyKey("Unknown error: " + e.getMessage());
                            reset();
                            promptLibrary();
                        }
                        break;
                    case "user-logout":
                        loggedInUser = null;
                        anyKey("You have successfully logged out!");
                        reset();
                        promptMainMenu();
                        break;
                    case "user-close":
                        if (getUser().accessLevel == 2)
                        {
                            anyKey("Sorry, admins can't close their own accounts!");
                            reset();
                            promptLibrary();
                            return;
                        }
                        userDB.removeUser(getUser().username);
                        anyKey("Your account has been closed & permanently deleted!");
                        reset();
                        promptMainMenu();
                        break;
                }
            });
        } finally {
            tryRestore();
        }
    }

    public Book promptSelectBook()
    {
        ArrayList<Book> books = bookManager.getBooks();
        if (books.isEmpty())
            return null;
        BookList bookListPrompt = new BookList();
        bookListPrompt.build();
        HashMap<String, ? extends PromtResultItemIF> bookListResult = bookListPrompt.showPrompt();
        String selectedBookISBN = ((ListResult)bookListResult.get("booklist")).getSelectedId();
        if (selectedBookISBN.equalsIgnoreCase("~close"))
        {
            reset();
            promptLibrary();
            return null;
        } else return bookManager.getBook(selectedBookISBN);
    }

    public User promptSelectUser() throws UnknownUserException, SelfModificationException
    {
        SelectUser promptSelectUser = new SelectUser();
        promptSelectUser.build();
        HashMap<String, ? extends PromtResultItemIF> selectResult = promptSelectUser.showPrompt();
        String selectedUser = ((ListResult)selectResult.get("selectedUser")).getSelectedId();
        if (selectedUser.equals("~close")) return null;
        try
        {
            User su = userDB.getUser(selectedUser);
            if (su.username.equals(getUser().username))
                throw new SelfModificationException();
            return su;
        } catch (UnknownUserException ex) {
            anyKey("That user does not exist!?", "Error: " + ex.getMessage());
            return promptSelectUser();
        }
    }

    public int askForAccessLevel()
    {
        try
        {
            int al = promptChangeAccessLevel();
            if (al < 1 || al > 2) throw new NumberFormatException();
            return al;
        } catch (NumberFormatException ex) {
            anyKey("Valid values for access level are: '1' and '2'!");
            reset();
            return askForAccessLevel();
        }
    }

    public int promptChangeAccessLevel() throws NumberFormatException
    {
        ChangeAccessLevel promptAccessLevel = new ChangeAccessLevel();
        promptAccessLevel.build();
        HashMap<String, ? extends PromtResultItemIF> accessLevelResult = promptAccessLevel.showPrompt();
        String newAccessLevel = ((InputResult) accessLevelResult.get("newAccessLevel")).getInput();
        return Integer.parseInt(newAccessLevel);
    }

    public void anyKey(String... lines)
    {
        if (lines != null)
            for (String str : lines)
                System.out.println(str);
        System.out.println("Press any key to continue...");
        try
        {
            System.in.read();
        } catch (Exception ignored) {}
    }

    private void reset()
    {
        System.out.println(ansi().eraseScreen().cursor(0, 0));
        printDoomLogo();
    }

    private void printDoomLogo()
    {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/ascii_logo.txt")))))
        {
            for(String line; (line = br.readLine()) != null; )
            {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void tryRestore()
    {
        try
        {
            TerminalFactory.get().restore();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public User getUser()
    {
        return loggedInUser;
    }

    public BookManager getBookManager()
    {
        return bookManager;
    }

    public UserDB getUserDB()
    {
        return userDB;
    }

    public void setUser(User user)
    {
        loggedInUser = user;
    }
}
