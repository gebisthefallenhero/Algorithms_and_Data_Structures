
// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void makeEmpty( )      --> Remove all items


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Cuckoo Hashing table
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss and Manuel Santana
 */
public class HashTable<E extends Comparable> {
    /**
     * Construct the hash table.
     */
    public HashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    /**
     * Construct the hash table.
     *
     * @param size the approximate initial size.
     */
    public HashTable(int size) {
        allocateArray(size);
        doClear();
    }

    /**
     * Insert into the hash table.
     * Check before to see if the hashtable is already empty.
     * @param x the item to insert.
     */

    void insert(E x) {
        final int MAX_LOOP_ITERATIONS = 9;
        for (int i = 0; i < MAX_LOOP_ITERATIONS; i++) {
            int loc1 = hash1(x);
            if (array1[loc1] == null) {
                array1[loc1] = new HashEntry(x);
                actualInserts++;
                numTable1Entries++;
                return;
            }
            HashEntry y = array1[loc1];
            array1[loc1] = new HashEntry<>(x);
            actualInserts++;
            int loc2 = hash2((E) y);
            if (array2[loc2] == null) {
                array2[loc2] = y;
                actualInserts++;
                numTable2Entries++;
                return;
            }
            x = (E) array2[loc2];
            array2[loc2] = y;
            actualInserts++;
        }
        rehash();
        insert(x);
        actualInserts++;
    }

    /**
     * Prints the interesting stats of the hash table without printing the actual table.
     */
    public void printStats(){
        System.out.println("The number of requested inserts was " + insertsRequested);
        System.out.print("The number of acutal inserts was " + actualInserts + "\n");
        float ratio = (float)actualInserts / (float)insertsRequested;
        String insertRatio = String.format(" The ratio of actual to requested inserts was %.2f", ratio);
        System.out.println(insertRatio);
        System.out.print("Rehash count " + rehashCount + "\n");
        System.out.print("Table 1 (" + numTable1Entries + "/" + array1.length + ") \n");
        System.out.print("Table 2 (" + numTable2Entries + "/" + array2.length + ") \n");
    }

    /**
     * Makes a string containing the table and interesting facts about it.
     * @param limit: How many values of the table to print
     * @return The string of the table and interesting facts about it.
     */
    public String toString(int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("The number of requested inserts was " + insertsRequested);
        sb.append("\n The number of acutal inserts was " + actualInserts + "\n");
        float ratio = (float)actualInserts / (float)insertsRequested;
        String insertRatio = String.format(" The ratio of actual to requested inserts was %.2f", ratio);
        sb.append(insertRatio + " \n");
        sb.append("Rehash Count = " + rehashCount + " \n");
        int ct = 0;
        sb.append(" Table 1 (" + numTable1Entries + "/" + array1.length + ") \n");
        for (int i = 0; i < array1.length && ct < limit; i++) {
            if (array1[i] != null && array1[i].isActive) {
                sb.append( i + ":" +  array1[i].element + "\n");
                ct++;
            }
        }
        sb.append(" Table 2 (" + numTable2Entries + "/" + array2.length + ") \n");
        for (int i = 0; i < array2.length && ct < limit; i++) {
            if (array2[i] != null && array2[i].isActive) {
                sb.append(i + ":" + array2[i].element + "\n");
                ct++;
            }
        }
        return sb.toString();
    }

    /**
     * Expand the hash table.
     */
    private void rehash() {
        rehashCount++;
        HashEntry<E>[] oldArray1 = array1;
        HashEntry<E>[] oldArray2 = array2;

        // Create a new double-sized, empty table
        allocateArray(2 * oldArray1.length);
        numTable1Entries = 0;
        numTable2Entries = 0;
        // Copy table over
        for (HashEntry<E> entry : oldArray1) {
            if (entry != null) {
                insert(entry.element);
            }
        }
        for (HashEntry<E> entry : oldArray2) {
            if (entry != null) {
                insert(entry.element);
            }
        }
    }


    /**
     * Remove from the hash table.
     *
     * @param x the item to remove.
     * @return true if item removed
     */
    public boolean remove(E x) {
        int hashArray1 = hash1(x);
        int hashArray2 = hash2(x);
        HashEntry<E> elem1 = array1[hashArray1];
        HashEntry<E> elem2 = array2[hashArray2];
        if (elem2 == null && elem1 == null) {
            return true;
        }
        if (elem1 != null && elem2 != null) {
            if (elem1.compareTo(x) == 0) {
                array1[hashArray1] = null;
                numTable1Entries--;
                return true;
            }
            if (elem2.compareTo(x) == 0) {
                array2[hashArray2] = null;
                numTable2Entries--;
                return true;
            }
        }
        if (elem1 == null && elem2 != null) {
            if (elem2.compareTo(x) == 0) {
                array2[hashArray2] = null;
                numTable2Entries--;
                return true;
            }
        }
        if (elem1 != null && elem2 == null) {
            if (elem1.compareTo(x) == 0) {
                array1[hashArray1] = null;
                numTable1Entries--;
                return true;
            }

        }
        return false;
    }

