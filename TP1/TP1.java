import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.Math.min;
import static java.lang.Math.max;

class TP1 {

    private static InputReader in;
    private static PrintWriter out;
    private static Deque<Agent> allKodeUnik = new ArrayDeque<>(); // rank yang sudah terurut
    private static Map<String, Agent> murid = new HashMap<>(); // map string nama dengan object agent
    private static long deploy[][][];
    private static boolean cekDeploy[][][];
    private static Object arrKodeUnik[];
    private static int indexAwal;
    private static int indexAkhir;

    
    public static class Agent{
        public String nama;
        public String spesialis;
        public int ditunjuk;
        public int ranking;
        public boolean apakahEval;
        
        public Agent(String nama, String spesialis){
            this.nama = nama;
            this.spesialis = spesialis;
            this.ditunjuk = 0;
            this.ranking = 0;
            this.apakahEval = true;
        }

        public String getNama(){
            return this.nama;
        }        

        public String getSpesialis(){
            return this.spesialis;
        }

        public int getDitunjuk(){
            return this.ditunjuk;
        }
        
        public int getRanking(){
            return this.ranking;
        }

        public boolean getApakahEval(){
            return this.apakahEval;
        }

        public void setApakahEval(boolean apakahEval){
            this.apakahEval = apakahEval;
        }

        public void setRanking(int ranking){
            this.ranking = ranking;

        }

        public void setDitunjuk(){
            this.ditunjuk += 1;
        }

    }

    /* Handle ranking 
    jika murid containskey kode unik, masuk ke if.
    V == 0 berarti offer ke urutan awal
    V == 1 berarti offer ke urutan akhir
    */
    private static void handleRank(String U, int V){
        if (murid.containsKey(U)){
            Agent muridBaru = murid.get(U);
            muridBaru.setDitunjuk(); // berguna untuk kompetitif
            allKodeUnik.remove(muridBaru); // remove dulu agar tidak duplikat        
            if (V == 0){
                allKodeUnik.offerFirst(muridBaru); // add ke urutan awal
            } else if (V == 1){
                allKodeUnik.offerLast(muridBaru); // add ke urutan akhir
    
            }
        }
      
    }

    /* Handle Panutan
     memakai for each jika Q = 0  print 0 0
     jika spesialis = B maka menambah countBakso 1
     jika spesialis = S maka menambak countSiomay 1
     */
    static private void handlePanutan(int Q){
        long countBaso = 0;
        long countSiomay = 0;
        for (Agent kodeUnik : allKodeUnik){
            if (Q == 0){
                break;
            }
            if (kodeUnik.getSpesialis().equals("B")){
                countBaso++;
            } else {
                countSiomay++;
            }
            Q--;
        }
        out.println(countBaso + " " + countSiomay);
    }

    /* Handle Kompetitif 
     jika mendapatkan count terbesar maka print string kode unik 
     dan print count yang paling banyak ditunjuk siesta*/
    static private void handleKompetitif(){
        int count = 0;
        String maksDitunjuk = "";
        for (Agent kodeUnik : allKodeUnik ) {
            if (kodeUnik.getDitunjuk() > count){
                count = kodeUnik.getDitunjuk();
                maksDitunjuk = kodeUnik.getNama();
            }
        }
        out.println(maksDitunjuk + " " + count); 
    }

    /* Handle Evaluasi
    jika evaluasi maka print kode unik tersebut
    jika tidak ada yang dieval print "TIDAK ADA" */
    static private void handleEvaluasi(){
        ArrayList<String> namaTemp = new ArrayList<>();
        boolean adaEvaluasi = false;
        for (Agent kodeUnik : allKodeUnik){
            if (kodeUnik.getApakahEval()){
                namaTemp.add(kodeUnik.getNama());
                adaEvaluasi = true;
            }
        }
        if (!adaEvaluasi){
            out.println("TIDAK ADA");
        } else {
            out.println(String.join(" ", namaTemp));
        }
    }

    /* Handel Duo
     membuat arraylist untuk spesialis baso, siomay, temp (tampung semua spesialis)
     1. jika spesialis kode unik = B tampung ke arraylist baso
        jika spesialis kode unik = S tampung ke arraylist siomay
     2. jika size baso lebih kecil maka menggunakan size baso
        jika tidak maka sebaliknya
        
    3. lalu print kode unik spesialis baso dan siomay sesuai kesamaan index
       kalau ada sisa berarti masukkan ke TIDAK DAPAT: */
    static private void handleDuo(){
        ArrayList<String> baso = new ArrayList<>();
        ArrayList<String> siomay = new ArrayList<>();
        Deque<String> temp = new ArrayDeque<>();

        for (Agent kodeUnik : allKodeUnik){
            temp.add(kodeUnik.getNama());
            if(kodeUnik.getSpesialis().equals("B")){
                baso.add(kodeUnik.getNama());
            } else if(kodeUnik.getSpesialis().equals("S")){
                siomay.add(kodeUnik.getNama());
            }
        }

        int count = 0;
        if (baso.size() < siomay.size()){
            count = baso.size();
        } else {
            count = siomay.size();
        }

        for (int i = 0; i < count; i++){
            out.println(baso.get(i).toString() + " " + siomay.get(i).toString());
            temp.remove(baso.get(i));
            temp.remove(siomay.get(i));
        }

        if (!temp.isEmpty()){
            out.print("TIDAK DAPAT: ");
            for (String t : temp){
                out.print(t + " ");
            }
            out.println(" ");
        }

    }

