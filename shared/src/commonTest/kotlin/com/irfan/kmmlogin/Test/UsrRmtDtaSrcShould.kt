package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.UserRmtRspnseDto
import com.irfan.kmmlogin.UsrRmtDto
import com.irfan.kmmlogin.UsrApi
import com.irfan.kmmlogin.UsrRmtDtaSrc
import com.varabyte.truthish.assertThat
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UsrRmtDtaSrcShould {

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @AfterTest
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun returnSuccess() {
        val user = UsrRmtDto("Jams",101)
        val api = getUserApiWith(user,200)
        val result: UsrRmtDto = UsrRmtDtaSrc(api).authntcat("##","##").getOrThrow()
        assertThat(result).isEqualTo(user)
    }
    @Test
    fun returnError(){
        val api = getUserApiWith(null,400)
        UsrRmtDtaSrc(api).authntcat("##","###").onFailure {
            assertThat(it.message == "Not valid user").isTrue()
        }
    }

    @Test
    fun returnNetworkError(){
        val api = getApiWithException()
        UsrRmtDtaSrc(api).authntcat("###","###").onFailure {
            assertThat(it.message == "Network Error").isTrue()
        }
    }



    private fun getApiWithException(): UsrApi{
            return object : UsrApi{
                override fun authntcat(username: String, password: String): UserRmtRspnseDto {
                    return throw Exception("Network Error") // App own exception classes may be created
                }
            }
    }


    private fun getUserApiWith(usrRmtDto: UsrRmtDto?,statusCode:Int):UsrApi{
        return object : UsrApi{
            override fun authntcat(username: String, password: String): UserRmtRspnseDto {
                return UserRmtRspnseDto(usrRmtDto,statusCode)
            }

        }
    }

}