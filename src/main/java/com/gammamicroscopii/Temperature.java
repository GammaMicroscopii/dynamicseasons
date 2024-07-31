package com.gammamicroscopii;


public class Temperature {

	public static final Temperature FREEZING = Temperature.of(0f, Unit.CELSIUS);
	public static final Temperature ABSOLUTE_ZERO = Temperature.of(0f, Unit.KELVIN);

	private final float value; //value stored in Celsius

	public Temperature(float value) { // unit = Unit.CELSIUS
		this.value = value;
	}

	public Temperature(float value, Unit unit) {
		this.value = value * unit.unitSize + unit.originCelsius;
	}

	public static Temperature of(float value, Unit unit) {
		return new Temperature(value, unit);
	}

	public float get() { // unit = Unit.CELSIUS
		return value;
	}

	public float get(Unit unit) {
		return (value - unit.originCelsius) / unit.unitSize;
	}

	public String toString() { // unit = Unit.CELSIUS
		return Float.toString(this.value).concat(" ºC");
	}

	public String toString(Unit unit) {
		return Float.toString(this.get(unit)).concat(unit.symbol);
	}

	public Temperature delta(float increase) { // unit = Unit.CELSIUS
		return new Temperature(this.value + increase);
	}

	public Temperature delta(float increase, Unit unit) {
		return new Temperature(this.value + increase * unit.unitSize);
	}

	public static enum Unit {
		VANILLA(-3f, 30f, ""),
		CELSIUS(0f, 1f, " ºC"),
		FAHRENHEIT(-17.7777778f, 0.55555556f, " ºF"),
		KELVIN(-273.15f, 1f, " K");

		public final float originCelsius;
		public final float unitSize;
		public final String symbol;

		Unit(float originCelsius, float unitSize, String symbol) {
			this.unitSize = unitSize;
			this.originCelsius = originCelsius;
			this.symbol = symbol;
		}

		public String toString() {
			return symbol;
		}

	}

}
