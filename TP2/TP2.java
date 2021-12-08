import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.LongAccumulator;

import javax.xml.crypto.Data;

import static java.lang.Math.min;
import static java.lang.Math.max;



public class TP2 {

    public static class Dataran {
        Dataran next;
        Dataran prev;
        long data;
        String namaKuil = "";
        boolean kuilBoolean;
        
        public Dataran(int data, Dataran prev, Dataran next, String namaKuil){
            this.data = data;
            this.next = next;
            this.prev = prev;
            this.namaKuil = namaKuil;
            this.kuilBoolean = false;
        }
    
        public Dataran(int data){
            this(data, null, null, null);
        }
    
        public long getValue(){
            return this.data;
        }
    }
    
    public static class Pulau {
    
        public Dataran head;
        public Dataran tail;
        public Dataran current;
        public long size;
        public String namaPulau;
        public Pulau nextPulau;
        public Pulau prevPulau;
    
    
        public Pulau(){
            this.head = null;
            this.tail = null;
            this.current = null;
            this.size = 0;
            this.namaPulau = null;
            this.nextPulau = null;
            this.prevPulau = null;
        }
    
        public Pulau(Dataran pulau){
            this.head = null;
            this.tail = null;
            this.current = null;
            this.size = 0;
            this.namaPulau = null;
            this.nextPulau = null;
            this.prevPulau = null;
        }
    
        public Pulau(String nama){
            this.head = null;
            this.tail = null;
            this.current = null;
            this.size = 0;
            this.namaPulau = nama;
            this.nextPulau = null;
            this.prevPulau = null;
        }
    
        public long size(){
            return size();
        } 

        public Dataran getHead(){
            return this.head;
        }
    
        public void bangun(int input){
            Dataran dataranBaru = new Dataran(input);
            /* 2 case: kalau di depan dan di tengah/tail */
            if (head == null && tail == null){
                head = dataranBaru;
                tail = dataranBaru;
                dataranBaru.namaKuil = namaPulau;
                size++;
                dataranBaru.kuilBoolean = true;
            } else {
                this.tail.next = dataranBaru;
                dataranBaru.prev = this.tail;
                this.tail = dataranBaru;
                size++;
            }
        }
    
        public void unifikasi(String pulauSatuInput, String pulauDuaInput, Map<String, Pulau> pulauMap){  
            Pulau pulauGabung = pulauMap.get(pulauDuaInput); // pulau yang mau digabung
            Pulau thisSekarang = this; // pulau yang kirinya

            /* while nextpulau thisnya gak null maka ditambah dengan nextpulau size 
            kalau udah gabung, pulau gabungnya baru digabung di paling akhir*/
            while (thisSekarang.nextPulau != null){
                thisSekarang.nextPulau.size += pulauGabung.size; // nambahin size pulau next sama pulau yang mau digabung
                thisSekarang = thisSekarang.nextPulau;
            }

                thisSekarang.nextPulau = pulauGabung;
                pulauGabung.prevPulau = thisSekarang;
                
            /* gabungin dataran */
            if (this.tail != null && pulauGabung.head != null){
                    this.tail.next = pulauGabung.head;
                    pulauGabung.head.prev = this.tail;

                    /* set tail pulau juga */
                    if (thisSekarang.nextPulau != null){
                        while (thisSekarang.nextPulau != null){
                            thisSekarang.tail = pulauGabung.tail;
                            thisSekarang =thisSekarang.nextPulau;
                        }
                    }
                    // set tail dataran
                    this.tail = pulauGabung.tail;
                    
             }
                out.println(this.size);
        }
    
        public void pisah(String pisahKuil, Map<String, Pulau> pulau){
            /* pisah pulau = yang mau dipisah
            pulau ujung = prev pulau yang mau dipisah */
            Pulau pisahPulau = this;
            Pulau pulauUjung = this.prevPulau;

            Dataran tempSebelumHead = this.head.prev;
            
            // pulau ujung next pulau dijadiin null, pisah pulau prevnya dijadiin null
            pulauUjung.nextPulau = null;
            pisahPulau.prevPulau = null;

            // pisah dataran
            Dataran tempHeadPrev = this.head.prev ;
            tempHeadPrev.next =null;
            this.head.prev = null;

            pulauUjung.size -= this.size;

            // update size, kalau prev prevnya tidak null dan dikurang sama size pulau yang dipisah
            while (pulauUjung.prevPulau!= null){
               pulauUjung.tail = tempSebelumHead;
                pulauUjung = pulauUjung.prevPulau;
                pulauUjung.size -= this.size;
            }


            pulauUjung.tail = tempSebelumHead;

            out.println(pulauUjung.size + " " + this.size);


        }
    
