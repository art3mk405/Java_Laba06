import java.util.ArrayList;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("           Laboratory Work #6");
        System.out.println("===========================================");
        System.out.println("Topic: Working with collections in Java");
        System.out.println("Variant: 4 (MyList + singly linked list)");
        System.out.println("===========================================\n");

        try (Scanner sc = new Scanner(System.in)) {
     
            List<Composition> baseTracks = createBaseTracks();

            MyList<Composition> list1 = new MyLinkedList<>();
            for (Composition c : baseTracks) {
                list1.add(c);
            }

            System.out.println("Initial list (created by empty constructor):");
            printList(list1);

            System.out.println("\nSize of list1: " + list1.size());

            Composition single = new PopComposition("Single Track", 200);
            MyList<Composition> list2 = new MyLinkedList<>(single);
            System.out.println("\nList2 (created with one element):");
            printList(list2);

            MyList<Composition> list3 = new MyLinkedList<>(baseTracks);
            System.out.println("\nList3 (created from java.util.List):");
            printList(list3);

            try {
                System.out.print("\nEnter index to get element from list1: ");
                int index = sc.nextInt();
                Composition atIndex = list1.get(index);
                System.out.println("Element at index " + index + ": " + atIndex);

                System.out.print("\nEnter index to remove from list1: ");
                int removeIndex = sc.nextInt();
                Composition removed = list1.removeAt(removeIndex);
                System.out.println("Removed: " + removed);

                System.out.println("\nList1 after removeAt:");
                printList(list1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Index error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Input error: please enter integer index.");
            }

            System.out.println("\nDemo of remove(element) on list3:");
            System.out.println("Before remove: ");
            printList(list3);
            if (list3.remove(single)) {
                System.out.println("Removed element: " + single);
            } else {
                System.out.println("Element not found in list3.");
            }
            System.out.println("After remove: ");
            printList(list3);

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static List<Composition> createBaseTracks() {
        List<Composition> tracks = new ArrayList<>();
        tracks.add(new RockComposition("Stone Road", 210));
        tracks.add(new PopComposition("Summer Night", 185));
        tracks.add(new JazzComposition("Blue Morning", 240));
        tracks.add(new RockComposition("Electric Storm", 195));
        tracks.add(new PopComposition("Simple Love", 160));
        tracks.add(new JazzComposition("Old Street", 300));
        return tracks;
    }

    private static void printList(MyList<Composition> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }
    }
}

abstract class Composition {
    private String title;
    private int durationSec;
    private String style;

    public Composition(String title, int durationSec, String style) {
        this.title = title;
        this.durationSec = durationSec;
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public String getStyle() {
        return style;
    }

    public abstract void play();

    @Override
    public String toString() {
        return "\"" + title + "\" (" + style + ", " + durationSec + " sec)";
    }
}

class RockComposition extends Composition {
    public RockComposition(String title, int durationSec) {
        super(title, durationSec, "Rock");
    }

    @Override
    public void play() {
        System.out.println("Playing rock track: " + getTitle());
    }
}

class PopComposition extends Composition {
    public PopComposition(String title, int durationSec) {
        super(title, durationSec, "Pop");
    }

    @Override
    public void play() {
        System.out.println("Playing pop track: " + getTitle());
    }
}

class JazzComposition extends Composition {
    public JazzComposition(String title, int durationSec) {
        super(title, durationSec, "Jazz");
    }

    @Override
    public void play() {
        System.out.println("Playing jazz track: " + getTitle());
    }
}

//custom collection

interface MyList<T> {
    void add(T element);
    T get(int index);
    T removeAt(int index);
    boolean remove(T element);
    int size();
    boolean isEmpty();
    void clear();
}

class MyLinkedList<T> implements MyList<T> {

    // вузол однозв’язного списку
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T d) {
            data = d;
        }
    }

    private Node<T> head;
    private int size;

    public MyLinkedList() {
    }

    public MyLinkedList(T element) {
        add(element);
    }

    public MyLinkedList(Collection<T> collection) {
        for (T e : collection) {
            add(e);
        }
    }

    @Override
    public void add(T element) {
        Node<T> node = new Node<>(element);
        if (head == null) {
            head = node;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }
        size++;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        return current.data;
    }

    @Override
    public T removeAt(int index) {
        checkIndex(index);
        Node<T> removed;

        if (index == 0) {
            removed = head;
            head = head.next;
        } else {
            Node<T> prev = head;
            int i = 0;
            while (i < index - 1) {
                prev = prev.next;
                i++;
            }
            removed = prev.next;
            prev.next = removed.next;
        }

        size--;
        return removed.data;
    }

    @Override
    public boolean remove(T element) {
        if (head == null) return false;

        if ((head.data == null && element == null) ||
                (head.data != null && head.data.equals(element))) {
            head = head.next;
            size--;
            return true;
        }

        Node<T> prev = head;
        Node<T> current = head.next;

        while (current != null) {
            if ((current.data == null && element == null) ||
                    (current.data != null && current.data.equals(element))) {
                prev.next = current.next;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }
}
