package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

/*
 * TODO Problem 3.1.2 - implement this class (section 3.2.1 in course syllabus)
 * !!! If an abstract class method does not make sense to be implemented in a particular RandVar class,
 * an UnsupportedOperationException should be thrown !!!
 *
 * Uniform distributed random variable.
 */
public class Uniform extends RandVar {
	private  double a;
	private  double b;


	public Uniform(double a, double b, RNG rng) {
		super(rng);
		if(a >= b)
			throw new IllegalArgumentException("a must be less than b for uniform distribution");
		this.a = a;
		this.b = b;
	}

	public Uniform(RNG rng, double mean, double cvar) {
		super(rng);
		this.a = mean * (1 - Math.sqrt(3) * cvar);
		this.b = mean * (1 + Math.sqrt(3) * cvar);
		if(a >= b)
			throw new IllegalArgumentException("a must be less than b for uniform distribution");
	}

	public Uniform(RNG rng) {
		super(rng);
		this.a = 0;
		this.b = 2;
		setMean(1.0);
	}


	@Override
	public double getRV() {
		double u = rng.rnd();
		double x = a + (b -a) * u;
		return x;
	}

	@Override
	public double getMean() {
		return (a + b) / 2;
	}

	@Override
	public double getVariance() {
		return (Math.pow((b - a), 2))/12;
	}

	@Override
	public void setMean(double m) {
		double range = b - a;
		this.a = m - range / 2;
		this.b = m + range / 2;

		if (a >= b) {
			throw new IllegalArgumentException("a must be less than b for uniform distribution.");
		}
	}

	@Override
	public void setStdDeviation(double s) {
		if (s <= 0) {
			throw new IllegalArgumentException("Standard deviation must be > 0 for uniform distribution.");
		}

		double mean = getMean(); // (a + b)/2
		double delta = (s * Math.sqrt(12)) / 2;

		this.a = mean - delta;
		this.b = mean + delta;

		if(a >= b)
			throw new IllegalArgumentException("a must be less than b for uniform distribution.");
	}


	@Override
	public void setMeanAndStdDeviation(double m, double s) {
		setMean(m);
		setStdDeviation(s);
	}

	@Override
	public String getType() {
		return "Uniform distribution";
	}

	@Override
	public String toString() {
		return super.toString() +
			String.format("\tparameters:\n\t\ta: %.4f\n\t\tb: %.4f\n", this.a, this.b);
	}
	
}
