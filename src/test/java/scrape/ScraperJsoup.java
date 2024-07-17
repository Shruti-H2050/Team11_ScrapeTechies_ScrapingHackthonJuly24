package scrape;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
	private Map<String, List<String>> cuisineCategoryMap = new HashMap<>();

    public ScraperJsoup() {
        loadCuisineCategories();
    }

	public List<RecipeData> extractRecipeData(String baseUrl) {

		List<RecipeData> dataList =  new ArrayList<>();
		//String[] alphabets = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

		String[] alphabets = {"P"};

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
				extractionByPageIndexTarlaDalal(dataList, pageurl);
				//extractionByPageIndexMemberOnly(dataList,pageurl);

			}		

		}catch(IOException e) {
			try {
				System.err.println("Exception retrying.." + pageurl);
				extractionByPageIndexTarlaDalal(dataList, pageurl);
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

	private void extractionByPageIndexTarlaDalal(List<RecipeData> dataList, String pageurl) throws IOException {
		
		System.out.println(" extractionByPageIndexTarlaDalal Connecting: " + pageurl);

		Document recipeDoc = Jsoup.connect(pageurl).timeout(20000).get();
		
		System.out.println("extractionByPageIndexTarlaDalal Connected: " + pageurl);
		
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
	private void loadCuisineCategories() {
	    Properties properties = new Properties();
	    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("cuisine_categories.properties")) {
	        if (inputStream == null) {
	            System.err.println("Sorry, unable to find cuisine_categories.properties");
	            return;
	        }
	        properties.load(inputStream);
	        for (String cuisine : properties.stringPropertyNames()) {
	            String[] keywords = properties.getProperty(cuisine).split(",");
	            cuisineCategoryMap.put(cuisine.toLowerCase(), Arrays.asList(keywords));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


    private String extractCuisineCategory(List<String> taglist) {
        for (String tag : taglist) {
            for (Map.Entry<String, List<String>> entry : cuisineCategoryMap.entrySet()) {
                String cuisineCategory = entry.getKey();
                List<String> keywords = entry.getValue();
                for (String keyword : keywords) {
                    if (tag.toLowerCase().contains(keyword)) {
                        return cuisineCategory;
                    }
                }
            }
        }
        return "NA"; 
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
		//Extracting Cuisine Category
		String cuisineCategory = extractCuisineCategory(taglist);
		rd.setCuisineCategory(cuisineCategory);


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


}