package ca.brianchau.jfl;

/**
 * Created by brian on 2014-10-06.
 */
public class Empty extends Cons {
    Empty() {
        super(null, null);
    }
    public String toString() {
        return "empty";
    }
}
