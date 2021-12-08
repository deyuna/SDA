import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.Math.max;

class Lab6 {

    private static InputReader in;
    private static PrintWriter out;
    private static final int FRONT = 1;

    public static class Dataran {
        int tinggiDataran;
        int urutan;
        int urutanHeap;

        public Dataran(int tinggiDataran, int urutan){
            this.tinggiDataran = tinggiDataran;
            this.urutan = urutan;
        }

    }

    public static class MinHeap {

        private Dataran[] Heap;
        private int size;
        private int maxsize;
        
    // Constructor of this class
    public MinHeap(int maxsize)
    {
  
        // This keyword refers to current object itself
        this.maxsize = maxsize;
        this.size = 0;
  
        Heap = new Dataran[this.maxsize + 1];
        Dataran dataran = new Dataran(0, Integer.MIN_VALUE);
        Heap[0] = dataran;
    }
  
    // Method 1
    // Returning the position of
    // the parent for the node currently
    // at pos
    private int parent(int pos) { return pos / 2; }
  
    // Method 2
    // Returning the position of the
    // left child for the node currently at pos
    private int leftChild(int pos) { return (2 * pos); }
  
    // Method 3
    // Returning the position of
    // the right child for the node currently
    // at pos
    private int rightChild(int pos)
    {
        return (2 * pos) + 1;
    }
  
    // Method 4
    // Returning true if the passed
    // node is a leaf node
    private boolean isLeaf(int pos)
    {
  
        if (pos > (size / 2) && pos <= size) {
            return true;
        }
  
        return false;
    }
  
    // Method 5
    // To swap two nodes of the heap
    private void swap(int fpos, int spos)
    {
  
        Dataran tmp;
        tmp = Heap[fpos];
  
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;

        
    }
  
    // Method 6
    // To heapify the node at pos
    private void minHeapify(int pos)
    {
  
        // If the node is a non-leaf node and greater
        // than any of its child
        if (!isLeaf(pos)) {
            if (Heap[pos].tinggiDataran > Heap[leftChild(pos)].tinggiDataran
                || Heap[pos].tinggiDataran > Heap[rightChild(pos)].tinggiDataran) {
  
                // Swap with the left child and heapify
                // the left child
                if (Heap[leftChild(pos)].tinggiDataran
                    < Heap[rightChild(pos)].tinggiDataran) {
                    swap(pos, leftChild(pos));
                    minHeapify(leftChild(pos));
                }
  
                // Swap with the right child and heapify
                // the right child
                else {
                    swap(pos, rightChild(pos));
                    minHeapify(rightChild(pos));
                }
            }
        }
    }
  
    // Method 7
    // To insert a node into the heap
    public void insert(Dataran element)
    {
  
        if (size >= maxsize) {
            return;
        }
  
        Heap[++size] = element;
        element.urutanHeap = size;

        int current = size;
  
        while (Heap[current].tinggiDataran < Heap[parent(current)].tinggiDataran) {
            swap(current, parent(current));
            current = parent(current);
        }
    

    }
  
    // Method 8
    // To print the contents of the heap
    public void print()
    {
        for (int i = 1; i <= size / 2; i++) {
  
            // Printing the parent and both childrens
            System.out.print(
                " PARENT : " + Heap[i].tinggiDataran
                + " LEFT CHILD : " + Heap[2 * i].tinggiDataran
                + " RIGHT CHILD :" + Heap[2 * i + 1].tinggiDataran);
  
            // By here new line is required
            System.out.println();
        }
    }
  
    // Method 9
    // To remove and return the minimum
    // element from the heap
    public Dataran remove()
    {
  
        Dataran popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--];
        minHeapify(FRONT);
  
        return popped;
    }
  


    }

  
    
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        List<Dataran> tanah = new ArrayList<>();
        MinHeap minHeap = new MinHeap(100000);
        int urutan = 0;

        
        int N = in.nextInt();
        for (int i = 0; i < N; i++) {
            int height = in.nextInt();
            Dataran dataran = new Dataran(height, urutan);
            tanah.add(dataran);
            minHeap.insert(dataran);
        }

        int Q = in.nextInt();
        while(Q-- > 0) {
            String query = in.next();
            if (query.equals("A")) {
                // TODO: Handle query A
                int ketinggian = in.nextInt();
                Dataran dataran = new Dataran(ketinggian, urutan);
                tanah.add(dataran);
                minHeap.insert(dataran);
                urutan++;
                

                //minHeap.print();
            } else if (query.equals("U")) {
                // TODO: Handle query U
                int indexDataran = in.nextInt();
                int ubahTinggi = in.nextInt();
                Dataran dataran = tanah.get(indexDataran);
                dataran.tinggiDataran = ubahTinggi;
                tanah.set(indexDataran, dataran);
                minHeap.minHeapify(dataran.tinggiDataran);
            } else {
                // TODO: Handle query R
                Dataran tempMin = minHeap.remove();
                int tempMax = 0;
                if(tempMin.urutan == tanah.size()){
                    Dataran sebelahAkhir = tanah.get(tempMin.urutan- 1);
                    tempMax = max(tempMin.tinggiDataran, sebelahAkhir.tinggiDataran);
                    tempMin.tinggiDataran = tempMax;
                    minHeap.minHeapify(sebelahAkhir.urutanHeap);
                    minHeap.insert(tempMin);
                }
                else if(tempMin.urutan == 0){
                    Dataran sebelahAwal = tanah.get(1);
                    tempMax = max(tempMin.tinggiDataran, sebelahAwal.tinggiDataran);
                    tempMin.tinggiDataran = tempMax;
                    minHeap.minHeapify(sebelahAwal.urutanHeap);
                    minHeap.insert(tempMin);
                }  else {
                    Dataran indexKanan = tanah.get(tempMin.urutan + 1);
                    Dataran indexKiri = tanah.get(tempMin.urutan - 1);
                    tempMax = max(indexKanan.tinggiDataran, indexKiri.tinggiDataran);
                    tempMin.tinggiDataran = tempMax;
                    indexKanan.tinggiDataran = tempMax;
                    indexKiri.tinggiDataran = tempMax;
                    minHeap.minHeapify(indexKanan.tinggiDataran);
                    minHeap.minHeapify(indexKiri.tinggiDataran);
                    minHeap.insert(tempMin);
            }
        }
    }
    


        out.flush();
}
    

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader readeur;
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
