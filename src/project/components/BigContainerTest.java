package project.components;

import javax.swing.*;

import project.Global;
import project.Manager;
import project.events.ForceMaxSize;

import java.awt.*;
import java.awt.event.*;

public class BigContainerTest implements Runnable {
  private JFrame bigContainer;
  private JSplitPane mainPane;

  public BigContainerTest(JSplitPane mainPane) {
    this.mainPane = mainPane;
    this.mainPane.setBorder(BorderFactory.createEmptyBorder());
    bigContainer = new JFrame("Exp ~ MP4J");
    bigContainer.setIconImage(Global.rd.getFromAsImageIcon(Manager.PROGRAM_ICON_LOGO).getImage());
    bigContainer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    bigContainer.setPreferredSize(new Dimension(Manager.MIN_WIDTH, Manager.MIN_HEIGHT));
    bigContainer.setMaximumSize(new Dimension(Manager.MAX_WIDTH, Manager.MAX_HEIGHT));
    bigContainer.addComponentListener(
        new ForceMaxSize(bigContainer, Manager.MAX_WIDTH, Manager.MAX_HEIGHT, Manager.MIN_WIDTH, Manager.MIN_HEIGHT));
    bigContainer.getContentPane().add(mainPane);
  }

  @Override
  public void run() {
    bigContainer.pack();
    bigContainer.setLocationRelativeTo(null);
    bigContainer.setVisible(true);
  }

}