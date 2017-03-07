package cs455.scaling.utilities;

import java.util.*;

/**
 * Created by eloza on 3/5/17.
 */
public class SyncLinkedList<T> implements List<T>{

    private final LinkedList<T> ll = new LinkedList<T>();

    //----Methods
    public synchronized boolean removeIfPresent(T t){
        boolean present = ll.contains(t);
        if (present){
            ll.push(t);
        }
        return present;
    }


    //-----List Overrides------------------

    @Override
    public synchronized int size() {
        return ll.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return ll.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return ll.contains(o);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return ll.iterator();
    }

    @Override
    public synchronized Object[] toArray() {
        return ll.toArray();
    }

    @Override
    public synchronized <T1> T1[] toArray(T1[] a) {
        return ll.toArray(a);
    }

    @Override
    public synchronized boolean add(T t) {
        return ll.add(t);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return ll.remove(o);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return ll.containsAll(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {
        return ll.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends T> c) {
        return ll.addAll(index, c);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return ll.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return ll.retainAll(c);
    }

    @Override
    public synchronized void clear() {
        ll.clear();
    }

    @Override
    public synchronized T get(int index) {
        return ll.get(index);
    }

    @Override
    public synchronized T set(int index, T element) {
        return ll.set(index, element);
    }

    @Override
    public synchronized void add(int index, T element) {
        ll.add(index, element);
    }

    @Override
    public synchronized T remove(int index) {
        return ll.remove(index);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return ll.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return ll.lastIndexOf(o);
    }

    @Override
    public synchronized ListIterator<T> listIterator() {
        return ll.listIterator();
    }

    @Override
    public synchronized ListIterator<T> listIterator(int index) {
        return ll.listIterator(index);
    }

    @Override
    public synchronized List<T> subList(int fromIndex, int toIndex) {
        return ll.subList(fromIndex, toIndex);
    }

    //------LinkedList Functionality

    public synchronized void addFirst(T t){
        ll.addFirst(t);
    }

    public synchronized void addLast(T t){
        ll.addLast(t);
    }

    public synchronized Object clone(){
        return ll.clone();
    }

    public synchronized Iterator<T> descendingIterator(){
        return ll.descendingIterator();
    }

    public synchronized T element(){
        return ll.element();
    }

    public synchronized T getFirst(){
        return ll.getFirst();
    }

    public synchronized T getLast(){
        return ll.getLast();
    }

    public synchronized boolean offer (T t){
        return ll.offer(t);
    }

    public synchronized boolean offerFirst (T t){
        return ll.offerFirst(t);
    }

    public synchronized boolean offerLast (T t){
        return ll.offerLast(t);
    }

    public synchronized T peek(){
        return ll.peek();
    }

    public synchronized T peekFirst(){
        return ll.peekFirst();
    }

    public synchronized T peekLast(){
        return ll.peekLast();
    }

    public synchronized T poll(){
        return ll.poll();
    }

    public synchronized T pollFirst(){
        return ll.pollFirst();
    }

    public synchronized T pollLast(){
        return ll.pollLast();
    }

    public synchronized T pop(){
        return ll.pop();
    }

    public synchronized void push(T t){
        ll.push(t);
    }

    public synchronized T remove(){
        return ll.remove();
    }

    public synchronized T removeFirst(){
        return ll.removeFirst();
    }

    public synchronized boolean removeFirstOccurrence(Object o){
        return ll.removeFirstOccurrence(o);
    }

    public synchronized T removeLast(){
        return ll.removeLast();
    }

    public synchronized boolean removeLastOccurrence(Object o){
        return ll.removeLastOccurrence(o);
    }
}
