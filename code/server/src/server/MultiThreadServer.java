package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author YICHENG JIN
 * @student ID: 962546
 * @userName: YiCheng Jin
 */

public class MultiThreadServer {
	private static ArrayList<Server> multiThread = new ArrayList<Server>();

	public static void addThread(Server server) {
		multiThread.add(server);
	}
	
	public static ArrayList<Server> getThread(){
		return multiThread;
	}
	
	public static void removeThread(Server server) {
		multiThread.remove(server);
	}
	
	public static void main(String[] args) {
		final int MAXTHREADS = 100;
		Integer port = Integer.parseInt(args[0]);
		ServerSocket serverSocket = null;
		Socket socket = null;
		ExecutorService executors = Executors.newFixedThreadPool(MAXTHREADS);
		try {
			serverSocket = new ServerSocket(port);
			ServerGUI serverGUI = new ServerGUI();
			serverGUI.drawServerGUI();
			while (true) {
				serverGUI.getPort().setText(new Integer(port).toString());
				socket = serverSocket.accept();
				Server server = new Server(args[1], socket, serverGUI);
				MultiThreadServer.addThread(server);
				executors.execute(server);
				serverGUI.setHostAndAddress();
				Integer size = new Integer(MultiThreadServer.getThread().size());
				serverGUI.getNumOfClient().setText(size.toString());
			}
		}
		catch (SocketException e) {
			System.out.println("There is a problem with new an object of socket.");
		}
		catch (IOException e) {
			System.out.println("There is a problem with waiting for connection.");
		}
		finally {
			try {
				socket.close();
				serverSocket.close();
			} 
			catch (IOException e) {
				System.out.println("There is a problem with closing server.");
			}
		}
		
	}
}
