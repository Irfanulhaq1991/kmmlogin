package com.irfan.kmmlogin

class UsrRmtDtaSrc(private val api: UsrApi) {
  suspend  fun authntcat(username:String,password:String): Result<UsrRmtDto> {
        try {
            val response = api.authntcat(username,password)
            if (response.statusCode == 200) {
                return Result.success(response.usrRmtDto!!)
            }else{
                return Result.failure(Throwable("Not valid user"))
            }
        } catch (e: Exception) {
            if (e.message == "Network Error")
                return Result.failure(Throwable("Network Error"))
            else {
                return Result.failure(Throwable("Unknown Error"))
            }
        }


    }

}
