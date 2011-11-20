package sourceparser;

import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import japa.parser.ast.body.VariableDeclarator;

public class VariableVisitor<A> extends VoidVisitorAdapter<A> {
	
	private Lines lines = new Lines();
	
	public Lines getLines() {
		return lines;
	}
	
	@Override
	public void visit(NameExpr n, A arg) {		
		addVariableToLines(n.getBeginLine(), n.getName());
		
        super.visit(n, arg);
	}
	
	@Override
	public void visit(VariableDeclarator n, A arg) {		
		addVariableToLines(n.getBeginLine(), n.getId().getName());
		
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
