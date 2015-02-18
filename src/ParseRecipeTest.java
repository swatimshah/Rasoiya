import com.app.mymeal.data.Recipe;

import android.test.AndroidTestCase;


public class ParseRecipeTest extends AndroidTestCase {

	public void testPopulateRecipe() throws Exception {
		int resource = getContext().getResources().getIdentifier("jeerarice_import", 
				"xml", getContext().getPackageName());
		
		Recipe.populateDb(getContext(), resource);
	}
	
	
	protected void setUp() throws Exception {
		
	};
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
}


