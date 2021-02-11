public class UnionFind{
    int [] unionList; // The list containing the union find information

    /**
     * Constructor for UnionFind object
     * @param numEntries How many entries are in the set.
     */
    public UnionFind(int numEntries){
        final int INTIALIZE_ROOT = -1;
        unionList = new int[numEntries];
        for (int i = 0; i < numEntries; i++){
            unionList[i] = INTIALIZE_ROOT;
        }
    }

    /**
     * Method to find the root of the given nodes group.
     * @param location the location of node in question in the array
     * @return the location of the parent.
     */
    public int find(int location){
        int parent = unionList[location];
        if (parent < 0){
            return location;
        }
        int root  = find(parent);
        unionList[location] = root;
        return  root;
    }

    /**
     * A method to determine if two indexes are in the same group.
     * @param location1 The first index to be considered.
     * @param location2 The second index to be considered
     * @return True if they are in the same group. False if not.
     */
    public boolean isSameGoup(int location1, int location2){
        return find(location1) == find(location2);
    }

    /**
     * Union by size two nodes
     * @param val1 the location of the first value
     * @param val2 the location of the second value
     */
    public void union(int val1, int val2){
        int rootVal1 = find(val1);
        int rootVal2 = find(val2);
        if (rootVal1 == rootVal2){
            return;
        }
        int size1 = unionList[rootVal1];
        int size2 = unionList[rootVal2];
        //The case where val1 is from the bigger tree
        if (size1 < size2){
            unionList[rootVal1] = size1 + size2;
            unionList[rootVal2] = rootVal1;
        }
        else{
            unionList[rootVal2] = size1 + size2;
            unionList[rootVal1] = rootVal2;
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int val : unionList){
            sb.append(val + " ");
        }
        return sb.toString();
    }

}