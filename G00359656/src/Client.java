import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//test
public class Client 
{
	
	private Socket connection;
	private String message;
	private Scanner console;
	private String ipaddress;
	private int portaddress;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	// club variables
	String clubName;
	String clubId;
	String clubEmail;
	String fundsAvail;
	
	// agent variables
	String agentName;
	String agentId;
	String agentEmail;
	
	// player variables
	String playerName;
	String playerAge;
	String playerId;
	String playerValuation;
	String playerStatus;
	String playerPosition;
	
	String choice;
	int i = 0;
	
	public Client()
	{
		console = new Scanner(System.in);
		
		System.out.println("Enter the IP Address of the server");
		ipaddress = console.nextLine();
		
		System.out.println("Enter the TCP Port");
		portaddress  = console.nextInt();
		
	}
	
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) 
	{
			Client temp = new Client();
			temp.clientapp();
	}

	public void clientapp()
	{
		
		try 
		{
			connection = new Socket(ipaddress,portaddress);
		
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			System.out.println("Client Side ready to communicate");
		
		
		    // Client App
			do
			{
				do
				{
					// enter as club or agent, repeat while not equal to 1 or 2
					message = (String)in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
				}while(!message.equals("club")&&!message.equals("2"));
				
				if(message.equals("1"))
				{
					do
					{
						// 1 to register or 2 to log in as club
						message = (String)in.readObject();
						System.out.println(message);
						message = console.next();
						sendMessage(message);
					}while(!message.equals("1")&&!message.equals("2"));
					
					if(message.equals("1"))
					{
						clubRegister();
					}
					
					else if(message.equals("2"))
					{
						clubLogIn();
					}
				}
			
				else if(message.equals("2"))
				{
					do
					{
						// 1 to register or 2 to log in as agent
						message = (String)in.readObject();
						System.out.println(message);
						message = console.next();
						sendMessage(message);
					}while(!message.equals("1")&&!message.equals("2"));
					
					if(message.equals("1"))
					{
						agentRegister();
					}
					
					else if(message.equals("2"))
					{
						agentLogIn();
					}
				}
				// enter y to repeat or n to terminate
				message = (String)in.readObject();
				System.out.println(message);
				message = console.next();
				sendMessage(message);
			}while(message.equalsIgnoreCase("Y"));
			
			out.close();
			in.close();
			connection.close();
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	} // clientApp()
	
	public void clubRegister() {
		try {
			// enter details of club to register
			message = (String)in.readObject();
			System.out.println(message);
			clubName = console.next();
			sendMessage(clubName);
			
			message = (String)in.readObject();
			System.out.println(message);
			clubId = console.next();
			sendMessage(clubId);
			
			message = (String)in.readObject();
			System.out.println(message);
			clubEmail = console.next();
			sendMessage(clubEmail);
			
			message = (String)in.readObject();
			System.out.println(message);
			fundsAvail = console.next();
			sendMessage(fundsAvail);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // clubRegister()
	
	public void clubLogIn() {
		try {
			// enter details to log in
			message = (String)in.readObject();
			System.out.println(message);
			clubName = console.next();
			sendMessage(clubName);
			
			message = (String)in.readObject();
			System.out.println(message);
			clubId = console.next();
			sendMessage(clubId);
			
			// menu
			do {
				message = (String)in.readObject();
				System.out.println(message);
				choice = console.next();
				sendMessage(choice);
				
				switch(choice)
				{
				case "1":
					viewPlayerInPos();
					break;
				
				case "5":
					break;
					
				default:
					message = (String)in.readObject();
					System.out.println(message);
					break;
				}
			}while(!(choice.equals("5")));
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // clubLogIn()
	
	public void agentRegister() {
		try {
			// enter details of agent to register
			message = (String)in.readObject();
			System.out.println(message);
			agentName = console.next();
			sendMessage(agentName);
			
			message = (String)in.readObject();
			System.out.println(message);
			agentId = console.next();
			sendMessage(agentId);
			
			message = (String)in.readObject();
			System.out.println(message);
			agentEmail = console.next();
			sendMessage(agentEmail);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // agentRegister()
	
	public void agentLogIn() {
		try {
			// enter details to log in
			message = (String)in.readObject();
			System.out.println(message);
			agentName = console.next();
			sendMessage(agentName);
			
			message = (String)in.readObject();
			System.out.println(message);
			agentId = console.next();
			sendMessage(agentId);
			
			// menu
			do {
				message = (String)in.readObject();
				System.out.println(message);
				message = (String)in.readObject();
				System.out.println(message);
				choice = console.next();
				sendMessage(choice);
				
				switch(choice)
				{
				case "1":
					addPlayer();
					break;
				
				case "5":
					break;
					
				default:
					message = (String)in.readObject();
					System.out.println(message);
					break;
				}
			}while(!(choice.equals("5")));
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // agentLogIn()
	
	// allows agent to add a player
	public void addPlayer() {
		try {
			message = (String)in.readObject();
			System.out.println(message);
			playerName = console.next();
			sendMessage(playerName);
			
			message = (String)in.readObject();
			System.out.println(message);
			playerAge = console.next();
			sendMessage(playerAge);
			
			message = (String)in.readObject();
			System.out.println(message);
			playerId = console.next();
			sendMessage(playerId);
			
			message = (String)in.readObject();
			System.out.println(message);
			clubId = console.next();
			sendMessage(clubId);
			
			message = (String)in.readObject();
			System.out.println(message);
			agentId = console.next();
			sendMessage(agentId);
			
			message = (String)in.readObject();
			System.out.println(message);
			playerValuation = console.next();
			sendMessage(playerValuation);
			
			message = (String)in.readObject();
			System.out.println(message);
			playerPosition = console.next();
			sendMessage(playerPosition);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // addPlayer()
	
	// shows players in position club selected
	public void viewPlayerInPos() {
		try {
			
			message = (String)in.readObject();
			System.out.println(message);
			playerPosition = console.next();
			sendMessage(playerPosition);
			
			for(int i = 0; i< 10; i++) {
				message = (String)in.readObject();
				System.out.println(message);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // viewPlayerInPos()
}