    /* Handle Deploy
      base case:
      program berhenti jika index akhir dari grup lebih besar dari array
      
      recursive case: 
      - jika awal dan akhir grup mempunyai spesialis sama,
        maka cek apakah grup == 1 dan indexAkhir == length array - 1
        lalu return 1
      - jika grup >= 1 dan awal dan akhir grup mempunyai spesialis sama,
        maka result = mengecek dua kemungkinan
        else, akan melihat indexAkhir + 1 di array

       */
    public static long handleDeploy(int grup, int indexAwal, int indexAkhir, Object[] arrKodeUnik){
        long mod = 1000000007;

        if (indexAkhir > arrKodeUnik.length - 1 ){
            return 0;
        }

        String awal = ((Agent)arrKodeUnik[indexAwal]).getSpesialis();
        String akhir = ((Agent)arrKodeUnik[indexAkhir]).getSpesialis();


        /* Setelah suatu state memiliki nilai maka kita tidak perlu melakukan rekursi kembali dan hanya
        perlu memanggil dari isi array dengan state tersebut. */ 
        if (cekDeploy[grup][indexAwal][indexAkhir]){
            return deploy[grup][indexAwal][indexAkhir];
        }

        if (awal.equals(akhir)){
            if (grup == 1 && indexAkhir == arrKodeUnik.length-1){
                return 1;
            }
        }
       
        long result = 0;

        if (grup >= 1 && awal.equals(akhir)){
            result = handleDeploy(grup-1, indexAkhir+1, indexAkhir+2, arrKodeUnik) + handleDeploy(grup, indexAwal, indexAkhir+1, arrKodeUnik);
        } else {
            result = handleDeploy(grup, indexAwal, indexAkhir+1, arrKodeUnik);
        }
           
        cekDeploy[grup][indexAwal][indexAkhir] = true;
        deploy[grup][indexAwal][indexAkhir] = result;

        return result%mod;
    
}

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int banyakBatch = in.nextInt(); // banyak batch

        for (int i = 0; i < banyakBatch; i++){
            int banyakMurid = in.nextInt(); // banyak murid

            for (int j = 0; j < banyakMurid; j++){
                String kodeUnik = in.next(); // kode unik
                String spesialisasiMurid = in.next(); // spesialiasasi murid

                Agent agentBaru = new Agent(kodeUnik, spesialisasiMurid);
                agentBaru.setRanking(j); // set ranking awal
                murid.put(agentBaru.getNama(), agentBaru); // put ke map
                allKodeUnik.add(agentBaru); // add ke deque
            }
    
            int berapaHari = in.nextInt(); // berapa hari
            
            for (int j = 0; j < berapaHari; j++){
                int berapaKali = in.nextInt(); // berapa kali
    
                for (int k = 0; k < berapaKali; k++){
                    String kodeUnikKedua = in.next(); // kode unik
                    int updateRanking = in.nextInt(); // update ranking
                    handleRank(kodeUnikKedua, updateRanking);
                    }
                    /* Count untuk hari ini, getRanking yang hari kemarin
                       jika getRanking lebih besar, otomatis urutan hari ini lebih tinggi
                       maka tidak evaluasi
                       kebalikannya maka evaluasi

                       lalu setRanking sekarang menggunakan count */
                    int count = 0;
                    for (Agent murid : allKodeUnik){
                        out.print(murid.getNama() + " ");
                        if(murid.getRanking() > count){
                            murid.setRanking(count);
                            murid.setApakahEval(false);
                        } else if (murid.getRanking() < count){
                            murid.setRanking(count);
                        }
                        count ++;
                    }

                    out.println(" ");
                }
    
            String evaluasi = in.next();
    
            if(evaluasi.equals("PANUTAN")) {
                int Q = in.nextInt(); // Q ranking teratas
                handlePanutan(Q);
                allKodeUnik = new ArrayDeque<>();
    
            } else if(evaluasi.equals("KOMPETITIF")) {
                handleKompetitif();
                allKodeUnik = new ArrayDeque<>();
    
            } else if(evaluasi.equals("EVALUASI")){
                handleEvaluasi();
                allKodeUnik = new ArrayDeque<>();
    
            } else if(evaluasi.equals("DUO")){
                handleDuo();
                allKodeUnik = new ArrayDeque<>();
    
            } else if(evaluasi.equals("DEPLOY")){
                int K = in.nextInt(); // berapa banyak grup
                indexAwal = 0;
                indexAkhir = 1;
                arrKodeUnik = allKodeUnik.toArray(); // dijadikan ke array
                deploy = new long[K + 1][arrKodeUnik.length + 1][arrKodeUnik.length + 1];
                cekDeploy = new boolean[K + 1][arrKodeUnik.length + 1][arrKodeUnik.length + 1];
                out.println(handleDeploy(K, indexAwal, indexAkhir, arrKodeUnik));
                allKodeUnik = new ArrayDeque<>();
                    
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

/*

Berkolaborasi dengan:
1. Alya Astrid Shakila
2. Evan Christoper Samosir
3. Nahda Amalia
4. Priyanka Devi 

*/

