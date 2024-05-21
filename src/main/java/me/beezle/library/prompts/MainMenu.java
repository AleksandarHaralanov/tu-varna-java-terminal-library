package me.beezle.library.prompts;

import me.beezle.library.util.ConsoleColors;

import static org.fusesource.jansi.Ansi.ansi;

public class MainMenu extends AbstractPrompt
{
    @Override
    public void build()
    {
        promptBuilder.createListPrompt()
                .name("usergate")
                .message(ConsoleColors.WHITE_BOLD_BRIGHT + "What would you like to do?" + ConsoleColors.WHITE)
                .newItem().text("Create user").add()
                .newItem().text("User sign-in").add()
                .newItem().text("Exit").add()
                .addPrompt();
    }
}
