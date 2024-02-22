package com.irfan.kmmlogin

class UsrRmtDtaSrc(private val  api: UsrApi) {
    fun authntcat():Boolean {
       return api.authntcat()
    }

}
