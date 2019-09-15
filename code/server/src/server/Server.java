package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author YICHENG JIN
 * @student ID: 962546
 * @userName: YiCheng Jin
 */

public class Server extends Thread{
	private JSONObject respond;
	private JSONObject dictionary;
	private String fileName;
	private Socket socket;
	private ServerGUI serverGUI;
	
	public Server(String fileName, Socket socket, ServerGUI serverGUI) {
		this.socket = socket;
		this.fileName = fileName;
		this.serverGUI = serverGUI;
		loadFile();
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void run() {
		String command = null;
		String word = null;
		String meaning = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;
		JSONObject inputJsonObject = null;
		try {
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());
			String clientMessage = null;

			while ((clientMessage = inputStream.readUTF()) != null) {
				inputJsonObject = new JSONObject(clientMessage);
				command = inputJsonObject.getString("command");

				if (command.equals("exit")) {
					exit(outputStream);
					break;
				}
				word = inputJsonObject.getString("word");
				if (command.equals("query")) {
					query(word);
				}
				else if (command.equals("add")) {
					meaning = inputJsonObject.getString("meaning");
					add(word, meaning);
				}
				else if (command.equals("remove")) {
					remove(word);
				}
				outputStream.writeUTF(respond.toString());
			}
		}
		catch (SocketException e) {
			System.out.println("There is a problem with new an object of socket.");
		}
		catch (JSONException e) {
			System.out.println("There is a problem with new an object of JSONObject.");
		}
		catch (IOException e) {
			System.out.println("There is a problem when sending message to a client.");
		} 	
		finally {
			try {
				outputStream.close();
				inputStream.close();
				socket.close();
			} 
			catch (IOException e) {
				System.out.println("There is a problem when closing socket.");
			}
		}
	}
	
	private synchronized void loadFile() {
		File file =new File(fileName); 
		if (file.exists()) {
			BufferedReader reader = null;
			String dictionaryString = "";
			try {
				FileInputStream fileInputStream = new FileInputStream(fileName);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				reader = new BufferedReader(inputStreamReader);
				String temp = null;
				while ((temp = reader.readLine()) != null) {
					dictionaryString += temp;
				}
				if (dictionaryString.equals("")) {
					dictionary = new JSONObject();
				}
				else {
					dictionary = new JSONObject(dictionaryString);
				}
			}
			catch (JSONException e) {
				System.out.println("There is a problem with new an object of JSONObject.");
			}
			catch (IOException e) {
				System.out.println("There is a problem with reading " + fileName);
			} 
			finally {
				if (reader != null) {
					try {
						reader.close();
					}
					catch (IOException e) {
						System.out.println("There is a problem with closing reader.");
					}
				}
			}
		}
		else {
			try {
				file.createNewFile();
				dictionary = new JSONObject();
			}
			catch (IOException e) {
				System.out.println("There is a problem with creating "+ fileName);
			}
		}
	}
	
	private synchronized void update(String fileName) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), "UTF-8"));
			String dictionartToUpdate = dictionary.toString();
			writer.write(dictionartToUpdate);
		}
		catch (IOException e) {
			System.out.println("There is a problem with updating "+ fileName);
		}
		finally {
			try {
				if (writer != null) {
					writer.close();
				}
			}
			catch (IOException e) {
				System.out.println("There is a problem with closing writer.");
			}
		}	
	}
	
	private synchronized void query(String word) {
		loadFile();
		respond = new JSONObject();
		try {
			if (dictionary.has(word)) {
				respond.put("state", "success");
				respond.put("message", dictionary.getString(word));
			}
			else {
				respond.put("state", "error");
				respond.put("message", "The word is not in the dictionary.");
			}
		}
		catch (JSONException e) {
			System.out.println("There is a problem with writing respond when querying the word.");
		}
	}
	
	private synchronized void add(String word, String meanString) {
		loadFile();
		respond = new JSONObject();
		try {
			if (dictionary.has(word)) {
				respond.put("state", "duplicate");
				respond.put("message", "The word has already existed in the dictionary.");
			}
			else if (meanString.equals("")) {
				respond.put("state", "error");
				respond.put("message", "Add the word without meaning");
			}
			else if (!dictionary.has(word)) {
				dictionary.put(word, meanString);
				update(fileName);
				respond.put("state", "success");
				respond.put("message", "The word is successfully entered into the dictionary.");
			}
		}
		catch (JSONException e) {
			System.out.println("There is a problem with writing respond when adding the word.");
		}
	}
	
	private synchronized void remove(String word) {
		loadFile();
		respond = new JSONObject();
		try {
			if (dictionary.has(word)) {
				dictionary.remove(word);
				update(fileName);
				respond.put("state", "success");
				respond.put("message", "The word was successfully deleted from the dictionary.");
			}
			else if (!dictionary.has(word)) {
				respond.put("state", "error");
				respond.put("message", "The word is not found.");
			}
		}
		catch (JSONException e) {
			System.out.println("There is a problem with writing respond when removing the word.");
		}
	}
	
	private synchronized void exit(DataOutputStream outputStream) {
		
		respond = new JSONObject();
		try {
			respond.put("state", "close");
			respond.put("message", "The system close.");
			outputStream.writeUTF(respond.toString());
			socket.close();
			MultiThreadServer.removeThread(this);
			Integer size = new Integer(MultiThreadServer.getThread().size());
			serverGUI.getNumOfClient().setText(size.toString());
			serverGUI.setHostAndAddress();
		}
		catch (JSONException e) {
			System.out.println("There is a problem with writing respond when removing the word.");
		}
		catch (IOException e) {
			System.out.println("There is a problem when sending message to a client.");
		} 	
	}
}
