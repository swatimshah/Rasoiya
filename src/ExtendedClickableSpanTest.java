import com.app.mymeal.MainActivity;
import com.app.mymeal.views.ExtendedClickableSpan;

import android.graphics.Typeface;
import android.test.ActivityInstrumentationTestCase2;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

public class ExtendedClickableSpanTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity mMainActivity;

	public ExtendedClickableSpanTest() {
	    super(MainActivity.class);
	}	
 	
	public ExtendedClickableSpanTest(Class<MainActivity> mainActivity) {
		super(mainActivity);
	}

	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		mMainActivity = getActivity();
	};

	public void testCase1() throws Exception {

		SpannableString spannableString = new SpannableString(
				"Dum Aloo, Paratha");

		ExtendedClickableSpan extendedClickableSpan = new ExtendedClickableSpan(
				getActivity());

		spannableString.setSpan(extendedClickableSpan, 0, 8,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		EditText editText = new EditText(getActivity());
		editText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));

		editText.setText(spannableString);

		extendedClickableSpan.onClick(editText);
	}

	// public void testCase2() throws Exception {
	//
	// ExtendedClickableSpan extendedClickableSpan = ExtendedClickableSpan
	// .getComponent(getContext(), new SpannableString("Dum Aloo"));
	// }

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

}
