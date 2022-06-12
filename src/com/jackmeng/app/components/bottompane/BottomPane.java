package com.jackmeng.app.components.bottompane;

import javax.swing.*;

import com.jackmeng.app.constant.Manager;
import com.jackmeng.app.utils.TextParser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Tabs in the BottomPane.
 * 
 * @author Jack Meng
 * @since 3.0
 */
public class BottomPane extends JTabbedPane {
  private JComponent[] tabs;

  /**
   * Represents an absolute list of folders
   * that have been selected by the user.
   */
  private ArrayList<String> foldersAbsolute;

  public BottomPane(List<BPTabs> tabs) {
    super();
    foldersAbsolute = new ArrayList<>();

    setPreferredSize(new Dimension(Manager.FILEVIEW_MAX_WIDTH, Manager.FILEVIEW_MIN_HEIGHT));
    setMaximumSize(new Dimension(Manager.FILEVIEW_MAX_WIDTH, Manager.FILEVIEW_MAX_HEIGHT));
    setMinimumSize(new Dimension(Manager.FILEVIEW_MIN_WIDTH, Manager.FILEVIEW_MIN_HEIGHT));
    this.tabs = new JComponent[tabs.size()];
    int i = 0;
    for (BPTabs t : tabs) {
      this.tabs[i] = t.getTabContent();
      addTab(t.restrainTabName() ? TextParser.strip(t.getTabName(), Manager.TAB_VIEW_MIN_TEXT_STRIP_LENGTH)
          : t.getTabName(), t.getTabIcon(), t.getTabContent(), t.getTabToolTip());
      i++;
    }
    setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    
  }

  public JComponent[] getTabs() {
    return tabs;
  }

  public void pokeNewFileListTab(String folder) {

  }
}
