package scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pojo.RecipeData;

public class ScraperJsoup {
	
	public List<RecipeData> extractRecipeData() {
	
	try {
		Document recipeDoc = Jsoup.connect("https://www.tarladalal.com/RecipeAtoZ.aspx").get();
		
		List<Element> elements = recipeDoc.select("div[class=rcc_recipecard]");
		
		//System.out.println("Element" + elements);
		List<RecipeData> dataList =  new ArrayList<>();
		
		for (Element e : elements) {
			
			RecipeData data = new RecipeData();
			
			data.setRecipeId(e.attribute("id").getValue());
			data.setRecipeUrl(e.select("span[class=rcc_recipename]").select("a[href]").attr("href"));
			data.setRecipeName(e.select("span[class=rcc_recipename]").select("a[href]").text());
			
			dataList.add(data);
		}
		
		for (RecipeData rd : dataList) {
			
			List<String> ings =  new ArrayList<>();
			
			Document ingredientsDoc = Jsoup.connect("https://www.tarladalal.com/"+ rd.getRecipeUrl()).get();
			
			for(Element e : ingredientsDoc.select("span[itemprop=recipeIngredient]")) {
				
				ings.add(e.select("a").text());
									
			}
			
			rd.setPrepTime(ingredientsDoc.select("Time[itemprop=prepTime]").text());
			rd.setCookTime(ingredientsDoc.select("Time[itemprop=cookTime]").text());
			
			
			rd.setIngredients(ings);
		}
		
		
		return dataList;
		
	}catch(IOException e) {
		
		e.printStackTrace();
	}
	return null;
	}
	
	
}