        public void gerak(String arah, Integer langkah){

            if (arah.equals("KIRI")){
                if (this.current.prev == null){
                        out.println(this.current.getValue());
                } else {
                    while(langkah != 0){
                        if (this.current.prev == null){
                            break;
                        } else {
                            this.current = this.current.prev;
                             langkah--;

                        }
                    }
                  
                    out.println(this.current.getValue());
                }
    
            } else {
                if (this.current.next == null){
                    out.println(this.current.getValue());
                
                } else{
                    while (langkah != 0){
                        if (this.current.next == null){
                            break;
                        } else if (this.current.next != null) {
                            this.current = this.current.next;
                            langkah--;
                        } 
                    
                    }
                    
                    out.println(this.current.getValue());

                }
            }
        }
    
        public void tebas(String arah, int langkah){

            if (arah.equals("KIRI")){          
                if (this.current == null) {
                    out.println("0");

                }
                else if (this.current.prev== null){
                    out.println("0");
                    
                } else {
                    long tempAwal = this.current.getValue(); // nampung value awal
                    Dataran currentPrev = this.current.prev; // nampung prev 
                    int tempLangkah = langkah; // nampung langkah awal
                    long intCurrentPrev = currentPrev.getValue();
                    
                    // while current prev tidak sama dengan null
                    while (currentPrev != null && langkah > 0 ){
                       // kalau current tidak sama dengan prev
                        if(currentPrev != null && intCurrentPrev != tempAwal ){
                            // kalau misal dia pas di cek prev prevnya itu null
                            if(currentPrev.prev == null){
                                break;
                            } else {
                                currentPrev = currentPrev.prev;
                                intCurrentPrev = currentPrev.getValue();
                            }
                            
    
                        } 
                    
                        // kalau curreny sama dengan prev
                        else if(currentPrev != null && intCurrentPrev == tempAwal){
                            this.current = currentPrev; // baru diset thisnya
                            currentPrev = currentPrev.prev;
                            if (currentPrev != null){
                                intCurrentPrev = currentPrev.getValue();
    
                            }
                            langkah--;
                        } 

                        else if (langkah == 0){
                            break;
                        }

    
                    }

                    //case kalau raiden tidak bergerak
                    if (langkah == tempLangkah){
                        out.println("0");
                    } else{
                       out.println(this.current.next.getValue());
    
                    }
                }
                

            }
        
            else if(arah.equals("KANAN")){
                long tempAwal = this.current.getValue(); // nampung value awal
                Dataran currentNext = this.current.next; // nampung next 
                int tempLangkah = langkah;

                if (currentNext == null){
                    out.println("0");
                    
                } else {
                    long intCurrentNext = currentNext.getValue();

                    while (currentNext != null && langkah != 0 ){
                        // kalau current tidak sama dengan next
                        if(currentNext != null && intCurrentNext != tempAwal){
                            if(currentNext.next == null){
                                break;
                            } else {
                                currentNext = currentNext.next;
                                intCurrentNext = currentNext.getValue();
                            }

                        }

                        // kalau current sama dengan next
                        else if(currentNext != null && intCurrentNext == tempAwal){
                            this.current = currentNext;

                            currentNext = currentNext.next;
                            if (currentNext != null){
                                intCurrentNext = currentNext.getValue();

                            }
                            langkah--;

                        }

                        else if (langkah == 0){
                            break;
                        }
                 
                    }
                
                    //case kalau raiden tidak bergerak
                    if (langkah == tempLangkah){
                        out.println("0");
                    
                    } else{
                        out.println(this.current.prev.getValue());

                }
                

                }
            
        }
    }

    
    public void teleportasi(Pulau pulauRaiden, Dataran dataranRaiden, String pindahKuil){

            Pulau raidenTujuan = pulauMap.get(pindahKuil);
            Dataran raidenTujuanHead = raidenTujuan.head;
            String raidenString = raidenTujuanHead.namaKuil;
  
            while(raidenTujuanHead != null){
                    if(raidenTujuan.namaPulau.equals(this.namaPulau) && raidenString.equals(pindahKuil)){

                        pulauRaiden.current = raidenTujuanHead;
                        raidenTujuan.current = raidenTujuanHead;

                        break;

                    } else if(!raidenTujuan.namaPulau.equals(this.namaPulau) && raidenString.equals(pindahKuil)){
            
                        pulauRaiden.current = raidenTujuanHead;
                        raidenTujuan.current = raidenTujuanHead;

                      
                        break;

                    }
                    raidenTujuanHead = raidenTujuanHead.next;

                    

            }

            out.println(pulauRaiden.current.getValue());
            
        }
    
