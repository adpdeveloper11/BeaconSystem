package beacon.project.com.beaconsystem.POJO;

/**
 * Created by Dell4050 on 12/6/2017.
 */

public class Activity {

    String activity_name;
    String date_issuer;
    String detail_activity;
    String datetime_start;
    String datetime_end;
    String mac_beacon;

    public Activity(String activity_name, String date_issuer, String detail_activity, String datetime_start, String datetime_end, String mac_beacon) {
        this.activity_name = activity_name;
        this.date_issuer = date_issuer;
        this.detail_activity = detail_activity;
        this.datetime_start = datetime_start;
        this.datetime_end = datetime_end;
        this.mac_beacon = mac_beacon;
    }

    public Activity() {
    }


    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getDate_issuer() {
        return date_issuer;
    }

    public void setDate_issuer(String date_issuer) {
        this.date_issuer = date_issuer;
    }

    public String getDetail_activity() {
        return detail_activity;
    }

    public void setDetail_activity(String detail_activity) {
        this.detail_activity = detail_activity;
    }

    public String getDatetime_start() {
        return datetime_start;
    }

    public void setDatetime_start(String datetime_start) {
        this.datetime_start = datetime_start;
    }

    public String getDatetime_end() {
        return datetime_end;
    }

    public void setDatetime_end(String datetime_end) {
        this.datetime_end = datetime_end;
    }

    public String getMac_beacon() {
        return mac_beacon;
    }

    public void setMac_beacon(String mac_beacon) {
        this.mac_beacon = mac_beacon;
    }
}
