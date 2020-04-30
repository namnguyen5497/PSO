package PSO;

class Function {

    /**
     * Calculate the result of (x^4)-2(x^3).
     * Domain is (-infinity, infinity).
     * Minimum is -1.6875 at x = 1.5.
     * @param x     the x component
     * @return      the y component
     */
    static double functionA (double x) {
        return Math.pow(x, 4) - 2 * Math.pow(x, 3);
    }

    /**
     * Perform Ackley's function.
     * Domain is [5, 5]
     * Minimum is 0 at x = 0 & y = 0.
     * @param x     the x component
     * @param y     the y component
     * @return      the z component
     */
    static double ackleysFunction (double x, double y) {
        double p1 = -20*Math.exp(-0.2*Math.sqrt(0.5*((x*x)+(y*y))));
        double p2 = Math.exp(0.5*(Math.cos(2*Math.PI*x)+Math.cos(2*Math.PI*y)));
        return p1 - p2 + Math.E + 20;
    }

    /**
     * Perform Booth's function.
     * Domain is [-10, 10]
     * Minimum is 0 at x = 1 & y = 3.
     * @param x     the x component
     * @param y     the y component
     * @return      the z component
     */
    static double boothsFunction (double x, double y) {
        double p1 = Math.pow(x + 2*y - 7, 2);
        double p2 = Math.pow(2*x + y - 5, 2);
        return p1 + p2;
    }

    /**
     * Perform the Three-Hump Camel function.
     * @param x     the x component
     * @param y     the y component
     * @return      the z component
     */
    static double threeHumpCamelFunction (double x, double y) {
        double p1 = 2*x*x;
        double p2 = 1.05*Math.pow(x, 4);
        double p3 = Math.pow(x, 6) / 6;
        return p1 - p2 + p3 + x*y + y*y;
    }
    
    static Vector myFunction(int workLoad, Vector p, int currentWorkload){
    	/**
         * @param workLoad 			number of images from 1 service
         * @param p    				percent workload
         * @param Theta				share service or not ( 0 or 1)
         * @param currentWorkload	current workload at that node
         * @return      			total time
         */
    	Vector p_temp = p.clone(); // for 2nd part
    	
    	final double avgImageSize = 5413.26287; //bytes
    	double scpRate = 0.000187 + Math.pow(workLoad*avgImageSize, 0.75429); // testSCP_26.04
    	
    	//1st part: p*Wi / Bji
    	p.mul(Double.valueOf(workLoad)); 
    	p.div(scpRate*1000000);  // bytes / (Mbps*1,000,000)
    	
    	//2nd part: 1.2855 + 0.04928*p*workLoad (test_detect_pi_26.04)
    	p_temp.mul(Double.valueOf(workLoad));
    	p_temp.mul(0.04928);
    	p_temp.add(new Vector(1.2855, 1.2855, 1.2855));
    	
    	//3rd part: Theta*(1.28550+0.04928*currentWorkload)
    	Vector Theta = new Vector(1,1,1);
    	
    	if(p.getX() == 0)
    		Theta.setX(0);
    	if(p.getY() == 0)
    		Theta.setY(0);
    	if(p.getZ() == 0)
    		Theta.setZ(0);
    	Theta.mul(1.28550+0.04928*currentWorkload); 
    	
    	//4th part: TsendResult Detect_on_worker_27.03 (T6)
    	Vector T_result = new Vector(0.047675,0.047675,0.047675); //TsendResult Detect_on_worker_27.03 (T6)
    	
    	
    	//Sum of 4 parts:
    	p.add(p_temp);
    	p.add(Theta);
    	p.add(T_result);
    	
    	return p;
    }

}