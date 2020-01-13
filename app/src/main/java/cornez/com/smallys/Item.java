package cornez.com.smallys;

public class Item {

    private String description;
    private int count;

    public Item(){

    }

    public Item(String description, int count) {
        this.description = description;
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public String toString(){
        return Integer.toString(count) +"x: " + description;
    }
}
