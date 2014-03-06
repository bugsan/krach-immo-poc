package fr.krachimmo.expression;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExpressionTest {

	@Test
	public void example() {
		// ((a²+b+2)/2)%4
		Expression e1 = new Pow(new Var("a"), new Const(2));
		Expression e2 = new Var("b");
		Expression e3 = new Add(e1, new Add(e2, new Const(5)));
		Expression e4 = new Div(e3, new Const(2));
		Expression e5 = new Mod(e4, new Const(4));

		ContextMap context = new ContextMap();
		context.put("a", 2);
		context.put("b", 3);

		assertEquals(2, e5.eval(context));
		assertEquals(3, new Sqrt(new Const(9)).eval(context));
	}
}
