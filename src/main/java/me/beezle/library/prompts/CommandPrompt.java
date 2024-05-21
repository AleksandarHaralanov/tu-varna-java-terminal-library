package me.beezle.library.prompts;

public class CommandPrompt extends AbstractPrompt
{
    @Override
    public void build()
    {
        promptBuilder.createInputPrompt()
                .name("cmd")
                .message("Please enter a command:")
                .defaultValue("help")
                .addPrompt();
    }
}
