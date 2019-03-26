package coursework;

import java.util.ArrayList;
import java.util.Random;

import model.Fitness;
import model.Individual;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;

/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * 
 * You Can Use This Class to implement your EA or implement your own class that extends {@link NeuralNetwork} 
 * 
 */
public class ExampleEvolutionaryAlgorithm extends NeuralNetwork {
	

	/**
	 * The Main Evolutionary Loop
	 */
	@Override
	public void run() {		
		//Initialise a population of Individuals with random weights
		population = initialise();

		//Record a copy of the best Individual in the population
		best = getBest();
		System.out.println("Best From Initialisation " + best);

		/**
		 * main EA processing loop
		 */		
		
		while (evaluations < Parameters.maxEvaluations) {

			/**
			 * this is a skeleton EA - you need to add the methods.
			 * You can also change the EA if you want 
			 * You must set the best Individual at the end of a run
			 * 
			 */

			// Select 2 Individuals from the current population. Currently returns random Individual
		//	Individual parent1 = select(); 
		//	Individual parent2 = select();
			
			Individual parent1 = tournament_selection();
			Individual parent2 = tournament_selection();

			// Generate a child by crossover. Not Implemented			
			//ArrayList<Individual> children = reproduce(parent1, parent2);			
			ArrayList<Individual>children = singleP_reproduce(parent1,parent2);
			//mutate the offspring
			mutate(children);
			
			// Evaluate the children
			evaluateIndividuals(children);			

			// Replace children in population
			replace(children);

			// check to see if the best has improved
			best = getBest();
			
			// Implemented in NN class. 
			outputStats();
			
			//Increment number of completed generations			
		}

		//save the trained network to disk
		saveNeuralNetwork();
	}

	

	/**
	 * Sets the fitness of the individuals passed as parameters (whole population)
	 * 
	 */
	private void evaluateIndividuals(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}


	/**
	 * Returns a copy of the best individual in the population
	 * 
	 */
	private Individual getBest() {
		best = null;;
		for (Individual individual : population) {
			if (best == null) {
				best = individual.copy();
			} else if (individual.fitness < best.fitness) {
				best = individual.copy();
			}
		}
		return best;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			//chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}

	/**
	 * Selection --
	 * 
	 * NEEDS REPLACED with proper selection this just returns a copy of a random
	 * member of the population
	 */
	private Individual select() {		
		Individual parent = population.get(Parameters.random.nextInt(Parameters.popSize));
		return parent.copy();
	}

	public static int CreateRandomNumber(int number)
	{
		int x = (int) Math.random()*number;
		return x;
	}
	
	private Individual tournament_selection()
	{
		ArrayList <Individual> potentialrndParents = new ArrayList<Individual>();
		
		//pick random individuals from population to become potential parents
		for(int i=0; i<population.size();i++)
		{
			potentialrndParents.add(population.get(CreateRandomNumber(i)).copy());
		}
		//check fitness of randomly picked individuals
		
		Individual winner = new Individual();
		winner = null;
		for(Individual rndIndivi : potentialrndParents)
		{
			if(winner == null)
			{
				winner = rndIndivi.copy();
			}
			else if(rndIndivi.fitness <winner.fitness)
			{
				winner = rndIndivi.copy();
			}
		}
	//	System.out.println(winner);
		return winner;
	}
	
	private Individual rank_selection()
	{
		
		return winner;
	}
	
	private Individual roulette_selection()
	{
		
		return winner;
	}
	
	//SINGLE POINT CROSSOVER
	private ArrayList<Individual>singleP_reproduce(Individual parent1, Individual parent2)
	{
		//empty child array
		ArrayList<Individual> children = new ArrayList<>();
		
		
		
		//pick randomly a point in both parents
		Random myRandom = new Random();
		int cutPoint = myRandom.nextInt(parent1.chromosome.length);
		
		//cut both of the parents in chosen random points
		for(int i=0; i<cutPoint; i++)
		{
			children.add(parent1);
			
			//add genes from the second parent
			for(i = cutPoint; i<parent2.chromosome.length;i++)
			{
				children.add(parent2);
			}
		}
		
		//return the offspring 
		return children;
	}
	/**
	 * Crossover / Reproduction
	 * 
	 * NEEDS REPLACED with proper method this code just returns exact copies of the
	 * parents. 
	 */
	private ArrayList<Individual> reproduce(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		children.add(parent1.copy());
		children.add(parent2.copy());		
		return children;
	} 
	
	//TO TRY - SINGLE POINT CROSSOVER AND MULTIPOINT CROSSOVER - a.k.
	
	
	
	/**
	 * Mutation
	 * 
	 * 
	 */
	
	
	
	//TO TRY - INTEGER OR BINARY MUTATION - a.k.
	
	
	private void mutate(ArrayList<Individual> individuals) 
	{		
		for(Individual individual : individuals) {
			for (int i = 0; i < individual.chromosome.length; i++) {
				if (Parameters.random.nextDouble() < Parameters.mutateRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[i] += (Parameters.mutateChange);
					} else {
						individual.chromosome[i] -= (Parameters.mutateChange);
					}
				}
			}
		}		
	}

	//bit
	private void mutate_BitFlip(ArrayList<Individual> individuals)
	{
		for(Individual individual : individuals)
		{
			
		}
	}
	
	
	
	/**
	 * 
	 * Replaces the worst member of the population 
	 * (regardless of fitness)
	 * 
	 */
	private void replace(ArrayList<Individual> individuals) {
		for(Individual individual : individuals) {
			int idx = getWorstIndex();		
			population.set(idx, individual);
		}		
	}

	

	/**
	 * Returns the index of the worst member of the population
	 * @return
	 */
	private int getWorstIndex() {
		Individual worst = null;
		int idx = -1;
		for (int i = 0; i < population.size(); i++) {
			Individual individual = population.get(i);
			if (worst == null) {
				worst = individual;
				idx = i;
			} else if (individual.fitness > worst.fitness) {
				worst = individual;
				idx = i; 
			}
		}
		return idx;
	}	

	@Override
	public double activationFunction(double x) {
		if (x < -20.0) {
			return -1.0;
		} else if (x > 20.0) {
			return 1.0;
		}
		return Math.tanh(x);
	}
}
