package me.beezle.library.util;

import java.util.ArrayList;

public class Book
{
    public String author;
    public String title;
    public String genre;
    public String description;
    public int publishingYear;
    public ArrayList<String> keywords;
    public double rating;
    public String isbn;

    public String getKeywords()
    {
        StringBuilder sb = new StringBuilder();
        for (String keyword : keywords)
            sb.append(keyword).append(", ");
        String pre = sb.toString().trim();
        return pre.substring(0, pre.length() - 1);
    }
}
