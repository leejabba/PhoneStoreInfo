package kr.heythisway.phonestoreinfo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by SMARTHINK_MBL13 on 2017. 6. 12..
 */

@DatabaseTable(tableName = "storeInfo")
public class StoreInfo {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(uniqueIndexName = "storeCode")
    private String storeCode;
    @DatabaseField
    private String storeName;
    @DatabaseField
    private String address;
    @DatabaseField
    private String teleCompanyName;
    @DatabaseField
    private String tel;
    @DatabaseField
    private String fax;
    @DatabaseField
    private String managerName;
    @DatabaseField
    private Date date;

    public StoreInfo() {
        // ORMLite는 생성자가 없으면 동작하지 않습니다.
        setDate();
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        Date date = new Date(System.currentTimeMillis());
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTeleCompanyName() {
        return teleCompanyName;
    }

    public void setTeleCompanyName(String teleCompanyName) {
        this.teleCompanyName = teleCompanyName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
