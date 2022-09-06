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

package com.jackmeng.halcyoninae.cosmos.identifier;

import javax.swing.*;
import java.beans.ConstructorProperties;

/**
 * A buton that can hold an attribute
 *
 * @author Jack Meng
 * @since 3.3
 */
public class AttributableButton extends JButton {
    private transient String attribute;

    public AttributableButton(Icon icon) {
        super(null, icon);
    }

    @ConstructorProperties({"text"})
    public AttributableButton(String text) {
        super(text, null);
    }

    /**
     * @return The attribute that was set
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param e The attribute to be set
     */
    public void setAttribute(String e) {
        this.attribute = e;
    }
}