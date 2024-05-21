package me.beezle.library.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;

public class BookManager
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ArrayList<Book> books;

    public BookManager()
    {
        books = new ArrayList<>();
    }

    public ArrayList<Book> getBooks()
    {
        return books;
    }

    public Book getBook(String isbn)
    {
        for (Book book : books)
            if (book.isbn.equalsIgnoreCase(isbn))
                return book;
        return null;
    }

    public ArrayList<Book> findBook(SortQuery query, String input)
    {
        ArrayList<Book> foundBooks = new ArrayList<>();
        switch (query)
        {
            case TITLE:
                for (Book book : books)
                {
                    for (String token : input.toLowerCase().split(" "))
                        if (book.title.toLowerCase().contains(token))
                            foundBooks.add(book);
                }
                break;
            case AUTHOR:
                for (Book book : books)
                {
                    for (String token : input.toLowerCase().split(" "))
                        if (book.author.toLowerCase().contains(token))
                            foundBooks.add(book);
                }
                break;
            case YEAR:
                for (Book book : books)
                    for (String token : input.toLowerCase().split(" "))
                        if (String.valueOf(book.publishingYear).equalsIgnoreCase(token))
                            foundBooks.add(book);
                break;
            case RATING:
                for (Book book : books)
                    for (String token : input.toLowerCase().split(" "))
                        if (String.valueOf(book.rating).equalsIgnoreCase(token))
                            foundBooks.add(book);
                break;
        }
        return foundBooks;
    }

    public ArrayList<Book> getSortedBooks(SortQuery query, SortOption option)
    {
        ArrayList<Book> books = new ArrayList<>();
        books.addAll(this.books);
        books.sort(Comparator.comparingInt(b -> b.publishingYear));
        return books; //TODO: get sorted books list
    }

    public void addBook(Book book)
    {
        books.add(book);
        saveBooks();
    }

    public void addAllBooks(ArrayList<Book> books)
    {
        this.books.addAll(books);
    }

    public void deleteBook(String isbn)
    {
        books.removeIf((book) -> book.isbn.equalsIgnoreCase(isbn));
        saveBooks();
    }

    public void editBook(String isbn, Book newBook)
    {
        deleteBook(isbn);
        addBook(newBook);
        saveBooks();
    }

    public void loadBooks()
    {
        Type listType = new TypeToken<ArrayList<Book>>(){}.getType();
        try (FileReader reader = new FileReader(new File(Util.getDataFolder(), "books.json")))
        {
            books = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        Logger.getLogger("BeezleDB").info("Loaded " + books.size() + " books");
    }

    public void saveBooks()
    {
        try (FileWriter writer = new FileWriter(new File(Util.getDataFolder(), "books.json")))
        {
            gson.toJson(books, writer);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
