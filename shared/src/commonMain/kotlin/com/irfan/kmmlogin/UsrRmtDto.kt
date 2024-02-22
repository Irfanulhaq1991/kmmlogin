package com.irfan.kmmlogin

data class UsrRmtDto(val name:String,val id:Int)

data class UserRmtRspnseDto(val usrRmtDto: UsrRmtDto?,val statusCode:Int )
