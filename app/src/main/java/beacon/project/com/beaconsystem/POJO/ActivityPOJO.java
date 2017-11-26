package beacon.project.com.beaconsystem.POJO;

/**
 * Created by Dell4050 on 11/26/2017.
 */

public class ActivityPOJO {
    private String nameActivity;
    private String path_photo;
    private String datesave;
    public ActivityPOJO(){

    }

    public ActivityPOJO(String nameActivity,String path_photo,String datesave){

        this.nameActivity = nameActivity;
        this.path_photo = path_photo;
        this.datesave = datesave;

    }

    public void setNameActivity(String nameActivity){
        this.nameActivity = nameActivity;
    }

    public void setPath_photo(String path_photo){
        this.path_photo = path_photo;
    }

    public void setDatesave(String datesave){
        this.datesave = datesave;
    }

    public String getNameActivity(){
        return nameActivity;
    }
    public String getPath_photo(){
        return path_photo;
    }
    public String getDatesave(){

        return datesave;
    }
}
