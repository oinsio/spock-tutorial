package com.example;

class Node {
    int value;
    int minValue;
    Node prevNode;

    public Node(int value, int min, Node next) {
        this.value = value;
        this.minValue = min;
        this.prevNode = next;
    }
}

class NodeBasedMinStack implements MinStack {

    Node head;

    public NodeBasedMinStack() {}

    public void push(int val) {
        head = (head == null) ? new Node(val, val, null) : new Node(val, Math.min(val, head.minValue), head);
    }

    public void pop() {
        head = head.prevNode;
    }

    public int top() {
        return head.value;
    }

    public int getMin() {
        return head.minValue;
    }
}
