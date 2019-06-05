package com.dreams.best.db

import androidx.room.*
import com.dreams.best.Model.User

@Dao
interface UserDao {

    @Insert
    fun insert(user:User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT MAX(id) from userData")
    fun getLastId(): Int

    @Query("UPDATE userData SET nick = :nick WHERE id = :id")
    fun updateNick(nick: String, id: Int)

    @Query("UPDATE userData SET birth = :birth WHERE id = :id")
    fun updateBirth(birth: String, id: Int)

    @Query("UPDATE userData SET height = :height WHERE id = :id")
    fun updateHeight(height: String, id: Int)

    @Query("UPDATE userData SET weight = :weight WHERE id = :id")
    fun updateWeight(weight: String, id: Int)

    @Query("UPDATE userData SET race = :race WHERE id = :id")
    fun updateRace(race: String, id: Int)

    @Query("UPDATE userData SET status = :status WHERE id = :id")
    fun updateStatus(status: String, id: Int)

    @Query("UPDATE userData SET other_data = :other_data WHERE id = :id")
    fun updateotherData(other_data: String, id: Int)

    @Query("UPDATE userData SET make_clear = :make_clear WHERE id = :id")
    fun updateMakeClear(make_clear: String, id: Int)

    @Query("UPDATE userData SET agreement = :agreement WHERE id = :id")
    fun updateAgreement(agreement: String, id: Int)

    @Query("UPDATE userData SET picture = :userPic WHERE id = :id ")
    fun updateUserPic(userPic:String, id:Int)

    @Query("UPDATE userData SET lookFor = :lookFor WHERE id= :id")
    fun updateLookFor(lookFor: String, id:Int)

    @Query("SELECT COUNT(*) from userData WHERE email = :email ")
    fun isEmailInDb(email: String): Int

    @Query("SELECT * FROM userData WHERE email = :email")
    fun getUserFromDb(email: String): User

    @Query("SELECT * FROM userData WHERE id = :id")
    fun getUserFromDbById(id: Int): User

    @Query("SELECT COUNT(*) from userData WHERE email = :email AND password = :password")
    fun verifaicate(email: String, password: String): Int
}