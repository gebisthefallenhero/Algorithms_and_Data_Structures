import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FindWords {
    /**
     *  Prints all words edit distance 1 away, and if any word edit distance less than 2 is found while less than
     *  ten words are printed it prints them as well.
     * @param dictionary An arraylist of strings to check the edit distance against for a give word.
     * @param word The mispelled string to check the edit distance against.
     */
    public static void possibleWords(ArrayList<String> dictionary, String word){
        int count = 0;
        final int MAX_WORDS_TO_PRINT = 10;
        final int DESIRED_EDIT_DISTANCE1 = 1;
        final int DESIRED_EDIT_DISTANCE2 = 2;
        for (String dictW: dictionary){
            int editDistance = MinDistance.minDistance(word,dictW);
            if (editDistance == DESIRED_EDIT_DISTANCE1){
                System.out.print(dictW + "(" + editDistance + ") ");
                count++;
            }
            if (editDistance == DESIRED_EDIT_DISTANCE2 && count < MAX_WORDS_TO_PRINT){
                System.out.print(dictW + "(" +  editDistance + ") ");
                count++;
            }
        }
    }

    /**
     * This function checks to see if a mispelled word has been seen before. If not, it prints the possible words
     * and if it has been before it updates the number of times the misspelled word has been found.
     * @param word The current string to check against
     * @param freqTable The hash table containg already found words that have been misspelledl
     * @param dictionary An arraylist of words to pass into the possible words function to check for edit distance.
     */
    public static void printWordsAndTable(String word, HashTable<HashTable.WordFreq> freqTable, ArrayList<String> dictionary){
        HashTable.WordFreq newWord = new HashTable.WordFreq(word);
        boolean isInTable = freqTable.contains(newWord);
        if (!isInTable){
            freqTable.insert(newWord);
            freqTable.insertsRequested++;
            System.out.println("Found misspelled word " + word);
            possibleWords(dictionary,word);
            System.out.println();
        }
        else {
            freqTable.find(newWord).count++;
        }
    }

    /**
     * This function takes in an arraylist and a hash table and puts the words in them needed to make a dictionary.
     * @param dictionary An arraylist of the words that will be useful for finding edit distance
     * @param dictHash A hashtable that will be used to check if a certain word is misspelled
     */
    public static void updateDictionary(ArrayList<String> dictionary, HashTable dictHash){
        try{
            File dictionaryFile = new File("dictionary.txt");
            Scanner myReader = new Scanner(dictionaryFile);
            while (myReader.hasNextLine()){
                String line = myReader.nextLine();
                line = line.toLowerCase().replaceAll("\\p{Punct}", "");
                String[] words = line.split(" ");
                for (String w: words){
                    dictionary.add(w);
                    dictHash.insert(w);
                    dictHash.insertsRequested++;
                }
            }
        }
        catch (
                FileNotFoundException e){
            System.out.print("Incorrect File Name");
            e.printStackTrace();
        }
    }

    /**
     * This function takes a file or files and prints suggestions for possible misspelled words, as well as their 
     * frequency, and the statistics for the dictionary and hash table used.
     * @param fileNames A list of text files to check
     * @param dictionary An arraylist of words used to find suggestions for misspelled words.
     * @param dictHash A hashtable of words to check if a word is misspelled.
     */
    public static void printMisspelledWords(String[] fileNames, ArrayList<String> dictionary, HashTable dictHash){
        for (String file: fileNames){
            try{
                System.out.println(file + "\n");
                File paragraphFile = new File(file);
                Scanner myReader = new Scanner(paragraphFile);
                HashTable<HashTable.WordFreq> freqHash = new HashTable<>();
                while (myReader.hasNextLine()){
                    String line = myReader.nextLine();
                    line = line.toLowerCase().replaceAll("\\p{Punct}", "");
                    String[] words = line.split(" ");
                    for (String word: words){
                        Boolean isSpelledCorrectly = (dictHash.contains(word));
                        int compareEmpty = word.compareTo("");
                        if (!isSpelledCorrectly && compareEmpty != 0){
                            printWordsAndTable(word,freqHash,dictionary);
                        }
                    }
                }
                System.out.println("The Hash Table for " + file);
                System.out.print(freqHash.toString(200));
            }
            catch (FileNotFoundException e){
                System.out.print("Incorrect File Name");
                e.printStackTrace();
            }
        }
        System.out.println("The Stats for the Dictionary");
        dictHash.printStats();
    }

}
