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

package com.jackmeng.halcyoninae.halcyon.utils;

import java.awt.*;
import java.util.Random;

/**
 * A Class to manipulate Color utility
 *
 * @author Jack Meng
 * @since 2.0
 */
public final class ColorTool {
    private ColorTool() {
    }

    /**
     * Given a hex, it will return a {@link java.awt.Color} Object
     * representing the color.
     *
     * @param hex The hex to convert
     * @return Color The color object
     */
    public static Color hexToRGBA(String hex) {
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }
        return new Color(
            Integer.valueOf(hex.substring(1, 3), 16),
            Integer.valueOf(hex.substring(3, 5), 16),
            Integer.valueOf(hex.substring(5, 7), 16));
    }

    /**
     * Convert a color object to a string hex representation.
     *
     * @param color The color to convert
     * @return String The hex representation of the color
     */
    public static String rgbTohex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }


    /**
     * @param c
     * @param percent
     * @return Color
     */
    public static Color brightenColor(Color c, int percent) {
        int r = c.getRed() + (255 - c.getRed()) * percent / 100;
        int g = c.getGreen() + (255 - c.getGreen()) * percent / 100;
        int b = c.getBlue() + (255 - c.getBlue()) * percent / 100;
        int a = c.getAlpha() - c.getAlpha() * 30 / 100;
        return new Color(r, g, b, a);
    }

    /**
     * Returns an integer array representing the standard RED GREEN BLUE
     * colors. Where arr[0]:RED arr[1]:GREEN arr[2]:BLUE. Alpha is not
     * represented
     *
     * @param c A Color object (java.awt.Color)
     * @return An integer array of length 3
     */
    public static int[] colorBreakDown(Color c) {
        return new int[]{c.getRed(), c.getGreen(), c.getBlue()};
    }

    /**
     * Get a color with 0 RED GREEN BLUE ALPHA
     *
     * @return A Color object (java.awt.Color)
     */
    public static Color getNullColor() {
        return new Color(0, 0, 0, 0);
    }

    /**
     * Returns a random color
     *
     * @return A color object (int8)
     */
    public static Color rndColor() {
        Random r = new Random();
        return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }
}