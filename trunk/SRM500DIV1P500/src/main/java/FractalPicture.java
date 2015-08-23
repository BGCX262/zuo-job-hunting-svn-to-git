/**
 * Created by stms130834 on 2014/3/26.
 */
public class FractalPicture {
    public double getLength(int x1, int y1, int x2, int y2){

        Rectangle r = new Rectangle(x1,y1, x2, y2);

        Point center = new Point(0.0,54.0);
        Point east = new Point(27.0,54.0);
        Point west = new Point(-27.0,54.0);
        Point north = new Point(0.0,81.0);
        Point south = new Point(0.0,0.0);

        Shape s = new Shape(center, south, west, east, north, 1, 500, DIRECTION.NORTH);
        return s.getLength(r);
    }
}

class Point{
    double x;
    double y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean inside(Rectangle r){
        return r.contains(this);
    }

    public double to(Point s) {
        double a = (s.x-this.x)*(s.x-this.x);
        double b = (s.y-this.y)*(s.y-this.y);
        return Math.sqrt(a+b);
    }

    public Point move(DIRECTION d, double offset){
        switch (d){
            case  NORTH:
                return new Point(x,y+offset);
            case WEST:
                return new Point(x-offset,y);
            case SOUTH:
                return new Point(x,y-offset);
            case EAST:
                return new Point(x+offset,y);
            default:
                return null;
        }
    }
}

interface caculatable {
    public double getLength(Rectangle r);
}

enum DIRECTION{
    NORTH, SOUTH, EAST, WEST
}

class Line implements caculatable{
    Point a;
    Point b;

    Line(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Since the line is vertical or horizontal, we can simplify the solution
     *
     * */
    @Override
    public double getLength(Rectangle r) {
        if(a.x != b.x && a.y == b.y){
            // then the line is horizontal
            if(r.y2 >= a.y && r.y1 <= a.y){
                double front = a.x < b.x? a.x:b.x;
                double end = a.x < b.x? b.x:a.x;
                if(front < r.x1 && end <= r.x1){
                    return 0.0;
                }
                else if(front <= r.x1 && end >= r.x1 && end <= r.x2){
                    return end - r.x1;
                }
                else if(front <= r.x1 && end >= r.x2){
                    return r.x2-r.x1;
                }
                else if(front >= r.x1 && front <= r.x2 && end <= r.x2){
                    return end -front;
                }
                else if(front >= r.x1 && front <= r.x2 && end >= r.x2){
                    return r.x2 - front;
                }
                else if(front >= r.x2){
                    return 0.0;
                }
                else{
                    System.out.println("unpredictable situation");
                }
            }
            else{
                return 0.0;
            }
        }
        else if(a.x == b.x && a.y != b.y){
            // then the line is vertical
            if(r.x1<= a.x && r.x2 >=a.x){
                double bottom = a.y < b.y? a.y:b.y;
                double top = a.y < b.y? b.y:a.y;
                if(bottom <= r.y1 && top <= r.y1){
                    return 0.0;
                }
                else if(bottom <= r.y1 && top >= r.y1 && top <= r.y2){
                    return top - r.y1;
                }
                else if(bottom <= r.y1 && top >= r.y2){
                    return r.y2-r.y1;
                }
                else if(bottom >= r.y1 && bottom <= r.y2 && top <= r.y2){
                    return top -bottom;
                }
                else if(bottom >= r.y1 && bottom <= r.y2 && top >= r.y2){
                    return r.y2 - bottom;
                }
                else if(bottom >= r.y2){
                    return 0.0;
                }
                else{
                    System.out.println("unpridictable situation");
                }
            }
            else{
                return 0.0;
            }

        }
        System.out.println("not horizontal and not vertical");
        return 0.0;
    }
}

class Rectangle{
    double x1;
    double y1;
    double x2;
    double y2;

    Rectangle(double x1, double y1, double x2, double y2) {
        this.x1 = x1<x2?x1:x2;
        this.x2 = x1<x2?x2:x1;
        this.y1 = y1<y2?y1:y2;
        this.y2 = y1<y2?y2:y1;
    }

    public boolean contains(Point p){
        if(x1<=p.x && x2>=p.x && y1<=p.y && y2>=p.y){
            return true;
        }
        return false;
    }

    public boolean overlap(Rectangle r) {
        if( this.y2 <= r.y1 ){
            return false;
        }
        else if(this.y1 >= r.y2){
            return false;
        }
        else if(this.x2<= r.x1){
            return false;
        }
        else if(this.x1 >= r.x2){
            return false;
        }
        return true;
    }
}

class Shape implements caculatable {
    // zuo: the point means edge point rather than the so-called root point
    Point c;
    Point s;
    Point w;
    Point e;
    Point n;

    int depth;
    int maxDepth;
    DIRECTION direction;

    Shape(Point c, Point s, Point w, Point e, Point n, int depth, int maxDepth, DIRECTION direction) {
        this.c = c;
        this.s = s;
        this.w = w;
        this.e = e;
        this.n = n;
        this.depth=depth;
        this.maxDepth=maxDepth;
        this.direction = direction;
    }

