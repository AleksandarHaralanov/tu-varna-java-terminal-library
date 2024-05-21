package me.beezle.library.prompts;

import de.codeshelf.consoleui.prompt.builder.InputValueBuilder;
import me.beezle.library.util.Book;
import me.beezle.library.util.ConsoleColors;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public class EditBook extends AbstractPrompt
{
    private Book book;

    public EditBook(Book book)
    {
        this.book = book;
    }

    @Override
    public void build()
    {
        InputValueBuilder nameBuilder = promptBuilder.createInputPrompt()
                .name("bookName")
                .message(ConsoleColors.WHITE + "What is the title of this book?");

        if (book != null) nameBuilder.defaultValue(book.title);
        nameBuilder.addPrompt();

        InputValueBuilder authorBuilder = promptBuilder.createInputPrompt()
                .name("bookAuthor")
                .message(ConsoleColors.WHITE + "Who is the author of the book?");

        if (book != null) authorBuilder.defaultValue(book.author);
        authorBuilder.addPrompt();

        InputValueBuilder descriptionBuilder = promptBuilder.createInputPrompt()
                .name("bookDescription")
                .message(ConsoleColors.WHITE + "Please provide a description for the book:");

        if (book != null) descriptionBuilder.defaultValue(book.description);
        descriptionBuilder.addPrompt();

        InputValueBuilder keywordsBuilder = promptBuilder.createInputPrompt()
                .name("bookKeywords")
                .message(ConsoleColors.WHITE + "Please provide some comma-separated keywords for this book:");

        if (book != null) keywordsBuilder.defaultValue(book.getKeywords());
        keywordsBuilder.addPrompt();

        InputValueBuilder genreBuilder = promptBuilder.createInputPrompt()
                .name("bookGenre")
                .message(ConsoleColors.WHITE + "What is the genre of the book?");

        if (book != null) genreBuilder.defaultValue(book.genre);
        genreBuilder.addPrompt();

        InputValueBuilder yearBuilder = promptBuilder.createInputPrompt()
                .name("bookYear")
                .message(ConsoleColors.WHITE + "What year was this book published?");

        if (book != null) yearBuilder.defaultValue(String.valueOf(book.publishingYear));
        yearBuilder.addPrompt();

        InputValueBuilder ratingBuilder = promptBuilder.createInputPrompt()
                .name("bookRating")
                .message(ConsoleColors.WHITE + "What is the rating of this book?");

        if (book != null) ratingBuilder.defaultValue(String.valueOf(book.rating));
        ratingBuilder.addPrompt();

        InputValueBuilder isbnBuilder = promptBuilder.createInputPrompt()
                .name("isbn")
                .message(ConsoleColors.WHITE + "What is the ISBN-10 of this book?");

        if (book != null) isbnBuilder.defaultValue(book.isbn);
        isbnBuilder.addPrompt();
    }
}
