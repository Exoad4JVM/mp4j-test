/*
 *  Copyright: (C) 2022 name of Jack Meng
 * Halcyon MP4J is music-playing software.
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

package com.halcyoninae.halcyon.constant;

import com.halcyoninae.cosmos.theme.Theme;
import com.halcyoninae.cosmos.theme.ThemeBundles;
import com.halcyoninae.halcyon.utils.ColorTool;

import java.awt.*;

/**
 * This interface holds constants for any color values that
 * may be used throughout the program for
 * GUI based colors.
 *
 * @author Jack Meng
 * @since 3.0
 */
public final class ColorManager {
    private ColorManager() {}
    public static Theme programTheme = ThemeBundles.getDefaultTheme();

    // stable const
    public static Color ONE_DARK_BG = ColorTool.hexToRGBA("#21252B");
    public static Color BORDER_THEME = ColorTool.hexToRGBA("#5F657D");

    public static Color MAIN_FG_THEME = programTheme.getForegroundColor();
    public static String MAIN_FG_STR = ColorTool.rgbTohex(programTheme.getForegroundColor());
    public static Color MAIN_BG_THEME = programTheme.getBackgroundColor();


    public static void refreshColors() {
        MAIN_FG_THEME = programTheme.getForegroundColor();
        MAIN_FG_STR = ColorTool.rgbTohex(programTheme.getForegroundColor());
        MAIN_BG_THEME = programTheme.getBackgroundColor();
    }
}
