package com.halcyoninae.setup;

import com.halcyoninae.cloudspin.enums.SpeedStyle;
import com.halcyoninae.cloudspin.lib.gradient.GradientGenerator;
import com.halcyoninae.cosmos.Cosmos;
import com.halcyoninae.cosmos.dialog.ErrorWindow;
import com.halcyoninae.cosmos.events.ForceMaxSize;
import com.halcyoninae.cosmos.identifier.AttributableButton;
import com.halcyoninae.cosmos.theme.Theme;
import com.halcyoninae.cosmos.theme.ThemeBundles;
import com.halcyoninae.halcyon.connections.properties.ProgramResourceManager;
import com.halcyoninae.halcyon.connections.properties.ExternalResource;
import com.halcyoninae.halcyon.constant.ColorManager;
import com.halcyoninae.halcyon.constant.Global;
import com.halcyoninae.halcyon.constant.Manager;
import com.halcyoninae.halcyon.debug.Debugger;
import com.halcyoninae.halcyon.utils.ColorTool;
import com.halcyoninae.halcyon.utils.DeImage;
import com.halcyoninae.halcyon.utils.DeImage.Directional;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class runs a setup routine everytime
 * the main program is run. The setup routine helps
 * to make sure certain things are setup before the main
 * program is run; for example: making sure the correct
 * assets are present etc.. It also helps to setup
 * the program to a first time user.
 * <p>
 * To run this class, simply call the static main() instead
 * of creating a new object. Creating a new object will
 * only instantiate the GUI part and not the file access part.
 * Particularly it will be setup to handle some basic tasks, such as
 * determining what theme the user wants to use, etc.. All of this are run
 * after the threaded task schedulers have been started and the resource
 * properties have been loaded.
 *
 * @author Jack Meng
 * @since 3.3
 */
public class Setup extends JFrame implements Runnable {
    public static final File MAIN_LOCK_USER_SETUP = new File(
            ProgramResourceManager.PROGRAM_RESOURCE_FOLDER + ProgramResourceManager.FILE_SLASH
                    + ProgramResourceManager.RESOURCE_SUBFOLDERS[2]
                    + ProgramResourceManager.FILE_SLASH + "__user.lock");
    public static final String KILL_ARG = "kill";
    private static final transient java.util.List<SetupListener> listener = new ArrayList<>(); // must specify java.util
    // cuz
    public static boolean SETUP_EXISTS = false;
    private final JPanel content;
    // Java.AWT is stupid and its own
    // List class??!?!??!!?
    private final JLabel welcomeLabel;
    private final AttributableButton[] themeButtons;

    public Setup() {
        setTitle("Setup Routine");
        setIconImage(Global.rd.getFromAsImageIcon(Manager.PROGRAM_ICON_LOGO).getImage());
        setPreferredSize(new Dimension(350, 400));
        setResizable(true);
        addComponentListener(new ForceMaxSize(getPreferredSize().width, getPreferredSize().height,
                getPreferredSize().width, getPreferredSize().height));

        content = new JPanel();
        content.setPreferredSize(getPreferredSize());
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new OverlayLayout(welcomePanel));
        welcomePanel.setPreferredSize(new Dimension(350, 100));

