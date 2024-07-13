package Scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pojo.RecipeData;

public class Scraper_Eliminate {
	public static void main(String args[]) {
		Document recipeDoc;
		try {
			recipeDoc = Jsoup.connect("https://www.tarladalal.com/RecipeAtoZ.aspx").get();
			//System.out.println("extractiondata: " + recipeDoc);
			List<Element> elements = recipeDoc.select("div[class=rcc_recipecard]");
			//System.out.println("Element" + elements);
			List<RecipeData> dataList =  new ArrayList<>();
			for (Element e : elements) {
				
				RecipeData data = new RecipeData();
				
				
				data.setRecipeId(e.attribute("id").getValue());
				data.setRecipeUrl(e.select("span[class=rcc_recipename]").select("a[href]").attr("href"));
				data.setRecipeName(e.select("span[class=rcc_recipename]").select("a[href]").text());
				
				dataList.add(data);
				System.out.println(data.getRecipeId());
				//System.out.println(data.getIngredients());
								
			}
			
			for (RecipeData rd : dataList) {
				
				List<String> ings =  new ArrayList<>();
				//System.out.println(" recipeUrl Connecting: " + "https://www.tarladalal.com/"+ rd.getRecipeUrl());
				
				Document ingredientsDoc = Jsoup.connect("https://www.tarladalal.com/"+ rd.getRecipeUrl()).get();
				//System.out.println(" recipeUrl Connected: ");
				
				for(Element e : ingredientsDoc.select("span[itemprop=recipeIngredient]")) {
					
					ings.add(e.select("a").text());
										
				}
				rd.setIngredients(ings);
				//System.out.println(rd.getIngredients());
			
			
			
			List<String> taglist = new ArrayList<>();
			for(Element tags: ingredientsDoc.select("div[id=recipe_tags] a"))
			{
				taglist.add(tags.text());
			}
			System.out.println("Taglist:"+taglist);
			rd.setTag(taglist);
			//*********//
			//Recipecategory
			List<CharSequence> R_category ;
			
			
			R_category = taglist.stream().collect(Collectors.toList());
			Pattern p1 = Pattern.compile( 
		             "Snack" , Pattern.CASE_INSENSITIVE); 
			Pattern p2 = Pattern.compile( 
		            "Breakfast", Pattern.CASE_INSENSITIVE);
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
			//Food Category
			
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
                matcher=p8.matcher(e);
				
			    while(matcher.find())
			     {
			    	String S=p8.toString();
			        rd.setFoodCategory(S);
			        }
               matcher=p9.matcher(e);
				
			    while(matcher.find())
			     {
			    	String S=p9.toString();
			        rd.setFoodCategory(S);
			        }
			 
			}
			if(rd.getFoodCategory()==null)
			{
				rd.setFoodCategory("NA");
			}
			  
			
			
			System.out.println("Food Category:"+rd.getFoodCategory());
			System.out.println("Receipe Category:"+rd.getRecipeCategory());
		    	
			
			
			
			
			}
		 
		}
			
			
			
			
			catch (IOException e) {
				
				e.printStackTrace();
			}
		
				
	}
	 /*RecipeData rd=new RecipeData();
	 System.out.println(rd.getRecipeId());
	 System.out.println(rd.getIngredients());*/
	


}
