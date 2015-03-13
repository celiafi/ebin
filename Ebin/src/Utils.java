public class Utils {
	public static final String NEG_INF = "-1.#J";

	public static float parseFloat(String input) {
		float f = Float.NaN;

		if (input.isEmpty())
			return f;
		
		if (input.equals(NEG_INF)) {
			f = -Float.NEGATIVE_INFINITY;
			// TODO calculate correct value
		} else
			f = Float.parseFloat(input);

		return f;
	}
}
