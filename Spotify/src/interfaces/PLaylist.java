package interfaces;

public interface PLaylist <T> {
    public void addItem(T item);
    public void removeCurrentItem();
    public  T getCurrentItem();
    public  T nextItem();
    public  T previousItem();
    public void reset();
    public void showAll();
}
