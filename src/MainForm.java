import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Thomas on 04-Jul-16.
 */
public class MainForm {
    private JButton btnGenerate;
    private JButton addButton;
    private JPanel rootPanel;
    private JComboBox cboxMon;
    private JLabel lblMonday;
    private JComboBox cboxTues;
    private JLabel lblTues;
    private JLabel lblWed;
    private JComboBox cboxWed;
    private JLabel lblThurs;
    private JComboBox cboxThurs;
    private JLabel lblFri;
    private JComboBox cboxFri;
    private JLabel lblSat;
    private JComboBox cboxSat;
    private JLabel lblSun;
    private JComboBox cboxSun;
    private JSpinner spnMon;
    private JSpinner spnTues;
    private JSpinner spnWed;
    private JSpinner spnThurs;
    private JSpinner spnFri;
    private JSpinner spnSat;
    private JSpinner spnSun;
    private JButton btnViewRecipes;
    private JLabel lblMonResult;
    private JLabel lblTuesResult;
    private JLabel lblWedResult;
    private JLabel lblThursResult;
    private JLabel lblFriResult;
    private JLabel lblSatResult;
    private JLabel lblSunResult;
    private JLabel lblError;
    private JButton shoppingListButton;
    private static List<Recipe> recipes = new LinkedList<>();
    private static File file = new File("recipes.txt");
    private File shoppingFile = new File("shoppinglist.txt");
    private static List<Recipe> currentMealPlan = new LinkedList<>();

