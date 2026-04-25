package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

public enum OrderState {
    PLACED {
        @Override
        public OrderState next() {
            return PROCESSED;
        }
    },
    PROCESSED {
        @Override
        public OrderState next() {
            return SHIPPED;
        }
    },
    SHIPPED {
        @Override
        public OrderState next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public OrderState next() throws OrderIsAlreadyFinalException {
            throw new OrderIsAlreadyFinalException();
        }

        @Override
        public OrderState cancel() throws CannotCancelFinalOrderException {
            throw new CannotCancelFinalOrderException();
        }
    },
    CANCELED {
        @Override
        public OrderState next() throws OrderIsAlreadyFinalException {
            throw new OrderIsAlreadyFinalException();
        }

        @Override
        public OrderState cancel() throws CannotCancelFinalOrderException {
            throw new CannotCancelFinalOrderException();
        }
    };

    public abstract OrderState next() throws OrderIsAlreadyFinalException;
    public OrderState cancel() throws CannotCancelFinalOrderException {
        return CANCELED;
    }
}
