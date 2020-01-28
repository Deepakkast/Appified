package app.appified.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
import static java.lang.Boolean.FALSE;
@Dao
public interface AppDao {

    @Query( "SELECT * FROM  apps")
    List<User> getAll();

    @Query( "DELETE FROM apps")
    void   deleteAll();


    @Insert(onConflict = REPLACE)
    void insert( User userinfo);


    @Query( "SELECT * FROM apps WHERE package_name = :packagename LIMIT 1")
    User findAppByPackageName(String packagename);


    @Query("SELECT * FROM apps WHERE server_sync = :seversync")
    List<User> findAppNotSyncServer(boolean seversync);


    @Query( "DELETE FROM apps WHERE package_name=:packagename")
    void deleteByPackageName(String packagename);


    @Query("UPDATE apps SET is_install = :is_install WHERE package_name = :packagename")
    void updatePackageSyncWithServer(boolean is_install,String packagename);

    @Query("UPDATE apps SET server_sync = :is_server_sync WHERE package_name = :packagename")
    void updateIsSyncServer(String packagename,boolean is_server_sync);

}

