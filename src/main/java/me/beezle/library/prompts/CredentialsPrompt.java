package me.beezle.library.prompts;

import me.beezle.library.util.ConsoleColors;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public class CredentialsPrompt extends AbstractPrompt
{
    @Override
    public void build()
    {
        promptBuilder.createInputPrompt()
                .name("username")
                .message(ConsoleColors.WHITE + "Username")
                .defaultValue("John")
                .addPrompt();

        promptBuilder.createInputPrompt()
                .name("password")
                .message(ConsoleColors.WHITE + "Password")
                .mask('*')
                .defaultValue("abc123")
                .addPrompt();
    }
}
