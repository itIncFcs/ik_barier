/**
 * Created by Вадим on 10.03.2016.
 */
public class Config {
    private int distance_factor;
    private int camera_id;
    private String com_port_name;
    private String db_txt_name;

    public void setDistance_factor(int distance_factor){
        this.distance_factor = distance_factor;
    }

    public void setCamera_id(int camera_id){
        this.camera_id = camera_id;
    }

    public void setCom_port_name(String com_port_name){
        this.com_port_name = com_port_name;
    }

    public void setDb_txt_name(String db_txt_name){
        this.db_txt_name = db_txt_name;
    }

    public int getDistance_factor(){
        return this.distance_factor;
    }

    public int getCamera_id(){
        return this.camera_id;
    }

    public String getCom_port_name(){
        return this.com_port_name;
    }

    public String getDb_txt_name(){
        return this.db_txt_name;
    }

}
