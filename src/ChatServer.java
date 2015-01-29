import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	
	ArrayList clientOutputList;

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.go();
	}
	
	public void go() {
		clientOutputList = new ArrayList();
		
		try {
			int i = 0;
			ServerSocket serverSock = new ServerSocket(5000);
			
			while(true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputList.add(writer);
				
				Thread clientThread = new Thread(new ClientHandler(clientSocket));
				clientThread.start();
				
			}
		}catch(Exception ex) {
			ex.printStackTrace();	
		}
	}
	
	public void tellEveryone(String message) {
		Iterator it = clientOutputList.iterator();
		
		while(it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSock) {
			try {
				sock = clientSock;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
			
		}

		public void run() {
			String message;
			try {
				while((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					tellEveryone(message);
				}
			}catch(IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}

}
