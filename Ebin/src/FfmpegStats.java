import java.util.Scanner;

public class FfmpegStats {
	float lufs;

	private final static String LUFS_ID = "    I:";

	FfmpegStats(float lufs) {
		this.lufs = lufs;
	}

	static FfmpegStats parseStats(String stats) {
		float lufs = Float.NaN;

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
		}
		scanner.close();

		return new FfmpegStats(lufs);
	}
}
