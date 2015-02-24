import java.util.Scanner;
import java.util.StringTokenizer;

public class SoxStats {

	private float peakLevel;
	private float snr;
	private float rmsPk;
	private float rmsTr;

	/**
	 * SoX output parsing constants
	 */
	private final static String PEAK_LEVEL_ID = "Pk lev dB";
	private final static String RMS_PEAK_ID = "RMS Pk dB";
	private final static String RMS_TROUGH_ID = "RMS Tr dB";

	SoxStats(float peakLevel, float rmsPk, float rmsTr) {
		this.peakLevel = peakLevel;
		this.rmsPk = rmsPk;
		this.rmsTr = rmsTr;

		this.snr = rmsPk - rmsTr;
	}

	float getPeakLevel() {
		return this.peakLevel;
	}

	float getSnr() {
		return this.snr;
	}

	float getRmsPeak() {
		return this.rmsPk;
	}

	float getRmsTrough() {
		return this.rmsTr;
	}

	static SoxStats parseStats(String stats) {
		float peakLevel = Float.NaN;
		float rmsPk = Float.NaN;
		float rmsTr = Float.NaN;

		Scanner scanner = new Scanner(stats);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			if (line.startsWith(PEAK_LEVEL_ID)) {
				int trimLength = PEAK_LEVEL_ID.length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				String token = getFirstToken(trimmed);
				if (!token.isEmpty())
					peakLevel = Float.parseFloat(token);
			}

			if (line.startsWith(RMS_PEAK_ID)) {
				int trimLength = RMS_PEAK_ID.length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				String token = getFirstToken(trimmed);
				if (!token.isEmpty())
					rmsPk = Float.parseFloat(token);
			}

			if (line.startsWith(RMS_TROUGH_ID)) {
				int trimLength = RMS_TROUGH_ID.length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				String token = getFirstToken(trimmed);
				token = getToken(line, 4);
				if (!token.isEmpty())
					rmsTr = Float.parseFloat(token);
			}
		}
		scanner.close();

		return new SoxStats(peakLevel, rmsPk, rmsTr);
	}

	static String getToken(String s, int n) {
		String token = "";
		StringTokenizer st = new StringTokenizer(s);
		int i = 0;
		while (st.hasMoreTokens() && i <= n) {
			token = st.nextToken();
			i++;
		}
		return token;
	}

	static String getFirstToken(String s) {
		String firstToken = "";
		StringTokenizer st = new StringTokenizer(s);
		if (st.hasMoreTokens()) {
			firstToken = st.nextToken();
		}
		return firstToken;
	}

}
