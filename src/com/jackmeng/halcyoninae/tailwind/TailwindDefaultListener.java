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

package com.jackmeng.halcyoninae.tailwind;

import com.jackmeng.halcyoninae.halcyon.debug.Debugger;
import com.jackmeng.halcyoninae.tailwind.TailwindEvent.TailwindStatus;

/**
 * @author Jack Meng
 * @since 3.2
 */
public record TailwindDefaultListener(Tailwind player)
    implements TailwindListener.StatusUpdateListener, TailwindListener.GenericUpdateListener {

    /**
     * @param status
     */
    @Override
    public void statusUpdate(TailwindStatus status) {
        if (status.equals(TailwindStatus.END)) {
            player.close();
        }
        Debugger.crit("Tailwind STATUS_REC: " + status);
    }

    /**
     * @param event
     */
    @Override
    public void genericUpdate(TailwindEvent event) {
    }

}
