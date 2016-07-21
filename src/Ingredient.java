/**
 * Created by Thomas on 04-Jul-16.
 */
public class Ingredient {
    private String ingredientName;
    private String amount;

    public Ingredient(String name, String amount) {
        ingredientName = name;
        this.amount = amount;
    }

    public String getName() {
        return ingredientName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String newAmount) {
        amount = newAmount;
    }
}
