package ht.bekend.instapro;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import ht.bekend.instapro.Models.post;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("NI7W8aEEScioaKNlnF6Pg9O56zZLt7oob17SkAvU")
                .clientKey("d4KlRNJdBrnq1TWDMIy9UyRyMv2H8Wnt5KKFdK6o")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
