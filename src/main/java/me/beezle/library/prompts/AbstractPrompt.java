package me.beezle.library.prompts;

import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;

import java.io.IOException;
import java.util.HashMap;

public abstract class AbstractPrompt
{
    protected ConsolePrompt prompt;
    protected PromptBuilder promptBuilder;

    public AbstractPrompt()
    {
        prompt = new ConsolePrompt();
        promptBuilder = prompt.getPromptBuilder();
    }

    public abstract void build();

    public final HashMap<String, ? extends PromtResultItemIF> showPrompt()
    {
        try
        {
            return prompt.prompt(promptBuilder.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
