package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.LoginRepository
import com.irfan.kmmlogin.RemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoginRepositoryShould {

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var loginRepository: LoginRepository

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        loginRepository = LoginRepository(remoteDataSource)
    }

    @AfterTest
    fun dearDown() {
        unmockkAll()
    }

    @Test
    fun callRemoteDataSource(){
        loginRepository.doLogin()
        verify { remoteDataSource.executeRemoteCall() }
    }
}