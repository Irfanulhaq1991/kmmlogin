package com.irfan.kmmlogin

import com.varabyte.truthish.assertThat
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class CommonGreetingTest {


  @BeforeTest
  fun setup(){
      MockKAnnotations.init(this, relaxUnitFun = true)
  }
    @AfterTest
    fun tear(){
        unmockkAll()
    }

    @Test
    fun testExample() {
        assertThat("").isEmpty()
    }

}