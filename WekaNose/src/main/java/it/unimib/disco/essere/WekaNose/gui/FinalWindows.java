package it.unimib.disco.essere.WekaNose.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import it.unimib.disco.essere.WekaNose.dfcm4j.DatasetLogger;

public class FinalWindows {

	private JFrame frame;
	private String path;
	private String title; 
	private String button_message;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinalWindows window = new FinalWindows("Test", "test", "/home/umberto/Documents");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FinalWindows(String title, String button_message, String path) {
		this.title = title;
		this.button_message = button_message;
		this.path = path;
		initialize();
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle(title);
		frame.setBounds(100, 100, 450, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JEditorPane dtrpnAnErrorOccur = new JEditorPane(); 
		dtrpnAnErrorOccur.setText("DONE!\nif you need any information, please look the logger info printed below");
		dtrpnAnErrorOccur.setForeground(Color.BLUE);
		dtrpnAnErrorOccur.setFont(new Font("Dialog", Font.BOLD, 13));
		dtrpnAnErrorOccur.setBackground(UIManager.getColor("Button.background"));
		dtrpnAnErrorOccur.setEditable(false);
		dtrpnAnErrorOccur.setBounds(30, 4, 390, 60);
		frame.getContentPane().add(dtrpnAnErrorOccur);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(20, 70, 410, 2);
		frame.getContentPane().add(separator);
		
		TextArea textArea = new TextArea();
		textArea.setBackground(UIManager.getColor("Button.highlight"));
		textArea.setText(DatasetLogger.getInstance().getLoggedMessages());
		textArea.setEditable(false);
		textArea.setBounds(20, 80, 410, 220);
		frame.getContentPane().add(textArea);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(60, 315, 105, 25); 
		btnOk.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    	frame.dispose();
		    }
		});
		frame.getContentPane().add(btnOk);
		
		JButton btnCheckOutThe = new JButton(button_message);
		btnCheckOutThe.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    	try {
					Desktop.getDesktop().open(new File(path));
				} catch (Exception e) {
					StringWriter stackTrace = new StringWriter();
					e.printStackTrace(new PrintWriter(stackTrace));
					ErrorWindows error = new ErrorWindows(e.getMessage(), stackTrace.toString());
				}
		    	frame.dispose();
		    }
		});
		btnCheckOutThe.setBounds(225, 315, 195, 25);
		frame.getContentPane().add(btnCheckOutThe);
	}
}
