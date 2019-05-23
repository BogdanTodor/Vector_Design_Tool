import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import static java.lang.Double.max;
import static java.lang.Math.min;

/** Used to construct the GUI, assign button functionality */
public class GUI extends JFrame implements Runnable {

    /** Instance of a menu bar*/
    JMenuBar menuBar;
    JPanel mainPanel;

    /** Instance of a toggle button*/
    JToggleButton fillToggleButton;

    /** The initial width of the GUI.*/
    public static final int WIDTH = 460;

    /** The initial height of the GUI.*/
    public static final int HEIGHT = 470;

    /** Stores the resizeable width of the GUI.*/
    public static double dynamicWidth;

    /** Stores the resizeable height of the GUI.*/
    public static double dynamicHeight;

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setMinimumSize(new Dimension(460, 470));

        addComponentsToPane(getContentPane());
        setJMenuBar(createMenuBar());
        setupKeyBindings();

        repaint();
        this.setVisible(true);
    }

    /**
     * Create the menu bar.
     */
    public JMenuBar createMenuBar() {
        JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("File");
        menuBar.add(menu);

        menuItem = new JMenuItem("New");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newButtonClick();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Open");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openButtonClick();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Save");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonClick();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Browse history");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();

                ActionListener menuListener = new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        Shape shape = null;
                        String vecItem = event.getActionCommand();
                        String[] splitted = vecItem.split(" ");
                        // Display output of the read lines.

                        if (splitted[0].equals("LINE")) {
                            shape = new Line(vecItem);
                        }
                        else if (splitted[0].equals("PLOT")) {
                            shape = new Plot(vecItem);
                        }
                        else if (splitted[0].equals("RECTANGLE")) {
                            shape = new Rectangle(vecItem);
                        }
                        else if (splitted[0].equals("PEN")) {
                            shape = new Pen(vecItem);
                        }
                        else if (splitted[0].equals("FILL")) {
                            shape = new Fill(vecItem);
                        }
                        else if (splitted[0].equals("ELLIPSE")) {
                            shape = new Ellipse(vecItem);
                        }
                        else if (splitted[0].equals("POLYGON")) {
                            shape = new Polygon(vecItem);
                        }

                        int index = Integer.MAX_VALUE;
                        for (int i = 0; i < Shape.lineCommands.size(); i++) {
                            if (shape.toString() == Shape.lineCommands.get(i).toString())
                            {
                                index = i;
                                System.out.println(i);
                                break;
                            }
                        }
                        while(Shape.lineCommands.size() > index+1) {
                            Shape.deletedLineCommands.add(Shape.lineCommands.get(index+1));
                            Shape.lineCommands.remove(index+1);
                        }
                        revalidate();
                        repaint();
                    }
                };
                for (int i = Shape.lineCommands.size(); i-- > 0; ) {
                    JMenuItem item = new JMenuItem(Shape.lineCommands.get(i).toString());
                    popup.add(item);
                    item.addActionListener(menuListener);
                }
                popup.show(getContentPane(), 10, 50);
                // End
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Recover changes");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LinkedList<Shape> lineCommandsOriginal;
                JPopupMenu popup = new JPopupMenu();
                ActionListener menuListener = new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        Shape shape = null;
                        String vecItem = event.getActionCommand();
                        String[] splitted = vecItem.split(" ");
                        // Display output of the read lines.

                        if (splitted[0].equals("LINE")) {
                            shape = new Line(vecItem);
                        }
                        else if (splitted[0].equals("PLOT")) {
                            shape = new Plot(vecItem);
                        }
                        else if (splitted[0].equals("RECTANGLE")) {
                            shape = new Rectangle(vecItem);
                        }
                        else if (splitted[0].equals("PEN")) {
                            shape = new Pen(vecItem);
                        }
                        else if (splitted[0].equals("FILL")) {
                            shape = new Fill(vecItem);
                        }
                        else if (splitted[0].equals("ELLIPSE")) {
                            shape = new Ellipse(vecItem);
                        }
                        else if (splitted[0].equals("POLYGON")) {
                            shape = new Polygon(vecItem);
                        }

                        int index = Integer.MAX_VALUE;
                        for (int i = 0; i < Shape.deletedLineCommands.size(); i++) {
                            if (shape.toString() == Shape.deletedLineCommands.get(i).toString())
                            {
                                index = i;
                                System.out.println(i);
                                break;
                            }
                        }
                        for(int i = index; i>=0; i--) {
                            System.out.println(i);
                            System.out.println(Shape.deletedLineCommands.get(i).toString());
                            Shape temp = Shape.deletedLineCommands.get(i);
                            Shape.lineCommands.add(temp);
                            Shape.deletedLineCommands.remove(temp);
                        }
                        revalidate();
                        repaint();
                    }
                };
                for (int i = Shape.deletedLineCommands.size(); i-- > 0; ) {
                    JMenuItem item = new JMenuItem(Shape.deletedLineCommands.get(i).toString());
                    popup.add(item);
                    item.addActionListener(menuListener);
                }
                popup.show(getContentPane(), 10, 50);
                // End
            }
        });
        menu.add(menuItem);

        menu = new JMenu("Edit");
        menuBar.add(menu);

        menuItem = new JMenuItem("Grid size");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editGridSize();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Pen color");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newPenColor = JColorChooser.showDialog(GUI.this, "Select Pen Color", Color.BLACK);
                if (newPenColor != null) {
                    String hex = String.format("#%02x%02x%02x", newPenColor.getRed(), newPenColor.getGreen(), newPenColor.getBlue());
                    Shape.lineCommands.add(new Pen("PEN " + hex));
                }
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Fill color");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newFillColor = JColorChooser.showDialog(GUI.this, "Select Fill Color", Color.BLACK);
                if (newFillColor != null) {
                    fillToggleButton.setSelected(true);
                    fillToggleButton.setText("Fill: On");
                    String hex = String.format("#%02x%02x%02x", newFillColor.getRed(), newFillColor.getGreen(), newFillColor.getBlue());
                    Shape.lineCommands.add(new Fill("FILL " + hex));
                }
            }
        });
        menu.add(menuItem);

        return menuBar;
    }

    /**
     * Adds components to the content pane, including:
     * Panels, Buttons, ColorChoosers, Tabs
     *
     * @parem pane the pane to add components to.
     *
     */
    public void addComponentsToPane(Container pane) {
        // Setup layout and components
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
//        JPanel mainPanel, leftPanel, rightPanel, bottomPanel;
        JPanel leftPanel, rightPanel, bottomPanel;
        JButton undoButton;
        ButtonGroup drawingButtons = new ButtonGroup();
        JToggleButton plotButton, lineButton, rectangleButton, ellipseButton, polyButton, gridButton;
        JColorChooser penColorChooser, fillColorChooser;
        JTabbedPane colorTabs;

        // Initialise require components first
        mainPanel = new drawingPanel();
        fillColorChooser = new JColorChooser();

        /*
        ADD JLABELS
         */
//        JLabel polyhelp =new JLabel("Polygon: Left-click to place points.\n Right click to finish");
        JLabel[] labels = new JLabel[5];
        JLabel plotHelp = new JLabel("<html>Plot tool tips:<br/>Click left mouse button<br/>to place point.</html>", SwingConstants.CENTER);
        JLabel lineHelp = new JLabel("<html>Line tool tips:<br/>Press left mouse button, <br/>drag and release to place.</html>", SwingConstants.CENTER);
        JLabel ellipseHelp = new JLabel("<html>Ellipse tool tips:<br/>Press left mouse button, <br/>drag and release to place.</html>", SwingConstants.CENTER);
        JLabel rectangleHelp = new JLabel("<html>Rectangle tool tips:<br/>Press left mouse button, <br/>drag and release to place.</html>", SwingConstants.CENTER);
        JLabel polyHelp = new JLabel("<html>Polygon tool tips:<br/>Left-click to place points.<br/>Right click to connect.</html>", SwingConstants.CENTER);
        labels[0] = (plotHelp);
        labels[1] = (lineHelp);
        labels[2] = (ellipseHelp);
        labels[3] = (rectangleHelp);
        labels[4] = (polyHelp);
        Font f = new Font("Ariel",Font.PLAIN,12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy = 5;
        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(f);
            labels[i].setVisible(false);
            pane.add(labels[i], c);
            pane.add(labels[i], c);
        }

        /*
        ADD BUTTONS
         */
//        c.fill = GridBagConstraints.HORIZONTAL;

        plotButton = new JToggleButton("Plot");
        plotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel label : labels) {
                    label.setVisible(false);
                }
                plotHelp.setVisible(plotButton.isSelected());
            }
        });
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        drawingButtons.add(plotButton);
        pane.add(plotButton, c);

        lineButton = new JToggleButton("Line");
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel label : labels) {
                    label.setVisible(false);
                }
                lineHelp.setVisible(lineButton.isSelected());
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        drawingButtons.add(lineButton);
        pane.add(lineButton, c);

        rectangleButton = new JToggleButton("Rectangle");
        rectangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel label : labels) {
                    label.setVisible(false);
                }
                rectangleHelp.setVisible(rectangleButton.isSelected());
            }
        });
        c.gridx = 1;
        c.gridy = 0;
        drawingButtons.add(rectangleButton);
        pane.add(rectangleButton, c);

        polyButton = new JToggleButton(" Polygon ");
        polyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel label : labels) {
                    label.setVisible(false);
                }
                polyHelp.setVisible(polyButton.isSelected());
            }
        });
        c.gridx = 1;
        c.gridy = 1;
        drawingButtons.add(polyButton);
        pane.add(polyButton, c);

        ellipseButton = new JToggleButton("Ellipse");
        ellipseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel label : labels) {
                    label.setVisible(false);
                }
                ellipseHelp.setVisible(ellipseButton.isSelected());
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        drawingButtons.add(ellipseButton);
        pane.add(ellipseButton, c);

        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        c.gridx = 0;
        c.gridy = 2;
        pane.add(undoButton, c);

        fillToggleButton = new JToggleButton("Fill: Off");
        fillToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fillToggleButton.isSelected()) {
                    Color newFillColor = fillColorChooser.getColor();
                    String hex = String.format("#%02x%02x%02x", newFillColor.getRed(), newFillColor.getGreen(), newFillColor.getBlue());
                    Shape.lineCommands.add(new Fill("FILL " + hex));
                    fillToggleButton.setText("Fill: On");
                }
                else if (!fillToggleButton.isSelected()) {
                    Shape.lineCommands.add(new Fill("FILL OFF"));
                    fillToggleButton.setText("Fill: Off");
                }
                revalidate();
                repaint();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        pane.add(fillToggleButton, c);

        gridButton = new JToggleButton("Grid: Off");
        gridButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gridButton.isSelected()) {
                    gridButton.setText("Grid: On");

                }
                else if (!gridButton.isSelected()) {
                    gridButton.setText("Grid: Off");
                }
                revalidate();
                repaint();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        pane.add(gridButton, c);

        /*
        ADD COLOR CHOOSERS
         */
        // Pen Color Chooser
        penColorChooser = new JColorChooser();
        penColorChooser.setPreviewPanel(new JPanel()); // removes the preview panel
        AbstractColorChooserPanel[] panels = penColorChooser.getChooserPanels();
        for (AbstractColorChooserPanel accp : panels) { // remove other tabs
            if (!accp.getDisplayName().equals("Swatches")) {
                penColorChooser.removeChooserPanel(accp);
            }
        }
        ColorSelectionModel penModel = penColorChooser.getSelectionModel();
        ChangeListener changeListenerPen = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                Color newPenColor = penColorChooser.getColor();
                String hex = String.format("#%02x%02x%02x", newPenColor.getRed(), newPenColor.getGreen(), newPenColor.getBlue());
                Shape.lineCommands.add(new Pen("PEN " + hex));
            }
        };
        penModel.addChangeListener(changeListenerPen);

        // Fill Color Chooser
        fillColorChooser.setPreviewPanel(new JPanel()); // removes the preview panel
        AbstractColorChooserPanel[] panels2 = fillColorChooser.getChooserPanels();
        for (AbstractColorChooserPanel accp : panels2) { // remove other tabs
            if (!accp.getDisplayName().equals("Swatches")) {
                fillColorChooser.removeChooserPanel(accp);
            }
        }
        ColorSelectionModel fillModel = fillColorChooser.getSelectionModel();
        ChangeListener changeListenerFill = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                if (fillToggleButton.isSelected()) {
                    Color newFillColor = fillColorChooser.getColor();
                    String hex = String.format("#%02x%02x%02x", newFillColor.getRed(), newFillColor.getGreen(), newFillColor.getBlue());
                    Shape.lineCommands.add(new Fill("FILL " + hex));
                }
            }
        };
        fillModel.addChangeListener(changeListenerFill);

        // Add color choosers to respective tabs
        colorTabs = new JTabbedPane();
        colorTabs.addTab("Pen", penColorChooser);
        colorTabs.addTab("Fill", fillColorChooser);
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        pane.add(colorTabs,c);

        /*
        ADD PANELS
         */

        // Left panel
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.LIGHT_GRAY);
        setConstraints(leftPanel,0,0,2,6,0,0,c,pane);

        // Right panel
        rightPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(pane.getWidth() - mainPanel.getWidth() - leftPanel.getWidth() - 3, 0);
            }
        };
        rightPanel.setBackground(Color.LIGHT_GRAY);
        setConstraints(rightPanel,3,0,1,6,0,0,c,pane);

        // Bottom panel
        bottomPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, pane.getHeight() - mainPanel.getHeight() - menuBar.getHeight() + 20);
            }
        };
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        setConstraints(bottomPanel,0,6,4,1,0,0,c,pane);

        // Main drawing canvas panel
        setConstraints(mainPanel,2,0,1,6,1,1,c,pane);

        // Keep track of main drawing canvas panel size
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent c) {
                dynamicHeight = c.getComponent().getHeight();
                dynamicWidth = c.getComponent().getHeight();
            }
        });

        // Drawing canvas mouse listener: Listens for clicks (plot and polygon),
        // presses and releases (Line, rectangle, ellipse)
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double x = round(e.getX()/dynamicWidth,2);
                double y = round(e.getY()/dynamicHeight,2);
                if(plotButton.isSelected()) {
                    Shape.lineCommands.add(new Plot("PLOT "+x+" "+y));
                }
                if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1 && polyButton.isSelected()) {
                    System.out.println("Right CLICK");
                    String input = "POLYGON";
                    for (int i = 0; i < Polygon.getClickCount(); i++) {
                        input+= " " + Polygon.ClickCoordsX[i] + " " + Polygon.ClickCoordsY[i];
                    }
                    input += " " + Polygon.ClickCoordsX[0] + " " + Polygon.ClickCoordsY[0];
                    System.out.println(input);
                    Shape.lineCommands.add(new Polygon(input));
                    Polygon.resetClickCount();

                }
                else if(polyButton.isSelected()) {
                    System.out.println("Clicked " +x+" "+y);
                    System.out.println("Click count " + Polygon.getClickCount());
                    Polygon.ClickCoordsX[Polygon.getClickCount()] = x;
                    Polygon.ClickCoordsY[Polygon.getClickCount()] = y;
                    Polygon.addClick();
                }
                revalidate();
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                double x1 = round(e.getX()/dynamicWidth,2);
                double y1 = round(e.getY()/dynamicHeight,2);
                if(lineButton.isSelected()) {
                    Line.firstClick(x1, y1);
                }
                else if(rectangleButton.isSelected()) {
                    Rectangle.firstClick(x1, y1);
                }
                else if(ellipseButton.isSelected()) {
                    Ellipse.firstClick(x1, y1);
                }
                revalidate();
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                double x2 = round(e.getX()/dynamicWidth,2);
                double y2 = round(e.getY()/dynamicHeight,2);
                if (lineButton.isSelected()) {
                    Shape.lineCommands.add(new Line("LINE "+ Line.getFirstClickX()+ " " + Line.getFirstClickY() + " " +x2+" "+y2));
                }
                else if (rectangleButton.isSelected()) {
                    Shape.lineCommands.add(new Rectangle("RECTANGLE "+ min(Rectangle.getFirstClickX(),x2)+ " " + min(Rectangle.getFirstClickY(),y2) + " " + max(Rectangle.getFirstClickX(),x2)+" "+max(Rectangle.getFirstClickY(),y2)));
                }
                else if (ellipseButton.isSelected()) {
                    Shape.lineCommands.add(new Ellipse("ELLIPSE "+ min(Ellipse.getFirstClickX(),x2)+ " " + min(Ellipse.getFirstClickY(),y2) + " " +max(Ellipse.getFirstClickX(),x2)+" "+max(Ellipse.getFirstClickY(),y2)));
                }
                revalidate();
                repaint();
            }
        });
    }

    /**
     * Sets constraints of the panel component and adds it to the pane.
     *
     * @parem panel the component to add to the pane
     * @parem gridx the x position
     * @parem gridy the y position
     * @parem gridwidth the width
     * @parem gridheight the height
     * @parem weightx x weighting
     * @parem weighty y weighting
     * @parem c the Grid Bag Constraints
     */
    public static void setConstraints(JPanel panel, int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty, GridBagConstraints c, Container pane) {
        c.fill = GridBagConstraints.BOTH;
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.weighty = weighty;
        pane.add(panel, c);
    }

    /** Prompts user to enter an appropriate grid size.*/
    public static void editGridSize() {
        String input = JOptionPane.showInputDialog(null, "Enter grid size ranging from 0 to 0.5");
        System.out.println(input);
    }

    /** Setup key bindings */
    public void setupKeyBindings() {
        String UNDO = "Undo action key";
        Action undoAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        };

        mainPanel.getActionMap().put(UNDO, undoAction);

        InputMap[] inputMaps = new InputMap[] {
                mainPanel.getInputMap(JComponent.WHEN_FOCUSED),
                mainPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT),
                mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW),
        };
        for(InputMap i : inputMaps) {
            // UNDO support: On Mac = cmd-z, on Windows = ctrl-z
            i.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), UNDO);
        }
    }

