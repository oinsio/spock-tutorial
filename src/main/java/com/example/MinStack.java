package com.example;

/**
 * https://leetcode.com/problems/min-stack/
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(val);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */
public interface MinStack {
    /**
     * Pushes the element val onto the stack
     * @param val int value
     */
    void push(int val);

    /**
     * Removes the element on the top of the stack
     */
    void pop();

    /**
     * Gets the top element of the stack
     * @return the top element of the stack
     */
    int top();

    /**
     * Retrieves the minimum element in the stack
     * @return the minimum element in the stack
     */
    int getMin();
}
