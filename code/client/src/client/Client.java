package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author YICHENG JIN
 * @student ID: 962546
 * @userName: YiCheng Jin
 */

public class Client{
	
	public static void main(String[] args) {
		Integer port = Integer.parseInt(args[1]);
		Socket socket = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;
		ClientGUI window = null;
		try {
			socket = new Socket(args[0], port);
			window = new ClientGUI();
			window.drawGUI();
			window.getState().setText("Connection established.");
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());
			String respondString = null;
			String state = null;
			String respondMessage = null;
			JSONObject respond = new JSONObject();
			JSONObject message = new JSONObject();
			window.setDataOutputStream(outputStream);
			window.setMessage(message);
			while (true) {		
				respondString = new String(inputStream.readUTF());
				respond = new JSONObject(respondString);
				state = respond.getString("state");
				respondMessage = respond.getString("message");
				window.getState().setText(state);
				window.getTextArea().setText(respondMessage);
				if (state.equals("close")){
					break;
				}
			}	
		}
		catch (SocketException e) {
			window.getTextArea().setText("There is a problem with new an object of socket.");
		}
		catch (JSONException e) {
			window.getTextArea().setText("There is a problem with new an object of JSONObject.");
		}
		catch (IOException e) {
			window.getState().setText("close");
			window.getTextArea().setText("The socket is close.");
		} 
		finally {
			try {
				outputStream.close();
				inputStream.close();
				socket.close();
			} 
			catch (IOException e) {
				window.getTextArea().setText("There is a problem when closing socket.");
			}
		}
	}
}
	
	
	
