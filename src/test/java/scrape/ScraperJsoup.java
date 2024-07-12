package scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pojo.RecipeData;

public class ScraperJsoup {
	
	public List<RecipeData> extractRecipeData(String baseUrl) {
	
		List<RecipeData> dataList =  new ArrayList<>();
		//String[] alphabets = {"A","B","C"};
		
		String[] alphabets = {"A"};
		
		for(String a : alphabets) {
		
			String azurl = baseUrl + "?beginswith=" + a;
	
			extractByAZ(azurl,dataList);
	
		}
		return dataList;
	}

	private void extractByAZ(String azurl,List<RecipeData> dataList) {
		
		System.out.println("extractByAZ Connecting: " + azurl);
		
		try {
			Document recipeDoc = Jsoup.connect(azurl).get();
			
			System.out.println("extractByAZ Connected: " + azurl);
			
			//int pageCount =Integer.parseInt(recipeDoc.select("a[class=respglink]").last().text());
			
			int pageCount = 2;
			
			for (int i=1;i<=pageCount;i++) {
				
				String pageurl = azurl + "&pageindex=" + i;
				extractionByPageIndex(dataList, pageurl);
			
			}		
			
		}catch(IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private void extractionByPageIndex(List<RecipeData> dataList, String pageurl) throws IOException {
		
		System.out.println(" extractionByPageIndex Connecting: " + pageurl);
		Document recipeDoc = Jsoup.connect(pageurl).get();
		System.out.println("extractionByPageIndex Connected: " + pageurl);
		List<Element> elements = recipeDoc.select("div[class=rcc_recipecard]");
		
		//System.out.println("Element" + elements);
		
		
		for (Element e : elements) {
			
			RecipeData data = new RecipeData();
			
			data.setRecipeId(e.attribute("id").getValue());
			data.setRecipeUrl(e.select("span[class=rcc_recipename]").select("a[href]").attr("href"));
			data.setRecipeName(e.select("span[class=rcc_recipename]").select("a[href]").text());
			
			dataList.add(data);
		}
		
		for (RecipeData rd : dataList) {
			
			List<String> ings =  new ArrayList<>();
			System.out.println(" recipeUrl Connecting: " + "https://www.tarladalal.com/"+ rd.getRecipeUrl());
			
			Document ingredientsDoc = Jsoup.connect("https://www.tarladalal.com/"+ rd.getRecipeUrl()).get();
			System.out.println(" recipeUrl Connected: ");
			
			for(Element e : ingredientsDoc.select("span[itemprop=recipeIngredient]")) {
				
				ings.add(e.select("a").text());
									
			}
			
			rd.setPrepTime(ingredientsDoc.select("Time[itemprop=prepTime]").text());
			rd.setCookTime(ingredientsDoc.select("Time[itemprop=cookTime]").text());
			
			List<String> taglist = new ArrayList<>();
			for(Element tags: ingredientsDoc.select("div[id=recipe_tags] a"))
			{
				taglist.add(tags.text());
			}
			rd.setTag(taglist);
			
			List<String> methodlist = new ArrayList<>();
			
			for(Element method: ingredientsDoc.select("div[id=recipe_small_steps] li") )
			{
				methodlist.add(method.text());
			}
			rd.setPreparationMethod(methodlist);
			
			rd.setServings(ingredientsDoc.select("#ctl00_cntrightpanel_lblServes").text());
			rd.setIngredients(ings);
			if(taglist.contains("BreakFast"))
			{
				rd.setRecipeCategory("BreakFast");
			}
			else if(taglist.contains("Lunch"))
			{
				rd.setRecipeCategory("Lunch");
			}
			else if(taglist.contains("Dinner"))
			{
				rd.setRecipeCategory("Dinner");
			}
		    else if(taglist.contains("Snack"))
			{
				rd.setRecipeCategory("Snack");
			}
		    else
		    {
				rd.setRecipeCategory("NA");
			}
			if(taglist.contains("Veg"))
			{
				rd.setFoodCategory("Veg");
			}
			else if(taglist.contains("Non Veg"))
			{
				rd.setFoodCategory("Non Veg");
			}
			else if(taglist.contains("Vegan"))
			{
				rd.setFoodCategory("Vegan");
			}
		  
		    else
		    {
				rd.setFoodCategory("NA");
			}
			
			Element rcpnutsDiv = ingredientsDoc.selectFirst("#rcpnuts");
			 List<String> nutrientValues = new ArrayList<>();
            if (rcpnutsDiv != null) {
                
                Elements nutrientTableRows = rcpnutsDiv.select("table tr");
               
                for (Element row : nutrientTableRows) {
                    Elements cells = row.select("td");
                    for (Element cell : cells) {
                        String nutrientValue = cell.text();
                        nutrientValues.add(nutrientValue);
                    }
                }
            
            rd.setNutrientValues(nutrientValues);
            }	
            else 
            	{
            		nutrientValues.add("Not Given");
            		rd.setNutrientValues(nutrientValues);
            	}
			
		
		}
	}
}