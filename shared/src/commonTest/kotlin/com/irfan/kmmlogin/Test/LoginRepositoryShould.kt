package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.UserRepo
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
    private lateinit var userRepo: UserRepo

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        userRepo = UserRepo(remoteDataSource)
    }

    @AfterTest
    fun dearDown() {
        unmockkAll()
    }

    @Test
    fun callRemoteDataSource(){
        userRepo.doLogin()
        verify { remoteDataSource.executeRemoteCall() }
    }
}