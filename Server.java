import ca.pfv.spmf.algorithms.frequentpatterns.pascal.AlgoPASCAL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static void main(String[] args) {
        System.out.println("Server started ...");
        int port = 2000;

        ServerSocket ss;
        try {
            ss = new ServerSocket(port);


            while (true) {
                Socket s = ss.accept();

                ServerThread t = new ServerThread(s);
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MyPair {
    public int key;
    public String value;
    public int value2;

    public MyPair(int key, String value, int value2) {
        this.key = key;
        this.value = value;
        this.value2 = value2;
    }
}

class ServerThread extends Thread {
    Socket s;
    BufferedReader in;
    PrintStream out;

    public ServerThread(Socket s) {
        this.s = s;
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintStream(s.getOutputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String w = "";
        double minsup = 0.4;
        try {
            String file = in.readLine();
            File fileinput = new File(file);
            BufferedReader br = new BufferedReader(new FileReader(fileinput));

            int position = 0;
            Vector mySet = new Vector();
            Vector<MyPair> helperSet = new Vector<MyPair>();
            Vector<TreeSet<Integer>> allSets = new Vector();


            while (w != null) {
                w = br.readLine();
                if (w == null) break;
                StringBuilder sb = new StringBuilder();
                StringBuilder what = new StringBuilder();
                StringBuilder with = new StringBuilder();
                String text = "The user with id '";
                int pos = w.indexOf(text) + text.length();
                if (pos - text.length() != -1) {
                    while (w.charAt(pos) != '\'') {
                        sb.append(w.charAt(pos++));
                    }

                    pos += 2;
                    while (w.charAt(pos) != ' ') {
                        what.append(w.charAt(pos++));
                    }


                    String text2 = " id '";
                    pos = w.lastIndexOf(text2) + text2.length();
                    while (w.charAt(pos) != '\'') {
                        with.append(w.charAt(pos++));
                    }
                } else {
                    continue;
                }

                int key = Integer.parseInt(sb.toString());
                int value = Integer.parseInt(with.toString());


                if (!mySet.contains(key)) {
                    mySet.add(position, key);
                    position++;
                    allSets.add(position - 1, new TreeSet<Integer>());
                    allSets.get(position - 1).add(value);
                } else {
                    int ind = mySet.indexOf(key);
                    allSets.get(ind).add(value);
                }
                helperSet.add(new MyPair(key, what.toString(), Integer.parseInt(with.toString())));
            }
            PrintStream out = new PrintStream(new FileOutputStream("input_file.txt"));


            for (int i = 0; i < allSets.size(); i++) {
                Iterator<Integer> iterator = allSets.elementAt(i).iterator();
                while (iterator.hasNext()) {
                    out.print(iterator.next() + " ");
                }
                out.println();
            }


            AlgoPASCAL a = new AlgoPASCAL();
            a.runAlgorithm(minsup, "input_file.txt", "result_file.txt");

            File fileresinput = new File("result_file.txt");
            BufferedReader br2 = new BufferedReader(new FileReader(fileresinput));

            PrintStream resout = new PrintStream(new FileOutputStream("result.txt"));

            String st = "";
            String sup = "#SUP: ";
            while(st != null)
            {
                st = br2.readLine();
                if(st == null) break;
                int ind2 = st.indexOf(sup);
                String tmp = st.substring(0, ind2);
                String tmp2 = st.substring(ind2 + 6, ind2 + 9);
                int tmp3 = Integer.parseInt(tmp2);
                for(int i = 0; i < helperSet.size(); ++i) {
                    if (tmp.contains(String.valueOf(helperSet.get(i).key))) {
                        if(helperSet.get(i).value.equals("viewed"))
                        {
                            resout.println("Courses with id " + helperSet.get(i).value2 + " were viewed " + tmp3 + " times.");
                            break;
                        }

                    }
                }
            }



        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }
}
