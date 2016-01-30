package utils;

/**
 * Created by paolo on 18/11/2015.
 * An enum containig the possibilities for the Shape to be shown.
 * BE CAREFUL that the default set (NOIMAGE), used only for exceptions, NEEDS to be the FIRST.
 */
public enum Shape {

    NOIMAGE /* NEED TO BE THE FIRST! */
    , SQUARE, TRIANGLE, CIRCLE, RECTANGLE;


    /**
     * You knwow.
     * @return a String with the name of the Shape, or "Not a valid Image" if it's not supported".
     */
    public String toString() {
        if(this.equals(Shape.CIRCLE)) return "Circle"; else
        if(this.equals(Shape.SQUARE)) return "Square"; else
        if(this.equals(Shape.TRIANGLE)) return "Triangle"; else
        if(this.equals(Shape.RECTANGLE)) return "Rectangle"; else
        return "Not a valid Image";
    }
}
