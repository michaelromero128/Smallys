package cornez.com.smallys;

import java.util.ArrayList;

public class Order {
    private String name;
    private ArrayList<Item> items;
    private String userID;



    public Order() {

    }

    public Order(String name, ArrayList<Item> items, String userID) {
        this.name = name;
        this.items = items;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
    public void addItems(Item item){
        this.items.add(item);
    }
    public void removeItems(Item item){
        this.items.remove(item);
    }





    public String toString(){
        String string = "";
        if(items == null){
            return "order is blank";
        }
        if(items.size()==0){
            return "order is blank";
        }else {
            string = name +": ";
            for(Item item : items){
                string+= item.toString() +", ";
            }
           string= (String) string.subSequence(0, string.length()-2);
            return string;
        }

    }
    public String otherToString(){
        String string = "";
        if(items == null){
            return "order is blank";
        }
        if(items.size()==0){
            return "order is blank";
        }else {
            string = "";
            for(Item item : items){
                string+= item.toString() +", ";
            }
            string= (String) string.subSequence(0, string.length()-2);
            return string;
        }

    }

}

