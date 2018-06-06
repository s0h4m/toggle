package cc.soham.togglesample.network;

import cc.soham.toggle.objects.Config;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * Created by sohammondal on 22/01/16.
 */
public class MyApi {
    private static MyApiService myApiService;

    public static MyApiService getApi() {
        if(myApiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://gist.githubusercontent.com/sohamtriveous/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            myApiService = retrofit.create(MyApiService.class);
        }
        return myApiService;
    }

    public interface MyApiService {
        @GET("938d85011cdd5a914f6e/raw/5ce9fd74c8cc955ba282b968f39eed7bdac85411/sample_toggle_config.json")
        Call<Config> getConfig();
    }
}
