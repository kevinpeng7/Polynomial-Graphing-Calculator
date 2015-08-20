package binchat.graphing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Rational {
    Polynomial numerator, denominator;

    public Rational(Polynomial num, Polynomial den) {
        this.numerator = num;
        this.denominator = den;
    }

    // Evaluates the function a x value
    public double evaluate(double x) {
        double num = numerator.evaluate(x);
        double den = denominator.evaluate(x);
        if (den == 0) {
            throw new IllegalArgumentException("Argument 'divisor' is 0");
        }
        return num / den;
    }

    // Return true if the Rational has a horizontal asymptote
    public boolean hasHorizontalAsym() {
        if (this.numerator.getHighestPower() - 1 >= this.denominator.getHighestPower()) {
            return false;
        }
        return true;
    }

    // Returns the horizontal asymptote or returns Not a Number if there isn't one
    public Double getHorizontalAsym() {
        if (this.numerator.getHighestPower() + 1 <= this.denominator.getHighestPower()) {
            return 0.0D;
        } else if (this.hasHorizontalAsym())
            return this.numerator.getTerm(0) / this.denominator.getTerm(0);
        else {
            return Double.NaN;
        }
    }

    // Returns all the verticle asymptotes to the function
    /*public List<Double> getVerticleAsym() {
        ArrayList<Double> asym = new ArrayList<Double>();
        for(Double root : this.denominator.getRealRoots()) {
            asym.add(-root);
        }
        return asym;
    }*/

    // returns a string for the function
    public String getEquation() {
        return  "(" + this.numerator.getEquation() + ")/(" + this.denominator.getEquation() + ")";
    }

}
