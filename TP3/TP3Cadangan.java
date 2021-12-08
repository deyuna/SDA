import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;


class TP3 {

    private static InputReader in;
    private static PrintWriter out;
    private static ArrayList<Integer> rankingList = new ArrayList<>();
    private static LinkedList<Integer> perusahaanLinked[];
    private static int banyakKaryawan = 100000;   // No. of vertices
    private static ArrayList<Integer> rankingListDuplikat = new ArrayList<>();
    private static LinkedList<Integer> perusahaanLinkedDuplikat[];

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

        void boss(int karyawan){
            karyawan -=1;
            if (perusahaanLinked[karyawan].size() == 0){
                out.println("0");
            } else {
                // temp awal
                int tempRankingTinggi = rankingList.get(perusahaanLinked[karyawan].getFirst()-1);
                for (Integer teman : perusahaanLinked[karyawan]){
                    int tempRankTeman = rankingList.get(teman-1);
                    if(tempRankTeman > tempRankingTinggi){
                        tempRankingTinggi = tempRankTeman;
                    }

                    //out.println(rankingList.get(teman));
                    
                }
                
                out.println(tempRankingTinggi);
            }
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
        
        
        void sebar(int karyawanU, int karyawanV){
            out.println("-1");
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
                graphPerusahaan.boss(networkU);
            }
            else if (pertanyaan == 5){
                int karyawanU = in.nextInt();
                int karyawanV = in.nextInt();
                graphPerusahaan.sebar(karyawanU, karyawanV);

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
