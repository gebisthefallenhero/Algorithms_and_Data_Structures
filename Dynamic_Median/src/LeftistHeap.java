// A leftist heap implemented as a max heap
public class LeftistHeap<E extends Comparable> extends Tree{

    /**
     * Create an empty leftist heap
     */
    LeftistHeap(){
        root = null;
    }

    /**
     * Create a heap with an initial value in the root
     * @param value
     */
    LeftistHeap(E value){
        root = new BinaryNode(value);
    }

    /**
     * Returns the null path length of a node or null reference
     * @param node The node to get the null path length from
     * @return An interger of the null path length
     */
    public static int getNpl(BinaryNode node){
        if (node == null){
            return -1;
        }
        else{
            return node.npl;
        }
    }

    /**
     * A method to remove and return the greatest element
     * @return Returns the greatest element of the heap.
     */
    public E deleteMax(){
        if (root == null){
            return null;
        }
        E maxElement = (E) root.element;
        size--;
        root = merge(root.left,root.right);
        return maxElement;
    }


    /**
     * Merges two max heaps recursively
     * @param node1 The first node to be merged.
     * @param node2 The second node to be merged.
     * @return The root of the newly merged heaps.
     */
    private BinaryNode  merge(BinaryNode node1, BinaryNode node2){
        if (node1 == null){
            return node2;
        }
        if (node2 == null){
            return node1;
        }
        final int UPDATE_NPL = 1;
        int compareNode = node1.compareTo(node2);
        if (compareNode > 0){
            node1.right = merge(node1.right,node2);
            if (getNpl(node1.right) > getNpl(node1.left)){
               swapChildren(node1);
            }
            //Update the null path length as we go along
            node1.npl = getNpl(node1.right) + UPDATE_NPL;
            return node1;

        }
        //This is the case the 2nd node is larger
       else{
            node2.right = merge(node2.right,node1);
            if (getNpl(node2.right) > getNpl(node2.left)){
                swapChildren(node2);
            }
            node2.npl = getNpl(node2.right) + UPDATE_NPL;
            return node2;
        }
    }

    /**
     * Inserts an element into the heap
     * @param x The element to be inserted.
     */
    public void insert(E x){
        BinaryNode toInsert = new BinaryNode(x);
        root = merge(toInsert,root);
        size++;
    }
}