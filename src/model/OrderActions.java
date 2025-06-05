package model;

import system.*;

public enum OrderActions implements OrderAction {
    TAKE {
        @Override
        public void apply(Order order, Manager manager) throws SystemException {
            order.take(manager);
        }
    },
    READY {
        @Override
        public void apply(Order order, Manager manager) throws SystemException {
            order.markAsReady();
        }
    },
    DELIVER {
        @Override
        public void apply(Order order, Manager manager) throws SystemException {
            order.deliver();
        }
    }
}
