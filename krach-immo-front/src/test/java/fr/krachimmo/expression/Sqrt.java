package fr.krachimmo.expression;

public class Sqrt implements Expression {

	private final Expression sub;

	public Sqrt(Expression sub) {
		this.sub = sub;
	}
	@Override
	public int eval(Context context) {
		return (int) Math.sqrt(this.sub.eval(context));
	}
}
