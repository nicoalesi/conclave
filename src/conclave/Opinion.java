package conclave;

public class Opinion {
        int id;
        int value;

        Opinion(int id, int value) {
            this.id = id;
            this.value = value;
        }

        public String toString() {

            return "(id: " + id + ", value: " + value + ") ";

        }
    }