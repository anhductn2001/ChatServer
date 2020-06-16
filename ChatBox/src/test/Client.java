package test;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.Date;

public class Client implements Runnable {
    public final String host = "localhost";
    public Socket socketClient;
    public BufferedReader is;
    public BufferedWriter os;
    int port;
    private String name  ;

    public Client(int port){
        try {
            name = "noname";
            try {
                socketClient = new Socket(host, port);

            }
            catch (UnknownHostException e){
                e.printStackTrace();
            }
            is = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
            os.newLine();
            os.write("Client " + name + " created");
            os.flush();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            // Ghi dữ liệu vào luồng đầu ra của Socket tại Client.
            os.newLine(); // kết thúc dòng
            os.flush();

            // Đọc dữ liệu trả lời từ phía server
            // Bằng cách đọc luồng đầu vào của Socket tại Client.
            String responseLine;
            while ((responseLine = is.readLine()) != null) {
                System.out.println(responseLine);
                if (responseLine.contains("OK")) {
                    break;
                }
            }

            os.close();
            is.close();
            socketClient.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    public void sent(String mess){
        try {

            os.newLine();
            os.write("Client "  + name + " : " +mess);
            os.flush();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    /*public String getMessenge(){
        return is.readLine();
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

