package app.appified.Database;

import com.activeandroid.Configuration;
import com.activeandroid.content.ContentProvider;

public class DatabaseContentProvider extends ContentProvider {


    @Override
    protected Configuration getConfiguration() {
        Configuration.Builder builder = new Configuration.Builder(getContext());
        builder.addModelClass(AppModel.class);
        return builder.create();
    }
}
