package events;

import java.util.Stack;

public class VariablesStack extends Stack<VariablesMap> {
	private int lineNumber;
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
}
