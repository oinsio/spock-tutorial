package com.example;

import java.util.LinkedList;
import java.util.List;

public class LinkedListMinStack implements MinStack {

    private final List<Integer> stack;
    private final List<Integer> minStack;

    public LinkedListMinStack() {
        stack = new LinkedList<>();
        minStack = new LinkedList<>();
    }

    public void push(int val) {
        stack.addFirst(val);
        if (minStack.isEmpty() || minStack.getFirst() >= val) minStack.addFirst(val);
    }

    public void pop() {
        if (minStack.getFirst().equals(stack.removeFirst())) minStack.removeFirst();
    }

    public int top() {
        return stack.getFirst();
    }

    public int getMin() {
        return minStack.getFirst();
    }

}
