package it.unimib.disco.essere.WekaNose.gui.dataset.creation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.UIManager;

import it.unimib.disco.essere.WekaNose.dataset.creation.Advisor;
import it.unimib.disco.essere.WekaNose.dataset.creation.DatasetCreator;
import it.unimib.disco.essere.WekaNose.exceptions.WorkspaceException;
import it.unimib.disco.essere.WekaNose.gui.ErrorWindows;

import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.awt.Color;

public class DatasetInfo {

	private JFrame frmDatasetInfo;
	protected String name;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatasetInfo window = new DatasetInfo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DatasetInfo() {
		initialize();
		this.frmDatasetInfo.setVisible(true);
		this.frmDatasetInfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDatasetInfo = new JFrame();
		frmDatasetInfo.setTitle("dataset info");
		frmDatasetInfo.setBounds(100, 100, 450, 350);
		frmDatasetInfo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDatasetInfo.getContentPane().setLayout(null);

		JEditorPane dtrpnAllTheFollowing = new JEditorPane();
		dtrpnAllTheFollowing.setBackground(UIManager.getColor("Button.background"));
		dtrpnAllTheFollowing.setFont(new Font("Dialog", Font.ITALIC, 14));
		dtrpnAllTheFollowing.setText("All the following informations are required for start \nwith the creation of the dataset:");
		dtrpnAllTheFollowing.setEditable(false);
		dtrpnAllTheFollowing.setBounds(50, 29, 350, 36);
		frmDatasetInfo.getContentPane().add(dtrpnAllTheFollowing);

		JEditorPane dtrpnExperiment = new JEditorPane();
		dtrpnExperiment.setToolTipText("the name which will be used for save the dataset");
		dtrpnExperiment.setFont(new Font("Dialog", Font.PLAIN, 14));
		dtrpnExperiment.setBackground(UIManager.getColor("Button.background"));
		dtrpnExperiment.setEditable(false);
		dtrpnExperiment.setText("dataset name:");
		dtrpnExperiment.setBounds(18, 95, 175, 21);
		frmDatasetInfo.getContentPane().add(dtrpnExperiment);

		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(200, 0, null, 50) );
		spinner.setBounds(325, 150, 100, 30);
		frmDatasetInfo.getContentPane().add(spinner);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		
		comboBox.setModel(new DefaultComboBoxModel<String>(Advisor.VALID_LEVEL));
		comboBox.setBounds(296, 187, 80, 24);
		frmDatasetInfo.getContentPane().add(comboBox);
		
		final JButton btnContinue = new JButton("continue");
		btnContinue.setEnabled(false);
		btnContinue.setToolTipText("continue with the next step of the dataset creation");
		btnContinue.setBounds(97, 240, 277, 25);
		btnContinue.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    	
		        try {
		        	DatasetCreator dataset = new DatasetCreator(name, ((String)comboBox.getSelectedItem()).equals("method"), ((Integer)spinner.getValue()).intValue());
					DFCM4JForm form = new DFCM4JForm(dataset, 1);
				} catch (WorkspaceException e) {
					StringWriter stackTrace = new StringWriter();
					e.printStackTrace(new PrintWriter(stackTrace));
					ErrorWindows error = new ErrorWindows(e.getMessage(), stackTrace.toString());
				}

		        frmDatasetInfo.dispose();
		    }
		});
		frmDatasetInfo.getContentPane().add(btnContinue);

		final JFormattedTextField dtrpnTheNameThat = new JFormattedTextField();
		dtrpnTheNameThat.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String[] charNotAllowed = {">", "<", "/", "\\", "@", "?", "#", "&", "%", "~", ":", "+", "|", "\"", "*"};
				
				if(!dtrpnTheNameThat.getText().replaceAll(" ", "").equals("")){
					boolean valid = true;
					for(String character: charNotAllowed) {
						if(dtrpnTheNameThat.getText().contains(character)) {
							valid = false;
						}
					}
					if(valid) {
						name = dtrpnTheNameThat.getText();
						btnContinue.setEnabled(true);
					}else {
						btnContinue.setEnabled(false);
					}
				}
				else {
					btnContinue.setEnabled(false);
				}
			}
		});
		dtrpnTheNameThat.setBounds(205, 95, 220, 21);
		frmDatasetInfo.getContentPane().add(dtrpnTheNameThat);

		JEditorPane dtrpnNumberOfIstances = new JEditorPane();
		dtrpnNumberOfIstances.setEditable(false);
		dtrpnNumberOfIstances.setToolTipText("the number of row that the dataset will have");
		dtrpnNumberOfIstances.setFont(new Font("Dialog", Font.PLAIN, 14));
		dtrpnNumberOfIstances.setBackground(UIManager.getColor("Button.background"));
		dtrpnNumberOfIstances.setText("number of instances: ");
		dtrpnNumberOfIstances.setBounds(18, 150, 220, 21);
		frmDatasetInfo.getContentPane().add(dtrpnNumberOfIstances);

		JEditorPane dtrpnTheCodeSmell = new JEditorPane();
		dtrpnTheCodeSmell.setEditable(false);
		dtrpnTheCodeSmell.setFont(new Font("Dialog", Font.PLAIN, 14));
		dtrpnTheCodeSmell.setBackground(UIManager.getColor("Button.background"));
		dtrpnTheCodeSmell.setText("the code smell take in consideration is ");
		dtrpnTheCodeSmell.setBounds(18, 187, 278, 21);
		frmDatasetInfo.getContentPane().add(dtrpnTheCodeSmell);

		JTextPane txtpnLevel = new JTextPane();
		txtpnLevel.setEditable(false);
		txtpnLevel.setBackground(UIManager.getColor("Button.background"));
		txtpnLevel.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtpnLevel.setText("level");
		txtpnLevel.setBounds(386, 187, 52, 21);
		frmDatasetInfo.getContentPane().add(txtpnLevel);
		
		JEditorPane dtrpnTheFollowingCharacter = new JEditorPane();
		dtrpnTheFollowingCharacter.setEditable(false);
		dtrpnTheFollowingCharacter.setFont(new Font("Dialog", Font.PLAIN, 11));
		dtrpnTheFollowingCharacter.setText("the following character are NOT allowed: \n>   <   /   \\   @   ?   #   &   %   ~   :   +   |   \"   *");
		dtrpnTheFollowingCharacter.setForeground(Color.BLUE);
		dtrpnTheFollowingCharacter.setBackground(UIManager.getColor("Button.background"));
		dtrpnTheFollowingCharacter.setBounds(25, 115, 400, 30);
		frmDatasetInfo.getContentPane().add(dtrpnTheFollowingCharacter);
		
		
	}
}
