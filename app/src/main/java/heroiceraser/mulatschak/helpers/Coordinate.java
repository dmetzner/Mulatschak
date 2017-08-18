package heroiceraser.mulatschak.helpers;

/**
 * Created by Daniel Metzner on 10.08.2017.
 */

//----------------------------------------------------------------------------------------------
//  A simple 2 dimensional integer Coordinate
//
public class Coordinate {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    int x_;
    int y_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public Coordinate() {}

    public Coordinate(int x, int y) {
        x_ = x;
        y_ = y;
    }

    public Coordinate(Coordinate c) {
        x_ = c.getX();
        y_ = c.getY();
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getX() {
        return x_;
    }

    public void setX(int x) { x_ = x; }

    public int getY() {
        return y_;
    }

    public void setY(int y) { y_ = y; }

    //----------------------------------------------------------------------------------------------
    //
    //

}
