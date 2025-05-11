package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

/*
 * TODO Problem 3.1.2 - implement this class (section 3.2.3 in course syllabus)
 * !!! If an abstract class method does not make sense to be implemented in a particular RandVar class,
 * an UnsupportedOperationException should be thrown !!!
 *
 * Erlang-k distributed random variable.
 */
public class ErlangK extends RandVar {

	private final int k;
	private final double lambda;

	public ErlangK(RNG rng, int k, double lambda) {
		super(rng);
		this.k = k;
		this.lambda = lambda;

	}



	@Override
	public double getRV() {
		double factor = 1.0 / lambda;
		double uProd = rng.rnd();
		for (int i = 1; i < k; i++) {
			uProd *= rng.rnd();
		}
		return -1.0 * factor * Math.log(uProd);
	}

	@Override
	public double getMean() {
		return k/lambda;
	}

	@Override
	public double getVariance() {
		// TODO Auto-generated method stub
		return k/Math.pow(lambda, 2);
	}

	@Override
	public void setMean(double m) {
		throw new UnsupportedOperationException("setMean is not supported for ErlangK distributions");


	}

	@Override
	public void setStdDeviation(double s) {
		throw new UnsupportedOperationException("setStdDeviation is not supported for ErlangK distributions");


	}

	@Override
	public void setMeanAndStdDeviation(double m, double s) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("setMeanAndStdDeviation is not supported for ErlangK distributions");


	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "ErlangK";
	}

	@Override
	public String toString() {
		return String.format(
				"ErlangK distribution with parameters k: %d, λ: %.4f\nMean: %.4f, Variance: %.4f",
				k, lambda, getMean(), getVariance()
		);
	}

}