        JPanel welcomePanelBack = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setComposite(
                        AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER,
                                0.5f));
                g2.drawImage(
                        DeImage.createGradient(
                                GradientGenerator.make(SpeedStyle.QUALITY, welcomePanel.getPreferredSize(),
                                        ColorTool.rndColor(), ColorTool.rndColor(), false),
                                200, 0, Directional.TOP),
                        0, 0, null);
                g2.dispose();
            }
        };
        welcomePanelBack.setPreferredSize(welcomePanel.getPreferredSize());
        welcomePanel.setOpaque(true);
        welcomePanel.setIgnoreRepaint(true);
        welcomePanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent arg0) {
                welcomePanelBack.repaint(150);
                welcomePanel.repaint(20);
                Runtime.getRuntime().gc();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent arg0) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent arg0) {
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent arg0) {
            }

        });

        welcomeLabel = new JLabel(
                "<html><p style=\"font-size:18px;\"><strong>Welcome :3</strong></p><p style=\"font-size:10px;color:#a6a6a6;\">Let's get started</p></html>");
        welcomeLabel.setPreferredSize(welcomePanel.getPreferredSize());
        welcomeLabel.setForeground(ColorManager.MAIN_FG_THEME);
        welcomeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(welcomePanelBack);

        JPanel buttonsCtrl = new JPanel();
        buttonsCtrl.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton okButton = new JButton("Moosic!");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireListeners();
                dispose();
            }
        });

        JButton cancel = new JButton("Quit");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Debugger.warn("Failing setup... [0]");
                System.exit(0);
            }
        });

        buttonsCtrl.add(okButton);
        buttonsCtrl.add(cancel);

        JScrollPane contentPanel = new JScrollPane();
        contentPanel.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        contentPanel.setPreferredSize(new Dimension(330, 250));
        contentPanel.setMinimumSize(contentPanel.getPreferredSize());

        JPanel contentPanelBack = new JPanel();
        contentPanelBack.setPreferredSize(contentPanel.getPreferredSize());
        contentPanelBack.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JLabel themeLabel = new JLabel(
                "<html><p style=\"font-size:9px;\"><strong>Preferred Color Theme:</strong></p></html>");
        Theme[] themes = ThemeBundles.getThemes();
        JPanel themePanel = new JPanel();
        themePanel.setPreferredSize(contentPanelBack.getPreferredSize());
        themeButtons = new AttributableButton[themes.length];
        themePanel.setLayout(new GridLayout(themes.length + 1, 1));
        themePanel.add(themeLabel);
        for (int i = 0; i < themeButtons.length; i++) {
            themeButtons[i] = new AttributableButton(themes[i].getThemeName());
            themeButtons[i].setAttribute(themes[i].getCanonicalName());
            themePanel.add(themeButtons[i]);
            AttributableButton t = themeButtons[i];
            t.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Cosmos.refreshUI(ThemeBundles.searchFor(t.getAttribute()));
                    } catch (UnsupportedLookAndFeelException e1) {
                        new ErrorWindow(e1.getMessage()).run();
                        ExternalResource.dispatchLog(e1);
                    }
                }
            });
        }
        contentPanelBack.add(themePanel);
        contentPanel.getViewport().add(contentPanelBack);

        content.add(welcomePanel);
        content.add(contentPanel);
        content.add(buttonsCtrl);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        getContentPane().add(content);
    }

    private static void fireListeners() {
        listener.forEach(x -> x.updateStatus(SETUP_EXISTS ? SetupStatus.PASSED : SetupStatus.NEW));
    }

    public static void addSetupListener(SetupListener... listeners) {
        for (SetupListener e : listeners) {
            listener.add(e);
        }
    }

    /**
     * Any applications that wants to the run a setup routine
     * SHOULD ONLY CALL THE MAIN METHOD.
     * Creating a new Setup object will only gurantee a launch
     * of the GUI part and can confuse any potential users.
     *
     * @param args No arguments are accepted / used in the setup routine
     */
    public static void main(String... args) {
        if (args != null && args.length > 0) {
            if (args[0].equals(KILL_ARG) && MAIN_LOCK_USER_SETUP.isFile() && MAIN_LOCK_USER_SETUP.exists()) {
                MAIN_LOCK_USER_SETUP.delete();
            }
        }
        if (MAIN_LOCK_USER_SETUP.isFile() && MAIN_LOCK_USER_SETUP.exists()) {
            SETUP_EXISTS = true;
            Debugger.info("Found a setup to be already run. Not deploying GUI");
            fireListeners();
        } else {
            SETUP_EXISTS = false;
            Debugger.info("Failed to find a lock. Deploying GUI");
            Setup s = new Setup();
            s.run();
            s.dispatchSetupYay();
        }
    }

    /**
     * Is called upon to send a notice to the file service
     * writer that we need to place the setup lock file signifying
     * that the user has performed a setup
     */
    public void dispatchSetupYay() {
        if (!MAIN_LOCK_USER_SETUP.exists() || !MAIN_LOCK_USER_SETUP.isFile()) {
            try {
                MAIN_LOCK_USER_SETUP.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try (PrintWriter bos = new PrintWriter(MAIN_LOCK_USER_SETUP)) {
                bos.write("Hello! This is just a file to keep track of whether you have setup the program or not :)");
                bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                new ErrorWindow(e.getMessage()).run();
            }
        }
    }

    /**
     * Any components to be added to the GUI
     * part of the program.
     *
     * @param components The components to be added to the GUI
     */
    public void addContent(JComponent... components) {
        for (JComponent c : components) {
            content.add(c);
            content.revalidate();
        }
    }

    @Override
    public void run() {
        pack();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getPreferredSize().width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - this.getPreferredSize().height) / 2);
        setVisible(true);
        Debugger.warn("Running setup routine...");
    }
}