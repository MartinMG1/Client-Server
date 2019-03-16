import java.io.*;
import java.net.*;
import java.util.*;


public class Client {

    public static boolean term = false;

    public static void main(String[] args) {
        System.out.println("Client started ...");

        String host = "52.16.120.232";
        int port = 2000;

        try {
            Socket s = new Socket(host, port);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));

            PrintStream out= new PrintStream(s.getOutputStream());

            BufferedReader cons = new BufferedReader(
                    new InputStreamReader(System.in));

            ClientThread ct = new ClientThread(in);

            Scanner sc = new Scanner(System.in);

            term = false;

            String file = "";
            System.out.print("file: ");
            file = cons.readLine();
            out.println(file);


        } catch (InputMismatchException e) {
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

class ClientThread extends Thread {
    BufferedReader in;
    public boolean term = false;

    public ClientThread(BufferedReader in) {
        this.in = in;
        this.start();
    }

    public void run() {

        String w = "";
        do {
            try {
                w = in.readLine();
            } catch (IOException e) {
            }
        } while (!w.equals("end"));
        this.term = true;
    }

}
