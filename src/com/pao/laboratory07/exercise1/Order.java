package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Stack;

public class Order {
    private OrderState currentState;
    private Stack<OrderState> stack = new Stack<>();
    public Order(OrderState initialState) {
        this.currentState = initialState;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        stack.push(currentState);
        currentState = currentState.next();

        System.out.println("Order state updated to: " + currentState);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        stack.push(currentState);
        currentState = currentState.cancel();

        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if(stack.empty()) {
            throw new CannotRevertInitialOrderStateException();
        }
        currentState = stack.pop();

        System.out.println("Order state reverted to: " + currentState);
    }
}
