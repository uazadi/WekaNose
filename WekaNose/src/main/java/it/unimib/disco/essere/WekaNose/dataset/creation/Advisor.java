package it.unimib.disco.essere.WekaNose.dataset.creation;

import java.util.Arrays;

import it.unimib.disco.essere.WekaNose.exceptions.NotValidConditionException;

/**
 * This class model an Advisor, i.e. a deterministic rule that gives 
 * a classification of a code element, telling if it is a code smell or not.
 * 
 * The regular expression that describe an Advisor is: [METRIC]{1}_[VALID_LEVEL]{1}[VALID_SYMBOL]{1}[0-9]+
 * For the metrics list see DatasetRow.java
 * 
 * */
public class Advisor {
	
	/** The symbol that can be use to specify the rule */
	public static String[] VALID_SYMBOL = {"<", "<=", ">", ">=", "!=", "==", "between"};
	
	/** The levels of the metrics that can be used to specify the rule */
	public static String[] VALID_LEVEL = {"method", "class"};
	
	/** The metric that is used to express the rule*/
	private String metric;
	
	/** The level of the metric that is used to express the rule*/
	private String level;

	/** The metric that is used to express the rule*/
	private String symbol;
	
	/** The threshold of the rule*/
	private int threshold1;
	
	/** The second threshold of the rule, that can be specified only if the symbol is "between" */
	private int threshold2;
	
	/**
	 * Check if all the required information needed to create an Advisor are consistent.
	 * This constructor allow to create a "between rules" by specifying the second threshold.
	 * 
	 * @throws NotValidConditionException in case some of the information aren't valid.
	 */
	public Advisor(String metric, String level, String symbol, int threshold1, int threshold2) throws NotValidConditionException{
		
		if(Arrays.asList(DatasetRow.METHOD_METRICS).contains(metric)
		   || Arrays.asList(DatasetRow.CLASS_METRICS).contains(metric))
			this.metric = metric;
		else 
			throw new NotValidConditionException("the metric " + metric + " is not valid");
		
		if(Arrays.asList(Advisor.VALID_LEVEL).contains(level))
			this.level = level;
		else
			throw new NotValidConditionException("the level " + level + " is not valid");
		
		if(Arrays.asList(Advisor.VALID_SYMBOL).contains(symbol))
			this.symbol = (symbol.equals("!=")) ? "<>" : symbol;
		else
			throw new NotValidConditionException("the symbol " + symbol + " is not valid");
		
		try {
			this.threshold1 = threshold1;
			this.threshold2 = (this.symbol.equals("between")) ? threshold2 : 0;
		} catch(NumberFormatException e) {
			throw new NotValidConditionException("one (or both) of the thresholdbers are  not valid");
		}
	}
	
	/**
	 * Check if all the required information needed to create an Advisor are consistent.
	 * This constructor allow to create all the rule except the one that involve the symbol "between".
	 * 
	 * @throws NotValidConditionException in case some of the information aren't valid.
	 */
	public Advisor(String metric, String level, String symbol, int threshold1) throws NotValidConditionException{
		this(metric, level, symbol, threshold1, 0);
	}
	
	public void setMetric(String metric) throws NotValidConditionException {
		if(Arrays.asList(DatasetRow.METHOD_METRICS).contains(metric)
			|| Arrays.asList(DatasetRow.CLASS_METRICS).contains(metric))
				this.metric = metric;
		else 
				throw new NotValidConditionException("the metric " + metric + " is not valid");
	}
	
	public void setLevel(String level) throws NotValidConditionException {
		if(Arrays.asList(Advisor.VALID_LEVEL).contains(level))
			this.level = level;
		else
			throw new NotValidConditionException("the level " + level + " is not valid");
	}

	public void setSymbol(String symbol) throws NotValidConditionException {
		if(Arrays.asList(Advisor.VALID_SYMBOL).contains(symbol))
			this.symbol = symbol;
		else
			throw new NotValidConditionException("the symbol " + symbol + " is not valid");
	}

	public void setThreshold1(int threshold1) {
		this.threshold1 = threshold1;
	}

	public void setThreshold2(int threshold2) {
		this.threshold2 = threshold2;
	}

	public String getMetric() {
		return metric;
	}
	
	public String getLevel() {
		return level;
	}

	public  String getSymbol() {
		return symbol;
	}

	public int getThreshold1() {
		return threshold1;
	}

	public int getThreshold2() {
		return threshold2;
	}

	
	/**
	 * Return Returns a String object representing the Advisor.
	 * For example: NOP == 5 become NOP_method_equal_5
	 * */
	public String toString() {
		String string = this.metric + "_" + this.level;
		
		string = (symbol.equals("<"))  ? string + "_less_"       + threshold1 : string;
		string = (symbol.equals("<=")) ? string + "_less_eq_"    + threshold1 : string;
		string = (symbol.equals(">"))  ? string + "_greater_"    + threshold1 : string;
		string = (symbol.equals(">=")) ? string + "_greater_eq_" + threshold1 : string;
		string = (symbol.equals("!=")) ? string + "_not_eq_" 	 + threshold1 : string;
		string = (symbol.equals("==")) ? string + "_equal_" 	 + threshold1 : string;
		string = (symbol.equals("between")) ? string + "_between_"+ threshold1 +"_"+ threshold2 : string;
		
		return string;
	}
}