    @Override
    public double getLength(Rectangle r) {
        if (r.contains(s) && r.contains(w) && r.contains(e) && r.contains(n)) {
            return this.getShapeLength();
        }
        else if(r.overlap( new Rectangle(w.x, s.y, e.x, n.y))){
            double offset = this.getNonFinalLength() / 9.0;
            Line l;
            Point westCenter;
            Point eastCenter;
            Point southCenter;
            Point northCenter;

            Shape east;
            Shape west;
            Shape north;
            Shape south;

            switch (this.direction){
                case NORTH:
                    l = new Line(s,c);

                    westCenter = c.move(DIRECTION.WEST, offset*2.0);
                    west = new Shape(westCenter,westCenter.move(DIRECTION.SOUTH, offset), this.w, this.c, westCenter.move(DIRECTION.NORTH, offset), this.depth+1, this.maxDepth, DIRECTION.WEST);

                    eastCenter = c.move(DIRECTION.EAST, offset*2.0);
                    east = new Shape(eastCenter,eastCenter.move(DIRECTION.SOUTH, offset), this.c, this.e, eastCenter.move(DIRECTION.NORTH, offset), this.depth+1, this.maxDepth, DIRECTION.EAST);

                    northCenter = c.move(DIRECTION.NORTH, offset*2.0);
                    north = new Shape(northCenter,this.c, northCenter.move(DIRECTION.WEST, offset), northCenter.move(DIRECTION.EAST, offset), this.n, this.depth+1, this.maxDepth, DIRECTION.NORTH);

                    return l.getLength(r)+west.getLength(r)+east.getLength(r)+north.getLength(r);

                case WEST:
                    l = new Line(e,c);

                    westCenter = c.move(DIRECTION.WEST, offset*2.0);
                    west = new Shape(westCenter,westCenter.move(DIRECTION.SOUTH, offset), this.w, this.c, westCenter.move(DIRECTION.NORTH, offset), this.depth+1, this.maxDepth, DIRECTION.WEST);

                    northCenter = c.move(DIRECTION.NORTH, offset*2.0);
                    north = new Shape(northCenter,this.c, northCenter.move(DIRECTION.WEST, offset), northCenter.move(DIRECTION.EAST, offset), this.n, this.depth+1, this.maxDepth, DIRECTION.NORTH);

                    southCenter = c.move(DIRECTION.SOUTH, offset*2.0);
                    south = new Shape(southCenter,this.s, southCenter.move(DIRECTION.WEST, offset), southCenter.move(DIRECTION.EAST, offset), this.c, this.depth+1, this.maxDepth, DIRECTION.SOUTH);

                    return l.getLength(r)+west.getLength(r)+south.getLength(r)+north.getLength(r);

                case SOUTH:
                    l = new Line(n,c);

                    westCenter = c.move(DIRECTION.WEST, offset*2.0);
                    west = new Shape(westCenter,westCenter.move(DIRECTION.SOUTH, offset), this.w, this.c, westCenter.move(DIRECTION.NORTH, offset), this.depth+1, this.maxDepth, DIRECTION.WEST);

                    eastCenter = c.move(DIRECTION.EAST, offset*2.0);
                    east = new Shape(eastCenter,eastCenter.move(DIRECTION.SOUTH, offset), this.c, this.e, eastCenter.move(DIRECTION.NORTH, offset), this.depth+1, this.maxDepth, DIRECTION.EAST);

                    southCenter = c.move(DIRECTION.SOUTH, offset*2.0);
                    south = new Shape(southCenter,this.s, southCenter.move(DIRECTION.WEST, offset), southCenter.move(DIRECTION.EAST, offset), this.c, this.depth+1, this.maxDepth, DIRECTION.SOUTH);

                    return l.getLength(r)+west.getLength(r)+east.getLength(r)+south.getLength(r);

                case EAST:
                    l = new Line(w,c);

                    eastCenter = c.move(DIRECTION.EAST, offset*2.0);
                    east = new Shape(eastCenter,eastCenter.move(DIRECTION.SOUTH, offset), this.c, this.e, eastCenter.move(DIRECTION.NORTH, offset), this.depth+1, this.maxDepth, DIRECTION.EAST);

                    northCenter = c.move(DIRECTION.NORTH, offset*2.0);
                    north = new Shape(northCenter,this.c, northCenter.move(DIRECTION.WEST, offset), northCenter.move(DIRECTION.EAST, offset), this.n, this.depth+1, this.maxDepth, DIRECTION.NORTH);

                    southCenter = c.move(DIRECTION.SOUTH, offset*2.0);
                    south = new Shape(southCenter,this.s, southCenter.move(DIRECTION.WEST, offset), southCenter.move(DIRECTION.EAST, offset), this.c, this.depth+1, this.maxDepth, DIRECTION.SOUTH);

                    return l.getLength(r)+east.getLength(r)+south.getLength(r)+north.getLength(r);
                default:
                    return 0.0;
            }
        }
        else{
            return 0.0;
        }
    }

    private double getNonFinalLength() {
        double ns = n.to(s);
        double we = w.to(e);
        return ns>we?ns:we;
    }

    /**
     * If this shape is completely contained, how long it will contain
     * */
    private  double getShapeLength(){
        return this.getNonFinalLength()*2.0*(this.maxDepth-this.depth)/3.0 + this.getNonFinalLength();
    }
}