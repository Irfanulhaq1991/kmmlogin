package com.irfan.kmmlogin

class UsrRepo(private val usrRmtDtaSrc: UsrRmtDtaSrc) {
    fun authntict(userName:String,password:String):Result<User> {
        return usrRmtDtaSrc.authntcat(userName,password)
            .fold({
                Result.success(it.toUser())
            },{
                Result.failure(it)
            })
    }
    
}
