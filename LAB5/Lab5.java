import java.io.*;
import java.util.*;

public class Lab5 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    public static Map<String, Integer> hargaPermen = new HashMap<>();
    public static Map<String, Integer> tipePermen = new HashMap<>();
    public static Map<Integer, Integer> countHarga = new HashMap<>();
    //public static ArrayList<String> duplikatTipe = new ArrayList<>();
    //public static Permen root;
    static AVLTree tree = new AVLTree();
    public static int count;


    public static class Permen {
        //ArrayList<String> namaduplikat = new ArrayList<>();
        String nama;
        int harga; 
        int tipe;
        int height;
        Permen left;
        Permen right;
        int count;
        //Permen root;
        // ArrayList<String> duplikatHarga = new ArrayList<>();
        //ArrayList<String> duplikatTipe = new ArrayList<>();

        //ArrayList<Integer> tipeDuplikat = new ArrayList<Integer>();

        public Permen(int harga){
            //this.nama = nama;
            this.harga = harga;
            //this.tipe = tipe;
            this.height += 1;
        }
    }

    public static class AVLTree {
        public static Permen root;
        List<Integer> intArray = new ArrayList<>();

        public int height(Permen size){
            if (size == null)
                return 0;
            
                return size.height;
            }
        
        Permen rightRotate(Permen y) {
            Permen x = y.left;
            Permen T2 = x.right;
    
            // Perform rotation
            x.right = y;
            y.left = T2;
    
            // Update heights
            y.height = max(height(y.left), height(y.right)) + 1;
            x.height = max(height(x.left), height(x.right)) + 1;
    
            // Return new root
            return x;
        }

        static int max(int a, int b) {
        return (a > b) ? a : b;
        }

        static Permen minValueNode(Permen node){
        Permen current = node;
 
        /* loop down to find the leftmost leaf */
        while (current.left != null)
            current = current.left;
 
        return current;
    }
    
        // A utility function to left rotate subtree rooted with x
        // See the diagram given above.
        Permen leftRotate(Permen x) {
            Permen y = x.right;
            Permen T2 = y.left;
    
            // Perform rotation
            y.left = x;
            x.right = T2;
    
            //  Update heights
            x.height = max(height(x.left), height(x.right)) + 1;
            y.height = max(height(y.left), height(y.right)) + 1;
    
            // Return new root
            return y;
        }
    
        // Get Balance factor of node N
        int getBalance(Permen N) {
            if (N == null)
                return 0;
    
            return height(N.left) - height(N.right);
        }
    
        Permen insert(Permen node, int harga) {
    
            /* 1.  Perform the normal BST insertion */
            if (node == null){
                return new Permen(harga);
                
            }
                
    
            if (harga < node.harga) {
                node.left = insert(node.left, harga);
            }
            else if (harga > node.harga){
                node.right = insert(node.right, harga);
            }    
            else {
                // node.duplikatHarga.add(nama);
               //node.duplikatTipe.add(nama);
               node.count++;
                return node;
            } // Duplicate keys not allowed
    
            /* 2. Update height of this ancestor node */
            node.height = 1 + max(height(node.left),
                                height(node.right));
    
            /* 3. Get the balance factor of this ancestor
                node to check whether this node became
                unbalanced */
            int balance = getBalance(node);
    
            // If this node becomes unbalanced, then there
            // are 4 cases Left Left Case
            if (balance > 1 && harga < node.left.harga)
                return rightRotate(node);
    
            // Right Right Case
            if (balance < -1 && harga > node.right.harga)
                return leftRotate(node);
    
            // Left Right Case
            if (balance > 1 && harga > node.left.harga) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
    
            // Right Left Case
            if (balance < -1 && harga < node.right.harga) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
    
            /* return the (unchanged) node pointer */
            return node;
        }
    
        // A utility function to print preorder traversal
        // of the tree.
        // The function also prints height of every node
        void preOrder(Permen node) {
            if (node != null) {
                //out.println(node.height + " ");
                preOrder(node.left);
                preOrder(node.right);
            }
        }

    //     void printInorder(Permen node)
    // {
    //     if (node == null)
    //         return;
 
    //     /* first recur on left child */
    //     printInorder(node.left);
 
    //     /* then print the data of node */
    //     out.print(node.harga + " ");
 
    //     /* now recur on right child */
    //     printInorder(node.right);
    // }
    

    Permen deleteNode(Permen root, int harga) {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (root == null)
            return root;
 
        // If the key to be deleted is smaller than
        // the root's key, then it lies in left subtree
        if (harga < root.harga)
            root.left = deleteNode(root.left, harga);
 
        // If the key to be deleted is greater than the
        // root's key, then it lies in right subtree
        else if (harga > root.harga)
            root.right = deleteNode(root.right, harga);
 
        // if key is same as root's key, then this is the node
        // to be deleted
        else
        {

            if(root.count>1){
                root.count--;
                return null;
            }

            // node with only one child or no child
            if ((root.left == null) || (root.right == null))
            {
                Permen temp = null;
                if (temp == root.left)
                    temp = root.right;
                else
                    temp = root.left;
 
                // No child case
                if (temp == null)
                {
                    temp = root;
                    root = null;
                }
                else // One child case
                    root = temp; // Copy the contents of
                                // the non-empty child
            }
            else
            {
 
                // node with two children: Get the inorder
                // successor (smallest in the right subtree)
                Permen temp = minValueNode(root.right);
 
                // Copy the inorder successor's data to this node
                root.harga = temp.harga;
 
                // Delete the inorder successor
                root.right = deleteNode(root.right, temp.harga);
            }
        }
 
        // If the tree had only one node then return
        if (root == null)
            return root;
 
        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        root.height = max(height(root.left), height(root.right)) + 1;
 
        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        int balance = getBalance(root);
 
        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);
 
        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0)
        {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
 
        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);
 
        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0)
        {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
 
        return root;
    }

    

    public void beli(int harga1, int harga2){
        intArray.clear();
        floorCeilBST(root, harga1);
        floorCeilBST(root, harga2);
    }

    static int floor = 0;
    static int ceil = 0;
    

    public void floorCeilBSTHelper(Permen root, int key)
    {
        while (root != null)
        {
            if (root.harga == key)
            {
                ceil = root.harga;
                floor = root.harga;
                return;
            }
    
            if (key > root.harga)
            {
                floor = root.harga;
                root = root.right;
            }
            else
            {
                ceil = root.harga;
                root = root.left;
            }
        }
        return;
    }           
 
        // Display the floor and ceil of a
        // given key in BST. If key is less
        // than the min key in BST, floor
        // will be -1; If key is more than
        // the max key in BST, ceil will be -1;
        public void floorCeilBST(Permen root, int key)
        {
            
            // Variables 'floor' and 'ceil'
            // are passed by reference
            floor = -1;
            ceil = -1;
         
            floorCeilBSTHelper(root, key);
            intArray.add(ceil);
            intArray.add(floor);



        
        }
        

        
}

   
    
       
    public static void main(String[] args) {
       
        //Menginisialisasi kotak sebanyak N
        int N = in.nextInt();
        for(int i = 0; i < N; i++){
            String nama = in.next();
            int harga = in.nextInt();
            int tipe = in.nextInt();
            handleStock(nama, harga, tipe);
        }

        //Query 
        //(method dan argumennya boleh diatur sendiri, sesuai kebutuhan)
        int NQ = in.nextInt();
        for(int i = 0; i < NQ; i++){
            String Q = in.next();
            if (Q.equals("BELI")){
                int L = in.nextInt();
                int R = in.nextInt();
                out.println(handleBeli(L, R));

            }else if(Q.equals("STOCK")){
                String nama = in.next();
                int harga = in.nextInt();
                int tipe = in.nextInt();
                handleStock(nama, harga, tipe);

            }else{ //SOLD_OUT
                String nama = in.next();
                handleSoldOut(nama);

            }
        }

        out.flush();
    }

    //TODO
    static String handleBeli(int L, int R){
        tree.beli(L, R);

        int floor = tree.intArray.get(0);
        int ceil = tree.intArray.get(3);

         if (count > 1){
             return "-1 -1";
         }

        if (floor == ceil && floor != 1 && countHarga.get(floor) == 1){
            return "-1 -1";
        }

        if (floor > ceil){
            return "-1 -1";
        }

        return floor + " " + ceil;
    }

    //TODO
    static void handleStock(String nama, int harga, int tipe){
        AVLTree tree = new AVLTree();
        countHarga.put(harga, countHarga.getOrDefault(harga, 0)+1);

        hargaPermen.put(nama, harga);
        tipePermen.put(nama, harga);

        if (countHarga.get(harga) == 1){
            tree.root = tree.insert(tree.root, harga);
        }
    }

    //TODO
    static void handleSoldOut(String nama){
        AVLTree tree = new AVLTree();

        countHarga.put(hargaPermen.get(nama),countHarga.get(hargaPermen.get(nama))-1);

        if (countHarga.get(hargaPermen.get(nama)) == 0){
            tree.root = tree.deleteNode(tree.root, hargaPermen.get(nama));
        }
    }


    // taken from https://codeforces.com/submissions/Petr
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

/* Sumber:
https://www.geeksforgeeks.org/avl-with-duplicate-keys/
https://www.geeksforgeeks.org/avl-tree-set-2-deletion/?ref=lbp
https://www.geeksforgeeks.org/floor-and-ceil-from-a-bst/
https://www.geeksforgeeks.org/avl-tree-set-1-insertion/?ref=lbp
 */