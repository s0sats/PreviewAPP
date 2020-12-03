package com.namoadigital.prj001.model;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.namoadigital.prj001.service.SV_LocationTracker;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.Properties;
import java.util.Set;

public class NamoaDeviceInfo {

    private Context context;
    private String build_version;
    private String build_version_code;
    private String build_board;
    private String build_bootloader;
    private String build_brand;
    private String build_device;
    private String build_display;
    private String build_fingerprint;
    private String build_hardware;
    private String build_host;
    private String build_id;
    private String build_manufacturer;
    private String build_model;
    private String build_product;
    private String build_serial;
    private String[] build_supported_32_bit_abis;
    private String[] build_supported_64_bit_abis;
    private String[] build_supported_abis;
    private String build_tags;
    private long build_time;
    private String build_type;
    private String build_user;
    private String string_properties = "\n ";

    public NamoaDeviceInfo(Context context) {
        this.context = context;
    }

    public String getBuild_version() {
        return build_version;
    }

    public void setBuild_version(String build_version) {
        this.build_version = build_version;
    }

    public String getBuild_version_code() {
        return build_version_code;
    }

    public void setBuild_version_code(String build_version_code) {
        this.build_version_code = build_version_code;
    }

    public String getBuild_board() {
        return build_board;
    }

    public void setBuild_board(String build_board) {
        this.build_board = build_board;
    }

    public String getBuild_bootloader() {
        return build_bootloader;
    }

    public void setBuild_bootloader(String build_bootloader) {
        this.build_bootloader = build_bootloader;
    }

    public String getBuild_brand() {
        return build_brand;
    }

    public void setBuild_brand(String build_brand) {
        this.build_brand = build_brand;
    }

    public String getBuild_device() {
        return build_device;
    }

    public void setBuild_device(String build_device) {
        this.build_device = build_device;
    }

    public String getBuild_display() {
        return build_display;
    }

    public void setBuild_display(String build_display) {
        this.build_display = build_display;
    }

    public String getBuild_fingerprint() {
        return build_fingerprint;
    }

    public void setBuild_fingerprint(String build_fingerprint) {
        this.build_fingerprint = build_fingerprint;
    }

    public String getBuild_hardware() {
        return build_hardware;
    }

    public void setBuild_hardware(String build_hardware) {
        this.build_hardware = build_hardware;
    }

    public String getBuild_host() {
        return build_host;
    }

