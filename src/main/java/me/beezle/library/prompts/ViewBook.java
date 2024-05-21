package me.beezle.library.prompts;

import me.beezle.library.Application;
import me.beezle.library.util.Book;

public class ViewBook extends AbstractPrompt
{
    private Book book;

    public ViewBook(Book book)
    {
        this.book = book;
    }

    @Override
    public void build()
    {
        System.out.println("\nViewing Book: " + '"' + book.title + '"' + " by " + book.author + "\n");
        System.out.println("== Description ==\n\n" + book.description + "\n");
        System.out.println("== Keywords ==\n\n" + book.getKeywords() + "\n");
        System.out.println("Publishing Year: " + book.publishingYear);
        System.out.println("Genre: " + book.genre);
        System.out.println("Rating: " + book.rating);
        System.out.println("ISBN: " + book.isbn);
        Application.getInstance().anyKey("\n");
    }
}
