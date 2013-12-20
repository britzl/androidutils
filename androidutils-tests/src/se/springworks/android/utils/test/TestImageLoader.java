package se.springworks.android.utils.test;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.image.BitmapUtils;
import se.springworks.android.utils.image.IImageLoader;
import se.springworks.android.utils.image.ImageLoader;
import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.inject.guice.InjectLoggerListener;
import se.springworks.android.utils.mock.MockSimpleHttpClient;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.matcher.Matchers;

public class TestImageLoader extends AndroidTestCase {
	
	public class TestImageLoaderModule extends AbstractModule {
		private Context context;
		private MockSimpleHttpClient mockHttp;

		public TestImageLoaderModule(Context context, MockSimpleHttpClient mockHttp) {
			this.context = context;
			this.mockHttp = mockHttp;
		}
		
		@Override
		protected void configure() {
            bindListener(Matchers.any(), new InjectLoggerListener());
			bind(Context.class).toInstance(context);

			bind(ISimpleHttpClient.class).toInstance(mockHttp);
			bind(AssetManager.class).toInstance(context.getAssets());
			bind(IImageLoader.class).to(ImageLoader.class);
		}
	}

	@Inject
	private IImageLoader imageLoader;
	
	private MockSimpleHttpClient mockHttp;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		mockHttp = new MockSimpleHttpClient();
		GrapeGuice.getInjector(this).rebind(new TestImageLoaderModule(getContext(), mockHttp)).injectMembers(this);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAsBitmapFromUrl() throws IOException {		
		final String url = "http://www.someurl.com/test64x64.png";
		mockHttp.setResponse(url, getContext().getAssets().open("images/test64x64.png"));
		
		Bitmap bitmap = imageLoader.getAsBitmap(url);
		assertNotNull(bitmap);
		assertNull(imageLoader.getAsBitmap("http://www.someurl.com/doesnotexist"));	
		assertTrue(BitmapUtils.isSame(bitmap, imageLoader.getFromAssets("images/test64x64.png")));
		assertFalse(BitmapUtils.isSame(bitmap, imageLoader.getFromAssets("images/test128x128.png")));
	}

	@Test
	public void testGetAsBitmapFromAssets() throws IOException {		
		final String fileName = "images/test64x64.png";
		Bitmap bitmap = imageLoader.getFromAssets(fileName);
		assertNotNull(bitmap);
		assertNull(imageLoader.getFromAssets("images/doesnotexist"));	
		assertTrue(BitmapUtils.isSame(bitmap, imageLoader.getFromAssets("images/test64x64.png")));
		assertFalse(BitmapUtils.isSame(bitmap, imageLoader.getFromAssets("images/test128x128.png")));
	}

}
