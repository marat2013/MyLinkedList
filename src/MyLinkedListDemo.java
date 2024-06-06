import java.util.ArrayList;
import java.util.Comparator;

public class MyLinkedListDemo {
    public static void main(String[] args) {

        MyLinkedList<Integer> liste = new MyLinkedList<>();
        liste.add(1);
        liste.add(2);
        liste.add(3);
        liste.add(0, 10);
        liste.add(2, 12);
        liste.add(4, 14);
        liste.add(6, 16);
        liste.remove(6);
        liste.remove(4);
        liste.remove(2);
        liste.remove(0);
        liste.add(5);
        liste.add(6);
        liste.add(7);
        liste.add(8);

        ArrayList<Integer> array = new ArrayList<>();
        array.add(100);
        array.add(200);
        array.add(300);
        array.add(400);

        liste.addAll(5, array);

        IntegerComparator comparator = new IntegerComparator();
        liste.sort(comparator);

        for(int i =0; i < liste.size(); i ++) {
            System.out.println(liste.get(i));
        }
    }

}

class IntegerComparator implements Comparator<Integer> {
    public int compare(Integer int1,Integer int2) {
        return int1.compareTo(int2);
    }
}
