package me.beezle.library;

import me.beezle.library.ui.TerminalFrame;
import me.beezle.library.util.OS;
import me.beezle.library.util.Util;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;

import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

public class Main
{
    public static void main(String[] args) throws URISyntaxException
    {
        File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if (!currentJar.getName().endsWith(".jar"))
            throw new RuntimeException("Runtime code-source URI location does not point to a JAR file.\n(Are you running this from an IDE?)");
        bootstrap(currentJar);
    }

    private static void bootstrap(File jar)
    {
        try
        {
            BasicConfigurator.configure();
            extractNatives();
            TerminalFrame.getInstance().create();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Oops!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void extractNatives()
    {
        switch (OS.getOS())
        {
            case Windows:
                File winDst = new File(Util.getDataFolder(), "jansi.dll");
                if (!winDst.exists()) extractResourceToDisk("/natives/jansi.dll", winDst);
                break;
            case Mac:
                File macDst = new File(Util.getDataFolder(), "libjansi.jnlib");
                if (!macDst.exists()) extractResourceToDisk("/natives/libjansi.jnlib", macDst);
                break;
            case Linux:
                File linuxDst = new File(Util.getDataFolder(), "libjansi.so");
                if (!linuxDst.exists()) extractResourceToDisk("/natives/libjansi.so", linuxDst);
                break;
        }
    }

    private static void extractResourceToDisk(String resource, File dst)
    {
        try (InputStream is = Objects.requireNonNull(Main.class.getResourceAsStream(resource)))
        {
            try (OutputStream os = new FileOutputStream(dst))
            {
                IOUtils.copy(is, os);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
