package choco;

import java.util.Iterator;

import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;
import choco.kernel.solver.variables.integer.IntDomainVar;

public class TutorialSolutionLettersOnly {

	public void solve() {
	// Build model
	Model model = new CPModel();
	
	// Declare every letter as a variable
	IntegerVariable d = Choco.makeIntVar("d", 0, 9, Options.V_ENUM);
	IntegerVariable o = Choco.makeIntVar("o", 0, 9, Options.V_ENUM);
	IntegerVariable n = Choco.makeIntVar("n", 0, 9, Options.V_ENUM);
	IntegerVariable a = Choco.makeIntVar("a", 0, 9, Options.V_ENUM);
	IntegerVariable l = Choco.makeIntVar("l", 0, 9, Options.V_ENUM);
	IntegerVariable g = Choco.makeIntVar("g", 0, 9, Options.V_ENUM);
	IntegerVariable e = Choco.makeIntVar("e", 0, 9, Options.V_ENUM);
	IntegerVariable r = Choco.makeIntVar("r", 0, 9, Options.V_ENUM);
	IntegerVariable b = Choco.makeIntVar("b", 0, 9, Options.V_ENUM);
	IntegerVariable t = Choco.makeIntVar("t", 0, 9, Options.V_ENUM);
	
	// Array of coefficients
	int[] c = new int[]{100000, 10000, 1000, 100, 10, 1};
	
	// Declare every combination of letter as an integer expression
	IntegerExpressionVariable donaldLetters = Choco.scalar(new IntegerVariable[]{d, o, n, a,
	l, d}, c);
	IntegerExpressionVariable geraldLetters = Choco.scalar(new IntegerVariable[]{g, e, r, a,
	l, d}, c);
	IntegerExpressionVariable robertLetters = Choco.scalar(new IntegerVariable[]{r, o, b, e,
	r, t}, c);
	

	// Add constraint name sum
	model.addConstraint(Choco.eq(Choco.plus(donaldLetters, geraldLetters), robertLetters));
	
	// Add constraint of all different letters.
	model.addConstraint(Choco.allDifferent(d, o, n, a, l, g, e, r, b, t));
	
	// Build a solver, read the model and solve it
	Solver s = new CPSolver();
	s.read(model);
	s.solve();
	
	// Print name value
	Iterator<IntDomainVar> it=s.getIntVarIterator();
	while(it.hasNext()){
		IntDomainVar var=it.next();
		System.out.println(var.getName() + " = " +var.getVal());
	}
	}
}
