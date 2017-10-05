package it.unimib.disco.essere.WekaNose.gui.machine.learning.approach;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import weka.classifiers.Classifier;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.gui.GenericObjectEditor;
import weka.gui.explorer.ExplorerDefaults;
import weka.gui.PropertyPanel;

import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.UIManager;

import it.unimib.disco.essere.WekaNose.gui.ErrorWindows;
import it.unimib.disco.essere.WekaNose.gui.FinalWindows;
import it.unimib.disco.essere.WekaNose.outline.MLAHandler;
import it.unimib.disco.essere.experiment.DataExperimenter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import javax.swing.SwingConstants;

public class PropertiesCreator {

	private JFrame frmPropertyFileCreation;
	private String datasetPath;
	private String propertiesPath = null;
	private List< GenericObjectEditor> classifiers;
	private JPanel panel;
	private int x;
	private int y;
	private int width;
	private int height;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PropertiesCreator window = new PropertiesCreator();
					window.frmPropertyFileCreation.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PropertiesCreator() {
		this.classifiers = new ArrayList< GenericObjectEditor>();
		initialize();
		frmPropertyFileCreation.setVisible(true);
		this.frmPropertyFileCreation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() { 

		frmPropertyFileCreation = new JFrame();
		frmPropertyFileCreation.setTitle("Property file creation");
		frmPropertyFileCreation.setBounds(100, 100, 1000, 550);
		frmPropertyFileCreation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JEditorPane dtrpnSetUpThe = new JEditorPane();
		dtrpnSetUpThe.setEditable(false);
		dtrpnSetUpThe.setBackground(UIManager.getColor("Button.background"));
		dtrpnSetUpThe.setFont(new Font("Dialog", Font.BOLD, 14));
		dtrpnSetUpThe.setText("Set up the experiment");
		dtrpnSetUpThe.setBounds(30, 360, 200, 21);
		this.frmPropertyFileCreation.getContentPane().add(dtrpnSetUpThe);
		
		JEditorPane dtrpnExperimentType = new JEditorPane();
		dtrpnExperimentType.setBackground(UIManager.getColor("Button.background"));
		dtrpnExperimentType.setText("experiment type:");
		dtrpnExperimentType.setBounds(30, 390, 120, 21);
		this.frmPropertyFileCreation.getContentPane().add(dtrpnExperimentType);
		
		JEditorPane dtrpnSplitType = new JEditorPane();
		dtrpnSplitType.setText("split type:");
		dtrpnSplitType.setBackground(UIManager.getColor("Button.background"));
		dtrpnSplitType.setBounds(340, 390, 70, 21);
		this.frmPropertyFileCreation.getContentPane().add(dtrpnSplitType);
		
		JEditorPane dtrpnRuns = new JEditorPane();
		dtrpnRuns.setText("runs:");
		dtrpnRuns.setBackground(UIManager.getColor("Button.background"));
		dtrpnRuns.setBounds(615, 390, 45, 21);
		this.frmPropertyFileCreation.getContentPane().add(dtrpnRuns);
		
		JEditorPane dtrpnFolds = new JEditorPane();
		dtrpnFolds.setText("folds:");
		dtrpnFolds.setBackground(UIManager.getColor("Button.background"));
		dtrpnFolds.setBounds(800, 390, 45, 21);
		this.frmPropertyFileCreation.getContentPane().add(dtrpnFolds);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(DataExperimenter.getExptypeValues()));
		comboBox.setBounds(155, 390, 160, 24);
		this.frmPropertyFileCreation.getContentPane().add(comboBox);
		
		final JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(new DefaultComboBoxModel<String>(DataExperimenter.getSplittypeValues()));
		comboBox_1.setBounds(420, 390, 160, 24);
		this.frmPropertyFileCreation.getContentPane().add(comboBox_1);
		
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(10, 0, null, 1));
		spinner.setBounds(665, 390, 90, 20);
		this.frmPropertyFileCreation.getContentPane().add(spinner);
		
