package com.jackmeng.app;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import com.jackmeng.ProjectManager;
import com.jackmeng.app.components.BigContainerTest;
import com.jackmeng.app.components.bbloc.BBlocButton;
import com.jackmeng.app.components.bbloc.BBlocView;
import com.jackmeng.app.components.bbloc.buttons.AddFolder;
import com.jackmeng.app.components.bbloc.buttons.GenericWebsiteLinker;
import com.jackmeng.app.components.bbloc.buttons.RefreshFileView;
import com.jackmeng.app.components.bbloc.buttons.Settings;
import com.jackmeng.app.components.bottompane.BPTabs;
import com.jackmeng.app.components.bottompane.BottomPane;
import com.jackmeng.app.components.dialog.ErrorWindow;
import com.jackmeng.app.components.toppane.TopPane;
import com.jackmeng.app.connections.discord.Discordo;
import com.jackmeng.app.connections.properties.ResourceFolder;
import com.jackmeng.app.constant.Global;
import com.jackmeng.app.constant.Manager;
import com.jackmeng.app.constant.ProgramResourceManager;

public class Halcyon {
  public static BigContainerTest bgt;

  public static void main(String... args) {
    try {
      new ThreadedScheduler();
      TopPane tp = new TopPane(Global.ifp, Global.bctp);
      Global.ifp.addInfoViewUpdateListener(Global.bctp);
      JSplitPane bottom = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      bottom.setMinimumSize(new Dimension(Manager.MIN_WIDTH, Manager.MIN_HEIGHT / 2));
      bottom.setPreferredSize(new Dimension(Manager.MIN_WIDTH, Manager.MIN_HEIGHT / 2));
      bottom.setMaximumSize(new Dimension(Manager.MAX_WIDTH, Manager.MAX_HEIGHT / 2));
      ArrayList<BBlocButton> bb = new ArrayList<>();
      bb.add(new AddFolder());
      bb.add(new RefreshFileView());
      bb.add(new Settings());
      bb.add(GenericWebsiteLinker.getButton(ProjectManager.PROJECT_GITHUB_PAGE, Manager.PROJECTPAGE_BUTTON_TOOLTIP,
          Global.rd.getFromAsImageIcon(Manager.GITHUB_LOGO_LIGHT)));
      BBlocView b = new BBlocView();
      b.addBBlockButtons(bb.toArray(new BBlocButton[bb.size()]));
      bottom.add(b);
      bottom.add(Global.bp);

      ResourceFolder.checkResourceFolder(ProgramResourceManager.PROGRAM_RESOURCE_FOLDER);
      for (String str : ProgramResourceManager.RESOURCE_SUBFOLDERS) {
        ResourceFolder.createFolder(str);
      }

      JSplitPane m = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tp, bottom);
      bgt = new BigContainerTest(m);
      bgt.run();

      // IGNORED FOR NOW: Global.ifp.addInfoViewUpdateListener(new Discordo());

    } catch (Exception ex) {
      ResourceFolder.dispatchLog(ex);
      new ErrorWindow(ex.toString()).run();
    }
  }
}
