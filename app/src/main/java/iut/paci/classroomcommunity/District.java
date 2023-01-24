package iut.paci.classroomcommunity;

public class District {
    private String name;
    private int idDistrict;
    private String img;

    public District(String name, int idDistrict) {
        this.name = name;
        this.idDistrict = idDistrict;
        this.img="img_district"+idDistrict;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(int idDistrict) {
        this.idDistrict = idDistrict;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString()  {
        return name;
    }
}
