package clientUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;


public class ui extends JFrame implements ActionListener{
	private Logger log = Logger.getLogger(getClass());
	
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JPanel jp3 = new JPanel();
	JPanel jp4 = new JPanel();
	JPanel jp5 = new JPanel();
	
	JButton jbtn1 = new JButton("접속");
	JButton jbtn2 = new JButton("Get전송");
	JButton jbtn3 = new JButton("Put전송");
	
	JLabel labelID = new JLabel("ID");
	JLabel labelGet= new JLabel("Get");
	JLabel labelPut= new JLabel("Put");
	
	TextArea textA = new TextArea();
	TextField textFID = new TextField();
	TextField textFGet = new TextField();
	TextField textFPut = new TextField();
	
	int portNum = 1234;
	String ip = "127.0.0.1";
	Socket soc;
	DataOutputStream dos;
	DataInputStream dis;
	
	Scanner scan = new Scanner(System.in);
	String sendMsg;
	String receiveMsg;
	
	String type;
	String key;
	String value;
	
	clientReceiver cr;
	Thread tr;
	
	boolean isCan=false;
	
	public ui() {
		// TODO Auto-generated constructor stub
		
		jp1.setLayout(new BorderLayout());
		jp1.add(labelID,BorderLayout.WEST);
		jp1.add(textFID,BorderLayout.CENTER);
		jp1.add(jbtn1,BorderLayout.EAST);
		
		jp2.setLayout(new BorderLayout());
		jp2.add(labelGet,BorderLayout.WEST);
		jp2.add(textFGet,BorderLayout.CENTER);
		jp2.add(jbtn2,BorderLayout.EAST);
		
		jp3.setLayout(new BorderLayout());
		jp3.add(labelPut,BorderLayout.WEST);
		jp3.add(textFPut,BorderLayout.CENTER);
		jp3.add(jbtn3,BorderLayout.EAST);
		
		jp4.setLayout(new GridLayout(3,1));
		jp4.add(jp1);
		jp4.add(jp2);
		jp4.add(jp3);
		
		jp5.setLayout(new BorderLayout());
		jp5.add(jp4, BorderLayout.NORTH);
		jp5.add(textA,BorderLayout.CENTER);
		
		add(jp5);
		
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
		
		setTitle("temp client");
		setSize(300, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		
	}
	public static void main(String[] args) {
		new ui().connectServer();
	}
	
	public void connectServer(){
		
		try {
			soc = new Socket(ip,portNum);
			dos = new DataOutputStream(new BufferedOutputStream(soc.getOutputStream()));
			dis = new DataInputStream(new BufferedInputStream(soc.getInputStream()));
			
			cr = new clientReceiver(dis,this);
			tr = new Thread(cr);
			tr.start();

			while(true){
				if(isCan){
					System.out.println("command :");
					type = scan.nextLine();
					if(type.equals("end")){
						break;
					}else if(type.equals("getA")){
						sendMsg = makeJSONPacket(type);
					}else{
						System.out.println("key :");
						key = scan.nextLine();
						if(type.equals("put")){
							System.out.println("value :");
							value = scan.nextLine();
							sendMsg = makeJSONPacket(type, key, value);
						}else{
							sendMsg = makeJSONPacket(type, key);
						}
					}
				
					
					dos.writeUTF(sendMsg);
					dos.flush();
					System.out.println("======전송======");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			endConnect();
		}
		log.info("연결을 종료 하였습니다.");	
	}
	public String makeJSONPacket(String type){
		JSONObject packetJSON = new JSONObject();
		packetJSON.put("TYPE", type);
	
		return packetJSON.toJSONString();
	}
	public String makeJSONPacket(String type,String key){
		JSONObject packetJSON = new JSONObject();
		packetJSON.put("TYPE", type);
		packetJSON.put("KEY", key);
	
		return packetJSON.toJSONString();
	}
	public String makeJSONPacket(String type,String key,String value){
		JSONObject packetJSON = new JSONObject();
		packetJSON.put("TYPE", type);
		packetJSON.put("KEY", key);
		packetJSON.put("VALUE", value);
	
		return packetJSON.toJSONString();
	}
	public void endConnect(){
	try {
		dos.close();
		dis.close();
		soc.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("접속")){
			
		}else if(e.getActionCommand().equals("Get전송")){
			//.getText();
		}
	}

}
