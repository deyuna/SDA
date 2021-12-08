import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;


class TP3CadanganBaru {

    private static InputReader in;
    private static PrintWriter out;
    private static ArrayList<Integer> rankingList = new ArrayList<>();
    private static LinkedList<Integer> perusahaanLinked[];
    private static int banyakKaryawan = 100000;   // No. of vertices
    private static ArrayList<Integer> rankingListDuplikat = new ArrayList<>();
    private static LinkedList<Integer> perusahaanLinkedDuplikat[];

    static class Karyawan {
        private int identitas;
        private int pangkat;
        private int maxRankingTeman;
        
        public Karyawan(int identitas, int pangkat){
            this.identitas = identitas;
            this.pangkat = pangkat;
            //this.maxRankingTeman = maxRankingTeman;
        }

        public int getIdentitas(){
            return this.identitas;
        } 

        public int getPangkat(){
            return this.pangkat;
        } 

        public int getMaxRankingTeman(){
            return this.maxRankingTeman;
        }  
     }

    // https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
    public static class Graph {
        //Constructor
        int banyakKaryawan;
        public Graph(int banyakKaryawan) {
            this.banyakKaryawan = banyakKaryawan;
            perusahaanLinked = new LinkedList[banyakKaryawan];
            for (int i=0; i<banyakKaryawan; i++){
                perusahaanLinked[i] = new LinkedList<>();
                perusahaanLinked[i].add(-1);
            }
                
        }

        void tambah(int karyawanU, int karyawanV){
            perusahaanLinked[karyawanU-1].add(karyawanV);
            perusahaanLinked[karyawanV-1].add(karyawanU);
            // out.println("---------tambah---------");
            // debug();
        }

        void resign(int kResign){
            kResign -= 1;
            for (int karyawan: perusahaanLinked[kResign]){
                perusahaanLinked[karyawan-1].remove((Integer)(kResign+1));

            }
            perusahaanLinked[kResign].clear();
            perusahaanLinked[kResign].add(-1);
            rankingList.set(kResign, -1);
            // out.println("---------resign---------");
            // debug();

        }
        
        void carry(int karyawan){
            // debug
            // for (int i = 0; i < rankingList.size(); i++){
            //     out.println("ranking list " + rankingList.get(i));
            // }

            karyawan-=1;
            if (perusahaanLinked[karyawan].size() == 0){
                out.println("0");
            }else {
                int tempRankingTinggi = rankingList.get(perusahaanLinked[karyawan].getFirst()-1);
                // out.println("get first : " + perusahaanLinked[karyawan].getFirst());
                // out.println("temp ranking : " + tempRankingTinggi);

               for (Integer teman : perusahaanLinked[karyawan]){
                    int tempRank = rankingList.get(teman-1);
                    if(tempRank>tempRankingTinggi){
                        tempRankingTinggi = tempRank;
                }
               }
               out.println(tempRankingTinggi);

               
            }
        }

        void resignSimulasi(int kResign){
            kResign -= 1;
            for (int karyawan: perusahaanLinkedDuplikat[kResign]){
                perusahaanLinkedDuplikat[karyawan-1].remove((Integer)(kResign+1));

            }
            perusahaanLinkedDuplikat[kResign].clear();
            perusahaanLinkedDuplikat[kResign].add(-1);
            rankingListDuplikat.set(kResign, -1);
            // out.println("---------resign---------");
            // debug();

        }

