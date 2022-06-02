package project.components;

import javax.swing.*;

import project.Global;
import project.Manager;
import project.components.dialog.SelectApplicableFolders;
import project.components.dialog.SelectApplicableFolders.FolderSelectedListener;

import java.awt.event.*;
import java.awt.*;

public class BBlocView extends JPanel {
  private JButton openAddFolder;

  public BBlocView() {
    super();
    openAddFolder = new JButton("+");
    openAddFolder.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        SelectApplicableFolders s = new SelectApplicableFolders();
        s.setFolderSelectedListener(new FolderSelectedListener() {
          /// TODO: Make only one instance of this class or this instance of the program be visible at any given time.
          @Override
          public void folderSelected(String folder) {
            Global.f.pokeFolder(folder);
            System.out.println(folder);
          }
        });
        s.run();
      }
    });
    setPreferredSize(new Dimension(Manager.B_MIN_WIDTH, Manager.B_MIN_HEIGHT));
    setMinimumSize(new Dimension(Manager.B_MIN_WIDTH, Manager.B_MIN_HEIGHT));
    setMaximumSize(new Dimension(Manager.B_MAX_WIDTH, Manager.B_MAX_HEIGHT));
    System.out.println(
        Manager.B_MAX_HEIGHT + " " + Manager.B_MAX_WIDTH + " " + Manager.B_MIN_HEIGHT + " " + Manager.B_MIN_WIDTH);
    add(openAddFolder);
  }
}
