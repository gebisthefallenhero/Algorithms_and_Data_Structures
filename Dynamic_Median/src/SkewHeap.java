// A skew heap implemented as min heap
public class SkewHeap<E extends Comparable> extends Tree {

    /**
     * Create an empty skew heap
     */
    SkewHeap() {
        root = null;
    }

    /**
     * Create a heap with an initial value in the root
     *
     * @param value The value to place at the inital root
     */
    SkewHeap(E value) {
        root = new BinaryNode(value);
    }


    /**
     * A method to remove and return the least element
     *
     * @return Returns the least element of the heap.
     */
    public E deleteMin() {
        if (root == null) {
            return null;
        }
        E minElement = (E) root.element;
        root = merge(root.left, root.right);
        size--;
        return minElement;
    }

    /**
     * Merges two min heaps recursively
     *
     * @param node1 The first node to be merged.
     * @param node2 The second node to be merged.
     * @return The root of the newly merged heaps.
     */
    private BinaryNode merge(BinaryNode node1, BinaryNode node2) {
        if (node1 == null) {
            return node2;
        }
        if (node2 == null) {
            return node1;
        }
        int compareNode = node1.compareTo(node2);
        if (compareNode < 0) {
            node1.right = merge(node1.right, node2);
            swapChildren(node1);
            return node1;

        }
        //This is the case the 2nd node is smaller
        else {
            node2.right = merge(node2.right, node1);
            swapChildren(node2);
            return node2;
        }
    }

    /**
     * Inserts an element into the heap
     *
     * @param x The element to be inserted.
     */
    public void insert(E x) {
        BinaryNode toInsert = new BinaryNode(x);
        root = merge(toInsert, root);
        if (x != null){size++;}
    }

}