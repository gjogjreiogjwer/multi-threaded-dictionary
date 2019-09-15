package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author YICHENG JIN
 * @student ID: 962546
 * @userName: YiCheng Jin
 */

public class ClientGUI{
	private JTextArea textArea;
	private JTextField textField;
	private JLabel state;
	private JButton query;
	private JButton add;
	private JButton remove;
	private JButton exit;
	private String input;
	private JSONObject message;
	private boolean systemOpen;
	private DataOutputStream outputStream;
	
	public ClientGUI() {
		systemOpen = true;
	}
	
	public void setDataOutputStream(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public void setMessage(JSONObject message) {
		this.message = message;
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public JLabel getState() {
		return state;
	}
	
	public void drawGUI() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(null);  

		JLabel inputLabel = new JLabel("input: ");
		JLabel onputLabel = new JLabel("ontput: ");
		JLabel stateLabel = new JLabel("state: ");
		query = new JButton("query");
		add = new JButton("add");
		remove = new JButton("remove");
		exit = new JButton("exit");
		textField = new JTextField();
		state = new JLabel();
		textArea = new JTextArea();
		
		frame.setTitle("ClientGUI");
		frame.setSize(500, 400);
		
		inputLabel.setBounds(30, 40, 70, 30);
		onputLabel.setBounds(30, 120, 70, 30);
		stateLabel.setBounds(30, 80, 70, 30);
		state.setBounds(120, 80, 300, 30);
		textField.setBounds(120, 40, 300, 30);
		textArea.setBounds(120, 120, 300, 80);
		query.setBounds(80, 220, 80, 30);
		add.setBounds(180, 220, 80, 30);
		remove.setBounds(280, 220, 80, 30);
		exit.setBounds(380, 220, 80, 30);
		textArea.setLineWrap(true);
		
		inputLabel.setFont(new Font("Arial", Font.BOLD, 18));
		onputLabel.setFont(new Font("Arial", Font.BOLD, 18));
		stateLabel.setFont(new Font("Arial", Font.BOLD, 18));
		state.setFont(new Font("Arial", Font.BOLD, 18));
		query.setFont(new Font("Arial", Font.PLAIN, 16));
		add.setFont(new Font("Arial", Font.PLAIN, 16));
		remove.setFont(new Font("Arial", Font.PLAIN, 16));
		exit.setFont(new Font("Arial", Font.PLAIN, 16));

		addActionListenerQ(query);
		addActionListenerA(add);
		addActionListenerR(remove);
		addActionListenerE(exit);

		panel.add(inputLabel);
		panel.add(onputLabel);
		panel.add(stateLabel);
		panel.add(state);
		panel.add(textField);
		panel.add(textArea);
		panel.add(query);
		panel.add(add);
		panel.add(remove);
		panel.add(exit);

		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setVisible(true);
	}
	
	private void addActionListenerQ(JButton button) {
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if (systemOpen == true) {
            		try {
            		input = textField.getText().trim();
					message.put("command", "query");
					message.put("word", input);
					outputStream.writeUTF(message.toString());
				} 
				catch (JSONException e) {
					textArea.setText("There is a problem when querying.");
				}
				catch (IOException e) {
					textArea.setText("The socket is close.");
				}
            	}
            }
        });
    }
	
	private void addActionListenerA(JButton button) {
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if (systemOpen == true) {
            		input = textField.getText().trim();
            		StringTokenizer st=new StringTokenizer(input);
            		
            		
        			String meaning = "";
        			String word = null;
        			if (st.hasMoreTokens()) {
        				word = st.nextToken();
        			}
        			while (st.hasMoreTokens()) {
        				meaning += st.nextToken() +  " ";
        			}
        			try {
        				message.put("command", "add");
        				message.put("word", word);
        				message.put("meaning", meaning);
        				outputStream.writeUTF(message.toString());
        			} 
        			catch (JSONException e) {
        				textArea.setText("There is a problem when adding.");
        			}
        			catch (IOException e) {
        				textArea.setText("The socket is close.");
        			}
            		
            	}
            }
        });
    }
	
	private void addActionListenerR(JButton button) {
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if (systemOpen == true) {
            		try {
            		input = textField.getText().trim();
					message.put("command", "remove");
					message.put("word", input);
					outputStream.writeUTF(message.toString());
            		} 
            		catch (JSONException e) {
            			textArea.setText("There is a problem with removing.");
            		}
            		catch (IOException e) {
            			textArea.setText("The socket is close.");
            		}
            	}
            }
        });
    }
	
	private void addActionListenerE(JButton button) {
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if (systemOpen == true) {
            		try {
					message.put("command", "exit");
					outputStream.writeUTF(message.toString());
					systemOpen = false;
            		} 
            		catch (JSONException e) {
            			textArea.setText("There is a problem with exiting.");
            		} 
            		catch (IOException e) {
            			textArea.setText("The socket is close.");
            		}
            	}
            }
        });
    }
}
