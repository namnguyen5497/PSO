package PSO;

import java.util.Random;

/**
 * Represents a particle from the Particle Swarm Optimization algorithm.
 */
class Particle {

    private Vector position;        // Current position.
    private Vector velocity;
    private Vector bestPosition;    // Personal best solution.
    private double bestEval;        // Personal best value.
  
    /**
     * Construct a Particle with a random starting position.
     * @param beginRange    the minimum xyz values of the position (inclusive)
     * @param endRange      the maximum xyz values of the position (exclusive)
     */
    Particle () {
        position = new Vector();
        velocity = new Vector();
        setRandomPosition();
        bestPosition = position.clone();
        bestEval = eval();
    }

    /**
     * The evaluation of the current position.
     * @return      the evaluation
     */
    private double eval () {
    	Vector currentWorkload = new Vector(100, 0 , 0);
    	double eval = Function.mainFunction(this, 100, currentWorkload);
    	//double length = Math.sqrt(Math.pow(eval.getX(),2) + Math.pow(eval.getY(),2) + Math.pow(eval.getZ(),2));
        return eval;
        //return length;
    }

    private void setRandomPosition () {
        double x = rand();
        double y = rand();
        double z = rand();
        if(x == 0 && x == y && y == z){
        	position.set(0, 0, 0);
        }else{
        	position.set(x/(x+y+z), y/(x+y+z), z/(x+y+z));
        }
        
    }

    /**
     * Generate a random number between 0.0 and 1.0.
     * @return              the randomly generated value
     */
    private static double rand () {
        Random r = new java.util.Random();
        return r.nextDouble(); //generate random from [0.0,1.0)
    }

    /**
     * Update the personal best if the current evaluation is better.
     */
    void updatePersonalBest (double eval) {
    	if (eval < bestEval) {
    		bestPosition = position.clone();
    		bestEval = eval;
    	}
    }

    /**
     * Get a copy of the position of the particle.
     * @return  the x position
     */
    Vector getPosition () {
        return position.clone();
    }

    /**
     * Get a copy of the velocity of the particle.
     * @return  the velocity
     */
    Vector getVelocity () {
        return velocity.clone();
    }

    /**
     * Get a copy of the personal best solution.
     * @return  the best position
     */
    Vector getBestPosition() {
        return bestPosition.clone();
    }

    /**
     * Get the value of the personal best solution.
     * @return  the evaluation
     */
    double getBestEval () {
        return bestEval;
    }

    /**
     * Update the position of a particle by adding its velocity to its position.
     */
    void updatePosition () {
        this.position.add(velocity);
       // Vector currentP = this.position;
      //  this.position.set(currentP.getX() / (currentP.getX() + currentP.getY() + currentP.getZ())
        //							, currentP.getY() / (currentP.getX() + currentP.getY() + currentP.getZ())
       // 							, currentP.getZ() / (currentP.getX() + currentP.getY() + currentP.getZ()));
    }

    /**
     * Set the velocity of the particle.
     * @param velocity  the new velocity
     */
    void setVelocity (Vector velocity) {
        this.velocity = velocity.clone();
    }
    

}
