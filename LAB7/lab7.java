import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;


public class lab7 {
    private static InputReader in;
    private static PrintWriter out;
    final static int INF = 99999;
    private static int[][] dist;
    private static int[][] graph;

    public static int jumlahKota;
    
    // TODO: method to initialize graph
    public static void createGraph(int N) {
        jumlahKota = N;
        graph = new int[N][N];
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                graph[i][j] = INF;
            }
        }
    }

    // TODO: method to create an edge with type==T that connects vertex U and vertex V in a graph
    public static void addEdge(int U, int V, int T) {
        graph[U-1][V-1] = T;
        graph[V-1][U-1] = T;
    }

    public static void floydWarshall(int N)
    {

        dist = new int[jumlahKota][jumlahKota];
        int i, j, k;
      
        /* Initialize the solution matrix
           same as input graph matrix.
           Or we can say the initial values
           of shortest distances
           are based on shortest paths
           considering no intermediate
           vertex. */
        for (i = 0; i < N; i++)
            for (j = i; j < N; j++){
                dist[i][j] = graph[i][j];
                dist[j][i] = dist[i][j];
                //graph[i][j] = dist[i][j];
            }
 
        /* Add all vertices one by one
           to the set of intermediate
           vertices.
          ---> Before start of an iteration,
               we have shortest
               distances between all pairs
               of vertices such that
               the shortest distances consider
               only the vertices in
               set {0, 1, 2, .. k-1} as
               intermediate vertices.
          ----> After the end of an iteration,
                vertex no. k is added
                to the set of intermediate
                vertices and the set
                becomes {0, 1, 2, .. k} */
        for (k = 0; k < N; k++)
        {
            // Pick all vertices as source one by one
            for (i = 0; i < N; i++)
            {
                // Pick all vertices as destination for the
                // above picked source
                for (j = i; j < N; j++)
                {

                    if(dist[i][k] == INF || dist[k][j] == INF){
                        continue;
                    }
                    // If vertex k is on the shortest path from
                    // i to j, then update the value of dist[i][j]
                    if (dist[i][k] + dist[k][j] < dist[i][j]){
                        dist[i][j] = dist[i][k] + dist[k][j];
                        dist[j][i] = dist[i][k] + dist[k][j];

                    }
                }
            }
        }
 
    }

    // TODO: Handle teman X Y K
    public static int canMudik(int X, int Y, int K) {
        if(dist[X-1][Y-1] <= K ){
            return 1;
        }
        return 0;
    } 

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int M = in.nextInt();
        int Q = in.nextInt();
        createGraph(N);
        
        for (int i=0;i < M;i++) {
            int U = in.nextInt();
            int V = in.nextInt();
            int T = in.nextInt();
            addEdge(U, V, T);
        }

        floydWarshall(N);

        while(Q-- > 0) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int K = in.nextInt();
            out.println(canMudik(X, Y, K));
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
//source : https://www.geeksforgeeks.org/floyd-warshall-algorithm-dp-16/
// ide referensi : Muhammad Athallah 