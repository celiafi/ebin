import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ebin {

	private static String executeExternalCommand(String[] commandArray)
			throws IOException {
		String result = "";
		Process process;

		process = new ProcessBuilder(commandArray).start();

		InputStream err = process.getErrorStream();
		Reader errReader = new InputStreamReader(err);
		BufferedReader eReader = new BufferedReader(errReader);

		InputStream stream = process.getInputStream();
		Reader reader = new InputStreamReader(stream);
		BufferedReader bReader = new BufferedReader(reader);

		String nextLine = null;
		result = result.concat("Error stream:\n");
		while ((nextLine = eReader.readLine()) != null) {
			result = result.concat(nextLine);
			result = result.concat("\n");
		}

		result = result.concat("Output stream:\n");
		while ((nextLine = bReader.readLine()) != null) {
			result = result.concat(nextLine);
			result = result.concat("\n");
		}

		int exitValue = process.exitValue();
		result = result.concat("Process exited with value: " + exitValue);
		result = result.concat("\n");

		return result;
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
		return executeExternalCommand(soxCommandArray);
	}

	private static String analyzeWithFFMPEG(String filepath) throws IOException {
		String[] ffmpegCommandArray = getFFMPEGCommandArray(filepath);
		return executeExternalCommand(ffmpegCommandArray);
	}

	private static void analyzeFile(String path) throws IOException {
		System.out.print(analyzeWithSox(path));
		System.out.print(analyzeWithFFMPEG(path));
	}

	public static void main(String[] args) {

		File directory = new File(args[0]);
		File[] files = directory.listFiles();

		for (File file : files) {
			try {
				analyzeFile(file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
