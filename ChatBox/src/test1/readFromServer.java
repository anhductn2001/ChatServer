package test1;

import java.util.*;
import javax.swing.*;
public class readFromServer extends Thread{
	client c;
	readFromServer(client cc)
	{
		c = cc;
	}
	public void run(){
		String s;
		while (true)
		{
			if (client.logout)
			{
				return;
			}
			s = client.read();
			if (s.startsWith("List"))
			{
				client.mainText.setText("Connected as " + client.nick);
				c.setTitle("Simple Java Chat - " + c.nick + " -Connected to " + c.server);
				client.connected = true;
				client.list.clear();
				String nextNick = "";
				StringTokenizer st = new StringTokenizer(s.substring(5,s.length()),", ");
				String temp = null;
				while(st.hasMoreTokens())
				{
					temp = st.nextToken();
					client.list.addElement(replace(temp,";",""));
				}
				System.out.print("List updated: New names: ");
				for (int i = 0; i < client.list.size();i++)
				{
					System.out.print(client.list.get(i) + " ");
				}
				System.out.println();
			}
			else if (s.startsWith("Recieve"))
			{
				client.mainText.setText(client.mainText.getText() + "\n" + s.substring(8,s.length()));
				client.mainText.setCaretPosition(client.mainText.getText().length());
			}
			else if (s.startsWith("PrivateRecieve"))
			{
				client.mainText.setText(client.mainText.getText() + "\n" + "Private Messages: " + s.substring(14,s.length()));
				client.mainText.setCaretPosition(client.mainText.getText().length());
			}
			else if (s.startsWith("NewNick"))
			{
				client.mainText.setText("");
				String newnick = JOptionPane.showInputDialog(null,"New nick:");
				client.connected = false;
				client.jMenuItem1.setEnabled(true);
				client.jMenuItem2.setEnabled(false);
				if (newnick != null)
				{
					client.nick = newnick;
					client.jMenuItem1.setEnabled(false);
					client.jMenuItem2.setEnabled(true);
					client.send("Login: "+newnick);
				}
			}
		System.out.println(s);
		}
	}
	static String replace(String str, String pattern, String replace)
	{
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