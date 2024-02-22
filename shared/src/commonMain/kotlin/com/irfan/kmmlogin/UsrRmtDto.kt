package com.irfan.kmmlogin

data class UsrRmtDto(val name:String,val id:Int){
    fun toUser():User{
        return User(id,name)
    }
}

data class UserRmtRspnseDto(val usrRmtDto: UsrRmtDto?,val statusCode:Int )
