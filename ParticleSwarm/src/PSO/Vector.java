package PSO;

/**
 * Can represent a position as well as a velocity.
 */
class Vector {

    private double x, y, z;
    private double limit = Double.MAX_VALUE;

    Vector () {
        this(0, 0, 0);
    }

    Vector (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double getX () {
        return x;
    }

    double getY () {
        return y;
    }

    double getZ () {
        return z;
    }

    void set (double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setX (double x) {
        this.x = x;
    }

    public void setY (double y) {
        this.y = y;
    }

    public void setZ (double z) {
        this.z = z;
    }

    void add (Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        limit();
    }

    void sub (Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        limit();
    }

    void mul (double s) {
        x *= s;
        y *= s;
        z *= s;
        limit();
    }

    void div (double s) {
        x /= s;
        y /= s;
        z /= s;
        limit();
    }

    void normalize () {
        double m = mag();
        if (m > 0) {
            x /= m;
            y /= m;
            z /= m;
        }
    }

    private double mag () {
        return Math.sqrt(x*x + y*y);
    }

    void limit (double l) {
        limit = l;
        limit();
    }

    private void limit () {
        double m = mag();
        if (m > limit) {
            double ratio = m / limit;
            x /= ratio;
            y /= ratio;
        }
    }

    public Vector clone () {
        return new Vector(x, y, z);
    }

    public String toString () {
        return "(" + x + ", " + y + ", " + z + ")";
    }
    
    
    public double[] getVectorCoordinate(){
    	double[] coordinates = new  double[]{this.getX(), this.getY(), this.getZ()};
    	
    	return coordinates;
    }
    
    public double getBiggestResult(){
    	double max = this.getX();
    	if(max < this.getY())
    		max = this.getY();
    	if(max < this.getZ())
    		max = this.getZ();
    	
    	return max;
    }

}

