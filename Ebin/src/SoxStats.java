import java.util.Scanner;

public class SoxStats {

	float peakLevel;
	float snr;
	float rmsPk;
	float rmsTr;

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
			
			if(line.startsWith("Pk lev dB")){
				int  trimLength = "Pk lev dB".length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				peakLevel = Float.parseFloat(trimmed);
			}
			
			if(line.startsWith("RMS Pk dB")){
				int trimLength = "RMS Pk dB".length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				rmsPk = Float.parseFloat(trimmed);
			}
			
			if(line.startsWith("RMS Tr dB")){
				int trimLength = "RMS Tr dB".length();
				String stripped = line.substring(trimLength);
				String trimmed = stripped.trim();
				rmsTr = Float.parseFloat(trimmed);
			}
		}
		scanner.close();
		
		return new SoxStats(peakLevel, rmsPk, rmsTr);
	}

}
