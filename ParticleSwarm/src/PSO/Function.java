package PSO;

class Function {
	
	private Function(){}
    
    static Vector mainFunction(Particle p, int workLoad, Vector currentWorkload){
    	/**
         * @param workLoad 			number of images from 1 service
         * @param p    				percent workload
         * @param Theta				share service or not ( 0 or 1)
         * @param currentWorkload	current workload at that node
         * @return      			total time
         */
    	Vector position = p.getPosition().clone();
    	Vector p_temp = position.clone(); // for 2nd part
    	
    	final double avgImageSize = 5413.26287; //bytes
    	
    	
    	//1st part: p*Wi / Bji
    	position.mul(workLoad*avgImageSize*0.0009); 
    	position.add(new Vector(614.3, 614.3, 614.3));
    	position.mul(0.001); // change to second
    	
    	//2nd part: 1.2855 + 0.04928*p*workLoad (test_detect_pi_26.04)
    	p_temp.mul(Double.valueOf(workLoad));
    	p_temp.mul(0.04928);
    	p_temp.add(new Vector(1.2855, 1.2855, 1.2855));
    	
    	//3rd part: Theta*(1.28550+0.04928*currentWorkload)
    	Vector Theta = new Vector(0,0,0);
    	currentWorkload.mul(0.04928);
    	
    	if(position.getX() != 0)
    		Theta.setX(1.28550 + currentWorkload.getX());
    	if(position.getY() != 0)
    		Theta.setY(1.28550 + currentWorkload.getY());
    	if(position.getZ() != 0)
    		Theta.setZ(1.28550 + currentWorkload.getZ());
    	
    	//4th part: TsendResult Detect_on_worker_27.03 (T6)
    	Vector T_result = new Vector(0.047675,0.047675,0.047675); //TsendResult Detect_on_worker_27.03 (T6)
    	
    	
    	//Sum of 4 parts:
    	position.add(p_temp);
    	position.add(Theta);
    	position.add(T_result);
    	//System.out.println("Tmax = " + p.getBiggestResult());
    	return position;
    	
    }
    /**
     * Sum pi = 1
     * @param p particle
     * @return true if not satisfy 
     */
    static boolean constraintF1(Particle p){
    	if(p.getPosition().getX() + p.getPosition().getY() + p.getPosition().getZ() != 1.0){
    		//System.out.println("F1 = true");
    		return true;
    	}
    	return false;
    }
    /**
     * Pi >= 0
     * @param p
     * @return true if not satisfy
     */
    static boolean constraintF2(Particle p){
    	Vector position = p.getPosition();
    	if(position.getX() < 0 || position.getY() < 0 || position.getZ() < 0){
    		//System.out.println("F2 = true");
    		return true;
    	}
    	return false;
    }
    
    /**
     * pi*W <= (W + sum(N)) / n
     * @param p
     * @return  true if not satisfy 
     */
    static boolean constraintF3(Particle p, int workLoad, Vector currentWorkload){
    	
    	double averageWorkload = (workLoad + currentWorkload.getX() + currentWorkload.getY() + currentWorkload.getZ()) / 3; 
    	for (int i = 0; i<p.getPosition().getVectorCoordinate().length; i++){
    		if(p.getPosition().getVectorCoordinate()[i] > averageWorkload){
    			//System.out.println("F3 = true at " + i);
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * pW >= aW 
     * @param p
     * @param workLoad
     * @return
     */
    static boolean constraintF4(Particle p, int workLoad){
    	return false;
    }
}