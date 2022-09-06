/*
 *  Copyright: (C) 2022 MP4J Jack Meng
 * CloudSpin a graphics library for image manipulation is licensed under the following
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

package com.jackmeng.halcyoninae.cloudspin;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;

/**
 * @author Jack Meng
 * @since 3.2
 */
public interface CloudSpinFilters {
    BufferedImageOp[] filters = {
        new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null),
    };

    float[] BLUR_KERNEL = {
        (1.0f / 9.0f), (1.0f / 9.0f), (1.0f / 9.0f),
        (1.0f / 9.0f), (1.0f / 9.0f), (1.0f / 9.0f),
        (1.0f / 9.0f), (1.0f / 9.0f), (1.0f / 9.0f)
    };

    int AFF_GREY = 0;
}
