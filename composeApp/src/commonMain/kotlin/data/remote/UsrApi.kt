package data.remote


interface UsrApi {
   suspend fun authntcat(username:String,password:String): UserRemoteRspnseDto
}
