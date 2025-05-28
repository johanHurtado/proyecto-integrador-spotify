package infrastructure;

import interfaces.PLaylist;

public class CircularLinkedList<T> implements PLaylist<T> {
    private Node<T> head, current;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) {
            this.data = data;
        }
    }

    @Override
    public void addItem(T item) {
        Node<T> node = new Node<>(item);
        if (head == null) {
            head = node;
            head.next = head;
            current = head;
        } else {
            Node<T> temp = head;
            while (temp.next != head) temp = temp.next;
            temp.next = node;
            node.next = head;
        }
    }

    @Override
    public void removeCurrentItem() {
        if (current == null || head == null) return;

        if (current == head && head.next == head) {
            head = null;
            current = null;
        } else {
            Node<T> temp = head;
            while (temp.next != current) temp = temp.next;
            temp.next = current.next;
            if (current == head) head = current.next;
            current = current.next;
        }
    }

    @Override
    public T getCurrentItem() {
        return current != null ? current.data : null;
    }

    @Override
    public T nextItem() {
        if (current != null) {
            current = current.next;
            return current.data;
        }
        return null;
    }

    @Override
    public T previousItem() {
        throw new UnsupportedOperationException("No soportado en lista circular simple.");
    }

    @Override
    public void reset() {
        current = head;
    }

    @Override
    public void showAll() {
        if (head == null) return;
        Node<T> temp = head;
        do {
            System.out.println(temp.data);
            temp = temp.next;
        } while (temp != head);
    }
}
