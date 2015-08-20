package binchat.graphing;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Polynomial {

    double[] terms;
    // these values are for determining desired accuracy for finding roots
    private static final double INTERVAL = 0.0001;
    private static final double RANGE = 100;

    public Polynomial(double[] terms) {
        this.terms = terms;
    }

    public double getTerm(int x) {
        return terms[x];
    }

    public int getHighestPower() {
        return this.terms.length - 1;
    }

    // given an x value, evaluate the function
    public double evaluate(double x) {
        double output = 0;
        for (int i = 0; i < this.terms.length; i++) {
            output += terms[i] * Math.pow(x, i);
        }
        return output;
    }

    // calculates the derivative of a function
    public Polynomial derivative() {
        double[] new_terms = new double[this.terms.length - 1];
        for (int i = 0; i < new_terms.length; i++) {
            new_terms[i] = this.terms[i + 1] * (i + 1);
        }
        return new Polynomial(new_terms);
    }

    // returns a string for the function
    public String getEquation() {
        DecimalFormat df = new DecimalFormat("#.##");
        String ret = "";
        for (int i = this.terms.length - 1; i >= 0; i--) {
            if (this.terms[i] == 1) ret = ret + "+";
            else if (this.terms[i] == -1) ret = ret + "-";
            else if (this.terms[i]>0) ret = ret + "+" + df.format(this.terms[i]);
            else if (this.terms[i] == 0) continue;
            else ret = ret + df.format(this.terms[i]);
            if (i == 1) ret = ret + "x";
            else if (i != 0) ret = ret + "x^" + i;
        }
        if (ret.charAt(0)=='+')ret = ret.substring(1,ret.length());
        ret = "y=" + ret;
        return ret;
    }

    // adds the polynomial with another polynomial
    public Polynomial add(Polynomial other) {
        int iterations;
        double[] new_terms;
        boolean other_is_longer;
        if (this.terms.length > other.terms.length) {
            other_is_longer = false;
            iterations = other.terms.length;
            new_terms = new double[this.terms.length];
        } else {
            other_is_longer = true;
            iterations = this.terms.length;
            new_terms = new double[other.terms.length];
        }
        for (int i = 0; i < iterations; i++) {
            new_terms[i] = this.terms[i] + other.terms[i];
        }
        if (other_is_longer) {
            for (int i = iterations; i < new_terms.length; i++) {
                new_terms[i] = other.terms[i];
            }
        } else {
            for (int i = iterations; i < new_terms.length; i++) {
                new_terms[i] = this.terms[i];
            }
        }
        return new Polynomial(new_terms);
    }

    // subtracts a polynomial from the current polynomial
    public Polynomial subtract(Polynomial other) {
        for (int i = 0; i < other.terms.length; i++) {
            other.terms[i] = -other.terms[i];
        }
        return this.add(other);
    }

    // multiplies this polynomial by another
    public Polynomial multiply(Polynomial other) {
        int degree = this.terms.length + other.terms.length - 1;
        double[] new_terms = new double[degree];
        // populate new_terms with 0s
        for (int i = 0; i < new_terms.length; i++) {
            new_terms[i] = 0;
        }
        for (int i = 0; i < this.terms.length; i++) {
            for (int j = 0; j < other.terms.length; j++) {
                int current_degree = i + j;
                new_terms[current_degree] += (this.terms[i] * other.terms[j]);
            }
        }
        return new Polynomial(new_terms);
    }

    // uses the quadratic formula to return real or complex roots in string form
    // complex roots are not necessarily in reduced form
    // returns empty array if no real roots with null values
    public String[] factorQuadratic() {
        String[] factors = new String[2];
        if(this.terms.length==3){
            double sqrt = Math.pow(this.terms[1], 2) - 4 * this.terms[0] * this.terms[2];
            if (sqrt >= 0) {
                factors[0] = ""+(-this.terms[1] + Math.sqrt(sqrt)) / (2 * this.terms[2]);
                factors[1] = ""+(-this.terms[1] - Math.sqrt(sqrt)) / (2 * this.terms[2]);
            }
            else{
                factors[0] = "-"+this.terms[1]+"+√("+(-sqrt)+")i/(" + (2*terms[2])+")";
                factors[1] = "-"+this.terms[1]+"-√("+(-sqrt)+")i/(" + (2*terms[2])+")";
            }
        }
        return factors;
    }

    public Rational divide(Polynomial other) {
        return new Rational(this, other);
    }

    // Get roots the brute force way, since we have the computing power.
    // scans over a range, iterating by intervals defined by the CONSTANTS at the top
    public ArrayList<Double> getRoots() {
        double ACCURACY = 0.01D;
        ArrayList<Double> roots = new ArrayList<Double>();
        ArrayList<Double> refined = new ArrayList<Double>();
        for (double i = -RANGE; i < RANGE; i += INTERVAL) {
            double y = this.evaluate(i);
            if (y > -ACCURACY && y < ACCURACY) {
                roots.add(i);
            }
        }
        //add a placeholder. this will never be checked by the loop and signifies the end of a loop.
        roots.add(3.1415926535);
        double counter = 0;
        double total = 0;
        // the estimated roots will be in series of x values.
        // this will average the x values to get a more refined result, so as to not double count roots.
        // note this is not by any means a perfect method, as it can be skewed for result non symmetrical functions.
        for (int i = 0; i < roots.size()-1; i++) {
            if (roots.get(i) + INTERVAL != roots.get(i + 1)) {
                counter ++;
                total += roots.get(i);
                refined.add((double)Math.round((total/counter)*100)/100);
                counter = 0;
                total = 0;
            }
            else{
                counter++;
                total += roots.get(i);
            }
        }
        return refined;
    }

    // Mins and Maxes are calculated based on the roots of the derivative
    // by checking values left and right of the x value, it can be determined whether it is a min, max or IP
    public ArrayList<Double> calculateMaxsMins(){
        Polynomial derrivative = this.derivative();
        ArrayList<Double> maxsmins = derrivative.getRoots();
        ArrayList<Double> mins = new ArrayList<Double>();
        ArrayList<Double> maxs = new ArrayList<Double>();
        ArrayList<Double> ip = new ArrayList<Double>();

        for (int i = 0; i < maxsmins.size(); i++) {
            double lower = derrivative.evaluate(maxsmins.get(i)-0.1);
            double upper = derrivative.evaluate(maxsmins.get(i)+0.1);
            if (lower>0 && upper < 0) maxs.add(maxsmins.get(i));
            else if (lower<0 && upper > 0) mins.add(maxsmins.get(i));
            else ip.add(maxsmins.get(i));
        }
        //print maxes
        if(maxs.size()>0){
            System.out.print("Local maxes at: ");
            for (int i = 0; i < maxs.size(); i++) {
                System.out.print(maxs.get(i));
                if(i!=maxs.size()-1) System.out.print(", ");
            }
            System.out.println();
        }else System.out.println("No local maxes.");

        //print mins
        if(mins.size()>0){
            System.out.print("Local mins at: ");
            for (int i = 0; i < mins.size(); i++) {
                System.out.print(mins.get(i));
                if(i!=mins.size()-1) System.out.print(", ");
            }
            System.out.println();
        }else System.out.println("No local mins.");

        //print inflection points
        if(ip.size()>0){
            System.out.print("Inflection points at: ");
            for (int i = 0; i < ip.size(); i++) {
                System.out.print(ip.get(i));
                if(i!=ip.size()-1) System.out.print(", ");
            }
            System.out.println();
        }else System.out.println("No inflection points.");

        // Find absolute Maxs/Mins if they exist
        int index=-1;
        if(this.evaluate(100)>0 && this.evaluate(-100)>0){
            double lowest =this.evaluate(100);
            for (int i = 0; i < mins.size(); i++) {
               if(this.evaluate(mins.get(i))<lowest) {
                   index = i;
                   lowest = this.evaluate(mins.get(i));
               }
            }
            System.out.println("Absolute minimum = (" + mins.get(index) + ", " + Math.round(lowest*100)/100 + ")");
        }else System.out.println("No absolute minimum.");

        if(this.evaluate(100)<0 && this.evaluate(-100)<0){
            double highest =this.evaluate(100);
            for (int i = 0; i < maxs.size(); i++) {
                if(this.evaluate(maxs.get(i))>highest){
                    highest = this.evaluate(maxs.get(i));
                    index = i;
                }
            }
            System.out.println("Absolute maximum = (" + maxs.get(index) + ", " + Math.round(highest*100)/100 + ")");
        }else System.out.println("No absolute maximum.");
        //returns maxs, mins and ip in one arraylist
        return maxsmins;
    }

}
