import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;


class CobainLab2 {

    private static InputReader in;
    private static PrintWriter out;

    
    static ArrayDeque<String> penguin = new ArrayDeque<String>();
    static ArrayDeque<Integer> num = new ArrayDeque<Integer>();
    static HashMap<String, Integer> geng = new HashMap<String, Integer>();
    static int jumSebelumLayani = 0;
    
    // TODO
    static private int handleDatang(String Gi, int Xi) {
        num.add(Xi);
        penguin.add(Gi);
        jumSebelumLayani += Xi;
        return jumSebelumLayani;
    }

    // TODO
    static private String handleLayani(int Yi) {
        jumSebelumLayani -= Yi;
        while (Yi > 0){
            int tempNum = num.peek();
            if (Yi < num.peek()){
                String LayaniPenguin = penguin.peek();
                int LayaniNum = num.peek();

                num.offerFirst(LayaniNum - Yi);

                if (!geng.containsKey(LayaniPenguin)){
                    geng.put(LayaniPenguin, Yi);
                } else {
                    geng.put(LayaniPenguin, geng.get(LayaniPenguin) + Yi);
                }

            } else if(Yi >= num.peek()){
                String LayaniPenguin = penguin.peek();
                int LayaniNum = num.remove();

                penguin.remove();

                if (!geng.containsKey(LayaniPenguin)){
                    geng.put(LayaniPenguin, LayaniNum);
                } else {
                    geng.put(LayaniPenguin, geng.get(LayaniPenguin) + LayaniNum);
                }

            }
            Yi -= tempNum;
        }
        
        return penguin.peek();
    }

    // TODO
    static private int handleTotal(String Gi) {
        if (!geng.containsKey(Gi)){
            return 0;
        }
        return geng.get(Gi);
    }

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N;

        N = in.nextInt();

        for(int tmp=0;tmp<N;tmp++) {
            String event = in.next();

            if(event.equals("DATANG")) {
                String Gi = in.next();
                int Xi = in.nextInt();

                out.println(handleDatang(Gi, Xi));
            } else if(event.equals("LAYANI")) {
                int Yi = in.nextInt();
                
                out.println(handleLayani(Yi));
            } else {
                String Gi = in.next();

                out.println(handleTotal(Gi));
            }
        }

        out.flush();
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