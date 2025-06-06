package project.conclave;

public class Opinion {
        public int id;
        public int value;

        public Opinion(int id, int value) {
            this.id = id;
            this.value = value;
        }

        public String toString() {
            return "(id: " + id + ", value: " + value + ") ";
        }
    }