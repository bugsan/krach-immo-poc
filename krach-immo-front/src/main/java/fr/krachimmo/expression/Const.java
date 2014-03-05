package fr.krachimmo.expression;

public class Const implements Expression {

	private final int value;

	public Const(int value) {
		this.value = value;
	}
	@Override
	public int eval(Context context) {
		return this.value;
	}
}
