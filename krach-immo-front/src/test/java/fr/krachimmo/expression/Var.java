package fr.krachimmo.expression;

public class Var implements Expression {

	private final String name;

	public Var(String name) {
		this.name = name;
	}
	@Override
	public int eval(Context context) {
		return context.getValue(this.name);
	}
}
