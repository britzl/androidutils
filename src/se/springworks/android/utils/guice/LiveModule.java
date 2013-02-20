package se.springworks.android.utils.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.springworks.android.utils.file.AssetFileHandler;
import se.springworks.android.utils.file.IAssetFileHandler;
import se.springworks.android.utils.file.IFileHandler;
import se.springworks.android.utils.file.StorageFileHandler;
import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.http.SimpleHttpClient;
import se.springworks.android.utils.iab.IIabHelper;
import se.springworks.android.utils.iab.IabHelper;
import se.springworks.android.utils.iab.IabHelperFactory;
import se.springworks.android.utils.image.AsyncImageLoader;
import se.springworks.android.utils.image.IImageLoader;
import se.springworks.android.utils.image.ImageLoader;
import se.springworks.android.utils.json.IJsonParser;
import se.springworks.android.utils.json.JacksonParser;
import se.springworks.android.utils.rest.IRestClient;
import se.springworks.android.utils.rest.RestClient;
import se.springworks.android.utils.sound.ISoundPlayer;
import se.springworks.android.utils.sound.SoundPlayer;
import se.springworks.android.utils.sound.SoundPlayerFactory;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.squareup.otto.Bus;

public class LiveModule extends AbstractModule  {

	private static final Logger logger = LoggerFactory.getLogger(LiveModule.class);

	private Application app;
	
	public LiveModule(Application app) {
        super();
        this.app = app;
    }

	@Override
	public void configure() {
		logger.debug("configure()");

		bind(Context.class).toInstance(app.getApplicationContext());

		bind(Resources.class).toInstance(app.getResources());
		
		bind(AssetManager.class).toInstance(app.getAssets());
		
		bind(IJsonParser.class).to(JacksonParser.class);
		
		bindListener(Matchers.any(), new Slf4jTypeListener());
		
		bind(IRestClient.class).to(RestClient.class).in(Singleton.class);

		bind(AssetFileHandler.class).in(Singleton.class);
		
		bind(IFileHandler.class).to(StorageFileHandler.class);
		
		bind(IAssetFileHandler.class).to(AssetFileHandler.class);
		
		bind(ISimpleHttpClient.class).to(SimpleHttpClient.class);
		
		bind(IImageLoader.class).to(ImageLoader.class);
		
		bind(AsyncImageLoader.class);
		
		bind(Bus.class).in(Singleton.class);
		
		install(new FactoryModuleBuilder().implement(ISoundPlayer.class, SoundPlayer.class).build(SoundPlayerFactory.class));

		install(new FactoryModuleBuilder().implement(IIabHelper.class, IabHelper.class).build(IabHelperFactory.class));
	}
}
