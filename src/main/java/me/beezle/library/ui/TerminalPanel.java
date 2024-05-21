package me.beezle.library.ui;

import com.jediterm.pty.PtyMain;
import com.jediterm.terminal.TtyConnector;
import com.jediterm.terminal.emulator.ColorPalette;
import com.jediterm.terminal.emulator.ColorPaletteImpl;
import com.jediterm.terminal.ui.JediTermWidget;
import com.jediterm.terminal.ui.UIUtil;
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;
import com.pty4j.PtyProcess;
import me.beezle.library.Main;
import me.beezle.library.util.Util;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TerminalPanel extends JPanel
{
    public final JediTermWidget jediTermWidget;
    public final TtyConnector ttyConnector;
    public PtyProcess ptyProcess;

    public TerminalPanel()
    {
        super();
        setLayout(new GridLayout());

        jediTermWidget = new JediTermWidget(90, 25, new DefaultSettingsProvider()
        {
            public float getTerminalFontSize()
            {
                return 12;
            }

            public ColorPalette getTerminalColorPalette()
            {
                return new ColorPalette()
                {
                    @Override
                    protected @NotNull Color getForegroundByColorIndex(int colorIndex)
                    {
                        Color color = customColors[colorIndex];
//                        System.out.println("Foreground color: " + color + " (index=" + colorIndex + ")");
                        return color;
                    }

                    @Override
                    protected @NotNull Color getBackgroundByColorIndex(int colorIndex)
                    {
                        Color color = customColors[colorIndex];
//                        System.out.println("Background color: " + color + " (index=" + colorIndex + ")");
                        return color;
                    }
                };
            }
        });

        ttyConnector = createTtyConnector();
        jediTermWidget.setTtyConnector(ttyConnector);

        jediTermWidget.start();
        add(jediTermWidget);
    }

    private TtyConnector createTtyConnector()
    {
        try
        {
            File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Map<String, String> envs = new HashMap<>();
            envs.putAll(System.getenv());
            String[] command;

            if (UIUtil.isWindows) command = new String[]{ "cmd.exe", "/C", "java", "-Djava.library.path=" + Util.getDataFolder(), "-cp", currentJar.getAbsolutePath(), "me.beezle.library.Application" };
            else {
                command = new String[]{ "java", "-Djava.library.path=" + Util.getDataFolder(), "-cp", currentJar.getAbsolutePath(), "me.beezle.library.Application"};
                envs.put("TERM", "xterm");
            }

            System.out.println("PTY command: " + Arrays.toString(command));

            PtyProcess process = PtyProcess.exec(command, envs, null);
            if (ptyProcess != null) ptyProcess.destroyForcibly();
            ptyProcess = process;
            return new PtyMain.LoggingPtyProcessTtyConnector(process, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static final Color[] customColors = new Color[]{
            new Color(0xc0c0c0), //White
            new Color(0x800000), //Red
            new Color(0x008000), //Green
            new Color(0x808000), //Yellow
            new Color(0x000080), //Blue
            new Color(0x800080), //Magenta
            new Color(0x008080), //Cyan
            new Color(0x808080), //Black
            //Bright versions of the ISO colors
            new Color(0xffffff), //White
            new Color(0xff0000), //Red
            new Color(0x00ff00), //Green
            new Color(0xffff00), //Yellow
            new Color(0x4682b4), //Blue
            new Color(0xff00ff), //Magenta
            new Color(0x00ffff), //Cyan
            new Color(0x000000), //Black
    };
}
