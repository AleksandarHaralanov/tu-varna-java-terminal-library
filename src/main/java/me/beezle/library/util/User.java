package me.beezle.library.util;

import java.util.Objects;

public class User
{
    public String username;
    public String hashedPassword;
    public int accessLevel;

    public boolean isPasswordValid(String input)
    {
        return Objects.equals(Util.sha256str(input), hashedPassword);
    }
}
