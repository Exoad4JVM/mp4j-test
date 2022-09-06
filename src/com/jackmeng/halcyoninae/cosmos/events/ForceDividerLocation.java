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

package com.jackmeng.halcyoninae.cosmos.events;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A class with a listener to constantly keep a divider in between a
 * threshold from the original location
 *
 * @author Jack Meng
 * @since 3.0
 */
public class ForceDividerLocation implements PropertyChangeListener {
    private final JSplitPane e;
    private final int min;
    private final int max;

    /**
     * @param e         The JSplitPane instance
     * @param threshold The threshold (center+threshold and center-threshold)
     */
    public ForceDividerLocation(JSplitPane e, int threshold) {
        this.e = e;
        min = (int) ((e.getPreferredSize().getHeight() / 2) - threshold);
        max = (int) (threshold + (e.getPreferredSize().getHeight() / 2));
    }


    /**
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("dividerLocation")) {
            int dividerLoc = e.getDividerLocation();
            if (dividerLoc < min) {
                e.setDividerLocation(min);
            }
            if (dividerLoc > max) {
                e.setDividerLocation(max);
            }
        }
    }
}
