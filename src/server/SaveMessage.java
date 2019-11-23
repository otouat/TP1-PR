package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveMessage {

	public static void writeSingleMessage(String line) throws IOException {

		PrintWriter writer;
		writer = new PrintWriter(new BufferedWriter(new FileWriter("Messages.txt", true)));
		writer.println(line);
		writer.close();

	}

	public static String readAllMessages() throws IOException {
		String messages = "";
		for (String line : Files.readAllLines(Paths.get("Messages.txt"))) {
			messages += (line + "\n");
		}
		return messages;
	}

}