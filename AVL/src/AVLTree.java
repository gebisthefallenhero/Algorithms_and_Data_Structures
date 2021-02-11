import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean contains( x )  --> Return true if x is present
// boolean remove( x )    --> Return true if x was present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 *
 * @author Mark Allen Weiss
 */
public class AVLTree<AnyType extends Comparable<? super AnyType>> {
    /**
     * Construct the tree.
     */
    public AVLTree() {
        root = null;
        prev = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     *
     * @param x the item to insert.
     */
    public void insert(AnyType x) {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param x the item to remove.
     */
    public void remove(AnyType x) {
        root = remove(x, root);
    }


    /**
     * Internal method to remove from a subtree.
     *
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> remove(AnyType x, AvlNode<AnyType> t) {
        if (t == null)
            return t;   // Item not found; do nothing

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            t.left = remove(x, t.left);
        else if (compareResult > 0)
            t.right = remove(x, t.right);
        else if (t.left != null && t.right != null) // Two children
        {
            t.element = findMin(t.right).element;
            t.right = remove(t.element, t.right);
        } else
            t = (t.left != null) ? t.left : t.right;
        return balance(t);
    }

    /**
     * Find the smallest item in the tree.
     *
     * @return smallest item or null if empty.
     */
    public AnyType findMin() {
        if (isEmpty())
            throw new RuntimeException();
        return findMin(root).element;
    }

    public void deleteMin() {

        root = deleteMin(root);
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item of null if empty.
     */
    public AnyType findMax() {
        if (isEmpty())
            throw new RuntimeException();
        return findMax(root).element;
    }

    /**
     * Find an item in the tree.
     *
     * @param x the item to search for.
     * @return true if x is found.
     */
    public AnyType contains(AnyType x) {
        return contains(x, root);
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree(String label) {
        System.out.println(label);
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root, "");
    }

    private static final int ALLOWED_IMBALANCE = 1;

    // Assume t is either balanced or within one of being balanced
    private AvlNode<AnyType> balance(AvlNode<AnyType> t) {
        if (t == null)
            return t;

        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE)
            if (height(t.left.left) >= height(t.left.right))
                t = rightRotation(t);
            else
                t = doubleRightRotation(t);
        else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE)
            if (height(t.right.right) >= height(t.right.left))
                t = leftRotation(t);
            else
                t = doubleLeftRotation(t);

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(AvlNode<AnyType> t) {
        if (t == null)
            return -1;

        int hl = checkBalance(t.left);
        int hr = checkBalance(t.right);
        if (Math.abs(height(t.left) - height(t.right)) > 1 ||
            height(t.left) != hl || height(t.right) != hr)
            System.out.println("\n\n***********************OOPS!!");


        return height(t);
    }


    /**
     * Internal method to insert into a subtree.  Duplicates are allowed
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> insert(AnyType x, AvlNode<AnyType> t) {
        if (t == null)
            return new AvlNode<>(x, null, null);

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            t.left = insert(x, t.left);
        else
            t.right = insert(x, t.right);

        return balance(t);
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<AnyType> findMin(AvlNode<AnyType> t) {
        if (t == null)
            return t;

        while (t.left != null)
            t = t.left;
        return t;
    }

    private AvlNode<AnyType> deleteMin(AvlNode<AnyType> t) {
        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<AnyType> findMax(AvlNode<AnyType> t) {
        if (t == null)
            return t;

        while (t.right != null)
            t = t.right;
        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private AnyType contains(AnyType x, AvlNode<AnyType> t) {
        while (t != null) {
            int compareResult = x.compareTo(t.element);

            if (compareResult < 0)
                t = t.left;
            else if (compareResult > 0)
                t = t.right;
            else
                return t.element;    // Match
        }

        return null;   // No match
    }

    AvlNode<AnyType> findNext(AvlNode<AnyType> p) {
        System.out.println("findNext " + p.element);
        AvlNode<AnyType> curr = root;
        AvlNode<AnyType> succ = null;
        while (curr != null) {
            if (p.element.compareTo(curr.element) < 0) {
                succ = curr;
                curr = curr.left;
                System.out.println("findNext go Left succ  " + succ.element);
            } else if (p.element.compareTo(curr.element) == 0) {
                System.out.println("findNext found succ  " + succ.element);
                return succ;
            } else {
                System.out.println("findNext goRight succ  " );
                curr = curr.right;
            }


        }
        return null;
    }


    AnyType getNext() {
        if (prev == null)
            prev = findMin(root);
        else if (prev.right != null) {
            prev = findMin(prev.right);
        } else prev = findNext(prev);
        if (prev !=null)
        return (AnyType) prev.element;
        return null;
    }


    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param t the node that roots the tree.
     */
    private void printTree(AvlNode<AnyType> t, String indent) {
        if (t != null) {
            printTree(t.right, indent + "   ");
            System.out.println(indent + t.element );
            printTree(t.left, indent + "   ");
        }
    }

    /**
     * Internal method to print a subtree in sorted order.
     */
    public String toString() {
        return toString("  ", root);
    }

    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param t the node that roots the tree.
     */

    private String toString(String Spacing, AvlNode node) {
        if (node == null) {
            return "";
        } else {
            StringBuilder toPrint = new StringBuilder();
            toPrint.append(toString(Spacing + "\t",node.right));
            toPrint.append(Spacing);
            toPrint.append(node.element);
            toPrint.append("\n");
            toPrint.append(toString(Spacing + "\t",node.left));
            return toPrint.toString();
        }
    }
//    private String toString(AvlNode<AnyType> t) {
//        StringBuilder sb = new StringBuilder();
//
//        if (t != null) {
//            sb.append(toString(t.left));
//
//            sb.append(t.element.toString());
//            sb.append(toString(t.right));
//        }
//        return sb.toString();
//    }
//
//    public int height() {
//        return height(root);
//    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height(AvlNode<AnyType> t) {
        if (t == null) return -1;
        return t.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rightRotation(AvlNode<AnyType> t) {
        AvlNode<AnyType> theLeft = t.left;
        t.left = theLeft.right;
        theLeft.right = t;
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        theLeft.height = Math.max(height(theLeft.left), t.height) + 1;
        return theLeft;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> leftRotation(AvlNode<AnyType> t) {
        AvlNode<AnyType> theRight = t.right;
        t.right = theRight.left;
        theRight.left = t;
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        theRight.height = Math.max(height(theRight.right), t.height) + 1;
        return theRight;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleRightRotation(AvlNode<AnyType> t) {
        t.left = leftRotation(t.left);
        return rightRotation(t);

    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleLeftRotation(AvlNode<AnyType> t) {
        t.right = rightRotation(t.right);
        return leftRotation(t);
    }

    private static class AvlNode<AnyType> {
        // Constructors
        AvlNode(AnyType theElement) {
            this(theElement, null, null);
        }

        AvlNode(AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt) {
            element = theElement;
            left = lt;
            right = rt;
            height = 0;
        }

        AnyType element;      // The data in the node
        AvlNode<AnyType> left;         // Left child
        AvlNode<AnyType> right;        // Right child
        int height;       // Height
    }

    /**
     * The tree root.
     */
    private AvlNode<AnyType> root;
    private AvlNode prev;

    //Class to track the words and their frequency
    static class WordFreq implements Comparable<WordFreq> {
        String word; // This holds the word
        int count; // This holds how many times the word has been found

        public WordFreq(){}

        public WordFreq(String word){
            this.word = word;
            count = 1;
        }

        public void addCount(){
            this.count++;
        }

        @Override
        public String toString(){
            String information = this.word + " " + this.count;
            return information;
        }

        @Override
        public int compareTo(WordFreq o) {
            return this.word.compareTo(o.word);
        }
    }

    public static void possibleWords(ArrayList<String> dictionary, String word){
        int count = 0;
        for (String dictW: dictionary){
            int editDistance = MinDistance.minDistance(word,dictW);
            if (editDistance == 1){
                System.out.print(dictW + "(" + editDistance + ") ");
                count++;
            }
            if (editDistance == 2 && count < 10){
                System.out.print(dictW + "(" +  editDistance + ") ");
                count++;
            }
        }
    }

    public static void printWordsAndTree(String word, AVLTree<WordFreq> freqTree, ArrayList<String> dictionary){
        WordFreq newWord = new WordFreq(word);
        WordFreq checkInTree = freqTree.contains(newWord);
        if (checkInTree == null){
            freqTree.insert(newWord);
            System.out.println("Found misspelled word " + word);
            possibleWords(dictionary,word);
            System.out.println();
        }
        else {
            checkInTree.count++;
        }
    }
    // Test program
    public static void main(String[] args) {
        ArrayList<String> dictionary = new ArrayList<>();
        AVLTree<String> dictTree = new AVLTree();
        try{
            File dictionaryFile = new File("dictionary.txt");
            Scanner myReader = new Scanner(dictionaryFile);
            while (myReader.hasNextLine()){
                String line = myReader.nextLine();
                line = line.toLowerCase().replaceAll("\\p{Punct}", "");
                String[] words = line.split(" ");
                for (String w: words){
                    dictionary.add(w);
                    dictTree.insert(w);
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.print("Incorrect File Name");
            e.printStackTrace();
        }

        //Now iterate through the files
        String[] fileNames = {"p.txt", "paragraph1.txt", "paragraph2.txt", "paragraph3.txt"};
        for (String file: fileNames){
            try{
                System.out.println(file + "\n");
                File paragraphFile = new File(file);
                Scanner myReader = new Scanner(paragraphFile);
                AVLTree<WordFreq> freqTree = new AVLTree<>();
                while (myReader.hasNextLine()){
                    String line = myReader.nextLine();
                    line = line.toLowerCase().replaceAll("\\p{Punct}", "");
                    String[] words = line.split(" ");
                    for (String word: words){
                        Boolean isSpelledCorrectly = (dictTree.contains(word) != null);
                        int compareEmpty = word.compareTo("");
                        if (!isSpelledCorrectly && compareEmpty != 0){
                            printWordsAndTree(word,freqTree,dictionary);
                        }
                    }
                }
                System.out.print(freqTree.toString());
            }
            catch (FileNotFoundException e){
                System.out.print("Incorrect File Name");
                e.printStackTrace();
            }
        }
    }

}
