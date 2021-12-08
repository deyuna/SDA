import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;


class TP3{

    private static InputReader in;
    private static PrintWriter out;

    private static ArrayList<Integer> rankingList = new ArrayList<>();
    private static LinkedList<Integer> perusahaanLinked[];
    private static ArrayList<Karyawan> karyawanList = new ArrayList<>();
    
    private static ArrayList<ArrayList<Karyawan>> rankingSama = new ArrayList<>(); // untuk handle sebar

    private static int banyakKaryawan = 100000;   // No. of vertices
    private static int countSimulasi = 0;
    private static boolean isSimulasi = false;
    private static int temanTinggiUSebelum = 0 ;
    private static int temanTinggiVSebelum = 0 ;


    public static class Karyawan {
        int identitas;
        int pangkat;
        boolean isPalingTinggi = false; // is lulus
        int temanPangkatAtas;

        public Karyawan(int identitas){
            this.identitas = identitas;
        }
        
        public Karyawan(int identitas, int pangkat){
            this.identitas = identitas;
            this.pangkat = pangkat;
            //this.maxRankingTeman = maxRankingTeman;
        }

        public int getIdentitas(){
            return this.identitas;
        } 

        public void setIdentitas(int karyawan){
            this.identitas = karyawan;
        }

        public int getPangkat(){
            return this.pangkat;
        } 

        public void setPangkat(int karyawan){
            this.pangkat = karyawan;
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

            if (isSimulasi && karyawanList.get(karyawanU-1).temanPangkatAtas > 0 && temanTinggiUSebelum == 0 ){
                countSimulasi--;
                karyawanList.get(karyawanU-1).isPalingTinggi = false;
            }
            if (isSimulasi && karyawanList.get(karyawanV-1).temanPangkatAtas > 0 && temanTinggiVSebelum == 0 ){
                countSimulasi--;
                karyawanList.get(karyawanV-1).isPalingTinggi = false;

            }
            // out.println("---------tambah---------");
            // out.println("tambah U: " + karyawanList.get(karyawanU-1).identitas + " " + karyawanList.get(karyawanU-1).temanPangkatAtas);
            // out.println("tambah V: " + karyawanList.get(karyawanV-1).identitas + " " + karyawanList.get(karyawanV-1).temanPangkatAtas);

            // debug();
        }

