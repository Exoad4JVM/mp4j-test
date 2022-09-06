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

package com.jackmeng.halcyoninae.cosmos.tasks;

import com.jackmeng.halcyoninae.cosmos.components.bottompane.BottomPane;

import java.util.concurrent.ExecutorService;

/**
 * A class designed to constantly ping
 * the file view system in order to alert
 * it of any change.
 * <p>
 * In order to automatically update the file view
 * system without the user having to update it manually.
 */
public final class PingFileView implements Runnable {
    private final BottomPane bp;
    private ExecutorService worker;

    /**
     * Calls the default BottomPane Object
     *
     * @param bp the bottompane instance
     * @see com.jackmeng.halcyoninae.cosmos.components.bottompane.BottomPane
     */
    public PingFileView(BottomPane bp) {
        this.bp = bp;
    }

    @Override
    public void run() {
        if (worker == null) {
            /*
             * worker = new Thread(() -> {
             * while (true) {
             * bp.mastRevalidate();
             * Program.cacher.pingLikedTracks();
             * Program.cacher.pingSavedPlaylists();
             * Program.cacher.forceSaveQuiet();
             * try {
             * Thread.sleep(ConcurrentTiming.MAX_TLE);
             * } catch (InterruptedException e) {
             * e.printStackTrace();
             * }
             * }
             * });
             * worker.start();
             */
        }
    }

}
