package binchat;
// All functioning code can be found in folders "parser" and "graphing"
import binchat.parser.ParserManager;
import java.util.Scanner;

public class BinChat {
    public static boolean chatting = true;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the chat room. Type a message in a line to send it, or use commands to make calculations and graph functions. Type /help for more information and for a full list of commands.");
        ParserManager parserManager = new ParserManager();

        //The program will continue to loop through, waiting on the scanner for a new command or chat line.
        //If the user types in a command, it was be sent to the command parser.
        //If a parameter of a command is a polynomial, it will be interpreted by the math parser, which returns a polynomial.
        while (chatting) {
            parserManager.parseChatLine(in.nextLine());
        }
    }
}
