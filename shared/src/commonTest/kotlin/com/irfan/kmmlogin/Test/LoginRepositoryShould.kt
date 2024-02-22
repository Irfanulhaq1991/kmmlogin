package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.User
import com.irfan.kmmlogin.UserRmtRspnseDto
import com.irfan.kmmlogin.UsrRepo
import com.irfan.kmmlogin.UsrRmtDtaSrc
import com.irfan.kmmlogin.UsrRmtDto
import com.varabyte.truthish.assertThat
import io.mockk.impl.annotations.MockK
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every


import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoginRepositoryShould {

    @MockK
    private lateinit var usrRmtDtaSrc: UsrRmtDtaSrc

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
    fun callRemoteDataSource()= runTest{
        coEvery { usrRmtDtaSrc.authntcat(any(),any()) } returns Result.success(UsrRmtDto("",200))
        usrRepo.authntict("###","###")
        coVerify { usrRmtDtaSrc.authntcat(any(),any()) }
    }
    @Test
    fun returnDomainModel() = runTest{
       val usrRmtDto =  UsrRmtDto("",200)
        coEvery { usrRmtDtaSrc.authntcat(any(),any()) } returns Result.success(usrRmtDto)
        val result = usrRepo.authntict("##","###").getOrThrow()
        assertThat(result).isInstanceOf(User::class)
    }

    @Test
    fun returnError() = runTest(){
        coEvery { usrRmtDtaSrc.authntcat(any(),any()) } returns Result.failure<UsrRmtDto>(Throwable("Error"))
        val result = usrRepo.authntict("##","###")
        assertThat(result.isFailure).isTrue()
        result.onFailure {
            assertThat(it.message).isEqualTo("Error")
        }
     }
}