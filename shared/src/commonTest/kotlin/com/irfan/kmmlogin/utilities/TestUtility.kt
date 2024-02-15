package com.irfan.kmmlogin.utilities

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll

object TestUtility {
    fun setup(obj:Any){
        MockKAnnotations.init(obj, relaxUnitFun = true)
    }
    fun tearDown(){
        unmockkAll()
    }
}