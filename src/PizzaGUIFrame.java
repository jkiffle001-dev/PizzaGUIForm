import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * PizzaGUIFrame is a form for ordering one pizza. The user picks a crust
 * (JRadioButton group), a size (JComboBox) and toppings (JCheckBox).
 * Order builds a cash-register style receipt in a JTextArea, Clear resets
 * the whole form, and Quit asks for confirmation before exiting.
 *
 * Prices: Small $8, Medium $12, Large $16, Super $20; each topping $1;
 * tax 7% of the sub-total.
 */
public class PizzaGUIFrame extends JFrame
{
    private static final String[] SIZES = {"Small", "Medium", "Large", "Super"};
    private static final double[] SIZE_PRICES = {8.00, 12.00, 16.00, 20.00};
    private static final String[] TOPPINGS = {
            "Pepperoni", "Mushrooms", "Extra Cheese", "Dragon Peppers",
            "Troll Toe Sausage", "Swamp Onions", "Kraken Calamari", "Ghost Pepper Glitter"};
    private static final double TOPPING_PRICE = 1.00;
    private static final double TAX_RATE = 0.07;
    private static final String DOUBLE_LINE = "=".repeat(44);
    private static final String SINGLE_LINE = "-".repeat(44);

    private JRadioButton thinRB;
    private JRadioButton regularRB;
    private JRadioButton deepDishRB;
    private ButtonGroup crustGroup;

    private JComboBox<String> sizeCB;
    private final JCheckBox[] toppingCBs = new JCheckBox[TOPPINGS.length];

    private JTextArea receiptTA;

