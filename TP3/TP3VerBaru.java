import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;


class TP3VerBaru {

    private static InputReader in;
    private static PrintWriter out;
    public static int identitas;
    public static int pangkat;
    private static ArrayList<Karyawan>[] perusahaanArray = null; // tadinya linkedlist
    private static ArrayList<Karyawan> objekKaryawan = null;
    private static int banyakKaryawan = 0;   // No. of vertices
    private static ArrayList<Karyawan>[] perusahaanArrayDuplikat = null; // tadinya linkedlist
    private static ArrayList<Karyawan> objekKaryawanDuplikat = null;
    private static Karyawan karyawanMinSatu = new Karyawan(identitas-1, pangkat-1); //pengurangan karyawan
    //public static Karyawan[] dataKaryawan = new Karyawan[100000];

    static class Karyawan {
        public int identitas;
        public int pangkat;
        private boolean flagRentan;
        
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
            perusahaanArray = new ArrayList[banyakKaryawan+1];
            for (int i=0; i<banyakKaryawan; i++){
                perusahaanArray[i] = new ArrayList<>();
                perusahaanArray[i].add(karyawanMinSatu);
            }
                
        }

        int findMax(int karyawan){
            if (perusahaanArray[karyawan-1].size() == 0){
                return 0;
            }
            return perusahaanArray[karyawan-1].get(perusahaanArray[karyawan-1].size()-1).getPangkat();
        }

        int findMin(int karyawan){
            if (perusahaanArray[karyawan-1].size() == 0){
                return 0;
            }
            return perusahaanArray[karyawan-1].get(0).getPangkat();
        }

        void tambah(int karyawanU, int karyawanV){
           
              

            tambahBinarySearch(perusahaanArray[karyawanU-1], karyawanV);
            tambahBinarySearch(perusahaanArray[karyawanV-1], karyawanU);

            // out.println("---------tambah---------");
            // debug();
        }

        //https://www.geeksforgeeks.org/java-program-to-perform-binary-search-on-arraylist/
        int tambahBinarySearch(ArrayList<Integer> arr, int x) 
        { 
            int left = 0, right = arr.size() - 1; 
            
            while (left <= right)
            { 
                int mid = left + (right - left) / 2; 
        
                // Check if x is present at mid 
                if (arr.get(mid) == x) {
                    return mid; 
                    break;
                }
        
                // If x greater, ignore left half 
                if (arr.get(mid) < x) 
                    left = mid + 1; 
        
                // If x is smaller, ignore right half 
                else
                    right = mid - 1; 
            } 
        
            if(left >= mid){
                arr.add(left, x);
            } else if (right == -1){
                arr.add(0,x);
            } else if (right <= mid){
                arr.add(right, x);
            }
        } 

        void resign(int kResign){
            kResign -= 1;
            Karyawan idxDihapus = objekKaryawan.get(kResign);
            ArrayList<Karyawan> listKryU = perusahaanArray[u];
            for (int i =0 ; i < listKaryawan.size();i++){
                ArrayList<Karyawan> listKrySebelah = perusahaanArray[listKryU.get(i).getIdentitas()-1];
                int idx = resignBinarySearch(listKrySebelah, idxDihapus);
                listKrySebelah.remove(idx);

            }

        }

        int resignBinarySearch(ArrayList<Integer> arr, Karyawan x) 
        { 
            int left = 0, right = arr.size() - 1; 
            
            while (left <= right)
            { 
                int mid = left + (right - left) / 2; 
        
                // Check if x is present at mid 
                if (arr.get(mid) == x.getPangkat()) 
                    return mid; 
        
                // If x greater, ignore left half 
                if (arr.get(mid) < x.getPangkat()) 
                    left = mid + 1; 
        
                // If x is smaller, ignore right half 
                else
                    right = mid - 1; 
            } 
        
            // if we reach here, then element was 
            // not present 
            return -1; 
        } 
        
