/**
 * Copyright 2019 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */

package jogamp.newt.driver.ios;

import com.jogamp.nativewindow.DefaultGraphicsScreen;
import com.jogamp.nativewindow.util.Rectangle;

import jogamp.nativewindow.ios.IOSUtil;
import jogamp.newt.MonitorModeProps;
import jogamp.newt.ScreenImpl;

import com.jogamp.common.util.ArrayHashSet;
import com.jogamp.newt.Display;
import com.jogamp.newt.MonitorDevice;
import com.jogamp.newt.MonitorMode;
import com.jogamp.opengl.math.FloatUtil;

public class ScreenDriver extends ScreenImpl {

    static {
        DisplayDriver.initSingleton();
    }

    public ScreenDriver() {
    }

    @Override
    protected void createNativeImpl() {
        aScreen = new DefaultGraphicsScreen(getDisplay().getGraphicsDevice(), screen_idx);
    }

    @Override
    protected void closeNativeImpl() { }

    private MonitorMode getMonitorModeImpl(final MonitorModeProps.Cache cache, final int crt_id, final int mode_idx) {
        final int[] modeProps = getMonitorMode0(crt_id, mode_idx);
        final MonitorMode res;
        if (null == modeProps  || 0 >= modeProps.length) {
            res = null;
        } else {
            res = MonitorModeProps.streamInMonitorMode(null, cache, modeProps, 0);
        }
        return res;
    }

    class CrtProps {
        CrtProps() {
            crtIDs = getMonitorDeviceIds0();
            count = crtIDs.length;
            pixelScaleArray = new float[count];
            propsOrigArray = new int[count][];
            propsFixedArray = new int[count][];

            //
            // Gather whole topology of monitors (NSScreens)
            //
            for(int crtIdx=0; crtIdx<count; crtIdx++) {
                final int crt_id = crtIDs[crtIdx];
                final float pixelScaleRaw = IOSUtil.GetScreenPixelScaleByScreenIdx(crt_id);
                pixelScaleArray[crtIdx] = FloatUtil.isZero(pixelScaleRaw, FloatUtil.EPSILON) ? 1.0f : pixelScaleRaw;
                propsOrigArray[crtIdx] = getMonitorProps0(crt_id);
                if ( null == propsOrigArray[crtIdx] ) {
                    throw new InternalError("Could not gather device props "+crtIdx+"/"+count+" -> "+Display.toHexString(crt_id));
                }
                // copy orig -> fixed
                final int propsLen = propsOrigArray[crtIdx].length;
                propsFixedArray[crtIdx] = new int[propsLen];
                System.arraycopy(propsOrigArray[crtIdx], 0, propsFixedArray[crtIdx], 0, propsLen);
            }

            //
            // Fix scaled viewport w/ pixelScale of each monitorProps,
            // i.e. size by its own pixelScale and x/y offset by querying it's neighbors.
            //
            for(int crtIdx=0; crtIdx<count; crtIdx++) {
                final int[] thisMonitorProps = propsFixedArray[crtIdx];
                final int x = thisMonitorProps[MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT+0];
                final int y = thisMonitorProps[MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT+1];
                final float thisPixelScale = pixelScaleArray[crtIdx];
                thisMonitorProps[MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT+2] *= thisPixelScale; // fix width
                thisMonitorProps[MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT+3] *= thisPixelScale; // fix height
                if( 0 != x ) {
                    // find matching viewport width for x-offset to apply it's pixelSize
                    for(int i=0; i<count; i++) {
                        if( i != crtIdx && x == propsOrigArray[i][MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT+2] ) {
                            thisMonitorProps[MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT+0] *= pixelScaleArray[i];
                            break;
                        }
                    }
                }
                if( 0 != y ) {
                    // find matching viewport height for y-offset to apply it's pixelSize
                    for(int i=0; i<count; i++) {
                        if( i != crtIdx && y == propsOrigArray[i][MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT+3] ) {
                            thisMonitorProps[MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT+1] *= pixelScaleArray[i];
                            break;
                        }
                    }
                }
            }
        }
        int getIndex(final int crt_id) {
            for(int i=0; i<count; i++) {
                if( crt_id == crtIDs[i] ) {
                    return i;
                }
            }
            return -1;
        }
        final int count;
        final int[] crtIDs;
        final float[] pixelScaleArray;
        final int[][] propsOrigArray;
        final int[][] propsFixedArray;
    }

