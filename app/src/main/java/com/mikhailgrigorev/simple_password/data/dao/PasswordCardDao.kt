package com.mikhailgrigorev.simple_password.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mikhailgrigorev.simple_password.data.dbo.PasswordCard

@Dao
interface PasswordCardDao {

    @Insert
    fun insert(card: PasswordCard)

    @Query("select * from password_card where _id = :id")
    fun getByID(id: Int): LiveData<PasswordCard>

    @Query(
            """SELECT * FROM password_card WHERE name LIKE '%' || :search || '%' OR tags LIKE '%' || :search || '%' ORDER BY favorite DESC,
                    CASE WHEN :isAsc = 1 THEN name END ASC,
                    CASE WHEN :isAsc = 0 THEN name END DESC"""
    )
    fun getByNameSortName(
        search: String,
        isAsc: Boolean = false
    ): LiveData<List<PasswordCard>>

    @Query(
            """SELECT * FROM password_card WHERE name LIKE :search OR tags LIKE :search  ORDER BY favorite DESC,
                    CASE WHEN :isAsc = 1 THEN time END ASC,
                    CASE WHEN :isAsc = 0 THEN time END DESC"""
    )
    fun getByNameSortTime(
        search: String,
        isAsc: Boolean = false
    ): LiveData<List<PasswordCard>>

    @Query(
            """SELECT * FROM password_card WHERE quality = :value ORDER BY favorite DESC,
                    CASE WHEN :isAsc = 1 THEN name END ASC,
                    CASE WHEN :isAsc = 0 THEN name END DESC"""
    )
    fun getByQualitySortName(
        value: Int,
        isAsc: Boolean = false
    ): LiveData<List<PasswordCard>>

    @Query(
            """SELECT * FROM password_card WHERE quality = :value ORDER BY favorite DESC, 
                    CASE WHEN :isAsc = 1 THEN name END ASC,
                    CASE WHEN :isAsc = 0 THEN name END DESC"""
    )
    fun getByQualitySortTime(
        value: Int,
        isAsc: Boolean = false
    ): LiveData<List<PasswordCard>>

    @Query("SELECT * FROM password_card WHERE favorite = 1")
    fun getFavorite(): LiveData<List<PasswordCard>>

    @Query(
            """select * from password_card ORDER BY favorite DESC,
                    CASE WHEN :isAsc = 1 THEN name END ASC, 
                    CASE WHEN :isAsc = 0 THEN name END DESC"""
    )
    fun getAllSortName(
        isAsc: Boolean = false
    ): LiveData<List<PasswordCard>>

    @Query(
            """select * from password_card ORDER BY favorite DESC,
                    CASE WHEN :isAsc = 1 THEN time END ASC, 
                    CASE WHEN :isAsc = 0 THEN time END DESC"""
    )
    fun getAllSortTime(
        isAsc: Boolean = false
    ): LiveData<List<PasswordCard>>

    @Query(
            "select * from password_card WHERE folder = :folder_"
    )
    fun getAllFromFolder(
        folder_: Int
    ): LiveData<List<PasswordCard>>

    // Getting additional data

    @Query("SELECT COUNT(*) FROM password_card ")
    fun getItemsNumber(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM password_card WHERE use_2fa = 1")
    fun getItemsNumberWith2fa(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM password_card WHERE encrypted = 1")
    fun getItemsNumberWithEncrypted(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM password_card WHERE use_time = 1")
    fun getItemsNumberWithTimeLimit(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM password_card WHERE is_card_pin = 1")
    fun getPinItems(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM password_card WHERE quality = :value")
    fun getItemsNumberWithQuality(value: Int): LiveData<Int>


    // Operations with password

    @Update
    suspend fun update(card: PasswordCard)

    @Delete
    fun delete(card: PasswordCard)

    @Query("DELETE FROM password_card")
    suspend fun deleteAll()
}