        void resign(int kResign){
            kResign -= 1;
            for (int teman: perusahaanLinked[kResign]){
                int tempTeman = teman;
                int karyawanPangkat = karyawanList.get(kResign).getPangkat();
                Karyawan objTemanResign = karyawanList.get(tempTeman-1);
                int pangkatTemanResign = objTemanResign.getPangkat();
                if(pangkatTemanResign < karyawanPangkat){
                    //out.println("counter di if awal sini: " + countSimulasi);

                    objTemanResign.temanPangkatAtas--;

                    if (isSimulasi && objTemanResign.temanPangkatAtas == 0){
                        //out.println("counter di sini: " + countSimulasi);
                        countSimulasi++;
                        objTemanResign.isPalingTinggi = true;
                    }
                }


                perusahaanLinked[teman-1].remove((Integer)(kResign+1));

            }

            // jika pernah simulasi dan kResign adalah rank tertinggi
            // maka count simulasi dikurang dan dia bukan tertinggi lagi karena diresign
            if (isSimulasi && karyawanList.get(kResign).isPalingTinggi){
                countSimulasi--;
                karyawanList.get(kResign).isPalingTinggi = false;
            }



            perusahaanLinked[kResign].clear();
            perusahaanLinked[kResign].add(-1);
            Karyawan objKResign = karyawanList.get(kResign);
            rankingSama.get(objKResign.getPangkat()-1).remove(objKResign);
            objKResign.setPangkat(-1);
            rankingList.set(kResign, -1);

            //karyawanList.get(kResign).pangkat--;
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

        void simulasi(){
            
            if (!isSimulasi){
                for (int i = 0 ; i< karyawanList.size(); i++){
                    if (karyawanList.get(i).getPangkat() != -1 && karyawanList.get(i).temanPangkatAtas == 0){
                        countSimulasi++;
                        karyawanList.get(i).isPalingTinggi = true;
                    }
                    isSimulasi = true;

                }
            }
            out.println(countSimulasi);
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

        private void sebar(int karyawanU, int karyawanV, int v){
            int pred[] = new int[v+1];
            int dist[] = new int[v+1];

            // kalau karyawan yang dituju sama seperti karyawan source
            if (karyawanU == karyawanV){
                out.println("0");
                return;
            }
            
            // kalau karyawan yang dituju itu temennya
            for (Integer teman : perusahaanLinked[karyawanU-1]){
                if (teman == karyawanV){
                    out.println("0");

                    return;
                }
            }

            // kalau karyawan null
            if (perusahaanLinked[karyawanU-1] == null){
                out.println("-1");

                return;
            }
           
            // kalau gak ada jalan ke karyawan yang dituju
            else if (BFS(karyawanU, karyawanV, v, pred, dist) == false) {
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
        boolean visitedRanking[] = new boolean[v+1];
 
        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            visitedRanking[i] = false;
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
            // untuk pangkat beda
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

            // untuk pangkat sama
            if(karyawanList.get(u-1).pangkat != -1 && !visitedRanking[karyawanList.get(u-1).pangkat -1]){
                ArrayList<Karyawan> tempRankingSama = rankingSama.get(karyawanList.get(u-1).pangkat-1);
                visitedRanking[karyawanList.get(u-1).pangkat -1] = true;
                for (int i = 0; i < tempRankingSama.size(); i++) {
                    if (tempRankingSama.get(i).identitas != -1){
                    if (visited[tempRankingSama.get(i).identitas] == false) {
                        visited[tempRankingSama.get(i).identitas] = true;
                        dist[tempRankingSama.get(i).identitas] = dist[u] + 1;
                        pred[tempRankingSama.get(i).identitas] = u;
                        queue.add(tempRankingSama.get(i).identitas);
         
                        // stopping condition (when we find
                            // our destination)
                        if (tempRankingSama.get(i).identitas == dest)
                            return true;
                        }
                    }
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

        for (int i = 0; i < N; i++){
             rankingSama.add(new ArrayList<>()); // new array list ranking sama
        }

        // rank
        for (int i = 0; i < N; i++){
            int ranking = in.nextInt();
            rankingList.add(ranking);
            Karyawan objKaryawan = new Karyawan(i+1);
            objKaryawan.setPangkat(ranking);
            rankingSama.get(ranking-1).add(objKaryawan); // nambah ranking sama
            karyawanList.add(objKaryawan);
            perusahaanLinked[i].clear();
        }     
        
        for (int i = 0; i < M; i++){
            int temanSatu = in.nextInt();
            int temanDua = in.nextInt();

            Karyawan objSatu = karyawanList.get(temanSatu-1);
            Karyawan objDua = karyawanList.get(temanDua-1);

            temanTinggiUSebelum = objSatu.temanPangkatAtas;
            temanTinggiVSebelum = objDua.temanPangkatAtas;

            if (objSatu.pangkat == objDua.pangkat){
                objSatu.temanPangkatAtas++;
                objDua.temanPangkatAtas++;

            }
            /* jika pangkat dua lebih besar maka teman pangkat atas objek satu ditambah
            jika pangkat satu lebih besar maka teman pangkat atas objek dua ditambah */
            else if (objSatu.getPangkat() <= objDua.getPangkat()){
                objSatu.temanPangkatAtas++;
            } 
            
            else if(objSatu.getPangkat() >= objDua.getPangkat()){
                objDua.temanPangkatAtas++;

            }
            graphPerusahaan.tambah(temanSatu, temanDua);
        }

        while(Q-- > 0){
            int pertanyaan = in.nextInt();
            if (pertanyaan == 1){
                int karyawanU = in.nextInt();
                int karyawanV = in.nextInt();
                Karyawan objSatu = karyawanList.get(karyawanU-1);
                Karyawan objDua = karyawanList.get(karyawanV-1);
    
                temanTinggiUSebelum = objSatu.temanPangkatAtas;
                temanTinggiVSebelum = objDua.temanPangkatAtas;
    
                if (objSatu.pangkat == objDua.pangkat){
                    objSatu.temanPangkatAtas++;
                    objDua.temanPangkatAtas++;
    
                }
                /* jika pangkat dua lebih besar maka teman pangkat atas objek satu ditambah
                jika pangkat satu lebih besar maka teman pangkat atas objek dua ditambah */
                else if (objSatu.getPangkat() <= objDua.getPangkat()){
                    objSatu.temanPangkatAtas++;
                } 
                
                else if(objSatu.getPangkat() >= objDua.getPangkat()){
                    objDua.temanPangkatAtas++;
    
                }
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

/* Ide: Zuhal, Sabyna Maharina, Zuhal 'Alimul Hadi 
Kolaborator: Nahda Amalia, Priyanka Devi, Evan Christoper */
