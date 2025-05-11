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
	private final double a;
	private final double b;


	public Uniform(RNG rng, double a, double b) {
		super(rng);
		this.a = a;
		this.b = b;
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
		throw new UnsupportedOperationException("setMean is not supported for uniform distributions");

	}

	@Override
	public void setStdDeviation(double s) {
		throw new UnsupportedOperationException("setStdDeviation is not supported for uniform distributions");

	}

	@Override
	public void setMeanAndStdDeviation(double m, double s) {
		throw new UnsupportedOperationException("setMeanAndStdDeviation is not supported for uniform distributions");


	}

	@Override
	public String getType() {

		return "Uniform";
	}

	@Override
	public String toString() {
		return String.format(
				"Uniform distribution with parameters a: %.4f, b: %.4f\nMean: %.4f\nVariance: %.4f",
				a, b, getMean(), getVariance()
		);
	}
	
}
