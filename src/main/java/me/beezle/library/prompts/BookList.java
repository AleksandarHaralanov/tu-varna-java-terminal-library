package me.beezle.library.prompts;

import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import me.beezle.library.Application;
import me.beezle.library.util.Book;
import me.beezle.library.util.ConsoleColors;

public class BookList extends AbstractPrompt
{
    @Override
    public void build()
    {
        ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt()
                .name("booklist")
                .message(ConsoleColors.WHITE_BOLD_BRIGHT + "Please select a book:" + ConsoleColors.WHITE);
        for (Book book : Application.getInstance().getBookManager().getBooks())
            listPromptBuilder.newItem(book.isbn).text(" -- \"" + book.title + "\"" + ConsoleColors.WHITE + " by " + ConsoleColors.WHITE_BOLD + book.author + ConsoleColors.WHITE + " (" + book.publishingYear + ")").add();
        listPromptBuilder.newItem("~close").text("Cancel & return to menu").add();
        listPromptBuilder.addPrompt();
    }
}
