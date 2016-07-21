import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 04-Jul-16.
 */
public class Recipe {
    private String mealName;
    private List<Ingredient> ingredients = new LinkedList<>();
    private String recipe;
    private int timeToCook;
    private MealType type;

    public Recipe(String name, String recipe, int time, MealType type) {
        mealName = name;
        this.recipe = recipe;
        timeToCook = time;
        this.type = type;
    }

    public Recipe(String name, List<Ingredient> ingredients, String recipe, int time, MealType type) {
        mealName = name;
        this.recipe = recipe;
        this.ingredients = ingredients;
        timeToCook = time;
        this.type = type;
    }

    public String getMealName() {
        return mealName;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getRecipe() {
        return recipe;
    }

    public int getTimeToCook() {
        return timeToCook;
    }

    public MealType getType() {
        return type;
    }

    public void setIngredients(List<Ingredient> newIngredients) {
        ingredients = newIngredients;
    }

    @Override
    public String toString() {
        return mealName;
    }

}
