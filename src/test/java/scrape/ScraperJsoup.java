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
			rd.setRecipeDescription(ingredientsDoc.select("p[id=recipe_description]").text());
			
			rd.setIngredients(ings);
			
			List<String> taglist = new ArrayList<>();
			for(Element tags: ingredientsDoc.select("div[id=recipe_tags] a"))
			{
				taglist.add(tags.text());
			}
			System.out.println("Taglist:"+taglist);
			rd.setTag(taglist);
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
			// cuisine category
            else if(listContainsString(taglist,"Rajasthani"))
			{
				rd.setCuisineCategory("Rajasthani");
			}
			else if(listContainsString(taglist,"Punjabi"))
			{
				rd.setCuisineCategory("Punjabi");
			}
			else if(listContainsString(taglist,"Bengali"))
			{
				rd.setCuisineCategory("Bengali");
			}
			else if(listContainsString(taglist,"orissa"))
			{
				rd.setCuisineCategory("orissa");
			}
			else if(listContainsString(taglist,"Gujarati"))
			{
				rd.setCuisineCategory("Gujarati");
			}
			else if(listContainsString(taglist,"Maharashtrian"))
			{
				rd.setCuisineCategory("Maharashtrian");
			}
			else if(listContainsString(taglist,"Andhra"))
			{
				rd.setCuisineCategory("Andhra");
			}
			else if(listContainsString(taglist,"Kerala"))
			{
				rd.setCuisineCategory("Kerala");
			}
			else if(listContainsString(taglist,"Jain"))
			{
				rd.setCuisineCategory("Jain");
			}
			else if(listContainsString(taglist,"Tamilnadu"))
			{
				rd.setCuisineCategory("Tamilnadu");
			}
			else if(listContainsString(taglist,"Karnataka"))
			{
				rd.setCuisineCategory("Karnataka");
			}
			else if(listContainsString(taglist,"Sindhi"))
			{
				rd.setCuisineCategory("Sindhi");
			}
			else if(listContainsString(taglist,"Chhattisgarhi"))
			{
				rd.setCuisineCategory("Chhattisgarhi");
			}
			else if(listContainsString(taglist,"Madhya pradesh"))
			{
				rd.setCuisineCategory("Madhya pradesh");
			}
			else if(listContainsString(taglist,"Assamese"))
			{
				rd.setCuisineCategory("Assamese");
			}
			else if(listContainsString(taglist,"Manipuri"))
			{
				rd.setCuisineCategory("Manipuri");
			}
			else if(listContainsString(taglist,"Tripuri"))
			{
				rd.setCuisineCategory("Tripuri");
			}
			else if(listContainsString(taglist,"Sikkimese"))
			{
				rd.setCuisineCategory("Sikkimese");
			}
			else if(listContainsString(taglist,"Mizo"))
			{
				rd.setCuisineCategory("Mizo");
			}
			else if(listContainsString(taglist,"Arunachali"))
			{
				rd.setCuisineCategory("Arunachali");
			}
			else if(listContainsString(taglist,"uttarakhand"))
			{
				rd.setCuisineCategory("uttarakhand");
			}
			else if(listContainsString(taglist,"Haryanvi"))
			{
				rd.setCuisineCategory("Haryanvi");
			}
			else if(listContainsString(taglist,"Goan"))
			{
				rd.setCuisineCategory("Goan");
			}
			else if(listContainsString(taglist,"Kashmiri"))
			{
				rd.setCuisineCategory("Kashmiri");
			}
			else if(listContainsString(taglist,"Awadhi"))
			{
				rd.setCuisineCategory("Awadhi");
			}
			else if(listContainsString(taglist,"Bihar"))
			{
				rd.setCuisineCategory("Bihar");
			}
			else if(listContainsString(taglist,"Uttar pradesh"))
			{
				rd.setCuisineCategory("Uttar pradesh");
			}
			else if(listContainsString(taglist,"Delhi"))
			{
				rd.setCuisineCategory("Delhi");
			}
			else if(listContainsString(taglist,"South Indian"))
			{
				rd.setCuisineCategory("South Indian");
			}
			else if(listContainsString(taglist,"North Indian"))
			{
				rd.setCuisineCategory("North Indian");
			}
			else if(listContainsString(taglist,"Indian"))
			{
				rd.setCuisineCategory("Indian");
			}	
			//end of selection for cuisine category
		    else
		    {
		    	rd.setCuisineCategory("NA");
				rd.setFoodCategory("NA");
			}
		    	
			
		}
	}
	//method to extract name of the cuisine 
	private static boolean listContainsString (List<String> tagList, String valuetoCheck) {
		boolean valuecontains = false;
		for (String tag : tagList) {
			if (tag.contains(valuetoCheck)) {
				valuecontains = true;
				break;
			}
		}
		return valuecontains;
	}

}


