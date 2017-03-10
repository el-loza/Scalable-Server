//package cs455.scaling.client;
//
///**
// * Created by eloza on 3/5/17.
// */
//public class HashNode {
//
//    private String hash;
//    private HashNode next;
//    private HashNode prev;
//
//    public HashNode(String hash){
//        this.hash = hash;
//        this.next = null;
//        this.prev = null;
//    }
//
//    public void addNext(HashNode node){
//        this.next = node;
//    }
//
//    public void removeNext(){
//        this.next = null;
//    }
//
//    public HashNode getNext(){
//        return getNext();
//    }
//
//    public void addPrev(HashNode node){
//        this.prev = node;
//    }
//
//    public void removePrev(){
//        this.prev = null;
//    }
//
//    public HashNode getPrev(){
//        return getPrev();
//    }
//
//    public String getHash(){
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object node){
//        if (node == null){
//            return false;
//        }
//        if (node.getClass() != this.getClass()){
//            return false;
//        }
//
//        final HashNode comNode = (HashNode) node;
//
//        if (!this.hash.equals(comNode.getHash())){
//            return false;
//        }
//
//        return true;
//    }
//
//}
