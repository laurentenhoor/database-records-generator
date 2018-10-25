package com.topdesk.si2011.dbgenerator.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;

import com.topdesk.si2011.dbgenerator.core.GenerationConfiguration;
import com.topdesk.si2011.dbgenerator.core.WorkBench;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.storage.JSonStructureInterpreter;

public class GeneratorUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private final JPanel pnlMain;
	private final JTree tree = new JTree();
	private final JTextArea txaDBOutput = new JTextArea();

	private final WorkBench workBench;
	private final GenerationConfiguration config;
	private JTextField footprintField;
	private DefaultListModel foundFootprints = new DefaultListModel();

	public GeneratorUI(WorkBench workBench, GenerationConfiguration config) {
		super("LIFT Database generator");
		this.workBench = workBench;
		this.config = config;

		pnlMain = new JPanel(new BorderLayout());

		setResizable(false);
		setSize(1024, 768);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		initiateMenu();
		initiatePanel();

	}

	private void initiateMenu() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();
		// Build the first menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);

		menuBar.add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("New...", KeyEvent.VK_N);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				workBench.clear();

				tree.setModel(new DBStructureTreeModel(workBench
						.getCurrentStructure()));
			}
		});
		menu.add(menuItem);

		// a group of JMenuItems
		menuItem = new JMenuItem("Load...", KeyEvent.VK_L);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Create a file chooser
				final JFileChooser fc = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"JSon structure files", "json");

				fc.setCurrentDirectory(new File(
						JSonStructureInterpreter.REST_STRUCTURE_JSON_DIRECTORY));
				fc.setFileFilter(filter);

				// In response to a button click:
				int returnVal = fc.showOpenDialog(pnlMain);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();

					System.out.print("Loading structure...");

					workBench.loadStructureFromFile(file.getAbsolutePath());

					System.out.println("done");

					tree.setModel(new DBStructureTreeModel(workBench
							.getCurrentStructure()));
				} else {
					System.err.println("NO FILE SELECTED");
				}
			}
		});
		menu.add(menuItem);

		// a group of JMenuItems
		menuItem = new JMenuItem("Save...", KeyEvent.VK_L);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Create a file chooser
				final JFileChooser fc = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"JSon structure files", "json");

				fc.setDialogTitle("Save");
				fc.setApproveButtonText("Save");
				fc.setCurrentDirectory(new File(
						JSonStructureInterpreter.REST_STRUCTURE_JSON_DIRECTORY));
				fc.setFileFilter(filter);

				// In response to a button click:
				int returnVal = fc.showOpenDialog(pnlMain);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();

					System.out.print("Saving structure...");

					if (file.getAbsolutePath().endsWith(".json")) {
						workBench.saveStructureToFile(file.getAbsolutePath());
					} else {
						workBench.saveStructureToFile(file.getAbsolutePath()
								+ ".json");
					}

					System.out.println("done");
				} else {
					System.err.println("NO FILE SELECTED");
				}
			}
		});
		menu.add(menuItem);

		// a group of radio button menu items
		menu.addSeparator();

		// a group of JMenuItems
		menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(menuItem);

		setJMenuBar(menuBar);
	}

	private void initiatePanel() {

		// Initiate the other panels
		initiateInformationPanel();
		initiateFootprintPanel();

		add(pnlMain);
		validate();

	}

	private void initiateInformationPanel() {
		// Create the panel
		JPanel pnlInformation = new JPanel(new BorderLayout());

		// Since the components have to be created already, here we don't have
		// to create any objects
		JScrollPane scrTree = new JScrollPane(tree);
		scrTree.setPreferredSize(new Dimension(320, 670));

		tree.setModel(new DBStructureTreeModel(null));

		// Set that a selection in the Tree will put the selected text in the
		// textfield
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			private String selectedTableName;

			public void valueChanged(TreeSelectionEvent evt) {

				// Get all nodes whose selection status has changed
				// Note that there is only one path
				TreePath path = evt.getPaths()[0];

				String element = (String) path.getLastPathComponent();
				if (!element.contains("(")) {
					selectedTableName = path.getLastPathComponent().toString();
					footprintField.setText(path.getLastPathComponent()
							.toString());
				}
			}
		});

		// Add the components
		pnlInformation.add(scrTree, BorderLayout.WEST);
		pnlInformation.add(txaDBOutput, BorderLayout.CENTER);

		// Add the panel
		pnlMain.add(pnlInformation, BorderLayout.WEST);

	}

	private void initiateFootprintPanel() {
		// Create the panel
		JPanel pnlFootprint = new JPanel(new BorderLayout());

		JPanel searchPanel = searchPanel();
		JPanel resultsPanel = resultsPanel();

		// Add the components
		pnlFootprint.add(searchPanel, BorderLayout.NORTH);
		pnlFootprint.add(resultsPanel, BorderLayout.EAST);
		pnlFootprint.setPreferredSize(new Dimension(320, 768));

		// Add the panel
		pnlMain.add(pnlFootprint, BorderLayout.CENTER);

	}

	private JPanel searchPanel() {
		JPanel textFieldPanel = new JPanel(new BorderLayout());

		String labelString = "Enter Table";

		JLabel label = new JLabel();

		// Create the text field and set it up.
		footprintField = new JTextField();
		footprintField.setColumns(20);
		JComponent field = footprintField;

		label = new JLabel(labelString, JLabel.TRAILING);
		label.setLabelFor(field);
		label.setPreferredSize(new Dimension(80, 20));
		textFieldPanel.add(label, BorderLayout.WEST);
		textFieldPanel.add(field, BorderLayout.EAST);

		// Create Soft footprint button
		JButton btnFindFootPrint = new JButton("Calculate 'soft' footprint");
		btnFindFootPrint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				updateFoundSoftFootprints();

			}

		});

		// Create Hard footprint button
		JButton btnFindFootPrintHard = new JButton("Calculate 'hard' footprint");
		btnFindFootPrintHard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				updateFoundHardFootprints();

			}

		});

		JPanel searchButtons = new JPanel(new BorderLayout());

		searchButtons.add(btnFindFootPrint, BorderLayout.NORTH);
		searchButtons.add(btnFindFootPrintHard, BorderLayout.SOUTH);
		btnFindFootPrintHard.setPreferredSize(new Dimension(getWidth(), 20));
		btnFindFootPrint.setPreferredSize(new Dimension(getWidth(), 20));

		textFieldPanel.add(searchButtons, BorderLayout.SOUTH);

		JTextField tf = null;
		tf = (JTextField) field;

		tf.addActionListener(this);

		return textFieldPanel;

	}

	private JPanel resultsPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		final JList list = new JList(foundFootprints);
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int index = list.locationToIndex(e.getPoint());
					footprintField.setText((String) foundFootprints.get(index));
				}
			}
		};
		list.addMouseListener(mouseListener);

		JScrollPane scrollPane = new JScrollPane(list);

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(320, getWidth()));

		return panel;

	}

	public void updateFoundHardFootprints() {
		foundFootprints.clear();

		List<DbLocation> footprints = config.getConfiguredDependency(
				footprintField.getText(), -1);

		for (DbLocation footprint : footprints) {
			foundFootprints.addElement(footprint.toString());
		}
	}

	public void updateFoundSoftFootprints() {
		foundFootprints.clear();

		List<DbLocation> footprints = config.getConfiguredDependency(
				footprintField.getText(), -1);

		for (DbLocation footprint : footprints) {
			foundFootprints.addElement(footprint.toString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// if(true) {
		updateFoundSoftFootprints();
		// } else {
		// actionPerformed();
		// }
	}
}