    public MainForm() {

        //Set up meal type combo boxes
        cboxMon.setModel(new DefaultComboBoxModel<>(MealType.values()));
        cboxTues.setModel(new DefaultComboBoxModel<>(MealType.values()));
        cboxWed.setModel(new DefaultComboBoxModel<>(MealType.values()));
        cboxThurs.setModel(new DefaultComboBoxModel<>(MealType.values()));
        cboxFri.setModel(new DefaultComboBoxModel<>(MealType.values()));
        cboxSat.setModel(new DefaultComboBoxModel<>(MealType.values()));
        cboxSun.setModel(new DefaultComboBoxModel<>(MealType.values()));

        //Set up meal time spinners
        spnMon.setModel(new SpinnerNumberModel(60,0,400, 1));
        spnTues.setModel(new SpinnerNumberModel(60,0,400, 1));
        spnWed.setModel(new SpinnerNumberModel(60,0,400, 1));
        spnThurs.setModel(new SpinnerNumberModel(60,0,400, 1));
        spnFri.setModel(new SpinnerNumberModel(60,0,400, 1));
        spnSat.setModel(new SpinnerNumberModel(60,0,400, 1));
        spnSun.setModel(new SpinnerNumberModel(60,0,400, 1));


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRecipe dialog = new AddRecipe();
                dialog.pack();
                Recipe newRecipe = dialog.showDialog();
                if (newRecipe == null) {
                    return;
                }
                recipes.add(newRecipe);

                saveRecipes();
            }
        });

        btnViewRecipes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewAllRecipes dialog = new ViewAllRecipes();
                dialog.pack();
                recipes = dialog.showDialog(recipes);

                saveRecipes();
            }
        });

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Generate all possible recipes for each day;
                List<List<Recipe>> dayList = new LinkedList<List<Recipe>>();
                dayList.add(generateDayList((int) spnMon.getValue(), (MealType) cboxMon.getSelectedItem()));
                dayList.add(generateDayList((int) spnTues.getValue(), (MealType) cboxTues.getSelectedItem()));
                dayList.add(generateDayList((int) spnWed.getValue(), (MealType) cboxWed.getSelectedItem()));
                dayList.add(generateDayList((int) spnThurs.getValue(), (MealType) cboxThurs.getSelectedItem()));
                dayList.add(generateDayList((int) spnFri.getValue(), (MealType) cboxFri.getSelectedItem()));
                dayList.add(generateDayList((int) spnSat.getValue(), (MealType) cboxSat.getSelectedItem()));
                dayList.add(generateDayList((int) spnSun.getValue(), (MealType) cboxSun.getSelectedItem()));

                List<JLabel> labels = new LinkedList<>();
                labels.add(lblMonResult);
                labels.add(lblTuesResult);
                labels.add(lblWedResult);
                labels.add(lblThursResult);
                labels.add(lblFriResult);
                labels.add(lblSatResult);
                labels.add(lblSunResult);

                //Check if possible, show error if there is
                for (int i = 0; i < dayList.size(); i++) {
                    List<Recipe> recipeList = dayList.get(i);
                    if (recipeList.size() == 0) {
                        lblError.setText("On " + intToDay(i) + " There is no possible meal, either add more meals or change settings");
                        return;
                    }
                }

                //No error, get rid of any error message
                lblError.setText(" ");

                //Generate the meal plan
                currentMealPlan = new LinkedList<Recipe>();
                for (int i = 0; i < dayList.size(); i++) {
                    List<Recipe> recipeList = dayList.get(i);
                    JLabel label = labels.get(i);
                    int random = randInt(recipeList.size() - 1);
                    Recipe selectedRecipe = recipeList.get(random);
                    label.setText(format(selectedRecipe.toString()));
                    currentMealPlan.add(selectedRecipe);

                    //If possible remove the recipe from the other days possible plans
                    for (List<Recipe> toRemoveDay : dayList) {
                        if (toRemoveDay.size() > 1) {
                            toRemoveDay.remove(selectedRecipe);
                        }
                    }

                }
            }
        });

        shoppingListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter writer = new PrintWriter(shoppingFile.getPath());
                    for (Recipe recipe : currentMealPlan) {
                        for (Ingredient ingredient : recipe.getIngredients()) {
                            writer.println(ingredient.getName() + "\t\t" + ingredient.getAmount());
                        }
                    }

                    writer.close();

                    Desktop.getDesktop().open(shoppingFile);
                } catch (FileNotFoundException error) {
                    System.err.println("File not found");
                    try {
                        file.createNewFile();
                        actionPerformed(e);
                    } catch (IOException ioerr) {
                        System.err.println("Couldn't create new file");
                        ioerr.printStackTrace();
                    }
                } catch (IOException ioerr) {
                    System.err.println("Couldn't open file");
                }
            }
        });

        lblMonResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ViewRecipe dialog = new ViewRecipe();
                dialog.pack();
                String toSearch = lblMonResult.getText().replaceAll("<html>", "").replaceAll("-<br>-", "");
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(toSearch)) {
                        dialog.showDialog(recipe);
                        break;
                    }
                }
            }
        });

        lblTuesResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ViewRecipe dialog = new ViewRecipe();
                dialog.pack();
                String toSearch = lblTuesResult.getText().replaceAll("<html>", "").replaceAll("-<br>-", "");
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(toSearch)) {
                        dialog.showDialog(recipe);
                        break;
                    }
                }
            }
        });

        lblWedResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ViewRecipe dialog = new ViewRecipe();
                dialog.pack();
                String toSearch = lblWedResult.getText().replaceAll("<html>", "").replaceAll("-<br>-", "");
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(toSearch)) {
                        dialog.showDialog(recipe);
                        break;
                    }
                }
            }
        });

        lblThursResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ViewRecipe dialog = new ViewRecipe();
                dialog.pack();
                String toSearch = lblThursResult.getText().replaceAll("<html>", "").replaceAll("-<br>-", "");
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(toSearch)) {
                        dialog.showDialog(recipe);
                        break;
                    }
                }
            }
        });

        lblFriResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ViewRecipe dialog = new ViewRecipe();
                dialog.pack();
                String toSearch = lblFriResult.getText().replaceAll("<html>", "").replaceAll("-<br>-", "");
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(toSearch)) {
                        dialog.showDialog(recipe);
                        break;
                    }
                }
            }
        });

        lblSatResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ViewRecipe dialog = new ViewRecipe();
                dialog.pack();
                String toSearch = lblSatResult.getText().replaceAll("<html>", "").replaceAll("-<br>-", "");
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(toSearch)) {
                        dialog.showDialog(recipe);
                        break;
                    }
                }
            }
        });

        lblSunResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ViewRecipe dialog = new ViewRecipe();
                dialog.pack();
                String toSearch = lblSunResult.getText().replaceAll("<html>", "").replaceAll("-<br>-", "");
                for (Recipe recipe : recipes) {
                    if (recipe.toString().equals(toSearch)) {
                        dialog.showDialog(recipe);
                        break;
                    }
                }
            }
        });


    }

    private String format(String s) {
        if (s.length() > 20) {
            s = "<html>" + s.substring(0, 20) + "-<br>-" + formatHelper(s.substring(20));
        }
        return s;
    }

    private String formatHelper(String s) {
        if (s.length() > 20) {
            s = s.substring(0, 20) + "-<br>-" + formatHelper(s.substring(20));
        }
        return s;
    }

    private String intToDay(int number) {
        switch (number) {
            case 0: return "Monday";
            case 1: return "Tuesday";
            case 2: return "Wednesday";
            case 3: return "Thursday";
            case 4: return "Friday";
            case 5: return "Saturday";
            case 6: return "Sunday";
        }
        System.err.println("Not a valid day to convert");
        return null;
    }

    private int randInt (int max) {
        Random rand = new Random();
        return rand.nextInt(max + 1);
    }

    private List<Recipe> generateDayList(int time, MealType mealType) {
        List<Recipe> result = new LinkedList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getTimeToCook() <= time && recipe.getType().equals(mealType)) {
                result.add(recipe);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        //Initialisation
        loadRecipes();


        frame.setVisible(true);
    }

    private static void loadRecipes() {
        try {
            Path path = file.toPath();
            BufferedReader in = Files.newBufferedReader(path);
            String line = "null";
            do {
                if (line.equals("null")) {
                    line = in.readLine();
                    continue;
                }

                //Get individual parts of a recipe
                String name = line;
                String ingredients = in.readLine();
                String recipe = in.readLine();
                int timeToCook = Integer.valueOf(in.readLine());
                MealType type = MealType.valueOf(in.readLine());

                List<Ingredient> ingredientsList = new LinkedList<>();
                //Construct ingredients list
                Scanner scanner = new Scanner(ingredients);
                while (scanner.hasNext()) {
                    String ingredientName = scanner.next();
                    String amount = scanner.next();
                    ingredientName = ingredientName.replaceAll("_"," ");
                    amount = amount.replaceAll("_", " ");
                    Ingredient newIng = new Ingredient(ingredientName, amount);
                    ingredientsList.add(newIng);
                }

                //Construct Recipe
                Recipe newRecipe = new Recipe(name, ingredientsList, recipe, timeToCook, type);
                recipes.add(newRecipe);
                line = in.readLine();
            } while (!line.equals("EOF"));
        } catch (FileNotFoundException e) {
            System.err.println("Could not find previous saved recipes");
        } catch (IOException e) {
            System.err.println("IOException: No recipes saved recipes will be shown");
        }
    }

    private static void saveRecipes() {
        try {
            PrintWriter writer = new PrintWriter(file.getPath());
            for (Recipe recipe : recipes) {

                //Configure ingredients
                String ingredients = "";
                for (Ingredient ingredient : recipe.getIngredients()) {
                    ingredients += ingredient.getName().replaceAll(" ", "_") + " ";
                    ingredients += ingredient.getAmount().replaceAll(" ", "_") +  " ";
                }

                writer.println(recipe.getMealName());
                writer.println(ingredients);
                writer.println(recipe.getRecipe());
                writer.println(recipe.getTimeToCook());
                writer.println(recipe.getType());

            }
            writer.println("EOF");
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            try {
                file.createNewFile();
                saveRecipes();
            } catch (IOException ioerr) {
                System.err.println("Couldn't create new file");
                ioerr.printStackTrace();
            }
        }
    }

}
