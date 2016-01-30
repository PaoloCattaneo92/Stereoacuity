package utils;

/**
 * Created by paolo on 22/11/2015.
 * This class represent a Setting giving you name, id and a description for the Settings activity.
 */
public class Setting {

    public String id; //it's the same of the constants variables
    public String name; //the name
    public String description; //short description
    public int valueToShow;

    /**
     * Constructor
     * @param id it's the same of the constants variables
     * @param name the name
     * @param description short description
     * @param value the value to show
     */
    public Setting(String id, String name, String description, int value) {
        this.id=id;
        this.name=name;
        this.description=description;
        this.valueToShow=value;
    }


}