    @Override
    protected final void collectNativeMonitorModesAndDevicesImpl(final MonitorModeProps.Cache cache) {
        final CrtProps crtProps = new CrtProps();

        //
        // Collect all monitorModes for all monitorDevices
        //
        for(int crtIdx=0; crtIdx<crtProps.count; crtIdx++) {
            final int crt_id = crtProps.crtIDs[crtIdx];
            final ArrayHashSet<MonitorMode> supportedModes =
                    new ArrayHashSet<MonitorMode>(false, ArrayHashSet.DEFAULT_INITIAL_CAPACITY, ArrayHashSet.DEFAULT_LOAD_FACTOR);
            int modeIdx = 0;
            {
                // Get all supported modes for this monitorDevice
                MonitorMode mode;
                while( true ) {
                    mode = getMonitorModeImpl(cache, crt_id, modeIdx);
                    if( null != mode ) {
                        if( mode.getSurfaceSize().getBitsPerPixel() >= 24 ) { // drop otherwise
                            supportedModes.getOrAdd(mode);
                        }
                        modeIdx++; // next mode on same monitor
                    } else {
                        break; // done with modes on this monitor
                    }
                }
            }
            if( 0 >= modeIdx ) {
                throw new InternalError("Could not gather single mode of device "+crtIdx+"/"+crtProps.count+" -> "+Display.toHexString(crt_id));
            }
            final MonitorMode currentMode = getMonitorModeImpl(cache, crt_id, -1);
            if ( null == currentMode ) {
                throw new InternalError("Could not gather current mode of device "+crtIdx+"/"+crtProps.count+" -> "+Display.toHexString(crt_id)+", but gathered "+modeIdx+" modes");
            }
            // merge monitor-props + supported modes
            final float pixelScale = crtProps.pixelScaleArray[crtIdx];
            MonitorModeProps.streamInMonitorDevice(cache, this, currentMode,
                                                   new float[] { pixelScale, pixelScale },
                                                   supportedModes, crtProps.propsFixedArray[crtIdx], 0, null);
        }
    }

    @Override
    protected boolean updateNativeMonitorDeviceViewportImpl(final MonitorDevice monitor, final float[] pixelScale, final Rectangle viewportPU, final Rectangle viewportWU) {
        final CrtProps crtProps = new CrtProps();
        final int crt_id = monitor.getId();
        if( 0 == crt_id ) {
            throw new IllegalArgumentException("Invalid monitor id "+Display.toHexString(crt_id));
        }
        final int crt_idx = crtProps.getIndex(crt_id);
        if( 0 > crt_idx || crt_idx >= crtProps.count ) {
            throw new IndexOutOfBoundsException("monitor id "+crt_idx+" not within [0.."+(crtProps.count-1)+"]");
        }
        final int[] fixedMonitorProps = crtProps.propsFixedArray[crt_idx];
        int offset = MonitorModeProps.IDX_MONITOR_DEVICE_VIEWPORT;
        viewportPU.set(fixedMonitorProps[offset++], fixedMonitorProps[offset++], fixedMonitorProps[offset++], fixedMonitorProps[offset++]);
        viewportWU.set(fixedMonitorProps[offset++], fixedMonitorProps[offset++], fixedMonitorProps[offset++], fixedMonitorProps[offset++]);
        final float _pixelScale = crtProps.pixelScaleArray[crt_idx];
        pixelScale[0] = _pixelScale;
        pixelScale[1] = _pixelScale;
        return true;
    }

    @Override
    protected MonitorMode queryCurrentMonitorModeImpl(final MonitorDevice monitor) {
        return getMonitorModeImpl(null, monitor.getId(), -1);
    }

    @Override
    protected boolean setCurrentMonitorModeImpl(final MonitorDevice monitor, final MonitorMode mode)  {
        return setMonitorMode0(monitor.getId(), mode.getId(), mode.getRotation());
    }

    @Override
    protected int validateScreenIndex(final int idx) {
        return 0; // big-desktop w/ multiple monitor attached, only one screen available
    }

    private native int[] getMonitorDeviceIds0();
    private native int[] getMonitorProps0(int crt_id);
    private native int[] getMonitorMode0(int crt_id, int mode_idx);
    private native boolean setMonitorMode0(int crt_id, int nativeId, int rot);
}
