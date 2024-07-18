package pojo;

import java.util.ArrayList;
import java.util.List;
//import lombok.Data;

//@Data
public class FilterCriteria {

	private List<String> lfvEliminate = new ArrayList<>();
	private List<String> lfvAdd = new ArrayList<>();
	private List<String> lfvAddNotVegan = new ArrayList<>();
	private List<String> lfvReceipeToAvoid = new ArrayList<>();
	private List<String> lfvOptionalRecipe = new ArrayList<>();
	
	private List<String> lchfEliminate = new ArrayList<>();
	private List<String> lchfAdd = new ArrayList<>();
	private List<String> lchfReceipeToAvoid = new ArrayList<>();
	private List<String> lchfFoodProcessing = new ArrayList<>();
	
	private List<String> AllergyList;
	
	
	public List<String> getLfvEliminate() {
		return lfvEliminate;
	}

	public void setLfvEliminate(List<String> lfvEliminate) {
		this.lfvEliminate = lfvEliminate;
	}

	public List<String> getLfvAdd() {
		return lfvAdd;
	}

	public void setLfvAdd(List<String> lfvAdd) {
		this.lfvAdd = lfvAdd;
	}


	public List<String> getLfvAddNotVegan() {
		return lfvAddNotVegan;
	}


	public void setLfvAddNotVegan(List<String> lfvAddNotVegan) {
		this.lfvAddNotVegan = lfvAddNotVegan;
	}


	public List<String> getLfvReceipeToAvoid() {
		return lfvReceipeToAvoid;
	}


	public void setLfvReceipeToAvoid(List<String> lfvReceipeToAvoid) {
		this.lfvReceipeToAvoid = lfvReceipeToAvoid;
	}


	public List<String> getLfvOptionalRecipe() {
		return lfvOptionalRecipe;
	}


	public void setLfvOptionalRecipe(List<String> lfvOptionalRecipe) {
		this.lfvOptionalRecipe = lfvOptionalRecipe;
	}


	public List<String> getLchfEliminate() {
		return lchfEliminate;
	}


	public void setLchfEliminate(List<String> lchfEliminate) {
		this.lchfEliminate = lchfEliminate;
	}


	public List<String> getLchfAdd() {
		return lchfAdd;
	}


	public void setLchfAdd(List<String> lchfAdd) {
		this.lchfAdd = lchfAdd;
	}


	public List<String> getLchfReceipeToAvoid() {
		return lchfReceipeToAvoid;
	}


	public void setLchfReceipeToAvoid(List<String> lchfReceipeToAvoid) {
		this.lchfReceipeToAvoid = lchfReceipeToAvoid;
	}


	public List<String> getLchfFoodProcessing() {
		return lchfFoodProcessing;
	}


	public void setLchfFoodProcessing(List<String> lchfFoodProcessing) {
		this.lchfFoodProcessing = lchfFoodProcessing;
	}

	public List<String> getAllergyList() {
		return AllergyList;
	}

	public void setAllergyList(List<String> allergyList) {
		AllergyList = allergyList;
	}

	@Override
	public String toString() {
		return "FilterCriteria [lfvEliminate=" + lfvEliminate + ", lfvAdd=" + lfvAdd + ", lfvAddNotVegan="
				+ lfvAddNotVegan + ", lfvReceipeToAvoid=" + lfvReceipeToAvoid + ", lfvOptionalRecipe="
				+ lfvOptionalRecipe + ", lchfEliminate=" + lchfEliminate + ", lchfAdd=" + lchfAdd
				+ ", lchfReceipeToAvoid=" + lchfReceipeToAvoid + ", lchfFoodProcessing=" + lchfFoodProcessing
				+ ", AllergyList=" + AllergyList + "]";
	}

}
