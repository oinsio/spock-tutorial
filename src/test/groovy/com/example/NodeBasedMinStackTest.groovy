package com.example

import spock.lang.Specification

/*
Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

Implement the MinStack class:

MinStack() initializes the stack object.
void push(int val) pushes the element val onto the stack.
void pop() removes the element on the top of the stack.
int top() gets the top element of the stack.
int getMin() retrieves the minimum element in the stack.
You must implement a solution with O(1) time complexity for each function.
 */
class NodeBasedMinStackTest extends Specification {

    MinStack stack;

    void setup() {
        stack = new NodeBasedMinStack() as MinStack
    }

    def "should return pushed ints in the reverse order"() {
        given:
        stack.push(10)
        stack.push(20)
        stack.push(30)

        expect:
        stack.top() == 30
        stack.pop()
        and:
        stack.top() == 20
        stack.pop()
        and:
        stack.top() == 10
    }

    def "should return min value from the stack"() {
        given:
        stack.push(1)
        stack.push(5)

        expect:
        stack.getMin() == 1
    }

    def "should return the previous min value from the stack after pop the top min value"() {
        given:
        stack.push(10)
        stack.push(50)
        stack.push(5)

        expect:
        stack.top() == 5
        stack.getMin() == 5
        and:
        stack.pop()
        stack.top() == 50
        stack.getMin() == 10
        and:
        stack.pop()
        stack.top() == 10
        stack.getMin() == 10
    }

    def "should work with a lot of commands"() {
        given: "push, push, push"
        stack.push(0)
        stack.push(1)
        stack.push(0)

        expect: "getMin, pop, getMin, pop, getMin, pop"
        stack.getMin() == 0
        stack.pop()
        stack.getMin() == 0
        stack.pop()
        stack.getMin() == 0
        stack.pop()

        and: "push, push, push"
        stack.push(-2)
        stack.push(-1)
        stack.push(-2)

        and: "getMin, pop, top, getMin, pop, getMin, pop"
        stack.getMin() == -2
        stack.pop()
        stack.top() == -1
        stack.getMin() == -2
        stack.pop()
        stack.getMin() == -2
        stack.pop()
    }

    def "should work with another list of commands"() {
        given:
        stack.push(2)
        stack.push(0)
        stack.push(3)
        stack.push(0)

        expect:
        stack.getMin() == 0
        stack.pop()
        stack.getMin() == 0
        stack.pop()
        stack.getMin() == 0
        stack.pop()
        stack.getMin() == 2
    }
}