    /**
     * Get current size.
     *
     * @return the size.
     */
    public int size() {
        return numTable1Entries + numTable2Entries;
}

    /**
     * Get length of both tables combined
     *
     * @return the size.
     */
    public int capacity() {
        return array1.length + array2.length;
    }


    public boolean contains(E x) {
        return (find(x) != null);
    }

    /**
     * Find an item in the hash table.
     *
     * @param x the item to search for.
     * @return the matching item or null if the item is not in the tree
     */
    public E find(E x) {
        int hashArray1 = hash1(x);
        int hashArray2 = hash2(x);
        HashEntry<E> elem1 = array1[hashArray1];
        HashEntry<E> elem2 = array2[hashArray2];
        if (elem2 == null && elem1 == null) {
            return null;
        }
        if (elem1 != null && elem2 != null) {
            if (elem1.element.compareTo(x) == 0) {
                return elem1.element;
            }
            if (elem2.element.compareTo(x) == 0) {
                return elem2.element;
            }
        }
        if (elem1 == null && elem2 != null) {
            if (elem2.element.compareTo(x) == 0) {
                return elem2.element;
            }
        }
        if (elem1 != null && elem2 == null) {
            if (elem1.element.compareTo(x) == 0) {
                return elem1.element;
            }
        }
        return null;
    }


    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty() {
        doClear();
    }

    private void doClear() {
        for (int i = 0; i < array1.length; i++)
            array1[i] = null;
        for (int i = 0; i < array2.length; i++)
            array2[i] = null;
    }

    /**
     *
     * @param x An object that is hashable
     * @return Returns the hash value based on table 1
     */
    private int hash1(E x) {
        int hashVal = x.hashCode();

        hashVal %= array1.length;
        if (hashVal < 0)
            hashVal += array1.length;

        return hashVal;
    }
    /**
     *
     * @param x An object that is hashable
     * @return Returns the hash value based on table 2
     */
    private int hash2(E x) {
        int hashVal = x.hashCode();

        hashVal %= array2.length;
        if (hashVal < 0)
            hashVal += array2.length;

        return hashVal;
    }

    private static class HashEntry<E extends Comparable> implements Comparable<E> {
        public E element;   // the element
        public boolean isActive;  // false if marked deleted

        public HashEntry(E e) {
            this(e, true);
        }

        public HashEntry(E e, boolean i) {
            element = e;
            isActive = i;
        }

        @Override
        public int hashCode(){
            return this.element.hashCode();
        }
        @Override
        public int compareTo(E e) {
            return element.compareTo(e);
        }

    }

    private static final int DEFAULT_TABLE_SIZE = 101;

    private HashEntry[] array1; // The array of elements
    private HashEntry[] array2;
    private int numTable1Entries;
    private int numTable2Entries;
    public int insertsRequested;
    private int actualInserts;
    private int rehashCount = 0; // Starts at zero since there are no inserts.

    /**
     * Internal method to allocate array.
     *
     * @param arraySize the size of the array.
     */
    private void allocateArray(int arraySize) {
        array1 = new HashEntry[nextPrime(arraySize * 2)];
        array2 = new HashEntry[nextPrime(arraySize)];

    }

    /**
     * Internal method to find a prime number at least as large as n.
     *
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private static int nextPrime(int n) {
        if (n % 2 == 0)
            n++;

        for (; !isPrime(n); n += 2)
            ;

        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     *
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime(int n) {
        if (n == 2 || n == 3)
            return true;

        if (n == 1 || n % 2 == 0)
            return false;

        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0)
                return false;

        return true;
    }


      static class WordFreq implements Comparable<WordFreq> {
        String word; // This holds the word
        int count; // This holds how many times the word has been found

        public WordFreq() {
        }

        public WordFreq(String word) {
            this.word = word;
            count = 1;
        }

        public void addCount() {
            this.count++;
        }

        @Override
        public String toString() {
            String information = this.word + " " + this.count;
            return information;
        }

        @Override
        public int hashCode() {
            return this.word.hashCode();
        }

        @Override
        public int compareTo(WordFreq o) {
            return this.word.compareTo(o.word);
        }

    }


    public static void main(String[] args) {
        ArrayList<String> dictionary = new ArrayList<>();
        HashTable<String> dictHash = new HashTable();
        FindWords.updateDictionary(dictionary,dictHash);
        String[] fileNames = {"p.txt", "paragraph1.txt", "paragraph2.txt", "paragraph3.txt"};
        FindWords.printMisspelledWords(fileNames,dictionary,dictHash);
    }
}

