public class EdgeInfo {

    public EdgeInfo(int from, int to, int capacity,int cost){
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.cost = cost;

    }
    public String toString(){
        return "Edge " + from + "->" + to + " ("+ capacity + ", " + cost + ") " ;
    }

    int from;        // source of edge
    int to;          // destination of edge
    int capacity;    // capacity of edge
    int cost;        // cost of edge


}
