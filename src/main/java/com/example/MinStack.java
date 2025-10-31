package com.example;

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(val);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */
class MinStack {

    /*
    Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

    Implement the MinStack class:

    MinStack() initializes the stack object.
    void push(int val) pushes the element val onto the stack.
    void pop() removes the element on the top of the stack.
    int top() gets the top element of the stack.
    int getMin() retrieves the minimum element in the stack.
    You must implement a solution with O(1) time complexity for each function.

    -231 <= val <= 231 - 1
    Methods pop, top and getMin operations will always be called on non-empty stacks.
    At most 3 * 104 calls will be made to push, pop, top, and getMin.
     */
    Node head;

    public MinStack() {
    }

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
