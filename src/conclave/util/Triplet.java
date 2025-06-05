package conclave.util;

public class Triplet<E extends String, V extends Integer> implements Comparable<Triplet<?, V>> {
    public E name;
    public E surname;
    public V votes;

    public Triplet(E first, E second, V value) {
        name = first;
        surname = second;
        votes = value;
    }

    @Override
    public int compareTo(Triplet o) {
        return Integer.compare(this.votes, o.votes);
    }
}