        void carry(int karyawan){
            // debug
            // for (int i = 0; i < rankingList.size(); i++){
            //     out.println("ranking list " + rankingList.get(i));
            // }

            karyawan-=1;
            if (perusahaanArray[karyawan].size() == 0){
                out.println("0");
            }else {
                int tempRankingTinggi = rankingList.get(perusahaanArray[karyawan].getFirst()-1);
                // out.println("get first : " + perusahaanArray[karyawan].getFirst());
                // out.println("temp ranking : " + tempRankingTinggi);

               for (Integer teman : perusahaanArray[karyawan]){
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
            for (int karyawan: perusahaanArrayDuplikat[kResign]){
                perusahaanArrayDuplikat[karyawan-1].remove((Integer)(kResign+1));

            }
            perusahaanArrayDuplikat[kResign].clear();
            perusahaanArrayDuplikat[kResign].add(-1);
            rankingListDuplikat.set(kResign, -1);
            // out.println("---------resign---------");
            // debug();

        }

        void simulasi(){
            Graph graphDuplikat = this;

            perusahaanArrayDuplikat = new LinkedList[banyakKaryawan];
            rankingListDuplikat = new ArrayList<Integer>();
            
            //int banyakKaryawanDuplikat = banyakKaryawan;
            int counterBanyakKaryawan = 0;
            int counterKaryawanRentan = 0;

            // duplikat linked list
            for (int i = 0; i < banyakKaryawan; i++){
                perusahaanArrayDuplikat[i] = new LinkedList<Integer>(perusahaanArray[i]);
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
                    for (int networkingRentan : perusahaanArrayDuplikat[indexKaryawanRentan]){
                        int rankingNetworkingThis = rankingListDuplikat.get(networkingRentan-1);
                        if (rankingNetworkingThis >= rankingKaryawan){
                            counterTeman++;
                        }
                        //out.println("teman: " + counterTeman);


                        if (counterTeman == perusahaanArrayDuplikat[indexKaryawanRentan].size()){
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
                for (int i = 0; i < perusahaanArray[u-1].size(); i++) {
                   
                        if (visited[perusahaanArray[u-1].get(i).getPangkat()-1] == false) {
                            visited[perusahaanArray[u-1].get(i).getPangkat()-1] = true;
                           
                            // stopping condition (when we find
                            // our destination)
                            int tempRanking = rankingList.get(perusahaanArray[u-1].get(i).getPangkat()-1);

                            if (tempRanking > tempRankingTinggi){
                                tempRankingTinggi = tempRanking;
                            }
                            queue.add(perusahaanArray[u-1].get(i));
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
            
            for (Integer teman : perusahaanArray[karyawanU-1]){
                //out.println("b");

                if (teman == karyawanV){
                    out.println("0");

                    return;
                }
            }

            if (perusahaanArray[karyawanU-1].size() == 0){
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
            for (int i = 0; i < perusahaanArray[u-1].size(); i++) {
                if (visited[perusahaanArray[u-1].get(i)] == false) {
                    visited[perusahaanArray[u-1].get(i)] = true;
                    dist[perusahaanArray[u-1].get(i)] = dist[u] + 1;
                    pred[perusahaanArray[u-1].get(i)] = u;
                    queue.add(perusahaanArray[u-1].get(i));
 
                    // stopping condition (when we find
                    // our destination)
                    if (perusahaanArray[u-1].get(i) == dest)
                        return true;
                }
            }
        }
        return false;
    }

        void debug(){
            int counter = 1;
                for(LinkedList<Integer> p: perusahaanArray){
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

        banyakKaryawan = in.nextInt(); // banyak karyawan mula-mula
        int M = in.nextInt(); // banyak pertemanan mula-mula
        int Q = in.nextInt(); // banyak kejadian atau pertanyaan Kaguya

        Graph graphPerusahaan = new Graph(banyakKaryawan);

        // rank
        for (int i = 0; i < banyakKaryawan; i++){
            int ranking = in.nextInt();
            //rankingList.add(ranking);
            objekKaryawan.add(Karyawan((i+1), ranking));
            perusahaanArray[i].clear();
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