    private final Font titleFont = new Font("Serif", Font.BOLD, 32);
    private final Font formFont = new Font("SansSerif", Font.PLAIN, 16);
    private final Font receiptFont = new Font("Monospaced", Font.PLAIN, 16);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 16);

    /**
     * Builds the form. The crust, size and toppings panels sit side by side
     * across the top; the receipt panel is above the button panel at the
     * bottom.
     */
    public PizzaGUIFrame()
    {
        super("Pizza Order Form");

        JPanel mainPnl = new JPanel(new BorderLayout(10, 10));
        mainPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLbl = new JLabel("Monster Pizza Co. - Order Form", SwingConstants.CENTER);
        titleLbl.setFont(titleFont);

        JPanel choicesPnl = new JPanel(new BorderLayout(10, 10));
        choicesPnl.add(createCrustPanel(), BorderLayout.WEST);
        choicesPnl.add(createSizePanel(), BorderLayout.CENTER);
        choicesPnl.add(createToppingsPanel(), BorderLayout.EAST);

        JPanel topPnl = new JPanel(new BorderLayout(10, 10));
        topPnl.add(titleLbl, BorderLayout.NORTH);
        topPnl.add(choicesPnl, BorderLayout.CENTER);

        mainPnl.add(topPnl, BorderLayout.NORTH);
        mainPnl.add(createReceiptPanel(), BorderLayout.CENTER);
        mainPnl.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPnl);

        // closing the window behaves like the Quit button
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                confirmQuit();
            }
        });

        setSize(900, 820);
        setLocationRelativeTo(null);
    }

    /**
     * @return the crust panel: three radio buttons in one ButtonGroup
     */
    private JPanel createCrustPanel()
    {
        JPanel crustPnl = new JPanel(new GridLayout(3, 1, 5, 5));
        crustPnl.setBorder(BorderFactory.createTitledBorder("Crust"));

        thinRB = new JRadioButton("Thin");
        regularRB = new JRadioButton("Regular");
        deepDishRB = new JRadioButton("Deep-dish");

        crustGroup = new ButtonGroup();
        for (JRadioButton rb : new JRadioButton[]{thinRB, regularRB, deepDishRB})
        {
            rb.setFont(formFont);
            crustGroup.add(rb);
            crustPnl.add(rb);
        }
        return crustPnl;
    }

    /**
     * @return the size panel with a combo box of sizes and prices
     */
    private JPanel createSizePanel()
    {
        JPanel sizePnl = new JPanel(new BorderLayout(5, 5));
        sizePnl.setBorder(BorderFactory.createTitledBorder("Size"));

        sizeCB = new JComboBox<>();
        sizeCB.addItem("-- Select a size --");
        for (int i = 0; i < SIZES.length; i++)
        {
            sizeCB.addItem(String.format("%s ($%.2f)", SIZES[i], SIZE_PRICES[i]));
        }
        sizeCB.setFont(formFont);

        JPanel holder = new JPanel();
        holder.add(sizeCB);
        sizePnl.add(holder, BorderLayout.NORTH);
        return sizePnl;
    }

    /**
     * @return the toppings panel: a check box for each topping ($1.00 each)
     */
    private JPanel createToppingsPanel()
    {
        JPanel toppingsPnl = new JPanel(new GridLayout(4, 2, 5, 5));
        toppingsPnl.setBorder(BorderFactory.createTitledBorder("Toppings ($1.00 each)"));
        for (int i = 0; i < TOPPINGS.length; i++)
        {
            toppingCBs[i] = new JCheckBox(TOPPINGS[i]);
            toppingCBs[i].setFont(formFont);
            toppingsPnl.add(toppingCBs[i]);
        }
        return toppingsPnl;
    }

    /**
     * @return the receipt panel: a read-only JTextArea in a JScrollPane
     */
    private JPanel createReceiptPanel()
    {
        JPanel receiptPnl = new JPanel(new BorderLayout());
        receiptPnl.setBorder(BorderFactory.createTitledBorder("Your Order"));
        receiptTA = new JTextArea(16, 44);
        receiptTA.setEditable(false);
        receiptTA.setFont(receiptFont);
        receiptPnl.add(new JScrollPane(receiptTA), BorderLayout.CENTER);
        return receiptPnl;
    }

    /**
     * @return the button panel with Order, Clear and Quit
     */
    private JPanel createButtonPanel()
    {
        JPanel buttonPnl = new JPanel(new GridLayout(1, 3, 20, 10));

        JButton orderBtn = new JButton("Order");
        JButton clearBtn = new JButton("Clear");
        JButton quitBtn = new JButton("Quit");

        orderBtn.addActionListener((ActionEvent ae) -> placeOrder());
        clearBtn.addActionListener((ActionEvent ae) -> clearForm());
        quitBtn.addActionListener((ActionEvent ae) -> confirmQuit());

        for (JButton b : new JButton[]{orderBtn, clearBtn, quitBtn})
        {
            b.setFont(buttonFont);
            buttonPnl.add(b);
        }
        return buttonPnl;
    }

    /**
     * Builds the order from the form and shows it as a receipt. The order
     * must have a crust, a size and at least one topping.
     */
    private void placeOrder()
    {
        String crust = getSelectedCrust();
        int sizeIndex = sizeCB.getSelectedIndex() - 1;   // index 0 is the prompt

        StringBuilder missing = new StringBuilder();
        if (crust == null)
        {
            missing.append("- a crust\n");
        }
        if (sizeIndex < 0)
        {
            missing.append("- a size\n");
        }
        boolean anyTopping = false;
        for (JCheckBox cb : toppingCBs)
        {
            anyTopping = anyTopping || cb.isSelected();
        }
        if (!anyTopping)
        {
            missing.append("- at least one topping\n");
        }
        if (missing.length() > 0)
        {
            JOptionPane.showMessageDialog(this, "Please choose:\n" + missing,
                    "Incomplete Order", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double subTotal = SIZE_PRICES[sizeIndex];
        StringBuilder r = new StringBuilder();
        r.append(DOUBLE_LINE).append('\n');
        r.append(line("Type of Crust & Size", "Price"));
        r.append(line(crust + " crust, " + SIZES[sizeIndex], money(SIZE_PRICES[sizeIndex])));
        r.append(line("Ingredient", "Price"));
        for (JCheckBox cb : toppingCBs)
        {
            if (cb.isSelected())
            {
                r.append(line("  " + cb.getText(), money(TOPPING_PRICE)));
                subTotal += TOPPING_PRICE;
            }
        }
        double tax = subTotal * TAX_RATE;
        double total = subTotal + tax;

        r.append('\n');
        r.append(line("Sub-total:", money(subTotal)));
        r.append(line("Tax (7%):", money(tax)));
        r.append(SINGLE_LINE).append('\n');
        r.append(line("Total:", money(total)));
        r.append(DOUBLE_LINE).append('\n');

        receiptTA.setText(r.toString());
        receiptTA.setCaretPosition(0);
    }

    /**
     * @return one receipt line: text on the left, amount right-aligned
     */
    private static String line(String left, String right)
    {
        return String.format("%-32s%12s%n", left, right);
    }

    /**
     * @return the amount formatted as dollars, e.g. "$19.00"
     */
    private static String money(double amount)
    {
        return String.format("$%.2f", amount);
    }

    /**
     * @return the selected crust name, or null if none is selected
     */
    private String getSelectedCrust()
    {
        if (thinRB.isSelected())
        {
            return "Thin";
        }
        if (regularRB.isSelected())
        {
            return "Regular";
        }
        if (deepDishRB.isSelected())
        {
            return "Deep-dish";
        }
        return null;
    }

    /**
     * Wipes every component so the form is ready for a new order.
     */
    private void clearForm()
    {
        crustGroup.clearSelection();
        sizeCB.setSelectedIndex(0);
        for (JCheckBox cb : toppingCBs)
        {
            cb.setSelected(false);
        }
        receiptTA.setText("");
    }

    /**
     * Asks the user to confirm before quitting.
     */
    private void confirmQuit()
    {
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?",
                "Quit", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }
}
