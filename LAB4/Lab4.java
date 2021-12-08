import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO - class untuk Lantai
class Lantai {
    Lantai next;
    Lantai previous;
    String data;

    public Lantai(String data, Lantai previous, Lantai next){
        this.data = data; 
        this.next = next;
        this.previous = previous;

    }

    public Lantai(String data){
        this(data, null, null);
    }

    public String getValue(){
        return this.data;
    }

}


// TODO - class untuk Gedung
class Gedung {

    public Lantai head;
    public Lantai tail;
    public int size;
    public Lantai current;
    public int currentInt;

    public Gedung() {
        this.head = null;
        this.tail = null;
        this.current = null;
        this.currentInt = 0;
        this.size = 0;

    }

    public int size(){
        return size();
    } 

    public void bangun(String input){
        // TODO - handle BANGUN
        Lantai lantaiBaru = new Lantai(input);
        if (head == null && tail == null){
            head = lantaiBaru;
            tail = lantaiBaru;
            current = lantaiBaru;
            size++;
        } else {
        // IDE:
        // A null
        // lantaibaru previous = A 
        // lantai baru next = null 
        // pointer next = B
        // pointer = B
        // A B
            lantaiBaru.previous = current;
            lantaiBaru.next = current.next;
            current.next = lantaiBaru;
            current = lantaiBaru;
            size++;
            if (current.next == null){
                tail = current;
            } else {
                current.next.previous = current;
            }
        }
    }

    public String lift(String input){
        // TODO - handle LIFT
        if(size == 1){
            return current.getValue();
        }
        else {
            if (current != null){
                if(input.equals("ATAS")){
                    if (current.next != null){
                        current = current.next;
                    } 
                    else {
                        return current.getValue();
                    }
    
                } else {
                    if(current.previous != null){
                        current = current.previous;
                    }
                    else {
                        return current.getValue();
                    }
                }
            }
        }
        
        return current.getValue();
    }

    public String hancurkan(){
        // TODO - handle HANCURKAN
        Lantai temp = current;

        if (current == head && current == tail){
            current = null;
            head = null;
            tail = null;

        }

        else {

            // kalau mau hancurin di head
            if (head == current ){
                head = current.next;
                current.next.previous = null;
                current = head;

            }

            // kalau mau hancurin di tail
            else if (tail == current ){
                tail = current.previous;
                current.previous.next = null;
                current = tail;

            }

            // kalau mau hancurin di tengah
            else {
                current.previous.next = current.next;
                current.next.previous = current.previous;
                current = current.previous;
            }

        }

        size--;
        return temp.getValue();
    }

    public void timpa(Gedung input){
        // TODO - handle TIMPA
        if (this.tail != null && input.head != null){
            this.tail.next = input.head;
            input.head.previous = this.tail;
            this.size += input.size;
            if (input.tail != null){
                this.tail = input.tail;
            }
        }
        
    }

    public String sketsa(){
        // TODO - handle SKETSA
        StringBuilder sketsa = new StringBuilder();
        Lantai n = this.head;
        for (int i=0; i<size; i++) {
            sketsa.append(n.data);
            n = n.next;
        } 
        return sketsa.toString();
    }

}

public class Lab4 {
    private static InputReader in;
    public static PrintWriter out;
    public static Gedung Gedung;
    public static Map<String, Gedung> gedungMap = new HashMap<>(); // map string nama dengan object gedung
    
    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // N operations
        int N = in.nextInt();
        String cmd;

        // TODO - handle inputs
        for (int zz = 0; zz < N; zz++) {
            
            cmd = in.next();
            
            
            if(cmd.equals("FONDASI")){
                String A = in.next();
                Gedung gedungBaru = new Gedung();
                gedungMap.put(A, gedungBaru);
            }
            else if(cmd.equals("BANGUN")){
                String A = in.next();
                String X = in.next();
                // TODO
                gedungMap.get(A).bangun(X);
            }
            else if(cmd.equals("LIFT")){
                String A = in.next();
                String X = in.next();
                // TODO
                out.println(gedungMap.get(A).lift(X));

            }
            else if(cmd.equals("SKETSA")){
                String A = in.next();
                // TODO
                out.println(gedungMap.get(A).sketsa());
            }
            else if(cmd.equals("TIMPA")){
                String A = in.next();
                String B = in.next();
                // TODO
                gedungMap.get(A).timpa(gedungMap.get(B));
            }
            else if(cmd.equals("HANCURKAN")){
                String A = in.next();
                // TODO
                out.println(gedungMap.get(A).hancurkan());
                

            }
        }
     
        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;
 
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }
 
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
 
        public int nextInt() {
            return Integer.parseInt(next());
        }
 
    }
}