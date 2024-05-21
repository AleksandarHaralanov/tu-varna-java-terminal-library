package me.beezle.library.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;

public class UserDB
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ArrayList<User> users;

    public UserDB()
    {
        users = new ArrayList<>();
    }

    public ArrayList<User> getUsers()
    {
        return users;
    }

    public User getUser(String username) throws UnknownUserException
    {
        for (User user : users)
            if (user.username.equalsIgnoreCase(username))
                return user;
        throw new UnknownUserException("Could not find an account with that username!");
    }

    public void addUser(User user)
    {
        users.add(user);
        saveUsers();
    }

    public void removeUser(String username)
    {
        users.removeIf(user -> user.username.equals(username));
        saveUsers();
    }

    public void loadUsers()
    {
        try (FileReader reader = new FileReader(new File(Util.getDataFolder(), "users.json")))
        {
            UserDB db = gson.fromJson(reader, UserDB.class);
            users = db.users;
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void saveUsers()
    {
        try
        {
            File file = new File(Util.getDataFolder(), "users.json");
            if (!file.exists()) file.createNewFile();
            try (FileWriter writer = new FileWriter(file))
            {
                gson.toJson(this, UserDB.class, writer);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void updateUser(String username, User user)
    {
        users.removeIf(u -> u.username.equals(username));
        users.add(user);
        saveUsers();
    }
}
