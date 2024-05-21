package me.beezle.library.prompts;

import me.beezle.library.util.ConsoleColors;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public class InitialSetup extends AbstractPrompt
{
    @Override
    public void build()
    {
        System.out.println("\n\nWelcome to the Library!");
        System.out.println("\nThis library has just installed a new digital database.");
        System.out.println("To complete the setup process, please enter in your admin credentials:\n");

        promptBuilder.createInputPrompt()
                .name("adminusername")
                .message(ConsoleColors.WHITE + "Admin Username")
                .defaultValue("admin")
                .addPrompt();

        promptBuilder.createInputPrompt()
                .name("adminpassword")
                .message(ConsoleColors.WHITE + "Admin Password")
                .mask('*')
                .defaultValue("abc123")
                .addPrompt();
    }
}