        void simulasi(){
            Graph graphDuplikat = this;

            perusahaanLinkedDuplikat = new LinkedList[banyakKaryawan];
            rankingListDuplikat = new ArrayList<Integer>();
            
            //int banyakKaryawanDuplikat = banyakKaryawan;
            int counterBanyakKaryawan = 0;
            int counterKaryawanRentan = 0;

            // duplikat linked list
            for (int i = 0; i < banyakKaryawan; i++){
                perusahaanLinkedDuplikat[i] = new LinkedList<Integer>(perusahaanLinked[i]);
            }

            // duplikat ranking list
            for (int i = 0; i < rankingList.size(); i++){
                rankingListDuplikat.add(rankingList.get(i));
            }
            

            for (int i = 1; i <= rankingListDuplikat.size(); i++){
                int counterTeman = 0; // counter teman yang pangkatnya lebih gede 

                if (rankingListDuplikat.indexOf(i) != -1){
                    counterBanyakKaryawan+=1;
                    //out.println("banyak karyawan " + i + " " + counterBanyakKaryawan);
                    //debug();
                    //out.println(rankingListDuplikat.get(i));

                    // index of karyawan
                    int indexKaryawanRentan = rankingListDuplikat.indexOf(i);

                    // ranking temp sekarang
                    int rankingKaryawan = i;

                    // for each networking yang rentang
                    for (int networkingRentan : perusahaanLinkedDuplikat[indexKaryawanRentan]){
                        int rankingNetworkingThis = rankingListDuplikat.get(networkingRentan-1);
                        if (rankingNetworkingThis >= rankingKaryawan){
                            counterTeman++;
                        }
                        //out.println("teman: " + counterTeman);


                        if (counterTeman == perusahaanLinkedDuplikat[indexKaryawanRentan].size()){
                            graphDuplikat.resignSimulasi(indexKaryawanRentan+1);
                            counterKaryawanRentan++;
                            //out.println("rentan: " +  counterKaryawanRentan);

                        }
                    }
                }

            }
            // out.println("rentan: " +  counterKaryawanRentan);
            out.println(counterBanyakKaryawan - counterKaryawanRentan);


        }

        // void boss(int karyawan){
        //     karyawan -=1;
        //     if (perusahaanLinked[karyawan].size() == 0){
        //         out.println("0");
        //     } else {
        //         // temp awal
        //         int tempRankingTinggi = rankingList.get(perusahaanLinked[karyawan].getFirst()-1);
        //         for (Integer teman : perusahaanLinked[karyawan]){
        //             int tempRankTeman = rankingList.get(teman-1);
        //             if(tempRankTeman > tempRankingTinggi){
        //                 tempRankingTinggi = tempRankTeman;
        //             }

        //             //out.println(rankingList.get(teman));
                    
        //         }
                
        //         out.println(tempRankingTinggi);
        //     }
        // }

        private Integer boss(int karyawanU){
            LinkedList<Integer> queue = new LinkedList<Integer>();
 
            // boolean array visited[] which stores the
            // information whether ith vertex is reached
            // at least once in the Breadth first search
            boolean visited[] = new boolean[banyakKaryawan+1];
    
            // initially all vertices are unvisited
            // so v[i] for all i is false
            // and as no path is yet constructed
            // dist[i] for all i set to infinity
            for (int i = 0; i < banyakKaryawan; i++) {
                visited[i] = false;
            }
    
            // now source is first to be visited and
            // distance from source to itself should be 0
            visited[karyawanU-1] = true;
            queue.add(karyawanU);
                
            int tempRankingTinggi = 0;

    
            // bfs Algorithm
            while (!queue.isEmpty()) {
                int u = queue.remove();
                for (int i = 0; i < perusahaanLinked[u-1].size(); i++) {
                   
                        if (visited[perusahaanLinked[u-1].get(i)-1] == false) {
                            visited[perusahaanLinked[u-1].get(i)-1] = true;
                           
                            // stopping condition (when we find
                            // our destination)
                            int tempRanking = rankingList.get(perusahaanLinked[u-1].get(i)-1);

                            if (tempRanking > tempRankingTinggi){
                                tempRankingTinggi = tempRanking;
                            }
                            queue.add(perusahaanLinked[u-1].get(i));
                        }
                    
                    
                }
            }
            return tempRankingTinggi;
        }

        private static void sebar(int karyawanU, int karyawanV, int v){
            int pred[] = new int[v+1];
            int dist[] = new int[v+1];

            if (karyawanU == karyawanV){
                //out.println("a");

                out.println("0");
                return;
            }
            
            for (Integer teman : perusahaanLinked[karyawanU-1]){
                //out.println("b");

                if (teman == karyawanV){
                    out.println("0");

                    return;
                }
            }

            if (perusahaanLinked[karyawanU-1].size() == 0){
                //out.println("c");

                out.println("-1");

                return;
            }
           

            else if (BFS(karyawanU, karyawanV, v, pred, dist) == false) {
                //out.println("d");

                out.println("-1");

                return;
            }
        
            
            LinkedList<Integer> path = new LinkedList<Integer>();
            int crawl = karyawanV;
            path.add(crawl);
            while(pred[crawl] != -1) {
                path.add(pred[crawl]);
                crawl = pred[crawl];
            }

            out.println(dist[karyawanV]-1);

        }
 