        public void rise(int dataran, int tambah){

            Dataran temp = this.head;
            int result = 0;
            while(temp != null){
                if(temp.data > dataran){
                    temp.data += tambah;
                    result++;
                }
                temp = temp.next;
            }
            out.println(result);
        }
    
        public void quake(int dataran, int kurang){
       
            Dataran temp = this.head;
            int result = 0;

            while(temp != null){
                if(temp.data < dataran){
                    temp.data -= kurang;
                    result++;

                }
                temp = temp.next;
            }
            out.println(result);
        }
    
        public void crumble(Pulau pulauRaiden, Map<String, Pulau> pMap){
  
        
            if(this.current.kuilBoolean){
                out.println("0");
            } else {
                // kalau current ditengah
                if (this.current.prev != null && this.current.next != null){
                    out.println(this.current.data);
                    // mutus hubungan current
                    this.current.next.prev = this.current.prev;
                    this.current.prev.next = this.current.next;
                    this.current = this.current.prev;
                    size--;

                // kalau current diujung
                } else if (this.current.next == null){
                    out.println(this.current.data);
                    this.current = this.current.prev;
                    this.current.next.prev = null;
                    this.current.next = null;
                    this.tail = this.current; // set tail
                    size--;
                }
            }

        }
    
        public void stabilize(Dataran dataranRaiden, Map<String, Pulau> pMap){

            Dataran dataranBaru = new Dataran(0);

            if (this.current.kuilBoolean){
                out.println("0");
            } else {
                // jika ditengah
                if (this.current.prev != null && this.current.next != null){

                    /* jika prevnya kurang sama dengan dari current, 
                    berarti data dataranBarunya current prev*/
                    if (this.current.prev.getValue() <= this.current.getValue()){
     

                        dataranBaru.data = this.current.prev.getValue();

                        dataranBaru.prev = this.current;
                        dataranBaru.next = this.current.next;
                        this.current.next.prev = dataranBaru;
                        this.current.next = dataranBaru;

                        size++;
                        out.println(dataranBaru.data);

                        
                    } 
                     /* jika prevnya lebih dari current, 
                    berarti data dataranBarunya current*/
                    else if (this.current.prev.getValue() > this.current.getValue()){

                        dataranBaru.data = this.current.getValue();

                        dataranBaru.prev = this.current;
                        dataranBaru.next = this.current.next;
                        this.current.next.prev = dataranBaru;
                        this.current.next = dataranBaru;

                        size++;
                        out.println(dataranBaru.getValue());

                    }
                } 
                // case jika diujung
                else if(this.current.next == null){
                    /* jika prevnya kurang sama dengan dari current, 
                    berarti data dataranBarunya current prev*/
                    if (this.current.prev.getValue() <= this.current.getValue()){

                        dataranBaru.data = this.current.prev.getValue();

                        dataranBaru.prev = this.current;
                        this.current.next = dataranBaru;
                        this.tail = dataranBaru;

                        size++;
                        out.println(dataranBaru.getValue());

                    } 
                    /* jika prevnya lebih dari current, 
                    berarti data dataranBarunya current*/
                    else if (this.current.prev.getValue() > this.current.getValue()){

                        dataranBaru.data = this.current.getValue();

                        dataranBaru.prev = this.current;
                        this.current.next = dataranBaru;
                        this.tail = dataranBaru;

                        size++;
                        out.println(dataranBaru.getValue());

                    }
                }
            }

        }

        public void sweeping(int tinggiLaut){
            // jika headnya kurang dari inputan maka coutnya ditambah
            Dataran temp = this.head;
            int count = 0;
            while (temp!=null){
                if (temp.data < tinggiLaut){
                    count++;
                }
                temp = temp.next;

            }
            out.println(count);
        }

        // public void sketsa(){
        //     out.println("---SKETSA---");
        //     // TODO - handle SKETSA
        //     StringBuilder sketsa = new StringBuilder();
        //     Pulau n = this;
        //     while(n != null) {
        //         sketsa.append(n.namaPulau + " ");
        //         n = n.nextPulau;
        //     } 
        //     out.println(sketsa.toString());
        // }

        // public void sketsaDataran(){
        //     out.println("---SKETSA---");
        //     // TODO - handle SKETSA
        //     StringBuilder sketsa = new StringBuilder();
        //     Dataran n = this.head;
        //     while(n != null) {
        //         sketsa.append(n.data + " ");
        //         n = n.next;
        //     } 
        //     out.println(sketsa.toString());
        // }
    
    }

