package me.beezle.library.prompts;

import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import me.beezle.library.Application;
import me.beezle.library.util.ConsoleColors;
import me.beezle.library.util.User;

import static org.fusesource.jansi.Ansi.ansi;

public class SelectUser extends AbstractPrompt
{
    @Override
    public void build()
    {
        ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt()
                .name("selectedUser")
                .message(ConsoleColors.WHITE_BOLD_BRIGHT + "Which user would you like to perform this action on?" + ConsoleColors.WHITE);
        for (User user : Application.getInstance().getUserDB().getUsers())
            listPromptBuilder.newItem(user.username).text("\"" + user.username + "\" (access level " + user.accessLevel + ")").add();
        listPromptBuilder.newItem("~close").text("Cancel & return to menu").add();
        listPromptBuilder.addPrompt();
    }
}
