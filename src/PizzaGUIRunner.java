import javax.swing.JFrame;

/**
 * PizzaGUIRunner is the main class for the Pizza Order form. It creates
 * the PizzaGUIFrame and makes it visible.
 */
public class PizzaGUIRunner
{
    /**
     * Program entry point.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        PizzaGUIFrame frame = new PizzaGUIFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }
}