		final JSpinner spinner_1 = new JSpinner(new SpinnerNumberModel(10, 0, null, 1));
		spinner_1.setBounds(850, 390, 90, 20);
		this.frmPropertyFileCreation.getContentPane().add(spinner_1);
		
		final JCheckBox chckbxSerialize = new JCheckBox("serialize the machine learning algorithms");
		chckbxSerialize.setSelected(true);
		chckbxSerialize.setBounds(30, 425, 320, 23);
		this.frmPropertyFileCreation.getContentPane().add(chckbxSerialize);

		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 0, 10));
		panel.add(new AlgorithmPanel(0));

		final JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBounds(20, 180, 660, 110);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(690, 20, 2, 330);
		frmPropertyFileCreation.getContentPane().add(separator_1);

		JEditorPane dtrpnOrSelectA = new JEditorPane();
		dtrpnOrSelectA.setText("Or select a properties file\nalready created");
		dtrpnOrSelectA.setFont(new Font("Dialog", Font.BOLD, 14));
		dtrpnOrSelectA.setEditable(false);
		dtrpnOrSelectA.setBackground(UIManager.getColor("Button.background"));
		dtrpnOrSelectA.setBounds(740, 30, 220, 60);
		frmPropertyFileCreation.getContentPane().add(dtrpnOrSelectA);	

		JEditorPane dtrpnPropertyFileCreation = new JEditorPane();
		dtrpnPropertyFileCreation.setBounds(260, 15, 180, 21);
		dtrpnPropertyFileCreation.setEditable(false);
		dtrpnPropertyFileCreation.setBackground(UIManager.getColor("Button.background"));
		dtrpnPropertyFileCreation.setFont(new Font("Dialog", Font.BOLD, 14));
		dtrpnPropertyFileCreation.setText("Property file creation");
		frmPropertyFileCreation.getContentPane().add(dtrpnPropertyFileCreation);

		JEditorPane dtrpnDatasetPath = new JEditorPane();
		dtrpnDatasetPath.setBounds(50, 60, 110, 20);
		dtrpnDatasetPath.setFont(new Font("Dialog", Font.PLAIN, 14));
		dtrpnDatasetPath.setBackground(UIManager.getColor("Button.background"));
		dtrpnDatasetPath.setEditable(false);
		dtrpnDatasetPath.setText("Dataset path:");
		frmPropertyFileCreation.getContentPane().add(dtrpnDatasetPath);
		
		final JEditorPane dtrpnPleaseWait = new JEditorPane();
		dtrpnPleaseWait.setBackground(UIManager.getColor("Button.background"));
		dtrpnPleaseWait.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		dtrpnPleaseWait.setBounds(700, 475, 250, 60);
		frmPropertyFileCreation.getContentPane().add(dtrpnPleaseWait);

		final JButton btnNext = new JButton("Start the experiment");
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dtrpnPleaseWait.setForeground(Color.BLUE);
				String timestamp = new Timestamp(System.currentTimeMillis()).toString();
				dtrpnPleaseWait.setText("The experiment is started,\nplease wait...\n(started at " 
						+ timestamp.substring(timestamp.indexOf(" "), timestamp.length())
						+  ")");
				
				frmPropertyFileCreation.revalidate();
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					MLAHandler mla = null;
					System.out.println(propertiesPath);
					if(propertiesPath == null) {
						mla = new MLAHandler();
						ArrayList<String> stringClassifiers = new ArrayList<String>();
						for( GenericObjectEditor c: classifiers) {
							stringClassifiers.add(prepareStringClassifier(c));
						}
						mla.generateProperties(datasetPath, stringClassifiers);
					}else {
						mla = new MLAHandler(propertiesPath);
					}
					mla.runExperiment((String) comboBox.getSelectedItem(), 
							  (String) comboBox_1.getSelectedItem(), 
							  (Integer) spinner.getValue(), 
							  (Integer) spinner_1.getValue(), 
							  chckbxSerialize.isSelected());
					FinalWindows fin = new FinalWindows("Experiment terminated", "Check out the results", mla.getResultsPath());
					frmPropertyFileCreation.dispose();
				} catch (Exception e) {
					dtrpnPleaseWait.setForeground(Color.RED);
					dtrpnPleaseWait.setText("Error occured!");
					//label.setVisible(false);
					StringWriter stackTrace = new StringWriter();
					e.printStackTrace(new PrintWriter(stackTrace));
					ErrorWindows error = new ErrorWindows(e.getMessage(), stackTrace.toString());
				}
			}
		});
		btnNext.setBounds(350, 500, 300, 25);
		btnNext.setEnabled(false);
		frmPropertyFileCreation.getContentPane().add(btnNext);

		JSeparator separator = new JSeparator();
		separator.setBackground(UIManager.getColor("Button.background"));
		separator.setBounds(30, 355, 940, 2);
		separator.setForeground(Color.BLACK);
		frmPropertyFileCreation.getContentPane().add(separator);

		
		final JFormattedTextField textFieldDataset = new JFormattedTextField();
		
		final JFormattedTextField formattedTextField = new JFormattedTextField();
		formattedTextField.addKeyListener(new KeyAdapter() { 
			@Override
			public void keyReleased(KeyEvent e) {
				if(!formattedTextField.getText().replaceAll(" ", "").equals("")){
					propertiesPath = formattedTextField.getText();
					btnNext.setEnabled(true);	
				}
				else {
					propertiesPath = null;
					if(formattedTextField.getText().replaceAll(" ", "").equals(""))
						btnNext.setEnabled(false);
				}
			}
		});
		formattedTextField.setBounds(720, 113, 250, 21);
		frmPropertyFileCreation.getContentPane().add(formattedTextField);

		final JFileChooser browse_properties = new JFileChooser();
		browse_properties.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		JButton button = new JButton("Browse");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int returnVal = browse_properties.showOpenDialog(new JPanel());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = browse_properties.getSelectedFile();
					formattedTextField.setText(file.getAbsolutePath());
					propertiesPath = formattedTextField.getText();
					btnNext.setEnabled(true);
				} 
			}
		});
		button.setBounds(793, 177, 100, 25);
		frmPropertyFileCreation.getContentPane().add(button);

		
		textFieldDataset.setBounds(160, 60, 380, 21);
		textFieldDataset.addKeyListener(new KeyAdapter() { 
			@Override
			public void keyReleased(KeyEvent e) {
				if(!textFieldDataset.getText().replaceAll(" ", "").equals("")){
					datasetPath= textFieldDataset.getText();
					btnNext.setEnabled(true);	
				}
				else {
					if(formattedTextField.getText().replaceAll(" ", "").equals(""))
						btnNext.setEnabled(false);
				}
			}
		});
		frmPropertyFileCreation.getContentPane().add(textFieldDataset);

		final JFileChooser browse_dataset= new JFileChooser();
		browse_dataset.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(550, 57, 100, 25);
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int returnVal = browse_dataset.showOpenDialog(new JPanel());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = browse_dataset.getSelectedFile();
					textFieldDataset.setText(file.getAbsolutePath());
					datasetPath= textFieldDataset.getText();
					btnNext.setEnabled(true);
				} 
			}
		});
		frmPropertyFileCreation.getContentPane().add(btnBrowse);

		JEditorPane dtrpnSelectTheMachine = new JEditorPane();
		dtrpnSelectTheMachine.setBounds(50, 115, 500, 21);
		dtrpnSelectTheMachine.setBackground(UIManager.getColor("Button.background"));
		dtrpnSelectTheMachine.setEditable(false);
		dtrpnSelectTheMachine.setFont(new Font("Dialog", Font.PLAIN, 14));
		dtrpnSelectTheMachine.setText("Select the machine learning arlgoritms:");
		frmPropertyFileCreation.getContentPane().add(dtrpnSelectTheMachine);

		final JButton btnAddAnotherAlgorithm = new JButton("Add another algorithm");
		btnAddAnotherAlgorithm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				panel.add(new AlgorithmPanel(20));
			}
		});
		btnAddAnotherAlgorithm.setToolTipText("add another box whitin you can select a mchine learning algortim");
		btnAddAnotherAlgorithm.setBounds(250, 300, 200, 25);
		frmPropertyFileCreation.getContentPane().add(btnAddAnotherAlgorithm);
		btnAddAnotherAlgorithm.setVisible(true);

		JEditorPane dtrpnForMoreInfo = new JEditorPane();
		dtrpnForMoreInfo.setBounds(50, 140, 600, 40);
		dtrpnForMoreInfo.setEditable(false);
		dtrpnForMoreInfo.setBackground(UIManager.getColor("Button.background"));
		dtrpnForMoreInfo.setText("for more info about setting of the algorithms: http://weka.wikispaces.com/Classifiers\nfor  a tutorial about it: https://youtu.be/Nje8mblA7bs?t=269");
		frmPropertyFileCreation.getContentPane().add(dtrpnForMoreInfo);

		JEditorPane dtrpnrequired = new JEditorPane();
		dtrpnrequired.setBounds(60, 75, 80, 20);
		dtrpnrequired.setBackground(UIManager.getColor("Button.background"));
		dtrpnrequired.setText("(required)");
		frmPropertyFileCreation.getContentPane().add(dtrpnrequired);

		frmPropertyFileCreation.getContentPane().add(scrollPane);


	}

	private class AlgorithmPanel extends JPanel{

		AlgorithmPanel myInstance = this;

		public AlgorithmPanel(int y_offset) {
			this.setLayout(new GridLayout(0, 2, 5, 5));

			if(y_offset == 0) {
				final GenericObjectEditor m_ClassifierEditor = new GenericObjectEditor(true);
				m_ClassifierEditor.setClassType(Classifier.class);
				m_ClassifierEditor.setValue(ExplorerDefaults.getClassifier());
				frmPropertyFileCreation.getContentPane().setLayout(null);
				PropertyPanel classifierPanel = new PropertyPanel(m_ClassifierEditor);
				x = classifierPanel.getX();
				y = classifierPanel.getY();
				width = classifierPanel.getWidth();
				height = classifierPanel.getHeight();
				classifiers.add(m_ClassifierEditor);
				classifierPanel.setLayout(new GridLayout(0, 1, 0, 0));
				this.add(classifierPanel );
			}else {
				GenericObjectEditor m_ClassifierEditor = new GenericObjectEditor(true);
				m_ClassifierEditor.setClassType(Classifier.class);
				m_ClassifierEditor.setValue(ExplorerDefaults.getClassifier());
				frmPropertyFileCreation.getContentPane().setLayout(null);
				PropertyPanel classifierPanel = new PropertyPanel(m_ClassifierEditor);
				y = y + y_offset;
				classifierPanel.setBounds(x, y, width, height);
				this.add(classifierPanel);
				frmPropertyFileCreation.revalidate();
				classifiers.add(m_ClassifierEditor);
				this.add(classifierPanel );
			}
			panel.revalidate();

			JButton btnDelete = new JButton("Delete");
			btnDelete.setFont(new Font("Dialog", Font.BOLD, 10));
			btnDelete.setBounds(501, 110, 71, 25);
			btnDelete.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					panel.remove(myInstance);
					panel.revalidate();
				}
			});
			this.add(btnDelete);
		}
	}

	public String prepareStringClassifier(GenericObjectEditor m_Editor) {
		Object value = m_Editor.getValue();
		String str = "";
		if (value.getClass().isArray()) {
			str += value.getClass().getName();
			Object[] arr = (Object[])value;
			for (Object v : arr) {
				String s = v.getClass().getName();
				if (v instanceof OptionHandler) {
					s += " " + Utils.joinOptions(((OptionHandler) v).getOptions());
				}
				str += " \"" + Utils.backQuoteChars(s.trim()) + "\"";
			}
		} else {
			str += value.getClass().getName();
			if (value instanceof OptionHandler) {
				str += " " + Utils.joinOptions(((OptionHandler) value).getOptions());
			}
		}
		return str;
	}
}
