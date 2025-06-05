public class Triplet<E extends String, V extends Integer> implements Comparable<Triplet<?, V>> {
    E name;
    E surname;
    V votes;

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
