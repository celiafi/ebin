import java.util.Scanner;

public class SoxStats {

	float peakLevel;
	float snr;
	float rmsPk;
	float rmsTr;
	
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
	
	static SoxStats parseStats(String stats) {
		float peakLevel = Float.NaN;
		float rmsPk = Float.NaN;
		float rmsTr = Float.NaN;
		
		Scanner scanner = new Scanner(stats);
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			
			if(line.startsWith(PEAK_LEVEL_ID)){
				int  trimLength = PEAK_LEVEL_ID.length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				peakLevel = Float.parseFloat(trimmed);
			}
			
			if(line.startsWith(RMS_PEAK_ID)){
				int trimLength = RMS_PEAK_ID.length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				rmsPk = Float.parseFloat(trimmed);
			}
			
			if(line.startsWith(RMS_TROUGH_ID)){
				int trimLength = RMS_TROUGH_ID.length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				rmsTr = Float.parseFloat(trimmed);
			}
		}
		scanner.close();
		
		return new SoxStats(peakLevel, rmsPk, rmsTr);
	}

}
