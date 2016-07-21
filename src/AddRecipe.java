import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class AddRecipe extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lblAddRecipe;
    private JLabel lblRecipeName;
    private JTextField txtRecipeName;
    private JLabel lblRecipeLocation;
    private JTextField txtRecipeLocation;
    private JLabel lblTimeToCook;
    private JSpinner spnTimeToCook;
    private JLabel lblMealType;
    private JComboBox cboxMealType;
    private JLabel lblIngredients;
    private JButton addIngredientButton;
    private JLabel lblCurrentIngredients;
    private JList lstIngredients;
    private JTextField txtIngredientName;
    private JTextField txtAmount;
    private JLabel lblIngredientName;
    private JLabel lblIngredientAmount;
    private JScrollPane ingredientsScroll;
    private JTable tblIngredients;
    private JLabel lblResponce;
    private DefaultTableModel tableModel;
    private DefaultListModel ingredientsModel;
    private DefaultListModel amountsModel;

    private Recipe result = null;

    public AddRecipe() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //Set up spinner
        SpinnerNumberModel timeModel = new SpinnerNumberModel(0, 0, 400, 1) ;
        spnTimeToCook.setModel(timeModel);

        //Set up comboBox
        cboxMealType.setModel(new DefaultComboBoxModel<>(MealType.values()));

        //Set up lists
        ingredientsModel = new DefaultListModel();
        amountsModel = new DefaultListModel();

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

        addIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {onAdd(); }
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

    public Recipe showDialog() {
        setVisible(true);
        return result;
    }

    private void onOK() {
        if (txtRecipeName.getText().equals("")) {
            errorMessage("No Recipe Name");
        } else if (txtRecipeLocation.getText().equals("")) {
            errorMessage("No Recipe Location");
        } else if (spnTimeToCook.getValue().equals(0)) {
            errorMessage("Enter a valid time");
        } else if (tableModel.getRowCount() == 0) {
            errorMessage("Enter Ingredients");
        } else {

            result = new Recipe(txtRecipeName.getText(), txtRecipeLocation.getText(), (int) spnTimeToCook.getValue(), (MealType) cboxMealType.getSelectedItem());

            List<Ingredient> ingredientsList = new LinkedList<>();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = (String) tableModel.getValueAt(i, 0);
                String amount = (String) tableModel.getValueAt(i, 1);

                Ingredient newIngr = new Ingredient(name, amount);
                ingredientsList.add(newIngr);
            }

            result.setIngredients(ingredientsList);

            setVisible(false);
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onAdd() {
        if (!txtIngredientName.getText().equals("")) {
            if(!txtAmount.getText().equals("")) {
                String name = txtIngredientName.getText();
                String amount = txtAmount.getText();

                tableModel.addRow(new Object[] {name, amount});

                txtIngredientName.setText("");
                txtAmount.setText("");
            }
        }
    }

    private void errorMessage(String error) {
        lblResponce.setText(error);
    }
}

