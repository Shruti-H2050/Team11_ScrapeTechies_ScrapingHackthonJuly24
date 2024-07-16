package scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static scrape.ScraperMain.*;

import pojo.RecipeData;

public class ScraperJsoup{
	
	//ExecutorService exec = Executors.newFixedThreadPool(20);
	
	public List<RecipeData> extractRecipeData(String baseUrl) {
	
		List<RecipeData> dataList =  new ArrayList<>();
		//String[] alphabets = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
		String[] alphabets = {"B"};
		
		for(String a : alphabets) {
		
			String azurl = baseUrl + "?beginswith=" + a;
	
			extractByAZ(azurl,dataList);
	
		}
		return dataList;
	}
	
	private void extractionByPageIndexMemberOnly(List<RecipeData> dataList,String pageurl) {
		try {
			Map<String,String> dataMap =  new HashMap<String,String>();
			dataMap.put("ctl00$cntleftpanel$rbltdmem", "member");
			dataMap.put("__EVENTTARGET", "ctl00$cntleftpanel$rbltdmem$1");
			dataMap.put("__EVENTARGUMENT", "");
			System.out.println("connecting.. " + pageurl);
			List<Element> elements  = Jsoup.connect(pageurl).data(dataMap).timeout(20000).post().select("div[class=rcc_recipecard]");
			System.out.println("connected..");
			extractRecipe(dataList, elements);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void extractByAZ(String azurl,List<RecipeData> dataList) {
		
		System.out.println("extractByAZ Connecting: " + azurl);
		
		Runnable task = () -> {
			String pageurl=azurl;
		try {
			Document recipeDoc = Jsoup.connect(azurl).timeout(20000).get();
			
						
			System.out.println("extractByAZ Connected: " + azurl);
			
			int pageCount =Integer.parseInt(recipeDoc.select("a[class=respglink]").last().text());
			
			//int pageCount = 2;
			
			for (int i=1;i<=pageCount;i++) {
				
				pageurl = azurl + "&pageindex=" + i;
				//extractionByPageIndex(dataList, pageurl);
				extractionByPageIndexMemberOnly(dataList,pageurl);
			
			}		
			
		}catch(IOException e) {
			try {
				System.err.println("Exception retrying.." + pageurl);
				extractionByPageIndex(dataList, pageurl);
			} catch (IOException e1) {
				ERROR_MAP.put(pageurl,null);
				e1.printStackTrace();
			}
			
		}
		
		};
		Thread t = new Thread(task);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private void extractionByPageIndex(List<RecipeData> dataList, String pageurl) throws IOException {
		
		System.out.println(" extractionByPageIndex Connecting: " + pageurl);
		Document recipeDoc = Jsoup.connect(pageurl).timeout(20000).get();
		System.out.println("extractionByPageIndex Connected: " + pageurl);
		List<Element> elements = recipeDoc.select("div[class=rcc_recipecard]");
		
		//System.out.println("Element" + elements);
		
		extractRecipe(dataList, elements);
	}

	private void extractRecipe(List<RecipeData> dataList, List<Element> elements) {
		for (Element e : elements) {
			
			RecipeData data = new RecipeData();
			
			data.setRecipeId(e.attribute("id").getValue()); //Extracting RecipeId
			data.setRecipeUrl(e.select("span[class=rcc_recipename]").select("a[href]").attr("href")); //Extracting RecipeUrl
			data.setRecipeName(e.select("span[class=rcc_recipename]").select("a[href]").text()); //Extracting RecipeName
			
			dataList.add(data);
			
			Runnable r = () -> {
				try {
					extractFields(data);
				} catch (IOException e1) {
					try {
						System.err.println("Exception retrying.." + data);
						extractFields(data);
					} catch (IOException e2) {
						ERROR_MAP.put(data.getRecipeUrl(), data);
						e2.printStackTrace();
					}
				}
			};
			Thread t = new Thread(r);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		    	
		}
	}

	private void extractFields(RecipeData rd) throws IOException {
		System.out.println(" recipeUrl Connecting: " + "https://www.tarladalal.com/"+ rd.getRecipeUrl());
		
		Document ingredientsDoc = Jsoup.connect("https://www.tarladalal.com/"+ rd.getRecipeUrl()).timeout(20000).get();
		System.out.println(" recipeUrl Connected: ");
		
		//Extracting Ingredients
		
		List<String> ings =  new ArrayList<>();
		for(Element e : ingredientsDoc.select("span[itemprop=recipeIngredient]")) {
			
			ings.add(e.select("a").text());
								
		}
		rd.setIngredients(ings);
		
		//Extracting Preparation Time
		
		rd.setPrepTime(ingredientsDoc.select("Time[itemprop=prepTime]").text());
		
		//Extracting Cook Time
		
		rd.setCookTime(ingredientsDoc.select("Time[itemprop=cookTime]").text());
		
		//Extracting Recipe Description
		
		rd.setRecipeDescription(ingredientsDoc.select("p[id=recipe_description]").text());
		
		//Extracting Tags
		
		List<String> taglist = new ArrayList<>();
		
		for(Element tags: ingredientsDoc.select("div[id=recipe_tags] a"))
		{
			taglist.add(tags.text());
		}
					
		rd.setTag(taglist);
		
		//Extracting preparation method
		
		List<String> methodlist = new ArrayList<>();
			
		for(Element method: ingredientsDoc.select("div[id=recipe_small_steps] li") )
		{
			methodlist.add(method.text());
		}
		rd.setPreparationMethod(methodlist);
		
		//Extracting Servings
		
		rd.setServings(ingredientsDoc.select("#ctl00_cntrightpanel_lblServes").text());
		
		//Extracting nutrientValues
		
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
		
		//Extracting Cuisine Category
		
		 if(listContainsString(taglist,"Rajasthani"))
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
			else if(listContainsString(taglist,"Chinese"))
			{
				rd.setCuisineCategory("Chinese");
			}	
			else if(listContainsString(taglist,"American"))
			{
				rd.setCuisineCategory("American");
			}	
			else if(listContainsString(taglist,"French"))
			{
				rd.setCuisineCategory("French");
			}	
			else if(listContainsString(taglist,"Italian"))
			{
				rd.setCuisineCategory("Italian");
			}
			else if(listContainsString(taglist,"Lebanese"))
			{
				rd.setCuisineCategory("Lebanese");
			}
			else if(listContainsString(taglist,"Mexican"))
			{
				rd.setCuisineCategory("Mexican");
			}
			else if(listContainsString(taglist,"Thai"))
			{
				rd.setCuisineCategory("Thai");
			}
		 	
			//end of selection for cuisine category
		    else
		    {
		    	rd.setCuisineCategory("NA");
				
			}
		    	
		 //Extracting Recipe Category
		
		List<CharSequence> R_category ;
		
		
		R_category = taglist.stream().collect(Collectors.toList());
		Pattern p1 = Pattern.compile( 
	             "Snacks" , Pattern.CASE_INSENSITIVE); 
		Pattern p2 = Pattern.compile( 
	            "BreakFast", Pattern.CASE_INSENSITIVE);
		Pattern p3 = Pattern.compile( 
	             "Lunch" , Pattern.CASE_INSENSITIVE); 
		Pattern p4 = Pattern.compile( 
	            "Dinner", Pattern.CASE_INSENSITIVE);
		for(CharSequence e:R_category){
		
			Matcher matcher;
			matcher=p1.matcher(e);
		
		    while(matcher.find())
		     {
		    	String S=p1.toString();
		        rd.setRecipeCategory(S);
		        }
		    
			matcher=p2.matcher(e);
		
		    while(matcher.find())
		     {
		    	String S=p2.toString();
		        rd.setRecipeCategory(S);
		        }
		    
			matcher=p3.matcher(e);
		
		    while(matcher.find())
		     {
		    	String S=p3.toString();
		        rd.setRecipeCategory(S);
		        }
		    
			matcher=p4.matcher(e);
		
		    while(matcher.find())
		     {
		    	String S=p4.toString();
		        rd.setRecipeCategory(S);
		        }
		 
		}
		if(rd.getRecipeCategory()==null)
		{
			rd.setRecipeCategory("NA");
		}
		
	
		//Extracting Food Category
		
        List<CharSequence> F_category ;
		
		
        F_category = taglist.stream().collect(Collectors.toList());
		Pattern p5 = Pattern.compile( 
	             "Veg" , Pattern.CASE_INSENSITIVE); 
		Pattern p6 = Pattern.compile( 
	            "Non Veg", Pattern.CASE_INSENSITIVE);
		Pattern p7 = Pattern.compile( 
	             "Vegan" , Pattern.CASE_INSENSITIVE); 
		Pattern p8 = Pattern.compile( 
	            "Eggitarian", Pattern.CASE_INSENSITIVE);
		Pattern p9 = Pattern.compile( 
	             "Jain" , Pattern.CASE_INSENSITIVE);
		for(CharSequence e:F_category){
		
			Matcher matcher;
			matcher=p5.matcher(e);
		
		    while(matcher.find())
		     {
		    	String S=p5.toString();
		        rd.setFoodCategory(S);
		        }
		    
			matcher=p6.matcher(e);
		
		    while(matcher.find())
		     {
		    	String S=p6.toString();
		        rd.setFoodCategory(S);
		        }
		    matcher=p7.matcher(e);
			
		    while(matcher.find())
		     {
		    	String S=p7.toString();
		        rd.setFoodCategory(S);
		        }
			}
		
		if(rd.getFoodCategory()==null)
		{
			rd.setFoodCategory("NA");
		}
	}
	
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