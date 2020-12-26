package com.example.jeonse_alarm;

public class SaleConstruct {

//            "id": "1",
//                    "name": "분당두산위브파빌리온",
//                    "naver_id": "2068440034",
//                    "price": "103000",
//                    "img_link": "",
//                    "naver_link": "https://m.land.naver.com/article/info/2068440034",
//                    "type": "OPST",
//                    "station": "468",
//                    "create_dt": "2020-12-11 11:20:26"

    private String id;
    private String name;
    private String naver_id;
    private String price;
    private String img_link;
    private String naver_link;
    private String type;
    private String station;
    private String create_dt;
    private String recent_sale;


    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getNaverId() {
        return naver_id;
    }
    public String getPrice() {
        return price;
    }
    public String getImgLink() {
        return img_link;
    }
    public String getNaverLink() {
        return naver_link;
    }
    public String getType() {
        return type;
    }
    public String getStation() {
        return station;
    }
    public String getCreateDt() {
        return create_dt;
    }
    public String getRecentSale() {
        return recent_sale;
    }


    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNaverId(String naver_id) {
        this.naver_id = naver_id;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setImgLink(String img_link) {
        this.img_link = img_link;
    }
    public void setNaverLink(String naver_link) {
        this.naver_link = naver_link;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStation(String station) {
        this.station = station;
    }
    public void setCreateDt(String create_dt) {
        this.create_dt = create_dt;
    }
    public void setRecentSale(String recent_sale) {
        this.recent_sale = recent_sale;
    }




}
