import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class ViewRecipe extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lblRecipeName;
    private JLabel lblRecipe;
    private JLabel lblTimeToCook;
    private JTable tblIngredients;
    private DefaultTableModel tableModel;

    public ViewRecipe() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        tableModel = new DefaultTableModel()
        {
            public boolean isCellEditable(int row, int Column){
                return false;
            }
        };
        tableModel.addColumn("Ingredient");
        tableModel.addColumn("Amount");
        tblIngredients.setModel(tableModel);
        tblIngredients.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void showDialog(Recipe recipe) {
        lblRecipeName.setText(recipe.toString());
        lblRecipe.setText(recipe.getRecipe());
        lblTimeToCook.setText(Integer.toString(recipe.getTimeToCook()) + " minutes");
        List<Ingredient> ingList = recipe.getIngredients();

        for (Ingredient ingredient : ingList) {
            tableModel.addRow(new Object[] {ingredient.getName(), ingredient.getAmount()});
        }


        setVisible(true);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
