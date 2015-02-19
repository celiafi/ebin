import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Ebin {

	private static boolean executeExternalCommand() throws IOException {
		Process process;

		String[] comAr = new String[4];
		comAr[0] = "C:\\Program Files (x86)\\sox-14-4-1\\sox.exe";
		comAr[1] = "C:\\ws\\320015_0001.mp3";
		comAr[2] = "-n";
		comAr[3] = "stats";

		process = new ProcessBuilder(comAr).start();
		
		InputStream err = process.getErrorStream();
		Reader errReader = new InputStreamReader(err);
		BufferedReader eReader = new BufferedReader(errReader);
		
		InputStream stream = process.getInputStream();
		Reader reader = new InputStreamReader(stream);
		BufferedReader bReader = new BufferedReader(reader);
		
		String nextLine = null;
		while ((nextLine = eReader.readLine()) != null) {
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

	private static void analyzeWithSox() throws IOException {
		executeExternalCommand();
	}

	public static void main(String[] args) {
		try {
			analyzeWithSox();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("ebin juddu mage");
	}

}
