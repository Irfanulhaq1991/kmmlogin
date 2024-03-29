package data.remote

import domain.model.User

data class UserRemoteDto(val name:String, val id:Int){
    fun toUser(): User {
        return User(id, name)
    }
}

data class UserRemoteRspnseDto(val userRemoteDto: UserRemoteDto?, val statusCode:Int )
