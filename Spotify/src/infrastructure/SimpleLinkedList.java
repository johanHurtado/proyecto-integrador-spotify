package infrastructure;

import interfaces.PLaylist;


public class SimpleLinkedList<T> implements PLaylist<T> {
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
            current = head;
        } else {
            Node<T> temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = node;
        }
    }

    @Override
    public void removeCurrentItem() {
         if (current == null || head == null) return;
        if (current == head) {
            head = head.next;
            current = head;
            return;
             }
        Node<T> temp = head;
        while (temp.next != current && temp.next != null) {
            temp = temp.next;
        }
        if (temp.next == current) {
            temp.next = current.next;
            current = temp.next;
        }
    }

    @Override
    public  T getCurrentItem() {
         return current != null ?  current.data : null;
    }

    @Override
    public  T nextItem() {
        if (current != null && current.next != null) {
            current = current.next;
            return  current.data;
        }
        return null;
    }

    @Override
    public  T previousItem() {
        throw new UnsupportedOperationException("No soportado en lista simple.");    
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
   


