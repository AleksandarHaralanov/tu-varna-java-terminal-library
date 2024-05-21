package me.beezle.library.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

public class Util
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static File getDataFile()
    {
        try
        {
            File file = new File(getDataFolder(), "data.json");
            if (!file.exists()) file.createNewFile();
            return file;
        } catch (Exception ignored) {}
        return null;
    }

    public static File getDataFolder()
    {
        File file = new File(getHomeFolder(), ".beezle-db");
        if (!file.exists()) file.mkdirs();
        return file;
    }

    public static File getHomeFolder()
    {
        return new File(System.getProperty("user.home"));
    }

    public static String sha256str(String input)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash)
            {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Font loadFont(String path)
    {
        Font customFont = loadFont(path, 24f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);
        return customFont;
    }

    public static Font loadFont(String path, float size){
        try
        {
            Font myFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Util.class.getResourceAsStream(path)));
            return myFont.deriveFont(Font.PLAIN, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return null;
    }
}
