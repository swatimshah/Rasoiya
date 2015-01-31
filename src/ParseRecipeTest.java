import com.app.mymeal.R;
import com.app.mymeal.Recipe;

import android.test.AndroidTestCase;


public class ParseRecipeTest extends AndroidTestCase {

//	public ParseRecipeTest(Class<Application> applicationClass) {
//		super(applicationClass);
//		// TODO Auto-generated constructor stub
//	}

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


