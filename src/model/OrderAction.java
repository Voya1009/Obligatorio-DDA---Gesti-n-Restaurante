package model;

import system.*;

public interface OrderAction {

    void apply(Order order, Manager manager) throws SystemException;
}
