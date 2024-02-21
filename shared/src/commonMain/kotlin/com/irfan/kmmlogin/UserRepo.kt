package com.irfan.kmmlogin

class UserRepo(private val remoteDataSource: RemoteDataSource) {
    fun doLogin() {
        remoteDataSource.executeRemoteCall()
    }
    
}
