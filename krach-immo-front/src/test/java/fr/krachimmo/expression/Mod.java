package fr.krachimmo.expression;

public class Mod implements Expression {

	private final Expression left;
	private final Expression right;

	public Mod(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}
	@Override
	public int eval(Context context) {
		return this.left.eval(context) % this.right.eval(context);
	}
}
