import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Ebin {

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
		sb.append("\n");

		return sb.toString();
	}

	private static String readReader(BufferedReader bReader) throws IOException {
		StringBuilder sb = new StringBuilder();
		String nextLine = null;
		while ((nextLine = bReader.readLine()) != null) {
			sb.append(nextLine);
			sb.append("\n");
		}
		return sb.toString();
	}

	private static String[] getSoxCommandArray(String filepath) {
		String[] comAr = new String[4];
		comAr[0] = "sox";
		comAr[1] = filepath;
		comAr[2] = "-n";
		comAr[3] = "stats";
		return comAr;
	}

	private static String[] getFFMPEGCommandArray(String filepath) {
		String[] comAr = new String[9];
		comAr[0] = "ffmpeg";
		comAr[1] = "-nostats";
		comAr[2] = "-i";
		comAr[3] = filepath;
		comAr[4] = "-filter_complex";
		comAr[5] = "ebur128";
		comAr[6] = "-f";
		comAr[7] = "null";
		comAr[8] = "-";
		return comAr;
	}

	private static String analyzeWithSox(String filepath) throws IOException {
		String[] soxCommandArray = getSoxCommandArray(filepath);
		StringBuilder sb = new StringBuilder();

		String soxOutput = executeExternalCommand(soxCommandArray);
		SoxStats stats = SoxStats.parseStats(soxOutput);

		sb.append("\r\n\r\nFILE: ");
		sb.append(filepath);
		sb.append("\r\nPEAK LEVEL: ");
		sb.append(stats.peakLevel);
		sb.append("\r\nSNR: ");
		sb.append(stats.snr);

		return sb.toString();
	}

	private static String analyzeWithFFMPEG(String filepath) throws IOException {
		String[] ffmpegCommandArray = getFFMPEGCommandArray(filepath);
		StringBuilder sb = new StringBuilder();

		String ffmpegOutput = executeExternalCommand(ffmpegCommandArray);
		FfmpegStats stats = FfmpegStats.parseStats(ffmpegOutput);

		sb.append("\r\nLUFS: ");
		sb.append(stats.lufs);

		return sb.toString();
	}

	private static String analyzeFile(String path) throws IOException {
		File report = new File("C:\\report.txt");

		String soxResult = analyzeWithSox(path);
		String ffmpegResult = analyzeWithFFMPEG(path);

		return soxResult + "\r\n" + ffmpegResult + "\r\n\r\n";
	}

	public static void main(String[] args) {

		File directory = new File(args[0]);
		File report = new File(args[1]);
		File[] files = directory.listFiles();

		StringBuilder sb = new StringBuilder();

		for (File file : files) {
			try {
				String analysis = analyzeFile(file.getAbsolutePath());
				System.out.println(analysis);
				sb.append(analysis);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			ReportWriter.writeStringToFile(sb.toString(), report);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
