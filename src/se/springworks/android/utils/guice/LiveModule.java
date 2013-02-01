package se.springworks.android.utils.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.springworks.android.utils.file.ExternalFileHandler;
import se.springworks.android.utils.file.IFileHandler;
import se.springworks.android.utils.file.InternalFileHandler;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class LiveModule extends AbstractModule  {

	private static final Logger logger = LoggerFactory.getLogger(LiveModule.class);

	public LiveModule() {
        super();
    }

	@Override
	public void configure() {
		logger.debug("configure()");

		bindListener(Matchers.any(), new Slf4jTypeListener());
		
		if (ExternalFileHandler.isAvailable()) {
			bind(IFileHandler.class).to(ExternalFileHandler.class);
		} else {
			bind(IFileHandler.class).to(InternalFileHandler.class);
		}
	}
}
