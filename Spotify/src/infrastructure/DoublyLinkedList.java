package infrastructure;

import interfaces.PLaylist;

public class DoublyLinkedList<T> implements PLaylist<T> {
    private Node<T> head, tail, current;

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
            head = tail = current = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    @Override
    public void removeCurrentItem() {
        if (current == null) return;

        if (current == head) head = current.next;
        if (current == tail) tail = current.prev;

        if (current.prev != null) current.prev.next = current.next;
        if (current.next != null) current.next.prev = current.prev;

        current = current.next != null ? current.next : head;
    }

    @Override
    public T getCurrentItem() {
        return current != null ? current.data : null;
    }

    @Override
    public T nextItem() {
        if (current != null && current.next != null) {
            current = current.next;
            return current.data;
        }
        return null;
    }

    @Override
    public T previousItem() {
        if (current != null && current.prev != null) {
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
        Node<T> temp = head;
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }
}

