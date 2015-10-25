# Polynomial-Graphing-Calculator
##Startup
On startup, the program will print the message to the screen, saying welcome to the chat room. The console will be used for all input. Typing text will attempt to send a message to other users (for this build, the chat aspect will not be working, so it will always return the error message, “Message not sent”). Users can use commands from the console and graph polynomials.

##Commands
All commands given to the software must start with a forward slash ( / ) for the software to interpret the text as a command. Otherwise, the program will treat the input as a message to send. These commands carry out operations on polynomials, and can be used to visually graph polynomials.

##Possible Commands:
<b>/help</b> - will print out all the possible commands on screen
e.g.
input: /help
output: a list of commands and explanation of how to use the software.

<b>/add</b> - will add two given polynomials
	e.g.
	input: /add y=x^3+x+2, y=x+5
	output: y=x^3+2x+7
<b>/calculate</b> - will evaluate any given polynomial function
e.g.
input: /calculate f(3)=x^2+4x+4
output: f(3)=25.0


<b>/derivative</b>  - will return the derivative of the given polynomial
	e.g.
	input: y=2x^4+5x^2+3
	output: y’=8x^3+10x

<b>/disconnect</b> - will close the graphing window, and end the process.
	e.g.
	input: /disconnect
	outcome: closes the graphing window
<b>/evaluate</b> - will be interpreted as ‘/calculate’

<b>/factor</b> - will factor any given polynomial function and display the roots
e.g.	
input: /factor  y=x^4+2x^3-13x^2-14x+24
	output: The roots are -4, -2, 1, 3

<b>/factorquadratic</b> - will find the real or complex roots  for a quadratic function
	e.g.
input: /factorquadratic  y=x^2-2x-8
	output: The roots of the quadratic are 4.0 and -2.0

<b>/graph</b> - will graph any given polynomial function
e.g.	
input: /graph f(x)=x^2+4x+4, -20, 20, -20, 20
	outcome: graph with the line of the function on graphing screen with an x-min 			      of -20, an x-max of 20, a y-min of -20, and a y-max of 20

<b>/kick</b> - will be interpreted as ‘/disconnect’

<b>/minmax</b> - will find and display the maximums, minimums, and the inflection points; if applicable, returns absolute max and absolute min
	e.g.
	input: /minmax y=x^4+10x^3+23x^2-34x-120
	output: Local maxes at: -3.44
   Local mins at: -4.59, 0.54
   No inflection points.
   Absolute minimum = (0.54, -129)
   No absolute maximum.

<b>/multiply</b>  - will multiply two given polynomials
	e.g.
	input: /multiply y=3x^3+4x^2+2x-2, y=x^3-2x^2+x+3
	output: y=3x^6-2x^5-3x^4+7x^3+18x^2+4x-6

<b>/subtract</b> - will subtract two given polynomials
	e.g.
	input: /subtract y=4x^3+2x^2+3x+1, y=x^3-2x^2+x+3
	output: y=3x^3+4x^2+2x-2


##Graphing
The user can enter any polynomial function in any degrees after entering the command ‘/graph’ and the software will draw the line of the function in the second half of the window. The command should contain 5 parameters in the following order: 
<polynomial>, <xMin>, <xMax>, <yMin>, <yMax>
If the parameters for the window size are not specified, a window size will be guessed based on the roots of the function. When guessing, the program will always display all calculated roots on screen.
To close the graphing window, the user can give the command ‘/disconnect’ at any time.
An example of the graphing function is as follows:
/graph x^8+ 8x^7 - 111x^6 -792x^5 + 4371x^4 + 23520x^3 -70117x^2-192080x + 235200,-9,9,-350000,900000
![alt tag](https://github.com/kevinpeng7/Polynomial-Graphing-Calculator/blob/master/src/main/java/binchat/graphing/graph.png)
