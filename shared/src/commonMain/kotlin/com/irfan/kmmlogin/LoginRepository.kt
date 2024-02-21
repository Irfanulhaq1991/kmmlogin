package com.irfan.kmmlogin

class LoginRepository(private val remoteDataSource: RemoteDataSource) {
    fun doLogin() {
        remoteDataSource.executeRemoteCall()
    }
    
}
