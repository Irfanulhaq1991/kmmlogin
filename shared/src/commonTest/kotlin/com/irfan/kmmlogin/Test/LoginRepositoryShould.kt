package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.UsrRepo
import com.irfan.kmmlogin.usrRmtDtaSrc
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoginRepositoryShould {

    @MockK
    private lateinit var usrRmtDtaSrc: usrRmtDtaSrc
    private lateinit var usrRepo: UsrRepo

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        usrRepo = UsrRepo(usrRmtDtaSrc)
    }

    @AfterTest
    fun dearDown() {
        unmockkAll()
    }

    @Test
    fun callRemoteDataSource(){
        usrRepo.authntict()
        verify { usrRmtDtaSrc.authntcat() }
    }
}