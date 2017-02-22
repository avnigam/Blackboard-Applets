package Probability2aa;


import blackboard.Blackboard;
import javax.swing.JApplet;

/**
 *
 * @author avinash awate
 *
 */

/**
 *
 * This class is the top level class for the applet.
 * It instantiates a teacher class which knows about the topic to teach.
 * It instantiates a blackboard and provides it to the teacher class.
 * It supports the top level applet methods (init etc.).
 * It also handles user interactions (of the state of the applet) through a panel.
 * Other user interactions (student solving) are handled by the teacher.
 * These methods in turn call corresponding teacher methods which actually.
 * perform the required actions.
 *
 */
public class Probablilty2aa extends JApplet
{
private Blackboard bb;                  /* Black board to interact with student */
private ControlPanel controls;          /* Panel at bottom to control applet */
private Teacher teacher;                /* Manages student laerning thorugh blackboard */
private boolean firstTime;              /* To restrict maximize/Minimize etc. */
private int mode;                       /* (demo, interaction, solve, test) */
private final static int demoMode = 1;           /* demo mode is initialized for the value of mode is 1 */
private final static int interactionMode = 2;    /* interaction mode is initialized for the value of mode is 2 */
private final static int solveMode = 3;          /* solve mode is initialized for the value of mode is 3 */
private final static int testMode = 4;           /* test mode is initialized for the value of mode is 4 */

/* Size of display elements */
private int topLeftX;                   /* X coordinate of Top Left Corner */
private int topLeftY;                   /* X coordinate of Top Left Corner */
private int displayWidth;               /* Width of applet */
private int blackboardHeight;           /* Height of blackboard */
private int panelHeight;                /* Height of Panel */
private int digit;                      /* number of digits for addition */

/**
 *
 * Initialization method that will be called after the applet is loaded.
 * into the browser.
 * In this method, all resources such as images will be loaded.
 * and all support objects instantiated.
 * After this method, the applet is ready to run.
 *
 */
@Override
public void init()
{
    /* Initialise Display Variables */
    setDisplayVariables();
    this.setSize(800,650);
    /* Load resources such as images for the application */
    loadResources();

    /* We want to do our own layout */
    this.setLayout(null);

    /* Instantiate a blackboard object */
    bb = new Blackboard(topLeftX, topLeftY, displayWidth, blackboardHeight);
    this.add(bb);                       /* Display Blackboard */

    /* Instantiate a teacher object */
    teacher = new Teacher(bb, digit);

    /* Instantiate a control panel */
    controls = new ControlPanel(this, topLeftX, blackboardHeight, displayWidth, panelHeight);
    this.add(controls);                 /* Display Controls */
}

/**
 *
 * start method.
 * Value of firstTime is set to false.
 * This ensures that minimizing and maximizing the screen doesn't restart the run method.
 *
 */
@Override
public void start()
{
    if (firstTime == true)
    {
        firstTime = false;
        teacher.setAddDigits(digit);
        teacher.start();
    }
}

/**
 *
 * method to set default values of variables.
 *
 */
public void setDisplayVariables()
{
    /* Initialise Display Variables */
    topLeftX = 0;
    topLeftY = 0;
    displayWidth = 800;
    blackboardHeight = 600;
    panelHeight = 50;

    /* Set other defaults */
    firstTime = true;
    mode = demoMode;
}

/**
 *
 * This method loads resources such as images etc.
 *
 */
public void loadResources()
{
    /* This application does not have application Level Resources */
    /* reading the digit for addition (like 3 digit addition, 4 digit addition */
    String tempDigit = this.getParameter("digit");
    if( tempDigit!= null)
    {
        /* catching number format exception in read parameter*/
        try {
            digit = Integer.parseInt(tempDigit);    /* Parse the string to integer */
            if(digit > 4 || digit < 1) {
                digit = 4;
            }

        }catch(NumberFormatException nfe) {
            digit = 4;
        }
    } else {
        digit = 4;
    }
}

/**
 *
 * This method is called to set value of data element mode.
 * If the value changes then processMode is called to take appropriate action.
 *
 */
public void setMode(int mode)
{
    if ( (mode >= demoMode) && (mode <= testMode) )
    {   /* Valid parameter */
        /* if mode has changed, invoke actions */
       if (mode != getMode())
       {
            this.mode = mode;
            processMode();
       }
    }
}

/**
 *
 * This method is called to get value of data element mode.
 *
 */
public int getMode()
{
    return(this.mode);
}

/**
 *
 * This method is called whenever there is a change in mode.
 *
 */
public void processMode()
{
   teacher.changeMode(getMode());
}

/* End of class FourDigit Addition */
}
