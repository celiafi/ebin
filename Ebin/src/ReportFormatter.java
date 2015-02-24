
public class ReportFormatter {
	
	static String formatStats(Stats stats) {

		StringBuilder sb = new StringBuilder();
		sb.append("file:\t");
		sb.append(stats.getPath());
		sb.append(Constants.NEWLINE + "pk dB:\t");
		sb.append(stats.getPeak());
		sb.append(Constants.NEWLINE + "snr dB:\t");
		sb.append(stats.getSnr());
		sb.append(Constants.NEWLINE + "lufs:\t");
		sb.append(stats.getLufs());

		return sb.toString();
	}
}
