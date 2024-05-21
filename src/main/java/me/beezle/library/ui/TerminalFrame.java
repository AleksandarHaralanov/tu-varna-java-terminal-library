package me.beezle.library.ui;

import com.jediterm.terminal.TtyConnectorWaitFor;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;

public class TerminalFrame extends JFrame
{
    private static TerminalFrame instance;
    public static TerminalFrame getInstance()
    {
        if (instance == null) instance = new TerminalFrame();
        return instance;
    }

    private TerminalPanel terminalPanel;

    public TerminalFrame()
    {
        setSize(676, 343);
        getContentPane().setBackground(Color.BLACK);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Library");

        terminalPanel = new TerminalPanel();
        add(terminalPanel);

        pack();
    }

    public void create()
    {
        setVisible(true);
    }

    public void destroy()
    {
        terminalPanel.ptyProcess.destroy();
        terminalPanel.ptyProcess.destroyForcibly();
        terminalPanel.ttyConnector.close();
        terminalPanel.jediTermWidget.stop();
        terminalPanel.jediTermWidget.close();
        terminalPanel.jediTermWidget.setVisible(false);
        new TtyConnectorWaitFor(terminalPanel.ttyConnector, Executors.newFixedThreadPool(1));
        removeAll();
        setVisible(false);
        dispose();
    }
}
