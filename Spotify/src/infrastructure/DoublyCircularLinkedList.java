package infrastructure;

import interfaces.PLaylist;

public class DoublyCircularLinkedList<T> implements PLaylist<T> {
    private Node<T> head, current;

    private static class Node<T> {
        T data;
        Node<T> next, prev;
        Node(T data) {
            this.data = data;
        }
    }

    @Override
    public void addItem(T item) {
        Node<T> node = new Node<>(item);
        if (head == null) {
            head = node;
            head.next = head.prev = head;
            current = head;
        } else {
            Node<T> tail = head.prev;
            tail.next = node;
            node.prev = tail;
            node.next = head;
            head.prev = node;
        }
    }

    @Override
    public void removeCurrentItem() {
        if (current == null) return;

        if (current.next == current) {
            head = current = null;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
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
        if (current != null) {
            current = current.prev;
            return current.data;
        }
        return null;
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
