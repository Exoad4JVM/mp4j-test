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

package com.jackmeng.halcyoninae.cosmos.components.minimizeplayer;

import com.jackmeng.halcyoninae.halcyon.constant.Global;
import com.jackmeng.halcyoninae.halcyon.constant.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * This class represents a frame that holds a minimized player.
 * <p>
 * What is a minimized player?
 * A minimized player is an indirect way to display what is being
 * played, in a very compact form factor. This form factor, however,
 * is not used to directly control the audio instead is maybe to display
 * what is being played without having the main player interface
 * being shown.
 * <p>
 * This class specifically represents the window
 * part of the miniplayer.
 *
 * @author Jack Meng
 * @see com.jackmeng.halcyoninae.cosmos.components.minimizeplayer.MiniContentPane
 * @since 3.2
 */
public class MiniPlayer extends JFrame implements Runnable {
    private int pX, pY;
    private transient MiniPlayerListener listener;

    public MiniPlayer() {
        setPreferredSize(
            new Dimension(MiniPlayerManager.MINI_PLAYER_MIN_WIDTH, MiniPlayerManager.MINI_PLAYER_MIN_HEIGHT));
        setUndecorated(true);
        setIconImage(Global.rd.getFromAsImageIcon(Manager.PROGRAM_ICON_LOGO).getImage());
        setAutoRequestFocus(true);
        setFocusable(true);
        MiniContentPane pane = new MiniContentPane();
        Global.ifp.addInfoViewUpdateListener(pane);
        setContentPane(pane);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x + me.getX() - pX,
                    getLocation().y + me.getY() - pY);

            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x + me.getX() - pX,
                    getLocation().y + me.getY() - pY);

            }
        });
        addMouseListener(new MiniPlayerClickMenu(this));
        setResizable(true);
    }

    /**
     * Only one listener is allowed per miniplayer.
     *
     * @param listener A Listener implementation or anonymous class.
     */
    public void setMiniPlayerListener(MiniPlayerListener listener) {
        this.listener = listener;
    }

    /**
     * Calls for the listener that the window is closing.
     */
    public void pounceListener() {
        listener.closingWindow();
    }

    @Override
    public void run() {
        pack();
        setVisible(true);
    }
}
