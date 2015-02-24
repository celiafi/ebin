import java.util.Scanner;

public class FfmpegStats {
	private float lufs;
	private float lra;

	/**
	 * FFMPEG output parsing constants
	 */
	private final static String LUFS_ID = "    I:";
	private final static String LRA_ID =  "    LRA:";

	FfmpegStats(float lufs, float lra) {
		this.lufs = lufs;
		this.lra = lra;
	}
	
	float getLufs(){
		return this.lufs;
	}
	
	float getLra(){
		return this.lra;
	}

	static FfmpegStats parseStats(String stats) {
		float lufs = Float.NaN;
		float lra = Float.NaN;

		Scanner scanner = new Scanner(stats);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			if (line.startsWith(LUFS_ID)) {
				int prefixTrimLength = LUFS_ID.length();
				int suffixTrimLength = " LUFS".length();

				String stripped = line.substring(prefixTrimLength,
						line.length() - suffixTrimLength);

				String trimmed = stripped.trim();
				if (!trimmed.isEmpty())
					lufs = Float.parseFloat(trimmed);
			}
			
			if (line.startsWith(LRA_ID)) {
				int prefixTrimLength = LRA_ID.length();
				int suffixTrimLength = " LU".length();

				String stripped = line.substring(prefixTrimLength,
						line.length() - suffixTrimLength);

				String trimmed = stripped.trim();
				if (!trimmed.isEmpty())
					lra = Float.parseFloat(trimmed);
			}
		}
		scanner.close();

		return new FfmpegStats(lufs, lra);
	}
}
