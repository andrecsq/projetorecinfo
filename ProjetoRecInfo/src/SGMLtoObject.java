
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SGMLtoObject {

	public static ArrayList<Doc> toDoc(String filepath) throws IOException {
		File dir = new File(filepath);
		// File("C:\\Users\\André\\eclipse-workspace\\LuceneDemo\\src\\colecao_teste");
		File[] directoryListing = dir.listFiles();
		ArrayList<Doc> docs = new ArrayList<Doc>();
		if (directoryListing != null) {

			for (File child : directoryListing) {

				try (BufferedReader br = new BufferedReader(new FileReader(child))) {
					Doc d = null;

					String line = null;

					while ((line = br.readLine()) != null) {
						if (line.equals("<DOC>")) {
							d = new Doc();
							line = br.readLine();
							d.setId(line.substring(7, 20));
							d.setText("");
							// System.out.println(d.getId());
							br.readLine(); // <DOCID>
							br.readLine(); // <DATE>
							br.readLine(); // <TEXT> OU <CATEGORY>
							line = br.readLine(); // PRIMEIRA LINHA DO TEXTO ou <TEXT>
							if (line.contentEquals("<TEXT>")){
								line = br.readLine(); // agora é a primeira linha do texto
							}
							d.setText(line);
							while ((line = br.readLine()) != null) {
								if (line.length() >= 7 && line.substring(0, 7).equals("</TEXT>")) {
									break;
								}
								d.setText(d.getText() + " " + line);
							}
						} else if (line.equals("</DOC>")) {
							if (d != null) {
								docs.add(d);
								d = null;
							}
						}
					}
				}

				// Do something with child
			}
/*
			for (Doc d1 : docs) {
				System.out.println(d1.getId());
				System.out.println(d1.getText());
				System.out.println();
			}*/

		} else {
			System.out.println("deu ruim time");
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}

		return docs;
	}

}
