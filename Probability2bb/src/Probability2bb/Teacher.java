
package Probability2bb;

import blackboard.Blackboard;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 *
 * @author avinash awate
 *
 */

/**
 *
 * The Teacher class is responsible for teaching the concept to the student.
 * It owns blackboard and it's accessories (dustor,chalk).
 * The teacher can do the following:
 * <ul>
 *     <li> write characters and strings to the blackboard.
 *     <li> draw diagrams on the blackboard.
 *     <li> erase areas off the  blackboard.
 *     <li> display student's text response (integers/floats/text).
 *     <li> ask for student's button response (repeat/step/auto/done).
 * </ul>
 *
 * This particular instance of teacher is responsible for teaching the concept of <b>MULTIPLE DIGIT ADDITION WITH CARRY</b>
 *
 * The concept is to be taught in four phases:
 * <ul>
 *     <li> Demo Mode        : Solve a sum with explanation.
 *     <li> Interaction Mode : Solve sum given by student.
 *     <li> Guided Mode      : Ask student to solve sums through prompting.
 *     <li> Test Mode        : Ask students to solve examples.
 * </ul>
 *
 * In each phase, the teacher continues with multiple sums.
 * After each sum, the student can ask for another sum (repeat button).
 *
 * The mode can be changed through a button on a panel outside teacher's area.
 * The teacher can be forced to change phase by a method changeMode.
 *
 */
