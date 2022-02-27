import java.util.ArrayList;

public class Node<E> {

    private E Data;
    private ArrayList<Node<E>> Child;

    public Node(E Data) {
        this.Data = Data;
        this.Child = new ArrayList<Node<E>>();
    }
    public E getData() { return this.Data; }
    public ArrayList<Node<E>> getChild() { return this.Child; }
}