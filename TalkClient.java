import java.io.*;
import java.util.*;
import java.net.*;

public class TalkClient
{
	public static void main(String[] args)
	{
    String host = "localhost";//IP address is 127.0.0.1
		int port = 2009;

		NetworkClientII nwClient = new NetworkClientII(host, port);

		nwClient.connect();
	}
}

class NetworkClientII
{
	protected String host;
	protected int port;
  /** Build a server on specified port. It will continue to accept connections
      until an explicit exit command is sent.
  */
	public NetworkClientII(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

  /** Establishes the connection, then passes the socket to
      handleConnection.
  */
	public void connect()
	{

		try
		{
			InetAddress addr = InetAddress.getByName(host);

			 Socket client = new Socket(addr, port);

			 handleConnection(client);

		}
		catch (UnknownHostException uhe){ System.out.println("Unknown host: " + uhe); }
		catch (IOException ioe){ System.out.println("IOException: " + ioe); }
	}
  /**
      This is the method that provides the behavior to the server
  */

    protected void handleConnection(Socket client) throws IOException
   {
          String s="";
          // to read from the keyboard
          BufferedReader reader = new BufferedReader( new InputStreamReader(System.in ));

	  //to get input from the server
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

	  //Out to the server -- Enable auto-flush
	  PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

	  System.out.println("Network ClientII:\n" + "made connection to " +
		                  host + " and got " + in.readLine() + "\n\n" +
											"This is your phone book." + "\n" +
											"To insert a contact, type: STORE 'first name' 'last name' 'number'" + "\n" +
											"To view a contact's number, type: GET 'first name' 'last name'" + "\n" +
											"To remove a contact, type: REMOVE 'first name' 'last name'" + "\n");
          System.out.println( in.readLine());

	  while (true)
	  {
		  if((s = reader.readLine()) != null)  // get input from the KBD
		  {
                          s = s.trim(); //get rid of leading and trailing white spaces
			  if (s.equalsIgnoreCase("END"))
                          {
                           //sen END message to server to close down.
                            out.println("END");
                            //respond to KBD.
                            System.out.println("OK:-> Bye! " );
                            //done
                           break;
                          }


                    //send the KBD input to the server
                    out.println(s);
                    //get response from the server and print it out
                    System.out.println(in.readLine());

		  }


	  }
	  client.close();
	}
}