public class Teacher implements Runnable, ActionListener
{
/**
 * Variable to control number of digits to be added.
 */
private int addDigits;                  /* Variable to control number of digits to be added */
/**
 * Maximum number to be added (9999 if addDigits = 4).
 */
private int maxNumber;                  /* maximum Number to be added */
/**
 * Variable to stop current thread (terminate).
 */
boolean stopRun;                        /* Variable to stop current thread (terminate) */
/**
 * Variable to restart simulation with a new mode/problem.
 */
boolean restart;                        /* Variable to restart simulation with a new mode/problem.

/**
 * Blackboard Resource on which to write.
 */
Blackboard bb;                          /* Blackboard Resource on which to write */

/**
 * Depending on blackboard size, we wish to position numbers and text.
 * Logical number of lines on blackboard.
 */
private final static int bbLines=10;       /* Logical number of lines on blackboard */
/**
 * Depending on blackboard size, we wish to position numbers and text.
 * Logical number of chracters per line.
 */
private final static int bbCharsPerLine=20;/* Logical number of characters per line */
/**
 * Pixel gap between two logical lines.
 */
private int lineGapY;                   /* Y Position offset between two lines */
/**
 * Pixel gap between two characters.
 */
private int charGapX;                   /* X Position offset between two characters */

/**
 * Layout Variable.
 * X Position of numbers(MSB), carry and result.
 */
private int topNumberPositionX;         /* X Position of numbers(MSB), carry and result */
/**
 * Layout Variable.
 * Y Position of numbers(MSB), carry and result.
 */
private int topNumberPositionY;         /* Y Position of first number */
/**
 * Layout Variable.
 * X Position of workarea to show addition of single column.
 * (Y position depends on topNumberPositionY and lineGapY).
 */
private int workAreaX;                  /* X Position of Work Area */
/**
 * Layout Variable.
 * X Position of leftmost button (repeat, step, auto and done buttons).
 */
private int buttonPositionX;            /* X Position of Buttons */
/**
 * Layout Variable.
 * Y Position of all buttons (repeat, step, auto and done buttons).
 */
private int buttonPositionY;            /* Y Position of Buttons */

/**
 * To differentiate between step-by-step and automatic demonstration.
 */
private boolean stepMode;               /* Whether demos should run in step mode */
/**
 * To get out of the delay loop which waits on pressing step button.
 */
private boolean nextPlay;               /* Whether next Step is to be shown (controls step by step execution) */
/**
 * To get out of the delay loop which waits on pressing step button.
 */
private boolean donePressed;            /* Whether done button is pressed */

/**
 * Random number generator.
 */
private Random random;                  /* random number generator */

/**
 * Single thread to control blackboard display and sleep to slow drawing.
 */
private Thread animation;               /* To animate the actions while teaching */

/**
 * First number to be added.
 */
int firstNumber;                        /* First Number */
/**
 * Second number to be added.
 */
int secondNumber;                       /* Second Number */
/**
 * Result of addition.
 */
int result;                             /* Result */

/**
 * Mode of teaching.
 * demoMode = 1:        Demonstration mode to show concept
 * interactionMode = 2: Interaction mode to solve user examples
 * guidedMode = 3:      Guided mode to solve examples in step by step manner
 * testMode = 4:        Test mode to assess understanding of concept
 */
int mode;                               /* Current phase of Teacher class */

private final static int defaultMode=1; /* Default phase of Teacher class */
private final static int demoMode = 1;  /* Demonstration mode to show concept */
private final static int interactionMode = 2; /* Interaction mode to solve user examples */
private final static int guidedMode = 3; /* Guided mode to solve examples in step by step manner */
private final static int testMode = 4;  /* Test mode to assess understanding of concept */

/**
 * Line thickness.
 */
private final static float lineThickness = 1.0f;  /* default line thickness */

/* Buttons */
/**
 * Button repeat : show next problem for solving.
 */
Button repeat;                          /* Button to go for next sum */
/**
 * Button step : (In step mode) Go for next step after understanding the present step.
 */
Button step;                            /* Button to go for next step */
/**
 * Button automatic : Go for next step automatically (after a delay).
 */
Button automatic;                       /* Button to go for automatic mode */
/**
 * Button done : Pressed by student after supplying input.
 */
Button done;                            /* Button to indicate user action is complete */

/* Statistics */
/**
 * Number of demonstration examples.
 */
private int demoExamples;               /* Number of demonstration examples */
/**
 * Number of interaction examples.
 */
private int interactionExamples;        /* Number of interaction examples */
/**
 * Number of guided examples.
 */
private int guidedExamples;             /* Number of guided examples */
/**
 * Number of guided examples solved correctly.
 */
private int guidedCorrect;              /* Number of guided examples solved correctly */
/**
 * Number of test examples.
 */
private int testExamples;               /* Number of test examples */
/**
 * Number of test examples solved correctly.
 */
private int testCorrect;                /* Number of test examples solved correctly */

/* Error Log */
/**
 * Array to store errors in guided and test mode.
 */
private errorRecord errorLog[];         /* To store errors in the guided and test mode */
/**
 * Number of errors in array.
 */
private int numberErrors;               /* Number of errors in the errorLog */

/**
 * Delay defines. All delays in milliseconds.
 * sDelay=100:    Short Delay.
 * mDelay=500:    Medium Delay.
 * lDelay=1000:   Long Delay.
 * xlDelay=10000: Extra long Delay.
 */
private final static int sDelay=100;    /* Short Delay */
private final static int mDelay=500;    /* Medium Delay */
private final static int lDelay=1000;   /* Long Delay */
private final static int xlDelay=10000; /* Extra long Delay */

/* Mouse Tracking */
/**
 * X position of Mouse/cursor
 */
int mouseX;                             /* X position of Mouse/cursor */
/**
 * Y position of Mouse/cursor
 */
int mouseY;                             /* Y position of Mouse/cursor */

/**
 *
 * Constructor for Teacher.
 * This method first gets the dimensions of the blackboard resource.
 * It plans where to position various elements for teaching and interaction.
 * It initializes various teaching objects (buttons etc.).
 * @param bb Handle to blackboard instance.
 * @param numberDigits Maximum number of digits in numbers to be added.
 *
 */
public Teacher(Blackboard bb, int numberDigits)
{
    this.mode = defaultMode;
    this.bb = bb;

    if (numberDigits <= 6) addDigits = numberDigits;
    else addDigits = 2;

    /* Create errorLog */
    errorLog = new errorRecord[10];
    numberErrors = 0;
    /* Teacher decides where to write everything */
    setLayoutVariables();
    /* Set stepMode to default (true) */
    stepMode = true;
    /* Set stepMode to default (true) */
    restart = true;
    stopRun = false;
    /* Teacher Objects (buttons etc.) are initialised */
    initObjects();
}

/**
 *
 * This method first gets the dimensions of the blackboard resource.
 * It then decides where to position various elements for teaching and interaction.
 * Finally, it sets the character size of the blackboard text.
 *
 */
private void setLayoutVariables()
{
int topLeftX;                           /* Left Top X of blackboard */
int topLeftY;                           /* Left Top Y of blackboard */
int width;                              /* Width of blackboard */
int height;                             /* Height of blackboard */

    /* Get Blackboard dimensions and position */
    topLeftX = bb.getTopLeftX();
    topLeftY = bb.getTopLeftY();
    width    = bb.getBlackboardWidth();
    height   = bb.getBlackboardHeight();
   
    /* Set Layout variables for drawing */
    /* 10 Lines and 20 characters per line */
    lineGapY = height/bbLines;
    charGapX = width/bbCharsPerLine;
    /* X Position of numbers(MSB), carry and result (6 chars from right) */
    topNumberPositionX = topLeftX + width - ((addDigits+2)*charGapX);
    /* Y Position of first number (3rd line from top) */
    topNumberPositionY = topLeftY + (3 * lineGapY);
    /* X Position of Work Area */
    workAreaX = topNumberPositionX - (2*charGapX);
    /* X Position of Buttons (2 char from left) */
    buttonPositionX = topLeftX + (2*charGapX);
    /* Y Position of Buttons (1 line from bottom) */
    buttonPositionY = topLeftY + height - (2*lineGapY);

    /* Set Blackboard character size */
    bb.setFontSize((int) (lineGapY*0.6));

    /* Add developer credit */
    bb.setDeveloperName("Aviral Nigam");

    /* Add permanent topic */
    bb.setFontSize(22);
    bb.setForegroundColor(Color.RED);
    bb.setLineThickness(2.0f);
    bb.drawPermanentRectangle(topLeftX-3, topLeftY+7, width+4, height-12, null);
    bb.writePermanentString("Probability of events based on observation tables",topLeftX + ((width - (int) (lineGapY * 0.4) * 28 )/ 2) + (2*(int) (lineGapY * 0.3)), topLeftY+((int)(lineGapY*0.3)), false);
    bb.drawPermanentLine(topLeftX+ ((width - (int) (lineGapY * 0.4) * 25) / 2), topLeftY + lineGapY-10, topLeftX + ((width - (int) (lineGapY * 0.4) * 40) / 2) + (32 * (int) (lineGapY * 0.4)), topLeftY + lineGapY-10, Color.GREEN, 2.0f);
    bb.setLineThickness(1.0f);
    bb.setForegroundColor(Color.WHITE);
}

/**
 *
 * This method changes the mode of teaching (demo, automatic, guided, test).
 * It sets the mode variable and restarts the teaching by setting restart flag.
 * @param mode New phase of teaching.
 *
 */
public void changeMode(int mode)
{
    this.mode = mode;
    if (mode == demoMode) demoExamples = 0;
    else if (mode == interactionMode) interactionExamples = 0;
    else if (mode == guidedMode) { guidedExamples = 0; guidedCorrect = 0; }
    else if (mode == testMode) { testExamples = 0; testCorrect = 0; }
    restart = true;
}

/**
 *
 * This method initialises the objects (buttons etc.) required for teaching.
 *
 */
private void initObjects()
{
    /* Get a new random number generator */
    random = new Random();
    
    /* initioalise errors */
    numberErrors = 0;

    /* Load all possible buttons and other accessories and mark them as invisible */
    repeat = new Button("repeat");
    repeat.setFont(new Font("Arial",Font.PLAIN,12));
    step = new Button("step");
    step.setFont(new Font("Arial",Font.PLAIN,12));
    automatic = new Button("auto");
    automatic.setFont(new Font("Arial",Font.PLAIN,12));
    done = new Button("done");
    done.setFont(new Font("Arial",Font.PLAIN,12));

    /* Load Action Commands for the above buttons */
    repeat.setActionCommand("repeat");
    step.setActionCommand("step");
    automatic.setActionCommand("automatic");
    done.setActionCommand("done");

    /* Load Action Listeners for the above buttons */
    repeat.addActionListener(this);
    step.addActionListener(this);
    automatic.addActionListener(this);
    done.addActionListener(this);

    /* Place the buttons in appropriate places */
    repeat.setBounds(buttonPositionX,buttonPositionY,(3*charGapX),lineGapY);
    step.setBounds(buttonPositionX+(4*charGapX),buttonPositionY,(3*charGapX),lineGapY);
    automatic.setBounds(buttonPositionX+(8*charGapX),buttonPositionY,(3*charGapX),lineGapY);
    done.setBounds(buttonPositionX+(12*charGapX),buttonPositionY,(3*charGapX),lineGapY);

    /* Mark Buttons as invisible */
    repeat.setVisible(false);
    step.setVisible(false);
    automatic.setVisible(false);
    done.setVisible(false);

    /* Mark Buttons as disabled */
    repeat.setEnabled(false);
    step.setEnabled(false);
    automatic.setEnabled(false);
    done.setEnabled(false);

    /* Change the cursor when enabled */
    repeat.setCursor(new Cursor(Cursor.HAND_CURSOR));
    step.setCursor(new Cursor(Cursor.HAND_CURSOR));
    automatic.setCursor(new Cursor(Cursor.HAND_CURSOR));
    done.setCursor(new Cursor(Cursor.HAND_CURSOR));

    /* Add Buttons to blackboard */
    bb.add(repeat);
    bb.add(step);
    bb.add(automatic);
    bb.add(done);
}

/**
 * This method sets the value of the addDigits variable,
 * The addDigits variable governs the number of digits in the two numbers to be added.
 * The method also computes the maxNumber variable.
 */
public void setAddDigits(int numberDigits)
{
    if (numberDigits <= 6)
    {
        addDigits = numberDigits;
        maxNumber = 1;
        while (numberDigits > 0)
        {
            maxNumber *= 10;
            numberDigits--;
        }
        maxNumber -= 1;
    }
}
/**
 *
 * Method to perform actions.
 * Handles repeat, step, automatic and done actions.
 * All actions reset flags and return.
 * further processing is controlled by the flags..
 * The repeat action restarts teaching in the same mode (with a new problem).
 * The step action sets the step mode and continues teaching.
 * The automatic action resets the step mode and continues teaching.
 * The done action resets accept input mode and continues to process unser input.
 * @param e Action event.
 *
 */
public void actionPerformed(ActionEvent e)
{
    mouseY = buttonPositionY + (lineGapY / 2);
    if (e.getActionCommand().equals("repeat"))
    {
        mouseX = buttonPositionX + 2 * charGapX;
        restart = true;
    }
    else if (e.getActionCommand().equals("step"))
    {
        mouseX = buttonPositionX + 6 * charGapX;
        stepMode = true;
        nextPlay = true;
    } 
    else if (e.getActionCommand().equals("automatic"))
    {
        mouseX = buttonPositionX + 10 * charGapX;
        stepMode = false;
        nextPlay = true;
    } 
    else if (e.getActionCommand().equals("done"))
    {
        mouseX = buttonPositionX + 14 * charGapX;
        donePressed = true;
    }
}

/**
 *
 * Method to reset teacher.
 * Restarts teaching from beginning.
 *
 */
public void reset()
{
    numberErrors = 0;
    stepMode = true;
    initObjects();
    changeMode(demoMode);
    restart = true;
}


/**
 *
 * Method to start teaching.
 *
 */
public void start()
{
    if (animation == null)
    {
       try
       {
           animation = new Thread(this);
       }
       catch (Exception e) { }
    }
    if (animation != null) animation.start();
}

/**
 *
 * Method to pause teaching.
 *
 */
public void stop()
{
   try
   {
       if (animation != null && animation.isAlive())
       {
           animation.interrupt();
       }
   }
   catch (Exception e) { }
}

/**
 *
 * Method to run single animation Thread.
 * This method has an unending loop.
 * In the loop:
 *     If restart flag is set, the problem is solved
 *     followed by a delay till repeat or any other user action
 *
 */
public void run()
{
    /* Never Ending Loop */
    while (stopRun == false)
    {
        if (restart == true) solveProblem();
	delaySimple(lDelay);
    }
}

/**
 *
 * This method is used as a wrapper to thread sleep.
 * The solveProblem method calls for a delay for user to absorb actions.
 * If this is interrupted, it results in unexpected behaviour.
 * This wrapper traps the interrupt gracefully.
 * It also gets rid of the warning for calling Thread.sleep in a loop.
 *
 */
private void delaySimple(int delay)
{
    try { Thread.sleep(delay); }
    catch (InterruptedException e) { /* Do Nothing */ }
}

/**
 *
 * If the simulation is in the step mode:
 *     The method waits till the step button is pressed (nextPlay = false).
 * (When the button is pressed nextPlay is made true).
 * The function exits the wait loop and sets nextPlay to false.
 * This method also exits if restart flag is set to true.
 *
 */
private void delayNextAction(int delay)
{
    delaySimple(delay);
    if (stepMode == true) nextPlay = false;
    while ( (nextPlay == false) && (restart == false) )
    {
        delaySimple(delay);
    }
    if (stepMode == true) nextPlay = false;
}

/**
 *
 * This method is used to primarily wait till user signals end of input by done button.
 * This method also exits if restart flag is set to true.
 *
 */
private void waitTillDone(int delay)
{
    donePressed = false;
    while ( (donePressed == false) && (restart == false) )
    {
        delaySimple(delay);
    }
    donePressed = false;
}

/**
 *
 * The following class is used to store errors when user gives multiple inputs
 * An error element stores location of the error
 * The enclosing class contains a list of errors
 * The containing class can then display the errors to the user as appropriate
 *
 */
class errorRecord
{
public int topLeftX;
public int topLeftY;
public int width;
public int height;

/**
 *
 * Constructor of errorRecord
 *
 */
public errorRecord(int tlX, int tlY, int w, int h)
{
    this.topLeftX = tlX;
    this.topLeftY = tlY;
    this.width    = w;
    this.height   = h;
}

/* End of class errorRecord */
}

/**
 *
 * The following method updates an entry in the error log
 *
 */
public void addErrorRecord(int tlX, int tlY, int w, int h)
{
    if (numberErrors < 10)
    {
        errorLog[numberErrors] = new errorRecord(tlX, tlY, w, h);
        numberErrors++;
    }
}

/**
 *
 * The following method clears the error log
 *
 */
private void clearErrorLog()
{
    numberErrors = 0;
}

/**
 *
 * This method shows the buttons on the blackboard.
 * This method uses the mode variable to sisplay the buttons.
 * This is called by the solveProblem method.
 *
 */
private void showButtons()
{
    /* Display and enable buttons required in this mode */
    repeat.setVisible(true);
    repeat.setEnabled(true);
    if (mode == demoMode)
    {
        step.setVisible(true);
        step.setEnabled(true);
        automatic.setVisible(true);
        automatic.setEnabled(true);
        done.setVisible(false);
        done.setEnabled(false);
    }
    else if (mode == interactionMode)
    {
        step.setVisible(true);
        step.setEnabled(true);
        automatic.setEnabled(true);
        automatic.setVisible(true);
        done.setVisible(true);
        done.setEnabled(true);
    }
    else
    {   /* Both guided and test mode */
        step.setVisible(false);
        step.setEnabled(false);
        automatic.setVisible(false);
        automatic.setEnabled(false);
        done.setVisible(true);
        done.setVisible(true);
    }
}

/***************************************************************************
 *                                                                         *
 * Most of the above code will remain relatively siimilar for all applets. *
 * The code that follows is application (concept) specific.                *
 *                                                                         *
 ***************************************************************************/

/**
 *
 * This method shows the problem on the blackboard.
 * If withUserdata is true, user is prompted to give the numbers.
 * If withUserdata is false, the numbers are randomly generated.
 *
 */
String c[][]={{"A","B","C","D","E","F"},
            {"1","2","3","4","5","6"},
            {"I","II","III","IV","V","VI"}
        };
        int n = 0;
        int a=7,sum=0;
        int b[] = new int[6];

private void showProblem(boolean withUserdata)
{
        bb.setFontSize(20);
        bb.drawRectangle(charGapX*2,lineGapY*2,charGapX*17,lineGapY*2);
        bb.drawLine(((charGapX*2) +(charGapX*5)) , lineGapY*2, ((charGapX*2) + (charGapX*5)), (lineGapY*2+lineGapY*2));
        bb.drawLine(((charGapX*2) +(charGapX*7)) , lineGapY*2, ((charGapX*2) + (charGapX*7)), (lineGapY*2+lineGapY*2));
        bb.drawLine(((charGapX*2) +(charGapX*9)) , lineGapY*2, ((charGapX*2) + (charGapX*9)), (lineGapY*2+lineGapY*2));
        bb.drawLine(((charGapX*2) +(charGapX*11)) , lineGapY*2, ((charGapX*2) + (charGapX*11)), (lineGapY*2+lineGapY*2));
        bb.drawLine(((charGapX*2) +(charGapX*13)) , lineGapY*2, ((charGapX*2) + (charGapX*13)), (lineGapY*2+lineGapY*2));
        bb.drawLine(((charGapX*2) +(charGapX*15)) , lineGapY*2, ((charGapX*2) + (charGapX*15)), (lineGapY*2+lineGapY*2));
        bb.drawLine((charGapX*2), lineGapY*3, ((charGapX*2) + (charGapX*17)),lineGapY*3);
        bb.writeString("Outcome",charGapX*3,lineGapY*2 + 15 ,false);
        bb.writeString("Frequency",charGapX*3,lineGapY*3 + 15 ,false);
        if (restart == true) {
                                return;  /* Exit to restart simulation */
                            }
                
        n = random.nextInt(3);
        if(mode == demoMode || mode == guidedMode || mode == testMode) {
            a=7;
            sum =0;
            for(int i=0;i<6;i++){
                delaySimple(mDelay);
                bb.writeString(c[n][i],charGapX*a +20,lineGapY*2 + 15 ,false);
                b[i] = random.nextInt(100);
                if (restart == true) {
                                return;  /* Exit to restart simulation */
                            }
                delaySimple(mDelay);
                bb.writeNumber(b[i],charGapX*a +20,lineGapY*3+15,false);
                sum=sum+b[i];
                a=a+2;
            }
        }

        if (restart == true) {
                                return;  /* Exit to restart simulation */
                            }
        
        if(mode == interactionMode){
            a=7;
            sum =0;
            for(int i=0;i<6;i++){
                bb.writeString(c[n][i],charGapX*a +20,lineGapY*2 + 15 ,false);
                do{
                    bb.drawHighlightRectangle(charGapX * a+10 , lineGapY * 3 + 15, charGapX +20, 30,Color.BLUE);
                    bb.acceptInteger(2, charGapX*a +13,lineGapY*3+15);
                    waitTillDone(sDelay);
                    if (restart == true) return;  /* Exit to restart simulation */
                    b[i] = bb.getAcceptedInteger();
                    bb.clearHighlight();
                    if(b[i]<0)
                        bb.eraseArea(charGapX * a+10 , lineGapY * 3 + 15, charGapX +20, 30);
                }while(b[i]<0);
                
                sum=sum+b[i];
                a=a+2;
            }
        }
        delaySimple(mDelay);
}

/**
 *
 * This method constitutes the major teaching action by the teacher.
 * First the teacher erases the usable blackboard area.
 * The teacher starts writing different things on the blackboard depending on the mode.
 * The animation either flows with pre-defined timing or with student controlled steps.
 * 
 * Actions in the demo Mode (with appropriate delays or in step mode):
 * <ul>
 *     <li> write first number in designated area.
 *     <li> write the addition sign.
 *     <li> write the second number.
 *     <li> draw the addition line.
 *     <li> print the result and carry strings.
 *     <li> Starting from the lsd, add digit by digit with carry till sum is done.
 *     <li> Wait for student prompt next.
 * </ul>
 * 
 * Actions in the interactive Mode (with appropriate delays or in step mode):
 * <ul>
 *     <li> Get first number from student in designated area.
 *     <li> write the addition sign.
 *     <li> Get second number from student in designated area.
 *     <li> draw the addition line.
 *     <li> print the result and carry strings.
 *     <li> Starting from the lsd, add digit by digit with carry till sum is done.
 *     <li> Wait for student prompt next.
 * </ul>
 * 
 * Actions in the guided Mode:
 * <ul>
 *     <li> write first number in designated area.
 *     <li> write the addition sign.
 *     <li> write the second number.
 *     <li> draw the addition line.
 *     <li> print the result and carry strings.
 *     <li> Starting from the lsd, get digit by digit response from student of result and carry.
 *     <li> If answer is incorrect, show errors and the wrong image else show correct image.
 *     <li> Display results so far (correct/total).
 *     <li> Wait for student prompt next.
 * </ul>
 * 
 * Actions in the test Mode:
 * <ul>
 *     <li> write first number in designated area.
 *     <li> write the addition sign.
 *     <li> write the second number.
 *     <li> draw the addition line.
 *     <li> print the result and carry strings.
 *     <li> get answer of addition from student.
 *     <li> If answer is incorrect, show the wrong image else show correct image.
 *     <li> Display results so far (correct/total).
 *     <li> Wait for student prompt next.
 * </ul>
 *
 */
public void solveProblem()
{
        int inputDigit = 0;                         
 
        boolean isCorrect = false;                     

        restart = false;

        bb.clean();
        showButtons();

    
        if (mode == interactionMode) {
            showProblem(true); /* With user data */
        } else {
            showProblem(false);                        /* With random data */
        }
        
        if (restart == true) {
                return;  /* Exit to restart simulation */
            }

            if ((mode == demoMode) || (mode == interactionMode) || (mode == guidedMode)) {  
           
            
                if((mode == demoMode) || (mode == interactionMode)){

                    if (mode == demoMode) {
                        demoExamples++;
                    }
                    if (mode == interactionMode) {
                        interactionExamples++;
                    }
                    for(int i=0;i<6;i++){
                        bb.setFontSize(20);

                        bb.writeHighlightString("Probability of "+ c[n][i]+" = ",charGapX*2,lineGapY*5 ,false);
                        delaySimple(mDelay);
                        if (restart == true) return;
                        bb.writeHighlightString("Frequency of "+c[n][i],charGapX*7 + 10,lineGapY*5 -15,false);
                        delaySimple(mDelay);
                        bb.drawHighlightLine(charGapX*7 + 10,lineGapY*5 + 10,charGapX*11+20,lineGapY*5 + 10,Color.WHITE,1);
                        delaySimple(mDelay);
                        bb.writeHighlightString("Total Frequency",charGapX*7 + 10,lineGapY*5 + 10,false);
                        delaySimple(mDelay);
                        bb.writeHighlightString(" = ",charGapX*11 +30,lineGapY*5,false);
                        delayNextAction(mDelay);
                        if (restart == true) return;
                        bb.setFontSize(15);
                        delaySimple(mDelay);
                        bb.writeHighlightNumber(b[i],charGapX*15 ,lineGapY*5 -10,false);
                        delaySimple(mDelay);
                        bb.drawHighlightLine(charGapX*12+20,lineGapY*5 + 10,charGapX*17+30,lineGapY*5 + 10,Color.WHITE,1);
                        delaySimple(mDelay);

                        int s=0;
                        for(int j=0;j<6;j++){
                            bb.writeHighlightNumber(b[j],charGapX*12 + 20 + s,lineGapY*5 + 10,false);
                            delaySimple(mDelay);
                            if(j<5){
                                if (restart == true) return;
                                s=s+20;
                                bb.writeHighlightString(" + ",charGapX*12 +20 + s,lineGapY*5+10,false);
                                s=s+15;
                                delaySimple(mDelay);
                                if (restart == true) {
                                    return;  /* Exit to restart simulation */
                                }
                            }
                        }

                        bb.setFontSize(20);
                        bb.writeHighlightString(" = ",charGapX*18,lineGapY*5,false);
                        delayNextAction(mDelay);
                        delaySimple(mDelay);
                        bb.writeHighlightNumber(b[i],charGapX*18+25 ,lineGapY*5 -15,false);
                        delaySimple(mDelay);
                        bb.drawHighlightLine(charGapX*18+20,lineGapY*5 + 10,charGapX*18+60,lineGapY*5 + 10,Color.WHITE,1);
                        delaySimple(mDelay);
                        bb.writeHighlightNumber(sum,charGapX*18+25,lineGapY*5 + 10,false);

                        delayNextAction(mDelay);
                        if(stepMode==false){
                            delaySimple(mDelay);
                        }

                        if(i<5)
                        bb.clearHighlight();
                        if (restart == true) {
                            return;  /* Exit to restart simulation */
                        }
                    }
                }

            
                else if (mode == guidedMode) {   

                    guidedExamples++;
                    clearErrorLog();

                    isCorrect = true;

                    for(int i=0;i<6;i++){
                        bb.clearHighlight();
                        bb.eraseArea(charGapX*2 -10, lineGapY*5 -20,charGapX*19-10 , lineGapY*3);
                        bb.setFontSize(20);
                        bb.writeString("Probability of "+ c[n][i]+" = ",charGapX*2,lineGapY*5 ,false);
                        delaySimple(mDelay);
                        bb.writeString("Frequency of "+c[n][i],charGapX*7 + 10,lineGapY*5 -15,false);
                        delaySimple(mDelay);
                        bb.drawLine(charGapX*7 + 10,lineGapY*5 + 10,charGapX*11+20,lineGapY*5 + 10,Color.WHITE,1);
                        delaySimple(mDelay);
                        bb.writeString("Total Frequency",charGapX*7 + 10,lineGapY*5 + 10,false);
                        delaySimple(mDelay);
                        bb.writeString(" = ",charGapX*11 +30,lineGapY*5,false);
                        bb.setFontSize(15);
                        delaySimple(mDelay);
                        bb.clearHighlight();

                        do{
                                bb.drawHighlightRectangle(charGapX*15 ,lineGapY*5 -10, 30, 20,Color.BLUE);
                                bb.acceptInteger(addDigits, charGapX*15 ,lineGapY*5 -10);
                                waitTillDone(sDelay);
                                if (restart == true) return;  /* Exit to restart simulation */
                                inputDigit = bb.getAcceptedInteger();

                                bb.clearHighlight();
                                if(inputDigit<0)
                                    bb.eraseArea(charGapX*15 ,lineGapY*5 -10, 30,20);
                        }while(inputDigit<0);

                        if (inputDigit != b[i]) {   
                            addErrorRecord(charGapX * 15, lineGapY * 5-10, 30, 20);
                            isCorrect = false;
                        }

                        bb.drawLine(charGapX*12+20,lineGapY*5 + 10,charGapX*17+40,lineGapY*5 + 10,Color.WHITE,1);

                        int s=0;
                        for(int j=0;j<6;j++){
                            do{
                                bb.drawHighlightRectangle((charGapX * 12+10)+s , lineGapY * 5 + 20, 25, 20,Color.BLUE);
                                bb.acceptInteger(addDigits, charGapX*12 + 12 + s,lineGapY*5 + 20);
                                waitTillDone(sDelay);
                                if (restart == true) return;  /* Exit to restart simulation */
                                inputDigit = bb.getAcceptedInteger();
                                bb.clearHighlight();
                                if(inputDigit<0)
                                    bb.eraseArea((charGapX * 12+10)+s , lineGapY * 5 + 20, 25, 30);
                            }while(inputDigit<0);

                            if (inputDigit != b[j]) {   
                                addErrorRecord((charGapX * 12+10)+s , lineGapY * 5 + 20, 25,20);
                                isCorrect = false;
                            }

                            if(j<5){
                                s=s+20;
                                bb.writeString(" + ",charGapX*12 +20 + s,lineGapY*5+20,false);
                                s=s+20;
                                if (restart == true) {
                                    return;  /* Exit to restart simulation */
                                }
                            }
                        }

                        bb.setFontSize(20);
                        bb.writeString(" = ",charGapX*18+10,lineGapY*5,false);
                        do{
                                bb.drawHighlightRectangle(charGapX*18+35 ,lineGapY*5 -15, 30, 20,Color.BLUE);
                                bb.acceptInteger(addDigits, charGapX*18+35 ,lineGapY*5 -15);
                                waitTillDone(sDelay);
                                if (restart == true) return;  /* Exit to restart simulation */
                                inputDigit = bb.getAcceptedInteger();
                                bb.clearHighlight();
                                if(inputDigit<0)
                                    bb.eraseArea(charGapX*18+35 ,lineGapY*5 -15, 30, 20);
                        }while(inputDigit<0);

                        if (inputDigit != b[i]) {   
                            addErrorRecord(charGapX*18+35 ,lineGapY*5 -15, 30, 20);
                            isCorrect = false;
                        }

                        bb.drawLine(charGapX*18+30,lineGapY*5 + 10,charGapX*18+90,lineGapY*5 + 10,Color.WHITE,1);

                        do{
                                bb.drawHighlightRectangle(charGapX*18+35,lineGapY*5 + 20,2* charGapX-20, 20,Color.BLUE);
                                bb.acceptInteger(addDigits, charGapX*18+35,lineGapY*5 + 20);
                                waitTillDone(sDelay);
                                if (restart == true) return;  /* Exit to restart simulation */
                                inputDigit = bb.getAcceptedInteger();
                                bb.clearHighlight();
                                if(inputDigit<0)
                                    bb.eraseArea(charGapX*18+35,lineGapY*5 + 20, 2*charGapX, 20);
                        }while(inputDigit<0);

                        if (inputDigit != sum) {   
                            addErrorRecord(charGapX*18+35,lineGapY*5 + 20, 2*charGapX, 20);
                            isCorrect = false;
                        }
                        if(i<5)
                        bb.clearHighlight();

                    if (restart == true) {
                        return;  /* Exit to restart simulation */
                    }

                    if (isCorrect == false) {
                        showErrorLog();
                    } 
                    else {
                    guidedCorrect++;
                    }

                    bb.writeHighlightString("Total " + guidedCorrect + "/" + guidedExamples, buttonPositionX, buttonPositionY - lineGapY, false);
                    delaySimple(mDelay);
                    bb.eraseArea(charGapX*2 -10, lineGapY*5 -20,charGapX*19-10 , lineGapY*3);
                    }
                }
            }            
            
            else if (mode == testMode) {  
          
                testExamples++;
                clearErrorLog();
                isCorrect = true;
                
                bb.setFontSize(20);
                bb.clearHighlight();
                bb.drawHighlightRectangle(charGapX * 8, lineGapY * 5 - 30 , 3*charGapX, lineGapY, Color.BLUE);
                
                int i= random.nextInt(6);
                bb.writeString("Probability of "+ c[n][i]+"  = ",charGapX*2,lineGapY*5 ,false);
                
                bb.acceptDecimalNumber(4, charGapX * 8+10, lineGapY * 5);
                waitTillDone(sDelay);
                float answer = bb.getAcceptedDecimalNumber();
                
                if (restart == true) {
                    return;  /* Exit to restart simulation */
                }
                
                float res = (float)b[i]/sum;
                if ( (answer<res-0.01) || (answer>res+0.01) ) {  
                    addErrorRecord(charGapX * 8, lineGapY * 5 - 30, 3*charGapX, lineGapY);
                    isCorrect = false;
                }
                 
                if (isCorrect == false) {
                    showErrorLog();
                } 
                else {
                   testCorrect++;
                }
            
                bb.writeString("Total " + testCorrect + "/" + testExamples, buttonPositionX, buttonPositionY - lineGapY, false);
                delaySimple(mDelay);
            }
    /* Never Ending Loop till external action restarts solveProblem */
}

/**
 *
 * The following method highlights the errors in errorLog
 *
 */
private void showErrorLog()
{
int index;

    index = 0;
    while (index < numberErrors)
    {
        bb.drawHighlightRectangle(errorLog[index].topLeftX,errorLog[index].topLeftY,
                                  errorLog[index].width,errorLog[index].height, Color.YELLOW);
        index++;
    }
}

/* End of class Teacher */
} 
