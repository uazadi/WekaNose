package it.unimib.disco.essere.WekaNose.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JEditorPane;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JSeparator;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class ErrorWindows {

	private JFrame frmError;
	private String summary;
	private String full;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ErrorWindows window = new ErrorWindows("", "");
					window.frmError.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ErrorWindows(String summaryMessage, String fullMessage) {
		this.summary = summaryMessage;
		this.full = fullMessage;
		initialize();
		this.frmError.setVisible(true);
		this.frmError.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmError = new JFrame();
		frmError.setTitle("Error");
		frmError.setBounds(100, 100, 450, 400);
		frmError.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmError.getContentPane().setLayout(null);
		
		JEditorPane dtrpnAnErrorOccur = new JEditorPane(); 
		dtrpnAnErrorOccur.setForeground(Color.RED);
		dtrpnAnErrorOccur.setFont(new Font("Dialog", Font.BOLD, 13));
		dtrpnAnErrorOccur.setBackground(UIManager.getColor("Button.background"));
		dtrpnAnErrorOccur.setEditable(false);
		dtrpnAnErrorOccur.setText("An error occurred: " + this.summary);
		dtrpnAnErrorOccur.setBounds(30, 10, 390, 60);
		frmError.getContentPane().add(dtrpnAnErrorOccur);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(20, 70, 410, 2);
		frmError.getContentPane().add(separator);
		
		TextArea textArea = new TextArea();
		textArea.setText(this.full);
		textArea.setBackground(UIManager.getColor("Button.highlight"));
		textArea.setEditable(false);
		textArea.setBounds(20, 80, 410, 220);
		frmError.getContentPane().add(textArea);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(161, 313, 117, 25); 
		btnOk.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    	frmError.dispose();
		    }
		});
		frmError.getContentPane().add(btnOk);

	}
}
