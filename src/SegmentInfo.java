
public class SegmentInfo {
   public final double epsilon = 1e-5;
    private double _x;
    private double _y;
    public double GetX(){
        return _x;
    }
    public double GetY(){
        return _y;
    }

    public SegmentInfo(double x1, double y1){
        this._x = x1;
        this._y = y1;

    }
    @Override
    public String toString(){
        return "(" + _x + ',' + _y + ")";
    }

}
