package server;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * @author YICHENG JIN
 * @student ID: 962546
 * @userName: YiCheng Jin
 */

public class ServerGUI {
	private JLabel numOfClient;
	private JLabel portNum;
	private JList<String> hostName;
	private JList<String> address;
	private JButton kill;
	private JButton exit;

	public JList<String> getHostName() {
		return hostName;
	}
	
	public JList<String> getAddress() {
		return address;
	}
	
	public JLabel getNumOfClient() {
		return numOfClient;
	}
	
	public JLabel getPort() {
		return portNum;
	}
	
	public void setHostAndAddress() {
		ArrayList<Server> newThreadList = MultiThreadServer.getThread();
    	String[] hostArray = new String[newThreadList.size()];
    	String[] addressArray = new String[newThreadList.size()];
    	int i = 0;
    	for (Server entry: newThreadList) {
    		hostArray[i] = entry.getSocket().getInetAddress().getHostName();
    		addressArray[i] = entry.getSocket().getInetAddress().getHostAddress();
    		i++;
    	}
    	hostName.setListData(hostArray);
    	address.setListData(addressArray);
	}
	
	public void drawServerGUI() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(null);  
		
		JLabel hostNameJLabel = new JLabel("HostName");
		JLabel addressJLabel = new JLabel("Address");
		JLabel numOfClientJLabel = new JLabel("The number of connected clients: ");
		JLabel portJLabel = new JLabel("Server listening on port: ");
		numOfClient = new JLabel("0");
		portNum = new JLabel();
		hostName = new JList<String>();
		address = new JList<String>();
		kill = new JButton("kill threads");
		exit = new JButton("exit");
		
		frame.setTitle("ServerGUI");
		frame.setSize(500, 400);
		
		hostNameJLabel.setBounds(90, 40, 100, 30);
		addressJLabel.setBounds(280, 40, 100, 30);
		hostName.setBounds(60, 80, 150, 130);
		address.setBounds(250, 80, 150, 130);
		numOfClientJLabel.setBounds(30, 240, 300, 30);
		numOfClient.setBounds(350, 240, 50, 30);
		kill.setBounds(70, 270, 130, 30);
		exit.setBounds(270, 270, 130, 30);
		portJLabel.setBounds(30, 220, 300, 30);
		portNum.setBounds(310, 220, 50, 30);
		
		hostNameJLabel.setFont(new Font("Arial", Font.BOLD, 16));
		addressJLabel.setFont(new Font("Arial", Font.BOLD, 16));
		numOfClientJLabel.setFont(new Font("Arial", Font.BOLD, 16));
		numOfClient.setFont(new Font("Arial", Font.BOLD, 16));
		kill.setFont(new Font("Arial", Font.BOLD, 16));
		exit.setFont(new Font("Arial", Font.BOLD, 16));
		portJLabel.setFont(new Font("Arial", Font.BOLD, 16));
		portNum.setFont(new Font("Arial", Font.BOLD, 16));
		
		addActionListenerK(kill);
		addActionListenerE(exit);
		
		panel.add(hostNameJLabel);
		panel.add(addressJLabel);
		panel.add(numOfClientJLabel);
		panel.add(numOfClient);
		panel.add(hostName);
		panel.add(address);
		panel.add(kill);
		panel.add(exit);
		panel.add(portJLabel);
		panel.add(portNum);

		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setVisible(true);
	}
	
	private void addActionListenerK(JButton button) {
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	String killName = (String) hostName.getSelectedValue();
            	ArrayList<Server> threadList = MultiThreadServer.getThread();
            	for (Server entry: threadList) {
            		if (killName.equals(entry.getSocket().getInetAddress().getHostName())) {
            			try {
							entry.getSocket().close();
							MultiThreadServer.removeThread(entry);
							break;
						} 
            			catch (IOException e) {
							System.out.println("There is a problem with closing socket.");
						}
            		}
            	}
            	setHostAndAddress();
            	Integer clientsNum = Integer.parseInt(numOfClient.getText()) - 1;
            	if (clientsNum >= 0) {
            		numOfClient.setText(clientsNum.toString());
            	}
            }
        });
    }
	
	private void addActionListenerE(JButton button) {
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	System.exit(0);
            }
        });
    }
}













