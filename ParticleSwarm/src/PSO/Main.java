package PSO;

import java.util.Scanner;

public class Main {

    public static void main (String[] args) {

    	
    	System.out.println("Default Inertia, Cognitive and Social Components.");
    	System.out.println("Inertia:             " + Swarm.INERTIA_MAX); // initial value 
    	System.out.println("Cognitive Component: " + Swarm.DEFAULT_COGNITIVE);
    	System.out.println("Social Component:    " + Swarm.DEFAULT_SOCIAL);
    	int particles = getUserInt("Particles: ");
        int epochs = getUserInt("Epochs:    ");
        int workLoad = 1000;
        Vector currentWorkload = new Vector(500,0,0);
        
        Swarm swarm = new Swarm(particles, epochs, workLoad, currentWorkload);
        swarm.run();
    }


    private static int getUserInt (String msg) {
        int input;
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print(msg);

            if (sc.hasNextInt()) {
                input = sc.nextInt();

                if (input <= 0) {
                    System.out.println("Number must be positive.");
                } else {
                    break;
                }

            } else {
                System.out.println("Invalid input.");
            }
        }
        return input;
    }

    private static double getUserDouble (String msg) {
        double input;
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print(msg);

            if (sc.hasNextDouble()) {
                input = sc.nextDouble();

                if (input <= 0) {
                    System.out.println("Number must be positive.");
                } else {
                    break;
                }

            } else {
                System.out.println("Invalid input.");
            }
        }
        return input;
    }

}
