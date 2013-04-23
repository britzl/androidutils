package se.springworks.android.utils.inject.guice;

import se.springworks.android.utils.auth.GoogleAuthentication;
import se.springworks.android.utils.auth.IAuthentication;
import se.springworks.android.utils.eventbus.IEventBus;
import se.springworks.android.utils.eventbus.OttoBus;
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
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;
import se.springworks.android.utils.notification.AndroidNotificationManager;
import se.springworks.android.utils.notification.INotificationManager;
import se.springworks.android.utils.persistence.IKeyValueStorage;
import se.springworks.android.utils.persistence.SharedPreferencesStorage;
import se.springworks.android.utils.resource.ParameterLoader;
import se.springworks.android.utils.rest.IRestClient;
import se.springworks.android.utils.rest.RestClient;
import se.springworks.android.utils.sound.ISoundPlayer;
import se.springworks.android.utils.sound.SoundPlayer;
import se.springworks.android.utils.sound.SoundPlayerFactory;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;

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

		bindListener(Matchers.any(), new InjectLoggerListener());
		bindListener(Matchers.any(), new InjectExtraListener());
		bindListener(Matchers.any(), new InjectResourceListener(app.getResources()));
		

		bind(LayoutInflater.class).toInstance((LayoutInflater)app.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		
		bind(NotificationManager.class).toInstance((NotificationManager)app.getSystemService(Context.NOTIFICATION_SERVICE));
		
		bind(INotificationManager.class).to(AndroidNotificationManager.class);
		
		bind(Context.class).toInstance(app.getApplicationContext());
		
		bind(ParameterLoader.class).toInstance(new ParameterLoader(app.getApplicationContext()));

		bind(Resources.class).toInstance(app.getResources());
		
		bind(AssetManager.class).toInstance(app.getAssets());
		
		bind(IJsonParser.class).to(JacksonParser.class);
		
		bind(IRestClient.class).to(RestClient.class).in(Singleton.class);

		bind(IFileHandler.class).to(StorageFileHandler.class);
		
		bind(IAssetFileHandler.class).to(AssetFileHandler.class).in(Singleton.class);
		
		bind(ISimpleHttpClient.class).to(SimpleHttpClient.class);
		
		bind(IImageLoader.class).to(ImageLoader.class);
		
		bind(AsyncImageLoader.class);
		
		bind(IEventBus.class).to(OttoBus.class).in(Singleton.class);
		
		bind(IAuthentication.class).toInstance(new GoogleAuthentication(app.getApplicationContext()));
		
		bind(IKeyValueStorage.class).to(SharedPreferencesStorage.class).in(Singleton.class);
		
//		bind(IAnalyticsTracker.class).to(GoogleTracker.class).in(Singleton.class);
		
		install(new FactoryModuleBuilder().implement(ISoundPlayer.class, SoundPlayer.class).build(SoundPlayerFactory.class));

		install(new FactoryModuleBuilder().implement(IIabHelper.class, IabHelper.class).build(IabHelperFactory.class));
	}
}
