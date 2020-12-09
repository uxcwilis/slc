package topapp.id.app.smartlivingcost.Rest;

/**
 * Created by toha on 09/04/18.
 * Class penghubung BaseApiService dan RetrofitClient
 */

public class UtilsApi {
    // 192.168.43.71 ini adalah localhost.
    //public static final String BASE_URL_API = "http://www.topapp.id/slc/slc/api/";

    //penggunaan 10.0.2.2 adalah apabila anda akan menjalankanya dalam emulator android di server yang anda gunakan
    public static final String BASE_URL_API = "http://10.0.2.2/slc/slc/api/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
