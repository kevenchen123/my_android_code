package com.keven.retrofit.model;

import java.util.List;

public class POIbean {
    /**
     * status : 1
     * count : 48
     * info : OK
     * infocode : 10000
     * pois : [{"id":"B0FFGFVN0C","parent":"B0FFF5C7P2","childtype":"308","name":"虹桥绿谷广场G幢","type":"商务住宅;楼宇;商住两用楼宇","typecode":"120203","address":"甬虹路与申贵路交叉口南100米","location":"121.316925,31.189559","tel":"021-64398555","pcode":"310000","pname":"上海市","citycode":"021","cityname":"上海市","adcode":"310112","adname":"闵行区","gridcode":"4621622521","distance":"36","navi_poiid":"H51F010011_334973","entr_location":"121.316926,31.189799","photos":[{"url":"http://store.is.autonavi.com/showpic/567b88d7a3104d32a8242531"}]}]
     */

    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<Pois> pois;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<Pois> getPois() {
        return pois;
    }

    public void setPois(List<Pois> pois) {
        this.pois = pois;
    }

    public static class Pois {
        /**
         * id : B0FFGFVN0C
         * parent : B0FFF5C7P2
         * childtype : 308
         * name : 虹桥绿谷广场G幢
         * type : 商务住宅;楼宇;商住两用楼宇
         * typecode : 120203
         * address : 甬虹路与申贵路交叉口南100米
         * location : 121.316925,31.189559
         * tel : 021-64398555
         * pcode : 310000
         * pname : 上海市
         * citycode : 021
         * cityname : 上海市
         * adcode : 310112
         * adname : 闵行区
         * gridcode : 4621622521
         * distance : 36
         * navi_poiid : H51F010011_334973
         * entr_location : 121.316926,31.189799
         * photos : [{"url":"http://store.is.autonavi.com/showpic/567b88d7a3104d32a8242531"}]
         */

        private String id;
        private String parent;
        private String childtype;
        private String name;
        private String type;
        private String typecode;
        private String address;
        private String location;
        private String tel;
        private String pcode;
        private String pname;
        private String citycode;
        private String cityname;
        private String adcode;
        private String adname;
        private String gridcode;
        private String distance;
        private String navi_poiid;
        private String entr_location;
        private List<Photos> photos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getChildtype() {
            return childtype;
        }

        public void setChildtype(String childtype) {
            this.childtype = childtype;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypecode() {
            return typecode;
        }

        public void setTypecode(String typecode) {
            this.typecode = typecode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getPcode() {
            return pcode;
        }

        public void setPcode(String pcode) {
            this.pcode = pcode;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getAdname() {
            return adname;
        }

        public void setAdname(String adname) {
            this.adname = adname;
        }

        public String getGridcode() {
            return gridcode;
        }

        public void setGridcode(String gridcode) {
            this.gridcode = gridcode;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getNavi_poiid() {
            return navi_poiid;
        }

        public void setNavi_poiid(String navi_poiid) {
            this.navi_poiid = navi_poiid;
        }

        public String getEntr_location() {
            return entr_location;
        }

        public void setEntr_location(String entr_location) {
            this.entr_location = entr_location;
        }

        public List<Photos> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photos> photos) {
            this.photos = photos;
        }

        public static class Photos {
            /**
             * url : http://store.is.autonavi.com/showpic/567b88d7a3104d32a8242531
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        @Override
        public String toString() {
            return "Pois{" +
                    "id='" + id + '\'' +
                    ", parent='" + parent + '\'' +
                    ", childtype='" + childtype + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", typecode='" + typecode + '\'' +
                    ", address='" + address + '\'' +
                    ", location='" + location + '\'' +
                    ", tel='" + tel + '\'' +
                    ", pcode='" + pcode + '\'' +
                    ", pname='" + pname + '\'' +
                    ", citycode='" + citycode + '\'' +
                    ", cityname='" + cityname + '\'' +
                    ", adcode='" + adcode + '\'' +
                    ", adname='" + adname + '\'' +
                    ", gridcode='" + gridcode + '\'' +
                    ", distance='" + distance + '\'' +
                    ", navi_poiid='" + navi_poiid + '\'' +
                    ", entr_location='" + entr_location + '\'' +
                    ", photos=" + photos +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "POIbean{" +
                "status='" + status + '\'' +
                ", count='" + count + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", pois=" + pois +
                '}';
    }
}
