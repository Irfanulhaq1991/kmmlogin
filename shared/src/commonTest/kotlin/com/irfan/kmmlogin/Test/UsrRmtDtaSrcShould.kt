package com.irfan.kmmlogin.test

import com.irfan.kmmlogin.UsrApi
import com.irfan.kmmlogin.UsrRmtDtaSrc
import com.varabyte.truthish.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.unmockkAll
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

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
        val api = getUserApi()
        assertTrue { UsrRmtDtaSrc(api).authntcat() }
    }


    private fun getUserApi():UsrApi{
        return object : UsrApi{
            override fun authntcat(): Boolean {
                return true;
            }
        }
    }



}