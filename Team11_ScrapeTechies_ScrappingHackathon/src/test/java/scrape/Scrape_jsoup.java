package Scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pojo.RecipeData;

public class Scrape_jsoup {
	

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
			
			List<String> taglist = new ArrayList<>();
			for(Element tags: ingredientsDoc.select("div[id=recipe_tags] a"))
			{
				taglist.add(tags.text());
			}
			System.out.println("Taglist:"+taglist);
			rd.setTag(taglist);
			//Recipecategory
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
			//Food Category
			
            List<CharSequence> F_category ;
			
			
			F_category = taglist.stream().collect(Collectors.toList());
			Pattern p5 = Pattern.compile( 
		             "Veg" , Pattern.CASE_INSENSITIVE); 
			Pattern p6 = Pattern.compile( 
		            "Non Veg", Pattern.CASE_INSENSITIVE);
			Pattern p7 = Pattern.compile( 
		             "Vegan" , Pattern.CASE_INSENSITIVE); 
	
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
			     
			  
			
			
			System.out.println("Food Category:"+rd.getFoodCategory());
			System.out.println("Receipe Category:"+rd.getRecipeCategory());
		    	
			
			
			
			
			}
		 
		}
			
			
			
			
			catch (IOException e) {
				
				e.printStackTrace();
			}
		
				
	}
	 
	


}
