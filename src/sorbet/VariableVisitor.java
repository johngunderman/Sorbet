package sorbet;

import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import japa.parser.ast.body.VariableDeclarator;

public class VariableVisitor extends VoidVisitorAdapter {
	
	@Override
	public void visit(AssignExpr ae, Object arg) {
		System.out.println("line: " 
				+ ae.getBeginLine() + " variable: " + ae.getTarget().toString());
	}
	
	@Override
	public void visit(NameExpr ne, Object arg) {
		System.out.println("line: " + ne.getBeginLine() + " name: " + ne.getName());
	}
	
	@Override
	public void visit(VariableDeclarator vd, Object arg) {
		System.out.println("line: " +  vd.getBeginLine() + " var: " + vd.getId().getName());
	}
}
