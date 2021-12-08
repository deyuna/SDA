import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;


class Lab3{

    private static InputReader in;
    private static PrintWriter out;
    private static long returnBanyakGalian = 1005;

    static private long findMaxBerlian(ArrayList<Integer> S, ArrayList<Integer> M, ArrayList<Integer> B) {
        long [][][] table = new long [S.size()+1][S.size()+1][3];
        long returnMax = 0;
        
        /* Keterangan:
        i = hari,
        j = berapa kali,
        k = siang(0) malam(1) bolos(2) */

        for (int i = 1; i < S.size()+1; i++){
            for(int j = 1; j <= i ; j++){
                if (j==1){
                    table[i][j][0] = max(table[i-1][j-1][1], table[i-1][j-1][2])+S.get(i-1)+B.get(j-1);
                    table[i][j][1] = max(table[i-1][j-1][0], table[i-1][j-1][2])+M.get(i-1)+B.get(j-1);
                    table[i][j][2] = max(max(table[i-1][j][0], table[i-1][j][1]), table[i-1][j][2]);

                }
                else if (j <= i){
                    table[i][j][0] = max(table[i-1][j-1][1],table[i-1][j-1][2])+S.get(i-1)+B.get(j-1)-B.get(j-2);
                    table[i][j][1] = max(table[i-1][j-1][0],table[i-1][j-1][2])+M.get(i-1)+B.get(j-1)-B.get(j-2);
                    table[i][j][2] = max(max(table[i-1][j][0], table[i-1][j][1]), table[i-1][j][2]);
                }
                if (returnMax < max(max(table[i][j][0],table[i][j][1]),table[i][j][2])){
                        returnMax = max(max(table[i][j][0],table[i][j][1]),table[i][j][2]);
                        returnBanyakGalian = j;
                }
            }
        }
        return returnMax;
    }

    static private long findBanyakGalian(ArrayList<Integer> S, ArrayList<Integer> M, ArrayList<Integer> B) {
        return returnBanyakGalian;
    }

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        ArrayList<Integer> S = new ArrayList<>();
        ArrayList<Integer> M = new ArrayList<>();
        ArrayList<Integer> B = new ArrayList<>();
        
        int N = in.nextInt();
        
        for(int i=0;i<N;i++) {
            int tmp = in.nextInt();
            S.add(tmp);
        }

        for(int i=0;i<N;i++) {
            int tmp = in.nextInt();
            M.add(tmp);
        }

        for(int i=0;i<N;i++) {
            int tmp = in.nextInt();
            B.add(tmp);
        }

        long jawabanBerlian = findMaxBerlian(S,M,B);
        long jawabanGalian = findBanyakGalian(S,M,B);

        out.print(jawabanBerlian + " " + jawabanGalian);

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

// IDE = Bornyto Hamonangan