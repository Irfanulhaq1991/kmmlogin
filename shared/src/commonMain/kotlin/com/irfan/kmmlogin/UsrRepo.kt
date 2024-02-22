package com.irfan.kmmlogin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UsrRepo(private val usrRmtDtaSrc: UsrRmtDtaSrc) {
   suspend fun authntict(userName:String,password:String):Result<User> = withContext(Dispatchers.IO){
        usrRmtDtaSrc.authntcat(userName,password)
            .fold({
                Result.success(it.toUser())
            },{
                Result.failure(it)
            })
    }
    
}
