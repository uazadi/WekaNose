package it.unimib.disco.essere.WekaNose.dfcm4j;

import java.util.Arrays;

import it.unimib.disco.essere.WekaNose.exception.NotValidConditionException;

public class Condition {
	
	public static String[] VALID_SIYBOL = {"<", "<=", ">", ">=", "!=", "==", "between"};
	public static String[] VALID_LEVEL = {"method", "class"};
	
	private String metric;
	private String level;

	private String symbol;
	private int num1;
	private int num2;
	
	/**
	 * @param nameProject
	 * @param metric
	 * @param level
	 * @param symbol
	 * @param num1
	 * @param num2
	 * @throws NotValidConditionException
	 */
	public Condition(String metric, String level, String symbol, int num1, int num2) throws NotValidConditionException{
		
		this.metric = metric;
		
		if(Arrays.asList(Condition.VALID_LEVEL).contains(level))
			this.level = level;
		else
			throw new NotValidConditionException("the level " + level + " is not valid");
		
		if(Arrays.asList(Condition.VALID_SIYBOL).contains(symbol))
			this.symbol = (symbol.equals("!=")) ? "<>" : symbol;
		else
			throw new NotValidConditionException("the symbol " + symbol + " is not valid");
		
		try {
			this.num1 = num1;
			this.num2 = (this.symbol.equals("between")) ? num2 : 0;
		} catch(NumberFormatException e) {
			throw new NotValidConditionException("one (or both) of the numbers are  not valid");
		}
	}
	
	/**
	 * @param nameProject
	 * @param metric
	 * @param level
	 * @param symbol
	 * @param num1
	 * @param num2
	 * @throws NotValidConditionException
	 */
	public Condition(String metric, String level, String symbol, int num1) throws NotValidConditionException{
		this(metric, level, symbol, num1, 0);
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}
	
	public void setLevel(String level) throws NotValidConditionException {
		if(Arrays.asList(Condition.VALID_LEVEL).contains(level))
			this.level = level;
		else
			throw new NotValidConditionException("the level " + level + " is not valid");
	}

	public void setSymbol(String symbol) throws NotValidConditionException {
		if(Arrays.asList(Condition.VALID_SIYBOL).contains(symbol))
			this.symbol = symbol;
		else
			throw new NotValidConditionException("the symbol " + symbol + " is not valid");
	}

	public void setNum1(int num1) {
		this.num1 = num1;
	}

	public void setNum2(int num2) {
		this.num2 = num2;
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

	public int getNum1() {
		return num1;
	}

	public int getNum2() {
		return num2;
	}
	
	public String toString() {
		String string = this.metric + "_" + this.level;
		
		// "<", "<=", ">", ">=", "!=", "==", "between"
		string = (symbol.equals("<"))  ? string + "_less_"       + num1 : string;
		string = (symbol.equals("<=")) ? string + "_less_eq_"    + num1 : string;
		string = (symbol.equals(">"))  ? string + "_greater_"    + num1 : string;
		string = (symbol.equals(">=")) ? string + "_greater_eq_" + num1 : string;
		string = (symbol.equals("!=")) ? string + "_not_eq_" 	 + num1 : string;
		string = (symbol.equals("==")) ? string + "_equal_" 	 + num1 : string;
		string = (symbol.equals("between")) ? string + "_between_"+ num1 +"_"+ num2 : string;
		
		return string;
	}
}
