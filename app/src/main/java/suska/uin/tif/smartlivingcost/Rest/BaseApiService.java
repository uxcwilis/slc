package suska.uin.tif.smartlivingcost.Rest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import suska.uin.tif.smartlivingcost.Data.ResponseLogin;
import suska.uin.tif.smartlivingcost.Data.ResponseMyMenu;
import suska.uin.tif.smartlivingcost.Data.ResponseMyPromo;
import suska.uin.tif.smartlivingcost.Data.ResponseMyResto;
import suska.uin.tif.smartlivingcost.Data.ResponseMyReview;
import suska.uin.tif.smartlivingcost.Data.ResponsePromo;
import suska.uin.tif.smartlivingcost.Data.ResponseRecom;
import suska.uin.tif.smartlivingcost.Data.ResponseReportedComment;
import suska.uin.tif.smartlivingcost.Data.ResponseSuspendMenu;
import suska.uin.tif.smartlivingcost.Data.ResponseUserReview;

public interface BaseApiService {


    @FormUrlEncoded
    @POST("register")
    @Headers({"Content-Type: application/x-www-form-urlencoded",
            "X-Requested-With: XMLHttpRequest"})
    Call<ResponseBody> register(@Field("name") String name,
                                @Field("email") String email,
                                @Field("password") String password,
                                @Field("c_password") String cpassword);

