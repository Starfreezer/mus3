/**
 * 
 */
package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

/*
 * TODO Problem 3.1.2 - implement this class (section 3.2.2 in course syllabus)
 * !!! If an abstract class method does not make sense to be implemented in a particular RandVar class,
 * an UnsupportedOperationException should be thrown !!!
 *
 * Expnential distributed random variable.
 */
public class Exponential extends RandVar {
	private final double lambda;

	public Exponential(RNG rng, double lambda) {
		super(rng);
		this.lambda = lambda;
		// TODO Auto-generated constructor stub
	}


	@Override
	public double getRV() {
		double u = rng.rnd();
		double x = (Math.log(u) / lambda);
		return x;
	}


	@Override
	public double getMean() {
		return 1/lambda;
	}

	@Override
	public double getVariance() {
		return 1/Math.pow(lambda, 2);
	}

	@Override
	public void setMean(double m) {
		throw new UnsupportedOperationException("setMean not supported on Exponential variables");

	}

	@Override
	public void setStdDeviation(double s) {
		throw new UnsupportedOperationException("setStdDeviation not supported on Exponential variables");


	}

	@Override
	public void setMeanAndStdDeviation(double m, double s) {
		throw new UnsupportedOperationException("setMeanAndStdDeviation not supported on Exponential variables");


	}

	@Override
	public String getType() {
		return "Exponential";
	}

	@Override
	public String toString() {
		return String.format(
				"Uniform distribution with parameters lambda: %.4f,\nMean: %.4f\nVariance: %.4f",
				lambda, getMean(), getVariance()
		);
	}
	
}
