import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Rivals{
    UnionFind groups; //The union find that keeps track of the class
    BattleList[] battleLists; //A list containing the battlelist objects that say who the node last attacked and defended against.


    /**
     * Constructor the the rivals object
     * @param numVoters The number of voters being considered.
     */
    public Rivals(int numVoters){
        groups = new UnionFind(numVoters);
        battleLists = new BattleList[numVoters];
        for (int i = 0; i < numVoters; i++){
            battleLists[i] = new BattleList();
        }
    }

    /**
     * Perform and attack from one index to another, performing the approriate unions if necessary.
     * @param attacker The index attacking
     * @param defender The index defending
     * @return True if the attack happened, false if the attack didn't.
     */
    public boolean attack(int attacker, int defender){
        if (groups.isSameGoup(attacker,defender)){
            return false;
        }
        Integer aLastAttacked = battleLists[attacker].attack;
        Integer dLastDefended = battleLists[defender].defend;
        if (aLastAttacked != null){
            groups.union(aLastAttacked, defender);
        }
        if (dLastDefended != null){
            groups.union(dLastDefended, attacker);
        }
        battleLists[attacker].attack = defender;
        battleLists[defender].defend = attacker;
        Integer aLastDefended = battleLists[attacker].defend;
        Integer dLastAttacked = battleLists[defender].attack;
        if (aLastDefended != null){
            groups.union(defender,aLastDefended);
        }
        if (dLastAttacked != null){
            groups.union(attacker, dLastAttacked);
        }
        return true;
    }

    /**
     * A method to print how many members are in each group and the size of the largest group.
     */
    public void printGroups(){
        final int FIX_INDEXING = 1;
        int curr_largest = groups.unionList[0];
        for (int val = 0; val < groups.unionList.length; val ++){
            int valSize = groups.unionList[val];
            if (valSize < 0){
                //Since the group size is stored by a negative number we return the negative of it.
                System.out.println("Group " + (val + FIX_INDEXING) + " has " + -valSize + " members");
            }
            if (valSize < curr_largest){
                curr_largest = valSize;
            }
        }
        System.out.println("The largest group is of size " + -curr_largest);
    }

    /**
     * A method that reads a file and uses the attack information to separate the groups.
     * @param file THe file to be read in.
     */
    public static void readCase(String file){
        System.out.println("Reading " + file);
        final int ATTACKER_POS = 0;
        final int DEFENDER_POS = 1;
        final int FIX_INDEXING = 1; //This is to fix the fact that the lists are indexed at 1 and not 0
        try {
            File currCase = new File(file);
            Scanner myReader = new Scanner(currCase);
            String firstLine = myReader.nextLine();
            int numVoters = Integer.parseInt(firstLine);
            Rivals rivals = new Rivals(numVoters);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                //Remove any punctuation if there is any
                line = line.replaceAll("\\p{Punct}", "");
                String[] lineValues = line.split(" ");
                if (lineValues[0].equals("")){
                    continue;
                }
                //Take care of the cases where there are extra spaces.
                for (int i=0; i < lineValues.length; i++){
                    if (lineValues[i].equals("") && i != lineValues.length - 1){
                        lineValues[i] = lineValues[i + 1];
                    }

                }
                int attacker = Integer.parseInt(lineValues[ATTACKER_POS]);
                int defender = Integer.parseInt(lineValues[DEFENDER_POS]);
                System.out.println("Attack " + attacker + " " + defender);
                boolean didAttack = rivals.attack(attacker - FIX_INDEXING,defender - FIX_INDEXING);
                if (!didAttack){
                    System.out.println("Ignored Attack " + attacker + " " + defender);
                }
            }
            rivals.printGroups();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class BattleList{
        Integer attack; // The last person attacked
        Integer defend; //The last person defended against

        @Override
        public String toString(){
            return attack + " " + defend;
        }
    }

    public static void main(String[] args) {
        String[] fileNames = {"case1.txt", "case2.txt", "case3.txt", "case4.txt", "case5.txt", "case6.txt", "case7.txt"};
        for (String file : fileNames){
            readCase(file);
        }
    }
}