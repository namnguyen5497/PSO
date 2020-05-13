package PSO;

import java.util.Arrays;

/**
 * Can represent a position as well as a velocity.
 */

class Vector {
	
	private int nodes;
	private double[] p;
    private double limit = Double.MAX_VALUE;

    Vector (int nodes) {
    	this.nodes = nodes;
    	this.p = new double[nodes];
    	Arrays.fill(this.p, 0);
    }
    
    Vector (double[] v){
    	this.p = v;
    }

    double getPAt (int index) {
        return this.p[index];
    }


    void set (double[] v) {
        for(int i=0; i<p.length; i++){
        	this.p[i] = v[i];
        }
    }
    
    void setSingleValue (double v){
    	for(int i=0; i<p.length; i++){
        	this.p[i] = v;
        }
    }

    public void setPAt (int index, double value){
    	this.p[index] = value; 
    }

    void add (double[] v) {
    	for(int i=0; i<p.length; i++){
    		this.p[i] += v[i];
    	}
        limit();
    }

    void sub (double[] v) {
        for(int i=0; i<p.length; i++){
        	this.p[i] -= v[i];
        }
        limit();
    }

    void mul (double s) {
        for(int i=0; i<p.length; i++){
        	this.p[i] *= s;
        }
        limit();
    }

    void div (double s) {
    	for(int i=0; i<p.length; i++){
    		this.p[i] /= s;
    	}
        limit();
    }

    void normalize () {
        double m = mag();
        if (m > 0) {
        	for(int i=0; i<p.length; i++){
        		this.p[i] /= m;
        	}
        }
    }
    
    
    /*
    private double mag(){
    	double sum=0;
    	for (int i=0; i<p.length; i++){
    		sum += p[i];
    	}
    	return sum;
    }
    
    private void limit(){
    	double m = mag();
    	if(m > 100){
    		for(int i=0; i<p.length; i++){
    			this.p[i] /= m;
    		}
    	}
    }
     * 
     */
    private double mag () {
    	double sum=0;
    	for (int i=0; i<p.length; i++){
    		sum += p[i]*p[i];
    	}
    	return Math.sqrt(sum);
    }
    void limit (double l) {
    	limit = l;
    	limit();
    }
    
    
    private void limit () {
    	double m = mag();
    	if (m > limit) {
    		double ratio = m / limit;
    		for (int i=0; i<p.length; i++){
    			this.p[i] /= ratio ;
    		}
    	}
    }


    public Vector clone () {
    	Vector clone = new Vector(nodes);
    	clone.set(p);
        return clone;
    }


    public String toStringOutput () {
    	String output = "";
    	for(int i=0; i<p.length; i++){
    		output += "p"+i+" : " + p[i] + "\t";
    	}
        return output;
    }
    
    
    public double[] getVectorCoordinate(){
    	return p;
    }
    
    public double getBiggestResult(){
    	double max = p[0];
    	for(int i=1; i<p.length; i++){
    		if(max <= p[i])
    			max = p[i];		
    	}
    	return max;
    }
    
    public double getSum(){
    	double sum = 0;
    	for(int i=0; i<p.length; i++){
    		sum += p[i];
    	}
    	return sum;
    }

}

