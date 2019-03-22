import java.io.*;
import java.util.*;
import java.net.*;

public class TalkServer
{
	public static void main(String[] args)
	{
		int port = 2009;

		NetworkServer_1 nwServer = new NetworkServer_1(port);

		nwServer.listen();
	}
}

class NetworkServer_1
{
	protected int port;

  /** Build a server on specified port. It will continue to accept connections
      until an explicit exit command is sent.
  */
	public NetworkServer_1(int port)
	{
		this.port = port;
	}

  /** Monitor a port for connections. Each time one is established, pass resulting Socket to
      handleConnection.
  */
	public void listen()
	{

		try
		{
			 ServerSocket listener = new ServerSocket(port);
			 Socket server;
			 HashMap<String, String> phoneBook = new HashMap<String, String>(); //clients phone book

                         System.out.println(" TalkServer is up and running\n");
			 while(true)
			 {
				 server = listener.accept(); //accept connection request from a client
				 new ClientThread(server, phoneBook).start(); //starts the client thread
			 }

		}
		catch (IOException ioe){ System.out.println("IOException: " + ioe); }

	}
}

//Thread class
public class ClientThread extends Thread {
	Socket server;
	HashMap phoneBook;

	public ClientThread(Socket server, HashMap phoneBook) {
		this.server = server;
		this.phoneBook = phoneBook;
	}

	//Run method executes when thread is started
	public void run() {
		try {
			handleConnection(server, phoneBook);
		} catch(Exception e)
		{
			System.out.println(e);
		}
	}

	//method provides the behavior for the client on the server
	protected void handleConnection(Socket server, HashMap phoneBook) throws IOException
	{
		String s="";
		//to get input from the client
		BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));

		//Out to the client -- Enable auto-flush
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);


		System.out.println("got connection from " +
		server.getInetAddress().getHostName() + "\n");

		out.println(" TalkServer for CS-430 by Prof. Sattar" +
		"To close connection type, END");

		out.println("Type command below.");

		//HashMap<String, String> phoneBook = new HashMap<String, String>(); //clients phone book
		String name;
		String number;
		String[] splitResponse = new String[4];

		while (true)
		{
			// get input from the client
			if((s = in.readLine()).length() != 0)
			{
				if(s.equals("END"))
				{
					System.out.println("lost connection from client");
					break;
				}
				else if(s.split("").length > 4 && s.split("")[0].equals("S") && s.split("")[1].equals("T") && s.split("")[2].equals("O") //inserting a contact
								&& s.split("")[3].equals("R") && s.split("")[4].equals("E"))
				{
					splitResponse = s.split(" ", 4);

					if(splitResponse.length != 4)
					{
						out.println("SERVER> 400: The format is not correct, try again.");
					}
					else
					{
						name = splitResponse[1] + " " + splitResponse[2];

						if(phoneBook.containsKey(name))
						{
							out.println("SERVER> 400: This contact name is taken.");
						}
						else
						{
							number = splitResponse[3];
							phoneBook.put(name, number);
							out.println("SERVER> 100: The contact was inserted into your phone book.");
						}
					}
				}
				else if(s.split("").length > 2 && s.split("")[0].equals("G") && s.split("")[1].equals("E") && s.split("")[2].equals("T")) //retrieving a contact
				{
					splitResponse = s.split(" ", 4);

					if(splitResponse.length != 3) {
						out.println("SERVER> 400: The format is not correct, try again.");
					}
					else if(phoneBook.containsKey(splitResponse[1] + " " + splitResponse[2]) == true)
					{
						name = splitResponse[1] + " " + splitResponse[2];
						number = phoneBook.get(name).toString();
						out.println("SERVER> 200: The number is: " + number);
					}
					else
					{
						out.println("SERVER> 300: The contact was not found.");
					}
				}
				else if(s.split("").length > 5 && s.split("")[0].equals("R") && s.split("")[1].equals("E") && s.split("")[2].equals("M") //removing a contact
				&& s.split("")[3].equals("O") && s.split("")[4].equals("V") && s.split("")[5].equals("E"))
				{
					splitResponse = s.split(" ", 3);

					if(splitResponse.length != 3)
					{
						out.println("SERVER> 400: The format is not correct, try again.");
					}
					else
					{
						name = splitResponse[1] + " " + splitResponse[2];

						if(phoneBook.get(name) == null)
						{
							out.println("SERVER> 300: The contact was not found.");
						}
						else
						{
							name = splitResponse[1] + " " + splitResponse[2];
							phoneBook.remove(name);
							out.println("SERVER> 100: " + name + " has been removed from your contacts.");
						}
					}
				}
				else
				{
					out.println("SERVER> Not a valid response.");
				}
			}
			else
			{
				out.println("SERVER> Not a valid response.");
			}
		}
	}
}
