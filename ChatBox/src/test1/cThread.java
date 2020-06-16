package test1;
import java.net.*;
import java.io.*;
import java.util.*;
public class cThread extends Thread{
	String nick;
	Boolean connected;
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	Socket clientSocket;
	cThread(Socket s){
		super("cThread");
		connected = false;
		nick = "";
		clientSocket = s;
		try{
			out = new PrintWriter(clientSocket.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	public boolean equals(cThread c){
		return (c.nick.equals(this.nick));
	}
	synchronized void send(String msg){
		out.println(msg);
	}
	void listen(){
		try{
			while (true){
				String msg = in.readLine();
				System.out.println(msg);
				if (msg.startsWith("Login"))
				{
					login(msg);
				}
				else if (msg.equals("Logout"))
				{
					if (connected){
						connected = false;
						int k = Server.clients.indexOf(this);
						Server.clients.remove(this);
						sendList();
						out.println("OK");
						out.close();
						in.close();
						clientSocket.close();
						return;
					}
					else
					{
						send("Not Logged in !!");
					}
				}
				else if (msg.startsWith("Post "))
				{
					for (int i = 0; i < Server.clients.size() ; i ++)
					{
						cThread t = (cThread)Server.clients.get(i);
						if (t.connected)
						{
							t.send("Recieve "+ nick+": " +msg.substring(5, msg.length()));
						}
					}
				}
				else if (msg.startsWith("PrivatePost "))
				{
					StringTokenizer st = new StringTokenizer(msg.substring(12,msg.length()),", ");
					String message = st.nextToken();
					String to = st.nextToken();
					boolean success = false;
					for (int i = 0; i < Server.clients.size() ; i ++){
						cThread t = (cThread)Server.clients.get(i);
						if (t.nick.equals(to))
						{
							t.send("PrivateRecieve "+ nick+": " + message);
							success = true;
							break;
						}
					}
					if (!success)
					{
						send("Error!");
					}
				}
				else
				{
					send(msg);
				}
			}
		}
		catch (SocketException e)
		{
			if (connected)
			{
				try
				{
					connected = false;
					int k = Server.clients.indexOf(this);
					Server.clients.remove(this);
					sendList();
					out.close();
					in.close();
					clientSocket.close();
					return;
				}
				catch (Exception d)
				{
					return;
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	public void run()
	{
		listen();
	}
	boolean login(String msg)
	{
		if (connected)
		{
			out.println("Already Connected!");
			return true;
		}
		boolean exists = false;
		System.out.println("Login" + msg.substring(5, msg.length()));
		for (int i = 0;i<Server.clients.size();i++)
		{
			if (Server.clients.get(i) != null)
			{
				System.out.println(msg.substring(7, msg.length()));
				cThread temp = (cThread)Server.clients.get(i);
				if ((temp.nick).equals(msg.substring(7, msg.length())))
				{
					exists = true;
					break;
				}
			}
		}
		if (exists)
		{
				out.println("New Nick");
		}
		else
		{
			connected = true;
			nick = msg.substring(7,msg.length());
			sendList();
		}
		return true;
	}
	void sendList()
	{
		String list = "";
		System.out.println(Server.clients.size());
		if (Server.clients.size() == 0)
		{
			return;
		}
		for (int i = 0;i<Server.clients.size();i++)
		{
			cThread temp = (cThread)Server.clients.get(i);
			if (Server.clients.get(i) != null)
			{
				if (connected)
				{
					list =temp.nick + "," + list ;
				}
			}
		}
		list = "List " +list.substring(0,list.length() -1) +";";
		for (int i = 0; i < Server.clients.size() ; i ++)
		{
			cThread t = (cThread)Server.clients.get(i);
			if (t.connected)
			{
				t.send(list);
			}
		}
	}
	public static String replace(String str, String pattern, String replace){
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		while ((e = str.indexOf(pattern, s)) >= 0)
		{
			result.append(str.substring(s, e));
			result.append(replace);
			s = e+pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}
}
