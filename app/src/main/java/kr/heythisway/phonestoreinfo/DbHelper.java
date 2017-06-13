package kr.heythisway.phonestoreinfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by SMARTHINK_MBL13 on 2017. 6. 12..
 */

public class DbHelper extends OrmLiteSqliteOpenHelper {
    public static final String DATABASE_NAME = "storeInfo";
    public static final int DATABASE_VERSION = 2;

    // 싱글톤 구현
    private static DbHelper instance;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, StoreInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, StoreInfo.class, true);    //업데이트시 테이블 삭제
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create(StoreInfo storeInfo) {
        //테이블 연결
        try {
            Dao<StoreInfo, Integer> dao = getDao(StoreInfo.class);
            dao.create(storeInfo);
        } catch (SQLException e) {
            Log.e("Create Error", "=========== 데이터 입력에 실패하였습니다.");
        }
    }

    public List<StoreInfo> readAll() {
        List<StoreInfo> datas = null;
        try {
            Dao<StoreInfo, Integer> dao = getDao(StoreInfo.class);
            datas = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }

    public StoreInfo read(int id) {
        StoreInfo storeInfo = null;
        try {
            Dao<StoreInfo, Integer> dao = getDao(StoreInfo.class);
            storeInfo = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeInfo;
    }

    public List<StoreInfo> search(String keyword) {
        List<StoreInfo> datas = null;
        try {
            Dao<StoreInfo, Integer> dao = getDao(StoreInfo.class);
            String query = "select * from storeInfo where like '%" + keyword + "%'";
            GenericRawResults<StoreInfo> temps = dao.queryRaw(query, dao.getRawRowMapper());
            datas = temps.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }

    public void update(StoreInfo storeInfo) {
        try {
            Dao<StoreInfo, Integer> dao = getDao(StoreInfo.class);
            dao.update(storeInfo);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Object
    public void delete(StoreInfo storeInfo){
        try {
            // 1. 테이블에 연결
            Dao<StoreInfo,Integer> dao = getDao(StoreInfo.class);
            // 2. 데이터를 삭제
            dao.delete(storeInfo);
        } catch (SQLException e) {
            Log.e("삭제되지 않았습니다.", "으따?! 아직 삭제 로직이 동작하지 않았써라~");
        }
    }
    // Delete By Id
    public void delete(int id){
        try {
            // 1. 테이블에 연결
            Dao<StoreInfo,Integer> dao = getDao(StoreInfo.class);
            // 2. 데이터를 삭제
            dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
