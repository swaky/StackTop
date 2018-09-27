package stacktop.swanand.com.stacktop.di;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import stacktop.swanand.com.stacktop.ApiInterface;
import timber.log.Timber;

@Module
public class StackApplicationModule {

    @Provides
    @StackApplicationScope
    public ApiInterface apiInterface(Retrofit retrofit){

        return retrofit.create(ApiInterface.class);
    }

    @Provides
    @StackApplicationScope
    public Gson gson(){
        GsonBuilder gsonBuilder=new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @StackApplicationScope
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson){
    return  new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://api.stackexchange.com/2.2/")
                .build();


    }

    @Provides
    @StackApplicationScope
    public Context context(Application application){
        return application.getApplicationContext();
    }

    @Provides
    @StackApplicationScope
    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });

        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    @Provides
    @StackApplicationScope
    public Cache cache(File cachefile){
        return new Cache(cachefile,10* 1000 * 1000); // 10MB cache
    }

    @Provides
    @StackApplicationScope
    public File cachefile(Context context){
        return new File(context.getCacheDir(),"okhttp_cache");

    }
    @Provides
    @StackApplicationScope
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,Cache cache){

        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @StackApplicationScope
    public Picasso picasso(Context context, OkHttp3Downloader okHttp3Downloader){
        return new Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build();
    }

    @Provides
    @StackApplicationScope
    public OkHttp3Downloader okHttp3Downloader(OkHttpClient okHttpClient){
        return new OkHttp3Downloader(okHttpClient);
    }
}
