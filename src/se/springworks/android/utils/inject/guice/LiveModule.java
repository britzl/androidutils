package se.springworks.android.utils.inject.guice;

import se.springworks.android.utils.auth.GoogleAuthentication;
import se.springworks.android.utils.auth.IAuthentication;
import se.springworks.android.utils.eventbus.IEventBus;
import se.springworks.android.utils.eventbus.OttoBus;
import se.springworks.android.utils.file.AssetFileHandler;
import se.springworks.android.utils.file.FileDownloader;
import se.springworks.android.utils.file.IAssetFileHandler;
import se.springworks.android.utils.file.IFileDownloader;
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
import se.springworks.android.utils.system.ISystemSettings;
import se.springworks.android.utils.system.SystemSettings;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;

public class LiveModule extends AbstractModule  {

	private static final Logger logger = LoggerFactory.getLogger(LiveModule.class);

	private Context context;
	
	public LiveModule(Context context) {
        super();
        this.context = context;
    }

	@Override
	public void configure() {
		logger.debug("configure()");

		bindListener(Matchers.any(), new InjectLoggerListener());
		bindListener(Matchers.any(), new InjectExtraListener());
		bindListener(Matchers.any(), new InjectResourceListener(context.getResources()));
		

		bind(LayoutInflater.class).toInstance((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		
		bind(NotificationManager.class).toInstance((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE));
		
		bind(DownloadManager.class).toInstance((DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE));
		
		bind(INotificationManager.class).to(AndroidNotificationManager.class);
		
		bind(Context.class).toInstance(context.getApplicationContext());
		
		bind(ParameterLoader.class).toInstance(new ParameterLoader(context.getApplicationContext()));

		bind(Resources.class).toInstance(context.getResources());
		
		bind(AssetManager.class).toInstance(context.getAssets());
		
		bind(IJsonParser.class).to(JacksonParser.class);
		
		bind(IRestClient.class).to(RestClient.class).in(Singleton.class);

		bind(IFileHandler.class).to(StorageFileHandler.class);
		
		bind(IFileDownloader.class).to(FileDownloader.class).in(Singleton.class);
		
		bind(IAssetFileHandler.class).to(AssetFileHandler.class).in(Singleton.class);
		
		bind(ISimpleHttpClient.class).to(SimpleHttpClient.class);
		
		bind(IImageLoader.class).to(ImageLoader.class);
		
		bind(ISystemSettings.class).to(SystemSettings.class);
		
		bind(AsyncImageLoader.class);
		
		bind(IEventBus.class).to(OttoBus.class).in(Singleton.class);
		
		bind(IAuthentication.class).toInstance(new GoogleAuthentication(context.getApplicationContext()));
		
		bind(IKeyValueStorage.class).to(SharedPreferencesStorage.class).in(Singleton.class);
		
		bind(IIabHelper.class).to(IabHelper.class).in(Singleton.class);
		
		bind(GoogleCloudMessaging.class).toInstance(GoogleCloudMessaging.getInstance(context));
		
//		bind(IAnalyticsTracker.class).to(GoogleTracker.class).in(Singleton.class);
		
		install(new FactoryModuleBuilder().implement(ISoundPlayer.class, SoundPlayer.class).build(SoundPlayerFactory.class));

		install(new FactoryModuleBuilder().implement(IIabHelper.class, IabHelper.class).build(IabHelperFactory.class));
	}
}
