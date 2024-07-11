package pojo;

import java.util.List;

import lombok.Data;

@Data
public class RecipeData {
	
	private String recipeId;
	private String recipeName;
	private String recipeCategory;
    private String foodCategory;
    private List<String> ingredients;
    private String prepTime;
	private String cookTime;
	private String tag;
	private String servings;
    private String cuisineCategory;
    private String recipeDescription;
    private String preparationMethod;
    private String nutrientValues;	
	private String recipeUrl;
	
	
}
