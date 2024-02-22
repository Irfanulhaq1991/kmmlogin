package com.irfan.kmmlogin

class UsrRmtDtaSrc(private val api: UsrApi) {
    fun authntcat(): Result<UsrRmtDto> {
        try {
            val response = api.authntcat()
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
