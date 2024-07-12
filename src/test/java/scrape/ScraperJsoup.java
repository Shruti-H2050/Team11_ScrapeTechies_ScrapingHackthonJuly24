package scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
			
			
			rd.setIngredients(ings);
		}
	}
}