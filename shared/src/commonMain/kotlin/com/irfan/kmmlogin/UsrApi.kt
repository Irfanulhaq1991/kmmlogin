package com.irfan.kmmlogin

interface UsrApi {
   suspend fun authntcat(username:String,password:String):UserRmtRspnseDto
}
