package kurtis.chudasama.service;

import kurtis.chudasama.entity.Item;

import java.util.ArrayList;

public interface IItemService {

    Item findById(int id);

    /*Item findByCartId(int cartId);

    Item findByOrderId(int orderId);

    ArrayList<Item> findItemsByCartId(int cartId);*/

    ArrayList<Item> findItemsByName(String itemName);

    void saveItem(Item item);
}
