package pojo;

import java.util.ArrayList;
import java.util.List;

public class FilterCriteria {

	private List<String> lfvEliminate = new ArrayList<>();
	private List<String> lfvAdd = new ArrayList<>();
	private List<String> lfvAddNotVegan = new ArrayList<>();
	private List<String> receipeAvoid = new ArrayList<>();
	private List<String> optionalRecipe = new ArrayList<>();
	
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
	public List<String> getReceipeAvoid() {
		return receipeAvoid;
	}
	public void setReceipeAvoid(List<String> receipeAvoid) {
		this.receipeAvoid = receipeAvoid;
	}
	public List<String> getOptionalRecipe() {
		return optionalRecipe;
	}
	public void setOptionalRecipe(List<String> optionalRecipe) {
		this.optionalRecipe = optionalRecipe;
	}
	@Override
	public String toString() {
		return "FilterCriteria [lfvEliminate=" + lfvEliminate + ", \nlfvAdd=" + lfvAdd + ", \nlfvAddNotVegan="
				+ lfvAddNotVegan + ", \nreceipeAvoid=" + receipeAvoid + ", \noptionalRecipe=" + optionalRecipe + "]";
	}
	
	
	
	
}
