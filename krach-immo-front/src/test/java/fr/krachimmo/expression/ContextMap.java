package fr.krachimmo.expression;

import java.util.HashMap;
import java.util.Map;

public class ContextMap implements Context {

	private final Map<String, Integer> variables = new HashMap<String, Integer>();

	@Override
	public int getValue(String name) {
		if (!this.variables.containsKey(name)) {
			throw new IllegalStateException(name + " cannot be resolved");
		}
		return this.variables.get(name);
	}

	public void put(String name, Integer value) {
		this.variables.put(name, value);
	}
}
