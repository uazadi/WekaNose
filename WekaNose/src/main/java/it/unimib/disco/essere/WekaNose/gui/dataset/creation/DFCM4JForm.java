package it.unimib.disco.essere.WekaNose.gui.dataset.creation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import javax.swing.UIManager;

import it.unimib.disco.essere.WekaNose.dataset.creation.DatasetCreator;
import it.unimib.disco.essere.WekaNose.exceptions.DFCM4JException;
import it.unimib.disco.essere.WekaNose.exceptions.WorkspaceException;
import it.unimib.disco.essere.WekaNose.gui.ErrorWindows;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import javax.swing.JSeparator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

public class DFCM4JForm {

	private JFrame frmSourceLoader;
	private DatasetCreator dataset;
	private String name;
	private String args;
	private boolean textAreaFull = false;
	private String textArea_s;
	private int count;
	private final Object lock = new Object();

	/**
	 * Just for test
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					DFCM4JForm window = new DFCM4JForm(new DatasetCreator("Test", true, 10), 1);
					window.frmSourceLoader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DFCM4JForm(DatasetCreator dataset, int count) {
		this.dataset = dataset;
		this.args = "";
		this.name = "";
		this.count = count;
		initialize();
		this.frmSourceLoader.setVisible(true);
		this.frmSourceLoader.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSourceLoader = new JFrame();
		frmSourceLoader.setTitle("source loader (the " + count + "° for " + dataset.getName() + ")");
		frmSourceLoader.setBounds(100, 100, 420, 750);
		frmSourceLoader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSourceLoader.getContentPane().setLayout(null);

		JEditorPane dtrpnsource = new JEditorPane();
		dtrpnsource.setFont(new Font("Dialog", Font.BOLD, 12));
		dtrpnsource.setEditable(false);
		dtrpnsource.setToolTipText("It represents a list of directories. These directories contain all the Java files that the user wants to analyse");
		dtrpnsource.setBackground(UIManager.getColor("Button.background"));
		dtrpnsource.setText("-source(*)");
		dtrpnsource.setBounds(25, 95, 77, 21);
		frmSourceLoader.getContentPane().add(dtrpnsource);

		JEditorPane dtrpnlib = new JEditorPane();
		dtrpnlib.setFont(new Font("Dialog", Font.ITALIC, 12));
		dtrpnlib.setEditable(false);
		dtrpnlib.setToolTipText("It represents the list of paths where the libraries are stored (as jar files). ");
		dtrpnlib.setText("-lib (#)");
		dtrpnlib.setBackground(UIManager.getColor("Button.background"));
		dtrpnlib.setBounds(25, 130, 60, 21);
		frmSourceLoader.getContentPane().add(dtrpnlib);



		final JFormattedTextField textArea_lib = new JFormattedTextField();
		textArea_lib.setBounds(110, 130, 210, 20);
		frmSourceLoader.getContentPane().add(textArea_lib);

		JEditorPane dtrpnLoadTheSource = new JEditorPane(); 
		dtrpnLoadTheSource.setToolTipText("load the source");
		dtrpnLoadTheSource.setEditable(false);
		dtrpnLoadTheSource.setFont(new Font("Dialog", Font.BOLD, 12));
		dtrpnLoadTheSource.setBackground(UIManager.getColor("Button.background"));
		dtrpnLoadTheSource.setText("LOAD THE SOURCE ");
		dtrpnLoadTheSource.setBounds(135, 5, 150, 21);
		frmSourceLoader.getContentPane().add(dtrpnLoadTheSource);

		JEditorPane dtrpninputtype = new JEditorPane();
		dtrpninputtype.setEditable(false);
		dtrpninputtype.setToolTipText("It allows to select the input strategy to give the parameters \n");
		dtrpninputtype.setBackground(UIManager.getColor("Button.background"));
		dtrpninputtype.setText("-input-type");
		dtrpninputtype.setBounds(38, 190, 106, 21);
		frmSourceLoader.getContentPane().add(dtrpninputtype);

		JEditorPane dtrpnencoding = new JEditorPane();
		dtrpnencoding.setEditable(false);
		dtrpnencoding.setToolTipText("This parameter refers to the project’s encoding");
		dtrpnencoding.setBackground(UIManager.getColor("Button.background"));
		dtrpnencoding.setText("-encoding");
		dtrpnencoding.setBounds(38, 290, 106, 21);
		frmSourceLoader.getContentPane().add(dtrpnencoding);

		JEditorPane dtrpnjv = new JEditorPane();
		dtrpnjv.setEditable(false);
		dtrpnjv.setToolTipText("It is the compliance version");
		dtrpnjv.setBackground(UIManager.getColor("Button.background"));
		dtrpnjv.setText("-JV");
		dtrpnjv.setBounds(38, 340, 106, 21);
		frmSourceLoader.getContentPane().add(dtrpnjv);

		JEditorPane dtrpnanalysis = new JEditorPane();
		dtrpnanalysis.setEditable(false);
		dtrpnanalysis.setToolTipText("It is the name of the analysis");
		dtrpnanalysis.setBackground(UIManager.getColor("Button.background"));
		dtrpnanalysis.setText("-analysis");
		dtrpnanalysis.setBounds(38, 390, 106, 21);
		frmSourceLoader.getContentPane().add(dtrpnanalysis);

		JEditorPane dtrpnthreadsnumber = new JEditorPane();
		dtrpnthreadsnumber.setEditable(false);
		dtrpnthreadsnumber.setToolTipText("the number of threads that will run to compute the analysis");
		dtrpnthreadsnumber.setBackground(UIManager.getColor("Button.background"));
		dtrpnthreadsnumber.setText("-threads-number");
		dtrpnthreadsnumber.setBounds(38, 440, 115, 21);
		frmSourceLoader.getContentPane().add(dtrpnthreadsnumber);

		final JFormattedTextField textArea_config = new JFormattedTextField();
		textArea_config.setBounds(150, 240, 210, 20);
		frmSourceLoader.getContentPane().add(textArea_config);

		final JFormattedTextField textArea_encoding = new JFormattedTextField();
		textArea_encoding.setBounds(150, 290, 210, 20);
		frmSourceLoader.getContentPane().add(textArea_encoding);

		final JFormattedTextField textArea_jv = new JFormattedTextField();
		textArea_jv.setBounds(150, 340, 210, 20);
		frmSourceLoader.getContentPane().add(textArea_jv);

		final JFormattedTextField textArea_analysis = new JFormattedTextField();
		textArea_analysis.setBounds(150, 390, 210, 20);
		frmSourceLoader.getContentPane().add(textArea_analysis);

		final JFormattedTextField textArea_threads = new JFormattedTextField();
		textArea_threads.setBounds(160, 440, 201, 20);
		frmSourceLoader.getContentPane().add(textArea_threads);

		JEditorPane dtrpnForMoreInfo = new JEditorPane();
		dtrpnForMoreInfo.setEditable(false);
		dtrpnForMoreInfo.setText("for more info about the parameters: \nhttp://essere.disco.unimib.it/wiki/jcodeodor_doc");
		dtrpnForMoreInfo.setBackground(UIManager.getColor("Button.background"));
		dtrpnForMoreInfo.setBounds(40, 480, 320, 40);
		frmSourceLoader.getContentPane().add(dtrpnForMoreInfo);

		JEditorPane dtrpnconfig = new JEditorPane();
		dtrpnconfig.setEditable(false);
		dtrpnconfig.setToolTipText("This parameter refers to the .properties file. This file must contain the references to the Java libraries.");
		dtrpnconfig.setBackground(UIManager.getColor("Button.background"));
		dtrpnconfig.setText("-config");
		dtrpnconfig.setBounds(38, 240, 106, 21);
		frmSourceLoader.getContentPane().add(dtrpnconfig);

		final JFormattedTextField textArea_input = new JFormattedTextField();
		textArea_input.setBounds(150, 190, 210, 20);
		frmSourceLoader.getContentPane().add(textArea_input);

		final JButton btnNextSource = new JButton("add another source");
		btnNextSource.setToolTipText("allows to select and load another source");
		btnNextSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DFCM4JForm next = new DFCM4JForm(dataset, count + 1);
				frmSourceLoader.dispose();
			}
		});
		btnNextSource.setBounds(30, 580, 180, 25);
		frmSourceLoader.getContentPane().add(btnNextSource);

		final JButton btnGenerateDataset = new JButton("continue");
		btnGenerateDataset.setToolTipText("continue with the next step of the dataset creation");
		btnGenerateDataset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConditionForm condition = new ConditionForm(dataset);
				frmSourceLoader.dispose();
			}
		});
		btnGenerateDataset.setBounds(230, 580, 130, 25);
		frmSourceLoader.getContentPane().add(btnGenerateDataset);

		JEditorPane dtrpnRequired = new JEditorPane();
		dtrpnRequired.setEditable(false);
		dtrpnRequired.setFont(new Font("Dialog", Font.BOLD, 12));
		dtrpnRequired.setBackground(UIManager.getColor("Button.background"));
		dtrpnRequired.setText("(*) required\n(#) most likely needed (but optional)\n(the not marked ones are optional)");
		dtrpnRequired.setBounds(38, 620, 300, 50);
		frmSourceLoader.getContentPane().add(dtrpnRequired);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBounds(20, 170, 360, 2);
		frmSourceLoader.getContentPane().add(separator);

		final JEditorPane dtrpnLoadingPleaseWait = new JEditorPane();
		dtrpnLoadingPleaseWait.setEditable(false);
		dtrpnLoadingPleaseWait.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		dtrpnLoadingPleaseWait.setText("");
		dtrpnLoadingPleaseWait.setForeground(UIManager.getColor("Button.background"));
		dtrpnLoadingPleaseWait.setBackground(UIManager.getColor("Button.background"));
		dtrpnLoadingPleaseWait.setBounds(165, 530, 200, 40);
		frmSourceLoader.getContentPane().add(dtrpnLoadingPleaseWait);

		final JButton btnLoadSource = new JButton("load source");
		btnLoadSource.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dtrpnLoadingPleaseWait.setForeground(Color.BLUE);
				String timestamp = new Timestamp(System.currentTimeMillis()).toString();
				dtrpnLoadingPleaseWait.setText("Loading, please wait...\n(start at " 
						+ timestamp.substring(timestamp.indexOf(" "), timestamp.length())
						+  ")");
				btnGenerateDataset.setEnabled(false);
				btnNextSource.setEnabled(false);
				frmSourceLoader.revalidate();
			}
		});
		btnLoadSource.setEnabled(false);
		btnLoadSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					long start = System.currentTimeMillis();
					updateArgs("-source", textArea_s.replace(" ", "\\ "));
					updateArgs("-lib", textArea_lib.getText().replace(" ", "\\ "));
					updateArgs("-input-type", textArea_input.getText());
					updateArgs("-config", textArea_config.getText());
					updateArgs("-encoding", textArea_encoding.getText());
					updateArgs("-JV", textArea_jv.getText());
					updateArgs("-analysis", textArea_analysis.getText());
					updateArgs("-threads-number", textArea_threads.getText());
					dataset.genereateSQLite(name, args.substring(1)); 
					dtrpnLoadingPleaseWait.setText("Done!\n it took: " 
							+ (System.currentTimeMillis() - start)/1000 + " s");
				} catch (DFCM4JException e) {
					args = "";
					StringWriter stackTrace = new StringWriter();
					e.getException().printStackTrace(new PrintWriter(stackTrace));
					ErrorWindows error = new ErrorWindows(e.getMessage(), stackTrace.toString());
					dtrpnLoadingPleaseWait.setForeground(Color.RED);
					dtrpnLoadingPleaseWait.setText("Error occured! ");
				} catch (WorkspaceException e) {
					name = "";
					args = "";
					StringWriter stackTrace = new StringWriter();
					e.printStackTrace(new PrintWriter(stackTrace));
					ErrorWindows error = new ErrorWindows(e.getMessage(), stackTrace.toString());
					dtrpnLoadingPleaseWait.setForeground(Color.RED);
					dtrpnLoadingPleaseWait.setText("Error occured! ");
				}

				btnGenerateDataset.setEnabled(true);
				btnNextSource.setEnabled(true);
			}
		});
		btnLoadSource.setToolTipText("load the source specified above.");
		btnLoadSource.setBounds(30, 535, 117, 25);
		frmSourceLoader.getContentPane().add(btnLoadSource);

		final JFormattedTextField textArea_name = new JFormattedTextField();
		textArea_name.setBounds(160, 30, 160, 20);
		textArea_name.addKeyListener(new KeyAdapter() { 
			@Override
			public void keyReleased(KeyEvent e) {
				if(!textArea_name.getText().replaceAll(" ", "").equals("")){
					
					name = textArea_name.getText();
					String[] charNotAllowed = {">", "<", "/", "\\", "@", "?", "#", "&", "%", "~", ":", "+", "|", "\"", "*"};
					boolean valid = true;
					for(String character: charNotAllowed) {
						if(textArea_name.getText().contains(character)) {
							valid = false;
						}
					}
					
					if(textAreaFull && valid)
						btnLoadSource.setEnabled(true);	
					else
						btnLoadSource.setEnabled(false);	
				}
				else {
					btnLoadSource.setEnabled(false);
				}
			}
		});
		frmSourceLoader.getContentPane().add(textArea_name);



		final  JFormattedTextField textArea_source = new  JFormattedTextField();
		textArea_source.setBounds(110, 95, 210, 20);
		textArea_source.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(!textArea_source.getText().replaceAll(" ", "").equals("")){
					textAreaFull = true;
					textArea_s = textArea_source.getText();
					if(!textArea_name.getText().replaceAll(" ", "").equals("")) 
						btnLoadSource.setEnabled(true);	
				}
				else {
					textAreaFull = false;
					btnLoadSource.setEnabled(false);
				}
			}
		});
		frmSourceLoader.getContentPane().add(textArea_source);
		
		JEditorPane dtrpnName = new JEditorPane();
		dtrpnName.setFont(new Font("Dialog", Font.BOLD, 12));
		dtrpnName.setToolTipText("the name with will be used to refer to this source");
		dtrpnName.setText("with the name(*) :");
		dtrpnName.setEditable(false);
		dtrpnName.setBackground(UIManager.getColor("Button.background"));
		dtrpnName.setBounds(25, 30, 140, 21);
		frmSourceLoader.getContentPane().add(dtrpnName);
		
		JEditorPane dtrpnTheFollowingCharacter = new JEditorPane();
		dtrpnTheFollowingCharacter.setEditable(false);
		dtrpnTheFollowingCharacter.setFont(new Font("Dialog", Font.PLAIN, 11));
		dtrpnTheFollowingCharacter.setText("the following character are NOT allowed: \n>   <   /   \\   @   ?   #   &   %   ~   :   +   |   \"   *");
		dtrpnTheFollowingCharacter.setForeground(Color.BLUE);
		dtrpnTheFollowingCharacter.setBackground(UIManager.getColor("Button.background"));
		dtrpnTheFollowingCharacter.setBounds(25, 50, 310, 30);
		frmSourceLoader.getContentPane().add(dtrpnTheFollowingCharacter);

		
		final JFileChooser browse_source = new JFileChooser();
		browse_source.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		JButton btnBrowse_source= new JButton("Browse");
		btnBrowse_source.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int returnVal = browse_source.showOpenDialog(new JPanel());

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = browse_source.getSelectedFile();
		            textArea_source.setText(file.getAbsolutePath());
		            textAreaFull = true;
		            textArea_s = file.getAbsolutePath();
		            if(!textArea_name.getText().replaceAll(" ", "").equals(""))
		            	btnLoadSource.setEnabled(true);	
		        } 
			}
		});
		btnBrowse_source.setHorizontalAlignment(SwingConstants.LEFT);
		btnBrowse_source.setFont(new Font("Dialog", Font.BOLD, 9));
		btnBrowse_source.setBounds(325, 95, 69, 20);
		frmSourceLoader.getContentPane().add(btnBrowse_source);
		
		final JFileChooser browse_lib = new JFileChooser();
		browse_lib.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		JButton btnBrowse_lib = new JButton("Browse");
		btnBrowse_lib.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int returnVal = browse_lib.showOpenDialog(new JPanel());

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = browse_lib.getSelectedFile();
		            textArea_lib.setText(file.getAbsolutePath());
		        } 
			}
		});
		btnBrowse_lib.setHorizontalAlignment(SwingConstants.LEFT);
		btnBrowse_lib.setFont(new Font("Dialog", Font.BOLD, 9));
		btnBrowse_lib.setBounds(325, 130, 69, 20);
		frmSourceLoader.getContentPane().add(btnBrowse_lib);
	}

	public void updateArgs(String command, String value) {
		if(!"".equals(value)) {
			args += " " + command + " " + value;
		}

	}
}
