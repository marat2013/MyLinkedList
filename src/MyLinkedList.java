import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class MyLinkedList <T> {

    private int amountOfElements = 0;

    private Node<T> first = null;
    private Node<T> last = null;

    // служебный класс, объекты которого хранят ссылки друг на друга и параметры элементов
    // которые помещаются к коллекции(индекс и значение), с помощью нод все методы и обрабатывают
    // данные
    private static class  Node <T>  {
        private Node <T> previous;
        private Node <T> next;
        private int nodeIndex;
        private T item;


        private Node(Node<T> previous, Node<T> next, int nodeIndex, T item) {
            this.previous = previous;
            this.next = next;
            this.item = item;
            this.nodeIndex = nodeIndex;
        }
    }

    public void add(T item) {
        Node<T> node;
        if(amountOfElements == 0) node = createNode(0, item);
        else node = createNode(last.nodeIndex+1, item);
    }

    public void add(int index, T item) {
        Node<T> node = createNode(index, item);
    }
    public boolean addAll(int index, Collection<? extends T> collection) {
        boolean wasInserted = false;
        for(T element : collection) {
            add(index, element);
            index+=1;
            wasInserted = true;
        }
        return wasInserted;
    }

    public void sort(Comparator<? super T> comparator) {
        int comparingResult;
        boolean elementsSwappingOccured;
        for(int i =0; i<amountOfElements; i++) {

            Node<T> node = first;
            elementsSwappingOccured = false;
            for(int j =0; j<amountOfElements; j++) {

                Optional<Node<T>> nextNode = Optional.ofNullable(node.next);
                if(nextNode.isPresent()) {
                    comparingResult = comparator.compare(node.item, node.next.item);
                    if(comparingResult > 0) {
                        swapNeighbouringNodes(node, node.next);
                        elementsSwappingOccured = true;
                    }
                    node = node.next;
                }
            }
            if( !elementsSwappingOccured) return;
        }

    }

    public T get(int index) {
        Optional<Node<T>> optionalNode = Optional.ofNullable(findNodeByIndex(index));
        if(optionalNode.isPresent()) return optionalNode.get().item;
        return null;
    }
    public T remove() {
        Optional<Node<T>> optionalNodeToDelete = Optional.ofNullable(first);
        if(optionalNodeToDelete.isPresent()){
            Node<T> nodeToDelete = optionalNodeToDelete.get();
            T firstNodeItem = nodeToDelete.item;
            removeNodeFromChain(nodeToDelete);
            return firstNodeItem;
        }
        return null;
    }
    public T remove (int index) {
        Optional<Node<T>> optionalNodeToDelete = Optional.ofNullable(findNodeByIndex(index));
        if(optionalNodeToDelete.isPresent()){
            Node<T> nodeToDelete = optionalNodeToDelete.get();
            T nodeToDeleteItem = nodeToDelete.item;
            removeNodeFromChain(nodeToDelete);
            return nodeToDeleteItem;
        }
        return null;
    }
    public boolean remove (T item) {
        Optional<Node<T>> optionalNodeToDelete = Optional.ofNullable(findNodeByItem(item));
        if(optionalNodeToDelete.isPresent()) {
            Node<T> nodeToDelete = optionalNodeToDelete.get();
            removeNodeFromChain(nodeToDelete);
            return true;
        }
        return false;
    }
    public int size() { return amountOfElements;}

    // если индекс меньше нуля или не соответствует элементу на конце коллекции
    // или внутри коллекции то выбрасывается исключение,
    //также метод увеличивает количество элементов на 1
    private Node<T> createNode(int index, T item) {
        Node<T> node;
        if(index > amountOfElements || index < 0) throw new IndexOutOfBoundsException(
                "Max value of index parameter should be equal or less than amount of elements in collection");
        if(index == 0) {
            if(amountOfElements == 0) {
                node = new Node<>(null,null,0,item);
                first = node;
                last = node;
            }
            else {
                node = new Node<>(null,first,0,item);
                first.previous=node;
                first = node;
            }
        }
        else if (index == amountOfElements) {
            node = new Node<>(last, null, last.nodeIndex + 1, item);
            last.next = node;
            last = node;
        }
        else {
            Node <T> tmpNode = findNodeByIndex(index);
            node = new Node<>(tmpNode.previous, tmpNode, index, item);
            tmpNode.previous.next = node;
            tmpNode.previous = node;
        }
        while(node.next != null) {
            node = node.next;
            node.nodeIndex += 1;
        }
        amountOfElements += 1;
        return node;
    }

    // служебный метод, меняет значения item у нод из параметров метода
    // первая нода обязательно должна иметь индекс на 1 меньше чем вторая
    // если представить все ноды в виде списка в линию то первая нода и
    //параметров метода находится левее второй ноды
    private void swapNeighbouringNodes(Node<T> leftNode, Node<T> rightNode){
        T tmp;
        tmp = leftNode.item;
        leftNode.item = rightNode.item;
        rightNode.item = tmp;
    }

    // служебный метод, убирает ссылки(previous и next) которые связывают ноду из параметра
    // и соседние с ней ноды, после выполнения ноды соседние с нодой из параметра указывают друг на друга
    // также метод уменьшает количество элементов на 1
    private void removeNodeFromChain(Node<T> node) {
        if (node != first && node != last) {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }

        if (node == first) {
            node.next.previous = null;
            first = node.next;
        }

        if (node == last) {
            node.previous.next = null;
            last = node.previous;
        }

        while(node.next != null) {
            node = node.next;
            node.nodeIndex-=1;
        }
        amountOfElements-=1;
    }
    // служебный метод, может возвращать null
    private Node<T> findNodeByItem (T item) {
        Node<T> currentNode = first;
        for(int i = 0; i < amountOfElements; i++) {

            if (currentNode.item.equals(item) ) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }
    // служебный метод, может возвращать null
    private Node<T> findNodeByIndex (int index) {
        Node<T> currentNode = first;
        if(index == 0) return first;
        while(currentNode.next!=null) {

            currentNode = currentNode.next;
            if (currentNode.nodeIndex == index) {
                return currentNode;
            }
        }
        return null;
    }
}
