import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	public static void main(String[] args) {
		
		ServerSocket listener;
		int clientid=0;
		try 
		{
			 listener = new ServerSocket(10000,10);
			 
			 while(true)
			 {
				System.out.println("Main thread listening for incoming new connections");
				Socket newconnection = listener.accept();
				
				System.out.println("New connection received and spanning a thread");
				Connecthandler t = new Connecthandler(newconnection, clientid);
				clientid++;
				t.start();
			 }
			
		} 
		
		catch (IOException e) 
		{
			System.out.println("Socket not opened");
			e.printStackTrace();
		}
	}

}

class Connecthandler extends Thread
{

	Socket individualconnection;
	int socketid;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	
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
	int result;
	
	public Connecthandler(Socket s, int i)
	{
		individualconnection = s;
		socketid = i;
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
	
	public void run()
	{
		try 
		{
			out = new ObjectOutputStream(individualconnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(individualconnection.getInputStream());
			System.out.println("Connection"+ socketid+" from IP address "+individualconnection.getInetAddress());
		
		 
			// Commence
			do 
			{
				do
				{
					sendMessage("Please enter 1 to Enter as Club or 2 to Enter as Agent");
					message = (String)in.readObject();
				}while(!message.equals("1")&&!message.equals("2"));
			
				// club
				if(message.equals("1"))
				{
					do
					{
						sendMessage("Please enter 1 to register or 2 to log in");
						message = (String)in.readObject();
					}while(!message.equals("1")&&!message.equals("2"));
					
					// register as club
					if(message.equals("1"))
					{
						clubRegister();
					}
					// sign in as club
					else if(message.equals("2"))
					{
						clubLogIn();
					}
					
				}
				// agent
				else if(message.equals("2"))
				{
					do
					{
						sendMessage("Please enter 1 to register or 2 to log in");
						message = (String)in.readObject();
					}while(!message.equals("1")&&!message.equals("2"));
					
					// register as agent
					if(message.equals("1"))
					{
						agentRegister();
					}
					// sign in as agent
					else if(message.equals("2"))
					{
						agentLogIn();
					}	
				}
			
				sendMessage("Y to go back to start or N to terminate program");
				message = (String)in.readObject();
			}while(message.equalsIgnoreCase("Y"));
		
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		finally
		{
			try 
			{
				out.close();
				in.close();
				individualconnection.close();
			}
			
	
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
	} // run()
	
	public void clubRegister() {
		try {
			// write new file clubRegister.txt
			File clubRegisterFile = new File("clubRegister.txt");
			FileWriter fileWriterClub = new FileWriter(clubRegisterFile.getAbsoluteFile(), true);
			
			// write new file clubLogIn.txt
			File clubLogInFile = new File("clubLogIn.txt");
			FileWriter fileWriterClub2 = new FileWriter(clubLogInFile, true);
			
			BufferedWriter bwReg = new BufferedWriter(fileWriterClub);
			BufferedWriter bwLog = new BufferedWriter(fileWriterClub2);
			
			BufferedReader br = new BufferedReader(new FileReader(clubRegisterFile));
			List<String> lines = new ArrayList<String>();
			String line;
			
			int i = 0;
			
			while((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
			
			sendMessage("Please enter your club name");
			clubName = (String)in.readObject();
			bwReg.write("\n" + clubName + " ");
			bwLog.write("\n" + clubName + " ");
			
			//Ensure club name is unique
			while(i < lines.size())
			{
				String details;
				details = lines.get(i);
				if(details.contains(clubName)) {
					sendMessage("Club name already taken");
					clubName=(String) in.readObject();
					i = 0;
					continue;
				}
				i++;
			}
			
			sendMessage("Please enter your club id");
			clubId = (String)in.readObject();
			bwReg.write(clubId + " ");
			bwLog.write(clubId + " ");
			
			// Ensure club ID is unique
			while(i < lines.size())
			{
				String details;
				details = lines.get(i);
				if(details.contains(clubId)) {
					sendMessage("Club ID already taken");
					clubId=(String) in.readObject();
					i = 0;
					continue;
				}
				i++;
			}
			
			sendMessage("Please enter your email");
			clubEmail = (String)in.readObject();
			bwReg.write(clubEmail + " ");
			
			sendMessage("Please enter the funds available for transfer");
			fundsAvail = (String)in.readObject();
			bwReg.write(fundsAvail + " ");
			
			// close files
			try{
				if(bwReg != null)
				{
					bwReg.close();
				}
				if(fileWriterClub != null)
				{
					fileWriterClub.close();
				}
				if(bwLog != null)
				{
					bwLog.close();
				}
				if(fileWriterClub2 != null)
				{
					fileWriterClub2.close();
				}
			}catch(IOException ioException)
			{
				ioException.printStackTrace();
			}
			
			}
			catch(IOException ioException)
			{
				ioException.printStackTrace();
			}
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} 
	} // clubRegister()
	
	public void clubLogIn() {
		try {
			// read file clubLogIn.txt
			File clubLogInFile = new File("clubLogIn.txt");
			BufferedReader br = new BufferedReader(new FileReader(clubLogInFile));
			List<String> lines = new ArrayList<String>();
			
			String line;
			int i = 0;
			String account[] = {};
			
			while((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
			
			sendMessage("Please enter your club name");
			clubName = (String)in.readObject();
			
			// if file contains clubName
			while(i<lines.size())
			{
				String details;
				details = lines.get(i);
				if(details.contains(clubName))
				{
					account = details.split(" ");
					i=0;
					break;
				}
				i++;
			}
			
			sendMessage("Please enter your club id");
			clubId = (String)in.readObject();
			
			// if clubId is correct for clubName
			while(!(clubId.equals(account[1])) )
			{
				sendMessage("ClubID Incorrect. Try Again.");
				clubId=(String)in.readObject();
				i++;
				
				if(i>5) {
					return;
				}
				
			}
			
			//sendMessage("Log In successful");
			
			// menu
			do {
				sendMessage("Enter:\n1 to view a player in a position\n"
						+ "5 to exit menu");
				choice = (String)in.readObject();
				
				switch(choice)
				{
				case "1":
					viewPlayerInPos();
					break;
				
				case "5":
					break;
					
				default:
					sendMessage("Invalid choice. Enter again");
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
			// write new file agentRegister.txt
			File agentRegisterFile = new File("agentRegister.txt");
			FileWriter fileWriterAgentRegister = new FileWriter(agentRegisterFile.getAbsoluteFile(), true);
			
			// write new file agentLogIn.txt
			File agentLogInFile = new File("agentLogIn.txt");
			FileWriter fileWriterAgentLogIn = new FileWriter(agentLogInFile, true);
			
			BufferedWriter bwReg = new BufferedWriter(fileWriterAgentRegister);
			BufferedWriter bwLog = new BufferedWriter(fileWriterAgentLogIn);
			
			BufferedReader br = new BufferedReader(new FileReader(agentRegisterFile));
			List<String> lines = new ArrayList<String>();
			String line;
			
			int i = 0;
			
			while((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
			
			sendMessage("Please enter your name");
			agentName = (String)in.readObject();
			bwReg.write("\n" + agentName + " ");
			bwLog.write("\n" + agentName + " ");
			
			//Ensure agent name is unique
			while(i < lines.size())
			{
				String details;
				details = lines.get(i);
				if(details.contains(agentName)) {
					sendMessage("Agent name already taken");
					agentName=(String) in.readObject();
					i = 0;
					continue;
				}
				i++;
			}
			
			sendMessage("Please enter your agent id");
			agentId = (String)in.readObject();
			bwReg.write(agentId + " ");
			bwLog.write(agentId + " ");
			
			//Ensure agent ID is unique
			while(i < lines.size())
			{
				String details;
				details = lines.get(i);
				if(details.contains(agentId)) {
					sendMessage("Agent id already taken");
					agentId=(String) in.readObject();
					i = 0;
					continue;
				}
				i++;
			}
			
			sendMessage("Please enter your email");
			agentEmail = (String)in.readObject();
			bwReg.write(agentEmail + " ");
			
			// close files
			try{
				if(bwReg != null)
				{
					bwReg.close();
				}
				if(fileWriterAgentRegister != null)
				{
					fileWriterAgentRegister.close();
				}
				if(bwLog != null)
				{
					bwLog.close();
				}
				if(fileWriterAgentLogIn != null)
				{
					fileWriterAgentLogIn.close();
				}
			}catch(IOException ioException)
			{
				ioException.printStackTrace();
			}
			
			}
			catch(IOException ioException)
			{
				ioException.printStackTrace();
			}
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} 
	} // agentRegister()
	
	public void agentLogIn() {
		try {
			// read file agentLogIn.txt
			File agentLogInFile = new File("agentLogIn.txt");
			BufferedReader br = new BufferedReader(new FileReader(agentLogInFile));
			List<String> lines = new ArrayList<String>();
			
			String line;
			int i = 0;
			String account[] = {};
			
			while((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
			
			sendMessage("Please enter your name");
			agentName = (String)in.readObject();
			
			// if agentName is in file
			while(i<lines.size())
			{
				String details;
				details = lines.get(i);
				if(details.contains(agentName))
				{
					account = details.split(" ");
					System.out.println("Account found");
					i=0;
					break;
				}
				i++;
			}
			
			sendMessage("Please enter your agent id");
			agentId = (String)in.readObject();
			
			// if agentId is correct for agentName
			while(!(agentId.equals(account[1])))
			{
				sendMessage("AgentID incorrect. Try Again");
				agentId=(String)in.readObject();
				i++;
				
				if(i>5) {
					return;
				}
			}
			
			// menu
			do {
				sendMessage("Log In successful");
				sendMessage("Enter:\n1 to add a player\n"
						+ "5 to exit menu");
				choice = (String)in.readObject();
				
				switch(choice)
				{
				case "1":
					addPlayer();
					break;
				
				case "5":
					break;
					
				default:
					sendMessage("Invalid choice. Enter again");
					break;
				}
			}while(!(choice.equals("5")));	
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // agentLogIn()
	
	public void addPlayer() {
		try {
			// create file playerDetails.txt
			File playerDetails = new File("playerDetails.txt");
			FileWriter fileWriterPlayer = new FileWriter(playerDetails, true);
			
			BufferedWriter bwPlayer = new BufferedWriter(fileWriterPlayer);
			
			sendMessage("Enter player name");
			playerName = (String)in.readObject();
			bwPlayer.write("\nName:  " + playerName + " ");
			
			sendMessage("Enter player age");
			playerAge = (String)in.readObject();
			bwPlayer.write("Age: " + playerAge + " ");
			
			sendMessage("Enter player id");
			playerId = (String)in.readObject();
			bwPlayer.write("ID: " + playerId + " ");
			
			sendMessage("Enter club id");
			clubId = (String)in.readObject();
			bwPlayer.write("Club ID: " + clubId + " ");
			
			sendMessage("Enter agent id");
			agentId = (String)in.readObject();
			bwPlayer.write("Agent ID: " + agentId + " ");
			
			sendMessage("Enter player valuation");
			playerValuation = (String)in.readObject();
			bwPlayer.write("Price: " + playerValuation + " ");
			
			sendMessage("Enter player position (Goalkeeper, Defender, Midfielder, Attacker)");
			playerPosition = (String)in.readObject();
			bwPlayer.write("Position: " + playerPosition + " ");	
			
			bwPlayer.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // addPlayer()
	
	public void viewPlayerInPos() {
		try {
			// read file playerDetails.txt
			File players = new File("playerDetails.txt");
			BufferedReader br = new BufferedReader(new FileReader(players));
			List<String> lines = new ArrayList<String>();
			String line;
			
			sendMessage("Enter position you would like to view");
			playerPosition = (String)in.readObject();
			
			// finds players in position entered by club
			while((line = br.readLine()) != null)
			{
				if(line.contains(playerPosition)) 
				{
					lines.add(line+"\n");
					sendMessage(line + " enter e to exit or any other key to continue");
					choice = (String)in.readObject();
					if(choice == "e") {
						break;
					}
				}
			}
			
			//br.close();
		} catch(EOFException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	} // viewPlayerInPos()
}
