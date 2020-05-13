package PSO;

import java.util.Arrays;

class Function {
	
	private static int nodes;
	
	public Function(int nodes){
		this.nodes = nodes;
	}

    
    public Vector mainFunction(Particle p, int workLoad, Vector currentWorkload){
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
    	double[] coefficient = new double[nodes];
    	
    	//1st part: p*Wi / Bji
    	position.mul(workLoad*avgImageSize*0.0009);
    	Arrays.fill(coefficient,614.3);
    	position.add(coefficient); 
    	position.mul(0.001); // change to second
    	
    	//2nd part: 1.2855 + 0.04928*p*workLoad (test_detect_pi_26.04)
    	p_temp.mul(Double.valueOf(workLoad));
    	p_temp.mul(0.04928);
    	Arrays.fill(coefficient, 1.2855);
    	p_temp.add(coefficient);
    	
    	//3rd part: Theta*(1.28550+0.04928*currentWorkload)
    	
    	currentWorkload.mul(0.04928);
    	
    	Vector Theta = new Vector(nodes);
    	for(int i = 0; i < position.getVectorCoordinate().length; i++){
    		if(position.getPAt(i) != 0){
    			Theta.setPAt(i, 1.28550 + currentWorkload.getPAt(i));
    		}
    	}
    	
    	//4th part: TsendResult Detect_on_worker_27.03 (T6)
    	Arrays.fill(coefficient, 0.047675);
    	//TsendResult Detect_on_worker_27.03 (T6)
    	
    	
    	//Sum of 4 parts:
    	position.add(p_temp.getVectorCoordinate());
    	position.add(Theta.getVectorCoordinate());
    	position.add(coefficient);
    	//System.out.println("Tmax = " + p.getBiggestResult());
    	return position;
    	
    }
    /**
     * Sum pi = 1
     * @param p particle
     * @return true if not satisfy 
     */
    static boolean constraintF1(Particle p){
    	if(p.getPosition().getSum() != 1.0){
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
    	for(int i = 0; i<position.getVectorCoordinate().length; i++){
    		if(position.getVectorCoordinate()[i] < 0)
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
    	double averageWorkload = (workLoad + currentWorkload.getSum()) / p.getPosition().getVectorCoordinate().length; 
    	for (int i = 0; i<p.getPosition().getVectorCoordinate().length; i++){
    		if(p.getPosition().getVectorCoordinate()[i] > averageWorkload){
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
    	Vector position = p.getPosition();
    	position.mul(workLoad);
    	for(int i=0; i<position.getVectorCoordinate().length;i++){
    		if(position.getVectorCoordinate()[i] < 6)
    			return true;
    	}
    	return false;
    }
}