package com.irfan.kmmlogin.utilities

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll

class TestUtility {
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
    fun tearDown(){
        unmockkAll()
    }
}