//    /** Display user controls on left panel. Adapts to current selected control. */
//    public void userControls() {
//        Jlabel
//    }

    /**
     * When menu bar > 'File' > 'new' is clicked, clear the current list of VEC shapes and repaint the canvas blank.
     * Fill toggle button is toggled to off.
     * Pen color defaults to black.
     *
     */
    public void newButtonClick() {
        Shape.lineCommands.clear();
        resetFillButton();
    }

    /** Untoggles Fill button, and updates text to "Fill: Off". */
    public void resetFillButton() {
        fillToggleButton.setSelected(false);
        fillToggleButton.setText("Fill: Off");
        revalidate();
        repaint();
    }

    /**
     * When menu bar > 'File' > 'open' is clicked, opens up a file chooser. Files with extension
     * '.VEC' are filtered for. When the file is selected, overwrite the current VEC shape file
     * with the target file, and immediately show on the drawing canvas. Fill and pen color
     * configuration is maintained from the open file.
     *
     */
    public void openButtonClick() {
        JFileChooser fileChooser = new JFileChooser();
        // Creates filter for VEC files, adds the filter to the open event and sets the filter as the default file type.
        FileFilter VECFilter = new FilterFileType(".vec", "VEC Documents");
        fileChooser.addChoosableFileFilter(VECFilter);
        fileChooser.setFileFilter(VECFilter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileReaderClass.open(selectedFile.getPath());
                if (Colors.isFillOn) {
                    fillToggleButton.setSelected(true);
                    fillToggleButton.setText("Fill: On");
                }
                if (!Colors.isFillOn) {
                    resetFillButton();
                }
                revalidate();
                repaint();
            }
            catch (Exception ex) {
            }
        }
    }

    /**
     * When menu bar > 'File' > 'save' is clicked, opens up a file chooser. Files with extension
     * '.VEC' are filtered for. When the file name is specified, save the current VEC shape file
     * to the target file. The extension ".VEC" is added automatically.
     */
    public void saveButtonClick() {
        // set file filter
        JFileChooser fileChooser = new JFileChooser();

        FileFilter VECFilter = new FilterFileType(".vec", "VEC Documents");
        fileChooser.addChoosableFileFilter(VECFilter);
        fileChooser.setFileFilter(VECFilter);

        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileReaderClass.save(selectedFile.getPath()+".vec");
                revalidate();
                repaint();
            }
            catch (Exception ex) {
            }
        }
    }

