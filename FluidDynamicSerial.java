package fluidDynamicsProject;

//import libraries
import java.util.Scanner;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import com.fazecast.jSerialComm.*;
//import com.google.common.base.Strings;
import java.io.*;
import java.util.Date;

public class FluidDynamicSerial {
	
	//class variables for file
	static File file;
	static FileWriter writeFile;
	static BufferedWriter buffWriteFile;
	
	//class var for Serial Port
	static SerialPort port;
	
	@SuppressWarnings({ "deprecation" })
	public static void main(String[] args) throws IOException {
		//create the window
		JFrame win = new JFrame();
		win.setSize(770, 150);
		win.setLayout(new BorderLayout());
		win.setTitle("Fluid Dynamics Serial Data Reciever");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Store the current directory in a variable
		File currentDir = new File(new File(".").getAbsolutePath());
		
		//create dropdown, directory type dropdown, connect button
		JComboBox<String> portList = new JComboBox<String>();
		JComboBox<String> directoryTypeList = new JComboBox<String>();
		JButton connectButton = new JButton("Connect");
		
		//Create button to choose a Directory to save .csv file
		JButton chooseDir = new JButton("Change/Choose Save Directory");
		
		//create labels to show serial status and Transmission time
		JLabel statusLab = new JLabel("Not connected to any port");
		//JLabel timerLab = new JLabel("Connect to Arduino to show Timing");
		
		//create labels to show the chosen directory
		JLabel guideLab = new JLabel("Chosen Directory: ");
		JLabel dirLab = new JLabel(currentDir.getCanonicalPath());
		
		//get all available ports and put in dropdown
		SerialPort[] portNames = SerialPort.getCommPorts();
		for(int i = 0; i < portNames.length; i++) {
			portList.addItem(portNames[i].getSystemPortName());
		}
		
		//put options in directory type dropdown
		directoryTypeList.addItem("Type: \\ (Recomended for windows)");
		directoryTypeList.addItem("Type: / (Recomended for MacOS)");
		
		//Choose Directory Button code
		chooseDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton openButton = new JButton("Open");
				
				//create the Directory/File Chooser
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Choose directory to save output csv file");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				//set the directory label to the chosen directory
				if(chooser.showOpenDialog(openButton) == JFileChooser.APPROVE_OPTION) {}
				try {
					dirLab.setText(chooser.getSelectedFile().getAbsolutePath());
				} catch(Exception e1) {
					JOptionPane.showMessageDialog(win,
						    "Cannot access Directory!",
						    "File Chooser Exception",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//Perform an action when the conect button is clicked
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(connectButton.getText().equals("Connect")) {
					//create and open the file
					String slashType = null;
					Date date = new Date();
					String month = new Integer(date.getMonth()+1).toString();
					String yr = new Integer(date.getYear()+1900).toString();
					String name = month + "-" + date.getDate() + "-" + yr + "_" + date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds() + ".csv";
					
					if(directoryTypeList.getSelectedIndex() == 0) {
						slashType = "\\";
					} else if(directoryTypeList.getSelectedIndex() == 1) {
						slashType = "/";
					}
					
					file = new File(dirLab.getText() + slashType + name);
					writeFile = null;
					try {
						writeFile = new FileWriter(file);
					} catch (IOException e1) {
						//show a error msg if file cannot be opened
						JOptionPane.showMessageDialog(win,
							    "Cannot open or create file",
							    "File Input Exception",
							    JOptionPane.ERROR_MESSAGE);
					}
					buffWriteFile = new BufferedWriter(writeFile);
					
					//connect to the selected serial port
					//disable dropdown and change connect button to disconnect button
					port = SerialPort.getCommPort(portList.getSelectedItem().toString());
					port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					if(port.openPort()) {
						connectButton.setText("Disconnect");
						statusLab.setText("connected and recieving data from " + port.getSystemPortName());
						portList.setEnabled(false);
					}
					
					Thread thread = new Thread() {
						//write serial data to file
						@Override
						public void run() {
							Scanner scan = new Scanner(port.getInputStream());
							while(scan.hasNext()) {
								//store serial data in variable
								/*String serialData = scan.next();
								if(Strings.isNullOrEmpty(serialData)) {
									continue;
								}*/
								
								//check if data is in csv format
								/*if(!serialData.contains(",")) {
									JOptionPane.showMessageDialog(win,
										    "The serial input does not contain the de-limiter \",\"",
										    "CSV De-limiter warning",
										    JOptionPane.WARNING_MESSAGE);
									continue;
								}
								
								//split the serial data from the commas to graph
								String[] dataValues = serialData.split(",");
								
								//make sure data sends on 3 kinds of information
								if(dataValues.length > 0 && dataValues.length == 3) {
									
									//store time and sensor reading as their data type in a var
									String millis = dataValues[0];
									String sensorOne = dataValues[1];
									String sensorTwo = dataValues[2];
									*/
									//write data in the excel file
									try {
										buffWriteFile.append(scan.nextLine() + "\n");
									} catch (IOException e) {
										JOptionPane.showMessageDialog(win,
											    "Cannot write to file",
											    "File Writer Exception",
											    JOptionPane.ERROR_MESSAGE);
									}
									
									//timerLab.setText(millis);

								//}
							}
							scan.close();

						}
					};
					thread.start();
				} else {
					//close the port and file once disconnect is clicked
					port.closePort();
					portList.setEnabled(true);
					connectButton.setText("Connect");
					//timerLab.setText("Connect to Arduino to show timing");
					try {
						buffWriteFile.close();
					} catch (IOException e1) {
						//show error msg if cannot close file
						JOptionPane.showMessageDialog(win,
							    "Cannot close file",
							    "Input/Output Exception",
							    JOptionPane.ERROR_MESSAGE);
					}
					
					//show info msg with created file path
					JOptionPane.showMessageDialog(win,
						    "File has been created in " + file.getAbsolutePath());
					statusLab.setText("Not connected to any port");
					//timerLab.setText("Connect to the Arduino to show timing");
				}
			}
			
		});
		
		
		
		
		//place dropdown, connect button and Transmission label on the top of the window
		JPanel topPanel = new JPanel();
		topPanel.add(portList);
		topPanel.add(connectButton);
		topPanel.add(statusLab);
		win.add(topPanel, BorderLayout.NORTH);
		
		
		//place chosen directory in center
		JPanel centerPanel = new JPanel();
		centerPanel.add(guideLab);
		centerPanel.add(dirLab);
		win.add(centerPanel, BorderLayout.CENTER);
		
		//Directory button and labels on the bottom
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(chooseDir);
		bottomPanel.add(directoryTypeList);
		//bottomPanel.add(timerLab);
		win.add(bottomPanel, BorderLayout.SOUTH);
		
		//make the GUI visible
		win.setVisible(true);
	}
}