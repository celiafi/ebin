import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Ebin {

	private static final float PEAK_MIN = -2.0f;
	private static final float PEAK_MAX = 0.0f;
	private static final float SNR_MIN = 40.0f;
	private static final float LUFS_MIN = -25.0f;
	private static final float LUFS_MAX = -13.0f;

	private static ArrayList<Stats> invalidStats = new ArrayList<Stats>();

	private static String executeExternalCommand(String[] commandArray)
			throws IOException {
		StringBuilder sb = new StringBuilder();

		Process process;

		process = new ProcessBuilder(commandArray).start();

		InputStream err = process.getErrorStream();
		Reader errReader = new InputStreamReader(err);
		BufferedReader eReader = new BufferedReader(errReader);

		InputStream stream = process.getInputStream();
		Reader reader = new InputStreamReader(stream);
		BufferedReader bReader = new BufferedReader(reader);

		sb.append("Error stream:\n");
		String eStream = readReader(eReader);
		sb.append(eStream);

		sb.append("Output stream:\n");
		String oStream = readReader(bReader);
		sb.append(oStream);

		int exitValue = process.exitValue();
		sb.append("Process exited with value: " + exitValue);
		sb.append(Constants.NEWLINE);

		return sb.toString();
	}

	private static String readReader(BufferedReader bReader) throws IOException {
		StringBuilder sb = new StringBuilder();
		String nextLine = null;
		while ((nextLine = bReader.readLine()) != null) {
			sb.append(nextLine);
			sb.append(Constants.NEWLINE);
		}
		return sb.toString();
	}

	private static String[] getSoxCommandArray(String filepath) {
		String[] comAr = new String[4];
		String workingDir = System.getProperty("user.dir");

		comAr[0] = workingDir + "\\sox";
		comAr[1] = filepath;
		comAr[2] = "-n";
		comAr[3] = "stats";
		return comAr;
	}

	private static String[] getFFMPEGCommandArray(String filepath) {
		String[] comAr = new String[9];
		String workingDir = System.getProperty("user.dir");

		comAr[0] = workingDir + "\\ffmpeg.exe";
		comAr[1] = "-nostats";
		comAr[2] = "-i";
		comAr[3] = filepath;
		comAr[4] = "-filter_complex";
		comAr[5] = "ebur128=peak=true";
		comAr[6] = "-f";
		comAr[7] = "null";
		comAr[8] = "-";
		return comAr;
	}

	private static SoxStats analyzeWithSox(String filepath) throws IOException {
		String[] soxCommandArray = getSoxCommandArray(filepath);

		String soxOutput = executeExternalCommand(soxCommandArray);
		SoxStats stats = SoxStats.parseStats(soxOutput);

		return stats;
	}

	private static FfmpegStats analyzeWithFFMPEG(String filepath)
			throws IOException {
		String[] ffmpegCommandArray = getFFMPEGCommandArray(filepath);

		String ffmpegOutput = executeExternalCommand(ffmpegCommandArray);
		FfmpegStats stats = FfmpegStats.parseStats(ffmpegOutput);

		return stats;
	}

	private static Stats analyzeFile(String path) throws IOException {
		SoxStats soxStats = analyzeWithSox(path);
		FfmpegStats ffmpegStats = analyzeWithFFMPEG(path);

		Stats stats = new Stats(path, soxStats, ffmpegStats);

		return stats;
	}

	private static String getExtensionFromFile(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return name.substring(lastIndexOf);
	}

	private static boolean matchExtension(File file, String extension) {
		String fileExtension = getExtensionFromFile(file);
		return fileExtension.equals(extension);
	}

	public static ArrayList<File> listAllFiles(File dir) {
		ArrayList<File> files = new ArrayList<File>();
		for (File file : dir.listFiles()) {
			if (file.isFile())
				files.add(file);
			else if (file.isDirectory())
				files.addAll(listAllFiles(file));
		}
		return files;
	}

	public static void main(String[] args) {

		if (args.length != 2) {
			showUsage();
			return;
		}

		File directory = new File(args[0]);
		File report = new File(args[1]);
		File[] files = directory.listFiles();
		ArrayList<File> allFiles = listAllFiles(directory);

		StringBuilder sb = new StringBuilder();
		sb.append("Audio analysis report for directory " + directory);
		sb.append(Constants.NEWLINE);

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		sb.append("Analysis started at " + timeStamp);
		sb.append(Constants.NEWLINE);
		sb.append(Constants.NEWLINE);

		for (File file : allFiles) {
			if (matchExtension(file, ".mp3") || matchExtension(file, ".wav")) {
				try {
					Stats stats = analyzeFile(file.getAbsolutePath());

					String analysis = ReportFormatter.formatStats(stats);

					sb.append(analysis);
					sb.append(Constants.NEWLINE);
					sb.append(Constants.NEWLINE);

					System.out.println(analysis);

					if (!checkStatsForValidity(stats)) {
						invalidStats.add(stats);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar
				.getInstance().getTime());
		sb.append("Analysis finished at " + timeStamp);

		String invalid = appendInvalidStats();
		sb.append(invalid);

		sb.append("End of report.");
		System.out.println("Done.");

		try {
			ReportWriter.writeStringToFile(sb.toString(), report);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String appendInvalidStats() {
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.NEWLINE + "INVALID FILES:" + Constants.NEWLINE
				+ Constants.NEWLINE);

		for (Stats stats : invalidStats) {
			sb.append(ReportFormatter.formatStats(stats));
			sb.append(Constants.NEWLINE);
			sb.append(Constants.NEWLINE);
		}

		return sb.toString();
	}

	private static boolean checkStatsForValidity(Stats stats) {
		float peak = stats.getPeak();
		float snr = stats.getSnr();
		float lufs = stats.getLufs();

		if (!(peak <= PEAK_MAX && peak >= PEAK_MIN))
			return false;
		if (snr < SNR_MIN)
			return false;
		if (!(lufs <= LUFS_MAX && lufs >= LUFS_MIN))
			return false;

		return true;
	}

	private static void showUsage() {
		System.out.println("Usage information:");
		System.out.println("java -jar ebin.jar dir report");
		System.out.println("where \"dir\" is the directory to be analyzed");
		System.out
				.println("and \"report\" is where you want the report to be saved");
	}

}
