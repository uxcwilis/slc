package topapp.id.app.smartlivingcost.Util;

/**
 * Created by toha on 10/04/18.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import topapp.id.app.smartlivingcost.Activity.Intro;
import topapp.id.app.smartlivingcost.Activity.LoginActivity;
import topapp.id.app.smartlivingcost.Activity.MainActivity;


public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int mode = 0;

    private static final String pref_name = "crudpref";
    private static final String is_login = "islogin";
    private static final String name = "name";
    private static final String email = "email";
    private static final String uang_bulanan = "uang_bulanan";
    private static final String uang_makan = "uang_makan";
    private static final String password = "password";
    private static final String lat = "lat";
    private static final String lng = "lng";
    private static final String id_kat_recom = "id_kat_recom";
    private static final String nama_kat_recom = "nama_kat_recom";
    private static final String id_icon_kat_recom = "id_icon_kat_recom";
    private static final String token = "token";
    private static final String id = "id";
    private static final String req_jarak = "req_jarak";
    private static final String req_nama = "req_nama";
    private static final String req_row = "req_row";
    private static final String req_harga = "req_harga";
    private static final String notifikasi = "notifikasi";
    private static final String isAdmin = "isAdmin";
    private static final String openMainActivity = "openMainActivity";
    private static final String openDashboardFrag = "openDashboardFrag";
    private static final String openTransaksiFrag = "openTransaksiFrag";
    private static final String openInputFrag = "openInputFrag";
    private static final String openMakanFrag = "openMakanFrag";
    private static final String openInfoFrag = "openInfoFrag";
    private static final String openCatInFrag = "openCatInFrag";
    private static final String openCatOutFrag = "openCatOutFrag";
    private static final String openPlanningActivity = "openPlanningActivity";
    private static final String openAngsuranActivity = "openAngsuranActivity";
    private static final String openRecomSettingActivity = "openRecomSettingActivity";
    private static final String openMyRestoActivity = "openMyRestoActivity";
    private static final String openMyMenuActivity = "openMyMenuActivity";
    private static final String openFrontRestoActivity = "openFrontRestoActivity";
    private static final String openFrontMenuActivity = "openFrontMenuActivity";
    private static final String openMyMenuFrag = "openMyMenuFrag";
    private static final String openReviewFrag = "openReviewFrag";
//    public  final String SP_SUDAH_LOGIN = "spSudahLogin";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(pref_name, mode);
        editor = pref.edit();
    }

    public void createSession(String name, String email, String id, String token, String password, String isAdmin) {
        editor.putBoolean(is_login, true);
        editor.putString(SessionManager.name, name);
        editor.putString(SessionManager.email, email);
        editor.putString(SessionManager.id, id);
        editor.putString(SessionManager.token, token);
        editor.putString(SessionManager.password, password);
        editor.putString(SessionManager.req_nama, "");
        editor.putString(SessionManager.id_kat_recom, "1");
        editor.putString(SessionManager.nama_kat_recom, "Makan Minum");
        editor.putString(SessionManager.id_icon_kat_recom, "454");
        editor.putString(SessionManager.isAdmin, isAdmin);
        editor.putString(SessionManager.openMainActivity, "0");
        editor.putString(SessionManager.openDashboardFrag, "0");
        editor.putString(SessionManager.openTransaksiFrag, "0");
        editor.putString(SessionManager.openInputFrag, "0");
        editor.putString(SessionManager.openMakanFrag, "0");
        editor.putString(SessionManager.openInfoFrag, "0");
        editor.putString(SessionManager.openCatInFrag, "0");
        editor.putString(SessionManager.openCatOutFrag, "0");
        editor.putString(SessionManager.openPlanningActivity, "0");
        editor.putString(SessionManager.openAngsuranActivity, "0");
        editor.putString(SessionManager.openRecomSettingActivity, "0");
        editor.putString(SessionManager.openMyRestoActivity, "0");
        editor.putString(SessionManager.openMyMenuActivity, "0");
        editor.putString(SessionManager.openFrontRestoActivity, "0");
        editor.putString(SessionManager.openFrontMenuActivity, "0");
        editor.putString(SessionManager.openMyMenuFrag, "0");
        editor.putString(SessionManager.openReviewFrag, "0");

        editor.commit();
    }


    public String getOpenMainActivity() {
        return pref.getString(openMainActivity, "");
    }

    public void setOpenMainActivity(String id) {
        editor.putString(SessionManager.openMainActivity, id);
        editor.commit();
    }

    public String getOpenDashboardFrag() {
        return pref.getString(openDashboardFrag, "");
    }

    public void setOpenDashboardFrag(String id) {
        editor.putString(SessionManager.openDashboardFrag, id);
        editor.commit();
    }

    public String getOpenTransaksiFrag() {
        return pref.getString(openTransaksiFrag, "");
    }

    public void setOpenTransaksiFrag(String id) {
        editor.putString(SessionManager.openTransaksiFrag, id);
        editor.commit();
    }

    public String getOpenInputFrag() {
        return pref.getString(openInputFrag, "");
    }

    public void setOpenInputFrag(String id) {
        editor.putString(SessionManager.openInputFrag, id);
        editor.commit();
    }

    public String getOpenMakanFrag() {
        return pref.getString(openMakanFrag, "");
    }

    public void setOpenMakanFrag(String id) {
        editor.putString(SessionManager.openMakanFrag, id);
        editor.commit();
    }

    public String getOpenInfoFrag() {
        return pref.getString(openInfoFrag, "");
    }

    public void setOpenInfoFrag(String id) {
        editor.putString(SessionManager.openInfoFrag, id);
        editor.commit();
    }

    public String getOpenCatInFrag() {
        return pref.getString(openCatInFrag, "");
    }

    public void setOpenCatInFrag(String id) {
        editor.putString(SessionManager.openCatInFrag, id);
        editor.commit();
    }

    public String getOpenCatOutFrag() {
        return pref.getString(openCatOutFrag, "");
    }

    public void setOpenCatOutFrag(String id) {
        editor.putString(SessionManager.openCatOutFrag, id);
        editor.commit();
    }

    public String getOpenPlanningActivity() {
        return pref.getString(openPlanningActivity, "");
    }

    public void setOpenPlanningActivity(String id) {
        editor.putString(SessionManager.openPlanningActivity, id);
        editor.commit();
    }

    public String getOpenAngsuranActivity() {
        return pref.getString(openAngsuranActivity, "");
    }

    public void setOpenAngsuranActivity(String id) {
        editor.putString(SessionManager.openAngsuranActivity, id);
        editor.commit();
    }

    public String getOpenRecomSettingActivity() {
        return pref.getString(openRecomSettingActivity, "");
    }

    public void setOpenRecomSettingActivity(String id) {
        editor.putString(SessionManager.openRecomSettingActivity, id);
        editor.commit();
    }

    public String getOpenMyRestoActivity() {
        return pref.getString(openMyRestoActivity, "");
    }

    public void setOpenMyRestoActivity(String id) {
        editor.putString(SessionManager.openMyRestoActivity, id);
        editor.commit();
    }

    public String getOpenMyMenuActivity() {
        return pref.getString(openMyMenuActivity, "");
    }

    public void setOpenMyMenuActivity(String id) {
        editor.putString(SessionManager.openMyMenuActivity, id);
        editor.commit();
    }

    public String getOpenFrontRestoActivity() {
        return pref.getString(openFrontRestoActivity, "");
    }

    public void setOpenFrontRestoActivity(String id) {
        editor.putString(SessionManager.openFrontRestoActivity, id);
        editor.commit();
    }

    public String getOpenFrontMenuActivity() {
        return pref.getString(openFrontMenuActivity, "");
    }

    public void setOpenFrontMenuActivity(String id) {
        editor.putString(SessionManager.openFrontMenuActivity, id);
        editor.commit();
    }

    public String getOpenMyMenuFrag() {
        return pref.getString(openMyMenuFrag, "");
    }

    public void setOpenMyMenuFrag(String id) {
        editor.putString(SessionManager.openMyMenuFrag, id);
        editor.commit();
    }

    public String getOpenReviewFrag() {
        return pref.getString(openReviewFrag, "");
    }

    public void setOpenReviewFrag(String id) {
        editor.putString(SessionManager.openReviewFrag, id);
        editor.commit();
    }

    public void updateDetail(String name, String email, String password) {
        editor.putString(SessionManager.name, name);
        editor.putString(SessionManager.email, email);
        editor.putString(SessionManager.password, password);
        editor.commit();
    }



    public void setNama_kat_recom(String id) {
        editor.putString(SessionManager.nama_kat_recom, id);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(token, null);
    }

    public String getNama_kat_recom() {
        return pref.getString(nama_kat_recom, null);
    }

    public void setId_icon_kat_recom(String id) {
        editor.putString(SessionManager.id_icon_kat_recom, id);
        editor.commit();
    }

    public String getId_icon_kat_recom() {
        return pref.getString(id_icon_kat_recom, null);
    }

    public void setId_kat_recom(String id) {
        editor.putString(SessionManager.id_kat_recom, id);
        editor.commit();
    }

    public String getId_kat_recom() {
        return pref.getString(id_kat_recom, null);
    }

    public void login() {
        editor.putBoolean(is_login, true);
        editor.commit();
    }

    public void setLat(double lat) {
        editor.putString(SessionManager.lat, String.valueOf(lat));
        editor.commit();
    }

    public void setLng(double lng) {
        editor.putString(SessionManager.lng, String.valueOf(lng));
        editor.commit();
    }

    public void setIsAdmin(String isAdmin) {
        editor.putString(SessionManager.isAdmin, isAdmin);
        editor.commit();
    }

    public String getIsAdmin() {
        return pref.getString(isAdmin, null);
    }

    public String getLat() {
        return pref.getString(lat, null);
    }

    public String getLng() {
        return pref.getString(lng, null);
    }

    public String getName() {
        return pref.getString(name, null);
    }

    public String getEmail() {
        return pref.getString(email, null);
    }

    public String getUang_bulanan() {
        return pref.getString(uang_bulanan, null);
    }

    public String getUang_makan() {
        return pref.getString(uang_makan, null);
    }

    public String getPassword() {
        return pref.getString(password, null);
    }

    public void checkLogin() {
        if (!this.is_register()) {
            Intent i = new Intent(context, Intro.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (!this.is_login()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("frgToLoad", "");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public boolean is_login() {
        return pref.getBoolean(is_login, false);
    }

    public boolean is_register() {
        if (pref.getString(email, null) == null || pref.getString(email, null).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void logout_temp() {
        editor.putBoolean(is_login, false);
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(pref_name, pref.getString(pref_name, null));
        user.put(email, pref.getString(email, null));
        user.put(uang_bulanan, pref.getString(uang_bulanan, null));
        user.put(uang_makan, pref.getString(uang_makan, null));
        user.put(name, pref.getString(name, null));
        user.put(password, pref.getString(password, null));
        return user;
    }

    public String getReq_jarak() {
        return pref.getString(req_jarak, null);
    }

    public void setReq_jarak(String jarak) {
        editor.putString(SessionManager.req_jarak, jarak);
        editor.commit();
    }

    public String getReq_nama() {
        return pref.getString(req_nama, null);
    }

    public void setReq_nama(String nama) {
        editor.putString(SessionManager.req_nama, nama);
        editor.commit();
    }

    public String getReq_row() {
        return pref.getString(req_row, null);
    }

    public void setReq_row(String row) {
        editor.putString(SessionManager.req_row, row);
        editor.commit();
    }

    public String getReq_harga() {
        return pref.getString(req_harga, null);
    }

    public void setReq_harga(String harga) {
        editor.putString(SessionManager.req_harga, harga);
        editor.commit();
    }


    public String getId() {
        return pref.getString(id, null);
    }
}