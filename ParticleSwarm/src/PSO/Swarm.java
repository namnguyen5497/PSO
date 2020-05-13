package PSO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents a swarm of particles from the Particle Swarm Optimization algorithm.
 */
public class Swarm {

    private int numOfParticles, epochs;
    private double inertia_max, inertia_min, cognitiveComponent, socialComponent;
    private int workLoad;
    private static int nodes;
    private static Vector currentWorkload;
    private static Function model;
    private Vector bestPosition;
    private double bestEval;
    public static final double INERTIA_MAX = 0.9;
    public static final double INERTIA_MIN = 0.4;
    public static final double DEFAULT_COGNITIVE = 1; // Cognitive component.
    public static final double DEFAULT_SOCIAL = 1.05; // Social component.
    
    public static final double INFINITY = Double.POSITIVE_INFINITY;
   
    /**
     * Construct the Swarm with default values.
     * @param particles     the number of particles to create
     * @param epochs        the number of generations
     */
    public Swarm (int particles, int epochs, int nodes, int workLoad, Vector currentWorkload) {
        this(particles, epochs, nodes, INERTIA_MAX, INERTIA_MIN, DEFAULT_COGNITIVE, DEFAULT_SOCIAL,
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
    public Swarm (int particles, int epochs,int nodes, double inertia_max, double inertia_min, 
    													double cognitive, double social,
    													int workLoad, Vector currentWorkload) {
        this.numOfParticles = particles;
        this.epochs = epochs;
        this.nodes = nodes;
        this.inertia_max = inertia_max;
        this.inertia_min = inertia_min;
        this.cognitiveComponent = cognitive;
        this.socialComponent = social;
        this.workLoad = workLoad;
        this.currentWorkload = currentWorkload;
        bestPosition = new Vector(nodes);
        bestPosition.setSingleValue(INFINITY);
        bestEval = INFINITY;
       
        model = new Function(nodes); //model 
    }

    /**
     * Execute the algorithm.
     */
    public void run () {
        Particle[] particles = initialize();

        double oldEval = bestEval;
        System.out.println("--------------------------EXECUTING-------------------------");
        System.out.println("Global Best Evaluation (Epoch " + 0 + "):\t"  + bestEval);
        System.out.println("---------------------------------------------------------------");
        for (int i = 0; i < epochs; i++) {
        	//update inertia 
        	double inertia = inertia_max - (((inertia_max - inertia_min)*(i+1)) / epochs);

            if (bestEval < oldEval) {
            	System.out.println("---------------------------------------------------------------");
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
        System.out.println("Best position is: " + bestPosition.toStringOutput());
        Particle bestParticle = new Particle("bestParticle", nodes);
        bestParticle.setPosition(bestPosition);
        
        System.out.println(model.mainFunction(bestParticle, workLoad, currentWorkload).toStringOutput());
        System.out.println("max Time = " + model.mainFunction(bestParticle, workLoad, currentWorkload).getBiggestResult());
        
        System.out.println("Final Best Evaluation: " + bestEval);
        System.out.println("Final workload ratio: ");
        Map<String, Integer> map = mappingRatio(bestParticle, workLoad);
        for(String key : map.keySet()){
        	System.out.println(key + " " + map.get(key));
        }
        System.out.println("---------------------------COMPLETE-------------------------");

    }

    /**
     * Create a set of particles, each with random starting positions.
     * @return  an array of particles
     */
    private Particle[] initialize () {
        Particle[] particles = new Particle[numOfParticles];
        for (int i = 0; i < numOfParticles; i++) {
            Particle particle = new Particle("p"+i, nodes);
            System.out.println("Particle" + i + ": " + particle.getPosition().toStringOutput());
            										
            double initialEval =  model.mainFunction(particle, workLoad, currentWorkload).getSum();
            System.out.println("particle"+i + " initial eval: " + initialEval );
            particle.updatePersonalBest(initialEval);;
            //Checked sum = 100 
            particles[i] = particle;
            updateGlobalBest(particle);
        }
        return particles;
    }
    
    private double eval(Particle p){
    	double eval = model.mainFunction(p, workLoad, currentWorkload).getSum();

    	if(Function.constraintF1(p))
    		eval = INFINITY;
    	
    	if(Function.constraintF2(p))
    		eval = INFINITY;

    	if(Function.constraintF3(p, workLoad , currentWorkload ))
    		eval = INFINITY;
    	
    	if(Function.constraintF4(p, workLoad))
    		eval = INFINITY;
    	System.out.println("Evaluation of particle " + p.getName()+": " + eval);
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
            System.out.println("Gbest: " + bestPosition.toString());
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
        pBest.sub(pos.getVectorCoordinate());
        pBest.mul(cognitiveComponent);
        pBest.mul(r1);
        newVelocity.add(pBest.getVectorCoordinate());

        // The third product of the formula.
        gBest.sub(pos.getVectorCoordinate());
        gBest.mul(socialComponent);
        gBest.mul(r2);
        newVelocity.add(gBest.getVectorCoordinate());
        System.out.println("New Velocity of particle " + particle.getName());
        particle.setVelocity(newVelocity);
    }
    
    private static Map<String, Integer> mappingRatio (Particle bestParticle, int workLoad){
    	Map<String, Integer> workLoadRatio = new HashMap<String, Integer>();
    	Vector result_workload = bestParticle.getPosition(); //cloning
    	result_workload.mul(1000);
    	System.out.println("Inside mapping: " + result_workload.toStringOutput());
    	double[] ratio = result_workload.getVectorCoordinate();
    	
    	
    	int sum = 0;
    	for(int i = 1; i < ratio.length; i++){ //skip 0 because 0 is for Manager already
    		workLoadRatio.put("worker-"+i+"-id", (int)Math.round(ratio[i]));
    		sum += (int)Math.round(ratio[i]);
    	}
    	
    	workLoadRatio.put("Manager", workLoad - sum);
    	return workLoadRatio;
    }
    

}