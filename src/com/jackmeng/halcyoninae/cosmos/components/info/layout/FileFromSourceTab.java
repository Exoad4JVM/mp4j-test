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

package com.jackmeng.halcyoninae.cosmos.components.info.layout;

import com.jackmeng.halcyoninae.cosmos.components.bottompane.bbloc.buttons.LegalNoticeButton;
import com.jackmeng.halcyoninae.cosmos.components.info.InformationTab;
import com.jackmeng.halcyoninae.halcyon.connections.properties.ExternalResource;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This is a special tab that can be used to write pure text to.
 * The programmar can also decide to use the builtin {@link #getContent(String)}
 * method to read the contents of a file.
 * All content can be in HTML or standard TEXT
 *
 * @author Jack Meng
 * @since 3.2
 */
public class FileFromSourceTab extends JScrollPane implements InformationTab {
    private final JEditorPane text;
    private String tabName = "";

    /**
     * Two arguments are accepted in the construction of this custom tab.
     *
     * @param tabName The preferred tab name (should be kept at a small length)
     * @param textTab The content of the tab {@link #getContent(String)}
     * @see #getContent(String)
     */
    public FileFromSourceTab(String tabName, String textTab) {
        setPreferredSize(new Dimension(LegalNoticeButton.LEGALNOTICEDIALOG_MIN_WIDTH,
            LegalNoticeButton.LEGALNOTICEDIALOG_MIN_HEIGHT));
        setFocusable(true);

        this.tabName = tabName;

        text = new JEditorPane();
        text.setEditable(false);
        text.setContentType("text/html");
        text.setText(textTab);
        text.setCaretPosition(0);

        getViewport().add(text);
    }

    /**
     * If the user decides to read from a file, this method should be used
     * to grab the content of the file. This method should be only used
     * in the constructor of this GUI object.
     *
     * @param file A String representing the file location
     * @return String The content of the file in the native encoding.
     */
    public static String getContent(String file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            ExternalResource.dispatchLog(e);
        }
        return sb.toString();
    }

    /**
     * @return String
     */
    @Override
    public String getName() {
        return tabName;
    }

    /**
     * @return String
     */
    @Override
    public String getToolTip() {
        return "View Legal Information regarding this program.";
    }

    /**
     * @return JComponent
     */
    @Override
    public JComponent getComponent() {
        return this;
    }
}
