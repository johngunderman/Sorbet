package sourceparser;

import java.util.ArrayList;

import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import japa.parser.ast.body.VariableDeclarator;

public class VariableVisitor<A> extends VoidVisitorAdapter<A> {
	
	private Lines lines = new Lines();
	
	// store the col of the last field access we've seen. cleared out if the
	// next thing isn't a thisExpr
	private int fieldCol = -1;

	private String lastField = "";
	
	public Lines getLines() {
		return lines;
	}
	
	@Override
	public void visit(NameExpr n, A arg) {		
		addVariableToLines(n.getBeginLine(), n.getName());
		
		fieldCol = -1;
		lastField  = "";
		
        super.visit(n, arg);
	}
	
	@Override
	public void visit(VariableDeclarator n, A arg) {		
		addVariableToLines(n.getBeginLine(), n.getId().getName());
		
		fieldCol = -1;
		lastField  = "";
		
        super.visit(n, arg);
	}
	
	@Override
	public void visit(FieldAccessExpr n, A arg) {		
		addVariableToLines(n.getBeginLine(), n.getField());
		
		fieldCol = n.getBeginColumn();
		lastField = n.getField();
		
        super.visit(n, arg);
	}
	
	@Override
	public void visit(ThisExpr n, A arg) {
		// we found a 'this' expression.
		if (fieldCol == n.getBeginColumn()) {
			Variables vars = lines.get(n.getBeginLine());
			if (vars == null) {
				//something must have borked
				fieldCol = -1;
				lastField  = "";
				System.err.println("pretty sure something broke in our visitor");
				return;
			}
			vars.removeLastOccurrence(lastField);
			vars.add("this." + lastField);
		}
        super.visit(n, arg);
	}
	
	private void addVariableToLines(int line, String variable) {
		Variables variables = lines.get(line);		
		if (variables == null) {
			variables = new Variables();
			
			lines.put(line, variables);
		}
		
		if (variables.contains(variable) == false) {
			variables.add(variable);
		}
	}
}
