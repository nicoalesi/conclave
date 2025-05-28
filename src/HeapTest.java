public class HeapTest {
    public static void main(String[] args) {

        Heap heap = new Heap(30);
        
        heap.add(0, 1);
        heap.add(11, 11);
        heap.add(2, 3);
        heap.add(3, 4);
        heap.add(4, 5);
        heap.add(5, 6);
        heap.add(6, 7);
        heap.add(7, 8);
        heap.add(8, 9);
        heap.add(9, 10);
        heap.add(10, 3);
        heap.add(1, 200);
        heap.remove(1);
        heap.add(12, 12);
        heap.add(13, 13);
        heap.add(14, 14);
        heap.add(15, 15);
        heap.add(16, 16);
        heap.add(17, 17);
        heap.add(18, 18);

        heap.add(2, 391203);
        heap.add(19, 31293120);

        System.out.println(heap.remove(19));

        heap.printHeap();

        System.out.println("Heap created successfully.");
    }
    
}
