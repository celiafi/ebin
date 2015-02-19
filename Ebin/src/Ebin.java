import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Ebin {

	private static boolean executeExternalCommand(String[] commandArray)
			throws IOException {
		Process process;

		process = new ProcessBuilder(commandArray).start();

		InputStream err = process.getErrorStream();
		Reader errReader = new InputStreamReader(err);
		BufferedReader eReader = new BufferedReader(errReader);

		InputStream stream = process.getInputStream();
		Reader reader = new InputStreamReader(stream);
		BufferedReader bReader = new BufferedReader(reader);

		String nextLine = null;
		System.out.println("Error stream:");
		while ((nextLine = eReader.readLine()) != null) {
			System.out.println(nextLine);
		}

		System.out.println("Output stream:");
		while ((nextLine = bReader.readLine()) != null) {
			System.out.println(nextLine);
		}

		int exitValue = process.exitValue();
		System.out.println("Process exited with value: " + exitValue);
		if (exitValue == 0) {
			return true;
		} else {
			return false;
		}
	}

	private static String[] getSoxCommandArray() {
		String[] comAr = new String[4];
		comAr[0] = "sox";
		comAr[1] = "C:\\ws\\320015_0001.mp3";
		comAr[2] = "-n";
		comAr[3] = "stats";
		return comAr;
	}

	private static String[] getFFMPEGCommandArray() {
		String[] comAr = new String[9];
		comAr[0] = "ffmpeg";
		comAr[1] = "-nostats";
		comAr[2] = "-i";
		comAr[3] = "C:\\ws\\320015_0001.mp3";
		comAr[4] = "-filter_complex";
		comAr[5] = "ebur128";
		comAr[6] = "-f";
		comAr[7] = "null";
		comAr[8] = "-";
		return comAr;
	}

	private static void analyzeWithSox() throws IOException {
		String[] soxCommandArray = getSoxCommandArray();
		executeExternalCommand(soxCommandArray);
	}

	private static void analyzeWithFFMPEG() throws IOException {
		String[] ffmpegCommandArray = getFFMPEGCommandArray();
		executeExternalCommand(ffmpegCommandArray);
	}

	public static void main(String[] args) {
		try {
			analyzeWithSox();
			analyzeWithFFMPEG();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
