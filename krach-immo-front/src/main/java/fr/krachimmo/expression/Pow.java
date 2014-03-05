package fr.krachimmo.expression;

public class Pow implements Expression {

	private final Expression left;
	private final Expression right;

	public Pow(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}
	@Override
	public int eval(Context context) {
		return (int) Math.pow(this.left.eval(context), this.right.eval(context));
	}
}