    public void setBuild_host(String build_host) {
        this.build_host = build_host;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getBuild_manufacturer() {
        return build_manufacturer;
    }

    public void setBuild_manufacturer(String build_manufacturer) {
        this.build_manufacturer = build_manufacturer;
    }

    public String getBuild_model() {
        return build_model;
    }

    public void setBuild_model(String build_model) {
        this.build_model = build_model;
    }

    public String getBuild_product() {
        return build_product;
    }

    public void setBuild_product(String build_product) {
        this.build_product = build_product;
    }

    public String getBuild_serial() {
        return build_serial;
    }

    public void setBuild_serial(String build_serial) {
        this.build_serial = build_serial;
    }

    public String[] getBuild_supported_32_bit_abis() {
        return build_supported_32_bit_abis;
    }

    public void setBuild_supported_32_bit_abis(String[] build_supported_32_bit_abis) {
        this.build_supported_32_bit_abis = build_supported_32_bit_abis;
    }

    public String[] getBuild_supported_64_bit_abis() {
        return build_supported_64_bit_abis;
    }

    public void setBuild_supported_64_bit_abis(String[] build_supported_64_bit_abis) {
        this.build_supported_64_bit_abis = build_supported_64_bit_abis;
    }

    public String[] getBuild_supported_abis() {
        return build_supported_abis;
    }

    public void setBuild_supported_abis(String[] build_supported_abis) {
        this.build_supported_abis = build_supported_abis;
    }

    public String getBuild_tags() {
        return build_tags;
    }

    public void setBuild_tags(String build_tags) {
        this.build_tags = build_tags;
    }

    public long getBuild_time() {
        return build_time;
    }

    public String getBuild_time_formatted() {
        return ToolBox_Inf.millisecondsToString(
                            build_time,
                "yyyy-MM-dd HH:mm:ss"
                    );
    }

    public void setBuild_time(long build_time) {
        this.build_time = build_time;
    }

    public String getBuild_type() {
        return build_type;
    }

    public void setBuild_type(String build_type) {
        this.build_type = build_type;
    }

    public String getBuild_user() {
        return build_user;
    }

    public void setBuild_user(String build_user) {
        this.build_user = build_user;
    }

    public String getString_properties() {
        return string_properties;
    }

    public void setString_properties(String string_properties) {
        this.string_properties = string_properties;
    }

    public void getInfo() {
        build_version = System.getProperty("os.version");
        build_version_code = Build.VERSION.RELEASE;
        build_board = Build.BOARD;
        build_bootloader = Build.BOOTLOADER;
        build_brand = Build.BRAND;
        build_device = Build.DEVICE;
        build_display = Build.DISPLAY;
        build_fingerprint = Build.FINGERPRINT;
        build_hardware = Build.HARDWARE;
        build_host = Build.HOST;
        build_id = Build.ID;
        build_manufacturer = Build.MANUFACTURER;
        build_model = Build.MODEL;
        build_product = Build.PRODUCT;
        build_serial = Build.SERIAL;
        build_supported_32_bit_abis = Build.SUPPORTED_32_BIT_ABIS;
        build_supported_64_bit_abis = Build.SUPPORTED_64_BIT_ABIS;
        build_supported_abis = Build.SUPPORTED_ABIS;
        build_tags = Build.TAGS;
        build_time = Build.TIME;
        build_type = Build.TYPE;
        build_user = Build.USER;
        //
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = System.getProperties();
        Set<String> propertiesList = properties.stringPropertyNames();
        //
        for (String key : propertiesList) {
            string_properties += key + " - >" + properties.getProperty(key) + "\n ";
        }
    }


    public String getAboutDeviceInfo() {
        return
                "Fabricante:\n\t\t " + build_manufacturer + "\n" +
                "Modelo:\n\t\t " + build_model + "\n" +
                "Versão do Android (API): \n\t\t" + build_version_code + "(" + Build.VERSION.SDK_INT + ")" + "\n" +
                "Versão do Kernel:\n\t\t " +
                        build_version + "\n\t\t " +
                        build_user + "@" +
                        build_host + "\n\t\t " +
                        getBuild_time_formatted()+ "\n" +
                "Numero da Versão:\n\t\t " +
                        build_display;
    }

    public String getHardwareInfo() {
        return getRamInfo() + "\n" +
               getStorageInfo();

    }

    public String getPermissionsGrant() {
        String txt = "Permissoes: \n\t\t";

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String permission_name : info.requestedPermissions) {
                    txt += permission_name + "  =  ";
                    if (info.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.M) {
                        txt += String.valueOf(ContextCompat.checkSelfPermission(context, permission_name) == PackageManager.PERMISSION_GRANTED)
                                + "\n\t\t";
                    } else {
                        txt += String.valueOf(PermissionChecker.checkSelfPermission(context, permission_name) == PackageManager.PERMISSION_GRANTED)
                                + "\n\t\t";
                    }
                }
            } else {
                txt = "Nenhuma permissão solicitada";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txt.replace("android.permission.", "");
    }

    //
    private String getRamInfo() {
        String ramInfo = "RAM:\n\t\t";
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        //Total de Memoria ram
        long totalMemory = memInfo.totalMem;
        //Total disponivel(desconsiderado o reservado pelo sistema)
        long freeMemory = memInfo.availMem;
        //Limite minimo de memorio em que o sys começa matar app e serviços em background
        long thresholdMemory = memInfo.threshold;
        //Verdadeiro se o sistema considerar que está atualmente em uma situação de pouca memória.
        boolean sysConsiderLowMemorry = memInfo.lowMemory;
        //
        ramInfo +=
                "Total: " + ToolBox_Inf.convertBytesToFormattedString(totalMemory, false) + "\n\t\t" +
                "Livre: " + ToolBox_Inf.convertBytesToFormattedString(freeMemory, false) + "\n\t\t" +
                "Limite minimo(Threshold): " + ToolBox_Inf.convertBytesToFormattedString(thresholdMemory, false) + "\n\t\t" +
                "Operando com baixa memoria: " + String.valueOf(sysConsiderLowMemorry);
        return ramInfo;
    }

