package binchat.parser;

import binchat.BinChat;
import binchat.graphing.Polynomial;
import binchat.graphing.TemporaryWindow;
import com.sun.javafx.scene.control.skin.DoubleFieldSkin;

import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ParserManager {
    TemporaryWindow temporaryWindow;
    DecimalFormat decimalFormat;
    public ParserManager(){
        temporaryWindow = new TemporaryWindow();
        temporaryWindow.initialize();
        decimalFormat = new DecimalFormat("#.##");
    }
    // takes the initial input line, and checks whether it should be dealt with by the command parser or sent to the chat
    public void parseChatLine(String chat_line){
        if(chat_line.length()>0){
            if(chat_line.substring(0,1).equals("/")){
                chat_line = chat_line.substring(1,chat_line.length());
                //sends the string to the command parser below
                commandParser(chat_line);
            }
            else {
                // by default, message will not send, server is not working
                System.out.println("SENDING MESSAGE: " + chat_line);
                System.out.println(". . ."); // send message to server
                System.out.println("Message not sent."); // if fails, print message not sent
            }
        }
    }
    // parses a string into its parameters, delimited by commas
    public ArrayList<String> parameters(String chat_line){
        ArrayList<String> paras = new ArrayList<String>();
        int currentIndex = 0;
        chat_line = chat_line.replace(" ", "");
        while(chat_line.contains(",")){
            paras.add(chat_line.substring(0,chat_line.indexOf(",")));
            chat_line = chat_line.substring(chat_line.indexOf(",")+1, chat_line.length());
        }
        if(chat_line.length()>0) paras.add(chat_line);
        return paras;
    }
    // after the slash are removed, check if the command exists and if the correct parameters exist
    public void commandParser(String chat_line){
        if (chat_line.length()!=0){
            // finds the command keyword
            int endWord = chat_line.indexOf(" ");
            if (endWord==-1) endWord = chat_line.length();
            //saves the command
            String command = chat_line.substring(0, endWord).toLowerCase()
                    .replace("kick", "disconnect").replace("calculate", "evaluate");

            // Graphs the polynomial on the screen, takes 5 parameters: function,x-min,x-max,y-min,y-max
            if(command.equals("graph")){
                int PARAMETERS = 5;
                chat_line = chat_line.replace(" ","").replace("graph", "");
                if(chat_line.length()>0){
                    ArrayList<String> parameters = parameters(chat_line);
                    if(parameters.size()>=PARAMETERS){
                        temporaryWindow.display(mathParser(parameters.get(0)),
                                Double.parseDouble(parameters.get(1)),
                                Double.parseDouble(parameters.get(2)),
                                Double.parseDouble(parameters.get(3)),
                                Double.parseDouble(parameters.get(4)));
                    }
                    else if(parameters.size()==1){
                        //makes a guess at what a suitable window might be
                        Polynomial p = mathParser(parameters.get(0));
                        ArrayList<Double> pRoots= p.getRoots();
                        double xmax,xmin;
                        if(pRoots.size()!=0){
                            xmin = pRoots.get(0);
                            xmax = pRoots.get(0);
                            for (int i = 0; i < pRoots.size(); i++) {
                                if(pRoots.get(i)>xmax) xmax = pRoots.get(i);
                                if(pRoots.get(i)<xmin) xmin = pRoots.get(i);
                            }
                        }
                        else{
                            //xmax = Math.abs(p.evaluate(10));
                            xmax = 9;
                            xmin = -xmax;
                        }
                        double y = Math.abs(p.evaluate(xmax + 1));
                        if(Math.abs(p.evaluate(xmin-1))>y)y = Math.abs(p.evaluate(xmin - 1));
                        temporaryWindow.display(p, xmin-1, xmax+1, -y, y);
                    }
                    else System.out.println("ERROR in graphing. Looking for "+ PARAMETERS +" parameters, found " + parameters.size());
                }
                else{
                    System.out.println("ERROR in graphing. Looking for "+ PARAMETERS + " parameters, found 0.");
                }
            }

            //closes the graphing window
            else if(command.equals("disconnect")){
                System.out.println("CLOSING GRAPHING WINDOW.");
                temporaryWindow.close();
                BinChat.chatting = false;
            }

            // finds the roots of the polynomial
            else if(command.equals("factor")){
                int PARAMETERS = 1;
                if(chat_line.length()>0){
                    chat_line = chat_line.replace("factor", "");
                    System.out.print("The roots are ");
                    ArrayList<Double> roots = mathParser(chat_line).getRoots();
                    for (int i = 0; i < roots.size(); i++) {
                        System.out.print(decimalFormat.format(roots.get(i)));
                        if(i!=roots.size()-1) System.out.print(", ");
                    }
                    System.out.println();
                }
                else System.out.println("ERROR in factoring. Looking for 1 parameter, found 0.");
            }

            // evaluates the function at a given x value
            else if(command.equals("evaluate")){
                int PARAMETERS = 2;
                chat_line = chat_line.replace("evaluate", "");
                ArrayList<String> parameters = parameters(chat_line);
                if(parameters.size()>0){
                    // if the user inputs f(3)= , this will parse the 3 as a new parameter
                    if(parameters.get(0).contains("f(") && parameters.get(0).contains(")")){
                        int startIndex = parameters.get(0).indexOf("(") +1;
                        int endIndex = parameters.get(0).indexOf(")");
                        String x = parameters.get(0).substring(startIndex,endIndex);
                        if(parameters.size()==1) parameters.add(x);
                        else parameters.set(1,x);
                        parameters.set(0,parameters.get(0).substring(endIndex + 1, parameters.get(0).length()).replace("=", ""));
                    }
                    if(parameters.size()>=2){
                        Polynomial p = mathParser(parameters.get(0));
                        System.out.println("f(" + parameters.get(1) + ")="+p.evaluate(Double.parseDouble(parameters.get(1))));
                    }
                    else System.out.println("ERROR in evaluating. Looking for "+ PARAMETERS +" parameters, found " + parameters.size());
                }
                else System.out.println("ERROR evaluating. Looking for " + PARAMETERS + " parameters, found 0.");
            }

            // displays a list of commands and gives instructions
            else if(command.equals("help")){
                System.out.println(
                        "****************HELP****************\n" +
                                "Type a slash as the first character to denote that you want to use a function, then " +
                                "type the function name and then a space. Finally, give it the necessary parameters, delimited by commas. " +
                                "The following is an example to graph a polynomial:\n" +
                                "/graph x^8+ 8x^7 - 111x^6 -792x^5 + 4371x^4 + 23520x^3 -70117x^2-192080x + 235200,-9,9,-350000,900000\n" +
                                "\n LIST OF COMMANDS:\n" +
                                "/add polynomial,polynomial                         will add two polynomials and will return a new polynomial \n" +
                                "/calculate polynomial, double                      will evaluate a function, given the form f(3)= or f(x)= x^2 + 4x + 4,3\n" +
                                "/derivative polynomial                             will return the polynomial's derivative.\n" +
                                "/disconnect                                        will close the graphing window if it is open.\n" +
                                "/evaluate polynomial,double                        same as calculate: will evaluate a function, given the form f(3)= or f(x)= x^2 + 4x +4,3\n" +
                                "/factor polynomial                                 will return an estimate of the roots of the given polynomial\n" +
                                "/factorquadratic polynomial                        will factor a quadratic for its real or complex roots.\n" +
                                "/graph polynomial                                  will graph the function from x=-10 to x=10\n" +
                                "/graph polynomial, x-min, x-max, y-min, y-max      will graph the function in the given window size.\n" +
                                "/help                                              you are looking at it now! Displays a list of all functions and their descriptions\n" +
                                "/kick                                              same as disconnect: will close the graphing window if it is open.\n" +
                                "/minmax polynomial                                 will return the local mins, local maxes and inflection points of a polynomial. Also returns the absolute maximum or minimum.\n" +
                                "/multiply polynomial, polynomial                   will multiply 2 polynomials and return a new polynomial\n" +
                                "/subtract polynomial1,polynomial2                  will subtract polynomial1-polynomial2 and return a new polynomial\n");
            }

            // polynomial expansion
            else if(command.equals("multiply")){
                chat_line = chat_line.replace("multiply", "").replace(" ", "");
                if(chat_line.length()>0){
                    ArrayList<String> para = parameters(chat_line);
                    if(para.size()>=2){
                        System.out.println(mathParser(para.get(0)).multiply(mathParser(para.get(1))).getEquation());
                    }
                    else System.out.println("ERROR in evaluating. Looking for "+ 2 +" parameters, found " + para.size());
                }else System.out.println("ERROR in multiplication. Looking for 2 parameters, found 0.");
            }

            //factoring using quadratic formula
            else if(command.equals("factorquadratic")){
                chat_line = chat_line.replace("factorquadratic", "").replace(" ", "");
                if(chat_line.length()>0){
                    Polynomial p = mathParser(chat_line);
                    String[] factors = p.factorQuadratic();
                    if(factors[0]!=null){
                        System.out.println("The roots of the quadratic are " + factors[0] +" and " + factors[1]);
                    }
                    else System.out.println(p.getEquation()+" is not a quadratic.");
                }else System.out.println("ERROR factoring. Looking for 1 parameter, found 0.");
            }

            //addition command
            else if(command.equals("add")){
                int PARAMETERS = 2;
                chat_line = chat_line.replace("add","").replace(" ", "");
                if(chat_line.length()>0){
                    ArrayList<String> para = parameters(chat_line);
                    if(para.size()>=2){
                        System.out.println(mathParser(para.get(0)).add(mathParser(para.get(1))).getEquation());
                    }
                    else System.out.println("ERROR in addition. Looking for "+ 2 +" parameters, found " + para.size());
                }else System.out.println("ERROR in addition. Looking for 2 parameters, found 0.");
            }

            // subtract command
            else if(command.equals("subtract")){
                chat_line = chat_line.replace("subtract", "").replace(" ", "");
                if(chat_line.length()>0){
                    ArrayList<String> para = parameters(chat_line);
                    if(para.size()>=2){
                        System.out.println(mathParser(para.get(0)).subtract(mathParser(para.get(1))).getEquation());
                    }
                    else System.out.println("ERROR in subtraction. Looking for "+ 2 +" parameters, found " + para.size());
                }else System.out.println("ERROR in subtraction. Looking for 2 parameters, found 0.");
            }

            else if(command.equals("minmax")){
                chat_line = chat_line.replace("minmax", "").replace(" ","");
                if(chat_line.length()>0){
                    mathParser(chat_line).calculateMaxsMins();
                }
                else System.out.println("ERROR in Min/Max. Looking for 1 parameter, found 0.");
            }

            // prints the function's derivative
            else if(command.equals("derivative")){
                chat_line = chat_line.replace("derivative", "").replace(" ","");
                if(chat_line.length()>0){
                    System.out.println(mathParser(chat_line).derivative().getEquation().replace("y", "y'"));
                }
                else System.out.println("ERROR in derivative. Looking for 1 parameter, found 0.");
            }

            // command not in list, print error
            else{
                System.out.println("Command \"" + command + "\" not recognized! Please refer to /help to get a list of all functions.");
            }

        }
    }

    public Polynomial mathParser(String chat_line){
        // Parses a polynomial of the form y = x^2 + 4x - 4
        chat_line = chat_line.toLowerCase().replace(" ", "").replace("y=", "").replace("f(x)=", "");
        List<Double> terms = new ArrayList<Double>(); // will have a list of all coefficients
        terms.add(0,0.0);
        terms.add(0,0.0); // populates the basic polynomial, a binomial
        while(chat_line.contains("x")){ // while it has x terms
            if(chat_line.substring(0,1).equals("+")) chat_line = chat_line.substring(1,chat_line.length()); // removes + in front of coefficient
            int x_index = chat_line.indexOf("x");
            double coefficent;
            if (x_index == 0) coefficent = 1;
            else{
                String sub = chat_line.substring(0, x_index);
                if(sub.equals("-")) sub = "-1";
                else if(sub.length()==0) sub = "1";
                coefficent = Double.parseDouble(sub);
            }
            if(chat_line.length()>0){
                if(chat_line.contains("^")){
                    if(chat_line.substring(x_index+1,x_index+2).equals("^")) { // if after the x there is a caret
                        int degree_index = x_index + 2;
                        int end_index = degree_index;
                        while (end_index<chat_line.length()){ // will scan the string until it reaches a + or - sign (this is for degrees of 10 or higher)
                            if (chat_line.charAt(end_index) == '+' || chat_line.charAt(end_index) == '-'){
                                break;
                            }
                            end_index ++;
                        }
                        int degree = Integer.parseInt(chat_line.substring(degree_index, end_index)); // This as it is written, assumes degree less than 10
                        if(degree+1 > terms.size()){
                            int new_terms = degree+1 -(terms.size());
                            for (int i = 0; i < new_terms; i++) {
                                terms.add(terms.size()-1,0.0);
                            }
                        }
                        terms.set(degree, terms.get(degree) +coefficent);
                        chat_line = chat_line.substring(end_index, chat_line.length()); // shortens the string starting at the +/- sign
                    }
                    else{
                        // If it does not have a caret, it is of degree 1
                        terms.set(1, terms.get(1) + coefficent);
                        if(x_index+1 != chat_line.length()) chat_line = chat_line.substring(x_index+1,chat_line.length());
                        else chat_line = "";
                    }
                }
                else{
                    // If it does not have a caret, it is of degree 1
                    terms.set(1, terms.get(1) + coefficent);
                    if(x_index+1 != chat_line.length()) chat_line = chat_line.substring(x_index+1,chat_line.length());
                    else chat_line = "";
                }
            }

        }
        // once it finishes parsing the x terms, we need to add the constant term, represented as the zeroith degree
        chat_line.replace("+", "").replace(" ", "");
        if(chat_line.length()>0) terms.set(0, terms.get(0) + Double.parseDouble(chat_line));
        // convert it to an array
        double[] output = new double[terms.size()];
        for (int i = 0; i < terms.size(); i++) {
            output[i] = terms.get(i);
        }
        return new Polynomial(output);
    }
}