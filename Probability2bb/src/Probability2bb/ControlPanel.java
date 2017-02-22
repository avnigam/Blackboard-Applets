
package Probability2bb;


import java.awt.MediaTracker;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * This class displays a panel.
 * The panel contains multiple buttons which can be used to control the teaching.
 * In this panel we have only two buttons.
 * The next button changes the mode of the teaching to next level.
 * The previous button changes the mode of teaching to previous level.
 * @author rkjangir
 *
 */
public class ControlPanel extends JPanel implements ActionListener
{
/* variable declaration for Control Panel */
JButton next;                           /* Button to go for next mode */
JButton previous;                       /* Button to go for previous mode */
Probablilty2bb parent;           /* To access parent methods for communication */

private ImageIcon nextIcon=null;        /* Icon for the next button */
private ImageIcon previousIcon=null;    /* Icon for the prvious button */

private final static int demoMode = 1;           /* demo mode is initialized for the value of mode is 1 */
private final static int interactionMode = 2;    /* interaction mode is initialized for the value of mode is 2 */
private final static int solveMode = 3;          /* solve mode is initialized for the value of mode is 3 */
private final static int testMode = 4;           /* test mode is initialized for the value of mode is 4 */

/**
 *
 * Control Panel constructor.
 * This method first sets the bounds for the panel.
 * It then initialises display variables.
 * It then loads resources such as images.
 * Finally, the button panel is created.
 * @param p Handle to parent to access the teaching mode.
 * @param tlX X coordinate of top left corner.
 * @param tlY Y coordinate of top left corner.
 * @param w Panel width.
 * @param h Panel height.
 *
 */
public ControlPanel(Probablilty2bb p, int tlX, int tlY, int w, int h)
{
    setBounds(tlX, tlY, w, h);
    this.parent = p;
    initialise();
    createPanel();
}

/**
 *
 * This method creates the panel of buttons.
 * It first calculates the placement of the buttons in the center.
 * It then loads the next and previous buttons (with Hand Cursor).
 *
 */
public final void createPanel()
{
int topLeftX;                           /* X Co-ordinate of Top Left corner */
int topLeftY;                           /* Y Co-ordinate of Top Left corner */
int panelWidth;                         /* Width of panel */
int panelHeight;                        /* Height of panel */
int numberButtons;                      /* Number of Buttons */
int buttonWidth;                        /* Button Width */
int buttonHeight;                       /* Button Height */
int buttonGap;                          /* Gap between Buttons */
int buttonTLX;                          /* Currrent Button Top Left X */
int buttonTLY;                          /* Currrent Button Top Left Y */

    /* We want to do our own layout */
    this.setLayout(null);
    /* Set display characteristics */
    setDisplayCharacteristics();

    /* Position buttons in center */
    topLeftX = getX();
    topLeftY = getY();
    panelWidth =  getWidth();
    panelHeight =  getHeight();
    numberButtons = 2;
    buttonWidth = 50;
    buttonHeight = 16;
    buttonGap = 24;
    buttonTLX = (panelWidth  - ((numberButtons * (buttonWidth + buttonGap) - buttonGap))) / 2;
    buttonTLY = (panelHeight - (buttonHeight)) / 2;

    /* Load Next Button */
    if (nextIcon != null)
    {
        next = new JButton(nextIcon);
        /* Set Attributes of button */
        next.setContentAreaFilled(false);
        next.setFocusPainted(false);
        next.setBorderPainted(false);
        next.setRolloverEnabled(false);
    }
    else
    {
        next = new JButton("Next");
    }
    next.setBounds(buttonTLX,buttonTLY,buttonWidth,buttonHeight);
    next.setCursor(new Cursor(Cursor.HAND_CURSOR));
    next.setActionCommand("nextMode");
    next.addActionListener(this);
    add(next);
    next.setVisible(true);
    next.setEnabled(true);
    buttonTLX += (buttonWidth + buttonGap);

    /* Load Previous Button */
    if (previousIcon != null)
    {
        previous = new JButton(previousIcon);
        /* Set Attributes of button */
        previous.setContentAreaFilled(false);
        previous.setFocusPainted(false);
        previous.setBorderPainted(false);
        previous.setRolloverEnabled(false);
        
    }
    else
    {
        previous = new JButton("Previous");
    }
    previous.setBounds(buttonTLX,buttonTLY,buttonWidth,buttonHeight);
    previous.setCursor(new Cursor(Cursor.HAND_CURSOR));
    previous.setActionCommand("previousMode");
    previous.addActionListener(this);
    add(previous);
    previous.setVisible(true);
    previous.setEnabled(false);
}

/**
 *
 * This method sets display variables.
 *
 */
public void setDisplayCharacteristics()
{
    setForeground(Color.WHITE);
    setBackground(Color.GRAY);
    setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
}

/**
 *
 * This method loads resources required.
 *
 */
private void initialise()
{
Image nextImage;                        /* Next Arrow on button */
Image previousImage;                    /* Previous Arrow on button */

    nextImage = null;
    previousImage = null;
    try
    {
        nextImage =  ImageIO.read(this.getClass().getResource("next.png"));
        previousImage = ImageIO.read(this.getClass().getResource("previous.png"));
    }
    catch(IOException e){
        System.out.println("Cannot find file: " + e);
    }
    
    /* Good practice to have a media tracker to ensure that all images have finished loading */
    MediaTracker tracker = new MediaTracker(this);
    tracker.addImage(nextImage, 0);
    tracker.addImage(previousImage, 1);
    try {
            tracker.waitForAll();
    }
    catch (Exception e) { }
    if (nextImage != null) nextIcon = new ImageIcon(nextImage, "Next");
    if (previousImage != null) previousIcon = new ImageIcon(previousImage, "Previous");
}

/**
 *
 * Method to perform actions.
 * Handles two actions.
 * The next button starts the next mode.
 *     - enables previous button (action).
 *     - increments and changes parents mode.
 *     - if maximum mode (test) disables itself.
 * The previous button starts the previous mode.
 *     - enables next button (action).
 *     - decrements and changes parents mode.
 *     - if minimum mode (demo) disables itself.
 * @param e Action event.
 *
 */
public void actionPerformed(ActionEvent e)
{
int currentMode;                        /* Current Mode */

    currentMode = parent.getMode();
    if(e.getActionCommand().equals("nextMode"))
    {
        if (currentMode < testMode)
        {
            previous.setEnabled(true);
            currentMode++;
            if (currentMode == testMode) next.setEnabled(false);
            parent.setMode(currentMode);
        }
    }
    else if(e.getActionCommand().equals("previousMode"))
    {
        if (currentMode > demoMode)
        {
            next.setEnabled(true);
            currentMode--;
            if (currentMode == demoMode) previous.setEnabled(false);
            parent.setMode(currentMode);
        }
    }
}

/* End of class controlpanel */
}
