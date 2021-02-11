public class Tree<E extends Comparable<? super E>> {
    public BinaryNode<E> root;  // Root of tree
    public int size; //The number of nodes in the tree.

    /**
     * Create an empty tree
     */
    public Tree() {
        root = null;
    }
    

    /**
     *  Swaps the children of a node.
     * @param node The binary node to have the children swaped with.
     */
    public static void swapChildren(BinaryNode node){
        if (node == null){
            return;
        }
        BinaryNode tempNode = node.left;
        node.left = node.right;
        node.right = tempNode;
    }

    public static class BinaryNode<AnyType extends Comparable>{
        AnyType element;            // The data in the node
        BinaryNode<AnyType> left;   // Left child
        BinaryNode<AnyType> right;  // Right child
        public int npl; //The null path length of the node

        // Constructors
        BinaryNode(AnyType theElement) {
            this(theElement, null, null, null);
        }

        BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt, BinaryNode<AnyType> pt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        /**
         * method to compare two binary nodes
         * @param node the node to be compared to
         * @return An integer same as regular compare element.
         */
        public int compareTo(BinaryNode node){
            return this.element.compareTo(node.element);
        }
    }
}
