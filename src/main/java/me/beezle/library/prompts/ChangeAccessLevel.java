package me.beezle.library.prompts;

import me.beezle.library.util.ConsoleColors;

import static org.fusesource.jansi.Ansi.ansi;

public class ChangeAccessLevel extends AbstractPrompt
{
    @Override
    public void build()
    {
        promptBuilder.createInputPrompt()
                .name("newAccessLevel")
                .message(ConsoleColors.WHITE_BOLD_BRIGHT + "What access level would you like to give this user (1 or 2)?" + ConsoleColors.WHITE)
                .defaultValue("1")
                .addPrompt();
    }
}
