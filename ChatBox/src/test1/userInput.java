package test1;

import java.io.*;
public class userInput extends Thread{
	public void run()
	{
		BufferedReader kin = new BufferedReader(new
				InputStreamReader(System.in));
		while(true)
		{
			if (client.logout)
			{
				return;
			}
			try
			{
				String command = kin.readLine();
				if (command.equals("Logout"))
				{
					client.send(command);
					String response = client.read();
					client.logout = true;
					return;
				}
				else
				{
					client.send(command);
				}
			}
			catch (Exception e) {}
			
		}
	}
}