    private static InputReader in;
    private static PrintWriter out;
    public static Pulau Pulau;
    public static Map<String, Pulau> pulauMap = new HashMap<>(); 

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Write here
        int banyakPulau = in.nextInt(); // banyak pulau

        for (int i = 0; i < banyakPulau; i++){
            String namaPulau = in.next(); // nama pulau
            Pulau pulauBaru = new Pulau(namaPulau);
            pulauMap.put(namaPulau, pulauBaru);
            long banyakDataran = in.nextInt(); // banyak dataran
            for (int j = 0; j < banyakDataran; j++){
                int tinggiDataran = in.nextInt();
                pulauMap.get(namaPulau).bangun(tinggiDataran);

                //Pulau letakKuil = pulauMap.get(namaPulau);
                //kuilMap.put(namaPulau, letakKuil);
            }
        }

        // Set current Raiden
        String pulauRaiden = in.next(); // pulau raiden berada
        long dataranRaiden = in.nextInt();
        Pulau tempatRaiden = pulauMap.get(pulauRaiden); // get pulauRaiden
        Dataran pulauHead = tempatRaiden.head; // set head Raiden

        /* jika dataran raidennya 1 maka langsung di set di head
        kalau int nya gak satu di set currentnya itu di mana index yang diinginkan */
        if (dataranRaiden == 1){
            tempatRaiden.current = pulauHead;
        } else {
            for (int i = 1 ; i <= dataranRaiden ; i ++){
                tempatRaiden.current = pulauHead;              
                pulauHead = pulauHead.next;

            }
        }

        int perintahManipulasi = in.nextInt();

        for (int i = 0; i < perintahManipulasi; i++){
            String manipulasi = in.next();

            if(manipulasi.equals("UNIFIKASI")){
                String pulauSatu = in.next();
                String pulauDua = in.next(); 
                Pulau pulauSatuGet = pulauMap.get(pulauSatu);
                Pulau pulauDuaGet = pulauMap.get(pulauDua);
                pulauSatuGet.size += pulauDuaGet.size; // set size
                pulauSatuGet.unifikasi(pulauSatu, pulauDua, pulauMap);

                /* kalau pulau dua itu sama seperti tempatRaiden, berarti tempatRaiden dijadiin pulausatu
                current tempatraidennya diset*/
                if (pulauDuaGet == tempatRaiden){
                    tempatRaiden = pulauSatuGet;
                    tempatRaiden.current = pulauDuaGet.current;
                }

            }
    
            else if(manipulasi.equals("PISAH")){

                String pisahKuil = in.next();

                pulauMap.get(pisahKuil).pisah(pisahKuil, pulauMap);
                

            }
    
            else if(manipulasi.equals("GERAK")){
                String arah = in.next();
                int langkah = in.nextInt();
                pulauMap.get(tempatRaiden.namaPulau).gerak(arah, langkah);

            }
    
            else if(manipulasi.equals("TEBAS")){
                String arah = in.next();
                int langkah = in.nextInt();
                pulauMap.get(tempatRaiden.namaPulau).tebas(arah, langkah);
               
            }
    
            else if(manipulasi.equals("TELEPORTASI")){
                String pindahKuil = in.next();
                pulauMap.get(tempatRaiden.namaPulau).teleportasi(pulauMap.get(tempatRaiden.namaPulau), tempatRaiden.current, pindahKuil);
                tempatRaiden = pulauMap.get(pindahKuil);
            }
    
            else if(manipulasi.equals("RISE")){
                String dataranTinggi = in.next();
                int angka = in.nextInt();
                int naikMeter = in.nextInt(); 
                pulauMap.get(dataranTinggi).rise(angka, naikMeter);
            } 
    
            else if(manipulasi.equals("QUAKE")){
                String dataranRendah = in.next();
                int angka = in.nextInt();
                int turunMeter = in.nextInt(); 
                pulauMap.get(dataranRendah).quake(angka, turunMeter);
            } 
    
            else if(manipulasi.equals("CRUMBLE")){
                pulauMap.get(tempatRaiden.namaPulau).crumble(tempatRaiden, pulauMap);
            } 
    
            else if(manipulasi.equals("STABILIZE")){
                pulauMap.get(tempatRaiden.namaPulau).stabilize(tempatRaiden.current, pulauMap);
            }

            else if(manipulasi.equals("SWEEPING")){
                String pulau = in.next();
                int tinggiLaut = in.nextInt();
                pulauMap.get(pulau).sweeping(tinggiLaut);
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