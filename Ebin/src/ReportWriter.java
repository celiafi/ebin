import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportWriter {

	static void writeStringToFile(String string, File file) throws IOException {

		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(string);
		bw.close();
	}
}
