package test;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Client client1 = new Client(7777);
        Client client2 = new Client(7777);

        client1.setName("Vuong");

        client1.sent("fuck");
        client2.sent("duck");


        Thread thread1 = new Thread(client1);
        Thread thread2 = new Thread(client2);
        Arrays.asList(thread1, thread2).parallelStream().forEach(Thread::start);


    }
}