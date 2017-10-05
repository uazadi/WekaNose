package it.unimib.disco.essere.WekaNose.gui.dataset.creation;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import it.unimib.disco.essere.WekaNose.dfcm4j.Condition;
import it.unimib.disco.essere.WekaNose.dfcm4j.DatasetCreator;
import it.unimib.disco.essere.WekaNose.dfcm4j.DatasetRow;
import it.unimib.disco.essere.WekaNose.exception.NotValidConditionException;
import it.unimib.disco.essere.WekaNose.gui.ErrorWindows;
import it.unimib.disco.essere.WekaNose.gui.FinalWindows;

import java.awt.GridLayout;

public class ConditionForm {

	private JFrame frmInsertCondition;
	private DatasetCreator dataset;
	private List<Condition> conditions;
	private JPanel panelConditions;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatasetCreator dc = new DatasetCreator("Test", true, 10);
					dc.genereateSQLite("freemind", "-source /home/umberto/Documents/QualitasCorpus/jasml/jasml-0.10/src");
					ConditionForm window = new ConditionForm(dc);
					window.frmInsertCondition.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} 

	/**
	 * Create the application.
	 */
	public ConditionForm(DatasetCreator dataset) {
		this.dataset = dataset;
		this.conditions = new ArrayList<Condition>();
		initialize();
		this.frmInsertCondition.setVisible(true);
		this.frmInsertCondition.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initialize the contents of the frmInsertCondition.
	 */
	private void initialize() {
		frmInsertCondition = new JFrame();
		frmInsertCondition.setTitle("Insert conditions");
		frmInsertCondition.setBounds(100, 100, 600, 450);
		frmInsertCondition.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmInsertCondition.getContentPane().setLayout(null);

		panelConditions = new JPanel();
		panelConditions.setSize(560, 100);
		panelConditions.setLocation(20, 130);

		final JScrollPane scrollPanel = new JScrollPane(panelConditions);
		scrollPanel.setSize(560, 150);
		scrollPanel.setLocation(20, 120);

		final ConditionJPanel condition = new ConditionJPanel();
		condition.setSize(500, 25);
		condition.setLocation(50, 130);
		GridLayout grid = new GridLayout(0, 1, 0, 8);
		grid.preferredLayoutSize(condition);
		panelConditions.setLayout(grid);
		panelConditions.add(condition);


		JEditorPane dtrpnInstertTheCondition = new JEditorPane();
		dtrpnInstertTheCondition.setEditable(false);
		dtrpnInstertTheCondition.setBackground(UIManager.getColor("Button.background"));
		dtrpnInstertTheCondition.setFont(new Font("Dialog", Font.BOLD, 14));
		dtrpnInstertTheCondition.setText("Insert the conditions (advisors) witch will be used to create a group:");
		dtrpnInstertTheCondition.setBounds(30, 12, 488, 36);
		frmInsertCondition.getContentPane().add(dtrpnInstertTheCondition);

		final JEditorPane dtrpnPleaseWait = new JEditorPane();
		dtrpnPleaseWait.setBackground(UIManager.getColor("Button.background"));
		dtrpnPleaseWait.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		dtrpnPleaseWait.setBounds(414, 335, 182, 60);
		frmInsertCondition.getContentPane().add(dtrpnPleaseWait);

		JButton btnGenerateDataset = new JButton("Generate dataset");
		btnGenerateDataset.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dtrpnPleaseWait.setForeground(Color.BLUE);
				String timestamp = new Timestamp(System.currentTimeMillis()).toString();
				dtrpnPleaseWait.setText("Generating,\n please wait...\n(start at " 
						+ timestamp.substring(timestamp.indexOf(" "), timestamp.length())
						+  ")");
				frmInsertCondition.revalidate();
			}
		});
		btnGenerateDataset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0; i < panelConditions.getComponentCount(); i++) {
					ConditionJPanel condition = (ConditionJPanel) panelConditions.getComponent(i);
					addNextCondition((String) condition.comboBox.getSelectedItem(),
							dataset.isMethodLevel() ? "method" : "class",
									(String) condition.comboBox_1.getSelectedItem(),
									(Integer) condition.spinner_1.getValue(),
									(Integer) condition.spinner_2.getValue());
				}

				try {
					dataset.performQuery(conditions);
					dataset.generateDataset(dataset.getName());
				} catch (Exception e) {
					dtrpnPleaseWait.setForeground(Color.RED);
					dtrpnPleaseWait.setText("Error occured!");
					StringWriter stackTrace = new StringWriter();
					e.printStackTrace(new PrintWriter(stackTrace));
					ErrorWindows error = new ErrorWindows(e.getMessage(), stackTrace.toString());
				}
				FinalWindows fin = new FinalWindows("Dataset created", "Check out the dataset", dataset.getWorkspace().getPath());
				frmInsertCondition.dispose();
			}
		});
		btnGenerateDataset.setBounds(204, 350, 200, 22);
		frmInsertCondition.getContentPane().add(btnGenerateDataset);

		JEditorPane dtrpnMetric = new JEditorPane();
		dtrpnMetric.setBackground(UIManager.getColor("Button.background"));
		dtrpnMetric.setEditable(false);
		dtrpnMetric.setFont(new Font("Dialog", Font.BOLD, 14));
		dtrpnMetric.setText("Metric");
		dtrpnMetric.setBounds(45, 72, 90, 21);
		frmInsertCondition.getContentPane().add(dtrpnMetric);

		JEditorPane dtrpnBooleanOperator = new JEditorPane();
		dtrpnBooleanOperator.setBackground(UIManager.getColor("Button.background"));
		dtrpnBooleanOperator.setEditable(false);
		dtrpnBooleanOperator.setFont(new Font("Dialog", Font.BOLD, 13));
		dtrpnBooleanOperator.setText("Boolean \nOperator");
		dtrpnBooleanOperator.setBounds(160, 60, 95, 41);
		frmInsertCondition.getContentPane().add(dtrpnBooleanOperator);

		JEditorPane dtrpnNumber = new JEditorPane();
		dtrpnNumber.setEditable(false);
		dtrpnNumber.setFont(new Font("Dialog", Font.BOLD, 14));
		dtrpnNumber.setBackground(UIManager.getColor("Button.background"));
		dtrpnNumber.setText("Number");
		dtrpnNumber.setBounds(290, 72, 81, 21);
		frmInsertCondition.getContentPane().add(dtrpnNumber);

		JEditorPane dtrpnSecond = new JEditorPane();
		dtrpnSecond.setFont(new Font("Dialog", Font.BOLD, 13));
		dtrpnSecond.setEditable(false);
		dtrpnSecond.setBackground(UIManager.getColor("Button.background"));
		dtrpnSecond.setText("Second \nNumber");
		dtrpnSecond.setBounds(420, 60, 130, 40);
		frmInsertCondition.getContentPane().add(dtrpnSecond);

		JButton btnAddAnotherCondition = new JButton("Add another condition");
		btnAddAnotherCondition.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final ConditionJPanel condition = new ConditionJPanel();
				condition.setSize(500, 25);
				condition.setLocation(50, 130);
				panelConditions.add(condition);
				panelConditions.revalidate();
				scrollPanel.revalidate();
			}
		});
		btnAddAnotherCondition.setBounds(204, 291, 200, 25);
		frmInsertCondition.getContentPane().add(btnAddAnotherCondition);
		
		JButton btnNewButton = new JButton("< Back");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DFCM4JForm back = new DFCM4JForm(dataset, dataset.getWorkspace().getProjectsCreated().size() + 1); 
				frmInsertCondition.dispose();
			}
		});
		btnNewButton.setBounds(30, 350, 117, 25);
		frmInsertCondition.getContentPane().add(btnNewButton);

		frmInsertCondition.getContentPane().add(scrollPanel);
	}

	private class ConditionJPanel extends JPanel{

		private static final long serialVersionUID = 1L;
		final JComboBox<String> comboBox;
		final JComboBox<String> comboBox_1;
		final JSpinner spinner_1;
		final JSpinner spinner_2;
		ConditionJPanel myInstance = this;

		public ConditionJPanel() {

			this.setLayout(new GridLayout(0, 5, 8, 0));

			comboBox = new JComboBox<String>();
			comboBox.setBackground(UIManager.getColor("Button.highlight"));
			if(dataset.isMethodLevel()) {
				comboBox.setModel(new DefaultComboBoxModel<String>(DatasetRow.METHOD_METRICS));
				comboBox.setSelectedItem(DatasetRow.METHOD_METRICS[0]);
			}
			else {
				comboBox.setModel(new DefaultComboBoxModel<String>(DatasetRow.CLASS_METRICS));
				comboBox.setSelectedItem(DatasetRow.METHOD_METRICS[0]);
			}
			comboBox.setBounds(45, 110, 80, 25);
			this.add(comboBox);

			spinner_2 = new JSpinner(new SpinnerNumberModel(0, 0, null, 10) );
			spinner_2.setEnabled(false);
			spinner_2.setFont(new Font("Dialog", Font.BOLD, 13));
			spinner_2.setBounds(330, 110, 50, 25);

			comboBox_1 = new JComboBox<String>();
			comboBox_1.setBackground(UIManager.getColor("Button.highlight"));
			comboBox_1.setModel(new DefaultComboBoxModel<String>(Condition.VALID_SIYBOL));
			comboBox_1.setSelectedItem(Condition.VALID_SIYBOL[0]);
			comboBox_1.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if (comboBox_1.getSelectedItem().equals("between")) 
						spinner_2.setEnabled(true);
					else
						spinner_2.setEnabled(false);
				}
			});
			comboBox_1.setBounds(190, 110 , 80, 25);
			this.add(comboBox_1);

			spinner_1 = new JSpinner(new SpinnerNumberModel(0, 0, null, 10) );
			spinner_1.setFont(new Font("Dialog", Font.BOLD, 13));
			spinner_1.setBounds(420, 110, 50, 25);
			this.add(spinner_1);

			this.add(spinner_2);

			JButton btnDelete = new JButton("Delete");
			btnDelete.setFont(new Font("Dialog", Font.BOLD, 10));
			btnDelete.setBounds(501, 110, 71, 25);
			btnDelete.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					panelConditions.remove(myInstance);
					panelConditions.revalidate();
				}
			});
			this.add(btnDelete);
		}

	}

	public void addNextCondition(String metric, String level, String symbol, int num1, int num2) {
		try {
			Condition condition = new Condition(metric, level, symbol, num1, num2);
			conditions.add(condition);
		} catch (NotValidConditionException e) {
			StringWriter stackTrace = new StringWriter();
			e.printStackTrace(new PrintWriter(stackTrace));
			ErrorWindows error = new ErrorWindows(e.getMessage(), stackTrace.toString());
		}
	}
}
