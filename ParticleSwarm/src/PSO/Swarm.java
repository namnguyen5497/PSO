package PSO;

import java.util.Random;

/**
 * Represents a swarm of particles from the Particle Swarm Optimization algorithm.
 */
public class Swarm {

    private int numOfParticles, epochs;
    private double inertia_max, inertia_min, cognitiveComponent, socialComponent;
    private static int workLoad;
    private static Vector currentWorkload;
    
    private Vector bestPosition;
    private double bestEval;
    public static final double INERTIA_MAX = 0.9;
    public static final double INERTIA_MIN = 0.4;
    public static final double DEFAULT_COGNITIVE = 2; // Cognitive component.
    public static final double DEFAULT_SOCIAL = 2.05; // Social component.
    
    public static final double INFINITY = Double.POSITIVE_INFINITY;
   
    /**
     * Construct the Swarm with default values.
     * @param particles     the number of particles to create
     * @param epochs        the number of generations
     */
    public Swarm (int particles, int epochs, int workLoad, Vector currentWorkload) {
        this(particles, epochs, INERTIA_MAX, INERTIA_MIN, DEFAULT_COGNITIVE, DEFAULT_SOCIAL,
        		workLoad, currentWorkload);
    }

    /**
     * Construct the Swarm with custom values.
     * @param particles     the number of particles to create
     * @param epochs        the number of generations
     * @param inertia       the particles resistance to change
     * @param cognitive     the cognitive component or introversion of the particle
     * @param social        the social component or extroversion of the particle
     */
    public Swarm (int particles, int epochs, double inertia_max, double inertia_min, 
    																		double cognitive, double social,
    																		int workLoad, Vector currentWorkload) {
        this.numOfParticles = particles;
        this.epochs = epochs;
        this.inertia_max = inertia_max;
        this.inertia_min = inertia_min;
        this.cognitiveComponent = cognitive;
        this.socialComponent = social;
        this.workLoad = workLoad;
        this.currentWorkload = currentWorkload;
        bestPosition = new Vector(INFINITY, INFINITY, INFINITY);
        bestEval = INFINITY;
    }

    /**
     * Execute the algorithm.
     */
    public void run () {
        Particle[] particles = initialize();

        double oldEval = bestEval;
        System.out.println("--------------------------EXECUTING-------------------------");
        System.out.println("Global Best Evaluation (Epoch " + 0 + "):\t"  + bestEval);

        for (int i = 0; i < epochs; i++) {
        	//update inertia 
        	double inertia = inertia_max - (((inertia_max - inertia_min)*(i+1)) / epochs);

            if (bestEval < oldEval) {
                System.out.println("Global Best Evaluation (Epoch " + (i + 1) + "):\t" + bestEval);
                oldEval = bestEval;
            }

            for (Particle p : particles) {
            	double eval = eval(p);
                p.updatePersonalBest(eval);
                updateGlobalBest(p);
            }

            for (Particle p : particles) {
                updateVelocity(p, inertia);
                p.updatePosition();
            }
            
        }

        System.out.println("---------------------------RESULT---------------------------");
        System.out.println("x = " + bestPosition.getX() + " y = " + bestPosition.getY() + " z = " + bestPosition.getZ());
        
        System.out.println("Final Best Evaluation: " + bestEval);
        System.out.println("---------------------------COMPLETE-------------------------");

    }

    /**
     * Create a set of particles, each with random starting positions.
     * @return  an array of particles
     */
    private Particle[] initialize () {
        Particle[] particles = new Particle[numOfParticles];
        for (int i = 0; i < numOfParticles; i++) {
            Particle particle = new Particle();
            System.out.println("Particle: " + particle.getPosition().getX() + 
            											" " + particle.getPosition().getY()+ 
            											" " + particle.getPosition().getZ());
            //Checked sum = 100 
            particles[i] = particle;
            updateGlobalBest(particle);
        }
        return particles;
    }
    
    private double eval(Particle p){
    	double eval = Function.mainFunction(p, workLoad, currentWorkload);
    	if(Function.constraintF1(p)){
    		eval = INFINITY;
    	}
    	if(Function.constraintF2(p)){
    		eval = INFINITY;
    	}

    	if(Function.constraintF3(p, workLoad , currentWorkload )){
    		eval = INFINITY;
    	}
    	return eval;
    }

    /**
     * Update the global best solution if a the specified particle has
     * a better solution
     * @param particle  the particle to analyze
     */
    private void updateGlobalBest (Particle particle) {
        if (particle.getBestEval() < bestEval) {
            bestPosition = particle.getBestPosition();
            bestEval = particle.getBestEval();
        }
    }

    /**
     * Update the velocity of a particle using the velocity update formula
     * @param particle  the particle to update
     */
    private void updateVelocity (Particle particle, double inertia) {
        Vector oldVelocity = particle.getVelocity();
        Vector pBest = particle.getBestPosition();
        Vector gBest = bestPosition.clone();
        Vector pos = particle.getPosition();

        Random random = new Random();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        // The first product of the formula.
        Vector newVelocity = oldVelocity.clone();
        newVelocity.mul(inertia);

        // The second product of the formula.
        pBest.sub(pos);
        pBest.mul(cognitiveComponent);
        pBest.mul(r1);
        newVelocity.add(pBest);

        // The third product of the formula.
        gBest.sub(pos);
        gBest.mul(socialComponent);
        gBest.mul(r2);
        newVelocity.add(gBest);

        particle.setVelocity(newVelocity);
    }

}