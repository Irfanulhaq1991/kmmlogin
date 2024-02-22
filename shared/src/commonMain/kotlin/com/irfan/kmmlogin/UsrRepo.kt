package com.irfan.kmmlogin

class UsrRepo(private val usrRmtDtaSrc: UsrRmtDtaSrc) {
    fun authntict(userName:String,password:String) {
        usrRmtDtaSrc.authntcat(userName,password)
    }
    
}