    @FormUrlEncoded
    @POST("login")
    @Headers({"Content-Type: application/x-www-form-urlencoded",
            "X-Requested-With: XMLHttpRequest"})
    Call<ResponseLogin> login(@Field("email") String email,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST("update")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> updateProfil(@Header("Authorization") String token,
                                    @Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("password/create")
    @Headers({"Content-Type: application/x-www-form-urlencoded",
            "X-Requested-With: XMLHttpRequest"})
    Call<ResponseBody> reqResetPass(@Field("email") String email);

    @FormUrlEncoded
    @POST("password/reset")
    @Headers({"Content-Type: application/x-www-form-urlencoded",
            "X-Requested-With: XMLHttpRequest"})
    Call<ResponseBody> ResetPass(@Field("email") String email,
                                 @Field("password") String password,
                                 @Field("token") String token,
                                 @Field("password_confirmation") String password_confirmation);

    @GET("logout")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @Multipart
    @POST("backup")
    Call<ResponseBody> backup(@Header("Authorization") String token,
                              @Part MultipartBody.Part backup_db);

    @GET("resto")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseMyResto> myResto(@Header("Authorization") String token);

    @Multipart
    @POST("resto/create")
    Call<ResponseBody> addMyResto(@Header("Authorization") String token,
                                  @Part MultipartBody.Part gambar_resto,
                                  @Part("id_inputer") int id_inputer,
                                  @Part("nama") RequestBody nama,

                                  @Part("lat") RequestBody lat,
                                  @Part("lng") RequestBody lng,
                                  @Part("alamat") RequestBody alamat,
                                  @Part("status") int status);

    @Multipart
    @POST("resto/update")
    Call<ResponseBody> updateMyRestoNopict(@Header("Authorization") String token,
                                           @Part("id") int id_resto,
                                           @Part("nama") RequestBody nama,
                                           @Part("lat") RequestBody lat,
                                           @Part("lng") RequestBody lng,
                                           @Part("alamat") RequestBody alamat,
                                           @Part("status") int status);

    @Multipart
    @POST("resto/update")
    Call<ResponseBody> updateMyResto(@Header("Authorization") String token,
                                     @Part MultipartBody.Part gambar_resto,
                                     @Part("id") int id_resto,
                                     @Part("nama") RequestBody nama,
                                     @Part("lat") RequestBody lat,
                                     @Part("lng") RequestBody lng,
                                     @Part("alamat") RequestBody alamat,
                                     @Part("status") int status);

    @FormUrlEncoded
    @POST("resto/delete")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> deleteMyresto(@Header("Authorization") String token,
                                     @Field("id_resto") int id_resto);


    @FormUrlEncoded
    @POST("resto/setstatus")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> setStatusResto(@Header("Authorization") String token,
                                      @Field("id_resto") int id_resto,
                                      @Field("status") int status);

    @FormUrlEncoded
    @POST("menu")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseMyMenu> myMenu(@Header("Authorization") String token,
                                @Field("id_resto") int id_resto);

    @Multipart
    @POST("menu/create")
    Call<ResponseBody> addMyMenu(@Header("Authorization") String token,
                                 @Part MultipartBody.Part gambar_menu,
                                 @Part("id_resto") int id_resto,
                                 @Part("nama_menu") RequestBody nama_menu,
                                 @Part("harga") int harga,
                                 @Part("menu_deskripsi") RequestBody menu_deskripsi,
                                 @Part("status") int status);

    @Multipart
    @POST("menu/update")
    Call<ResponseBody> updateMyMenuNoPic(@Header("Authorization") String token,
                                         @Part("id") int id_menu,
                                         @Part("nama_menu") RequestBody nama_menu,
                                         @Part("harga") int harga,
                                         @Part("menu_deskripsi") RequestBody menu_deskripsi,
                                         @Part("status") int status);

    @Multipart
    @POST("menu/update")
    Call<ResponseBody> updateMyMenu(@Header("Authorization") String token,
                                    @Part MultipartBody.Part gambar_menu,
                                    @Part("id") int id_menu,
                                    @Part("nama_menu") RequestBody nama_menu,
                                    @Part("harga") int harga,
                                    @Part("menu_deskripsi") RequestBody menu_deskripsi,
                                    @Part("status") int status);

    @FormUrlEncoded
    @POST("menu/delete")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> deleteMymenu(@Header("Authorization") String token,
                                    @Field("id_menu") int id_menu);


    @FormUrlEncoded
    @POST("menu/setstatus")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> setStatusMenu(@Header("Authorization") String token,
                                     @Field("id_menu") int id_resto,
                                     @Field("status") int status);

    @FormUrlEncoded
    @POST("review/my")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseUserReview> getUserReview(@Header("Authorization") String token,
                                           @Field("id_user") int id_user);

    @FormUrlEncoded
    @POST("review")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseMyReview> getMyReview(@Header("Authorization") String token,
                                       @Field("id_menu") int id_menu);

    @FormUrlEncoded
    @POST("review/create")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> createReview(@Header("Authorization") String token,
                                    @Field("id_menu") int id_menu,
                                    @Field("id_user") int id_user,
                                    @Field("comment") String comment,
                                    @Field("rating") int rating,
                                    @Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("review/update")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> updateReview(@Header("Authorization") String token,
                                    @Field("id") int id_review,
                                    @Field("comment") String comment,
                                    @Field("rating") int rating,
                                    @Field("tanggal") String tanggal,
                                    @Field("id_menu") int id_menu);

    @FormUrlEncoded
    @POST("review/delete")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> deleteReview(@Header("Authorization") String token,
                                    @Field("id_review") int id_review,
                                    @Field("id_menu") int id_menu);

    @FormUrlEncoded
    @POST("report")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> sendReport(@Header("Authorization") String token,
                                  @Field("id_menu") int id_menu,
                                  @Field("comment") String comment);

    @GET("report/get")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseSuspendMenu> getReport(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("report/getcomment")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseReportedComment> getReportedCommment(@Header("Authorization") String token,
                                                      @Field("id_menu") int id_menu);

    @FormUrlEncoded
    @POST("report/suspend")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> suspendMenu(@Header("Authorization") String token,
                                   @Field("id_menu") int id_menu,
                                   @Field("suspend") int suspend);

    @FormUrlEncoded
    @POST("menu/recom")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseRecom> recom(@Header("Authorization") String token,
                              @Field("lat") double lat,
                              @Field("lng") double lng,
                              @Field("req_jarak") String req_jarak,
                              @Field("req_nama") String req_nama,
                              @Field("row") String row,
                              @Field("harga") String harga);

    @FormUrlEncoded
    @POST("promo/single")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseMyPromo> singlePromo(@Header("Authorization") String token,
                                      @Field("id_menu") int id_menu);

    @Multipart
    @POST("promo/update")
    Call<ResponseBody> updateMyPromoNoPic(@Header("Authorization") String token,
                                          @Part("id") int id_promo,
                                          @Part("keterangan") RequestBody nama_menu,
                                          @Part("harga") int harga,
                                          @Part("mulai") RequestBody tanggal_mulai,
                                          @Part("batas") RequestBody tanggal_batas);

    @Multipart
    @POST("promo/update")
    Call<ResponseBody> updateMyPromo(@Header("Authorization") String token,
                                     @Part MultipartBody.Part gambar_promo,
                                     @Part("id") int id_promo,
                                     @Part("keterangan") RequestBody nama_menu,
                                     @Part("harga") int harga,
                                     @Part("mulai") RequestBody tanggal_mulai,
                                     @Part("batas") RequestBody tanggal_batas);

    @Multipart
    @POST("promo/create")
    Call<ResponseBody> createMyPromo(@Header("Authorization") String token,
                                    @Part MultipartBody.Part gambar_promo,
                                    @Part("id_menu") int id_menu,
                                    @Part("keterangan") RequestBody keterangan,
                                    @Part("harga") int harga,
                                    @Part("mulai") RequestBody tanggal_mulai,
                                    @Part("batas") RequestBody tanggal_batas);

    @FormUrlEncoded
    @POST("promo/delete")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponseBody> deletePromo(@Header("Authorization") String token,
                                                      @Field("id_promo") int id_promo);

    @FormUrlEncoded
    @POST("promo")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Call<ResponsePromo> getAllPromo(@Header("Authorization") String token,
                                    @Field("lat") double lat,
                                    @Field("lng") double lng,
                                    @Field("req_jarak") String req_jarak,
                                    @Field("req_nama") String req_nama,
                                    @Field("row") String row,
                                    @Field("harga") String harga);

}

