package com.jackmeng.app.events;

import java.awt.event.*;

import com.jackmeng.Halcyon;
import com.jackmeng.constant.Global;
import com.jackmeng.debug.Program;

public class InstantClose implements WindowListener {

  @Override
  public void windowOpened(WindowEvent e) {
    // TO BE IMPLEMENTED OR IGNORED
  }

  @Override
  public void windowClosing(WindowEvent e) {
    Halcyon.bgt.getFrame().dispose();
    if (Global.player.getStream().isOpen() || Global.player.getStream().isPlaying()) {
      Global.player.getStream().close();
    }
    Program.forcedSavePlaylists();

    System.exit(0);
  }

  @Override
  public void windowClosed(WindowEvent e) {
    // TO BE IMPLEMENTED OR IGNORED
  }

  @Override
  public void windowIconified(WindowEvent e) {
    // TO BE IMPLEMENTED OR IGNORED
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
    // TO BE IMPLEMENTED OR IGNORED
  }

  @Override
  public void windowActivated(WindowEvent e) {
    // TO BE IMPLEMENTED OR IGNORED
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
    // TO BE IMPLEMENTED OR IGNORED
  }

}