//    /**
//     * Configure the grid.
//     *
//     * @parem state if the grid is on or off.
//     *
//     */
//    public void configureGrid(boolean state) {
//        if (state == true) {
//            gridButton.setText("Grid: On");
//        }
//    }

    /**
     * Helper function to round coordinates on the drawing canvas to 2 decimal places
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /** Removes the last VEC command drawn on screen or opened from file. */
    public void undo() {
        try {Shape.lineCommands.removeLast(); }
        catch (Exception undoException) {
        }
        revalidate();
        repaint();
    }

    /**
     * Undo History Support: brings up a list containing the history of drawing
     * commands for that image, allowing the user to step back and forth through
     * the creation of that image
     */
    public void undoHistory() {
        new historyPopup(this);
        revalidate();
        repaint();
    }

    public void refresh() {
        this.revalidate();
        this.repaint();
    }

    /** Calls the method which creates the GUI.*/
    @Override
    public void run() {
        createAndShowGUI();
    }

    /** The main method which instantiates the GUI.*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GUI());
    }
}

class drawingPanel extends JPanel {

    /** Sets default size to 300x300 and adds a Gray line border.*/
    public drawingPanel() {
        setSize(300,300);
        setBorder(new LineBorder(Color.GRAY));
        setVisible(true);
    }

    /**
     * Sets the size of the drawing panel to be the maximum possible square size of available space.
     * Iterates over each VEC shape command and draws to screen.
     *
     */
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = min(getWidth(), getHeight());
        setSize(size, size);
        g.setColor(Color.BLACK);
        Colors.setPenColor(Color.BLACK);
        Colors.setIsFillOn(false);
        for (Shape shape : Shape.lineCommands) {
            shape.drawShape(g, size);
        }
    }
}
