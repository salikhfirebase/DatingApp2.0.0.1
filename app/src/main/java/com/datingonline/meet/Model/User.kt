package com.datingonline.meet.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "userData")
class User (

    @PrimaryKey(autoGenerate = true) private var id: Int? = null,
    @ColumnInfo(name = "email") private var email: String = "",
    @ColumnInfo(name = "password") private var password: String = "",
    @ColumnInfo(name = "nick") private var nick: String ="",
    @ColumnInfo(name =  "birth") private var birth: String = "",
    @ColumnInfo(name = "height") private var height: String = "",
    @ColumnInfo(name = "weight") private var weight: String = "",
    @ColumnInfo(name = "race") private var race: String = "",
    @ColumnInfo(name = "status") private var status: String = "",
    @ColumnInfo(name = "other_data") private var otherData: String = "",
    @ColumnInfo(name = "make_clear") private var makeClear: String = "",
    @ColumnInfo(name = "agreement") private var agreement: String = "false",
    @ColumnInfo(name = "picture") private var userPic: String = "",
    @ColumnInfo(name = "lookFor") private var lookFor: String = ""

) {

    fun getId(): Int? {
        return id
    }

    fun getEmail(): String {
        return email
    }

    fun getPassword(): String {
        return password
    }

    fun getNick(): String {
        return nick
    }

    fun getBirth(): String {
        return birth
    }

    fun getHeight(): String {
        return height
    }

    fun getWeight(): String {
        return weight
    }

    fun getRace(): String {
        return race
    }

    fun getStatus(): String {
        return status
    }

    fun getOtherData(): String {
        return otherData
    }

    fun getMakeClear(): String {
        return makeClear
    }

    fun getAgreement(): String {
        return agreement
    }

    fun getUserPic(): String {
        return userPic
    }

    fun getLookFor(): String {
        return lookFor
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setNick(nick: String) {
        this.nick = nick
    }

    fun setBirth(birth: String) {
        this.birth = birth
    }

    fun setHeight(height: String) {
        this.height = height
    }

    fun setWeight(weight: String) {
        this.weight = weight
    }

    fun setRace(race: String) {
        this.race = race
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun setOtherData(other_data: String) {
        this.otherData = other_data
    }

    fun setMakeClear(make_clear: String) {
        this.makeClear = make_clear
    }

    fun setAgreement(agreement: String) {
        this.agreement = agreement
    }

    fun setUserPic(userPic: String) {
        this.userPic = userPic
    }

    fun setLookFor(lookFor: String) {
        this.lookFor = lookFor
    }

}