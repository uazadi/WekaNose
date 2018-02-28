package it.unimib.disco.essere.WekaNose.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.UIManager;

import it.unimib.disco.essere.WekaNose.gui.dataset.creation.DatasetInfo;
import it.unimib.disco.essere.WekaNose.gui.machine.learning.approach.PropertiesCreator;

public class HomePage {

	private JFrame frmWekaNose;

	/**
	 * Create the application.
	 */
	public HomePage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWekaNose = new JFrame();
		frmWekaNose.setTitle("WEKA nose");
		frmWekaNose.getContentPane().setBackground(UIManager.getColor("Button.background"));
		frmWekaNose.setBounds(100, 100, 540, 280);
		frmWekaNose.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWekaNose.getContentPane().setLayout(null);

		String icon_path = "";
		if(System.getProperty("os.name").toLowerCase().contains("win"))
			icon_path = new java.io.File("").getAbsolutePath().substring(0,  
				new java.io.File("").getAbsolutePath().indexOf("\\WekaNose") + "/WekaNose".length())
				+ "/docs/pictures/WekaNose3.png";
		else
			icon_path = new java.io.File("").getAbsolutePath().substring(0,  
					new java.io.File("").getAbsolutePath().indexOf("/WekaNose") + "/WekaNose".length())
					+ "/docs/pictures/WekaNose3.png";
		
		JLabel label = new JLabel(new ImageIcon(icon_path));
		label.setForeground(Color.WHITE);
		label.setBackground(Color.WHITE);
		label.setLabelFor(frmWekaNose.getContentPane());
		label.setBounds(15, 0, 266, 265);
		frmWekaNose.getContentPane().add(label);

		JButton dataset = new JButton("Dataset Creator");
		dataset.setBounds(313, 55, 180, 25);
		frmWekaNose.getContentPane().add(dataset);
		dataset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DatasetInfo info = new DatasetInfo();   
			} 
		});
		JButton btnMachineLearningApplier = new JButton("<html>Machine Learning<br />Applier</html>");
		btnMachineLearningApplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PropertiesCreator info = new PropertiesCreator();   
			} 
		});
		btnMachineLearningApplier.setBounds(313, 153, 180, 50);
		frmWekaNose.getContentPane().add(btnMachineLearningApplier);
	}

	/**
	 * Launch the application.
	 */
	public void launch(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomePage window = new HomePage();
					window.frmWekaNose.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * just for test
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomePage window = new HomePage();
					window.frmWekaNose.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