    private String getStorageInfo() {
        String storageInf = "\nArmazenamento: \n\t\t";
        //
        File internal = new File(context.getFilesDir().getAbsoluteFile().toString());
        //File external = new File(context.getExternalFilesDir(null).toString());
        long totalBytesInternal = internal.getTotalSpace();
        long freeBytesInternal = internal.getFreeSpace();
//        long totalBytesExternal = external.getTotalSpace();
//        long freeBytesExternal = external.getFreeSpace();
        //
        storageInf +=
                "Espaço Interno Total: " + ToolBox_Inf.convertBytesToFormattedString(totalBytesInternal, false) + "\n\t\t" +
                "Espaço Interno Livre: " + ToolBox_Inf.convertBytesToFormattedString(freeBytesInternal, false) + "\n\t\t" +
//                "External Total Space: " + ToolBox_Inf.convertBytesToFormattedString(totalBytesExternal, false) + "\n\t\t" +
//                "External Free Space: " + ToolBox_Inf.convertBytesToFormattedString(freeBytesExternal, false) + "\n\t\t" +
                "Possui cartao SD: " + String.valueOf(hasSdCard())

        ;
        //
        return storageInf;

    }

    private boolean hasSdCard() {
        File[] externals = ContextCompat.getExternalFilesDirs(context, null);
        //Loop abaixo verifica espaço disponivel em todos os "diretorios externos" retornados.
       /*
        String name = "";
        String path = "";
        long freeSpace = 0l;
        String freeSpaceS = "";

        for(File file: externals){
            path = file.getAbsolutePath();
            name = file.getName();
            freeSpace = file.getFreeSpace();
            freeSpaceS = ToolBox_Inf.convertBytesToFormattedString(freeSpace,false);
        }
        */
        //
        if (externals.length > 1 && externals[0] != null && externals[1] != null) {
            return true;
        }
        return false;
    }

    //
    @Override
    public String toString() {
        //return super.toString();

        return "\n build_version -> " + build_version +
                "\n build_version_code -> " + build_version_code +
                "\n build_board -> " + build_board +
                "\n build_bootloader -> " + build_bootloader +
                "\n build_brand -> " + build_brand +
                "\n build_device -> " + build_device +
                "\n build_display -> " + build_display +
                "\n build_fingerprint -> " + build_fingerprint +
                "\n build_hardware -> " + build_hardware +
                "\n build_host -> " + build_host +
                "\n build_id -> " + build_id +
                "\n build_manufacturer -> " + build_manufacturer +
                "\n build_model -> " + build_model +
                "\n build_product -> " + build_product +
                "\n build_serial -> " + build_serial +
                "\n build_supported_32_bit_abis -> " + (build_supported_32_bit_abis.length > 0 ? build_supported_32_bit_abis[0] : "") +
                "\n build_supported_64_bit_abis -> " + (build_supported_64_bit_abis.length > 0 ? build_supported_64_bit_abis[0] : "") +
                "\n build_supported_abis -> " + (build_supported_abis.length > 0 ? build_supported_abis[0] : "") +
                "\n build_tags -> " + build_tags +
                "\n build_time -> " + getBuild_time_formatted() +
                "\n build_type -> " + build_type +
                "\n build_user -> " + build_user +
                "\n\n\n\n" +
                //"\n Properties -> " + System.getProperties();
                "\n Properties -> " + string_properties
                ;
    }

    public String getResourcesInfo() {
        String txt = "Recursos: \n\t\t";

        try {
            txt +=
                    "Status do Servico de Localizacao: " + SV_LocationTracker.status + "\n\t\t" +
                     "Status do Funcao de GPS : " + ToolBox_Con.hasGPSResourceActive(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return txt;
    }

    public String getDeveloperModeInfo() {
        String txt = "Modo desenvolvedor ativo: \n\t\t";

        try {
            txt += Settings.Secure.getInt(context.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) == 1 ? "Sim" : "Não";

        } catch (Exception e) {
            e.printStackTrace();
            txt += "Exception , Impossivel verificar";
        }
        return txt;
    }

    public String getFormattedInfo(){
        String data =   getAboutDeviceInfo() +"\n\n" +
                        getHardwareInfo() +"\n\n" +
                        getDeveloperModeInfo() +"\n\n" +
                        getPermissionsGrant() +"\n\n" +
                        getResourcesInfo();
        return data;
    }

}
