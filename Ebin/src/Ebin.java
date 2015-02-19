import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

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

	private static void analyzeWithSox(String filepath) throws IOException {
		String[] soxCommandArray = getSoxCommandArray(filepath);
		executeExternalCommand(soxCommandArray);
	}

	private static void analyzeWithFFMPEG(String filepath) throws IOException {
		String[] ffmpegCommandArray = getFFMPEGCommandArray(filepath);
		executeExternalCommand(ffmpegCommandArray);
	}
	
	
	private static void analyzeFile(String path) throws IOException {
		analyzeWithSox(path);
		analyzeWithFFMPEG(path);
	}

	public static void main(String[] args) {

		File directory = new File(args[0]);
		File[] files = directory.listFiles();
		
		for (File file : files){
			try {
				analyzeFile(file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
