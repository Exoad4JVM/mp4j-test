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

package com.jackmeng.halcyoninae.tailwind.audioinfo;

import com.jackmeng.halcyoninae.halcyon.debug.CLIStyles;
import com.jackmeng.halcyoninae.halcyon.debug.Debugger;
import com.jackmeng.halcyoninae.halcyon.debug.TConstr;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AudioInfoArtworkDialog extends JFrame implements Runnable {
    private final JPanel drawable;

    private final transient BufferedImage img;
    private final transient AudioInfo info;

    public AudioInfoArtworkDialog(AudioInfo info) {
        this.info = info;
        this.img = info.getArtwork();
        setTitle("Halcyon ~ Artwork Viewer | " + img.getWidth() + "x" + img.getHeight());
        setIconImage(img);


        setPreferredSize(new Dimension(600, 400));
        setSize(getPreferredSize());

        drawable = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, (drawable.getWidth() - img.getWidth()) / 2,
                    (drawable.getHeight() - img.getHeight()) / 2, null);
                g.dispose();
            }
        };

        JScrollPane jsp = new JScrollPane(drawable);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add(jsp);

    }


    /**
     * @return AudioInfo
     */
    public AudioInfo getAudioInfo() {
        return info;
    }


    /**
     * @return BufferedImage
     */
    public BufferedImage getViewportArtwork() {
        return img;
    }

    @Override
    public void run() {
        Debugger.alert(new TConstr(CLIStyles.BLUE_TXT, "New Artwork Viewport being dispatched... Taking my time"));
        pack();
        setVisible(true);
    }
}