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

import com.jackmeng.halcyoninae.halcyon.connections.properties.ExternalResource;
import com.jackmeng.halcyoninae.halcyon.connections.properties.ProgramResourceManager;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A utility class for text manipulation
 *
 * @author Jack Meng
 * @since 3.0
 */
public final class TextParser {
    private TextParser() {
    }

    /**
     * Returns a string that has been stripped based on the desired length.
     * <p>
     * For example:
     * <p>
     * strip("helloworld", 2) --> "he..."
     *
     * @param str         The string to strip
     * @param validLength The valid length (from 1)
     * @return A string that has been stripped based on the desired length
     */
    public static String strip(String str, int validLength) {
        return str != null ? str.length() > validLength ? str.substring(0, validLength) + "..." : str : "";
    }


    /**
     * @param str
     * @param validLength
     * @return String
     */
    public static String fulfill(String str, int validLength) {
        return str != null ? str.length() > validLength ? str.substring(0, validLength) + "..."
            : str.length() < validLength ? str + getCopies(validLength, " ") : str : "";
    }


    /**
     * @param n
     * @param s
     * @return String
     */
    public static String getCopies(int n, String s) {
        return String.valueOf(s).repeat(Math.max(0, n + 1));
    }

    /**
     * @param str
     * @return String
     */
    public static String clipText(String str) {
        return str.substring(0, str.length() - 1);
    }

    /**
     * @param str
     * @return boolean
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @return String
     */
    public static String getPropertyTextEncodingName() {
        return ExternalResource.pm.get(ProgramResourceManager.KEY_USER_CHAR_SET_WRITE_TABLE).equals("utf8") ? "UTF-8"
            : (ExternalResource.pm.get(ProgramResourceManager.KEY_USER_CHAR_SET_WRITE_TABLE).equals("utf16le")
            ? "UTF-16LE"
            : "UTF-16BE");
    }

    /**
     * @param str
     * @return String
     */
    public static String parseAsPure(String str) {
        return new String(ExternalResource.pm.get(ProgramResourceManager.KEY_USER_CHAR_SET_WRITE_TABLE).equals("utf16")
            ? str.getBytes(StandardCharsets.UTF_16)
            : (ExternalResource.pm
            .get(ProgramResourceManager.KEY_USER_CHAR_SET_WRITE_TABLE).equals("utf8")
            ? str.getBytes(StandardCharsets.UTF_8)
            : (ExternalResource.pm.get(ProgramResourceManager.KEY_USER_CHAR_SET_WRITE_TABLE)
            .equals("utf16le")
            ? str.getBytes(StandardCharsets.UTF_16LE)
            : str.getBytes(StandardCharsets.UTF_16BE))));
    }

    /**
     * @return Charset
     */
    public static Charset getCharset() {
        return TextParser.getPropertyTextEncodingName().equals("UTF-8") ? StandardCharsets.UTF_8
            : (TextParser.getPropertyTextEncodingName().equals("UTF-16LE") ? StandardCharsets.UTF_16LE
            : StandardCharsets.UTF_16BE);
    }
}