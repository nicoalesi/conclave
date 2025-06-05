public class Heap {

    int[] positions;
    Opinion[] array;
    int elements = 0;
    final int SIZE;

    public Heap(int size) {

        this.SIZE = size;
        this.array = new Opinion[size];
        this.positions = new int[size];
        for (int i = 0; i < size; i++) {
            this.array[i] = null;
            this.positions[i] = -1;
        }
    }

    private int parent(int index) {
        return (int) Math.floor((index + 1) / 2) - 1;
    }

    private int left(int index) {
        return (2 * (index + 1)) -1;
    }

    private int right(int index) {
        return (2 * (index + 1));
    }

    public Opinion getTop() {

        if (elements == 0) {
            return null;
        }
        return array[0];
    }

    private void heapifyUp(int index) {

        int target = parent(index);
        
        if (target >= 0 && array[index].value > array[target].value) {

            Opinion temp = array[index];
            array[index] = array[target];
            array[target] = temp;
            positions[array[index].id] = index;
            positions[array[target].id] = target;

            heapifyUp(target);
        }
    }

    private void heapifyDown(int index) {

        int left = left(index);

        if (left > elements - 1) {
            return;
        }

        int right = right(index);
        int target = -1 ;

        if (right > elements - 1) {
            target = left;

        } else {
            target = (array[left].value > array[right].value) ? left : right;
        }

        if (target != -1 && array[index].value < array[target].value) {

            Opinion temp = array[index];
            array[index] = array[target];
            array[target] = temp;
            positions[array[index].id] = index;
            positions[array[target].id] = target;

            heapifyDown(target);
        }
    }

    public void add(int id, int value) {

        if (positions[id] != -1) {

            return;
        }

        array[elements] = new Opinion(id, value);
        positions[id] = elements++;

        heapifyUp(elements - 1);
        
    }

    public Opinion remove(int id) {

        if (positions[id] == -1) {

            return new Opinion(id, 0);
        }

        Opinion output = array[positions[id]];
        array[positions[id]] = array[--elements];
        int temp = positions[id];    
        positions[array[elements].id] = positions[id];
        positions[id] = -1;
        array[elements] = null;

        heapifyDown(temp);

        return output;
    }

    public void printHeap() {

        System.out.print("Heap: ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
        System.out.print("Positions: ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(positions[i] + " ");
        }
        
        System.out.println();
    }
}
