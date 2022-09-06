/*
 *  Copyright: (C) 2022 MP4J Jack Meng
 * Halcyon MP4J is a music player designed openly.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package com.jackmeng.halcyoninae.cosmos.dialog;

import com.jackmeng.halcyoninae.halcyon.constant.Global;
import com.jackmeng.halcyoninae.halcyon.constant.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfirmWindow extends JFrame implements Runnable, ActionListener {

    static final String DIALOG_CONFIRM_WIN_TITLE    = "Confirmation!";
    /// Confirm Config START
    final int DIALOG_CONFIRM_MIN_WIDTH              = 300;
    final int DIALOG_CONFIRM_MIN_HEIGHT             = 200;
    final int DIALOG_CONFIRM_PROMPT_AREA_MIN_WIDTH  = DIALOG_CONFIRM_MIN_WIDTH - 20;
    final int DIALOG_CONFIRM_PROMPT_AREA_MIN_HEIGHT = DIALOG_CONFIRM_MIN_HEIGHT / 5;
    /// Confirm Config END
    private final JButton confirm;
    private final transient ConfirmationListener[] listeners;

    public ConfirmWindow(String content, ConfirmationListener... listeners) {
        super(DIALOG_CONFIRM_WIN_TITLE);
        setIconImage(Global.rd.getFromAsImageIcon(Manager.PROGRAM_ICON_LOGO).getImage());
        this.listeners = listeners;
        setPreferredSize(new Dimension(DIALOG_CONFIRM_MIN_WIDTH, DIALOG_CONFIRM_MIN_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        confirm = new JButton("Confirm");
        confirm.setAlignmentY(Component.CENTER_ALIGNMENT);
        confirm.addActionListener(this);

        JButton cancel = new JButton("Cancel");
        cancel.setAlignmentY(Component.CENTER_ALIGNMENT);
        cancel.addActionListener(this);

        JTextArea prompt = new JTextArea(content);
        prompt.setLineWrap(true);
        prompt.setWrapStyleWord(true);
        prompt.setAlignmentY(Component.CENTER_ALIGNMENT);
        prompt.setEditable(false);

        JScrollPane container = new JScrollPane(prompt);
        container.setPreferredSize(
            new Dimension(DIALOG_CONFIRM_PROMPT_AREA_MIN_WIDTH, DIALOG_CONFIRM_PROMPT_AREA_MIN_HEIGHT));
        container.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        container.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout(FlowLayout.CENTER));
        jp.setPreferredSize(
            new Dimension(DIALOG_CONFIRM_PROMPT_AREA_MIN_WIDTH, DIALOG_CONFIRM_PROMPT_AREA_MIN_HEIGHT));
        jp.add(confirm);
        jp.add(cancel);

        setLayout(new BorderLayout());
        setResizable(false);
        add(container, BorderLayout.NORTH);
        add(jp, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispatchConfirmationEvents(false);
            }
        });

        getContentPane().add(container);
    }

    /**
     * @param status
     */
    private void dispatchConfirmationEvents(boolean status) {
        for (ConfirmationListener listener : listeners) {
            listener.onStatus(status);
        }
    }

    @Override
    public void run() {
        pack();
        setAlwaysOnTop(true);
        setVisible(true);
    }

    /**
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        dispatchConfirmationEvents(e.getSource().equals(confirm));
        dispose();
    }


    /**
     * A listener that is called upon for any classes that
     * wish to listen in on anything about the update for this confirmation
     * daemon.
     *
     * @author Jack Meng
     * @since 3.0
     */
    public interface ConfirmationListener {
        /**
         * Called for a status update for this class.
         *
         * @param status The status (true || false)
         */
        void onStatus(boolean status);
    }
}
