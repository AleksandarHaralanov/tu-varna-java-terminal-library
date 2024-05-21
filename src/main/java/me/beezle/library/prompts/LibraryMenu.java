package me.beezle.library.prompts;

import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import me.beezle.library.Application;
import me.beezle.library.util.ConsoleColors;
import me.beezle.library.util.User;

import static org.fusesource.jansi.Ansi.ansi;

public class LibraryMenu extends AbstractPrompt
{
    @Override
    public void build()
    {
        User user = Application.getInstance().getUser();
        String username = user.username;
        int accessLevel = user.accessLevel;
        System.out.println("\n\nYou are signed in as: " + username + " (access level " + accessLevel + ")\n\n");

        ListPromptBuilder list = promptBuilder.createListPrompt();

        list.name("librarymenu");
        list.message(ConsoleColors.WHITE_BOLD_BRIGHT + "What would you like to do?" + ConsoleColors.WHITE);

        list.newItem("books-all").text("View all books").add();
        list.newItem("books-find").text("Find a book").add();
        list.newItem("books-sort").text("Sort books").add();

        boolean admin = (accessLevel == 2);

        if (admin)
        {
            list.newItem("books-add").text("(Admin) Add a book").add();
            list.newItem("books-delete").text("(Admin) Delete a book").add();
            list.newItem("books-edit").text("(Admin) Edit a book").add();
            list.newItem("user-admindelete").text("(Admin) Delete a user").add();
            list.newItem("user-adminedit").text("(Admin) Change access level").add();
        }

        list.newItem("user-logout").text("Logout").add();
        list.newItem("user-close").text("Close my account").add();
        list.addPrompt();
    }
}
