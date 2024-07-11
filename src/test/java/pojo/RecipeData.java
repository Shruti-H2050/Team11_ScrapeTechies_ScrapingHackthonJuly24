package pojo;

import java.util.List;

//import lombok.Data;

//@Data
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
	public String getRecipeId() {
		return recipeId;
	}
	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}
	public String getRecipeName() {
		return recipeName;
	}
	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
	public String getRecipeCategory() {
		return recipeCategory;
	}
	public void setRecipeCategory(String recipeCategory) {
		this.recipeCategory = recipeCategory;
	}
	public String getFoodCategory() {
		return foodCategory;
	}
	public void setFoodCategory(String foodCategory) {
		this.foodCategory = foodCategory;
	}
	public List<String> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}
	public String getPrepTime() {
		return prepTime;
	}
	public void setPrepTime(String prepTime) {
		this.prepTime = prepTime;
	}
	public String getCookTime() {
		return cookTime;
	}
	public void setCookTime(String cookTime) {
		this.cookTime = cookTime;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getServings() {
		return servings;
	}
	public void setServings(String servings) {
		this.servings = servings;
	}
	public String getCuisineCategory() {
		return cuisineCategory;
	}
	public void setCuisineCategory(String cuisineCategory) {
		this.cuisineCategory = cuisineCategory;
	}
	public String getRecipeDescription() {
		return recipeDescription;
	}
	public void setRecipeDescription(String recipeDescription) {
		this.recipeDescription = recipeDescription;
	}
	public String getPreparationMethod() {
		return preparationMethod;
	}
	public void setPreparationMethod(String preparationMethod) {
		this.preparationMethod = preparationMethod;
	}
	public String getNutrientValues() {
		return nutrientValues;
	}
	public void setNutrientValues(String nutrientValues) {
		this.nutrientValues = nutrientValues;
	}
	public String getRecipeUrl() {
		return recipeUrl;
	}
	public void setRecipeUrl(String recipeUrl) {
		this.recipeUrl = recipeUrl;
	}
	@Override
	public String toString() {
		return "/nRecipeData [recipeId=" + recipeId + ", recipeName=" + recipeName + ", recipeCategory=" + recipeCategory
				+ ", foodCategory=" + foodCategory + ", ingredients=" + ingredients + ", prepTime=" + prepTime
				+ ", cookTime=" + cookTime + ", tag=" + tag + ", servings=" + servings + ", cuisineCategory="
				+ cuisineCategory + ", recipeDescription=" + recipeDescription + ", preparationMethod="
				+ preparationMethod + ", nutrientValues=" + nutrientValues + ", recipeUrl=" + recipeUrl + "]";
	}
	
	
}
