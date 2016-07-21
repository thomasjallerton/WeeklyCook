import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class ViewAllRecipes extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList lstAllRecipes;
    private JButton btnRemove;
    private JButton btnAddRecipe;
    private JButton btnView;
    private List<Recipe> recipes;
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public ViewAllRecipes() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        lstAllRecipes.setModel(listModel);

        btnAddRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRecipe dialog = new AddRecipe();
                dialog.pack();
                Recipe newRecipe = dialog.showDialog();
                if (newRecipe == null) {
                    return;
                }
                recipes.add(newRecipe);
                listModel.addElement(newRecipe.toString());
            }
        });

        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = (String) lstAllRecipes.getSelectedValue();
                if (selection == null) {
                    return;
                }
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(selection)) {
                        recipes.remove(recipe);
                        break;
                    }
                }
                for (int i = 0; i < listModel.size(); i++) {
                    if (listModel.getElementAt(i).equals(selection)) {
                        listModel.remove(i);
                    }
                }
            }
        });

        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewRecipe dialog = new ViewRecipe();
                dialog.pack();
                String selection = (String) lstAllRecipes.getSelectedValue();
                if (selection == null) {
                    return;
                }
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(selection)) {
                        dialog.showDialog(recipe);
                        break;
                    }
                }
            }
        });

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

    public List<Recipe> showDialog(List<Recipe> allRecipes) {

        recipes = allRecipes;

        for (Recipe recipe : allRecipes) {
            listModel.add(0, recipe.toString());
        }
        setVisible(true);
        return recipes;
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