    // https://www.geeksforgeeks.org/shortest-path-unweighted-graph/
    // a modified version of BFS that stores predecessor
    // of each vertex in array pred
    // and its distance from source in array dist
    private static boolean BFS(int src, int dest, int v, int pred[], int dist[])
    {

        // adj = ranking list
        // src = karyawan u
        // dest = unknown
        // v banyak karyawan

        // a queue to maintain queue of vertices whose
        // adjacency list is to be scanned as per normal
        // BFS algorithm using LinkedList of Integer type
        LinkedList<Integer> queue = new LinkedList<Integer>();
 
        // boolean array visited[] which stores the
        // information whether ith vertex is reached
        // at least once in the Breadth first search
        boolean visited[] = new boolean[v+1];
 
        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }
 
        // now source is first to be visited and
        // distance from source to itself should be 0
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);
 
        // bfs Algorithm
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < perusahaanLinked[u-1].size(); i++) {
                if (visited[perusahaanLinked[u-1].get(i)] == false) {
                    visited[perusahaanLinked[u-1].get(i)] = true;
                    dist[perusahaanLinked[u-1].get(i)] = dist[u] + 1;
                    pred[perusahaanLinked[u-1].get(i)] = u;
                    queue.add(perusahaanLinked[u-1].get(i));
 
                    // stopping condition (when we find
                    // our destination)
                    if (perusahaanLinked[u-1].get(i) == dest)
                        return true;
                }
            }
        }
        return false;
    }

        void debug(){
            int counter = 1;
                for(LinkedList<Integer> p: perusahaanLinked){
                    if (p.contains(-1)){
                        counter++;
                        continue;
                    }
                    out.println("karyawan " + counter++ + ": " + p + ", ");
                }
                out.println();
        }
        

        void networking(){
            out.println("0");
        }
    }
   

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(); // banyak karyawan mula-mula
        int M = in.nextInt(); // banyak pertemanan mula-mula
        int Q = in.nextInt(); // banyak kejadian atau pertanyaan Kaguya

        Graph graphPerusahaan = new Graph(banyakKaryawan);

        // rank
        for (int i = 0; i < N; i++){
            int ranking = in.nextInt();
            rankingList.add(ranking);
            perusahaanLinked[i].clear();
        }     
        
        for (int i = 0; i < M; i++){
            int temanSatu = in.nextInt();
            int temanDua = in.nextInt();
            graphPerusahaan.tambah(temanSatu, temanDua);
        }

        while(Q-- > 0){
            int pertanyaan = in.nextInt();
            if (pertanyaan == 1){
                int karyawanU = in.nextInt();
                int karyawanV = in.nextInt();
                graphPerusahaan.tambah(karyawanU, karyawanV);
                //graphPerusahaan.tambah(karyawanU, karyawanV);

            }
            else if (pertanyaan == 2){
                int karyawanU = in.nextInt();
                graphPerusahaan.resign(karyawanU);
                //graphPerusahaan.resign(karyawanU);

            }
            else if (pertanyaan == 3){
                int karyawanU = in.nextInt();
                graphPerusahaan.carry(karyawanU);
            }
            else if (pertanyaan == 4){
                int networkU = in.nextInt();
                out.println(graphPerusahaan.boss(networkU));

            }
            else if (pertanyaan == 5){
                int karyawanU = in.nextInt();
                int karyawanV = in.nextInt();
                graphPerusahaan.sebar(karyawanU, karyawanV, N);

            }
            else if (pertanyaan == 6){
                graphPerusahaan.simulasi();
            }
            else if (pertanyaan == 7){
                graphPerusahaan.networking();
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

        public long nextLong() {
            return Long.parseLong(next());
        }

        public char nextChar() {
            return next().charAt(0);
        }
    }
}

